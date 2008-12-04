<?php

// $Id: db_team.php,v 1.8 2002/01/25 20:30:05 thomas Exp $

// Definitions
define("TEAM_ID", 0);
define("TEAM_NAME", 1);
define("TEAM_CLASS_ID", 2);
define("TEAM_PERMISSION", 3);
define("TEAM_MANAGER", 4);
define("TEAM_ACTIVE_STATUS", 5);
define("TEAM_DATE", 6);


class DBTeam {

    // Private:
    // --------

    var $pr_id;
    var $pr_name;
    var $pr_class_id;
    var $pr_class_name;
    var $pr_permission;
    var $pr_manager_id;
    var $pr_manager_alias;
    var $pr_active_status;
    var $pr_date;
    var $pr_query;


    // Public:
    // -------

    // Constructor:
    function DBTeam() {

	// Nothing to do here
    }
    
    // Method: Init with Team ID
    function init_with_team_id($a_team_id) {
    
	// Create the query string
	$this->pr_query = "select * from rct_teams
                           where team_id='$a_team_id'";

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

	    // Assigining the private team members
	    $this->pr_id = $row[TEAM_ID];
	    $this->pr_name = $row[TEAM_NAME];
	    $this->pr_class_id = $row[TEAM_CLASS_ID];
	    $this->pr_permission = $row[TEAM_PERMISSION];
	    $this->pr_manager_id = $row[TEAM_MANAGER];
	    $this->pr_active_status = $row[TEAM_ACTIVE_STATUS];
	    $this->pr_date = $row[TEAM_DATE];
	}
	else {

	    disp_err_msg("ERROR: DB returned zero or more then one tuple!");
	}

	// Close DB connection
	$db->close();
	unset($db);
    }

    // Method: get Team ID
    function get_id() {

	return $this->pr_id;
    }
    
    // Method: Get Team Name
    function get_name() {

	return $this->pr_name;
    }

    // Method: Get Team Class ID
    function get_class_id() {

	return $this->pr_class_id;
    }

    // Method: Get the Class Name to which the team
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
    

    // Method: Get the team index
    function get_index($access_id = "DB_USER") {

	// Return value
	$team_index = "";
    
	// Query
	$query = "select team_index from rct_indices";
        
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

	    // Get the actual team index
	    $team_index = $row[0];

	    // Now we need to update/increment the team index in the DB
	    $index = $team_index + 1;

	    $query = "UPDATE rct_indices SET team_index='$index'";

	    $db_res = $db->exec($query, $access_id);

	    if(!$db_res) {

		disp_err_msg("ERROR: Was not able to update team index in DB!");
		$db->close();
		unset($db);
		// Return empty string
		return $team_index;
	    }
	    else {

		// All the team_ids start with the letter "T" followed by a number
		$team_index = "T" . $team_index;
		$db->close();
		unset($db);
		// Return team index
		return $team_index;
	    }
	}
	else {

	    disp_err_msg("ERROR: Something wrong with the rct_indices table!");
	    $db->close();
	    unset($db);
	    // Return empty string
	    return $team_index;
	}
    }


    // Method: Test if team exists in db
    // Return true if team exists in db
    // Return false if team does not exists in db
    function does_team_exist_in_db($team_name) {

	// Query
	$query = "select * from rct_teams where team_name='$team_name'";
    
	// Create a DB object
	$db = new DB();

	// Connect to the DB
	$db->connect();

	// Execute query
	$db_res = $db->exec($query);

	// Test db result
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

    // Method: Add Team to DB
    // Returns true on success
    // Returns false on error
    // Note: $ $manager is an user alias
    function add_team_to_db($team_name,
                            $manager,
                            $permission,
                            $active_status,
                            $class_id,
                            $access_id = "DB_USER") {
	
	// First we need to get a unique team index
	$team_id = $this->get_index($access_id);
	
	if(!$team_id || (0 == strcmp($team_id, ""))) {

	    disp_err_msg("ERROR: Was not able to get a team index!");
	    return false;
	}

	// We need to actual user id
	$this->set_manager_id($manager);
	
	// Query
	$query = "INSERT INTO rct_teams
                  VALUES ('$team_id',
                          '$team_name',
                          '$class_id',
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

	    disp_err_msg("ERROR: Was not able to insert new team into DB!");
	    $db->close();
	    unset($db);
	    return false;
	}
	else {

	    disp_msg("Added team [ " . $team_name . " ] successfully to DB.");
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