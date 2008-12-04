<?php

// $Id: all_sound_input.php,v 1.1 2002/11/18 22:59:45 thomas Exp $

require("com_all_sound_input.php");

gen_form("all_sound_input.php",
	 "admin_tasks.php",
	 $_GET['assembly_type'],
	 ADMIN_ACCESS,
	 RCT_OFFSET);

?>