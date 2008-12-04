/* $Id: TeamServerImpl.cc,v 1.35 2003/05/08 20:34:43 thomas Exp $ */

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

#include "TeamServerImpl.h"


// IDL Method:
void
TeamServer_i::get_all_teams(TeamSeq_out a_team_seq)
{
	
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DatabaseServerS * l_db_server = DatabaseServerS::instance();

    // This will hold the tuple from the db query
    DBresult* l_res = NULL;

    // Create the empty team sequence
    a_team_seq = new TeamSeq();

    // Compose the query string
    CORBA::String_var l_query;
    l_query = CORBA::string_dup(SELECT_TEAMS);

    // Execute the query
    l_res = l_db_server->exec_db_query(l_query);

    // Test if we have a valid result
    if(!l_db_server->has_result_no_tuple(l_res)) {

        // Creating vars to hold the number of tuples
        CORBA::Long l_tuples = l_db_server->db_ntuples(l_res);

        // Setting the size of the team sequence
        a_team_seq->length(l_tuples);
        
        // Now we can access the tuples and populate
        // the team sequence
        for(unsigned int i = 0; i < l_tuples; i++) {

            a_team_seq[i].team_id = l_db_server->db_getvalue(l_res, i, RCT_TEAM_ID);
            a_team_seq[i].name = l_db_server->db_getvalue(l_res, i, RCT_TEAM_NAME);
            a_team_seq[i].class_id = l_db_server->db_getvalue(l_res, i, RCT_TEAM_CLASS_ID);
            a_team_seq[i].permission = l_db_server->db_getvalue(l_res, i, RCT_TEAM_PER);
            a_team_seq[i].active_status = l_db_server->db_getvalue(l_res, i, RCT_TEAM_AS);
            a_team_seq[i].date = l_db_server->db_getvalue(l_res, i, RCT_TEAM_DATE);
            a_team_seq[i].manager = l_db_server->db_getvalue(l_res, i, RCT_TEAM_MGR);
        }
    }

    // Freeing memory
    l_db_server->db_clear(l_res);
}


// IDL Method:
void
TeamServer_i::get_teams_from_user_and_class(const char *a_user_id1,
                                            const char *a_user_id2,
                                            const char *a_class_id,
                                            TeamSeq_out a_team_seq)
{
	
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DatabaseServerS * l_db_server = DatabaseServerS::instance();

    // This will hold the tuple from the db query
    DBresult* l_res = NULL;

    // Create the empty team sequence
    a_team_seq = new TeamSeq();
    
    // Compose the query string
    CORBA::String_var l_query;
    l_query = UTIL::comp_string(SELECT_TEAMS_FOR_USER_IN_CLASS,
                                a_user_id1,
                                "' and M2.user_id='",
                                a_user_id2,
                                "' and T.class_id='",
                                a_class_id,
                                "'",
                                END);

    // Execute the query
    l_res = l_db_server->exec_db_query(l_query);

    // Test if we have a valid result
    if(!l_db_server->has_result_no_tuple(l_res)) {

        // Creating vars to hold the number of tuples
        CORBA::Long l_tuples = l_db_server->db_ntuples(l_res);

        // Setting the size of the team sequence
        a_team_seq->length(l_tuples);
        
        // Now we can access the tuples and populate
        // the team sequence
        for(unsigned int i = 0; i < l_tuples; i++) {

            a_team_seq[i].team_id = l_db_server->db_getvalue(l_res, i, RCT_TEAM_ID);
            a_team_seq[i].name = l_db_server->db_getvalue(l_res, i, RCT_TEAM_NAME);
            a_team_seq[i].class_id = l_db_server->db_getvalue(l_res, i, RCT_TEAM_CLASS_ID);
            a_team_seq[i].permission = l_db_server->db_getvalue(l_res, i, RCT_TEAM_PER);
            a_team_seq[i].active_status = l_db_server->db_getvalue(l_res, i, RCT_TEAM_AS);
            a_team_seq[i].date = l_db_server->db_getvalue(l_res, i, RCT_TEAM_DATE);
            a_team_seq[i].manager = l_db_server->db_getvalue(l_res, i, RCT_TEAM_MGR);
        }
    }

    // Freeing memory
    l_db_server->db_clear(l_res);
}


// IDL Method:
void
TeamServer_i::get_teams_from_class_name(const char *a_class_name, TeamSeq_out a_team_seq)
{
	
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DatabaseServerS * l_db_server = DatabaseServerS::instance();

    // Make the class name safe fore db entry
    char* l_class_name_esc =
        l_db_server->db_escape_string(a_class_name,
                                      strlen(a_class_name));
    
    // This will hold the tuple from the db query
    DBresult* l_res = NULL;

    // Create the empty team sequence
    a_team_seq = new TeamSeq();

    // Compose the query string
    CORBA::String_var l_query;
    l_query = UTIL::comp_string(SELECT_TEAMS_FOR_CLASS,
                                l_class_name_esc,
                                "')",
                                END);

    // Execute the query
    l_res = l_db_server->exec_db_query(l_query);

    // Test if we have a valid result
    if(!l_db_server->has_result_no_tuple(l_res)) {

        // Creating vars to hold the number of tuples
        CORBA::Long l_tuples = l_db_server->db_ntuples(l_res);

        // Setting the size of the team sequence
        a_team_seq->length(l_tuples);
        
        // Now we can access the tuples and populate
        // the team sequence
        for(unsigned int i = 0; i < l_tuples; i++) {

            a_team_seq[i].team_id = l_db_server->db_getvalue(l_res, i, RCT_TEAM_ID);
            a_team_seq[i].name = l_db_server->db_getvalue(l_res, i, RCT_TEAM_NAME);
            a_team_seq[i].class_id = l_db_server->db_getvalue(l_res, i, RCT_TEAM_CLASS_ID);
            a_team_seq[i].permission = l_db_server->db_getvalue(l_res, i, RCT_TEAM_PER);
            a_team_seq[i].active_status = l_db_server->db_getvalue(l_res, i, RCT_TEAM_AS);
            a_team_seq[i].date = l_db_server->db_getvalue(l_res, i, RCT_TEAM_DATE);
            a_team_seq[i].manager = l_db_server->db_getvalue(l_res, i, RCT_TEAM_MGR);
        }
    }

    // Freeing memory
    l_db_server->db_clear(l_res);

    // Cleaning up escaped name
    delete l_class_name_esc;
}

