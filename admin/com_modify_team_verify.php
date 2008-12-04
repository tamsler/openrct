<?php

// $Id: com_modify_team_verify.php,v 1.1 2002/10/02 23:00:06 thomas Exp $

require("common.php");
require("db_team.php");

function gen_form($file1, $file2) {

    if(page_access_granted($_POST['access_id'], $_POST['access'])) {

	// Remove any white space from user input
	$_POST['team_name'] = trim($_POST['team_name']);
	$_POST['manager'] = trim($_POST['manager']);

	gen_html_header("Verify Input Modify Team:");

	// Create team object
	$db_team = new DBTeam();

	// Check for quotes
	if(has_quotes($_POST['team_name']) ||
	   has_quotes($_POST['manager'])) {

	    go_back($_POST['team_id'], $_POST['team_name_orig'],
	    $_POST['manager'], $_POST['permission'],
	    $_POST['active_status'], $file1);
	}
	// Check if the mandatory variables are entered
	elseif (!$_POST['team_name'] || !$_POST['manager']) {

	    disp_err_msg(" Error: Team Name, and Manager are mandatory!") ;
	
	    go_back($_POST['team_id'], $_POST['team_name_orig'],
	    $_POST['manager'], $_POST['permission'],
	    $_POST['active_status'], $file1);
	}
	// Check if team name was modified
	elseif($db_team->does_team_exist_in_db($_POST['team_name']) &&
	(0 != strcmp($_POST['team_name'], $_POST['team_name_orig']))) {

	    disp_err_msg("ERROR: Team already exists!");
	    
	    go_back($_POST['team_id'], $_POST['team_name_orig'],
	    $_POST['manager'], $_POST['permission'],
	    $_POST['active_status'], $file1);
	}
	// Check if manager is an existing user
	elseif(!$db_team->verify_manager($_POST['manager'])) {

	    disp_err_msg("ERROR: Manager has to be an existing user!");

	    // NOTE: We send the empty string for $manager because
	    // if the $manager start's with a "U", we get an error,
	    // since the input form test explicitly for a manager's user id
	    go_back($_POST['team_id'], $_POST['team_name_orig'],
	    "", $_POST['permission'], $_POST['active_status'], $file1);
	}
	else {

	    modify_team($_POST['team_id'], $_POST['team_name_orig'],
	    $_POST['team_name'], $_POST['manager'],
	    $_POST['permission'], $_POST['active_status'],
	    $_POST['access'], $_POST['access_id'], $file2);
	}

	gen_menu_bar1();
	gen_html_footer();

	// Cleanup memory
	unset($db_team);
    }
}

// Functions
// ---------

function go_back($team_id, $team_name, $manager, $permission, $active_status, $file1) {

    printf("<form method='post' action='" . $file1 . "'>
            <input type=hidden name=team_id value='" . $team_id . "'>
	    <input type=hidden name=team_name value='" . $team_name . "'>
	    <input type=hidden name=manager value='" . $manager . "'>
	    <input type=hidden name=permission value=" . $permission . ">
	    <input type=hidden name=active_status value=" . $active_status . ">
	    <input type=hidden name=access value='" . $_POST['access'] . "'>
	    <input type=hidden name=access_id value='" . $_POST['access_id'] . "'>
	    <input type=submit value='Go Back'>
	    </form>");
}

function modify_team($team_id, $team_name_orig, $team_name, $manager,
                     $permission, $active_status, $access, $access_id, $file2) {

    // Get user id for the manager
    $db_team = new DBTeam();
    $db_team->set_manager_id($manager);
    $manager_id = $db_team->get_manager_id();
    
    // Query
    $query = "UPDATE rct_teams SET
              team_name='" . $team_name . "',
              permission='" . $permission . "',
              manager='" . $manager_id . "',
              active_status='" . $active_status . "',
              rct_date='now'
              where team_name='$team_name_orig' and team_id='$team_id'";

    // Create a DB object
    $db = new DB();

    // Connect to the DB
    $db->connect();

    // Execute transaction
    $db_res = $db->exec($query, $access_id);

    // Check for errors
    if(!$db_res) {

	disp_err_msg("ERROR: Was not able to modify team in DB!");

    }
    else {

	disp_msg("Modified $team_name successfully to DB.");

	go_to_admin_tasks($access_id, $access, $file2);
    }

    // Close DB connection
    $db->close();
    unset($db);
    unset($db_team);
}

?>
	      
