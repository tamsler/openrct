<?php

// $Id: db_class.php,v 1.9 2003/03/31 17:40:42 thomas Exp $

// Definitions
define("CLASS_ID", 0);
define("CLASS_NAME", 1);
define("CLASS_PERMISSION", 2);
define("CLASS_MANAGER", 3);
define("CLASS_ACTIVE_STATUS", 4);
define("CLASS_DATE", 5);


class DBClass {

    // Private:
    // --------

    var $pr_id;
    var $pr_name;
    var $pr_permission;
    var $pr_manager_id;
    var $pr_manager_alias;
    var $pr_active_status;
    var $pr_date;
    var $pr_query;

    // Public:
    // -------

    // Constructor:
    function DBClass() {

	// Nothing to do here
    }

    // Method: Init with Class ID
    function init_with_class_id($a_class_id) {
    
	// Create the query string
	$this->pr_query = "select * from rct_classes
                           where class_id='$a_class_id'";

	// Create a DB object
	$db = new DB();

	// Connect to the DB
	$db->connect();

	// Get the user info
	$db_res = $db->exec($this->pr_query);

	// Test db result
	if($db->one_tuple($db_res)) {

	    // Get the row from the result
	    $row = pg_fetch_row($db_res, 0);

	    // Assigining the private class members
	    $this->pr_id = $row[CLASS_ID];
	    $this->pr_name = $row[CLASS_NAME];
	    $this->pr_permission = $row[CLASS_PERMISSION];
	    $this->pr_manager_id = $row[CLASS_MANAGER];
	    $this->pr_active_status = $row[CLASS_ACTIVE_STATUS];
	    $this->pr_date = $row[CLASS_DATE];
	}
	else {

	    disp_err_msg("ERROR: DB returned zero or more then one tuple!");
	}

	// Close DB connection
	$db->close();
	unset($db);
    }

    // Method: get Class ID
    function get_id() {

	return $this->pr_id;
    }
    
    // Method: Get Class Name
    function get_name() {

	return $this->pr_name;
    }

    // Method: Get Permssion
    function get_permission() {

	return $this->pr_permission;
    }

    // Method: Get Manager ID
    function get_manager_id() {

	return $this->pr_manager_id;
    }

    // Meethod: Get Manger Alias
    function get_manager_alias() {

	return $this->pr_manager_alias;
    }
	
    // Method: Get Active Status
    function get_active_status() {

	return $this->pr_active_status;
    }

    // Method: Get Date
    function get_date() {

	return $this->pr_date;
    }

    // Method: Set manager's alias from an id
    function set_manager_alias($a_user_id) {

	// Create the query string
	$this->pr_query = "select alias from rct_users
                           where user_id='$a_user_id'";

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
	    $this->pr_manager_alias = $row[0];
	}
	else {

	    disp_err_msg("ERROR: DB returned zero or more then one tuple!");
	}