// IDL Method:
CORBA::Boolean
TeamServer_i::join_team(const char *a_class_name,
                        const char *a_team_name,
                        const char *a_user_id,
                        const char *a_user_alias)
{
	
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DatabaseServerS * l_db_server = DatabaseServerS::instance();

    // Make the team and class name safe for db query
    char* l_class_name_esc =
        l_db_server->db_escape_string(a_class_name,
                                      strlen(a_class_name));
    char* l_team_name_esc =
        l_db_server->db_escape_string(a_team_name,
                                      strlen(a_team_name));

    // This will hold the tuple from the db query
    DBresult* l_res = NULL;

    
    // Compose the query string
    CORBA::String_var l_query;
    l_query = UTIL::comp_string(SELECT_MEMBER_TEAMS,
                                a_user_id,
                                "' and team_name='",
                                l_team_name_esc,
                                "' and class_name='",
                                l_class_name_esc,
                                "'",
                                END);

    // Enter critical section
    RctMutex::team.lock();
    
    // Execute the query
    l_res = l_db_server->exec_db_query(l_query);

    // Now if the user is a member of the team update the relevant tables in db
    // And send user joins team notification to all other related online users
    if (l_db_server->has_result_one_tuple(l_res)) {

        // User is member of the team
        CORBA::String_var l_team_id = l_db_server->db_getvalue(l_res, 0 , RCT_MTV_TEAM_ID);
        update_user_online_in_team_tables(l_team_id,
                                          a_user_id);
            
        // Send user joins team notification to all other related online users 
        user_joins_team_notification(a_class_name,
                                     a_team_name,
                                     a_user_alias);

        // Exit critical section
        RctMutex::team.unlock();
        
        // Freeing memory
        l_db_server->db_clear(l_res);

        // Clean up escaped names
        delete l_class_name_esc;
        delete l_team_name_esc;
        
        return 1;
                        
    } else {
        
        // User is not member of any team

        // Exit critical section
        RctMutex::team.unlock();
        
        // Freeing memory
        l_db_server->db_clear(l_res);

        // Clean up escaped names
        delete l_class_name_esc;
        delete l_team_name_esc;
        
        return 0;
    }
}


// IDL Method:
void
TeamServer_i::exit_team(const char *a_class_name,
                        const char *a_team_name,
                        const char *a_user_id,
                        const char *a_user_alias)
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DatabaseServerS * l_db_server = DatabaseServerS::instance();
    ClassServerS *l_class_server = ClassServerS::instance();
    ControlServerS *l_control_server = ControlServerS::instance();

    // Get class and team id
    CORBA::String_var l_class_id = l_class_server->get_class_id_from_class_name(a_class_name);
    CORBA::String_var l_team_id = get_team_id_from_team_name(l_class_id, a_team_name);

    
    // Test if user is in specific queue
    // Add here any additional test for each controlled module
    // CASE TEXTPAD:
    if(l_control_server->is_user_in_queue(l_class_id,
                                          l_team_id,
                                          a_user_id,
                                          MODULE_TEXTPAD)) {

        // Testing for textpad queues
        l_control_server->exit_request(a_class_name,
                                       a_team_name,
                                       a_user_alias,
                                       a_user_id,
                                       MODULE_TEXTPAD,
                                       CONTROL_EXIT_TEAM);
    }
    
    // Now delete the user from rct_active_user+team table
    // Compose the trans string
    CORBA::String_var l_trans;
    l_trans = UTIL::comp_string(DEL_USER_ACTIVE_FROM_TEAM,
                                (char*)a_user_id,
                                "' and team_id='",
                                (char*)l_team_id,
                                "'",
                                END);

    // Execute the trans
    l_db_server->exec_db_delete(l_trans);

    // Now send user_exits_team notification to all other online
    // users in the class
    user_exits_team_notification(a_class_name,
                                 a_team_name,
                                 a_user_alias);

}


// IDL Method:
CORBA::Boolean
TeamServer_i::is_user_active_in_team(const char *a_class_name,
                                     const char *a_team_name,
                                     const char *a_user_id,
                                     const char *a_user_alias)
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DatabaseServerS * l_db_server = DatabaseServerS::instance();

    CORBA::Boolean l_user_active = 0;
    ClassServerS *l_class_server = ClassServerS::instance();

    // Get class id and team id
    CORBA::String_var l_class_id = l_class_server->get_class_id_from_class_name(a_class_name);
    CORBA::String_var l_team_id = get_team_id_from_team_name(l_class_id, a_team_name);

    // This will hold the tuple from the db query
    DBresult* l_res = NULL;
    
    // Compose the query string
    CORBA::String_var l_query;
    l_query = UTIL::comp_string(SELECT_IS_USER_ACTIVE_IN_TEAM,
                                (char*)a_user_id,
                                "' and team_id='",
                                (char*)l_team_id,
                                "'",
                                END);

    // Execute the query        
    l_res = l_db_server->exec_db_query(l_query);
        
    if (l_db_server->has_result_one_tuple(l_res)) {

        l_user_active = 1;
    } 

    // Freeing memory
    l_db_server->db_clear(l_res);

    return l_user_active;
}


