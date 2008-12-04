/* $Id: UserServerImpl.cc,v 1.20 2003/05/08 20:34:43 thomas Exp $ */

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

#include "UserServerImpl.h"


// IDL Method:
User*
UserServer_i::get_user_from_user_id(const char *a_user_id)
{
	      
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DatabaseServerS * l_db_server = DatabaseServerS::instance();
    
    // This will hold the tuple from the db query
    DBresult* l_res = NULL;

    // Pointer to user struct
    User_var l_user = new User(); 

    // Compose the query string
    CORBA::String_var l_query;
    l_query = UTIL::comp_string(SELECT_USER,
                                a_user_id,
                                "'",
                                END);

    l_res = l_db_server->exec_db_query(l_query);
        
    if(l_db_server->has_result_one_tuple(l_res)) {

        l_user->user_id = l_db_server->db_getvalue(l_res, 0, RCT_USER_ID);
        l_user->alias = l_db_server->db_getvalue(l_res, 0, RCT_USER_ALIAS);
        l_user->first_name = l_db_server->db_getvalue(l_res, 0, RCT_USER_FN);
        l_user->last_name = l_db_server->db_getvalue(l_res, 0, RCT_USER_LN);
        l_user->password = l_db_server->db_getvalue(l_res, 0, RCT_USER_PW);
        l_user->permission = l_db_server->db_getvalue(l_res, 0, RCT_USER_PER);
        l_user->online_status = l_db_server->db_getvalue(l_res, 0, RCT_USER_OS);
        
    }
    else if(l_db_server->has_result_no_tuple(l_res)) {

        // We just return the NULL pointer since no memory has been allocated
        cerr << "ERROR: UserID not known!" << endl;
    }
    else {

        // This is the case when the query returned more than one
        // tuple, thus we have duplicate UserIDs in the DB
        // We just return the Null pointer since no memory as been allocated
        cerr << "ERROR: Database inconsistency!" << endl;
    }
    

    // Freeing memory
    l_db_server->db_clear(l_res);

    return l_user._retn();
}


// IDL Method:
User*
UserServer_i::get_user_from_user_alias(const char *a_alias)
{
	      
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DatabaseServerS * l_db_server = DatabaseServerS::instance();

    // Make the group name safe fore db entry
    char* l_alias_esc =
        l_db_server->db_escape_string(a_alias,
                                      strlen(a_alias));
    
    // This will hold the tuple from the db query
    DBresult* l_res = NULL;

    // Pointer to user struct
    User_var l_user = new User(); 

    // Compose the query string
    CORBA::String_var l_query;
    l_query = UTIL::comp_string(SELECT_USER_ALIAS,
                                l_alias_esc,
                                "'",
                                END);

    l_res = l_db_server->exec_db_query(l_query);
        
    if(l_db_server->has_result_one_tuple(l_res)) {

        l_user->user_id = l_db_server->db_getvalue(l_res, 0, RCT_USER_ID);
        l_user->alias = l_db_server->db_getvalue(l_res, 0, RCT_USER_ALIAS);
        l_user->first_name = l_db_server->db_getvalue(l_res, 0, RCT_USER_FN);
        l_user->last_name = l_db_server->db_getvalue(l_res, 0, RCT_USER_LN);
        l_user->password = l_db_server->db_getvalue(l_res, 0, RCT_USER_PW);
        l_user->permission = l_db_server->db_getvalue(l_res, 0, RCT_USER_PER);
        l_user->online_status = l_db_server->db_getvalue(l_res, 0, RCT_USER_OS);
        
    }
    else if(l_db_server->has_result_no_tuple(l_res)) {

        // We just return the NULL pointer since no memory has been allocated
        cerr << "ERROR: UserALIAS not known!" << endl;
    }
    else {

        // This is the case when the query returned more than one
        // tuple, thus we have duplicate UserIDs in the DB
        // We just return the Null pointer since no memory as been allocated
        cerr << "ERROR: Database inconsistency!" << endl;
    }
    

    // Freeing memory
    l_db_server->db_clear(l_res);

    // Cleaning up escaped name
    delete l_alias_esc;
    
    return l_user._retn();
}


