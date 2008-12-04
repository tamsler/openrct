/* $Id: UserServerImpl.h,v 1.15 2003/05/08 20:34:43 thomas Exp $ */

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

#ifndef __USERSERVERIMPL_H__
#define __USERSERVERIMPL_H__

#include <vector>
#include <string>
#include <iostream>
#include "UserServer.hh"
#include "ServerImpl.h"
#include "DatabaseServerImpl.h"
#include "TeamServerImpl.h"
#include "FileServerImpl.h"
#include "Const.h"
#include "MakeSingle.h"


using namespace RCT;


class UserServer_i : public POA_RCT::UserServer,
                     public Server_i
{

protected:

    // Constructor:
    inline UserServer_i() : Server_i(SM1) { }

    // Destructor:
    virtual inline ~UserServer_i() { }


public:

    // IDL Method:
    // Returning a user that matches a UserId
    virtual User* get_user_from_user_id(const char *a_user_id);

    // IDL Method:
    // Returning a user that matches a User alias
    virtual User* get_user_from_user_alias(const char *a_alias);

    // IDL Method:
    // Returning a list of users for a class
    virtual void get_users_from_class_name(const char *a_class_name,
                                           UserSeq_out a_user_seq);

    // IDL Method:
    // Returning a list of users for a team
    virtual void get_users_from_team_name(const char *a_class_name,
                                          const char *a_team_name,
                                          UserSeq_out a_user_seq);

    // IDL Method:
    // Returning a list of user ids from a team
    virtual void get_user_ids_from_team_name(const char *a_class_name,
                                             const char *a_team_name,
                                             UserIdSeq_out a_user_id_seq);

    // IDL Method:
    // Returning a list of users for a group
    virtual void get_users_from_group_name(const char *a_class_name,
                                           const char *a_group_name,
                                           UserSeq_out a_user_seq);

    // IDL Method:
    // Returning a list of user ids for a group
    virtual void get_user_ids_from_group_name(const char *a_class_name,
                                              const char *a_group_name,
                                              UserIdSeq_out a_user_id_seq);

    // IDL Method:
    // Returning a list of users related by a class
    virtual void get_users_related_by_classes(const char *a_user_id,
                                              UserSeq_out a_user_seq);
    
    // IDL Method:
    // Returning a list containing all users
    virtual void get_all_users(UserSeq_out a_user_seq);

    // IDL Method:
    // Returning a list of online users for a team
    // -- meaning users who are active in a team
    virtual void get_online_users_from_team_name(const char *a_class_name,
                                                 const char *a_team_name,
                                                 UserSeq_out a_user_seq);

    // IDL Method:
    // Returning a list of online user ids for a team
    // -- meaning users who are active in a team
    virtual void get_online_user_ids_from_team_name(const char *a_class_name,
                                                    const char *a_team_name,
                                                    UserIdSeq_out a_user_seq);

    // IDL Method:
    // Returning the file location and name of the user image
    virtual CORBA::Boolean get_user_image(const char *a_user_alias,
                                          CORBA::String_out a_user_id,
                                          FileInfo_out a_file_info,
                                          BinaryFile_out a_file_data);

    // IDL Method:
    virtual void get_module_control_queue_users(const char *a_class_name,
                                                const char *a_assembly_name,
                                                const char *a_module_name,
                                                UserAliasSeq_out a_user_alias_seq,
                                                CORBA::Long a_type);

    // IDL Method:
    virtual CORBA::Boolean is_user_manager_of_assembly(const char *a_class_name,
                                                       const char *a_assembly_name,
                                                       const char *a_user_id,
                                                       CORBA::Long a_type);
    
    // Local Method:
    // Set a user's online status to [ true | false ]
    void set_user_online_status(const char *a_user_alias,
                                bool a_status);
       
    // Local Method:
    // Returning a list of online users for a class
    void get_online_users_related_by_team(const char *a_class_name,
                                          const char *a_team_name,
                                          UserSeq_out a_user_seq);

    // Local Method:
    // Returning a list of online users for a class
    void get_online_users_from_class_name(const char *a_class_name,
                                          UserSeq_out a_user_seq);

    // Local Method:
    // Returning the user_id from user_alias
    char* get_id_from_alias(const char *a_user_alias);

    // Local Method:
    // Returning the user_alias from user_id
    char* get_alias_from_id(const char *a_user_id);

    // Local Method:
    // Returning the users password
    char* get_password_from_id(const char *a_user_id);

};

typedef MakeSingle<UserServer_i> UserServerS;

#endif //__USERSERVERIMPL_H__












