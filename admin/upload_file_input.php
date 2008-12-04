<?php

// $Id: upload_file_input.php,v 1.3 2002/09/10 18:23:07 thomas Exp $

printf("<html>
        <body>
       
        <H1>Instructions:</H1><br>

        <font color='red'>Step 1:</font><br>
        First, click on the Browse button to select the file<br>
        you would like to upload.

        <form ENCTYPE='multipart/form-data' method='POST'
        action='upload_file_verify.php?module_type=" . $_GET['module_type'] . "&file_id=" . $_GET['file_id'] . "'>
        File: <INPUT TYPE='FILE' NAME='userfile' SIZE='35'>
        <input type='hidden' name='MAX_FILE_SIZE' value='5000000'>

        <br><br><br>

        <font color='red'>Step 2:</font><br>
        Then click on the Upload button to actually upload<br>
        the selected file. Please click only <b>once</b> and wait<br>
        for confirmation<br><br>
        <center>
        <input type='submit' value='Upload' name='B1'>
        </center>
        </form>
        </body>
        </html>");
?>

