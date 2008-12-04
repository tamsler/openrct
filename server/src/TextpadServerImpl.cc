/* $Id: TextpadServerImpl.cc,v 1.28 2003/05/08 20:34:43 thomas Exp $ */

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

#include "TextpadServerImpl.h"


// Init
void
TextpadServer_i::init()
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif
    
    // Init the counter
    pr_textpad_counter = 0;

    // Init the textpad id and ref arrays
    for(int i = 0; i < MAX_NUM_TEXTPADS; i++) {

        pr_textpad_refs[i] = NULL;
        pr_textpad_ids[i] = CORBA::string_dup("");
    }
}

// IDL Method:
void
TextpadServer_i::update(const CORBA::WChar *a_text,
                        CORBA::Long a_offset,
                        CORBA::Long a_textpad_ref,
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

    // Enter critical section
    RctMutex::textpad_op[a_textpad_ref].lock();

    // Create the textpad update message
    TextpadMessage l_tp_msg;
    l_tp_msg.base_msg.version = a_version;
    l_tp_msg.base_msg.user_alias = a_user_alias;
    l_tp_msg.base_msg.class_name = a_class_name;
    l_tp_msg.base_msg.permission = a_permission;
    l_tp_msg.base_msg.type = a_type;
    l_tp_msg.ref = a_textpad_ref;
    l_tp_msg.offset = a_offset;
    l_tp_msg.text = a_text;

    // Get all the relevant users from team or group
    UserIdSeq_var l_user_id_seq;

    // Check if we deal with a team message
    if((TEXTPAD_TEAM_REM_INS == a_type) ||
       (TEXTPAD_TEAM_REM == a_type) ||
       (TEXTPAD_TEAM_INS == a_type)) {

        l_tp_msg.base_msg.team_name = a_assembly_name;
        l_user_server->get_online_user_ids_from_team_name(a_class_name, a_assembly_name, l_user_id_seq);
    }
    // Check if we deal with a group message
    else if((TEXTPAD_GROUP_REM_INS == a_type) ||
            (TEXTPAD_GROUP_REM == a_type) ||
            (TEXTPAD_GROUP_INS == a_type)) {

        l_tp_msg.base_msg.group_name = a_assembly_name;
        l_user_server->get_user_ids_from_group_name(a_class_name, a_assembly_name, l_user_id_seq);
    }
    else {

        cerr << "ERROR: Wrong message type!" << endl;
    }

    // Now we send the message to each user event channel
    for (int i = 0; i < l_user_id_seq->length(); i++) {

        // Note: the [USER_ID_OFFSET] accesses the user_id string's numeric part bypassing the "U" of
        // the user_id, thus if we have U123, we just convert 123 into an integer
        l_session_server->push_textpad_msg(l_tp_msg, atoi(&l_user_id_seq[i][USER_ID_OFFSET]));
    }

    // Now we maintain the server textpad copy
    if((TEXTPAD_TEAM_INS == a_type) ||
       (TEXTPAD_GROUP_INS == a_type)) {

        // Concatenate text to current string
        int l_len1 = wcslen(pr_textpad_refs[a_textpad_ref]);
        int l_len2 = wcslen(a_text);

        CORBA::WChar *dest = CORBA::wstring_alloc(l_len1 + l_len2 + 1);
        wcscpy(dest, pr_textpad_refs[a_textpad_ref]); 
        wcscat(dest, a_text);
        
        CORBA::wstring_free(pr_textpad_refs[a_textpad_ref]);

        pr_textpad_refs[a_textpad_ref] = dest;
    }
    else if((TEXTPAD_TEAM_REM_INS == a_type) ||
            (TEXTPAD_GROUP_REM_INS == a_type)) {

        // Take substring from 0 to offset
        // Concatenate substring if text

        CORBA::WChar *dest1 = CORBA::wstring_alloc(a_offset + 1);
        wcsncpy(dest1, pr_textpad_refs[a_textpad_ref], a_offset);
        dest1[a_offset] = L'\0';

        int l_len1 = wcslen(dest1);
        int l_len2 = wcslen(a_text);
        
        CORBA::WChar *dest2 = CORBA::wstring_alloc(l_len1 + l_len2 + 1);
        wcscpy(dest2, dest1);
        wcscat(dest2, a_text);
        
        CORBA::wstring_free(pr_textpad_refs[a_textpad_ref]);
        CORBA::wstring_free(dest1);
        
        pr_textpad_refs[a_textpad_ref] = dest2;
    }
    else if((TEXTPAD_TEAM_REM == a_type) ||
            (TEXTPAD_GROUP_REM == a_type)) {

        // Take substring from 0 to offset

        CORBA::WChar *dest = CORBA::wstring_alloc(a_offset + 1);
        wcsncpy(dest, pr_textpad_refs[a_textpad_ref], a_offset);
        dest[a_offset] = L'\0';
        
        CORBA::wstring_free(pr_textpad_refs[a_textpad_ref]);

        pr_textpad_refs[a_textpad_ref] = dest;
    }

    // Exit critical section
    RctMutex::textpad_op[a_textpad_ref].unlock();
}


