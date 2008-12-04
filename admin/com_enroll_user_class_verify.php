<?php

// $Id: com_enroll_user_class_verify.php,v 1.5 2002/11/12 17:22:40 thomas Exp $

require("common.php");
require("enroll_module.php");

function gen_form($file1, $file2) {

    if(page_access_granted($_POST['access_id'], $_POST['access'])) {

	gen_html_header("Verify Input User --> Class Enrollment:");

	// Check if user selected a user and class
	if(!$_POST['user_id'] && !$_POST['class_id']) {

	    disp_err_msg("ERROR: You didn't select a USER nor a CLASS!");

	    go_back($file1);

	    gen_menu_bar1();
    
	    gen_html_footer();

	    exit();
	}
    
	// Check if user selected a user 
	if(!$_POST['user_id']) {

	    disp_err_msg("ERROR: You didn't select a USER!");

	    go_back($file1);

	    gen_menu_bar1();
    
	    gen_html_footer();

	    exit();
	}

	// Check if user selected a class
	if(!$_POST['class_id']) {

	    disp_err_msg("ERROR: You didn't select a CLASS!");

	    go_back($file1);

	    gen_menu_bar1();
    
	    gen_html_footer();
	
	    exit();
	}

	for($val = current($_POST['user_id']); $val; $val = next($_POST['user_id'])) {

	    // Create user object
	    $db_user_sel = new DBUser();
	    $db_user_sel->init_with_user_id($val);
	    $user = $db_user_sel->get_alias();
	    unset($db_user_sel);

	    $em = new EnrollModule();
	    
	    if($em->is_user_enrolled_in_class($_POST['class_id'], $val)) {
		disp_msg($user ." was already enrolld in that class.");
	    }
	    elseif($em->enroll_user_in_class($_POST['class_id'], $val)) {
		disp_msg("Enrolled " . $user . " in class  successfully.");
	    }
	    else {
		disp_err_msg("ERROR: Was not able to insert user class enrollment into DB!");
	    }
	    
	    unset($em);
	}

	enroll_user_in_another_class($file1);
    
	go_to_admin_tasks($_POST['access_id'], $_POST['access'], $file2);
    
	gen_menu_bar1();
    
	gen_html_footer();
    }
}


// Functions
// ---------

function go_back($file1) {

    printf("<form method='post' action='" . $file1 . "'>
	    <input type=hidden name=access value='" . $_POST['access'] . "'>
	    <input type=hidden name=access_id value='" . $_POST['access_id'] . "'>
	    <input type=submit value='Go Back'>
	    </form>");
}

function enroll_user_in_another_class($file1) {

    printf("<form method='post' action='" . $file1 . "'>
	    <input type=hidden name=access value='" . $_POST['access'] . "'>
	    <input type=hidden name=access_id value='" . $_POST['access_id'] . "'>
	    <input type=submit value='Enroll User In Another Class'>
	    </form>");
}

?>
