/* $Id: RctMutex.h,v 1.11 2003/05/16 16:08:25 thomas Exp $ */

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

#ifndef __RCTMUTEX_H__
#define __RCTMUTEX_H__

#include "RctCorba.h"
#include "TextpadServerConst.h"


class RctMutex
{

private:

public:

    // Authentication and Ping Server
    static omni_mutex auth;
    static omni_mutex auth_msg_buf;

    // Database Server
    static omni_mutex db_chat_index;
    static omni_mutex db_group_index;
    static omni_mutex db_file_index;
    static omni_mutex db_textpad_index;
    
    // Control Server
    static omni_mutex control;

    // User Server
    static omni_mutex user;

    // Team Server
    static omni_mutex team;
    
    // Group Server
    static omni_mutex group;

    // Session Server
    static omni_mutex session;

    // Textpad Server
    static omni_mutex textpad;
    static omni_mutex textpad_op[MAX_NUM_TEXTPADS];

    // File Server
    static omni_mutex file;
    
    // TD Server
    static omni_mutex td;
};

#endif // __RCTMUTEX_H__


