// $Id: ArchiveFtpPageGui.java,v 1.11 2003/05/14 21:21:37 thomas Exp $

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
import org.openrct.client.module.FtpModule;
import org.openrct.client.module.FileModule;
import org.openrct.client.module.PageModule;
import org.openrct.client.module.UserModule;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;

public class ArchiveFtpPageGui extends JPanel implements Const {

	private JPanel jButtonPanel = new JPanel();

	private JPanel jProgressPanel = new JPanel();

	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private JButton downloadButton = new JButton();

	private JButton cancelButton = new JButton();

	private JButton closeButton = new JButton();

	private JProgressBar jProgressBar = new JProgressBar();

	private JLabel jLabelProgress = new JLabel();

	private JScrollPane jScrollPane1 = new JScrollPane();

	private TitledBorder titledBorder1;

	private JTable jTable1;

	private DefaultTableColumnModel columnModel;

	private TableModel dataModel_;

	private String className_;

	private String teamName_;

	private ArchivePageGui archivePage_;

	private String fromDate_;

	private String toDate_;

	private String fileDownloadId;

	public ArchiveFtpPageGui(String className, String teamName,
			ArchivePageGui archivePage, TableModel dataModel) {

		className_ = className;
		teamName_ = teamName;
		archivePage_ = archivePage;
		dataModel_ = dataModel;

		// Notify Page page module about this page
		PageModule.putArchivePage(className_, teamName_, this);

		jLabelProgress.setText(LangModule.i18n.getString("FtpPageGuiLabel3"));

		jTable1 = new JTable();
		jTable1.setFont(UNICODE_FONT_16);
		jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jTable1.setAutoCreateColumnsFromModel(false);
		jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		jTable1.setModel(dataModel_);

		columnModel = new DefaultTableColumnModel();

		TableColumn col1 = new TableColumn(FILE_USER_ALIAS);
		TableColumn col2 = new TableColumn(FILE_ALIAS);
		TableColumn col3 = new TableColumn(FILE_LENGTH);
		TableColumn col4 = new TableColumn(FILE_DATE);

		col1.setHeaderValue(dataModel_.getColumnName(FILE_USER_ALIAS));
		col2.setHeaderValue(dataModel_.getColumnName(FILE_ALIAS));
		col3.setHeaderValue(dataModel_.getColumnName(FILE_LENGTH));
		col4.setHeaderValue(dataModel_.getColumnName(FILE_DATE));

		columnModel.addColumn(col1);
		columnModel.addColumn(col2);
		columnModel.addColumn(col3);
		columnModel.addColumn(col4);

		jTable1.setColumnModel(columnModel);

		jTable1.setShowVerticalLines(false);

		titledBorder1 = new TitledBorder("");

		this.setLayout(gridBagLayout1);

		titledBorder1.setTitle(LangModule.i18n.getString("FtpPageGuiBorder"));
		titledBorder1.setTitleJustification(2);

		downloadButton.setText(LangModule.i18n.getString("FtpPageGuiButton1"));
		downloadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				pressedDownloadButton();
			}
		});

		cancelButton.setText(LangModule.i18n.getString("ButtonLabelCancel"));
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				pressedCancelButton();
			}
		});

		closeButton.setText(LangModule.i18n.getString("ButtonLabelClose"));
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				pressedCloseButton();
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

		jButtonPanel.add(downloadButton);
		jButtonPanel.add(cancelButton);
		jButtonPanel.add(closeButton);

		// Disable the cancel button
		cancelButton.setEnabled(false);
	}

	// Methods:
	// --------

	// Method:
	public void incrementProgressBar(int incrementValue) {

		int currentValue = jProgressBar.getValue();
		jProgressBar.setValue(currentValue + incrementValue);
	}

	// Method:
	public void endDownload() {

		// Enable/Disable buttons
		downloadButton.setEnabled(true);
		closeButton.setEnabled(true);
		cancelButton.setEnabled(false);
		jLabelProgress.setText(LangModule.i18n.getString("FtpPageGuiLabel3"));
		jProgressBar.setValue(0);
	}

	// Callbacks:
	// ----------

	// Callback:
	public void pressedDownloadButton() {

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

		// Get the course content source
		final String fileName = (String) dataModel_.getValueAt(selectedRow,
				FILE_ALIAS);
		final String fileId = (String) dataModel_.getValueAt(selectedRow,
				FILE_ID);
		final int fileSize = Integer.parseInt((String) dataModel_.getValueAt(
				selectedRow, FILE_LENGTH));

		// Setting the fileDownloadId
		fileDownloadId = fileId;

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

					downloadButton.setEnabled(false);
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

					FtpModule.download(fileId, file, className_, teamName_,
							FTP_ARCHIVE_DOWNLOAD);
				}
			});

			// Start thread
			downloadThread.start();
		}
	}

	// Callback
	public void pressedCancelButton() {

		// Enable/Disable buttons
		cancelButton.setEnabled(false);

		try {

			FileModule.cancelDownload(fileDownloadId, UserModule.getId(),
					className_, teamName_);
		} catch (Exception e) {
		}
	}

	// Callback
	public void pressedCloseButton() {

		PageModule.removeArchivePage(className_, teamName_);
		archivePage_.closePage();
	}
}

