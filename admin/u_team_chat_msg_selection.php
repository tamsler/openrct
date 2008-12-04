<?php

// $Id: u_team_chat_msg_selection.php,v 1.1 2002/07/05 20:21:22 thomas Exp $

require("common.php");


// Note: obj_id is team_id
if(page_access_granted($_POST['access_id'], $_POST['access'])) {

    gen_html_header("Team Chat Messages:");

    printf("
            <form method='post' action='u_team_chat_msg_input.php'>
            <table>

            <tr>
            <th bgcolor=" . TB_COLOR_2 . " >Teams</th>
            </tr>

            <tr>
            <td>
            <select name=obj_id size=10>");

    get_all_teams();

    printf("</select>
            </td>
            <td>
            &nbsp;
            <input type=submit value='List Chat Messages For Selected Team'>
            <input type=hidden name=form_name value='u_team_chat_msg_selection.php'>
	    <input type=hidden name='access' value='" . $_POST['access'] . "'>
            <input type=hidden name='access_id' value='" . $_POST['access_id'] . "'>
            &nbsp;
            </td>
            </tr>
            </table>
            </form>");

    back_to_admin_tasks($_POST['access_id'], $_POST['access'], "user_tasks.php");

    gen_menu_bar1();
    
    gen_html_footer();
}


// Functions
// ---------

function get_all_teams() {

    // Query
    $query = "select distinct T.team_id, T.team_name from
              rct_teams T, rct_member_team M where
              T.team_id=M.team_id and
              M.user_id='" . $_POST['access_id'] . "'";
    
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
	      
