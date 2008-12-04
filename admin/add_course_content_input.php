<?php

// $Id: add_course_content_input.php,v 1.8 2002/10/02 18:00:56 thomas Exp $

// Query
$query = "select class_id, class_name from rct_classes";

require("com_add_course_content_input.php");

gen_form("add_course_content_verify.php",
	 "admin_tasks.php",
	 $query);


?>

	