<?php

// $Id: com_all_chat_msg_input.php,v 1.4 2002/11/04 18:20:07 thomas Exp $

require("common.php");
require("db_chat_team.php");
require("db_chat_group.php");

// $assembly_type indicates if we show team, group, or team and group data
// 1 = TEAM
// 2 = GROUP
// 3 = TEAM and GROUP


// Test if the the page is calling itself
if(0 == strcmp($_POST['access_more'], basename(__FILE__))) {

    gen_form($_POST['file1'], $_POST['file2'], $_POST['file3'], $_POST['file4'],
             $_POST['query1'], $_POST['query2'], $_POST['assembly_type'], $_POST['offset']);
}


function gen_form($file1, $file2, $file3, $file4, $query1, $query2, $assembly_type, $offset) {

    // Adding offset to queries
    $query1 = stripslashes($query1);
    $query2 = stripslashes($query2);
    
    $tmp_query1 = $query1 . " offset " . $offset;
    $tmp_query2 = $query2 . " offset " . $offset;

    if(page_access_granted($_POST['access_id'], $_POST['access'])) {

	gen_html_header("All Chat Messages:");

	if(TEAM == $assembly_type || TEAM_AND_GROUP == $assembly_type) {

	    disp_simple_msg("List Of All Team Chat Messages:");

	    display_all_team_chat_messages($file1, $file3, $tmp_query1);

	    gen_next_previous($file1, $file2, $file3, $file4, $query1, $query2, $assembly_type, $offset );

	    echo("<br>");
	}

	if(GROUP == $assembly_type || TEAM_AND_GROUP == $assembly_type) {
	
	    disp_simple_msg("List Of All Group Chat Messages:");

	    display_all_group_chat_messages($file2, $file3, $tmp_query2);

	    gen_next_previous($file1, $file2, $file3, $file4, $query1, $query2, $assembly_type, $offset);
	}
    
	back_to_admin_tasks($_POST['access_id'], $_POST['access'], $file4);

	gen_menu_bar1();
    
	gen_html_footer();
    }
}

// Functions
// ---------

function display_all_group_chat_messages($file1, $file2, $query) {

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

        disp_msg("There are no group chat messages");
	$db->close();
        unset($db);
        return;
    }
    
    printf ("<table border='1' cellpadding='5'>\n");

    printf("<tr>");

    for($i = 0; $i < $numfields; $i++) {

	printf("<td align='center'><b><font face='arial' color=" . TB_HEAD_1 . " >" . $db->get_field_name($db_res, $i) . "</font></b></td>");
    }

    print("<td align='center'><b><font face='arial' color=" . TB_HEAD_1 . " >Delete</font></b></td>");
    printf("</tr>\n");

    for($i = 0; $i < $numrows; $i++) {

	$myrow = $db->fetch_row ($db_res, $i);

	// Create a chat message object
	$db_gc = new DBGC();
	$db_gc->init_with_db_tuple($myrow);
	
	if($i % 2) {

	    printf("<tr bgcolor=" . TB_COLOR_1 . " >");

	    for($j = 0; $j < $numfields; $j++) {

		if(GC_CREATOR == $j) {
		    printf("<td>%s</td>", $db_gc->get_creator_alias());
		}
		elseif(GC_CLASS_ID == $j) {
		    printf("<td>%s</td>", $db_gc->get_class_name());
		}
		elseif(GC_GROUP_ID == $j) {
		    printf("<td>%s</td>", $db_gc->get_group_name());
		}
		elseif(GC_MSG == $j) {
		    printf ("<td style='width:200px'>%s</td>", $myrow[$j]);
		}
		else {
		    printf ("<td>%s</td>", $myrow[$j]);
		}
	    }

	    gen_chat_msg_delete($myrow, $file1, $file2);

	    printf("</tr>\n");
	}
	else {

	    printf("<tr bgcolor=" . TB_COLOR_2 . " >");

	    for($j = 0; $j < $numfields; $j++) {

		if(GC_CREATOR == $j) {
		    printf("<td>%s</td>", $db_gc->get_creator_alias());
		}
		elseif(GC_CLASS_ID == $j) {
		    printf("<td>%s</td>", $db_gc->get_class_name());
		}
		elseif(GC_GROUP_ID == $j) {
		    printf("<td>%s</td>", $db_gc->get_group_name());
		}
		elseif(GC_MSG == $j) {
		    printf ("<td style='width:200px'>%s</td>", $myrow[$j]);
		}
		else {
		    printf ("<td>%s</td>", $myrow[$j]);
		}
	    }

	    gen_chat_msg_delete($myrow, $file1, $file2);

	    printf("</tr>\n");
	}

	unset($db_gc);
    }

    printf ("</table>");

    $db->close();
    unset($db);
}


