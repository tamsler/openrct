// $Id: Const.java,v 1.25 2003/07/10 17:00:26 thomas Exp $

/*
 * 
 * OpenRCT - Open Remote Collaboration Tool
 * 
 * Copyright (c) 2000 by Thomas Amsler
 * 
 * This file is part of OpenRCT.
 * 
 * OpenRCT is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * OpenRCT is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * OpenRCT; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 *  
 */

package org.openrct.client;

import org.openrct.client.module.LangModule;
import org.openrct.client.module.PlatformModule;
import java.awt.Font;

// This interface is used to define constant variables

public interface Const {

	// Version
	// -------
	public static final String RCT_VERSION = "1.6.0";
	public static final String NO_TEST = "--no-test";

	// RCT Strings
	// -----------
	public static final String RCT_NAME = "RCT";

	public static final String OPEN_RCT_NAME = "OpenRCT";

	public static final String OPEN_RCT_URL = "www.openrct.org";

	// CORBA Strings
	// -------------
	public static final String ROOT_POA = "RootPOA";

	public static final String NAME_SERVICE = "NameService";

	// JAVA SWING
	// ----------
	public static final String LINE_STYLE = "JTree.lineStyle";

	public static final String ANGLED = "Angled";

	// System specific strings and chars
	// ---------------------------------
	// New line, File Separator, Space, etc.
	public static final String NL = System.getProperty("line.separator");
	
	public static final String FS = System.getProperty("file.separator");
	
	public static final String SP = " ";
	
	public static final String QM = "?";
	
	public static final String EM = "!";
	
	// I18N
	// ----
	public static final String DEFAULT_LOC_PRE = "en";

	public static final String DEFAULT_LOC_SUF = "US";

	// UTILITY
	// -------
	public static final String LOCALHOST_IP = "127.0.0.1";

	// These config vars indicate the Server To Client connection relation
	// GLOB = Global
	// PRIV = Private
	// [Server Com. Type] TO [Client Com. Type]
	public static final int GLOB_TO_GLOB = 1;

	public static final int GLOB_TO_PRIV = 2;

	public static final int PRIV_TO_PRIV = 3;

	public static final int PRIV_TO_GLOB = 4;

	// SERVICE
	// -------
	public static final int RCT_INPUT_LENGTH = 10000; // 10000 Chars

	public static final int RCT_FILE_SIZE = 4194304; // 4MBytes

	// CONFIG
	// ------

	// openrct.properties
	public static final String PROP_VERSION = "openrct.version=" + RCT_VERSION;

	// Data
	public static final String RCT_DATA = System.getProperty("user.home")
			+ System.getProperty("file.separator") + "OpenRctDataDoNotDelete_";

	// Config File
	public static final String CONFIG_DIR = System.getProperty("user.home");
	public static final String PROPERTIES_DIR = "properties" + FS;

	public static final String CONFIG_FILE_RCT = "openrct.properties";

	// Packet size: Based on N * 1024
	public static final int PACKET_SIZE_LAN = 512000;

	public static final int PACKET_SIZE_DSL = 102400;

	public static final int PACKET_SIZE_MOD = 10240;

	// Networking
	public static final int SERVER_NAME = 1;

	public static final int SERVER_IP = 2;

	public static final int SERVER_PORT = 3;

	// JacORB
	// From JacORB 2.2 on, we need to call the properties file
	// orb.properties in order for jacorb to locate it in user's home directory
	//public static final String CONFIG_FILE_ORB = "jacorb.properties";
	//public static final String CONFIG_FILE_TMP = "jacorb.properties-tmp";
	public static final String CONFIG_FILE_ORB = "orb.properties";
	public static final String CONFIG_FILE_TMP = "orb.properties-tmp";
	
	// Sound 1 - 99
	public static final int CONF_SND_MAX_REC_TIME = 30000; // 30 seconds

	public static final int CONF_SND_RECORD = 1;

	public static final int CONF_SND_PLAY = 2;

	public static final int CONF_SND_STOP = 3;

	public static final int CONF_SND_RING_BELL = 4;

	// System 100 - 199
	public static final int CONF_ACTION_TIMER_SLEEP = 60000; // 60 seconds

	public static final int CONF_SYS_KEEP_DATA_DIR = 100;

