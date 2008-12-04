<?php

// $Id: m_add_team_input.php,v 1.2 2002/10/01 17:24:15 thomas Exp $

require("com_add_team_input.php");

// Query
$query = "select class_id, class_name from rct_classes
          where manager='" . $_POST['access_id'] . "'";

gen_form("m_add_team_verify.php",
	 "manager_tasks.php",
	 $query);

?>

	