// IDL Method:
CORBA::Boolean
TextpadServer_i::create(const CORBA::WChar *a_name,
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

    DatabaseServerS *l_db_server = DatabaseServerS::instance();
    UserServerS *l_user_server = UserServerS::instance();
    SessionServer_i *l_session_server = SessionServer_i::instance();
    ClassServerS *l_class_server = ClassServerS::instance();
    TeamServerS *l_team_server = TeamServerS::instance();
    GroupServerS *l_group_server = GroupServerS::instance();

    // Enter critical section
    RctMutex::textpad.lock();

    if(MAX_NUM_TEXTPADS <= pr_textpad_counter) {

        // There are too many textpads active
        // at this time. Tell the client to try
        // again later.

        // Exit critical section
        RctMutex::textpad.unlock();

        cerr << "INFO: Max number of supported textpads reached!" << endl;
        return 0;
    }

    // Increment the counter
    pr_textpad_counter++;

    CORBA::Long l_tp_ref = get_textpad_ref();

    // Error checking
    if(ERROR == l_tp_ref) {

        // Exit critical section
        RctMutex::textpad.unlock();
        
        cerr << "ERROR: Was not able to get a textpad reference location!" << endl;
        return 0;
    }
    
    pr_textpad_refs[l_tp_ref] = CORBA::wstring_dup(L"");
    
    // Exit critical section
    RctMutex::textpad.unlock();

    // Make DB entry and send message to clients
    // with the just created l_rextpad_ref
    CORBA::String_var l_tp_id;
    CORBA::String_var l_tp_index;

    l_tp_index = l_db_server->get_textpad_index();
    l_tp_id = UTIL::comp_string(RCT_TEXTPAD,
                                (char *)l_tp_index,
                                END);

    pr_textpad_ids[l_tp_ref] = CORBA::string_dup(l_tp_id);

    // Create the textpad create message
    TextpadMessage l_tp_msg;
    l_tp_msg.base_msg.version = a_version;
    l_tp_msg.base_msg.user_alias = a_user_alias;
    l_tp_msg.base_msg.class_name = a_class_name;
    l_tp_msg.base_msg.permission = a_permission;
    l_tp_msg.base_msg.type = a_type;
    l_tp_msg.name = a_name;
    l_tp_msg.ref = l_tp_ref;
    l_tp_msg.id = CORBA::string_dup(l_tp_id);

    // Get all the relevant users from team or group
    UserIdSeq_var l_user_id_seq;

    // Check if we deal with a team message
    if(TEXTPAD_TEAM_CREATE == a_type) {

        l_tp_msg.base_msg.team_name = a_assembly_name;
        l_user_server->get_online_user_ids_from_team_name(a_class_name, a_assembly_name, l_user_id_seq);
    }
    // Check if we deal with a group message
    else if(TEXTPAD_GROUP_CREATE == a_type) {

        l_tp_msg.base_msg.group_name = a_assembly_name;
        l_user_server->get_user_ids_from_group_name(a_class_name, a_assembly_name, l_user_id_seq);
    }
    else {

        cerr << "ERROR: Wrong message type!" << endl;
    }

    // Now we send the message to each user event channel
    for (int i = 0; i < l_user_id_seq->length(); i++) {

        // Note: the [USER_ID_OFFSET] accesses the user_id string's numeric part bypassing the "U" of
        // the user_id, thus if we have U123, we just convert 123 into an integer
        l_session_server->push_textpad_msg(l_tp_msg, atoi(&l_user_id_seq[i][USER_ID_OFFSET]));
    }

    // After sending the textpad message, we need to add the info to the database
    CORBA::String_var l_trans;
    CORBA::String_var l_per = UTIL::int_to_str(a_permission);

    // Getting the class_id from class_name
    CORBA::String_var l_class_id;
    l_class_id = l_class_server->get_class_id_from_class_name(a_class_name);

    // Getting the string length of the tp name
    int l_str_size = wcstombs(NULL, a_name, 0) + 1;

    // Create the multi byte tp name string
    char l_name_multi_byte[l_str_size];
    
    if(-1 <= wcstombs(l_name_multi_byte, a_name, l_str_size)) {
        
        cerr << "ERROR: Could not convert a character!" << endl;
    }

    // Make the tp name safe fore db entry
    char* l_name_multi_byte_esc =
        l_db_server->db_escape_string(l_name_multi_byte,
                                      l_str_size - 1);
    
    if(TEXTPAD_TEAM_CREATE == a_type) {

        // Getting the team_id from team_name
        CORBA::String_var l_team_id;
        l_team_id = l_team_server->get_team_id_from_team_name(l_class_id,
                                                              a_assembly_name);

        l_trans = UTIL::comp_string(INS_TEXTPAD,
                                    (char *)l_tp_id,
                                    "','",
                                    l_name_multi_byte_esc,
                                    "','",
                                    (char *)l_class_id,
                                    "','",
                                    (char *)l_team_id,
                                    "','",
                                    DEFAULT_GROUP,
                                    "','",
                                    a_user_id,
                                    "','",
                                    (char *)l_per,
                                    "','",
                                    "now",
                                    "','",
                                    a_version,
                                    "');",
                                    END);
    }
    else if(TEXTPAD_GROUP_CREATE == a_type) {
        
        // Getting the team_id from team_name
        CORBA::String_var l_group_id;
        l_group_id = l_group_server->get_group_id_from_group_name(l_class_id,
                                                                  a_assembly_name);

        l_trans = UTIL::comp_string(INS_TEXTPAD,
                                    (char *)l_tp_id,
                                    "','",
                                    l_name_multi_byte_esc,
                                    "','",
                                    (char *)l_class_id,
                                    "','",
                                    DEFAULT_TEAM,
                                    "','",
                                    (char *)l_group_id,
                                    "','",
                                    a_user_id,
                                    "','",
                                    (char *)l_per,
                                    "','",
                                    "now",
                                    "','",
                                    a_version,
                                    "');",
                                    END);
    }
    
    // Insert consumed chat message into the DB
    l_db_server->exec_db_insert(l_trans);

    // Cleaning up escaped tp name
    delete l_name_multi_byte_esc;
    
    return 1;
}

