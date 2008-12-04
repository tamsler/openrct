<?php

// $Id: com_add_class_input.php,v 1.1 2002/10/01 17:12:27 thomas Exp $

require("common.php");
require("db_class.php");


function gen_form($file1, $file2) {

    if(page_access_granted($_POST['access_id'], $_POST['access'])) {

	// Create a class object
	$db_class = new DBClass();
    
	gen_html_header("Add a new CLASS:");

	printf("
           <form method='post' action='" . $file1 . "'>
           <table>

	    <tr>
	    <td><b><font face='arial'>Class Name: </font></b></td>
	    <td><input type=text name=class_name value='" . $_POST['class_name'] . "'></td>
	    </tr>

	    <tr>
	    <td><b><font face='arial'>Manager: </font></b></td>
	    <td><input type=text name=manager value='" . $_POST['manager'] . "'></td>
	    </tr>

	    <tr>
	    <td><b><font face='arial'>Permission: </font></b></td>
	    <td>%s</td>
	    </tr>

	    <tr>
	    <td><b><font face='arial'>Active Status: </font></b></td>
	    <td>%s</td>
	    </tr>

	    <tr>
	    <td></td>
	    <td><br><input type=submit value='Add Class'>
                    <input type=hidden name='access' value='" . $_POST['access'] . "'>
                    <input type=hidden name='access_id' value='" . $_POST['access_id'] . "'></td>
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
	    </table>", $db_class->select_permission($_POST['permission']),
	       $db_class->select_active_status($_POST['active_status']));

	gen_menu_bar1();
	gen_html_footer();

	// Cleanup memory
	unset($db_class);
    }
}
?>
