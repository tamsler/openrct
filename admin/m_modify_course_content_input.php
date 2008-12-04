<?php

// $Id: m_modify_course_content_input.php,v 1.4 2002/10/02 21:23:03 thomas Exp $

require("com_modify_course_content_input.php");

// Query
$query = "select class_id, class_name from rct_classes
          where manager='" . $_POST['access_id'] . "'";

gen_form("m_modify_course_content_verify.php",
	 "manager_tasks.php",
	 $query);

?>

	