/* $Id: DatabaseServerImpl.h,v 1.18 2003/05/08 20:34:43 thomas Exp $ */

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

#ifndef __DATABASESERVERIMPL_H__
#define __DATABASESERVERIMPL_H__

#include <iostream>
#include <string>
#include "RctMutex.h"
#include "Types.h"
#include "DatabaseServer.hh"
#include "ServerImpl.h"
#include "Util.h"
#include "MakeSingle.h"


//using namespace RCT;
using namespace std;

class DatabaseServer_i : public POA_RCT::DatabaseServer,
                         public Server_i
{

private:

    // String holding the db name
    CORBA::String_var pr_db_name;

    // String holding the db login
    CORBA::String_var pr_db_login;

    // String holding the db password
    CORBA::String_var pr_db_password;

    // These vars hold the current index for the various obj
    int pr_file_index;
    int pr_chat_index;
    int pr_group_index;
    int pr_textpad_index;
    
protected:

    // Constructor: Singleton
    DatabaseServer_i();

    // Destructor
    inline virtual ~DatabaseServer_i() { }
    
    
public:

    // Local Method:
    char* get_file_index();

    // Local Method:
    char* get_chat_index();

    // Local Method:
    char* get_group_index();

    // Local Method:
    void reset_group_index();

    // Local Method:
    char* get_textpad_index();

    // Local Method:
    // Getting the DB's current time
    char* get_current_time();

    // Local Method:
    // This method takes a query that specifically counts
    // tuples and returns the number of tuples
    CORBA::Long get_num_tuples(const char *a_query);
    
    // Local Method:
    // Execute query with provided query string
    DBresult* exec_db_query(const char *a_query);

    // Local Method:
    // Update a tuple 
    void exec_db_update(const char *a_trans);

    // Local Method:
    // Insert a tuple
    void exec_db_insert(const char *a_trans);

    // Local Method:
    // Delete one or several tuples
    // depending on the transaction
    void exec_db_delete(const char *a_trans);
    
    // Local Method:
    // Check if query result has no tuples
    CORBA::Boolean has_result_no_tuple(DBresult *a_result);

    // Local Method:
    // Check if query result has only one tuple
    CORBA::Boolean has_result_one_tuple(DBresult *a_result);

    // Local Method:
    // Getting the indices for all the objects
    // and init the servers ID generator
    void get_obj_indices(void);

    
    /////////////////
    // DB Wrappers //
    /////////////////
    //////////////////////////////////////////////////////////////////////////

    
    // Local Method:
    // DB Wrapper
    // Returns a single field (attribute) value of one tuple of
    // a PGresult. Tuple and field indices start at 0
    char* db_getvalue(const PGresult *a_res,
                      int a_tup_num,
                      int a_field_num);

    // Local Method:
    // DB Wrapper
    // Returns a single field as a UNICODE string
    CORBA::WChar* db_get_w_value(const PGresult *a_res,
                                 int a_tup_num,
                                 int a_field_num);

    // Local Method:
    // DB Wrapper
    // Returns the number of tuples (rows) in the query result
    int db_ntuples(const PGresult *a_res);

    // Local Method:
    // DB Wrapper
    // Returns the number of fields (attributes) in each tuple
    // of the query result
    int db_nfields(const PGresult *a_res);

    // Local Method:
    // DB Wrapper
    // Returns the field (attribute) name associated with the
    // given field index. Field indices start at 0
    char* db_fname(const PGresult *a_res,
                   int a_field_index);

    // Local Method:
    // DB Wrapper
    // PQgetisnull Tests a field for a NULL entry. Tuple and
    // field indices start at 0. This function returns 1 if
    // the field contains a NULL, 0 if it contains a non-null
    // value. (Note that db_getvalue will return an empty string,
    // not a null pointer, for a NULL field.)
    int db_getisnull(const PGresult *a_res,
                     int a_tup_num,
                     int a_field_num);

      
    

    // Local Method:
    // DB Wrapper:
    // Returns the field (attribute) index associated with the
    // given field name. -1 is returned if the given name does
    // not match any field.
    int db_fnumber(const PGresult *a_res,
                   const char *a_field_name);


    // Local Method:
    // DB Wrapper:
    // PQgetlength Returns the length of a field (attribute) in
    // bytes. Tuple and field indices start at 0. This is the
    // actual data length for the particular data value, that is
    // the size of the object pointed to by PQgetvalue. Note that
    // for ASCII-represented values, this size has little to do
    // with the binary size reported by PQfsize.
    int db_getlength(const PGresult *a_res,
                     int a_tup_num,
                     int a_field_num);

      
    
    // Local Method:
    // DB Wrapper
    // Close the connection to the backend. Also frees memory used
    // by the PGconn object. Note that even if the backend connection
    // attempt fails (as indicated by PQstatus), the application should
    // call PQfinish to free the memory used by the PGconn object. The
    // PGconn pointer should not be used after PQfinish has been called.
    void db_finish(PGconn *a_conn);

    
    // Local Method:
    // DB Wrapper
    // Makes a new connection to the database server
    PGconn* db_set_db_login(const char *a_host,
                            const char *a_port,
                            const char *a_options,
                            const char *a_tty,
                            const char *a_db_name,
                            const char *a_db_login,
                            const char *a_db_password);

    // Local Method:
    // DB Wrapper
    // Returns the status of the connection. The status can be one
    // of a number of values. However, only two of these are seen
    // outside of an asynchronous connection procedure - CONNECTION_OK
    // or CONNECTION_BAD. A good connection to the database has the status
    // CONNECTION_OK. A failed connection attempt is signaled by status
    // CONNECTION_BAD. Ordinarily, an OK status will remain so until
    // PQfinish, but a communications failure might result in the status
    // changing to CONNECTION_BAD prematurely. In that case the application
    //could try to recover by calling PQreset. 
    void db_status(PGconn *a_conn, const char *a_db_name);

    // Local Method:
    // DB Wrapper
    // Submit a query to Postgres and wait for the result. Returns a PGresult
    // pointer or possibly a NULL pointer. A non-NULL pointer will generally
    // be returned except in out-of-memory conditions or serious errors such
    // as inability to send the query to the backend. If a NULL is returned,
    // it should be treated like a PGRES_FATAL_ERROR result. Use PQerrorMessage
    //to get more information about the error.
    PGresult* db_exec(PGconn *a_conn, const char* a_trans);

    // Local Method:
    // DB Wrapper
    // RCT method to check if query is ok
    CORBA::Boolean db_result_tuples_ok(PGresult *a_res);
    
    // Local Method:
    // DB Wrapper
    CORBA::Boolean db_trans_result_status(PGresult *a_res, PGconn *a_conn);

    // Local Method:
    // DB Wrapper
    CORBA::Boolean db_tuple_result_status(PGresult *a_res, PGconn *a_conn);

    // Local Method:
    // DB Wrapper
    // Frees the storage associated with the PGresult. Every query
    // result should be freed via PQclear when it is no longer needed. 
    void db_clear(PGresult *a_res);

    // Local Method:
    // DB Wrapper
    // This method escapes a string to make it save for db entry
    // The caller has to free the returned string
    char* db_escape_string(const char* a_from, size_t a_length);

    // Local Method:
    // DB Wrapper
    // This method escapes a wide string to make it save for db entry
    // The caller has to free the returned string
    char* db_escape_wstring(const char* a_from, size_t a_length);
};

typedef MakeSingle<DatabaseServer_i> DatabaseServerS;

#endif // __DATABASESERVERIMPL_H__









