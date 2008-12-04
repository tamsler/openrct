<?php

// $Id: m_all_course_content_input.php,v 1.6 2003/03/31 21:53:30 thomas Exp $

require("com_all_course_content_input.php");
    
// Query
$query = "select distinct
          CC.cc_id as \"ID\",
          CC.cc_alias as \"Alias\",
          CC.cc_name as \"Name\",
          CC.cc_location as \"Location\",
          CC.cc_source as \"Source\",
          CC.cc_mime_type as \"Mime Type\",
          C.class_name as \"Class Name\",
          CC.cc_visible as \"Visible\",
          CC.cc_permission as \"Permission\",
          CC.cc_length as \"Size\",
          CC.rct_date as \"Date\",
          CC.rct_version as \"Version\"
          from rct_course_content CC, rct_classes C where
          CC.cc_class_id=C.class_id and
          C.manager='" . $_POST['access_id'] . "'";

gen_form("m_modify_course_content_input.php",
	 "m_delete_course_content_verify.php",
	 "manager_tasks.php",
	 $query);

?>

