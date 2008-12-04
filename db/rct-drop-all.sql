--- $Id: rct-drop-all.sql,v 1.5 2003/05/22 20:08:22 thomas Exp $


--- Drop Views
DROP VIEW rct_td_msg_read_view;
DROP VIEW rct_td_view;
DROP VIEW rct_member_team_view;
DROP VIEW rct_control_team_view;
DROP VIEW rct_control_group_view;
DROP VIEW rct_team_textpads_view;
DROP VIEW rct_group_textpads_view;
DROP VIEW rct_team_sounds_view;
DROP VIEW rct_group_sounds_view;
DROP VIEW rct_chat_log_teams_view;
DROP VIEW rct_chat_log_groups_view;
DROP VIEW rct_team_ftp_view;
DROP VIEW rct_group_ftp_view;
DROP VIEW rct_files_view;

--- Drop tables first
DROP TABLE rct_users CASCADE;
DROP TABLE rct_classes CASCADE;
DROP TABLE rct_groups CASCADE;
DROP TABLE rct_teams CASCADE;
DROP TABLE rct_enrolled CASCADE;
DROP TABLE rct_indices;
DROP TABLE rct_chat_log_teams CASCADE;
DROP TABLE rct_chat_log_groups CASCADE;
DROP TABLE rct_member_team CASCADE;
DROP TABLE rct_member_group CASCADE;
DROP TABLE rct_active_user_team CASCADE;
DROP TABLE rct_admin_transactions;
DROP TABLE rct_files CASCADE;
DROP TABLE rct_course_content CASCADE;
DROP TABLE rct_user_logging;
DROP TABLE rct_control CASCADE;
DROP TABLE rct_textpads CASCADE;
DROP TABLE rct_td_read CASCADE;
DROP TABLE rct_td CASCADE;
DROP TABLE rct_server_status;

