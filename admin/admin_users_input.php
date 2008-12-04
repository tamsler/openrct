<?php

// $Id: admin_users_input.php,v 1.16 2002/11/04 22:37:44 thomas Exp $

require("com_admin_users_input.php");

// Query
$query = "select
          user_id as \"ID\",
          alias as \"Alias\",
          first_name as \"First Name\",
          last_name as \"Last Name\",
          password as \"Password\",
          permission as \"Permission\",
          online_status as \"Online Status\",
          rct_date as \"Date\"
          from rct_users order by alias asc";

gen_form("modify_user_input.php",
	 "delete_user_input.php",
	 "admin_tasks.php",
	 $query);
?>