// IDL Meethod:
CORBA::Boolean
TextpadServer_i::edit(const char *a_id,
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

    DatabaseServerS *l_db_server = DatabaseServerS::instance();
    UserServerS *l_user_server = UserServerS::instance();
    SessionServer_i *l_session_server = SessionServer_i::instance();

    // Enter critical section
    RctMutex::textpad.lock();

    if(MAX_NUM_TEXTPADS <= pr_textpad_counter) {

        // There are too many textpads active
        // at this time. Tell the client to try
        // again later.

        // Exit critical section
        RctMutex::textpad.unlock();

        cerr << "INFO: Max number of supported textpads reached!" << endl;
        return 0;
    }

    // Increment the counter
    pr_textpad_counter++;

    CORBA::Long l_tp_ref = get_textpad_ref();

    // Error checking
    if(ERROR == l_tp_ref) {

        // Exit critical section
        RctMutex::textpad.unlock();
        
        cerr << "ERROR: Was not able to get a textpad reference location!" << endl;
        return 0;
    }

    // Assign the empty string for now until we load the textpad from the db
    pr_textpad_refs[l_tp_ref] = CORBA::wstring_dup(L"");
    // Set the tp id
    pr_textpad_ids[l_tp_ref] = CORBA::string_dup(a_id);
    
    // Exit critical section
    RctMutex::textpad.unlock();

    // Now we can load the textpad and it's info from the db.

    DBresult* l_res = NULL;
    
    CORBA::String_var l_query;

    l_query = UTIL::comp_string(SELECT_TEXTPAD_ALL_FROM_ID,
                                a_id,
                                "';",
                                END);

    // Execute the query
    l_res = l_db_server->exec_db_query(l_query);

    // Create the textpad edit message
    TextpadMessage l_tp_msg;

    l_tp_msg.base_msg.version = a_version;
    l_tp_msg.base_msg.user_alias = a_user_alias;
    l_tp_msg.base_msg.class_name = a_class_name;
    l_tp_msg.base_msg.permission = a_permission;
    l_tp_msg.base_msg.type = a_type;
    l_tp_msg.id = a_id;
    l_tp_msg.ref = l_tp_ref;


    // Check if the requested textpad was retrieved
    if (l_db_server->has_result_one_tuple(l_res)) {

        // Now we fill in the rest of the textpad message
        l_tp_msg.name = l_db_server->db_get_w_value(l_res, 0, RCT_TP_NAME);
        l_tp_msg.text = l_db_server->db_get_w_value(l_res, 0, RCT_TP_TEXT);

        // We also create the tp in memory
        int l_len = wcslen(l_tp_msg.text);
        CORBA::WChar *dest = CORBA::wstring_alloc(l_len + 1);
        wcscpy(dest, l_tp_msg.text);
        CORBA::wstring_free(pr_textpad_refs[l_tp_ref]);
        pr_textpad_refs[l_tp_ref] = dest;

        // Get all the relevant users from team or group
        UserIdSeq_var l_user_id_seq;

        if(TEXTPAD_TEAM_EDIT == a_type) {
            
            l_tp_msg.base_msg.team_name = a_assembly_name;
            l_user_server->get_online_user_ids_from_team_name(a_class_name, a_assembly_name, l_user_id_seq);
        }
        else if(TEXTPAD_GROUP_EDIT == a_type) {

            l_tp_msg.base_msg.group_name = a_assembly_name;
            l_user_server->get_user_ids_from_group_name(a_class_name, a_assembly_name, l_user_id_seq);
        }
        else {

            cerr << "ERROR: Wrong message type!" << endl;
        }

        // Now we send the message to each user event channel
        for (int i = 0; i < l_user_id_seq->length(); i++) {

            // Note: the [USER_ID_OFFSET] accesses the user_id string's numeric part bypassing the "U" of
            // the user_id, thus if we have U123, we just convert 123 into an integer
            l_session_server->push_textpad_msg(l_tp_msg, atoi(&l_user_id_seq[i][USER_ID_OFFSET]));
        }

        // Freeing memory
        l_db_server->db_clear(l_res);

        return 1;
    }
    else {

        // Cleanup the pr_textpad_refs
        if(NULL != pr_textpad_refs[l_tp_ref]) {
            
            CORBA::wstring_free(pr_textpad_refs[l_tp_ref]);
            pr_textpad_refs[l_tp_ref] = NULL;
        }

        // Freeing memory
        l_db_server->db_clear(l_res);    

        return 0;
    }
}


