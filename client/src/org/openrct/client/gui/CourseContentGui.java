// $Id: CourseContentGui.java,v 1.7 2003/05/08 20:09:03 thomas Exp $

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
import org.openrct.client.module.SessionModule;
import org.openrct.client.module.CourseContentModule;
import org.openrct.client.module.UrlModule;
import org.openrct.client.module.ClassModule;
import org.openrct.client.module.FileModule;
import org.openrct.client.module.UserModule;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

public class CourseContentGui extends JPanel implements Const {

	private JPanel jButtonPanel = new JPanel();

	private JPanel jProgressPanel = new JPanel();

	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private JScrollPane jScrollPane1 = new JScrollPane();

	private JButton downloadButton = new JButton();

	private JButton refreshButton = new JButton();

	private JButton cancelButton = new JButton();

	private JButton closeButton = new JButton();

	private JProgressBar jProgressBar = new JProgressBar();

	private JLabel jLabelProgress = new JLabel();

	private TitledBorder titledBorder1;

	private JTable jTable1;

	private DefaultTableColumnModel columnModel;

	private TableModel dataModel;

	private String className_ = "";

	private boolean isSet_ = false;

	// This flag is used to check if we can close the CourseContentGui
	// in the case a download is in progress
	private boolean canClose_ = true;

	private String fileDownloadId_;

	// Constructor
	public CourseContentGui() {

		isSet_ = false;

		// Let the class module know
		ClassModule.setCourseContentRef(this);
	}

