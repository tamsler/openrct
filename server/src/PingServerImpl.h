/* $Id: PingServerImpl.h,v 1.8 2003/05/08 20:34:43 thomas Exp $ */

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

#ifndef __PINGSERVERIMPL_H__
#define __PINGSERVERIMPL_H__

#include <string>
#include <map>
#include <iostream>
#include "RctCorba.h"
#include "RctMutex.h"
#include "ServerImpl.h"
#include "PingServer.hh"
#include "PingServerConst.h"
#include "SessionServerImpl.h"
#include "DatabaseServerImpl.h"
#include "AuthenticationServerImpl.h"
#include "UserServerImpl.h"
#include "MakeSingle.h"


using namespace std;

typedef map<string, char, less<string> > PingMap;
typedef pair<map<string, char>::iterator, bool> PingMap_Res;
typedef map<string, char>::iterator PingMap_Iter;


class PingServer_i : public POA_RCT::PingServer,
                     public Server_i
{

private:

    // Ping map from userid(key) to online_status(value)
    PingMap pr_ping_map;
    PingMap_Res pr_pmap_res;
    PingMap_Iter pr_pmap_iter;

    // Thread id variable for the check users online status thread
    pthread_t p_tid;

protected:

    // Constructor:
    inline PingServer_i() : Server_i(SM1) { }
    
    // Destructor
    virtual inline ~PingServer_i() { }

    
public:

    // Local Method:
    // Sets the ping map value for a particular key
    void set_map_value(const char* a_user_id, const char a_online_status);

    // Local Method:
    // Returns the ping map value for a particular key  
    char get_map_value(const char* a_user_id);

    // Local Method:
    void create_ping_thread();
    
    // IDL Method
    virtual void ping(const char* a_user_id);

    // IDL Method:
    virtual void bandwidth(const BinaryPacket &a_packet);

};

// Thread method that checks the online status of all users periodically
// and updates the database for those who have crashed
void* check_user_online_status(void* a_arg);
    
typedef MakeSingle<PingServer_i> PingServerS;


#endif // __PINGSERVERIMPL_H__








