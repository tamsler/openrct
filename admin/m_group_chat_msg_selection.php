<?php

// $Id: m_group_chat_msg_selection.php,v 1.2 2002/11/04 21:42:08 thomas Exp $

require("com_group_chat_msg_selection.php");

// Query
$query = "select group_id, group_name from rct_groups
          where manager='" . $_POST['access_id'] . "' order by group_name asc";
    
gen_form("m_group_chat_msg_input.php",
	 "m_group_chat_msg_selection.php",
	 "manager_tasks.php",
	 $query);

?>
	      
