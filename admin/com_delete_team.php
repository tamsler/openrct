<?php

// $Id: com_delete_team.php,v 1.2 2002/09/30 22:36:30 thomas Exp $

require("common.php");


function gen_form($file1) {

    if(page_access_granted($_POST['access_id'], $_POST['access'])) {

	gen_html_header("Verify Input Delete Team:");

	// Adopting messages in RCT CHAT LOG TEAMS
	adopt_chat_log_teams();
        
	// Deleting membership in RCT MEMBER TEAM
	delete_member_team();

	// Deleting team rct_files entries
	// This includes Coursecontent, Sounds, etc.
	delete_team_files();

	// Delete team textpads
	delete_team_textpads();
	
	// Deleting entrie in table RCT TEAMS
	delete_team();
    
	go_to_admin_tasks($_POST['access_id'], $_POST['access'], $file1);
    
	gen_menu_bar1();
    
	gen_html_footer();
    }
}

// Functions
// ---------
    
// Adopting messages in RCT CHAT LOG TEAMS
function adopt_chat_log_teams() {

    // Query
    $query = "UPDATE rct_chat_log_teams SET
              team_id='" . DEF_TEAM . "',
              rct_date='now'
              where team_id='" . $_POST['team_id'] . "'";
    
    // Create a DB object
    $db = new DB();

    // Connect to the DB
    $db->connect();

    // Execute transaction
    $db_res = $db->exec($query, $_POST['access_id']);

    // Check for errors
    if(!$db_res) {

	disp_err_msg("ERROR: Was not able to modify chat log teams in DB!");

    }
    // We only display this message if there were affected records
    elseif($db->affected_tuples($db_res)) {

	disp_msg("Adopted chat log teams messages successfully in DB.");
    }

    // Close DB connection
    $db->close();
    unset($db);
}


// Deleting membership in RCT MEMBER TEAM
function delete_member_team() {

    // Query
    $query = "DELETE FROM rct_member_team WHERE team_id='" . $_POST['team_id'] . "'";

    // Create a DB object
    $db = new DB();

    // Connect to the DB
    $db->connect();

    // Execute transaction
    $db_res = $db->exec($query, $_POST['access_id']);

    // Check for errors
    if(!$db_res) {

	disp_err_msg("ERROR: Was not able to delete user team memberships in DB!");

    }
    // We only display this message if there were affected records
    elseif($db->affected_tuples($db_res)) {

	disp_msg("Deleted all the user team memberships successfully in DB.");
    }

    // Close DB connection
    $db->close();
    unset($db);
}

// Deleting team files
// This includes Coursecontent, Sounds, etc.
// Only use one method instead specific ones per module
function delete_team_files() {

    // Query
    $query = "DELETE FROM rct_files WHERE file_team_id='" . $_POST['team_id'] . "'";

    // Create a DB object
    $db = new DB();

    // Connect to the DB
    $db->connect();

    // Execute transaction
    $db_res = $db->exec($query, $_POST['access_id']);

    // Check for errors
    if(!$db_res) {

	disp_err_msg("ERROR: Was not able to delete team files in DB!");

    }
    // We only display this message if there were affected records
    elseif($db->affected_tuples($db_res))  {

	disp_msg("Deleted team files successfully in DB.");
    }

    // Close DB connection
    $db->close();
    unset($db);
    
}


// Deleting team textpads
function delete_team_textpads() {

    // Query
    $query = "DELETE FROM rct_textpads WHERE tp_team_id='" . $_POST['team_id'] . "'";

    // Create a DB object
    $db = new DB();

    // Connect to the DB
    $db->connect();

    // Execute transaction
    $db_res = $db->exec($query, $_POST['access_id']);

    // Check for errors
    if(!$db_res) {

	disp_err_msg("ERROR: Was not able to delete team textpads in DB!");

    }
    // We only display this message if there were affected records
    elseif($db->affected_tuples($db_res))  {

	disp_msg("Deleted team textpads successfully in DB.");
    }

    // Close DB connection
    $db->close();
    unset($db);
    
}


// Deleting entrie in table RCT TEAMS
function delete_team() {

    // Query
    $query = "DELETE FROM rct_teams WHERE team_id='" . $_POST['team_id'] . "'";

    // Create a DB object
    $db = new DB();

    // Connect to the DB
    $db->connect();

    // Execute transaction
    $db_res = $db->exec($query, $_POST['access_id']);

    // Check for errors
    if(!$db_res) {

	disp_err_msg("ERROR: Was not able to delete team in DB!");

    }
    // We only display this message if there were affected records
    elseif($db->affected_tuples($db_res))  {

	disp_msg("Deleted team successfully in DB.");
    }

    // Close DB connection
    $db->close();
    unset($db);
}

?>