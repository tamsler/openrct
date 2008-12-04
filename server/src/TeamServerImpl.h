/* $Id: TeamServerImpl.h,v 1.19 2003/05/08 20:34:43 thomas Exp $ */

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

#ifndef __TEAMSERVERIMPL_H__
#define __TEAMSERVERIMPL_H__

#include <string>
#include <iostream>
#include "PermissionObj.hh"
#include "TeamServer.hh"
#include "TeamServerConst.h"
#include "ServerImpl.h"
#include "DatabaseServerImpl.h"
#include "SessionServerImpl.h"
#include "ClassServerImpl.h"
#include "UserServerImpl.h"
#include "ChatServerImpl.h"
#include "SoundServerImpl.h"
#include "TextpadServerImpl.h"
#include "FtpServerImpl.h"
#include "Const.h"
#include "MakeSingle.h"


using namespace RCT;


class TeamServer_i : public POA_RCT::TeamServer,
                     public Server_i
{
    
protected:

    // Constructor:
    inline TeamServer_i() : Server_i(SM1) { }

    // Destructor:
    virtual inline ~TeamServer_i() { }

    
public:

    // IDL Method:
    // Returning a sequence of all teams
    virtual void get_all_teams(TeamSeq_out a_team_seq);

    // IDL Method:
    // Returning a team sequence for a certain user
    virtual void get_teams_from_user_and_class(const char *a_user_id1,
                                               const char *a_user_id2,
                                               const char *a_class_id,
                                               TeamSeq_out a_team_seq);

    // IDL Method:
    // Returnign a team sequence for a certain class name
    virtual void get_teams_from_class_name(const char *a_class_name,
                                           TeamSeq_out a_team_seq);

    // IDL Method:
    // Become active in team
    virtual CORBA::Boolean join_team(const char *a_class_name,
                                     const char *a_team_name,
                                     const char *a_user_id,
                                     const char *a_user_alias);

    // IDL Method:
    // Become inactive in team
    virtual void exit_team(const char *a_class_name,
                           const char *a_team_name,
                           const char *a_user_id,
                           const char *a_user_alias);

    // IDL Method:
    // Return whether user is currently active in the team
    virtual CORBA::Boolean is_user_active_in_team(const char *a_class_name,
                                                  const char *a_team_name,
                                                  const char *a_user_id,
                                                  const char *a_user_alias);

    // IDL Method:
    // Returning a team sequence of teams the user is active in
    virtual void get_teams_user_is_active_in(const char *a_user_id,
                                             TeamSeq_out a_team_seq);

    // IDL Method:
    // Returns the team chat archive for requested (time period) 
    // This returns messages via a sequence
    virtual void get_chat_archive_time_range(const char *a_class_name,
                                             const char *a_team_name,
                                             const char *a_from_date,
                                             const char *a_to_date,
                                             ChatMsgHistSeq_out a_chat_msg_seq)
        throw(CORBA::SystemException, RCT::TeamServer::DataSelectionExceedsLimit);

    // IDL Method:
    // Returns the team sound archvie for requested time range
    virtual void get_sound_archive_time_range(const char *a_class_name,
                                              const char *a_team_name,
                                              const char *a_from_date,
                                              const char *a_to_date,
                                              SoundMsgHistSeq_out a_snd_msg_seq)
                                              throw(CORBA::SystemException, RCT::TeamServer::DataSelectionExceedsLimit);

    // IDL Method:
    // Returns the team textpad archvie for requested time range
    virtual void get_textpad_archive_time_range(const char *a_class_name,
                                                const char *a_team_name,
                                                const char *a_from_date,
                                                const char *a_to_date,
                                                TextpadMsgHistSeq_out a_textpad_msg_seq)
        throw(CORBA::SystemException, RCT::TeamServer::DataSelectionExceedsLimit);

    // IDL Method:
    // Returns the team ftp archvie for requested time range
    virtual void get_ftp_archive_time_range(const char *a_class_name,
                                            const char *a_team_name,
                                            const char *a_from_date,
                                            const char *a_to_date,
                                            FtpMsgHistSeq_out a_ftp_msg_seq)
        throw(CORBA::SystemException, RCT::TeamServer::DataSelectionExceedsLimit);

    // IDL Method:
    // Returns the team chat archive for requested (last n messages)
    // This returns messages via a sequence
    virtual void get_chat_archive_last_n(const char *a_class_name,
                                         const char *a_team_name,
                                         CORBA::Long a_num_msg,
                                         ChatMsgHistSeq_out a_chat_msg_seq)
        throw(CORBA::SystemException, RCT::TeamServer::DataSelectionExceedsLimit);

    // IDL Method:
    // Returns the team sound archive for requested last n sound messages
    // This returns messages via a sequence
    virtual void get_sound_archive_last_n(const char *a_class_name,
                                          const char *a_team_name,
                                          CORBA::Long a_num_snd,
                                          SoundMsgHistSeq_out a_snd_msg_seq)
        throw(CORBA::SystemException, RCT::TeamServer::DataSelectionExceedsLimit);

    // IDL Method:
    // Returns the team textpad archive for requested last n textpads
    // This returns textpads via a sequence
    virtual void get_textpad_archive_last_n(const char *a_class_name,
                                            const char *a_team_name,
                                            CORBA::Long a_num_textpad,
                                            TextpadMsgHistSeq_out a_textpad_msg_seq)
        throw(CORBA::SystemException, RCT::TeamServer::DataSelectionExceedsLimit);

    // IDL Method:
    // Returns the team ftp archive for requested last n ftp files
    // This returns ftp file info via a sequence
    virtual void get_ftp_archive_last_n(const char *a_class_name,
                                        const char *a_team_name,
                                        CORBA::Long a_num_ftp_info,
                                        FtpMsgHistSeq_out a_ftp_msg_seq)
        throw(CORBA::SystemException, RCT::TeamServer::DataSelectionExceedsLimit);
    
    // Local Method:
    Team* get_team_from_team_id(const char *a_team_id);
    
    // Local Method:
    char* get_team_id_from_team_name(const char *a_class_id,
                                     const char *a_team_name);

    // Local Method:
    void update_user_online_in_team_tables(const char *a_team_id,
                                           const char *a_user_id);

    // Local Method:
    void user_joins_team_notification(const char *a_class_name,
                                      const char *a_team_name,
                                      const char *a_user_alias);

    // Local Method:
    void user_exits_team_notification(const char *a_class_name,
                                      const char *a_team_name,
                                      const char *a_user_alias);

    // Local Method:
    // This method checks if a team already exists in a class
    // with the provided name
    CORBA::Boolean does_team_exist(const char *a_class_name,
                                   const char *a_team_name);

    // Local Method:
    // Check for valid date
    CORBA::Boolean is_date_valid(const char *a_date);
};

typedef MakeSingle<TeamServer_i> TeamServerS;

#endif // __TEAMSERVERIMPL_H__












