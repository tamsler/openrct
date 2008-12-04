/* $Id: FileServerImpl.h,v 1.20 2003/05/08 20:34:43 thomas Exp $ */

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

#ifndef __FILESERVERIMPL_H__
#define __FILESERVERIMPL_H__

#include <fstream>
#include <iostream>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>
#include "FileServer.hh"
#include "FileDownloadInfo.h"
#include "FileServerConst.h"
#include "PermissionObj.hh"
#include "ServerImpl.h"
#include "ClassServerImpl.h"
#include "TeamServerImpl.h"
#include "GroupServerImpl.h"
#include "DatabaseServerImpl.h"
#include "SoundServerImpl.h"
#include "FtpServerImpl.h"
#include "Const.h"
#include "MakeSingle.h"


using namespace RCT;


class FileServer_i : public POA_RCT::FileServer,
                     public Server_i
{

 private:
  
  FileDownloadInfo* pr_file_download_info_refs[MAX_NUMBER_DOWNLOADS];

  // Local Method:
  CORBA::Boolean store_file_download_info(FileDownloadInfo* a_fdi);

 protected:

    // Constructor:
    inline FileServer_i() : Server_i(SM1) { }

    // Destructor
    virtual inline ~FileServer_i() { }
    
public:

    // Local Method:
    // Init the vars
    void init();

    // Local Method:
    // Get's the file with info from db
    CORBA::Boolean fetch(const char *a_file_id,
                         BinaryFile_out a_file_data,
                         FileAccess a_type = RCT_FA_COMMON);
    
    // Local Method:
    // Receives the file and puts it into the database
    CORBA::Boolean send(const char *a_id,
                        const CORBA::WChar *a_alias,
                        const char *a_name,
                        const char *a_source,
                        const char *a_mime_type,
                        const char *a_class_name,
                        const char *a_assembly_name,
                        const char *a_user_id,
                        const char *a_user_alias,
                        const char *a_version,
                        const char *a_module,
                        CORBA::Long a_assembly_type,
                        ObjPermission a_permission,
                        CORBA::Boolean a_has_id,
                        const BinaryFile &a_file_data);

    // IDL Method:
    // Get a DB file index
    virtual char* get_index();

    // IDL Method:
    virtual void start_transfer(const char *a_name)
        throw(CORBA::SystemException, RCT::FileServer::FileIOException);
                                
    // IDL Method:
    virtual void end_transfer(const char *a_id,
                              const CORBA::WChar *a_alias,
                              const char *a_name,
                              const char *a_mime_type,
                              const char *a_class_name,
                              const char *a_assembly_name,
                              const char *a_user_id,
                              const char *a_user_alias,
                              const char *a_version,
                              const char *a_module,
                              CORBA::Long a_type,
                              ObjPermission a_permission,
			      const char *a_length);

    // IDL Method:
    virtual void send_packet(const char *a_name,
                             const FilePacket &a_packet)
        throw(CORBA::SystemException, RCT::FileServer::FileIOException);

     // IDL Method:
    virtual void cancel_transfer(const char *a_name)
      throw(CORBA::SystemException, RCT::FileServer::FileIOException);

    // IDL Method:
    virtual void start_download(const char *a_id,
				const char *a_user_id,
				const char *a_user_alias,
                                const char *a_class_name,
                                const char *assembly_name,
				const char *a_version,
				CORBA::Long a_type,
				CORBA::Long a_packet_size)
      throw(CORBA::SystemException, RCT::FileServer::FileIOException);

    // IDL Method:
    virtual void cancel_download(const char *a_id,
                                 const char *a_user_id,
                                 const char *a_class_name,
                                 const char *a_assembly_name)
      throw(CORBA::SystemException, RCT::FileServer::FileIOException);
};

// Thread method that chunks and sends download data for files
void * chunk_and_send_data(void* a_arg);

// Thread method that chunks and sends download data for course content
void * chunk_and_send_data_cc(void* a_arg);

typedef MakeSingle<FileServer_i> FileServerS;

#endif // __FILESERVERIMPL_H__










