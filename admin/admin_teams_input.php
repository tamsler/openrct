<?php

// $Id: admin_teams_input.php,v 1.14 2002/11/04 22:58:38 thomas Exp $

require("com_admin_teams_input.php");

$query = "select
          team_id as \"ID\",
          team_name as \"Team Name\",
          class_id as \"Class Name\",
          permission as \"Permission\",
          manager as \"Manager\",
          active_status as \"Active Status\",
          rct_date as \"Date\"
          from rct_teams order by team_name asc";

gen_form("admin_tasks.php",
	 "delete_team.php",
	 "modify_team_input.php",
	 $query);
?>
