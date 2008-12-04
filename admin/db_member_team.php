<?php

// $Id: db_member_team.php,v 1.2 2002/01/08 19:59:10 thomas Exp $

// Definitions
// MT = MEMBER_TEAM
define("MT_USER_ID", 0);
define("MT_TEAM_ID", 1);


class DBMemberTeam {

    // Private:
    // --------
    var $pr_user_id;
    var $pr_team_id;
    var $pr_query;
    var $pr_user_alias;
    var $pr_team_name;
    
    
    // Public:
    // -------

    // Constructor:
    function DBMemberTeam($a_user_id, $a_team_id) {
    	
	// Create the query string
	$this->pr_query = "select * from rct_member_team
                           where user_id='$a_user_id'
                           and team_id='$a_team_id'";

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
	    $this->pr_user_id = $row[0];
	    $this->pr_team_id = $row[1];

	}
	else {

	    disp_err_msg("ERROR: DB returned zero or more then one tuple!");
	}

	// Close DB connection
	$db->close();
	unset($db);
    }

    // Method: Get User ID
    function get_user_id() {

	return $this->pr_user_id;
    }
    
    // Method: Get Team ID
    function get_team_id() {

	return $this->pr_team_id;
    }

    // Method: Get User Name from User ID:
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

	    // Get the user name
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

	    // Get the team name
	    $this->pr_team_name = $row[0];
	}
	else {

	    disp_err_msg("ERROR: DB returned zero or more then one tuple!");
	}

	// Close DB connection
	$db->close();
	unset($db);

	return $this->pr_team_name;
    }
}

?>