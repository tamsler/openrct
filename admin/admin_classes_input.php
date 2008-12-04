<?php

// $Id: admin_classes_input.php,v 1.17 2002/11/04 22:58:38 thomas Exp $

require("com_admin_classes_input.php");

// Query
$query = "select
          class_id as \"ID\",
          class_name as \"Class Name\",
          permission as \"Permission\",
          manager as \"Manager\",
          active_status as \"Active Status\",
          rct_date as \"Date\"
          from rct_classes order by class_name asc";

gen_form("modify_class_input.php",
	 "delete_class.php",
	 "admin_tasks.php",
	 $query);
	 


?>