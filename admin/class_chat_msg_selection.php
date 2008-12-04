<?php

// $Id: class_chat_msg_selection.php,v 1.7 2002/11/04 20:50:26 thomas Exp $

require("com_class_chat_msg_selection.php");

$query = "select class_id, class_name from rct_classes order by class_name asc";

gen_form("class_chat_msg_input.php",
	 "class_chat_msg_selection.php",
	 "admin_tasks.php",
	 $query,
	 $_GET['assembly_type']);

?>
	      
