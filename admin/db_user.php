<?php

// $Id: db_user.php,v 1.19 2002/11/06 23:55:15 thomas Exp $

// Definitions
define("USER_ID", 0);
define("USER_ALIAS", 1);
define("USER_FIRST_NAME", 2);
define("USER_LAST_NAME", 3);
define("USER_PASSWORD", 4);
define("USER_PERMISSION", 5);
define("USER_ONLINE_STATUS", 6);
define("USER_DATE", 7);


class DBUser {

    // Private:
    // --------

    var $pr_id;
    var $pr_alias;
    var $pr_first_name;
    var $pr_last_name;
    var $pr_password;
    var $pr_permission;
    var $pr_online_status;
    var $pr_date;
    var $pr_query;
    var $db;


    // Public:
    // -------

    // Constructor:
    function DBUser() {

	// Nothing to do here
    }

    // Method: Init with User ID
    function init_with_user_id($a_user_id) {

	// Create the query string
	$this->pr_query = "select * from rct_users
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

	    // Assigining the private class members
	    $this->pr_id = $row[USER_ID];
	    $this->pr_alias = $row[USER_ALIAS];
	    $this->pr_first_name = $row[USER_FIRST_NAME];
	    $this->pr_last_name = $row[USER_LAST_NANME];
	    $this->pr_password = $row[USER_PASSWORD];
	    $this->pr_permission = $row[USER_PERMISSION];
	    $this->pr_online_status = $row[USER_ONLINE_STATUS];
	    $this->pr_date = $row[USER_DATE];
	}
	else {

	    disp_err_msg("ERROR: DB returned zero or more then one tuple!");
	}