// IDL Meethod:
void
TextpadServer_i::close(const char *a_id,
                       CORBA::Long a_textpad_ref,
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
    DatabaseServerS *l_db_server = DatabaseServerS::instance();

    // Getting the string length of the textpad
    size_t l_tp_size = wcstombs(NULL, pr_textpad_refs[a_textpad_ref], 0) + 1;

    // Create the multi byte textpad text string
    char l_tp_text_multi_byte[l_tp_size];

    if(ERROR <= wcstombs(l_tp_text_multi_byte, pr_textpad_refs[a_textpad_ref], l_tp_size)) {
        
        cerr << "ERROR: Could not convert a character!" << endl;
    }

    // Enter critical section
    RctMutex::textpad.lock();

    pr_textpad_counter--;

    CORBA::wstring_free(pr_textpad_refs[a_textpad_ref]);
    pr_textpad_refs[a_textpad_ref] = NULL;
    
    // Exit critical section
    RctMutex::textpad.unlock();
    
    // Create the textpad create message
    TextpadMessage l_tp_msg;
    l_tp_msg.base_msg.version = a_version;
    l_tp_msg.base_msg.user_alias = a_user_alias;
    l_tp_msg.base_msg.class_name = a_class_name;
    l_tp_msg.base_msg.permission = a_permission;
    l_tp_msg.base_msg.type = a_type;
    l_tp_msg.ref = a_textpad_ref;

    // Get all the relevant users from team or group
    UserIdSeq_var l_user_id_seq;
    
    // Check if we deal with a team message
    if(TEXTPAD_TEAM_CLOSE == a_type) {

        l_tp_msg.base_msg.team_name = a_assembly_name;
        l_user_server->get_online_user_ids_from_team_name(a_class_name, a_assembly_name, l_user_id_seq);
    }
    // Check if we deal with a group message
    else if(TEXTPAD_GROUP_CLOSE == a_type) {

        l_tp_msg.base_msg.group_name = a_assembly_name;
        l_user_server->get_user_ids_from_group_name(a_class_name, a_assembly_name, l_user_id_seq);
    }
    else {

        cerr << "ERROR: Wrong message type!" << endl;
    }

    // Now we send the message to each user event channel
    for (int i = 0; i < l_user_id_seq->length(); i++) {

        // Note: the [USER_ID_OFFSET] accesses the user_id string's numeric part bypassing the "U" of
        // the user_id, thus if we have U123, we just convert 123 into an integer
        l_session_server->push_textpad_msg(l_tp_msg, atoi(&l_user_id_seq[i][USER_ID_OFFSET]));
    }

    // After sending the textpad message, we need to add the info to the database
    CORBA::String_var l_trans;

    // Make the textpad safe fore db entry
    char* l_tp_text_multi_byte_esc =
        l_db_server->db_escape_wstring(l_tp_text_multi_byte,
                                       l_tp_size - 1);
    
    l_trans = UTIL::comp_string(UPDATE_TEXTPAD,
                                l_tp_text_multi_byte_esc,
                                "' where tp_id='",
                                a_id,
                                "'",
                                END);
    
    // Insert consumed chat message into the DB
    l_db_server->exec_db_insert(l_trans);

    // Cleaning up escaped tp
    delete l_tp_text_multi_byte_esc;
}


