/* $Id: SessionServerImpl.cc,v 1.27 2003/05/16 19:18:19 thomas Exp $ */

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

#include "SessionServerImpl.h"


// Init object instance
SessionServer_i* SessionServer_i::pr_session_server = NULL;


// Local Method:
SessionServer_i*
SessionServer_i::instance(CORBA::ORB_ptr a_orb,
                          PortableServer::POA_ptr a_poa,
                          CosNA::EventChannel_ptr a_channel)
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    if(NULL == pr_session_server) {

        pr_session_server = new SessionServer_i(a_orb,
                                                a_poa,
                                                a_channel,
                                                SM1);
    }

    return pr_session_server;
}

// Constructor
SessionServer_i::SessionServer_i(CORBA::ORB_ptr a_orb,
                                 PortableServer::POA_ptr a_poa,
                                 CosNA::EventChannel_ptr a_channel,
                                 const char *a_status)
    : Server_i(a_status)
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    // Assign orb
    pr_orb = a_orb;

    // Assign poa
    pr_poa = a_poa;

    // Getting a nsu obj
    pr_nsu = new NameServerUtil(a_orb);
    
    // Creating Notify Util object
    pr_notify_util = new NotifyUtil(a_orb);
    
    // Creating the Server's bc push supplier
    pr_bc_push_supplier = new PushSupplier_i(a_orb, a_poa, a_channel);
    CosNotifyComm::PushSupplier_var l_bc_supplier_ref = pr_bc_push_supplier->_this();
    pr_bc_push_supplier->_remove_ref();

    // Trying to connect the bc push supplier
    if(!pr_bc_push_supplier->connect()) {
        
        cerr << "ERROR: BC PushSupplier did not connect" << endl;
    }

    // Setting the language locale
    if(NULL == setlocale(LC_CTYPE, "en_US.UTF-8")) {
        
        cerr << "ERROR: Could not set the UNICODE locale!" << endl;
    }
}


// Local Method
void
SessionServer_i::set_server_status(void)
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DatabaseServerS *l_db_server = DatabaseServerS::instance();

    // Execute the update transaction
    l_db_server->exec_db_update(UPDATE_SERVER_STATUS);
}  

// Local Method
void
SessionServer_i::init_db_server()
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    // Usef for servant ref
    CORBA::Object_var l_obj;

    // Instantiate DataBase Server object
    DatabaseServerS *l_db_server = DatabaseServerS::instance();

    // Getting the object indices
    l_db_server->get_obj_indices();
    
    // Activate DatabaseServer servant with POA
    PortableServer::ObjectId_var l_db_server_id;
    l_db_server_id = pr_poa->activate_object(l_db_server);

    // Getting ref to DatabaseServer servant
    l_obj = l_db_server->POA_RCT::DatabaseServer::_this();

    if(!pr_nsu->bind_object_to_name(l_obj, DBSERVER, SERVER)) {

        cerr << "ERROR: Couldn't bind object to name" << endl;
    }

    l_db_server->_remove_ref();
}


// Local Method
void
SessionServer_i::init_auth_server()
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    // Used for servant ref
    CORBA::Object_var l_obj;

    // Instantiate AuthServer object
    AuthenticationServerS *l_auth_server = AuthenticationServerS::instance();
    
    // Activate AuthServer servant with POA
    PortableServer::ObjectId_var l_auth_server_id;
    l_auth_server_id = pr_poa->activate_object(l_auth_server);

    // Getting ref to ClassServer servant
    l_obj = l_auth_server->POA_RCT::AuthenticationServer::_this();

    if(!pr_nsu->bind_object_to_name(l_obj, AUTHSERVER, SERVER)) {

        cerr << "ERROR: Couldn't bind object to name" << endl;
    }

    l_auth_server->_remove_ref();
}


// Local Method
void
SessionServer_i::init_session_server()
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    // Used for servant ref
    CORBA::Object_var l_obj;

    // Activate ClassServer servant with POA
    PortableServer::ObjectId_var l_session_server_id;
    l_session_server_id = pr_poa->activate_object(this);

    // Getting ref to ClassServer servant
    l_obj = this->POA_RCT::SessionServer::_this();

    if(!pr_nsu->bind_object_to_name(l_obj, SESSIONSERVER, SERVER) ) {
        
        cerr << "ERROR: Couldn't bind object to name" << endl;
    }
    
    this->_remove_ref(); 
}


