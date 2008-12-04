<?php

// $Id: login_input.php,v 1.14 2002/05/17 23:00:31 thomas Exp $


require("common.php");


// Checking if the values are set
if($_GET['person_type']) {
    
    gen_html_header($_GET['person_type'] . " Login:");
}
else {

    // Reset to lowest access level,
    // user level
    $_GET['permission'] = USER;
    $_GET['person_type'] = TYPE_USER;
    gen_html_header($_GET['person_type'] . " Login:");
}

// JavaScript used to encode the password before we send it
printf("<script language='JavaScript' src='md5.js'></script>

        <script language='JavaScript'>

        function encrypt_value(pw) {
        
        return calcMD5(pw);
        }

</script>");


printf("
<form method='post' action='login_verify.php'>
<table>

<tr>
<td><b>Login</b></td>
<td><input type=text name=alias value='" . $_POST['alias'] . "'></td>
</tr>

<tr>
<td><b>Password</b></td>
<td><input type=password name=password></td>
</tr>

<tr>
<td></td>
<td><br><input type=submit onclick='this.form.password.value=encrypt_value(this.form.password.value);' value='Login'></td>
</tr>
</table>
<input type=hidden name=person_type value='" . $_GET['person_type'] . "'>
<input type=hidden name=permission value='" . $_GET['permission'] . "'>
</form>");

gen_menu_bar3();

gen_html_footer();

// Functions
// ---------

?>