// IDL Method:
// Returning a team sequence of teams the user is active in
void
TeamServer_i::get_teams_user_is_active_in(const char *a_user_id,
                                          TeamSeq_out a_team_seq)
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DatabaseServerS * l_db_server = DatabaseServerS::instance();

    // Create the empty team sequence
    a_team_seq = new TeamSeq();
    
    // This will hold the tuple from the db query
    DBresult* l_res = NULL;

    // Compose the query string
    CORBA::String_var l_query;
    l_query = UTIL::comp_string(SELECT_TEAMS_USER_IS_ACTIVE_IN,
                                a_user_id,
                                "'",
                                END);

    // Execute the query
    l_res = l_db_server->exec_db_query(l_query);

    // Test if we have a valid result
    if(!l_db_server->has_result_no_tuple(l_res)) {
        
        // Creating vars to hold the number of tuples
        CORBA::Long l_ntuples = l_db_server->db_ntuples(l_res);

        // Setting the size of the team sequence
        a_team_seq->length(l_ntuples);

        for(unsigned int i = 0; i < l_ntuples; i++) {

            a_team_seq[i].team_id = l_db_server->db_getvalue(l_res, i, RCT_TEAM_ID);
            a_team_seq[i].name = l_db_server->db_getvalue(l_res, i, RCT_TEAM_NAME);
            a_team_seq[i].class_id = l_db_server->db_getvalue(l_res, i, RCT_TEAM_CLASS_ID);
            a_team_seq[i].permission = l_db_server->db_getvalue(l_res, i, RCT_TEAM_PER);
            a_team_seq[i].active_status = l_db_server->db_getvalue(l_res, i, RCT_TEAM_AS);
            a_team_seq[i].date = l_db_server->db_getvalue(l_res, i, RCT_TEAM_DATE);
            a_team_seq[i].manager = l_db_server->db_getvalue(l_res, i, RCT_TEAM_MGR);
        }
    }
            
    // Freeing memory
    l_db_server->db_clear(l_res);      
}


// IDL Method:
void
TeamServer_i::get_chat_archive_time_range(const char *a_class_name,
                                          const char *a_team_name,
                                          const char *a_from_date,
                                          const char *a_to_date,
                                          ChatMsgHistSeq_out a_chat_msg_seq)
    throw(CORBA::SystemException, RCT::TeamServer::DataSelectionExceedsLimit)
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DatabaseServerS * l_db_server = DatabaseServerS::instance();
    ClassServerS *l_class_server = ClassServerS::instance();

    // Create the empty ChatMsg sequence
    a_chat_msg_seq = new ChatMsgHistSeq();

    // First we check if the dates are in a valid format

    // Check FROM DATE
    if(!is_date_valid(a_from_date)) {

        throw RCT::TeamServer::DataSelectionExceedsLimit("RCT_EXCEPTION: Wrong from date format");
    }

    // Check TO DATE
    if(!is_date_valid(a_to_date)) {

        throw RCT::TeamServer::DataSelectionExceedsLimit("RCT_EXCEPTION: Wrong to date format");
    }
    // Done checking dates
    
    // This will hold the tuple from the db query
    DBresult* l_res = NULL;

    // Query string
    CORBA::String_var l_query;
    CORBA::String_var l_query_count;
    
    // Get class and team id
    CORBA::String_var l_class_id = l_class_server->get_class_id_from_class_name(a_class_name);
    CORBA::String_var l_team_id = get_team_id_from_team_name(l_class_id, a_team_name);
    
    // Compose the count query string
    l_query_count = UTIL::comp_string(COUNT_CHAT_MSG_FOR_TEAM,
                                      (char*)l_team_id,
                                      "'",
                                      " and rct_date >= '",
                                      a_from_date,
                                      "' and rct_date <= '",
                                      a_to_date,
                                      "'",
                                      END);

    CORBA::Long l_num_tuples = l_db_server->get_num_tuples(l_query_count);

    // Test if the selected data range exceeds the data limit
    if(RCT_ARCHIVE_MESSAGE_LIMIT < l_num_tuples) {
        
        throw RCT::TeamServer::DataSelectionExceedsLimit("RCT_EXCEPTION: Data Selection Exceeds Limit");
    }

    // Continue if there was not exception thrown 
    
    // Compose the count query string
    l_query = UTIL::comp_string(SELECT_CHAT_MSG_FOR_TEAM,
                                (char*)l_team_id,
                                "'",
                                " and rct_date >= '",
                                a_from_date,
                                "' and rct_date <= '",
                                a_to_date,
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
            
        // Setting the size of the chat msg sequence
        a_chat_msg_seq->length(l_ntuples);

        // Fill in the sequence with DB data
        for(unsigned int i = 0; i < l_ntuples; i++) {
            
            a_chat_msg_seq[i].chat_id = l_db_server->db_getvalue(l_res, i, RCT_CHATV_ID);
            a_chat_msg_seq[i].chat_msg = l_db_server->db_get_w_value(l_res, i, RCT_CHATV_MSG);
            a_chat_msg_seq[i].user_id = l_db_server->db_getvalue(l_res, i, RCT_CHATV_USER_ID);
            a_chat_msg_seq[i].user_alias = l_db_server->db_getvalue(l_res, i, RCT_CHATV_USER_ALIAS);
            a_chat_msg_seq[i].class_id = l_db_server->db_getvalue(l_res, i, RCT_CHATV_CLASS_ID);
            a_chat_msg_seq[i].class_name = l_db_server->db_getvalue(l_res, i, RCT_CHATV_CLASS_NAME);
            a_chat_msg_seq[i].assembly_id = l_db_server->db_getvalue(l_res, i, RCT_CHATV_TEAM_ID);
            a_chat_msg_seq[i].assembly_name = l_db_server->db_getvalue(l_res, i, RCT_CHATV_TEAM_NAME);
            a_chat_msg_seq[i].permission = l_db_server->db_getvalue(l_res, i, RCT_CHATV_PER);
            a_chat_msg_seq[i].rct_date = l_db_server->db_getvalue(l_res, i, RCT_CHATV_DATE);
            a_chat_msg_seq[i].rct_version = l_db_server->db_getvalue(l_res, i, RCT_CHATV_VER);
        }   
    }

    // Freeing memory
    l_db_server->db_clear(l_res);      
}

