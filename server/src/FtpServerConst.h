/* $Id: FtpServerConst.h,v 1.7 2003/05/08 20:34:43 thomas Exp $ */

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

#ifndef __FTPSERVERCONST_H__
#define __FTPSERVERCONST_H__

// Ftp Server:
#define FTPSM1          "FtpServer: OK"
#define FTPSM2          "FtpServer: FAILED"
#define FTPSERVER       "FtpServer"


// FTP Message range 800-899
#define FTP_TEAM_UPLOAD         800
#define FTP_GROUP_UPLOAD        801
#define FTP_TEAM_DOWNLOAD       802
#define FTP_GROUP_DOWNLOAD      803
#define FTP_ARCHIVE_DOWNLOAD    804

// DB Queries
#define SELECT_FTP_INFO         "select * from rct_files_view where file_module='ftp' and \
                                 file_class_id='"

#endif // __FTPSERVERCONST_H__