// Local Method
void
SessionServer_i::init_class_server()
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif
   
    // Used for servant ref
    CORBA::Object_var l_obj;

    // Instantiate ClassServer object
    ClassServerS *l_class_server = ClassServerS::instance();

    // Activate ClassServer servant with POA
    PortableServer::ObjectId_var l_class_server_id;
    l_class_server_id = pr_poa->activate_object(l_class_server);

    // Getting ref to ClassServer servant
    l_obj = l_class_server->POA_RCT::ClassServer::_this();

    if(!pr_nsu->bind_object_to_name(l_obj, CLASSSERVER, SERVER)) {
        
        cerr << "ERROR: Couldn't bind object to name" << endl;
    }
    
    l_class_server->_remove_ref();
}


// Local Method
void
SessionServer_i::init_cc_server()
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif
   
    // Used for servant ref
    CORBA::Object_var l_obj;

    // Instantiate ClassServer object
    CourseContentServerS *l_cc_server = CourseContentServerS::instance();

    // Activate ClassServer servant with POA
    PortableServer::ObjectId_var l_cc_server_id;
    l_cc_server_id = pr_poa->activate_object(l_cc_server);

    // Getting ref to ClassServer servant
    l_obj = l_cc_server->POA_RCT::CourseContentServer::_this();

    if(!pr_nsu->bind_object_to_name(l_obj, COURSECONTENTSERVER, SERVER)) {
        
        cerr << "ERROR: Couldn't bind object to name" << endl;
        exit(1);
    }
    
    l_cc_server->_remove_ref();
}


// Local Method
void
SessionServer_i::init_user_server()
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif
    
    // Used for servant ref
    CORBA::Object_var l_obj;

    // Instantiate UserServer object
    UserServerS *l_user_server = UserServerS::instance();

    // Activate UserServer servant with POA
    PortableServer::ObjectId_var l_user_server_id;
    l_user_server_id = pr_poa->activate_object(l_user_server);

    // Getting ref to UserServer servant
    l_obj = l_user_server->POA_RCT::UserServer::_this();

    if(!pr_nsu->bind_object_to_name(l_obj, USERSERVER, SERVER) ) {
        
        cerr << "ERROR: Coundln't bind object to name" << endl;
    }
    
    l_user_server->_remove_ref();
}


// Local Method
void
SessionServer_i::init_group_server()
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif
    
    // Used for servant ref
    CORBA::Object_var l_obj;

    // Instantiate GroupServer object
    GroupServerS *l_group_server = GroupServerS::instance();

    // Activate GroupServer servant with POA
    PortableServer::ObjectId_var l_group_server_id;
    l_group_server_id = pr_poa->activate_object(l_group_server);

    // Getting ref to GroupServer servant
    l_obj = l_group_server->POA_RCT::GroupServer::_this();

    if(!pr_nsu->bind_object_to_name(l_obj, GROUPSERVER, SERVER) ) {
        
        cerr << "ERROR: Coundln't bind object to name" << endl;
    }
    
    l_group_server->_remove_ref();
}

// Local Method
void
SessionServer_i::init_team_server()
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif
    
    // Used for servant ref
    CORBA::Object_var l_obj;

    // Instantiate TeamServer object
    TeamServerS *l_team_server = TeamServerS::instance();

    // Activate TeamServer servant with POA
    PortableServer::ObjectId_var l_team_server_id;
    l_team_server_id = pr_poa->activate_object(l_team_server);

    // Getting ref to TeamServer servant
    l_obj = l_team_server->POA_RCT::TeamServer::_this();

    if(!pr_nsu->bind_object_to_name(l_obj, TEAMSERVER, SERVER) ) {
        
        cerr << "ERROR: Coundln't bind object to name" << endl;
    }
    
    l_team_server->_remove_ref();
}


// Local Method
void
SessionServer_i::init_chat_server()
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif
    
    // Used for servant ref
    CORBA::Object_var l_obj;

    // Instantiate ChatServer object
    ChatServerS *l_chat_server = ChatServerS::instance();

    // Activate ChatServer servant with POA
    PortableServer::ObjectId_var l_chat_server_id;
    l_chat_server_id = pr_poa->activate_object(l_chat_server);

    // Getting ref to ChatServer servant
    l_obj = l_chat_server->POA_RCT::ChatServer::_this();

    if(!pr_nsu->bind_object_to_name(l_obj, CHATSERVER, SERVER) ) {
        
        cerr << "ERROR: Coundln't bind object to name" << endl;
    }
    
    l_chat_server->_remove_ref();
}

