<?php

// $Id: admin_user_class_enrollment_input.php,v 1.10 2002/10/31 21:37:41 thomas Exp $

require("com_admin_user_class_enrollment_input.php");

$query = "select class_id
          from rct_enrolled 
          where user_id='" . $_POST['user_id'] . "'";

gen_form("delete_user_class_enrollment_verify.php",
	 "admin_user_class_enrollment_selection.php",
	 "admin_tasks.php",
	 $query);

?>
	      
