<?php

// $Id: com_add_multiple_users_textarea_input.php,v 1.2 2002/11/06 19:23:33 thomas Exp $

require("common.php");

// This page allows the admin to add more then one user at a time
// You have to enter all the user information in the html textarea
// adhering a given syntax.
// <Login>:<FirstName>:<LastName>:<Password>:<Permission>

// Left Bracket
define("LB", "&#60;");
// Right Bracket
define("RB", "&#62;");
// Deliminator composition
define("DL", LB . ":" . RB);

function gen_form($file1, $file2) {

    // Array of all possible deliminators
    // The Name values are ASCII Decimal values
    $delim_arr = array("Name" => array ("Single Colon ".LB.":".RB,
					"Single Hash  ".LB."#".RB,
					"Tab",
					"Space"),
		       "Value" => array (58, 35, 9, 32));

    // Determin how many entries the deliminator array has
    $num_delim = count($delim_arr["Name"]);


    // Check page access
    if(page_access_granted($_POST['access_id'], $_POST['access'])) {

	gen_html_header("Add Multiple USERS:");

	// Show the admin the syntax of the user sting composition
	disp_simple_msg("Syntax Example:");
	printf("<table border='1' cellpadding='10' bgcolor='#000000'>
            <tr>
            <td bgcolor=" . TB_COLOR_2 . " >
            <font face=Arial><b>%s</b></font><br><br>
            <font face=Arial>%s</font><br>
            <font face=Arial><b>%s</b></font>
            </td>
            </tr>
            </table><br><br>",
	       LB."Login".RB.DL.LB."FirstName".RB.DL.LB."LastName".RB.DL.LB."Password".RB.DL.LB."Permission { USER = 0 , MANAGER = 1 }".RB,
	       "eg.",
	       "thomas:Thomas:Amsler:password:0");

	// Show a list of possible deliminators
	// from which the admin can choose from
	// and the text area.
	disp_simple_msg("Please select one of the following deliminators<br>
                     that matches the ones you use in the text area:");
	printf("<form action='add_multiple_users_textarea_verify.php' method='post'>
            <select name=deliminator size=$num_delim>");

	for($i = 0; $i < $num_delim; $i++) {

	    printf("<option value=" . $delim_arr["Value"][$i] . ">" . $delim_arr["Name"][$i] . "</option>");
	}

	printf("</select><br><br>");

	// Show the text area
	disp_simple_msg("Please enter the users in the text area below:");
	printf("<textarea name='user_data' cols=70 rows=10 wrap=soft>
            </textarea><br><br>
            <input type='hidden' name='access' value='" . $_POST['access'] . "'>
            <input type='hidden' name='access_id' value='" . $_POST['access_id'] . "'>
            <input type='submit' value='Process Text'>
	    </form><br>");

	back_to_admin_tasks($_POST['access_id'], $_POST['access']);

	gen_menu_bar1();

	gen_html_footer();
    }
}

?>
