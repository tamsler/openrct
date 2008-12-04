--- $Id: rct-test.sql,v 1.34 2003/05/22 20:08:22 thomas Exp $

--- Creating table RCT_USERS
CREATE TABLE rct_users (
	user_id		varchar(80) primary key,
        alias		varchar(80) not null unique,
	first_name	varchar(80) not null,
	last_name	varchar(80) not null,
	password	varchar(80) not null,
	permission	integer not null,
	online_status	bool not null,
	rct_date	timestamp not null
);

--- Creating table RCT_CLASSES
CREATE TABLE rct_classes (
	class_id	varchar(80) primary key,
	class_name	varchar(80) not null,
	permission	integer not null,
	manager		varchar(80) not null references rct_users,
	active_status	bool not null,
	rct_date	timestamp not null
);


--- Creating table RCT_GROUPS
CREATE TABLE rct_groups (
	group_id	varchar(80) primary key,
	group_name	varchar(80) not null,
	class_id	varchar(80) not null references rct_classes,
	permission	integer not null,
	manager		varchar(80) not null references rct_users,
	rct_date	timestamp not null
);


--- Creating table RCT_TEAMS
CREATE TABLE rct_teams (
	team_id		varchar(80) primary key,
	team_name	varchar(80) not null,
        class_id	varchar(80) not null references rct_classes,
	permission	integer not null,
	manager		varchar(80) not null references rct_users,
	active_status	bool not null,
	rct_date	timestamp not null
);

--- Creating table RCT_CHAT_LOG_GROUPS
CREATE TABLE rct_chat_log_groups (
	chat_id		varchar(80) primary key,
        chat_msg	text,
	creator		varchar(80) not null references rct_users,
	class_id	varchar(80) not null references rct_classes,
	group_id	varchar(80) not null references rct_groups,
	permission	integer not null,
	rct_date	timestamp not null,
	rct_version	varchar(10) not null
);


--- Creating table RCT_CHAT_LOG_TEAMS
CREATE TABLE rct_chat_log_teams (
	chat_id		varchar(80) primary key,
        chat_msg	text,
	creator		varchar(80) not null references rct_users,
	class_id	varchar(80) not null references rct_classes,
	team_id		varchar(80) not null references rct_teams,
	permission	integer not null,
	rct_date	timestamp not null,
	rct_version	varchar(10) not null
);

--- Creating table RCT_INDICES
CREATE TABLE rct_indices (
	user_index	integer not null,
	class_index	integer not null,
	group_index	integer not null,
	team_index	integer not null,
	chat_index	integer not null,
	file_index	integer not null,
	textpad_index	integer not null,
	cc_index	integer not null
);


--- Creating table RCT_ENROLLED
CREATE TABLE rct_enrolled (
	user_id		varchar(80) references rct_users,
	class_id	varchar(80) references rct_classes,
	rct_date	timestamp not null,
	primary key (user_id, class_id)
);

--- Creating table RCT_MEMBER_TEAM
CREATE TABLE rct_member_team (
       user_id		     varchar(80) references rct_users,
       team_id		     varchar(80) references rct_teams,
       primary key (user_id, team_id)
);


--- Creating table RCT_MEMBER_GROUP
CREATE TABLE rct_member_group (
       user_id               varchar(80) references rct_users,
       group_id		     varchar(80) references rct_groups,
       primary key (user_id, group_id)
);


--- Creating table RCT_ACTIVE_USER_TEAM
CREATE TABLE rct_active_user_team (
	user_id		varchar(80) references rct_users,
	team_id		varchar(80) references rct_teams,
	primary key (user_id, team_id)
);

--- Creating table RCT_ADMIN_TRANSACTIONS
CREATE TABLE rct_admin_transactions (
	user_id		varchar(80) not null,
        rct_date	timestamp not null,
	rct_query	text not null
);


--- Creating table RCT_FILES
--- The file_source is: { 1 = server, 2 = client, 3 = web, ... }
CREATE TABLE rct_files (
	file_id		varchar(80),
	file_alias	varchar(250) not null,
	file_name	varchar(250) not null,
	file_location	varchar(250),
	file_source	integer not null,
	file_mime_type	varchar(80),
	file_class_id	varchar(80) references rct_classes,
	file_team_id	varchar(80) references rct_teams,
	file_group_id	varchar(80) references rct_groups,
	file_user_id	varchar(80) references rct_users,
	file_module	varchar(80) not null,
	file_visible	bool not null,
	file_permission integer not null,
	file_length	varchar(10),
	rct_date	timestamp not null,
	rct_version	varchar(10) not null,
	primary key (file_id, file_class_id, file_team_id,
                     file_group_id, file_user_id)
);

