// $Id: ServiceModule.java,v 1.4 2003/05/08 19:37:23 thomas Exp $

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

// This class is used to check and to enforce service
// related issues.

public class ServiceModule implements Const {

	// Constructor
	public ServiceModule() {
	}

	// Method:
	// This method checks a string for certain length violation
	public static boolean allowInput(String input) {

		if (ConfigModule.getNumber(CONF_INPUT_LENGTH) > input.length()) {

			return true;
		} else {

			JOptionPane.showMessageDialog(SessionModule.getFrame(),
					LangModule.i18n.getString("ServiceDialog1"),
					LangModule.i18n.getString("ServiceTitle1"),
					JOptionPane.WARNING_MESSAGE);
			return false;
		}
	}

	// Method:
	// This method checks for allowable file size
	public static boolean allowFileSize(int size) {

		if (ConfigModule.getNumber(CONF_FILE_SIZE) > size) {

			return true;
		} else {

			JOptionPane.showMessageDialog(SessionModule.getFrame(),
					LangModule.i18n.getString("ServiceDialog2"),
					LangModule.i18n.getString("ServiceTitle1"),
					JOptionPane.WARNING_MESSAGE);
			return false;
		}
	}
}