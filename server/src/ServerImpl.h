/* $Id: ServerImpl.h,v 1.9 2004/10/23 06:06:46 thomas Exp $ */

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

#ifndef __SERVERIMPL_H__
#define __SERVERIMPL_H__

#include <iostream>
#include "RctCorba.h"
#include "Server.hh"
#include "Const.h"

using namespace std;

class Server_i :
    public POA_RCT::Server,
    public PortableServer::RefCountServantBase
{

private:
    
    // String for server status
    CORBA::String_var pr_server_status;

public:

    // Constructor:
    Server_i(const char *a_status = SM1);

    // Destructor:
    inline virtual ~Server_i() { }
    
    // IDL Method:
    // Returning the server's status
    virtual char* get_status(void);

    // IDL Method:
    // Setting the server's status
    virtual void set_status(const char *a_status);
};    

#endif // __SERVERIMPL_H__



















