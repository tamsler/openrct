<?php

// $Id: all_chat_msg_input.php,v 1.12 2002/11/04 21:54:58 thomas Exp $

require("com_all_chat_msg_input.php");

$query1 = "select
           chat_id as \"ID\",
           chat_msg as \"Message\",
           creator as \"Creator\",
           class_id as \"Class Name\",
           team_id as \"Team Name\",
           permission as \"Permission\",
           rct_date as \"Date\",
           rct_version as \"Version\"
           from rct_chat_log_teams
           order by rct_date asc
           limit " . RCT_LIMIT;

$query2 = "select
           chat_id as \"ID\",
           chat_msg as \"Message\",
           creator as \"Creator\",
           class_id as \"Class Name\",
           group_id as \"Group Name\",
           permission as \"Permission\",
           rct_date as \"Date\",
           rct_version as \"Version\"
           from rct_chat_log_groups
           order by rct_date asc
           limit " . RCT_LIMIT;


gen_form("delete_team_chat_msg_verify.php",
	 "delete_group_chat_msg_verify.php",
	 "all_chat_msg_input.php",
	 "admin_tasks.php",
	 $query1,
	 $query2,
	 $_GET['assembly_type'],
	 RCT_OFFSET);

?>
	      
