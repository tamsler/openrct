<?php

// $Id: help_add_course_content_input.php,v 1.3 2002/11/08 23:29:20 thomas Exp $

require("common.php");

gen_help_html_header();

?>

<center><b><h1>HELP: Add Course Content</b></h1></center>
<br><br>
<b>Name:</b>
<br>
This is a general name for the course content:<br>
eg.<br>
Homework Solution 1<br>
Google<br>
German Sound 1<br>
<br><br>

<b>File Name:</b>
<br>
Add a File Name only for Client and Internet sources.<br><br>
Client Source Examples:<br>
hw_sol1.pdf<br>
Bookreport.txt<br><br>
Internet Source Examples:<br>
http://www.google.com<br>
http://www.openrct.org<br>
<br><br>

<b>Location:</b>
<br>
Add a Location only for Client source.<br>
eg.<br>
c:\files<br>
/opt/homework<br>
<br><br>

<b>Source:</b>
<br>
Select the the course content's destination.
<br><br>

<b>Mime Type:</b>
<br>
The mime type describes the type of the course content. 
Please click on the "Types" link to get a complete list 
of all Mime Types.<br>
<br><br>

<b>Visible:</b>
<br>
If you want the clients to see the created course content, 
select Yes. otherwise select No.<br>
<br><br>

<b>Classes:</b>
<br>
Select a class to which you would like to attach the course content.
<br><br>

<b>Click on "Add Course Content"</b><br>
After you have entered all the information, please click 
on the "Add Course Content" button. On the next page,
you can upload a file if the SOURCE selection was of
type Server.

<?php

gen_help_html_footer();

?>