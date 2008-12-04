/* $Id: CourseContentServerImpl.h,v 1.7 2003/05/08 20:34:43 thomas Exp $ */

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

#ifndef __COURSECONTENTSERVERIMPL_H__
#define __COURSECONTENTSERVERIMPL_H__

#include <iostream>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>
#include "CourseContentServer.hh"
#include "CourseContentServerConst.h"
#include "ServerImpl.h"
#include "ClassServerImpl.h"
#include "DatabaseServerImpl.h"
#include "FileServerImpl.h"
#include "Const.h"
#include "MakeSingle.h"


using namespace RCT;

class CourseContentServer_i : public POA_RCT::CourseContentServer,
                              public Server_i
{

protected:

    // Constructor:
    inline CourseContentServer_i() : Server_i(SM1) { }

    // Destructor
    virtual inline ~CourseContentServer_i() { }

    
public:

    // IDL Method:
    // Returning a course content sequence for a certain class
    virtual void get_course_content_from_class_name(const char *a_class_name,
                                                    CourseContentSeq_out a_cc_seq);

    // IDL Method:
    virtual CORBA::Boolean fetch(const char *a_cc_id,
                                 BinaryFile_out a_file_data);
    
};

typedef MakeSingle<CourseContentServer_i> CourseContentServerS;

#endif // __COURSECONTENTSERVERIMPL_H__









