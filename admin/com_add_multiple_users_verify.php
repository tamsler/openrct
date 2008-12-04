<?php

// $Id: com_add_multiple_users_verify.php,v 1.1 2002/10/01 17:44:09 thomas Exp $

require("common.php");

function gen_form($file1, $file2) {

    if(page_access_granted($_POST['access_id'], $_POST['access'])) {

	gen_html_header("Verify Input Add Multiple Users:");

	// Create db user object
	$db_user = new DBUser();
    
	// Evaluate how many users were entered
	$num = count($_POST['alias']) - 1;

	// Init error flag
	// This flag is used to evaluate input errors
	// Withing the following if statements, this flag is set on error
	$error_flag = false;
    
	// Remove any white space from user input
	for($u = 0; $u < $num; $u++) {

	    $_POST['alias'][$u] = trim($_POST['alias'][$u]);
	    $_POST['first_name'][$u] = trim($_POST['first_name'][$u]);
	    $_POST['last_nam'][$u] = trim($_POST['last_name'][$u]);
	    $_POST['password'][$u] = trim($_POST['password'][$u]);

	    // Check if input strings contain single or double quotes
	    if(has_quotes($_POST['alias'][$u]) ||
	       has_quotes($_POST['first_name'][$u]) ||
	       has_quotes($_POST['last_name'][$u]) ||
	       has_quotes($_POST['password'][$u])) {

		$error_flag = true;
	    }
	
	    // While we are at it,
	    // check if all the fields have data
	    if(!$_POST['alias'][$u] ||
	    !$_POST['first_name'][$u] ||
	    !$_POST['last_name'][$u] ||
	    !$_POST['password'][$u]) {

		disp_err_msg("ERROR: You did not enter all the user data in row " . $u);
		$error_flag = true;
	    }
	}

	// Check if some input fields were missing
	if($error_flag) {

	    disp_msg("Use the Web Browser's \"Back\" button to return to the entry form.");
	    gen_menu_bar1();
	    gen_html_footer();
	    exit();
	}

	for($j = 0; $j < $num; $j++) {
	
	    // Check if Login is taken
	    if($db_user->does_user_exist_in_db($_POST['alias'][$j])) {

		disp_err_msg("ERROR: User Login in row " . $j . " is taken! Please choose another one.");
		$error_flag = true;
	    }
	}

	// Check if login was taken
	if($error_flag) {

	    disp_msg("Use the Web Browser's \"Back\" button to return to the entry form.");
	    gen_menu_bar1();
	    gen_html_footer();
	    exit();
	}


	for($x = 0; $x < $num; $x++) {

	    // Check if we have duplicated logins in the form
	    for($y = $x; $y < $num; $y++) {

		if((0 == strcmp($_POST['alias'][$x], $_POST['alias'][$y])) && ($x != $y)) {

		    disp_err_msg("ERROR: You entered a Duplicated Login at $x and $y");
		    $error_flag = true;
		}
	    }
	}

	// Check if duplicate login was entered
	if($error_flag) {

	    disp_msg("Use the Web Browser's \"Back\" button to return to the entry form.");
	    gen_menu_bar1();
	    gen_html_footer();
	    exit();
	}

	// At this point, everything is ok so we can add the users
	for($k = 0; ($k < $num) && (!$error_flag); $k++) {

	    // Add the user
	    // NOTE: We have to convert the password to a md5 hash since we were not able to so
	    // so during the input process.
	    if(!$db_user->add_user_to_db($_POST['alias'][$k],
	    $_POST['first_name'][$k],
	    $_POST['last_name'][$k],
	    md5($_POST['password'][$k]),
	    $_POST['permission'][$k],
	    $_POST['access_id'])) {

		// We detected an error
		$error_flag = true;
	    }
	}

	if($error_flag) {

	    go_to_admin_tasks($_POST['access_id'], $_POST['access'], $file2);
	}
	else {

	    add_more_users($file1);
	    go_to_admin_tasks($_POST['access_id'], $_POST['access'], $file2);
	}
    
	gen_menu_bar1();
    
	gen_html_footer();

	// Cleanup memory
	unset($db_user);
    }
}

// Functions
// ---------

function add_more_users($file1) {

    printf("
            <form method='post' action='" . $file1 . "'>
            <input type=hidden name='access' value='" . $_POST['access'] . "'>
            <input type=hidden name='access_id' value='" . $_POST['access_id'] . "'>
            <input type=submit value='Add More Users'>
            </form>");
}

?>
