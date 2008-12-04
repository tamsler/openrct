<?php

// $Id: manager_tasks.php,v 1.17 2003/06/20 17:23:17 thomas Exp $


require("common.php");


// This is the main rct admin page from where an administrator
// can access all admin specific functions. 


if(page_access_granted($_POST['access_id'], $_POST['access'])) {

    gen_html_header("Admin Tasks:  Please choose one of the following:");

    display_general_admin_tasks();

    echo("<br><br>");

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

	   <tr align='left'>
           <th colspan=3>User, Class, Team Administer:</th>
           </tr>

	   <tr>
	   <th bgcolor=" . TB_COLOR_1 . " ><b><font face='arial' color=" . TB_HEAD_1 . " >Add:</font></b></th>
	   <th bgcolor=" . TB_COLOR_2 . " ><b><font face='arial' color=" . TB_HEAD_1 . " >Enroll:</font></b></th>
	   <th bgcolor=" . TB_COLOR_1 . " ><b><font face='arial' color=" . TB_HEAD_1 . " >Administer:</font></b></th>
	   </tr>

	   <tr align='center'>

	   <!-- ADD -->
	   <td bgcolor=" . TB_COLOR_1 . " valign=top>
	   <form method='post' action='m_add_user_input.php'>
	   <input type='hidden' name='access' value='" . $_POST['access'] . "'>
	   <input type='hidden' name='access_id' value='" . $_POST['access_id'] . "'>
	   <input style='width:15em' type='submit' value='Single User'>
	   </form>

	   <form method='post' action='m_add_multiple_users_input.php'>
	   <input type='hidden' name='access' value='" . $_POST['access'] . "'>
	   <input type='hidden' name='access_id' value='" . $_POST['access_id'] . "'>
	   <input style='width:15em' type='submit' value='Multiple Users (Form)'>
	   </form>

	   <form method='post' action='m_add_multiple_users_textarea_input.php'>
	   <input type='hidden' name='access' value='" . $_POST['access'] . "'>
	   <input type='hidden' name='access_id' value='" . $_POST['access_id'] . "'>
	   <input style='width:15em' type='submit' value='Multiple Users (Textarea)'>
	   </form>

	   <form method='post' action='m_add_class_input.php'>
	   <input type='hidden' name='access' value='" . $_POST['access'] . "'>
	   <input type='hidden' name='access_id' value='" . $_POST['access_id'] . "'>
	   <input style='width:15em' type='submit' value='Single Class'>
	   </form>

	   <form method='post' action='m_add_team_input.php'>
	   <input type='hidden' name='access' value='" . $_POST['access'] . "'>
	   <input type='hidden' name='access_id' value='" . $_POST['access_id'] . "'>
	   <input style='width:15em' type='submit' value='Single Team'>
	   </form>

           <form method='post' action='m_add_course_content_input.php'>
           <input type='hidden' name='access' value='" . $_POST['access'] . "'>
	   <input type='hidden' name='access_id' value='" . $_POST['access_id'] . "'>
	   <input style='width:15em' type='submit' value='Course Content'>
	   </form>
	   </td>

	   <!-- ENROLL -->
	   <td bgcolor=" . TB_COLOR_2 . " valign=top>
	   <form method='post' action='m_enroll_user_class_input.php'>
	   <input type='hidden' name='access' value='" . $_POST['access'] . "'>
	   <input type='hidden' name='access_id' value='" . $_POST['access_id'] . "'>
	   <input style='width:15em' type='submit' value='User In Class'>
	   </form>

	   <form method='post' action='m_enroll_user_team_input.php'>
	   <input type='hidden' name='access' value='" . $_POST['access'] . "'>
	   <input type='hidden' name='access_id' value='" . $_POST['access_id'] . "'>
	   <input style='width:15em' type='submit' value='User In Team'>
	   </form>

           <form method='post' action='m_enroll_user_class_team_input.php'>
	   <input type='hidden' name='access' value='" . $_POST['access'] . "'>
	   <input type='hidden' name='access_id' value='" . $_POST['access_id'] . "'>
	   <input style='width:15em;height:5em' type='submit' value='Multiple User In Class\nAnd Team\n(Textarea)'>
	   </form>
           </td>


	   <!-- ADMINISTRATE -->
	   <td bgcolor=" . TB_COLOR_1 . " valign=top>
	   <form method='post' action='m_admin_users_input.php'>
	   <input type='hidden' name='access' value='" . $_POST['access'] . "'>
	   <input type='hidden' name='access_id' value='" . $_POST['access_id'] . "'>
	   <input style='width:15em' type='submit' value='Users'>
	   </form>

	   <form method='post' action='m_admin_classes_input.php'>
	   <input type='hidden' name='access' value='" . $_POST['access'] . "'>
	   <input type='hidden' name='access_id' value='" . $_POST['access_id'] . "'>
	   <input style='width:15em' type='submit' value='Classes'>
	   </form>

	   <form method='post' action='m_admin_teams_input.php'>
	   <input type='hidden' name='access' value='" . $_POST['access'] . "'>
	   <input type='hidden' name='access_id' value='" . $_POST['access_id'] . "'>
	   <input style='width:15em' type='submit' value='Teams'>
	   </form>

           <form method='post' action='m_all_course_content_input.php'>
	   <input type='hidden' name='access' value='" . $_POST['access'] . "'>
	   <input type='hidden' name='access_id' value='" . $_POST['access_id'] . "'>
	   <input style='width:15em' type='submit' value='Course Content'>
	   </form>

           <form method='post' action='m_admin_user_class_enrollment_selection.php'>
	   <input type='hidden' name='access' value='" . $_POST['access'] . "'>
	   <input type='hidden' name='access_id' value='" . $_POST['access_id'] . "'>
	   <input style='width:15em' type='submit' value='User In Class Enrollment'>
	   </form>

           <form method='post' action='m_admin_user_team_enrollment_selection.php'>
	   <input type='hidden' name='access' value='" . $_POST['access'] . "'>
	   <input type='hidden' name='access_id' value='" . $_POST['access_id'] . "'>
	   <input style='width:15em' type='submit' value='User In Team Enrollment'>
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
           <tr align='left'>
           <th colspan=4>RCT Module Administration:</th>
           </tr>

           <tr>
	   <th bgcolor=" . TB_COLOR_1 . " ><b><font face='arial' color=" . TB_HEAD_1 . " >Chat:</font></b></th>
	   <th bgcolor=" . TB_COLOR_2 . " ><b><font face='arial' color=" . TB_HEAD_1 . " >Textpad:</font></b></th>
	   <th bgcolor=" . TB_COLOR_1 . " ><b><font face='arial' color=" . TB_HEAD_1 . " >Sound:</font></b></th>
	   <th bgcolor=" . TB_COLOR_2 . " ><b><font face='arial' color=" . TB_HEAD_1 . " >File Transfer:</font></b></th>
	   </tr>

	   <tr align='center'>

	   <!-- CHAT -->
	   <td bgcolor=". TB_COLOR_1 . " valign=top>
           <form method='post' action='m_all_chat_msg_input.php?assembly_type=" . TEAM . "'>
	   <input type='hidden' name='access' value='" . $_POST['access'] . "'>
	   <input type='hidden' name='access_id' value='" . $_POST['access_id'] . "'>
	   <input style='width:15em' type='submit' value='All Team Chat Messages'>
	   </form>

           <form method='post' action='m_all_chat_msg_input.php?assembly_type=" . GROUP . "'>
	   <input type='hidden' name='access' value='" . $_POST['access'] . "'>
	   <input type='hidden' name='access_id' value='" . $_POST['access_id'] . "'>
	   <input style='width:15em' type='submit' value='All Group Chat Messages'>
	   </form>

	   <form method='post' action='m_user_chat_msg_selection.php?assembly_type=" . TEAM . "'>
	   <input type='hidden' name='access' value='" . $_POST['access'] . "'>
	   <input type='hidden' name='access_id' value='" . $_POST['access_id'] . "'>
	   <input style='width:15em' type='submit' value='User Team Chat Messages'>
	   </form>

	   <form method='post' action='m_user_chat_msg_selection.php?assembly_type=" . GROUP . "'>
	   <input type='hidden' name='access' value='" . $_POST['access'] . "'>
	   <input type='hidden' name='access_id' value='" . $_POST['access_id'] . "'>
	   <input style='width:15em' type='submit' value='User Group Chat Messages'>
	   </form>

	   <form method='post' action='m_class_chat_msg_selection.php?assembly_type=" . TEAM . "'>
	   <input type='hidden' name='access' value='" . $_POST['access'] . "'>
	   <input type='hidden' name='access_id' value='" . $_POST['access_id'] . "'>
	   <input style='width:15em' type='submit' value='Class Team Chat Messages'>
	   </form>

	   <form method='post' action='m_class_chat_msg_selection.php?assembly_type=" . GROUP . "'>
	   <input type='hidden' name='access' value='" . $_POST['access'] . "'>
	   <input type='hidden' name='access_id' value='" . $_POST['access_id'] . "'>
	   <input style='width:15em' type='submit' value='Class Group Chat Messages'>
	   </form>

           <form method='post' action='m_team_chat_msg_selection.php'>
	   <input type='hidden' name='access' value='" . $_POST['access'] . "'>
	   <input type='hidden' name='access_id' value='" . $_POST['access_id'] . "'>
	   <input style='width:15em' type='submit' value='Team Chat Messages'>
	   </form>

           <form method='post' action='m_group_chat_msg_selection.php'>
	   <input type='hidden' name='access' value='" . $_POST['access'] . "'>
	   <input type='hidden' name='access_id' value='" . $_POST['access_id'] . "'>
	   <input style='width:15em' type='submit' value='Group Chat Messages'>
	   </form>
	   </td>

	   <!-- TEXTPAD -->
	   <td bgcolor=" . TB_COLOR_2 . " valign=top>
           <form method='post' action='m_all_tp_input.php?assembly_type=" . TEAM . "'>
	   <input type='hidden' name='access' value='" . $_POST['access'] . "'>
	   <input type='hidden' name='access_id' value='" . $_POST['access_id'] . "'>
	   <input style='width:15em' type='submit' value='All Team Textpads'>
	   </form>

           <form method='post' action='m_all_tp_input.php?assembly_type=" . GROUP . "'>
	   <input type='hidden' name='access' value='" . $_POST['access'] . "'>
	   <input type='hidden' name='access_id' value='" . $_POST['access_id'] . "'>
	   <input style='width:15em' type='submit' value='All Group Textpads'>
	   </form>
	   </td>

	   <!-- SOUND -->
	   <td bgcolor=" . TB_COLOR_1 . " valign=top>
           <form method='post' action='m_all_sound_input.php?assembly_type=" . TEAM . "'>
	   <input type='hidden' name='access' value='" . $_POST['access'] . "'>
	   <input type='hidden' name='access_id' value='" . $_POST['access_id'] . "'>
	   <input style='width:15em' type='submit' value='All Team Sound Messages'>
	   </form>

           <form method='post' action='m_all_sound_input.php?assembly_type=" . GROUP . "'>
	   <input type='hidden' name='access' value='" . $_POST['access'] . "'>
	   <input type='hidden' name='access_id' value='" . $_POST['access_id'] . "'>
	   <input style='width:15em' type='submit' value='All Group Sound Messages'>
	   </form>
	   </td>

	   <!-- File Transfer -->
	   <td bgcolor=" . TB_COLOR_1 . " valign=top>
           <form method='post' action='m_all_ftp_input.php?assembly_type=" . TEAM . "'>
	   <input type='hidden' name='access' value='" . $_POST['access'] . "'>
	   <input type='hidden' name='access_id' value='" . $_POST['access_id'] . "'>
	   <input style='width:15em' type='submit' value='All Team FTP Files'>
	   </form>

           <form method='post' action='m_all_ftp_input.php?assembly_type=" . GROUP . "'>
	   <input type='hidden' name='access' value='" . $_POST['access'] . "'>
	   <input type='hidden' name='access_id' value='" . $_POST['access_id'] . "'>
	   <input style='width:15em' type='submit' value='All Group FTP Files'>
	   </form>
	   </td>

	   </tr>
	   </table>");
}

?>
