/* $Id: FtpServerImpl.cc,v 1.6 2003/05/08 20:34:43 thomas Exp $ */

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

#include "FtpServerImpl.h"

// IDL Methdo:
void
FtpServer_i::upload(const char *a_id,
                    const CORBA::WChar *a_alias,
                    const char *a_name,
                    const char *a_mime_type,
                    const char *a_class_name,
                    const char *a_assembly_name,
                    const char *a_user_id,
                    const char *a_user_alias,
                    const char *a_version,
                    const char *a_length,
                    CORBA::Long a_type,
                    ObjPermission a_permission)
{
      
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    SessionServer_i *l_session_server = SessionServer_i::instance();
    UserServerS *l_user_server = UserServerS::instance();
    DatabaseServerS *l_db_server = DatabaseServerS::instance();
    
    // Once we got the ftp file, we have to send a
    // message to inform the class/assembly members
    // about its existance
    FtpMessage l_ftp_msg;
    l_ftp_msg.base_msg.version = a_version;
    l_ftp_msg.base_msg.user_alias = a_user_alias;
    l_ftp_msg.base_msg.class_name = a_class_name;
    l_ftp_msg.base_msg.permission = a_permission;
    l_ftp_msg.base_msg.type = a_type;
    l_ftp_msg.info.id = a_id;
    l_ftp_msg.info.alias = a_alias;
    l_ftp_msg.info.name = a_name;
    l_ftp_msg.info.mime_type = a_mime_type;
    l_ftp_msg.info.date = l_db_server->get_current_time();
    l_ftp_msg.info.length = a_length;
    
    // Get all the relevant users for team or group 
    UserIdSeq_var l_user_id_seq;
    
    if(FTP_TEAM_UPLOAD == a_type) {

        l_ftp_msg.base_msg.team_name = a_assembly_name;
        l_user_server->get_online_user_ids_from_team_name(a_class_name, a_assembly_name, l_user_id_seq);
    }
    else if(FTP_GROUP_UPLOAD == a_type) {

        l_ftp_msg.base_msg.group_name = a_assembly_name;
        l_user_server->get_user_ids_from_group_name(a_class_name, a_assembly_name, l_user_id_seq);
    }
    else {
        
        cerr << "ERROR: Wrong type!" << endl;
    }

    // Now we send the message to each user event channel
    for (unsigned int i = 0; i < l_user_id_seq->length(); i++) {

        // Note: the [USER_ID_OFFSET] accesses the user_id string's numeric part bypassing the "U" of
        // the user_id, thus if we have U123, we just convert 123 into an integer
        l_session_server->push_ftp_msg(l_ftp_msg, atoi(&l_user_id_seq[i][USER_ID_OFFSET]));
    }
}

// IDL Method:
CORBA::Boolean
FtpServer_i::download(const char *a_ftp_id,
                      BinaryFile_out a_file_data)
{
      
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    FileServerS *l_file_server = FileServerS::instance();
    
    // Call File Server's fetch method
    if(!l_file_server->fetch(a_ftp_id, a_file_data)) {

        cerr << "ERROR: Fetching Ftp File!" << endl;

        return 0;
    }

    // Everything went ok
    return 1;
}


// IDL Method:
void
FtpServer_i::get_ftp_info_from_class_and_assembly_name(const char *a_class_name,
                                                       const char *a_assembly_name,
                                                       CORBA::Long a_assembly_type,
                                                       FtpMsgHistSeq_out a_ftp_seq)
{
        
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DatabaseServerS *l_db_server = DatabaseServerS::instance();
    ClassServerS *l_class_server = ClassServerS::instance();
    TeamServerS *l_team_server = TeamServerS::instance();
    GroupServerS *l_group_server = GroupServerS::instance();

    // Create the empty ftp info sequence
    a_ftp_seq = new FtpMsgHistSeq();

    // Vars for class, team, and group id
    CORBA::String_var l_class_id = l_class_server->get_class_id_from_class_name(a_class_name);
    CORBA::String_var l_assembly_id;
    
    if(PAGE_TEAM == a_assembly_type) {

        l_assembly_id = l_team_server->get_team_id_from_team_name(l_class_id, a_assembly_name);
        
    }
    else if(PAGE_GROUP == a_assembly_type) {

        l_assembly_id = l_group_server->get_group_id_from_group_name(l_class_id, a_assembly_name);
    }
    else {

        cerr << "ERROR: Wrong type!" << endl;
    }

    // Compose the query string
    CORBA::String_var l_query;

    if(PAGE_TEAM == a_assembly_type) {

        l_query = UTIL::comp_string(SELECT_FTP_INFO,
                                    (char *)l_class_id,
                                    "' and file_team_id='",
                                    (char *)l_assembly_id,
                                    "'",
                                    END);

    }
    else if(PAGE_GROUP == a_assembly_type) {

        l_query = UTIL::comp_string(SELECT_FTP_INFO,
                                    (char *)l_class_id,
                                    "' and file_group_id='",
                                    (char *)l_assembly_id,
                                    "'",
                                    END);

    }
    else {

        cerr << "ERROR: Wrong type!" << endl;
    }

    // Execute the query
    DBresult* l_res = NULL;
    l_res = l_db_server->exec_db_query(l_query);

    // Test if we have a valid result
    if(!l_db_server->has_result_no_tuple(l_res)) {

        // Creating vars to hold the number of tuples
        CORBA::Long l_tuples = l_db_server->db_ntuples(l_res);

        // Setting the size of the course content sequence
        a_ftp_seq->length(l_tuples);

        // Now we can access the tuples and populate
        // the ftp info sequence
        for(unsigned int i = 0; i < l_tuples; i++) {

            a_ftp_seq[i].id = l_db_server->db_getvalue(l_res, i, RCT_FV_ID);
            a_ftp_seq[i].alias = l_db_server->db_get_w_value(l_res, i, RCT_FV_ALIAS);
            a_ftp_seq[i].name = l_db_server->db_getvalue(l_res, i, RCT_FV_NAME);
            a_ftp_seq[i].user_alias = l_db_server->db_getvalue(l_res, i, RCT_FV_USER_ALIAS);
            a_ftp_seq[i].mime_type = l_db_server->db_getvalue(l_res, i, RCT_FV_MIME_TYPE);
            a_ftp_seq[i].date = l_db_server->db_getvalue(l_res, i, RCT_FV_DATE);
            a_ftp_seq[i].length = l_db_server->db_getvalue(l_res, i, RCT_FV_LENGTH);
        }
    }

    // Freeing memory
    l_db_server->db_clear(l_res);
}

