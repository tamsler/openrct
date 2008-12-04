<?php

// $Id: m_class_chat_msg_selection.php,v 1.2 2002/11/04 20:50:26 thomas Exp $

require("com_class_chat_msg_selection.php");

// Query
$query = "select class_id, class_name from rct_classes
          where manager='" . $_POST['access_id'] . "' order by class_name asc";
    
gen_form("m_class_chat_msg_input.php",
	 "m_class_chat_msg_selection.php",
	 "manager_tasks.php",
	 $query,
	 $_GET['assembly_type']);

?>
	      
