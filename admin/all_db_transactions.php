<?php

// $Id: all_db_transactions.php,v 1.4 2002/11/04 23:59:59 thomas Exp $

require("common.php");


if(page_access_granted($_POST['access_id'], $_POST['access'])) {

    gen_html_header("All DB Transactions:");

    display_all_db_transactions();

    back_to_admin_tasks($_POST['access_id'], $_POST['access']);

    gen_menu_bar1();
    
    gen_html_footer();
}


// Functions
// ---------

function display_all_db_transactions() {

    // Query
    $query = "select * from rct_admin_transactions";

    // Create a DB object
    $db = new DB();

    // Connect to the DB
    $db->connect();

    // Execute query
    $db_res = $db->exec($query);

    // Check for errors
    if (!$db_res) {
	
	disp_err_msg("ERROR: Could not get transactions from DB!");
	$db->close();
	unset($db);
	exit();
    }

    // Init vars
    $numrows = $db->get_num_rows($db_res);
    $numfields = $db->get_num_fields($db_res);

    // Check if there are any chat messages
    if(0 == $numrows) {

        disp_msg("There are not db transactoins");
	$db->close();
        unset($db);
        return;
    }
    
    printf ("<table border='1' cellpadding='5'>");

    printf("<tr>");

    for($i = 0; $i < $numfields; $i++) {

	printf("<td align='center'><b><font face='arial' color=" . TB_HEAD_1 . " >" . $db->get_field_name($db_res, $i) . "</font></b></td>");
    }

    printf("</tr>");

    for($i = 0; $i < $numrows; $i++) {

	$myrow = $db->fetch_row ($db_res, $i);

	if($i % 2) {

	    printf("<tr bgcolor=" . TB_COLOR_1 . " >");

	    for($j = 0; $j < $numfields; $j++) {

		printf ("<td>%s</td>", $myrow[$j]);
	    }

	    printf("</tr>");
	}
	else {

	    printf("<tr bgcolor=" . TB_COLOR_2 . " >");

	    for($j = 0; $j < $numfields; $j++) {

		printf ("<td>%s</td>", $myrow[$j]);
	    }

	    printf("</tr>");
	}

	unset($db_gc);
    }

    printf ("</table><br><br>");

    $db->close();
    unset($db);
}

?>
	      