// Local Method
void
SessionServer_i::init_file_server()
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif
    
    // Used for servant ref
    CORBA::Object_var l_obj;

    // Instantiate FileServer object
    FileServerS *l_file_server = FileServerS::instance();
    l_file_server->init();

    // Activate FileServer servant with POA
    PortableServer::ObjectId_var l_file_server_id;
    l_file_server_id = pr_poa->activate_object(l_file_server);

    // Getting ref to FileServer servant
    l_obj = l_file_server->POA_RCT::FileServer::_this();

    if(!pr_nsu->bind_object_to_name(l_obj, FILESERVER, SERVER) ) {
        
        cerr << "ERROR: Coundln't bind object to name" << endl;
    }
    
    l_file_server->_remove_ref();
}

// Local Method
void
SessionServer_i::init_ping_server()
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif
    
    // Used for servant ref
    CORBA::Object_var l_obj;

    // Instantiate PingServer object
    PingServerS *l_ping_server = PingServerS::instance();

    // Activate PingServer servant with POA
    PortableServer::ObjectId_var l_ping_server_id;
    l_ping_server_id = pr_poa->activate_object(l_ping_server);

    // Getting ref to PingServer servant
    l_obj = l_ping_server->POA_RCT::PingServer::_this();

    if(!pr_nsu->bind_object_to_name(l_obj, PINGSERVER, SERVER) ) {
        
        cerr << "ERROR: Coundln't bind object to name" << endl;
    }
    
    l_ping_server->_remove_ref();
}

// Local Method
void
SessionServer_i::init_sound_server()
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif
    
    // Used for servant ref
    CORBA::Object_var l_obj;

    // Instantiate SoundServer object
    SoundServerS *l_sound_server = SoundServerS::instance();

    // Activate SoundServer servant with POA
    PortableServer::ObjectId_var l_sound_server_id;
    l_sound_server_id = pr_poa->activate_object(l_sound_server);

    // Getting ref to SoundServer servant
    l_obj = l_sound_server->POA_RCT::SoundServer::_this();

    if(!pr_nsu->bind_object_to_name(l_obj, SOUNDSERVER, SERVER) ) {
        
        cerr << "ERROR: Coundln't bind object to name" << endl;
    }
    
    l_sound_server->_remove_ref();
}

// Local Method
void
SessionServer_i::init_control_server()
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif
    
    // Used for servant ref
    CORBA::Object_var l_obj;

    // Instantiate ControlServer object
    ControlServerS *l_control_server = ControlServerS::instance();

    // Activate ControlServer servant with POA
    PortableServer::ObjectId_var l_control_server_id;
    l_control_server_id = pr_poa->activate_object(l_control_server);

    // Getting ref to ControlServer servant
    l_obj = l_control_server->POA_RCT::ControlServer::_this();

    if(!pr_nsu->bind_object_to_name(l_obj, CONTROLSERVER, SERVER) ) {
        
        cerr << "ERROR: Coundln't bind object to name" << endl;
    }
    
    l_control_server->_remove_ref();
}

// Local Method
void
SessionServer_i::init_textpad_server()
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif
    
    // Used for servant ref
    CORBA::Object_var l_obj;

    // Instantiate TextpadServer object
    TextpadServerS *l_textpad_server = TextpadServerS::instance();
    l_textpad_server->init();

    // Activate TextpadServer servant with POA
    PortableServer::ObjectId_var l_textpad_server_id;
    l_textpad_server_id = pr_poa->activate_object(l_textpad_server);

    // Getting ref to TextpadServer servant
    l_obj = l_textpad_server->POA_RCT::TextpadServer::_this();

    if(!pr_nsu->bind_object_to_name(l_obj, TEXTPADSERVER, SERVER) ) {
        
        cerr << "ERROR: Coundln't bind object to name" << endl;
    }
    
    l_textpad_server->_remove_ref();
}

// Local Method
void
SessionServer_i::init_url_server()
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif
    
    // Used for servant ref
    CORBA::Object_var l_obj;

    // Instantiate UrlServer object
    UrlServerS *l_url_server = UrlServerS::instance();

    // Activate UrlServer servant with POA
    PortableServer::ObjectId_var l_url_server_id;
    l_url_server_id = pr_poa->activate_object(l_url_server);

    // Getting ref to UrlServer servant
    l_obj = l_url_server->POA_RCT::UrlServer::_this();

    if(!pr_nsu->bind_object_to_name(l_obj, URLSERVER, SERVER) ) {
        
        cerr << "ERROR: Coundln't bind object to name" << endl;
    }
    
    l_url_server->_remove_ref();
}

