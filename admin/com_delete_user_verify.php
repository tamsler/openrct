<?php

// $Id: com_delete_user_verify.php,v 1.4 2002/11/07 18:26:40 thomas Exp $


require("common.php");

function gen_form($file1) {
    

    if(page_access_granted($_POST['access_id'], $_POST['access'])) {

	gen_html_header("Verify Input:");

	// Modify the manager of all affected classes
	if($_POST['classes']) {

	    $classes_array = explode(":", $_POST['classes']);
	    $num_classes = count($classes_array);

	    for($i = 0, $val = current($_POST['class_user_id']);
		(($i < $num_classes) || $val);
		$i++, $val = next($_POST['class_user_id'])) {

		update_class($classes_array[$i], $val);
	    }
	}

	// Modify the manager of all affected teams
	if($_POST['teams']) {

	    $teams_array = explode(":", $_POST['teams']);
	    $num_teams = count($teams_array);

	    for($i = 0, $val = current($_POST['team_user_id']);
		(($i < $num_teams) || $val);
		$i++, $val = next($_POST['team_user_id'])) {

		update_team($teams_array[$i], $val);
	    }
	}

	// Modify the manager of all affected groups
	if($_POST['groups']) {

	    $groups_array = explode(":", $_POST['groups']);
	    $num_groups = count($groups_array);

	    for($i = 0, $val = current($_POST['group_user_id']);
		(($i < $num_groups) || $val);
		$i++, $val = next($_POST['group_user_id'])) {

		update_group($groups_array[$i], $val);
	    }
	}

	// Deleting entries in table RCT CHAT LOG GROUPS
	delete_user_from_chat_log_groups();

	// Deleting entries in table RCT CHAT LOG TEAMS
	delete_user_from_chat_log_teams();
    
	// Deleting entries in table RCT ENROLLED
	delete_user_from_enrolled();
    
	// Deleting entries in talbe RCT MEMBER TEAM
	delete_user_from_member_team();

	// Deleting entries in talbe RCT MEMBER GROUOP
	delete_user_from_member_group();
    
	// Deleting rct_files entries
	// This includes all module related files
	delete_user_files();

	// NOTE: We don't need to delete any Course Context
	// since course content does not reference to the user table

	// Deleting rct_textpads entries
	delete_user_textpads();
	
	// Deleting entries in talbe RCT USERS
	delete_user();
    
	go_to_admin_tasks($_POST['access_id'], $_POST['access'], $file1);
    
	gen_menu_bar1();
    
	gen_html_footer();
    }
}

// Functions
// ---------

function update_class($class_id, $new_manager) {

    // Query
    $query = "UPDATE rct_classes SET
              manager='$new_manager',
              rct_date='now'
              where class_id='$class_id'";

    // Create a DB object
    $db = new DB();

    // Connect to the DB
    $db->connect();

    // Execute transaction
    $db_res = $db->exec($query, $_POST['access_id']);

    // Check for errors
    if(!$db_res) {

	disp_err_msg("ERROR: Was not able to modify class in DB!");

    }
    // We only display this message if there were affected records
    elseif($db->affected_tuples($db_res)) {

	disp_msg("Updated $class_id successfully in DB.");
    }

    // Close DB connection
    $db->close();
    unset($db);
    
}

function update_team($team_id, $new_manager) {

    // Query
    $query = "UPDATE rct_teams SET
              manager='$new_manager',
              rct_date='now'
              where team_id='$team_id'";

    // Create a DB object
    $db = new DB();

    // Connect to the DB
    $db->connect();

    // Execute transaction
    $db_res = $db->exec($query, $_POST['access_id']);

    // Check for errors
    if(!$db_res) {

	disp_err_msg("ERROR: Was not able to modify team in DB!");

    }
    // We only display this message if there were affected records
    elseif($db->affected_tuples($db_res)) {

	disp_msg("Updated $team_id successfully in DB.");
    }

    // Close DB connection
    $db->close();
    unset($db);
}

function update_group($group_id, $new_manager) {

    // Query
    $query = "UPDATE rct_groups SET
              manager='$new_manager',
              rct_date='now'
              where group_id='$group_id'";

    // Create a DB object
    $db = new DB();

    // Connect to the DB
    $db->connect();

    // Execute transaction
    $db_res = $db->exec($query, $_POST['access_id']);

    // Check for errors
    if(!$db_res) {

	disp_err_msg("ERROR: Was not able to modify group in DB!");

    }
    // We only display this message if there were affected records
    elseif($db->affected_tuples($db_res)) {

	disp_msg("Updated $group_id successfully in DB.");
    }

    // Close DB connection
    $db->close();
    unset($db);
}

function delete_user_from_chat_log_groups() {

    // Query
    $query = "DELETE FROM rct_chat_log_groups WHERE creator='" . $_POST['user_id'] . "'";

    // Create a DB object
    $db = new DB();

    // Connect to the DB
    $db->connect();

    // Execute transaction
    $db_res = $db->exec($query, $_POST['access_id']);

    // Check for errors
    if(!$db_res) {

	disp_err_msg("ERROR: Was not able to delete chat log group messages in DB!");

    }
    // We only display this message if there were affected records
    elseif($db->affected_tuples($db_res)) {

	disp_msg("Deleted all chat log group messages successfully in DB.");
    }

    // Close DB connection
    $db->close();
    unset($db);
}

