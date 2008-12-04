/* $Id: ClassServerImpl.cc,v 1.12 2003/05/08 20:34:43 thomas Exp $ */

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

#include "ClassServerImpl.h"


// IDL Method:
void
ClassServer_i::get_all_classes(ClassSeq_out a_class_seq)
{
	
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DatabaseServerS * l_db_server = DatabaseServerS::instance();
    
    // This will hold the tuple from the db query
    DBresult* l_res = NULL;

    // Create the empty class sequence
     a_class_seq = new ClassSeq();
     
    // Compose the query string
    CORBA::String_var l_query;
    l_query = CORBA::string_dup(SELECT_CLASSES);

    // Execute the query
    l_res = l_db_server->exec_db_query(l_query);

    // Test if we have a valid result
    if(!l_db_server->has_result_no_tuple(l_res)) {

        // Creating vars to hold the number of tuples
        CORBA::Long l_tuples = l_db_server->db_ntuples(l_res);

        // Setting the size of the class sequence
        a_class_seq->length(l_tuples);
        
        // Now we can access the tuples and populate
        // the class sequence
        for(unsigned int i = 0; i < l_tuples; i++) {

            a_class_seq[i].class_id = l_db_server->db_getvalue(l_res, i, RCT_CLASS_ID);
            a_class_seq[i].name = l_db_server->db_getvalue(l_res, i, RCT_CLASS_NAME);
            a_class_seq[i].permission = l_db_server->db_getvalue(l_res, i, RCT_CLASS_PER);
            a_class_seq[i].active_status = l_db_server->db_getvalue(l_res, i, RCT_CLASS_AS);
            a_class_seq[i].date = l_db_server->db_getvalue(l_res, i, RCT_CLASS_DATE);
            a_class_seq[i].manager = l_db_server->db_getvalue(l_res, i, RCT_CLASS_MGR);
        }
    }

    // Freeing memory
    l_db_server->db_clear(l_res);
}


// IDL Method:
void
ClassServer_i::get_classes_from_user_id(const char *a_user_id,
                                        ClassSeq_out a_class_seq)
{
	
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DatabaseServerS * l_db_server = DatabaseServerS::instance();
    
    // This will hold the tuple from the db query
    DBresult* l_res = NULL;

    // Create the empty class sequence
    a_class_seq = new ClassSeq();

    // Compose the query string
    CORBA::String_var l_query;
    l_query = UTIL::comp_string(SELECT_CLASSES_FOR_USER,
                                a_user_id,
                                "')",
                                END);

    // Execute the query
    l_res = l_db_server->exec_db_query(l_query);

    // Test if we have a valid result
    if(!l_db_server->has_result_no_tuple(l_res)) {

        // Creating vars to hold the number of tuples
        CORBA::Long l_tuples = l_db_server->db_ntuples(l_res);

        // Setting the size of the class sequence
        a_class_seq->length(l_tuples);
        
        // Now we can access the tuples and populate
        // the class sequence
        for(unsigned int i = 0; i < l_tuples; i++) {

            a_class_seq[i].class_id = l_db_server->db_getvalue(l_res, i, RCT_CLASS_ID);
            a_class_seq[i].name = l_db_server->db_getvalue(l_res, i, RCT_CLASS_NAME);
            a_class_seq[i].permission = l_db_server->db_getvalue(l_res, i, RCT_CLASS_PER);
            a_class_seq[i].active_status = l_db_server->db_getvalue(l_res, i, RCT_CLASS_AS);
            a_class_seq[i].date = l_db_server->db_getvalue(l_res, i, RCT_CLASS_DATE);
            a_class_seq[i].manager = l_db_server->db_getvalue(l_res, i, RCT_CLASS_MGR);
        }
    }

    // Freeing memory
    l_db_server->db_clear(l_res);
}


// IDL Method:
void
ClassServer_i::get_classes_from_user_ids(const char *a_user_id1,
                                         const char *a_user_id2,
                                         ClassSeq_out a_class_seq)
{
	
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DatabaseServerS * l_db_server = DatabaseServerS::instance();
    
    // This will hold the tuple from the db query
    DBresult* l_res = NULL;

    // Create the empty class sequence
    a_class_seq = new ClassSeq();

    // Compose the query string
    CORBA::String_var l_query;
    l_query = UTIL::comp_string(SELECT_CLASSES_FOR_USERS,
                                a_user_id1,
                                "' and E2.user_id='",
                                a_user_id2,
                                "'",
                                END);

    // Execute the query
    l_res = l_db_server->exec_db_query(l_query);

    // Test if we have a valid result
    if(!l_db_server->has_result_no_tuple(l_res)) {

        // Creating vars to hold the number of tuples
        CORBA::Long l_tuples = l_db_server->db_ntuples(l_res);

        // Setting the size of the class sequence
        a_class_seq->length(l_tuples);
        
        // Now we can access the tuples and populate
        // the class sequence
        for(unsigned int i = 0; i < l_tuples; i++) {

            a_class_seq[i].class_id = l_db_server->db_getvalue(l_res, i, RCT_CLASS_ID);
            a_class_seq[i].name = l_db_server->db_getvalue(l_res, i, RCT_CLASS_NAME);
            a_class_seq[i].permission = l_db_server->db_getvalue(l_res, i, RCT_CLASS_PER);
            a_class_seq[i].active_status = l_db_server->db_getvalue(l_res, i, RCT_CLASS_AS);
            a_class_seq[i].date = l_db_server->db_getvalue(l_res, i, RCT_CLASS_DATE);
            a_class_seq[i].manager = l_db_server->db_getvalue(l_res, i, RCT_CLASS_MGR);
        }
    }

    // Freeing memory
    l_db_server->db_clear(l_res);
}


