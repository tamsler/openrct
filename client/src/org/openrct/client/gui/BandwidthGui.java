// $Id: BandwidthGui.java,v 1.4 2003/05/08 20:09:03 thomas Exp $

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
import org.openrct.client.module.SessionModule;
import org.openrct.client.module.ConfigModule;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class BandwidthGui extends JDialog implements Const {

	private JPanel jPanel1 = new JPanel();

	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private GridBagLayout gridBagLayout2 = new GridBagLayout();

	private GridBagLayout gridBagLayout3 = new GridBagLayout();

	private JRadioButton jRadioButtonLan = new JRadioButton();

	private JRadioButton jRadioButtonDsl = new JRadioButton();

	private JRadioButton jRadioButtonMod = new JRadioButton();

	private ButtonGroup buttonGroup1 = new ButtonGroup();

	private JButton autoTestButton = new JButton();

	private JButton okButton = new JButton();

	private TitledBorder titledBorder1;

	private JLabel jLabel1 = new JLabel();

	private int buttonValue_ = JOptionPane.CLOSED_OPTION;

	//Construct the frame
	public BandwidthGui() {

		super(SessionModule.getFrame(), "Bandwidth Test", true);

		setResizable(false);

		titledBorder1 = new TitledBorder("");

		this.getContentPane().setLayout(gridBagLayout1);

		// Arrange to catch window closing events
		// and also set initial focus
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				buttonValue_ = JOptionPane.CLOSED_OPTION;
				BandwidthGui.this.dispose();
			}

			public void windowOpened(WindowEvent evt) {
				autoTestButton.requestFocus();
			}
		});

		// Ok Button
		okButton.setText("Ok");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				buttonValue_ = JOptionPane.CLOSED_OPTION;
				BandwidthGui.this.dispose();
			}
		});

		// Auto test button
		autoTestButton.setText("Auto Test");
		autoTestButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				buttonValue_ = JOptionPane.CLOSED_OPTION;
				BandwidthGui.this.dispose();
			}
		});

		jRadioButtonLan.setSelected(true);
		jRadioButtonLan.setText("LAN");
		jRadioButtonLan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonValue_ = JOptionPane.OK_OPTION;
				ConfigModule.set(CONF_CONNECTION_SPEED, PACKET_SIZE_LAN);
			}
		});

		jRadioButtonDsl.setText("DSL / Cable Modem");
		jRadioButtonDsl.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonValue_ = JOptionPane.OK_OPTION;
				ConfigModule.set(CONF_CONNECTION_SPEED, PACKET_SIZE_DSL);
			}
		});

		jRadioButtonMod.setText("Dialup Modem");
		jRadioButtonMod.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonValue_ = JOptionPane.OK_OPTION;
				ConfigModule.set(CONF_CONNECTION_SPEED, PACKET_SIZE_MOD);
			}
		});

		jPanel1.setBorder(titledBorder1);
		jPanel1.setLayout(gridBagLayout3);

		titledBorder1.setTitle("Connection Speed");
		titledBorder1.setBorder(BorderFactory.createLineBorder(Color.black));
		titledBorder1.setTitlePosition(2);
		titledBorder1.setTitleJustification(2);

		jLabel1.setText("Bandwidth Testing: In progress ...");
		jLabel1.setVerticalAlignment(SwingConstants.CENTER);

		this.getContentPane().add(
				jLabel1,
				new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(5, 5, 5, 5), 0, 0));
		this.getContentPane().add(
				jPanel1,
				new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(5, 5, 5, 5), 0, 0));

		jPanel1.add(jRadioButtonLan, new GridBagConstraints(0, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0));

		jPanel1.add(jRadioButtonDsl, new GridBagConstraints(0, 1, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0));

		jPanel1.add(jRadioButtonMod, new GridBagConstraints(0, 2, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0));

		jPanel1.add(okButton, new GridBagConstraints(0, 3, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(10, 10, 10, 10), 0, 0));

		this.getContentPane().add(
				autoTestButton,
				new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5),
						0, 0));

		buttonGroup1.add(jRadioButtonLan);
		buttonGroup1.add(jRadioButtonDsl);
		buttonGroup1.add(jRadioButtonMod);

		// Now, pack the widgets so that we get valid bounds
		this.pack();

		// Center Dialog over frame
		Rectangle rect1 = SessionModule.getFrame().getBounds();
		Rectangle rect2 = this.getBounds();

		// We want to center the the dialog w.r.t the frame
		int x = rect1.x + (rect1.width / 2) - (rect2.width / 2);
		int y = rect1.y + (rect1.height / 2) - (rect2.height / 2);
		this.setLocation(x, y);

		// Show the dialog
		this.setVisible(true);
	}

	// Methods:
	// --------

	// Method:
	// Get the return value
	public int getValue() {

		return buttonValue_;
	}
}