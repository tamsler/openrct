<?php

// $Id: m_enroll_user_class_input.php,v 1.2 2002/10/01 19:02:09 thomas Exp $

require("com_enroll_user_class_input.php");

// Query
$query = "select class_id, class_name from rct_classes where
          manager='" . $_POST['access_id'] . "'";

gen_form("m_enroll_user_class_verify.php",
	 "manager_tasks.php",
	 $query);

?>