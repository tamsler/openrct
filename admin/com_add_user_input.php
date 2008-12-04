<?php

// $Id: com_add_user_input.php,v 1.4 2002/11/06 19:23:33 thomas Exp $

require("common.php");

function gen_form($file1, $file2) {

    if(page_access_granted($_POST['access_id'], $_POST['access'])) {
    
	// Create a user object
	$db_user = new DBUser();
    
	// JavaScript used to encode the password before we send it
	printf("<script language='JavaScript' src='md5.js'></script>

            <script language='JavaScript'>

            function encrypt_value(pw) {

                 if (pw != \"\") {
                    return calcMD5(pw);
                 }
                 else {
                    return pw;
                 }
            }

            </script>");

	gen_html_header("Add a new USER:");

	printf("
           <form method='post' action='" . $file2 . "'>
           <table>

            <tr>
	    <td><b><font face='arial'>Login: </font></b></td>
	    <td><input type=text name=alias value='" . $_POST['alias'] . "'></td>
	    </tr>

	    <tr>
	    <td><b><font face='arial'>First Name: </font></b></td>
	    <td><input type=text name=first_name value='" . $_POST['first_name'] . "'></td>
	    </tr>

	    <tr>
	    <td><b><font face='arial'>Last Name: </font></b></td>
	    <td><input type=text name=last_name value='" . $_POST['last_name'] . "'></td>
	    </tr>

	    <tr>
	    <td><b><font face='arial'>Password: </font></b></td>
	    <td><input type=password name=password1></td>
	    </tr>

	    <tr>
	    <td><b><font face='arial'>Verify Password: </font></b></td>
	    <td><input type=password name=password2></td>
	    </tr>

	    <tr>
	    <td><b><font face='arial'>Permission: </font></b></td>
	    <td>%s</td>
	    </tr>

	    <tr>
	    <td></td>
	    <td><br><input type=submit onclick='this.form.password1.value=encrypt_value(this.form.password1.value);
                                                this.form.password2.value=encrypt_value(this.form.password2.value);' value='Add User'>
	            <input type=hidden name='access' value='" . $_POST['access'] . "'>
                    <input type=hidden name='access_id' value='" . $_POST['access_id'] . "'></td>
	    </tr>
            </form>
            <tr>
            <td></td>
	    <td><br><form method='post' action='". $file1 ."'>
                    <input type=submit value='Back To Admin Tasks'>
	            <input type=hidden name='access' value='" . $_POST['access'] . "'>
                    <input type=hidden name='access_id' value='" . $_POST['access_id'] . "'>
                    </form>
            </td>
	    </tr>
	    </table>", $db_user->select_permission($_POST['permission'],
	               $db_user->is_admin($_POST['access_id'])));

	gen_menu_bar1();

	gen_html_footer();

	// Freeup memory
	unset($db_user);
    }
}

?>