/* $Id: Const.h,v 1.81 2004/10/25 15:14:32 thomas Exp $ */

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

#ifndef __RCTCONST_H__
#define __RCTCONST_H__

// Version
#define RCT_VERSION         "1.6.0"

// Enumerated types
///////////////////

// User Tuple Field
enum UserTF { RCT_USER_ID = 0, RCT_USER_ALIAS, RCT_USER_FN, RCT_USER_LN, RCT_USER_PW,
              RCT_USER_PER, RCT_USER_OS, RCT_USER_DATE };

// Index Tuple Field
enum IndexTF { RCT_USER_INDEX = 0, RCT_CLASS_INDEX, RCT_GROUP_INDEX, RCT_TEAM_INDEX,
               RCT_CHAT_INDEX, RCT_FILE_INDEX, RCT_TEXTPAD_INDEX, RCT_CC_INDEX };

// Class Tuple Field
enum ClassTF { RCT_CLASS_ID = 0, RCT_CLASS_NAME, RCT_CLASS_PER, RCT_CLASS_MGR,
               RCT_CLASS_AS, RCT_CLASS_DATE };

// Team Tuple Field
enum TeamTF { RCT_TEAM_ID = 0, RCT_TEAM_NAME, RCT_TEAM_CLASS_ID, RCT_TEAM_PER, RCT_TEAM_MGR,
              RCT_TEAM_AS, RCT_TEAM_DATE };

// Group Tuple Field
enum GroupTF { RCT_GROUP_ID = 0, RCT_GROUP_NAME, RCT_GROUP_CLASS_ID, RCT_GROUP_PER,
               RCT_GROUP_MGR, RCT_GROUP_DATE };

// CHAT TEAM Tuple Field
enum ChatTF { RCT_CHAT_ID = 0, RCT_CHAT_MSG, RCT_CHAT_CREATOR, RCT_CHAT_CLASS_ID,
              RCT_CHAT_TEAM_ID, RCT_CHAT_PER, RCT_CHAT_DATE, RCT_CHAT_VER };

// CHAT TEAM VIEW Tuple Field
enum ChatVTF { RCT_CHATV_ID = 0, RCT_CHATV_MSG, RCT_CHATV_USER_ID, RCT_CHATV_USER_ALIAS,
               RCT_CHATV_CLASS_ID, RCT_CHATV_CLASS_NAME, RCT_CHATV_TEAM_ID, RCT_CHATV_TEAM_NAME,
               RCT_CHATV_PER, RCT_CHATV_DATE, RCT_CHATV_VER };

// File Tuple Field
enum FileTF { RCT_F_ID = 0, RCT_F_ALIAS, RCT_F_NAME, RCT_F_LOCATION, RCT_F_SOURCE,
              RCT_F_MIME_TYPE, RCT_F_CLASS_ID, RCT_F_TEAM_ID, RCT_F_GROUP_ID,
              RCT_F_USER_ID, RCT_F_MODULE, RCT_F_VISIBLE, RCT_F_PER, RCT_F_LENGTH,
              RCT_F_DATE, RCT_F_VER };

// File View Tuple Field 
enum FileVTF { RCT_FV_ID = 0, RCT_FV_ALIAS, RCT_FV_NAME, RCT_FV_LOCATION, RCT_FV_SOURCE,
	       RCT_FV_MIME_TYPE, RCT_FV_CLASS_ID, RCT_FV_CLASS_NAME, RCT_FV_TEAM_ID,
               RCT_FV_TEAM_NAME, RCT_FV_GROUP_ID, RCT_FV_GROUP_NAME, RCT_FV_USER_ID,
               RCT_FV_USER_ALIAS, RCT_FV_MODULE, RCT_FV_VISIBLE, RCT_FV_PER, RCT_FV_LENGTH,
               RCT_FV_DATE, RCT_FV_VER };

// COURSE CONTENT Tuple Field
enum CCTF { RCT_CC_ID = 0, RCT_CC_ALIAS, RCT_CC_NAME, RCT_CC_LOCATION, RCT_CC_SOURCE,
            RCT_CC_MIME_TYPE, RCT_CC_CLASS_ID, RCT_CC_VISIBLE, RCT_CC_PER,
            RCT_CC_LENGTH, RCT_CC_DATE, RCT_CC_VER };