// IDL Method:
void
TeamServer_i::get_sound_archive_time_range(const char *a_class_name,
                                           const char *a_team_name,
                                           const char *a_from_date,
                                           const char *a_to_date,
                                           SoundMsgHistSeq_out a_snd_msg_seq)
    throw(CORBA::SystemException, RCT::TeamServer::DataSelectionExceedsLimit)
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DatabaseServerS * l_db_server = DatabaseServerS::instance();
    ClassServerS *l_class_server = ClassServerS::instance();

    // Create the empty SndMsg sequence
    a_snd_msg_seq = new SoundMsgHistSeq();

    // First we check if the dates are in a valid format

    // Check FROM DATE
    if(!is_date_valid(a_from_date)) {

        throw RCT::TeamServer::DataSelectionExceedsLimit("RCT_EXCEPTION: Wrong from date format");
    }

    // Check TO DATE
    if(!is_date_valid(a_to_date)) {

        throw RCT::TeamServer::DataSelectionExceedsLimit("RCT_EXCEPTION: Wrong to date format");
    }
    // Done checking dates
    
    // This will hold the tuple from the db query
    DBresult* l_res = NULL;

    // Query string
    CORBA::String_var l_query;
    CORBA::String_var l_query_count;

    // Get class and team id
    CORBA::String_var l_class_id = l_class_server->get_class_id_from_class_name(a_class_name);
    CORBA::String_var l_team_id = get_team_id_from_team_name(l_class_id, a_team_name);

    // Compose the query string
    l_query_count = UTIL::comp_string(COUNT_SOUND_MSG_FOR_TEAM,
                                      (char*)l_team_id,
                                      "'",
                                      " and rct_date >= '",
                                      a_from_date,
                                      "' and rct_date <= '",
                                      a_to_date,
                                      "'",
                                      END);

    CORBA::Long l_num_tuples = l_db_server->get_num_tuples(l_query_count);

    // Test if the selected data range exceeds the data limit
    if(RCT_ARCHIVE_MESSAGE_LIMIT < l_num_tuples) {
        
        throw RCT::TeamServer::DataSelectionExceedsLimit("RCT_EXCEPTION: Data Selection Exceeds Limit");
    }

    // Continue if there was not exception thrown 
    
    // Compose the query string
    l_query = UTIL::comp_string(SELECT_SOUND_MSG_FOR_TEAM,
                                (char*)l_team_id,
                                "'",
                                " and rct_date >= '",
                                a_from_date,
                                "' and rct_date <= '",
                                a_to_date,
                                "'",
                                " order by rct_date asc",
                                END);

    // Execute the query
    l_res = l_db_server->exec_db_query(l_query);

    // Test if we have a valid result
    if(!l_db_server->has_result_no_tuple(l_res)) {

        // Creating vars to hold the number of tuples
        CORBA::Long l_tuples = l_db_server->db_ntuples(l_res);

        // Setting the size of the course content sequence
        a_snd_msg_seq->length(l_tuples);

        // Now we can access the tuples and populate
        // the sound info sequence
        for(unsigned int i = 0; i < l_tuples; i++) {

            a_snd_msg_seq[i].id = l_db_server->db_getvalue(l_res, i, RCT_FV_ID);
            a_snd_msg_seq[i].alias = l_db_server->db_get_w_value(l_res, i, RCT_FV_ALIAS);
            a_snd_msg_seq[i].name = l_db_server->db_getvalue(l_res, i, RCT_FV_NAME);
            a_snd_msg_seq[i].user_alias = l_db_server->db_getvalue(l_res, i, RCT_FV_USER_ALIAS);
            a_snd_msg_seq[i].mime_type = l_db_server->db_getvalue(l_res, i, RCT_FV_MIME_TYPE);
            a_snd_msg_seq[i].date = l_db_server->db_getvalue(l_res, i, RCT_FV_DATE);
            a_snd_msg_seq[i].length = l_db_server->db_getvalue(l_res, i, RCT_FV_LENGTH);
        }
    }

    // Freeing memory
    l_db_server->db_clear(l_res);
}


// IDL Method:
void
TeamServer_i::get_textpad_archive_time_range(const char *a_class_name,
                                             const char *a_team_name,
                                             const char *a_from_date,
                                             const char *a_to_date,
                                             TextpadMsgHistSeq_out a_textpad_msg_seq)
    throw(CORBA::SystemException, RCT::TeamServer::DataSelectionExceedsLimit)
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DatabaseServerS * l_db_server = DatabaseServerS::instance();
    ClassServerS *l_class_server = ClassServerS::instance();

    // Create the empty TextpadMsg sequence
    a_textpad_msg_seq = new TextpadMsgHistSeq();

    // First we check if the dates are in a valid format

    // Check FROM DATE
    if(!is_date_valid(a_from_date)) {

        throw RCT::TeamServer::DataSelectionExceedsLimit("RCT_EXCEPTION: Wrong from date format");
    }

    // Check TO DATE
    if(!is_date_valid(a_to_date)) {

        throw RCT::TeamServer::DataSelectionExceedsLimit("RCT_EXCEPTION: Wrong to date format");
    }
    // Done checking dates
    
    // This will hold the tuple from the db query
    DBresult* l_res = NULL;

    // Query string
    CORBA::String_var l_query;
    CORBA::String_var l_query_count;
    
    // Get class and team id
    CORBA::String_var l_class_id = l_class_server->get_class_id_from_class_name(a_class_name);
    CORBA::String_var l_team_id = get_team_id_from_team_name(l_class_id, a_team_name);
    
    // Compose the query string
    l_query_count = UTIL::comp_string(COUNT_TEXTPAD_MSG_FOR_TEAM,
                                      (char*)l_team_id,
                                      "'",
                                      " and rct_date >= '",
                                      a_from_date,
                                      "' and rct_date <= '",
                                      a_to_date,
                                      "'",
                                      END);

    CORBA::Long l_num_tuples = l_db_server->get_num_tuples(l_query_count);

    // Test if the selected data range exceeds the data limit
    if(RCT_ARCHIVE_MESSAGE_LIMIT < l_num_tuples) {
        
        throw RCT::TeamServer::DataSelectionExceedsLimit("RCT_EXCEPTION: Data Selection Exceeds Limit");
    }

    // Continue if there was not exception thrown 

    // Compose the query string
    l_query = UTIL::comp_string(SELECT_TEXTPAD_MSG_FOR_TEAM,
                                (char*)l_team_id,
                                "'",
                                " and rct_date >= '",
                                a_from_date,
                                "' and rct_date <= '",
                                a_to_date,
                                "'",
                                " order by rct_date asc",
                                END);

    // Execute the query
    l_res = l_db_server->exec_db_query(l_query);

    // Test if we have a valid result
    if(!l_db_server->has_result_no_tuple(l_res)) {

        // Creating vars to hold the number of tuples
        CORBA::Long l_tuples = l_db_server->db_ntuples(l_res);

        // Setting the size of the course content sequence
        a_textpad_msg_seq->length(l_tuples);

        // Now we can access the tuples and populate
        // the sound info sequence
        for(unsigned int i = 0; i < l_tuples; i++) {
            
            a_textpad_msg_seq[i].id = l_db_server->db_getvalue(l_res, i, 0);
            a_textpad_msg_seq[i].name = l_db_server->db_get_w_value(l_res, i, 1);
            a_textpad_msg_seq[i].date = l_db_server->db_getvalue(l_res, i, 2);
        }
    }

    // Freeing memory
    l_db_server->db_clear(l_res);
}

