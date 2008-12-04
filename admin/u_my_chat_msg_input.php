<?php

// $Id: u_my_chat_msg_input.php,v 1.3 2002/07/05 20:25:50 thomas Exp $

require("common.php");
require("db_chat_team.php");
require("db_chat_group.php");


if(page_access_granted($_POST['access_id'], $_POST['access'])) {

    gen_html_header("My Chat Messages:");

    disp_simple_msg("List All Team Chat Messages:");

    display_all_team_chat_messages();

    echo("<br>");

    disp_simple_msg("List All Group Chat Messages:");

    display_all_group_chat_messages();
    
    back_to_admin_tasks($_POST['access_id'], $_POST['access'], USER_TASKS);

    gen_menu_bar1();
    
    gen_html_footer();
}


// Functions
// ---------

function display_all_group_chat_messages() {

    // Query
    // Only get group chat messages that this specific user created
    $query = "select * from rct_chat_log_groups where creator='" . $_POST['access_id'] . "'";

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


function display_all_team_chat_messages() {

    // Query
    $query = "select * from rct_chat_log_teams where creator='" . $_POST['access_id'] . "'";

    // Create a DB object
    $db = new DB();

    // Connect to the DB
    $db->connect();

    // Execute query
    $db_res = $db->exec($query);

    // Check for errors
    if (!$db_res) {
	
	disp_err_msg("ERROR: Couldn't get team chat messages from DB!");
	$db->close();
	unset($db);
	exit();
    }

    // Init vars
    $numrows = $db->get_num_rows($db_res);
    $numfields = $db->get_num_fields($db_res);

    // Check if there are any chat messages
    if(0 == $numrows) {

        disp_msg("There are not team chat messages");
	$db->close();
        unset($db);
        return;
    }
    
    printf ("<table border='1' cellpadding='5'>\n");

    printf("<tr>");

    // Printing user friendly tabel headers
    printf("<td align='center'><b><font face='arial' color=" . TB_HEAD_1 . " >Chat Message</font></b></td>");
    printf("<td align='center'><b><font face='arial' color=" . TB_HEAD_1 . " >Class Name</font></b></td>");
    printf("<td align='center'><b><font face='arial' color=" . TB_HEAD_1 . " >Team Name</font></b></td>");
    printf("<td align='center'><b><font face='arial' color=" . TB_HEAD_1 . " >Creation Date</font></b></td>");
    

    printf("</tr>\n");

    for($i = 0; $i < $numrows; $i++) {

	$myrow = $db->fetch_row ($db_res, $i);

	// Create a chat message object
	$db_tc = new DBTC();
	$db_tc->init_with_db_tuple($myrow);
	
	if($i % 2) {

	    printf("<tr bgcolor=" . TB_COLOR_1 . " >");

	    for($j = 0; $j < $numfields; $j++) {

		// Get the message
		if(TC_MSG == $j) {
		    printf ("<td style='width:200px'>%s</td>", $myrow[$j]);
		}
		// Get the class name
		elseif(TC_CLASS_ID == $j) {
		    printf("<td>%s</td>", $db_tc->get_class_name());
		}
		// Get the team name
		elseif(TC_TEAM_ID == $j) {
		    printf("<td>%s</td>", $db_tc->get_team_name());
		}
		elseif(TC_DATE == $j) {
		    printf ("<td>%s</td>", $myrow[$j]);
		}
	    }

	    printf("</tr>\n");

	}
	else {

	    printf("<tr bgcolor=" . TB_COLOR_2 . " >");

	    for($j = 0; $j < $numfields; $j++) {

		// Get the message
		if(TC_MSG == $j) {
		    printf ("<td style='width:200px'>%s</td>", $myrow[$j]);
		}
		// Get the class name
		elseif(TC_CLASS_ID == $j) {
		    printf("<td>%s</td>", $db_tc->get_class_name());
		}
		// Get the team name
		elseif(TC_TEAM_ID == $j) {
		    printf("<td>%s</td>", $db_tc->get_team_name());
		}
		elseif(TC_DATE == $j) {
		    printf ("<td>%s</td>", $myrow[$j]);
		}
	    }

	    printf("</tr>\n");
	}

	unset($db_tc);
    }

    printf ("</table>\n");

    $db->close();
    unset($db);
}

?>
	      
