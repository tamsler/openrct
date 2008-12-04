/* $Id: AuthenticationServerImpl.cc,v 1.21 2003/05/08 20:34:43 thomas Exp $ */

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

#include "AuthenticationServerImpl.h"


// Local Method:
void
AuthenticationServer_i::set_logoff_time(const char *a_user_id,
                                        const char *a_user_on,
                                        CORBA::Boolean a_status) {

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DatabaseServerS *l_db_server = DatabaseServerS::instance();

    // Compose the trans string
    CORBA::String_var l_trans;

    if(NORMAL == a_status) {

        l_trans = UTIL::comp_string(UPDATE_USER_LOGGING,
                                    a_user_id,
                                    "'",
                                    " and user_on='",
                                    a_user_on,
                                    "'",
                                    END);
    }

    // Execute the update
    l_db_server->exec_db_update(l_trans);
}


// IDL Method:
AuthResponse*
AuthenticationServer_i::authenticate(const char *a_login,
                                     const char *a_password,
                                     CORBA::Long a_auth_level,
                                     const char *a_version)
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DatabaseServerS *l_db_server = DatabaseServerS::instance();
    UserServerS *l_user_server = UserServerS::instance();
    SessionServer_i *l_session_server = SessionServer_i::instance();
    PingServerS *l_ping_server = PingServerS::instance();
    
    CORBA::String_var l_login;
    CORBA::String_var l_password;
    CORBA::String_var l_user_id;
    CORBA::String_var l_online_status;
    CORBA::String_var l_auth_level;
    
    DBresult* l_res = NULL;
    AuthResponse_var l_response;

    // Allocate memory for the auth response
    // The client will deallocate this
    l_response = new AuthResponse();

    // Make the user login / alias safe for db query
    char* l_login_esc =
        l_db_server->db_escape_string(a_login,
                                      strlen(a_login));

    
    // Compose the query string
    CORBA::String_var l_query;
    l_query = UTIL::comp_string(SELECT_USER_ALIAS,
                                l_login_esc,
                                "'",
                                END);

    // Enter critical section
    RctMutex::auth.lock();
    
    // Execute the query
    l_res = l_db_server->exec_db_query(l_query);

    // Testing if we only got one tuple back 
    if(l_db_server->has_result_one_tuple(l_res)) {

        // Accessing login, password and user id in tuple
        l_login = l_db_server->db_getvalue(l_res, 0, RCT_USER_ALIAS);
        l_password = l_db_server->db_getvalue(l_res, 0, RCT_USER_PW);
        l_user_id = l_db_server->db_getvalue(l_res, 0, RCT_USER_ID);
        l_online_status = l_db_server->db_getvalue(l_res, 0, RCT_USER_OS);
        l_auth_level = l_db_server->db_getvalue(l_res, 0, RCT_USER_PER);
        int l_auth_level_num = atoi(l_auth_level);
        
        
        // Testing UserId, Password, and
        // if user is already loged in
        if(0 == strcmp(a_login, l_login) &&
           0 == strcmp(a_password, l_password) &&
           0 == strcmp(OFF, l_online_status) &&
           0 == strcmp(RCT_VERSION, a_version) &&
           AUTH_LEVEL_USER == a_auth_level) {

            // First, we need to create user event channel for the newly logged in
            // user.
            // The second argument, the a_user_index, is derived from the a_user_id
            // The a_user_id is of the from U1, U2, etc., and thus we get the address
            l_session_server->create_event_channel(l_user_id, atoi(&l_user_id[USER_ID_OFFSET]));

            // User authenticated and so we set the ping map value to true
            // and update the database
            l_ping_server->set_map_value(l_user_id, PING_TRUE);
            l_user_server->set_user_online_status(a_login, ONLINE);

            
            l_response->granted = 1;
            l_response->reason = CORBA::string_dup(ID_PW_OK);
        }
        else if(0 == strcmp(a_login, l_login) &&
                0 == strcmp(a_password, l_password) &&
                0 == strcmp(OFF, l_online_status) &&
                0 == strcmp(RCT_VERSION, a_version) &&
                AUTH_LEVEL_ADMIN == a_auth_level &&
                ADMIN_LEVEL == l_auth_level_num) {

            l_user_server->set_user_online_status(a_login, ONLINE);
            l_response->granted = 1;
            l_response->reason = CORBA::string_dup(ID_PW_OK);
        }
        else if(0 == strcmp(a_login, l_login) &&
                0 == strcmp(a_password, l_password) &&
                0 == strcmp(OFF, l_online_status) &&
                0 == strcmp(RCT_VERSION, a_version) &&
                AUTH_LEVEL_ADMIN == a_auth_level &&
                ADMIN_LEVEL != l_auth_level_num) {

            l_response->granted = 0;
            l_response->reason = CORBA::string_dup(NOT_ADMIN);
        }
        else if(0 == strcmp(a_login, l_login) &&
                0 == strcmp(a_password, l_password) &&
                0 == strcmp(OFF, l_online_status) &&
                0 != strcmp(RCT_VERSION, a_version)) {

            l_response->granted = 0;
            l_response->reason = CORBA::string_dup(VERSION_NOT_OK);
        }
        else if(0 == strcmp(a_login, l_login) &&
                0 == strcmp(a_password, l_password) &&
                0 == strcmp(ON, l_online_status)) {

            l_response->granted = 0;
            l_response->reason = CORBA::string_dup(USER_IS_ONLINE);
        }
        else if(0 == strcmp(a_login, l_login) &&
                0 != strcmp(a_password, l_password)) {

            l_response->granted = 0;
            l_response->reason = CORBA::string_dup(PW_NOT_OK);
        }
        else {

            l_response->granted = 0;
            l_response->reason = CORBA::string_dup(AUTH_ERROR);
        }
    }
    else if(l_db_server->has_result_no_tuple(l_res)) {

        l_response->granted = 0;
        l_response->reason = CORBA::string_dup(ID_NOT_OK);
    }
    else {

        // This is the case when the query returned more than one
        // tuple, thus we have duplicated User IDs in the DB
        cerr << "ERROR: Database is inconsistency. User ID returned more then one tuple" << endl;
        
        l_response->granted = 0;
        l_response->reason = CORBA::string_dup("ERROR: Server");
    }

    // Exit cricical section
    RctMutex::auth.unlock();
    
    // Delete db result
    l_db_server->db_clear(l_res);

    // Cleaning up escaped name
    delete l_login_esc;
    
    return l_response._retn();
}