// TEXTPAD Tuple Field
enum TextpadTF { RCT_TP_ID = 0, RCT_TP_NAME, RCT_TP_TEXT, RCT_TP_CLASS_ID, RCT_TP_TEAM_ID,
                 RCT_TP_GROUP_ID, RCT_TP_USER_ID, RCT_TP_PER, RCT_TP_DATE, RCT_TP_VER };

// Mime type Field
enum MimeTypeTF { RCT_MT_MIME0, RE = 0, RCT_MT_FILE_SUFFIX, RCT_MT_UNIX_APPL, RCT_MT_WIN_APPL,
                  RCT_MT_MAC_APPL };

// Control Tuple Field
enum ControlTF { RCT_CON_CALASS_ID = 0, RCT_CON_ASSEMBLY_ID, RCT_CON_USER_ID,
                 RCT_CON_MODULE, RCT_CON_QUEUE_POS };

// Member Team View Tuple Fields
enum MemberTeamViewTF { RCT_MTV_USER_ID = 0, RCT_MTV_USER_ALIAS, RCT_MTV_TEAM_ID,
                        RCT_MTV_TEAM_NAME, RCT_MTV_CLASS_ID, RCT_MTV_CLASS_NAME };

// Modules that access the file server
enum FileAccess { RCT_FA_COMMON = 0 , RCT_FA_COURSE_CONTENT };

// TD Load FIELDS
enum TDTF { RCT_TD_POST_ID = 0, RCT_TD_PARENT_ID, RCT_TD_SENDER,
	    RCT_TD_SUBJECT, RCT_TD_TYPE, RCT_TD_DATE, RCT_TD_IS_READ };  


// Default Entities
#define DEFAULT_USER            "U0"
#define DEFAULT_CLASS           "C0"
#define DEFAULT_TEAM            "T0"
#define DEFAULT_GROUP           "G0"


// Indices prefixes
#define RCT_CLASS   "C"
#define RCT_GROUP   "G"
#define RCT_TEAM    "T"
#define RCT_CHAT    "CH"
#define RCT_FILE    "F"
#define RCT_TEXTPAD "TP"
#define RCT_CC      "CC"

// Module File Prefix
#define MOD_PRE_SOUND   "sound_"
#define MOD_PRE_FTP     "ftp_"
#define MOD_PRE_CC      "cc_"

// Message Types

// BC Message types range 1-99
#define BC_MSG                          1
#define BC_MSG_USER_ONLINE              2
#define BC_MSG_USER_OFFLINE             3

// CHAT Message range 100-199
#define CHAT_GROUP_MSG                  101      
#define CHAT_TEAM_MSG                   102
#define CHAT_ARCHIVE_TEAM_MSG           103

// GROUP Message range 200-299
// GroupServerConst.h

// TEAM Message range 300-399
// TeamServerConst.h

// Sound Message range 400-499
// SoundServerConst.h

// Control Message range 500-599
#define CONTROL_REQ_TEAM                501
#define CONTROL_REQ_GROUP               502
#define CONTROL_REQ_TEAM_NOTIF          503
#define CONTROL_REQ_GROUP_NOTIF         504
#define CONTROL_REL_TEAM                505
#define CONTROL_REL_GROUP               506
#define CONTROL_REL_TEAM_NOTIF          507
#define CONTROL_REL_GROUP_NOTIF         508
#define CONTROL_TAKE_TEAM               509
#define CONTROL_TAKE_GROUP              510
#define CONTROL_TAKE_TEAM_NOTIF         511
#define CONTROL_TAKE_GROUP_NOTIF        512
#define CONTROL_CAN_REQ_TEAM            513
#define CONTROL_CAN_REQ_GROUP           514
#define CONTROL_CAN_REQ_TEAM_NOTIF      515
#define CONTROL_CAN_REQ_GROUP_NOTIF     516
#define CONTROL_ACTIVE_TEAM_NOTIF       517
#define CONTROL_ACTIVE_GROUP_NOTIF      518
#define CONTROL_EXIT_TEAM		519
#define CONTROL_EXIT_GROUP		520
#define CONTROL_EXIT_TEAM_NOTIF         521
#define CONTROL_EXIT_GROUP_NOTIF	522