// IDL Method:
void
UserServer_i::get_users_from_class_name(const char *a_class_name,
                                        UserSeq_out a_user_seq)
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

    // Create the empty user sequence
    a_user_seq = new UserSeq();
    
    // Compose the query string
    CORBA::String_var l_query;
    l_query = UTIL::comp_string(SELECT_USERS_IN_CLASS,
                                l_class_name_esc,
                                "')",
                                END);

    // Execute the query
    l_res = l_db_server->exec_db_query(l_query);

    // Test if we have a valid result
    if(!l_db_server->has_result_no_tuple(l_res)) {

        // Creating vars to hold the number of tuples
        CORBA::Long l_ntuples = l_db_server->db_ntuples(l_res);

        // Setting the size of the class sequence
        a_user_seq->length(l_ntuples);
        
        // Now we can access the tuples and populate
        // the class sequence
        for(unsigned int i = 0; i < l_ntuples; i++) {

            a_user_seq[i].user_id = l_db_server->db_getvalue(l_res, i, RCT_USER_ID);
            a_user_seq[i].alias = l_db_server->db_getvalue(l_res, i, RCT_USER_ALIAS);
            a_user_seq[i].first_name = l_db_server->db_getvalue(l_res, i, RCT_USER_FN);
            a_user_seq[i].last_name = l_db_server->db_getvalue(l_res, i, RCT_USER_LN);
            a_user_seq[i].password = l_db_server->db_getvalue(l_res, i, RCT_USER_PW);
            a_user_seq[i].permission = l_db_server->db_getvalue(l_res, i, RCT_USER_PER);
            a_user_seq[i].online_status = l_db_server->db_getvalue(l_res, i, RCT_USER_OS);
            a_user_seq[i].date = l_db_server->db_getvalue(l_res, i, RCT_USER_DATE);
        }
    }

    // Freeing memory
    l_db_server->db_clear(l_res);

    // Cleaning up escaped name
    delete l_class_name_esc;
}


// IDL Method:
void
UserServer_i::get_users_from_team_name(const char *a_class_name,
                                       const char *a_team_name,
                                       UserSeq_out a_user_seq)
{
	      
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DatabaseServerS * l_db_server = DatabaseServerS::instance();
    ClassServerS *l_class_server = ClassServerS::instance();
    TeamServerS *l_team_server = TeamServerS::instance();

    CORBA::String_var l_class_id = l_class_server->get_class_id_from_class_name(a_class_name);
    CORBA::String_var l_team_id = l_team_server->get_team_id_from_team_name(l_class_id,
                                                                            a_team_name);
    
    // This will hold the tuple from the db query
    DBresult* l_res = NULL;

    // Create the empty user sequence
    a_user_seq = new UserSeq();
    
    // Compose the query string
    CORBA::String_var l_query;
    l_query = UTIL::comp_string(SELECT_USERS_IN_TEAM,
                                (char *)l_team_id,
                                "'",
                                END);
    // Execute the query
    l_res = l_db_server->exec_db_query(l_query);

    // Test if we have a valid result
    if(!l_db_server->has_result_no_tuple(l_res)) {

        // Creating vars to hold the number of tuples
        CORBA::Long l_ntuples = l_db_server->db_ntuples(l_res);

        // Setting the size of the class sequence
        a_user_seq->length(l_ntuples);
        
        // Now we can access the tuples and populate
        // the class sequence
        for(unsigned int i = 0; i < l_ntuples; i++) {

            a_user_seq[i].user_id = l_db_server->db_getvalue(l_res, i, RCT_USER_ID);
            a_user_seq[i].alias = l_db_server->db_getvalue(l_res, i, RCT_USER_ALIAS);
            a_user_seq[i].first_name = l_db_server->db_getvalue(l_res, i, RCT_USER_FN);
            a_user_seq[i].last_name = l_db_server->db_getvalue(l_res, i, RCT_USER_LN);
            a_user_seq[i].password = l_db_server->db_getvalue(l_res, i, RCT_USER_PW);
            a_user_seq[i].permission = l_db_server->db_getvalue(l_res, i, RCT_USER_PER);
            a_user_seq[i].online_status = l_db_server->db_getvalue(l_res, i, RCT_USER_OS);
            a_user_seq[i].date = l_db_server->db_getvalue(l_res, i, RCT_USER_DATE);
        }
    }

    // Freeing memory
    l_db_server->db_clear(l_res);
}


// IDL Method:
void
UserServer_i::get_user_ids_from_team_name(const char *a_class_name,
                                          const char *a_team_name,
                                          UserIdSeq_out a_user_id_seq)
{
	      
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DatabaseServerS * l_db_server = DatabaseServerS::instance();
    ClassServerS *l_class_server = ClassServerS::instance();
    TeamServerS *l_team_server = TeamServerS::instance();

    CORBA::String_var l_class_id = l_class_server->get_class_id_from_class_name(a_class_name);
    CORBA::String_var l_team_id = l_team_server->get_team_id_from_team_name(l_class_id,
                                                                            a_team_name);
    
    // This will hold the tuple from the db query
    DBresult* l_res = NULL;

    // Create the empty user sequence
    a_user_id_seq = new UserIdSeq();
    
    // Compose the query string
    CORBA::String_var l_query;
    l_query = UTIL::comp_string(SELECT_USER_IDS_IN_TEAM,
                                (char *)l_team_id,
                                "'",
                                END);

    // Execute the query
    l_res = l_db_server->exec_db_query(l_query);

    // Test if we have a valid result
    if(!l_db_server->has_result_no_tuple(l_res)) {

        // Creating vars to hold the number of tuples
        CORBA::Long l_ntuples = l_db_server->db_ntuples(l_res);

        // Setting the size of the class sequence
        a_user_id_seq->length(l_ntuples);
        
        // Now we can access the tuples and populate
        // the class sequence
        for(unsigned int i = 0; i < l_ntuples; i++) {

            a_user_id_seq[i] = l_db_server->db_getvalue(l_res, i, RCT_USER_ID);
        }
    }

    // Freeing memory
    l_db_server->db_clear(l_res);
}