// IDL Method:
void
TeamServer_i::get_ftp_archive_time_range(const char *a_class_name,
                                         const char *a_team_name,
                                         const char *a_from_date,
                                         const char *a_to_date,
                                         FtpMsgHistSeq_out a_ftp_msg_seq)
    throw(CORBA::SystemException, RCT::TeamServer::DataSelectionExceedsLimit)
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DatabaseServerS * l_db_server = DatabaseServerS::instance();
    ClassServerS *l_class_server = ClassServerS::instance();

    // Create the empty FtpMsg sequence
    a_ftp_msg_seq = new FtpMsgHistSeq();

    // First we check if the dates are in a valid format

    // Check FROM DATE
    if(!is_date_valid(a_from_date)) {

        throw RCT::TeamServer::DataSelectionExceedsLimit("RCT_EXCEPTION: Wrong from date format");
    }

    // Check TO DATE
    if(!is_date_valid(a_to_date)) {

        throw RCT::TeamServer::DataSelectionExceedsLimit("RCT_EXCEPTION: Wrong to date format");
    }
    // Done checking dates

    // This will hold the tuple from the db query
    DBresult* l_res = NULL;

    // Query string
    CORBA::String_var l_query;
    CORBA::String_var l_query_count;
    
    // Get class and team id
    CORBA::String_var l_class_id = l_class_server->get_class_id_from_class_name(a_class_name);
    CORBA::String_var l_team_id = get_team_id_from_team_name(l_class_id, a_team_name);

    // Compose the query string
    l_query_count = UTIL::comp_string(COUNT_FTP_MSG_FOR_TEAM,
                                      (char*)l_team_id,
                                      "'",
                                      " and rct_date >= '",
                                      a_from_date,
                                      "' and rct_date <= '",
                                      a_to_date,
                                      "'",
                                      END);

    CORBA::Long l_num_tuples = l_db_server->get_num_tuples(l_query_count);

    // Test if the selected data range exceeds the data limit
    if(RCT_ARCHIVE_MESSAGE_LIMIT < l_num_tuples) {
        
        throw RCT::TeamServer::DataSelectionExceedsLimit("RCT_EXCEPTION: Data Selection Exceeds Limit");
    }

    // Continue if there was not exception thrown 

    // Compose the query string
    l_query = UTIL::comp_string(SELECT_FTP_MSG_FOR_TEAM,
                                (char*)l_team_id,
                                "'",
                                " and rct_date >= '",
                                a_from_date,
                                "' and rct_date <= '",
                                a_to_date,
                                "'",
                                " order by rct_date asc",
                                END);

    // Execute the query
    l_res = l_db_server->exec_db_query(l_query);

    // Test if we have a valid result
    if(!l_db_server->has_result_no_tuple(l_res)) {

        // Creating vars to hold the number of tuples
        CORBA::Long l_tuples = l_db_server->db_ntuples(l_res);

        // Setting the size of the course content sequence
        a_ftp_msg_seq->length(l_tuples);

        // Now we can access the tuples and populate
        // the ftp info sequence
        for(unsigned int i = 0; i < l_tuples; i++) {

            a_ftp_msg_seq[i].id = l_db_server->db_getvalue(l_res, i, RCT_FV_ID);
            a_ftp_msg_seq[i].alias = l_db_server->db_get_w_value(l_res, i, RCT_FV_ALIAS);
            a_ftp_msg_seq[i].name = l_db_server->db_getvalue(l_res, i, RCT_FV_NAME);
            a_ftp_msg_seq[i].user_alias = l_db_server->db_getvalue(l_res, i, RCT_FV_USER_ALIAS);
            a_ftp_msg_seq[i].mime_type = l_db_server->db_getvalue(l_res, i, RCT_FV_MIME_TYPE);
            a_ftp_msg_seq[i].date = l_db_server->db_getvalue(l_res, i, RCT_FV_DATE);
            a_ftp_msg_seq[i].length = l_db_server->db_getvalue(l_res, i, RCT_FV_LENGTH);
        }
    }

    // Freeing memory
    l_db_server->db_clear(l_res);
}


