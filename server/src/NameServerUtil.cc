/* $Id: NameServerUtil.cc,v 1.7 2003/05/08 20:34:43 thomas Exp $ */

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

#include "NameServerUtil.h"


// Constructor:
NameServerUtil::NameServerUtil(CORBA::ORB_ptr a_orb)
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    // Holds the ref to the name server
    CORBA::Object_var l_obj;

    try {
        
        // Obtain a reference to the root context of the Name service:
        l_obj = a_orb->resolve_initial_references("NameService");

        // Narrow the reference returned.
        pr_ns_context = CosNaming::NamingContext::_narrow(l_obj);

        if( CORBA::is_nil(pr_ns_context) ) {
            
            cerr << "Failed to narrow the root naming context." << endl;
        }
    }
    catch(CORBA::ORB::InvalidName& ex) {
        
        cerr << "Service required is invalid [does not exist]." << endl;
    }

    try {
        
        // Bind a context called "RCT.RCTD/" to the root context:
        CosNaming::Name l_name;
        l_name.length(1);
        l_name[0].id   = NSROOTCONTEXTID;
        l_name[0].kind = NSROOTCONTEXTKIND;
        
        try {
            
            // Bind the context to root.
            pr_rct_context = pr_ns_context->bind_new_context(l_name);
        }

        catch(CosNaming::NamingContext::AlreadyBound& ex) {
            
            // If the context already exists, this exception will be raised.
            // In this case, just resolve the name and assign testContext
            // to the object returned:
            l_obj = pr_ns_context->resolve(l_name);
            pr_rct_context = CosNaming::NamingContext::_narrow(l_obj);
            
            if(CORBA::is_nil(pr_rct_context)) {
                
                cerr << "Failed to narrow naming context." << endl;
            }
        }
    }
    catch(CORBA::COMM_FAILURE& ex) {
        
        cerr << "Caught system exception COMM_FAILURE -- unable to contact the "
             << "naming service." << endl;
    }
    catch(CORBA::SystemException&) {
        
        cerr << "Caught a CORBA::SystemException while using the naming service."
             << endl;
    }
}


// Method:
CORBA::Boolean
NameServerUtil::bind_object_to_name(CORBA::Object_ptr a_obj,
                                    const char *a_obj_id,
                                    const char *a_obj_kind)
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    try {

        // binding object to the name server
        CosNaming::Name l_name;
        l_name.length(1);
        l_name[0].id   = a_obj_id;
        l_name[0].kind = a_obj_kind;

        try {
            
            pr_rct_context->bind(l_name, a_obj);
        }
        catch(CosNaming::NamingContext::AlreadyBound& ex) {
            
            pr_rct_context->rebind(l_name, a_obj);
        }

    }
    catch(CORBA::COMM_FAILURE& ex) {
        
        cerr << "Caught system exception COMM_FAILURE -- unable to contact the "
             << "naming service." << endl;
        return 0;
    }
    catch(CORBA::SystemException&) {
        
        cerr << "Caught a CORBA::SystemException while using the naming service."
             << endl;
        return 0;
    }

    return 1;  
}



// Method:
CosNA::EventChannel_ptr
NameServerUtil::get_chan_from_ns(const char *a_channel_id,
                                 const char *a_channel_kind)
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif
    
    // Setting the channel to a nil channel so that we can
    // return it in case of an error
    CosNA::EventChannel_ptr l_channel = CosNA::EventChannel::_nil();
 
    // Check that a_channel_id and a_channel_kind are not NULL pointers and have valid length
    if (NULL == a_channel_id || NULL == a_channel_kind ||
        0 == strlen(a_channel_id) || 0 ==strlen(a_channel_kind)) {
        
        // we just return the nil pointer
        return l_channel;
    }

    // Prepage the CosNaming structure
    CosNaming::Name l_name;
    l_name.length(1);
    l_name[0].id   = a_channel_id;
    l_name[0].kind = a_channel_kind;

    try {
        
        // pr_ns_context has the NameServer info
        CORBA::Object_var l_obj = pr_ns_context->resolve(l_name);
        l_channel = CosNA::EventChannel::_narrow(l_obj);

        // Check if we got a channel
        if(CORBA::is_nil(l_channel)) {
            
            cerr << "Failed to narrow object found in naming service " <<
                " to type CosNotifyChannelAdmin::EventChannel" << endl;
            return l_channel; // failure
        }
    }
    catch(CORBA::ORB::InvalidName& ex) {
        
        cerr << "Invalid name" << endl;
        return l_channel; // failure
    }
    catch(CORBA::COMM_FAILURE& ex) {
        
        cerr << "Caught system exception COMM_FAILURE while resolving event channel name" << endl;
        return l_channel; // failure
    }
    catch(...) {
        
        cerr << "Caught exception while resolving event channel name" << endl;
        return l_channel; // failure
    }

    return l_channel; // success
}


// Method:
CosNA::EventChannelFactory_ptr
NameServerUtil::get_chan_fact_from_ns(const char *a_fact_id,
                                      const char *a_fact_kind)
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    // Setting the facory to a nil factory so that we can
    // return it in case of an error
    CosNA::EventChannelFactory_ptr l_factory = CosNA::EventChannelFactory::_nil();

    // Check that a_fact_id and a_fact_kind are not NULL pointers and have valid length
    if (NULL == a_fact_id || NULL == a_fact_kind ||
        0 == strlen(a_fact_id) || 0 ==strlen(a_fact_kind)) {
        
        // we just return the nil pointer
        return l_factory;
    }

    // Prepage the CosNaming structure
    CosNaming::Name l_name;
    l_name.length(1);
    l_name[0].id   = a_fact_id;
    l_name[0].kind = a_fact_kind;

    try {
        
        // pr_ns_context has the NameServer info
        CORBA::Object_var l_obj = pr_ns_context->resolve(l_name);
        l_factory = CosNA::EventChannelFactory::_narrow(l_obj);
        
        if(CORBA::is_nil(l_factory)) {
            
            cerr << "Failed to narrow object found in naming service "
                 << " to type CosNA_EventChannelFactory::EventChannelFactory" << endl;
            return l_factory; // failure
        }
    }
    catch(CORBA::ORB::InvalidName& ex) {
        
        cerr << "Invalid name" << endl;
        return l_factory; // failure
    }
    catch(CORBA::COMM_FAILURE& ex) {
        
        cerr << "Caught system exception COMM_FAILURE while "
             << "resolving event channel factoryname" << endl;
        return l_factory; // failure
    }
    catch(...) {
        
        cerr << "Caught exception while resolving event channel factory name" << endl;
        return l_factory; // failure
    }
    
    return l_factory;
}










