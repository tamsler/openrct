<?php

// $Id: m_enroll_user_class_team_input.php,v 1.2 2002/11/06 23:11:12 thomas Exp $

require("com_enroll_user_class_team_input.php");

$query1 = "select class_id, class_name from rct_classes where
           manager='" . $_POST['access_id'] . "'";

$query2 = "select T.team_id, T.team_name, C.class_name, C.class_id
          from rct_teams T, rct_classes C
          where T.class_id=C.class_id and
          (T.manager='" . $_POST['access_id'] . "' or
          C.manager='" . $_POST['access_id'] . "')";

gen_form("m_enroll_user_class_team_verify.php",
	 "manager_tasks.php",
	 $query1,
	 $query2);

?>