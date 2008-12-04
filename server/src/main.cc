/* $Id: main.cc,v 1.15 2003/05/08 20:34:43 thomas Exp $ */

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

#include "main.h"


int
main (int argc, char** argv)
{
    {
        // Make sure that omniNames and notifd had enough time to start
        sleep(1);

        // Local vars
        CORBA::ORB_var l_orb;
        CORBA::Object_var l_obj;
        PortableServer::POA_var l_poa;
        PortableServer::POAManager_var l_pman;
        SessionServer_i * pr_session_server;

        try {

            // Init POA
            l_orb = CORBA::ORB_init(argc, argv, "omniORB4");

            l_obj = l_orb->resolve_initial_references("RootPOA");
            l_poa = PortableServer::POA::_narrow(l_obj);
            l_pman = l_poa->the_POAManager();
            l_pman->activate();

            // Instantiate a NameServerUtil object
            NameServerUtil * l_nsu = new NameServerUtil(l_orb);

            // Instantiate omniNotify Channel
            CosNA::EventChannel_ptr l_bc_channel = l_nsu->get_chan_from_ns("EventChannel", "EventChannel");

            // See if we were able to get the channel
            if (CORBA::is_nil(l_bc_channel)) {

                cerr << "ERROR: Couldn't get broad cast channel object from name server" << endl;
                exit(1);
            }

            // Instantiate SessionServer object
            // NOTE: Make sure that this is the first module that
            // gets initialized.
            pr_session_server = SessionServer_i::instance(l_orb,
                                                          l_poa,
                                                          l_bc_channel);

            // Init all the modules
            pr_session_server->init_auth_server();
            pr_session_server->init_session_server();
            pr_session_server->init_class_server();
            pr_session_server->init_user_server();
            pr_session_server->init_db_server();
            pr_session_server->init_group_server();
            pr_session_server->init_team_server();
            pr_session_server->init_chat_server();
            pr_session_server->init_file_server();
            pr_session_server->init_ping_server();
            pr_session_server->init_cc_server();
            pr_session_server->init_sound_server();
            pr_session_server->init_control_server();
            pr_session_server->init_textpad_server();
            pr_session_server->init_url_server();
            pr_session_server->init_ftp_server();
            pr_session_server->init_td_server();
	    pr_session_server->init_admin_server();
            
            // Changing the servers' status
            pr_session_server->set_server_status(pr_session_server,
                                                 SSM1);
            pr_session_server->set_server_status(AuthenticationServerS::instance(),
                                                 ASM1);
            pr_session_server->set_server_status(ClassServerS::instance(),
                                                 CSM1);
            pr_session_server->set_server_status(UserServerS::instance(),
                                                 USM1);
            pr_session_server->set_server_status(DatabaseServerS::instance(),
                                                 DSM1);
            pr_session_server->set_server_status(GroupServerS::instance(),
                                                 GSM1);
            pr_session_server->set_server_status(TeamServerS::instance(),
                                                 TSM1);
            pr_session_server->set_server_status(ChatServerS::instance(),
                                                 CHSM1);
            pr_session_server->set_server_status(FileServerS::instance(),
                                                 FSM1);
            pr_session_server->set_server_status(PingServerS::instance(),
                                                 PSM1);
            pr_session_server->set_server_status(CourseContentServerS::instance(),
                                                 CCSM1);
            pr_session_server->set_server_status(SoundServerS::instance(),
                                                 SOSM1);
            pr_session_server->set_server_status(ControlServerS::instance(),
                                                 COSM1);
            pr_session_server->set_server_status(TextpadServerS::instance(),
                                                 TPSM1);
            pr_session_server->set_server_status(UrlServerS::instance(),
                                                 URLSM1);
            pr_session_server->set_server_status(FtpServerS::instance(),
                                                 FTPSM1);
	    pr_session_server->set_server_status(TDServerS::instance(),
						 TDSM1);
            pr_session_server->set_server_status(AdminServerS::instance(),
                                                 ADSM1);
            
                                                 

            // Getting the status from all the servers
            pr_session_server->print_all_server_status();

            // Creating thread to check whether user is still online or crashed
            PingServerS *l_ping_server = PingServerS::instance();
            l_ping_server->create_ping_thread();
            
            l_orb->run();
                
            l_orb->destroy();
            
        }
        catch(CORBA::SystemException&) {

            cerr << "Caught CORBA::SystemException." << endl;
        }
        catch(CORBA::Exception&) {

            cerr << "Caught CORBA::Exception." << endl;
        }
        catch(omniORB::fatalException& fe) {

            cerr << "Caught omniORB::fatalException:" << endl;
            cerr << "  file: " << fe.file() << endl;
            cerr << "  line: " << fe.line() << endl;
            cerr << "  mesg: " << fe.errmsg() << endl;
        }
        catch(...) {

            cerr << "Caught unknown exception." << endl;
        }

        return 0;
    }
}


