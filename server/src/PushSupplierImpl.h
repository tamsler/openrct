/* $Id: PushSupplierImpl.h,v 1.15 2003/05/16 19:18:19 thomas Exp $ */

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

#ifndef __PUSHSUPPLIERIMPL_H__
#define __PUSHSUPPLIERIMPL_H__

#include "RctCorba.h"
#include <iostream>
#include <iomanip>
#include "CosNotifyShorthands.h"
#include "NotifyUtil.h"
#include "BCMessageImpl.h"
#include "ChatMessageImpl.h"
#include "ControlMessageImpl.h"
#include "SoundMessageImpl.h"
#include "TextpadMessageImpl.h"
#include "UrlMessageImpl.h"
#include "FtpMessageImpl.h"
#include "TDMessageImpl.h"
#include "Const.h"


using namespace RCT;
using namespace std;

class PushSupplier_i :
    public POA_CosNotifyComm::PushSupplier,
    public PortableServer::RefCountServantBase
{

private:
    CosNA::ProxyPushConsumer_var pr_my_proxy;
    CosNA::SupplierAdmin_var     pr_my_admin;
    FilterSeq                    pr_my_filters;
    CORBA::String_var            pr_obj_name;
    CORBA::ULong                 pr_num_changes;
    NotifyUtil                   *pr_notify_util;

    PortableServer::POA_ptr      pr_poa;
    
    
    // Local Method:
    void offer_any(CosNC::NotifyPublish_ptr a_proxy,
                   const char *a_objnm);


public:

    // Constructor
    PushSupplier_i(CORBA::ORB_ptr a_orb,
                   PortableServer::POA_ptr a_poa,
                   CosNA::EventChannel_ptr a_channel,
                   const char* a_proxy_ior_file = "",
                   const char* a_admin_ior_file = "",
                   const char* a_constraint_expr = "");

    // Destructor:
    virtual ~PushSupplier_i();
    
    // IDL Method:
    virtual void disconnect_push_supplier();

    // IDL Method:
    virtual void subscription_change(const CosN::EventTypeSeq& a_added,
                                     const CosN::EventTypeSeq& a_deled);

    // Local Method:
    CORBA::Boolean connect();

    // Local Method:
    void cleanup();

    // Local Method:
    void push_msg(Message a_msg);
    
    // Local Method:
    void push_bc_msg(BCMessage a_msg);
    
    // Local Method:
    void push_chat_msg(ChatMessage a_msg);

    // Local Method:
    void push_control_msg(ControlMessage a_msg);

    // Local Methdo:
    void push_sound_msg(SoundMessage a_msg);

    // Local Method:
    void push_textpad_msg(TextpadMessage a_msg);

    // Local Method:
    void push_url_msg(UrlMessage a_msg);

    // Local Method:
    void push_ftp_msg(FtpMessage a_msg);

    // Local Method:
    void push_file_msg(FilePacketMessage a_msg);

    // Local Method:
    void push_td_msg(TDMessage a_msg);
};

#endif // __PUSHSUPPLIERIMPL_H__









