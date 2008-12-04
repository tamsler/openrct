<?php

// $Id: db_group.php,v 1.6 2002/01/25 20:30:05 thomas Exp $

// Definitions
define("GROUP_ID", 0);
define("GROUP_NAME", 1);
define("GROUP_CLASS_ID", 2);
define("GROUP_PERMISSION", 3);
define("GROUP_MANAGER", 4);
define("GROUP_DATE", 5);


class DBGroup {

    // Private:
    // --------

    var $pr_id;
    var $pr_name;
    var $pr_class_id;
    var $pr_class_name;
    var $pr_permission;
    var $pr_manager_id;
    var $pr_manager_alias;
    var $pr_date;
    var $pr_query;



    // Public:
    // -------

    // Constructor:
    function DBGroup() {

	// Nothing to do here
    }

    // Method:  Init with Group ID
    function init_with_group_id($a_group_id) {
	
	// Create the query string
	$this->pr_query = "select * from rct_groups
                           where group_id='$a_group_id'";

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

	    // Assigining the private group members
	    $this->pr_id = $row[GROUP_ID];
	    $this->pr_name = $row[GROUP_NAME];
	    $this->pr_class_id = $row[GROUP_CLASS_ID];
	    $this->pr_permission = $row[GROUP_PERMISSION];
	    $this->pr_manager_id = $row[GROUP_MANAGER];
	    $this->pr_date = $row[GROUP_DATE];
	}
	else {

	    disp_err_msg("ERROR: DB returned zero or more then one tuple!");
	}

	// Close DB connection
	$db->close();
	unset($db);
    }

    // Method: get Group ID
    function get_id() {

	return $this->pr_id;
    }
    
    // Method: Get Group Name
    function get_name() {

	return $this->pr_name;
    }

    // Method: Get Group Class ID
    function get_class_id() {

	return $this->pr_class_id;
    }

    // Method: Get the Class Name to which the group
    // is connected to
    function get_class_name() {

	return $this->pr_class_name;
    }

    // Method: Get Permssion
    function get_permission() {

	return $this->pr_permission;
    }

    // Method: Get Manager ID
    function get_manager_id() {

	return $this->pr_manager_id;
    }

    // Method: Get Manger Alias
    function get_manager_alias() {


	return $this->pr_manager_alias;
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

    // Method: Set class id from class name
    function set_class_id($a_class_name) {

	// Create the query string
	$this->pr_query = "select class_id from rct_classes
                           where class_name='$a_class_name'";

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
	    $this->pr_class_id = $row[0];
	}
	else {

	    disp_err_msg("ERROR: DB returned zero or more then one tuple!");
	}

	// Close DB connection
	$db->close();
	unset($db);
    }
    
    // Method: Set class name from class id
    function set_class_name($a_class_id) {

	// Create the query string
	$this->pr_query = "select class_name from rct_classes
                           where class_id='$a_class_id'";

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
    }
    
}

?>