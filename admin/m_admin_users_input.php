<?php

// $Id: m_admin_users_input.php,v 1.3 2002/11/04 22:37:44 thomas Exp $

require("com_admin_users_input.php");

// Query
$query = "select distinct
          U.user_id as \"ID\",
          U.alias as \"Alias\",
          U.first_name as \"First Name\",
          U.last_name as \"Last Name\",
          U.password as \"Password\",
          U.permission as \"Permission\",
          U.online_status as \"Online Status\",
          U.rct_date as \"Date\"
          from rct_users U, rct_classes C, rct_enrolled E
          where U.user_id=E.user_id
          and C.class_id=E.class_id
          and C.manager='" . $_POST['access_id'] . "' order by U.alias asc";

gen_form("m_modify_user_input.php",
	 "m_delete_user_input.php",
	 "manager_tasks.php",
	 $query);

?>

