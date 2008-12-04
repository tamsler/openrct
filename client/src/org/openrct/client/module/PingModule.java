// $Id: PingModule.java,v 1.4 2003/05/08 19:37:23 thomas Exp $

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

// Ping Module
package org.openrct.client.module;

import org.openrct.client.Const;

public class PingModule implements Const {

	// Access to the ping server
	private static RCT.PingServer pingServer_ = null;

	// Constructor
	private PingModule() {

		// Nothing to do here
	}

	// Init: Getting Ping Server Ref.
	public static void init(org.omg.CosNaming.NamingContext nc) {

		try {
			org.omg.CORBA.Object obj;

			org.omg.CosNaming.NameComponent[] objectName = new org.omg.CosNaming.NameComponent[2];
			objectName[0] = new org.omg.CosNaming.NameComponent(RCT_ID,
					RCT_KIND);
			objectName[1] = new org.omg.CosNaming.NameComponent(PING_ID,
					PING_KIND);

			obj = nc.resolve(objectName);

			pingServer_ = RCT.PingServerHelper.narrow(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Init the Ping Thread
	// With each ping, we send the alias along
	public static void initPingThread() {

		Thread pingThread = new Thread(new Runnable() {

			public void run() {

				while (true) {

					// Letting server know that client is still alive
					PingModule.ping();

					// Sleep for some time
					try {

						// NOTE: Argument is milliseconds
						Thread.sleep(PING_LOOP_SLEEP);
					} catch (InterruptedException e) {
					}
				}
			}
		});

		// Start thread
		pingThread.start();
	}

	// Method:
	public static void testBandwidth() {

		// LAN: 29-150
		// DSL:
		// MOD: 641-711
		if (testRate(10240, 500)) {

			ConfigModule.set(CONF_CONNECTION_SPEED, PACKET_SIZE_MOD);
		}
		// LAN: 150-701
		// DSL:
		// MOD: 4456-4466
		else if (testRate(102400, 900)) {

			ConfigModule.set(CONF_CONNECTION_SPEED, PACKET_SIZE_DSL);
		} else {

			ConfigModule.set(CONF_CONNECTION_SPEED, PACKET_SIZE_LAN);
		}
	}

	// Method:
	// Test Modem Bandwidth
	public static boolean testRate(int packetSize, int rate) {

		// Prepare packet
		byte[] packet = new byte[packetSize];

		long startTime = System.currentTimeMillis();
		PingModule.bandwidth(packet);
		long endTime = System.currentTimeMillis();

		long msec = endTime - startTime;

		// If msec is less then rate, bandwidth is greater than expected
		if (msec < rate) {
			return false;
		} else {
			return true;
		}
	}

	// IDL Wrapper Method:
	public static void ping() {

		try {

			pingServer_.ping(UserModule.getId());
		} catch (Exception e) {

			SessionModule.displayServerCannotBeReached();
		}
	}

	// IDL Wrapper Method:
	private static void bandwidth(byte[] packet) {

		pingServer_.bandwidth(packet);
	}
}