// IDL Method:
void
UserServer_i::get_users_from_group_name(const char *a_class_name,
                                        const char *a_group_name,
                                        UserSeq_out a_user_seq)
{
	      
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DatabaseServerS * l_db_server = DatabaseServerS::instance();
    ClassServerS *l_class_server = ClassServerS::instance();
    GroupServerS *l_group_server = GroupServerS::instance();

    CORBA::String_var l_class_id = l_class_server->get_class_id_from_class_name(a_class_name);
    CORBA::String_var l_group_id = l_group_server->get_group_id_from_group_name(l_class_id,
                                                                                a_group_name);
    // This will hold the tuple from the db query
    DBresult* l_res = NULL;

    // Create the empty user sequence
    a_user_seq = new UserSeq();

    // Compose the query string
    CORBA::String_var l_query;
    l_query = UTIL::comp_string(SELECT_USERS_IN_GROUP,
                                (char *)l_group_id,
                                "'",
                                END);

    // Execute the query
    l_res = l_db_server->exec_db_query(l_query);

    // Test if we have a valid result
    if(!l_db_server->has_result_no_tuple(l_res)) {

        // Creating vars to hold the number of tuples
        CORBA::Long l_ntuples = l_db_server->db_ntuples(l_res);

        // Setting the size of the class sequence
        a_user_seq->length(l_ntuples);
        
        // Now we can access the tuples and populate
        // the class sequence
        // Access each tuple
        for(unsigned int i = 0; i < l_ntuples; i++) {

            a_user_seq[i].user_id = l_db_server->db_getvalue(l_res, i, RCT_USER_ID);
            a_user_seq[i].alias = l_db_server->db_getvalue(l_res, i, RCT_USER_ALIAS);
            a_user_seq[i].first_name = l_db_server->db_getvalue(l_res, i, RCT_USER_FN);
            a_user_seq[i].last_name = l_db_server->db_getvalue(l_res, i, RCT_USER_LN);
            a_user_seq[i].password = l_db_server->db_getvalue(l_res, i, RCT_USER_PW);
            a_user_seq[i].permission = l_db_server->db_getvalue(l_res, i, RCT_USER_PER);
            a_user_seq[i].online_status = l_db_server->db_getvalue(l_res, i, RCT_USER_OS);
            a_user_seq[i].date = l_db_server->db_getvalue(l_res, i, RCT_USER_DATE);
        }
    }

    // Freeing memory
    l_db_server->db_clear(l_res);
}


// IDL Method:
void
UserServer_i::get_user_ids_from_group_name(const char *a_class_name,
                                           const char *a_group_name,
                                           UserIdSeq_out a_user_id_seq)
{
	      
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DatabaseServerS * l_db_server = DatabaseServerS::instance();
    ClassServerS *l_class_server = ClassServerS::instance();
    GroupServerS *l_group_server = GroupServerS::instance();

    CORBA::String_var l_class_id = l_class_server->get_class_id_from_class_name(a_class_name);
    CORBA::String_var l_group_id = l_group_server->get_group_id_from_group_name(l_class_id,
                                                                                a_group_name);
    // This will hold the tuple from the db query
    DBresult* l_res = NULL;

    // Create the empty user sequence
    a_user_id_seq = new UserIdSeq();

    // Compose the query string
    CORBA::String_var l_query;
    l_query = UTIL::comp_string(SELECT_USER_IDS_IN_GROUP,
                                (char *)l_group_id,
                                "'",
                                END);

    // Execute the query
    l_res = l_db_server->exec_db_query(l_query);

    // Test if we have a valid result
    if(!l_db_server->has_result_no_tuple(l_res)) {

        // Creating vars to hold the number of tuples
        CORBA::Long l_ntuples = l_db_server->db_ntuples(l_res);

        // Setting the size of the class sequence
        a_user_id_seq->length(l_ntuples);
        
        // Now we can access the tuples and populate
        // the class sequence
        // Access each tuple
        for(unsigned int i = 0; i < l_ntuples; i++) {

            a_user_id_seq[i] = l_db_server->db_getvalue(l_res, i, RCT_USER_ID);
        }
    }

    // Freeing memory
    l_db_server->db_clear(l_res);
}


