// $Id: ChatModule.java,v 1.5 2003/05/08 19:37:23 thomas Exp $

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

// Chat Module
package org.openrct.client.module;

import org.openrct.client.Const;
import org.openrct.client.gui.PageGui;
import org.openrct.client.gui.ChatPageGui;
import org.openrct.client.util.RctStyle;

public class ChatModule implements Const {

	// Access to the chat server
	private static RCT.ChatServer chatServer_ = null;

	// Constructor
	private ChatModule() {

		// Nothing to do here
	}

	// Init: Getting Chat Server Ref.
	public static void init(org.omg.CosNaming.NamingContext nc) {

		try {
			org.omg.CORBA.Object obj;

			org.omg.CosNaming.NameComponent[] objectName = new org.omg.CosNaming.NameComponent[2];
			objectName[0] = new org.omg.CosNaming.NameComponent(RCT_ID,
					RCT_KIND);
			objectName[1] = new org.omg.CosNaming.NameComponent(CHAT_ID,
					CHAT_KIND);

			obj = nc.resolve(objectName);

			chatServer_ = RCT.ChatServerHelper.narrow(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Dispatch Font Settings Change
	public static void dispatchFontChange() {

		PageGui pages[] = PageModule.getAllPages();

		for (int i = 0; i < pages.length; i++) {

			ChatPageGui chatPage = (ChatPageGui) pages[i]
					.getModulePage(MOD_CHAT);
			chatPage.dispatchFontChange();
		}
	}

	// IDL Wrapper Method:
	public static void send(String chatMsg, String version, String userId,
			String userAlias, RCT.ObjPermission permission, String className,
			String assemblyName, int type) {

		// Check for service restrictions
		if (!ServiceModule.allowInput(chatMsg)) {
			// Don't do anything yet
		}

		chatServer_.send(chatMsg, version, userId, userAlias, permission,
				className, assemblyName, type);
	}

	// IDL Wrapper Method:
	public static String getStatus() {

		return chatServer_.get_status();
	}

	// Gui Wrapper Method:
	public synchronized static void displayChatMsg(String className,
			String assemblyName, String userAlias, String chatMsg) {

		// If we get the chat message from current user,
		// we don't process it.
		if (UserModule.getAlias().equals(userAlias)) {

			return;
		}

		PageGui page = PageModule.getPage(className, assemblyName);

		ChatPageGui chatPage = null;

		if (null != page) {

			chatPage = (ChatPageGui) page.getModulePage(MOD_CHAT);
		} else {

			return;
		}

		if (null != chatPage) {

			// Display the chat message in the chat module
			chatPage.displayChatMsg("[" + userAlias + "]> " + chatMsg,
					RctStyle.REGULAR);

			// Display the chat message in the BC Channel
			SessionModule.displayModuleMsg(LangModule.i18n
					.getString("ChatModuleMsg1")
					+ " [ "
					+ className
					+ " : "
					+ assemblyName
					+ " : "
					+ userAlias + " ] > " + chatMsg);
		}
	}
}

