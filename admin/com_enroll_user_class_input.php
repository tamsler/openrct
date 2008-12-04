<?php

// $Id: com_enroll_user_class_input.php,v 1.2 2002/10/31 18:51:47 thomas Exp $

require("common.php");

define("NUM_CLASSES", 20);
define("NUM_USERS", 20);

function gen_form($file1, $file2, $query) {

    if(page_access_granted($_POST['access_id'], $_POST['access'])) {

	gen_html_header("Enroll Users In a Class:");

	printf("
            <form method='post' action='" . $file1 . "'>
            <table>

            <tr>
            <th bgcolor='#FDF5E6'><font face='arial'>Users</font></th>
            <th></th>
            <th bgcolor='#F0F8FF'><font face='arial'>Classes</font></th>
            </tr>

            <tr>
            <td>
            <select name=user_id[] multiple size=" . NUM_USERS . ">");

	get_users();

	printf("</select>
            </td>
            <td>
            &nbsp;
            <input type=submit value=' Enroll >> '>
	    <input type=hidden name='access' value='" . $_POST['access'] . "'>
            <input type=hidden name='access_id' value='" . $_POST['access_id'] . "'>
            &nbsp;
            </td>
            <td>
            <select name=class_id size=" . NUM_CLASSES . ">");

	get_classes($query);

	printf("</select>
            </td>
            </tr>
            </table>
            </form>

            <br><br>
            <form method='post' action='" . $file2 . "'>
            <input type=submit value='Back To Admin Tasks'>
	    <input type=hidden name='access' value='" . $_POST['access'] . "'>
            <input type=hidden name='access_id' value='" . $_POST['access_id'] . "'>
            </form>");

	gen_menu_bar1();
    
	gen_html_footer();
    }
}

// Functions
// ---------

function get_users() {

    // Query
    $query = "select user_id, alias from rct_users order by alias asc;";
 
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