// Local Method:
char*
ClassServer_i::get_class_id_from_class_name(const char *a_class_name)
{
	
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DatabaseServerS * l_db_server = DatabaseServerS::instance();

    // Make the group name safe fore db entry
    char* l_class_name_esc =
        l_db_server->db_escape_string(a_class_name,
                                      strlen(a_class_name));
    
    CORBA::String_var l_class_id;

    // This will hold the tuple from the db query
    DBresult* l_res = NULL;

    // Compose the query string
    CORBA::String_var l_query;
    l_query = UTIL::comp_string(SELECT_CLASS_ID_FROM_CLASS_NAME,
                                l_class_name_esc,
                                "'",
                                END);

    // Execute the query
    l_res = l_db_server->exec_db_query(l_query);

    // Test if we have a valid result
    if(l_db_server->has_result_one_tuple(l_res)) {

        l_class_id = l_db_server->db_getvalue(l_res, 0, RCT_CLASS_ID);
    }
    else {
        cerr << "ERROR: Did not get class id from class name" << endl;
    }

    // Freeing memory
    l_db_server->db_clear(l_res);

    // Cleaning up escaped name
    delete l_class_name_esc;
    
    return l_class_id._retn();
}



// Local Method:
char*
ClassServer_i::get_class_id_from_team_id(const char *a_team_id)
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DatabaseServerS * l_db_server = DatabaseServerS::instance();
    
    CORBA::String_var l_class_id;

    // This will hold the tuple from the db query
    DBresult* l_res = NULL;

    // Compose the query string
    CORBA::String_var l_query;
    l_query = UTIL::comp_string(SELECT_CLASS_ID_FROM_TEAM_ID,
                                a_team_id,
                                "'",
                                END);

    // Execute the query
    l_res = l_db_server->exec_db_query(l_query);

    // Test if we have a valid result
    if(l_db_server->has_result_one_tuple(l_res)) {

        l_class_id = l_db_server->db_getvalue(l_res, 0, 0);
    }
    else {

        cerr << "ERROR: DB inconsistent - Could not get class_id from team_id!" << endl;
    }

    // Freeing memory
    l_db_server->db_clear(l_res);

    return l_class_id._retn();
}


// Local Method:
char*
ClassServer_i::get_class_id_from_group_id(const char *a_group_id)
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DatabaseServerS * l_db_server = DatabaseServerS::instance();
    
    CORBA::String_var l_class_id;

    // This will hold the tuple from the db query
    DBresult* l_res = NULL;

    // Compose the query string
    CORBA::String_var l_query;
    l_query = UTIL::comp_string(SELECT_CLASS_ID_FROM_GROUP_ID,
                                a_group_id,
                                "'",
                                END);

    // Execute the query
    l_res = l_db_server->exec_db_query(l_query);

    // Test if we have a valid result
    if(l_db_server->has_result_one_tuple(l_res)) {

        l_class_id = l_db_server->db_getvalue(l_res, 0, 0);
    }
    else {

        cerr << "ERROR: DB inconsistent - Could not get class_id from team_id!" << endl;
    }

    // Freeing memory
    l_db_server->db_clear(l_res);
    
    return l_class_id._retn();
}


// Local Method:
Class*
ClassServer_i::get_class_from_class_id(const char *a_class_id)
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DatabaseServerS * l_db_server = DatabaseServerS::instance();
    
    Class_var l_class = new Class();

    // This will hold the tuple from the db query
    DBresult* l_res = NULL;

    // Compose the query string
    CORBA::String_var l_query;
    l_query = UTIL::comp_string(SELECT_CLASS,
                                a_class_id,
                                "'",
                                END);

    // Execute the query
    l_res = l_db_server->exec_db_query(l_query);

    // Test if we have a valid result
    if(l_db_server->has_result_one_tuple(l_res)) {

        l_class->class_id = l_db_server->db_getvalue(l_res, 0, RCT_CLASS_ID);
        l_class->name = l_db_server->db_getvalue(l_res, 0, RCT_CLASS_NAME);
        l_class->permission = l_db_server->db_getvalue(l_res, 0, RCT_CLASS_PER);
        l_class->active_status = l_db_server->db_getvalue(l_res, 0, RCT_CLASS_AS);
        l_class->date = l_db_server->db_getvalue(l_res, 0, RCT_CLASS_DATE);
        l_class->manager = l_db_server->db_getvalue(l_res, 0, RCT_CLASS_MGR);
    }
    else {

        cerr << "ERROR: Could not get class from class_id!" << endl;
    }
    
    // Freeing memory
    l_db_server->db_clear(l_res);

    return l_class._retn();
}






















