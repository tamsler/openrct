// $Id: ArchiveTextpadPageGui.java,v 1.5 2003/07/07 18:14:30 thomas Exp $

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
import org.openrct.client.module.LangModule;
import org.openrct.client.module.CacheModule;
import org.openrct.client.module.TextpadModule;
import org.openrct.client.util.Utility;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;
import org.omg.CORBA.*;

public class ArchiveTextpadPageGui extends JPanel implements Const {

	private JPanel jPanel3 = new JPanel();

	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private JButton openButton = new JButton();

	private JButton closeButton = new JButton();

	private JButton editButton = new JButton();

	private JTextArea textArea = new JTextArea();

	private JScrollPane jSPTable = new JScrollPane();

	private JScrollPane jSPTextArea = new JScrollPane();

	private TitledBorder titledBorder1;

	private TitledBorder titledBorder2;

	private JTable jTable1;

	private DefaultTableColumnModel columnModel;

	private TableModel dataModel_;

	private String className_;

	private String teamName_;

	private ArchivePageGui archivePage_;

	// Constructor
	public ArchiveTextpadPageGui(String className, String teamName,
			ArchivePageGui archivePage, TableModel dataModel) {

		className_ = className;
		teamName_ = teamName;
		archivePage_ = archivePage;
		dataModel_ = dataModel;

		jTable1 = new JTable();
		jTable1.setFont(new Font(ConfigModule.getString(CONF_UNICODE_FONT),
				Font.PLAIN, ConfigModule.getNumber(CONF_UNICODE_FONT_SIZE)));
		jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jTable1.setAutoCreateColumnsFromModel(false);
		jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		jTable1.setModel(dataModel_);

		columnModel = new DefaultTableColumnModel();
		TableColumn col1 = new TableColumn(TEXTPAD_NAME);
		TableColumn col2 = new TableColumn(TEXTPAD_DATE);
		col1.setHeaderValue(dataModel_.getColumnName(TEXTPAD_NAME));
		col2.setHeaderValue(dataModel_.getColumnName(TEXTPAD_DATE));
		columnModel.addColumn(col1);
		columnModel.addColumn(col2);
		jTable1.setColumnModel(columnModel);

		jTable1.setShowVerticalLines(false);

		titledBorder1 = new TitledBorder("");
		titledBorder2 = new TitledBorder("");
		titledBorder2.setTitleFont(new Font(ConfigModule
				.getString(CONF_UNICODE_FONT), Font.PLAIN, ConfigModule
				.getNumber(CONF_UNICODE_FONT_SIZE)));

		this.setLayout(gridBagLayout1);

		textArea.setWrapStyleWord(true);
		textArea.setEditable(false);
		textArea.setAutoscrolls(true);
		textArea.setLineWrap(true);
		textArea.setFont(new Font(ConfigModule.getString(CONF_UNICODE_FONT),
				Font.PLAIN, ConfigModule.getNumber(CONF_UNICODE_FONT_SIZE)));
		jSPTextArea.setAutoscrolls(true);
		jSPTextArea.getViewport().add(textArea);

		openButton.setText(LangModule.i18n.getString("ButtonLabelOpen"));
		openButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				pressedOpenButton();
			}
		});

		closeButton.setText(LangModule.i18n.getString("ButtonLabelClose"));
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				pressedCloseButton();
			}
		});

		editButton.setText(LangModule.i18n.getString("ButtonLabelEdit2"));
		editButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				pressedEditButton();
			}
		});

		jSPTable.setAutoscrolls(true);

		titledBorder1.setTitle(LangModule.i18n.getString("TextpadPageBorder2"));
		titledBorder1.setTitleJustification(2);

		titledBorder2.setTitle(LangModule.i18n
				.getString("TextpadPageDefaultName"));
		titledBorder2.setTitleJustification(2);

		jSPTable.setBorder(titledBorder1);
		jSPTextArea.setBorder(titledBorder2);

		this.add(jSPTable, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 40));

		this.add(jSPTextArea, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						5, 5, 5, 5), 0, 150));

		jSPTable.getViewport().add(jTable1);

		this.add(jPanel3, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0));

		jPanel3.add(openButton);
		jPanel3.add(editButton);
		jPanel3.add(closeButton);
	}

	// Methods:
	// --------

	// Method:
	public void dispatchFontChange() {

		textArea.setFont(new Font(ConfigModule.getString(CONF_UNICODE_FONT),
				Font.PLAIN, ConfigModule.getNumber(CONF_UNICODE_FONT_SIZE)));
	}

	// Method: set/display the textarea name
	private void displayTextName(String name) {

		titledBorder2.setTitle(LangModule.i18n.getString("TextpadPageBorder")
				+ ": " + name);
		this.validate();
		this.repaint();
	}

	// Callbacks:
	// ----------

	// Callback:
	public void pressedOpenButton() {

		// Make sure the user selected a textpad
		if (-1 == jTable1.getSelectedRow()) {

			// No textpad is selected. Let the user know
			JOptionPane.showMessageDialog(this, LangModule.i18n
					.getString("TextpadPageDialog6"), LangModule.i18n
					.getString("TextpadPage"), JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Get selcted row
		int selectedRow = jTable1.getSelectedRow();

		// Get textpad information
		String textpadId = (String) dataModel_.getValueAt(selectedRow,
				TEXTPAD_ID);
		String textpadName = (String) dataModel_.getValueAt(selectedRow,
				TEXTPAD_NAME);

		RCT.BinaryFileHolder fileData = new RCT.BinaryFileHolder();

		// Check Cache for textpad
		if (CacheModule.isHit(textpadId, MOD_TEXTPAD)) {

			// Hit
			CacheModule.get(textpadId, MOD_TEXTPAD, fileData);

			displayTextName(textpadName);

			try {

				// Frist, reset the text area
				textArea.setText("");
				textArea
						.append(new String(fileData.value, FILE_UTF_8_ENCODING));
			} catch (Exception e) {
			}
		} else {

			// MISS
			StringHolder text = new StringHolder();
			TextpadModule.getText(textpadId, text);

			try {

				fileData.value = text.value.getBytes(FILE_UTF_8_ENCODING);
			} catch (Exception e) {
			}

			CacheModule.put(textpadId, MOD_TEXTPAD, fileData, MODULE_TEXTPAD_
					+ textpadId);

			displayTextName(textpadName);

			try {

				// Frist, reset the text area
				textArea.setText("");
				textArea.append(text.value);
			} catch (Exception e) {
			}
		}

		// Show start of text area
		textArea.setCaretPosition(0);
	}

	// Callback
	public void pressedCloseButton() {

		archivePage_.closePage();
	}

	// Callback
	public void pressedEditButton() {

		// Make sure that user selected a textpad
		if (-1 == jTable1.getSelectedRow()) {

			// No textpad is selected. Let the user know
			JOptionPane.showMessageDialog(this, LangModule.i18n
					.getString("TextpadPageDialog6"), LangModule.i18n
					.getString("TextpadPage"), JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Get selcted row
		int selectedRow = jTable1.getSelectedRow();

		// Get textpad information
		String textpadId = (String) dataModel_.getValueAt(selectedRow,
				TEXTPAD_ID);
		String textpadName = (String) dataModel_.getValueAt(selectedRow,
				TEXTPAD_NAME);
		String textpadDate = (String) dataModel_.getValueAt(selectedRow,
				TEXTPAD_DATE);

		// Create the TextpadInfo object
		TextpadInfo textpadInfo = new TextpadInfo(textpadId, textpadName,
				Utility.getDateAndTime(textpadDate), className_, teamName_);

		// Insert it into the edit list
		TextpadModule.editList.insert(textpadInfo);

		JOptionPane.showMessageDialog(this, LangModule.i18n
				.getString("ArchiveTextpadPageGuiMsg2"), LangModule.i18n
				.getString("ArchiveTextpadPageGuiMsg1"),
				JOptionPane.INFORMATION_MESSAGE);
	}
}

