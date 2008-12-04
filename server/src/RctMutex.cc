/* $Id: RctMutex.cc,v 1.10 2003/05/16 16:08:24 thomas Exp $ */

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

#include "RctMutex.h"

// Authentication And Ping Server
omni_mutex RctMutex::auth;
omni_mutex RctMutex::auth_msg_buf;

// Database Server
omni_mutex RctMutex::db_chat_index;
omni_mutex RctMutex::db_group_index;
omni_mutex RctMutex::db_file_index;
omni_mutex RctMutex::db_textpad_index;

// Control Server
omni_mutex RctMutex::control;

// User Server
omni_mutex RctMutex::user;

// Team Server
omni_mutex RctMutex::team;

// Group Server
omni_mutex RctMutex::group;

// Session Server
omni_mutex RctMutex::session;

// Textpad Server
omni_mutex RctMutex::textpad;
omni_mutex RctMutex::textpad_op[MAX_NUM_TEXTPADS];

// File Server
omni_mutex RctMutex::file;

// TD Server
omni_mutex RctMutex::td;
