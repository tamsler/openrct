<?php

// $Id: m_class_chat_msg_input.php,v 1.2 2002/11/04 20:50:26 thomas Exp $

require("com_class_chat_msg_input.php");

gen_form("m_delete_team_chat_msg_verify.php",
	 "m_delete_group_chat_msg_verify.php",
	 "m_class_chat_msg_input.php",
	 "m_class_chat_msg_selection.php",
	 "manager_tasks.php",
	 $_GET['assembly_type'],
	 RCT_OFFSET);

?>
	      
