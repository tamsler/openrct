// $Id: ConfigModule.java,v 1.5 2003/07/07 17:02:05 thomas Exp $

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
import java.net.*;
import java.io.*;
import java.util.*;

// The Class deals with the clients config file

public class ConfigModule implements Const {

	// Section Sound Module
	static private boolean canRecordSound_;

	static private boolean canPlaySound_;

	static private boolean canStopSound_;

	static private boolean canRingBellOnMsgSound_;

	static private int maxRecordingTimeSound_;

	// Section Chat Module
	static private boolean canRingBellOnMsgChat_;

	// Section Textpad Module
	static private boolean canRingBellOnActiveTextpad_;

	// Section Url Module
	static private boolean canRingBellOnMsgUrl_;

	// Section Ftp Module
	static private boolean canRingBellOnMsgFtp_;

	// Section Client System
	static private int maxActionTimerSleepSys_;

	static private boolean keepDataDirSys_;

	static private int pullConsumerTimeout_;

	// Section Networking
	static private String serverName_;

	static private String serverPort_;

	static private String serverIp_;

	// Section Unicode
	static private String unicodeFont_;

	static private int unicodeFontSize_;

	// Section Session Module
	static private boolean canRingBellOnMsgSession_;

	static private boolean usingFireWallNAT_;

	static private int connectionSpeed_;

	// Section Team Module
	static private boolean canRingBellOnMsgTeam_;

	// Section Group Module
	static private boolean canRingBellOnMsgGroup_;

	// Service Module
	static private int inputLength_;

	static private int fileSize_;

	// Constructor
	public ConfigModule() {

		// INIT
		initConfigFiles();

		// Section Sound Module
		// Since Mac OS X Java 1.3.1 does not support sound recording,
		// we disable it here. Also Java 1.4.1_01 is still buggy
		// PlatformModule.javaVersionStartsWith(JAVA_1_3_1)
		if (PlatformModule.isMacOsX()) {

			canRecordSound_ = false;
		}
		// There are still problems with sound recording on Linux
		else if (PlatformModule.isLinux()) {

			canRecordSound_ = false;
		} else {

			canRecordSound_ = true;
		}

		canPlaySound_ = true;
		canStopSound_ = true;
		canRingBellOnMsgSound_ = true;
		maxRecordingTimeSound_ = CONF_SND_MAX_REC_TIME;

		// Section Chat Module
		canRingBellOnMsgChat_ = true;

		// Section Textpad Module
		canRingBellOnActiveTextpad_ = true;

		// Section Url Module
		canRingBellOnMsgUrl_ = true;

		// Section Ftp Module
		canRingBellOnMsgFtp_ = true;

		// Section Client System
		keepDataDirSys_ = false;
		maxActionTimerSleepSys_ = CONF_ACTION_TIMER_SLEEP;
		pullConsumerTimeout_ = CONF_PULL_CONS_SLEEP;

		// Section Networking
		serverIp_ = extractOrbProperties(SERVER_IP);
		serverName_ = ipToName(serverIp_);
		serverPort_ = extractOrbProperties(SERVER_PORT);

		// Section Unicode
		unicodeFont_ = UNICODE_FONT;
		unicodeFontSize_ = UNICODE_FONT_SIZE;

		// Section Session Module
		canRingBellOnMsgSession_ = true;
		usingFireWallNAT_ = false;
		connectionSpeed_ = PACKET_SIZE_LAN;

		// Section Team Module
		canRingBellOnMsgTeam_ = true;

		// Section Group Module
		canRingBellOnMsgGroup_ = true;

		// Service Module
		inputLength_ = RCT_INPUT_LENGTH;
		fileSize_ = RCT_FILE_SIZE;

	}

	// Methods:
	// --------

	// Methdo:
	// Get the config value
	public static boolean can(int config) {

		switch (config) {

		case CONF_SND_RECORD:

			return canRecordSound_;

		case CONF_SND_PLAY:

			return canPlaySound_;

		case CONF_SND_STOP:

			return canStopSound_;

		case CONF_SND_RING_BELL:

			return canRingBellOnMsgSound_;

		case CONF_SYS_KEEP_DATA_DIR:

			return keepDataDirSys_;

		case CONF_CHAT_RING_BELL:

			return canRingBellOnMsgChat_;

		case CONF_SESSION_RING_BELL:

			return canRingBellOnMsgSession_;

		case CONF_TEXTPAD_RING_BELL:

			return canRingBellOnActiveTextpad_;

		case CONF_URL_RING_BELL:

			return canRingBellOnMsgUrl_;

		case CONF_FTP_RING_BELL:

			return canRingBellOnMsgFtp_;

		case CONF_TEAM_RING_BELL:

			return canRingBellOnMsgTeam_;

		case CONF_GROUP_RING_BELL:

			return canRingBellOnMsgGroup_;

		case CONF_FIREWALL_NAT:

			return usingFireWallNAT_;

		default:

			System.err.println("ERROR: Wrong Config Value In CAN Method!");
			return false;
		}
	}

