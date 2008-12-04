<?php

// $Id: u_modify_user_input.php,v 1.4 2002/09/10 20:01:05 thomas Exp $

require("common.php");


if(page_access_granted($_POST['access_id'], $_POST['access'])) {

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

    
    gen_html_header("Modify My User Profile:");

    printf("
           <form method='post' action='u_modify_user_verify.php'>
           <table>

	    <tr>
	    <td><b>First Name</b></td>
	    <td><input type=text name=first_name value='" . $_POST['first_name'] . "'></td>
	    </tr>

	    <tr>
	    <td><b>Last Name</b></td>
	    <td><input type=text name=last_name value='" . $_POST['last_name'] . "'></td>
	    </tr>

	    <tr>
	    <td><b><font face='arial'>Password: </font></b></td>
	    <td><input type=password name=new_password1></td>
            <td><font color='blue' face='arial'><b>(NOTE: Leave Password field blank if you do not want to change it.)<b></font></td>
	    </tr>

	    <tr>
	    <td><b><font face='arial'>Verify Password: </font></b></td>
	    <td><input type=password name=new_password2></td>
            <td><font color='blue' face='arial'><b>(NOTE: Leave Password field blank if you do not want to change it.)<b></font></td>
	    </tr>

	    <tr>
	    <td></td>
	    <td><br><input type=submit onclick='this.form.new_password1.value=encrypt_value(this.form.new_password1.value);
                                                this.form.new_password2.value=encrypt_value(this.form.new_password2.value);' value='Modify User'>
                    <input type=hidden name='user_id' value='" . $_POST['user_id'] . "'>
	            <input type=hidden name='access' value='" . $_POST['access'] . "'>
                    <input type=hidden name='access_id' value='" . $_POST['access_id'] . "'></td>
	    </tr>
            </form>
            <tr>
            <td></td>
	    <td><br><form method='post' action='user_tasks.php'>
                    <input type=submit value='Back To Admin Tasks'>
	            <input type=hidden name='access' value='" . $_POST['access'] . "'>
                    <input type=hidden name='access_id' value='" . $_POST['access_id'] . "'>
                    </form>
            </td>
	    </tr>
	    </table>");

    gen_menu_bar1();

    gen_html_footer();
}


// Functions
// ---------

?>

	