function display_all_team_chat_messages($file1, $file2, $query) {

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

    for($i = 0; $i < $numfields; $i++) {

	printf("<td align='center'><b><font face='arial' color=" . TB_HEAD_1 . " >" . $db->get_field_name($db_res, $i) . "</font></b></td>");
    }

    print("<td align='center'><b><font face='arial' color=" . TB_HEAD_1 . " >Delete</font></b></td>");

    printf("</tr>\n");

    for($i = 0; $i < $numrows; $i++) {

	$myrow = $db->fetch_row ($db_res, $i);

	// Create a chat message object
	$db_tc = new DBTC();
	$db_tc->init_with_db_tuple($myrow);
	
	if($i % 2) {

	    printf("<tr bgcolor=" . TB_COLOR_1 . " >");

	    for($j = 0; $j < $numfields; $j++) {

		if(TC_CREATOR == $j) {
		    printf("<td>%s</td>", $db_tc->get_creator_alias());
		}
		elseif(TC_CLASS_ID == $j) {
		    printf("<td>%s</td>", $db_tc->get_class_name());
		}
		elseif(TC_TEAM_ID == $j) {
		    printf("<td>%s</td>", $db_tc->get_team_name());
		}
		elseif(TC_MSG == $j) {
		    printf ("<td style='width:200px'>%s</td>", $myrow[$j]);
		}
		else {
		    printf ("<td>%s</td>", $myrow[$j]);
		}
	    }

	    gen_chat_msg_delete($myrow, $file1, $file2);

	    printf("</tr>\n");

	}
	else {

	    printf("<tr bgcolor=" . TB_COLOR_2 . " >");

	    for($j = 0; $j < $numfields; $j++) {

		if(TC_CREATOR == $j) {
		    printf("<td>%s</td>", $db_tc->get_creator_alias());
		}
		elseif(TC_CLASS_ID == $j) {
		    printf("<td>%s</td>", $db_tc->get_class_name());
		}
		elseif(TC_TEAM_ID == $j) {
		    printf("<td>%s</td>", $db_tc->get_team_name());
		}
		elseif(TC_MSG == $j) {
		    printf ("<td style='width:200px'>%s</td>", $myrow[$j]);
		}
		else {
		    printf ("<td>%s</td>", $myrow[$j]);
		}
	    }

	    gen_chat_msg_delete($myrow, $file1, $file2);

	    printf("</tr>\n");
	}

	unset($db_tc);
    }

    printf ("</table>");

    $db->close();
    unset($db);
}

function gen_next_previous($file1, $file2, $file3, $file4, $query1, $query2, $assembly_type, $offset) {

    // Create a DB object
    $db = new DB();
    // Connect to the DB
    $db->connect();

    // Get total number of messages
    $count_query = "";
    if(TEAM == $assembly_type || TEAM_AND_GROUP == $assembly_type) {
	$count_query = "select count(chat_id) from rct_chat_log_teams";
    }
    else {
	$count_query = "select count(chat_id) from rct_chat_log_groups";
    }

    // Execute query
    $db_res = $db->exec($count_query);
    $myrow = $db->fetch_row($db_res, 0);

    // Assign total number of messages
    $total_num_msg = $myrow[0];

    printf("<br><table><tr>");
    
    // Error check offset bounds
    if(0 < $offset) {

	$prev_offset = $offset;
	
	if(0 >= $offset - RCT_LIMIT) {
	    $prev_offset = RCT_OFFSET;
	}
	else {
	    $prev_offset = $offset - RCT_LIMIT;
	}

	printf("<td>
           <form method='post' action='" . basename(__FILE__) . "'>
           <input type='hidden' name='access' value='" . $_POST['access'] . "'>
           <input type='hidden' name='access_id' value='" . $_POST['access_id'] . "'>
           <input type='hidden' name='access_more' value='" . basename(__FILE__) . "'>
           <input type='hidden' name='file1' value='" . $file1 . "'>
           <input type='hidden' name='file2' value='" . $file2 . "'>
           <input type='hidden' name='file3' value='" . $file3 . "'>
           <input type='hidden' name='file4' value='" . $file4 . "'>
           <input type='hidden' name='query1' value='" . htmlentities($query1, ENT_QUOTES) . "'>
           <input type='hidden' name='query2' value='" . htmlentities($query2, ENT_QUOTES) . "'>
           <input type='hidden' name='assembly_type' value='" . $assembly_type . "'>
           <input type='hidden' name='offset' value=" . $prev_offset . ">
           <input type='submit' value='<< Previous " . RCT_LIMIT . " Messages'>
           </form>
           </td>");
    }
    if($total_num_msg > $offset + RCT_LIMIT) {

	$next_offset = $offset + RCT_LIMIT;

	printf("<td>
           <form method='post' action='" . basename(__FILE__) . "'>
           <input type='hidden' name='access' value='" . $_POST['access'] . "'>
           <input type='hidden' name='access_id' value='" . $_POST['access_id'] . "'>
           <input type='hidden' name='access_more' value='" . basename(__FILE__) . "'>
           <input type='hidden' name='file1' value='" . $file1 . "'>
           <input type='hidden' name='file2' value='" . $file2 . "'>
           <input type='hidden' name='file3' value='" . $file3 . "'>
           <input type='hidden' name='file4' value='" . $file4 . "'>
           <input type='hidden' name='query1' value='" . htmlentities($query1, ENT_QUOTES) . "'>
           <input type='hidden' name='query2' value='" . htmlentities($query2, ENT_QUOTES) . "'>
           <input type='hidden' name='assembly_type' value='" . $assembly_type . "'>
           <input type='hidden' name='offset' value=" . $next_offset . ">
           <input type='submit' value='Next " . RCT_LIMIT . " Messages >>'>
           </form>
           </td>");
    }
    
    printf("</tr></table>");

    $db->close();
    unset($db);
}

function gen_chat_msg_delete($row, $file1, $file2) {

    printf("<td>
	    <form method='post' action='" . $file1 . "'>
            <input type='hidden' name=form_name value='" . $file2 . "'>
            <input type='hidden' name=chat_id value='" . $row[0] . "'>
	    <input type='hidden' name=access value='" . $_POST['access'] . "'>
	    <input type='hidden' name=access_id value='" . $_POST['access_id'] . "'>
	    <input type='submit' value='Delete'>
	    </form>
	    </td>");
}

?>
