<?php

// $Id: m_add_course_content_input.php,v 1.5 2002/10/02 18:00:56 thomas Exp $

// Query
$query = "select class_id, class_name from rct_classes where
          manager='" . $_POST['access_id'] . "'";

require("com_add_course_content_input.php");

gen_form("m_add_course_content_verify.php",
	 "manager_tasks.php",
	 $query);

?>