--- Creating table RCT_COURSE_CONTENT
--- The cc_source is: { 1 = server, 2 = client, 3 = web, ... }
CREATE TABLE rct_course_content (
	cc_id		varchar(80) primary key,
	cc_alias	varchar(250) not null,
	cc_name	varchar(250) not null,
	cc_location	varchar(250),
	cc_source	integer not null,
	cc_mime_type	varchar(80),
	cc_class_id	varchar(80) references rct_classes,
	cc_visible	bool not null,
	cc_permission integer not null,
	cc_length	varchar(10),
	rct_date	timestamp not null,
	rct_version	varchar(10) not null
);

--- Creating table RCT_USER_LOGGING
--- We don't use relations to the rct_users table
--- because we want to keep this information even
--- if user got deleted.
--- The exit_normal field is used to detect if a 
--- client has crashed.
CREATE TABLE rct_user_logging (
       user_id		varchar(80),
       user_alias	varchar(80),
       first_name	varchar(80),
       last_name	varchar(80),
       user_on		timestamp,
       user_off		timestamp,
       user_ip		varchar(80),
       user_os		varchar(80),
       rct_version	varchar(10),
       exit_normal	bool
);

--- Creating table RCT_CONTROL
CREATE TABLE rct_control (
       class_id		 varchar(80) references rct_classes,
       assembly_id	 varchar(80) not null,
       user_id		 varchar(80) references rct_users,
       rct_module	 varchar(80) not null,
       queue_position	 integer not null
);

--- Creating table RCT_TEXTPADS
CREATE TABLE rct_textpads (
       tp_id		  varchar(80),
       tp_name		  varchar(250) not null,
       tp_text		  text,
       tp_class_id	  varchar(80) references rct_classes,
       tp_team_id	  varchar(80) references rct_teams,
       tp_group_id	  varchar(80) references rct_groups,
       tp_user_id	  varchar(80) references rct_users,
       tp_permission	  integer not null,
       rct_date		  timestamp not null,
       rct_version	  varchar(10) not null,
       primary key (tp_id, tp_class_id, tp_team_id,
		    tp_group_id, tp_user_id)
);

--- Creating table RCT_THREADED_DISCUSSION
CREATE TABLE rct_td (
       td_post_id         serial primary key,
       td_parent_id       integer not null,
       td_sender          varchar(80) references rct_users,
       td_class_id        varchar(80) references rct_classes,
       td_team_id         varchar(80) references rct_teams,
       td_subject         varchar(250) not null,
       td_text            text,
       td_type            varchar(80) not null,
       rct_date           timestamp not null,
       rct_version        varchar(10) not null
);

--- Creating table RCT_TD_READ
CREATE TABLE rct_td_read (
	post_id		integer references rct_td,
	user_id		varchar(80) references rct_users,
	primary key (post_id, user_id)
);	

--- Creating table RCT_SERVER_STATUS
CREATE TABLE rct_server_status (
       rct_date		  timestamp not null

);

-----------------------------------------------------------------
-----------------------------------------------------------------

--- Creating Views

--- RCT_TD_MSG_READ_VIEW
create view rct_td_msg_read_view as
select T.td_post_id, T.td_parent_id, T.td_sender, U.alias,
T.td_class_id, T.td_team_id, T.td_subject, T.td_type, T.rct_date, R.user_id 
from rct_users U, rct_td T 
left outer join rct_td_read R on T.td_post_id=R.post_id 
where U.user_id=T.td_sender 
group by T.td_post_id, T.td_parent_id, T.td_sender,T.td_class_id, T.td_team_id, 
T.td_subject, T.td_type, T.rct_date, R.user_id, U.alias;

--- RCT_TD_VIEW
create view rct_td_view as
select TD.td_post_id, TD.td_parent_id, U.user_id, U.alias, TD.td_class_id,
TD.td_team_id, TD.td_subject, TD.td_type, TD.rct_date
from rct_td TD, rct_users U
where TD.td_sender=U.user_id;

