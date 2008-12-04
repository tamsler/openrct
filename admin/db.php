<?php

// $Id: db.php,v 1.12 2002/11/07 20:56:07 thomas Exp $

class DB {

    // Private:
    // --------
    var $pr_host;
    var $pr_user;
    var $pr_password;
    var $pr_db_name;
    var $pr_port;
    var $pr_conn;


    // Public:
    // -------

    // Constructor:
    function DB() {

	$this->pr_host = "localhost";
	$this->pr_user = "postgres";
	$this->pr_password = "";
	$this->pr_db_name = "rctdb";
    }

    // Method: Connect to the DB
    function connect() {

	$this->pr_conn = @pg_connect("host=" . $this->pr_host .
				    " user=" . $this->pr_user .
				    " dbname=" . $this->pr_db_name);

	if(!$this->pr_conn) {

	    echo("ERROR: Was not able to connect to DB!");
	}
    }

    // Method: Close DB connection
    function close() {

	@pg_close($this->pr_conn);

	if(!$this->pr_conn) {

	    echo("ERROR: Was not able to close DB connection!");
	}
	
    }

    // Method: Execute query
    // User_id is used for transaction logging
    function exec($a_query, $user_id = "NONE") {

	// We only log changes to the DB
	if(eregi("UPDATE|DELETE|INSERT|DROP|CREATE", $a_query)) {

	    // Vars used to filter out quotes in query string
	    $pat = "'";
	    $repl = "";
	
	    // Creating the transaction
	    // Making sure there are no quotes in the a_query
	    $trans = "INSERT INTO rct_admin_transactions VALUES ('$user_id', 'now', '" . ereg_replace($pat, $repl, $a_query) . "')";

	    // Log transaction
	    $db_res = pg_query($this->pr_conn, $trans);

	    if(!$db_res) {

		echo("ERROR: DB transaction logging!");
	    }
	}
	
	return pg_query($this->pr_conn, $a_query);
    }


    // Method: Return the number of rows
    function get_num_rows($a_res) {

	return pg_num_rows($a_res);
    }


    // Method: Return the number of fields
    function get_num_fields($a_res) {

	return pg_num_fields($a_res);
    }

    // Method: Return row
    function fetch_row($a_res, $index) {

	return pg_fetch_row($a_res, $index);
    }

    // Method: Return field name
    function get_field_name($a_res, $index) {

	return pg_field_name($a_res, $index);
    }

    // Method: Returns the number of tuples (instances/records/rows)
    // affected by INSERT, UPDATE, and DELETE queries
    function affected_tuples($a_res) {

	return pg_affected_rows($a_res);

    }


    // Method: Free result
    function free_result($a_res) {

	return pg_free_result($a_res);
    }
    
    // Method: Check if result has one tuple
    function one_tuple($a_res) {

	if(1 == pg_num_rows($a_res)) {

	    return TRUE;
	}
	else {

	    return FALSE;
	}
    }

    // Method: Check if result has no tuples
    function zero_tuple($a_res) {

	if(0 == pg_num_rows($a_res)) {

	    return TRUE;
	}
	else {

	    return FALSE;
	}
	
    }
}

?>
