<?php

// $Id: com_add_team_input.php,v 1.1 2002/10/01 17:24:15 thomas Exp $

require("common.php");
require("db_team.php");


function gen_form($file1, $file2, $query) {


    if(page_access_granted($_POST['access_id'], $_POST['access'])) {

	// Create a team object
	$db_team = new DBTeam();
    
	gen_html_header("Add a new TEAM:");

	printf("
           <form method='post' action='" . $file1 . "'>
           <table>

	    <tr>
	    <td><b><font face='arial'>Team Name: </font></b></td>
	    <td><input type=text name=team_name value='" . $_POST['team_name'] . "'></td>
	    </tr>

	    <tr>
	    <td><b><font face='arial'>Manager: </font></b></td>
	    <td><input type=text name=manager value='" . $_POST['manager'] . "'></td>
	    </tr>

	    <tr>
	    <td><b><font face='arial'>Permission: </font></b></td>
	    <td>%s</td>
	    </tr>

	    <tr>
	    <td><b><font face='arial'>Active Status: </font></b></td>
	    <td>%s</td>
	    </tr>

            <tr>
            <td><b><font face='arial'>Classes: </font><b></td>
            <td valign='top'>
            <select name=class_id size=5>", $db_team->select_permission($_POST['permission']),
	       $db_team->select_active_status($_POST['active_status']));

	get_classes($query);
    
	printf("
            </select>
            </td>
            </tr>

	    <tr>
	    <td></td>
	    <td><br><input type=submit value='Add Team'>
                    <input type=hidden name='access' value='" . $_POST['access'] . "'>
                    <input type=hidden name='access_id' value='" . $_POST['access_id'] . "'></td>
	    </tr>
            </form>
            <tr>
            <td></td>
	    <td><br><form method='post' action='" . $file2 . "'>
                    <input type=submit value='Back To Admin Tasks'>
                    <input type=hidden name='access' value='" . $_POST['access'] . "'>
                    <input type=hidden name='access_id' value='" . $_POST['access_id'] . "'></td>
                    </form>
            </td>
	    </tr>
	    </table>");
    
	gen_menu_bar1();
	gen_html_footer();

	// Cleanup memory
	unset($db_team);
    }
}

// Functions
// ---------

function get_classes($query) {

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

?>
