/* $Id: GroupServerConst.h,v 1.2 2003/05/08 20:34:43 thomas Exp $ */

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

#ifndef __GROUPSERVERCONST_H__
#define __GROUPSERVERCONST_H__

// Group Server definitions:
#define GSM1            "GroupServer: OK"
#define GSM2            "GroupServer: FAILED"
#define GROUPSERVER	"GroupServer"

// GROUP Message range 200-299
#define GROUP_CREATED_MSG               201
#define GROUP_JOIN_REQ_MSG              202
#define GROUP_JOIN_REQ_MSG_GRANTED      203
#define GROUP_JOIN_REQ_MSG_DENIED       204
#define GROUP_JOIN_MEMBER_MSG           205
#define GROUP_CHOOSE_NEW_MGR            206
#define GROUP_MEMBER_EXITS              207
#define GROUP_NEW_MANAGER               208
#define GROUP_REMOVED                   209

// DB Queries
#define SELECT_GROUP                    "select * from rct_groups where group_id='"

#define SELECT_GROUPS                   "select * from rct_grops"

#define SELECT_GROUPS_FOR_USER_IN_CLASS "select G.* from rct_groups G, rct_member_group M where \
                                         G.group_id=M.group_id and M.user_id='"

#define SELECT_GROUPS_FOR_CLASS_ID      "select distinct * from rct_groups where class_id='"

#define SELECT_GROUPS_FOR_CLASS         "select distinct * from rct_groups where class_id in \
                                         (select distinct class_id from rct_classes where class_name='"

#define INS_GROUP                       "INSERT INTO rct_groups VALUES ('"

#define INS_MEMBER_GROUP                "INSERT INTO rct_member_group (user_id, group_id) VALUES ('"

#define UPDATE_MANAGER_GROUP            "UPDATE rct_groups SET manager='"

#define SELECT_USER_IN_GROUP            "select * from rct_member_group where user_id='"

#define SELECT_GROUP_MGR                "select manager from rct_groups where group_id='"

#define SELECT_CHAT_MSG_FOR_GROUP       "select * from rct_chat_log_groups_view where group_id='"

#define SELECT_GROUP_ID                 "select group_id from rct_groups where group_name='"

#define DEL_MEMBER_GROUP                "DELETE from rct_member_group where user_id='"

#define SELECT_MEMBERS_IN_GROUP         "select distinct * from rct_member_group where group_id='"

#define UPDATE_CHAT_GROUP_MSGS          "UPDATE rct_chat_log_groups SET group_id='G0' where group_id='"

#define UPDATE_FILES_GROUP_MSGS         "UPDATE rct_files SET file_group_id='G0' where file_group_id='"

#define UPDATE_TEXTPAD_GROUP_MSGS       "UPDATE rct_textpads SET tp_group_id='G0' where tp_group_id='"

#define DEL_GROUP                       "DELETE from rct_groups where group_id='"

#define SELECT_GROUPS_USER_IS_MEMBER_OF "select distinct G.* from rct_groups G, rct_member_group M where \
                                         M.group_id=G.group_id and M.user_id='"

#define SELECT_HAS_GROUP_MANAGER        "select distinct U.user_id from rct_users U, rct_member_group M \
                                         where U.user_id=M.User_id and U.permission='1' and M.group_id='"

#define SELECT_ONE_GROUP_MEMBER         "select distinct user_id from rct_member_group where group_id='"

#define SELECT_SOUND_MSG_FOR_GROUP      "select * from rct_files_view where file_module='sound' and file_group_id='"

#define SELECT_FTP_MSG_FOR_GROUP        "select * from rct_files_view where file_module='ftp' and file_group_id='"

#endif // __GROUPSERVERCONST_H__