	// Method:
	// Setting config values
	public static void set(int config, boolean value) {

		switch (config) {

		case CONF_SND_RECORD:

			canRecordSound_ = value;
			break;

		case CONF_SND_PLAY:

			canPlaySound_ = value;
			break;

		case CONF_SND_STOP:

			canStopSound_ = value;
			break;

		case CONF_SND_RING_BELL:

			canRingBellOnMsgSound_ = value;
			break;

		case CONF_SYS_KEEP_DATA_DIR:

			keepDataDirSys_ = value;
			break;

		case CONF_CHAT_RING_BELL:

			canRingBellOnMsgChat_ = value;
			break;

		case CONF_SESSION_RING_BELL:

			canRingBellOnMsgSession_ = value;
			break;

		case CONF_TEXTPAD_RING_BELL:

			canRingBellOnActiveTextpad_ = value;
			break;

		case CONF_URL_RING_BELL:

			canRingBellOnMsgUrl_ = value;
			break;

		case CONF_FTP_RING_BELL:

			canRingBellOnMsgFtp_ = value;
			break;

		case CONF_TEAM_RING_BELL:

			canRingBellOnMsgTeam_ = value;
			break;

		case CONF_GROUP_RING_BELL:

			canRingBellOnMsgGroup_ = value;
			break;

		case CONF_FIREWALL_NAT:

			usingFireWallNAT_ = value;
			break;

		default:

			System.err
					.println("ERROR: Wrong Config Value In SET(Boolean) Method!");
		}
	}

	// Method:
	// Setting config values Integers
	public static void set(int config, int value) {

		switch (config) {

		case CONF_SND_MAX_REC_TIME:

			maxRecordingTimeSound_ = value;
			break;

		case CONF_ACTION_TIMER_SLEEP:

			maxActionTimerSleepSys_ = value;
			break;

		case CONF_UNICODE_FONT_SIZE:

			unicodeFontSize_ = value;
			break;

		case CONF_INPUT_LENGTH:

			inputLength_ = value;
			break;

		case CONF_FILE_SIZE:

			fileSize_ = value;
			break;

		case CONF_PULL_CONS_TIMEOUT:

			pullConsumerTimeout_ = value;
			break;

		case CONF_CONNECTION_SPEED:

			connectionSpeed_ = value;

			if (PACKET_SIZE_LAN == value) {

				SessionModule.changeConnectionSpeed(PACKET_SIZE_LAN);
			} else if (PACKET_SIZE_DSL == value) {

				SessionModule.changeConnectionSpeed(PACKET_SIZE_DSL);
			} else if (PACKET_SIZE_MOD == value) {

				SessionModule.changeConnectionSpeed(PACKET_SIZE_MOD);
			}
			break;

		default:
			System.err.println("ERROR: Wrong Config Value In SET(Int) Method!");
		}
	}

	// Method:
	// Setting config values Strings
	public static void set(int config, String value) {

		switch (config) {

		case CONF_UNICODE_FONT:

			unicodeFont_ = value;
			break;

		default:
			System.err
					.println("ERROR: Wrong Config Value In Set(String) Method!");
		}
	}

	// Method:
	// Getting config values
	public static int getNumber(int config) {

		switch (config) {

		case CONF_SND_MAX_REC_TIME:

			return maxRecordingTimeSound_;

		case CONF_ACTION_TIMER_SLEEP:

			return maxActionTimerSleepSys_;

		case CONF_UNICODE_FONT_SIZE:

			return unicodeFontSize_;

		case CONF_INPUT_LENGTH:

			return inputLength_;

		case CONF_FILE_SIZE:

			return fileSize_;

		case CONF_PULL_CONS_TIMEOUT:

			return pullConsumerTimeout_;

		case CONF_CONNECTION_SPEED:

			return connectionSpeed_;

		default:

			System.err
					.println("ERROR: Wrong Config Value In getNumber Method!");
			return ERROR;
		}
	}

	// Method:
	// Getting config values
	public static String getString(int config) {

		switch (config) {

		case CONF_UNICODE_FONT:

			return unicodeFont_;

		default:

			System.err
					.println("ERROR: Wrong Config Value in getString Method!");
			return null;
		}
	}

	// Method:
	// Get Server Name
	public static String getServerName() {

		return serverName_;
	}

	// Get Server Name Without Domain
	public static String getServerNameWithoutDomain() {

		StringTokenizer st = new StringTokenizer(serverName_, ".");

		String name = st.nextToken();

		return name;
	}

	// Method:
	// Get Server Port
	public static String getServerPort() {

		return serverPort_;
	}

	// Method:
	// Get Server IP
	public static String getServerIp() {

		return serverIp_;
	}

	// Method:
	// Set Server Name
	public static boolean setServerName(String serverName) {

		serverName_ = serverName;
		serverIp_ = nameToIp(serverName);
		return insert();
	}

	// Method:
	// Set Server Port
	public static boolean setServerPort(String serverPort) {

		serverPort_ = serverPort;
		return insert();
	}