	public static final int CONF_PULL_CONS_TIMEOUT = 101;

	public static final int CONF_PULL_CONS_SLEEP = 100; // 100 ms

	// Chat 200 - 299
	public static final int CONF_CHAT_RING_BELL = 200;

	// Session 300 - 399
	public static final int CONF_SESSION_RING_BELL = 300;

	public static final int CONF_FIREWALL_NAT = 301;

	public static final int CONF_CONNECTION_SPEED = 302;

	// Textpad 400-499
	public static final int CONF_TEXTPAD_RING_BELL = 400;

	// Url 500-599
	public static final int CONF_URL_RING_BELL = 500;

	// Ftp 600-699
	public static final int CONF_FTP_RING_BELL = 600;

	// Team 700-799
	public static final int CONF_TEAM_RING_BELL = 700;

	// Group 800-899
	public static final int CONF_GROUP_RING_BELL = 800;

	// Unicode 900 - 999
	public static final String UNICODE_FONT = "Arial Unicode MS";
	//public static final String UNICODE_FONT = "Code2000";
	//public static final String UNICODE_FONT = "Bitstream Cyberbit";
	
	public static final int UNICODE_FONT_SIZE = 16;

	public static final int UNICODE_BORDER_FONT_SIZE = 12;

	public static final int CONF_UNICODE_FONT = 900;

	public static final int CONF_UNICODE_FONT_SIZE = 901;

	public static final Font UNICODE_FONT_12 = new Font(UNICODE_FONT,
			Font.PLAIN, 12);

	public static final Font UNICODE_FONT_16 = new Font(UNICODE_FONT,
			Font.PLAIN, 16);

	// Service 1000 - 1999
	public static final int CONF_INPUT_LENGTH = 1000;

	public static final int CONF_FILE_SIZE = 1001;

	// SPLASH SCREEN
	// -------------
	public static final String SPLASH_IMG = "images" + FS + "splash.jpg";

	public static final String AUTH_STATUS_IMG = "images" + FS + "status.gif";

	// Default Entities
	public static final String DEFAULT_USER = "U0";

	public static final String DEFAULT_CLASS = "C0";

	public static final String DEFAULT_TEAM = "T0";

	public static final String DEFAULT_GROUP = "G0";

	// Module File Prefix
	public static final String MOD_PRE_SOUND = "sound_";

	public static final String MOD_PRE_FTP = "ftp_";

	public static final String MOD_PRE_CC = "cc_";

	// Users
	public static final String USER = "0";

	public static final String MANAGER = "1";

	public static final String ADMIN = "2";

	public static final String TYPE_USER = "User";

	public static final String TYPE_MANAGER = "Manager";

	public static final String TYPE_ADMIN = "Admin";

	public static final String TYPE_UNDEF = "";

	// Formatting string for the Node classes
	public static final String STATUS_ON_HEAD_USER = "<html><font color=black><b>[on-line] User : ";

	public static final String STATUS_OFF_HEAD_USER = "<html><font color=black>[off-line] User : ";

	public static final String STATUS_ON_HEAD_CLASS = "<html><font color=black>Class : ";

	public static final String STATUS_ON_HEAD_TEAM = "<html><font color=black>Team : ";

	public static final String STATUS_ON_HEAD_GROUP = "<html><font color=black>Group : ";

	public static final String STATUS_OFF_HEAD_CLASS = "<html><font color=black>Class : ";

	public static final String STATUS_OFF_HEAD_TEAM = "<html><font color=black>Team : ";

	public static final String STATUS_OFF_HEAD_GROUP = "<html><font color=black>Group : ";

	public static final String STATUS_ON_TAIL = "</font></html>";

	public static final String STATUS_OFF_TAIL = "</b></font></html>";

	// Default BC msg creator
	public static final String MSG_CREATOR = "RCT";

	// Name Service Context
	public static final String BC_CHANNEL_ID = "EventChannel";

	public static final String BC_CHANNEL_KIND = "EventChannel";

	public static final String RCT_ID = "RCT";

	public static final String RCT_KIND = "RCTD";

	public static final String SESSION_ID = "SessionServer";

	public static final String SESSION_KIND = "Server";

