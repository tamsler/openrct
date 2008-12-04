/* $Id: AuthenticationServerImpl.h,v 1.15 2003/05/08 20:34:43 thomas Exp $ */

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

#ifndef __AUTHENTICATIONSERVERIMPL_H__
#define __AUTHENTICATIONSERVERIMPL_H__

#include <iostream>
#include "RctCorba.h"
#include "RctMutex.h"
#include "ServerImpl.h"
#include "AuthenticationServer.hh"
#include "DatabaseServerImpl.h"
#include "SessionServerImpl.h"
#include "NameServerUtil.h"
#include "UserServerImpl.h"
#include "TeamServerImpl.h"
#include "PingServerImpl.h"
#include "Const.h"
#include "Util.h"
#include "MakeSingle.h"

//using namespace RCT;


class AuthenticationServer_i : public POA_RCT::AuthenticationServer,
                               public Server_i
{

private:

    // Local Method:
    // Set the users logoff time/date
    void set_logoff_time(const char *a_user_id,
                         const char *a_user_on,
                         CORBA::Boolean a_status);
    
protected:
    
    // Constructor:
    inline AuthenticationServer_i() : Server_i(SM1) { }

    // Destructor:      
    virtual inline ~AuthenticationServer_i() { }


public:

    // Local Method:
    // Return class instance
    static AuthenticationServer_i* instance(void);

    // IDL Method:
    // Returning the authentication result
    virtual AuthResponse* authenticate(const char *a_login,
                                       const char *a_password,
                                       CORBA::Long a_auth_level,
                                       const char *a_version);

    // IDL Method:
    virtual char* login(CORBA::Long a_type,
                        const char *a_user_id,
                        const char *a_user_alias,
                        const char *a_first_name,
                        const char *a_last_name,
                        const char *a_data,
                        const char *a_ip,
                        const char *a_os,
                        const char *a_version);

    // IDL Method:
    virtual void logout(CORBA::Long a_type,
                        const char *a_user_alias,
                        const char *a_password,
                        const char *a_msg,
                        const char *a_user_on, // Time stamp when user logged in
                        const char *a_version,
                        CORBA::Boolean a_status);

};

typedef MakeSingle<AuthenticationServer_i> AuthenticationServerS;

#endif //__AUTvel-ICATIONSERVERIMPL_H__