// IDL Method:
void
UserServer_i::get_users_related_by_classes(const char *a_user_id,
                                           UserSeq_out a_user_seq)
{
      
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DatabaseServerS * l_db_server = DatabaseServerS::instance();

    // This will hold the tuple from the db query
    DBresult* l_res = NULL;

    // Create the empty user sequence
    a_user_seq = new UserSeq();
    
    // Compose the query string
    CORBA::String_var l_query;
    l_query = UTIL::comp_string(SELECT_USERS_REL_CLASS,
                                a_user_id,
                                "')) order by alias asc",
                                END);

    // Execute the query
    l_res = l_db_server->exec_db_query(l_query);

    // Test if we have a valid result
    if(!l_db_server->has_result_no_tuple(l_res)) {

        // Creating vars to hold the number of tuples
        CORBA::Long l_ntuples = l_db_server->db_ntuples(l_res);

        // Setting the size of the class sequence
        a_user_seq->length(l_ntuples);
        
        // Now we can access the tuples and populate
        // the class sequence
        for(unsigned int i = 0; i < l_ntuples; i++) {

            a_user_seq[i].user_id = l_db_server->db_getvalue(l_res, i, RCT_USER_ID);
            a_user_seq[i].alias = l_db_server->db_getvalue(l_res, i, RCT_USER_ALIAS);
            a_user_seq[i].first_name = l_db_server->db_getvalue(l_res, i, RCT_USER_FN);
            a_user_seq[i].last_name = l_db_server->db_getvalue(l_res, i, RCT_USER_LN);
            a_user_seq[i].password = l_db_server->db_getvalue(l_res, i, RCT_USER_PW);
            a_user_seq[i].permission = l_db_server->db_getvalue(l_res, i, RCT_USER_PER);
            a_user_seq[i].online_status = l_db_server->db_getvalue(l_res, i, RCT_USER_OS);
            a_user_seq[i].date = l_db_server->db_getvalue(l_res, i, RCT_USER_DATE);
        }
    }

    // Freeing memory
    l_db_server->db_clear(l_res);
}



// IDL Method:
void
UserServer_i::get_all_users(UserSeq_out a_user_seq)
{
      
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DatabaseServerS * l_db_server = DatabaseServerS::instance();
    
    // This will hold the tuple from the db query
    DBresult* l_res = NULL;

    // Create the empty user sequence
    a_user_seq = new UserSeq();

    // Compose the query string
    CORBA::String_var l_query;
    l_query = CORBA::string_dup(SELECT_USERS);

    // Execute the query
    l_res = l_db_server->exec_db_query(l_query);

    // Test if we have a valid result
    if(!l_db_server->has_result_no_tuple(l_res)) {

        // Creating vars to hold the number of tuples
        CORBA::Long l_ntuples = l_db_server->db_ntuples(l_res);

        // Setting the size of the class sequence
        a_user_seq->length(l_ntuples);
        
        // Now we can access the tuples and populate
        // the class sequence
        // Access each tuple
        for(unsigned int i = 0; i < l_ntuples; i++) {

            a_user_seq[i].user_id = l_db_server->db_getvalue(l_res, i, RCT_USER_ID);
            a_user_seq[i].alias = l_db_server->db_getvalue(l_res, i, RCT_USER_ALIAS);
            a_user_seq[i].first_name = l_db_server->db_getvalue(l_res, i, RCT_USER_FN);
            a_user_seq[i].last_name = l_db_server->db_getvalue(l_res, i, RCT_USER_LN);
            a_user_seq[i].password = l_db_server->db_getvalue(l_res, i, RCT_USER_PW);
            a_user_seq[i].permission = l_db_server->db_getvalue(l_res, i, RCT_USER_PER);
            a_user_seq[i].online_status = l_db_server->db_getvalue(l_res, i, RCT_USER_OS);
            a_user_seq[i].date = l_db_server->db_getvalue(l_res, i, RCT_USER_DATE);
        }
    }

    // Freeing memory
    l_db_server->db_clear(l_res);
}


