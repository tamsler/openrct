/* $Id: TextpadServerImpl.h,v 1.15 2003/05/08 20:34:43 thomas Exp $ */

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

#ifndef __TEXTPADSERVERIMPL_H__
#define __TEXTPADSERVERIMPL_H__

#include <iostream>
#include <wchar.h>
#include "TextpadServer.hh"
#include "PermissionObj.hh"
#include "TextpadServerConst.h"
#include "ServerImpl.h"
#include "UserServerImpl.h"
#include "ClassServerImpl.h"
#include "TeamServerImpl.h"
#include "GroupServerImpl.h"
#include "DatabaseServerImpl.h"
#include "Util.h"
#include "MakeSingle.h"


using namespace RCT;

class TextpadServer_i : public POA_RCT::TextpadServer,
                        public Server_i
{

private:

    // The pr_textpad_refs pointer is the actual memory location of the
    // textpad. We us an array of the size of max. allowed textpads
    CORBA::WChar *pr_textpad_refs[MAX_NUM_TEXTPADS];

    // The pr_textpad_ids holds the textpad id corresponding to the location
    // of the textpads text, which is storred in pr_textpad_refs[]
    CORBA::String_var pr_textpad_ids[MAX_NUM_TEXTPADS];

    // We keep track on how many textpads can be open concurrently
    CORBA::Long pr_textpad_counter;
    
protected:

    // Constructor:
    inline TextpadServer_i() : Server_i(SM1) { }

    // Destructor:
    virtual inline ~TextpadServer_i() { }


public:

    // Local Method:
    // Init the vars
    void init();

    // Local Method:
    // Find the first free textpad reference location
    CORBA::Long get_textpad_ref(void);

    // Local Method:
    // Find the location of the provided textpad_id
    // in the array of active textpads.
    CORBA::Long get_textpad_ref(const char *a_tp_id);

    // IDL Method:
    virtual void update(const CORBA::WChar *a_text,
                        CORBA::Long a_offset,
                        CORBA::Long a_textpad_ref,
                        const char *a_version,
                        const char *a_user_id,
                        const char *a_user_alias,
                        ObjPermission a_permission,
                        const char *a_class_name,
                        const char *a_assembly_name,
                        CORBA::Long a_type);

    // IDL Method:
    virtual CORBA::Boolean create(const CORBA::WChar *a_name,
                                  const char *a_version,
                                  const char *a_user_id,
                                  const char *a_user_alias,
                                  ObjPermission a_permission,
                                  const char *a_class_name,
                                  const char *a_assembly_name,
                                  CORBA::Long a_type);

    // IDL Method:
    virtual CORBA::Boolean edit(const char *a_id,
                                const char *a_version,
                                const char *a_user_id,
                                const char *a_user_alias,
                                ObjPermission a_permission,
                                const char *a_class_name,
                                const char *a_assembly_name,
                                CORBA::Long a_type);
    
    // IDL Meethod:
    virtual void close(const char *a_id,
                       CORBA::Long a_textpad_ref,
                       const char *a_version,
                       const char *a_user_id,
                       const char *a_user_alias,
                       ObjPermission a_permission,
                       const char *a_class_name,
                       const char *a_assembly_name,
                       CORBA::Long a_type);

    // IDL Method:
    virtual CORBA::Boolean is_active(const char *a_class_name,
                                     const char *a_assembly_name,
                                     CORBA::Long a_type,
                                     CORBA::WString_out a_name,
                                     CORBA::String_out a_id,
                                     CORBA::Long_out a_ref,
                                     CORBA::WString_out a_text);

    // IDL Method:
    virtual CORBA::Boolean get_text(const char *a_id,
                                    CORBA::WString_out a_text);
    
};

typedef MakeSingle<TextpadServer_i> TextpadServerS;

#endif // __TEXTPADSERVERIMPL_H__





