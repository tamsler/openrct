<?php

// $Id: com_team_chat_msg_selection.php,v 1.1 2002/11/04 21:19:18 thomas Exp $

require("common.php");


function gen_form($file1, $file2, $file3, $query) {

    // Note: obj_id is team_id
    if(page_access_granted($_POST['access_id'], $_POST['access'])) {

	gen_html_header("Team Chat Messages:");

	printf("
            <form method='post' action='" . $file1 . "'>
            <table>

            <tr>
            <th bgcolor=" . TB_COLOR_2 . " >Teams</th>
            </tr>

            <tr>
            <td>
            <select name=obj_id size=10>");

	get_all_teams($query);

	printf("</select>
            </td>
            <td>
            &nbsp;
            <input type=submit value='List Chat Messages For Selected Team'>
            <input type=hidden name=form_name value='" . $file2 . "'>
	    <input type=hidden name='access' value='" . $_POST['access'] . "'>
            <input type=hidden name='access_id' value='" . $_POST['access_id'] . "'>
            &nbsp;
            </td>
            </tr>
            </table>
            </form>");

	back_to_admin_tasks($_POST['access_id'], $_POST['access'], $file3);

	gen_menu_bar1();
    
	gen_html_footer();
    }
}

// Functions
// ---------

function get_all_teams($query) {
    
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
	      
