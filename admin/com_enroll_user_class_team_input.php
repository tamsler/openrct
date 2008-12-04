<?php

// $Id: com_enroll_user_class_team_input.php,v 1.4 2002/11/08 19:17:03 thomas Exp $

require("common.php");

define("NUM_USERS", 20);
define("NUM_CLASSES", 20);
define("NUM_TEAMS", 20);



function gen_form($file1, $file2, $query1, $query2) {

    if(page_access_granted($_POST['access_id'], $_POST['access'])) {

	gen_html_header("Enroll Multiple Users In Class And Team:");

	printf("<form method='post' action='" . $file1 . "'>");

	printf("<table cellpadding=5><tr><th>Users</th><th>Classes</th><th>Teams</th></tr><tr><td>");

	printf("<textarea name='user_data' cols=30 rows=" . NUM_USERS . " wrap=soft></textarea>");

	printf("</td><td>");
	
	printf("<select name=class_id[] multiple size=" . NUM_CLASSES . ">");

	gen_classes($query1);

	printf("</select>");

	printf("</td><td>");

	printf("<select name=team_id[] multiple size=" . NUM_TEAMS . ">");

	gen_teams($query2);

	printf("</select>");

	printf("</td><td></tr></table>
                <input type='hidden' name='access' value='" . $_POST['access'] . "'>
                <input type='hidden' name='access_id' value='" . $_POST['access_id'] . "'>
                <input type='submit' value='Enroll'>
	        </form><br>");
	
	back_to_admin_tasks($_POST['access_id'], $_POST['access'], $file2);
	
	gen_menu_bar1("help_enroll_user_class_team_input.php");

	gen_html_footer();
    }
}

function gen_classes($query) {

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

	printf("<option value=$row[0]>$row[1]</option>");
    }

    $db->close();
    unset($db);
}

function gen_teams($query) {

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

	printf("<option value='" . $row[0] . DELIM_CHAR . $row[3] . "'>$row[1] --> $row[2]</option>");
    }

    $db->close();
    unset($db);
}

?>