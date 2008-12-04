<?php

// $Id: add_team_input.php,v 1.18 2002/10/01 17:24:15 thomas Exp $

require("com_add_team_input.php");

// Query
$query = "select class_id, class_name from rct_classes";

gen_form("add_team_verify.php",
	 "admin_tasks.php",
	 $query);

?>

	
