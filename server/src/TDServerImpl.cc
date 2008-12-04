/* $Id: TDServerImpl.cc,v 1.17 2003/05/22 19:58:33 thomas Exp $ */

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

#include "TDServerImpl.h"


// IDL Method:
void
TDServer_i::load(const char* a_user_id,
		 const char* a_class_name,
		 const char* a_assembly_name,
		 TDSeq_out a_td_seq) 
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DatabaseServerS * l_db_server = DatabaseServerS::instance();
    ClassServerS *l_class_server = ClassServerS::instance();
    TeamServerS *l_team_server = TeamServerS::instance();
    
    CORBA::String_var l_class_id = l_class_server->get_class_id_from_class_name(a_class_name);
    CORBA::String_var l_team_id = l_team_server->get_team_id_from_team_name(l_class_id,
                                                                            a_assembly_name);
     
    // This will hold the tuple from the db query
    DBresult* l_res = NULL;

    // Create the empty user sequence
    a_td_seq = new TDSeq();
    
    // Compose the query string
    CORBA::String_var l_query;
    l_query = UTIL::comp_string("select T.td_post_id, T.td_parent_id, U.alias, T.td_subject, T.td_type, T.rct_date, T.is_read \
                                 from rct_users as U, (select distinct *, true as is_read from rct_td \
                                 where td_post_id in (select post_id from rct_td_read where user_id='",
				a_user_id,
				"') union (select *, Null as is_read from rct_td) EXCEPT \
                                (select *, Null as is_read from rct_td where td_post_id in \
                                (select post_id from rct_td_read where user_id='",
				a_user_id,
				"'))) as T where U.user_id=T.td_sender and td_class_id='",
				(char *)l_class_id,
				"' and td_team_id='",
				(char *)l_team_id,
				"' order by td_post_id asc",
				END);

    // Execute the query
    l_res = l_db_server->exec_db_query(l_query);

    // Test if we have a valid result
    if(!l_db_server->has_result_no_tuple(l_res)) {

        // Creating vars to hold the number of tuples
        CORBA::Long l_ntuples = l_db_server->db_ntuples(l_res);

        // Setting the size of the sequence
        a_td_seq->length(l_ntuples);
        
        // Now we can access the tuples and populate
        // the sequence
        for(unsigned int i = 0; i < l_ntuples; i++) {

            a_td_seq[i].post_id = atoi(l_db_server->db_getvalue(l_res, i, 0));
            a_td_seq[i].parent_id = atoi(l_db_server->db_getvalue(l_res, i, 1));
            a_td_seq[i].sender = l_db_server->db_getvalue(l_res, i, 2);
            a_td_seq[i].subject = l_db_server->db_get_w_value(l_res, i, 3);
            a_td_seq[i].type = l_db_server->db_get_w_value(l_res, i, 4);
            a_td_seq[i].date = l_db_server->db_getvalue(l_res, i, 5);
	    
	    // The query fields td_is_read should have null values for messages that
	    // have not been read yet by the user
	    if(1 == l_db_server->db_getisnull(l_res, i, 6)) {
	      a_td_seq[i].is_read = 0;
	    }
	    else {
	      a_td_seq[i].is_read = 1;
	    }
        }

	// Freeing memory
	l_db_server->db_clear(l_res);
    }
}

