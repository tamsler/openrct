/* $Id: NameServerUtil.h,v 1.8 2003/05/08 20:34:43 thomas Exp $ */

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

#ifndef __NAMESERVERUTIL_H__
#define __NAMESERVERUTIL_H__

#include <iostream>
#include "RctCorba.h"
#include <CosNotifyShorthands.h>
#include "Const.h"


using namespace std;

// This class is used to deal with the NameService
// setup, configuratin, and management of context and objects

class NameServerUtil {

private:

    // Access point to the Name Server
    CosNaming::NamingContext_var pr_ns_context;

    // Root context RCT.RCTD
    CosNaming::NamingContext_var pr_rct_context;

    
public:

    // Constructor:
    NameServerUtil(CORBA::ORB_ptr a_orb);

    // Destructor:
    inline ~NameServerUtil() { }

    // Method:
    // This will bind an object to a nome in on the NameServer
    CORBA::Boolean bind_object_to_name(CORBA::Object_ptr a_obj,
                                       const char *a_obj_id,
                                       const char *a_obj_kind);

    // Method:
    // This will get an event channel ref
    CosNA::EventChannel_ptr get_chan_from_ns(const char *a_channel_id,
                                            const char *a_channel_kind);

    // Method:
    // This will get the event channel's factory ref
    CosNA::EventChannelFactory_ptr get_chan_fact_from_ns(const char *a_fact_id,
                                                         const char *a_fact_kind);
};


#endif // __NAMESERVERUTIL_H__




