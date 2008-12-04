<?php

// $Id: db_course_content.php,v 1.4 2002/10/25 22:13:08 thomas Exp $

// Definitions
define("CC_ID", 0);
define("CC_NAME", 1);
define("CC_FILE_NAME", 2);
define("CC_LOCATION", 3);
define("CC_SOURCE", 4);
define("CC_MIME_TYPE", 5);
define("CC_CLASS_ID", 6);
define("CC_VISIBLE", 7);
define("CC_PER", 8);
define("CC_DATE", 9);
define("CC_VER", 10);


class DBCourseContent {

    // Private:
    // --------

    var $pr_id;
    var $pr_name;
    var $pr_location;
    var $pr_file_name;
    var $pr_source;
    var $pr_mime_type;
    var $pr_visible;
    var $pr_class_id;
    var $pr_date;
    var $pr_per;
    var $pr_ver;
    var $pr_query;
    var $db;


    // Public:
    // -------

    // Constructor:
    function DBCourseContent() {

	// Nothing to do here
    }

    // Method: Init with Course Content ID
    function init_with_course_content_id($a_cc_id) {

	// Create the query string
	$this->pr_query = "select * from rct_files
                           where file_id='" . $a_cc_id . "'";

	// Create a DB object
	$db = new DB();

	// Connect to the DB
	$db->connect();

	// Execute query
	$db_res = $db->exec($this->pr_query);

	// Test db result
	if($db->one_tuple($db_res)) {

	    // Get the row from the result
	    $row = pg_fetch_row($db_res, 0);

	    // Assigining the private class members
	    $this->pr_id = $row[CC_ID];
	    $this->pr_name = $row[CC_NAME];
	    $this->pr_location = $row[CC_LOCATION];
	    $this->pr_file_name = $row[CC_FILE_NAME];
	    $this->pr_source = $row[CC_SOURCE];
	    $this->pr_mime_type = $row[CC_MIME_TYPE];
	    $this->pr_visible = $row[CC_VISIBLE];
	    $this->pr_class_id = $row[CC_CLASS_ID];
	    $this->pr_date = $row[CC_DATE];
	    $this->pr_per = $row[CC_PER];
	    $this->pr_ver = $row[CC_VER];
	}
	else {

	    disp_err_msg("ERROR: DB returned zero or more then one tuple!");
	}

	// Close DB connection
	$db->close();
	unset($db);
    }

    // Method: get Course Content ID
    function get_cc_id() {

	return $this->pr_id;
    }
    
    // Method: Get Name
    function get_name() {

	return $this->pr_name;
    }

    // Method: Get Location
    function get_location() {

	return $this->pr_location;
    }

    // Method: Get File Name
    function get_file_name() {

	return $this->pr_file_name;
    }

    // Method: Get Source
    function get_source() {

	return $this->pr_source;
    }

    // Method: Get Mime Type
    function get_mime_type() {

	return $this->pr_mime_type;
    }

    // Method: Get Visible
    function get_visible() {

	return $this->pr_visible;
    }

    // Method: Get Class ID
    function get_class_id() {

	return $this->pr_class_id;
    }
    
    // Method: Get Date
    function get_date() {

	return $this->pr_date;
    }

    // Method: Get Permission
    function get_per() {

	return $this->pr_per;
    }

    // Method: Get Version
    function get_ver() {

	return $this->pr_ver;
    }
}

?>
	