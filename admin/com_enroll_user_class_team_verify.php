<?php

// $Id: com_enroll_user_class_team_verify.php,v 1.5 2002/11/12 17:22:40 thomas Exp $

require("common.php");
require("enroll_module.php");


function gen_form($file1) {

    if(page_access_granted($_POST['access_id'], $_POST['access'])) {

	gen_html_header("Enroll Multiple Users In Class And Team:");

	// Use trim here because some browsers add white space
	$_POST['user_data'] = trim($_POST['user_data']);

	// Check if textarea has context
	if(!$_POST['user_data']) {

	    disp_err_msg("ERROR: You did not enter any users!");
	    back_to_admin_tasks($_POST['access_id'], $_POST['access'], $file1);
	    gen_menu_bar1();
	    gen_html_footer();
	    exit();
	}

	// Check if there is a selection in either classes or teams list
	if(!$_POST['class_id'] && !$_POST['team_id']) {

	    disp_err_msg("ERROR: You did not select a class nor a team!");
	    back_to_admin_tasks($_POST['access_id'], $_POST['access'], $file1);
	    gen_menu_bar1();
	    gen_html_footer();
	    exit();
	}

	// Make sure we get the linebreaks right
	$parse_text = nl2br($_POST['user_data']);

	// Splitting up all the lines from
	// the text area into an array
	$user_array = split("<br />", $parse_text);

	$num_users = count($user_array);

	// Create an User object
	$db_user = new DBUser();

	// Create an EnrollModule object
	$em = new EnrollModule();

	// Now loop through selected classes and enroll
	// each entered user in these classes
	if($_POST['class_id']) {

	    disp_simple_msg("Enrolling Users In Classes:");

	    // Counting successful user in class enrollments
	    $enroll_counter = 0;
	    
	    for($class_val = current($_POST['class_id']);
		$class_val;
		$class_val = next($_POST['class_id'])) {

		for($i = 0; $i < $num_users; $i++) {

		    $user_id = $db_user->get_id_from_alias(trim($user_array[$i]));

		    // Catching invalid strings
		    if(!$user_id || !$class_val) {
			// Do not display anything
		    }
		    // Test if user is already enrolled in class
		    elseif($em->is_user_enrolled_in_class($class_val, $user_id)) {
			// Do not display anything
		    }
		    // Enroll user in class and check for errors
		    elseif(!$em->enroll_user_in_class($class_val, $user_id)) {
			disp_err_msg("ERROR: Was not able to enroll user in class!");
		    }
		    else {
			$enroll_counter++;
		    }
		}
	    }

	    // Display total successful enrollments
	    disp_msg("Made " . $enroll_counter . " User --> Class Enrollments!");
	    echo "<br><br>";
	}

	// Now loop through selected teams and enroll
	// each entered user in these teams
	if($_POST['team_id']) {

	    disp_simple_msg("Enrolling Users In Teams:");

	    // Counting successful user in team enrollments
	    $enroll_counter = 0;
	    
	    for($team_val = current($_POST['team_id']);
		$team_val;
		$team_val = next($_POST['team_id'])) {

		// Getting the class_id as well just in case we need it later on
		list($l_team_id, $l_class_id) = split(DELIM_CHAR, $team_val, 2);
		
		for($i = 0; $i < $num_users; $i++) {

		    $user_id = $db_user->get_id_from_alias(trim($user_array[$i]));

		    // Catching invalid strings		    
		    if(!$user_id || !$l_team_id) {
			// Do not display anything
		    }
		    // Test if user is already enrolled in team
		    elseif($em->is_user_enrolled_in_team($l_team_id, $user_id)) {
			// Do not display anything
		    }
		    // Test if user is enrolled in class, which has the team attached
		    elseif(!$em->can_user_enroll_in_team($l_team_id, $user_id)) {
			disp_err_msg("ERROR: User is not enrolled in class to which the team belongs!");
		    }
		    // Enroll user in team and check for errors
		    elseif(!$em->enroll_user_in_team($l_team_id, $user_id)) {
			disp_err_msg("ERROR: Was not able to enroll user in team!");
		    }
		    else {
			$enroll_counter++;
		    }	
		    
		}
	    }

	    // Display total successful enrollments
	    disp_msg("Made " . $enroll_counter . " User --> Team Enrollments!");
	    echo "<br><br>";
	}

	back_to_admin_tasks($_POST['access_id'], $_POST['access'], $file1);
	
	gen_menu_bar1();

	gen_html_footer();

	// Clean up memory
	unset($db_user);
	unset($em);
    }
}
