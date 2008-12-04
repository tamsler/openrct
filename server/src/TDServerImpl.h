/* $Id: */

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

#ifndef __TDSERVERIMPL_H__
#define __TDSERVERIMPL_H__

#include "TDServer.hh"
#include "ServerImpl.h"
#include "TDServerConst.h"
#include "TDMessageImpl.h"
#include "MakeSingle.h"
#include "SessionServerImpl.h"
#include "UserServerImpl.h"
#include "ClassServerImpl.h"
#include "TeamServerImpl.h"
#include "DatabaseServerImpl.h"

using namespace RCT;

class TDServer_i : public POA_RCT::TDServer,
		   public Server_i
{

 protected:

  // Constructor:
  inline TDServer_i() : Server_i(SM1) { }

  // Destructor:
  virtual inline ~TDServer_i() {}

 public:

  // IDL Method:
  virtual void load(const char* a_user_id,
		    const char* a_class_name,
		    const char* a_assembly_name,
		    TDSeq_out a_td_seq);

  // IDL Method:
  virtual void post_new_msg(const char* a_sender_id,
			    const char* a_sender_alias,
			    const char* a_class_name,
			    const char* a_team_name,
			    const CORBA::WChar* a_type,
			    const char* a_version,
			    const CORBA::WChar* a_subject,
			    const CORBA::WChar* a_text,
			    ObjPermission a_permission);

  // IDL Method:
  virtual void post_reply_msg(const char* a_sender_id,
			      const char* a_sender_alias,
			      const char* a_class_name,
			      const char* a_team_name,
			      const CORBA::WChar* a_type,
			      const char* a_version,
			      CORBA::Long a_parent_id,
			      const CORBA::WChar* a_subject,
			      const CORBA::WChar* a_text,
			      ObjPermission a_permission);

  // IDL Method:
  virtual void get_text(const char* a_user_id,
			const char *a_post_id,
			CORBA::Boolean a_is_read,
			CORBA::WString_out a_text);


};

typedef MakeSingle<TDServer_i> TDServerS;

#endif // __TDSERVERIMPL_H__
