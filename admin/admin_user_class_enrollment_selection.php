<?php

// $Id: admin_user_class_enrollment_selection.php,v 1.7 2002/10/31 20:48:45 thomas Exp $

require("com_admin_user_class_enrollment_selection.php");

// Query
$query = "select user_id, alias from rct_users order by alias asc";

gen_form("admin_user_class_enrollment_input.php",
	 "admin_tasks.php",
	 $query);

?>
	      
