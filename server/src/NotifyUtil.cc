/* $Id: NotifyUtil.cc,v 1.8 2003/05/08 20:34:43 thomas Exp $ */

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

#include "NotifyUtil.h"


// Constructor:
NotifyUtil::NotifyUtil(CORBA::ORB_ptr a_orb)
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif 

    // Allocating memory ofr the name server util
    pr_name_server_util = new NameServerUtil(a_orb);
}


// Method:
CosNA::ProxyConsumer_ptr
NotifyUtil::get_proxy_push_consumer(CORBA::ORB_ptr a_orb,
                                    CosNA::EventChannel_ptr a_channel,
                                    CosNA::ClientType a_client_type,
                                    const char* a_admin_ior_file,
                                    CosNA::SupplierAdmin_ptr& a_admin)
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif 

    // Local variables
    CosNA::ProxyConsumer_ptr l_generic_proxy = CosNA::ProxyConsumer::_nil();
    CosNA::InterFilterGroupOperator l_int_file_grp_op = CosNA::AND_OP;
    CosNA::AdminID l_admin_id;
    CosNA::ProxyID l_proxy_id;

    a_admin = CosNA::SupplierAdmin::_nil();

    try {

        a_admin = a_channel->new_for_suppliers(l_int_file_grp_op, l_admin_id);

    } catch(...) {
        
        cerr << "Exception: channel->new_for_suppliers(...)" << endl;
    }

    if(CORBA::is_nil(a_admin)) {
        
        cerr << "Failed to obtain admin" << endl;
        return l_generic_proxy;
    }

    try {

        l_generic_proxy = a_admin->obtain_notification_push_consumer(a_client_type, l_proxy_id);
        
    } catch(...) {
        
        cerr << "Exception, a_admin->obtain_notification_push_consume(...)" << endl;
    }

    if(CORBA::is_nil(l_generic_proxy)) {

        cerr << "Failed to obtain proxy" << endl;

        try {
            
            a_admin->destroy();
            
        } catch(...) {
            
            cerr << "Exception: a_admin->destroy()" << endl;
        }

        a_admin = CosNA::SupplierAdmin::_nil();

        return l_generic_proxy;
    }

    

    write_ior_to_file(a_orb, a_admin, a_admin_ior_file);

    // success if l_generic_proxy is non-nil, otherwise failure
    return l_generic_proxy;
}


// Method:
CosNA::ProxySupplier_ptr
NotifyUtil::get_proxy_push_supplier(CORBA::ORB_ptr a_orb,
                                    CosNA::EventChannel_ptr a_channel,
                                    CosNA::ClientType a_client_type,
                                    const char* a_admin_ior_file,
                                    CosNA::ConsumerAdmin_ptr& a_admin)
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif 

    // Local variables
    CosNA::ProxySupplier_ptr l_generic_proxy = CosNA::ProxySupplier::_nil();
    CosNA::InterFilterGroupOperator l_int_file_grp_op = CosNA::AND_OP;
    CosNA::AdminID l_admin_id;
    CosNA::ProxyID l_proxy_id;


    a_admin = CosNA::ConsumerAdmin::_nil();

    try {
        
        a_admin = a_channel->new_for_consumers(l_int_file_grp_op, l_admin_id);
        if ( CORBA::is_nil(a_admin) ) {
            
            cerr << "Failed to obtain admin" << endl;
            return l_generic_proxy; // failure
        }
        
    } catch(...) {
        
        cerr << "Exception: Failed to obtain admin" << endl;
        return l_generic_proxy;  // failure
    }

    try {

        l_generic_proxy = a_admin->obtain_notification_push_supplier(a_client_type, l_proxy_id);
        
    } catch(...) {
        
        cerr << "Exception: a_admin->obtain_notification_push_supplier(...)" << endl;
    }

    if (CORBA::is_nil(l_generic_proxy)) {
        
        cerr << "Failed to obtain proxy" << endl;

        try {
            
            a_admin->destroy();
            
        } catch(...) {
            
            cerr << "Exception: a_admin->destroy()" << endl;
        }

        a_admin = CosNA::ConsumerAdmin::_nil();
        return l_generic_proxy;
    }
    
    write_ior_to_file(a_orb, a_admin, a_admin_ior_file);

    // success if l_generic_proxy is non-nil, otherwise failure
    return l_generic_proxy;
}


// Method:
void
NotifyUtil::write_ior_to_file(CORBA::ORB_ptr a_orb,
                              CORBA::Object_ptr a_obj,
                              const char* a_filename)
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif 

    
    if(NULL == a_filename || 0 == strlen(a_filename)) {
        
        return;
    }

    ofstream l_file(a_filename, ios::out);

    if(!l_file) {
        
        return;
    }

    char* l_ior_name = a_orb->object_to_string(a_obj);

    l_file << l_ior_name;

    l_file.close();

    delete [] l_ior_name;
}


