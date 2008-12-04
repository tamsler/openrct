<?php

// $Id: all_ftp_input.php,v 1.1 2002/11/25 17:42:11 thomas Exp $

require("com_all_ftp_input.php");

gen_form("all_ftp_input.php",
	 "admin_tasks.php",
	 $_GET['assembly_type'],
	 ADMIN_ACCESS,
	 RCT_OFFSET);

?>