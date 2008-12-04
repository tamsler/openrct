<?php

// $Id: m_team_chat_msg_selection.php,v 1.2 2002/11/04 21:19:18 thomas Exp $

require("com_team_chat_msg_selection.php");

// Query
$query = "select team_id, team_name from rct_teams
          where manager='" . $_POST['access_id'] . "' order by team_name asc";
  
gen_form("m_team_chat_msg_input.php",
	 "m_team_chat_msg_selection.php",
	 "manager_tasks.php",
	 $query);

?>
	      
