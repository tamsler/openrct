<?php

// $Id: m_user_chat_msg_input.php,v 1.3 2002/11/04 18:20:07 thomas Exp $

require("com_user_chat_msg_input.php");

gen_form("m_delete_team_chat_msg_verify.php",
	 "m_delete_group_chat_msg_verify.php",
	 "m_user_chat_msg_input.php",
	 "m_user_chat_msg_selection.php",
	 "manager_tasks.php",
	 $_GET['assembly_type'],
	 RCT_OFFSET);

?>
	      
