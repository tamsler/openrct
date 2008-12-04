// $Id: UrlModule.java,v 1.3 2003/05/08 19:37:23 thomas Exp $

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

// URL Module
package org.openrct.client.module;

import org.openrct.client.Const;
import org.openrct.client.gui.PageGui;
import org.openrct.client.gui.UrlPageGui;
import org.openrct.client.util.BrowserLauncher;
import javax.swing.*;

public class UrlModule implements Const {

	// Access to the url server
	private static RCT.UrlServer urlServer_ = null;

	// Constructor
	private UrlModule() {

		// Nothing to do here
	}

	// Init: Getting Url Server Ref.
	public static void init(org.omg.CosNaming.NamingContext nc) {

		try {
			org.omg.CORBA.Object obj;

			org.omg.CosNaming.NameComponent[] objectName = new org.omg.CosNaming.NameComponent[2];
			objectName[0] = new org.omg.CosNaming.NameComponent(RCT_ID,
					RCT_KIND);
			objectName[1] = new org.omg.CosNaming.NameComponent(URL_ID,
					URL_KIND);

			obj = nc.resolve(objectName);

			urlServer_ = RCT.UrlServerHelper.narrow(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// IDL Wrapper Method:
	public static void send(String url, String version, String userId,
			String userAlias, RCT.ObjPermission permission, String className,
			String assemblyName, int type) {

		urlServer_.send(url, version, userId, userAlias, permission, className,
				assemblyName, type);
	}

	// IDL Wrapper Method:
	public static String getStatus() {

		return urlServer_.get_status();
	}

	// Gui Wrapper Method:
	public synchronized static void displayUrlSendMsg(String userAlias,
			String className, String assemblyName, String url) {

		PageGui page = PageModule.getPage(className, assemblyName);

		UrlPageGui urlPage = null;

		if (null != page) {

			urlPage = (UrlPageGui) page.getModulePage(MOD_URL);
		} else {

			return;
		}

		if (null != urlPage) {

			urlPage.displayUrlSendMsg(userAlias, url);
		}
	}

	// Method:
	public static void openWebBrowser(String url) {

		// Windows
		if (PlatformModule.isWindows95() || PlatformModule.isWindows98()
				|| PlatformModule.isWindowsME()) {
			try {
				Runtime.getRuntime().exec(
						"rundll32 url.dll,FileProtocolHandler " + url);
			} catch (Exception e) {

				JOptionPane.showMessageDialog(null, LangModule.i18n
						.getString("UrlModuleDialog2")
						+ url, LangModule.i18n.getString("UrlModuleDialog1"),
						JOptionPane.ERROR_MESSAGE);
			}
		} else if (PlatformModule.isWindows()) {

			try {

				Runtime.getRuntime().exec("cmd /C start " + url);
			} catch (Exception e) {
			}
		}
		// Linux
		else if (PlatformModule.isLinux()) {

			try {

				Runtime.getRuntime().exec(URL_BROWSER_LINUX_1 + " " + url);
			} catch (Exception e) {
			}

			try {

				Runtime.getRuntime().exec(URL_BROWSER_LINUX_2 + " " + url);
			} catch (Exception e) {
			}

		}
		// Mac OS X
		else if (PlatformModule.isMacOsX()) {

			try {

				BrowserLauncher.openURL(url);
			} catch (Exception e) {
			}
		}
		// Other
		else {

			JOptionPane.showMessageDialog(null, LangModule.i18n
					.getString("UrlModuleDialog3")
					+ url, LangModule.i18n.getString("UrlModuleDialog1"),
					JOptionPane.ERROR_MESSAGE);
		}
	}

	// Method:
	public static boolean isValidUrl(String url) {

		java.net.URL test_url = null;
		java.net.URLConnection connection = null;
		;
		java.lang.Object obj = null;

		try {

			test_url = new java.net.URL(url);
			connection = test_url.openConnection();

			// If the URL is invalid, this should throw an exception
			obj = connection.getContent();
		} catch (Exception e) {

			return false;
		}

		if (null == obj) {

			return false;
		} else if (null == connection) {

			return false;
		} else {

			return true;
		}
	}
}