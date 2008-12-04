// $Id: ControlServerImpl.cc,v 1.11 2003/05/08 20:34:43 thomas Exp $

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

#include "ControlServerImpl.h"


// Local Method:
CORBA::Boolean
ControlServer_i::is_empty(const char *a_class_name,
                          const char *a_assembly_name,
                          const char *a_module_name,
                          CORBA::Long a_assembly_type)
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DatabaseServerS * l_db_server = DatabaseServerS::instance();

    // Make the class name safe fore db query
    char* l_class_name_esc =
        l_db_server->db_escape_string(a_class_name,
                                      strlen(a_class_name));

    // Make the assembly name safe for db query
    char* l_assembly_name_esc =
        l_db_server->db_escape_string(a_assembly_name,
                                      strlen(a_assembly_name));
    
    // Compose the query string
    CORBA::String_var l_query;

    if(TEAM == a_assembly_type) {

        l_query = UTIL::comp_string("select * from rct_control_team_view where class_name='",
                                l_class_name_esc,
                                "' and team_name='",
                                l_assembly_name_esc,
                                "' and rct_module='",
                                a_module_name,
                                "'",
                                END);

    }
    else if(GROUP == a_assembly_type) {

        l_query = UTIL::comp_string("select * from rct_control_group_view where class_name='",
                                l_class_name_esc,
                                "' and group_name='",
                                l_assembly_name_esc,
                                "' and rct_module='",
                                a_module_name,
                                "'",
                                END);
    }

    // This will hold the tuple from the db query
    DBresult* l_res = NULL;
    l_res = l_db_server->exec_db_query(l_query);

    // Test if there are users in the specific queue
    if(l_db_server->has_result_no_tuple(l_res)) {

        // Freeing memory
        l_db_server->db_clear(l_res);

        // Clean up escaped names
        delete l_class_name_esc;
        delete l_assembly_name_esc;

        // In this case there are no users
        // and the queue is empty
        return 1;
    }
    else {

        // Freeing memory
        l_db_server->db_clear(l_res);

        // Clean up escaped names
        delete l_class_name_esc;
        delete l_assembly_name_esc;

        // In this case there are users
        // and the queue is not empty
        return 0;
    }
}


// Local Method:
CORBA::Boolean
ControlServer_i::is_user_in_queue(const char *a_class_id,
                                  const char *a_assembly_id,
                                  const char *a_user_id,
                                  const char *a_module_name)
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DatabaseServerS * l_db_server = DatabaseServerS::instance();
    
    // Compose the query string
    CORBA::String_var l_query;
    l_query = UTIL::comp_string(SELECT_CONT_QUEUE_EMPTY,
                                a_class_id,
                                "' and assembly_id='",
                                a_assembly_id,
                                "' and user_id='",
                                a_user_id,
                                "' and rct_module='",
                                a_module_name,
                                "'",
                                END);

    // This will hold the tuple from the db query
    DBresult* l_res = NULL;
    l_res = l_db_server->exec_db_query(l_query);

    // Test if the user is in module queue
    if(l_db_server->has_result_no_tuple(l_res)) {

        // Freeing memory
        l_db_server->db_clear(l_res);

        // In this case the user is not in the queue
        return 0;
    }
    else {

        // Freeing memory
        l_db_server->db_clear(l_res);

        // In this case the user is in the queue
        return 1;
    }
}


