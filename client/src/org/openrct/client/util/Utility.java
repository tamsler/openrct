// $Id: Utility.java,v 1.4 2003/05/08 19:31:42 thomas Exp $

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

package org.openrct.client.util;

import org.openrct.client.Const;
import java.util.*;
import java.text.*;

public class Utility implements Const {

	// Constructor
	public Utility() {
	}

	// Method:
	// Extract Date and Time from serverside DB current time
	// The timestamp of in the following form:
	// 2002-09-24 12:02:19.1234-12
	// Sw we just want the "2002-09-24 12:02:19" part.
	public static String getDateAndTime(String timestamp) {

		StringTokenizer st = new StringTokenizer(timestamp, ".");

		String dateAndTime = st.nextToken();

		return dateAndTime;
	}

	// Method:
	// It takes a string and replaces every single and double
	// quotes in that string by the provided substitution char
	public static String replaceAllQuotes(String str, char sub) {

		char tmp[] = new char[str.length()];

		for (int i = 0; i < str.length(); i++) {

			if (('\'' == str.charAt(i)) || ('\"' == str.charAt(i))) {

				tmp[i] = sub;
			} else {

				tmp[i] = str.charAt(i);
			}
		}

		return new String(tmp);
	}

	// Method:
	// It takes a string and replaces every single
	// quotes in that string by the provided substitution char
	public static String replaceSingleQuotes(String str, char sub) {

		char tmp[] = new char[str.length()];

		for (int i = 0; i < str.length(); i++) {

			if ('\'' == str.charAt(i)) {

				tmp[i] = sub;
			} else {

				tmp[i] = str.charAt(i);
			}
		}

		return new String(tmp);
	}

	// Method:
	// It finds single quotes and escapes them by adding
	// one more single quote. eg."
	// It's --> It''s
	// This is needed for valid sql statements
	public static String escapeSingleQuote(String str) {

		// Assigning double the size of the argument string
		// since the worst case is where the argument string
		// only contains single quotes.
		char tmp[] = new char[2 * str.length()];
		int index = 0;

		// Looping through the argument string and detect
		// single quotes
		for (int i = 0; i < str.length(); i++, index++) {

			// If we find a single quotes, we escape it
			if (('\'' == str.charAt(i))) {

				tmp[index] = str.charAt(i);
				tmp[++index] = str.charAt(i);
			} else {

				tmp[index] = str.charAt(i);
			}
		}

		// Since in most cases the tmp array is too long,
		// we only get the part we need.
		return (new String(tmp)).substring(0, index);
	}

	// Method:
	// Check if any of the following chars is present in
	// a string:
	// ", ', forward and back slash
	public static boolean hasBadChars(String str) {

		if ((-1 != str.indexOf("\'")) || (-1 != str.indexOf("\""))
				|| (-1 != str.indexOf("\\")) || (-1 != str.indexOf("/"))
				|| (-1 != str.indexOf(".")) || (-1 != str.indexOf(","))
				|| (-1 != str.indexOf("*"))) {

			return true;
		} else {

			return false;
		}
	}

	// Method:
	// It takes two Ip number strings and checks if the IPs are
	// within a private address space. If both are in private
	// address space it checks if they are at least in the same
	// Class A subnet
	// Testing [ Server ] TO [Client] relation
	public static int canClientConnectToServer(String clientIp, String serverIp) {

		boolean client = isIpPrivate(clientIp);
		boolean server = isIpPrivate(serverIp);

		if (!server && isIpLocalhost(clientIp)) {

			return GLOB_TO_PRIV;
		} else if (server && client && sameClassASubnet(clientIp, serverIp)) {

			return PRIV_TO_PRIV;
		} else if (!server && !client) {

			return GLOB_TO_GLOB;
		} else if (!server && client) {

			return GLOB_TO_PRIV;
		} else {

			return PRIV_TO_GLOB;
		}
	}

	// Method:
	// It determines if the two ip number strings are in the
	// same Class A subnet
	private static boolean sameClassASubnet(String ip1, String ip2) {

		StringTokenizer st1 = new StringTokenizer(ip1, ".");
		StringTokenizer st2 = new StringTokenizer(ip2, ".");

		int tokNum1 = st1.countTokens();
		int tokNum2 = st2.countTokens();

		if ((4 != tokNum1) || (4 != tokNum2)) {

			System.err.println("ERROR: Ip number is in a wrong format!");
			return false;
		}

		int tok1[] = new int[st1.countTokens()];
		int tok2[] = new int[st2.countTokens()];

		for (int i = 0; st1.hasMoreTokens() && st2.hasMoreTokens(); i++) {

			tok1[i] = Integer.parseInt(st1.nextToken());
			tok2[i] = Integer.parseInt(st2.nextToken());
		}

		if (tok1[0] == tok2[0]) {

			return true;
		} else {

			return false;
		}
	}

	// Method:
	// It determines if the ip number string is in the
	// private address space.
	private static boolean isIpPrivate(String ip) {

		StringTokenizer st = new StringTokenizer(ip, ".");

		int tokNum = st.countTokens();

		if (4 != tokNum) {

			System.err.println("ERROR: Ip number is in a wrong format!");
			return false;
		}

		int tok[] = new int[st.countTokens()];

		for (int i = 0; st.hasMoreTokens(); i++) {

			tok[i] = Integer.parseInt(st.nextToken());
		}

		if ((192 == tok[0]) && (168 == tok[1]) && (0 <= tok[2])
				&& (255 >= tok[2]) && (0 <= tok[3]) && (255 >= tok[3])) {

			return true;
		} else if ((10 == tok[0]) && (0 <= tok[1]) && (255 >= tok[1])
				&& (0 <= tok[2]) && (255 >= tok[2]) && (0 <= tok[3])
				&& (255 >= tok[3])) {

			return true;
		} else if ((172 == tok[0]) && (16 <= tok[1]) && (31 >= tok[1])
				&& (0 <= tok[2]) && (255 >= tok[2]) && (0 <= tok[3])
				&& (255 >= tok[3])) {

			return true;
		} else {

			return false;
		}
	}

	// Method:
	// Check if the IP address is localhost
	// 127.0.0.1
	public static boolean isIpLocalhost(String ip) {

		return ip.equals(LOCALHOST_IP) ? true : false;
	}

	// Method:
	// Test if a given date, month, day, and year is valid
	public static boolean isDateValid(String month, String day, String year) {

		StringBuffer dateBuf = new StringBuffer();

		dateBuf.append(month);
		dateBuf.append("/");
		dateBuf.append(day);
		dateBuf.append("/");
		dateBuf.append(year);

		try {

			DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);

			// This is important!
			df.setLenient(false);

			Date date = df.parse(dateBuf.toString());
		} catch (Exception e) {

			return false;
		}

		// If no exception occured such as:
		// ParseException
		// IllegalArguemntException
		// We return ok/true
		return true;

	}
}

