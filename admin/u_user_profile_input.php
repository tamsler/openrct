<?php

// $Id: u_user_profile_input.php,v 1.4 2002/05/17 22:31:56 thomas Exp $

require("common.php");

define("NUM_FIELDS", 4);

if(page_access_granted($_POST['access_id'], $_POST['access'])) {

    gen_html_header("USER: My User Profile");

    // Query
    $query = "select * from rct_users where user_id='" . $_POST['access_id'] . "'";

    // Create a DB object
    $db = new DB();

    // Connect to the DB
    $db->connect();

    // Execute query
    $db_res = $db->exec($query);

    // Check for errors
    // We have to make sure that we only get one tuple
    if (!$db->one_tuple($db_res)) {
	
	disp_err_msg("ERROR: Could not get User from DB!");
	$db->close();
        unset($db);
	exit();
    }

    printf ("<table border='1' cellpadding='5'>\n");

    printf("<tr>");

    // Adding the table headers. We only show the first_name, the last_name, and the password field 
    printf("<td align='center'><b><font face='arial' color=" . TB_HEAD_1 . " >First Name</font></b></td>");
    printf("<td align='center'><b><font face='arial' color=" . TB_HEAD_1 . " >Last Name</font></b></td>");
    printf("<td align='center'><b><font face='arial' color=" . TB_HEAD_1 . " >Password</font></b></td>");
    print("<td align='center'><b><font face='arial' color=" . TB_HEAD_1 . " >Modify</font></b></td>");
    
    printf("</tr>\n");

    $myrow = $db->fetch_row ($db_res, $i);

    printf("<tr bgcolor=" . TB_COLOR_1 . ">");

    // Only showfirst_name, last_name, and password
    printf ("<td>%s</td>", $myrow[USER_FIRST_NAME]);
    printf ("<td>%s</td>", $myrow[USER_LAST_NAME]);
    printf ("<td>%s</td>", $myrow[USER_PASSWORD]);

    // Show the modify button
    gen_modify($myrow);

    printf("</tr>\n");

    printf ("</table>\n");

    back_to_admin_tasks($_POST['access_id'], $_POST['access'], USER_TASKS);
    
    gen_menu_bar1();

    gen_html_footer();

    $db->close();
    unset($db);
}


// Functions
// ---------

function gen_modify($row) {
    
    printf("<td>
            <form method='post' action='u_modify_user_input.php'>
            <input type=hidden name=user_id value='" . $row[USER_ID] . "'>
	    <input type=hidden name=first_name value='" . $row[USER_FIRST_NAME] . "'>
	    <input type=hidden name=last_name value='" . $row[USER_LAST_NAME] . "'>
	    <input type=hidden name=access value='" . $_POST['access'] . "'>
	    <input type=hidden name=access_id value='" . $_POST['access_id'] . "'>
	    <input type=submit value='Modify'>
	    </form>
	    </td>");
}

?>

