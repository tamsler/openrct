/* $Id: Util.h,v 1.6 2003/05/16 16:08:25 thomas Exp $ */

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

#ifndef __UTIL_H__
#define __UTIL_H__

#include <iostream>
#include <string>
#include <stdlib.h>
#include <stdarg.h>
#include <stdio.h>
#include "RctCorba.h"
#include "Const.h"
#include "DatabaseServerImpl.h"


#define END (char *)NULL


class UTIL
{

private:

    // Object instance:
    static UTIL *pr_util;

    // Constructor:
    inline UTIL() { }

    
public:

    // Destructor:
    inline ~UTIL() { }

    // Return class instance
    static UTIL* instance();

    // Local Method
    // String composer method
    // Usage: UTIL::comp_string("Hello", somestring_var, "RCT", END);
    // Note: You have to terminate the argument list with an END token.
    static char* comp_string(const char *a_first, ...);

    // Local Method:
    // Convert integer to a string
    static char* int_to_str(CORBA::Long a_number);

    // Local Method:
    // Convert float to a string
    static char* float_to_str(CORBA::Float a_number);

    // Local Method:
    // Convert a string to multi byte and db escape it
    // The caller must dealocate the returned value
    static char* make_multi_byte_esc(const CORBA::WChar *str);
};

#endif // __UTIL_H__



