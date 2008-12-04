// $Id: UrlPageGui.java,v 1.4 2003/05/08 20:09:03 thomas Exp $

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

import org.openrct.client.module.LangModule;
import org.openrct.client.module.ConfigModule;
import org.openrct.client.module.UrlModule;
import org.openrct.client.module.SessionModule;
import org.openrct.client.module.UserModule;
import org.openrct.client.util.ActionTimerRingBell;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class UrlPageGui extends UncontrolledModulePageGui {

	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private JList urlList = new JList();

	private DefaultListModel urlListModel = new DefaultListModel();

	private TitledBorder titledBorder1;

	private JCheckBox jCheckBox1 = new JCheckBox();

	private JLabel jLabel1 = new JLabel();

	private JTextField jTextField1 = new JTextField();

	private JPanel jPanel1 = new JPanel();

	private JButton viewURLButton = new JButton();

	private JButton sendURLButton = new JButton();

	private ActionTimerRingBell actionTimerRingBell_;

	public UrlPageGui(String className, String assemblyName, int pageType) {

		super(className, assemblyName, MODULE_URL, pageType);

		titledBorder1 = new TitledBorder("");

		this.setLayout(gridBagLayout1);

		urlList.setBorder(titledBorder1);

		titledBorder1.setTitle(LangModule.i18n.getString("UrlPageGuiBorder"));
		titledBorder1.setTitleJustification(2);

		urlList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		urlListModel.insertElementAt(URL_PREFIX + URL_DEFAULT, urlListModel
				.getSize());
		urlList.setModel(urlListModel);
		urlList.setSelectedIndex(0);

		jCheckBox1.setHorizontalAlignment(SwingConstants.CENTER);
		jCheckBox1.setText(LangModule.i18n.getString("UrlPageGuiCheckBox"));

		jLabel1.setText(URL_PREFIX);

		jTextField1.setText(URL_DEFAULT);

		viewURLButton.setText(LangModule.i18n.getString("UrlPageGuiButton1"));
		viewURLButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				pressedViewURLButton();
			}
		});

		sendURLButton.setText(LangModule.i18n.getString("UrlPageGuiButton2"));
		sendURLButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				pressedSendURLButton();
			}
		});

		this.add(urlList, new GridBagConstraints(0, 0, 2, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						5, 5, 5, 5), 0, 0));

		this.add(jCheckBox1, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0));

		this.add(jLabel1, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
						5, 5, 5, 0), 0, 0));

		this.add(jTextField1, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(5, 0, 5, 5), 0, 0));

		this.add(jPanel1, new GridBagConstraints(0, 3, 2, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0));

		jPanel1.add(sendURLButton);
		jPanel1.add(viewURLButton);

		// Create the ring bell action timer
		actionTimerRingBell_ = new ActionTimerRingBell(ConfigModule
				.getNumber(CONF_ACTION_TIMER_SLEEP));
		actionTimerRingBell_.startWorking();
	}

	// Methods:
	// --------

	// Method:
	// Display a sent url
	public synchronized void displayUrlSendMsg(String userAlias, String url) {

		// We don't use the url creator's name yet
		// This is the userAlias parameter

		// Insert url in the list
		urlListModel.insertElementAt(url, urlListModel.getSize());

		// Check if user wants the browser to be loaded automatically
		if (jCheckBox1.isSelected()) {

			UrlModule.openWebBrowser(url);
		}

		// If the client configuration for the
		// url bell ring event is set to true
		// we ring the system bell per url message
		if (ConfigModule.can(CONF_URL_RING_BELL)) {

			actionTimerRingBell_.performAction();
		}
	}

	// Callbacks:
	// ----------

	// Callback:
	public void pressedViewURLButton() {

		if (urlListModel.isEmpty()) {

			JOptionPane.showMessageDialog(this, LangModule.i18n
					.getString("UrlPageGuiDialog3"), LangModule.i18n
					.getString("UrlPageGuiDialog1"),
					JOptionPane.INFORMATION_MESSAGE);
			return;

		} else if (urlList.isSelectionEmpty()) {

			JOptionPane.showMessageDialog(this, LangModule.i18n
					.getString("UrlPageGuiDialog2"), LangModule.i18n
					.getString("UrlPageGuiDialog1"), JOptionPane.ERROR_MESSAGE);
			return;

		} else {

			UrlModule.openWebBrowser((String) urlList.getSelectedValue());
		}
	}

	// Callback:
	public void pressedSendURLButton() {

		// Since the url validation may take some time, we don't tie up
		// the main thread and just create a worker thread to validate
		// and send the URL

		// Getting the url from the text field
		final String url = URL_PREFIX + jTextField1.getText();

		Thread sendUrlThread = new Thread(new Runnable() {

			public void run() {

				// Before we send the url, make sure it is a valid one.
				if (!UrlModule.isValidUrl(url)) {

					JOptionPane.showMessageDialog(SessionModule.getFrame(),
							LangModule.i18n.getString("UrlPageGuiDialog4"),
							LangModule.i18n.getString("UrlPageGuiDialog1"),
							JOptionPane.ERROR_MESSAGE);

					// Since the url is not valid we return
					// and thus don't send the url to the server
					return;
				}

				// We have a valid url
				// Sending the url to the server
				if (PAGE_TEAM == pageType_) {

					UrlModule.send(url, RCT_VERSION, UserModule.getId(),
							UserModule.getAlias(),
							RCT.ObjPermission.UNCLASSIFIED, className_,
							assemblyName_, URL_TEAM_SEND);
				} else if (PAGE_GROUP == pageType_) {

					UrlModule.send(url, RCT_VERSION, UserModule.getId(),
							UserModule.getAlias(),
							RCT.ObjPermission.UNCLASSIFIED, className_,
							assemblyName_, URL_GROUP_SEND);
				} else {

					System.err
							.println("ERROR: Did not recognize the object type!");
				}
			}
		});

		sendUrlThread.start();
	}

	// Abstract Methods Implementation:
	// --------------------------------

	// Abstract Method:
	public void dispatchExitRequest() {

		actionTimerRingBell_.stopWorking();
	}
}

