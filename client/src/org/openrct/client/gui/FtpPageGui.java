// $Id: FtpPageGui.java,v 1.15 2003/07/02 16:48:56 thomas Exp $

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
import org.openrct.client.module.SessionModule;
import org.openrct.client.module.FtpModule;
import org.openrct.client.module.ServiceModule;
import org.openrct.client.module.FileModule;
import org.openrct.client.module.UserModule;
import org.openrct.client.util.ActionTimerRingBell;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;

public class FtpPageGui extends UncontrolledModulePageGui {

	private JPanel jButtonPanel = new JPanel();

	private JPanel jProgressPanel = new JPanel();

	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private JButton downloadButton = new JButton();

	private JButton uploadButton = new JButton();

	private JButton cancelButton = new JButton();

	private JProgressBar jProgressBar = new JProgressBar();

	private JLabel jLabelProgress = new JLabel();

	private JScrollPane jScrollPane1 = new JScrollPane();

	private TitledBorder titledBorder1;

	private JTable jTable1;

	private DefaultTableColumnModel columnModel;

	private TableModel dataModel;

	private ActionTimerRingBell actionTimerRingBell_;

	private int buttonStatus_ = FTP_STATUS_UNDEF;

	private String fileDownloadId_;

	public FtpPageGui(String className, String assemblyName, int pageType) {

		super(className, assemblyName, MODULE_FTP, pageType);

		this.setLayout(gridBagLayout1);

		jLabelProgress.setText(LangModule.i18n.getString("FtpPageGuiLabel3"));

		dataModel = new FtpData(className, assemblyName, pageType);

		jTable1 = new JTable();
		jTable1.setFont(UNICODE_FONT_16);
		jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jTable1.setAutoCreateColumnsFromModel(false);
		jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		jTable1.setModel(dataModel);

		columnModel = new DefaultTableColumnModel();

		TableColumn col1 = new TableColumn(FILE_USER_ALIAS);
		TableColumn col2 = new TableColumn(FILE_ALIAS);
		TableColumn col3 = new TableColumn(FILE_LENGTH);
		TableColumn col4 = new TableColumn(FILE_DATE);

		col1.setHeaderValue(dataModel.getColumnName(FILE_USER_ALIAS));
		col2.setHeaderValue(dataModel.getColumnName(FILE_ALIAS));
		col3.setHeaderValue(dataModel.getColumnName(FILE_LENGTH));
		col4.setHeaderValue(dataModel.getColumnName(FILE_DATE));

		columnModel.addColumn(col1);
		columnModel.addColumn(col2);
		columnModel.addColumn(col3);
		columnModel.addColumn(col4);

		jTable1.setColumnModel(columnModel);

		jTable1.setShowVerticalLines(false);

		titledBorder1 = new TitledBorder("");
		titledBorder1.setTitle(LangModule.i18n.getString("FtpPageGuiBorder"));
		titledBorder1.setTitleJustification(2);

		downloadButton.setText(LangModule.i18n.getString("FtpPageGuiButton1"));
		downloadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				pressedDownloadButton();
			}
		});

		uploadButton.setText(LangModule.i18n.getString("FtpPageGuiButton2"));
		uploadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				pressedUploadButton();
			}
		});

		cancelButton.setText(LangModule.i18n.getString("ButtonLabelCancel"));
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				pressedCancelButton();
			}
		});

		jScrollPane1.setBorder(titledBorder1);

		this.add(jScrollPane1, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						5, 5, 5, 5), 0, 0));

		jScrollPane1.getViewport().add(jTable1);

		this.add(jProgressPanel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0));

		jProgressPanel.add(jLabelProgress);
		jProgressPanel.add(jProgressBar);

		this.add(jButtonPanel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0));

		jButtonPanel.add(uploadButton);
		jButtonPanel.add(downloadButton);
		jButtonPanel.add(cancelButton);

		// Disable the cancel button
		cancelButton.setEnabled(false);

		// Create the ring bell action timer
		actionTimerRingBell_ = new ActionTimerRingBell(ConfigModule
				.getNumber(CONF_ACTION_TIMER_SLEEP));
		actionTimerRingBell_.startWorking();

	}

	// Methods:
	// --------

	// Method:
	public synchronized void displayFtpUploadMsg(String userAlias,
			RCT.FileInfo ftpInfo) {

		((FtpData) dataModel).append(userAlias, ftpInfo);

		// If the client configuration for the
		// ftp bell ring event is set to true
		// we ring the system bell per ftp message
		if (ConfigModule.can(CONF_FTP_RING_BELL)) {

			actionTimerRingBell_.performAction();
		}
	}

	// Method:
	public synchronized void displayFtpUploadMsg(
			RCT.FtpMsgHistSeqHolder ftpMsgHistSeq) {

		((FtpData) dataModel).append(ftpMsgHistSeq);
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
		uploadButton.setEnabled(true);
		cancelButton.setEnabled(false);

		jLabelProgress.setText(LangModule.i18n.getString("FtpPageGuiLabel3"));
		jProgressBar.setValue(0);
	}

	// Callbacks:
	// ----------

	// Callback:
	public void pressedDownloadButton() {

		// Setting status
		buttonStatus_ = FTP_STATUS_DOWNLOAD;

		// Make sure the user selected a file
		if (-1 == jTable1.getSelectedRow()) {

			// No file is selected. Let the user know
			JOptionPane.showMessageDialog(this, LangModule.i18n
					.getString("FtpPageGuiDialog2"), LangModule.i18n
					.getString("FtpPageGuiDialog1"), JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Get selected row
		int selectedRow = jTable1.getSelectedRow();

		// Get the file content
		final String fileName = (String) dataModel.getValueAt(selectedRow,
				FILE_ALIAS);
		final String fileId = (String) dataModel.getValueAt(selectedRow,
				FILE_ID);
		final int fileSize = Integer.parseInt((String) dataModel.getValueAt(
				selectedRow, FILE_LENGTH));

		// Setting the fileDownloadId
		fileDownloadId_ = fileId;

		final JFileChooser fc = new JFileChooser();

		fc.setDialogTitle(LangModule.i18n.getString("FtpPageGuiDialog3")
				+ " [ " + fileName + " ]");
		fc.setSelectedFile(new File(fileName));

		// Capture the return value after clicking on
		// a button in the FileChooser
		int returnVal = fc.showSaveDialog(SessionModule.getFrame());

		if (JFileChooser.APPROVE_OPTION == returnVal) {

			Thread downloadThread = new Thread(new Runnable() {

				public void run() {

					// Enable/Disable buttons
					downloadButton.setEnabled(false);
					uploadButton.setEnabled(false);
					cancelButton.setEnabled(true);

					// Init JProgressBar
					jProgressBar.setStringPainted(true);
					jProgressBar.setMinimum(0);
					jProgressBar.setMaximum(fileSize);
					jProgressBar.setValue(0);

					jLabelProgress.setText(LangModule.i18n
							.getString("FtpPageGuiLabel2")
							+ " " + fileName);

					// Init with default value
					int msgType = FTP_TEAM_DOWNLOAD;

					// Now we send the file to the server
					if (PAGE_TEAM == pageType_) {

						msgType = FTP_TEAM_DOWNLOAD;
					} else if (PAGE_GROUP == pageType_) {

						msgType = FTP_GROUP_DOWNLOAD;
					} else {

						System.err.println("ERROR: Wrong page type!");
					}

					// Getting the file name from the save file chooser
					File newFile = fc.getSelectedFile();

					File file = new File(fc.getCurrentDirectory()
							.getAbsolutePath(), newFile.getName());

					FtpModule.download(fileId, file, className_, assemblyName_,
							msgType);
				}
			});

			// Start thread
			downloadThread.start();
		}
	}

	// Callback:
	public void pressedUploadButton() {

		// Setting status
		buttonStatus_ = FTP_STATUS_UPLOAD;

		Thread uploadThread = new Thread(new Runnable() {

			public void run() {

				// Enable/Disable buttons
				downloadButton.setEnabled(false);
				uploadButton.setEnabled(false);
				cancelButton.setEnabled(true);

				JFileChooser fc = new JFileChooser();

				int returnVal = fc.showOpenDialog(SessionModule.getFrame());

				if (JFileChooser.APPROVE_OPTION == returnVal) {

					String ftpFileAlias = fc.getSelectedFile().getName();

					jLabelProgress.setText(LangModule.i18n
							.getString("FtpPageGuiLabel1")
							+ " " + ftpFileAlias);

					// Getting the selected file
					File uploadFile = new File(fc.getCurrentDirectory()
							.getAbsolutePath(), ftpFileAlias);

					// Check if the selection is a file
					if (!uploadFile.isFile()) {

						// Enable/Disable buttons
						downloadButton.setEnabled(true);
						uploadButton.setEnabled(true);
						cancelButton.setEnabled(false);
						jLabelProgress.setText(LangModule.i18n
								.getString("FtpPageGuiLabel3"));
						jProgressBar.setValue(0);

						return;
					}

					// Data will hold the file data
					byte[] data = null;

					try {

						int fileSize = (int) uploadFile.length();

						// Check for service restrictions
						if (!ServiceModule.allowFileSize(fileSize)) {

							// Enable/Disable buttons
							downloadButton.setEnabled(true);
							uploadButton.setEnabled(true);
							cancelButton.setEnabled(false);
							jLabelProgress.setText(LangModule.i18n
									.getString("FtpPageGuiLabel3"));
							jProgressBar.setValue(0);

							return;
						}

						data = new byte[fileSize];

						DataInputStream in = new DataInputStream(
								new FileInputStream(uploadFile));

						in.readFully(data);
						in.close();
					} catch (Exception e) {

						System.err.println("EXCEPTION: pressedUploadButton()!");
					}

					// Now we get the file index
					String ftpId = FileModule.getIndex();
					String ftpFileName = MOD_PRE_FTP + ftpId;

					// Init with default value
					int msgType = FTP_TEAM_UPLOAD;

					// Now we send the file to the server
					if (PAGE_TEAM == pageType_) {

						msgType = FTP_TEAM_UPLOAD;
					} else if (PAGE_GROUP == pageType_) {

						msgType = FTP_GROUP_UPLOAD;
					} else {

						System.err.println("ERROR: Wrong page type!");
					}

					FtpModule.upload(ftpId, ftpFileAlias, ftpFileName,
							FTP_MIME_TYPE, className_, assemblyName_,
							UserModule.getId(), UserModule.getAlias(),
							RCT_VERSION, msgType,
							RCT.ObjPermission.UNCLASSIFIED, data, jProgressBar);
				}

				// Enable/Disable buttons
				downloadButton.setEnabled(true);
				uploadButton.setEnabled(true);
				cancelButton.setEnabled(false);
				jLabelProgress.setText(LangModule.i18n
						.getString("FtpPageGuiLabel3"));
				jProgressBar.setValue(0);
			}
		});

		// Start thread
		uploadThread.start();
	}

	// Callback:
	public void pressedCancelButton() {

		// Here we only disable the cancel button.
		// The other buttons are set on the callback message
		cancelButton.setEnabled(false);

		switch (buttonStatus_) {

		case FTP_STATUS_DOWNLOAD:

			try {

				FileModule.cancelDownload(fileDownloadId_, UserModule.getId(),
						className_, assemblyName_);
			} catch (Exception e) {
			}

			break;

		case FTP_STATUS_UPLOAD:

			FtpModule.fileUploadDownload.stop();

			jLabelProgress.setText(LangModule.i18n
					.getString("FtpPageGuiLabel3"));
			jProgressBar.setValue(0);

			break;

		default:
		// Nothing to do here
		}

		// Setting status
		buttonStatus_ = FTP_STATUS_CANCEL;
	}

	// Abstract Methods Implementation:
	// --------------------------------

	// Abstract Method:
	public void dispatchExitRequest() {

		actionTimerRingBell_.stopWorking();
	}
}

