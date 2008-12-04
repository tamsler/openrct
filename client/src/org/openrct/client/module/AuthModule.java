// $Id: AuthModule.java,v 1.5 2003/05/08 19:37:23 thomas Exp $

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

// Auth Module
package org.openrct.client.module;

import org.openrct.client.Const;
import org.openrct.client.Client;
import org.openrct.client.Admin;
import org.openrct.client.util.MD5;
import java.io.*;

public class AuthModule implements Const {

	// Access to the authentication server
	private static RCT.AuthenticationServer authServer_ = null;

	// Save authentication response reason in case of error
	private static String authResponseReason_ = "";

	// String holding the login time/data
	private static String loginTime_;

	// Constructor
	private AuthModule() {

		// Nothing to do here
	}

	// Init: Getting Auth Server Ref.
	public static void init(org.omg.CosNaming.NamingContext nc) {

		try {
			org.omg.CORBA.Object obj;

			org.omg.CosNaming.NameComponent[] objectName = new org.omg.CosNaming.NameComponent[2];
			objectName[0] = new org.omg.CosNaming.NameComponent(RCT_ID,
					RCT_KIND);
			objectName[1] = new org.omg.CosNaming.NameComponent(AUTH_ID,
					AUTH_KIND);

			obj = nc.resolve(objectName);

			authServer_ = RCT.AuthenticationServerHelper.narrow(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Authentication process, verify userAlias and password
	public static boolean verifyUserAuth(String userAlias, String passwd,
			int authLevel) {

		String hexPassword;
		RCT.AuthResponse authResponse;
		MD5 hash;
		hash = new MD5(passwd);
		hexPassword = hash.asHex();

		// Call to server for authentication
		authResponse = authServer_.authenticate(userAlias, hexPassword,
				authLevel, RCT_VERSION);

		if (authResponse.granted) {

			// Right after we authenticated, we first need to setup
			// the user module
			UserModule.initUser(UserModule.getUserFromUserAlias(userAlias));

			// Create the data dir if it does not exist
			FileModule
					.createDirectory(new File(FileModule.getTempDataDirName()));

			// Start the session gui
			SessionModule.startSessionGui();

			// Init BC event channel
			SessionModule.initBcEventChannel(Client.rootContext);

			// Init User event channel
			SessionModule.initUserEventChannel(Client.rootContext);

			if (ConfigModule.can(CONF_FIREWALL_NAT)) {

				System.out
						.println("INFO: Enable Communication To Bypass Firewall NAT!");

				// Create the Broadcast Event Channel Any Pull Consumer
				SessionModule.initBcAnyPullConsumer();

				// Create the Module Message Event Channel Any Pull Consumer
				SessionModule.initMsgAnyPullConsumer();
			} else {

				System.out
						.println("INFO: Enable Communication Whithout Firewall NAT!");

				// Create the Broadcast Event Channel Any Push Consumer
				SessionModule.initBcAnyPushConsumer();

				// Create the Module Message Event Channel Any Push consumer
				SessionModule.initMsgAnyPushConsumer();
			}

			// Run ORB
			Client.runOrb();

			// Init the ping thread
			PingModule.initPingThread();

			return true;
		} else {

			authResponseReason_ = authResponse.reason;
			return false;
		}
	}

	// Authentication process, verify userAlias and password
	public static boolean verifyAdminAuth(String userAlias, String passwd,
			int authLevel) {

		String hexPassword;
		RCT.AuthResponse authResponse;
		MD5 hash;
		hash = new MD5(passwd);
		hexPassword = hash.asHex();

		// Call to server for authentication
		authResponse = authServer_.authenticate(userAlias, hexPassword,
				authLevel, RCT_VERSION);

		if (authResponse.granted) {

			// Start the admin gui
			// :TODO: At this point, we would start the Admin GUI ...

			// Start the orb
			Admin.runOrb();

			return true;
		} else {

			authResponseReason_ = authResponse.reason;
			return false;
		}
	}

	// Access to the auth. response reason
	public static String getAuthRespReason() {

		return authResponseReason_;
	}

	// IDL Wrapper Method:
	// This method lets everybody know that the user logged in
	// The data field is a client generated login message
	public static void login(int msgType, String userId, String userAlias,
			String firstName, String lastName, String data, String ip,
			String os, String version) {

		// Call the server's login method
		loginTime_ = authServer_.login(msgType, userId, userAlias, firstName,
				lastName, data, ip, os, version);

	}

	// IDL Wrapper Method:
	// This method lets everybody know that the user logged out
	// The data field is a client generated logout message
	public static void logout(int msgType, String userAlias, String password,
			String msg, String version) {

		authServer_.logout(msgType, userAlias, password, msg, loginTime_,
				version, NORMAL);
	}

	// IDL Wrapper Method:
	public static String getStatus() {

		return authServer_.get_status();
	}
}