// IDL Method:
void
TDServer_i::post_new_msg(const char* a_sender_id,
			 const char* a_sender_alias,
			 const char* a_class_name,
			 const char* a_team_name,
			 const CORBA::WChar* a_type,
			 const char* a_version,
			 const CORBA::WChar* a_subject,
			 const CORBA::WChar* a_text,
			 ObjPermission a_permission) 
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    UserServerS *l_user_server = UserServerS::instance();
    SessionServer_i *l_session_server = SessionServer_i::instance();
    ClassServerS *l_class_server = ClassServerS::instance();
    TeamServerS *l_team_server = TeamServerS::instance();
    DatabaseServerS *l_db_server = DatabaseServerS::instance();

    // Get class and team id
    CORBA::String_var l_class_id = l_class_server->get_class_id_from_class_name(a_class_name);
    CORBA::String_var l_team_id = l_team_server->get_team_id_from_team_name(l_class_id, a_team_name);


    CORBA::String_var l_trans = CORBA::string_dup(INSERT_TEMP_MSG);
    CORBA::String_var l_query = CORBA::string_dup(GET_POST_ID_DATE);

    CORBA::String_var l_post_id;
    CORBA::String_var l_date;

    // Convert and Escape UNICODE strings
    char* l_subject = UTIL::make_multi_byte_esc(a_subject);
    char* l_text = UTIL::make_multi_byte_esc(a_text);
    char* l_type = UTIL::make_multi_byte_esc(a_type);

    // Enter critical section
    RctMutex::td.lock();

    // First insert msg with temp data to generate post_id and date
    l_db_server->exec_db_insert(l_trans);

    // Now we can query the above db entry for its post_id and date
    DBresult* l_res = l_db_server->exec_db_query(l_query);

    // Test if we have a valid result
    if(l_db_server->has_result_one_tuple(l_res)) {

      l_post_id = l_db_server->db_getvalue(l_res, 0, 0);
      l_date = l_db_server->db_getvalue(l_res, 0, 1);
      
    }
    else {

      cout << "ERROR: TDServerImpl.cc postNewMsg" << endl;
    }

    // Exit critical section
    RctMutex::td.unlock();

    // Now we can update the above db entry with real data.
    l_trans = UTIL::comp_string(UPDATE_NEW_MSG,
				a_sender_id,
				"', td_class_id='",
				(char *)l_class_id,
				"', td_team_id='",
				(char * )l_team_id,
				"', td_subject='",
				l_subject,
				"', td_text='",
				l_text,
				"', td_type='",
				l_type,
				"', rct_version='",
				a_version,
                                "' where td_post_id='",
				(char *)l_post_id,
				"'",
                                END);

    l_db_server->exec_db_update(l_trans);

    // Create a TD message, which is sent to all relevant users
    // to let them know about the new post
    TDMessage l_td_msg;
    l_td_msg.base_msg.version = a_version;
    l_td_msg.base_msg.user_alias = a_sender_alias;
    l_td_msg.base_msg.class_name = a_class_name;
    l_td_msg.base_msg.team_name = a_team_name;
    l_td_msg.base_msg.permission = a_permission;
    l_td_msg.post_id = atoi(l_post_id);
    l_td_msg.parent_id = 0;
    l_td_msg.subject = a_subject;
    l_td_msg.type = a_type;
    l_td_msg.date = l_date;

    // Get all the relevant users for team or group 
    UserIdSeq_var l_user_id_seq;
    l_user_server->get_online_user_ids_from_team_name(a_class_name, a_team_name, l_user_id_seq);

    // Now we send the message to user event channels
    for (int i = 0; i < l_user_id_seq->length(); i++) {

        // Note: the [USER_ID_OFFSET] accesses the user_id string's numeric part bypassing the "U" of
        // the user_id, thus if we have U123, we just convert 123 into an integer
        l_session_server->push_td_msg(l_td_msg, atoi(&l_user_id_seq[i][USER_ID_OFFSET]));
    }

    // Freeing memory
    l_db_server->db_clear(l_res);					  

    // Cleaning up converted and escaped strings
    delete l_subject;
    delete l_text;
    delete l_type;
}


