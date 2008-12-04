/* $Id: GroupServerImpl.cc,v 1.25 2004/10/23 06:06:46 thomas Exp $ */

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

#include "GroupServerImpl.h"


// IDL Method:
void
GroupServer_i::get_all_groups(GroupSeq_out a_group_seq)
{
	
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DatabaseServerS *l_db_server = DatabaseServerS::instance();
    
    // This will hold the tuple from the db query
    DBresult* l_res = NULL;

    // Create the empty group sequence
    a_group_seq = new GroupSeq();

    // Compose the query string
    CORBA::String_var l_query;
    l_query = CORBA::string_dup(SELECT_GROUPS);

    // Execute the query
    l_res = l_db_server->exec_db_query(l_query);

    // Test if we have a valid result
    if(!l_db_server->has_result_no_tuple(l_res)) {

        // Creating vars to hold the number of tuples
        CORBA::Long l_tuples = l_db_server->db_ntuples(l_res);

        // Setting the size of the group sequence
        a_group_seq->length(l_tuples);
        
        // Now we can access the tuples and populate
        // the group sequence
        for(unsigned int i = 0; i < l_tuples; i++) {

            a_group_seq[i].group_id = l_db_server->db_getvalue(l_res, i, RCT_GROUP_ID);
            a_group_seq[i].name = l_db_server->db_getvalue(l_res, i, RCT_GROUP_NAME);
            a_group_seq[i].class_id = l_db_server->db_getvalue(l_res, i, RCT_GROUP_CLASS_ID);
            a_group_seq[i].permission = l_db_server->db_getvalue(l_res, i, RCT_GROUP_PER);
            a_group_seq[i].date = l_db_server->db_getvalue(l_res, i, RCT_GROUP_DATE);
            a_group_seq[i].manager = l_db_server->db_getvalue(l_res, i, RCT_GROUP_MGR);
        }
    }

    // Freeing memory
    l_db_server->db_clear(l_res);
}


// IDL Method:
void
GroupServer_i::get_groups_from_user_and_class(const char *a_user_id,
                                              const char *a_class_id,
                                              GroupSeq_out a_group_seq)
{
	
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DatabaseServerS *l_db_server = DatabaseServerS::instance();
    
    // This will hold the tuple from the db query
    DBresult* l_res = NULL;

    // Create the empty group sequence
    a_group_seq = new GroupSeq();
    
    // Compose the query string
    CORBA::String_var l_query;
    l_query = UTIL::comp_string(SELECT_GROUPS_FOR_USER_IN_CLASS,
                                a_user_id,
                                "' and G.class_id='",
                                a_class_id,
                                "'",
                                END);

    // Execute the query
    l_res = l_db_server->exec_db_query(l_query);

    // Test if we have a valid result
    if(!l_db_server->has_result_no_tuple(l_res)) {

        // Creating vars to hold the number of tuples
        CORBA::Long l_tuples = l_db_server->db_ntuples(l_res);

        // Setting the size of the group sequence
        a_group_seq->length(l_tuples);
        
        // Now we can access the tuples and populate
        // the group sequence
        for(unsigned int i = 0; i < l_tuples; i++) {

            a_group_seq[i].group_id = l_db_server->db_getvalue(l_res, i, RCT_GROUP_ID);
            a_group_seq[i].name = l_db_server->db_getvalue(l_res, i, RCT_GROUP_NAME);
            a_group_seq[i].class_id = l_db_server->db_getvalue(l_res, i, RCT_GROUP_CLASS_ID);
            a_group_seq[i].permission = l_db_server->db_getvalue(l_res, i, RCT_GROUP_PER);
            a_group_seq[i].date = l_db_server->db_getvalue(l_res, i, RCT_GROUP_DATE);
            a_group_seq[i].manager = l_db_server->db_getvalue(l_res, i, RCT_GROUP_MGR);
        }
    }

    // Freeing memory
    l_db_server->db_clear(l_res);
}


// IDL Method:
void
GroupServer_i::get_groups_from_class(const char *a_class_id,
                                     GroupSeq_out a_group_seq)
{
	
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DatabaseServerS *l_db_server = DatabaseServerS::instance();
    
    // This will hold the tuple from the db query
    DBresult* l_res = NULL;

    // Create the empty group sequence
    a_group_seq = new GroupSeq();
    
    // Compose the query string
    CORBA::String_var l_query;
    l_query = UTIL::comp_string(SELECT_GROUPS_FOR_CLASS_ID,
                                a_class_id,
                                "'",
                                END);

    // Execute the query
    l_res = l_db_server->exec_db_query(l_query);

    // Test if we have a valid result
    if(!l_db_server->has_result_no_tuple(l_res)) {

        // Creating vars to hold the number of tuples
        CORBA::Long l_tuples = l_db_server->db_ntuples(l_res);

        // Setting the size of the group sequence
        a_group_seq->length(l_tuples);
        
        // Now we can access the tuples and populate
        // the group sequence
        for(unsigned int i = 0; i < l_tuples; i++) {

            a_group_seq[i].group_id = l_db_server->db_getvalue(l_res, i, RCT_GROUP_ID);
            a_group_seq[i].name = l_db_server->db_getvalue(l_res, i, RCT_GROUP_NAME);
            a_group_seq[i].class_id = l_db_server->db_getvalue(l_res, i, RCT_GROUP_CLASS_ID);
            a_group_seq[i].permission = l_db_server->db_getvalue(l_res, i, RCT_GROUP_PER);
            a_group_seq[i].date = l_db_server->db_getvalue(l_res, i, RCT_GROUP_DATE);
            a_group_seq[i].manager = l_db_server->db_getvalue(l_res, i, RCT_GROUP_MGR);
        }
    }

    // Freeing memory
    l_db_server->db_clear(l_res);
}



