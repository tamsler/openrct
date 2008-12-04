<?php

// $Id: com_modify_course_content_verify.php,v 1.2 2002/10/25 22:13:08 thomas Exp $

require("common.php");

function gen_form($file1, $file2) {

    if(page_access_granted($_POST['access_id'], $_POST['access'])) {

	// Remove any white space from course content input
	$_POST['cc_name'] = trim($_POST['cc_name']);
	$_POST['cc_mime_type'] = trim($_POST['cc_mime_type']);
    
	gen_html_header("Verify Input For Modify Course Content:");

	// Check for quotes
	if(has_quotes($_POST['cc_name']) ||
	   has_quotes($_POST['cc_file_name']) ||
	   has_quotes($_POST['cc_location']) ||
	   has_quotes($_POST['cc_mime_type'])) {

	    go_back($file1);
	}
	// Check if the mandatory variables are entered for SERVER
	elseif(1 == $_POST['cc_source'] &&
	       (!$_POST['cc_name'] ||
	       !$_POST['cc_mime_type'] ||
	       !$_POST['cc_class_id'])) {

	    disp_err_msg(" Error: Name, Mime Type and Class are mandatory!") ;

	    go_back($file1);
	}
	// Check if the mandatory variables are entered for CLIENT
	elseif(2 == $_POST['cc_source'] &&
	       (!$_POST['cc_name'] ||
	       !$_POST['cc_file_name'] ||
	       !$_POST['cc_location'] ||
	       !$_POST['cc_mime_type'] ||
	       !$_POST['cc_class_id'])) {

	    disp_err_msg(" Error: Name, File Name, Location, Mime Type and Class are mandatory!") ;

	    go_back($file1);
	}
	// Check if the mandatory variables are entered for INTERNET
	elseif(3 == $_POST['cc_source'] &&
	       (!$_POST['cc_name'] ||
	       !$_POST['cc_file_name'] ||
	       !$_POST['cc_mime_type'] ||
	       !$_POST['cc_class_id'])) {

	    disp_err_msg(" Error: Name, File Name, Mime Type and Class are mandatory!") ;

	    go_back($file1);
	}
	else {

	    modify_course_content($file2);
	}
    
	gen_menu_bar1();
    
	gen_html_footer();
    }
}

// Functions
// ---------

function go_back($file1) {

    printf("<form method='post' action='" . $file1 . "'>
	    <input type='hidden' name='cc_id' value='" . $_POST['cc_id'] . "'>
	    <input type='hidden' name='cc_name' value='" . $_POST['cc_name'] . "'>
            <input type='hidden' name='cc_file_name' value='" . $_POST['cc_file_name'] . "'>
            <input type='hidden' name='cc_location' value='" . $_POST['cc_location'] . "'>
	    <input type='hidden' name='cc_source' value='" . $_POST['cc_source'] . "'>
	    <input type='hidden' name='cc_mime_type' value='" . $_POST['cc_mime_type'] . "'>
	    <input type='hidden' name='cc_visible' value='" . $_POST['cc_visible'] . "'>
	    <input type='hidden' name='access' value='" . $_POST['access'] . "'>
	    <input type='hidden' name='access_id' value='" . $_POST['access_id'] . "'>
	    <input type='submit' value='Go Back'>
	    </form>");
}

function modify_course_content($file2) {

    // Query
    $query = "UPDATE rct_course_content SET
              cc_alias='" . $_POST['cc_name'] . "',
              cc_name='" . $_POST['cc_file_name'] . "',
              cc_location='" . $_POST['cc_location'] . "',
              cc_source=" . $_POST['cc_source'] . ",
              cc_mime_type='" . $_POST['cc_mime_type'] . "',
              cc_visible=" . $_POST['cc_visible'] . ",
              cc_class_id='" . $_POST['cc_class_id'] . "',
              rct_date='now',
              rct_version='" . RCT_VERSION . "' 
              where cc_id='" . $_POST['cc_id'] . "'";

    // Create a DB object
    $db = new DB();

    // Connect to the DB
    $db->connect();

    // Execute transaction
    $db_res = $db->exec($query, $_POST['access_id']);

    // Check for errors
    if(!$db_res) {

	disp_err_msg("ERROR: Was not able to modify course content in DB!");
    }
    else {

	disp_msg("Modified course content [ " . $_POST['cc_name'] . " ] successfully in DB.");
    }

    go_to_admin_tasks($_POST['access_id'], $_POST['access'], $file2);

    // Close DB connection
    $db->close();
    unset($db);
}


?>
	      
