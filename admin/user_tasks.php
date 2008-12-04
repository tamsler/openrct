<?php

// $Id: user_tasks.php,v 1.7 2003/06/20 17:23:17 thomas Exp $

require("common.php");


// This is the main rct admin page from where an user
// can access all admin specific functions. 


if(page_access_granted($_POST['access_id'], $_POST['access'])) {

    gen_html_header("Admin Tasks:  Please choose one of the following:");

    disp_simple_msg("User Administration:");

    display_general_admin_tasks();

    echo("<br><br>");

    disp_simple_msg("RCT Module Administration:");
    
    display_module_admin_tasks();

    gen_menu_bar1();
    
    gen_html_footer();
}


// Functions
// ---------


// Displays the "Add:", "Administer:" and "Enroll" options
function display_general_admin_tasks() {

    printf("
	   <table border=1 cellspacing=1 cellpadding=5>
	   <tr>
	   <th bgcolor=" . TB_COLOR_1 . " ><b><font face='arial' color=" . TB_HEAD_1 . " >Edit:</font></b></th>
	   </tr>

	   <tr align='center'>

	   <!-- EDIT -->
	   <td bgcolor=" . TB_COLOR_1 . " valign=top>
	   <form method='post' action='u_user_profile_input.php'>
	   <input type='hidden' name='access' value='" . $_POST['access'] . "'>
	   <input type='hidden' name='access_id' value='" . $_POST['access_id'] . "'>
	   <input style='width:12em' type='submit' value='My User Profile'>
	   </form>
	   </td>
           </tr>
	   </table>");
}

// Displays the the different module admin tasks such as:
// Chat Module
function display_module_admin_tasks() {

    printf("
	   <table border=1 cellspacing=1 cellpadding=5>
	   <tr>
	   <th bgcolor=" . TB_COLOR_1 . " ><b><font face='arial' color=" . TB_HEAD_1 . " >Chat:</font></b></th>
	   <th bgcolor=" . TB_COLOR_2 . " ><b><font face='arial' color=" . TB_HEAD_1 . " >Whiteboard:</font></b></th>
	   <th bgcolor=" . TB_COLOR_1 . " ><b><font face='arial' color=" . TB_HEAD_1 . " >Sound:</font></b></th>
	   <th bgcolor=" . TB_COLOR_2 . " ><b><font face='arial' color=" . TB_HEAD_1 . " >Textpad:</font></b></th>
	   </tr>

	   <tr align='center'>

	   <!-- CHAT -->
	   <td bgcolor=". TB_COLOR_1 . " valign=top>
	   <form method='post' action='u_my_chat_msg_input.php'>
	   <input type='hidden' name='access' value='" . $_POST['access'] . "'>
	   <input type='hidden' name='access_id' value='" . $_POST['access_id'] . "'>
	   <input style='width:12em' type='submit' value='My Chat Messages'>
	   </form>

	   <form method='post' action='u_class_chat_msg_selection.php'>
	   <input type='hidden' name='access' value='" . $_POST['access'] . "'>
	   <input type='hidden' name='access_id' value='" . $_POST['access_id'] . "'>
	   <input style='width:12em' type='submit' value='My Class Chat Messages'>
	   </form>

	   <form method='post' action='u_team_chat_msg_selection.php'>
	   <input type='hidden' name='access' value='" . $_POST['access'] . "'>
	   <input type='hidden' name='access_id' value='" . $_POST['access_id'] . "'>
	   <input style='width:12em' type='submit' value='My Team Chat Messages'>
	   </form>

           <form method='post' action='u_group_chat_msg_selection.php'>
	   <input type='hidden' name='access' value='" . $_POST['access'] . "'>
	   <input type='hidden' name='access_id' value='" . $_POST['access_id'] . "'>
	   <input style='width:12em' type='submit' value='My Group Chat Messages'>
	   </form>
	   </td>

	   <!-- WHITEBOARD -->
	   <td bgcolor=". TB_COLOR_2 . " valign=top>
           <form method='post' action=''>
	   <input type='hidden' name='access' value='" . $_POST['access'] . "'>
	   <input type='hidden' name='access_id' value='" . $_POST['access_id'] . "'>
	   <input style='width:12em' type='button' value='Not Implemented Yet'>
	   </form>
	   </td>

	   <!-- SOUND -->
	   <td bgcolor=" . TB_COLOR_1 . " valign=top>
           <form method='post' action=''>
	   <input type='hidden' name='access' value='" . $_POST['access'] . "'>
	   <input type='hidden' name='access_id' value='" . $_POST['access_id'] . "'>
	   <input style='width:12em' type='button' value='Not Implemented Yet'>
	   </form>
	   </td>

	   <!-- TEXTPAD -->
	   <td bgcolor=" . TB_COLOR_2 . " valign=top>
           <form method='postdiretion=''>
	   <input type='hidden' name='access' value='" . $_POST['access'] . "'>
	   <input type='hidden' name='access_id' value='" . $_POST['access_id'] . "'>
	   <input style='width:12em' type='button' value='Not Implemented Yet'>
	   </form>
	   </td>
	   </tr>
	   </table>");
}

?>