// IDL Method:
void
ControlServer_i::request(const char *a_class_name,
                         const char *a_assembly_name,
                         const char *a_user_alias,
                         const char *a_user_id,
                         const char *a_module_name,
                         CORBA::Long a_type)
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DatabaseServerS * l_db_server = DatabaseServerS::instance();
    ClassServerS *l_class_server = ClassServerS::instance();
    TeamServerS *l_team_server = TeamServerS::instance();
    GroupServerS *l_group_server = GroupServerS::instance();
    UserServerS *l_user_server = UserServerS::instance();
    SessionServer_i *l_session_server = SessionServer_i::instance();

    // This flag is used to keep track if the queue is empty
    CORBA::Boolean is_queue_empty = 1;
    
    // Vars for class, team, and group id
    CORBA::String_var l_class_id = l_class_server->get_class_id_from_class_name(a_class_name);
    CORBA::String_var l_assembly_id;
    
    if(CONTROL_REQ_TEAM == a_type) {

        l_assembly_id = l_team_server->get_team_id_from_team_name(l_class_id, a_assembly_name);
        
    }
    else if(CONTROL_REQ_GROUP == a_type) {

        l_assembly_id = l_group_server->get_group_id_from_group_name(l_class_id, a_assembly_name);
    }
    else {

        cerr << "ERROR: Wrong type!" << endl;
    }

    // Compose the query string
    CORBA::String_var l_trans;

    // Enter critical section
    RctMutex::control.lock();
    
    // Check if there exists a specific queue
    if(is_empty(a_class_name,
                a_assembly_name,
                a_module_name,
                (CONTROL_REQ_TEAM == a_type) ? TEAM : GROUP)) {

        l_trans = UTIL::comp_string(INS_CONTROL,
                                    (char *)l_class_id,
                                    "','",
                                    (char *)l_assembly_id,
                                    "','",
                                    a_user_id,
                                    "','",
                                    a_module_name,
                                    "', 1)",
                                    END);
    }
    else {

        l_trans = UTIL::comp_string(INS_CONTROL,
                                    (char *)l_class_id,
                                    "','",
                                    (char *)l_assembly_id,
                                    "','",
                                    a_user_id,
                                    "','",
                                    a_module_name,
                                    "',",
                                    SELECT_CONT_QUEUE_POS,
                                    (char *)l_class_id,
                                    "' and assembly_id='",
                                    (char *)l_assembly_id,
                                    "' and rct_module='",
                                    a_module_name,
                                    "'))",
                                    END);

        // Setting flag since queue is not empty
        is_queue_empty = 0;
    }

    // Add the request to the DB
    // Execute db transaction
    l_db_server->exec_db_insert(l_trans);

    // Now we have to let the participating module
    // users know about the request

    // Create the user id seq and control message
    UserIdSeq_var l_user_id_seq;
    ControlMessage l_control_msg;
    
    l_control_msg.base_msg.version = RCT_VERSION;
    l_control_msg.base_msg.user_alias = a_user_alias;
    l_control_msg.base_msg.class_name = a_class_name;
    l_control_msg.base_msg.permission = UNCLASSIFIED;
    l_control_msg.module_name = a_module_name;

    // Perform team and group specific assignments
    if(CONTROL_REQ_TEAM == a_type) {

        l_control_msg.base_msg.type = CONTROL_REQ_TEAM_NOTIF;
        l_control_msg.base_msg.team_name = a_assembly_name;
        l_user_server->get_online_user_ids_from_team_name(a_class_name, a_assembly_name, l_user_id_seq);
    }
    else if(CONTROL_REQ_GROUP == a_type) {

        l_control_msg.base_msg.type = CONTROL_REQ_GROUP_NOTIF;
        l_control_msg.base_msg.group_name = a_assembly_name;
        l_user_server->get_user_ids_from_group_name(a_class_name, a_assembly_name, l_user_id_seq);
    }
    else {

        cerr << "ERROR: Wrong type!" << endl;
    }

    // Now we send the message to each user event channel
    for (int i = 0; i < l_user_id_seq->length(); i++) {

        // Note: the [USER_ID_OFFSET] accesses the user_id string's numeric part bypassing the "U" of
        // the user_id, thus if we have U123, we just convert 123 into an integer
        l_session_server->push_control_msg(l_control_msg, atoi(&l_user_id_seq[i][USER_ID_OFFSET]));
    }

    // Check if the queue was empty. If it was empty
    // we need to send the requester the notification
    // that he/she can access the module.
    if(is_queue_empty) {

        l_control_msg.base_msg.version = RCT_VERSION;
        l_control_msg.base_msg.user_alias = a_user_alias;
        l_control_msg.base_msg.class_name = a_class_name;
        l_control_msg.base_msg.permission = UNCLASSIFIED;
        l_control_msg.module_name = a_module_name;

        if(CONTROL_REQ_TEAM == a_type) {

            l_control_msg.base_msg.type = CONTROL_ACTIVE_TEAM_NOTIF;
            l_control_msg.base_msg.team_name = a_assembly_name;
        }
        else if(CONTROL_REQ_GROUP == a_type) {
        
            l_control_msg.base_msg.type = CONTROL_ACTIVE_GROUP_NOTIF;
            l_control_msg.base_msg.group_name = a_assembly_name;
        }

        // Note: the [USER_ID_OFFSET] accesses the user_id string's numeric part bypassing the "U" of
        // the user_id, thus if we have U123, we just convert 123 into an integer
        l_session_server->push_control_msg(l_control_msg, atoi(&a_user_id[USER_ID_OFFSET]));
    }

    // Exit cricical section
    RctMutex::control.unlock();
}


