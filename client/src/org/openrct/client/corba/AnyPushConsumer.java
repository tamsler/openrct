// $Id: AnyPushConsumer.java,v 1.3 2003/05/08 19:31:42 thomas Exp $

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
import org.omg.CosNotification.*;
import org.omg.CosNotifyChannelAdmin.*;
import org.omg.CORBA.*;

public class AnyPushConsumer implements Const {

	// Constructor
	public AnyPushConsumer(ORB orb, EventChannel ec) {

		try {

			EventTypeSeqHelper etsh;

			ConsumerAdmin consumerAdmin = ec.default_consumer_admin();

			org.omg.CORBA.IntHolder proxyID = new org.omg.CORBA.IntHolder();

			ProxySupplier proxySupplier = null;

			try {
				proxySupplier = consumerAdmin
						.obtain_notification_push_supplier(
								ClientType.ANY_EVENT, proxyID);
			} catch (org.omg.CosNotifyChannelAdmin.AdminLimitExceeded e) {
				e.printStackTrace();
				return;
			}

			ProxyPushSupplier proxyPushSupplier = null;

			try {
				proxyPushSupplier = ProxyPushSupplierHelper
						.narrow(proxySupplier);
			} catch (org.omg.CORBA.BAD_PARAM e) {
				e.printStackTrace();
				return;
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
	}
}