<?php

// $Id: com_delete_sound_verify.php,v 1.3 2002/11/25 18:05:04 thomas Exp $

require("common.php");

function gen_form($file1) {
    
    if(page_access_granted($_POST['access_id'], $_POST['access'])) {

	gen_html_header("Verify Delete Sound Messages:");

	if($_POST['snd_id']) {

	    delete_sound_message($_POST['snd_file']);
	}

	delete_another_sound_message();

	go_to_admin_tasks($_POST['access_id'], $_POST['access'], $file1);
    
	gen_menu_bar1();
    
	gen_html_footer();
    }
}

// Functions
// ---------

// The obj_id holds either a user_id, class_id, team_id, or a group_id
function delete_another_sound_message() {

    if(TEAM == $_POST['assembly_type']) {
	printf("<form method='post' action='" . $_POST['form_name'] . "?assembly_type=" . TEAM . "'>");
    }
    elseif(GROUP == $_POST['assembly_type']) {
	printf("<form method='post' action='" . $_POST['form_name'] . "?assembly_type=" . GROUP . "'>");
    }

    printf("<input type=hidden name='obj_id' value='" . $_POST['obj_id'] . "'>
            <input type=hidden name='access' value='" . $_POST['access'] . "'>
            <input type=hidden name='access_id' value='" . $_POST['access_id'] . "'>
            <input type=submit value='Delet Another Sound Message'>
            </form>");
}

function delete_sound_message($sound_file) {

    // Query
    $query = "DELETE FROM rct_files WHERE file_id='" . $_POST['snd_id'] . "'";

    // Create a DB object
    $db = new DB();

    // Connect to the DB
    $db->connect();

    // Execute transaction
    $db_res = $db->exec($query, $_POST['access_id']);

    // Check for errors
    if(!$db_res) {

	disp_err_msg("ERROR: Was not able to delete sound message in DB!");

    }
    else {

	disp_msg("Deleted sound message successfully in DB.");

	// Now we can delete the actual file
	if(file_exists($sound_file)) {

	    unlink($sound_file);
	}
	else {

	    disp_err_msg("ERROR: Was not able to delete file since it does not exist!");
	}
    }

    // Close DB connection
    $db->close();
    unset($db);
}

?>
