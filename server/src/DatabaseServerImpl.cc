/* $Id: DatabaseServerImpl.cc,v 1.17 2003/05/08 20:34:43 thomas Exp $ */

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

#include "DatabaseServerImpl.h"


// Constructor:
DatabaseServer_i::DatabaseServer_i()
    : Server_i(SM1)
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    // Init private members
    pr_db_name = CORBA::string_dup(DB_NAME);
    pr_db_login = CORBA::string_dup(DB_LOGIN);
    pr_db_password = CORBA::string_dup(DB_PASSWORD);
}

// Local Method:
char*
DatabaseServer_i::get_file_index() {

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    CORBA::String_var l_query;
    CORBA::String_var l_index;

    RctMutex::db_file_index.lock();
    
    // First, we need to increment the index
    pr_file_index++;

    // Creating the update query string
    l_index = UTIL::int_to_str(pr_file_index);
    
    l_query = UTIL::comp_string(UPDATE_FILE_INDEX,
                                (char *)l_index,
                                "'",
                                END);
    
    // Execute the update transaction
    exec_db_update(l_query);

    RctMutex::db_file_index.unlock();
    
    return l_index._retn();
}

// Local Method:
char*
DatabaseServer_i::get_chat_index() {

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    CORBA::String_var l_query;
    CORBA::String_var l_index;

    RctMutex::db_chat_index.lock();
    
    // First, we need to increment the index
    pr_chat_index++;

    // Creating the update query string
    l_index = UTIL::int_to_str(pr_chat_index);
    
    l_query = UTIL::comp_string(UPDATE_CHAT_INDEX,
                                (char *)l_index,
                                "'",
                                END);
    
    // Execute the update transaction
    exec_db_update(l_query);

    RctMutex::db_chat_index.unlock();
    
    return l_index._retn();
}

char*
DatabaseServer_i::get_group_index() {

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    CORBA::String_var l_query;
    CORBA::String_var l_index;

    RctMutex::db_group_index.lock();
    
    // First, we need to increment the index
    pr_group_index++;

    // Creating the update query string
    l_index = UTIL::int_to_str(pr_group_index);
    
    l_query = UTIL::comp_string(UPDATE_GROUP_INDEX,
                                (char *)l_index,
                                "'",
                                END);
    
    // Execute the update transaction
    exec_db_update(l_query);

    RctMutex::db_group_index.unlock();
    
    return l_index._retn();
}

// Local Method:
void
DatabaseServer_i::reset_group_index() {
     
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif
   
    CORBA::String_var l_query;
    CORBA::String_var l_index;
    
    RctMutex::db_group_index.lock();

    // Creating the update query string
    l_index = UTIL::int_to_str(DB_GROUP_INDEX_RESET);
    
    l_query = UTIL::comp_string(UPDATE_GROUP_INDEX,
                                (char *)l_index,
                                "'",
                                END);
    
    // Execute the update transaction
    exec_db_update(l_query);

    RctMutex::db_group_index.unlock();
}

// Local Method:
char*
DatabaseServer_i::get_textpad_index() {

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    CORBA::String_var l_query;
    CORBA::String_var l_index;

    RctMutex::db_textpad_index.lock();
    
    // First, we need to increment the index
    pr_textpad_index++;

    // Creating the update query string
    l_index = UTIL::int_to_str(pr_textpad_index);
    
    l_query = UTIL::comp_string(UPDATE_TEXTPAD_INDEX,
                                (char *)l_index,
                                "'",
                                END);
    
    // Execute the update transaction
    exec_db_update(l_query);

    RctMutex::db_textpad_index.unlock();
    
    return l_index._retn();
}


// Local Method:
char*
DatabaseServer_i::get_current_time() {

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    CORBA::String_var l_time;

    // Execute the query
    DBresult* l_res = NULL;
    l_res = exec_db_query(SELECT_CURRENT_DB_TIME);

    l_time = db_getvalue(l_res, 0, 0);

    // Delete db result
    db_clear(l_res);
    
    return l_time._retn();
}

