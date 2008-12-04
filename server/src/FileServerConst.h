/* $Id: FileServerConst.h,v 1.6 2004/10/23 06:06:45 thomas Exp $ */

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

#ifndef __FILESERVERCONST_H__
#define __FILESERVERCONST_H__

// File Server definitions:
#define FSM1            "FileServer: OK"
#define FSM2            "FileServer: FAILED"
#define FILESERVER      "FileServer"
#define PATH_TOKEN      "/"
#define DATA_DIR        "/opt/rct/data"
#define FILE_SERVER_SOURCE        "1"
#define FILE_CLIENT_SOURCE        "2"
#define FILE_WEB_SOURCE           "3"

#define MAX_NUMBER_DOWNLOADS      20 

// Packet size: Based on N * 1024
#define PACKET_SIZE_LAN           512000
#define PACKET_SIZE_DSL           102400
#define PACKET_SIZE_MOD           10240

// File Message range 1000-1999
#define FILE_DOWNLOAD_PACKET            1000
#define FILE_DOWNLOAD_END               1001
#define FILE_DOWNLOAD_STOPPED           1002

// DB Queries
#define SELECT_FILE_FROM_ID             "select * from rct_files where file_id='"
#define SELECT_CC_FILE_FROM_ID          "select * from rct_course_content where cc_id='"


#endif // __FILESERVERCONST_H__
