/* $Id: SessionServerConst.h,v 1.2 2003/05/08 20:34:43 thomas Exp $ */

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

#ifndef __SESSIONSERVERCONST_H__
#define __SESSIONSERVERCONST_H__

// Session Server definitions:
#define SSM1            "SessionServer: OK" 
#define SSM2            "Sessionserver: FAILED"
#define SESSIONSERVER   "SessionServer"

// DB Queries
#define UPDATE_SERVER_STATUS    "UPDATE rct_server_status SET rct_date='now'"


#endif // __SESSIONSERVERCONST_H__
