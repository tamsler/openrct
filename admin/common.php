<?php

// $Id: common.php,v 1.40 2003/04/01 19:43:42 thomas Exp $

// Definitions:
// ------------

// Version
define("RCT_VERSION", "v1.6.0");

// Server data, and image directories
define("RCT_SERVER_DATA_DIR", "/opt/rct/data");
define("RCT_SERVER_IMAGE_DIR", "/opt/rct/images");

// Line break
define("NL", "<br>\n");

// Colors for table tuples
// Make sure that there is a white space at the end
// of the hex color code to prevent string catenation
define("TB_COLOR_1", '#F0F8FF ');
define("TB_COLOR_2", '#FDF5E6 ');

// Color for table headers
define("TB_HEAD_1", '#3333FF ');

// Deliminator char for data string
define("DELIM_CHAR", ":");

// Modules
define("MODULE_USER", "user");
define("MODULE_CHAT", "chat");
define("MODULE_SOUND", "sound");
define("MODULE_TEXTPAD", "textpad");
define("MODULE_URL", "url");
define("MODULE_FTP", "ftp");
define("MODULE_CC", "coursecontent");

// Module File Prefix
define("MODULE_PREFIX_CC", "cc_");
define("MODULE_PREFIX_SOUND", "sound_");
define("MODULE_PREFIX_USER", "user_");

// When we select data for Team, Group or Both
define("TEAM", 1);
define("GROUP", 2);
define("TEAM_AND_GROUP", 3);

// Integer Admin, Manger and User types
define("ADMIN_ACCESS", 1);
define("MANAGER_ACCESS", 2);
define("USER_ACCESS", 3);

// Limit and Offset values for query result
define("RCT_LIMIT", 10);
define("RCT_OFFSET", 0);

// Person Type Permissions
define("USER", "0");
define("MANAGER", "1");
define("ADMIN", "2");

// Default User, Class, Team, Group
define("DEF_USER", "U0");
define("DEF_CLASS", "C0");
define("DEF_TEAM", "T0");
define("DEF_GROUP", "G0");

// Person Types
define("TYPE_USER", "User");
define("TYPE_MANAGER", "Manager");
define("TYPE_ADMIN", "Administrator");
define("TYPE_DEFAULT", "Default Type");

// Define specific user files
define("USER_TASKS", "user_tasks.php");

// Include files:
// --------------
require("db.php");
require("db_user.php");


// Test if a string contains any single
// or double quotes with error message
function has_quotes($str) {

    if(strstr($str, "\"") ||
       strstr($str, "\'")) {

	disp_err_msg(" Error: Input cannot contain single or double quoted strings") ;
	return true;
    }
    else {

	return false;
    }
}

// Test if a string contains any single
// or double quotes without error message
function has_quotes_no_msg($str) {

    if(strstr($str, "\"") ||
       strstr($str, "\'")) {

	return true;
    }
    else {

	return false;
    }
}

// Verify access to page
function page_access_granted($access_id, $access_key) {

    if($access_id && $access_key) {
	
	$db_user = new DBUser();

	$db_user->init_with_user_id($access_id);
    }
    else {

	gen_html_header("ERROR: You are not allowd to access this page!", 0);
    
	gen_html_footer();
    
	exit();
    }

    // Actual test for accessing the page
    if(0 == strcmp($access_key, $db_user->get_password())) {

	unset($db_user);
	return true;
    }
    else {

	gen_html_header("ERROR: You are not allowd to access this page!", 0);
        gen_html_footer();

	unset($db_user);
	return false;
    }
}

// Display error messages
function disp_err_msg($message) {

    printf("<TABLE CELLPADDING=4 CELLSPACING=0 BORDER=0>");

    printf("<TR BGCOLOR='#FF6633'><TD><FONT FACE=Arial><B>");

    printf("%s</B></FONT><BR></TD>", $message);

    printf("<TD ALIGN=right>");

    printf("</FONT></TD></TR>");

    printf("</TABLE><br><br>");
}


function disp_simple_msg($message) {

    printf("<font face=Arial><b>$message</b></font><br>");
}


function disp_msg($message) {

    printf("<TABLE CELLPADDING=4 CELLSPACING=0 BORDER=0>");

    printf("<TR BGCOLOR='#66CC66'><TD><FONT FACE=Arial><B>");

    printf("%s</B></FONT><BR></TD>", $message);

    printf("<TD ALIGN=right>");

    printf("</FONT></TD></TR>");

    printf("</TABLE><br><br>");
}