	// Constructor
	public CourseContentGui(String className) {

		try {

			isSet_ = true;

			// Let the class module know
			ClassModule.setCourseContentRef(this);

			className_ = className;

			jLabelProgress.setText(LangModule.i18n
					.getString("FtpPageGuiLabel3"));

			dataModel = new CourseContentData(className);

			jTable1 = new JTable();
			jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jTable1.setAutoCreateColumnsFromModel(false);
			jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
			jTable1.setModel(dataModel);

			columnModel = new DefaultTableColumnModel();
			TableColumn col1 = new TableColumn(CC_ALIAS);
			TableColumn col2 = new TableColumn(CC_LENGTH);
			TableColumn col3 = new TableColumn(CC_DATE);

			col1.setHeaderValue(dataModel.getColumnName(CC_ALIAS));
			col2.setHeaderValue(dataModel.getColumnName(CC_LENGTH));
			col3.setHeaderValue(dataModel.getColumnName(CC_DATE));

			columnModel.addColumn(col1);
			columnModel.addColumn(col2);
			columnModel.addColumn(col3);

			jTable1.setColumnModel(columnModel);

			this.setLayout(gridBagLayout1);

			titledBorder1 = new TitledBorder("");
			titledBorder1.setTitle(LangModule.i18n.getString("CCGuiBorder")
					+ " " + className);
			titledBorder1.setBorder(BorderFactory.createEtchedBorder());
			titledBorder1.setTitleJustification(2);

			this.setBorder(titledBorder1);

			jScrollPane1.setAutoscrolls(true);

			jTable1.setShowVerticalLines(false);

			downloadButton.setText(LangModule.i18n.getString("CCGuiButton2"));
			downloadButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					pressedDownloadViewButton();
				}
			});

			refreshButton.setText(LangModule.i18n.getString("CCGuiButton1"));
			refreshButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					pressedRefreshButton();
				}
			});

			closeButton.setText(LangModule.i18n.getString("ButtonLabelClose"));
			closeButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					pressedCloseButton();
				}
			});

			cancelButton
					.setText(LangModule.i18n.getString("ButtonLabelCancel"));
			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					pressedCancelButton();
				}
			});

			this.add(jScrollPane1, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(5, 5, 5, 5), 0, 0));

			this.add(jProgressPanel,
					new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5,
									5), 0, 0));

			jProgressPanel.add(jLabelProgress);
			jProgressPanel.add(jProgressBar);

			this.add(jButtonPanel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
					new Insets(5, 5, 5, 5), 0, 0));

			jButtonPanel.add(downloadButton);
			jButtonPanel.add(refreshButton);
			jButtonPanel.add(cancelButton);
			jButtonPanel.add(closeButton);

			jScrollPane1.getViewport().add(jTable1);

			// Disable the cancel button
			cancelButton.setEnabled(false);

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	// Methods:
	// --------

	// Method:
	public boolean isSelected(String className) {

		return className_.equals(className);
	}

	// Method:
	public boolean isSet() {

		return isSet_;
	}

	// Method:
	public boolean canClose() {

		return canClose_;
	}

	// Method:
	public void deactivate() {

		isSet_ = false;
	}

	// Method:
	public void incrementProgressBar(int incrementValue) {

		int currentValue = jProgressBar.getValue();
		jProgressBar.setValue(currentValue + incrementValue);
	}

	// Method:
	public void endDownload() {

		// Enable/Disable buttons
		downloadButton.setEnabled(true);
		refreshButton.setEnabled(true);
		closeButton.setEnabled(true);
		cancelButton.setEnabled(false);

		jLabelProgress.setText(LangModule.i18n.getString("FtpPageGuiLabel3"));
		jProgressBar.setValue(0);

		// Reset close flag
		canClose_ = true;
	}

	// Helper Methods:
	// ---------------

	// Helper Method:
	private void accessServerSource(int selectedRow) {

		final String fileName = (String) dataModel.getValueAt(selectedRow,
				CC_NAME);
		final String courseContentId = (String) dataModel.getValueAt(
				selectedRow, CC_ID);
		final int fileSize = Integer.parseInt((String) dataModel.getValueAt(
				selectedRow, CC_LENGTH));

		// Setting the fileDownloadId
		fileDownloadId_ = courseContentId;

		final JFileChooser fc = new JFileChooser();

		fc.setDialogTitle(LangModule.i18n.getString("CCGuiDialog6") + " [ "
				+ fileName + " ]");
		fc.setSelectedFile(new File(fileName));

		// Capture the return value after clicking on
		// a button in the FileChooser
		int returnVal = fc.showSaveDialog(SessionModule.getFrame());

		if (JFileChooser.APPROVE_OPTION == returnVal) {

			Thread downloadThread = new Thread(new Runnable() {

				public void run() {

					// Set close flag
					canClose_ = false;

					// Enable/Disable buttons
					downloadButton.setEnabled(false);
					refreshButton.setEnabled(false);
					closeButton.setEnabled(false);
					cancelButton.setEnabled(true);

					// Init JProgressBar
					jProgressBar.setStringPainted(true);
					jProgressBar.setMinimum(0);
					jProgressBar.setMaximum(fileSize);
					jProgressBar.setValue(0);

					jLabelProgress.setText(LangModule.i18n
							.getString("FtpPageGuiLabel2")
							+ " " + fileName);

					// Getting the file name from the save file chooser
					File newFile = fc.getSelectedFile();

					File file = new File(fc.getCurrentDirectory()
							.getAbsolutePath(), newFile.getName());

					CourseContentModule.download(courseContentId, file,
							className_, CC_DOWNLOAD);

				}
			});

			// Start thread
			downloadThread.start();
		}
	}

	// Helper Method:
	private void accessClientSource(int selectedRow) {

		// Not implemented yet
	}

	// Helper Method:
	private void accessWebSource(int selectedRow) {

		String url = (String) dataModel.getValueAt(selectedRow, CC_NAME);

		UrlModule.openWebBrowser(url);
	}

	// Callbacks:
	// ----------

	// Callback:
	private void pressedDownloadViewButton() {

		// Make sure the user selected a course content
		if (-1 == jTable1.getSelectedRow()) {

			// No course content is selected. Let the user know
			JOptionPane.showMessageDialog(this, LangModule.i18n
					.getString("CCGuiDialog5"), LangModule.i18n
					.getString("CCGuiDialog1"), JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Get selected row
		int selectedRow = jTable1.getSelectedRow();

		// Get the course content source
		String source = (String) dataModel.getValueAt(selectedRow, CC_SOURCE);

		// Check what action we need to perform depending on the source
		if (source.equals(CC_SOURCE_SERVER)) {

			accessServerSource(selectedRow);
		} else if (source.equals(CC_SOURCE_CLIENT)) {

			accessClientSource(selectedRow);
		} else if (source.equals(CC_SOURCE_WEB)) {

			accessWebSource(selectedRow);
		} else {

			System.err
					.println("ERROR: Did not recognize the course content source!");
		}
	}

	// Callback:
	private void pressedRefreshButton() {

		((CourseContentData) dataModel).refresh();
	}

	// Callback:
	private void pressedCloseButton() {

		this.setVisible(false);
		isSet_ = false;
		SessionGui.setCCBEnabled(true);
	}

	// Callback:
	public void pressedCancelButton() {

		// Here we only disable the cancel button.
		// The other buttons are set on the callback message
		cancelButton.setEnabled(false);

		try {

			FileModule.cancelDownload(fileDownloadId_, UserModule.getId(),
					className_, "");
		} catch (Exception e) {
		}
	}
}

