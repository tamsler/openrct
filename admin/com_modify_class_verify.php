<?php

// $Id: com_modify_class_verify.php,v 1.1 2002/10/02 22:40:54 thomas Exp $

require("common.php");
require("db_class.php");

function gen_form($file1, $file2) {

    if(page_access_granted($_POST['access_id'], $_POST['access'])) {

	// Remove any white space from user input
	$_POST['class_name'] = trim($_POST['class_name']);
	$_POST['manager'] = trim($_POST['manager']);

	gen_html_header("Verify Input Modify Class:");

	// Create class object
	$db_class = new DBClass();

	// Check for quotes
	if(has_quotes($_POST['class_name']) ||
	   has_quotes($_POST['manager'])) {

	    go_back($_POST['class_id'], $_POST['class_name_orig'],
	    $_POST['manager'], $_POST['permission'],
	    $_POST['active_status'], $file1);
	}
	// Check if the mandatory variables are entered
	elseif (!$_POST['class_name'] || !$_POST['manager']) {

	    disp_err_msg(" Error: Class Name, and Manager are mandatory!") ;
	
	    go_back($_POST['class_id'], $_POST['class_name_orig'],
	    $_POST['manager'], $_POST['permission'],
	    $_POST['active_status'], $file1);
	}
	// Check if class name was modified
	elseif($db_class->does_class_exist_in_db($_POST['class_name']) &&
	(0 != strcmp($_POST['class_name'], $_POST['class_name_orig']))) {

	    disp_err_msg("ERROR: Class already exists!");
	    
	    go_back($_POST['class_id'], $_POST['class_name_orig'],
	    $_POST['manager'], $_POST['permission'],
	    $_POST['active_status'], $file1);
	}
	// Check if manager is an existing user
	elseif(!$db_class->verify_manager($_POST['manager'])) {

	    disp_err_msg("ERROR: Manager has to be an existing user!");

	    // NOTE: We send the empty string for $manager because
	    // if the $manager start's with a "U", we get an error,
	    // since the input form test explicitly for a manager's user id
	    go_back($_POST['class_id'], $_POST['class_name_orig'],
	    "", $_POST['permission'], $_POST['active_status'], $file1);
	}
	else {

	    modify_class($_POST['class_id'], $_POST['class_name_orig'],
	    $_POST['class_name'], $_POST['manager'],
	    $_POST['permission'], $_POST['active_status'],
	    $_POST['access'], $_POST['access_id'], $file2);
	}

	gen_menu_bar1();
	gen_html_footer();

	// Cleanup memory
	unset($db_class);
    }
}

// Functions
// ---------


function go_back($class_id, $class_name, $manager,
                 $permission, $active_status, $file1) {

    printf("<form method='post' action='" . $file1 . "'>
            <input type=hidden name=class_id value='" . $class_id . "'>
	    <input type=hidden name=class_name value='" . $class_name . "'>
	    <input type=hidden name=manager value='" . $manager . "'>
	    <input type=hidden name=permission value=" . $permission . ">
	    <input type=hidden name=active_status value=" . $active_status . ">
	    <input type=hidden name=access value='" . $_POST['access'] . "'>
	    <input type=hidden name=access_id value='" . $_POST['access_id'] . "'>
	    <input type=submit value='Go Back'>
	    </form>");
}

function modify_class($class_id, $class_name_orig, $class_name, $manager,
                      $permission, $active_status, $access, $access_id, $file2) {

    // Get user id for the manager
    $db_class = new DBClass();
    $db_class->set_manager_id($manager);
    $manager_id = $db_class->get_manager_id();
    
    // Query
    $query = "UPDATE rct_classes SET
              class_name='" . $class_name . "',
              permission='" . $permission . "',
              manager='" . $manager_id . "',
              active_status='" . $active_status . "',
              rct_date='now'
              where class_name='" . $class_name_orig . "' and class_id='" . $class_id . "'";

    // Create a DB object
    $db = new DB();

    // Connect to the DB
    $db->connect();

    // Execute transaction
    $db_res = $db->exec($query, $access_id);

    // Check for errors
    if(!$db_res) {

	disp_err_msg("ERROR: Was not able to modify class in DB!");

    }
    else {

	disp_msg("Modified $class_name successfully to DB.");

	go_to_admin_tasks($access_id, $access, $file2);
    }

    // Close DB connection
    $db->close();
    unset($db);
    unset($db_class);
}

?>