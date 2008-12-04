// $Id: BcAnyPushConsumerImpl.java,v 1.4 2003/05/08 19:31:42 thomas Exp $

/*
 * 
 * OpenRCT - Open Remote Collaboration Tool
 * 
 * Copyright (c) 2000 by Thomas Amsler
 * 
 * This file is part of OpenRCT.
 * 
 * OpenRCT is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * OpenRCT is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * OpenRCT; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 *  
 */

package org.openrct.client.corba;

import org.openrct.client.Const;
import org.openrct.client.Client;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;
import org.omg.PortableServer.POAPackage.*;
import org.omg.CosNotifyComm.*;
import org.omg.CosNotification.*;

class BcAnyPushConsumerImpl extends PushConsumerPOA implements Const {

	private ORB orb_;

	private POA poa_;

	BcAnyPushConsumerImpl() {

		orb_ = Client.orb;
		poa_ = Client.rootPOA;
	}

	// IDL Methods
	// -----------

	public void push(Any data) throws org.omg.CosEventComm.Disconnected {

		BcAnyConsumerHandler.dispatchEvent(data);
	}

	// COS IDL interface
	public void disconnect_push_consumer() {

		byte[] oid = null;

		try {

			oid = poa_.servant_to_id(this);
		} catch (ServantNotActive e) {

			throw new RuntimeException();
		} catch (WrongPolicy e) {

			throw new RuntimeException();
		}

		try {

			poa_.deactivate_object(oid);
		} catch (ObjectNotActive e) {

			throw new RuntimeException();
		} catch (WrongPolicy e) {

			throw new RuntimeException();
		}

		orb_.shutdown(false);
	}

	// COS IDL interface
	public void offer_change(EventType[] added, EventType[] removed) {
		// Not implemented
	}

	// COS IDL interface
	public POA _default_POA() {

		return poa_;
	}
}