CORBA::Long
DatabaseServer_i::get_num_tuples(const char *a_query) {

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DBresult* l_res = NULL;
    
    l_res = exec_db_query(a_query);

    CORBA::Long l_num = atoi(db_getvalue(l_res, 0, 0));

    db_clear(l_res);

    return l_num;
}

// Local Method:
void
DatabaseServer_i::exec_db_insert(const char *a_trans)
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DBresult *l_res = NULL;
    DBconn *l_conn = NULL;

    // Start connection
    l_conn = db_set_db_login(NULL, NULL,
                             NULL, NULL,
                             pr_db_name,
                             pr_db_login,
                             pr_db_password);

    // Check connection status
    db_status(l_conn, pr_db_name);

    // Start a transaction block
    l_res = db_exec(l_conn, "BEGIN");

    // Checking if transaction start was successful
    db_trans_result_status(l_res, l_conn);
    db_clear(l_res);

    // Execute the transaction INSERT TUPLE
    l_res = db_exec(l_conn, a_trans);
    db_clear(l_res);

    // Commit the transaction
    l_res = db_exec(l_conn, "COMMIT");
    db_clear(l_res);

    // Close connection
    db_finish(l_conn);
}


// Local Method:
void
DatabaseServer_i::exec_db_delete(const char *a_trans)
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DBresult *l_res = NULL;
    DBconn *l_conn = NULL;

    // Start connection
    l_conn = db_set_db_login(NULL, NULL,
                             NULL, NULL,
                             pr_db_name,
                             pr_db_login,
                             pr_db_password);

    // check connection status
    db_status(l_conn, pr_db_name);

    // Start a transaction block
    l_res = db_exec(l_conn, "BEGIN");

    // Checking if transaction start was successful
    db_trans_result_status(l_res, l_conn);
    db_clear(l_res);

    // Execute the transaction DELETE TUPLE
    l_res = db_exec(l_conn, a_trans);
    db_clear(l_res);

    // Commit the transaction
    l_res = db_exec(l_conn, "COMMIT");
    db_clear(l_res);

    // Close connection
    db_finish(l_conn);
}


// Local Method:
DBresult*
DatabaseServer_i::exec_db_query(const char *a_query)
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    // We need a result that's not destroyed when the DBQuery object
    // gets deleted
    DBresult *l_temp_res = NULL;
    DBresult *l_return_res = NULL;
    DBconn *l_conn = NULL;

    // Start connection
    l_conn = db_set_db_login(NULL, NULL,
                             NULL, NULL,
                             pr_db_name,
                             pr_db_login,
                             pr_db_password);
    
    // Starting transaction
    l_temp_res = db_exec(l_conn, "BEGIN");

    if(!db_trans_result_status(l_temp_res, l_conn)) {

        return NULL;
    }
    
    db_clear(l_temp_res);

    // Compose query string
    CORBA::String_var l_query;
    l_query = UTIL::comp_string(QUERY, a_query, END);

    // Declaring the cursor
    l_temp_res = db_exec(l_conn, l_query);

    // Checking if transaction start was successful
    if(!db_trans_result_status(l_temp_res, l_conn)) {

        return NULL;
    }
    
    db_clear(l_temp_res);
    
    // Fetching all in cursor
    l_return_res = db_exec(l_conn, "FETCH ALL in mycursor");

    // Checking if we fetched tuples successfully
    if(!db_tuple_result_status(l_return_res, l_conn)) {

        return NULL;
    }

    // Close the cursor
    l_temp_res = db_exec(l_conn, "CLOSE mycursor");
    db_clear(l_temp_res);

    // Commit the transaction
    l_temp_res = db_exec(l_conn, "COMMIT");
    db_clear(l_temp_res);
    
    // Close connection
    db_finish(l_conn);

    // Caller needs to cleanup memory
    return l_return_res;        
}

