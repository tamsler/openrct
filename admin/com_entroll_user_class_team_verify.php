<?php

// $Id: com_entroll_user_class_team_verify.php,v 1.1 2002/11/06 23:11:12 thomas Exp $

require("common.php");


function gen_form($file1) {

    if(page_access_granted($_POST['access_id'], $_POST['access'])) {

	gen_html_header("Enroll Multiple Users In Class And Team:");

	back_to_admin_tasks($_POST['access_id'], $_POST['access'], $file1);
	
	gen_menu_bar1();

	gen_html_footer();
    }
}

?>