	public static final String AUTH_ID = "AuthenticationServer";

	public static final String AUTH_KIND = "Server";

	public static final String USER_ID = "UserServer";

	public static final String USER_KIND = "Server";

	public static final String CLASS_ID = "ClassServer";

	public static final String CLASS_KIND = "Server";

	public static final String TEAM_ID = "TeamServer";

	public static final String TEAM_KIND = "Server";

	public static final String GROUP_ID = "GroupServer";

	public static final String GROUP_KIND = "Server";

	public static final String F_ID = "FileServer";

	public static final String F_KIND = "Server";

	public static final String PING_ID = "PingServer";

	public static final String PING_KIND = "Server";

	public static final String CHAT_ID = "ChatServer";

	public static final String CHAT_KIND = "Server";

	public static final String COURSEC_ID = "CourseContentServer";

	public static final String COURSEC_KIND = "Server";

	public static final String SOUND_ID = "SoundServer";

	public static final String SOUND_KIND = "Server";

	public static final String CONTROL_ID = "ControlServer";

	public static final String CONTROL_KIND = "Server";

	public static final String URL_ID = "UrlServer";

	public static final String URL_KIND = "Server";

	public static final String FTP_ID = "FtpServer";

	public static final String FTP_KIND = "Server";

	public static final String TP_ID = "TextpadServer";

	public static final String TP_KIND = "Server";

	public static final String TD_ID = "TDServer";

	public static final String TD_KIND = "Server";

	public static final String ADMIN_ID = "AdminServer";

	public static final String ADMIN_KIND = "Server";

	// User And Class View String
	// --------------------------

	public static final String USER_NODE = "U: ";

	public static final String CLASS_NODE = "C: ";

	public static final String TEAM_NODE = "T: ";

	public static final String GROUP_NODE = "G: ";

	// Message Types
	// -------------

	// BC Message types range 1-99
	public static final int BC_MSG = 1;

	public static final int BC_MSG_USER_ONLINE = 2;

	public static final int BC_MSG_USER_OFFLINE = 3;

	// CHAT Message range 100-199
	public static final int CHAT_GROUP_MSG = 101;

	public static final int CHAT_TEAM_MSG = 102;

	public static final int CHAT_ARCHIVE_TEAM_MSG = 103;

	// GROUP Message range 200-299
	public static final int GROUP_CREATED_MSG = 201;

	public static final int GROUP_JOIN_REQ_MSG = 202;

	public static final int GROUP_JOIN_REQ_MSG_GRANTED = 203;

	public static final int GROUP_JOIN_REQ_MSG_DENIED = 204;

	public static final int GROUP_JOIN_MEMBER_MSG = 205;

	public static final int GROUP_CHOOSE_NEW_MGR = 206;

	public static final int GROUP_MEMBER_EXITS = 207;

	public static final int GROUP_NEW_MANAGER = 208;

	public static final int GROUP_REMOVED = 209;

	// TEAM Message range 300-399
	public static final int TEAM_JOIN_MSG = 301;

	public static final int TEAM_EXIT_MSG = 302;

	// SOUND Message range 400-499
	public static final int SOUND_TEAM = 401;

	public static final int SOUND_GROUP = 402;

	public static final int SOUND_TEAM_PLAY = 403;

	public static final int SOUND_GROUP_PLAY = 404;

	public static final int SOUND_ARCHIVE_PLAY = 405;

	// CONTROL Message range 500-599
	public static final int CONTROL_REQ_TEAM = 501;

	public static final int CONTROL_REQ_GROUP = 502;

	public static final int CONTROL_REQ_TEAM_NOTIF = 503;

	public static final int CONTROL_REQ_GROUP_NOTIF = 504;

	public static final int CONTROL_REL_TEAM = 505;

	public static final int CONTROL_REL_GROUP = 506;

	public static final int CONTROL_REL_TEAM_NOTIF = 507;

	public static final int CONTROL_REL_GROUP_NOTIF = 508;

	public static final int CONTROL_TAKE_TEAM = 509;

	public static final int CONTROL_TAKE_GROUP = 510;

	public static final int CONTROL_TAKE_TEAM_NOTIF = 511;

	public static final int CONTROL_TAKE_GROUP_NOTIF = 512;

