// $Id: WorkerDialog.java,v 1.5 2003/05/14 21:21:37 thomas Exp $

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
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class WorkerDialog extends JDialog implements Const {

	private JPanel panel = new JPanel();

	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private JLabel label;

	private JProgressBar progressBar = new JProgressBar();

	private JButton cancelButton;

	private int returnValue = JOptionPane.CLOSED_OPTION;

	public WorkerDialog(Frame f, String title, String message, boolean isModal,
			boolean hasProgressBar, int minValue, int maxValue, int initValue) {

		super(f, title, isModal);

		setResizable(false);

		this.getContentPane().setLayout(gridBagLayout1);

		label = new JLabel(message, SwingConstants.CENTER);
		label.setFont(UNICODE_FONT_16);

		progressBar.setOrientation(JProgressBar.HORIZONTAL);
		progressBar.setStringPainted(true);
		progressBar.setMinimum(minValue);
		progressBar.setMaximum(maxValue);
		progressBar.setValue(initValue);
		progressBar.setForeground(Color.green);
		progressBar.setBorderPainted(true);

		cancelButton = new JButton(LangModule.i18n
				.getString("ButtonLabelCancel"));

		panel.add(cancelButton);

		this.getContentPane().add(
				label,
				new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(10, 10, 10, 10), 0, 0));

		this.getContentPane().add(
				progressBar,
				new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL,
						new Insets(5, 15, 5, 15), 0, 0));

		this.getContentPane().add(
				panel,
				new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL,
						new Insets(20, 5, 10, 5), 0, 0));

		// Arrange to catch window closing events
		// and also set initial focus
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				returnValue = JOptionPane.CLOSED_OPTION;
				WorkerDialog.this.dispose();
			}

			public void windowOpened(WindowEvent evt) {
				cancelButton.requestFocus();
			}
		});

		// Catch the Cancel button
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				returnValue = JOptionPane.CANCEL_OPTION;
				WorkerDialog.this.dispose();
			}
		});

		if (!hasProgressBar) {

			progressBar.setVisible(false);
		}

		// Now, pack the widgets so that we get valid bounds
		this.pack();

		// Center Dialog over frame
		Rectangle rect1 = f.getBounds();
		Rectangle rect2 = this.getBounds();

		// We want to center the the dialog w.r.t the frame
		int x = rect1.x + (rect1.width / 2) - (rect2.width / 2);
		int y = rect1.y + (rect1.height / 2) - (rect2.height / 2);
		this.setLocation(x, y);

		// Show the dialog
		this.setVisible(true);
	}
}

