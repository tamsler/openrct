<?php

// $Id: com_add_multiple_users_textarea_verify.php,v 1.3 2002/11/07 19:23:24 thomas Exp $

define("TAB", 9);
define("SPACE", 32);

require("common.php");

function gen_form($file1) {

    // Check page access
    if(page_access_granted($_POST['access_id'], $_POST['access'])) {

	gen_html_header("Add Multiple USERS Input Verification:");

	// Use trim here because some browsers add white space
	$_POST['user_data'] = trim($_POST['user_data']);

	// Check if textarea has context
	if(!$_POST['user_data']) {

	    disp_err_msg("ERROR: You did not enter any user data!");
	    back_to_admin_tasks($_POST['access_id'], $_POST['access'], $file1);
	    gen_menu_bar1();
	    gen_html_footer();
	    exit();
	}

	// Check if a deliminator was selected
	if(!$_POST['deliminator']) {
	
	    disp_err_msg("ERROR: You did not select a deliminator!");
	    disp_msg("Use the browser's \"Back\" button to select a deliminator.");
	    gen_menu_bar1();
	    gen_html_footer();
	    exit();
	}
    
	// Make sure we get the linebreaks right
	$parse_text = nl2br($_POST['user_data']);

	// Splitting up all the lines from
	// the text area into an array
	$user_array = split("<br />", $parse_text);

	// Do some simple error checking
	// for correct deliminator selection
	test_deliminator_selection($user_array);

	// Now we parse all the users fields into an table and do
	// data verification with DB.
	if(!check_user_data_integrity($user_array)) {

	    disp_err_msg("ERROR: Please review the user data!");
	    disp_msg("Use the browser's \"Back\" button to adjust the user data.");
	    gen_menu_bar1();
	    gen_html_footer();
	    exit();
	}
	else {

	    disp_msg("All the entered user data passed the data integrity check.<br>
                  Sytems will add all the users to the database now.");

	    add_user($user_array);

	    back_to_admin_tasks($_POST['access_id'], $_POST['access'], $file1);
	    gen_menu_bar1();
	    gen_html_footer();
	}
    }
}

// Functions
// ---------

// Returns TRUE if all the data is ok
// Returns FALSE if some data is NOT ok
function check_user_data_integrity($user_array) {

    define("ALIAS" , 0);
    define("PERMISSION", 4);
    define("NUM_FIELDS" , 5);

    // Create a user object
    $db_user = new DBUser();
    
    // Now we need to determine how many lines, users,
    // we read in
    $num_users = count($user_array);

    // Dealing with having or NOT having a new line
    // charachter at the end of the text area's data
    if(!trim($user_array[$num_users - 1])) {

	$num_users--;
    }

    // We will set and return this flag below
    $user_data_ok_flag = true;
    
    // Now we need to get individual user fields by
    // splitting up each user string with the provided
    // deliminator from the input page
    printf("<table border=1>
            <tr>
            <th>alias</th>
	    <th>first_name</th>
	    <th>last_name</th>
	    <th>password</th>
	    <th>permission</th>
            </tr>");
    
    for($i = 0; $i < $num_users; $i++) {

	// Creating the user's field array
	$user_fields_array = split(chr($_POST['deliminator']), trim($user_array[$i]));

	// Checking if we have the right number of user fields
	$num_user_fields = count($user_fields_array);

	// If we do not have the right number of user fields we color
	// the whole row
	if(NUM_FIELDS != $num_user_fields) {

	    printf("<tr bgcolor=red>");
	    $user_data_ok_flag = false;
	}
	else {

	    printf("<tr>");
	}
	
	// Now we start individual field checking
	for($j = 0; $j < NUM_FIELDS; $j++) {

	    // Check for quotes
	    if(has_quotes_no_msg($user_fields_array[$j])) {
		printf("<td bgcolor=red>
		        QUOTES
		        </td>");
		$user_data_ok_flag = false;
	    }
	    // If we do not have the right number of user fields,
	    // we just print the field context. The whole row is colored
	    // red.  See above.
	    elseif(NUM_FIELDS != $num_user_fields) {

		printf("<td>
		        $user_fields_array[$j]
		        </td>");
	    }
	    // Testing for null string. We need to be carefull because the
	    // permission can be 0. Thus we specifically test for the ASCII value
	    elseif((!trim($user_fields_array[$j])) && (!ord($user_fields_array[$j]))) {

		printf("<td bgcolor=orangered>
		         Missing
		         </td>");
		$user_data_ok_flag = false;
		
	    }
	    // Test if the user exists, alias is unique
	    // Also test if we have duplicate logins in entered data
	    elseif(ALIAS == $j) {

		// Error flag for duplicate users
		$error_dup = false;

		// Fist test for duplicate logins in entered data
		$login_array[$i] = trim($user_fields_array[$j]);

		$num_logins = count($login_array);

		for($x = 0; $x < $num_logins; $x++) {

		    if((0 == strcmp($login_array[$i], $login_array[$x])) && ($i != $x)) {
			
			$user_data_ok_flag = false;
			$error_dup = true;
		    }
		}

		// Test if login is taken as well as duplicated in user data
		if($db_user->does_user_exist_in_db(trim($user_fields_array[$j])) && $error_dup) {

		    printf("<td bgcolor=orangered>
		            $user_fields_array[$j](Taken and Duplicated)
		            </td>");
		    $user_data_ok_flag = false;
		    // Reset error flag
		    $error_dup = false;
		}
		// Test if login is taken
		elseif($db_user->does_user_exist_in_db(trim($user_fields_array[$j]))) {

		    printf("<td bgcolor=orangered>
		            $user_fields_array[$j](Taken)
		            </td>");
		    $user_data_ok_flag = false;
		}
		elseif($error_dup) {

		    printf("<td bgcolor=orangered>
		            $user_fields_array[$j](Duplicated)
		            </td>");
		    // Reset error flag
		    $error_dup = false;
		}
		else {

		    printf("<td bgcolor=lightgreen>
		            $user_fields_array[$j]
		            </td>");
		}
	    }
	    // Test if the permission is withing the valid range {0, 1}
	    // Since we do not allow admin creation this way, we make
	    // sure that only USER and MANAGER types are created
	    // If we would allow ADMIN types, just set the test bellow up to 2
	    elseif(PERMISSION == $j) {

		if((0 > $user_fields_array[$j]) || (1 < $user_fields_array[$j])) {

		    printf("<td bgcolor=orangered>
		            $user_fields_array[$j]
		            </td>");
		    $user_data_ok_flag = false;
		}
		else {
		    
		    printf("<td bgcolor=lightgreen>");
		    echo $user_fields_array[$j];
		    printf("</td>");
		}
	    }
	    // Everything else goes here
	    else {

		printf("<td bgcolor=lightgreen>");
		echo $user_fields_array[$j];
		printf("</td>");
	    }
	}
	printf("</tr>");
    }
    printf("</table><br><br>");

    // Clean up memory
    unset($db_user);
    
    return $user_data_ok_flag;
}


function test_deliminator_selection($user_array) {

    // Error flag used to determine if the right
    // deliminator was selected
    $error_flag = false;

    // Do some simple error checking
    // for correct deliminator selection
    switch($_POST['deliminator']) {

    case TAB:

	$error_flag = !ereg("\t", $user_array[0]);
    break;

    case SPACE:

	$error_flag = !ereg(" ", $user_array[0]);
    break;

    default:

	$error_flag = !ereg(chr($_POST['deliminator']), $user_array[0]);
    }

    if($error_flag) {

	disp_err_msg("ERROR: You selected a wrong deliminator!");
	disp_msg("Use the browser's \"Back\" button to correct the selection.");
	gen_menu_bar1();
	gen_html_footer();
	exit();
    }
}


function add_user($user_array) {

    // Now we need to determine how many lines, users,
    // we read in
    $num_users = count($user_array);
    
    // Dealing with having or NOT having a new line
    // charachter at the end of the text area's data
    if(!trim($user_array[$num_users - 1])) {
	
	$num_users--;
    }

    // Create a user object
    $new_user = new DBUser();
    
    for($i = 0; $i < $num_users; $i++) {

	// Creating the user fields array
	$user_fields_array = split(chr($_POST['deliminator']), trim($user_array[$i]));
	
	// NOTE: Here we have to convert the password to md5 hash
	// since we were not able to do so during the input process
	$new_user->add_user_to_db($user_fields_array[USER_ALIAS - 1],
	$user_fields_array[USER_FIRST_NAME - 1],
	$user_fields_array[USER_LAST_NAME - 1],
	md5($user_fields_array[USER_PASSWORD - 1]),
	$user_fields_array[USER_PERMISSION - 1],
	$_POST['access_id']);

    }

    // Cleanup memory
    unset($new_user);
}

?>