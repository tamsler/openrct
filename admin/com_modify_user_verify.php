<?php

// $Id: com_modify_user_verify.php,v 1.1 2002/10/01 20:09:55 thomas Exp $

require("common.php");

function gen_form($file1, $file2) {

    if(page_access_granted($_POST['access_id'], $_POST['access'])) {

	// Remove any white space from user input
	$_POST['alias'] = trim($_POST['alias']);
	$_POST['first_name'] = trim($_POST['first_name']);
	$_POST['last_name'] = trim($_POST['last_name']);
	$_POST['new_password1'] = trim($_POST['new_password1']);
	$_POST['new_password2'] = trim($_POST['new_password2']);
    
	gen_html_header("Verify Input Modify User:");

	// Create user object
	$db_user = new DBUser();

	// Check for quotes
	if(has_quotes($_POST['alias']) ||
	   has_quotes($_POST['first_name']) ||
	   has_quotes($_POST['last_name']) ||
	   has_quotes($_POST['new_password1']) ||
	   has_quotes($_POST['new_password2'])) {

	    go_back($file1);
	}
	// Check if the mandatory variables are entered
	elseif (!$_POST['alias'] ||
	!$_POST['first_name'] ||
	!$_POST['last_name']) {

	    disp_err_msg(" Error: Login, First Name and Last Name are mandatory!") ;

	    go_back($file1);
	}
	// Check if password verification matches
	elseif(0 != strcmp($_POST['new_password1'], $_POST['new_password2'])) {

	    disp_err_msg("ERROR: Password verification failed!");

	    go_back($file1);
	}
	// Check if user id was modified
	elseif($db_user->does_user_exist_in_db($_POST['alias']) && (0 != strcmp($_POST['alias'], $_POST['alias_orig']))) {

	    disp_err_msg("ERROR: User already exists!");

	    go_back($file1);
	}
	else {

	    modify_user($file2);
	}
    
	gen_menu_bar1();
    
	gen_html_footer();

	// Cleanup memory
	unset($db_user);
    }
}


// Functions
// ---------

function go_back($file1) {

    printf("<form method='post' action='" . $file1 . "'>
            <input type=hidden name=user_id value='" . $_POST['user_id'] . "'>
	    <input type=hidden name=alias value='" . $_POST['alias'] . "'>
	    <input type=hidden name=first_name value='" . $_POST['first_name'] . "'>
	    <input type=hidden name=last_name value='" . $_POST['last_name'] . "'>
	    <input type=hidden name=permission value='" . $_POST['permission'] . "'>
	    <input type=hidden name=access value='" . $_POST['access'] . "'>
	    <input type=hidden name=access_id value='" . $_POST['access_id'] . "'>
	    <input type=submit value='Go Back'>
	    </form>");
}

function modify_user($file2) {

    // First we check if the a password update occured,
    // and construct accordingly the db trasaction string
    if($_POST['new_password1']) {

	// Query
	$query = "UPDATE rct_users SET
                  alias='" . $_POST['alias'] . "',
                  first_name='" . $_POST['first_name'] . "',
                  last_name='" . $_POST['last_name'] . "',
                  password='" . $_POST['new_password1'] . "',
                  permission='" . $_POST['permission'] . "',
                  online_status=false,
                  rct_date='now'
                  where alias='" . $_POST['alias_orig'] . "' and user_id='" . $_POST['user_id'] . "'";
    }
    else {

	// Query
	$query = "UPDATE rct_users SET
                  alias='" . $_POST['alias'] . "',
                  first_name='" . $_POST['first_name'] . "',
                  last_name='" . $_POST['last_name'] . "',
                  permission='" . $_POST['permission'] . "',
                  online_status=false,
                  rct_date='now'
                  where alias='" . $_POST['alias_orig'] . "' and user_id='" . $_POST['user_id'] . "'";
    }
    
    // Create a DB object
    $db = new DB();

    // Connect to the DB
    $db->connect();

    // Execute transaction
    $db_res = $db->exec($query, $_POST['access_id']);

    // Check for errors
    if(!$db_res) {

	disp_err_msg("ERROR: Was not able to modify user in DB!");

    }
    else {

	disp_msg("Modified " . $_POST['alias'] . " successfully to DB.");

	// Just in case the password has change, we reset access
	// But only if we changed the account of the current user
	if($_POST['new_password1'] && (0 == strcmp($_POST['user_id'], $_POST['access_id']))) {

	    $_POST['access'] = $_POST['new_password1'];
	}

	go_to_admin_tasks($_POST['access_id'], $_POST['access'], $file2);
    }

    // Close DB connection
    $db->close();
    unset($db);
}

?>