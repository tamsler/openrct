<?php

// $Id: u_group_chat_msg_input.php,v 1.1 2002/07/05 20:21:22 thomas Exp $

require("common.php");
require("db_chat_group.php");


if(page_access_granted($_POST['access_id'], $_POST['access'])) {

    gen_html_header("GroupChat Messages:");

    // Check if a group was selected
    // Note: obj_id is group_id
    if(!$_POST['obj_id']) {

	disp_err_msg("ERROR: You didn't select a GROUP!");

	go_back();

	gen_menu_bar1();
    
	gen_html_footer();
	
	exit();
    }

    disp_simple_msg("List Group Chat Messages:");

    display_group_chat_messages();
    
    back_to_admin_tasks($_POST['access_id'], $_POST['access'], "user_tasks.php");

    gen_menu_bar1();
    
    gen_html_footer();
}


// Functions
// ---------

function display_group_chat_messages() {

// Query
    $query = "select * from rct_chat_log_groups where
              group_id='" . $_POST['obj_id'] . "' and
              creator='" . $_POST['access_id'] . "'";

    // Create a DB object
    $db = new DB();

    // Connect to the DB
    $db->connect();

    // Execute query
    $db_res = $db->exec($query);

    // Check for errors
    if (!$db_res) {
	
	disp_err_msg("ERROR: Couldn't get group chat messages from DB!");
	$db->close();
	unset($db);
	exit();
    }

    // Init vars
    $numrows = $db->get_num_rows($db_res);
    $numfields = $db->get_num_fields($db_res);

    // Check if there are any chat messages
    if(0 == $numrows) {

        disp_msg("There are not group chat messages");
	$db->close();
        unset($db);
        return;
    }
    
    printf ("<table border='1' cellpadding='5'>\n");

    printf("<tr>");

    // Printing user friendly tabel headers
    printf("<td align='center'><b><font face='arial' color=" . TB_HEAD_1 . " >Chat Message</font></b></td>");
    printf("<td align='center'><b><font face='arial' color=" . TB_HEAD_1 . " >Class Name</font></b></td>");
    printf("<td align='center'><b><font face='arial' color=" . TB_HEAD_1 . " >Group Name</font></b></td>");
    printf("<td align='center'><b><font face='arial' color=" . TB_HEAD_1 . " >Creation Date</font></b></td>");
    
    printf("</tr>\n");

    for($i = 0; $i < $numrows; $i++) {

	$myrow = $db->fetch_row ($db_res, $i);

	// Create a chat message object
	$db_gc = new DBGC();
	$db_gc->init_with_db_tuple($myrow);
	
	if($i % 2) {

	    printf("<tr bgcolor=" . TB_COLOR_1 . " >");

	    for($j = 0; $j < $numfields; $j++) {

		// Get the message
		if(GC_MSG == $j) {

		    printf ("<td style='width:200px'>%s</td>", $myrow[$j]);
		}
		// Get the class name
		elseif(GC_CLASS_ID == $j) {

		    printf("<td>%s</td>", $db_gc->get_class_name());
		}
		// Get the group name
		elseif(GC_GROUP_ID == $j) {
		    printf("<td>%s</td>", $db_gc->get_group_name());
		}
		// Get the date
		elseif(GC_DATE == $j) {
		    printf ("<td>%s</td>", $myrow[$j]);
		}
	    }

	    printf("</tr>\n");
	}
	else {

	    printf("<tr bgcolor=" . TB_COLOR_2 . " >");

	    for($j = 0; $j < $numfields; $j++) {

		// Get the message
		if(GC_MSG == $j) {

		    printf ("<td style='width:200px'>%s</td>", $myrow[$j]);
		}
		// Get the class name
		elseif(GC_CLASS_ID == $j) {

		    printf("<td>%s</td>", $db_gc->get_class_name());
		}
		// Get the group name
		elseif(GC_GROUP_ID == $j) {
		    printf("<td>%s</td>", $db_gc->get_group_name());
		}
		// Get the date
		elseif(GC_DATE == $j) {
		    printf ("<td>%s</td>", $myrow[$j]);
		}
	    }

	    printf("</tr>\n");
	}

	unset($db_gc);
    }

    printf ("</table>\n");

    $db->close();
    unset($db);
}


function go_back() {

    printf("<form method='post' action='u_group_chat_msg_selection.php'>
	    <input type=hidden name=access value='" . $_POST['access'] . "'>
	    <input type=hidden name=access_id value='" . $_POST['access_id'] . "'>
	    <input type=submit value='Go Back'>
	    </form>");
}

?>
	      