// IDL Method:
void
GroupServer_i::get_groups_from_class_name(const char *a_class_name, GroupSeq_out a_group_seq)
{
	
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DatabaseServerS *l_db_server = DatabaseServerS::instance();

    // Make the group name safe fore db entry
    char* l_class_name_esc =
        l_db_server->db_escape_string(a_class_name,
                                      strlen(a_class_name));

    // This will hold the tuple from the db query
    DBresult* l_res = NULL;

    // Create the empty group sequence
    a_group_seq = new GroupSeq();
    
    // Compose the query string
    CORBA::String_var l_query;
    l_query = UTIL::comp_string(SELECT_GROUPS_FOR_CLASS,
                                l_class_name_esc,
                                "')",
                                END);

    // Execute the query
    l_res = l_db_server->exec_db_query(l_query);

    // Test if we have a valid result
    if(!l_db_server->has_result_no_tuple(l_res)) {

        // Creating vars to hold the number of tuples
        CORBA::Long l_tuples = l_db_server->db_ntuples(l_res);

        // Setting the size of the group sequence
        a_group_seq->length(l_tuples);
        
        // Now we can access the tuples and populate
        // the group sequence
        for(unsigned int i = 0; i < l_tuples; i++) {

            a_group_seq[i].group_id = l_db_server->db_getvalue(l_res, i, RCT_GROUP_ID);
            a_group_seq[i].name = l_db_server->db_getvalue(l_res, i, RCT_GROUP_NAME);
            a_group_seq[i].class_id = l_db_server->db_getvalue(l_res, i, RCT_GROUP_CLASS_ID);
            a_group_seq[i].permission = l_db_server->db_getvalue(l_res, i, RCT_GROUP_PER);
            a_group_seq[i].date = l_db_server->db_getvalue(l_res, i, RCT_GROUP_DATE);
            a_group_seq[i].manager = l_db_server->db_getvalue(l_res, i, RCT_GROUP_MGR);
        }
    }

    // Freeing memory
    l_db_server->db_clear(l_res);

    // Cleaning up escaped name
    delete l_class_name_esc;
}


// IDL Method:
CORBA::Boolean
GroupServer_i::create_group(const char* a_class_name,
                            const char *a_group_name,
                            const char* a_user_id,
                            const char* a_alias)
{
	
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DatabaseServerS *l_db_server = DatabaseServerS::instance();
    ClassServerS *l_class_server = ClassServerS::instance();
    TeamServerS *l_team_server = TeamServerS::instance();
    
    // Enter critical secion
    RctMutex::group.lock();
    
    // First check whether a group of the same name exists in this class
    if(does_group_exist(a_class_name, a_group_name)) {

        // End critical secion
        RctMutex::group.unlock();
        return 0;
    }
    // This is important since the client cannot needs unique team and group
    // names in a class.
    else if(l_team_server->does_team_exist(a_class_name, a_group_name)) {

        // End critical secion
        RctMutex::group.unlock();
        return 0;
    }

    // Vars
    DBresult* l_res = NULL;
    CORBA::String_var l_group_index;

    l_group_index = l_db_server->get_group_index();

    // Compose group id
    CORBA::String_var l_group_id;
    l_group_id = UTIL::comp_string(RCT_GROUP,
                                   (char*)(l_group_index),
                                   END);

    // Get class id from class name
    CORBA::String_var l_class_id = l_class_server->get_class_id_from_class_name(a_class_name);

    // Make the group name safe fore db entry
    char* l_group_name_esc =
        l_db_server->db_escape_string(a_group_name,
                                      strlen(a_group_name));
    
    // Compose the group insert transaction
    CORBA::String_var l_trans;
    l_trans = UTIL::comp_string(INS_GROUP,
                                (char*)(l_group_id),
                                "','",
                                l_group_name_esc,
                                "','",
                                (char*)l_class_id,
                                "','",
                                "0",
                                "','",
                                a_user_id,
                                "','"
                                "now",
                                "')",
                                END);

    // Execute transaction
    l_db_server->exec_db_insert(l_trans);

    // End critical secion
    RctMutex::group.unlock();
    
    // Insert user in group
    l_trans = UTIL::comp_string(INS_MEMBER_GROUP,
                                a_user_id,
                                "','",
                                (char*)l_group_id,
                                "')",
                                END);

    // Execute transaction
    l_db_server->exec_db_insert(l_trans);

    // Cleaning up escaped group name
    delete l_group_name_esc;
    
    // Now we have to let all the members of the
    // group relevant class know about the group creation.
    // Thus sending a message to the respective users.
    group_class_online_users_notification(a_class_name,
                                          a_group_name,
                                          a_alias,
                                          GROUP_CREATED_MSG);
    
    // Everything is ok
    return 1;
}


void
GroupServer_i::join_group_request(const char *a_class_name,
                                  const char *a_group_name,
                                  const char *a_user_id,
                                  const char *a_alias)
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif
    
    ClassServerS * l_class_server = ClassServerS::instance();
    SessionServer_i *l_session_server = SessionServer_i::instance();
    
    // Get class and group id
    CORBA::String_var l_class_id = l_class_server->get_class_id_from_class_name(a_class_name);
    CORBA::String_var l_group_id = get_group_id_from_group_name(l_class_id, a_group_name);
    
    CORBA::String_var l_group_mgr = get_group_mgr_from_group_id(l_group_id);


    
    // Creating the join group request message
    Message l_msg;
    l_msg.version = RCT_VERSION;
    l_msg.user_alias = a_alias;
    l_msg.permission = UNCLASSIFIED;
    l_msg.class_name = a_class_name;
    l_msg.group_name = a_group_name;
    l_msg.type = GROUP_JOIN_REQ_MSG;
    
    // Now we send the message to group mgr's event channel
    
    // Note: the user_id[1] accesses the user_id string's numeric part bypassing the "U" of
    // the user_id, thus if we have U123, we just convert 123 into an integer
    l_session_server->push_msg(l_msg, atoi(&l_group_mgr[USER_ID_OFFSET]));
}


