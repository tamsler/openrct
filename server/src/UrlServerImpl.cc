/* $Id: UrlServerImpl.cc,v 1.3 2003/05/08 20:34:43 thomas Exp $ */

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

#include "UrlServerImpl.h"

void
UrlServer_i::send(const CORBA::WChar *a_url,
                   const char *a_version,
                   const char *a_user_id,
                   const char *a_user_alias,
                   ObjPermission a_permission,
                   const char *a_class_name,
                   const char *a_assembly_name,
                   CORBA::Long a_type)
{

#ifdef DEBUG
    cout << "DEBUG: This is line " <<  __LINE__ << " of file " << __FILE__ << endl;
#endif
    
    UserServerS *l_user_server = UserServerS::instance();
    SessionServer_i *l_session_server = SessionServer_i::instance();
    
    // Creating the chat message from client provided values
    UrlMessage l_url_msg;
    l_url_msg.base_msg.version = a_version;
    l_url_msg.base_msg.user_alias = a_user_alias;
    l_url_msg.base_msg.class_name = a_class_name;
    l_url_msg.base_msg.permission = a_permission;
    l_url_msg.base_msg.type = a_type;
    l_url_msg.url = a_url;

    
    // Get all the relevant users for team or group 
    UserIdSeq_var l_user_id_seq;

    // Check if we deal with a team message
    if(URL_TEAM_SEND == a_type) {

        l_url_msg.base_msg.team_name = a_assembly_name;
        l_user_server->get_online_user_ids_from_team_name(a_class_name, a_assembly_name, l_user_id_seq);
    }
    // Check if we deal with a group message
    else if(URL_GROUP_SEND == a_type) {

        l_url_msg.base_msg.group_name = a_assembly_name;
        l_user_server->get_user_ids_from_group_name(a_class_name, a_assembly_name, l_user_id_seq);
    }
    else {

        cerr << "ERROR: Wrong message type!" << endl;
    }

    // Now we send the message to each user event channel
    for (int i = 0; i < l_user_id_seq->length(); i++) {

        // Note: the [USER_ID_OFFSET] accesses the user_id string's numeric part bypassing the "U" of
        // the user_id, thus if we have U123, we just convert 123 into an integer
        l_session_server->push_url_msg(l_url_msg, atoi(&l_user_id_seq[i][USER_ID_OFFSET]));
    }
}