// IDL Method:
void
TeamServer_i::get_chat_archive_last_n(const char *a_class_name,
                                      const char *a_team_name,
                                      CORBA::Long a_num_msg,
                                      ChatMsgHistSeq_out a_chat_msg_seq)
    throw(CORBA::SystemException, RCT::TeamServer::DataSelectionExceedsLimit)
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    // Create the empty ChatMsg sequence
    a_chat_msg_seq = new ChatMsgHistSeq();

    // Test if the selected data range exceeds the data limit
    if(RCT_ARCHIVE_MESSAGE_LIMIT < a_num_msg) {
        
        throw RCT::TeamServer::DataSelectionExceedsLimit("RCT_EXCEPTION: Data Selection Exceeds Limit");
    }

    // Continue if there was not exception thrown  
    
    DatabaseServerS * l_db_server = DatabaseServerS::instance();
    ClassServerS *l_class_server = ClassServerS::instance();

    // This will hold the tuple from the db query
    DBresult* l_res = NULL;

    // Query string
    CORBA::String_var l_query;

    // Get class and team id
    CORBA::String_var l_class_id = l_class_server->get_class_id_from_class_name(a_class_name);
    CORBA::String_var l_team_id = get_team_id_from_team_name(l_class_id, a_team_name);

    // Compose the query string
    l_query = UTIL::comp_string(SELECT_CHAT_MSG_FOR_TEAM,
                                (char*)l_team_id,
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

            a_chat_msg_seq[j].chat_id = l_db_server->db_getvalue(l_res, i, RCT_CHATV_ID);
            a_chat_msg_seq[j].chat_msg = l_db_server->db_get_w_value(l_res, i, RCT_CHATV_MSG);
            a_chat_msg_seq[j].user_id = l_db_server->db_getvalue(l_res, i, RCT_CHATV_USER_ID);
            a_chat_msg_seq[j].user_alias = l_db_server->db_getvalue(l_res, i, RCT_CHATV_USER_ALIAS);
            a_chat_msg_seq[j].class_id = l_db_server->db_getvalue(l_res, i, RCT_CHATV_CLASS_ID);
            a_chat_msg_seq[j].class_name = l_db_server->db_getvalue(l_res, i, RCT_CHATV_CLASS_NAME);
            a_chat_msg_seq[j].assembly_id = l_db_server->db_getvalue(l_res, i, RCT_CHATV_TEAM_ID);
            a_chat_msg_seq[j].assembly_name = l_db_server->db_getvalue(l_res, i, RCT_CHATV_TEAM_NAME);
            a_chat_msg_seq[j].permission = l_db_server->db_getvalue(l_res, i, RCT_CHATV_PER);
            a_chat_msg_seq[j].rct_date = l_db_server->db_getvalue(l_res, i, RCT_CHATV_DATE);
            a_chat_msg_seq[j].rct_version = l_db_server->db_getvalue(l_res, i, RCT_CHATV_VER);
        }   
    }

    // Freeing memory
    l_db_server->db_clear(l_res);      
}


// IDL Method:
void
TeamServer_i::get_sound_archive_last_n(const char *a_class_name,
                                       const char *a_team_name,
                                       CORBA::Long a_num_snd,
                                       SoundMsgHistSeq_out a_snd_msg_seq)
    throw(CORBA::SystemException, RCT::TeamServer::DataSelectionExceedsLimit)
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    // Create the empty SoundMsg sequence
    a_snd_msg_seq = new SoundMsgHistSeq();

    // Test if the selected data range exceeds the data limit
    if(RCT_ARCHIVE_MESSAGE_LIMIT < a_num_snd) {
        
        throw RCT::TeamServer::DataSelectionExceedsLimit("RCT_EXCEPTION: Data Selection Exceeds Limit");
    }

    // Continue if there was not exception thrown  
    
    DatabaseServerS * l_db_server = DatabaseServerS::instance();
    ClassServerS *l_class_server = ClassServerS::instance();

    // This will hold the tuple from the db query
    DBresult* l_res = NULL;

    // Query string
    CORBA::String_var l_query;

    // Get class and team id
    CORBA::String_var l_class_id = l_class_server->get_class_id_from_class_name(a_class_name);
    CORBA::String_var l_team_id = get_team_id_from_team_name(l_class_id, a_team_name);
    
    // Compose the query string
    l_query = UTIL::comp_string(SELECT_SOUND_MSG_FOR_TEAM,
                                (char*)l_team_id,
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
        if(a_num_snd < l_ntuples) {

            l_start_index = l_ntuples - a_num_snd;
            a_snd_msg_seq->length(a_num_snd);
        }
        else {

            a_snd_msg_seq->length(l_ntuples);
        }

        // Fill in the sequence with DB data
        for(unsigned int i = l_start_index, j = 0; i < l_ntuples; i++, j++) {
            
            a_snd_msg_seq[j].id = l_db_server->db_getvalue(l_res, i, RCT_FV_ID);
            a_snd_msg_seq[j].alias = l_db_server->db_get_w_value(l_res, i, RCT_FV_ALIAS);
            a_snd_msg_seq[j].name = l_db_server->db_getvalue(l_res, i, RCT_FV_NAME);
            a_snd_msg_seq[j].user_alias = l_db_server->db_getvalue(l_res, i, RCT_FV_USER_ALIAS);
            a_snd_msg_seq[j].mime_type = l_db_server->db_getvalue(l_res, i, RCT_FV_MIME_TYPE);
            a_snd_msg_seq[j].date = l_db_server->db_getvalue(l_res, i, RCT_FV_DATE);
            a_snd_msg_seq[j].length = l_db_server->db_getvalue(l_res, i, RCT_FV_LENGTH);
        }
    }

    // Freeing memory
    l_db_server->db_clear(l_res);      
}


