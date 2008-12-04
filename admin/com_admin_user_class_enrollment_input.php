<?php

// $Id: com_admin_user_class_enrollment_input.php,v 1.3 2002/11/04 23:06:20 thomas Exp $

require("common.php");
require("db_class.php");

function gen_form($file1, $file2, $file3, $query) {

    if(page_access_granted($_POST['access_id'], $_POST['access'])) {

	gen_html_header("Classes For Selected Person:");

	// Check if a user was selected
	if(!$_POST['user_id']) {

	    disp_err_msg("ERROR: You didn't select a USER!");

	    go_back($file2);

	    gen_menu_bar1();
    
	    gen_html_footer();
	}
	else {

	    // Getting the user's alias
	    $db_user_sel = new DBUser();
	    $db_user_sel->init_with_user_id($_POST['user_id']);
    
	    disp_simple_msg("[ " . $db_user_sel->get_alias(). " ] is Enrolled In These Classes:");
    
	    display_user_classes($file1, $query);
    
	    back_to_admin_tasks($_POST['access_id'], $_POST['access'], $file3);

	    gen_menu_bar1();
    
	    gen_html_footer();

	    unset($db_user_sel);
	}
    }
}

// Functions
// ---------

function display_user_classes($file, $query) {

    // Create a DB object
    $db = new DB();

    // Connect to the DB
    $db->connect();

    // Execute query
    $db_res = $db->exec($query);

    // Check for errors
    if (!$db_res) {
	
	disp_err_msg("ERROR: Couldn't get class enrollment from DB!");
	exit();
    }

    // Init vars
    $numrows = $db->get_num_rows($db_res);

    // Check if there are any classes
    if(0 == $numrows) {

        disp_msg("The selected user is not enrolled in any classes.");
	$db->close();
        unset($db);
        return;
    }

    printf("<table border='1' cellpadding='5'>");

    printf("<tr>");

    printf("<td align='center'><b><font face='arial' color=" . TB_HEAD_1 . " >Classes</font></b></td>");

    printf("<td align='center'><b><font face='arial' color=" . TB_HEAD_1 . " >Unenroll</font></b></td>");
    
    printf("</tr>");

    for($i = 0; $i < $numrows; $i++) {

	$myrow = $db->fetch_row ($db_res, $i);

	// Create a class object
	$db_class = new DBClass();
	$db_class->init_with_class_id($myrow[0]);
	
	if($i % 2) {

	    printf("<tr bgcolor=" . TB_COLOR_1 . " >");

	    printf("<td>%s</td>", $db_class->get_name());

	    gen_user_class_delete($db_class->get_id(), $file);

	    printf("</tr>");
	}
	else {

	    printf("<tr bgcolor=" . TB_COLOR_2 . " >");

	    printf("<td>%s</td>", $db_class->get_name());

	    gen_user_class_delete($db_class->get_id(), $file);

	    printf("</tr>");
	}

	// Free memory
	unset($db_class);
	
    } // End For Loop

    printf ("</table><br><br>");

    $db->close();
    unset($db);
}

function gen_user_class_delete($class_id, $file) {

    printf("<td>
	    <form method='post' action='" . $file . "'>
            <input type=hidden name=user_id value='" . $_POST['user_id'] . "'>
	    <input type=hidden name=class_id value='$class_id'>
	    <input type=hidden name=access value='" . $_POST['access'] . "'>
	    <input type=hidden name=access_id value='" . $_POST['access_id'] . "'>
	    <input type=submit value='Unenroll'>
	    </form>
	    </td>");
}

function go_back($file) {

    printf("<form method='post' action='" . $file . "'>
	    <input type=hidden name=access value='" . $_POST['access'] . "'>
	    <input type=hidden name=access_id value='" . $_POST['access_id'] . "'>
	    <input type=submit value='Go Back'>
	    </form>");
}

?>