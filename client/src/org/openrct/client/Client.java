// $Id: Client.java,v 1.7 2003/07/01 16:07:18 thomas Exp $

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

package org.openrct.client;

import org.openrct.client.module.ConfigModule;
import org.openrct.client.module.LangModule;

import org.openrct.client.module.PlatformModule;
import org.openrct.client.module.SessionModule;
import org.openrct.client.module.AuthModule;
import org.openrct.client.module.ChatModule;
import org.openrct.client.module.ClassModule;
import org.openrct.client.module.TeamModule;
import org.openrct.client.module.GroupModule;
import org.openrct.client.module.UserModule;
import org.openrct.client.module.FileModule;
import org.openrct.client.module.PingModule;
import org.openrct.client.module.CourseContentModule;
import org.openrct.client.module.SoundModule;
import org.openrct.client.module.ControlModule;
import org.openrct.client.module.TextpadModule;
import org.openrct.client.module.FtpModule;
import org.openrct.client.module.UrlModule;
import org.openrct.client.module.TDModule;
import org.openrct.client.gui.AuthModuleGui;
import javax.swing.*;
import java.util.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;

/**
 * Client is the main class that starts the client.
 * 
 * @author Thomas Amsler
 * @version $Id: Client.java,v 1.7 2003/07/01 16:07:18 thomas Exp $
 * @since OpenRCT 1.0.0
 */
public class Client implements Const {

	/**
	 * Client configuration.
	 */
	public static ConfigModule configModule = null;

	/**
	 * I18N.
	 */
	private static LangModule langModule_;

	/**
	 * Access to the ORB.
	 */
	public static org.omg.CORBA.ORB orb = null;

	/**
	 * Access to the Root POA.
	 */
	public static org.omg.PortableServer.POA rootPOA = null;

	/**
	 * Access to Nameserver Root Context.
	 */
	public static org.omg.CosNaming.NamingContext rootContext = null;

	/**
	 * Keep track of the main's aruments.
	 */
	private static String[] args_;

	/**
	 * Client's main method.
	 * 
	 * @param args
	 *            String array that holds the program arguments.
	 */
	public static void main(String[] args) {

		args_ = args;

		try {

			// Init the I18N setup
			String language;
			String country;

			// Chech the command line:
			// We can have either 2 or 3 arguments
			// Case 1:
			// java Client de DE
			// or
			// Case 2:
			// java Client de DE --no-test

			// Default Locale is en_US
			langModule_ = new LangModule(DEFAULT_LOC_PRE, DEFAULT_LOC_SUF);
			
			// Init Config Module
			configModule = new ConfigModule();

			// Test if the correct java version is present
			// Mac OS X has it's own java version. All other
			// Platforms are expected to use Sun's JDK
			if(!PlatformModule.hasCorrectJavaVersion()) {
				System.err.println("ERROR: You are using an unsupported Java version");
				System.exit(0);
			}
			
			// Test if OS Platform is supported
			if (!PlatformModule.isPlatformSupported()) {
				System.err.println("ERROR: Client uses unsupported Operating System");
				System.exit(0);
			}

			// Testing if client's IP is within the 169.254.X.Y range.
			if (PlatformModule.hasUnsupportedIP()) {
				System.err.println("ERROR: Client uses unsupported IP: 169.254.X.Y");
				System.exit(0);
			}

			// Load the Authentication window
			AuthModuleGui authModuleGui = new AuthModuleGui(AUTH_LEVEL_USER);
			
		} catch (Exception e) {

			System.out.println("EXCEPTION: Client.main()");
			e.printStackTrace();
		}
	}

	/**
	 * Initializes the ORB.
	 * 
	 * @return <code>void</code>
	 */
	public static void initCorba() {

		try {

			org.omg.CORBA.Object obj;

			// Tell the ORB to use jacorb's orb
			Properties p = System.getProperties();
			p.put("org.omg.CORBA.ORBClass", "org.jacorb.orb.ORB");
			p.put("org.omg.CORBA.ORBSingletonClass", "org.jacorb.orb.ORBSingleton");
			System.setProperties(p);

			// Initialize the ORB
			orb = ORB.init(args_, p);

			// Initialize RootPOA
			rootPOA = POAHelper.narrow(orb.resolve_initial_references(ROOT_POA));

			// Get the POA Manager
			org.omg.PortableServer.POAManager manager = rootPOA.the_POAManager();
			manager.activate();

			// Obtain Naming Service reference, root context
			obj = orb.resolve_initial_references(NAME_SERVICE);
			Client.rootContext = org.omg.CosNaming.NamingContextHelper.narrow(obj);

			// Init all the modules
			Client.initModules(Client.rootContext);

		} catch (Exception e) {

			JOptionPane.showMessageDialog(null,
					"The server is not running or there is a network problem!\n"
							+ "Please contact the OpenRCT team:\n"
							+ "http://www.openrct.org",
					"OpenRCT: Client Contacting Server ",
					JOptionPane.ERROR_MESSAGE);

			System.exit(0);
		}
	}

	/**
	 * Initializes all the modules.
	 * 
	 * @param nc
	 *            A NamingContext object
	 * @return <code>void</code>
	 */
	public static void initModules(org.omg.CosNaming.NamingContext nc) {

		try {

			SessionModule.init(nc);
			AuthModule.init(nc);
			ChatModule.init(nc);
			ClassModule.init(nc);
			TeamModule.init(nc);
			GroupModule.init(nc);
			UserModule.init(nc);
			FileModule.init(nc);
			PingModule.init(nc);
			CourseContentModule.init(nc);
			SoundModule.init(nc);
			ControlModule.init(nc);
			TextpadModule.init(nc);
			FtpModule.init(nc);
			UrlModule.init(nc);
			TDModule.init(nc);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Run the ORB in it's own thread.
	 * 
	 * @return <code>void</code>
	 */
	public static void runOrb() {

		Thread orbThread = new Thread(new Runnable() {

			public void run() {

				// Started The ORB Thread
				Client.orb.run();
			}
		});

		orbThread.start();
	}
}