// IDL Method:
CORBA::Boolean
TextpadServer_i::is_active(const char *a_class_name,
                           const char *a_assembly_name,
                           CORBA::Long a_type,
                           CORBA::WString_out a_name,
                           CORBA::String_out a_id,
                           CORBA::Long_out a_ref,
                           CORBA::WString_out a_text)
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DatabaseServerS *l_db_server = DatabaseServerS::instance();
    ClassServerS *l_class_server = ClassServerS::instance();
    TeamServerS *l_team_server = TeamServerS::instance();
    GroupServerS *l_group_server = GroupServerS::instance();

    // Get class id
    CORBA::String_var l_class_id = l_class_server->get_class_id_from_class_name(a_class_name);
    CORBA::String_var l_assembly_id;

    // This will hold the tuple from the db query
    DBresult* l_res = NULL;
    
    CORBA::String_var l_query;
    
    if(PAGE_TEAM == a_type) {

        l_assembly_id = l_team_server->get_team_id_from_team_name(l_class_id, a_assembly_name);

        l_query = UTIL::comp_string(SELECT_TEXTPAD,
                                    (char*)l_class_id,
                                    "' and tp_team_id='",
                                    (char*) l_assembly_id,
                                    "' order by rct_date desc",
                                    END);
    }
    else if(PAGE_GROUP == a_type) {

        l_assembly_id = l_group_server->get_group_id_from_group_name(l_class_id, a_assembly_name);

        l_query = UTIL::comp_string(SELECT_TEXTPAD,
                                    (char*)l_class_id,
                                    "' and tp_group_id='",
                                    (char*) l_assembly_id,
                                    "' order by rct_date desc",
                                    END);                                            
    }
    else {

        cerr << "ERROR: Wrong page type!" << endl;
    }

    // Execute the query
    l_res = l_db_server->exec_db_query(l_query);


    // First we check if there are any textpads for the
    // class and assembly name
    if (!l_db_server->has_result_no_tuple(l_res)) {

        // There are textpads. Now we have to figure out
        // there is an active one. Since the most recent textpad is at the
        // top of the query result, we just look at that textpad

        // Getting the top textpad's id and name
        CORBA::String_var l_tp_id = l_db_server->db_getvalue(l_res, 0 , 0);
        CORBA::WString_var l_tp_name = l_db_server->db_get_w_value(l_res, 0 , 1);

        // Now we search through the active textpads to see if this textpad
        // is active.
        CORBA::Long l_tp_ref = get_textpad_ref(l_tp_id);

        if(ERROR == l_tp_ref) {

            // There was no active text pad
            a_name = CORBA::wstring_dup(L"");
            a_id = CORBA::string_dup("");
            a_ref = ERROR;
            a_text = CORBA::wstring_dup(L"");

            // Freeing memory
            l_db_server->db_clear(l_res);     

            return 0;
        }
        else {

            // Enter critical section
            RctMutex::textpad_op[l_tp_ref].lock();
    
            // There is an active text pad
            a_name = CORBA::wstring_dup(l_tp_name);
            a_id = CORBA::string_dup(l_tp_id);
            a_ref = l_tp_ref;
            a_text = CORBA::wstring_dup(pr_textpad_refs[l_tp_ref]);

            // Freeing memory
            l_db_server->db_clear(l_res);     

            // Enter critical section
            RctMutex::textpad_op[l_tp_ref].unlock();

            return 1;
        }
                        
    } else {

        // There are no textpads so we return false
        a_name = CORBA::wstring_dup(L"");
        a_id = CORBA::string_dup("");
        a_ref = ERROR;
        a_text = CORBA::wstring_dup(L"");

        // Freeing memory
        l_db_server->db_clear(l_res);     

        return 0;
    }
}