// Local Method:
void
DatabaseServer_i::exec_db_update(const char *a_trans)
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DBresult* l_res = NULL;
    DBconn *l_conn = NULL;

    // Start connection
    l_conn = db_set_db_login(NULL, NULL,
                             NULL, NULL,
                             pr_db_name,
                             pr_db_login,
                             pr_db_password);

    // Start a transaction block
    l_res = db_exec(l_conn, "BEGIN");

    // Checking if transaction start was successful
    db_trans_result_status(l_res, l_conn);
    db_clear(l_res);

    // Execute the transaction UPDATE TUPLE
    l_res = db_exec(l_conn, a_trans);
    db_clear(l_res);
    
    // Commit the transaction
    l_res = db_exec(l_conn, "COMMIT");
    db_clear(l_res);

    // Close connection
    db_finish(l_conn);
}


// Local Method:
CORBA::Boolean
DatabaseServer_i::has_result_one_tuple(DBresult *a_result)
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    if(1 == db_ntuples(a_result)) {

        return 1;
    }
    else {
        
        return 0;
    }
           
}

// Local Method:
CORBA::Boolean
DatabaseServer_i::has_result_no_tuple(DBresult *a_result)
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif
    
    if(0 == db_ntuples(a_result)) {

        return 1;
    }
    else {
        
        return 0;
    }
}

// Local Method:
void
DatabaseServer_i::get_obj_indices(void)
{
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    // This will hold the tuple from the db query
    DBresult* l_res = NULL;
    l_res = exec_db_query(SELECT_INDICES);
    CORBA::String_var l_index;
        
    if(has_result_one_tuple(l_res)) {

        // Group Index
        // After starting the server, there cannot
        // be any groups besides the devault group G0.
        // Thus we always start from G1.
        pr_group_index = DB_GROUP_INDEX_RESET;
        reset_group_index();

        // Chat Index
        l_index = db_getvalue(l_res, 0, RCT_CHAT_INDEX);
        pr_chat_index = atoi(l_index);

        // File Index
        l_index = db_getvalue(l_res, 0, RCT_FILE_INDEX);
        pr_file_index = atoi(l_index);

        // Textpad Index
        l_index = db_getvalue(l_res, 0, RCT_TEXTPAD_INDEX);
        pr_textpad_index = atoi(l_index);
    }
    else if(has_result_no_tuple(l_res)) {

        // We just return the NULL pointer since no memory has been allocated
        cerr << "ERROR: Database inconsistency, no index tuple!" << endl;
    }
    else {

        // This is the case when the query returned more than one
        // tuple, thus we have duplicate UserIDs in the DB
        // We just return the Null pointer since no memory as been allocated
        cerr << "ERROR: Database inconsistency, more then one index tuple!" << endl;
    }
    

    // Cleaning up memory
    db_clear(l_res);
}


///////////////////////
// DB WRAPPERS IMPL. //
///////////////////////
/////////////////////////////////////////////////////////////////////////////////


// Local Method:
char*
DatabaseServer_i::db_getvalue(const PGresult *a_res,
                              int a_tup_num,
                              int a_field_num)
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    CORBA::String_var value;
    
    value = (const char*)PQgetvalue(a_res, a_tup_num, a_field_num);

    return value._retn();
}

// Local Method:
CORBA::WChar*
DatabaseServer_i::db_get_w_value(const PGresult *a_res,
                                 int a_tup_num,
                                 int a_field_num)
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    char *l_db_value = PQgetvalue(a_res, a_tup_num, a_field_num);
    
    // Getting the string length of the chat message
    int l_str_size = mbstowcs(NULL, l_db_value, 0) + 1;

    CORBA::WChar* l_ret_value = CORBA::wstring_alloc(l_str_size);
    
    if(-1 == mbstowcs(l_ret_value, l_db_value, l_str_size)) {

        cerr << "ERROR: In multi byte to wide string conversion!" << endl;
    }

    return l_ret_value;
}

// Local Method:
int
DatabaseServer_i::db_ntuples(const PGresult *a_res)
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    return PQntuples(a_res);
}