void
GroupServer_i::join_group_request_reply(const char *a_class_name,
                                        const char *a_group_name,
                                        const char *a_user_id,
                                        const char *a_alias,
                                        const char *a_requester,
                                        CORBA::Boolean a_granted)
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DatabaseServerS *l_db_server = DatabaseServerS::instance();
    ClassServerS * l_class_server = ClassServerS::instance();
    UserServerS *l_user_server = UserServerS::instance();
    SessionServer_i *l_session_server = SessionServer_i::instance();
    
    // Get class and group id
    CORBA::String_var l_class_id = l_class_server->get_class_id_from_class_name(a_class_name);
    CORBA::String_var l_group_id = get_group_id_from_group_name(l_class_id, a_group_name);

    // Get the id of the requester from his/her alias
    User_var l_requester = l_user_server->get_user_from_user_alias(a_requester);
    CORBA::String_var l_requester_id = CORBA::string_dup(l_requester->user_id);

    // Create the message
    Message l_msg;
    l_msg.version = RCT_VERSION;
    l_msg.user_alias = a_alias;
    l_msg.permission = UNCLASSIFIED;
    l_msg.class_name = a_class_name;
    l_msg.group_name = a_group_name;

    
    if (a_granted) {

        // Compose the group member insert transaction
        CORBA::String_var l_trans;
                
        // Insert user in group
        l_trans = UTIL::comp_string(INS_MEMBER_GROUP,
                                    (char*)l_requester_id,
                                    "','",
                                    (char*)l_group_id,
                                    "')",
                                    END);

        // Execute transaction
        l_db_server->exec_db_insert(l_trans);

        l_msg.type = GROUP_JOIN_REQ_MSG_GRANTED;
    }
    else {

        // Creating the join group request message reply
        l_msg.type = GROUP_JOIN_REQ_MSG_DENIED;
    }
            
    // Now we send the message to requester's event channel
    
    // Note: the user_id[1] accesses the user_id string's numeric part bypassing the "U" of
    // the user_id, thus if we have U123, we just convert 123 into an integer
    l_session_server->push_msg(l_msg, atoi(&l_requester_id[USER_ID_OFFSET]));
    
    if (a_granted) {
        
        // Now we have to let all the members of the
        // group relevant class know about the new member.
        // Thus sending a message to the respective users.
        group_class_online_users_notification(a_class_name,
                                              a_group_name,
                                              a_requester,
                                              GROUP_JOIN_MEMBER_MSG);
    }
}

// IDL Method:
void
GroupServer_i::exit_group(const char *a_class_name,
                          const char *a_group_name,
                          const char *a_user_id,
                          const char *a_user_alias,
                          CORBA::Boolean a_status)
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    ClassServerS *l_class_server = ClassServerS::instance();
    UserServerS *l_user_server = UserServerS::instance();
    ControlServerS *l_control_server = ControlServerS::instance();
    
    // Get class and group id
    CORBA::String_var l_class_id = l_class_server->get_class_id_from_class_name(a_class_name);
    CORBA::String_var l_group_id = get_group_id_from_group_name(l_class_id, a_group_name);

    // Test if user is in specific queue
    // Add here any additional test for each controlled module
    // CASE TEXTPAD:
    if(l_control_server->is_user_in_queue(l_class_id,
                                          l_group_id,
                                          a_user_id,
                                          MODULE_TEXTPAD)) {

        // Testing for textpad queues
        l_control_server->exit_request(a_class_name,
                                       a_group_name,
                                       a_user_alias,
                                       a_user_id,
                                       MODULE_TEXTPAD,
                                       CONTROL_EXIT_GROUP);
    }
    
    // First check whether the user is the manager of the group
    CORBA::String_var l_group_mgr = get_group_mgr_from_group_id(l_group_id);            

    if (0 == strcmp(a_user_id, l_group_mgr)) {

        CORBA::Long l_n_members = get_number_of_members_in_group(l_class_id, l_group_id);
                
        if (1 > l_n_members) {
                    
            cerr << "ERROR: Wrong number of group members -- inconsistency! " << endl;
                    
        } else if (1 == l_n_members) {
                    
            // Remove the user from the group and send all other class members
            // this notification
            remove_group_member(l_class_id, l_group_id, a_user_id);

            group_class_online_users_notification(a_class_name,
                                                  a_group_name,
                                                  a_user_alias,
                                                  GROUP_MEMBER_EXITS);

            remove_group(l_group_id);
                    
            group_class_online_users_notification(a_class_name,
                                                  a_group_name,
                                                  a_user_alias,
                                                  GROUP_REMOVED);

        } else {

            // If we have a NORMAL exit call
            if(a_status) {
            
                // The user is the manager of the group
                // send him a message asking him to choose another manager
                choose_new_group_manager(a_class_name,
                                         a_group_name,
                                         a_user_id,
                                         a_user_alias);
            }
            // In case of an ABNORMAL exit, client crashed
            else {

                // First we remove the manager from the group
                remove_group_member(l_class_id, l_group_id, a_user_id);
                group_class_online_users_notification(a_class_name,
                                                      a_group_name,
                                                      a_user_alias,
                                                      GROUP_MEMBER_EXITS);
                
                // Find a user in the group with the highest user mode.
                // Meaning, see if there is a manager. If there is not
                // a manager in the group, we pick the first user.
                CORBA::String_var l_user_id;
                CORBA::String_var l_user_alias;
                
                if(has_group_manager(l_group_id, l_user_id)) {

                    l_user_alias = l_user_server->get_alias_from_id(l_user_id);

                    // Setting new manager
                    set_manager_for_group(l_user_id, l_group_id);

                    // Send new manager notification
                    group_members_notification(a_class_name,
                                               a_group_name,
                                               l_user_alias,
                                               GROUP_NEW_MANAGER);
                }
                else if(get_one_user_from_group(l_group_id, l_user_id)) {

                    l_user_alias = l_user_server->get_alias_from_id(l_user_id);

                    // Setting new manager
                    set_manager_for_group(l_user_id, l_group_id);

                    // Send new manager notification
                    group_members_notification(a_class_name,
                                               a_group_name,
                                               l_user_alias,
                                               GROUP_NEW_MANAGER);
                }
                else {
                    // There are no users in the group anymore
                    // Don't do anything
                }
            }
        }

    } else {

        // Remove the user from the group and send all other class members
        // this notification
        remove_group_member(l_class_id,
                            l_group_id,
                            a_user_id);

        group_class_online_users_notification(a_class_name,
                                              a_group_name,
                                              a_user_alias,
                                              GROUP_MEMBER_EXITS);
    }
}


