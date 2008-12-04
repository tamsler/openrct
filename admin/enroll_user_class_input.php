<?php

// $Id: enroll_user_class_input.php,v 1.17 2002/10/01 19:02:09 thomas Exp $

require("com_enroll_user_class_input.php");

// Query
$query = "select class_id, class_name from rct_classes";

gen_form("enroll_user_class_verify.php",
	 "admin_tasks.php",
	 $query);

?>