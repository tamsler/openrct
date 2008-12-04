/* $Id: TDServerConst.h,v 1.13 2003/05/22 19:58:33 thomas Exp $ */

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

#ifndef __TDSERVERCONST_H__
#define __TDSERVERCONST_H__

// Threaded Discussion Server:
#define TDSM1      "TDServer: OK"
#define TDSM2      "TDServer: FAILED"
#define TDSERVER   "TDServer"

// Threaded Discussion Message range 1200-1299


// DB Queries
#define INSERT_TEMP_MSG "INSERT INTO rct_td ( \
                         td_parent_id, td_sender, td_class_id, td_team_id, td_subject, \
                         td_text, td_type, rct_date, rct_version) VALUES ( \
                         0, 'U0', 'C0', 'T0', '-', '-', '-', 'now', '-')"


#define GET_POST_ID_DATE "select max(td_post_id), rct_date from rct_td \
                          where td_post_id = (select max(td_post_id) from \
                          rct_td) group by rct_date;"

#define UPDATE_NEW_MSG   "UPDATE rct_td SET td_sender='"

#define UPDATE_REPLY_MSG "UPDATE rct_td SET td_parent_id='"

#define SELECT_LOAD      "select td_post_id, td_parent_id, alias, td_subject, \
                          td_type, rct_date, user_id from rct_td_msg_read_view where td_class_id='"

#define SELECT_TD_TEXT   "select td_text from rct_td where td_post_id='"

#define INSERT_READ_MSG  "INSERT INTO rct_td_read VALUES ('"

#endif // __TDSERVERCONST_H__
