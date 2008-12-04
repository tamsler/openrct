<?php

// $Id: index.php,v 1.5 2002/05/14 16:59:50 thomas Exp $

// URL Vars:
// ---------
// login_input.php:	$permission, $person_type

require("common.php");

gen_html_header("RCT Admin Tool:");

// Note the "double" quotes after the macro string cat.
$str1 = "<a href='login_input.php?person_type=" . TYPE_ADMIN . "&permission=" . ADMIN . "' style='text-decoration:none'><font color='green'>Administrator</font></a>";
$str2 = "<a href='login_input.php?person_type=" . TYPE_MANAGER . "&permission=" . MANAGER. "' style='text-decoration:none'><font color='green'>Manager</font></a>";
$str3 = "<a href='login_input.php?person_type=" . TYPE_USER . "&permission=" . USER . "' style='text-decoration:none'><font color='green'>User</font></a>";


printf("
<table clellpadding=2 cellspacing=4 border=0 bgcolor='#B0C4DE'>
<tr>
<th>Administration<br>Level</th>
</tr>
<tr align='center'>
<td bgcolor='#F0F8FF'>$str1</td>
</tr>
<tr align='center'>
<td  bgcolor='#F0F8FF'>$str2</td>
</tr>
<tr align='center'>
<td  bgcolor='#F0F8FF'>$str3</td>
</tr>
</table>");

gen_menu_bar3("help_index.php");

gen_html_footer();

?>