// IDL Method:
// Set the group's new manager
void
GroupServer_i::set_new_manager(const char *a_class_name,
                               const char *a_group_name,
                               const char *a_user_id,
                               const char *a_user_alias,
                               const char *a_new_mgr_alias)
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DatabaseServerS *l_db_server = DatabaseServerS::instance();
    ClassServerS * l_class_server = ClassServerS::instance();
    UserServerS *l_user_server = UserServerS::instance();
    
    CORBA::String_var l_new_mgr_id;
    l_new_mgr_id = l_user_server->get_id_from_alias(a_new_mgr_alias); 

    CORBA::String_var l_class_id;
    l_class_id = l_class_server->get_class_id_from_class_name(a_class_name);

    CORBA::String_var l_group_id = get_group_id_from_group_name(l_class_id, a_group_name);
    
    // Compose the trans string
    CORBA::String_var l_trans;
    l_trans = UTIL::comp_string(UPDATE_MANAGER_GROUP,
                                (char*)l_new_mgr_id,
                                "'",
                                " where group_id='",
                                (char*)l_group_id,
                                "' ",
                                END);

    // Execute the update
    l_db_server->exec_db_update(l_trans);

    // Now remove the old manager from the group
    remove_group_member(l_class_id,
                        l_group_id,
                        a_user_id);
    
    // Send this removal notification
    group_class_online_users_notification(a_class_name,
                                          a_group_name,
                                          a_user_alias,
                                          GROUP_MEMBER_EXITS);
    
    // Send new manager notification
    group_members_notification(a_class_name,
                               a_group_name,
                               a_new_mgr_alias,
                               GROUP_NEW_MANAGER);
}


// IDL Method:
CORBA::Boolean
GroupServer_i::is_user_in_group(const char *a_class_name,
                                const char *a_group_name,
                                const char *a_user_alias)
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DatabaseServerS *l_db_server = DatabaseServerS::instance();
    ClassServerS * l_class_server = ClassServerS::instance();
    UserServerS *l_user_server = UserServerS::instance();
    
    // Get class, group, and user ids
    CORBA::String_var l_class_id = l_class_server->get_class_id_from_class_name(a_class_name);
    CORBA::String_var l_group_id = get_group_id_from_group_name(l_class_id, a_group_name);
    CORBA::String_var l_user_id = l_user_server->get_id_from_alias(a_user_alias);
    
    // Compose the query string
    CORBA::String_var l_query;
    l_query = UTIL::comp_string(SELECT_USER_IN_GROUP,
                                (char *)l_user_id,
                                "' and group_id='",
                                (char *)l_group_id,
                                "'",
                                END);

    // This will hold the tuple from the db query
    DBresult* l_res = NULL;

    // Execute the query
    l_res = l_db_server->exec_db_query(l_query);

    // Test if user is in group
    if(l_db_server->has_result_no_tuple(l_res)) {

        // Freeing memory
        l_db_server->db_clear(l_res);
        return 0;
    }
    else {

        // Freeing memory
        l_db_server->db_clear(l_res);
        return 1;
    }
}


// IDL Method:
char *
GroupServer_i::get_manager_id(const char *a_class_name,
                              const char *a_group_name)
{
	
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif
    
    DatabaseServerS *l_db_server = DatabaseServerS::instance();
    ClassServerS * l_class_server = ClassServerS::instance();
        
    // Get class, group, and user ids
    CORBA::String_var l_class_id = l_class_server->get_class_id_from_class_name(a_class_name);
    CORBA::String_var l_group_id = get_group_id_from_group_name(l_class_id, a_group_name);
    CORBA::String_var l_user_id;
    
    // Compose the query string
    CORBA::String_var l_query;
    l_query = UTIL::comp_string(SELECT_GROUP_MGR,
                                (char *)l_group_id,
                                "'",
                                END);

    // This will hold the tuple from the db query
    DBresult* l_res = NULL;
    // Execute the query
    l_res = l_db_server->exec_db_query(l_query);
    
    // Test if we have a valid result
    if(l_db_server->has_result_one_tuple(l_res)) {
        
        l_user_id = l_db_server->db_getvalue(l_res, 0, 0);
    }
    else {

        cerr << "ERROR: Did not get groups manger id!" << endl;
    }

    // Freeing memory
    l_db_server->db_clear(l_res);

    return l_user_id._retn();
}


