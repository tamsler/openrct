<?php

// $Id: db_chat_team.php,v 1.3 2002/01/08 19:59:10 thomas Exp $

// Definitions

// TC = TEAM_CHAT 
define("TC_ID", 0);
define("TC_MSG", 1);
define("TC_CREATOR", 2);
define("TC_CLASS_ID", 3);
define("TC_TEAM_ID", 4);
define("TC_PERMISSION", 5);
define("TC_DATE", 6);
define("TC_VERSION", 7);

class DBTC {

    // Private:
    // --------

    var $pr_chat_id;
    var $pr_chat_msg;
    var $pr_creator;
    var $pr_class_id;
    var $pr_team_id;
    var $permission;
    var $pr_date;
    var $pr_version;
    var $pr_query;
    var $pr_creator_alias;
    var $pr_class_name;
    var $pr_team_name;
    
    
    // Public:
    // -------

    // Constructor:
    function DBTC() {

	// Nothing to do here
    }

    // Method: Init with Chat ID
    function init_with_chat_id($a_chat_id) {

	// Create the query string
	$this->pr_query = "select * from rct_chat_log_teams
                           where chat_id='$a_chat_id'";

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
	    $this->pr_chat_id = $row[TC_ID];
	    $this->pr_chat_msg = $row[TC_MSG];
	    $this->pr_creator = $row[TC_CREATOR];
	    $this->pr_class_id = $row[TC_CLASS_ID];
	    $this->pr_team_id = $row[TC_TEAM_ID];
	    $this->permission = $row[TC_PERMISSION];
	    $this->pr_date = $row[TC_DATE];
	    $this->pr_version = $row[TC_VERSION];
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
	$this->pr_chat_id = $a_tuple[TC_ID];
	$this->pr_chat_msg = $a_tuple[TC_MSG];
	$this->pr_creator = $a_tuple[TC_CREATOR];
	$this->pr_class_id = $a_tuple[TC_CLASS_ID];
	$this->pr_team_id = $a_tuple[TC_TEAM_ID];
	$this->permission = $a_tuple[TC_PERMISSION];
	$this->pr_date = $a_tuple[TC_DATE];
	$this->pr_version = $a_tuple[TC_VERSION];
    }
    
    // Method: get Chat ID
    function get_chat_id() {

	return $this->pr_chat_id;
    }
    
    // Method: Get Chat Message
    function get_chat_msg() {

	return $this->pr_chat_msg;
    }

    // Method: Get Creator
    function get_creator() {

	return $this->pr_creator;
    }

    // Method: Get Class ID
    function get_class_id() {

	return $this->pr_class_id;
    }

    // Method: Get Team ID
    function get_team_id() {

	return $this->pr_team_id;
    }

    // Method: Get Permission
    function get_permission() {

	return $this->pr_permission;
    }
    
    // Method: Get Date
    function get_date() {

	return $this->pr_date;
    }

    // Method: Get Version
    function get_version() {

	return $this->pr_version;
    }

    // Method: Get Creator's Alias
    function get_creator_alias() {

	// Create the query string
	$this->pr_query = "select alias from rct_users
                           where user_id='$this->pr_creator'";

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

	    // Get the alias
	    $this->pr_creator_alias = $row[0];
	}
	else {

	    disp_err_msg("ERROR: DB returned zero or more then one tuple!");
	}

	// Close DB connection
	$db->close();
	unset($db);

	return $this->pr_creator_alias;
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

    
    // Method: Get Team Name from Team ID:
    function get_team_name() {

	// Create the query string
	$this->pr_query = "select team_name from rct_teams
                           where team_id='$this->pr_team_id'";

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

	    // Get the group name
	    $this->pr_group_name = $row[0];
	}
	else {

	    disp_err_msg("ERROR: DB returned zero or more then one tuple!");
	}

	// Close DB connection
	$db->close();
	unset($db);

	return $this->pr_group_name;
    }
}

?>