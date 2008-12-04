// $Id: SoundServerImpl.h,v 1.13 2003/05/08 20:34:43 thomas Exp $

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

#ifndef __SOUNDSERVER_H__
#define __SOUNDSERVER_H__

#include <iostream>
#include "SoundServer.hh"
#include "ServerImpl.h"
#include "SoundServerConst.h"
#include "SoundMessageImpl.h"
#include "FileServerImpl.h"
#include "ClassServerImpl.h"
#include "TeamServerImpl.h"
#include "GroupServerImpl.h"
#include "SessionServerImpl.h"
#include "PermissionObj.hh"
#include "Const.h"
#include "MakeSingle.h"


using namespace RCT;

class SoundServer_i : public POA_RCT::SoundServer,
                      public Server_i
{

protected:

    // Constructor:
    inline SoundServer_i() : Server_i(SM1) { }

    // Destructor
    virtual inline ~SoundServer_i() { }

public:

    // Local Method:
    void send(const char *a_id,
              const CORBA::WChar *a_alias,
              const char *a_name,
              const char *a_mime_type,
              const char *a_class_name,
              const char *a_assembly_name,
              const char *a_user_id,
              const char *a_user_alias,
              const char *a_version,
              const char *a_length,
              CORBA::Long a_type,
              ObjPermission a_permission);

    // IDL Method:
    virtual CORBA::Boolean fetch(const char *a_snd_id,
                                 BinaryFile_out a_file_data);

    // IDL Method:
    virtual void get_sound_info_from_class_and_assembly_name(const char *a_class_name,
                                                             const char *a_assembly_name,
                                                             CORBA::Long a_assembly_type,
                                                             SoundMsgHistSeq_out a_snd_seq);

};

typedef MakeSingle<SoundServer_i> SoundServerS;

#endif // __SOUNDSERVER_H__