// IDL Method:
void
UserServer_i::get_online_users_from_team_name(const char *a_class_name,
                                              const char *a_team_name,
                                              UserSeq_out a_user_seq)
{
	      
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DatabaseServerS * l_db_server = DatabaseServerS::instance();
    ClassServerS *l_class_server = ClassServerS::instance();
    TeamServerS *l_team_server = TeamServerS::instance();
    
    CORBA::String_var l_class_id = l_class_server->get_class_id_from_class_name(a_class_name);
    CORBA::String_var l_team_id = l_team_server->get_team_id_from_team_name(l_class_id,
                                                                            a_team_name);

    // Now get the active users in this team
    // This will hold the tuple from the db query
    DBresult* l_res = NULL;

    // Create the empty user sequence
    a_user_seq = new UserSeq();

    // Compose the query string
    CORBA::String_var l_query;
    l_query = UTIL::comp_string(SELECT_USERS_IN_TEAM_ONLINE,
                                (char*)l_team_id,
                                "'",
                                END);

    // Enter critical section
    RctMutex::user.lock();
    
    // Execute the query
    l_res = l_db_server->exec_db_query(l_query);

    // Test if we have a valid result
    if(!l_db_server->has_result_no_tuple(l_res)) {

        // Creating vars to hold the number of tuples
        CORBA::Long l_ntuples = l_db_server->db_ntuples(l_res);

        // Setting the size of the class sequence
        a_user_seq->length(l_ntuples);

        // Now we can access the tuples and populate
        // the class sequence
        for(unsigned int i = 0; i < l_ntuples; i++) {

            a_user_seq[i].user_id = l_db_server->db_getvalue(l_res, i, RCT_USER_ID);
            a_user_seq[i].alias = l_db_server->db_getvalue(l_res, i, RCT_USER_ALIAS);
            a_user_seq[i].first_name = l_db_server->db_getvalue(l_res, i, RCT_USER_FN);
            a_user_seq[i].last_name = l_db_server->db_getvalue(l_res, i, RCT_USER_LN);
            a_user_seq[i].password = l_db_server->db_getvalue(l_res, i, RCT_USER_PW);
            a_user_seq[i].permission = l_db_server->db_getvalue(l_res, i, RCT_USER_PER);
            a_user_seq[i].online_status = l_db_server->db_getvalue(l_res, i, RCT_USER_OS);
            a_user_seq[i].date = l_db_server->db_getvalue(l_res, i, RCT_USER_DATE);
        }
    }

    // Exit critical section
    RctMutex::user.unlock();

    // Freeing memory
    l_db_server->db_clear(l_res);
}

// IDL Method:
void
UserServer_i::get_online_user_ids_from_team_name(const char *a_class_name,
                                                 const char *a_team_name,
                                                 UserIdSeq_out a_user_id_seq)
{
	      
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DatabaseServerS * l_db_server = DatabaseServerS::instance();
    ClassServerS *l_class_server = ClassServerS::instance();
    TeamServerS *l_team_server = TeamServerS::instance();
    
    CORBA::String_var l_class_id = l_class_server->get_class_id_from_class_name(a_class_name);
    CORBA::String_var l_team_id = l_team_server->get_team_id_from_team_name(l_class_id,
                                                                            a_team_name);

    // Now get the active users in this team
    // This will hold the tuple from the db query
    DBresult* l_res = NULL;

    // Create the empty user sequence
    a_user_id_seq = new UserIdSeq();

    // Compose the query string
    CORBA::String_var l_query;
    l_query = UTIL::comp_string(SELECT_USER_IDS_IN_TEAM_ONLINE,
                                (char*)l_team_id,
                                "'",
                                END);

    // Execute the query
    l_res = l_db_server->exec_db_query(l_query);

    // Test if we have a valid result
    if(!l_db_server->has_result_no_tuple(l_res)) {

        // Creating vars to hold the number of tuples
        CORBA::Long l_ntuples = l_db_server->db_ntuples(l_res);

        // Setting the size of the class sequence
        a_user_id_seq->length(l_ntuples);

        // Now we can access the tuples and populate
        // the class sequence
        for(unsigned int i = 0; i < l_ntuples; i++) {

            a_user_id_seq[i] = l_db_server->db_getvalue(l_res, i, RCT_USER_ID);
        }
    }

    // Freeing memory
    l_db_server->db_clear(l_res);
}



// IDL Method:
CORBA::Boolean
UserServer_i::get_user_image(const char *a_user_alias,
                             CORBA::String_out a_user_id,
                             FileInfo_out a_file_info,
                             BinaryFile_out a_file_data)
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DatabaseServerS * l_db_server = DatabaseServerS::instance();
    FileServerS *l_file_server = FileServerS::instance();
    
    // Getting the user 
    CORBA::String_var l_user_id = get_id_from_alias(a_user_alias);
    a_user_id = CORBA::string_dup(l_user_id);
    
    // This will hold the tuple from the db query
    DBresult* l_res = NULL;

    a_file_info = new FileInfo();
    
    // Compose the query string
    CORBA::String_var l_query;
    l_query = UTIL::comp_string(SELECT_USER_IMAGE,
                                (char *)l_user_id,
                                "'",
                                END);

    // Execute the query
    l_res = l_db_server->exec_db_query(l_query);

    if(l_db_server->has_result_one_tuple(l_res)) {
        
        a_file_info->id = l_db_server->db_getvalue(l_res, 0, RCT_F_ID);
        a_file_info->alias = l_db_server->db_get_w_value(l_res, 0, RCT_F_ALIAS);
        a_file_info->name = l_db_server->db_getvalue(l_res, 0, RCT_F_NAME);
        a_file_info->mime_type = l_db_server->db_getvalue(l_res, 0, RCT_F_MIME_TYPE);
        a_file_info->date = l_db_server->db_getvalue(l_res, 0, RCT_F_DATE);

        
        // Freeing memory
        l_db_server->db_clear(l_res);
    }
    else {

        // Freeing memory
        l_db_server->db_clear(l_res);

        // Make sure we assign an empty byte sequence.
        a_file_data = new BinaryFile();

        return 0;
    }

    // Call File Server's fetch method
    if(!l_file_server->fetch(a_file_info->id, a_file_data)) {

        cerr << "ERROR: Could not access user image file!" << endl;

        return 0;
    }
    
    return 1;
}