	public static final int CONTROL_CAN_REQ_TEAM = 513;

	public static final int CONTROL_CAN_REQ_GROUP = 514;

	public static final int CONTROL_CAN_REQ_TEAM_NOTIF = 515;

	public static final int CONTROL_CAN_REQ_GROUP_NOTIF = 516;

	public static final int CONTROL_ACTIVE_TEAM_NOTIF = 517;

	public static final int CONTROL_ACTIVE_GROUP_NOTIF = 518;

	public static final int CONTROL_EXIT_TEAM = 519;

	public static final int CONTROL_EXIT_GROUP = 520;

	public static final int CONTROL_EXIT_TEAM_NOTIF = 521;

	public static final int CONTROL_EXIT_GROUP_NOTIF = 522;

	// Textpad Message range 600-699
	public static final int TEXTPAD_TEAM_CREATE = 600;

	public static final int TEXTPAD_GROUP_CREATE = 601;

	public static final int TEXTPAD_TEAM_CLOSE = 602;

	public static final int TEXTPAD_GROUP_CLOSE = 603;

	public static final int TEXTPAD_TEAM_REM_INS = 604;

	public static final int TEXTPAD_GROUP_REM_INS = 605;

	public static final int TEXTPAD_TEAM_INS = 606;

	public static final int TEXTPAD_GROUP_INS = 607;

	public static final int TEXTPAD_TEAM_REM = 608;

	public static final int TEXTPAD_GROUP_REM = 609;

	public static final int TEXTPAD_TEAM_EDIT = 610;

	public static final int TEXTPAD_GROUP_EDIT = 611;

	// URL Message range 700-799
	public static final int URL_TEAM_SEND = 700;

	public static final int URL_GROUP_SEND = 701;

	// FTP Message range 800-899
	public static final int FTP_TEAM_UPLOAD = 800;

	public static final int FTP_GROUP_UPLOAD = 801;

	public static final int FTP_TEAM_DOWNLOAD = 802;

	public static final int FTP_GROUP_DOWNLOAD = 803;

	public static final int FTP_ARCHIVE_DOWNLOAD = 804;

	// Authentication Message range 900-999
	public static final int AUTH_LEVEL_USER = 900;

	public static final int AUTH_LEVEL_ADMIN = 901;

	// File Message range 1000-1099
	public static final int FILE_DOWNLOAD_PACKET = 1000;

	public static final int FILE_DOWNLOAD_END = 1001;

	public static final int FILE_DOWNLOAD_STOPPED = 1002;

	// Course Content range 1100-1199
	public static final int CC_DOWNLOAD = 1100;

	// Modules
	// -------

	// General
	//public static final int SESSION_FRAME_WIDTH =
	// java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
	//public static final int SESSION_FRAME_HEIGHT =
	// java.awt.Toolkit.getDefaultToolkit().getScreenSize().height - 100;
	public static final int SESSION_FRAME_WIDTH = 800;

	public static final int SESSION_FRAME_HEIGHT = 600;

	public static final boolean GRANTED = true;

	public static final boolean DENIED = false;

	public static final boolean NORMAL = true;

	public static final boolean ABNORMAL = false;

	public static final boolean RING_BELL_ON = true;

	public static final boolean RING_BELL_OFF = false;

	public static final String TRUE_STR1 = "true";

	public static final String TRUE_STR2 = "t";

	public static final String FALSE_STR1 = "false";

	public static final String FALSE_STR2 = "f";

	public static final int ERROR = -1;

	public static final int PAGE_TEAM = 1;

	public static final int PAGE_GROUP = 2;

	public static final int ASSEMBLY_TYPE_TEAM = 1;

	public static final int ASSEMBLY_TYPE_GROUP = 2;

	public static final String MODULE_SOUND = "sound";

	public static final String MODULE_SOUND_ = "sound_";

	public static final String MODULE_TEXTPAD = "textpad";

	public static final String MODULE_TEXTPAD_ = "textpad_";

	public static final String MODULE_WHITEBOARD = "whiteboard";

	public static final String MODULE_CHAT = "chat";

	public static final String MODULE_MAIL = "mail";

	public static final String MODULE_FTP = "ftp";

	public static final String MODULE_FTP_ = "ftp_";

