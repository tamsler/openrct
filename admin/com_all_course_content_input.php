<?php

// $Id: com_all_course_content_input.php,v 1.2 2002/11/04 23:59:59 thomas Exp $

require("common.php");
require("db_course_content.php");

function gen_form($file1, $file2, $file3, $query) {

    if(page_access_granted($_POST['access_id'], $_POST['access'])) {

	gen_html_header("All Course Content:");

	disp_simple_msg("List Of All Course Content:");

	display_all_course_content($file1, $file2, $query);

	back_to_admin_tasks($_POST['access_id'], $_POST['access'], $file3);
    
	gen_menu_bar1();

	gen_html_footer();
    }
}

// Functions
// ---------

function display_all_course_content($file1, $file2, $query) {
    
    // Create a DB object
    $db = new DB();

    // Connect to the DB
    $db->connect();

    // Execute query
    $db_res = $db->exec($query);

    // Check for errors
    if (!$db_res) {
	
	disp_err_msg("ERROR: Couldn't get Course Content from DB!");
	$db->close();
        unset($db);
	exit();
    }

    // Init vars
    $numrows = $db->get_num_rows($db_res);
    $numfields = $db->get_num_fields($db_res);

    // Check if there are any course content
    if(0 == $numrows) {

        disp_msg("There is no course content.");
        $db->close();
        unset($db);
        return;
    }

    printf ("<table border='1' cellpadding='5'>");

    printf("<tr>");

    // Print the fields names
    for($i = 0; $i < $numfields; $i++) {

	if((CC_ID        == $i) ||
	   (CC_NAME      == $i) ||
	   (CC_FILE_NAME == $i) ||
	   (CC_LOCATION  == $i) ||
	   (CC_SOURCE    == $i) ||
	   (CC_MIME_TYPE == $i) ||
	   (CC_CLASS_ID  == $i) ||
	   (CC_VISIBLE   == $i) ||
	   (CC_DATE      == $i) ||
	   (CC_PER       == $i) ||
	   (CC_VER       == $i)) {
	    
	    printf("<td align='center'><b><font face='arial' color=" . TB_HEAD_1 . " >" . $db->get_field_name($db_res, $i) . "</font></b></td>");
	}
    }

    print("<td align='center'><b><font face='arial' color=" . TB_HEAD_1 . " >Modify</font></b></td>");
    print("<td align='center'><b><font face='arial' color=" . TB_HEAD_1 . " >Delete</font></b></td>");
    
    printf("</tr>");

    for($i = 0; $i < $numrows; $i++) {

	$myrow = $db->fetch_row ($db_res, $i);

	if($i % 2) {

	    printf("<tr bgcolor=" . TB_COLOR_1 . ">");
	    for($j = 0; $j < $numfields; $j++) {
		if((CC_ID        == $j) ||
		   (CC_NAME      == $j) ||
		   (CC_FILE_NAME == $j) ||
		   (CC_LOCATION  == $j) ||
		   (CC_SOURCE    == $j) ||
		   (CC_MIME_TYPE == $j) ||
		   (CC_CLASS_ID  == $j) ||
		   (CC_VISIBLE   == $j) ||
		   (CC_DATE      == $j) ||
		   (CC_PER       == $j) ||
		   (CC_VER       == $j)) {
		    printf ("<td>%s</td>", $myrow[$j]);
		}
	    }
	    gen_modify($myrow, $file1);
	    gen_delete($myrow, $file2);
	    printf("</tr>");
	}
	else {

	    printf("<tr bgcolor=" . TB_COLOR_2 . ">");
	    for($j = 0; $j < $numfields; $j++) {
		if((CC_ID        == $j) ||
		   (CC_NAME      == $j) ||
		   (CC_FILE_NAME == $j) ||
		   (CC_LOCATION  == $j) ||
		   (CC_SOURCE    == $j) ||
		   (CC_MIME_TYPE == $j) ||
		   (CC_CLASS_ID  == $j) ||
		   (CC_VISIBLE   == $j) ||
		   (CC_DATE      == $j) ||
		   (CC_PER       == $j) ||
		   (CC_VER       == $j)) {
		    printf ("<td>%s</td>", $myrow[$j]);
		}
	    }
	    gen_modify($myrow, $file1);
	    gen_delete($myrow, $file2);
	    printf("</tr>");
	}
	
    }

    printf ("</table><br><br>");

    $db->close();
    unset($db);
}

function gen_delete($row, $file2) {

    printf("<td>
	    <form method='post' action='" . $file2 . "'>
	    <input type='hidden' name='cc_id' value='" . $row[CC_ID] . "'>
	    <input type='hidden' name='access' value='" . $_POST['access'] . "'>
	    <input type='hidden' name='access_id' value='" . $_POST['access_id'] . "'>
	    <input type='submit' value='Delete'>
	    </form>
	    </td>");
}

function gen_modify($row, $file1) {
    
    printf("<td>
            <form method='post' action='" . $file1 ."'>
	    <input type='hidden' name='cc_id' value='" . $row[CC_ID] . "'>
	    <input type='hidden' name='cc_name' value='" . $row[CC_NAME] . "'>
	    <input type='hidden' name='cc_location' value='" . $row[CC_LOCATION] . "'>
	    <input type='hidden' name='cc_file_name' value='" . $row[CC_FILE_NAME] . "'>
	    <input type='hidden' name='cc_source' value='" . $row[CC_SOURCE] . "'>
	    <input type='hidden' name='cc_mime_type' value='" . $row[CC_MIME_TYPE] . "'>
	    <input type='hidden' name='cc_visible' value='" . $row[CC_VISIBLE] . "'>
	    <input type='hidden' name='cc_class_id' value='" . $row[CC_CLASS_ID] . "'>
	    <input type='hidden' name='access' value='" . $_POST['access'] . "'>
	    <input type='hidden' name='access_id' value='" . $_POST['access_id'] . "'>
	    <input type='submit' value='Modify'>
	    </form>
	    </td>");
}

?>
