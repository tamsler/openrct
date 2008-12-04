<?php

// $Id: m_all_chat_msg_input.php,v 1.6 2002/11/04 21:54:58 thomas Exp $

require("com_all_chat_msg_input.php");

$query1 = "select
           CT.chat_id as \"ID\",
           CT.chat_msg as \"Message\",
           CT.creator as \"Creator\",
           CT.class_id as \"Class Name\",
           CT.team_id as \"Team Name\",
           CT.permission as \"Permission\",
           CT.rct_date as \"Date\",
           CT.rct_version as \"Version\"
           from rct_chat_log_teams CT, rct_classes C where
           CT.class_id=C.class_id and C.manager='" . $_POST['access_id'] . "'   
           order by CT.rct_date asc
           limit " . RCT_LIMIT;

$query2 = "select
           CG.chat_id as \"ID\",
           CG.chat_msg as \"Message\",
           CG.creator as \"Creator\",
           CG.class_id as \"Class Name\",
           CG.group_id as \"Group Name\",
           CG.permission as \"Permission\",
           CG.rct_date as \"Date\",
           CG.rct_version as \"Version\"
           from rct_chat_log_groups CG, rct_classes C where
           CG.class_id=C.class_id and C.manager='" . $_POST['access_id'] . "'  
           order by CG.rct_date asc 
           limit " . RCT_LIMIT;

gen_form("m_delete_team_chat_msg_verify.php",
	 "m_delete_group_chat_msg_verify.php",
	 "m_all_chat_msg_input.php",
	 "manager_tasks.php",
	 $query1,
	 $query2,
	 $_GET['assembly_type'],
	 RCT_OFFSET);

?>
	      