	public static final String MODULE_URL = "url";

	public static final String MODULE_USER = "user";

	public static final String MODULE_CLASS = "class";

	public static final String MODULE_TEAM = "team";

	public static final String MODULE_GROUP = "group";

	public static final String MODULE_CC = "cc";

	public static final String MODULE_CC_ = "cc_";

	public static final String MODULE_TD = "td";

	public static final String MODULE_TD_ = "td_";

	public static final int MOD_CHAT = 1;

	public static final int MOD_SOUND = 2;

	public static final int MOD_TEXTPAD = 3;

	public static final int MOD_WHITEBOARD = 4;

	public static final int MOD_URL = 5;

	public static final int MOD_MAIL = 6;

	public static final int MOD_FTP = 7;

	public static final int MOD_USER = 8;

	public static final int MOD_CLASS = 9;

	public static final int MOD_TEAM = 10;

	public static final int MOD_GROUP = 11;

	public static final int MOD_CC = 12;

	public static final int MOD_TD = 13;

	public static final int MOD_UNDEFINED = -1;

	public static final int RING_BELL_NOW = 1;

	public static final int RING_BELL_TIMED = 2;

	// ServerListData
	public static final String SERVER_LIST_URL = "http://www.openrct.org/OpenRctServerList.html";

	public static final String SERVER_LIST_DELIM = ":";

	public static final int SERVER_LIST_MARK = 100000;

	// ServerListDialog
	public static final int SERVER_LIST_WIDTH = 850;

	public static final int SERVER_LIST_HEIGHT = 370;

	// Cache Module
	public static final boolean HIT = true;

	public static final boolean MISS = false;

	// AuthModuleGui
	public static final int AUTH_GUI_WIDTH = 470;

	public static final int AUTH_GUI_HEIGHT = 370;

	// Authentication Module
	public static final int LOGIN_LEN = 35;

	public static final int PW_LEN = 35;

	public static final int SERVER_LIST_VERSION = 0;

	public static final int SERVER_LIST_STATUS = 1;

	public static final int SERVER_LIST_NAME = 2;

	public static final int SERVER_LIST_PORT = 3;

	public static final int SERVER_LIST_ORGANIZATION = 4;

	public static final int SERVER_LIST_DEPARTMENT = 5;

	public static final int SERVER_LIST_EMAIL = 6;

	public static final int SERVER_LIST_COMMENT = 7;

	public static final int SERVER_LIST_N_FIELDS = 8;

	public static final String[] SERVER_LIST_COLUMN_NAMES = {
			LangModule.i18n.getString("ServerListVersion"),
			LangModule.i18n.getString("ServerListStatus"),
			LangModule.i18n.getString("ServerListName"),
			LangModule.i18n.getString("ServerListPort"),
			LangModule.i18n.getString("ServerListOrganization"),
			LangModule.i18n.getString("ServerListDepartment"),
			LangModule.i18n.getString("ServerListEmail"),
			LangModule.i18n.getString("ServerListComment") };

	// Ping Module
	public static final int PING_LOOP_SLEEP = 60000; // 1 minute

	// Chat Module
	public static final int CLASS_TEAM_TYPE = 1;

	public static final int CLASS_GROUP_TYPE = 2;

	public static final int CLASS_GROUP_MGT_TYPE = 3;

	public static final int MIN_N_MSG = 0;

	public static final int MAX_N_MSG = 100;

	public static final int TICK_N_MSG = 10;

	public static final String[] DAYS_OF_MONTH = { "01", "02", "03", "04",
			"05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15",
			"16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26",
			"27", "28", "29", "30", "31" };

	public static final String[] MONTHS_OF_YEAR_STR = {
			LangModule.i18n.getString("ConstJan"),
			LangModule.i18n.getString("ConstFeb"),
			LangModule.i18n.getString("ConstMar"),
			LangModule.i18n.getString("ConstApr"),
			LangModule.i18n.getString("ConstMay"),
			LangModule.i18n.getString("ConstJun"),
			LangModule.i18n.getString("ConstJul"),
			LangModule.i18n.getString("ConstAug"),
			LangModule.i18n.getString("ConstSep"),
			LangModule.i18n.getString("ConstOct"),
			LangModule.i18n.getString("ConstNov"),
			LangModule.i18n.getString("ConstDec") };

