// $Id: TextpadModule.java,v 1.4 2003/05/08 19:37:23 thomas Exp $

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

// TEXTPAD Module
package org.openrct.client.module;

import org.openrct.client.Const;
import org.openrct.client.gui.PageGui;
import org.openrct.client.gui.TextpadEditList;
import org.openrct.client.gui.TextpadPageGui;
import org.omg.CORBA.*;

public class TextpadModule implements Const {

	// Access to the textpad server
	private static RCT.TextpadServer textpadServer_ = null;

	// Hash Table to access textpad edit list
	public static TextpadEditList editList = new TextpadEditList();

	// Constructor
	private TextpadModule() {

		// Nothing to do here
	}

	// Init: Getting Textpad Server Ref.
	public static void init(org.omg.CosNaming.NamingContext nc) {

		try {
			org.omg.CORBA.Object obj;

			org.omg.CosNaming.NameComponent[] objectName = new org.omg.CosNaming.NameComponent[2];
			objectName[0] = new org.omg.CosNaming.NameComponent(RCT_ID,
					RCT_KIND);
			objectName[1] = new org.omg.CosNaming.NameComponent(TP_ID, TP_KIND);

			obj = nc.resolve(objectName);

			textpadServer_ = RCT.TextpadServerHelper.narrow(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Dispatch Font Settings Change
	public static void dispatchFontChange() {

		PageGui pages[] = PageModule.getAllPages();

		for (int i = 0; i < pages.length; i++) {

			TextpadPageGui textpadPage = (TextpadPageGui) pages[i]
					.getModulePage(MOD_TEXTPAD);
			textpadPage.dispatchFontChange();
		}

	}

	// IDL Wrapper Method:
	public static String getStatus() {

		return textpadServer_.get_status();
	}

	// IDL Wrapper Method:
	public static void update(String text, int offset, int textpadRef,
			String version, String userId, String userAlias,
			RCT.ObjPermission permission, String className,
			String assemblyName, int type) {

		// Check for service restrictions
		if (!ServiceModule.allowInput(text)) {
			// Don't do anything yet
		}

		textpadServer_.update(text, offset, textpadRef, version, userId,
				userAlias, permission, className, assemblyName, type);
	}

	// IDL Wrapper Method:
	public static boolean create(String name, String version, String userId,
			String userAlias, RCT.ObjPermission permission, String className,
			String assemblyName, int type) {

		return textpadServer_.create(name, version, userId, userAlias,
				permission, className, assemblyName, type);
	}

	// IDL Wrapper Method:
	public static boolean edit(String textpadId, String version, String userId,
			String userAlias, RCT.ObjPermission permission, String className,
			String assemblyName, int type) {

		return textpadServer_.edit(textpadId, version, userId, userAlias,
				permission, className, assemblyName, type);
	}

	// IDL Wrapper Method:
	public static void close(String textpadId, int textpadRef, String version,
			String userId, String userAlias, RCT.ObjPermission permission,
			String className, String assemblyName, int type) {

		textpadServer_.close(textpadId, textpadRef, version, userId, userAlias,
				permission, className, assemblyName, type);
	}

	// IDL Wrapper Method:
	public static boolean isActive(String className, String assemblyName,
			int type, StringHolder name, StringHolder id, IntHolder ref,
			StringHolder text) {

		return textpadServer_.is_active(className, assemblyName, type, name,
				id, ref, text);
	}

	// IDL Wrapper Method:
	public static boolean getText(String id, StringHolder text) {

		return textpadServer_.get_text(id, text);
	}

	// Gui Wrapper Method:
	public synchronized static void displayCreate(String className,
			String assemblyName, String userAlias, String name, int ref,
			String id) {

		TextpadPageGui textpadPage = null;
		textpadPage = getPage(className, assemblyName);

		if (null != textpadPage) {

			textpadPage.displayCreate(userAlias, name, ref, id);
		}
	}

	// Gui Wrapper Method:
	public synchronized static void displayEdit(String className,
			String assemblyName, String userAlias, String name, int ref,
			String id, String text) {

		TextpadPageGui textpadPage = null;
		textpadPage = getPage(className, assemblyName);

		if (null != textpadPage) {

			textpadPage.displayEdit(userAlias, name, ref, id, text);
		}
	}

	// Gui Wrapper Method:
	public synchronized static void displayClose(String className,
			String assemblyName) {

		TextpadPageGui textpadPage = null;
		textpadPage = getPage(className, assemblyName);

		if (null != textpadPage) {

			textpadPage.displayClose();
		}
	}

	// Gui Wrapper Method:
	public synchronized static void displayRemIns(String className,
			String assemblyName, String userAlias, int offset, String text) {

		TextpadPageGui textpadPage = null;
		textpadPage = getPage(className, assemblyName);

		if (null != textpadPage) {

			textpadPage.displayRemIns(userAlias, offset, text);
		}
	}

	// Gui Wrapper Method:
	public synchronized static void displayIns(String className,
			String assemblyName, String userAlias, int offset, String text) {

		TextpadPageGui textpadPage = null;
		textpadPage = getPage(className, assemblyName);

		if (null != textpadPage) {

			textpadPage.displayIns(userAlias, offset, text);
		}
	}

	// Gui Wrapper Method:
	public synchronized static void displayRem(String className,
			String assemblyName, String userAlias, int offset, String text) {

		TextpadPageGui textpadPage = null;
		textpadPage = getPage(className, assemblyName);

		if (null != textpadPage) {

			textpadPage.displayRem(userAlias, offset, text);
		}
	}

	// Method:
	// Helper method to get the controlled module page
	private static TextpadPageGui getPage(String className, String assemblyName) {

		PageGui page = PageModule.getPage(className, assemblyName);

		if (null != page) {

			return (TextpadPageGui) page.getModulePage(MOD_TEXTPAD);
		} else {

			return null;
		}
	}
}

