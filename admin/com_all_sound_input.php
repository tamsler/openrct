<?php

// $Id: com_all_sound_input.php,v 1.1 2002/11/18 22:59:45 thomas Exp $

require("common.php");

// Test if the the page is calling itself
if(0 == strcmp($_POST['access_more'], basename(__FILE__))) {

    gen_form($_POST['file1'], $_POST['file2'], $_POST['assembly_type'],
             $_POST['person_access'], $_POST['offset']);
}


function gen_form($file1, $file2, $assembly_type, $person_access, $offset) {

    if(page_access_granted($_POST['access_id'], $_POST['access'])) {

	gen_html_header("All Sound Messages:");

	if(TEAM == $assembly_type || TEAM_AND_GROUP == $assembly_type) {

	    disp_simple_msg("List Of All Team Sound Messages:");

	    display_all_sound_messages($file1, TEAM, $person_access, $offset);
	    
	    gen_next_previous($file1, $file2, $assembly_type, $person_access, $offset);

	    echo("<br>");
	}

	if(GROUP == $assembly_type || TEAM_AND_GROUP == $assembly_type) {
	
	    disp_simple_msg("List Of All Group Sound Messages:");

	    display_all_sound_messages($file1, GROUP, $person_access, $offset);

	    gen_next_previous($file1, $file2, $assembly_type, $person_access, $offset);
	}
    
	back_to_admin_tasks($_POST['access_id'], $_POST['access'], $file2);

	gen_menu_bar1();
    
	gen_html_footer();
    }
}

function display_all_sound_messages($file1, $assembly_type, $person_access, $offset) {


    $query = "";
    
    if(TEAM == $assembly_type &&
       ADMIN_ACCESS == $person_access) {

	$query = "select * from rct_team_sounds_view
                  order by \"Date\" asc
                  limit " . RCT_LIMIT . " offset " . $offset;
    }
    elseif(TEAM == $assembly_type &&
	   MANAGER_ACCESS == $person_access) {
	
	$query = "select TSV.* from rct_team_sounds_view TSV, rct_classes C
                  where C.class_name=TSV.\"Class Name\" and
                  C.manager='" . $_POST['access_id'] . "' order by TSV.\"Date\" asc
                  limit " . RCT_LIMIT . " offset " . $offset;
    }
    elseif(GROUP == $assembly_type &&
	   ADMIN_ACCESS == $person_access) {

	$query = "select * from rct_group_sounds_view
                  order by \"Date\" asc
                  limit " . RCT_LIMIT . " offset " . $offset;
    }
    elseif(GROUP == $assembly_type &&
	   MANAGER_ACCESS == $person_access) {

	$query = "select GSV.* from rct_group_sounds_view GSV, rct_classes C
                  where C.class_name=GSV.\"Class Name\" and
                  C.manager='" . $_POST['access_id'] . "' order by GSV.\"Date\" asc
                  limit " . RCT_LIMIT . " offset " . $offset;
    }
    
    // Create a DB object
    $db = new DB();

    // Connect to the DB
    $db->connect();

    // Execute query
    $db_res = $db->exec($query);

    // Check for errors
    if(!$db_res) {
	
	disp_err_msg("ERROR: Couldn't get sound messages from DB!");
	$db->close();
	unset($db);
	return;
    }

    // Init vars
    $numrows = $db->get_num_rows($db_res);
    $numfields = $db->get_num_fields($db_res);

    // Check if there were zero tuples
    if(0 == $numrows) {

        disp_msg("There are no sound messages.");
	$db->close();
        unset($db);
        return;
    }

    printf ("<table border='1' cellpadding='5'>");

    printf("<tr>");

    for($i = 0; $i < $numfields; $i++) {

	printf("<td align='center'><b><font face='arial' color=" . TB_HEAD_1 . " >" . $db->get_field_name($db_res, $i) . "</font></b></td>");
    }

    print("<td align='center'><b><font face='arial' color=" . TB_HEAD_1 . " >Delete</font></b></td>");
    printf("</tr>");

    for($i = 0; $i < $numrows; $i++) {

	$myrow = $db->fetch_row ($db_res, $i);

	if($i % 2) {

	    printf("<tr bgcolor=" . TB_COLOR_1 . " >");

	    for($j = 0; $j < $numfields; $j++) {

		printf("<td>%s</td>", $myrow[$j]);
	    }

	    // Compose the path and file name for deletion
	    $sound_file = $myrow[3] . "/" . $myrow[2];
	    
	    gen_sound_delete($myrow[0], $sound_file, $file1, $person_access, $assembly_type);
	    
	    printf("</tr>");
	}
	else {

	    printf("<tr bgcolor=" . TB_COLOR_2 . " >");

	    for($k = 0; $k < $numfields; $k++) {
	    
		printf("<td>%s</td>", $myrow[$k]);
	    }
	    
	    // Compose the path and file name for deletion
	    $sound_file = $myrow[3] . "/" . $myrow[2];

	    gen_sound_delete($myrow[0], $sound_file, $file1, $person_access, $assembly_type);
	    
	    printf("</tr>");
	}
    }

    printf("</table>");

    $db->close();
    unset($db);
}

function gen_sound_delete($snd_id, $sound_file, $file, $person_access, $assembly_type) {

    printf("<td>");

    if(ADMIN_ACCESS == $person_access) {
	printf("<form method='post' action='delete_sound_verify.php'>");
    }
    elseif(MANAGER_ACCESS == $person_access) {
	printf("<form method='post' action='m_delete_sound_verify.php'>");
    }

    printf("<input type='hidden' name=form_name value='" . $file . "'>
            <input type='hidden' name=snd_id value='" . $snd_id . "'>
            <input type='hidden' name=snd_file value='" . $sound_file . "'>
            <input type='hidden' name=assembly_type value=" . $assembly_type . ">
	    <input type='hidden' name=access value='" . $_POST['access'] . "'>
	    <input type='hidden' name=access_id value='" . $_POST['access_id'] . "'>
	    <input type='submit' value='Delete'>
	    </form>
	    </td>");
}


function gen_next_previous($file1, $file2, $assembly_type, $person_access, $offset) {

    // Create a DB object
    $db = new DB();
    // Connect to the DB
    $db->connect();

    // Get total number of messages
    $count_query = "";
    if(TEAM == $assembly_type || TEAM_AND_GROUP == $assembly_type) {
	$count_query = "select count(\"ID\") from rct_team_sounds_view";
    }
    else {
	$count_query = "select count(\"ID\") from rct_group_sounds_view";
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
           <input type='hidden' name='person_access' value=" . $person_access . ">
           <input type='hidden' name='assembly_type' value=" . $assembly_type . ">
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
           <input type='hidden' name='person_access' value=" . $person_access . ">
           <input type='hidden' name='assembly_type' value=" . $assembly_type . ">
           <input type='hidden' name='offset' value=" . $next_offset . ">
           <input type='submit' value='Next " . RCT_LIMIT . " Messages >>'>
           </form>
           </td>");
    }
    
    printf("</tr></table>");

    $db->close();
    unset($db);
}


?>
