// $Id: ErrorModule.java,v 1.4 2003/05/08 19:37:23 thomas Exp $

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
import java.awt.*;
import javax.swing.*;

public class ErrorModule implements Const {

	// Constructor
	private ErrorModule() {
		// Nothing to do here
	}

	// Methods:
	// --------

	// Method:
	public static void display(String title, String errorMsg) {

		JOptionPane.showMessageDialog(null, errorMsg, title,
				JOptionPane.ERROR_MESSAGE);
	}

	// Method:
	public static void display(Frame f, String title, String errorMsg) {

		JOptionPane.showMessageDialog(f, errorMsg, title,
				JOptionPane.ERROR_MESSAGE);
	}

	// Method:
	public static void displayAndExit(String title, String errorMsg) {

		display(title, errorMsg);

		System.exit(0);
	}

	// Method:
	public static void displayAndExit(Frame f, String title, String errorMsg) {

		display(f, title, errorMsg);

		System.exit(0);
	}
}