--- RCT_MEMBER_TEAM_VIEW
create view rct_member_team_view as
select U.user_id, U.alias, T.team_id, T.team_name, C.class_id, C.class_name 
from rct_users U, rct_teams T, rct_classes C , rct_member_team MT 
where MT.user_id=U.user_id 
and MT.team_id=T.team_id 
and T.class_id=C.class_id;

--- RCT_CONTROL_TEAM_VIEW
create view rct_control_team_view as
select CON.user_id, U.alias, CON.class_id, C.class_name, T.team_id, T.team_name, 
CON.rct_module, CON.queue_position 
from rct_users U, rct_teams T, rct_classes C, rct_control CON 
where CON.user_id=U.user_id 
and CON.class_id=C.class_id 
and T.team_id=CON.assembly_id;

--- RCT_CONTROL_GROUP_VIEW
create view rct_control_group_view as
select CON.user_id, U.alias, CON.class_id, C.class_name, G.group_id, G.group_name, 
CON.rct_module, CON.queue_position 
from rct_users U, rct_groups G, rct_classes C, rct_control CON 
where CON.user_id=U.user_id 
and CON.class_id=C.class_id 
and G.Group_id=CON.assembly_id;

--- RCT_TEAM_TEXTPADS_VIEW
create view rct_team_textpads_view as
select TP.tp_id as "ID",
       TP.tp_name as "Subject",
       TP.tp_text as "Text",
       C.class_name as "Class Name",
       T.team_name as "Team Name",
       U.alias as "User Name",
       TP.tp_permission as "Permission",
       TP.rct_date as "Date",
       TP.rct_version as "Version"
       from rct_textpads TP, rct_classes C, rct_teams T, rct_users U
       where TP.tp_class_id=C.class_id and
       TP.tp_team_id=T.team_id and
       TP.tp_user_id=U.user_id and
       TP.tp_team_id!='T0';

--- RCT_GROUP_TEXTPADS_VIEW
create view rct_group_textpads_view as
select TP.tp_id as "ID",
       TP.tp_name as "Subject",
       TP.tp_text as "Text",
       C.class_name as "Class Name",
       G.group_name as "Group Name",
       U.alias as "User Name",
       TP.tp_permission as "Permission",
       TP.rct_date as "Date",
       TP.rct_version as "Version"
       from rct_textpads TP, rct_classes C, rct_groups G, rct_users U
       where TP.tp_class_id=C.class_id and
       TP.tp_group_id=G.group_id and
       TP.tp_user_id=U.user_id and
       TP.tp_team_id='T0';

--- RCT_FILES_VIEW
create view rct_files_view as
select F.file_id,
       F.file_alias,
       F.file_name,
       F.file_location,
       F.file_source,
       F.file_mime_type,
       F.file_class_id,
       C.class_name as file_class_name,
       F.file_team_id,
       T.team_name as file_team_name,
       F.file_group_id,
       G.group_name as file_group_name,
       F.file_user_id,
       U.alias as file_user_alias,
       F.file_module,
       F.file_visible,
       F.file_permission,
       F.file_length,
       F.rct_date,
       F.rct_version
       from rct_files F, rct_classes C, rct_teams T, 
       rct_groups G, rct_users U
       where F.file_class_id=C.class_id and
       F.file_team_id=T.team_id and
       F.file_group_id=G.group_id and
       F.file_user_id=U.user_id;

--- RCT_TEAM_SOUND_VIEW
create view rct_team_sounds_view as
select file_id as "ID",
       file_alias as "Alias",
       file_name as "Name",
       file_location as "Location",
       file_source as "Source",
       file_mime_type as "Mime Type",
       file_class_name as "Class Name",
       file_team_name as "Team Name",
       file_user_alias as "User Name",
       file_permission as "Permission",
       file_length as "Size",
       rct_date as "Date",
       rct_version as "Version"
       from rct_files_view
       where file_module='sound' and
       file_team_id!='T0';

--- RCT_GROUP_SOUND_VIEW
create view rct_group_sounds_view as
select file_id as "ID",
       file_alias as "Alias",
       file_name as "Name",
       file_location as "Location",
       file_source as "Source",
       file_mime_type as "Mime Type",
       file_class_name as "Class Name",
       file_group_name as "Group Name",
       file_user_alias as "User Name",
       file_permission as "Permission",
       file_length as "Size",
       rct_date as "Date",
       rct_version as "Version"
       from rct_files_view
       where file_module='sound' and
       file_team_id='T0';