// IDL Method:
void
ControlServer_i::release(const char *a_class_name,
                         const char *a_assembly_name,
                         const char *a_user_alias,
                         const char *a_user_id,
                         const char *a_module_name,
                         CORBA::Long a_type)
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif
    
    DatabaseServerS * l_db_server = DatabaseServerS::instance();
    ClassServerS *l_class_server = ClassServerS::instance();
    TeamServerS *l_team_server = TeamServerS::instance();
    GroupServerS *l_group_server = GroupServerS::instance();
    UserServerS *l_user_server = UserServerS::instance();
    SessionServer_i *l_session_server = SessionServer_i::instance();

    // Vars for class, team, and group id
    CORBA::String_var l_class_id = l_class_server->get_class_id_from_class_name(a_class_name);
    CORBA::String_var l_assembly_id;
    
    if(CONTROL_REL_TEAM == a_type) {

        l_assembly_id = l_team_server->get_team_id_from_team_name(l_class_id, a_assembly_name);
        
    }
    else if(CONTROL_REL_GROUP == a_type) {

        l_assembly_id = l_group_server->get_group_id_from_group_name(l_class_id, a_assembly_name);
    }
    else {

        cerr << "ERROR: Wrong type!" << endl;
    }

    // Compose the query string
    CORBA::String_var l_query;
    l_query = UTIL::comp_string(SELECT_GET_QUEUE,
                                (char *)l_class_id,
                                "' and assembly_id='",
                                (char *)l_assembly_id,
                                "' and rct_module='",
                                a_module_name,
                                "' order by queue_position asc",
                                END);

    // Enter critical section
    RctMutex::control.lock();

    // This will hold the tuple from the db query
    DBresult* l_res = NULL;
    l_res = l_db_server->exec_db_query(l_query);

    // Assign front of queue
    CORBA::String_var l_user_id_front;
    CORBA::String_var l_user_id_next;
    CORBA::String_var l_user_alias_next;
    UserIdSeq_var l_user_id_seq;
    
    // Get the queue size
    CORBA::Long l_n_tuples = l_db_server->db_ntuples(l_res);
    
    // Test if we have a valid result
    if(!l_db_server->has_result_no_tuple(l_res)) {

        l_user_id_front = l_db_server->db_getvalue(l_res, 0, 0);

        // Send a control message to everybody about the release
        ControlMessage l_control_msg_release;
         
        l_control_msg_release.base_msg.version = RCT_VERSION;
        l_control_msg_release.base_msg.user_alias = a_user_alias;
        l_control_msg_release.base_msg.class_name = a_class_name;
        l_control_msg_release.base_msg.permission = UNCLASSIFIED;
        l_control_msg_release.module_name = a_module_name;

        if(CONTROL_REL_TEAM == a_type) {

            l_control_msg_release.base_msg.type = CONTROL_REL_TEAM_NOTIF;
            l_control_msg_release.base_msg.team_name = a_assembly_name;
            l_user_server->get_online_user_ids_from_team_name(a_class_name, a_assembly_name, l_user_id_seq);

        }
        else if(CONTROL_REL_GROUP == a_type) {
            
            l_control_msg_release.base_msg.type = CONTROL_REL_GROUP_NOTIF;
            l_control_msg_release.base_msg.group_name = a_assembly_name;
            l_user_server->get_user_ids_from_group_name(a_class_name, a_assembly_name, l_user_id_seq);
        }

        // Now we send the message to each user event channel
        for (int i = 0; i < l_user_id_seq->length(); i++) {
            
            // Note: the [USER_ID_OFFSET] accesses the user_id string's numeric part bypassing the "U" of
            // the user_id, thus if we have U123, we just convert 123 into an integer
            l_session_server->push_control_msg(l_control_msg_release, atoi(&l_user_id_seq[i][USER_ID_OFFSET]));
        }


        // Send specific message to second user in queue
        // Check if there is more then one user in the queue
        if(1 < l_n_tuples) {

            // If the queue has more than one user, we need to inform
            // the second user about the his/her turn, active status
            l_user_id_next = l_db_server->db_getvalue(l_res, 1, 0);
            l_user_alias_next = l_user_server->get_alias_from_id(l_user_id_next);
            
            ControlMessage l_control_msg_active;
            
            l_control_msg_active.base_msg.version = RCT_VERSION;
            l_control_msg_active.base_msg.user_alias = l_user_alias_next;
            l_control_msg_active.base_msg.class_name = a_class_name;
            l_control_msg_active.base_msg.permission = UNCLASSIFIED;
            l_control_msg_active.module_name = a_module_name;

            if(CONTROL_REL_TEAM == a_type) {

                l_control_msg_active.base_msg.type = CONTROL_ACTIVE_TEAM_NOTIF;
                l_control_msg_active.base_msg.team_name = a_assembly_name;
            }
            else if(CONTROL_REL_GROUP == a_type) {
                
                l_control_msg_active.base_msg.type = CONTROL_ACTIVE_GROUP_NOTIF;
                l_control_msg_active.base_msg.group_name = a_assembly_name;
            }

            // Note: the [USER_ID_OFFSET] accesses the user_id string's numeric part bypassing the "U" of
            // the user_id, thus if we have U123, we just convert 123 into an integer
            l_session_server->push_control_msg(l_control_msg_active, atoi(&l_user_id_next[USER_ID_OFFSET]));
        }
    }

    // Now we need to delete the user from front of queue
    // Compose the query string
    CORBA::String_var l_trans;
    l_trans = UTIL::comp_string(DELETE_CONTROL_REQUEST,
                                (char *)l_class_id,
                                "' and assembly_id='",
                                (char *)l_assembly_id,
                                "' and user_id='",
                                a_user_id,
                                "' and rct_module='",
                                a_module_name,
                                "'",
                                END);

    // Delete the queue entry from the DB
    // Execute db transaction
    l_db_server->exec_db_delete(l_trans);

    // Exit cricical section
    RctMutex::control.unlock();
    
    // Freeing memory
    l_db_server->db_clear(l_res);
}

