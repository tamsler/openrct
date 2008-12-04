<?php

// $Id: com_add_team_verify.php,v 1.1 2002/10/01 18:05:04 thomas Exp $

require("common.php");
require("db_team.php");

function gen_form($file1, $file2) {

    if(page_access_granted($_POST['access_id'], $_POST['access'])) {

	// Remove any white space from user input
	$_POST['team_name'] = trim($_POST['team_name']);
	$_POST['manager'] = trim($_POST['manager']);

	gen_html_header("Verify Input For Add Team:");


	// Create a team object
	$db_team = new DBTeam();

	// Check for quotes
	if(has_quotes($_POST['team_name']) ||
	   has_quotes($_POST['manager'])) {

	    go_back($file1);
	}
	// Check if the mandatory variables are entered
	elseif (!$_POST['team_name'] || !$_POST['manager'] || !$_POST['class_id']) {

	    disp_err_msg(" Error: Team Name, Manager, and Class  are mandatory!") ;

	    go_back($file1);
	}
	// Check if the chosen team name is already taken
	// for the chosen class
	elseif(does_team_exist_in_class()) {

	    disp_err_msg("ERROR: Team Name already exists!");

	    go_back($file1);
	}
	// Check if manager is an existing user
	elseif(!verify_manager()) {
	    
	    disp_err_msg("ERROR: Manager has to be an existing user!");

	    go_back($file1);
	}
	// Everything looks good so we add the team
	else {

	    if($db_team->add_team_to_db($_POST['team_name'],
	    $_POST['manager'],
	    $_POST['permission'],
	    $_POST['active_status'],
	    $_POST['class_id'],
	    $_POST['access_id'])) {

		add_another_team($file1);
	    }

	    go_to_admin_tasks($_POST['access_id'], $_POST['access'], $file2);
	}

	gen_menu_bar1();
	gen_html_footer();

	// Cleanup memory
	unset($db_team);
    }
}


// Functions
// ---------

// Returns TRUE if team exists in class
// Returns FLASE if team does not exist in class
function does_team_exist_in_class() {

    // Query
    $query = "select * from rct_teams where team_name='" . $_POST['team_name'] . "'
              and class_id='" . $_POST['class_id'] . "'";
        
    // Create a DB object
    $db = new DB();

    // Connect to the DB
    $db->connect();

    // Execute query
    $db_res = $db->exec($query);

    // Test db result
    if($db->zero_tuple($db_res)) {

	// Close DB connection
	$db->close();
	unset($db);
	return FALSE;
    }
    else {

	// Close DB connection
	$db->close();
	unset($db);
	return TRUE;
    }
}

// Return TRUE if manager is a user
// Return FALSE if manager is not a user
function verify_manager() {

    // Query
    $query = "select * from rct_users where alias='" . $_POST['manager'] . "'";
    
    // Create a DB object
    $db = new DB();

    // Connect to the DB
    $db->connect();

    // Execute query
    $db_res = $db->exec($query);

    // Test db result
    if($db->one_tuple($db_res)) {

	// Close DB connection
	$db->close();
	unset($db);
	return TRUE;
    }
    else {

	// Close DB connection
	$db->close();
	unset($db);
	return FALSE;
    }
}

function go_back($file1) {

    printf("<form method='post' action='" . $file1 . "'>
                <input type=hidden name=team_name value='" . $_POST['team_name'] . "'>
                <input type=hidden name=manager value='" . $_POST['manager'] . "'>
                <input type=hidden name=permission value='" . $_POST['permission'] . "'>
                <input type=hidden name=active_status value='" . $_POST['active_status'] . "'>
                <input type=hidden name=access value='" . $_POST['access'] . "'>
                <input type=hidden name=access_id value='" . $_POST['access_id'] . "'>
                <input type=submit value='Go Back'>
                </form>");
}

function add_another_team($file1) {

    printf("
            <form method='post' action='" . $file1 . "'>
            <input type=hidden name='access' value='" . $_POST['access'] . "'>
            <input type=hidden name='access_id' value='" . $_POST['access_id'] . "'>
            <input type=submit value='Add Another Team'>
            </form>");
}

?>