	public static final String[] MONTHS_OF_YEAR_NUM = { "01", "02", "03", "04",
			"05", "06", "07", "08", "09", "10", "11", "12" };

	public static final String[] YEARS = { "2002", "2003", "2004", "2005",
			"2006", "2007", "2008", "2009" };

	// Archive
	public static final String[] ARCHIVE_MODULES = {
			LangModule.i18n.getString("ArchiveModuleChat"),
			LangModule.i18n.getString("ArchiveModuleSound"),
			LangModule.i18n.getString("ArchiveModuleTextpad"),
			LangModule.i18n.getString("ArchiveModuleFtp") };

	public static final int ARCH_MOD_CHAT = 0;

	public static final int ARCH_MOD_SOUND = 1;

	public static final int ARCH_MOD_TEXTPAD = 2;

	public static final int ARCH_MOD_FTP = 3;

	public static final String[] ARCHIVE_DATA_SELECTION = {
			LangModule.i18n.getString("ArchiveDataToday"),
			LangModule.i18n.getString("ArchiveDataLastN"),
			LangModule.i18n.getString("ArchiveDataTimeRange") };

	public static final int ARCH_SEL_TODAY = 0;

	public static final int ARCH_SEL_LASTN = 1;

	public static final int ARCH_SEL_TIMERANGE = 2;

	// Session Module / GUI
	public static final int USER_VIEW_PAGE = 0;

	public static final int CLASS_VIEW_PAGE = 1;

	public static final int MODULE_PAGE = 2;

	public static final String USER_IMG = "images/User.gif";

	public static final String CLASS_IMG = "images/Class.gif";

	public static final String TEAM_IMG = "images/Team.gif";

	public static final String GROUP_IMG = "images/Group.gif";

	// SessionAboutGui
	public static final int SESSION_ABOUT_GUI_PRODUCT = 0;

	public static final int SESSION_ABOUT_GUI_VERSION = 1;

	public static final int SESSION_ABOUT_GUI_CPRIGHT = 2;

	public static final int SESSION_ABOUT_GUI_COMMENTS = 3;

	public static final int SESSION_ABOUT_GUI_JAVA_VER = 4;

	public static final int SESSION_ABOUT_GUI_URL = 5;

	public static final int SESSION_ABOUT_GUI_SERVER = 6;

	public static final int SESSION_ABOUT_GUI_BUTTON = 7;

	public static final int SESSION_ABOUT_GUI_N_FIELDS = 8;

	public static final String[] SESSION_ABOUT_GUI_LABELS = {
			LangModule.i18n.getString("SessionAboutProduct"),
			LangModule.i18n.getString("SessionAboutVersion") + RCT_VERSION,
			LangModule.i18n.getString("SessionAboutCopyRight"),
			LangModule.i18n.getString("SessionAboutComments"),
			"Java v" + PlatformModule.getJavaVersion(), "URL: " + OPEN_RCT_URL,
			"ServerName : Port", LangModule.i18n.getString("ButtonLabelOk") };

	// File Module
	public static final int FILE_ID = 0;

	public static final int FILE_ALIAS = 1;

	public static final int FILE_NAME = 2;

	public static final int FILE_LOCATION = 3;

	public static final int FILE_SOURCE = 4;

	public static final int FILE_MIME_TYPE = 5;

	public static final int FILE_CLASS_ID = 6;

	public static final int FILE_CLASS_NAME = 7;

	public static final int FILE_TEAM_ID = 8;

	public static final int FILE_TEAM_NAME = 9;

	public static final int FILE_GROUP_ID = 10;

	public static final int FILE_GROUP_NAME = 11;

	public static final int FILE_USER_ID = 12;

	public static final int FILE_USER_ALIAS = 13;

	public static final int FILE_MODULE = 14;

	public static final int FILE_VISIBLE = 15;

	public static final int FILE_PER = 16;

	public static final int FILE_DATE = 17;

	public static final int FILE_VER = 18;

	public static final int FILE_LENGTH = 19;

	public static final int FILE_N_FIELDS = 20;

