<?php

// $Id: com_add_user_verify.php,v 1.1 2002/10/01 17:34:06 thomas Exp $

require("common.php");

function gen_form($file1, $file2) {

    if(page_access_granted($_POST['access_id'], $_POST['access'])) {

	// Remove any white space from user input
	$_POST['alias'] = trim($_POST['alias']);
	$_POST['first_name'] = trim($_POST['first_name']);
	$_POST['last_name'] = trim($_POST['last_name']);
	$_POST['password1'] = trim($_POST['password1']);
	$_POST['password2'] = trim($_POST['password2']);
    
	gen_html_header("Verify Input For Add User:");

	// Create a user object
	$db_user = new DBUser();

	// Check if input strings contain single or double quotes
	if(has_quotes($_POST['alias']) ||
	   has_quotes($_POST['first_name']) ||
	   has_quotes($_POST['last_name']) ||
	   has_quotes($_POST['password1']) ||
	   has_quotes($_POST['password2'])) {

	    go_back($file1);
	}
	// Check if the mandatory variables are entered
	elseif(!$_POST['alias'] ||
	!$_POST['first_name'] ||
	!$_POST['last_name'] ||
	!$_POST['password1'] ||
	!$_POST['password2']) {

	    disp_err_msg(" Error: Login, First Name, Last Name and Password are mandatory!") ;

	    go_back($file1);
	}
	// Check if password verification matches
	elseif(0 != strcmp($_POST['password1'], $_POST['password2'])) {

	    disp_err_msg("ERROR: Password verification failed!");

	    go_back($file1);
	}
	// Check if the chosen alias is already taken
	elseif($db_user->does_user_exist_in_db($_POST['alias'])) {

	    disp_err_msg("ERROR: User already exists!");

	    go_back($file1);
	}
	else {

	    // Adding the user and test for error
	    // Password needs to be converted to 
	    if($db_user->add_user_to_db($_POST['alias'], $_POST['first_name'],
	    $_POST['last_name'], $_POST['password1'],
	    $_POST['permission'], $_POST['access_id'])) {

		add_another_user($file1);
		back_to_admin_tasks($_POST['access_id'], $_POST['access'], $file2);
	    }
	    else {

		back_to_admin_tasks($_POST['access_id'], $_POST['access'], $file2);
	    }
	}
    
	gen_menu_bar1();
	gen_html_footer();

	// Cleaning up memory
	unset($db_user);
    }
}

// Functions
// ---------

function go_back($file1) {

    printf("<form method='post' action='" . $file1 . "'>
	    <input type=hidden name=alias value='" . $_POST['alias'] . "'>
	    <input type=hidden name=first_name value='" . $_POST['first_name'] . "'>
	    <input type=hidden name=last_name value='" . $_POST['last_name'] . "'>
	    <input type=hidden name=permission value='" . $_POST['permission'] . "'>
	    <input type=hidden name=access value='" . $_POST['access'] . "'>
	    <input type=hidden name=access_id value='" . $_POST['access_id'] . "'>
	    <input type=submit value='Go Back'>
	    </form>");
}


function add_another_user($file1) {

    printf("
            <form method='post' action='" . $file1 . "'>
            <input type=hidden name='access' value='" . $_POST['access'] . "'>
            <input type=hidden name='access_id' value='" . $_POST['access_id'] . "'>
            <input type=submit value='Add Another User'>
            </form>");
}

?>