// Type:
// 0 : error
// 1 : success
// 2 : generic
function gen_html_header($message, $type = 2, $logo = "OpenRCTAdminLogo.png") {
    
    printf("<HEAD> <TITLE> Admin Remote Collaboration Download DB </TITLE>
            <meta http-equiv='Content-Type' content='text/html; CHARSET=utf-8'>
            </HEAD>");

    printf("<BODY TEXT='#000000' BGCOLOR='#efefef' LINK='#000000' VLINK='#000000' ALINK='#000000'>\n");

    printf("<img src='$logo'><font face=Arial><b><H3>" . RCT_VERSION . "</H3></b></font>");
    
    printf("<TABLE CELLPADDING=4 CELLSPACING=0 BORDER=0>");

    switch($type) {
    case 0:
	printf("<TR BGCOLOR='#FF6633'><TD><FONT FACE=Arial><B>");
	printf("%s</B></FONT><BR></TD>", $message);
	break;
	
    case 1:
	printf("<TR BGCOLOR='#66CC66'><TD><FONT FACE=Arial><B>");
	printf("%s</B></FONT><BR></TD>", $message);
	break;
	
    case 2:
	printf("<TR BGCOLOR='#B0C4DE'><TD><FONT FACE=Arial><B>");
	printf("%s</B></FONT><BR></TD>", $message);
	break;
	
    default:
	printf("<TR BGCOLOR='#B0C4DE'><TD><FONT FACE=Arial><B>");
	printf("%s</B></FONT><BR></TD>", $message);
    }
    
    printf("</TR>");
    
    printf("</TABLE>");

    printf("<BR>");

    printf("<BR>");
}


function gen_html_footer() {

    printf("</BODY>");

    printf("</HTML>");
}


function gen_help_html_header() {

    printf("
            <html>
            <title>RCT Admin Help</title>
            <body bgcolor = 'white'>
            <font face='arial'>");
}


function gen_help_html_footer() {

    printf("
            <br><br><br>
            <center>
            <form>
            <input type=submit onclick=window.close() value='Close'>
            </form>
            </center>
            </font>
            </body>
            </html>");
}


function go_to_admin_tasks($access_id, $access, $action_file = "admin_tasks.php") {

    printf("
            <form method='post' action='$action_file'>
            <input type=hidden name='access' value=$access>
            <input type=hidden name='access_id' value=$access_id>
            <input type=submit value='Go To Admin Tasks'>
	    </form>");
    
}

function back_to_admin_tasks($access_id, $access, $action_file = "admin_tasks.php") {

    printf("<br>
            <form method='post' action='$action_file'>
            <input type=hidden name='access' value=$access>
            <input type=hidden name='access_id' value=$access_id>
            <input type=submit value='Back To Admin Tasks'>
	    </form>");
    
}

// Menu Bars
// bar1: Logout, Contact US, Help
// bar2: Login, Contact US, Help
// bar3: Home, Contact US, Help

function gen_menu_bar1($help_file = "rct_admin_help_generic.html") {

    $color = "#6495ED";
    $str1 = "style='width:120px'>&nbsp;<a href='index.php' style='text-decoration:none'";
    $str2 = "style='width:120px'>&nbsp;<a href='mailto:amsler@cs.ucdavis.edu?Subject=RCT%20ADMIN' style='text-decoration:none'";
    $str3 = "style='width:120px'>&nbsp;<a href='#top' style='text-decoration:none' onclick=showHelp()";
    
    printf("
            <script language='JavaScript'>
    
            function showHelp(){
               window.open('$help_file', 'Help', 'scrollbars=yes, width=400, height=400');
            }
            </script>");

    
    printf("<br><br>
            <a name='top'></a>
            <table clellpadding=4 cellspacing=0 border=0>
            <tr>
            <td colspan=7 bgcolor=$color><img src='cleardot.gif' height=2></td>
            </tr>
            <tr align='center'>
            <td style='width:1px' bgcolor='$color'>&nbsp;</td>
            <td bgcolor='#B0C4DE' %s><b><font face='arial'>Logout</font><b></a>&nbsp;</td>
            <td style='width:1px' bgcolor='$color'>&nbsp;</td>
            <td bgcolor='#B0C4DE' %s><b><font face='arial'>Contact Us</font></b></a>&nbsp;</td>
            <td style='width:1px' bgcolor='$color'>&nbsp;</td>
            <td bgcolor='#B0C4DE' %s><b><font face='arial'>Help</font></b></a>&nbsp;</td>
            <td style='width:1px' bgcolor='$color'>&nbsp;</td>
            </tr>
            <td colspan=7 bgcolor=$color><img src='cleardot.gif' height=2></td>
            </tr>
            </table>", $str1, $str2, $str3);
}


function gen_menu_bar2($help_file = "rct_admin_help_generic.html") {

    $color = "#6495ED";
    $str1 = "style='width:120px'>&nbsp;<a href='index.php' style='text-decoration:none'";
    $str2 = "style='width:120px'>&nbsp;<a href='mailto:amsler@cs.ucdavis.edu?Subject=RCT%20ADMIN' style='text-decoration:none'";
    $str3 = "style='width:120px'>&nbsp;<a href='#top' style='text-decoration:none' onclick=showHelp()";
    
    printf("
            <script language='JavaScript'>
    
            function showHelp(){
               window.open('$help_file', 'Help', 'scrollbars=yes, width=400, height=400');
            }
            </script>");

    
    printf("<br><br>
            <a name='top'></a>
            <table clellpadding=4 cellspacing=0 border=0>
            <tr>
            <td colspan=7 bgcolor=$color><img src='cleardot.gif' height=2></td>
            </tr>
            <tr align='center'>
            <td style='width:1px' bgcolor='$color'>&nbsp;</td>
            <td bgcolor='#B0C4DE' %s><b><font face='arial'>Login</font><b></a>&nbsp;</td>
            <td style='width:1px' bgcolor='$color'>&nbsp;</td>
            <td bgcolor='#B0C4DE' %s><b><font face='arial'>Contact Us</font></b></a>&nbsp;</td>
            <td style='width:1px' bgcolor='$color'>&nbsp;</td>
            <td bgcolor='#B0C4DE' %s><b><font face='arial'>Help</font></b></a>&nbsp;</td>
            <td style='width:1px' bgcolor='$color'>&nbsp;</td>
            </tr>
            <tr>
            <td colspan=7 bgcolor=$color><img src='cleardot.gif' height=2></td>
            </tr>
            </table>", $str1, $str2, $str3);
}

function gen_menu_bar3($help_file = "rct_admin_help_generic.html") {

    $color = "#6495ED";
    $str1 = "style='width:120px'>&nbsp;<a href='index.php' style='text-decoration:none'";
    $str2 = "style='width:120px'>&nbsp;<a href='mailto:amsler@cs.ucdavis.edu?Subject=RCT%20ADMIN' style='text-decoration:none'";
    $str3 = "style='width:120px'>&nbsp;<a href='#top' style='text-decoration:none' onclick=showHelp()";


    printf("
            <script language='JavaScript'>
    
            function showHelp(){
               window.open('$help_file', 'Help', 'scrollbars=yes, width=400, height=400');
            }
            </script>");

    
    printf("<br><br>
            <a name='top'></a>
            <table clellpadding=4 cellspacing=0 border=0>
            <tr>
            <td colspan=7 bgcolor=$color><img src='cleardot.gif' height=2></td>
            </tr>
            <tr align='center'>
            <td style='width:1px' bgcolor='$color'>&nbsp;</td>
            <td bgcolor='#B0C4DE' %s><b><font face='arial'>Home</font></b></a>&nbsp;</td>
            <td style='width:1px' bgcolor='$color'>&nbsp;</td>
            <td bgcolor='#B0C4DE' %s><b><font face='arial'>Contact Us</font></b></a>&nbsp;</td>
            <td style='width:1px' bgcolor='$color'>&nbsp;</td>
            <td bgcolor='#B0C4DE' %s><b><font face='arial'>Help</font></b></a>&nbsp;</td>
            <td style='width:1px' bgcolor='$color'>&nbsp;</td>
            </tr>
            <tr>
            <td colspan=7 bgcolor=$color><img src='cleardot.gif' height=2></td>
            </tr>
            </table>", $str1, $str2, $str3);
}


// Generate a file index:
function get_file_index() {

    // Return value
    $file_index = "";
    
    // Query
    $query = "select file_index from rct_indices";
        
    // Create a DB object
    $db = new DB();

    // Connect to the DB
    $db->connect();

    // Execute transaction
    $db_res = $db->exec($query);

    // Test db result
    if($db->one_tuple($db_res)) {

	// Get the row from the result
	$row = pg_fetch_row($db_res, 0);

	// Get the actual file index
	$file_index = $row[0];

	// Now we need to update/increment the file index in the DB
	$index = $file_index + 1;

	$query = "UPDATE rct_indices SET file_index='" . $index . "'";

	$db_res = $db->exec($query, $_POST['access_id']);

	if(!$db_res) {

	    disp_err_msg("ERROR: Was not able to update file index in DB!");
	    $db->close();
	    unset($db);
	    // Return empty string
	    return $file_index;
	}
	else {

	    // All the file_ids start with the "F" followed by a number
	    $file_index = "F" . $file_index;
	    $db->close();
	    unset($db);
	    // Return file index
	    return $file_index;
	}
    }
    else {

	disp_err_msg("ERROR: Something wrong with the rct_indices table!");
	$db->close();
	unset($db);
	// Return empty string
	return $file_index;
    }
}

function file_upload($module = "default", $file_id = "default") {

    printf("
            <script language='JavaScript'>
    
            function fileUpload() {
                window.open('upload_file_input.php?module_type=$module&file_id=$file_id', 'Upload_File', 'scrollbars=yes, width=400, height=400');
            }

            </script>");

    printf("<a href='#' onclick=fileUpload()><H2><b><font color='red'>Click here to upload a file</font><b></H2></a>");
}

// Function removes openrct interman file name prefix
function remove_name_prefix($prefixed_name, $source_type) {

    // We only remove prefix if source_type is of type server
    if(1 == $source_type) {
    
	// If the prefixed_name look like this: cc_f22_some_name

	// Getting index of first "_"
	$index = strpos($prefixed_name, "_");

	// Getting substring eg. f22_some_name
	$name = substr($prefixed_name, $index + 1);

	// Getting index of second "_"
	$index = strpos($name, "_");

	// Return name without prefix eg. some_name
	return substr($name, $index + 1);
    }
    else {

	return $prefixed_name;
    }
}

?>
