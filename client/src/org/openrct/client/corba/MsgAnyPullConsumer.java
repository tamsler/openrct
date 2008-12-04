// $Id: MsgAnyPullConsumer.java,v 1.4 2003/05/08 19:31:42 thomas Exp $

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
import org.openrct.client.module.ConfigModule;
import org.omg.CosNotifyChannelAdmin.*;
import org.omg.CORBA.*;

public class MsgAnyPullConsumer implements Const {

	private ProxyPullSupplier proxyPullSupplier_ = null;

	// Constructor
	public MsgAnyPullConsumer(EventChannel ec) {

		ConsumerAdmin consumerAdmin = ec.default_consumer_admin();

		org.omg.CORBA.IntHolder proxyId = new org.omg.CORBA.IntHolder();
		ProxySupplier proxySupplier = null;

		try {

			proxySupplier = consumerAdmin.obtain_notification_pull_supplier(
					ClientType.ANY_EVENT, proxyId);
		} catch (org.omg.CosNotifyChannelAdmin.AdminLimitExceeded ex) {

			System.err.println("Admin limit exceeded.");
			return;
		}

		try {

			proxyPullSupplier_ = ProxyPullSupplierHelper.narrow(proxySupplier);
		} catch (org.omg.CORBA.BAD_PARAM ex) {

			System.err.println("Unable to narrow proxy.");
			return;
		}

		try {

			proxyPullSupplier_.connect_any_pull_consumer(null);
		} catch (org.omg.CosEventChannelAdmin.AlreadyConnected ex) {

			System.err.println("Already connected.");
			return;
		}

		Thread msgAnyPullConsumerThread = new Thread(new Runnable() {

			BooleanHolder hasEventHolder = new BooleanHolder();

			boolean hasEvent = false;

			Any data = null;

			public void run() {

				while (true) {

					try {

						data = proxyPullSupplier_.try_pull(hasEventHolder);
						hasEvent = hasEventHolder.value;
					} catch (org.omg.CosEventComm.Disconnected ex) {

						System.err
								.println("EXCEPTION: org.omg.CosEventComm.Disconnected");
						ex.printStackTrace();
						return;
					}

					try {

						// If there wasen't an event, we sleep a little
						if (!hasEvent) {

							Thread.sleep(ConfigModule
									.getNumber(CONF_PULL_CONS_TIMEOUT));
						} else {

							// Take the any data and pass it on to the Event
							// handler
							MsgAnyConsumerHandler.dispatchEvent(data);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

					// Reset event flag
					hasEvent = false;
				}
			}
		});

		msgAnyPullConsumerThread.start();
	}

	// Methods:
	// --------

	// Method:
	public void logout() {

		try {

			proxyPullSupplier_.disconnect_pull_supplier();
		} catch (org.omg.CORBA.TRANSIENT ex) {
		} catch (org.omg.CORBA.COMM_FAILURE ex) {
		}
	}
}

