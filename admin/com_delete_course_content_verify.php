<?php

// $Id: com_delete_course_content_verify.php,v 1.5 2002/11/25 18:05:04 thomas Exp $

require("common.php");

function gen_form($file1, $file2) {

    if(page_access_granted($_POST['access_id'], $_POST['access'])) {

	gen_html_header("Verify Delete Course Content:");

	// Delete the course content
	if($_POST['cc_id']) {

	    delete_course_content();
	}
	else {

	    disp_err_msg("ERROR: Received Invalid Course Content ID!");
	}

	delete_another_course_content($file1);

	go_to_admin_tasks($_POST['access_id'], $_POST['access'], $file2);
    
	gen_menu_bar1();
    
	gen_html_footer();
    }
} 


// Functions
// ---------

function delete_another_course_content($file1) {

    printf("
            <form method='post' action='" . $file1 . "'>
            <input type=hidden name='access' value='" . $_POST['access'] . "'>
            <input type=hidden name='access_id' value='" . $_POST['access_id'] . "'>
            <input type=submit value='Delet Another Course Content'>
            </form>");
}

function delete_course_content() {

    // Query
    $query = "DELETE FROM rct_course_content WHERE cc_id='" . $_POST['cc_id'] . "'";

    // Create a DB object
    $db = new DB();

    // Connect to the DB
    $db->connect();

    // Execute transaction
    $db_res = $db->exec($query, $_POST['access_id']);

    // Check for errors
    if(!$db_res) {

	disp_err_msg("ERROR: Was not able to delete course contentin DB!");
    }
    else {

	disp_msg("Deleted Course Content successfully in DB.");

	// Compose the course content file name, which is of the form:
	// cc_CCX where X is any integer from 0 to N
	
	$cc_file = RCT_SERVER_DATA_DIR . "/" . MODULE_PREFIX_CC . $_POST['cc_id'];
	
	// Now we can delete the actual file
	if(file_exists($cc_file)) {

	    unlink($cc_file);
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
	      
