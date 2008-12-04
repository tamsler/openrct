<?php

// $Id: com_add_course_content_input.php,v 1.2 2002/11/08 23:29:20 thomas Exp $

require("common.php");

function gen_form($file1, $file2, $query) {

    if(page_access_granted($_POST['access_id'], $_POST['access'])) {

	// Init
	// style='text-decoration:none'
	$mime_types = "<a href='#' style='text-decoration:none' onclick=showMimeTypes()><b><font color='red'>[ Types ]</font></b></a>";

	printf("
            <script language='JavaScript'>
    
            function showMimeTypes() {
               window.open('mime_types.txt', 'Mime_Types', 'scrollbars=yes, width=400, height=400');
            }
            </script>");

	gen_html_header("Add a new Course Content:");

	disp_simple_msg("Please click on Help below for detailed instructions!");
	
	printf("
           <form method='post' action='" . $file1 . "'>
           <table>
            <tr align=left>
            <th>&nbsp;</th>
            <th>&nbsp;</th>
            <th>Information:</th>
            </tr>

            <tr>
	    <td><b>Name</b></td>
	    <td><input type='text' name='cc_name' value='" . $_POST['cc_name'] . "'></td>
            <td><font color='blue'><b>Give some description.</b></font></td>
            </tr>

            <tr>
            <td><b>File Name</b></td>
            <td><input type='text' name='cc_file_name' value='" . $_POST['cc_file_name'] . "'></td>
            <td><font color='blue'><b>Leave blank if SOURCE is SERVER!</b></font></td>
            </tr>

	    <tr>
	    <td><b>Location</b></td>
	    <td><input type='text' name='cc_location' value='" . $_POST['cc_location'] . "'></td>
            <td><font color='blue'><b>Leave blank if SOURCE is INTERNET or SERVER!</b></font></td>
	    </tr>

	    <tr>
	    <td><b>Source</b></td>
	    <td>%s</td>
	    </tr>

	    <tr>
	    <td><b>Mime Type</b></td>
 	    <td><input type='text' name='cc_mime_type' value='" . $_POST['cc_mime_type'] . "'></td>
            <td>&nbsp;&nbsp;%s</td>
	    </tr>

            <tr>
	    <td><b>Visible</b></td>
	    <td>%s</td>
	    </tr>

            <tr>
            <td><b>Classes</b></td>
            <td valign='top'>
            <select name='cc_class_id' size=5>", select_cc_source($_POST['cc_source']),
	       $mime_types,
	       select_cc_visible($_POST['cc_visible']));

	get_classes($query);

	printf("</select>
            </td>
            </tr>

	    <tr>
	    <td></td>
	    <td><br><input type='submit' value='Add Course Content'>
	            <input type='hidden' name='access' value='" . $_POST['access'] . "'>
                    <input type='hidden' name='access_id' value='" . $_POST['access_id'] . "'></td>
	    </tr>
            </form>
            <tr>
            <td></td>
	    <td><br><form method='post' action='" . $file2 . "'>
                    <input type=submit value='Back To Admin Tasks'>
	            <input type=hidden name='access' value='" . $_POST['access'] . "'>
                    <input type=hidden name='access_id' value='" . $_POST['access_id'] . "'>
                    </form>
            </td>
	    </tr>
	    </table>");

	gen_menu_bar1("help_add_course_content_input.php");

	gen_html_footer();
    }
}

// Functions
// ---------

// Generate the html course content source selection
function select_cc_source($cc_source) {

    switch($cc_source) {
	
    case 1:
	return "<select name='cc_source'>
		<option value=1 selected>Server
		<option value=2>Client
		<option value=3>Internet
		</select>";
	break;

    case 2:
	return "<select name='cc_source'>
		<option value=1>Server
		<option value=2 selected>Client
		<option value=3>Internet
		</select>";
	break;

    case 3:
	return "<select name='cc_source'>
		<option value=1>Server
		<option value=2>Client
		<option value=3 selected>Internet
		</select>";
	break;

    default:
	return "<select name='cc_source'>
		<option value=1 selected>Server
		<option value=2>Client
		<option value=3>Internet
		</select>";
    }
}

// Generate the html course content visible selection
function select_cc_visible($cc_visible) {

    switch($cc_visible) {

    case "true":
	return "<select name='cc_visible'>
		<option value='true' selected>Yes
		<option value='false'>No
		</select>";
	break;

    case "false":
	return "<select name='cc_visible'>
		<option value='true'>Yes
		<option value='false'selected>No
		</select>";
	break;
	
    default:
        return "<select name='cc_visible'>
                <option value='true' selected>Yes
                <option value='false'>No
                </select>";
    }
}

// Print all the classes
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