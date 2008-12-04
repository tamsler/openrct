<?php

// $Id: admin_user_team_enrollment_selection.php,v 1.8 2002/10/31 21:54:57 thomas Exp $

require("com_admin_user_team_enrollment_selection.php");

$query = "select user_id, alias from rct_users order by alias asc";

gen_form("admin_user_team_enrollment_input.php",
	 "admin_tasks.php",
	 $query);

?>
	      
