<?php

// $Id: m_admin_user_class_enrollment_selection.php,v 1.2 2002/10/31 20:48:45 thomas Exp $

require("com_admin_user_class_enrollment_selection.php");
// Query
$query = "select distinct U.user_id, U.alias
          from rct_users U, rct_classes C, rct_enrolled E
          where U.user_id=E.user_id and
          E.class_id=C.class_id and
          C.manager='" . $_POST['access_id'] . "' order by U.alias asc";

gen_form("m_admin_user_class_enrollment_input.php",
	 "manager_tasks.php",
	 $query);

?>
	      
