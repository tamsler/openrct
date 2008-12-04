// $Id: ControlModule.java,v 1.5 2003/05/08 19:37:23 thomas Exp $

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

// Control Module
package org.openrct.client.module;

import org.openrct.client.Const;
import org.openrct.client.gui.PageGui;
import org.openrct.client.gui.ControlledModulePageGui;

public class ControlModule implements Const {

	// Access the control server
	private static RCT.ControlServer controlServer_ = null;

	// Constructor
	private ControlModule() {

		// Nothing to do here
	}

	// Init: Getting Control Server Ref.
	public static void init(org.omg.CosNaming.NamingContext nc) {

		try {
			org.omg.CORBA.Object obj;

			org.omg.CosNaming.NameComponent[] objectName = new org.omg.CosNaming.NameComponent[2];
			objectName[0] = new org.omg.CosNaming.NameComponent(RCT_ID,
					RCT_KIND);
			objectName[1] = new org.omg.CosNaming.NameComponent(CONTROL_ID,
					CONTROL_KIND);

			obj = nc.resolve(objectName);

			controlServer_ = RCT.ControlServerHelper.narrow(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Methods:
	// --------

	// Method:
	private static int getModuleType(String moduleName) {

		// Test only for controlled modules
		if (moduleName.equals(MODULE_SOUND)) {

			return MOD_SOUND;
		} else if (moduleName.equals(MODULE_WHITEBOARD)) {

			return MOD_WHITEBOARD;
		} else if (moduleName.equals(MODULE_TEXTPAD)) {

			return MOD_TEXTPAD;
		} else if (moduleName.equals(MODULE_URL)) {

			return MOD_URL;
		} else if (moduleName.equals(MODULE_FTP)) {

			return MOD_FTP;
		} else {

			System.err.println("ERROR: Did not recognize the module name!");

			return MOD_UNDEFINED;
		}
	}

	// Gui Wrapper Methods:
	// --------------------

	// Gui Wrapper Methdo:
	public synchronized static void displayRequestControl(String className,
			String assemblyName, String userAlias, String moduleName) {

		ControlledModulePageGui controlledPage = null;
		controlledPage = getPage(className, assemblyName, moduleName);

		if (null != controlledPage) {

			controlledPage.displayRequestControl(userAlias);
		}
	}

	// Gui Wrapper Methdo:
	public synchronized static void displayCancelRequest(String className,
			String assemblyName, String userAlias, String moduleName) {

		ControlledModulePageGui controlledPage = null;
		controlledPage = getPage(className, assemblyName, moduleName);

		if (null != controlledPage) {

			controlledPage.displayCancelRequest(userAlias);
		}
	}

	// Gui Wrapper Method:
	public synchronized static void displayReleaseControl(String className,
			String assemblyName, String userAlias, String moduleName) {

		ControlledModulePageGui controlledPage = null;
		controlledPage = getPage(className, assemblyName, moduleName);

		if (null != controlledPage) {

			controlledPage.displayReleaseControl(userAlias);
		}
	}

	// Gui Wrapper Method:
	public synchronized static void displayTakeControl(String className,
			String assemblyName, String userAlias, String moduleName) {

		ControlledModulePageGui controlledPage = null;
		controlledPage = getPage(className, assemblyName, moduleName);

		if (null != controlledPage) {

			controlledPage.displayTakeControl(userAlias);
		}
	}

	// Gui Wrapper Method:
	public synchronized static void displayActiveControl(String className,
			String assemblyName, String userAlias, String moduleName) {

		ControlledModulePageGui controlledPage = null;
		controlledPage = getPage(className, assemblyName, moduleName);

		if (null != controlledPage) {

			controlledPage.displayActiveControl(userAlias);
		}
	}

	// Gui Wrapper Method:
	public synchronized static void displayExitControl(String className,
			String assemblyName, String userAlias, String moduleName) {

		ControlledModulePageGui controlledPage = null;
		controlledPage = getPage(className, assemblyName, moduleName);

		if (null != controlledPage) {

			controlledPage.displayExitControl(userAlias);
		}
	}

	// Method:
	// Helper method to get the controlled module page
	private static ControlledModulePageGui getPage(String className,
			String assemblyName, String moduleName) {

		PageGui page = PageModule.getPage(className, assemblyName);

		if (null != page) {

			return (ControlledModulePageGui) page
					.getModulePage(getModuleType(moduleName));
		} else {

			return null;
		}
	}

	// IDL Wrapper Methods:
	// --------------------

	// IDL Wrapper Method:
	public static void request(String className, String assemblyName,
			String userAlias, String userId, String moduleName, int type) {

		controlServer_.request(className, assemblyName, userAlias, userId,
				moduleName, type);
	}

	// IDL Wrapper Method:
	public static void release(String className, String assemblyName,
			String userAlias, String userId, String moduleName, int type) {

		controlServer_.release(className, assemblyName, userAlias, userId,
				moduleName, type);
	}

	// IDL Wrapper Method:
	public static void take(String className, String assemblyName,
			String userAlias, String userId, String moduleName, int type)
			throws RCT.ControlServerPackage.QueueIsEmpty {

		controlServer_.take(className, assemblyName, userAlias, userId,
				moduleName, type);
	}

	// IDL Wrapper Method:
	public static void cancelRequest(String className, String assemblyName,
			String userAlias, String userId, String moduleName, int type) {

		controlServer_.cancel_request(className, assemblyName, userAlias,
				userId, moduleName, type);
	}

	// IDL Wrapper Method:
	public static void exitRequest(String className, String assemblyName,
			String userAlias, String userId, String moduleName, int type) {

		controlServer_.exit_request(className, assemblyName, userAlias, userId,
				moduleName, type);
	}
}