// IDL Method:
void
TeamServer_i::get_textpad_archive_last_n(const char *a_class_name,
                                         const char *a_team_name,
                                         CORBA::Long a_num_textpad,
                                         TextpadMsgHistSeq_out a_textpad_msg_seq)
    throw(CORBA::SystemException, RCT::TeamServer::DataSelectionExceedsLimit)
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    // Create the empty TextpadMsg sequence
    a_textpad_msg_seq = new TextpadMsgHistSeq();

    // Test if the selected data range exceeds the data limit
    if(RCT_ARCHIVE_MESSAGE_LIMIT < a_num_textpad) {
        
        throw RCT::TeamServer::DataSelectionExceedsLimit("RCT_EXCEPTION: Data Selection Exceeds Limit");
    }

    // Continue if there was not exception thrown  
    
    DatabaseServerS * l_db_server = DatabaseServerS::instance();
    ClassServerS *l_class_server = ClassServerS::instance();

    // This will hold the tuple from the db query
    DBresult* l_res = NULL;

    // Query string
    CORBA::String_var l_query;

    // Get class and team id
    CORBA::String_var l_class_id = l_class_server->get_class_id_from_class_name(a_class_name);
    CORBA::String_var l_team_id = get_team_id_from_team_name(l_class_id, a_team_name);
    
    // Compose the query string
    l_query = UTIL::comp_string(SELECT_TEXTPAD_MSG_FOR_TEAM,
                                (char*)l_team_id,
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
        if(a_num_textpad < l_ntuples) {

            l_start_index = l_ntuples - a_num_textpad;
            a_textpad_msg_seq->length(a_num_textpad);
        }
        else {

            a_textpad_msg_seq->length(l_ntuples);
        }

        // Fill in the sequence with DB data
        for(unsigned int i = l_start_index, j = 0; i < l_ntuples; i++, j++) {
            
            a_textpad_msg_seq[j].id = l_db_server->db_getvalue(l_res, i, 0);
            a_textpad_msg_seq[j].name = l_db_server->db_get_w_value(l_res, i, 1);
            a_textpad_msg_seq[j].date = l_db_server->db_getvalue(l_res, i, 2);
        }
    }

    // Freeing memory
    l_db_server->db_clear(l_res);      
}

// IDL Method:
void
TeamServer_i::get_ftp_archive_last_n(const char *a_class_name,
                                     const char *a_team_name,
                                     CORBA::Long a_num_ftp_info,
                                     FtpMsgHistSeq_out a_ftp_msg_seq)
    throw(CORBA::SystemException, RCT::TeamServer::DataSelectionExceedsLimit)
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    // Create the empty FtpMsg sequence
    a_ftp_msg_seq = new FtpMsgHistSeq();
    
    // Test if the selected data range exceeds the data limit
    if(RCT_ARCHIVE_MESSAGE_LIMIT < a_num_ftp_info) {
        
        throw RCT::TeamServer::DataSelectionExceedsLimit("RCT_EXCEPTION: Data Selection Exceeds Limit");
    }

    // Continue if there was not exception thrown  

    DatabaseServerS * l_db_server = DatabaseServerS::instance();
    ClassServerS *l_class_server = ClassServerS::instance();

    // This will hold the tuple from the db query
    DBresult* l_res = NULL;

    // Query string
    CORBA::String_var l_query;

    // Get class and team id
    CORBA::String_var l_class_id = l_class_server->get_class_id_from_class_name(a_class_name);
    CORBA::String_var l_team_id = get_team_id_from_team_name(l_class_id, a_team_name);
    
    // Compose the query string
    l_query = UTIL::comp_string(SELECT_FTP_MSG_FOR_TEAM,
                                (char*)l_team_id,
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
        if(a_num_ftp_info < l_ntuples) {

            l_start_index = l_ntuples - a_num_ftp_info;
            a_ftp_msg_seq->length(a_num_ftp_info);
        }
        else {

            a_ftp_msg_seq->length(l_ntuples);
        }

        // Fill in the sequence with DB data
        for(unsigned int i = l_start_index, j = 0; i < l_ntuples; i++, j++) {
            
            a_ftp_msg_seq[j].id = l_db_server->db_getvalue(l_res, i, RCT_FV_ID);
            a_ftp_msg_seq[j].alias = l_db_server->db_get_w_value(l_res, i, RCT_FV_ALIAS);
            a_ftp_msg_seq[j].name = l_db_server->db_getvalue(l_res, i, RCT_FV_NAME);
            a_ftp_msg_seq[j].user_alias = l_db_server->db_getvalue(l_res, i, RCT_FV_USER_ALIAS);
            a_ftp_msg_seq[j].mime_type = l_db_server->db_getvalue(l_res, i, RCT_FV_MIME_TYPE);
            a_ftp_msg_seq[j].date = l_db_server->db_getvalue(l_res, i, RCT_FV_DATE);
            a_ftp_msg_seq[j].length = l_db_server->db_getvalue(l_res, i, RCT_FV_LENGTH);
        }
    }

    // Freeing memory
    l_db_server->db_clear(l_res);      
}


// Local Method:
Team*
TeamServer_i::get_team_from_team_id(const char *a_team_id)
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DatabaseServerS * l_db_server = DatabaseServerS::instance();

    Team_var l_team = new Team();
    
    // This will hold the tuple from the db query
    DBresult* l_res = NULL;

    // Compose the query string
    CORBA::String_var l_query;
    l_query = UTIL::comp_string(SELECT_TEAM,
                                a_team_id,
                                "'",
                                END);

    // Execute the query
    l_res = l_db_server->exec_db_query(l_query);

    // Test if we have a valid result
    if(l_db_server->has_result_one_tuple(l_res)) {

        l_team->team_id = l_db_server->db_getvalue(l_res, 0, RCT_TEAM_ID);
        l_team->name = l_db_server->db_getvalue(l_res, 0, RCT_TEAM_NAME);
        l_team->class_id = l_db_server->db_getvalue(l_res, 0, RCT_TEAM_CLASS_ID);
        l_team->permission = l_db_server->db_getvalue(l_res, 0, RCT_TEAM_PER);
        l_team->active_status = l_db_server->db_getvalue(l_res, 0, RCT_TEAM_AS);
        l_team->date = l_db_server->db_getvalue(l_res, 0, RCT_TEAM_DATE);
        l_team->manager = l_db_server->db_getvalue(l_res, 0, RCT_TEAM_MGR);
        
    }
    else {
        
        cerr << "ERROR: Did not get team from team id!" << endl;
    }

    // Freeing memory
    l_db_server->db_clear(l_res);
       
    return l_team._retn();
}

