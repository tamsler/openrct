<?php

// $Id: com_modify_class_input.php,v 1.1 2002/10/02 21:55:42 thomas Exp $

require("common.php");
require("db_class.php");

function gen_form($file1, $file2) {

    if(page_access_granted($_POST['access_id'], $_POST['access'])) {

	// Create a class object
	$db_class = new DBClass();
    
	// Remember the original class name
	$class_name_orig = $_POST['class_name'];

	gen_html_header("Modify Class:");
    
	printf("
           <form method='post' action='" . $file1 . "'>
           <table>

	    <tr>
	    <td><b><font face='arial'>Class Name: </font></b></td>
	    <td><input type=text name=class_name value='" . $_POST['class_name'] . "'></td>
	    </tr>

	    <tr>
	    <td><b><font face='arial'>Manager: </font></b></td>
	    <td><input type=text name=manager value='%s'></td>
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
	    <td><br><input type=submit value='Modify Class'>
                    <input type=hidden name='class_name_orig' value='%s'>
                    <input type=hidden name='class_id' value='" . $_POST['class_id'] . "'>
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
	    </table>", ($_POST['manager'] ? get_manager_alias($_POST['manager']) : ""),
	       $db_class->select_permission($_POST['permission']),
	       $db_class->select_active_status($_POST['active_status']),
	       $class_name_orig);
	gen_menu_bar1();
	gen_html_footer();

	// Cleanup memory
	unset($db_class);
    }
}

// Functions
// ---------

// Returns the manager's alias
function get_manager_alias($a_manager) {

    if(0 != strcmp($a_manager[0], "U")) {

	return $a_manager;
    }
    else {
	
	// Create a class object
	$db_class = new DBClass();
    
	$db_class->set_manager_alias($a_manager);

	$manager_alias = $db_class->get_manager_alias();

	unset($db_class);

	return $manager_alias;
    }
}


?>