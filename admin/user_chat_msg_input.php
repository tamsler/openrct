<?php

// $Id: user_chat_msg_input.php,v 1.9 2002/11/04 18:20:07 thomas Exp $

require("com_user_chat_msg_input.php");

gen_form("delete_team_chat_msg_verify.php",
	 "delete_group_chat_msg_verify.php",
	 "user_chat_msg_input.php",
	 "user_chat_msg_selection.php",
	 "admin_tasks.php",
	 $_GET['assembly_type'],
	 RCT_OFFSET);

?>
	      
