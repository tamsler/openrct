<?php

// $Id: m_group_chat_msg_input.php,v 1.2 2002/11/04 21:42:08 thomas Exp $

require("com_group_chat_msg_input.php");

gen_form("m_delete_group_chat_msg_verify.php",
	 "m_group_chat_msg_input.php",
	 "m_group_chat_msg_selection.php",
	 "manager_tasks.php",
	 RCT_OFFSET);

?>
	      
