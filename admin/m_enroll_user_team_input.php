<?php

// $Id: m_enroll_user_team_input.php,v 1.4 2002/10/31 18:51:47 thomas Exp $

require("com_enroll_user_team_input.php");

// Query
$query1 = "select T.team_id, T.team_name, C.class_name
          from rct_teams T, rct_classes C
          where T.class_id=C.class_id and
          (T.manager='" . $_POST['access_id'] . "' or
          C.manager='" . $_POST['access_id'] . "')";

$query2 = "select user_id, alias from rct_users where user_id in
           (select user_id from rct_enrolled where class_id in
           (select class_id from rct_classes where manager='" .  $_POST['access_id'] . "'))
           order by alias asc;";

gen_form("m_enroll_user_team_verify.php",
	 "manager_tasks.php",
	 $query1, $query2);


?>