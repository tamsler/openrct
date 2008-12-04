<?php

// $Id: login_verify.php,v 1.22 2002/05/17 23:00:31 thomas Exp $

require("common.php");


// First we do error checking if all the fields were entered

// Check if the login name and password have been entered
if(!$_POST['alias'] && !$_POST['password']) {

    gen_html_header("Verify " . $_POST['person_type'] . " Login:");

    disp_err_msg("ERROR: You forgot to enter the Login and the Password!");

    go_back();
    
    gen_menu_bar2();
    
    gen_html_footer();

    exit();
}

// Check if only the login name is missing
if(!$_POST['alias']) {

    gen_html_header("Verify " . $_POST['person_type'] . " Login:");

    disp_err_msg("ERROR: You forgot to enter the Login!");

    go_back();
        
    gen_menu_bar2();
    
    gen_html_footer();

    exit();
}

// Check if only the password is missing
if(!$_POST['password']) {

    gen_html_header("Verify " . $_POST['person_type'] . " Login:");

    disp_err_msg("ERROR: You forgot to enter the Password!");

    go_back();
    
    gen_menu_bar2();
    
    gen_html_footer();

    exit();
}

// At this poing the person entered
// all the required fields

// Remove any white space from user input
$_POST['alias'] = trim($_POST['alias']);
$_POST['password'] = trim($_POST['password']);

// Query
$query = "select * from rct_users where alias='" . $_POST['alias'] . "'";

// Create a DB object
$db = new DB();

// Connect to the DB
$db->connect();

// Execute query
$db_res = $db->exec($query);


// Test db result, we should have one tuple
// that matched the login/alias
if($db->one_tuple($db_res)) {

    // Get the row from the result
    $row = pg_fetch_row($db_res, 0);

    // Check Login and Password
    // Login and Password are correct
    // This part is the same for all person types, admin, manager, and user
    if((0 == strcmp($row[USER_ALIAS], $_POST['alias'])) && (0 == strcmp($row[USER_PASSWORD], $_POST['password']))) {

	// Check if person has administrator permission
	if((0 == strcmp($row[USER_PERMISSION], ADMIN)) && (0 == strcmp(ADMIN, $_POST['permission']))) {

	    grant_admin_login($row[USER_ALIAS], $row[USER_PASSWORD], $row[USER_ID]);

	    // Add info to transaction table about admin actions start
	    add_login_transaction($row[USER_ID], $row[USER_ALIAS]);
	}
	// Check if person has manager permission
	elseif((0 == strcmp($row[USER_PERMISSION], MANAGER)) && (0 == strcmp(MANAGER, $_POST['permission']))) {

	    grant_manager_login($row[USER_ALIAS], $row[USER_PASSWORD], $row[USER_ID]);

	    // Add info to transaction table about admin actions start
	    add_login_transaction($row[USER_ID], $row[USER_ALIAS]);
	}
	// Check if person has user permission
	elseif((0 == strcmp($row[USER_PERMISSION], USER)) && (0 == strcmp(USER, $_POST['permission']))) {

	    grant_user_login($row[USER_ALIAS], $row[USER_PASSWORD], $row[USER_ID]);

	    // Add info to transaction table about admin actions start
	    add_login_transaction($row[USER_ID], $row[USER_ALIAS]);
	}
	else {

	    gen_html_header("Verify " . $_POST['person_type'] . " Login:");

	    disp_err_msg("ERROR: You do not have " . $_POST['person_type'] . " rights!");

	    start_over();

	    gen_menu_bar2();

	    gen_html_footer();
	}
    }
    // Login is correct, Password is NOT correct
    elseif((0 == strcmp($row[USER_ALIAS], $_POST['alias'])) && (0 != strcmp($row[USER_PASSWORD], $_POST['password']))) {

	gen_html_header("Verify " . $_POST['person_type'] . " Login:");

	disp_err_msg("ERROR: You entered a wrong Password!");

	start_over();

	gen_menu_bar2();

	gen_html_footer();
    }
    else {

	gen_html_header("Verify " . $_POST['person_type'] . " Login:");

	disp_err_msg("ERROR: This should never happen!");

	start_over();

	gen_html_footer();
    }
    
}
else {

    // Query result didn't have one tuple
    
    gen_html_header("Verify " . $_POST['person_type'] . " Login:");

    disp_err_msg("ERROR: You entered a wrong Login!");

    start_over();

    gen_html_footer();
}

