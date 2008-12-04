/* $Id: PushSupplierImpl.cc,v 1.15 2004/10/23 06:06:46 thomas Exp $ */

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

#include "PushSupplierImpl.h"


// Constructor
PushSupplier_i::PushSupplier_i(CORBA::ORB_ptr a_orb,
                               PortableServer::POA_ptr a_poa,
                               CosNA::EventChannel_ptr a_channel,
                               const char* a_proxy_ior_file,
                               const char* a_admin_ior_file,
                               const char* a_constraint_expr) :
    pr_my_filters(0),
    pr_num_changes(0)
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    // Assign poa
    pr_poa = a_poa;
    
    pr_obj_name = CORBA::string_dup("any_push_supplier");
    
    // Allocating memory for the NotifyUtil object
    pr_notify_util = new NotifyUtil(a_orb);
    
    CosN::EventTypeSeq l_event_type_seq;
    l_event_type_seq.length(0);

    // Obtain appropriate proxy object
    CosNA::SupplierAdmin_ptr l_admin = CosNA::SupplierAdmin::_nil();
    pr_my_admin = l_admin;


    CosNA::ProxyConsumer_var l_generic_proxy =
        pr_notify_util->get_proxy_push_consumer(a_orb,
                                                a_channel,
                                                CosNA::ANY_EVENT,
                                                a_admin_ior_file,
                                                l_admin);
    
    pr_my_proxy = CosNA::ProxyPushConsumer::_narrow(l_generic_proxy);

    // get_proxy_consumer failed
    if(CORBA::is_nil(pr_my_proxy)) {

        cerr << "ERROR: Failed to narrow proxy push consumer" << endl;
    }

    // If l_event_type_seq or a_constraint_expr are non-empty, add a filter to proxy
    CosNF::Filter_ptr l_filter = CosNF::Filter::_nil();

    if (&l_event_type_seq) {

        // Creating filter test boolean
        CORBA::Boolean l_filter_error;
        l_filter_error= pr_notify_util->sample_add_filter(a_channel,
                                                          pr_my_proxy,
                                                          l_event_type_seq,
                                                          a_constraint_expr,
                                                          pr_obj_name,
                                                          l_filter);
                                                   
        if (l_filter_error) {
            
            try {
                
                l_admin->destroy();
            }
            catch(...) {
                
                cerr << "Exception: l_admin->destroy()" << endl;
            }
        }
    }

    if(!CORBA::is_nil(l_filter)) {
        
        pr_my_filters.length(1);
        pr_my_filters[0] = l_filter;
    }

    // Write proxy IOR to file
    pr_notify_util->write_ior_to_file(a_orb, pr_my_proxy, a_proxy_ior_file);
}


PushSupplier_i::~PushSupplier_i()
{
    // Freeing memory
    delete pr_notify_util;
}

void
PushSupplier_i::disconnect_push_supplier()
{
        
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

}


void
PushSupplier_i::subscription_change(const CosN::EventTypeSeq& a_added,
                                    const CosN::EventTypeSeq& a_deled)
{
        
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    pr_num_changes++;
}



CORBA::Boolean
PushSupplier_i::connect()
{
        
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    try {
        
        pr_my_proxy->connect_any_push_supplier(_this());
        pr_my_proxy->obtain_subscription_types(CosNA::NONE_NOW_UPDATES_ON);
    }
    catch(CORBA::BAD_PARAM& ex) {
        
        cerr << pr_obj_name << ": BAD_PARAM Exception while connecting" << endl;
        return 0; // error
    }
    catch(CosEventChannelAdmin::AlreadyConnected& ex) {
        
        cerr << pr_obj_name << ": Already connected" << endl;
        return 0; // error
    }
    catch(...) {
        
        cerr << pr_obj_name << ": Failed to connect" << endl;
        return 0; // error
    }
    
    // register the types to be supplied
    offer_any(pr_my_proxy, pr_obj_name);

    // spawn a thread to do pushing

    return 1; // OK
}





void
PushSupplier_i::cleanup()
{
        
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    if(CORBA::is_nil(pr_my_proxy)) {
        
        cerr << "Coding error: cleanup already called?" << endl;
        return;
    }
    
    // this method takes sole ownership of pr_my_proxy ref
    CosNA::ProxyPushConsumer_var l_proxy = pr_my_proxy;
    pr_my_proxy = CosNA::ProxyPushConsumer::_nil();
    
    try {
        
        if(!CORBA::is_nil(l_proxy)) {
            
            l_proxy->disconnect_push_consumer();
        }
    }
    catch(...) {

        cerr << "Exception: l_proxy->disconnect_push_consumer()" << endl;
    }

    try {
        
        pr_my_admin = CosNA::SupplierAdmin::_nil();
        pr_notify_util->destroy_filters(pr_my_filters);
    }
    catch(...) {

        cerr << "Exception: pr_notify_util->destroy_filters(pr_my_filters);" << endl;
    }

    PortableServer::ObjectId_var oid = pr_poa->servant_to_id(this);
    pr_poa->deactivate_object(oid.in());
}


