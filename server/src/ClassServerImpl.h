/* $Id: ClassServerImpl.h,v 1.11 2003/05/08 20:34:43 thomas Exp $ */

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

#ifndef __CLASSSERVERIMPL_H__
#define __CLASSSERVERIMPL_H__

#include <string>
#include <iostream>
#include "ClassServer.hh"
#include "ServerImpl.h"
#include "DatabaseServerImpl.h"
#include "Const.h"
#include "MakeSingle.h"


using namespace RCT;

class ClassServer_i : public POA_RCT::ClassServer,
                      public Server_i
{

protected:
	
    // Constructor:
    inline ClassServer_i() : Server_i(SM1) { }
    
    // Destructor:
    virtual inline ~ClassServer_i() { }

    
public:
	
    // IDL Method:
    // Returning a sequence of all classes
    virtual void get_all_classes(ClassSeq_out a_class_seq);

    // IDL Method:
    // Returning a class sequence for a certain user
    virtual void get_classes_from_user_id(const char *a_user_id,
                                          ClassSeq_out a_class_seq);

    // IDL Method:
    // Returning a class sequence for two users
    virtual void get_classes_from_user_ids(const char *a_user_id1,
                                           const char *a_user_id2,
                                           ClassSeq_out a_class_seq);

    // Local Method:
    char* get_class_id_from_class_name(const char *a_class_name);

    // Local Method:
    char* get_class_id_from_team_id(const char *a_team_id);

    // Local Method:
    char* get_class_id_from_group_id(const char *a_group_id);
    
    // Local Method:
    Class* get_class_from_class_id(const char *a_class_id);
};	

typedef MakeSingle<ClassServer_i> ClassServerS;

#endif // __CLASSSERVERIMPL_H__