// Local Method:
int
DatabaseServer_i::db_nfields(const PGresult *a_res)
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    return PQnfields(a_res);
}

// Local Method:
char*
DatabaseServer_i::db_fname(const PGresult *a_res,
                           int a_field_index)
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    CORBA::String_var fname;

    fname = (const char*) PQfname(a_res, a_field_index);

    return fname._retn();
}

// Local Method:
int
DatabaseServer_i::db_getisnull(const PGresult *a_res,
                               int a_tup_num,
                               int a_field_num)
{
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    return PQgetisnull(a_res, a_tup_num, a_field_num);
}


// Local Method:
int
DatabaseServer_i::db_fnumber(const PGresult *a_res,
                             const char *a_field_name)
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    return PQfnumber(a_res, a_field_name);
}


// Local Method:
int
DatabaseServer_i::db_getlength(const PGresult *a_res,
                               int a_tup_num,
                               int a_field_num)
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    return PQgetlength(a_res, a_tup_num, a_field_num);
}




// Local Method:
void
DatabaseServer_i::db_finish(PGconn *a_conn)
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    PQfinish(a_conn);  
}


// Local Method:
PGconn*
DatabaseServer_i::db_set_db_login(const char *a_host,
                                  const char *a_port,
                                  const char *a_options,
                                  const char *a_tty,
                                  const char *a_db_name,
                                  const char *a_db_login,
                                  const char *a_db_password)
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    return PQsetdbLogin(a_host,
                        a_port,
                        a_options,
                        a_tty,
                        a_db_name,
                        a_db_login,
                        a_db_password);
}


// Local Method:
void
DatabaseServer_i::db_status(PGconn *a_conn, const char *a_db_name)
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    if(PQstatus(a_conn) == CONNECTION_BAD) {
        
        cerr << "ERROR: Connection to database >" << a_db_name << "< failed." << endl;
        cerr << "ERROR: " << PQerrorMessage(a_conn) << endl;

        PQfinish(a_conn);
        exit(1);
    }
}


// Local Method:
PGresult*
DatabaseServer_i::db_exec(PGconn *a_conn, const char* a_trans)
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    return PQexec(a_conn, a_trans);
}


// Local Method:
CORBA::Boolean
DatabaseServer_i::db_tuple_result_status(PGresult *a_res, PGconn *a_conn)
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif
    
    if(!a_res || PQresultStatus(a_res) != PGRES_TUPLES_OK) {

        PQclear(a_res);
        PQfinish(a_conn);
        return 0;
    }
    else {

        return 1;
    }
}

// Local Method:
CORBA::Boolean
DatabaseServer_i::db_result_tuples_ok(PGresult *a_res) {
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    if(!a_res || PQresultStatus(a_res) != PGRES_TUPLES_OK) {

        PQclear(a_res);
        a_res = NULL;
        return 0;
    }
    else {

        PQclear(a_res);
        a_res = NULL;
        return 1;
    }
}

// Local Method:
CORBA::Boolean
DatabaseServer_i::db_trans_result_status(PGresult *a_res, PGconn *a_conn)
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    if(!a_res || PQresultStatus(a_res) != PGRES_COMMAND_OK) {

        PQclear(a_res);
        PQfinish(a_conn);
        return 0;
    }
    else {

        return 1;
    }
}


// Local Method:
void
DatabaseServer_i::db_clear(PGresult *a_res)
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

        PQclear(a_res);
}

// Local Method:
char*
DatabaseServer_i::db_escape_string(const char* a_from, size_t a_length)
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    char* l_to = new char[(a_length * 2) + 1];
    
    PQescapeString(l_to, a_from, a_length);

    return l_to;
}



// Local Method:
char*
DatabaseServer_i::db_escape_wstring(const char* a_from, size_t a_length)
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    char* l_to = new char[(a_length * 2) + 1];
    
    PQescapeString(l_to, a_from, a_length);

    return l_to;
}

