// $Id: ServerListDialog.java,v 1.5 2003/05/14 21:21:37 thomas Exp $

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
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;

public class ServerListDialog extends JDialog implements Const {

	private JPanel panel = new JPanel();

	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private JButton selectButton;

	private JButton cancelButton;

	private JScrollPane jScrollPane = new JScrollPane();

	private TitledBorder titledBorder;

	private JTable jTable;

	private DefaultTableColumnModel columnModel;

	private TableModel dataModel;

	// Result values
	private int buttonValue_ = JOptionPane.CLOSED_OPTION;

	private String serverName_ = "";

	private String portNumber_ = "";

	public ServerListDialog(Frame f, String title) {

		super(f, title, true);

		//setResizable(false);

		this.getContentPane().setLayout(gridBagLayout1);

		dataModel = new ServerListData();

		jTable = new JTable();
		jTable.setFont(UNICODE_FONT_12);
		jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jTable.setAutoCreateColumnsFromModel(false);
		jTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		jTable.setModel(dataModel);

		columnModel = new DefaultTableColumnModel();

		TableColumn col1 = new TableColumn(SERVER_LIST_NAME, 200);
		TableColumn col2 = new TableColumn(SERVER_LIST_PORT, 40);
		TableColumn col3 = new TableColumn(SERVER_LIST_VERSION, 40);
		TableColumn col4 = new TableColumn(SERVER_LIST_ORGANIZATION, 200);
		TableColumn col5 = new TableColumn(SERVER_LIST_DEPARTMENT, 200);
		TableColumn col6 = new TableColumn(SERVER_LIST_STATUS, 40);

		col1.setHeaderValue(dataModel.getColumnName(SERVER_LIST_NAME));
		col2.setHeaderValue(dataModel.getColumnName(SERVER_LIST_PORT));
		col3.setHeaderValue(dataModel.getColumnName(SERVER_LIST_VERSION));
		col4.setHeaderValue(dataModel.getColumnName(SERVER_LIST_ORGANIZATION));
		col5.setHeaderValue(dataModel.getColumnName(SERVER_LIST_DEPARTMENT));
		col6.setHeaderValue(dataModel.getColumnName(SERVER_LIST_STATUS));

		columnModel.addColumn(col1);
		columnModel.addColumn(col2);
		columnModel.addColumn(col3);
		columnModel.addColumn(col4);
		columnModel.addColumn(col5);
		columnModel.addColumn(col6);

		jTable.setColumnModel(columnModel);

		jTable.setShowVerticalLines(false);

		titledBorder = new TitledBorder("");

		selectButton = new JButton(LangModule.i18n
				.getString("ButtonLabelSelect"));
		cancelButton = new JButton(LangModule.i18n
				.getString("ButtonLabelCancel"));

		panel.add(selectButton);
		panel.add(cancelButton);

		jScrollPane.setAutoscrolls(true);

		titledBorder.setTitle(LangModule.i18n.getString("ServerListBorder"));
		titledBorder.setTitleJustification(2);

		jScrollPane.setBorder(titledBorder);

		this.getContentPane().add(
				jScrollPane,
				new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
						GridBagConstraints.NORTH, GridBagConstraints.BOTH,
						new Insets(5, 5, 5, 5), 0, 0));

		jScrollPane.getViewport().add(jTable);

		this.getContentPane().add(
				panel,
				new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL,
						new Insets(20, 5, 10, 5), 0, 0));

		// Arrange to catch window closing events
		// and also set initial focus
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				buttonValue_ = JOptionPane.CLOSED_OPTION;
				ServerListDialog.this.dispose();
			}

			public void windowOpened(WindowEvent evt) {
				selectButton.requestFocus();
			}
		});

		// Catch the Cancel button
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				buttonValue_ = JOptionPane.CANCEL_OPTION;
				ServerListDialog.this.dispose();
			}
		});

		// Catch the Select button
		selectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				buttonValue_ = JOptionPane.OK_OPTION;

				// Make sure the user selected an entry
				if (-1 == jTable.getSelectedRow()) {

					JOptionPane.showMessageDialog(ServerListDialog.this,
							LangModule.i18n.getString("ServerListDialog2"),
							LangModule.i18n.getString("ServerListDialog1"),
							JOptionPane.ERROR_MESSAGE);
				} else {

					// Getting info from selection

					int selectedRow = jTable.getSelectedRow();

					serverName_ = (String) dataModel.getValueAt(selectedRow,
							SERVER_LIST_NAME);
					portNumber_ = (String) dataModel.getValueAt(selectedRow,
							SERVER_LIST_PORT);

					ServerListDialog.this.dispose();
				}
			}
		});

		// Now, pack the widgets so that we get valid bounds
		this.pack();
		this.setSize(SERVER_LIST_WIDTH, SERVER_LIST_HEIGHT);

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

	// Methods:
	// --------

	// Method:
	public ServerListDialogResult getValue() {

		ServerListDialogResult result = new ServerListDialogResult(
				buttonValue_, serverName_, portNumber_);

		return result;
	}

	// Classes:
	// --------

	// Class
	public class ServerListDialogResult {

		private int buttonValue_;

		private String serverName_;

		private String portNumber_;

		public ServerListDialogResult(int buttonValue, String serverName,
				String portNumber) {

			buttonValue_ = buttonValue;
			serverName_ = serverName;
			portNumber_ = portNumber;
		}

		public int getButtonValue() {

			return buttonValue_;
		}

		public String getServerName() {

			return serverName_;
		}

		public String getPortNumber() {

			return portNumber_;
		}
	}
}