	// Close DB connection
	$db->close();
	unset($db);
    }

    // Method: Set manager's id from an alias
    function set_manager_id($a_alias) {

	// Create the query string
	$this->pr_query = "select user_id from rct_users
                           where alias='$a_alias'";

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
	    $this->pr_manager_id = $row[0];
	}
	else {

	    disp_err_msg("ERROR: DB returned zero or more then one tuple!");
	}

	// Close DB connection
	$db->close();
	unset($db);
    }

    // Method: Get a unique class index
    function get_index($access_id = "DB_USER") {

	// Return value
	$class_index = "";
    
	// Query
	$query = "select class_index from rct_indices";
        
	// Create a DB object
	$db = new DB();

	// Connect to the DB
	$db->connect();

	// Execute query
	$db_res = $db->exec($query);

	// Test db result
	if($db->one_tuple($db_res)) {

	    // Get the row from the result
	    $row = pg_fetch_row($db_res, 0);

	    // Get the actual class index
	    $class_index = $row[0];

	    // Now we need to update/increment the class index in the DB
	    $index = $class_index + 1;

	    $query = "UPDATE rct_indices SET class_index='$index'";

	    $db_res = $db->exec($query, $access_id);

	    if(!$db_res) {

		disp_err_msg("ERROR: Was not able to update class index in DB!");
		$db->close();
		unset($db);
		// Return empty string
		return $class_index;
	    }
	    else {

		// All the class_ids start with the letter "C" followed by a number
		$class_index = "C" . $class_index;
		$db->close();
		unset($db);
		// Return class index
		return $class_index;
	    }
	}
	else {

	    disp_err_msg("ERROR: Something wrong with the rct_indices table!");
	    $db->close();
	    unset($db);
	    // Return empty string
	    return $class_index;
	}
    }

    // Method: Test if class exists in db
    // Returns true if class exists in db
    // Returns false if class does not exist in db
    function does_class_exist_in_db($class_name) {

	// Query
	$query = "select * from rct_classes where class_name='$class_name'";
    
	// Create a DB object
	$db = new DB();

	// Connect to the DB
	$db->connect();

	// Execute query
	$db_res = $db->exec($query);

	// Test db result
	// If we get zero tuples, then the class does not exist
	if($db->zero_tuple($db_res)) {
	    
	    // Close DB connection
	    $db->close();
	    unset($db);
	    return false;
	}
	else {

	    // Close DB connection
	    $db->close();
	    unset($db);
	    return true;
	}
    }
    
    // Method: Test if the manager is a user in the db
    // Return TRUE if manager is a user
    // Return FALSE if manager is not a user
    function verify_manager($a_alias) {

	// Query
	$query = "select * from rct_users where alias='$a_alias'";
    
	// Create a DB object
	$db = new DB();

	// Connect to the DB
	$db->connect();

	// Execute query
	$db_res = $db->exec($query);

	// Test db result
	if($db->one_tuple($db_res)) {

	    // Close DB connection
	    $db->close();
	    unset($db);
	    return true;
	}
	else {
	    
	    // Close DB connection
	    $db->close();
	    unset($db);
	    return false;
	}
    }


    // Method: Add Class to DB
    // Returns true on success
    // Returns false on error
    // NOTE: $manager is an user alias
    function add_class_to_db($class_name,
                             $manager,
                             $permission,
                             $active_status,
                             $access_id = "DB_USER") {


	// First, we need to get a unique class index
	$class_id = $this->get_index($access_id);

	if(!$class_id || (0 == strcmp($class_id, ""))) {

	    disp_err_msg("ERROR: Was not able to get a class index!");
	    return false;
	}

	// We need the actual user id
	$this->set_manager_id($manager);
	
	// Query
	$query = "INSERT INTO rct_classes
                  VALUES ('$class_id',
                          '$class_name',
                          '$permission',
                          '" . $this->get_manager_id() . "',
                          '$active_status',
                          'now')";
    
	// Create a DB object
	$db = new DB();

	// Connect to the DB
	$db->connect();

	// Execute transaction
	$db_res = $db->exec($query, $access_id);

	// Check for errors
	if(!$db_res) {

	    disp_err_msg("ERROR: Was not able to insert new class into DB!");
	    $db->close();
	    unset($db);
	    return false;
	}
	else {

	    disp_msg("Added class [ " . $class_name . " ] successfully to DB.");
	    $db->close();
	    unset($db);
	    return true;
	}
    }

    // Method: Generate the html permission selection code
    function select_permission($permission) {

	switch($permission) {

	case 0:
	    return "<select name=permission>
                    <option value=0 selected>UNCLASSIFIED
                    <option value=1>CLASSIFIED
                    <option value=2>SECRET
                    <option value=3>TOPSECRET
                    </select>";
	    break;

	case 1:
	    return "<select name=permission>
                    <option value=0>UNCLASSIFIED
                    <option value=1 selected>CLASSIFIED
                    <option value=2>SECRET
                    <option value=3>TOPSECRET
                    </select>";
        break;

	case 2:
	    return "<select name=permission>
                    <option value=0>UNCLASSIFIED
                    <option value=1>CLASSIFIED
                    <option value=2 selected>SECRET
                    <option value=3>TOPSECRET
                    </select>";
	    break;

	case 3:
	    return "<select name=permission>
                    <option value=0>UNCLASSIFIED
                    <option value=1>CLASSIFIED
                    <option value=2>SECRET
                    <option value=3 selected>TOPSECRET
                    </select>";
	    break;

	default:
	    return "<select name=permission>
                    <option value=0 selected>UNCLASSIFIED
                    <option value=1>CLASSIFIED
                    <option value=2>SECRET
                    <option value=3>TOPSECRET
                    </select>";
	}
    }

    // Method: Generate the html active status selection code
    function select_active_status($active_status) {
    
	switch($active_status) {

	case "true":
	case "t":
	    return "<select name=active_status>
                    <option value='true' selected>ACTIVE
                    <option value='false'>INACTIVE
                    </select>";
	    break;

	case "false":
	case "t":
	    return "<select name=active_status>
                    <option value='true'>ACTIVE
                    <option value='false' selected>INACTIVE
                    </select>";
	    break;

	default:
	    return "<select name=active_status>
                    <option value='true' selected>ACTIVE
                    <option value='false'>INACTIVE
                    </select>";
	}
    }
}

?>
