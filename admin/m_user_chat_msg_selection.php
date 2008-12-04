<?php

// $Id: m_user_chat_msg_selection.php,v 1.3 2002/11/04 18:20:07 thomas Exp $
require("com_user_chat_msg_selection.php");

$query = "select distinct U.user_id, U.alias from
          rct_users U, rct_classes C, rct_enrolled E,
          rct_teams T, rct_member_team M where
          U.user_id=E.user_id and
          E.class_id=C.class_id and
          U.user_id=M.user_id and
          M.team_id=T.team_id and
          (C.manager='" . $_POST['access_id'] . "' or
           T.manager='" . $_POST['access_id'] . "') order by U.alias asc";

gen_form("m_user_chat_msg_input.php",
	 "m_user_chat_msg_selection.php",
	 "manager_tasks.php",
	 $query,
	 $_GET['assembly_type']);
?>
	      
