// $Id: Admin.java,v 1.3 2003/05/08 19:31:42 thomas Exp $

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

// This is the admin's main file
package org.openrct.client;

import org.openrct.client.Const;
import org.openrct.client.module.ConfigModule;
import org.openrct.client.module.LangModule;
import org.openrct.client.module.AuthModule;
import org.openrct.client.module.AdminModule;
import org.openrct.client.gui.AuthModuleGui;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;

public class Admin implements Const {

	// Config Module
	public static ConfigModule configModule = new ConfigModule();

	// I18N
	private static LangModule langModule_;

	// Access to the ORB
	public static org.omg.CORBA.ORB orb = null;

	// Access to the Root POA
	public static org.omg.PortableServer.POA rootPOA = null;

	// Access to Nameserver Root Context
	public static org.omg.CosNaming.NamingContext rootContext = null;

	private static String[] args_;

	// Main
	public static void main(String[] args) {

		args_ = args;

		try {

			// Default Locale is en_US
			if (args.length != 2) {

				langModule_ = new LangModule(DEFAULT_LOC_PRE, DEFAULT_LOC_SUF);
			} else {

				langModule_ = new LangModule(args[0], args[1]);
			}

			// Load the Authentication window
			AuthModuleGui authModuleGui = new AuthModuleGui(AUTH_LEVEL_ADMIN);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	// Class Methods:
	// -------------

	public static void initCorba() {

		try {

			org.omg.CORBA.Object obj;

			// Initialize the ORB
			orb = ORB.init(args_, null);

			// Initialize RootPOA
			rootPOA = POAHelper
					.narrow(orb.resolve_initial_references(ROOT_POA));

			// Get the POA Manager
			org.omg.PortableServer.POAManager manager = rootPOA
					.the_POAManager();
			manager.activate();

			// Obtain Naming Service reference, root context
			obj = orb.resolve_initial_references(NAME_SERVICE);
			Admin.rootContext = org.omg.CosNaming.NamingContextHelper
					.narrow(obj);

			// Init all the modules
			Admin.initModules(Admin.rootContext);

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	// Init all the modules
	public static void initModules(org.omg.CosNaming.NamingContext nc) {

		try {

			AuthModule.init(nc);
			AdminModule.init(nc);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Run the ORB in it's own thread
	public static void runOrb() {

		Thread orbThread = new Thread(new Runnable() {

			public void run() {

				// Started The ORB Thread
				Admin.orb.run();
			}
		});

		orbThread.start();
	}
}