--- RCT_TEAM_FTP_VIEW
create view rct_team_ftp_view as
select file_id as "ID",
       file_alias as "Alias",
       file_name as "Name",
       file_location as "Location",
       file_source as "Source",
       file_mime_type as "Mime Type",
       file_class_name as "Class Name",
       file_team_name as "Team Name",
       file_user_alias as "User Name",
       file_permission as "Permission",
       file_length as "Size",
       rct_date as "Date",
       rct_version as "Version"
       from rct_files_view
       where file_module='ftp' and
       file_team_id!='T0';

--- RCT_GROUP_FTP_VIEW
create view rct_group_ftp_view as
select file_id as "ID",
       file_alias as "Alias",
       file_name as "Name",
       file_location as "Location",
       file_source as "Source",
       file_mime_type as "Mime Type",
       file_class_name as "Class Name",
       file_group_name as "Group Name",
       file_user_alias as "User Name",
       file_permission as "Permission",
       file_length as "Size",
       rct_date as "Date",
       rct_version as "Version"
       from rct_files_view
       where file_module='ftp' and
       file_team_id='T0';

--- RCT_CHAT_LOG_TEAMS_VIEW
create view rct_chat_log_teams_view as
select M.chat_id,
       M.chat_msg,
       U.user_id,
       U.alias,
       C.class_id,
       C.class_name,
       T.team_id,
       T.team_name,
       M.permission,
       M.rct_date,
       M.rct_version
       from rct_chat_log_teams M, rct_teams T,
       rct_classes C, rct_users U
       where M.class_id=C.class_id and
       M.team_id=T.team_id and
       M.creator=U.user_id;

--- RCT_CHAT_LOG_GROUPS_VIEW
create view rct_chat_log_groups_view as
select M.chat_id,
       M.chat_msg,
       U.user_id,
       U.alias,
       C.class_id,
       C.class_name,
       G.group_id,
       G.group_name,
       M.permission,
       M.rct_date,
       M.rct_version
       from rct_chat_log_groups M, rct_groups G,
       rct_classes C, rct_users U
       where M.class_id=C.class_id and
       M.group_id=G.group_id and
       M.creator=U.user_id;

-----------------------------------------------------------------
-----------------------------------------------------------------


--- Inserting init tuple for all the indices
--- (user, class, group, team, chat, file, textpad, course-content)
INSERT INTO rct_indices
   VALUES (13,2,1,2,0,2,0,1);

--- Insert initial date for server status
INSERT INTO rct_server_status
   VALUES('now');

--- Inserting a few users into the users table
INSERT INTO rct_users
   VALUES ('U0', 'admin', 'Admin', 'Admin', '21232f297a57a5a743894a0e4a801fc3', 2, false, 'now');

INSERT INTO rct_users
   VALUES ('U1', 'tamsler', 'Thomas', 'Amsler', 'ef6e65efc188e7dffd7335b646a85a21', 1, false, 'now');

INSERT INTO rct_users
   VALUES ('U2', 'rwalters', 'Dick', 'Walters', '7f55a0ed8b021080de00960cc73768fb', 0, false, 'now');

INSERT INTO rct_users
   VALUES ('U3', 's1', 'User1', 'User1', '8ddf878039b70767c4a5bcf4f0c4f65e', 0, false, 'now');

INSERT INTO rct_users
   VALUES ('U4', 's2', 'User2', 'User2', 'fac989447cad2edbc89fbcba70003b36', 0, false, 'now');

INSERT INTO rct_users
   VALUES ('U5', 's3', 'User3', 'User3', 'c0828e0381730befd1f7a025057c74fb', 0, false, 'now');

INSERT INTO rct_users
   VALUES ('U6', 's4', 'User4', 'User4', '7e7cb6814b74d6596098fc80127569a5', 0, false, 'now');

INSERT INTO rct_users
   VALUES ('U7', 's5', 'User5', 'User5', '437f57809f82b9e2947f70fd488eb536', 0, false, 'now');

INSERT INTO rct_users
   VALUES ('U8', 's6', 'User6', 'User6', '425db905039f7a6559ce1115efa7d397', 0, false, 'now');

