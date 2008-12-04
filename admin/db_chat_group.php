<?php

// $Id: db_chat_group.php,v 1.3 2002/01/08 19:59:10 thomas Exp $

// Definitions

// GC = GROUP_CHAT 
define("GC_ID", 0);
define("GC_MSG", 1);
define("GC_CREATOR", 2);
define("GC_CLASS_ID", 3);
define("GC_GROUP_ID", 4);
define("GC_PERMISSION", 5);
define("GC_DATE", 6);
define("GC_VERSION", 7);

class DBGC {

    // Private:
    // --------

    var $pr_chat_id;
    var $pr_chat_msg;
    var $pr_creator;
    var $pr_class_id;
    var $pr_group_id;
    var $permission;
    var $pr_date;
    var $pr_version;
    var $pr_query;
    var $pr_creator_alias;
    var $pr_class_name;
    var $pr_group_name;
    
    
    // Public:
    // -------

    // Constructor:
    function DBGC() {

	// Nothing to do here
    }

    // Method: Init with Chat ID
    function init_with_chat_id($a_chat_id) {

	// Create the query string
	$this->pr_query = "select * from rct_chat_log_groups
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
	    $this->pr_chat_id = $row[GC_ID];
	    $this->pr_chat_msg = $row[GC_MSG];
	    $this->pr_creator = $row[GC_CREATOR];
	    $this->pr_class_id = $row[GC_CLASS_ID];
	    $this->pr_group_id = $row[GC_GROUP_ID];
	    $this->permission = $row[GC_PERMISSION];
	    $this->pr_date = $row[GC_DATE];
	    $this->pr_version = $row[GC_VERSION];
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
	$this->pr_chat_id = $a_tuple[GC_ID];
	$this->pr_chat_msg = $a_tuple[GC_MSG];
	$this->pr_creator = $a_tuple[GC_CREATOR];
	$this->pr_class_id = $a_tuple[GC_CLASS_ID];
	$this->pr_group_id = $a_tuple[GC_GROUP_ID];
	$this->permission = $a_tuple[GC_PERMISSION];
	$this->pr_date = $a_tuple[GC_DATE];
	$this->pr_version = $a_tuple[GC_VERSION];
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

    // Method: Get Group ID
    function get_group_id() {

	return $this->pr_group_id;
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

    
    // Method: Get Group Name from Group ID:
    function get_group_name() {

	// Create the query string
	$this->pr_query = "select group_name from rct_groups
                           where group_id='$this->pr_group_id'";

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