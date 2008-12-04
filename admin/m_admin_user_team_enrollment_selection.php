<?php

// $Id: m_admin_user_team_enrollment_selection.php,v 1.2 2002/10/31 21:54:57 thomas Exp $

require("com_admin_user_team_enrollment_selection.php");

$query = "select distinct U.user_id, U.alias from
          rct_users U, rct_classes C, rct_enrolled E,
          rct_teams T, rct_member_team M where
          U.user_id=E.user_id and
          E.class_id=C.class_id and
          U.user_id=M.user_id and
          M.team_id=T.team_id and
          (C.manager='" . $_POST['access_id'] . "' or
           T.manager='" . $_POST['access_id'] . "') order by U.alias asc";

gen_form("m_admin_user_team_enrollment_input.php",
	 "manager_tasks.php",
	 $query);
?>
	      
