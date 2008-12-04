// $Id: SessionAboutGui.java,v 1.4 2003/05/08 20:09:03 thomas Exp $

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
import org.openrct.client.module.LangModule;
import org.openrct.client.module.ConfigModule;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SessionAboutGui extends JDialog implements ActionListener, Const {

	JPanel panel1 = new JPanel();

	JButton button1 = new JButton();

	JLabel label1 = new JLabel();

	JLabel label2 = new JLabel();

	JLabel label3 = new JLabel();

	JLabel label4 = new JLabel();

	JLabel label5 = new JLabel();

	JLabel label6 = new JLabel();

	JLabel label7 = new JLabel();

	GridLayout gridLayout1 = new GridLayout();

	// Constructor
	public SessionAboutGui(Frame parent) {

		super(parent);

		enableEvents(AWTEvent.WINDOW_EVENT_MASK);

		try {

			jbInit();
		} catch (Exception e) {

			e.printStackTrace();
		}

		pack();
		setVisible(true);
	}

	// Component initialization
	private void jbInit() throws Exception {

		this.setTitle(LangModule.i18n.getString("SessionAboutTitle"));
		setResizable(false);

		gridLayout1.setRows(SESSION_ABOUT_GUI_N_FIELDS);
		gridLayout1.setColumns(1);

		panel1.setLayout(gridLayout1);

		label1.setText(SESSION_ABOUT_GUI_LABELS[SESSION_ABOUT_GUI_PRODUCT]);
		label2.setText(SESSION_ABOUT_GUI_LABELS[SESSION_ABOUT_GUI_VERSION]);
		label3.setText(SESSION_ABOUT_GUI_LABELS[SESSION_ABOUT_GUI_CPRIGHT]);
		label4.setText(SESSION_ABOUT_GUI_LABELS[SESSION_ABOUT_GUI_COMMENTS]);
		label5.setText(SESSION_ABOUT_GUI_LABELS[SESSION_ABOUT_GUI_JAVA_VER]);
		label6.setText(SESSION_ABOUT_GUI_LABELS[SESSION_ABOUT_GUI_URL]);
		label7.setText(ConfigModule.getServerName() + " : "
				+ ConfigModule.getServerPort());

		button1.setText(SESSION_ABOUT_GUI_LABELS[SESSION_ABOUT_GUI_BUTTON]);
		button1.addActionListener(this);

		panel1.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		this.getContentPane().add(panel1);

		panel1.add(label1);
		panel1.add(label2);
		panel1.add(label3);
		panel1.add(label4);
		panel1.add(label6);
		panel1.add(label5);
		panel1.add(label7);
		panel1.add(button1);
	}

	// Overridden so we can exit when window is closed
	protected void processWindowEvent(WindowEvent e) {

		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			cancel();
		}

		super.processWindowEvent(e);
	}

	// Close the dialog
	void cancel() {

		dispose();
	}

	// Close the dialog on a button event
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == button1) {
			cancel();
		}
	}
}