// Local Method:
char*
TeamServer_i::get_team_id_from_team_name(const char *a_class_id,
                                         const char *a_team_name)
{
	
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DatabaseServerS * l_db_server = DatabaseServerS::instance();

    // Make the team name safe fore db entry
    char* l_team_name_esc =
        l_db_server->db_escape_string(a_team_name,
                                      strlen(a_team_name));
    
    CORBA::String_var l_team_id;
      
    // This will hold the tuple from the db query
    DBresult* l_res = NULL;

    // Compose the query string
    CORBA::String_var l_query;
    l_query = UTIL::comp_string(SELECT_TEAM_ID,
                                l_team_name_esc,
                                "'",
                                " and class_id='",
                                a_class_id,
                                "'",
                                END);

    // Execute the query
    l_res = l_db_server->exec_db_query(l_query);

    // Test if we have a valid result
    if(l_db_server->has_result_one_tuple(l_res)) {

        l_team_id = l_db_server->db_getvalue(l_res, 0, 0);
    }
    else {
        
        cerr << "ERROR: Did not get team id from team name" << endl;
    }

    // Freeing memory
    l_db_server->db_clear(l_res);

    // Cleaning up escaped name
    delete l_team_name_esc;
    
    return l_team_id._retn();
}



// Local Method:
void
TeamServer_i::update_user_online_in_team_tables(const char* a_team_id,
                                                const char* a_user_id)
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DatabaseServerS * l_db_server = DatabaseServerS::instance();

    // Compose the query string
    CORBA::String_var l_trans;
    l_trans = UTIL::comp_string(INSERT_USER_ONLINE_IN_TEAM,
                                a_user_id,
                                "', '",
                                a_team_id,
                                "')",
                                END);

    // Insert into database 
    l_db_server->exec_db_insert(l_trans);
}


// Local Method:
void
TeamServer_i::user_joins_team_notification(const char *a_class_name,
                                           const char *a_team_name,
                                           const char *a_user_alias)
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif


    DatabaseServerS * l_db_server = DatabaseServerS::instance();
    SessionServer_i *l_session_server = SessionServer_i::instance();
    UserServerS *l_user_server = UserServerS::instance();

    // Creating the notification message
    Message l_msg;
    l_msg.version = RCT_VERSION;
    l_msg.user_alias = a_user_alias;
    l_msg.permission = UNCLASSIFIED;
    l_msg.class_name = a_class_name;
    l_msg.team_name = a_team_name;
    l_msg.type = TEAM_JOIN_MSG;

    // Get all the online users relevant to the class
    UserSeq_var l_user_seq;

    // BUG fix this only send notification to members of team!!
    l_user_server->get_online_users_related_by_team(a_class_name,
                                                    a_team_name,
                                                    l_user_seq);

    // Now we send the message to each user event channel
    for(unsigned int i = 0; i < l_user_seq->length(); i++) {

        // Note: the user_id[1] accesses the user_id string's numberic part bypassing the "U" of
        // the user_id, thus if we have U123, we just convert 123 into and integer
        l_session_server->push_msg(l_msg, atoi(&l_user_seq[i].user_id[USER_ID_OFFSET]));
    }
}



// Local Method:
void
TeamServer_i::user_exits_team_notification(const char *a_class_name,
                                           const char *a_team_name,
                                           const char *a_user_alias)
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DatabaseServerS * l_db_server = DatabaseServerS::instance();
    SessionServer_i *l_session_server = SessionServer_i::instance();
    UserServerS *l_user_server = UserServerS::instance();

    // Creating the notification message
    Message l_msg;
    l_msg.version = RCT_VERSION;
    l_msg.user_alias = a_user_alias;
    l_msg.permission = UNCLASSIFIED;
    l_msg.class_name = a_class_name;
    l_msg.team_name = a_team_name;
    l_msg.type = TEAM_EXIT_MSG;
    
    // Get all the online users relevant to the class
    UserSeq_var l_user_seq;
    l_user_server->get_online_users_related_by_team(a_class_name,
                                                    a_team_name,
                                                    l_user_seq);

    // Now we send the message to each user event channel
    for(unsigned int i = 0; i < l_user_seq->length(); i++) {

        // Note: the user_id[1] accesses the user_id string's numberic part bypassing the "U" of
        // the user_id, thus if we have U123, we just convert 123 into and integer
        l_session_server->push_msg(l_msg, atoi(&l_user_seq[i].user_id[USER_ID_OFFSET]));
    }
}


// Local Method:
CORBA::Boolean
TeamServer_i::does_team_exist(const char *a_class_name,
                              const char *a_team_name)
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    // Gettin all the teams from the given class name
    TeamSeq_var l_team_seq;
    get_teams_from_class_name(a_class_name, l_team_seq);

    for(int k = 0; k < l_team_seq->length(); k++) {

        if(0 == strcmp(l_team_seq[k].name, a_team_name)) {

            // We got a match and thus return true
            return 1;
        }
    }

    // We looped through the teams sequence and did not
    // find a match
    return 0;
}


// Local Method:
CORBA::Boolean
TeamServer_i::is_date_valid(const char *a_date)
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DatabaseServerS * l_db_server = DatabaseServerS::instance();

    // First we check if the dates are in a valid format
    DBresult* l_res = NULL;

    CORBA::String_var l_query;

    l_query = UTIL::comp_string("select date('",
                                a_date,
                                "')",
                                END);

    // Execute the query
    l_res = l_db_server->exec_db_query(l_query);

    if(l_db_server->db_result_tuples_ok(l_res)) {

        return 1;
    }
    else {

        return 0;
    }
}


