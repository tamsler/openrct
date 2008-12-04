<?php

// $Id: com_add_multiple_users_input.php,v 1.2 2002/11/06 19:23:33 thomas Exp $

require("common.php");

// This page allows the admin to add more then one user at a time

function gen_form($file1, $file2, $file3) {

    if(page_access_granted($_POST['access_id'], $_POST['access'])) {

	gen_html_header("Add Multiple USERS:");

	printf("
            <table border='1' cellpadding='10' bgcolor='#000000'>
            <tr>
            <td bgcolor=" . TB_COLOR_2 . " >
            <font face=Arial><b>%s</b></font><br>
            <font face=Arial><b>%s</b></font><br>
            <form name='forma' method='post' action='" . $file1. "'>
            <input type=text name=users value='" . $_POST['users'] . "' size='2' maxlength='2'>
            <input type=hidden name='access' value='" . $_POST['access'] . "'>
            <input type=hidden name='access_id' value='" .$_POST['access_id'] . "'>
            <input type=submit value='Create Users Input Form'>
            </form>
            </td>
            </tr>
            </table>",
	       "Please input the number of users that you would like to add.",
	       "Number of users has to be in the range of [1 - 50]:");

	// Check if the entered number lies between the defined bounds [1 . . 50]
	if($_POST['users'] && ($_POST['users'] > 0) && ($_POST['users'] < 51)) {

	    printf("
	       <form name='formb' method='post' action='" . $file2 . "'>
	       <table>

	       <tr>
    	       <td align='center'><b><font face='arial'>USER</font></b></td>
	       <td align='center'><b><font face='arial' color=" . TB_HEAD_1 . " >Login</font></b></td>
	       <td align='center'><b><font face='arial' color=" . TB_HEAD_1 . " >First Name</font></b></td>
	       <td align='center'><b><font face='arial' color=" . TB_HEAD_1 . " >Last Name</font></b></td>
	       <td align='center'><b><font face='arial' color=" . TB_HEAD_1 . " >Password</font></b></td>
	       <td align='center'><b><font face='arial' color=" . TB_HEAD_1 . " >Permission</font></b></td>
	       </tr>");


	    for($i = 0; $i < $_POST['users']; $i++) {

		printf("
                   <tr>
                   <td align='center'><b>$i</b></td>
                   <td><input type=text name=alias[] value='" . $_POST['alias'][$i] . "'></td>
                   <td><input type=text name=first_name[] value='" . $_POST['first_name'][$i] . "'></td>
                   <td><input type=text name=last_name[] value='" . $_POST['last_name'][$i] . "'></td>
                   <td><input type=text name=password[] value='" . $_POST['password'][$i] . "'></td>
                   <td>
                   <select name=permission[]>
		   <option value=0 selected>USER
		   <option value=1>MANAGER
		   </select>
                   </td>
                   </tr>");
	    }
	
	    printf("
	       <tr><td><br></td></tr>
	       <tr>
	       <td>
               <input type=submit value='Add Users'>
               <input type=hidden name='alias[]' value='" . $_POST['alias'] . "'>
               <input type=hidden name='first_name[]' value='" . $_POST['first_name'] . "'>
               <input type=hidden name='last_name[]' value='" . $_POST['last_name'] . "'>
               <input type=hidden name='password[]' value='" . $_POST['password'] . "'>
               <input type=hidden name='permission[]' value='" . $_POST['permission'] . "'>        
               <input type=hidden name='access' value='" . $_POST['access'] . "'>
	       <input type=hidden name='access_id' value='" . $_POST['access_id'] . "'>
               </td>
	       </tr>
	       </form>
	       </table>");
	}
    
	back_to_admin_tasks($_POST['access_id'], $_POST['access'], $file3);

	gen_menu_bar1();

	gen_html_footer();
    }
}
?>
