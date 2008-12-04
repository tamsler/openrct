<?php

// $Id: user_chat_msg_selection.php,v 1.10 2002/11/04 18:20:07 thomas Exp $

require("com_user_chat_msg_selection.php");

$query = "select user_id, alias from rct_users order by alias asc";

gen_form("user_chat_msg_input.php",
	 "user_chat_msg_selection.php",
	 "admin_tasks.php",
	 $query,
	 $_GET['assembly_type']);

?>
	      