// IDL Method:
void
ControlServer_i::take(const char *a_class_name,
                      const char *a_assembly_name,
                      const char *a_user_alias,
                      const char *a_user_id,
                      const char *a_module_name,
                      CORBA::Long a_type) throw(CORBA::SystemException,
                                                RCT::ControlServer::QueueIsEmpty)
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DatabaseServerS * l_db_server = DatabaseServerS::instance();
    ClassServerS *l_class_server = ClassServerS::instance();
    TeamServerS *l_team_server = TeamServerS::instance();
    GroupServerS *l_group_server = GroupServerS::instance();
    UserServerS *l_user_server = UserServerS::instance();
    SessionServer_i *l_session_server = SessionServer_i::instance();

    // Vars for class, team, and group id
    CORBA::String_var l_class_id = l_class_server->get_class_id_from_class_name(a_class_name);
    CORBA::String_var l_assembly_id;
    
    if(CONTROL_TAKE_TEAM == a_type) {

        l_assembly_id = l_team_server->get_team_id_from_team_name(l_class_id, a_assembly_name);
        
    }
    else if(CONTROL_TAKE_GROUP == a_type) {

        l_assembly_id = l_group_server->get_group_id_from_group_name(l_class_id, a_assembly_name);
    }
    else {

        cerr << "ERROR: Wrong type!" << endl;
    }

    // Enter critical section
    RctMutex::control.lock();

    // Update the front of the user queue

    // First we check if the queue is empty
    if(is_empty(a_class_name,
                a_assembly_name,
                a_module_name,
                (CONTROL_TAKE_TEAM == a_type) ? TEAM : GROUP)) {

        // Enter critical section
        RctMutex::control.unlock();

        throw RCT::ControlServer::QueueIsEmpty("RCT EXCEPTION: Control Queue Is Empty");

        return;
    }

    // Compose the query string
    CORBA::String_var l_trans;
    l_trans = UTIL::comp_string(UPDATE_CONT_QUEUE_FRONT,
                                a_user_id,
                                "' where class_id='",
                                (char *)l_class_id,
                                "' and assembly_id='",
                                (char *)l_assembly_id,
                                "' and rct_module='",
                                a_module_name,
                                "' and queue_position in (",
                                SELECT_CONT_MIN_QUEUE_POS,
                                ")",
                                END);

    l_db_server->exec_db_update(l_trans);

    // Create the user id seq and control message
    UserIdSeq_var l_user_id_seq;
    ControlMessage l_control_msg;
    
    l_control_msg.base_msg.version = RCT_VERSION;
    l_control_msg.base_msg.user_alias = a_user_alias;
    l_control_msg.base_msg.class_name = a_class_name;
    l_control_msg.base_msg.permission = UNCLASSIFIED;
    l_control_msg.module_name = a_module_name;

    // Perform team and group specific assignments
    if(CONTROL_TAKE_TEAM == a_type) {

        l_control_msg.base_msg.type = CONTROL_TAKE_TEAM_NOTIF;
        l_control_msg.base_msg.team_name = a_assembly_name;
        l_user_server->get_online_user_ids_from_team_name(a_class_name, a_assembly_name, l_user_id_seq);
    }
    else if(CONTROL_TAKE_GROUP == a_type) {

        l_control_msg.base_msg.type = CONTROL_TAKE_GROUP_NOTIF;
        l_control_msg.base_msg.group_name = a_assembly_name;
        l_user_server->get_user_ids_from_group_name(a_class_name, a_assembly_name, l_user_id_seq);
    }
    else {

        cerr << "ERROR: Wrong type!" << endl;
    }

    // Now we send the message to each user event channel
    for (int i = 0; i < l_user_id_seq->length(); i++) {

        // Note: the [USER_ID_OFFSET] accesses the user_id string's
        // numeric part bypassing the "U" of the user_id, thus if
        // we have U123, we just convert 123 into an integer
        l_session_server->push_control_msg(l_control_msg, atoi(&l_user_id_seq[i][USER_ID_OFFSET]));
    }

    // Exit cricical section
    RctMutex::control.unlock();
}

