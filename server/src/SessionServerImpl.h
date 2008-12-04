/* $Id: SessionServerImpl.h,v 1.23 2003/05/16 19:18:19 thomas Exp $ */

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

#ifndef __SESSIONSERVERIMPL_H__
#define __SESSIONSERVERIMPL_H__


#include <CosNotifyShorthands.h>
#include <iostream>
#include <string>
#include "RctCorba.h"
#include "RctMutex.h"
#include "ServerImpl.h"
#include "SessionServer.hh"
#include "SessionServerConst.h"
#include "DatabaseServerImpl.h"
#include "NameServerUtil.h"
#include "ClassServerImpl.h"
#include "UserServerImpl.h"
#include "PushSupplierImpl.h"
#include "AuthenticationServerImpl.h"
#include "TeamServerImpl.h"
#include "GroupServerImpl.h"
#include "ChatServerImpl.h"
#include "FileServerImpl.h"
#include "PingServerImpl.h"
#include "CourseContentServerImpl.h"
#include "SoundServerImpl.h"
#include "ControlServerImpl.h"
#include "TextpadServerImpl.h"
#include "UrlServerImpl.h"
#include "FtpServerImpl.h"
#include "TDServerImpl.h"
#include "AdminServerImpl.h"
#include "Const.h"


using namespace RCT;


class AuthenticationServer_i;
class ChatServer_i;
class GroupServer_i;
class TeamServer_i;
class UserServer_i;
class ClassServer_i;


class SessionServer_i :
    public POA_RCT::SessionServer,
    public Server_i
{

private:

    // Ref to ORB
    CORBA::ORB_ptr pr_orb;

    // Ref to POA
    PortableServer::POA_ptr pr_poa;

    // Instantiate a NameServerUtil object
    NameServerUtil *pr_nsu;
    
    // Ref to BC PushSupplier
    PushSupplier_i *pr_bc_push_supplier;

    // Ref to per user PushSuppliers
    PushSupplier_i *pr_user_push_supplier[MAX_NUM_USERS];

    // Ref to NotifyUtil
    NotifyUtil *pr_notify_util;

    // Ref to per user Event Channel
    CosNA::EventChannel_var pr_user_event_channel[MAX_NUM_USERS];

    // Session Server obj
    static SessionServer_i *pr_session_server;

    // Constructor:
    inline SessionServer_i() { }

    // Constructor:
    SessionServer_i(CORBA::ORB_ptr a_orb =
                    CORBA::ORB::_nil(),
                    PortableServer::POA_ptr a_poa =
                    PortableServer::POA::_nil(),
                    CosNA::EventChannel_ptr a_channel =
                    CosNA::EventChannel::_nil(),
                    const char *a_status = SM1);

    // Destructor:
    inline virtual ~SessionServer_i() { }

public:

    // Local Method:
    // Return class instance
    static SessionServer_i* instance(CORBA::ORB_ptr a_orb =
                                     CORBA::ORB::_nil(),
                                     PortableServer::POA_ptr a_poa =
                                     PortableServer::POA::_nil(),
                                     CosNA::EventChannel_ptr a_channel =
                                     CosNA::EventChannel::_nil());

    // Method to set the server's status in DB
    void set_server_status(void);

    // Local Method:
    // Initialize Database Server
    void init_db_server();
    
    // Local Method:
    // Initialize Authentication Server
    void init_auth_server();
    
    // Local Method:
    // Initialize Session Server
    void init_session_server();
    
    // Local Method:
    // Initialize Class Server
    void init_class_server();

    // Local Method:
    // Initialize User Server
    void init_user_server();

    // Local Method:
    // Initialize Team Server
    void init_team_server();
    
    // Local Method:
    // Initialize Group Server
    void init_group_server();

    // Local Method:
    // Initialize Chat Server
    void init_chat_server();

    // Local Method:
    // Initialize File Server
    void init_file_server();

    // Local Method:
    // Initialize Ping Server
    void init_ping_server();

    // Local Method:
    // Initialize Course Content Server
    void init_cc_server();

    // Local Method:
    // Initialize Sound Server
    void init_sound_server();
    
    // Local Method:
    // Initialize Control Server
    void init_control_server();

    // Local Methdo:
    // Initialize Textpad Server
    void init_textpad_server();

    // Local Method:
    // Initialize Url Server
    void init_url_server();

    // Local Method:
    // Initialize Ftp Server
    void init_ftp_server();

    // Local Method:
    // Initialize Threaded Discussion Server
    void init_td_server();

    // Local Method:
    // Initialize Admin Server
    void init_admin_server();
    
    // Local Method:
    // Setting the server's status
    void set_server_status(Server_i *a_server,
                           const char *a_status_msg);
    
    // Local Method:
    // Getting all the server status
    void print_all_server_status(void);

    // Local Method:
    // Sending base message
    void push_msg(Message a_msg, CORBA::Long a_user_index);
    
    // Local Method:
    // Sending a BC message
    void push_bc_msg(BCMessage a_msg);
    
    // Local Method:
    // Sending a chat message
    void push_chat_msg(ChatMessage a_msg, CORBA::Long a_user_index);

    // Local Method:
    // Sending a control message
    void push_control_msg(ControlMessage a_msg, CORBA::Long a_user_index);

    // Local Method:
    // Sending a sound message
    void push_sound_msg(SoundMessage a_msg, CORBA::Long a_user_index);

    // Local Method:
    // Sending a textpad message
    void push_textpad_msg(TextpadMessage a_msg, CORBA::Long a_user_index);

    // Local Method:
    // Sending an url message
    void push_url_msg(UrlMessage a_msg, CORBA::Long a_user_index);

    // Local Method:
    // Sending a ftp message
    void push_ftp_msg(FtpMessage a_msg, CORBA::Long a_user_index);

    // Local Method:
    // Sending a file message
    void push_file_msg(FilePacketMessage a_msg, CORBA::Long a_user_index);

    // Local Method:
    // Sending a threaded discussion message
    void push_td_msg(TDMessage a_msg, CORBA::Long a_user_index);
    
    // Local Method:
    // Create a new Event Channel for user
    void create_event_channel(const char *a_user_id, CORBA::Long a_user_index);
};

#endif // __SESSIONSERVERIMPL_H__










