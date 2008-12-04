<?php

// $Id: enroll_user_team_input.php,v 1.17 2002/10/31 18:51:47 thomas Exp $

require("com_enroll_user_team_input.php");

// Query
$query1 = "select T.team_id, T.team_name, C.class_name
          from rct_teams T, rct_classes C
          where C.class_id=T.class_id";

$query2 = "select user_id, alias from rct_users order by alias asc";

gen_form("enroll_user_team_verify.php",
	 "admin_tasks.php",
	 $query1, $query2);

?>