// $Id: RctJTextPane.java,v 1.4 2003/05/08 20:09:03 thomas Exp $

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

package org.openrct.client.gui;

import org.openrct.client.Const;
import org.openrct.client.module.ConfigModule;
import org.openrct.client.util.RctStyle;
import javax.swing.*;
import javax.swing.text.*;

// Supported Styles:
// regular
// bold

public class RctJTextPane extends JTextPane implements Const {

	// Constructor
	public RctJTextPane() {

		// Init Styles
		// Default
		Style def = StyleContext.getDefaultStyleContext().getStyle(
				StyleContext.DEFAULT_STYLE);

		// Regular
		Style regular = this.addStyle(RctStyle.REGULAR, def);
		StyleConstants.setFontFamily(def, ConfigModule
				.getString(CONF_UNICODE_FONT));

		// Bold
		Style bold = this.addStyle(RctStyle.BOLD, regular);
		StyleConstants.setBold(bold, true);
	}

	// Methods:
	// --------

	// Method:
	// This appends a string to the text are with a certain style
	public void append(String text, String style) {

		Document doc = this.getDocument();

		try {

			doc.insertString(doc.getLength(), text, this.getStyle(style));

		} catch (Exception e) {
			System.err.println("EXCEPTION: RctTextPane.append()");
		}
	}
}

