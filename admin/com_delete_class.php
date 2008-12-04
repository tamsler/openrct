<?php

// $Id: com_delete_class.php,v 1.5 2002/11/07 18:26:40 thomas Exp $

require("common.php");
require("db_chat_team.php");
require("db_chat_group.php");
require("db_enrolled.php");
require("db_class.php");
require("db_team.php");
require("db_group.php");
require("db_course_content.php");

function gen_form($file1) {

    if(page_access_granted($_POST['access_id'], $_POST['access'])) {

	gen_html_header("Delete Class:");

	// Adopting messages in RCT CHAT LOG GROUPS
	adopt_chat_log_groups();
    
	// Adopting messages in RCT CHAT LOG TEAMS
	adopt_chat_log_teams();
    
	// Deleting entries in table RCT ENROLLED
	delete_enrollment();
    
	// Adopting connections in RCT GROUPS
	adopt_class_group();
    
	// Adopting connections in RCT TEAMS
	adopt_class_team();

	// Deleteing course content
	delete_course_content();

	// Deleteing files
	// Deleting rct_files entries
        // This includes all module related files
	delete_files();

	// Delete textpads
	delete_textpads();
	
	// Deleting entrie in table RCT CLASSES
	delete_class();
    
	go_to_admin_tasks($_POST['access_id'], $_POST['access'], $file1);
    
	gen_menu_bar1();
    
	gen_html_footer();
    }
}

// Functions
// ---------
// Adopting messages in RCT CHAT LOG GROUPS
function adopt_chat_log_groups() {

    // Query
    $query = "UPDATE rct_chat_log_groups SET
              class_id='" . DEF_CLASS . "',
              rct_date='now'
              where class_id='" . $_POST['class_id'] . "'";
    
    // Create a DB object
    $db = new DB();

    // Connect to the DB
    $db->connect();

    // Execute transaction
    $db_res = $db->exec($query, $_POST['access_id']);

    // Check for errors
    if(!$db_res) {

	disp_err_msg("ERROR: Was not able to modify chat log groups in DB!");

    }
    // We only display this message if there were affected records
    elseif($db->affected_tuples($db_res)) {

	disp_msg("Adopted chat log groups messages successfully in DB.");
    }

    // Close DB connection
    $db->close();
    unset($db);
}
    
// Adopting messages in RCT CHAT LOG TEAMS
function adopt_chat_log_teams() {

    // Query
    $query = "UPDATE rct_chat_log_teams SET
              class_id='" . DEF_CLASS . "',
              rct_date='now'
              where class_id='" . $_POST['class_id'] . "'";
    
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


// Deleting entries in table RCT ENROLLED
function delete_enrollment() {

    // Query
    $query = "DELETE FROM rct_enrolled WHERE class_id='" . $_POST['class_id'] . "'";

    // Create a DB object
    $db = new DB();

    // Connect to the DB
    $db->connect();

    // Execute transaction
    $db_res = $db->exec($query, $_POST['access_id']);

    // Check for errors
    if(!$db_res) {

	disp_err_msg("ERROR: Was not able to delete class enrollment in DB!");

    }
    // We only display this message if there were affected records
    elseif($db->affected_tuples($db_res)) {

	disp_msg("Deleted all class enrollments successfully in DB.");
    }
    
    // Close DB connection
    $db->close();
    unset($db);
}

// Adopting connections in RCT GROUPS
function adopt_class_group() {

    // Query
    $query = "UPDATE rct_groups SET
              class_id='" . DEF_CLASS . "'
              where class_id='" . $_POST['class_id'] . "'";
    
    // Create a DB object
    $db = new DB();

    // Connect to the DB
    $db->connect();

    // Execute transactions
    $db_res = $db->exec($query, $_POST['access_id']);

    // Check for errors
    if(!$db_res) {

	disp_err_msg("ERROR: Was not able to set default class for the group in DB!");

    }
    // We only display this message if there were affected records
    elseif($db->affected_tuples($db_res)) {

	disp_msg("Adopted group by default class successfully in DB.");
    }

    // Close DB connection
    $db->close();
    unset($db);
}

// Adopting connections in RCT TEAMS
function adopt_class_team() {

    // Query
    $query = "UPDATE rct_teams SET
              class_id='" . DEF_CLASS . "' 
              where class_id='" . $_POST['class_id'] . "'";
    
    // Create a DB object
    $db = new DB();

    // Connect to the DB
    $db->connect();

    // Execute transaction
    $db_res = $db->exec($query, $_POST['access_id']);

    // Check for errors
    if(!$db_res) {

	disp_err_msg("ERROR: Was not able to set default class for the team in DB!");

    }
    // We only display this message if there were affected records
    elseif($db->affected_tuples($db_res)) {

	disp_msg("Adopted team by default class successfully in DB.");
    }

    // Close DB connection
    $db->close();
    unset($db);
}


// Deleting course content
function delete_course_content() {

    // Query
    $query = "DELETE FROM rct_course_content WHERE cc_class_id='" . $_POST['class_id'] . "'";

    // Create a DB object
    $db = new DB();

    // Connect to the DB
    $db->connect();

    // Execute transaction
    $db_res = $db->exec($query, $_POST['access_id']);

    // Check for errors
    if(!$db_res) {

	disp_err_msg("ERROR: Was not able to delete course content in DB!");

    }
    // We only display this message if there were affected records
    elseif($db->affected_tuples($db_res)) {

	disp_msg("Deleted course content successfully in DB.");
    }
    
    // Close DB connection
    $db->close();
    unset($db);
}


// Deleting files
function delete_files() {

    // Query
    $query = "DELETE FROM rct_files WHERE file_class_id='" . $_POST['class_id'] . "'";

    // Create a DB object
    $db = new DB();

    // Connect to the DB
    $db->connect();

    // Execute transaction
    $db_res = $db->exec($query, $_POST['access_id']);

    // Check for errors
    if(!$db_res) {

	disp_err_msg("ERROR: Was not able to delete files in DB!");

    }
    // We only display this message if there were affected records
    elseif($db->affected_tuples($db_res)) {

	disp_msg("Deleted files successfully in DB.");
    }
    
    // Close DB connection
    $db->close();
    unset($db);
}

// Deleting sound messages
function delete_textpads() {

    // Query
    $query = "DELETE FROM rct_textpads WHERE tp_class_id='" . $_POST['class_id'] . "'";

    // Create a DB object
    $db = new DB();

    // Connect to the DB
    $db->connect();

    // Execute transaction
    $db_res = $db->exec($query, $_POST['access_id']);

    // Check for errors
    if(!$db_res) {

	disp_err_msg("ERROR: Was not able to delete textpads in DB!");

    }
    // We only display this message if there were affected records
    elseif($db->affected_tuples($db_res)) {

	disp_msg("Deleted textpads successfully in DB.");
    }
    
    // Close DB connection
    $db->close();
    unset($db);
}


// Deleting entrie in table RCT CLASSES
function delete_class() {

    // Query
    $query = "DELETE FROM rct_classes WHERE class_id='" . $_POST['class_id'] . "'";

    // Create a DB object
    $db = new DB();

    // Connect to the DB
    $db->connect();

    // Execute transaction
    $db_res = $db->exec($query, $_POST['access_id']);

    // Check for errors
    if(!$db_res) {

	disp_err_msg("ERROR: Was not able to delete class in DB!");

    }
    // We only display this message if there were affected records
    elseif($db->affected_tuples($db_res)) {

	disp_msg("Deleted class successfully in DB.");
    }

    // Close DB connection
    $db->close();
    unset($db);
}
    

?>