// IDL Method:
void
UserServer_i::get_module_control_queue_users(const char *a_class_name,
                                             const char *a_assembly_name,
                                             const char *a_module_name,
                                             UserAliasSeq_out a_user_alias_seq,
                                             CORBA::Long a_type)
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DatabaseServerS * l_db_server = DatabaseServerS::instance();
    ClassServerS *l_class_server = ClassServerS::instance();
    GroupServerS *l_group_server = GroupServerS::instance();
    TeamServerS *l_team_server = TeamServerS::instance();

    // This will hold the tuple from the db query
    DBresult* l_res = NULL;

    // Create the empty user sequence
    a_user_alias_seq = new UserAliasSeq();
    
    // Vars for class, team, and group id
    CORBA::String_var l_class_id = l_class_server->get_class_id_from_class_name(a_class_name);
    CORBA::String_var l_assembly_id;
    
    if(PAGE_TEAM == a_type) {

        l_assembly_id = l_team_server->get_team_id_from_team_name(l_class_id, a_assembly_name);
        
    }
    else if(PAGE_GROUP == a_type) {

        l_assembly_id = l_group_server->get_group_id_from_group_name(l_class_id, a_assembly_name);
    }
    else {

        cerr << "ERROR: Wrong type!" << endl;
    }

    // Compose the query string
    CORBA::String_var l_query;
    l_query = UTIL::comp_string(SELECT_MOD_QUEUE_USERS,
                                (char *)l_class_id,
                                "' and C.assembly_id='",
                                (char *)l_assembly_id,
                                "' and C.rct_module='",
                                a_module_name,
                                "' order by C.queue_position asc",
                                END);

    // Execute the query
    l_res = l_db_server->exec_db_query(l_query);

    // Test if we have a valid result
    if(!l_db_server->has_result_no_tuple(l_res)) {

        // Creating vars to hold the number of tuples
        CORBA::Long l_ntuples = l_db_server->db_ntuples(l_res);

        // Setting the size of the class sequence
        a_user_alias_seq->length(l_ntuples);
        
        // Now we can access the tuples and populate
        // the class sequence
        // Access each tuple
        for(unsigned int i = 0; i < l_ntuples; i++) {

            a_user_alias_seq[i] = l_db_server->db_getvalue(l_res, i, 0);
        }
    }

    // Freeing memory
    l_db_server->db_clear(l_res);
}


// IDL Method:
CORBA::Boolean
UserServer_i::is_user_manager_of_assembly(const char *a_class_name,
                                          const char *a_assembly_name,
                                          const char *a_user_id,
                                          CORBA::Long a_type)
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DatabaseServerS * l_db_server = DatabaseServerS::instance();
    ClassServerS *l_class_server = ClassServerS::instance();
    GroupServerS *l_group_server = GroupServerS::instance();
    TeamServerS *l_team_server = TeamServerS::instance();

    // This will hold the user id, manager, from the db query 
    CORBA::String_var l_user_id_mgr;
    
    // This will hold the tuple from the db query
    DBresult* l_res = NULL;
    
    // Vars for class, team, and group id
    CORBA::String_var l_class_id = l_class_server->get_class_id_from_class_name(a_class_name);
    CORBA::String_var l_assembly_id;

    // Compose the query string
    CORBA::String_var l_query;    

    if(PAGE_TEAM == a_type) {

        l_assembly_id = l_team_server->get_team_id_from_team_name(l_class_id,
                                                                  a_assembly_name);

        l_query = UTIL::comp_string(SELECT_IS_MANAGER_TEAM,
                                    (char *)l_assembly_id,
                                    "'",
                                    END);
    }
    else if(PAGE_GROUP == a_type) {

        l_assembly_id = l_group_server->get_group_id_from_group_name(l_class_id,
                                                                     a_assembly_name);

        l_query = UTIL::comp_string(SELECT_IS_MANAGER_GROUP,
                                    (char *)l_assembly_id,
                                    "'",
                                    END);
    }
    else {

        cerr << "ERROR: Wrong type!" << endl;
    }


    // Execute the query
    l_res = l_db_server->exec_db_query(l_query);

    // Test if we have a valid result
    if(l_db_server->has_result_one_tuple(l_res)) {

            l_user_id_mgr = l_db_server->db_getvalue(l_res, 0, RCT_TEAM_MGR);
    }
    else {

        cerr << "ERROR: Wrong DB result!" << endl;
    }

    // Now we need to check if the user ids match
    if(0 == strcmp(a_user_id, l_user_id_mgr)) {

        // Freeing memory
        l_db_server->db_clear(l_res);

        return 1;
    }
    else {

        // Freeing memory
        l_db_server->db_clear(l_res);

        return 0;
    }
}