// IDL Method:
CORBA::Boolean
TextpadServer_i::get_text(const char *a_id,
                          CORBA::WString_out a_text)
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif
    
    DatabaseServerS *l_db_server = DatabaseServerS::instance();

    // This will hold the tuple from the db query
    DBresult* l_res = NULL;
    
    CORBA::String_var l_query;

    l_query = UTIL::comp_string(SELECT_TEXTPAD_TEXT_FROM_ID,
                                a_id,
                                "';",
                                END);

    // Execute the query
    l_res = l_db_server->exec_db_query(l_query);

    // Check if the requested textpad was retrieved
    if (l_db_server->has_result_one_tuple(l_res)) {

        a_text = l_db_server->db_get_w_value(l_res, 0, 0);

        // Freeing memory
        l_db_server->db_clear(l_res);

        return 1;
    }
    else {
        
        a_text = CORBA::wstring_dup(L"");

        // Freeing memory
        l_db_server->db_clear(l_res);    

        return 0;
    }
}

// Local Method:
CORBA::Long
TextpadServer_i::get_textpad_ref(void)
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    for(int i = 0; i < MAX_NUM_TEXTPADS; i++) {

        if(NULL == pr_textpad_refs[i]) {

            return i;   
        }
    }

    return ERROR; 
}

// Local Method:
CORBA::Long
TextpadServer_i::get_textpad_ref(const char *a_tp_id) {

    for(int i = 0; i < MAX_NUM_TEXTPADS; i++) {

        if((NULL != pr_textpad_refs[i]) &&
           (0 == strcmp(pr_textpad_ids[i], a_tp_id))) {
            
            return i;
        }
    }

    return ERROR;
}














