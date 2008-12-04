<?php

// $Id: com_add_course_content_verify.php,v 1.5 2003/03/31 21:50:10 thomas Exp $

require("common.php");

function gen_form($file1, $file2) {

    if(page_access_granted($_POST['access_id'], $_POST['access'])) {

	// Remove any white space from course content input
	$_POST['cc_name'] = trim($_POST['cc_name']);
	$_POST['cc_mime_type'] = trim($_POST['cc_mime_type']);
    
	gen_html_header("Verify Input For Add Course Content:");
    
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

	    add_course_content($file1);
	}
    
	gen_menu_bar1("help_add_course_content_verify.php");
    
	gen_html_footer();
    }
}

// Functions
// ---------

function get_cc_index() {

    // Return value
    $cc_index = "";
    
    // Query
    $query = "select cc_index from rct_indices";
        
    // Create a DB object
    $db = new DB();

    // Connect to the DB
    $db->connect();

    // Execute transaction
    $db_res = $db->exec($query);

    // Test db result
    if($db->one_tuple($db_res)) {

	// Get the row from the result
	$row = pg_fetch_row($db_res, 0);

	// Get the actual cc index
	$cc_index = $row[0];

	// Now we need to update/increment the cc index in the DB
	$index = $cc_index + 1;

	$query = "UPDATE rct_indices SET cc_index='" . $index . "'";

	$db_res = $db->exec($query, $_POST['access_id']);

	if(!$db_res) {

	    disp_err_msg("ERROR: Was not able to update cc index in DB!");
	    $db->close();
	    unset($db);
	    // Return empty string
	    return $cc_index;
	}
	else {

	    // All the cc_ids start with the "CC" followed by a number
	    $cc_index = "CC" . $cc_index;
	    $db->close();
	    unset($db);
	    // Return cc index
	    return $cc_index;
	}
    }
    else {

	disp_err_msg("ERROR: Something wrong with the rct_indices table!");
	$db->close();
	unset($db);
	// Return empty string
	return $cc_index;
    }
}

function go_back($file1) {

    printf("<form method='post' action='" . $file1 . "'>
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

function add_course_content($file1) {

    // Get the course content index
    $cc_id = get_cc_index();

    if(!$cc_id || (0 == strcmp($cc_id, ""))) {

	disp_err_msg("ERROR: Was not able to get a course content index!");
	exit();
	
    }

    // Server Source
    if(1 == $_POST['cc_source']) {

	// Query
	$query = "INSERT INTO rct_course_content
                  VALUES ('" . $cc_id . "',
                          '" . $_POST['cc_name'] . "',
                          'Default',
                          '" . RCT_SERVER_DATA_DIR . "',
                          " . $_POST['cc_source'] . ",
                          '" . $_POST['cc_mime_type'] . "',
                          '" . $_POST['cc_class_id'] . "',
                          " . $_POST['cc_visible'] . ",
                          0,
                          '0',
                          'now',
                          '" . RCT_VERSION . "')";
    }
    // Client Source
    elseif(2 == $_POST['cc_source']) {

	// Query
	$query = "INSERT INTO rct_course_content
                  VALUES ('" . $cc_id . "',
                          '" . $_POST['cc_name'] . "',
                          '" . $_POST['cc_file_name'] . "',
                          '" . $_POST['cc_location'] . "',
                          " . $_POST['cc_source'] . ",
                          '" . $_POST['cc_mime_type'] . "',
                          '" . $_POST['cc_class_id'] . "',
                          " . $_POST['cc_visible'] . ",
                          0,
                          '0',
                          'now',
                          '" . RCT_VERSION . "')";
    }	
    // Internet Source
    elseif(3 == $_POST['cc_source']) {

	// Query
	$query = "INSERT INTO rct_course_content
                  VALUES ('" . $cc_id . "',
                          '" . $_POST['cc_name'] . "',
                          '" . $_POST['cc_file_name'] . "',
                          '-',
                          " . $_POST['cc_source'] . ",
                          '" . $_POST['cc_mime_type'] . "',
                          '" . $_POST['cc_class_id'] . "',
                          " . $_POST['cc_visible'] . ",
                          0,
                          '0',
                          'now',
                          '" . RCT_VERSION . "')";
    }

    // Create a DB object
    $db = new DB();

    // Connect to the DB
    $db->connect();

    // Execute transaction
    $db_res = $db->exec($query, $_POST['access_id']);

    // Check for errors
    if(!$db_res) {

	disp_err_msg("ERROR: Was not able to insert new course content into DB!");

	go_to_admin_tasks($_POST['access_id'], $_POST['access']);
    }
    else {

	disp_simple_msg("Step 1:");
	disp_msg("Added course content [ " . $_POST['cc_name'] . " ] successfully to DB.");

	// We only show the upload link if the source is of type server
	if(1 == $_POST['cc_source']) {

	    // Added the cc to db now we need to upload it
	    disp_simple_msg("Step 2:");
	    file_upload("course-content", $cc_id);
	    echo "<br><br>";
	}

	go_to_add_course_content($file1);
 
	go_to_admin_tasks($_POST['access_id'], $_POST['access']);
    }

    // Close DB connection
    $db->close();
    unset($db);
}

function go_to_add_course_content($file1) {

    printf("
            <form method='post' action='" . $file1 . "'>
            <input type='hidden' name='access' value='" . $_POST['access'] . "'>
            <input type='hidden' name='access_id' value='" . $_POST['access_id'] . "'>
            <input type=submit value='Add Another Course Content'>
            </form>");
}

?>
