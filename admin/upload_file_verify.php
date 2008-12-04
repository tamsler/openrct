<?php

// $Id: upload_file_verify.php,v 1.6 2003/03/31 21:50:10 thomas Exp $

require("common.php");

if(0 == strcmp($_GET['module_type'], "course-content")) {

    upload(MODULE_PREFIX_CC);
}


// Functions
// =========

// Some file upload options are set in php.ini
function upload($file_mod_prefix) {

    if(is_uploaded_file($_FILES['userfile']['tmp_name'])) {

	$filename = $_FILES['userfile']['tmp_name'];

	$realname = $_FILES['userfile']['name'];

	$new_file_name = $file_mod_prefix . $_GET['file_id'];
	
	if(copy($_FILES['userfile']['tmp_name'], RCT_SERVER_DATA_DIR . "/" . $new_file_name)) {
	  
	    $file_size = filesize(RCT_SERVER_DATA_DIR . "/" . $new_file_name);

	    printf("File upload [ %s ] : <font color='green'>SUCCESSFUL</font>", $realname);

	    // Now we update the file name in the db
	    $query = "";

	    // Compose string per modules
	    if(0 == strcmp($_GET['module_type'], "course-content")) {

		$query = "UPDATE rct_course_content SET cc_name='" . $realname . "', 
                          cc_length='" . $file_size . "' where cc_id='" . $_GET['file_id'] . "'" ;
	    }

	    // Create a DB object
	    $db = new DB();

	    // Connect to the DB
	    $db->connect();

	    // Execute transaction
	    $db_res = $db->exec($query);

	    // Close DB connection
	    $db->close();
	    unset($db);
	}
	else {

	   printf("File upload [ %s ] : <font color='red'>FAILED</font>", $realname);
	}

    } else {

	printf("Possible file upload attack: filename %s.<br>", $_FILES['userfile']['name']);
    }

    printf("<br><br><br>
            <center>
            <form>
            <input type=submit onclick=window.close() value='Close'>
            </form>
            </center>");
}

?>