// IDL Method:
char*
AuthenticationServer_i::login(CORBA::Long a_type,
                              const char *a_user_id,
                              const char *a_user_alias,
                              const char *a_first_name,
                              const char *a_last_name,
                              const char *a_data,
                              const char *a_ip,
                              const char *a_os,
                              const char *a_version)
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DatabaseServerS *l_db_server = DatabaseServerS::instance();
    SessionServer_i *l_session_server = SessionServer_i::instance();

    // Make the alias, first, and last name safe for db entry
    char* l_user_alias_esc =
        l_db_server->db_escape_string(a_user_alias,
                                      strlen(a_user_alias));
    char* l_first_name_esc =
        l_db_server->db_escape_string(a_first_name,
                                      strlen(a_first_name));
    char* l_last_name_esc =
        l_db_server->db_escape_string(a_last_name,
                                      strlen(a_last_name));
    
    // User login time
    CORBA::String_var l_login_time;
    
    l_login_time = l_db_server->get_current_time();

    // Log the user login in the DB
    // Compose the query string
    CORBA::String_var l_trans;
    l_trans = UTIL::comp_string(INS_USER_LOGGING,
                                a_user_id,
                                "','",
                                l_user_alias_esc,
                                "','",
                                l_first_name_esc,
                                "','",
                                l_last_name_esc,
                                "','",
                                (char *)l_login_time,  // on
                                "','",
                                "now",  // off
                                "','",
                                a_ip,
                                "','",
                                a_os,
                                "','",
                                a_version,
                                "','",
                                "false"
                                "')",
                                END);

    // Execute the query
    l_db_server->exec_db_insert(l_trans);

    

    // Creating the bc object with client provided values

    BCMessage l_bc_msg;
    l_bc_msg.base_msg.version = a_version;
    l_bc_msg.base_msg.user_alias = a_user_alias;
    l_bc_msg.base_msg.permission = UNCLASSIFIED;
    l_bc_msg.base_msg.type = a_type;
    l_bc_msg.data = a_data;

    // Sending message to the bc channel
    l_session_server->push_bc_msg(l_bc_msg);

    // Cleaning up escaped name
    delete l_user_alias_esc;
    delete l_first_name_esc;
    delete l_last_name_esc;
    
    return l_login_time._retn();
}