// Textpad Message range 600-699
// TextpadServerConst.h

// URL Message range 700-799
#define URL_TEAM_SEND                   700
#define URL_GROUP_SEND                  701

// FTP Message range 800-899
// FtpServerConst.h

// Authentication Message range 900-999
#define AUTH_LEVEL_USER                 900
#define AUTH_LEVEL_ADMIN                901

// File Message range 1000-1099
// FileServerConst.h

// Course Content Message range 1100-1199
// CourseContentServerConst.h

// Threaded Discussion Message range 1200-1299
// TDServerConst.h

// Control Page Types
#define PAGE_TEAM                       1
#define PAGE_GROUP                      2


// Type for Team and Group
#define TEAM                            1
#define GROUP                           2

// Assembly Types
#define ASSEMBLY_TYPE_TEAM              1
#define ASSEMBLY_TYPE_GROUP             2

// Boolean true and false
#define TRUE	1
#define FALSE	0

// Boolean for logout
#define NORMAL          1
#define ABNORMAL        0

// Error
#define ERROR -1

// Boolean online status
#define ONLINE true
#define OFFLINE false

// Boolean File Send Has ID
#define HAS_FILE_ID true
#define HAS_NO_FILE_ID false

// Very Short String
#define VSS     20

// Short String
#define SS      80

// Long String
#define LS      256

// Number Of Classes
#define NOC     50

// Max number of archive messages that can be requested at a time
// This is used for team and group existing data access per module
#define RCT_ARCHIVE_MESSAGE_LIMIT 1000


// Base Server definitions:
#define SM1             "Server: OK"
#define SM2             "Server: FAILED"
#define SERVER          "Server"


// Database Server definitions:
#define DSM1            "DatabaseServer: OK"
#define DSM2            "DatabaseServer: FAILED"
#define DBSERVER	"DatabaseServer"
#define DB_NAME         "rctdb"
#define DB_LOGIN        "postgres"
#define DB_PASSWORD     ""
#define DB_GROUP_INDEX_RESET 1

// Authentication Server definitions:
#define ASM1            "AuthenticationServer: OK"
#define ASM2            "AuthenticationServer: FAILED"
#define AUTHSERVER      "AuthenticationServer"
#define ON              "t"
#define OFF             "f"
#define USER_LEVEL      0
#define ADMIN_LEVEL     2
#define ID_PW_OK        "UserId and Password are correct."
#define USER_IS_ONLINE  "User is already online!"
#define PW_NOT_OK       "Password is not correct!"
#define ID_NOT_OK       "UserID is not correct!"
#define VERSION_NOT_OK  "You are using an old client.\nPlease download the latest OpenRCT client.\nThank you."
#define AUTH_ERROR      "Authentication Error!"
#define NOT_ADMIN       "You do not have administrator rights!"


// Class Server definitions:
#define CSM1            "ClassServer: OK"
#define CSM2            "ClassServer: FAILED"
#define CLASSSERVER     "ClassServer"

// Class definitions:
#define CLASS_NAME      "NONAME"


// Chat Server definitions:
#define CHSM1           "ChatServer: OK"
#define CHSM2           "ChatServer: FAILED"
#define CHATSERVER      "ChatServer"


// User Server definitions:
#define USM1            "UserServer: OK"
#define USM2            "UserServer: FAILED"
#define USERSERVER      "UserServer"
#define MAX_NUM_USERS   1000      // Max number of users
#define USER_ID_OFFSET  (unsigned int)1


// Control Server:
#define COSM1           "ControlServer: OK"
#define COSM2           "ControlServer: FAILED"
#define CONTROLSERVER   "ControlServer"


// URL Server:
#define URLSM1          "UrlServer: OK"
#define URLSM2          "UrlServer: FAILED"
#define URLSERVER       "UrlServer"