	// Close DB connection
	$db->close();
	unset($db);
    }

    // Method: Init with Alias
    function init_with_alias($a_alias) {

	// Create the query string
	$this->pr_query = "select * from rct_users
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

	    // Assigining the private class members
	    $this->pr_id = $row[USER_ID];
	    $this->pr_alias = $row[USER_ALIAS];
	    $this->pr_first_name = $row[USER_FIRST_NAME];
	    $this->pr_last_name = $row[USER_LAST_NANME];
	    $this->pr_password = $row[USER_PASSWORD];
	    $this->pr_permission = $row[USER_PERMISSION];
	    $this->pr_online_status = $row[USER_ONLINE_STATUS];
	    $this->pr_date = $row[USER_DATE];
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

	// Assigining the private class members
	$this->pr_id = $a_tuple[USER_ID];
	$this->pr_alias = $a_tuple[USER_ALIAS];
	$this->pr_first_name = $a_tuple[USER_FIRST_NAME];
	$this->pr_last_name = $a_tuple[USER_LAST_NANME];
	$this->pr_password = $a_tuple[USER_PASSWORD];
	$this->pr_permission = $a_tuple[USER_PERMISSION];
	$this->pr_online_status = $a_tuple[USER_ONLINE_STATUS];
	$this->pr_date = $a_tuple[USER_DATE];
    }

    // Method: get User ID
    function get_id() {

	return $this->pr_id;
    }
    
    // Method: Get alias
    function get_alias() {

	return $this->pr_alias;
    }

    // Method: Get First Name
    function get_first_name() {

	return $this->pr_first_name;
    }

    // Method: Get Last Name
    function get_last_name() {

	return $this->pr_last_name;
    }

    // Method: Get Password
    function get_password() {

	return $this->pr_password;
    }

    // Method: Get Permission
    function get_permission() {

	return $this->pr_permission;
    }

    // Method: Get Online Status
    function get_online_status() {

	return $this->pr_online_status;
    }

    // Method: Get Date
    function get_date() {

	return $this->pr_date;
    }

    // Method: Get new USER ID
    function get_index($access_id = "DB_USER") {

	// Return value
	$user_index = "";
    
	// Query
	$query = "select user_index from rct_indices";
        
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

	    // Get the actual user index
	    $user_index = $row[0];

	    // Now we need to update/increment the user index in the DB
	    $index = $user_index + 1;

	    $query = "UPDATE rct_indices SET user_index='$index'";

	    $db_res = $db->exec($query, $access_id);

	    if(!$db_res) {

		disp_err_msg("ERROR: Was not able to update user index in DB!");
		$db->close();
		unset($db);
		return $user_index;
	    }
	    else {

		$user_index = "U" . $user_index;
		$db->close();
		unset($db);
		return $user_index;
	    }
	}
	else {

	    disp_err_msg("ERROR: Something wrong with the rct_indices table!");
	    $db->close();
	    unset($db);
	    return $user_index;
	}

    }

    // Method: Print the user fields
    function print_user() {

	printf("Alias:      %s<br>
                FirstName:  %s<br>
                LastName:   %s<br>
                Password:   %s<br>
                Permission: %s<br>",
	       $this->pr_alias,
	       $this->pr_first_name,
	       $this->pr_last_name,
	       $this->pr_password,
	       $this->pr_permission);
    }

    // Method: Add user to the DB
    // Returns true on success
    // Returns false on error
    function add_user_to_db($a_alias,
    $a_first_name,
    $a_last_name,
    $a_password,
    $a_permission,
    $a_access_id = "DB_USER") {

	// Get the user index
	$l_user_id = $this->get_index($a_access_id);

	if(!$l_user_id || (0 == strcmp($l_user_id, ""))) {

	    disp_err_msg("ERROR: Was not able to get a user index!");
	    return false;
	}
    
	// Query
	$l_trans = "INSERT INTO rct_users
                    VALUES ('$l_user_id',
                            '$a_alias',
                            '$a_first_name',
                            '$a_last_name',
                            '$a_password',
                            '$a_permission',
                            false,
                            'now')";
    
	// Create a DB object
	$l_db = new DB();

	// Connect to the DB
	$l_db->connect();

	// Execute transaction
	$l_db_res = $l_db->exec($l_trans, $a_access_id);

	// Check for errors
	if(!$l_db_res) {

	    disp_err_msg("ERROR: Was not able to insert new user into DB!");
	    $l_db->close();
	    unset($l_db);
	    return  false;
	}
	else {

	    disp_msg("Added user [ " . $a_alias . " ] successfully to DB.");
	    $l_db->close();
	    unset($l_db);
	    return true;
	}
    }

    // Method: Test if user exists in DB
    // Returns true if user exists in db
    // Returns false if user does not exist in db
    function does_user_exist_in_db($a_alias) {

	// Query
	$query = "select * from rct_users where alias='$a_alias'";
    
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

    // Method: Generate the html permission selection code
    function select_permission($a_permission, $can_assign_admin) {

	if(true == $can_assign_admin) {

	    switch($a_permission) {
	    
	    case 0:
		return "<select name=permission>
		    <option value=0 selected>USER
		    <option value=1>MANAGER
		    <option value=2>ADMIN
		    </select>";
		break;

	    case 1: 
		return "<select name=permission>
		    <option value=0>USER
		    <option value=1 selected>MANAGER
		    <option value=2>ADMIN
		    </select>";
		break;

	    case 2:
		return "<select name=permission>
		    <option value=0>USER
		    <option value=1>MANAGER
		    <option value=2 selected>ADMIN
		    </select>";
		break;

	    default:
		return "<select name=permission>
		    <option value=0 selected>USER
		    <option value=1>MANAGER
		    <option value=2>ADMIN
		    </select>";
	    }
	}
	else {

	    switch($a_permission) {
	    
	    case 0:
		return "<select name=permission>
		    <option value=0 selected>USER
		    <option value=1>MANAGER
		    </select>";
		break;

	    case 1: 
		return "<select name=permission>
		    <option value=0>USER
		    <option value=1 selected>MANAGER
		    </select>";
		break;

		// We do not have a case "2" since
		// we do not allow admin assignment
		// in this section
		
	    default:
		return "<select name=permission>
		    <option value=0 selected>USER
		    <option value=1>MANAGER
		    </select>";
	    }
	}
    }

    // Method: Get the user_id from user_alias
    function get_id_from_alias($a_alias) {

	// Query
	$query = "select * from rct_users where alias='" . $a_alias . "'";

	// Create a DB object
	$db = new DB();

	// Connect to the DB
	$db->connect();

	// Execute query
	$db_res = $db->exec($query);

	// Test db result
	if($db->one_tuple($db_res)) {

	    $row = pg_fetch_row($db_res, 0);

	    $user_id = $row[USER_ID];

	    // Close DB connection
	    $db->close();
	    unset($db);
	    return $user_id;
	}
	else {

	    // Close DB connection
	    $db->close();
	    unset($db);
	    return null;
	}
    }
    
    // Method: Test if user is of type admin
    function is_admin($a_user_id) {

	// Query
	$query = "select * from rct_users where user_id='" . $a_user_id . "'";

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

	    $row = pg_fetch_row($db_res, 0);

	    if(0 == strcmp(ADMIN, $row[USER_PERMISSION])) {

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
    }
}

?>
	