// IDL Method:
void
AuthenticationServer_i::logout(CORBA::Long a_type,
                               const char *a_user_alias,
                               const char *a_password,
                               const char *a_msg,
                               const char *a_user_on,
                               const char *a_version,
                               CORBA::Boolean a_status)
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    UserServerS *l_user_server = UserServerS::instance();
    TeamServerS *l_team_server = TeamServerS::instance();
    GroupServerS *l_group_server = GroupServerS::instance();
    ClassServerS * l_class_server = ClassServerS::instance();
    PingServerS *l_ping_server = PingServerS::instance();
    SessionServer_i *l_session_server = SessionServer_i::instance();
    
    // Get user_id from alias
    CORBA::String_var l_user_id = l_user_server->get_id_from_alias(a_user_alias);

    // First we need to check if the logout is ok
    CORBA::String_var l_password = l_user_server->get_password_from_id(l_user_id);

    // If the passwords don't match we don't do anything and return
    if(0 != strcmp(a_password, l_password)) {
        cerr << "SECURITY: #################################" << endl;
        cerr << "SECURITY: Unauthorized User Logout Attempt!" << endl;
        cerr << "SECURITY: Login:    " << a_user_alias << endl;
        cerr << "SECURITY: Passowrd: " << a_password << endl;
        cerr << "SECURITY: #################################" << endl;
        
        return;
    }

    const char *l_user_id_cp = l_user_id;
    
    // Set the logoff time for the user
    set_logoff_time(l_user_id_cp,
                    a_user_on,
                    a_status);
    
    // Start critical section
    RctMutex::auth_msg_buf.lock();

    // Changing the online status for the user in the DB 
    l_user_server->set_user_online_status(a_user_alias, OFFLINE);

    // Setting the ping map value for the user to false
    l_ping_server->set_map_value(l_user_id, PING_FALSE);
    
    // Send exit messages for all teams and groups user was active with
 
    // Get the teams the user is active in first
    TeamSeq_var l_team_seq;
    l_team_server->get_teams_user_is_active_in(l_user_id, l_team_seq);

    for(int i = 0; i < l_team_seq->length(); i++) {

        CORBA::String_var l_class_id;
        l_class_id = l_class_server->get_class_id_from_team_id(l_team_seq[i].team_id);
        
        Class_var l_class;
        l_class = l_class_server->get_class_from_class_id(l_class_id);

        // Now we have all required variables so call exit team
        l_team_server->exit_team(l_class->name,
                                 l_team_seq[i].name,
                                 l_user_id,
                                 a_user_alias);
    }

    // Now get the groups the user was a member of
    GroupSeq_var l_group_seq;
    l_group_server->get_groups_user_is_member_of(l_user_id, l_group_seq);

    for(int i = 0; i < l_group_seq->length(); i++) {

        CORBA::String_var l_class_id;
        l_class_id = l_class_server->get_class_id_from_group_id(l_group_seq[i].group_id);
        
        Class_var l_class;
        l_class = l_class_server->get_class_from_class_id(l_class_id);

        // Now we have all required variables so call exit group
        l_group_server->exit_group(l_class->name,
                                   l_group_seq[i].name,
                                   l_user_id,
                                   a_user_alias,
                                   a_status);

    }

    BCMessage l_bc_msg;
    l_bc_msg.base_msg.version = a_version;
    l_bc_msg.base_msg.user_alias = a_user_alias;
    l_bc_msg.base_msg.permission = UNCLASSIFIED;
    l_bc_msg.base_msg.type = a_type;
    l_bc_msg.data = a_msg;

    // Sending message to the bc channel
    l_session_server->push_bc_msg(l_bc_msg);
    
    
    // End critical section
    RctMutex::auth_msg_buf.unlock();
}