// Local Method:
void
UserServer_i::set_user_online_status(const char *a_user_alias,
                                     bool a_status)
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DatabaseServerS * l_db_server = DatabaseServerS::instance();

    // Make the alias safe fore db entry
    char* l_user_alias_esc =
        l_db_server->db_escape_string(a_user_alias,
                                      strlen(a_user_alias));
    
    CORBA::String_var l_query;

    // Creating the update query string
    if(a_status) {

        // Setting online status to ONLINE
        l_query = UTIL::comp_string(UPDATE_USR_STAT_ON,
                                    l_user_alias_esc,
                                    "'",
                                    END);
    }
    else {

        // Setting online status to OFFLINE
        l_query = UTIL::comp_string(UPDATE_USR_STAT_OFF,
                                    l_user_alias_esc,
                                    "'",
                                    END);
    }

    l_db_server->exec_db_update(l_query);

    // Cleaning up escaped name
    delete l_user_alias_esc;
}


// Local Method:
void
UserServer_i::get_online_users_related_by_team(const char *a_class_name,
                                               const char *a_team_name,
                                               UserSeq_out a_user_seq)
{
	      
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DatabaseServerS *l_db_server = DatabaseServerS::instance();
    ClassServerS *l_class_server = ClassServerS::instance();
    TeamServerS *l_team_server = TeamServerS::instance();

    // Getting class and team id
    CORBA::String_var l_class_id = l_class_server->get_class_id_from_class_name(a_class_name);
    CORBA::String_var l_team_id = l_team_server->get_team_id_from_team_name(l_class_id,
                                                                            a_team_name);
    
    // This will hold the tuple from the db query
    DBresult* l_res = NULL;

    // Create the empty user sequence
    a_user_seq = new UserSeq();

    // Compose the query string
    CORBA::String_var l_query;
    l_query = UTIL::comp_string(SELECT_USERS_RELATED_BY_TEAM,
                                (char *)l_team_id,
                                "'",
                                END);
    // Enter critical section
    RctMutex::user.lock();
    
    // Execute the query
    l_res = l_db_server->exec_db_query(l_query);

    // Test if we have a valid result
    if(!l_db_server->has_result_no_tuple(l_res)) {

        // Creating vars to hold the number of tuples
        CORBA::Long l_ntuples = l_db_server->db_ntuples(l_res);

        // Setting the size of the class sequence
        a_user_seq->length(l_ntuples);
        
        // Now we can access the tuples and populate
        // the class sequence
        for(unsigned int i = 0; i < l_ntuples; i++) {

            a_user_seq[i].user_id = l_db_server->db_getvalue(l_res, i, RCT_USER_ID);
            a_user_seq[i].alias = l_db_server->db_getvalue(l_res, i, RCT_USER_ALIAS);
            a_user_seq[i].first_name = l_db_server->db_getvalue(l_res, i, RCT_USER_FN);
            a_user_seq[i].last_name = l_db_server->db_getvalue(l_res, i, RCT_USER_LN);
            a_user_seq[i].password = l_db_server->db_getvalue(l_res, i, RCT_USER_PW);
            a_user_seq[i].permission = l_db_server->db_getvalue(l_res, i, RCT_USER_PER);
            a_user_seq[i].online_status = l_db_server->db_getvalue(l_res, i, RCT_USER_OS);
            a_user_seq[i].date = l_db_server->db_getvalue(l_res, i, RCT_USER_DATE);
        }
    }

    // Exit critical section
    RctMutex::user.unlock();
    
    // Freeing memory
    l_db_server->db_clear(l_res);
}


