<?php

// $Id: db_enrolled.php,v 1.3 2002/01/08 19:59:10 thomas Exp $

// Definitions
// ER = ENROLLED
define("ER_USER_ID", 0);
define("ER_CLASS_ID", 1);
define("ER_DATE", 2);


class DBEnrolled {

    // Private:
    // --------

    var $pr_user_id;
    var $pr_class_id;
    var $pr_date;
    var $pr_query;
    var $pr_user_alias;
    var $pr_class_name;
    
    
    // Public:
    // -------

    // Constructor:
    function DBEnrolled() {

	// Nothing to do here
    }

    // Method: Init with User ID and Class ID
    function init_with_user_class_id($a_user_id, $a_class_id) {

	// Create the query string
	$this->pr_query = "select * from rct_enrolled
                           where user_id='$a_user_id'
                           and class_id='$a_class_id'";

	// Create a DB object
	$db = new DB();

	// Connect to the DB
	$db->connect();

	// Execute query
	$db_res = $db->exec($this->pr_query);

	// Test db result
	if($db->one_tuple($db_res)) {

	    // Get the row from the result
	    $row = $db->fetch_row($db_res, 0);

	    // Assigining the private team members
	    $this->pr_user_id = $row[ER_USER_ID];
	    $this->pr_class_id = $row[ER_CLASS_ID];
	    $this->pr_date = $row[ER_DATE];

	}
	else {

	    disp_err_msg("ERROR: DB returned zero or more then one tuple!");
	}

	// Close DB connection
	$db->close();
	unset($db);
    }

    // Method: Init with DB Tuple
    function init_with_db_tuple($a_tuple) {

	// Assigining the private team members
	$this->pr_user_id = $a_tuple[ER_USER_ID];
	$this->pr_class_id = $a_tuple[ER_CLASS_ID];
	$this->pr_date = $a_tuple[ER_DATE];
    }

    // Method: get User ID
    function get_user_id() {

	return $this->pr_user_id;
    }
    
    // Method: Get Class ID
    function get_class_id() {

	return $this->pr_class_id;
    }
    
    // Method: Get Date
    function get_date() {

	return $this->pr_date;
    }

    // Method: Get User's Alias
    function get_user_alias() {

	// Create the query string
	$this->pr_query = "select alias from rct_users
                           where user_id='$this->pr_user_id'";

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

	    // Get the alias
	    $this->pr_user_alias = $row[0];
	}
	else {

	    disp_err_msg("ERROR: DB returned zero or more then one tuple!");
	}

	// Close DB connection
	$db->close();
	unset($db);

	return $this->pr_user_alias;
    }


    // Method: Get Class Name from Class ID:
    function get_class_name() {

	// Create the query string
	$this->pr_query = "select class_name from rct_classes
                           where class_id='$this->pr_class_id'";

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

	    // Get the class name
	    $this->pr_class_name = $row[0];
	}
	else {

	    disp_err_msg("ERROR: DB returned zero or more then one tuple!");
	}

	// Close DB connection
	$db->close();
	unset($db);

	return $this->pr_class_name;
    }
}

?>