// Method:
CORBA::Boolean
NotifyUtil::sample_add_filter(CosNA::EventChannel_ptr a_channel, 
                              CosNF::FilterAdmin_ptr a_filter_admin,
                              CosN::EventTypeSeq& a_event_type_seq,
                              const char* a_constraint_expr,
                              const char* a_obj_name,
                              CosNF::Filter_ptr& a_filter)
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif
    
    // if a_event_type_seq and constraint expr are empty, we ignore them + do not add a filter
    if((0 == a_event_type_seq.length()) &&
       (NULL == a_constraint_expr || (0 == strlen(a_constraint_expr)))) {
        
            return 0; // OK
    }

    // Obtain a reference to the default filter factory; create a filter object 
    CosNF::FilterFactory_ptr l_filter_factory;

    a_filter = CosNF::Filter::_nil();

    try {
        
        l_filter_factory = a_channel->default_filter_factory();  
        a_filter = l_filter_factory->create_filter("EXTENDED_TCL");
        
    } catch(CORBA::COMM_FAILURE& ex) {
        
        cerr << a_obj_name << ": Caught COMM_FAILURE obtaining filter object" << endl;
        return 1; // error
        
    } catch(...) {
        
        cerr << a_obj_name << ": Caught exception obtaining filter object" << endl;
        return 1; // error
    }

    
    // Construct a simple constraint expression; add it to fadmin
    CosNF::ConstraintExpSeq l_constraint_exp_seq;
    l_constraint_exp_seq.length(1);
    l_constraint_exp_seq[0].event_types = a_event_type_seq;
    l_constraint_exp_seq[0].constraint_expr = CORBA::string_dup(a_constraint_expr);
    
    CORBA::Boolean l_result = 0; // OK

    try {
        
        a_filter->add_constraints(l_constraint_exp_seq);
        a_filter_admin->add_filter(a_filter);
    
        if(a_event_type_seq.length()) {
            
            cout << a_obj_name << ": Added filter for types ";

            for(unsigned int j = 0; j < a_event_type_seq.length(); j++) { 
                cout << (const char*)a_event_type_seq[j].domain_name << "::" << (const char*)a_event_type_seq[j].type_name;
                if((j+1) < a_event_type_seq.length())
                    cout << ", ";
            }
            
        } else {

            cout << a_obj_name << ": Added filter for type *::* ";
        }

        cout << " and constraint expression \"" << a_constraint_expr << "\" " << endl;
    
    }
    catch(CosNF::InvalidConstraint& _exobj1) {
        
        cerr << a_obj_name << ": Exception thrown : Invalid constraint given "
             << (const char *)a_constraint_expr << endl;
        l_result = 1; // error
    }
    catch(...) {
        
        cerr << a_obj_name << ": Exception thrown while adding constraint " 
             << (const char *)a_constraint_expr << endl; 
        l_result = 1; // error
    }
    
    if(1 == l_result) { // error so destroy filter

        try {
            a_filter->destroy();

        } catch (...) {
            
            cerr << "Exception: a_filter->destroy()" << endl;
        }
        
        a_filter = CosNF::Filter::_nil();
        return 1; // error
    }
    
    return 0; // OK
}


// Method:
void
NotifyUtil::destroy_filters(FilterSeq& a_filter_seq)
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif
 
  for(unsigned int i = 0; i < a_filter_seq.length(); i++) {

      try {
          
      a_filter_seq[i]->destroy();
      a_filter_seq[i] = CosNF::Filter::_nil();
      
    } catch (...) {

        cerr << "Exception: a_filter_seq[i]->destroy()" << endl;
    }
  }
  
  a_filter_seq.length(0);
}


// Method:
CosNA::EventChannel_ptr
NotifyUtil::create_event_channel(const char *a_channel_id,
                                 const char *a_channel_kind,
                                 const char *a_fact_id,
                                 const char *a_fact_kind)
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    // Init local vars
    CosNA::EventChannelFactory_ptr l_factory = CosNA::EventChannelFactory::_nil();
    CosNA::EventChannel_ptr l_channel = CosNA::EventChannel::_nil();

    // Error checking for NULL or zero length args
    if(NULL == a_channel_id || NULL == a_channel_kind ||
       NULL == a_fact_id || NULL == a_fact_kind ||
       0 == strlen(a_channel_id) || 0 == strlen(a_channel_kind) ||
       0 == strlen(a_fact_id) || 0 == strlen(a_fact_kind)) {

        cerr << "ERROR: NotifyUtil in create_event_channel()" << endl;
        return l_channel; // failure
    }
    
    // Getting the Event Channel factory ref from the name server
    try {
        
        l_factory = pr_name_server_util->get_chan_fact_from_ns(a_fact_id, a_fact_kind);
        
    } catch (...) {
        
        cerr << "Exception: Was not able to get channel factory from name server!" << endl;
    }

    // Make sure that factory ref is not nil
    if(CORBA::is_nil(l_factory)) {
        
        cerr << "Failed to get factory ref from Name Server" << endl;
        return l_channel; // failure
    }


    // Creating properties needed to create a new channel
    CosN::QoSProperties l_qos_prop;
    CosN::AdminProperties l_admin_prop;
    CosNA::ChannelID l_channel_id;

    // Actaually creating the new channel
    l_channel = l_factory->create_channel(l_qos_prop, l_admin_prop, l_channel_id);

    // Make sure that the channel ref is not nil
    if(CORBA::is_nil(l_channel)) {
        
        cerr << "Failed to create a new channel" << endl;
        return l_channel; // failure
    }

    try {
    
        // Binding the new channel to a name
        pr_name_server_util->bind_object_to_name(l_channel, a_channel_id, a_channel_kind);
    }
    catch (...) {

        cerr << "Exception: Was not able to bind new channel to name!" << endl;
    }
    
    return l_channel; // success
}







