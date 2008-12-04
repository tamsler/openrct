<?php

// $Id: m_admin_user_class_enrollment_input.php,v 1.3 2002/10/31 21:37:41 thomas Exp $

require("com_admin_user_class_enrollment_input.php");

$query = "select E.class_id 
          from rct_enrolled E, rct_classes C 
          where E.user_id='" . $_POST['user_id'] . "' 
          and E.class_id=C.class_id 
          and C.manager='" . $_POST['access_id'] . "'";

gen_form("m_delete_user_class_enrollment_verify.php",
	 "m_admin_user_class_enrollment_selection.php",
	 "manager_tasks.php",
	 $query);

?>