// IDL Method:
void
GroupServer_i::get_chat_archive_last_n(const char *a_class_name,
                                       const char *a_group_name,
                                       CORBA::Long a_num_msg,
                                       ChatMsgHistSeq_out a_chat_msg_seq)
    throw(CORBA::SystemException, RCT::GroupServer::DataSelectionExceedsLimit)
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    // Test if the selected data range exceeds the data limit
    if(RCT_ARCHIVE_MESSAGE_LIMIT < a_num_msg) {
        
        throw RCT::GroupServer::DataSelectionExceedsLimit("RCT_EXCEPTION: Data Selection Exceeds Limit");
    }

    // Continue if there was not exception thrown  

    DatabaseServerS * l_db_server = DatabaseServerS::instance();
    ClassServerS *l_class_server = ClassServerS::instance();

    // This will hold the tuple from the db query
    DBresult* l_res = NULL;

    // Query string
    CORBA::String_var l_query;

    // Create the empty ChatMsg sequence
    a_chat_msg_seq = new ChatMsgHistSeq();

    // Get class and group id
    CORBA::String_var l_class_id = l_class_server->get_class_id_from_class_name(a_class_name);
    CORBA::String_var l_group_id = get_group_id_from_group_name(l_class_id, a_group_name);
    
    // Compose the query string
    l_query = UTIL::comp_string(SELECT_CHAT_MSG_FOR_GROUP,
                                (char*)l_group_id,
                                "'",
                                " order by rct_date asc",
                                END);

    // Execute the query
    l_res = l_db_server->exec_db_query(l_query);

    // Test if we have a valid result
    if(!l_db_server->has_result_no_tuple(l_res)) {
        
        // Get the number of tuples
        CORBA::Long l_ntuples = l_db_server->db_ntuples(l_res);

        // Get the number of fields
        CORBA::Long l_nfields = l_db_server->db_nfields(l_res);
        CORBA::Long l_start_index = 0;
            
        // Test if requested number of messages is smaller then the
        // number of tuples in the query space. Also adjust the
        // start index and end index.
        if(a_num_msg < l_ntuples) {

            l_start_index = l_ntuples - a_num_msg;
            a_chat_msg_seq->length(a_num_msg);
        }
        else {

            a_chat_msg_seq->length(l_ntuples);
        }

        // Fill in the sequence with DB data
        for(unsigned int i = l_start_index, j = 0; i < l_ntuples; i++, j++) {
            
            a_chat_msg_seq[j].chat_id = l_db_server->db_getvalue(l_res, i, 0);
            a_chat_msg_seq[j].chat_msg = l_db_server->db_get_w_value(l_res, i, 1);
            a_chat_msg_seq[j].user_id = l_db_server->db_getvalue(l_res, i, 2);
            a_chat_msg_seq[j].user_alias = l_db_server->db_getvalue(l_res, i, 3);
            a_chat_msg_seq[j].class_id = l_db_server->db_getvalue(l_res, i, 4);
            a_chat_msg_seq[j].class_name = l_db_server->db_getvalue(l_res, i, 5);
            a_chat_msg_seq[j].assembly_id = l_db_server->db_getvalue(l_res, i, 6);
            a_chat_msg_seq[j].assembly_name = l_db_server->db_getvalue(l_res, i, 7);
            a_chat_msg_seq[j].permission = l_db_server->db_getvalue(l_res, i, 8);
            a_chat_msg_seq[j].rct_date = l_db_server->db_getvalue(l_res, i, 9);
            a_chat_msg_seq[j].rct_version = l_db_server->db_getvalue(l_res, i, 10);
        }   
    }

    // Freeing memory
    l_db_server->db_clear(l_res);      
}

// IDL Method:
void
GroupServer_i::get_sound_archive_last_n(const char *a_class_name,
                                        const char *a_group_name,
                                        CORBA::Long a_num_msg,
                                        SoundMsgHistSeq_out a_sound_msg_seq)
    throw(CORBA::SystemException, RCT::GroupServer::DataSelectionExceedsLimit)
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    // Test if the selected data range exceeds the data limit
    if(RCT_ARCHIVE_MESSAGE_LIMIT < a_num_msg) {
        
        throw RCT::GroupServer::DataSelectionExceedsLimit("RCT_EXCEPTION: Data Selection Exceeds Limit");
    }

    // Continue if there was not exception thrown  

    DatabaseServerS * l_db_server = DatabaseServerS::instance();
    ClassServerS *l_class_server = ClassServerS::instance();

    // This will hold the tuple from the db query
    DBresult* l_res = NULL;

    // Query string
    CORBA::String_var l_query;

    // Create the empty SoundMsg sequence
    a_sound_msg_seq = new SoundMsgHistSeq();

    // Get class and group id
    CORBA::String_var l_class_id = l_class_server->get_class_id_from_class_name(a_class_name);
    CORBA::String_var l_group_id = get_group_id_from_group_name(l_class_id, a_group_name);
    
    // Compose the query string
    l_query = UTIL::comp_string(SELECT_SOUND_MSG_FOR_GROUP,
                                (char*)l_group_id,
                                "'",
                                " order by rct_date asc",
                                END);

    // Execute the query
    l_res = l_db_server->exec_db_query(l_query);

    // Test if we have a valid result
    if(!l_db_server->has_result_no_tuple(l_res)) {
        
        // Get the number of tuples
        CORBA::Long l_ntuples = l_db_server->db_ntuples(l_res);

        // Get the number of fields
        CORBA::Long l_nfields = l_db_server->db_nfields(l_res);
        CORBA::Long l_start_index = 0;
            
        // Test if requested number of messages is smaller then the
        // number of tuples in the query space. Also adjust the
        // start index and end index.
        if(a_num_msg < l_ntuples) {

            l_start_index = l_ntuples - a_num_msg;
            a_sound_msg_seq->length(a_num_msg);
        }
        else {

            a_sound_msg_seq->length(l_ntuples);
        }

        // Fill in the sequence with DB data
        for(unsigned int i = l_start_index, j = 0; i < l_ntuples; i++, j++) {

            a_sound_msg_seq[j].id = l_db_server->db_getvalue(l_res, i, RCT_FV_ID);
            a_sound_msg_seq[j].alias = l_db_server->db_get_w_value(l_res, i, RCT_FV_ALIAS);
            a_sound_msg_seq[j].name = l_db_server->db_getvalue(l_res, i, RCT_FV_NAME);
            a_sound_msg_seq[i].user_alias = l_db_server->db_getvalue(l_res, i, RCT_FV_USER_ALIAS);
            a_sound_msg_seq[j].mime_type = l_db_server->db_getvalue(l_res, i, RCT_FV_MIME_TYPE);
            a_sound_msg_seq[j].date = l_db_server->db_getvalue(l_res, i, RCT_FV_DATE);
        }   
    }

    // Freeing memory
    l_db_server->db_clear(l_res);      
}


