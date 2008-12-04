// $Id: TDModule.java,v 1.10 2003/05/22 23:38:30 thomas Exp $

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

// Threaded Discussion Module
package org.openrct.client.module;

import org.openrct.client.Const;
import org.openrct.client.gui.PageGui;
import org.openrct.client.gui.TDPageGui;
import org.openrct.client.util.Utility;
import org.omg.CORBA.*;

public class TDModule implements Const {

	// Access to the TD Server
	private static RCT.TDServer tdServer_ = null;

	// Constructor
	private TDModule() {

		// Nothing to do here
	}

	// Methods:
	// --------

	// Method:
	// Init: Getting TD Server Ref.
	public static void init(org.omg.CosNaming.NamingContext nc) {

		try {
			org.omg.CORBA.Object obj;

			org.omg.CosNaming.NameComponent[] objectName = new org.omg.CosNaming.NameComponent[2];
			objectName[0] = new org.omg.CosNaming.NameComponent(RCT_ID,
					RCT_KIND);
			objectName[1] = new org.omg.CosNaming.NameComponent(TD_ID, TD_KIND);

			obj = nc.resolve(objectName);

			tdServer_ = RCT.TDServerHelper.narrow(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Gui Wrapper Method:
	// Dispatch display post msg
	public static void displayPostMsg(String userAlias, String className,
			String teamName, int postId, int parentId, String subject,
			String type, String date) {

		PageGui page = PageModule.getPage(className, teamName);

		TDPageGui tdPage = null;

		if (null != page) {

			tdPage = (TDPageGui) page.getModulePage(MOD_TD);
		} else {

			return;
		}

		if ((null != tdPage) && tdPage.canReceiveMsg()) {

			tdPage.displayPostMsg(userAlias, postId, parentId, subject, type,
					Utility.getDateAndTime(date));
		}
	}

	// IDL Wrapper Methods:
	// --------------------

	// IDL Wrapper Method:
	public static void load(String userId, String className,
			String assemblyName, RCT.TDSeqHolder tdSeq) {

		tdServer_.load(userId, className, assemblyName, tdSeq);
	}

	// IDL Wrapper Method:
	public static void postNewMsg(String senderId, String senderAlias,
			String className, String teamName, String type, String version,
			String subject, String text, RCT.ObjPermission permission) {

		tdServer_.post_new_msg(senderId, senderAlias, className, teamName,
				type, version, subject, text, permission);
	}

	// IDL Wrapper Method:
	public static void postReplyMsg(String senderId, String senderAlias,
			String className, String teamName, String type, String version,
			int parentId, String subject, String text,
			RCT.ObjPermission permission) {

		tdServer_.post_reply_msg(senderId, senderAlias, className, teamName,
				type, version, parentId, subject, text, permission);
	}

	// IDL Wrapper Method:
	public static String getStatus() {

		return tdServer_.get_status();
	}

	// IDL Wrapper Method:
	public static void getText(String userId, String postId, boolean isRead,
			StringHolder text) {

		tdServer_.get_text(userId, postId, isRead, text);
	}
}