// IDL Method:
void
ControlServer_i::cancel_request(const char *a_class_name,
                                const char *a_assembly_name,
                                const char *a_user_alias,
                                const char *a_user_id,
                                const char *a_module_name,
                                CORBA::Long a_type)
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DatabaseServerS * l_db_server = DatabaseServerS::instance();
    ClassServerS *l_class_server = ClassServerS::instance();
    TeamServerS *l_team_server = TeamServerS::instance();
    GroupServerS *l_group_server = GroupServerS::instance();
    UserServerS *l_user_server = UserServerS::instance();
    SessionServer_i *l_session_server = SessionServer_i::instance();
    
    // Vars for class, team, and group id
    CORBA::String_var l_class_id = l_class_server->get_class_id_from_class_name(a_class_name);
    CORBA::String_var l_assembly_id;
    
    if(CONTROL_CAN_REQ_TEAM == a_type) {

        l_assembly_id = l_team_server->get_team_id_from_team_name(l_class_id, a_assembly_name);
        
    }
    else if(CONTROL_CAN_REQ_GROUP == a_type) {

        l_assembly_id = l_group_server->get_group_id_from_group_name(l_class_id, a_assembly_name);
    }
    else {

        cerr << "ERROR: Wrong type!" << endl;
    }

    // Enter cricical section
    RctMutex::control.lock();

    // Handle the case where we get a cancel request from a user
    // who is at top of the queue
    // Compose the query string
    CORBA::String_var l_query;
    l_query = UTIL::comp_string(SELECT_GET_QUEUE,
                                (char *)l_class_id,
                                "' and assembly_id='",
                                (char *)l_assembly_id,
                                "' and rct_module='",
                                a_module_name,
                                "' order by queue_position asc",
                                END);

    // This will hold the tuple from the db query
    DBresult* l_res = NULL;
    l_res = l_db_server->exec_db_query(l_query);

    // Vars to hold the first and second in the queue
    CORBA::String_var l_user_id_first;
    CORBA::String_var l_user_id_second;

    // Flag for determening if we need to send a message
    // to the second user in queue
    CORBA::Boolean l_send_msg_to_second_in_queue = 0;
    
    // Test if we have a valid result
    if(!l_db_server->has_result_no_tuple(l_res)) {

        // Get the queue size
        CORBA::Long l_n_tuples = l_db_server->db_ntuples(l_res);

        // There need to be more then one in the queue
        if(1 < l_n_tuples) {

            l_user_id_first = l_db_server->db_getvalue(l_res, 0, 0);
            l_user_id_second = l_db_server->db_getvalue(l_res, 1, 0);

            if(0 == strcmp(a_user_id, l_user_id_first)) {

                // Setting flag that we need to send active message
                // later on
                l_send_msg_to_second_in_queue = 1;
            }
        }
    }

    // Freeing memory
    l_db_server->db_clear(l_res); 

    // Compose the query string
    CORBA::String_var l_trans;
    l_trans = UTIL::comp_string(DELETE_CONTROL_REQUEST,
                                (char *)l_class_id,
                                "' and assembly_id='",
                                (char *)l_assembly_id,
                                "' and user_id='",
                                a_user_id,
                                "' and rct_module='",
                                a_module_name,
                                "'",
                                END);


    // Delete the queue entry from the DB
    // Execute db transaction
    l_db_server->exec_db_delete(l_trans);

    // Now we have to let the participating module
    // users know about the request

    // Create the user id seq and control message
    UserIdSeq_var l_user_id_seq;
    ControlMessage l_control_msg;
    
    l_control_msg.base_msg.version = RCT_VERSION;
    l_control_msg.base_msg.user_alias = a_user_alias;
    l_control_msg.base_msg.class_name = a_class_name;
    l_control_msg.base_msg.permission = UNCLASSIFIED;
    l_control_msg.module_name = a_module_name;

    // Perform team and group specific assignments
    if(CONTROL_CAN_REQ_TEAM == a_type) {

        l_control_msg.base_msg.type = CONTROL_CAN_REQ_TEAM_NOTIF;
        l_control_msg.base_msg.team_name = a_assembly_name;
        l_user_server->get_online_user_ids_from_team_name(a_class_name, a_assembly_name, l_user_id_seq);
    }
    else if(CONTROL_CAN_REQ_GROUP == a_type) {
        l_control_msg.base_msg.type = CONTROL_CAN_REQ_GROUP_NOTIF;
        l_control_msg.base_msg.group_name = a_assembly_name;
        l_user_server->get_user_ids_from_group_name(a_class_name, a_assembly_name, l_user_id_seq);
    }
    else {

        cerr << "ERROR: Wrong type!" << endl;
    }

    // Now we send the message to each user event channel
    for (int i = 0; i < l_user_id_seq->length(); i++) {


        // Note: the [USER_ID_OFFSET] accesses the user_id string's numeric part bypassing the "U" of
        // the user_id, thus if we have U123, we just convert 123 into an integer
        l_session_server->push_control_msg(l_control_msg, atoi(&l_user_id_seq[i][USER_ID_OFFSET]));
    }

    // Testing if we need to an active send message to second user in queue
    if(l_send_msg_to_second_in_queue) {

        CORBA::String_var l_user_alias_second;
        l_user_alias_second = l_user_server->get_alias_from_id(l_user_id_second);

        ControlMessage l_control_msg_active;
        l_control_msg_active.base_msg.version = RCT_VERSION;
        l_control_msg_active.base_msg.user_alias = l_user_alias_second;
        l_control_msg_active.base_msg.class_name = a_class_name;
        l_control_msg_active.base_msg.permission = UNCLASSIFIED;
        l_control_msg_active.module_name = a_module_name;

        if(CONTROL_CAN_REQ_TEAM == a_type) {
            
            l_control_msg_active.base_msg.type = CONTROL_ACTIVE_TEAM_NOTIF;
            l_control_msg_active.base_msg.team_name = a_assembly_name;
        }
        else if(CONTROL_CAN_REQ_GROUP == a_type) {
            
            l_control_msg_active.base_msg.type = CONTROL_ACTIVE_GROUP_NOTIF;
            l_control_msg_active.base_msg.group_name = a_assembly_name;
        }
        
        // Note: the [USER_ID_OFFSET] accesses the user_id string's numeric part bypassing the "U" of
        // the user_id, thus if we have U123, we just convert 123 into an integer
        l_session_server->push_control_msg(l_control_msg_active, atoi(&l_user_id_second[USER_ID_OFFSET]));
    }
    
    // Exit cricical section
    RctMutex::control.unlock();
}