// IDL Method:
void
GroupServer_i::get_ftp_archive_last_n(const char *a_class_name,
                                      const char *a_group_name,
                                      CORBA::Long a_num_msg,
                                      FtpMsgHistSeq_out a_ftp_msg_seq)
    throw(CORBA::SystemException, RCT::GroupServer::DataSelectionExceedsLimit)
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif
        // Test if the selected data range exceeds the data limit
    if(RCT_ARCHIVE_MESSAGE_LIMIT < a_num_msg) {
        
        throw RCT::GroupServer::DataSelectionExceedsLimit("RCT_EXCEPTION: Data Selection Exceeds Limit");
    }

    // Continue if there was not exception thrown  

    DatabaseServerS * l_db_server = DatabaseServerS::instance();
    ClassServerS *l_class_server = ClassServerS::instance();

    // This will hold the tuple from the db query
    DBresult* l_res = NULL;

    // Query string
    CORBA::String_var l_query;

    // Create the empty FtpMsg sequence
    a_ftp_msg_seq = new FtpMsgHistSeq();

    // Get class and group id
    CORBA::String_var l_class_id = l_class_server->get_class_id_from_class_name(a_class_name);
    CORBA::String_var l_group_id = get_group_id_from_group_name(l_class_id, a_group_name);
    
    // Compose the query string
    l_query = UTIL::comp_string(SELECT_FTP_MSG_FOR_GROUP,
                                (char*)l_group_id,
                                "'",
                                " order by rct_date asc",
                                END);

    // Execute the query
    l_res = l_db_server->exec_db_query(l_query);

    // Test if we have a valid result
    if(!l_db_server->has_result_no_tuple(l_res)) {
        
        // Get the number of tuples
        CORBA::Long l_ntuples = l_db_server->db_ntuples(l_res);

        // Get the number of fields
        CORBA::Long l_nfields = l_db_server->db_nfields(l_res);
        CORBA::Long l_start_index = 0;
            
        // Test if requested number of messages is smaller then the
        // number of tuples in the query space. Also adjust the
        // start index and end index.
        if(a_num_msg < l_ntuples) {

            l_start_index = l_ntuples - a_num_msg;
            a_ftp_msg_seq->length(a_num_msg);
        }
        else {

            a_ftp_msg_seq->length(l_ntuples);
        }

        // Fill in the sequence with DB data
        for(unsigned int i = l_start_index, j = 0; i < l_ntuples; i++, j++) {

            a_ftp_msg_seq[j].id = l_db_server->db_getvalue(l_res, i, RCT_FV_ID);
            a_ftp_msg_seq[j].alias = l_db_server->db_get_w_value(l_res, i, RCT_FV_ALIAS);
            a_ftp_msg_seq[j].name = l_db_server->db_getvalue(l_res, i, RCT_FV_NAME);
            a_ftp_msg_seq[i].user_alias = l_db_server->db_getvalue(l_res, i, RCT_FV_USER_ALIAS);
            a_ftp_msg_seq[j].mime_type = l_db_server->db_getvalue(l_res, i, RCT_FV_MIME_TYPE);
            a_ftp_msg_seq[j].date = l_db_server->db_getvalue(l_res, i, RCT_FV_DATE);
        }   
    }

    // Freeing memory
    l_db_server->db_clear(l_res);      
}


// Local Method:
char*
GroupServer_i::get_group_id_from_group_name(const char *a_class_id,
                                            const char *a_group_name)
{
	
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DatabaseServerS *l_db_server = DatabaseServerS::instance();
    
    CORBA::String_var l_group_id;

    // Make the group name safe for db query
    char* l_group_name_esc =
        l_db_server->db_escape_string(a_group_name,
                                      strlen(a_group_name));
    
    // This will hold the tuple from the db query
    DBresult* l_res = NULL;

    // Compose the query string
    CORBA::String_var l_query;
    l_query = UTIL::comp_string(SELECT_GROUP_ID,
                                l_group_name_esc,
                                "'",
                                " and class_id= '",
                                a_class_id,
                                "'",
                                END);

    // Execute the query
    l_res = l_db_server->exec_db_query(l_query);

    // Test if we have a valid result
    if(l_db_server->has_result_one_tuple(l_res)) {
        
        l_group_id = l_db_server->db_getvalue(l_res, 0, 0);
    }
    else {

        cerr << "ERROR: Did not get group id from group name" << endl;
    }

    // Freeing memory
    l_db_server->db_clear(l_res);

    // Cleaning up escaped group name
    delete l_group_name_esc;
    
    return l_group_id._retn();
}


// Local Method:
char*
GroupServer_i::get_group_mgr_from_group_id(const char *a_group_id)
{
	
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DatabaseServerS *l_db_server = DatabaseServerS::instance();
    
    CORBA::String_var l_group_mgr;
    
    // This will hold the tuple from the db query
    DBresult* l_res = NULL;

    // Compose the query string
    CORBA::String_var l_query;
    l_query = UTIL::comp_string(SELECT_GROUP,
                                a_group_id,
                                "'",
                                END);

    // Execute the query
    l_res = l_db_server->exec_db_query(l_query);

    // Test if we have a valid result
    if(l_db_server->has_result_one_tuple(l_res)) {

        l_group_mgr = l_db_server->db_getvalue(l_res, 0, RCT_GROUP_MGR);
    }
    else {

        cerr << "ERROR: Did not get group manager from group id" << endl;
    }

    // Freeing memory
    l_db_server->db_clear(l_res);
    
    return l_group_mgr._retn();
}


