/* $Id: SoundServerConst.h,v 1.4 2003/05/08 20:34:43 thomas Exp $ */

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

#ifndef __SOUNDSERVERCONST_H__
#define __SOUNDSERVERCONST_H__

// Sound Server:
#define SOSM1           "SoundServer: OK"
#define SOSM2           "SoundServer: FAILED"
#define SOUNDSERVER     "SoundServer"

// Sound Message range 400-499
#define SOUND_TEAM                      401
#define SOUND_GROUP                     402
#define SOUND_TEAM_PLAY                 403
#define SOUND_GROUP_PLAY                404
#define SOUND_ARCHIVE_PLAY              405

// DB Queries
#define SELECT_SOUND_INFO               "select * from rct_files_view where file_module='sound' and \
                                         file_class_id='"


#endif // __SOUNDSERVERCONST_H__