	// Method:
	// Changes the server name and server port number in the
	// jacorb.properties file
	private static boolean insert() {

		boolean hasError = false;

		try {

			File inFile = new File(CONFIG_DIR, CONFIG_FILE_ORB);
			File outFile = new File(CONFIG_DIR, CONFIG_FILE_TMP);

			// In the case where there is an out file, we delete it
			if (outFile.exists()) {

				outFile.delete();
			}

			BufferedReader in = new BufferedReader(new FileReader(inFile));
			PrintWriter out = new PrintWriter(new FileWriter(outFile));

			String line;

			while ((line = in.readLine()) != null) {

				if (line.startsWith("ORBInitRef.NameService=corbaloc")) {

					out.println("ORBInitRef.NameService=corbaloc::" + serverIp_
							+ ":" + serverPort_ + "/NameService");
				} else {

					out.println(line);
				}
			}

			in.close();
			out.close();

			if (inFile.exists()) {

				inFile.delete();
			}

			outFile.renameTo(inFile);
		} catch (Exception e) {

			hasError = true;
		}

		// Check if we had any errors
		if (hasError) {

			return false;
		} else {

			return true;
		}
	}

	// Method:
	// Extracts configuration values from jacorb.properties
	private static String extractOrbProperties(int segment) {

		String line = "";
		String ip = "";
		String serverName = "";
		String port = "";
		int str_loc1 = 0;
		int str_loc2 = 0;
		int str_loc3 = 0;
		int str_loc4 = 0;

		try {

			BufferedReader in = new BufferedReader(new FileReader(new File(
					CONFIG_DIR, CONFIG_FILE_ORB)));

			while ((line = in.readLine()) != null) {
				if (line.startsWith("ORBInitRef.NameService=corbaloc")) {
					break;
				}
			}

			// ORBInitRef.NameService=corbaloc::192.168.1.1:3350/NameService
			//                                  ^
			for (int i = 0; i < line.length(); i++) {

				if (line.charAt(i) == ':') {
					str_loc1 = i + 2;
					break;
				}
			}

			// ORBInitRef.NameService=corbaloc::192.168.1.1:3350/NameService
			//                                            ^ ^
			for (int j = str_loc1; j < line.length(); j++) {

				if (line.charAt(j) == ':') {
					str_loc2 = j;
					str_loc3 = j + 1;
					break;
				}
			}

			// ORBInitRef.NameService=corbaloc::192.168.1.1:3350/NameService
			//                                                 ^
			for (int k = str_loc3; k < line.length(); k++) {

				if (line.charAt(k) == '/') {
					str_loc4 = k;
					break;
				}
			}

			ip = line.substring(str_loc1, str_loc2);
			port = line.substring(str_loc3, str_loc4);
			serverName = InetAddress.getByName(ip).getHostName();

			in.close();
		} catch (Exception e) {
		}

		if (SERVER_IP == segment) {

			return ip;
		} else if (SERVER_NAME == segment) {

			return serverName;
		} else if (SERVER_PORT == segment) {

			return port;
		} else {

			System.err.println("ERROR: unknown segment request!");
			return "";
		}
	}

	private static String ipToName(String ip) {

		String name = "";

		try {

			InetAddress host = InetAddress.getByName(ip);

			name = host.getHostName();
		} catch (Exception e) {
		}

		return name;
	}

	private static String nameToIp(String name) {

		String ip = "";

		try {

			InetAddress host = InetAddress.getByName(name);

			ip = host.getHostAddress();
		} catch (Exception e) {
		}

		return ip;
	}

	// Method:
	private static void initConfigFiles() {

		// Copy the Config files to the user's home directory
		// File: jacorb.properties
		// File: openrct.properties

		File rctConfigFile = new File(CONFIG_DIR, CONFIG_FILE_RCT);

		File orbConfigFile = new File(CONFIG_DIR, CONFIG_FILE_ORB);

		// First we test if openrct.properties exists and if it is
		// of the current version
		if (!isCurrentVersion(rctConfigFile, orbConfigFile)) {

			if (!FileModule.copy(new File(PROPERTIES_DIR + CONFIG_FILE_RCT), 
										  rctConfigFile)) {

				ErrorModule.displayAndExit("Configuration Error",
						"ERROR: Could not copy " + CONFIG_FILE_RCT
								+ " to user's home directory!");

			}
			if (!FileModule.copy(new File(PROPERTIES_DIR + CONFIG_FILE_ORB), 
										  orbConfigFile)) {

				ErrorModule.displayAndExit("Configuration Error",
						"ERROR: Could not copy " + CONFIG_FILE_ORB
								+ " to user's home directory!");
			}
		}
	}

	// Method:
	// If we find an openrct.properties file in the users home directory,
	// we test the client version
	private static boolean isCurrentVersion(File rctProperties,
			File orbProperties) {

		// First we check if the properties file exists
		if (!rctProperties.exists() || !orbProperties.exists()) {

			return false;
		}

		// Now we check the version property
		if (FileModule.hasMatchingLine(rctProperties, PROP_VERSION)) {

			return true;
		} else {

			return false;
		}
	}
}

