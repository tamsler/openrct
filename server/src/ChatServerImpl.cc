/* $Id: ChatServerImpl.cc,v 1.15 2003/05/08 20:34:43 thomas Exp $ */

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

#include "ChatServerImpl.h"


// IDL Method:
void
ChatServer_i::send(const CORBA::WChar *a_chat_msg,
                   const char *a_version,
                   const char *a_user_id,
                   const char *a_user_alias,
                   ObjPermission a_permission,
                   const char *a_class_name,
                   const char *a_assembly_name,
                   CORBA::Long a_type)
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif
    
    UserServerS *l_user_server = UserServerS::instance();
    SessionServer_i *l_session_server = SessionServer_i::instance();
    ClassServerS *l_class_server = ClassServerS::instance();
    TeamServerS *l_team_server = TeamServerS::instance();
    GroupServerS *l_group_server = GroupServerS::instance();
    DatabaseServerS *l_db_server = DatabaseServerS::instance();
    
    // We need to generate the chat object index
    CORBA::String_var l_chat_id;
    CORBA::String_var l_chat_index;

    l_chat_index = l_db_server->get_chat_index();
    l_chat_id = UTIL::comp_string(RCT_CHAT,
                                  (char *)l_chat_index,
                                  END);

    // Creating the chat message from client provided values
    ChatMessage l_chat_msg;
    l_chat_msg.base_msg.version = a_version;
    l_chat_msg.base_msg.user_alias = a_user_alias;
    l_chat_msg.base_msg.class_name = a_class_name;
    l_chat_msg.base_msg.permission = a_permission;
    l_chat_msg.base_msg.type = a_type;
    l_chat_msg.data = a_chat_msg;

    
    // Get all the relevant users for team or group 
    UserIdSeq_var l_user_id_seq;

    // Check if we deal with a team message
    if(CHAT_TEAM_MSG == a_type) {

        l_chat_msg.base_msg.team_name = a_assembly_name;
        l_user_server->get_online_user_ids_from_team_name(a_class_name, a_assembly_name, l_user_id_seq);
    }
    // Check if we deal with a group message
    else if(CHAT_GROUP_MSG == a_type) {

        l_chat_msg.base_msg.group_name = a_assembly_name;
        l_user_server->get_user_ids_from_group_name(a_class_name, a_assembly_name, l_user_id_seq);
    }
    else {

        cerr << "ERROR: Wrong message type!" << endl;
    }

    // Now we send the message to each user event channel
    for (int i = 0; i < l_user_id_seq->length(); i++) {

        // Note: the [USER_ID_OFFSET] accesses the user_id string's numeric part bypassing the "U" of
        // the user_id, thus if we have U123, we just convert 123 into an integer
        l_session_server->push_chat_msg(l_chat_msg, atoi(&l_user_id_seq[i][USER_ID_OFFSET]));
    }
            
    // After sending the chat message, we need to log it in the database
    
    CORBA::String_var l_trans;
    CORBA::String_var l_per = UTIL::int_to_str(a_permission);

    // Getting the class_id from class_name
    CORBA::String_var l_class_id;
    l_class_id = l_class_server->get_class_id_from_class_name(a_class_name);
    
    // Getting the string length of the chat message
    int l_str_size = wcstombs(NULL, a_chat_msg, 0) + 1;
    
    // Create the multi byte chat message string
    char l_chat_msg_multi_byte[l_str_size];

    if(-1 <= wcstombs(l_chat_msg_multi_byte, a_chat_msg, l_str_size)) {
        
        cerr << "ERROR: Could not convert a character!" << endl;
    }

    // Make the chat message safe fore db entry
    char* l_chat_msg_multi_byte_esc =
        l_db_server->db_escape_wstring(l_chat_msg_multi_byte,
                                       l_str_size - 1);
    
    // Check if we deal with a team message
    if(CHAT_TEAM_MSG == a_type) {

        
        // Getting the team_id from team_name
        CORBA::String_var l_team_id;
        l_team_id = l_team_server->get_team_id_from_team_name(l_class_id,
                                                              a_assembly_name);

        l_trans = UTIL::comp_string(INS_CHAT_LOG_TEAM,
                                    (char *)l_chat_id,
                                    "','",
                                    l_chat_msg_multi_byte_esc,
                                    "','",
                                    a_user_id,
                                    "','",
                                    (char *)l_class_id,
                                    "','",
                                    (char *)l_team_id,
                                    "','",
                                    (char *)l_per,
                                    "','",
                                    "now",
                                    "','",
                                    RCT_VERSION,
                                    "');",
                                    END);
    }
    // Check if we deal with a group message
    else if(CHAT_GROUP_MSG == a_type) {

        // Getting the team_id from team_name
        CORBA::String_var l_group_id;
        l_group_id = l_group_server->get_group_id_from_group_name(l_class_id,
                                                                  a_assembly_name);

        l_trans = UTIL::comp_string(INS_CHAT_LOG_GROUP,
                                    (char *)l_chat_id,
                                    "','",
                                    l_chat_msg_multi_byte_esc,
                                    "','",
                                    a_user_id,
                                    "','",
                                    (char *)l_class_id,
                                    "','",
                                    (char *)l_group_id,
                                    "','",
                                    (char *)l_per,
                                    "','",
                                    "now",
                                    "','",
                                    RCT_VERSION,
                                    "');",
                                    END);
    }  

    // Insert consumed chat message into the DB
    l_db_server->exec_db_insert(l_trans);

    // Cleaning up escaped chat message
    delete l_chat_msg_multi_byte_esc;
}

















    
