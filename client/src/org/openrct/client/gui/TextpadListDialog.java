// $Id: TextpadListDialog.java,v 1.5 2003/05/14 21:21:37 thomas Exp $

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
import org.openrct.client.module.TextpadModule;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;

public class TextpadListDialog extends JDialog implements Const {

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

	private String textpadId_ = "";

	public TextpadListDialog(Frame f, String title) {

		super(f, title, true);

		this.getContentPane().setLayout(gridBagLayout1);

		dataModel = new TextpadListData();

		jTable = new JTable();
		jTable.setFont(UNICODE_FONT_12);
		jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jTable.setAutoCreateColumnsFromModel(false);
		jTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		jTable.setModel(dataModel);

		columnModel = new DefaultTableColumnModel();

		TableColumn col1 = new TableColumn(TEXTPAD_LIST_NAME);
		TableColumn col2 = new TableColumn(TEXTPAD_LIST_DATE);

		col1.setHeaderValue(dataModel.getColumnName(TEXTPAD_LIST_NAME));
		col2.setHeaderValue(dataModel.getColumnName(TEXTPAD_LIST_DATE));

		columnModel.addColumn(col1);
		columnModel.addColumn(col2);

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

		titledBorder.setTitle(LangModule.i18n
				.getString("TextpadListDialogLabel1"));
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
				TextpadListDialog.this.dispose();
			}

			public void windowOpened(WindowEvent evt) {
				selectButton.requestFocus();
			}
		});

		// Catch the Cancel button
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				buttonValue_ = JOptionPane.CANCEL_OPTION;
				TextpadListDialog.this.dispose();
			}
		});

		// Catch the Select button
		selectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				buttonValue_ = JOptionPane.OK_OPTION;

				// Make sure the user selected an entry
				if (-1 == jTable.getSelectedRow()) {

					JOptionPane.showMessageDialog(TextpadListDialog.this,
							LangModule.i18n
									.getString("TextpadListDialogLabel2"),
							LangModule.i18n
									.getString("TextpadListDialogLabel1"),
							JOptionPane.ERROR_MESSAGE);
				} else {

					// Getting info from selection

					int selectedRow = jTable.getSelectedRow();

					// Get the selected textpad id from the data model
					textpadId_ = (String) dataModel.getValueAt(selectedRow,
							TEXTPAD_LIST_ID);

					// Now we remove the just selected textpad from the edit
					// list
					TextpadModule.editList.remove(textpadId_);

					// Close the Dialog window
					TextpadListDialog.this.dispose();
				}
			}
		});

		// Now, pack the widgets so that we get valid bounds
		this.pack();
		this.setSize(800, 300);

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
	public TextpadListDialogResult getValue() {

		TextpadListDialogResult result = new TextpadListDialogResult(
				buttonValue_, textpadId_);

		return result;
	}

	// Classes:
	// --------

	// Class
	public class TextpadListDialogResult {

		private int buttonValue_;

		private String textpadId_;

		public TextpadListDialogResult(int buttonValue, String textpadId) {

			buttonValue_ = buttonValue;
			textpadId_ = textpadId;
		}

		public int getButtonValue() {

			return buttonValue_;
		}

		public String getId() {

			return textpadId_;
		}
	}
}

