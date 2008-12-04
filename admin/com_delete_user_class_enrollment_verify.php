<?php

// $Id: com_delete_user_class_enrollment_verify.php,v 1.1 2002/10/31 21:37:41 thomas Exp $

require("common.php");


function gen_form($file1, $file2) {

    if(page_access_granted($_POST['access_id'], $_POST['access'])) {

	gen_html_header("Verify user class unenrollment:");

	// Check if we got a valid class and user id
	if($_POST['class_id'] && $_POST['user_id']) {

	    unenroll_user_from_class();
	}

	unenroll_user_from_another_class($file1);
    
	go_to_admin_tasks($_POST['access_id'], $_POST['access'], $file2);
    
	gen_menu_bar1();
    
	gen_html_footer();
    }
}

// Functions
// ---------
function unenroll_user_from_another_class($file) {

    printf("
            <form method='post' action='" . $file . "'>
            <input type=hidden name='user_id' value='" . $_POST['user_id'] . "'>
            <input type=hidden name='access' value='" . $_POST['access'] . "'>
            <input type=hidden name='access_id' value='" . $_POST['access_id'] . "'>
            <input type=submit value='Unenroll User From Another Class'>
            </form>");
}

function unenroll_user_from_class() {

    // We have to make sure that when a user gets unenrolled from a class,
    // that he/she also gets unenrolled from any class specific teams, since
    // a user can only be enrolled in a class team if the user is enrolled in
    // the class as well.
    
    // Query
    $query = "DELETE FROM rct_enrolled WHERE class_id='" . $_POST['class_id'] . "'
              and user_id='" . $_POST['user_id'] . "'";

    // Create a DB object
    $db = new DB();

    // Connect to the DB
    $db->connect();

    // Delete the user/class enrollment
    $db_res = $db->exec($query, $_POST['access_id']);

    // Check for errors
    if(!$db_res) {

	disp_err_msg("ERROR: Was not able to unenroll user from class in DB");

    }
    else {

	disp_msg("Unenrolled user from class successfully in DB.");
    }


    // Free result
    $db->free_result($db_res);
    
    // Query
    $query = "select team_id from rct_teams where class_id='" . $_POST['class_id'] . "'";

    // Getting all the class relevant teams
    $db_res = $db->exec($query, $_POST['access_id']);

    // Init vars
    $numrows = $db->get_num_rows($db_res);

    // Check if the user was enrolled in any teams
    if(0 == $numrows) {

        disp_msg("The selected user is not enrolled in any teams.");
	$db->close();
        unset($db);
        return;
    }

    // Now we check each team if the user was enrolled in it.
    // If the user is enrolled, we unenroll him/her from the team.
    for($i = 0; $i < $numrows; $i++) {

	$myrow = $db->fetch_row ($db_res, $i);

	$query = "DELETE FROM rct_member_team WHERE team_id='" . $myrow[0] . "'
                  and user_id='" . $_POST['user_id'] . "'";

	// Try to delete the user/team enrollment
	$db_res_del = $db->exec($query, $_POST['access_id']);

	// Check for errors
	if($db_res_del) {

	    disp_msg("Unenrolled user from team successfully in DB.");
	}

	$db->free_result($db_res_del);
    }

    // Close DB connection
    $db->close();
    unset($db);
}

?>
	      
