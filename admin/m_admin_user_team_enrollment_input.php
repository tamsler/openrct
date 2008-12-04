<?php

// $Id: m_admin_user_team_enrollment_input.php,v 1.2 2002/10/31 22:08:56 thomas Exp $

require("com_admin_user_team_enrollment_input.php");

$query = "select team_id from rct_member_team where user_id='" . $_POST['user_id'] . "'";

gen_form("m_delete_user_team_enrollment_verify.php'",
	 "m_admin_user_team_enrollment_selection.php",
	 "manager_tasks.php",
	 $query);


?>
	      