// IDL Method:
void
TDServer_i::post_reply_msg(const char* a_sender_id,
			   const char* a_sender_alias,
			   const char* a_class_name,
			   const char* a_team_name,
			   const CORBA::WChar* a_type,
			   const char* a_version,
			   CORBA::Long a_parent_id,
			   const CORBA::WChar* a_subject,
			   const CORBA::WChar* a_text,
			   ObjPermission a_permission) 
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    UserServerS *l_user_server = UserServerS::instance();
    SessionServer_i *l_session_server = SessionServer_i::instance();
    ClassServerS *l_class_server = ClassServerS::instance();
    TeamServerS *l_team_server = TeamServerS::instance();
    DatabaseServerS *l_db_server = DatabaseServerS::instance();

    // Get class and team id
    CORBA::String_var l_class_id = l_class_server->get_class_id_from_class_name(a_class_name);
    CORBA::String_var l_team_id = l_team_server->get_team_id_from_team_name(l_class_id, a_team_name);


    CORBA::String_var l_trans = CORBA::string_dup(INSERT_TEMP_MSG);
    CORBA::String_var l_query = CORBA::string_dup(GET_POST_ID_DATE);

    CORBA::String_var l_post_id;
    CORBA::String_var l_date;

    // Convert and Escape UNICODE strings
    char* l_subject = UTIL::make_multi_byte_esc(a_subject);
    char* l_text = UTIL::make_multi_byte_esc(a_text);
    char* l_type = UTIL::make_multi_byte_esc(a_type);

    // Enter critical section
    RctMutex::td.lock();

    // First insert msg with temp data to generate post_id and date
    l_db_server->exec_db_insert(l_trans);

    // Now we can query the above db entry for its post_id and date
    DBresult* l_res = l_db_server->exec_db_query(l_query);

    // Test if we have a valid result
    if(l_db_server->has_result_one_tuple(l_res)) {

      l_post_id = l_db_server->db_getvalue(l_res, 0, 0);
      l_date = l_db_server->db_getvalue(l_res, 0, 1);
      
    }
    else {

      cout << "ERROR: TDServerImpl.cc postNewMsg" << endl;
    }

    // Exit critical section
    RctMutex::td.unlock();

    // Now we can update the above db entry with real data.
    l_trans = UTIL::comp_string(UPDATE_REPLY_MSG,
				UTIL::int_to_str(a_parent_id),
				"', td_sender='",
				a_sender_id,
				"', td_class_id='",
				(char *)l_class_id,
				"', td_team_id='",
				(char * )l_team_id,
				"', td_subject='",
				l_subject,
				"', td_text='",
				l_text,
				"', td_type='",
				l_type,
				"', rct_version='",
				a_version,
                                "' where td_post_id='",
				(char*)l_post_id,
				"'",
                                END);

    l_db_server->exec_db_update(l_trans);

    // Create a TD message, which is sent to all relevant users
    // to let them know about the new post
    TDMessage l_td_msg;
    l_td_msg.base_msg.version = a_version;
    l_td_msg.base_msg.user_alias = a_sender_alias;
    l_td_msg.base_msg.class_name = a_class_name;
    l_td_msg.base_msg.team_name = a_team_name;
    l_td_msg.base_msg.permission = a_permission;
    l_td_msg.post_id = atoi(l_post_id);
    l_td_msg.parent_id = a_parent_id;
    l_td_msg.subject = a_subject;
    l_td_msg.type = a_type;
    l_td_msg.date = l_date;

    // Get all the relevant users for team or group 
    UserIdSeq_var l_user_id_seq;
    l_user_server->get_online_user_ids_from_team_name(a_class_name, a_team_name, l_user_id_seq);

    // Now we send the message to user event channels
    for (int i = 0; i < l_user_id_seq->length(); i++) {

        // Note: the [USER_ID_OFFSET] accesses the user_id string's numeric part bypassing the "U" of
        // the user_id, thus if we have U123, we just convert 123 into an integer
        l_session_server->push_td_msg(l_td_msg, atoi(&l_user_id_seq[i][USER_ID_OFFSET]));
    }

    // Freeing memory
    l_db_server->db_clear(l_res);					  

    // Cleaning up converted and escaped strings
    delete l_subject;
    delete l_text;
    delete l_type;
}

// IDL Method:
void
TDServer_i::get_text(const char* a_user_id,
		     const char *a_post_id,
		     CORBA::Boolean a_is_read,
		     CORBA::WString_out a_text)
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif
    
    DatabaseServerS *l_db_server = DatabaseServerS::instance();

    // This will hold the tuple from the db query
    DBresult* l_res = NULL;
    
    CORBA::String_var l_query;

    l_query = UTIL::comp_string(SELECT_TD_TEXT,
                                a_post_id,
                                "';",
                                END);

    // Execute the query
    l_res = l_db_server->exec_db_query(l_query);

    // Check if the requested td messagewas retrieved
    if (l_db_server->has_result_one_tuple(l_res)) {

        a_text = l_db_server->db_get_w_value(l_res, 0, 0);
    }
    else {
        
        a_text = CORBA::wstring_dup(L"");
    }

    if(!a_is_read) {

      // Now we make an entry in the read message table to note
      // that the user read the message
      CORBA::String_var l_trans = UTIL::comp_string(INSERT_READ_MSG,
						    a_post_id,
						    "', '",
						    a_user_id,
						    "')",
						    END);
    
      // Execute db transaction
      l_db_server->exec_db_insert(l_trans);
    }

    // Freeing memory
    l_db_server->db_clear(l_res);
}
