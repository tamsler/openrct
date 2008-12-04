<?php

// $Id: group_chat_msg_selection.php,v 1.7 2002/11/04 21:42:08 thomas Exp $

require("com_group_chat_msg_selection.php");

// Query
$query = "select group_id, group_name from rct_groups order by group_name asc";

gen_form("group_chat_msg_input.php",
	 "group_chat_msg_selection.php",
	 "admin_tasks.php",
	 $query);

?>
	      
