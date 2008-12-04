<?php

// $Id: modify_course_content_input.php,v 1.5 2002/10/02 21:23:03 thomas Exp $

require("com_modify_course_content_input.php");

// Query
$query = "select class_id, class_name from rct_classes";


gen_form("modify_course_content_verify.php",
	 "admin_tasks.php",
	 $query);

?>

	