// Close DB connection
$db->close();
unset($db);


// Functions
// ---------

// Dealing with Administrator type login
function grant_admin_login($a_alias, $a_password, $a_user_id) {

    gen_html_header("Verify Login: " . $_POST['person_type'] . " access granted for [ <font color='red'>" . $a_alias . "</font> ]");

    if ($file = fopen("admin_login_info.txt", "r")) {

	printf("
                <form method='post' action='admin_tasks.php'>
                <textarea name=login_info cols=50 rows=15 wrap=soft readonly>");

	fpassthru($file);

	printf("
                </textarea><br><br><br>
                <input type=hidden name='access' value='$a_password'>
                <input type=hidden name='access_id' value='$a_user_id'>
                <input type=submit value='Proceed To Admin Tasks'>
                </form>");
    }
    else {
	
	echo("ERROR: Was not able to open login info text file!" . NL);
    }

    gen_menu_bar1();
	
    gen_html_footer();
}


// Dealing with Manger type login
function grant_manager_login($a_alias, $a_password, $a_user_id) {

    gen_html_header("Verify Login: " . $_POST['person_type'] . " access granted for [ <font color='red'>" . $a_alias . "</font> ]");

    if ($file = fopen("manager_login_info.txt", "r")) {

	printf("
                <form method='post' action='manager_tasks.php'>
                <textarea name=login_info cols=50 rows=15 wrap=soft readonly>");

	fpassthru($file);

	printf("
                </textarea><br><br><br>
                <input type=hidden name='access' value='$a_password'>
                <input type=hidden name='access_id' value='$a_user_id'>
                <input type=submit value='Proceed To Manager Tasks'>
                </form>");
    }
    else {
	
	echo("ERROR: Was not able to open login info text file!" . NL);
    }

    gen_menu_bar1();
	
    gen_html_footer();
}


// Dealing with User type login
function grant_user_login($a_alias, $a_password, $a_user_id) {

    gen_html_header("Verify Login: " . $_POST['person_type'] . " access granted for [ <font color='red'>" . $a_alias . "</font> ]");

    if ($file = fopen("user_login_info.txt", "r")) {

	printf("
                <form method='post' action='user_tasks.php'>
                <textarea name=login_info cols=50 rows=15 wrap=soft readonly>");

	fpassthru($file);

	printf("
                </textarea><br><br><br>
                <input type=hidden name='access' value='$a_password'>
                <input type=hidden name='access_id' value='$a_user_id'>
                <input type=submit value='Proceed To User Tasks'>
                </form>");
    }
    else {
	
	echo("ERROR: Was not able to open login info text file!" . NL);
    }

    gen_menu_bar1();
	
    gen_html_footer();
}


// We need to log all logins in transaction table
function add_login_transaction($a_user_id, $a_alias) {

    // Transaction message
    $message = $_POST['person_type'] . ": " . $a_alias . " with user_id [" . $a_user_id . "] logged in";
    
    // Query
    $query = "INSERT INTO rct_admin_transactions VALUES ('" . $a_user_id . "', 'now', '" . $message . "')";

    // Create a DB object
    $db = new DB();

    // Connect to the DB
    $db->connect();

    // Execute transaction
    $db_res = $db->exec($query, $a_user_id);

    // Check for error
    if(!$db_res) {

        disp_err_msg("ERROR: Could not enter transaction to DB!");

    }

    $db->close();
    unset($db);
}


// Button functions
function start_over() {

    printf("<form method='post' action='index.php'>
            <input type=submit value='Start Over'>
            </form>");
}

function go_back() {

    printf("<form method='post' action='login_input.php'>
            <input type=hidden name=person_type value='". $_POST['person_type'] . "'>
            <input type=hidden name=permission value='" . $_POST['permission'] . "'>
            <input type=hidden name=alias value='" . $_POST['alias'] . "'>
            <input type=submit value='Go Back'>
            </form>");
}


?>