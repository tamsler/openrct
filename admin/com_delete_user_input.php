<?php

// $Id: com_delete_user_input.php,v 1.1 2002/10/01 19:51:41 thomas Exp $

require("common.php");
require("db_class.php");
require("db_team.php");
require("db_group.php");

function gen_form($file1, $file2) {

    if(page_access_granted($_POST['access_id'], $_POST['access'])) {

	gen_html_header("Delete USER:");

	echo("<form method='post' action='" . $file1 . "'>");

	disp_simple_msg("List of Classes where you need to change the MANAGER:");
	list_classes_for_user();

	echo("<br><br>");
    
	disp_simple_msg("List of Teams where you need to change the MANAGER:");
	list_teams_for_user();

	echo("<br><br>");
	disp_simple_msg("List of Groups where you need to change the MANAGER:");
	list_groups_for_user();
    
	printf("<br>
            <input type=hidden name='user_id' value='" . $_POST['user_id'] . "'>
            <input type=hidden name='access' value='" . $_POST['access'] . "'>
            <input type=hidden name='access_id' value='" . $_POST['access_id'] . "'>
            <input type=submit value='Delete User And Make Changes'>
            </form>");

	go_to_admin_tasks($_POST['access_id'], $_POST['access'], $file2);
    
	gen_menu_bar1();

	gen_html_footer();
    }
}

// Functions
// ---------

function list_classes_for_user() {

    // Create a class object
    $db_class = new DBClass();
    
    // Query
    $query = "select * from rct_classes where manager='" . $_POST['user_id'] . "'";

    // Create a DB object
    $db = new DB();

    // Connect to the DB
    $db->connect();

    // Execute query
    $db_res = $db->exec($query);

    // Check for errors
    if (!$db_res) {
	
	disp_err_msg("ERROR: Couldn't get Classes from DB!");
	$db->close();
	unset($db);
	exit();
    }

    // Init vars
    $numrows = $db->get_num_rows($db_res);
    $numfields = $db->get_num_fields($db_res);

    // Check if the user is a manager for any classes
    if(0 == $numrows) {

	disp_msg("User does not manage any classes.");
	$db->close();
	unset($db);
	return;
    }

    printf ("<table border='1' cellpadding='5'>\n");

    printf("<tr>");

    for($i = 0; $i < $numfields; $i++) {

	printf("<td align='center'><b><font face='arial' color=" . TB_HEAD_1 . " >" . $db->get_field_name($db_res, $i) . "</font></b></td>");
    }

    print("<td align='center'><b><font face='arial' color='red'>New Manager</font></b></td>");
    
    printf("</tr>\n");

    for($i = 0; $i < $numrows; $i++) {

	$myrow = $db->fetch_row ($db_res, $i);

	// Building the array of Class IDs
	$classes[$i] = $myrow[CLASS_ID];
	
	if($i % 2) {

	    printf("<tr bgcolor=" . TB_COLOR_1 . " >");
	    for($j = 0; $j < $numfields; $j++) {
		if(CLASS_MANAGER == $j) {
		    $db_class->set_manager_alias($myrow[CLASS_MANAGER]);
		    printf("<td>%s</td>", $db_class->get_manager_alias());
		}
		else {
		    printf ("<td>%s</td>", $myrow[$j]);
		}
	    }
	    gen_class_manager_list();
	    printf("</tr>\n");
	}
	else {

	    printf("<tr bgcolor=" . TB_COLOR_2 . " >");
	    for($j = 0; $j < $numfields; $j++) {
		if(CLASS_MANAGER == $j) {
		    $db_class->set_manager_alias($myrow[CLASS_MANAGER]);
		    printf("<td>%s</td>", $db_class->get_manager_alias());
		}
		else {
		    printf ("<td>%s</td>", $myrow[$j]);
		}
	    }
	    gen_class_manager_list();
	    printf("</tr>\n");
	}
	
    }

    printf ("</table>\n");

    // Now we stringify the array of Class IDs and add it as a hidden field
    $classes_str = implode(":", $classes);
    printf("<input type=hidden name='classes' value='$classes_str'>");

    $db->close();
    unset($db);
    unset($db_class);
}


function list_teams_for_user() {

    // Create a team object
    $db_team = new DBTeam();
    
    // Query
    $query = "select * from rct_teams where manager='" . $_POST['user_id'] . "'";

    // Create a DB object
    $db = new DB();

    // Connect to the DB
    $db->connect();

    // Execute query
    $db_res = $db->exec($query);

    // Check for errors
    if (!$db_res) {
	
	disp_err_msg("ERROR: Couldn't get Teams from DB!");
	$db->close();
	unset($db);
	exit();
    }

    // Init vars
    $numrows = $db->get_num_rows($db_res);
    $numfields = $db->get_num_fields($db_res);

    // Check if the user is a manager for any teams
    if(0 == $numrows) {

	disp_msg("User does not manage any teams.");
	$db->close();
	unset($db);
	return;
    }

    printf ("<table border='1' cellpadding='5'>\n");

    printf("<tr>");

    for($i = 0; $i < $numfields; $i++) {

	printf("<td align='center'><b><font face='arial' color=" . TB_HEAD_1 . " >" . $db->get_field_name($db_res, $i) . "</font></b></td>");
    }

    print("<td align='center'><b><font face='arial' color='red'>New Manager</font></b></td>");

    printf("</tr>\n");

    for($i = 0; $i < $numrows; $i++) {

	$myrow = $db->fetch_row ($db_res, $i);

	// Building the array of Team IDs
	$teams[$i] = $myrow[TEAM_ID];
	
	if($i % 2) {

	    printf("<tr bgcolor=" . TB_COLOR_1 . " >");
	    for($j = 0; $j < $numfields; $j++) {
		if(TEAM_MANAGER == $j) {
		    $db_team->set_manager_alias($myrow[TEAM_MANAGER]);
		    printf("<td>%s</td>", $db_team->get_manager_alias());
		}
		else {
		    printf ("<td>%s</td>", $myrow[$j]);
		}
	    }
	    gen_team_manager_list();
	    printf("</tr>\n");
	}
	else {

	    printf("<tr bgcolor=" . TB_COLOR_2 . " >");
	    for($j = 0; $j < $numfields; $j++) {
		if(TEAM_MANAGER == $j) {
		    $db_team->set_manager_alias($myrow[TEAM_MANAGER]);
		    printf("<td>%s</td>", $db_team->get_manager_alias());
		}
		else {
		    printf ("<td>%s</td>", $myrow[$j]);
		}
	    }
	    gen_team_manager_list();
	    printf("</tr>\n");
	}
	
    }

    printf ("</table>\n");

    // Now we stringify the array of Team IDs and add it as a hidden field
    $teams_str = implode(":", $teams);
    printf("<input type=hidden name='teams' value='$teams_str'>");

    $db->close();
    unset($db);
    unset($db_team);
}

function list_groups_for_user() {

    // Create a group object
    $db_group = new DBGroup();
    
    // Query
    $query = "select * from rct_groups where manager='" . $_POST['user_id'] . "'";

    // Create a DB object
    $db = new DB();

    // Connect to the DB
    $db->connect();

    // Execute query
    $db_res = $db->exec($query);

    // Check for errors
    if (!$db_res) {
	
	disp_err_msg("ERROR: Couldn't get Groups from DB!");
	$db->close();
	unset($db);
	exit();
    }

    // Init vars
    $numrows = $db->get_num_rows($db_res);
    $numfields = $db->get_num_fields($db_res);

    // Check if the user is a manager for any groups
    if(0 == $numrows) {

	disp_msg("User does not manage any groups.");
	$db->close();
	unset($db);
	return;
    }

    printf ("<table border='1' cellpadding='5'>\n");

    printf("<tr>");

    for($i = 0; $i < $numfields; $i++) {

	printf("<td align='center'><b><font face='arial' color=" . TB_HEAD_1 . " >" . $db->get_field_name($db_res, $i) . "</font></b></td>");
    }

    print("<td align='center'><b><font face='arial' color='red'>New Manager</font></b></td>");

    printf("</tr>\n");

    for($i = 0; $i < $numrows; $i++) {

	$myrow = $db->fetch_row ($db_res, $i);

	// Building the array of Group IDs
	$groups[$i] = $myrow[GROUP_ID];
	
	if($i % 2) {

	    printf("<tr bgcolor=" . TB_COLOR_1 . " >");
	    for($j = 0; $j < $numfields; $j++) {
		if(GROUP_MANAGER == $j) {
		    $db_group->set_manager_alias($myrow[GROUP_MANAGER]);
		    printf("<td>%s</td>", $db_group->get_manager_alias());
		}
		else {
		    printf ("<td>%s</td>", $myrow[$j]);
		}
	    }
	    gen_group_manager_list();
	    printf("</tr>\n");
	}
	else {

	    printf("<tr bgcolor=" . TB_COLOR_2 . " >");
	    for($j = 0; $j < $numfields; $j++) {
		if(GROUP_MANAGER == $j) {
		    $db_group->set_manager_alias($myrow[GROUP_MANAGER]);
		    printf("<td>%s</td>", $db_group->get_manager_alias());
		}
		else {
		    printf ("<td>%s</td>", $myrow[$j]);
		}
	    }
	    gen_group_manager_list();
	    printf("</tr>\n");
	}
	
    }

    printf ("</table>\n");

    // Now we stringify the array of Group IDs and add it as a hidden field
    $groups_str = implode(":", $groups);
    printf("<input type=hidden name='groups' value='$groups_str'>");

    $db->close();
    unset($db);
    unset($db_group);
}

function gen_class_manager_list() {

    printf("<td><select name=class_user_id[] size=1>");

    // Query
    $query = "select user_id, alias from rct_users";

    // Create a DB object
    $db = new DB();

    // Connect to the DB
    $db->connect();

    // Execute query
    $db_res = $db->exec($query);

    // Getting the number of rows returned
    $numrows = $db->get_num_rows($db_res);

    for($i = 0; $i < $numrows; $i++) {

	$row = $db->fetch_row($db_res, $i);

	// Don't list user you want to delete
	if(0 != strcmp($_POST['user_id'], $row[0])) {
	    printf("<option value=$row[0]>$row[1]</option>");
	}
    }

    $db->close();
    unset($db);
    
    printf("</select></td>");
}

function gen_team_manager_list() {

    printf("<td><select name=team_user_id[] size=1>");

    // Query
    $query = "select user_id, alias from rct_users";

    // Create a DB object
    $db = new DB();

    // Connect to the DB
    $db->connect();

    // Execute query
    $db_res = $db->exec($query);

    // Getting the number of rows returned
    $numrows = $db->get_num_rows($db_res);

    for($i = 0; $i < $numrows; $i++) {

	$row = $db->fetch_row($db_res, $i);

	// Don't list user you want to delete
	if(0 != strcmp($_POST['user_id'], $row[0])) {
	    printf("<option value=$row[0]>$row[1]</option>");
	}
    }	

    $db->close();
    unset($db);
    
    printf("</select></td>");
}

function gen_group_manager_list() {

    printf("<td><select name=group_user_id[] size=1>");

    // Query
    $query = "select user_id, alias from rct_users";

    // Create a DB object
    $db = new DB();

    // Connect to the DB
    $db->connect();

    // Execute query
    $db_res = $db->exec($query);

    // Getting the number of rows returned
    $numrows = $db->get_num_rows($db_res);

    for($i = 0; $i < $numrows; $i++) {

	$row = $db->fetch_row($db_res, $i);

	// Don't list user you want to delete
	if(0 != strcmp($_POST['user_id'], $row[0])) {
	    printf("<option value=$row[0]>$row[1]</option>");
	}
    }

    $db->close();
    unset($db);
    
    printf("</select></td>");
}

?>