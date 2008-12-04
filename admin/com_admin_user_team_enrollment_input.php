<?php

// $Id: com_admin_user_team_enrollment_input.php,v 1.2 2002/11/04 23:06:20 thomas Exp $

require("common.php");
require("db_team.php");

function gen_form($file1, $file2, $file3, $query) {

    if(page_access_granted($_POST['access_id'], $_POST['access'])) {

	gen_html_header("Teams For Selected User:");

	// Check if a user was selected
	if(!$_POST['user_id']) {

	    disp_err_msg("ERROR: You didn't select a USER!");

	    go_back($file3);

	    gen_menu_bar1();
    
	    gen_html_footer();
	}
	else {
    
	    // Getting the user's alias
	    $db_user_sel = new DBUser();
	    $db_user_sel->init_with_user_id($_POST['user_id']);
    
	    disp_simple_msg("[ " . $db_user_sel->get_alias(). " ] is Enrolled In These Teams:<br>");
    
	    display_user_teams($query, $file1);
    
	    back_to_admin_tasks($_POST['access_id'], $_POST['access'], $file3);

	    gen_menu_bar1();
    
	    gen_html_footer();

	    unset($db_user_sel);
	}
    }
}

// Functions
// ---------

function display_user_teams($query, $file1) {

    // Create a DB object
    $db = new DB();

    // Connect to the DB
    $db->connect();

    // Execute query
    $db_res = $db->exec($query);

    // Check for errors
    if (!$db_res) {
	
	disp_err_msg("ERROR: Couldn't get team enrollment from DB!");
	$db->close();
	unset($db);
	exit();
    }

    // Init vars
    $numrows = $db->get_num_rows($db_res);

    // Check if there are any teams
    if(0 == $numrows) {

        disp_msg("The selected person is not enrolled in any teams.");
	$db->close();
        unset($db);
        return;
    }

    printf("<table border='1' cellpadding='5'>");

    printf("<tr>");

    printf("<td align='center'><b><font face='arial' color=" . TB_HEAD_1 . " >Team in Class<br></font></b></td>");

    printf("<td align='center'><b><font face='arial' color=" . TB_HEAD_1 . " >Unenroll</font></b></td>");
    
    printf("</tr>");

    for($i = 0; $i < $numrows; $i++) {

	$myrow = $db->fetch_row ($db_res, $i);

	// Create a team object
	$db_team = new DBTeam();
	$db_team->init_with_team_id($myrow[0]);
	// We also init the class name
	$db_team->set_class_name($db_team->get_class_id());
	
	if($i % 2) {

	    printf("<tr bgcolor=" . TB_COLOR_1 . " >");

	    printf("<td>%s in %s</td>",
		   $db_team->get_name(),
		   $db_team->get_class_name());

	    gen_user_team_delete($db_team->get_id(), $file1);

	    printf("</tr>");
	}
	else {

	    printf("<tr bgcolor=" . TB_COLOR_2 . " >");

	    printf("<td>%s in %s</td>",
		   $db_team->get_name(),
		   $db_team->get_class_name());

	    gen_user_team_delete($db_team->get_id(), $file1);

	    printf("</tr>");
	}

	unset($db_team);
    }

    printf ("</table><br><br>");

    $db->close();
    unset($db);
}

function gen_user_team_delete($team_id, $file) {

    printf("<td>
	    <form method='post' action='" . $file . "'>
            <input type=hidden name=user_id value='" . $_POST['user_id'] . "'>
	    <input type=hidden name=team_id value='" . $team_id . "'>
	    <input type=hidden name=access value='" . $_POST['access'] . "'>
	    <input type=hidden name=access_id value='" . $_POST['access_id'] . "'>
	    <input type=submit value='Unenroll'>
	    </form>
	    </td>");
}

function go_back($file) {

    printf("<form method='post' action='" . $file . "'>
	    <input type=hidden name=access value='" . $_POST['access'] . "'>
	    <input type=hidden name=access_id value='" . $_POST['access_id'] . "'>
	    <input type=submit value='Go Back'>
	    </form>");
}

?>
