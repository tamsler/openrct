/* $Id: CourseContentServerImpl.cc,v 1.8 2003/05/08 20:34:43 thomas Exp $ */

/*
 *
 *   OpenRCT - Open Remote Collaboration Tool
 *
 *   Copyright (c) 2000 by Thomas Amsler
 * 
 *   This file is part of OpenRCT.
 *
 *   OpenRCT is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   OpenRCT is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with OpenRCT; if not, write to the Free Software
 *   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

#include "CourseContentServerImpl.h"


// IDL Method:
void
CourseContentServer_i::get_course_content_from_class_name(const char *a_class_name,
                                                          CourseContentSeq_out a_cc_seq)
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif
    
    ClassServerS *l_class_server = ClassServerS::instance(); 
    DatabaseServerS *l_db_server = DatabaseServerS::instance();
    
    // Create the empty course content sequence
    a_cc_seq = new CourseContentSeq();

    // First get the class id
    CORBA::String_var l_class_id = l_class_server->get_class_id_from_class_name(a_class_name); 

    // This will hold the tuple from the db query
    DBresult* l_res = NULL;
        
    // Compose the query string
    CORBA::String_var l_query;      
    l_query = UTIL::comp_string(SELECT_VISIBLE_CC_FROM_CLASS_ID,
                                (char*)l_class_id,
                                "'",
                                END);

    // Execute the query
    l_res = l_db_server->exec_db_query(l_query);
    
    // Test if we have a valid result
    if(!l_db_server->has_result_no_tuple(l_res)) {
        
        // Creating vars to hold the number of tuples
        CORBA::Long l_tuples = l_db_server->db_ntuples(l_res);

        // Setting the size of the course content sequence
        a_cc_seq->length(l_tuples);
        
        // Now we can access the tuples and populate
        // the course content sequence
        for(unsigned int i = 0; i < l_tuples; i++) {
                
            a_cc_seq[i].id = l_db_server->db_getvalue(l_res, i, RCT_CC_ID);
            a_cc_seq[i].alias = l_db_server->db_getvalue(l_res, i, RCT_CC_ALIAS);
            a_cc_seq[i].name = l_db_server->db_getvalue(l_res, i, RCT_CC_NAME);
            
            // We do not want to reveal the location of
            // the file to the client if it is located on the server
            if(0 == strcmp(a_cc_seq[i].source, CC_SERVER_SOURCE)) {

                a_cc_seq[i].location = (const char *)"SECRET";
            }
            else {
                
                a_cc_seq[i].location = l_db_server->db_getvalue(l_res, i, RCT_CC_LOCATION);
            }
            
            a_cc_seq[i].source = l_db_server->db_getvalue(l_res, i, RCT_CC_SOURCE);
            a_cc_seq[i].mime_type = l_db_server->db_getvalue(l_res, i, RCT_CC_MIME_TYPE);
            a_cc_seq[i].class_id = l_db_server->db_getvalue(l_res, i, RCT_CC_CLASS_ID);
            a_cc_seq[i].visible = l_db_server->db_getvalue(l_res, i, RCT_CC_VISIBLE);
	    a_cc_seq[i].permission = l_db_server->db_getvalue(l_res, i, RCT_CC_PER);
	    a_cc_seq[i].length = l_db_server->db_getvalue(l_res, i, RCT_CC_LENGTH);
            a_cc_seq[i].date = l_db_server->db_getvalue(l_res, i, RCT_CC_DATE);
	    a_cc_seq[i].version = l_db_server->db_getvalue(l_res, i, RCT_CC_VER);
        }
    }

    // Freeing memory
    l_db_server->db_clear(l_res);
}


// IDL Method:
CORBA::Boolean
CourseContentServer_i::fetch(const char *a_cc_id,
                             BinaryFile_out a_file_data) {

    FileServerS *l_file_server = FileServerS::instance();

    
    // Call File Server's fetch method
    if(!l_file_server->fetch(a_cc_id, a_file_data, RCT_FA_COURSE_CONTENT)) {

        cerr << "ERROR: Could not access course content file!" << endl;

        return 0;
    }

    // Everything went ok
    return 1;
}