// Local Method
Group*
GroupServer_i::get_group_from_group_id(const char *a_group_id)
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif
    
    DatabaseServerS *l_db_server = DatabaseServerS::instance();
    
    Group_var l_group= new Group();
    
    // This will hold the tuple from the db query
    DBresult* l_res = NULL;

    // Compose the query string
    CORBA::String_var l_query;
    l_query = UTIL::comp_string(SELECT_GROUP,
                                a_group_id,
                                "'",
                                END);

    // Execute the query
    l_res = l_db_server->exec_db_query(l_query);

    // Test if we have a valid result
    if(l_db_server->has_result_one_tuple(l_res)) {

        l_group->group_id = l_db_server->db_getvalue(l_res, 0, RCT_GROUP_ID);
        l_group->name = l_db_server->db_getvalue(l_res, 0, RCT_GROUP_NAME);
        l_group->class_id = l_db_server->db_getvalue(l_res, 0, RCT_GROUP_CLASS_ID);
        l_group->permission = l_db_server->db_getvalue(l_res, 0, RCT_GROUP_PER);
        l_group->manager = l_db_server->db_getvalue(l_res, 0, RCT_GROUP_MGR);
        l_group->date = l_db_server->db_getvalue(l_res, 0, RCT_GROUP_DATE);
    }
    else {
        
        cerr << "ERROR: Did not get group from group id!" << endl;
    }

    // Freeing memory
    l_db_server->db_clear(l_res);
       
    return l_group._retn();
}



// Local Method:
CORBA::Boolean
GroupServer_i::does_group_exist(const char *a_class_name,
                                const char *a_group_name)
{
	
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    // Gettin all the groups from the given class name
    GroupSeq_var l_group_seq;
    get_groups_from_class_name(a_class_name, l_group_seq);

    for(int k = 0; k < l_group_seq->length(); k++) {

        if(0 == strcmp(l_group_seq[k].name, a_group_name)) {

            // We got a match and thus return true
            return 1;
        }
    }

    // We looped through the groups sequence and did not
    // find a match
    return 0;
}


// Local Method:
void
GroupServer_i::group_class_online_users_notification(const char *a_class_name,
                                                     const char *a_group_name,
                                                     const char *a_alias,
                                                     CORBA::ULong a_msg_type)
{
	
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    UserServerS *l_user_server = UserServerS::instance();
    SessionServer_i *l_session_server = SessionServer_i::instance();

    // Creating the notification message
    Message l_msg;
    l_msg.version = RCT_VERSION;
    l_msg.user_alias = a_alias;
    l_msg.permission = UNCLASSIFIED;
    l_msg.class_name = a_class_name;
    l_msg.group_name = a_group_name;
    l_msg.type = a_msg_type;
    
    // Get all the online users relevant to the class
    UserSeq_var l_user_seq;
    l_user_server->get_online_users_from_class_name(a_class_name, l_user_seq);

    // Now we send the message to each user event channel
    for(unsigned int i = 0; i < l_user_seq->length(); i++) {

        // Note: the user_id[1] accesses the user_id string's numberic part bypassing the "U" of
        // the user_id, thus if we have U123, we just convert 123 into and integer
        l_session_server->push_msg(l_msg, atoi(&l_user_seq[i].user_id[USER_ID_OFFSET]));
    }
}


// Local Method:
void
GroupServer_i::group_members_notification(const char *a_class_name,
                                          const char *a_group_name,
                                          const char *a_alias,
                                          CORBA::ULong a_msg_type)
{
	
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    UserServerS *l_user_server = UserServerS::instance();
    SessionServer_i *l_session_server = SessionServer_i::instance();
    
    
    // Creating the notification message
    Message l_msg;
    l_msg.version = RCT_VERSION;
    l_msg.user_alias = a_alias;
    l_msg.permission = UNCLASSIFIED;
    l_msg.class_name = a_class_name;
    l_msg.group_name = a_group_name;
    l_msg.type = a_msg_type;

    // Get all the online users relevant to the class
    UserSeq_var l_user_seq;
    l_user_server->get_users_from_group_name(a_class_name, a_group_name, l_user_seq);

    // Now we send the message to each user event channel
    for(unsigned int i = 0; i < l_user_seq->length(); i++) {

        // Note: the user_id[1] accesses the user_id string's numberic part bypassing the "U" of
        // the user_id, thus if we have U123, we just convert 123 into and integer
        l_session_server->push_msg(l_msg, atoi(&l_user_seq[i].user_id[USER_ID_OFFSET]));
    }
}


// Local Method:
void
GroupServer_i::choose_new_group_manager(const char *a_class_name,
                                        const char *a_group_name,
                                        const char *a_user_id,
                                        const char *a_user_alias)
{
	
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    SessionServer_i *l_session_server = SessionServer_i::instance();

    // Creating the message
    Message l_msg;
    l_msg.version = RCT_VERSION;
    l_msg.user_alias = a_user_alias;
    l_msg.permission = UNCLASSIFIED;
    l_msg.class_name = a_class_name;
    l_msg.group_name = a_group_name;
    l_msg.type = GROUP_CHOOSE_NEW_MGR;

    // Note: the user_id[1] accesses the user_id string's numeric part bypassing the "U" of
    // the user_id, thus if we have U123, we just convert 123 into and integer
    l_session_server->push_msg(l_msg, atoi(&a_user_id[USER_ID_OFFSET]));
}


// Local Method:
void
GroupServer_i::remove_group_member(const char *a_class_id,
                                   const char *a_group_id,
                                   const char *a_user_id)
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif
    
    DatabaseServerS *l_db_server = DatabaseServerS::instance();
    
    // Compose the trans string
    CORBA::String_var l_trans;
    l_trans = UTIL::comp_string(DEL_MEMBER_GROUP,
                                a_user_id,
                                "'",
                                " and group_id='",
                                a_group_id,
                                "' ",
                                END);

    // Execute the transaction
    l_db_server->exec_db_delete(l_trans);

}


CORBA::Long
GroupServer_i::get_number_of_members_in_group(const char *a_class_id,
                                              const char *a_group_id)
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DatabaseServerS *l_db_server = DatabaseServerS::instance();
    
    // This will hold the tuple from the db query
    DBresult* l_res = NULL;

    // Compose the query string
    CORBA::String_var l_query;
    l_query = UTIL::comp_string(SELECT_MEMBERS_IN_GROUP,
                                (char*)a_group_id,
                                "'",
                                END);

    // Execute the query
    l_res = l_db_server->exec_db_query(l_query);
    
    CORBA::Long l_n_members = l_db_server->db_ntuples(l_res);
    
    // Freeing memory
    l_db_server->db_clear(l_res);
            
    return l_n_members;
}


