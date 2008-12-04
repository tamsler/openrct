// $Id: UserInfoGui.java,v 1.4 2003/05/08 20:09:03 thomas Exp $

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
import org.openrct.client.module.UserModule;
import org.openrct.client.module.LangModule;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class UserInfoGui extends JPanel implements Const {

	private TitledBorder titledBorder2 = new TitledBorder("");

	private ImageIcon icon;

	private JLabel jLabel2;

	private GridBagLayout gridBagLayout4 = new GridBagLayout();

	private JTextArea textArea = new JTextArea();

	private JButton hideButton = new JButton();

	private String userAlias_ = "";

	private boolean isSet_ = false;

	// Constructor
	public UserInfoGui() {

		isSet_ = false;
	}

	// Constructor
	public UserInfoGui(String userAlias, String firstName, String lastName,
			String permission) {

		try {

			isSet_ = true;

			userAlias_ = userAlias;

			String noImageFileName = USER_DEF_IMG;

			RCT.BinaryFileHolder fileData = new RCT.BinaryFileHolder();

			// Check if we can get the image file
			if (UserModule.getUserImage(userAlias, fileData)) {

				icon = new ImageIcon(fileData.value);
				jLabel2 = new JLabel(icon);
			} else {

				jLabel2 = new JLabel(new ImageIcon(noImageFileName));
			}

			titledBorder2
					.setTitle(LangModule.i18n.getString("SessionBorderUI"));
			titledBorder2.setTitleJustification(2);

			hideButton.setText(LangModule.i18n.getString("UserInfoHideButton"));
			hideButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					pressedHideButton();
				}
			});

			this.setBorder(titledBorder2);

			this.setLayout(gridBagLayout4);

			textArea.setText(LangModule.i18n.getString("UserInfoAlias") + " "
					+ userAlias + NL
					+ LangModule.i18n.getString("UserInfoFirstName") + " "
					+ firstName + NL
					+ LangModule.i18n.getString("UserInfoLastName") + " "
					+ lastName + NL
					+ LangModule.i18n.getString("UserInfoPermission") + " "
					+ permission);

			textArea.setEditable(false);

			this.add(jLabel2, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 0), 0, 0));

			this.add(textArea, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(5, 5, 5, 5), 0, 0));

			this.add(hideButton, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.NONE,
					new Insets(5, 5, 5, 5), 0, 0));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Methods:
	// --------

	// Method:
	public boolean isSelected(String userAlias) {

		return userAlias_.equals(userAlias);
	}

	// Method:
	public boolean isSet() {

		return isSet_;
	}

	// Method:
	public void deactivate() {

		isSet_ = false;
	}

	// Callaback:
	// ----------

	// Callback
	private void pressedHideButton() {

		this.setVisible(false);
		isSet_ = false;
		SessionGui.setUIBEnabled(true);
	}
}

