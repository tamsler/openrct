/* $Id: Util.cc,v 1.5 2003/05/16 16:08:25 thomas Exp $ */

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

#include "Util.h"

// Init object instance
UTIL* UTIL::pr_util = 0;

// Local Method:
UTIL*
UTIL::instance()
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    if(0 == pr_util) {

        pr_util = new UTIL();

    }

    return pr_util;
}


// Local Method:
char*
UTIL::comp_string(const char *a_first, ...)
{
 
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif
    
    CORBA::Long l_len;
    CORBA::String_var l_retbuf;
    va_list l_argp;
    char *l_p;
    
    if(NULL == a_first) {
        
        return NULL;
    }

    l_len = strlen(a_first);

    va_start(l_argp, a_first);

    while((l_p = va_arg(l_argp, char *)) != NULL) {
        
        l_len += strlen(l_p);
    }

    va_end(l_argp);

    l_retbuf = CORBA::string_alloc(l_len);

    if(NULL == l_retbuf) {
        
        return NULL;
    }

    (void)strcpy(l_retbuf, a_first);

    va_start(l_argp, a_first);

    while((l_p = va_arg(l_argp, char *)) != NULL) {
        
        (void)strcat(l_retbuf, l_p);
    }

    va_end(l_argp);

    return l_retbuf._retn();
}

// Local Method:
char*
UTIL::int_to_str(CORBA::Long a_number)
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    CORBA::String_var l_num;

    l_num = CORBA::string_alloc(VSS);

    sprintf(l_num, "%d", a_number);

    return l_num._retn();
}

// Local Method:
char*
UTIL::float_to_str(CORBA::Float a_number)
{

    CORBA::String_var l_num;

    l_num = CORBA::string_alloc(VSS);

    sprintf(l_num, "%f", a_number);

    return l_num._retn();
}

// Local Method:
char*
UTIL::make_multi_byte_esc(const CORBA::WChar* a_str) 
{
  
  DatabaseServerS *l_db_server = DatabaseServerS::instance();
  
  // Getting the string length
  int l_str_size = wcstombs(NULL, a_str, 0) + 1;

  // Create the multi byte string
  char l_str_multi_byte[l_str_size];

  if(-1 <= wcstombs(l_str_multi_byte, a_str, l_str_size)) {
        
        cerr << "ERROR: Could not convert a character!" << endl;
  }
  
  // Make the string safe fore db entry, escape it
  char* l_str_multi_byte_esc =
    l_db_server->db_escape_wstring(l_str_multi_byte,
				   l_str_size - 1);
  
  return l_str_multi_byte_esc;
}




