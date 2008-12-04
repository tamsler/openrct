// $Id: PlatformModule.java,v 1.6 2003/07/01 16:13:57 thomas Exp $

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

package org.openrct.client.module;

import org.openrct.client.Const;
import javax.swing.*;
import java.net.*;

public class PlatformModule implements Const {

	// Constructor
	private PlatformModule() {
	}

	// Method:
	public static String getOs() {

		return System.getProperty(PROP_OS);
	}

	// Method:
	public static String getIp() {

		String ipAddress = DEF_IP;

		try {

			ipAddress = InetAddress.getLocalHost().getHostAddress();
		} catch (Exception e) {

			e.printStackTrace();
		}

		return ipAddress;
	}

	// Method:
	// The os type is converted to lower case
	public static String getType() {

		return System.getProperty(PROP_OS).toLowerCase();
	}

	// Method:
	public static boolean isWindows95() {

		String osType = getType();

		if (osType.equals(OS_WIN95)) {
			return true;
		} else {
			return false;
		}
	}

	// Method:
	public static boolean isWindows98() {

		String osType = getType();

		if (osType.equals(OS_WIN98)) {
			return true;
		} else {
			return false;
		}
	}

	// Method:
	public static boolean isWindowsME() {

		String osType = getType();

		if (osType.equals(OS_WINME)) {
			return true;
		} else {
			return false;
		}
	}

	// Method:
	public static boolean isWindows() {

		String osType = getType();

		if (osType.startsWith(OS_WIN)) {
			return true;
		} else {
			return false;
		}
	}

	// Method:
	public static boolean isLinux() {

		String osType = getType();

		if (osType.startsWith(OS_LIN)) {
			return true;
		} else {
			return false;
		}
	}

	// Method:
	public static boolean isMacOsX() {

		String osType = getType();

		if (osType.startsWith(OS_MAC_X)) {
			return true;
		} else {
			return false;
		}
	}

	// Method:
	public static String getJavaVersion() {

		String javaVersion = System.getProperty("java.version");

		return javaVersion;
	}

	// Method:
	public static boolean javaVersionStartsWith(String prefix) {

		return getJavaVersion().startsWith(prefix);
	}

	// Method:
	// Test if the correct java version is present
	// Mac OS X has it's own java version. All other
	// Platforms are expected to use Sun's JDK
	public static boolean hasCorrectJavaVersion() {

		if(getJavaVersion().startsWith(JAVA_VER_EXCLUDE)){
			return false;
		}
		else {
			return true;
		}
		
		/*
		// Test for Mac OS X
		if (isMacOsX()) {

			for (int i = 0; i < JAVA_VER_MAC_OS_X.length; i++) {

				if (getJavaVersion().equals(JAVA_VER_MAC_OS_X[i])) {

					return true;
				}
			}

			JOptionPane.showMessageDialog(null, LangModule.i18n
					.getString("PlatformModuleMsg1")
					+ LangModule.i18n.getString("PlatformModuleMsg2")
					+ PlatformModule.getJavaVersion()
					+ LangModule.i18n.getString("PlatformModuleMsg3")
					+ JAVA_VER_MAC_OS_X[0], LangModule.i18n
					.getString("PlatformModuleTitle1"),
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		// Test for all others that use SUN's JDK
		else {

			for (int j = 0; j < JAVA_VER_SUN_JDK.length; j++) {

				if (getJavaVersion().equals(JAVA_VER_SUN_JDK[j])) {

					return true;
				}
			}

			JOptionPane.showMessageDialog(null, LangModule.i18n
					.getString("PlatformModuleMsg1")
					+ LangModule.i18n.getString("PlatformModuleMsg2")
					+ PlatformModule.getJavaVersion()
					+ LangModule.i18n.getString("PlatformModuleMsg3")
					+ JAVA_VER_SUN_JDK[0], LangModule.i18n
					.getString("PlatformModuleTitle1"),
					JOptionPane.ERROR_MESSAGE);

			return false;
		}
		*/
	}

	// Method:
	// Test if the client's platform is supported
	public static boolean isPlatformSupported() {

		for (int i = 0; i < SUPPORTED_PLATFORMS.length; i++) {

			if (getType().equals(SUPPORTED_PLATFORMS[i])) {

				return true;
			}
		}

		JOptionPane.showMessageDialog(null, LangModule.i18n
				.getString("PlatformModuleMsg4")
				+ LangModule.i18n.getString("PlatformModuleMsg5"),
				LangModule.i18n.getString("PlatformModuleTitle1"),
				JOptionPane.ERROR_MESSAGE);
		return false;
	}

	// Method:
	// Test for unsupported IPs such as 169.254.X.Y
	public static boolean hasUnsupportedIP() {

		for (int i = 0; i < UNSUPPORTED_IP_NUMS.length; i++) {

			if (getIp().startsWith(UNSUPPORTED_IP_NUMS[i])) {

				JOptionPane.showMessageDialog(null, "IP = " + getIp() + "\n"
						+ LangModule.i18n.getString("PlatformModuleMsg6")
						+ LangModule.i18n.getString("PlatformModuleMsg5"),
						LangModule.i18n.getString("PlatformModuleTitle1"),
						JOptionPane.ERROR_MESSAGE);

				return true;
			}
		}

		return false;
	}
}