// This helper routine informs channel that type ANY will be supplied
void
PushSupplier_i::offer_any(CosNC::NotifyPublish_ptr a_proxy,
                          const char *a_objnm)
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    CosN::EventTypeSeq l_added;
    CosN::EventTypeSeq l_deled;
    
    l_added.length(1);
    l_added[0].domain_name = CORBA::string_dup("");
    l_added[0].type_name = CORBA::string_dup("%ANY");
    l_deled.length(0);

    try {
        
        a_proxy->offer_change(l_added, l_deled);
    }
    catch (...) {
        
        cerr << "** registration failed **" << endl;
    }
}


// Local Method:
void
PushSupplier_i::push_msg(Message a_msg)
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    // Creating any type message
    CORBA::Any* l_data = new CORBA::Any;
    
    // Assigning the message to the any type
    *l_data <<= a_msg;

    // Now we can push the message
    try {
        pr_my_proxy->push(*l_data);
    }
    catch(...) {
        
        cerr << "ERROR: Communication error while calling push()" << endl;
    }

    // Cleaning up memory
    delete l_data;
}


// Local Method:
void
PushSupplier_i::push_bc_msg(BCMessage a_msg)
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    // Creating any type for bc message
    CORBA::Any* l_data = new CORBA::Any;
    
    // Assigning the message to the any type
    *l_data <<= a_msg;

    // Now we can push the message
    try {
        pr_my_proxy->push(*l_data);
    }
    catch(...) {
        
        cerr << "ERROR: Communication error while calling push()" << endl;
    }

    // Cleaning up memory
    delete l_data;
}


// Local Method:
void
PushSupplier_i::push_chat_msg(ChatMessage a_msg)
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    // Creating any type for chat message
    CORBA::Any* l_data = new CORBA::Any;
    
    // Assigning the message to the any type
    *l_data <<= a_msg;

    // Now we can push the message
    try {
        pr_my_proxy->push(*l_data);
    }
    catch(...) {
        
        cerr << "ERROR: Communication error while calling push()" << endl;
    }

    // Cleaning up memory
    delete l_data;
}


// Local Method:
void
PushSupplier_i::push_control_msg(ControlMessage a_msg)
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    // Creating any type for control message
    CORBA::Any* l_data = new CORBA::Any;
    
    // Assigning the message to the any type
    *l_data <<= a_msg;

    // Now we can push the message
    try {
        pr_my_proxy->push(*l_data);
    }
    catch(...) {
        
        cerr << "ERROR: Communication error while calling push()" << endl;
    }

    // Cleaning up memory
    delete l_data;
}


// Local Method:
void
PushSupplier_i::push_sound_msg(SoundMessage a_msg)
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    // Creating any type for sound message
    CORBA::Any* l_data = new CORBA::Any;
    
    // Assigning the message to the any type
    *l_data <<= a_msg;

    // Now we can push the message
    try {
        pr_my_proxy->push(*l_data);
    }
    catch(...) {
        
        cerr << "ERROR: Communication error while calling push()" << endl;
    }

    // Cleaning up memory
    delete l_data;
}


// Local Method:
void
PushSupplier_i::push_textpad_msg(TextpadMessage a_msg)
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    // Creating any type for textpad message
    CORBA::Any* l_data = new CORBA::Any;
    
    // Assigning the message to the any type
    *l_data <<= a_msg;

    // Now we can push the message
    try {
        pr_my_proxy->push(*l_data);
    }
    catch(...) {
        
        cerr << "ERROR: Communication error while calling push()" << endl;
    }

    // Cleaning up memory
    delete l_data;
}


// Local Method:
void
PushSupplier_i::push_url_msg(UrlMessage a_msg)
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    // Creating any type for url message
    CORBA::Any* l_data = new CORBA::Any;
    
    // Assigning the message to the any type
    *l_data <<= a_msg;

    // Now we can push the message
    try {
        pr_my_proxy->push(*l_data);
    }
    catch(...) {
        
        cerr << "ERROR: Communication error while calling push()" << endl;
    }

    // Cleaning up memory
    delete l_data;
}

// Local Method:
void
PushSupplier_i::push_ftp_msg(FtpMessage a_msg)
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    // Creating any type for ftp message
    CORBA::Any* l_data = new CORBA::Any;
    
    // Assigning the message to the any type
    *l_data <<= a_msg;

    // Now we can push the message
    try {
        pr_my_proxy->push(*l_data);
    }
    catch(...) {
        
        cerr << "ERROR: Communication error while calling push()" << endl;
    }

    // Cleaning up memory
    delete l_data;
}

// Local Method:
void
PushSupplier_i::push_file_msg(FilePacketMessage a_msg)
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    // Creating any type for file packet message
    CORBA::Any* l_data = new CORBA::Any;
    
    // Assigning the message to the any type
    *l_data <<= a_msg;

    // Now we can push the message
    try {
        pr_my_proxy->push(*l_data);
    }
    catch(...) {
        
        cerr << "ERROR: Communication error while calling push()" << endl;
    }

    // Cleaning up memory
    delete l_data;
}


// Local Method:
void
PushSupplier_i::push_td_msg(TDMessage a_msg)
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    // Creating any type for tdmessage
    CORBA::Any* l_data = new CORBA::Any;
    
    // Assigning the message to the any type
    *l_data <<= a_msg;

    // Now we can push the message
    try {
        pr_my_proxy->push(*l_data);
    }
    catch(...) {
        
        cerr << "ERROR: Communication error while calling push()" << endl;
    }

    // Cleaning up memory
    delete l_data;
}
