<?php

// $Id: m_admin_teams_input.php,v 1.3 2002/11/04 22:58:38 thomas Exp $

require("com_admin_teams_input.php");

$query = "select distinct
          T.team_id as \"ID\",
          T.team_name as \"Team Name\",
          T.class_id as \"Class Name\",
          T.permission as \"Permission\",
          T.manager as \"Manager\",
          T.active_status as \"Active Status\",
          T.rct_date as \"Date\"
          from rct_teams T, rct_classes C
          where T.class_id=C.class_id and
          (T.manager='" . $_POST['access_id'] . "' or
          C.manager='" . $_POST['access_id'] . "') order by team_name asc";

gen_form("manager_tasks.php",
	 "m_delete_team.php",
	 "m_modify_team_input.php",
	 $query);

?>