// Admin Server:
#define ADSM1           "AdminServer: OK"
#define ADSM2           "AdminServer: FAILED"
#define ADMINSERVER     "AdminServer"


// NotifyUtil definitions:
#define MNOC	MAX_NUM_USERS      // Max Number Of Channels


// NameServerUtil definitions:
#define NSROOTCONTEXTID         "RCT"
#define NSROOTCONTEXTKIND       "RCTD"


// Modules
#define MODULE_SOUND            "sound"
#define MODULE_TEXTPAD		"textpad"
#define MODULE_URL              "url"
#define MODULE_FTP              "ftp"
#define MODULE_USER             "user"
#define MODULE_CC               "cc"

// Module File Prefix
#define MODULE_PRE_CC           "cc_"
#define MODULE_PRE_USER         "user_"


// Database Queries and Transactions
// =================================

#define INS_CHAT_LOG_TEAM               "INSERT INTO rct_chat_log_teams (chat_id, chat_msg, creator, class_id, \
                                         team_id, permission, rct_date, rct_version) VALUES ('"

#define INS_CHAT_LOG_GROUP              "INSERT INTO rct_chat_log_groups (chat_id, chat_msg, creator, class_id, \
                                         group_id, permission, rct_date, rct_version) VALUES ('"

#define DELETE_CHAT                     "DELETE FROM rct_chat_log_group WHERE creator='"

#define DEL_CHAT_LOG_TEAM_ALL           "DELETE FROM rct_chat_log_team"

#define DEL_CHAT_LOG_GROUP_ALL          "DELETE FROM rct_chat_log_group"

#define SELECT_CHAT_LOG_FOR_TEAM        "select * from rct_chat_log_teams where team_id='"

#define UPDATE_USR_STAT_ON              "UPDATE rct_users SET online_status=true WHERE alias='"

#define UPDATE_USR_STAT_OFF             "UPDATE rct_users SET online_status=false WHERE alias='"

#define SELECT_USER                     "select * from rct_users where user_id='"

#define SELECT_USER_ALIAS               "select * from rct_users where alias='"

#define SELECT_USER_ID                  "select * from rct_users where user_id='"

#define SELECT_USERS                    "select * from rct_users"

#define SELECT_USERS_IN_CLASS           "select distinct * from rct_users where user_id in (select distinct user_id \
                                         from rct_enrolled where class_id='"

#define SELECT_USERS_IN_TEAM            "select distinct U.* from rct_users U, rct_member_team T \
                                         where U.user_id=T.user_id and T.team_id='"

#define SELECT_USER_IDS_IN_TEAM         "select distinct U.user_id from rct_users U, rct_member_team T \
                                         where U.user_id=T.user_id and T.team_id='"

#define SELECT_USERS_IN_CLASS_ONLINE    "select distinct * from rct_users where online_status='t' and user_id in \
                                        (select distinct user_id from rct_enrolled where class_id in \
                                        (select distinct class_id from rct_classes where class_name='"

#define SELECT_USERS_RELATED_BY_TEAM    "select U.* from rct_users U, rct_member_team M where \
                                         U.online_status='t' and U.user_id=M.user_id and M.team_id='"

#define SELECT_USERS_IN_TEAM_ONLINE     "select distinct U.* from rct_users U, rct_active_user_team A \
                                         where A.user_id=U.user_id and team_id='"

#define SELECT_USER_IDS_IN_TEAM_ONLINE  "select distinct U.user_id from rct_users U, rct_active_user_team A \
                                         where A.user_id=U.user_id and team_id='"

#define SELECT_USERS_IN_GROUP           "select distinct U.* from rct_users U, rct_member_group G \
                                         where U.user_id=G.user_id and G.group_id='"

#define SELECT_USER_IDS_IN_GROUP        "select distinct U.user_id from rct_users U, rct_member_group G \
                                         where U.user_id=G.user_id and G.group_id='"

#define SELECT_USERS_REL_CLASS          "select distinct * from rct_users where user_id in (select distinct user_id \
                                         from rct_enrolled where class_id in \
                                         (select distinct class_id from rct_enrolled where user_id='"

#define SELECT_USERID_STAT_ON           "select user_id, alias from rct_users where online_status='t'"

#define SELECT_USER_IMAGE               "select * from rct_files where file_module='user' and file_user_id='"

#define SELECT_CLASS                    "select * from rct_classes where class_id='"

#define SELECT_CLASSES                  "select * from rct_classes"

#define SELECT_CLASSES_FOR_USER         "select distinct * from rct_classes where class_id in (select distinct \
                                         class_id from rct_enrolled where user_id='"

#define SELECT_CLASSES_FOR_USERS        "select C.* from rct_classes C, rct_enrolled E1, rct_enrolled E2 where \
                                         E1.class_id=E2.class_id and C.class_id=E1.class_id and E1.user_id='"

#define SELECT_CLASS_ID                 "select class_id from rct_classes where class_name='"

#define SELECT_CLASS_ID_FROM_CLASS_NAME "select distinct * from rct_classes where class_name='"

#define SELECT_CLASS_ID_FROM_TEAM_ID    "select class_id from rct_teams where team_id='"

#define SELECT_CLASS_ID_FROM_GROUP_ID   "select class_id from rct_groups where group_id='"

#define SELECT_FILE_APPL_FROM_MIME_TYPE "select * from rct_mime_type where mime_type='"

#define SELECT_FILE_INFO                "select * from rct_mime_type where mime_type='"

#define SELECT_IS_USER_MEMBER_OF_TEAM   "select distinct * from rct_member_team where user_id='"

#define QUERY                           "DECLARE mycursor CURSOR FOR "

#define SELECT_INDICES                  "select * from rct_indices"

#define SELECT_GROUP_INDEX              "select group_index from rct_indices"

#define UPDATE_CHAT_INDEX               "UPDATE rct_indices SET chat_index='"

#define UPDATE_USER_INDEX               "UPDATE rct_indices SET user_index='"

#define UPDATE_GROUP_INDEX              "UPDATE rct_indices SET group_index='"

#define UPDATE_TEAM_INDEX               "UPDATE rct_indices SET team_index='"

#define UPDATE_CLASS_INDEX              "UPDATE rct_indices SET class_index='"

#define UPDATE_FILE_INDEX               "UPDATE rct_indices SET file_index='"

#define UPDATE_TEXTPAD_INDEX            "UPDATE rct_indices SET textpad_index='"

#define INS_USER_LOGGING                "INSERT INTO rct_user_logging VALUES ('"

#define UPDATE_USER_LOGGING             "UPDATE rct_user_logging SET user_off='now', \
                                         exit_normal=true where user_id='"

#define INS_CONTROL                     "INSERT INTO rct_control (class_id, assembly_id, user_id, \
                                         rct_module, queue_position) VALUES ('"

#define SELECT_CONT_QUEUE_POS           "(select max(queue_position) + 1 from rct_control where class_id='"

#define SELECT_CONT_QUEUE_EMPTY         "select * from rct_control where class_id='"

#define SELECT_MOD_QUEUE_USERS          "select U.alias from rct_users U, rct_control C where \
                                         U.user_id=C.user_id and C.class_id='"

#define SELECT_IS_MANAGER_TEAM          "select * from rct_teams where team_id='"

#define SELECT_IS_MANAGER_GROUP         "select * from rct_groups where group_id='"

#define DELETE_CONTROL_REQUEST          "DELETE FROM rct_control WHERE class_id='"

#define SELECT_GET_QUEUE                "select user_id from rct_control where class_id='"

#define UPDATE_CONT_QUEUE_FRONT         "UPDATE rct_control SET user_id='"

#define SELECT_CONT_MIN_QUEUE_POS       "select min(queue_position) from rct_control"

#define INS_FILE                        "INSERT INTO rct_files VALUES ('"

#define INS_TEXTPAD_TEAM                "INSERT INTO rct_textpads (tp_id, tp_name, tp_class_id, tp_team_id, \
                                         tp_group_id, tp_user_id, tp_permission, rct_date, rct_version) VALUES \
                                         ('"

#define SELECT_CURRENT_DB_TIME          "select current_timestamp;"

#endif // __RCTCONST_H__