// Local Method
void
SessionServer_i::init_ftp_server()
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif
    
    // Used for servant ref
    CORBA::Object_var l_obj;

    // Instantiate FtpServer object
    FtpServerS *l_ftp_server = FtpServerS::instance();

    // Activate FtpServer servant with POA
    PortableServer::ObjectId_var l_ftp_server_id;
    l_ftp_server_id = pr_poa->activate_object(l_ftp_server);

    // Getting ref to FtpServer servant
    l_obj = l_ftp_server->POA_RCT::FtpServer::_this();

    if(!pr_nsu->bind_object_to_name(l_obj, FTPSERVER, SERVER) ) {
        
        cerr << "ERROR: Coundln't bind object to name" << endl;
    }
    
    l_ftp_server->_remove_ref();
}

// Local Method
void
SessionServer_i::init_td_server()
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif
    
    // Used for servant ref
    CORBA::Object_var l_obj;

    // Instantiate TDServer object
    TDServerS *l_td_server = TDServerS::instance();

    // Activate TDServer servant with POA
    PortableServer::ObjectId_var l_td_server_id;
    l_td_server_id = pr_poa->activate_object(l_td_server);

    // Getting ref to TDServer servant
    l_obj = l_td_server->POA_RCT::TDServer::_this();

    if(!pr_nsu->bind_object_to_name(l_obj, TDSERVER, SERVER) ) {
        
        cerr << "ERROR: Coundln't bind object to name" << endl;
    }
    
    l_td_server->_remove_ref();
}

// Local Method
void
SessionServer_i::init_admin_server()
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif
    
    // Used for servant ref
    CORBA::Object_var l_obj;

    // Instantiate AdminServer object
    AdminServerS *l_admin_server = AdminServerS::instance();

    // Activate AdminServer servant with POA
    PortableServer::ObjectId_var l_admin_server_id;
    l_admin_server_id = pr_poa->activate_object(l_admin_server);

    // Getting ref to AdminServer servant
    l_obj = l_admin_server->POA_RCT::AdminServer::_this();

    if(!pr_nsu->bind_object_to_name(l_obj, ADMINSERVER, SERVER) ) {
        
        cerr << "ERROR: Coundln't bind object to name" << endl;
    }
    
    l_admin_server->_remove_ref();
}

// Local Method
void
SessionServer_i::set_server_status(Server_i *a_server,
                                   const char *a_status_msg)
{
    CORBA::String_var l_status = CORBA::string_dup(a_status_msg);
    
    a_server->set_status(l_status);
}


// Local Method
void
SessionServer_i::print_all_server_status(void)
{
     
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    DatabaseServerS *l_db_server = DatabaseServerS::instance();
    UserServerS *l_user_server = UserServerS::instance();
    AuthenticationServerS *l_auth_server = AuthenticationServerS::instance();
    ClassServerS *l_class_server = ClassServerS::instance();
    CourseContentServerS *l_cc_server = CourseContentServerS::instance();
    GroupServerS *l_group_server = GroupServerS::instance();
    TeamServerS *l_team_server = TeamServerS::instance();
    ChatServerS *l_chat_server = ChatServerS::instance();
    PingServerS *l_ping_server = PingServerS::instance();
    FileServerS *l_file_server = FileServerS::instance();
    SoundServerS *l_sound_server = SoundServerS::instance();
    ControlServerS *l_control_server = ControlServerS::instance();
    TextpadServerS *l_textpad_server = TextpadServerS::instance();
    UrlServerS *l_url_server = UrlServerS::instance();
    FtpServerS *l_ftp_server = FtpServerS::instance();
    TDServerS *l_td_server = TDServerS::instance();
    AdminServerS *l_admin_server = AdminServerS::instance();
    
    cout << endl;
    cout << "**************************" << endl;
    cout << " OpenRCT Server v" << RCT_VERSION << endl;
    cout << "**************************" << endl;
    cout << get_status() << endl;
    cout << l_user_server->get_status() << endl;
    cout << l_class_server->get_status() << endl;
    cout << l_group_server->get_status() << endl;
    cout << l_team_server->get_status() << endl;
    cout << l_db_server->get_status() << endl;
    cout << l_auth_server->get_status() << endl;
    cout << l_chat_server->get_status() << endl;
    cout << l_file_server->get_status() << endl;
    cout << l_ping_server->get_status() << endl;
    cout << l_cc_server->get_status() << endl;
    cout << l_sound_server->get_status() << endl;
    cout << l_control_server->get_status() << endl;
    cout << l_textpad_server->get_status() << endl;
    cout << l_url_server->get_status() << endl;
    cout << l_ftp_server->get_status() << endl;
    cout << l_td_server->get_status() << endl;
    cout << l_admin_server->get_status() << endl;
    cout << "**************************" << endl;
}