INSERT INTO rct_users
   VALUES ('U9', 's7', 'User7', 'User7', '9d7115b42254d59b82440ebe8084927f', 0, false, 'now');

INSERT INTO rct_users
   VALUES ('U10', 's8', 'User8', 'User8', '3e8d88fdd85d7153525e0647cdd97686', 0, false, 'now');

INSERT INTO rct_users
   VALUES ('U11', 's9', 'User9', 'User9', '03e04301b15a7969eae9d94821cc8222', 0, false, 'now');

INSERT INTO rct_users
   VALUES ('U12', 's10', 'User10', 'User10', '2b3357d90b70edb1773bba13390166c1', 0, false, 'now');





--- Inserting a few classes into the rct_classes table
INSERT INTO rct_classes
   VALUES ('C0', 'Default Class', 0, 'U0', true, 'now');
INSERT INTO rct_classes
   VALUES ('C1', 'OpenRCT Demo', 0, 'U1', true, 'now'); 


--- Inserting the default group into the rct_groups table
INSERT INTO rct_groups
   VALUES ('G0', 'Default Group', 'C0', 0, 'U0', 'now');


--- Inserting a few teams into the rct_teams table
INSERT INTO rct_teams
   VALUES ('T0', 'Default Team', 'C0', 0, 'U0', true, 'now');
INSERT INTO rct_teams
   VALUES ('T1', 'Demo', 'C1', 0, 'U1', true, 'now');


--- Inserting a few tuples into the rct_enrolled table
INSERT INTO rct_enrolled
   VALUES ('U1', 'C1', 'now');
INSERT INTO rct_enrolled
   VALUES ('U2', 'C1', 'now');
INSERT INTO rct_enrolled
   VALUES ('U3', 'C1', 'now');
INSERT INTO rct_enrolled
   VALUES ('U4', 'C1', 'now');
INSERT INTO rct_enrolled
   VALUES ('U5', 'C1', 'now');
INSERT INTO rct_enrolled
   VALUES ('U6', 'C1', 'now');
INSERT INTO rct_enrolled
   VALUES ('U7', 'C1', 'now');
INSERT INTO rct_enrolled
   VALUES ('U8', 'C1', 'now');
INSERT INTO rct_enrolled
   VALUES ('U9', 'C1', 'now');
INSERT INTO rct_enrolled
   VALUES ('U10', 'C1', 'now');
INSERT INTO rct_enrolled
   VALUES ('U11', 'C1', 'now');
INSERT INTO rct_enrolled
   VALUES ('U12', 'C1', 'now');


--- Insert a few tuples into the rct_member_team table
INSERT INTO rct_member_team
   VALUES ('U1', 'T1');
INSERT INTO rct_member_team
   VALUES ('U2', 'T1');
INSERT INTO rct_member_team
   VALUES ('U3', 'T1');
INSERT INTO rct_member_team
   VALUES ('U4', 'T1');
INSERT INTO rct_member_team
   VALUES ('U5', 'T1');
INSERT INTO rct_member_team
   VALUES ('U6', 'T1');
INSERT INTO rct_member_team
   VALUES ('U7', 'T1');
INSERT INTO rct_member_team
   VALUES ('U8', 'T1');
INSERT INTO rct_member_team
   VALUES ('U9', 'T1');
INSERT INTO rct_member_team
   VALUES ('U10', 'T1');
INSERT INTO rct_member_team
   VALUES ('U11', 'T1');
INSERT INTO rct_member_team
   VALUES ('U12', 'T1');


--- Insert a few tuples into the rct_files table
INSERT INTO rct_files
   VALUES ('F0', 'Amsler Thomas', 'user_amsler.jpg', '/opt/rct/images', 1,
           'image/jpeg', 'C0', 'T0', 'G0', 'U1', 'user', true, 0, '0','now', 'v1.2.0');
INSERT INTO rct_files
   VALUES ('F1', 'Dick Walters', 'user_walters.jpg', '/opt/rct/images', 1,
           'image/jpeg', 'C0', 'T0', 'G0', 'U2', 'user', true, 0, '0','now', 'v1.2.0');

--- Insert a few tuples into the rct_course_content table
INSERT INTO rct_course_content
   VALUES ('CC0', 'OpenRCT', 'http://www.openrct.org', '-', 3,
           'text/html', 'C1', true, 0, '0', 'now', 'v1.2.0');

