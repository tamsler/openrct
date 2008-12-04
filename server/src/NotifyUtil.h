/* $Id: NotifyUtil.h,v 1.8 2003/05/08 20:34:43 thomas Exp $ */

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

#ifndef __NOTIFYUTIL_H__
#define __NOTIFYUTIL_H__

#include <iostream>
#include <fstream>
#include "RctCorba.h"
#include <CosNotifyShorthands.h>
#include "NameServerUtil.h"
#include "Const.h"


typedef _CORBA_Unbounded_Sequence<CosNF::Filter_var> FilterSeq;


class NotifyUtil {

private:

    // Access piont to the name server
    NameServerUtil *pr_name_server_util;

public:

    // Constructor:
    NotifyUtil(CORBA::ORB_ptr a_orb);

    // Destructor:
    inline ~NotifyUtil() { }


    // Method:
    // Creating a proxy consumer
    CosNA::ProxyConsumer_ptr get_proxy_push_consumer(CORBA::ORB_ptr a_orb,
                                                    CosNA::EventChannel_ptr a_channel,
                                                    CosNA::ClientType a_client_type,
                                                    const char *a_admin_ior_file,
                                                    CosNA::SupplierAdmin_ptr& a_admin);

    // Method:
    // Creating a proxy supplier
    CosNA::ProxySupplier_ptr get_proxy_push_supplier(CORBA::ORB_ptr a_orb,
                                                    CosNA::EventChannel_ptr a_channel,
                                                    CosNA::ClientType a_client_type,
                                                    const char *a_admin_ior_file,
                                                    CosNA::ConsumerAdmin_ptr& a_admin);

    
    // Method:
    // Writing an object's IOR to a file
    void write_ior_to_file(CORBA::ORB_ptr a_orb,
                           CORBA::Object_ptr a_obj,
                           const char* a_filename);

    // Method:
    // This helper routine adds a filter to a FilterAdmin object
    // (which could be a supplier or consumer proxy or admin).
    // Returns pointer to filter, or nil on error
    CORBA::Boolean sample_add_filter(CosNA::EventChannel_ptr a_channel, 
                                     CosNF::FilterAdmin_ptr a_filter_admin,
                                     CosN::EventTypeSeq& a_event_type_seq,
                                     const char* a_constraint_expr,
                                     const char* a_obj_name,
                                     CosNF::Filter_ptr& a_filter);

    // Method:
    // Destroy a filter
    void destroy_filters(FilterSeq& a_filter_seq);

    // Method:
    // Create new channels
    CosNA::EventChannel_ptr create_event_channel(const char *a_channel_id,
                                                 const char *a_channel_kind,
                                                 const char *a_fact_id,
                                                 const char *a_fact_kind);
};


#endif // __NOTIFYUTIL_H__




