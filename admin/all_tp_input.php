<?php

// $Id: all_tp_input.php,v 1.2 2002/11/18 19:08:51 thomas Exp $

require("com_all_tp_input.php");

gen_form("all_tp_input.php",
	 "admin_tasks.php",
	 $_GET['assembly_type'],
	 ADMIN_ACCESS,
	 RCT_OFFSET);

?>