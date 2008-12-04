<?php

// $Id: com_admin_teams_input.php,v 1.3 2003/06/20 17:23:16 thomas Exp $

require("common.php");
require("db_team.php");


function gen_form($file1, $file2, $file3, $query) {

    if(page_access_granted($_POST['access_id'], $_POST['access'])) {

	gen_html_header("Administer TEAMS:");

	// Create a team object
	$db_team = new DBTeam();
    
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

	printf ("<table border='1' cellpadding='5'>\n");

	printf("<tr>");

	for($i = 0; $i < $numfields; $i++) {

	    printf("<td align='center'><b><font face='arial' color=" . TB_HEAD_1 . " >" . $db->get_field_name($db_res, $i) . "</font></b></td>");
	}

	print("<td align='center'><b><font face='arial' color=" . TB_HEAD_1 . " >Modify</font></b></td>");
	print("<td align='center'><b><font face='arial' color=" . TB_HEAD_1 . " >Delete</font></b></td>");
    
	printf("</tr>\n");

	for($i = 0; $i < $numrows; $i++) {

	    $myrow = $db->fetch_row ($db_res, $i);

	    if($i % 2) {

		printf("<tr bgcolor=" . TB_COLOR_1 . " >");
		for($j = 0; $j < $numfields; $j++) {
		    if(TEAM_MANAGER == $j) {
			$db_team->set_manager_alias($myrow[TEAM_MANAGER]);
			printf("<td>%s</td>", $db_team->get_manager_alias());
		    }
		    elseif(TEAM_CLASS_ID == $j) {
			$db_team->set_class_name($myrow[TEAM_CLASS_ID]);
			printf("<td>%s</td>", $db_team->get_class_name());
		    }
		    else {
			printf ("<td>%s</td>", $myrow[$j]);
		    }
		}
		gen_modify($myrow, $file3);
		gen_delete($myrow, $file2);
		printf("</tr>\n");
	    }
	    else {

		printf("<tr bgcolor=" . TB_COLOR_2 . " >");
		for($j = 0; $j < $numfields; $j++) {
		    if(TEAM_MANAGER == $j) {
			$db_team->set_manager_alias($myrow[TEAM_MANAGER]);
			printf("<td>%s</td>", $db_team->get_manager_alias());
		    }
		    elseif(TEAM_CLASS_ID == $j) {
			$db_team->set_class_name($myrow[TEAM_CLASS_ID]);
			printf("<td>%s</td>", $db_team->get_class_name());
		    }
		    else {
			printf ("<td>%s</td>", $myrow[$j]);
		    }
		}
		gen_modify($myrow, $file3);
		gen_delete($myrow, $file2);
		printf("</tr>\n");
	    }
	
	}

	printf ("</table>\n");
    
	back_to_admin_tasks($_POST['access_id'], $_POST['access'], $file1);

	gen_menu_bar1();

	gen_html_footer();

	$db->close();
	unset($db);
	unset($db_team);
    }
}

// Functions
// ---------

function gen_delete($row, $file2) {

    // Don't show the delete option for the Default Team
    if(0 != strcmp("T0", $row[0])) {
	
	printf("<td>
		<form method='post' action='" . $file2. "'>
		<input type=hidden name=team_id value='" . $row[0] . "'>
		<input type=hidden name=access value='" . $_POST['access'] . "'>
		<input type=hidden name=access_id value='" . $_POST['access_id'] . "'>
		<input type=submit value='Delete'>
		</form>
		</td>");
    }
    else {
	
	printf("<td>&nbsp;</td>");
    }
}

function gen_modify($row, $file3) {
    
    printf("<td>
            <form method='post' action='" . $file3 ."'>
            <input type=hidden name=team_id value='" . $row[0] . "'>
	    <input type=hidden name=team_name value='" . $row[1] . "'>
            <input type=hidden name=class_id value='" . $row[2] . "'>
	    <input type=hidden name=permission value='" . $row[3] . "'>
	    <input type=hidden name=manager value='" . $row[4] . "'>
	    <input type=hidden name=active_status value='" . $row[5] . "'>
	    <input type=hidden name=access value='" . $_POST['access'] . "'>
	    <input type=hidden name=access_id value='" . $_POST['access_id'] . "'>
	    <input type=submit value='Modify'>
	    </form>
	    </td>");
}

?>