// IDL Method:
void
ControlServer_i::exit_request(const char *a_class_name,
                              const char *a_assembly_name,
                              const char *a_user_alias,
                              const char *a_user_id,
                              const char *a_module_name,
                              CORBA::Long a_type)
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DatabaseServerS * l_db_server = DatabaseServerS::instance();
    ClassServerS *l_class_server = ClassServerS::instance();
    TeamServerS *l_team_server = TeamServerS::instance();
    GroupServerS *l_group_server = GroupServerS::instance();
    UserServerS *l_user_server = UserServerS::instance();
    SessionServer_i *l_session_server = SessionServer_i::instance();

    // This will hold the tuple from the db query
    DBresult* l_res = NULL;

    // Vars for class, team, and group id
    CORBA::String_var l_class_id = l_class_server->get_class_id_from_class_name(a_class_name);
    CORBA::String_var l_assembly_id;
    
    if(CONTROL_EXIT_TEAM == a_type) {

        l_assembly_id = l_team_server->get_team_id_from_team_name(l_class_id, a_assembly_name);
        
    }
    else if(CONTROL_EXIT_GROUP == a_type) {

        l_assembly_id = l_group_server->get_group_id_from_group_name(l_class_id, a_assembly_name);
    }
    else {

        cerr << "ERROR: Wrong type!" << endl;
    }

    // Enter critical section
    RctMutex::control.lock();
    
    // Compose the query string
    CORBA::String_var l_query;
    l_query = UTIL::comp_string(SELECT_GET_QUEUE,
                                (char *)l_class_id,
                                "' and assembly_id='",
                                (char *)l_assembly_id,
                                "' and rct_module='",
                                a_module_name,
                                "' order by queue_position asc",
                                END);

    // Execute the query
    l_res = l_db_server->exec_db_query(l_query);

    // Assign front of queue
    CORBA::String_var l_user_id_front;
    CORBA::String_var l_user_id_next;
    CORBA::String_var l_user_alias_next;
    UserIdSeq_var l_user_id_seq;
    
    // Get the queue size
    CORBA::Long l_n_tuples = l_db_server->db_ntuples(l_res);
    
    // Test if we have a valid result
    if(!l_db_server->has_result_no_tuple(l_res)) {

        l_user_id_front = l_db_server->db_getvalue(l_res, 0, 0);

        // Check if there is more then one user in the queue
        if((1 < l_n_tuples) &&
           (0 == strcmp(a_user_id, l_user_id_front))) {

            // If the queue has more than one user, we need to inform
            // the second user about his/her turn, active status
            l_user_id_next = l_db_server->db_getvalue(l_res, 1, 0);
            l_user_alias_next = l_user_server->get_alias_from_id(l_user_id_next);
            
            ControlMessage l_control_msg_active;
            
            l_control_msg_active.base_msg.version = RCT_VERSION;
            l_control_msg_active.base_msg.user_alias = l_user_alias_next;
            l_control_msg_active.base_msg.class_name = a_class_name;
            l_control_msg_active.base_msg.permission = UNCLASSIFIED;
            l_control_msg_active.module_name = a_module_name;

            if(CONTROL_EXIT_TEAM == a_type) {

                l_control_msg_active.base_msg.type = CONTROL_ACTIVE_TEAM_NOTIF;
                l_control_msg_active.base_msg.team_name = a_assembly_name;
            }
            else if(CONTROL_EXIT_GROUP == a_type) {
                
                l_control_msg_active.base_msg.type = CONTROL_ACTIVE_GROUP_NOTIF;
                l_control_msg_active.base_msg.group_name = a_assembly_name;
            }

            // Note: the [USER_ID_OFFSET] accesses the user_id string's numeric part bypassing the "U" of
            // the user_id, thus if we have U123, we just convert 123 into an integer
            l_session_server->push_control_msg(l_control_msg_active, atoi(&l_user_id_next[USER_ID_OFFSET]));
        }

        ControlMessage l_control_msg_exit;
         
        l_control_msg_exit.base_msg.version = RCT_VERSION;
        l_control_msg_exit.base_msg.user_alias = a_user_alias;
        l_control_msg_exit.base_msg.class_name = a_class_name;
        l_control_msg_exit.base_msg.permission = UNCLASSIFIED;
        l_control_msg_exit.module_name = a_module_name;

        if(CONTROL_EXIT_TEAM == a_type) {

            l_control_msg_exit.base_msg.type = CONTROL_EXIT_TEAM_NOTIF;
            l_control_msg_exit.base_msg.team_name = a_assembly_name;
            l_user_server->get_online_user_ids_from_team_name(a_class_name, a_assembly_name, l_user_id_seq);

        }
        else if(CONTROL_EXIT_GROUP == a_type) {
            
            l_control_msg_exit.base_msg.type = CONTROL_EXIT_GROUP_NOTIF;
            l_control_msg_exit.base_msg.group_name = a_assembly_name;
            l_user_server->get_user_ids_from_group_name(a_class_name, a_assembly_name, l_user_id_seq);
        }

        // Now we send the message to each user event channel
        for (int i = 0; i < l_user_id_seq->length(); i++) {
            
            // Note: the [USER_ID_OFFSET] accesses the user_id string's numeric part bypassing the "U" of
            // the user_id, thus if we have U123, we just convert 123 into an integer
            l_session_server->push_control_msg(l_control_msg_exit, atoi(&l_user_id_seq[i][USER_ID_OFFSET]));
        }
    }

    // Now we need to delete the user from front of queue
    // Compose the query string
    CORBA::String_var l_trans;

    l_trans = UTIL::comp_string(DELETE_CONTROL_REQUEST,
                                (char *)l_class_id,
                                "' and assembly_id='",
                                (char *)l_assembly_id,
                                "' and user_id='",
                                a_user_id,
                                "' and rct_module='",
                                a_module_name,
                                "'",
                                END);

    // Delete the queue entry from the DB
    // Execute db transaction
    l_db_server->exec_db_delete(l_trans);

    // Exit cricical section
    RctMutex::control.unlock();
    
    // Freeing memory
    l_db_server->db_clear(l_res);
}


















