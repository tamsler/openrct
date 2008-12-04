<?php

// $Id: com_delete_user_team_enrollment_verify.php,v 1.1 2002/10/31 22:13:58 thomas Exp $

require("common.php");

function gen_form($file1, $file2) {

    if(page_access_granted($_POST['access_id'], $_POST['access'])) {

	gen_html_header("Verify user team unenrollment:");

	// Check if we got a valid team and user id
	if($_POST['team_id'] && $_POST['user_id']) {

	    unenroll_user_from_team();
	}

	unenroll_user_from_another_team($file1);
    
	go_to_admin_tasks($_POST['access_id'], $_POST['access'], $file2);
    
	gen_menu_bar1();
    
	gen_html_footer();
    }
}

// Functions
// ---------
function unenroll_user_from_another_team($file) {

    printf("
            <form method='post' action='" . $file . "'>
            <input type=hidden name='user_id' value='" . $_POST['user_id'] . "'>
            <input type=hidden name='access' value='" . $_POST['access'] . "'>
            <input type=hidden name='access_id' value='" . $_POST['access_id'] . "'>
            <input type=submit value='Unenroll User From Another Team'>
            </form>");
}

function unenroll_user_from_team() {

    // Query
    $query = "DELETE FROM rct_member_team WHERE team_id='" . $_POST['team_id'] . "'
              and user_id='" . $_POST['user_id'] . "'";

    // Create a DB object
    $db = new DB();

    // Connect to the DB
    $db->connect();

    // Execute transaction
    $db_res = $db->exec($query, $_POST['access_id']);

    // Check for errors
    if(!$db_res) {

	disp_err_msg("ERROR: Was not able to unenroll user from team in DB");

    }
    else {

	disp_msg("Unenrolled user from team successfully in DB.");
    }

    // Close DB connection
    $db->close();
    unset($db);
}

?>