	public static final String[] FILE_COLUMN_NAMES = {
			LangModule.i18n.getString("FileId"),
			LangModule.i18n.getString("FileAlias"),
			LangModule.i18n.getString("FileName"),
			LangModule.i18n.getString("FileLocation"),
			LangModule.i18n.getString("FileSource"),
			LangModule.i18n.getString("FileMimeType"),
			LangModule.i18n.getString("FileClassId"),
			LangModule.i18n.getString("FileClassName"),
			LangModule.i18n.getString("FileTeamId"),
			LangModule.i18n.getString("FileTeamName"),
			LangModule.i18n.getString("FileGroupId"),
			LangModule.i18n.getString("FileGroupName"),
			LangModule.i18n.getString("FileUserId"),
			LangModule.i18n.getString("FileUserAlias"),
			LangModule.i18n.getString("FileModule"),
			LangModule.i18n.getString("FileVisible"),
			LangModule.i18n.getString("FilePer"),
			LangModule.i18n.getString("FileDate"),
			LangModule.i18n.getString("FileVer"),
			LangModule.i18n.getString("FileLength") };

	public static final int COL1_WIDTH = 185;

	public static final int COL2_WIDTH = 150;

	public static final int COL3_WIDTH = 500;

	public static final String FILE_DEF_SAVE_NAME = "OpenRCTFile.txt";

	public static final String FILE_UTF_8_ENCODING = "UTF8";

	// Course Content
	public static final String CC_SOURCE_SERVER = "1";

	public static final String CC_SOURCE_CLIENT = "2";

	public static final String CC_SOURCE_WEB = "3";

	public static final int CC_ID = 0;

	public static final int CC_ALIAS = 1;

	public static final int CC_NAME = 2;

	public static final int CC_LOCATION = 3;

	public static final int CC_SOURCE = 4;

	public static final int CC_MIME_TYPE = 5;

	public static final int CC_CLASS_ID = 6;

	public static final int CC_VISIBLE = 7;

	public static final int CC_PER = 8;

	public static final int CC_LENGTH = 9;

	public static final int CC_DATE = 10;

	public static final int CC_VER = 11;

	public static final int CC_N_FIELDS = 12;

	public static final String[] CC_COLUMN_NAMES = {
			LangModule.i18n.getString("FileId"),
			LangModule.i18n.getString("FileAlias"),
			LangModule.i18n.getString("FileName"),
			LangModule.i18n.getString("FileLocation"),
			LangModule.i18n.getString("FileSource"),
			LangModule.i18n.getString("FileMimeType"),
			LangModule.i18n.getString("FileClassId"),
			LangModule.i18n.getString("FileVisible"),
			LangModule.i18n.getString("FilePer"),
			LangModule.i18n.getString("FileLength"),
			LangModule.i18n.getString("FileDate"),
			LangModule.i18n.getString("FileVer") };

	// Plaform Module
	public static final String PROP_USER_HOME = "user.home";

	public static final String PROP_OS = "os.name";

	public static final String DEF_IP = "0.0.0.0";

	public static final String OS_WIN = "windows";

	public static final String OS_WIN95 = "windows 95";

	public static final String OS_WIN98 = "windows 98";

	public static final String OS_WINME = "windows me";

	public static final String OS_WINNT = "windows nt";

	public static final String OS_WIN2000 = "windows 2000";

	public static final String OS_WINXP = "windows xp";

	public static final String OS_LIN = "linux";

	public static final String OS_MAC_X = "mac os x";

	public static final String[] JAVA_VER_MAC_OS_X = { "1.3.1", "1.4.1_01" };

	public static final String[] JAVA_VER_SUN_JDK = { "1.4.2_05", "1.4.1_03",
			"1.4.1_02", "1.3.1_07", "1.3.1_06", "1.3.1_05" };
	
	public static final String JAVA_VER_EXCLUDE = "5";

	public static final String[] SUPPORTED_PLATFORMS = { OS_WIN95, OS_WIN98,
			OS_WINME, OS_WINNT, OS_WIN2000, OS_WINXP, OS_LIN, OS_MAC_X };

	public static final String[] UNSUPPORTED_IP_NUMS = { "169.254" };

	// User Info Gui
	public static final String USER_DEF_IMG = "images/notAvailable.jpg";

	// Sound Module
	public static final int TYPE_RECORD = 1;

