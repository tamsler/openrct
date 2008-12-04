<?php

// $Id: com_class_chat_msg_selection.php,v 1.1 2002/11/04 20:50:26 thomas Exp $

require("common.php");

function gen_form($file1, $file2, $file3, $query, $assembly_type) {

    // Note: obj_id is class_id
    if(page_access_granted($_POST['access_id'], $_POST['access'])) {

	gen_html_header("Class Chat Messages:");

	printf("
            <form method='post' action='" . $file1 . "?assembly_type=" . $assembly_type . "'>
            <table>

            <tr>
            <th bgcolor=" . TB_COLOR_2. " >Classes</th>
            </tr>

            <tr>
            <td>
            <select name=obj_id size=10>");

	get_all_classes($query);

	printf("</select>
            </td>
            <td>
            &nbsp;
            <input type=submit value='List Chat Messages For Selected Class'>
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

function get_all_classes($query) {

    // Query
    $query = "select class_id, class_name from rct_classes";
    
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
	      
