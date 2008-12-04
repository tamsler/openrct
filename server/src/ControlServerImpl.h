// $Id: ControlServerImpl.h,v 1.8 2003/05/08 20:34:43 thomas Exp $

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

#ifndef __CONTROLSERVER_H__
#define __CONTROLSERVER_H__

#include <iostream>
#include "ControlServer.hh"
#include "ServerImpl.h"
#include "ControlMessageImpl.h"
#include "DatabaseServerImpl.h"
#include "ClassServerImpl.h"
#include "TeamServerImpl.h"
#include "GroupServerImpl.h"
#include "Const.h"
#include "MakeSingle.h"


using namespace RCT;

class ControlServer_i : public POA_RCT::ControlServer,
                        public Server_i
{

protected:

    // Constructor:
    inline ControlServer_i() : Server_i(SM1) { }

    // Destructor
    virtual inline ~ControlServer_i() { }

public:

    // Local Method:
    // Checking if there are users in a specific
    // control queue
    CORBA::Boolean is_empty(const char *a_class_name,
                            const char *a_assembly_name,
                            const char *a_module_name,
                            CORBA::Long a_assembly_type);
    
    // Local Method:
    // Check if user is in module queue
    CORBA::Boolean is_user_in_queue(const char *a_class_id,
                                    const char *a_assembly_id,
                                    const char *a_user_id,
                                    const char *a_module_name);
    
    // IDL Method:
    virtual void request(const char *a_class_name,
                         const char *a_assembly_name,
                         const char *a_user_alias,
                         const char *a_user_id,
                         const char *a_module_name,
                         CORBA::Long a_type);

    // IDL Method:
    virtual void release(const char *a_class_name,
                         const char *a_assembly_name,
                         const char *a_user_alias,
                         const char *a_user_id,
                         const char *a_module_name,
                         CORBA::Long a_type);

    // IDL Method:
    virtual void take(const char *a_class_name,
                      const char *a_assembly_name,
                      const char *a_user_alias,
                      const char *a_user_id,
                      const char *a_module_name,
                      CORBA::Long a_type)
        throw(CORBA::SystemException, RCT::ControlServer::QueueIsEmpty);

    // IDL Method:
    virtual void cancel_request(const char *a_class_name,
                                const char *a_assembly_name,
                                const char *a_user_alias,
                                const char *a_user_id,
                                const char *a_module_name,
                                CORBA::Long a_type);

    // IDL Method:
    virtual void exit_request(const char *a_class_name,
                              const char *a_assembly_name,
                              const char *a_user_alias,
                              const char *a_user_id,
                              const char *a_module_name,
                              CORBA::Long a_type);
};

typedef MakeSingle<ControlServer_i> ControlServerS;
    

#endif // __CONTROLSERVER_H__







