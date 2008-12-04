<?php

// $Id: enroll_module.php,v 1.1 2002/11/12 17:22:40 thomas Exp $

class EnrollModule {

    // Constructor:
    function EnrollModule() {

	// Nothing to do here
    }

    // Method:
    // Test if user is enrolled in team
    function is_user_enrolled_in_team($team_id, $user_id) {
	
	$query = "select * from rct_member_team where
                  user_id='" . $user_id . "' and
                  team_id='" . $team_id . "'";

	// Create a DB object and connect
	$db = new DB();
	$db->connect();

	// Execute query
	$db_res = $db->exec($query);

	// Test result
	if(0 != $db->get_num_rows($db_res)) {

	    // Close DB connection
	    $db->close();
	    unset($db);

	    // There is an entry
	    return true;
	    
	}
	else {

	    // Close DB connection
	    $db->close();
	    unset($db);

	    return false;
	}
    }

    // Method:
    // Test if user is enrolled in class
    function is_user_enrolled_in_class($class_id, $user_id) {

	$query = "select * from rct_enrolled where
                   user_id='" . $user_id . "'
                   and class_id='" . $class_id . "'";

	// Create a DB object and connect
	$db = new DB();
	$db->connect();

	// Execute query
	$db_res = $db->exec($query);

	if(0 != $db->get_num_rows($db_res)) {

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
    
    // Method:
    // Test if a user is enrolled in the class where the
    // team is connected to.
    function can_user_enroll_in_team($team_id, $user_id) {

	$query = "select * from rct_teams where
                  team_id='" . $team_id . "' and
                  class_id in
                  (select class_id from rct_enrolled where user_id='" . $user_id . "')";

	// Create a DB object and connect
	$db = new DB();
	$db->connect();

	// Execute query
	$db_res = $db->exec($query);

	// Testing result
	if($db->zero_tuple($db_res)) {

	    $db->close();
	    unset($db);
	    return false;
	}
	else {

	    $db->close();
	    unset($db);
	    return true;
	}	
    }

    // Method:
    // Enroll a user in a class
    function enroll_user_in_class($class_id, $user_id) {

	$query = "INSERT INTO rct_enrolled VALUES
                   ('" . $user_id . "', '" . $class_id . "', 'now')";

	// Create a DB object and connect
	$db = new DB();
	$db->connect();

	// Execute query
	$db_res = $db->exec($query);

	// Check for errors
	if(!$db_res) {

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

    // Method:
    // Enroll a user in a team
    function enroll_user_in_team($team_id, $user_id) {

	$query = "INSERT INTO rct_member_team
                  VALUES ('" . $user_id . "', '" . $team_id . "')";

	// Create a DB object and connect
	$db = new DB();
	$db->connect();

	// Execute query
	$db_res = $db->exec($query);

	// Check for errors
	if(!$db_res) {

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
}

?>
	
    