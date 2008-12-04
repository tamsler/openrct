<?php

// $Id: com_delete_tp_verify.php,v 1.2 2002/11/18 22:59:45 thomas Exp $

require("common.php");

function gen_form($file1) {
    
    if(page_access_granted($_POST['access_id'], $_POST['access'])) {

	gen_html_header("Verify Delete Textpad:");

	if($_POST['tp_id']) {

	    delete_textpad();
	}

	delete_another_textpad();

	go_to_admin_tasks($_POST['access_id'], $_POST['access'], $file1);
    
	gen_menu_bar1();
    
	gen_html_footer();
    }
}

// Functions
// ---------

// The obj_id holds either a user_id, class_id, team_id, or a group_id
function delete_another_textpad() {

    if(TEAM == $_POST['assembly_type']) {
	printf("<form method='post' action='" . $_POST['form_name'] . "?assembly_type=" . TEAM . "'>");
    }
    elseif(GROUP == $_POST['assembly_type']) {
	printf("<form method='post' action='" . $_POST['form_name'] . "?assembly_type=" . GROUP . "'>");
    }

    printf("<input type=hidden name='obj_id' value='" . $_POST['obj_id'] . "'>
            <input type=hidden name='access' value='" . $_POST['access'] . "'>
            <input type=hidden name='access_id' value='" . $_POST['access_id'] . "'>
            <input type=submit value='Delet Another Textpad'>
            </form>");
}

function delete_textpad() {

    // Query
    $query = "DELETE FROM rct_textpads WHERE tp_id='" . $_POST['tp_id'] . "'";

    // Create a DB object
    $db = new DB();

    // Connect to the DB
    $db->connect();

    // Execute transaction
    $db_res = $db->exec($query, $_POST['access_id']);

    // Check for errors
    if(!$db_res) {

	disp_err_msg("ERROR: Was not able to delete textpad in DB!");

    }
    else {

	disp_msg("Deleted textpad successfully in DB.");
    }

    // Close DB connection
    $db->close();
    unset($db);
}

?>
