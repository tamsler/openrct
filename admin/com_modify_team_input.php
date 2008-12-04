<?php

// $Id: com_modify_team_input.php,v 1.1 2002/10/02 22:50:16 thomas Exp $

require("common.php");
require("db_team.php");

function gen_form($file1, $file2) {

    if(page_access_granted($_POST['access_id'], $_POST['access'])) {

	// Creat a team object
	$db_team = new DBTeam();
    
	// Remember the original team name
	$team_name_orig = $_POST['team_name'];
    
	gen_html_header("Modify Team:");

	printf("
           <form method='post' action='" . $file1 . "'>
           <table>

	    <tr>
	    <td><b><font face='arial'>Team Name: </font></b></td>
	    <td><input type=text name=team_name value='" . $_POST['team_name'] . "'></td>
	    </tr>

	    <tr>
	    <td><b><font face='arial'>Manager: </font></b></td>
	    <td><input type=text name=manager value='%s'></td>
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
	    <td></td>
	    <td><br><input type=submit value='Modify Team'>
                    <input type=hidden name='team_name_orig' value='%s'>
                    <input type=hidden name='team_id' value='" . $_POST['team_id'] . "'>
                    <input type=hidden name='access' value='" . $_POST['access'] . "'>
                    <input type=hidden name='access_id' value='" . $_POST['access_id'] . "'></td>
	    </tr>
            </form>
            <tr>
            <td></td>
	    <td><br><form method='post' action='" . $file2 . "'>
                    <input type=submit value='Back To Admin Tasks'>
	            <input type=hidden name='access' value='" . $_POST['access'] . "'>
                    <input type=hidden name='access_id' value='" . $_POST['access_id']. "'>
                    </form>
            </td>
	    </tr>
	    </table>", ($_POST['manager'] ? get_manager_alias($_POST['manager']) : ""),
	       $db_team->select_permission($_POST['permission']),
	       $db_team->select_active_status($_POST['active_status']),
	       $team_name_orig);
	gen_menu_bar1();
	gen_html_footer();

	// Cleanup memory
	unset($db_team);
    }
}


// Functions
// ---------


function get_manager_alias($a_manager) {

    if(0 != strcmp($a_manager[0], "U")) {

	return $a_manager;
    }
    else {
	
	// Create a team object
	$db_team = new DBTeam();
    
	$db_team->set_manager_alias($a_manager);

	$manager_alias = $db_team->get_manager_alias();

	unset($db_team);

	return $manager_alias;
    }
}

?>
