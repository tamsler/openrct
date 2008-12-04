<?php

// $Id: team_chat_msg_selection.php,v 1.7 2002/11/04 21:19:18 thomas Exp $

require("com_team_chat_msg_selection.php");

// Query
$query = "select team_id, team_name from rct_teams order by team_name asc";

gen_form("team_chat_msg_input.php",
	 "team_chat_msg_selection.php",
	 "admin_tasks.php",
	 $query);

?>
	      
