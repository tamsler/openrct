/* $Id: TextpadServerConst.h,v 1.5 2003/05/08 20:34:43 thomas Exp $ */

/*
 *
 *   OpenRCT - Open Remote Collaboration Tool
 *
 *   Copyright (c) 2000 by Thomas Amsler
 * 
 *   This file is part of OpenRCT.
 *
 *   OpenRCT is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   OpenRCT is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with OpenRCT; if not, write to the Free Software
 *   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

#ifndef __TEXTPADSERVERCONST_H__
#define __TEXTPADSERVERCONST_H__


#define TPSM1           "TextpadServer: OK"
#define TPSM2           "TextpadServer: FAILED"
#define TEXTPADSERVER   "TextpadServer"

#define MAX_NUM_TEXTPADS 20


// Textpad Message range 600-699
#define TEXTPAD_TEAM_CREATE             600
#define TEXTPAD_GROUP_CREATE            601
#define TEXTPAD_TEAM_CLOSE              602
#define TEXTPAD_GROUP_CLOSE             603
#define TEXTPAD_TEAM_REM_INS            604
#define TEXTPAD_GROUP_REM_INS           605
#define TEXTPAD_TEAM_INS                606
#define TEXTPAD_GROUP_INS               607
#define TEXTPAD_TEAM_REM                608
#define TEXTPAD_GROUP_REM               609
#define TEXTPAD_TEAM_EDIT               610
#define TEXTPAD_GROUP_EDIT              611

// DB Queries
#define SELECT_TEXTPAD_TEXT_FROM_ID     "select tp_text from rct_textpads where tp_id='"

#define SELECT_TEXTPAD_ALL_FROM_ID      "select * from rct_textpads where tp_id='"

#define SELECT_TEXTPAD                  "select tp_id, tp_name from rct_textpads where tp_class_id='"

#define UPDATE_TEXTPAD                  "UPDATE rct_textpads SET rct_date='now', tp_text='"

#define INS_TEXTPAD                     "INSERT INTO rct_textpads (tp_id, tp_name, tp_class_id, tp_team_id, \
                                         tp_group_id, tp_user_id, tp_permission, rct_date, rct_version) VALUES \
                                         ('"

#endif // __TEXTPADSERVERCONST_H__

   
