/* $Id: PingServerImpl.cc,v 1.13 2003/05/08 20:34:43 thomas Exp $ */

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

#include "PingServerImpl.h"


// Local Method:
// Sets the ping map value for a particular key
void
PingServer_i::set_map_value(const char* a_user_id, const char a_online_status)
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    CORBA::Long l_num_erased;
    
    l_num_erased = pr_ping_map.erase(a_user_id);
    
    pr_pmap_res = pr_ping_map.insert(make_pair(string(a_user_id), char(a_online_status)));
}


// Local Method:
// Returns the ping map value for a particular key
char
PingServer_i::get_map_value(const char* a_user_id)
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    pr_pmap_iter = pr_ping_map.find(a_user_id);

    if(pr_pmap_iter != pr_ping_map.end() ) {

        return pr_pmap_iter->second;
    }
    else {

        return PING_NULL;
    }
}

// Local Method
void
PingServer_i::create_ping_thread()
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    pthread_create(&p_tid, NULL, check_user_online_status, NULL);
}


// IDL method
// Sets the ping map value to true when invoked by the user
void
PingServer_i::ping(const char* a_user_id)
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    RctMutex::auth.lock();

    char l_map_value;
    l_map_value = get_map_value(a_user_id);

    if (PING_TRUE == l_map_value) {

        // Ping map value already true -- nothing to be done
    }
    else if (PING_FALSE == l_map_value) {

        set_map_value(a_user_id, PING_TRUE);
    }
    else if (PING_NULL == l_map_value) {

        set_map_value(a_user_id, PING_TRUE);
    }

    RctMutex::auth.unlock();
}


// IDL Method:
void
PingServer_i::bandwidth(const BinaryPacket &a_packet) {

    // We don't need to do anything in here
}

// Function:
// Thread method that checks the online status of all users periodically
// and updates the database for those who have crashed
void*
check_user_online_status(void* a_arg)
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    // Get module refs.
    DatabaseServerS *l_db_server = DatabaseServerS::instance(); 
    AuthenticationServerS *l_auth_server = AuthenticationServerS::instance();
    UserServerS *l_user_server = UserServerS::instance();
    PingServerS *l_ping_server = PingServerS::instance();
    SessionServer_i *l_session_server = SessionServer_i::instance();

    DBresult* l_res = NULL;
    CORBA::String_var l_user_id;
    CORBA::String_var l_user_alias;
    char l_map_value;
    CORBA::Long l_ntuples;
    
    // Check the online status of all users
    // updating database for those clients who have crashed
    while(1) {

        // Setting the server status on each ping maint. cycle
        l_session_server->set_server_status();
        
        // Sleep for a certain time between checking
        sleep(PINGSLEEP);

        RctMutex::auth.lock();

        // Execute the query
        // Gets the userids of all online users
        l_res = l_db_server->exec_db_query(SELECT_USERID_STAT_ON);

        // Test if we have any online users currently
        if (!l_db_server->has_result_no_tuple(l_res)) {

            // Get the number of users that are online
            l_ntuples = l_db_server->db_ntuples(l_res);

            // Loop to check the consistency of the database for each userid returned
            // ie.. whether the user is still online or crashed
            // In the case of inconsistency update database
            for (int i = 0; i < l_ntuples; i++) {

                // Get the user id
                l_user_id = l_db_server->db_getvalue(l_res, i, 0);

                // Get the map value for the user id
                l_map_value = l_ping_server->get_map_value(l_user_id);

                // Testing the map value
                if((PING_NULL == l_map_value) || (PING_FALSE == l_map_value)) {

                    l_user_alias = l_db_server->db_getvalue(l_res, i, 1);
                    CORBA::String_var l_password = l_user_server->get_password_from_id(l_user_id);
                    
                    l_auth_server->logout(BC_MSG_USER_OFFLINE,
                                          l_user_alias,
                                          l_password,
                                          " has disconnected",
                                          "",
                                          RCT_VERSION,
                                          ABNORMAL);
                }
                else if (PING_TRUE == l_map_value) {

                    // Set the ping map value for this user to false
                    // This is done because if the user crashes after this he will not
                    // send any more ping messages and so the ping map value will reamin
                    // false and we can then update the database in the next iteration
                    // of the while loop 
                    l_ping_server->set_map_value(l_user_id, PING_FALSE);
                } 
            }
        }

        // Delete db result
        l_db_server->db_clear(l_res);

        RctMutex::auth.unlock();
    }

    return NULL;
}




