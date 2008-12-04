/* $Id: TeamServerConst.h,v 1.6 2003/05/08 20:34:43 thomas Exp $ */

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

#ifndef __TEAMSERVERCONST_H__
#define __TEAMSERVERCONST_H__

// Team Server definitions:
#define TSM1            "TeamServer: OK"
#define TSM2            "TeamServer: FAILED"
#define TEAMSERVER	"TeamServer"

// TEAM Message range 300-399
#define TEAM_JOIN_MSG                   301
#define TEAM_EXIT_MSG                   302

// DB Queries
#define SELECT_TEAM                     "select * from rct_teams where team_id='"

#define SELECT_TEAMS                    "select * from rct_teams"

#define SELECT_TEAMS_FOR_USER_IN_CLASS  "select T.* from rct_teams T, rct_member_team M1, rct_member_team M2 \
                                        where T.team_id=M1.team_id and T.team_id=M2.team_id and M1.user_id='"

#define SELECT_TEAMS_FOR_CLASS          "select distinct * from rct_teams where class_id in (select distinct \
                                         class_id from rct_classes where class_name='"

#define SELECT_MEMBER_TEAMS             "select * from rct_member_team_view where user_id='"

#define DEL_USER_ACTIVE_FROM_TEAM       "DELETE from rct_active_user_team where user_id='" 

#define SELECT_IS_USER_ACTIVE_IN_TEAM   "select distinct * from rct_active_user_team where user_id='"

#define SELECT_TEAMS_USER_IS_ACTIVE_IN  "select distinct T.* from rct_teams T, rct_active_user_team A \
                                         where A.team_id=T.team_id and A.user_id='"

#define COUNT_CHAT_MSG_FOR_TEAM         "select count(*) from rct_chat_log_teams_view where team_id='"
#define SELECT_CHAT_MSG_FOR_TEAM        "select * from rct_chat_log_teams_view where team_id='"

#define COUNT_SOUND_MSG_FOR_TEAM        "select count(*) from rct_files_view where file_module='sound' and file_team_id='"
#define SELECT_SOUND_MSG_FOR_TEAM       "select * from rct_files_view where file_module='sound' and file_team_id='"

#define COUNT_TEXTPAD_MSG_FOR_TEAM      "select count(*) from rct_textpads where tp_team_id='"
#define SELECT_TEXTPAD_MSG_FOR_TEAM     "select tp_id, tp_name, rct_date from rct_textpads where tp_team_id='"

#define COUNT_FTP_MSG_FOR_TEAM          "select count(*) from rct_files_view where file_module='ftp' and file_team_id='"
#define SELECT_FTP_MSG_FOR_TEAM         "select * from rct_files_view where file_module='ftp' and file_team_id='"

#define SELECT_TEAM_ID                  "select team_id from rct_teams where team_name='"

#define INSERT_USER_ONLINE_IN_TEAM      "INSERT INTO rct_active_user_team VALUES ('" 



#endif // __TEAMSERVERCONST_H__