function delete_user_from_chat_log_teams() {

    // Query
    $query = "DELETE FROM rct_chat_log_teams WHERE creator='" . $_POST['user_id'] . "'";

    // Create a DB object
    $db = new DB();

    // Connect to the DB
    $db->connect();

    // Execute transaction
    $db_res = $db->exec($query, $_POST['access_id']);

    // Check for errors
    if(!$db_res) {

	disp_err_msg("ERROR: Was not able to delete chat log team messages in DB!");

    }
    // We only display this message if there were affected records
    elseif($db->affected_tuples($db_res)) {

	disp_msg("Deleted all chat log team messages successfully in DB.");
    }

    // Close DB connection
    $db->close();
    unset($db);
}
    
function delete_user_from_enrolled() {

    // Query
    $query = "DELETE FROM rct_enrolled WHERE user_id='" . $_POST['user_id'] . "'";

    // Create a DB object
    $db = new DB();

    // Connect to the DB
    $db->connect();

    // Execute transaction
    $db_res = $db->exec($query, $_POST['access_id']);

    // Check for errors
    if(!$db_res) {

	disp_err_msg("ERROR: Was not able to delete user class enrollments in DB!");

    }
    // We only display this message if there were affected records
    elseif($db->affected_tuples($db_res)) {

	disp_msg("Deleted all user class enrollments successfully in DB.");
    }

    // Close DB connection
    $db->close();
    unset($db);
}
    
function delete_user_from_member_team() {

    // Query
    $query = "DELETE FROM rct_member_team WHERE user_id='" . $_POST['user_id'] . "'";

    // Create a DB object
    $db = new DB();

    // Connect to the DB
    $db->connect();

    // Execute transaction
    $db_res = $db->exec($query, $_POST['access_id']);

    // Check for errors
    if(!$db_res) {

	disp_err_msg("ERROR: Was not able to delete user team membership in DB!");

    }
    // We only display this message if there were affected records
    elseif($db->affected_tuples($db_res)) {

	disp_msg("Deleted all user team membership successfully in DB.");
    }

    // Close DB connection
    $db->close();
    unset($db);
}


function delete_user_from_member_group() {

    // Query
    $query = "DELETE FROM rct_member_group WHERE user_id='" . $_POST['user_id'] . "'";

    // Create a DB object
    $db = new DB();

    // Connect to the DB
    $db->connect();

    // Execute transaction
    $db_res = $db->exec($query, $_POST['access_id']);

    // Check for errors
    if(!$db_res) {

	disp_err_msg("ERROR: Was not able to delete user group membership in DB!");

    }
    // We only display this message if there were affected records
    elseif($db->affected_tuples($db_res)) {

	disp_msg("Deleted all user group membership successfully in DB.");
    }

    // Close DB connection
    $db->close();
    unset($db);
}


function delete_user_files() {

    // Query
    $query = "DELETE FROM rct_files WHERE file_user_id='" . $_POST['user_id'] . "'";

    // Create a DB object
    $db = new DB();

    // Connect to the DB
    $db->connect();

    // Execute transaction
    $db_res = $db->exec($query, $_POST['access_id']);

    // Check for errors
    if(!$db_res) {

	disp_err_msg("ERROR: Was not able to delete user files in DB!");

    }
    // We only display this message if there were affected records
    elseif($db->affected_tuples($db_res)) {

	disp_msg("Deleted user files successfully in DB.");
    }

    // Close DB connection
    $db->close();
    unset($db);
}


function delete_user_textpads() {

    // Query
    $query = "DELETE FROM rct_textpads WHERE tp_user_id='" . $_POST['user_id'] . "'";

    // Create a DB object
    $db = new DB();

    // Connect to the DB
    $db->connect();

    // Execute transaction
    $db_res = $db->exec($query, $_POST['access_id']);

    // Check for errors
    if(!$db_res) {

	disp_err_msg("ERROR: Was not able to delete user textpads in DB!");

    }
    // We only display this message if there were affected records
    elseif($db->affected_tuples($db_res)) {

	disp_msg("Deleted user textpads successfully in DB.");
    }

    // Close DB connection
    $db->close();
    unset($db);
} 


function delete_user() {

    // Query
    $query = "DELETE FROM rct_users WHERE user_id='" . $_POST['user_id'] . "'";

    // Create a DB object
    $db = new DB();

    // Connect to the DB
    $db->connect();

    // Execute transaction
    $db_res = $db->exec($query, $_POST['access_id']);

    // Check for errors
    if(!$db_res) {

	disp_err_msg("ERROR: Was not able to delete user in DB!");

    }
    // We only display this message if there were affected records
    elseif($db->affected_tuples($db_res)) {

	disp_msg("Deleted user successfully in DB.");
    }

    // Close DB connection
    $db->close();
    unset($db);
}

?>
	      