// Local Method:
void
UserServer_i::get_online_users_from_class_name(const char *a_class_name,
                                               UserSeq_out a_user_seq)
{
	      
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DatabaseServerS *l_db_server = DatabaseServerS::instance();

    // Make the class name safe fore db entry
    char* l_class_name_esc =
        l_db_server->db_escape_string(a_class_name,
                                      strlen(a_class_name));
    
    // This will hold the tuple from the db query
    DBresult* l_res = NULL;

    // Create the empty user sequence
    a_user_seq = new UserSeq();

    // Compose the query string
    CORBA::String_var l_query;
    l_query = UTIL::comp_string(SELECT_USERS_IN_CLASS_ONLINE,
                                l_class_name_esc,
                                "'))",
                                END);

    // Execute the query
    l_res = l_db_server->exec_db_query(l_query);

    // Test if we have a valid result
    if(!l_db_server->has_result_no_tuple(l_res)) {

        // Creating vars to hold the number of tuples
        CORBA::Long l_ntuples = l_db_server->db_ntuples(l_res);

        // Setting the size of the class sequence
        a_user_seq->length(l_ntuples);
        
        // Now we can access the tuples and populate
        // the class sequence
        for(unsigned int i = 0; i < l_ntuples; i++) {

            a_user_seq[i].user_id = l_db_server->db_getvalue(l_res, i, RCT_USER_ID);
            a_user_seq[i].alias = l_db_server->db_getvalue(l_res, i, RCT_USER_ALIAS);
            a_user_seq[i].first_name = l_db_server->db_getvalue(l_res, i, RCT_USER_FN);
            a_user_seq[i].last_name = l_db_server->db_getvalue(l_res, i, RCT_USER_LN);
            a_user_seq[i].password = l_db_server->db_getvalue(l_res, i, RCT_USER_PW);
            a_user_seq[i].permission = l_db_server->db_getvalue(l_res, i, RCT_USER_PER);
            a_user_seq[i].online_status = l_db_server->db_getvalue(l_res, i, RCT_USER_OS);
            a_user_seq[i].date = l_db_server->db_getvalue(l_res, i, RCT_USER_DATE);
        }
    }

    // Freeing memory
    l_db_server->db_clear(l_res);

    // Cleaning up escaped name
    delete l_class_name_esc;
}


// Local Method:
char*
UserServer_i::get_id_from_alias(const char *a_user_alias)
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DatabaseServerS * l_db_server = DatabaseServerS::instance();

    // Make the user alias safe fore db entry
    char* l_user_alias_esc =
        l_db_server->db_escape_string(a_user_alias,
                                      strlen(a_user_alias));

    CORBA::String_var l_user_id;
    
    // This will hold the tuple from the db query
    DBresult* l_res = NULL;

    // Compose the query string
    CORBA::String_var l_query;
    l_query = UTIL::comp_string(SELECT_USER_ALIAS,
                                l_user_alias_esc,
                                "'",
                                END);

    // Execute the query
    l_res = l_db_server->exec_db_query(l_query);

    // Test if we have a valid result
    if(l_db_server->has_result_one_tuple(l_res)) {
        
        l_user_id = l_db_server->db_getvalue(l_res, 0, RCT_USER_ID);
    }
    else {

        cerr << "ERROR: Did not get user id from user alias" << endl;
    }

    // Freeing memory
    l_db_server->db_clear(l_res);

    // Cleaning up escaped name
    delete l_user_alias_esc;
    
    return l_user_id._retn();
}


// Local Method:
char*
UserServer_i::get_alias_from_id(const char *a_user_id)
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DatabaseServerS * l_db_server = DatabaseServerS::instance();
    
    CORBA::String_var l_user_alias;
    
    // This will hold the tuple from the db query
    DBresult* l_res = NULL;

    // Compose the query string
    CORBA::String_var l_query;
    l_query = UTIL::comp_string(SELECT_USER_ID,
                                a_user_id,
                                "'",
                                END);

    // Execute the query
    l_res = l_db_server->exec_db_query(l_query);

    // Test if we have a valid result
    if(l_db_server->has_result_one_tuple(l_res)) {
        
        l_user_alias = l_db_server->db_getvalue(l_res, 0, RCT_USER_ALIAS);
    }
    else {

        cerr << "ERROR: Did not get user alias from user id" << endl;
    }

    // Freeing memory
    l_db_server->db_clear(l_res);

    return l_user_alias._retn();
}


// Local Method:
char*
UserServer_i::get_password_from_id(const char *a_user_id)
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DatabaseServerS * l_db_server = DatabaseServerS::instance();
    
    CORBA::String_var l_password;
    
    // This will hold the tuple from the db query
    DBresult* l_res = NULL;

    // Compose the query string
    CORBA::String_var l_query;
    l_query = UTIL::comp_string(SELECT_USER_ID,
                                a_user_id,
                                "'",
                                END);

    // Execute the query
    l_res = l_db_server->exec_db_query(l_query);

    // Test if we have a valid result
    if(l_db_server->has_result_one_tuple(l_res)) {
        
        l_password = l_db_server->db_getvalue(l_res, 0, RCT_USER_PW);
    }
    else {

        cerr << "ERROR: Did not get user password from user id" << endl;
    }

    // Freeing memory
    l_db_server->db_clear(l_res);

    return l_password._retn();
}












