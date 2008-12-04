<?php

// $Id: u_modify_user_verify.php,v 1.5 2002/09/10 20:01:05 thomas Exp $

require("common.php");

if(page_access_granted($_POST['access_id'], $_POST['access'])) {

    // Remove any white space from user input
    $_POST['first_name'] = trim($_POST['first_name']);
    $_POST['last_name'] = trim($_POST['last_name']);
    $_POST['new_password1'] = trim($_POST['new_password1']);
    $_POST['new_password2'] = trim($_POST['new_password2']);

    gen_html_header("Verify Input Modify My User Profile:");

    // Check for quotes
    if(has_quotes($_POST['first_name']) ||
       has_quotes($_POST['last_name']) ||
       has_quotes($_POST['new_password1']) ||
       has_quotes($_POST['new_password2'])) {

	go_back();
    }
    // Check if the mandatory variables are entered
    elseif(!$_POST['first_name'] || !$_POST['last_name']) {

	disp_err_msg(" Error: First Name and Last Name are mandatory!") ;
	go_back();
    }
    // Check if password verification matches
    elseif(0 != strcmp($_POST['new_password1'], $_POST['new_password2'])) {

	disp_err_msg("ERROR: Password verification failed!");

	go_back();
    }
    else {

	modify_user();
    }
    
    gen_menu_bar1();
    
    gen_html_footer();
}


// Functions
// ---------

function go_back() {

    printf("<form method='post' action='u_modify_user_input.php'>
            <input type=hidden name=user_id value='" . $_POST['user_id'] . "'>
	    <input type=hidden name=first_name value='" . $_POST['first_name'] . "'>
	    <input type=hidden name=last_name value='" . $_POST['last_name'] . "'>
	    <input type=hidden name=access value='" . $_POST['access'] . "'>
	    <input type=hidden name=access_id value='" . $_POST['access_id'] . "'>
	    <input type=submit value='Go Back'>
	    </form>");
}

function modify_user() {

    // First we check if the a password update occured,
    // and construct accordingly the db trasaction string
    if($_POST['new_password1']) {

	// Query
	$query = "UPDATE rct_users SET
                  first_name='" . $_POST['first_name'] . "',
                  last_name='" . $_POST['last_name'] . "',
                  password='" . $_POST['new_password1'] . "',
                  rct_date='now'
                  where user_id='" . $_POST['user_id'] . "'";
	
    }
    else {
	
	// Query
	$query = "UPDATE rct_users SET
                  first_name='" . $_POST['first_name'] . "',
                  last_name='" . $_POST['last_name'] . "',
                  rct_date='now'
                  where user_id='" . $_POST['user_id'] . "'";
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

	disp_msg("Modified $alias successfully to DB.");

	// Just in case the password has change, we reset access
	if($_POST['new_password1']) {

	    $_POST['access'] = $_POST['new_password1'];
	}
	
	back_to_admin_tasks($_POST['access_id'], $_POST['access'], USER_TASKS);
    }

    // Close DB connection
    $db->close();
    unset($db);
}

?>
	      
