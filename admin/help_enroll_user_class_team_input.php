<?php

// $Id: help_enroll_user_class_team_input.php,v 1.1 2002/11/08 19:17:03 thomas Exp $

require("common.php");

gen_help_html_header();

?>

<center><b><h1>HELP: Enroll User In Classes And Teams</b></h1></center>
<br><br>
<b>Step 1:</b><br>
In the Users' text area, enter all the user logins<br>
each one on a separate line.<br>
eg.<br>
student1<br>
student2<br>
student3<br>
<br>
<b>Step 2:</b><br>
Select as many classes in which you want to<br>
enroll the users.<br>
<br>
<b>Step 3:</b><br>
Select as many teams in which you want to<br>
enroll the users.<br>
Make sure that you enroll the users in the<br>
class to which the team belongs first or at<br>
the same time.<br>
<br>
<b>Step 4:</b><br>
Click on the Enroll button.<br>
<br>
<b>NOTE:</b><br>
You do not have to do all three steps all the<br>
time. You are allowd to execute the steps in<br>
the following orders.<br>
<ul>
<li>
<b>Scenario 1:</b><br>
You just want to enroll some students in a<br>
class.<br>
<ul><li>Steps 1, 2, and 4</li></ul>
</li>
<li>
<b>Scenario 2:</b><br>
You just want to enroll some students in a<br>
team, and the students have been prior<br>
enrolled in the team's class.<br>
<ul><li>Steps 1, 3, and 4</li></ul>
</li>
<li>
<b>Scenario 3:</b><br>
You want to enroll some students in a<br>
class as well as in a team.<br>
<ul><li>Steps 1, 2, 3, and 4</li></ul>
</li>
</ul>

<?php
gen_help_html_footer();
?>