/* $Id: FileDownloadInfo.h,v 1.3 2003/05/08 20:34:43 thomas Exp $ */

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

#ifndef __FILEDOWNLOADINFO_H__
#define __FILEDOWNLOADINFO_H__


#include "RctCorba.h"
#include "Const.h"

class FileDownloadInfo
{

public:

    CORBA::String_var id;
    CORBA::String_var user_id;
    CORBA::String_var user_alias;
    CORBA::String_var class_name;
    CORBA::String_var assembly_name;
    CORBA::Long type;
    CORBA::Long packet_size;
    CORBA::Boolean is_stopped;

    // Constructor
    inline FileDownloadInfo(const char* a_id,
                            const char* a_user_id,
                            const char* a_user_alias,
                            const char* a_class_name,
                            const char* a_assembly_name,
                            CORBA::Long a_type,
                            CORBA::Long a_packet_size) 
    {
      
        id = a_id;
        user_id = a_user_id;
        user_alias = a_user_alias;
        class_name = a_class_name;
        assembly_name = a_assembly_name;
        type = a_type;
        packet_size = a_packet_size;
        is_stopped = 0;
    }
};

#endif // __FILEDOWNLOADINFO_H__