// Local Method:
void
GroupServer_i::remove_group(const char *a_group_id)
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DatabaseServerS *l_db_server = DatabaseServerS::instance();
    
    // Move all chat messages from this group to the default group
    // Compose the update string
    CORBA::String_var l_update;
    l_update = UTIL::comp_string(UPDATE_CHAT_GROUP_MSGS,
                                 a_group_id,
                                 "'",
                                 END);

    // Execute the transaction
    l_db_server->exec_db_update(l_update);

    // Move all the file related messages from this group to the default group
    // This affects the following modules:
    // Sound, Ftp
    // Compose the update string
    l_update = UTIL::comp_string(UPDATE_FILES_GROUP_MSGS,
                                 a_group_id,
                                 "'",
                                 END);

    // Execute transaction
    l_db_server->exec_db_update(l_update);

    // Move all the textpads from this group to the default group
    // Compose the update string
    l_update = UTIL::comp_string(UPDATE_TEXTPAD_GROUP_MSGS,
                                 a_group_id,
                                 "'",
                                 END);

    // Execute transaction
    l_db_server->exec_db_update(l_update);

    // Execute transaction
    l_db_server->exec_db_update(l_update);
    
    // Now remove this group from the database
    // Compose the trans string
    CORBA::String_var l_trans;
    l_trans = UTIL::comp_string(DEL_GROUP,
                                (char*)a_group_id,
                                "'",
                                END);

    // Execute the query
    l_db_server->exec_db_delete(l_trans);
}


// Local Method:
// Returning a sequence of groups the user is member of
void
GroupServer_i::get_groups_user_is_member_of(const char *a_user_id,
                                            GroupSeq_out a_group_seq)
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DatabaseServerS *l_db_server = DatabaseServerS::instance();
    
    // Create the empty group sequence
    a_group_seq = new GroupSeq();
    
    // This will hold the tuple from the db query
    DBresult* l_res = NULL;

    // Compose the query string
    CORBA::String_var l_query;
    l_query = UTIL::comp_string(SELECT_GROUPS_USER_IS_MEMBER_OF,
                                a_user_id,
                                "'",
                                END);

    // Execute the query
    l_res = l_db_server->exec_db_query(l_query);

    // Test if we have a valid result
    if(!l_db_server->has_result_no_tuple(l_res)) {
        
        // Creating vars to hold the number of tuples
        CORBA::Long l_ntuples = l_db_server->db_ntuples(l_res);

        // Setting the size of the group sequence
        a_group_seq->length(l_ntuples);

        for(unsigned int i = 0; i < l_ntuples; i++) {

            a_group_seq[i].group_id = l_db_server->db_getvalue(l_res, i, RCT_GROUP_ID);
            a_group_seq[i].name = l_db_server->db_getvalue(l_res, i, RCT_GROUP_NAME);
            a_group_seq[i].class_id = l_db_server->db_getvalue(l_res, i, RCT_GROUP_CLASS_ID);
            a_group_seq[i].permission = l_db_server->db_getvalue(l_res, i, RCT_GROUP_PER);
            a_group_seq[i].date = l_db_server->db_getvalue(l_res, i, RCT_GROUP_DATE);
            a_group_seq[i].manager = l_db_server->db_getvalue(l_res, i, RCT_GROUP_MGR);        
        }
    }
    
    // Freeing memory
    l_db_server->db_clear(l_res);      
}


// Local Method:
// Returning a sequence of groups the user is member of
CORBA::Boolean
GroupServer_i::has_group_manager(const char *a_group_id,
                                 CORBA::String_out a_user_id)
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DatabaseServerS *l_db_server = DatabaseServerS::instance();
    
    // This will hold the tuple from the db query
    DBresult* l_res = NULL;

    // Compose the query string
    CORBA::String_var l_query;
    l_query = UTIL::comp_string(SELECT_HAS_GROUP_MANAGER,
                                a_group_id,
                                "'",
                                END);

    // Execute the query
    l_res = l_db_server->exec_db_query(l_query);

    // Test if we have a valid result
    if(l_db_server->has_result_no_tuple(l_res)) {

        // Freeing memory
        l_db_server->db_clear(l_res);
        
        return 0;
    }
    else {

        a_user_id = l_db_server->db_getvalue(l_res, 0, 0);

        // Freeing memory
        l_db_server->db_clear(l_res);
        
        return 1;
    }
}

// Local Method:
// Get one user from the group
CORBA::Boolean
GroupServer_i::get_one_user_from_group(const char *a_group_id,
                                       CORBA::String_out a_user_id)
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    CORBA::String_var l_user_id;

    DatabaseServerS *l_db_server = DatabaseServerS::instance();
    
    // This will hold the tuple from the db query
    DBresult* l_res = NULL;

    // Compose the query string
    CORBA::String_var l_query;
    l_query = UTIL::comp_string(SELECT_ONE_GROUP_MEMBER,
                                a_group_id,
                                "'",
                                END);

    // Execute the query
    l_res = l_db_server->exec_db_query(l_query);

    // Test if we have a valid result
    if(l_db_server->has_result_no_tuple(l_res)) {

        // Freeing memory
        l_db_server->db_clear(l_res);
        
        return 0;
    }
    else {

        a_user_id = l_db_server->db_getvalue(l_res, 0, 0);

        // Freeing memory
        l_db_server->db_clear(l_res);
        
        return 1;
    }

    return l_user_id._retn();
}


// Local Method:
void
GroupServer_i::set_manager_for_group(const char *a_user_id,
                                     const char *a_group_id)
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DatabaseServerS *l_db_server = DatabaseServerS::instance();

    // Compose the trans string
    CORBA::String_var l_trans;
    l_trans = UTIL::comp_string(UPDATE_MANAGER_GROUP,
                                a_user_id,
                                "'",
                                " where group_id='",
                                a_group_id,
                                "' ",
                                END);

    // Execute the update
    l_db_server->exec_db_update(l_trans);
}