// Local Method:
void
SessionServer_i::push_msg(Message a_msg, CORBA::Long a_user_index)
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    if(NULL != pr_user_push_supplier[a_user_index]) {

        pr_user_push_supplier[a_user_index]->push_msg(a_msg);
    }
}


// Local Method:
void
SessionServer_i::push_bc_msg(BCMessage a_msg)
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    pr_bc_push_supplier->push_bc_msg(a_msg);

}


// Local Method:
void
SessionServer_i::push_chat_msg(ChatMessage a_msg, CORBA::Long a_user_index)
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    if(NULL != pr_user_push_supplier[a_user_index]) {

        pr_user_push_supplier[a_user_index]->push_chat_msg(a_msg);
    }
}


// Local Method:
void
SessionServer_i::push_control_msg(ControlMessage a_msg, CORBA::Long a_user_index)
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    if(NULL != pr_user_push_supplier[a_user_index]) {

        pr_user_push_supplier[a_user_index]->push_control_msg(a_msg);
    }
}


// Local Method:
void
SessionServer_i::push_sound_msg(SoundMessage a_msg, CORBA::Long a_user_index)
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    if(NULL != pr_user_push_supplier[a_user_index]) {

        pr_user_push_supplier[a_user_index]->push_sound_msg(a_msg);
    }
}


// Local Method:
void
SessionServer_i::push_textpad_msg(TextpadMessage a_msg, CORBA::Long a_user_index)
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    if(NULL != pr_user_push_supplier[a_user_index]) {

        pr_user_push_supplier[a_user_index]->push_textpad_msg(a_msg);
    }
}

// Local Method:
void
SessionServer_i::push_url_msg(UrlMessage a_msg, CORBA::Long a_user_index)
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    if(NULL != pr_user_push_supplier[a_user_index]) {

        pr_user_push_supplier[a_user_index]->push_url_msg(a_msg);
    }
}

// Local Method:
void
SessionServer_i::push_ftp_msg(FtpMessage a_msg, CORBA::Long a_user_index)
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    if(NULL != pr_user_push_supplier[a_user_index]) {

        pr_user_push_supplier[a_user_index]->push_ftp_msg(a_msg);
    }
}

// Local Method:
void
SessionServer_i::push_file_msg(FilePacketMessage a_msg, CORBA::Long a_user_index)
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    if(NULL != pr_user_push_supplier[a_user_index]) {

        pr_user_push_supplier[a_user_index]->push_file_msg(a_msg);
    }
}

// Local Method:
void
SessionServer_i::push_td_msg(TDMessage a_msg, CORBA::Long a_user_index)
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    if(NULL != pr_user_push_supplier[a_user_index]) {

        pr_user_push_supplier[a_user_index]->push_td_msg(a_msg);
    }
}

// Local Method:
void
SessionServer_i::create_event_channel(const char *a_user_id, CORBA::Long a_user_index)
{
    
#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif

    // Enter critical secion
    RctMutex::session.lock();

    // Creating the Event Channel
    pr_user_event_channel[a_user_index] =
        pr_notify_util->create_event_channel(a_user_id,
                                             a_user_id,
                                             "ChannelFactory",
                                             "ChannelFactory");
    
    if(CORBA::is_nil(pr_user_event_channel[a_user_index])) {

        cerr << "ERROR: Was not able to create Event Channel for [ "
             << a_user_id
             << " ] !"
             << endl;
    }

    // Creating the Server's push supplier
    pr_user_push_supplier[a_user_index] =
        new PushSupplier_i(pr_orb,
                           pr_poa,
                           pr_user_event_channel[a_user_index]);
    
    CosNotifyComm::PushSupplier_var l_user_supplier_ref =
        pr_user_push_supplier[a_user_index]->_this();
    
    pr_user_push_supplier[a_user_index]->_remove_ref();

    // Trying to connect the user push supplier
    if(!pr_user_push_supplier[a_user_index]->connect()) {
        
        cerr << "ERROR: User PushSupplier did not connect" << endl;
    }

    // End critical secion
    RctMutex::session.unlock();
}















