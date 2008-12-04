/* $Id: GroupServerImpl.h,v 1.11 2003/05/08 20:34:43 thomas Exp $ */

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

#ifndef __GROUPSERVERIMPL_H__
#define __GROUPSERVERIMPL_H__

#include <string>
#include <iostream>
#include "RctMutex.h"
#include "PermissionObj.hh"
#include "GroupServer.hh"
#include "ServerImpl.h"
#include "GroupServerConst.h"
#include "DatabaseServerImpl.h"
#include "SessionServerImpl.h"
#include "ClassServerImpl.h"
#include "UserServerImpl.h"
#include "ChatServerImpl.h"
#include "SoundServerImpl.h"
#include "UrlServerImpl.h"
#include "FtpServerImpl.h"
#include "MessageImpl.h"
#include "Const.h"
#include "MakeSingle.h"


using namespace RCT;


class GroupServer_i : public POA_RCT::GroupServer,
                      public Server_i
{

protected:
        
    // Constructor:
    inline GroupServer_i() : Server_i(SM1) { }

    // Destructor:
    virtual inline ~GroupServer_i() { }


public:

    // IDL Method:
    // Returning a sequence of all groups
    virtual void get_all_groups(GroupSeq_out a_group_seq);

    // IDL Method:
    // Returning a group sequence for a certain user
    virtual void get_groups_from_user_and_class(const char *a_user_id,
                                                const char *a_class_id,
                                                GroupSeq_out a_group_seq);

    // IDL Method:
    // Generates a group sequence for a class
    virtual void get_groups_from_class(const char *a_class_id,
                                       GroupSeq_out a_group_seq);
    
    // IDL Method:
    // Returning a group sequence for a certain class name
    virtual void get_groups_from_class_name(const char *a_class_name,
                                            GroupSeq_out a_group_seq);
    
    // IDL Method:
    // Create a new group
    virtual CORBA::Boolean create_group(const char *a_class_name,
                                        const char *a_group_name,
                                        const char *a_user_id,
                                        const char *a_alias);

    // IDL Method:
    // Request to join a group
    virtual void join_group_request(const char *a_class_name,
                                    const char *a_group_name,
                                    const char *a_user_id,
                                    const char *a_alias);

    // IDL Method:
    // Reply to join group request message
    virtual void join_group_request_reply(const char *a_class_name,
                                          const char *a_group_name,
                                          const char *a_user_id,
                                          const char *a_alias,
                                          const char *a_requester,
                                          CORBA::Boolean a_granted);

    // IDL Method:
    // Exit a group
    // The a_status flag is used to determined if
    // the exit_group was called normal or abnormal.
    // Normal, client exits
    // Abnormal, client has crashed
    virtual void exit_group(const char *a_class_name,
                            const char *a_group_name,
                            const char *a_user_id,
                            const char *a_user_alias,
                            CORBA::Boolean a_status);
    
    // IDL Method:
    // Set the group's new manager
    virtual void set_new_manager(const char *a_class_name,
                                 const char *a_group_name,
                                 const char *a_user_id,
                                 const char *a_user_alias,
                                 const char *a_new_mgr_alias);
    

    // IDL Method:
    // Method to test if a certain user is in a group
    virtual CORBA::Boolean is_user_in_group(const char *a_class_name,
                                            const char *a_group_name,
                                            const char *a_user_alias);

    // IDL Method:
    // Method that returns the user id of a group mgr
    virtual char* get_manager_id(const char *a_class_name,
                                 const char *a_group_name);


    // IDL Method:
    // Returns the group chat archive for requested (last n messages)
    // This returns messages via a sequence
    virtual void get_chat_archive_last_n(const char *a_class_name,
                                         const char *a_group_name,
                                         CORBA::Long a_num_msg,
                                         ChatMsgHistSeq_out a_chat_msg_seq)
        throw(CORBA::SystemException, RCT::GroupServer::DataSelectionExceedsLimit);

    // IDL Method:
    // Returns the group sound archive for requested (last n messages)
    // This returns messages via a sequence
    virtual void get_sound_archive_last_n(const char *a_class_name,
                                          const char *a_group_name,
                                          CORBA::Long a_num_msg,
                                          SoundMsgHistSeq_out a_sound_msg_seq)
        throw(CORBA::SystemException, RCT::GroupServer::DataSelectionExceedsLimit);


    // IDL Method:
    // Returns the group ftp archive for requested (last n messages)
    // This returns messages via a sequence
    virtual void get_ftp_archive_last_n(const char *a_class_name,
                                        const char *a_group_name,
                                        CORBA::Long a_num_msg,
                                        FtpMsgHistSeq_out a_ftp_msg_seq)
        throw(CORBA::SystemException, RCT::GroupServer::DataSelectionExceedsLimit);

    // Local Method:
    char* get_group_id_from_group_name(const char *a_class_id,
                                       const char *a_group_name);


    // Local Method:
    char* get_group_mgr_from_group_id(const char *a_group_id);

    // Local Method
    Group* get_group_from_group_id(const char *a_group_id);

    
    // Local Method:
    // This method checks if a group already exists in a class
    // with the provided name
    CORBA::Boolean does_group_exist(const char *a_class_name,
                                    const char *a_group_name);

    // Local Method:
    // Method to inform users about a group event
    // Only online users relevant to the class will
    // receive this notification
    void group_class_online_users_notification(const char *a_class_name,
                                               const char *a_group_name,
                                               const char *a_alias,
                                               CORBA::ULong a_msg_type);

    // Local Method:
    // Method to inform users about a group event
    // Only group members will receive this notification
    void group_members_notification(const char *a_class_name,
                                    const char *a_group_name,
                                    const char *a_alias,
                                    CORBA::ULong a_msg_type);
    
    // Local Method:
    // Sends a message to the current leader of the group to choose a
    // new leader
    void choose_new_group_manager(const char *a_class_name,
                                  const char *a_group_name,
                                  const char *a_user_id,
                                  const char *a_user_alias);

    // Local Method:
    // Removes user from the group
    void remove_group_member(const char *a_class_id,
                             const char *a_group_id,
                             const char *a_user_id);

    // Local Method:
    // This function returns the number of online members in a group
    CORBA::Long get_number_of_members_in_group(const char *a_class_id,
                                               const char *a_group_id);


    // Local Method:
    // Removes the group from the database
    void remove_group(const char *a_group_id);
    
    // Local Method:
    // Returning a sequence of groups the user is member of
    void get_groups_user_is_member_of(const char *a_user_id,
                                      GroupSeq_out a_group_seq);

    // Local Method:
    // Check if there is a user with manager permission in a group
    CORBA::Boolean has_group_manager(const char *a_group_id,
                                     CORBA::String_out a_user_id);

    // Local Method:
    // Get one user from the group
    CORBA::Boolean get_one_user_from_group(const char *a_group_id,
                                           CORBA::String_out a_user_id);

    // Local Method:
    // Set the manager
    void set_manager_for_group(const char *a_user_id,
                               const char *a_group_id);
};

typedef MakeSingle<GroupServer_i> GroupServerS;

#endif // __GROUPSERVERIMPL_H__








