<?php

// $Id: com_delete_team_chat_msg_verify.php,v 1.3 2002/11/18 19:08:51 thomas Exp $

require("common.php");

function gen_form($file1) {
    
    if(page_access_granted($_POST['access_id'], $_POST['access'])) {

	gen_html_header("Verify Delete Team Chat Message:");

	if($_POST['chat_id']) {

	    delete_chat_message();
	}

	delete_another_chat_message();

	go_to_admin_tasks($_POST['access_id'], $_POST['access'], $file1);
    
	gen_menu_bar1();
    
	gen_html_footer();
    }
}

// Functions
// ---------

// The obj_id holds either a user_id, class_id, team_id, or a group_id
function delete_another_chat_message() {

    printf("
            <form method='post' action='" . $_POST['form_name'] . "?assembly_type=" . TEAM . "'>
            <input type=hidden name='obj_id' value='" . $_POST['obj_id'] . "'>
            <input type=hidden name='access' value='" . $_POST['access'] . "'>
            <input type=hidden name='access_id' value='" . $_POST['access_id'] . "'>
            <input type=submit value='Delet Another Chat Message'>
            </form>");
}

function delete_chat_message() {

    // Query
    $query = "DELETE FROM rct_chat_log_teams WHERE chat_id='" . $_POST['chat_id'] . "'";

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
    else {

	disp_msg("Deleted team chat messages successfully in DB.");
    }

    // Close DB connection
    $db->close();
    unset($db);
}

?>