	public static final int TYPE_PLAY = 2;

	public static final String TEMP_SOUND_NAME_AU = "temp_sound.au";

	public static final String TEMP_SOUND_NAME_GSM = "temp_sound.gsm";

	public static final String SOUND_PREFIX = "sound_";

	public static final String SOUND_EXT_AU = ".au";

	public static final String SOUND_EXT_GSM = ".gsm";

	public static final String SOUND_MIME_TYPE = "audio/basic";

	public static final int AU_TYPE = 1;

	public static final int WAV_TYPE = 2;

	public static final int GSM_TYPE = 3;

	public static final int USER_PRESSED_STOP_BUTTON = 1;

	public static final int AUTO_PRESSED_STOP_BUTTON = 2;

	public static final int SOUND_STATUS_UNDEF = 0;

	public static final int SOUND_STATUS_PLAY = 1;

	public static final int SOUND_STATUS_RECORD = 2;

	public static final int SOUND_STATUS_CANCEL = 3;

	public static final int SOUND_STATUS_STOP = 4;

	// Textpad Module
	public static final int WORKER_THREAD_SLEEP = 1000;

	public static final int TEXTPAD_ID = 0;

	public static final int TEXTPAD_NAME = 1;

	public static final int TEXTPAD_TEXT = 2;

	public static final int TEXTPAD_CLASS_ID = 3;

	public static final int TEXTPAD_TEAM_ID = 4;

	public static final int TEXTPAD_GROUP_ID = 5;

	public static final int TEXTPAD_USER_ID = 6;

	public static final int TEXTPAD_PER = 7;

	public static final int TEXTPAD_DATE = 8;

	public static final int TEXTPAD_VER = 9;

	public static final int TEXTPAD_N_FIELDS = 10;

	public static final String[] TEXTPAD_COLUMN_NAMES = {
			LangModule.i18n.getString("TextpadId"),
			LangModule.i18n.getString("TextpadName"),
			LangModule.i18n.getString("TextpadText"),
			LangModule.i18n.getString("TextpadClassId"),
			LangModule.i18n.getString("TextpadTeamId"),
			LangModule.i18n.getString("TextpadGroupId"),
			LangModule.i18n.getString("TextpadUserId"),
			LangModule.i18n.getString("TextpadPer"),
			LangModule.i18n.getString("TextpadDate"),
			LangModule.i18n.getString("TextpadVer") };

	public static final String TEXTPAD_DEF_SAVE_NAME = "OpenRctTextpad.txt";

	public static final int TEXTPAD_LIST_ID = 0;

	public static final int TEXTPAD_LIST_NAME = 1;

	public static final int TEXTPAD_LIST_DATE = 2;

	public static final int TEXTPAD_LIST_CLASS_NAME = 3;

	public static final int TEXTPAD_LIST_TEAM_NAME = 4;

	public static final int TEXTPAD_LIST_N_FIELDS = 5;

	public static final String[] TEXTPAD_LIST_COLUMN_NAMES = {
			LangModule.i18n.getString("TextpadId"),
			LangModule.i18n.getString("TextpadName"),
			LangModule.i18n.getString("TextpadDate"),
			LangModule.i18n.getString("TextpadClassName"),
			LangModule.i18n.getString("TextpadTeamName") };

	// Url Module
	public static final String URL_PREFIX = "http://";

	public static final String URL_DEFAULT = "www.openrct.org";

	// UrlModuleGui
	public static final String URL_BROWSER_LINUX_1 = "netscape";

	public static final String URL_BROWSER_LINUX_2 = "mozilla";

	// Ftp Module
	public static final String FTP_MIME_TYPE = "application/byte-stream";

	public static final int FTP_STATUS_UNDEF = 0;

	public static final int FTP_STATUS_DOWNLOAD = 1;

	public static final int FTP_STATUS_UPLOAD = 2;

	public static final int FTP_STATUS_CANCEL = 3;

	// TD Module
	public static final int STATUS = 0;

	public static final int SUBJECT = 1;

	public static final int SENDER = 2;

	public static final int TYPE = 3;

	public static final int REPLIES = 4;

	public static final int DATE = 5;

	public static final int TD_ROW_HEIGHT = 18;
}

