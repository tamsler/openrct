/* $Id: UrlServerImpl.h,v 1.4 2003/05/08 20:34:43 thomas Exp $ */

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

#ifndef __URLSERVERIMPL_H__
#define __URLSERVERIMPL_H__

#include "UrlServer.hh"
#include "ServerImpl.h"
#include "PermissionObj.hh"
#include "UserServerImpl.h"
#include "Const.h"
#include "Util.h"
#include "MakeSingle.h"


using namespace RCT;

class UrlServer_i : public POA_RCT::UrlServer,
                    public Server_i
{

protected:

    // Constructor:
    inline UrlServer_i() : Server_i(SM1) { }

    // Destructor:
    virtual inline ~UrlServer_i() { }


public:

    // IDL Method:
    virtual void send(const CORBA::WChar *a_url,
                      const char *a_version,
                      const char *a_user_id,
                      const char *a_user_alias,
                      ObjPermission a_permission,
                      const char *a_class_name,
                      const char *a_assembly_name,
                      CORBA::Long a_type);
};

typedef MakeSingle<UrlServer_i> UrlServerS;

#endif // __URLSERVERIMPL_H__
