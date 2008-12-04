<?php

// $Id: com_admin_user_team_enrollment_selection.php,v 1.1 2002/10/31 21:54:57 thomas Exp $

require("common.php");

function gen_form($file1, $file2, $query) {

    if(page_access_granted($_POST['access_id'], $_POST['access'])) {

	gen_html_header("User --> Team Enrollment:");

	printf("
            <form method='post' action='" . $file1 . "'>
            <table>

            <tr>
            <th bgcolor=". TB_COLOR_2 . " ><font face='arial'>Users</font></th>
            </tr>

            <tr>
            <td>
            <select name=user_id size=10>");

	get_all_users($query);

	printf("</select>
            </td>
            <td>
            &nbsp;
            <input type=submit value='List Team Enrollment\nFor Selected User'>
	    <input type=hidden name='access' value='" . $_POST['access'] . "'>
            <input type=hidden name='access_id' value='" . $_POST['access_id'] . "'>
            &nbsp;
            </td>
            </tr>
            </table>
            </form>");

	back_to_admin_tasks($_POST['access_id'], $_POST['access'], $file2);

	gen_menu_bar1();
    
	gen_html_footer();
    }
}

// Functions
// ---------

function get_all_users($query) {
    
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
	      
