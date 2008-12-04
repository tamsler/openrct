<?php

// $Id: com_add_class_verify.php,v 1.1 2002/10/01 17:58:26 thomas Exp $

require("common.php");
require("db_class.php");

function gen_form($file1, $file2) {

    if(page_access_granted($_POST['access_id'], $_POST['access'])) {

	// Remove any white space from user input
	$_POST['class_name'] = trim($_POST['class_name']);
	$_POST['manager'] = trim($_POST['manager']);
    
	gen_html_header("Verify Input For Add Class:");

	// Create a class object
	$db_class = new DBClass();

	// Check for quotes
	if(has_quotes($_POST['class_name']) ||
	   has_quotes($_POST['manager'])) {

	    go_back($file1);
	}
	// Check if the mandatory variables are entered
	elseif (!$_POST['class_name'] || !$_POST['manager']) {

	    disp_err_msg(" Error: Class Name and Manager are mandatory!") ;

	    go_back($file1);
	}
	// Check if the chosen class name is already taken
	elseif($db_class->does_class_exist_in_db($_POST['class_name'])) {

	    disp_err_msg("ERROR: Class Name already exists!");

	    go_back($file1);
	}
	// Check if manager is an existing user
	elseif(!$db_class->verify_manager($_POST['manager'])) {
	    
	    disp_err_msg("ERROR: Manager has to be an existing user!");

	    go_back($file1);
	}
	// Everything looks good so we add the class
	else {

	    if($db_class->add_class_to_db($_POST['class_name'],
	    $_POST['manager'],
	    $_POST['permission'],
	    $_POST['active_status'],
	    $_POST['access_id'])) {

		add_another_class($file1);
	    }

	    go_to_admin_tasks($_POST['access_id'], $_POST['access'], $file2);
	}

	gen_menu_bar1();
	gen_html_footer();

	// Cleanup memory
	unset($db_class);
    }
}


// Functions
// ---------

function go_back($file1) {

    printf("<form method='post' action='" . $file1 . "'>
                <input type=hidden name=class_name value='" . $_POST['class_name'] . "'>
                <input type=hidden name=manager value='" . $_POST['manager'] . "'>
                <input type=hidden name=permission value='" . $_POST['permission'] . "'>
                <input type=hidden name=active_status value='" . $_POST['active_status'] . "'>
                <input type=hidden name=access value='" . $_POST['access'] . "'>
                <input type=hidden name=access_id value='" . $_POST['access_id'] . "'>
                <input type=submit value='Go Back'>
                </form>");
}


function add_another_class($file1) {

    printf("
            <form method='post' action='" . $file1 . "'>
            <input type=hidden name='access' value='" . $_POST['access'] . "'>
            <input type=hidden name='access_id' value='" . $_POST['access_id'] . "'>
            <input type=submit value='Add Another Class'>
            </form>");
}


?>
