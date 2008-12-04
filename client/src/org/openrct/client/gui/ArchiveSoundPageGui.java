// $Id: ArchiveSoundPageGui.java,v 1.10 2003/05/14 21:21:37 thomas Exp $

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
import org.openrct.client.module.SoundModule;
import org.openrct.client.module.ConfigModule;
import org.openrct.client.module.FileModule;
import org.openrct.client.module.PageModule;
import org.openrct.client.module.UserModule;
import org.openrct.client.util.AudioPlayer;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;

public class ArchiveSoundPageGui extends JPanel implements Const {

	private JPanel jButtonPanel = new JPanel();

	private JPanel jProgressPanel = new JPanel();

	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private JButton playButton = new JButton();

	private JButton stopButton = new JButton();

	private JButton closeButton = new JButton();

	private JButton cancelButton = new JButton();

	private JButton recordButton = new JButton();

	private JProgressBar jProgressBar = new JProgressBar();

	private JLabel jLabelProgress = new JLabel();

	private JScrollPane jScrollPane1 = new JScrollPane();

	private TitledBorder titledBorder1;

	private JTable jTable1;

	private DefaultTableColumnModel columnModel;

	private TableModel dataModel_;

	private AudioPlayer audioPlayer_;

	private String className_;

	private String teamName_;

	private ArchivePageGui archivePage_;

	private String fromDate_;

	private String toDate_;

	private int buttonStatus_ = SOUND_STATUS_UNDEF;

	private String soundId_;

	// Constructor
	public ArchiveSoundPageGui(String className, String teamName,
			ArchivePageGui archivePage, TableModel dataModel) {

		className_ = className;
		teamName_ = teamName;
		archivePage_ = archivePage;
		dataModel_ = dataModel;

		PageModule.putArchivePage(className_, teamName_, this);

		jLabelProgress.setText(LangModule.i18n.getString("SoundPageGuiLabel3"));

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

		playButton.setText(LangModule.i18n.getString("SoundPageButtonPlay"));
		playButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				pressedPlayButton();
			}
		});

		stopButton.setText(LangModule.i18n.getString("SoundPageButtonStop"));
		stopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				pressedStopButton();
			}
		});

		closeButton.setText(LangModule.i18n.getString("ButtonLabelClose"));
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				pressedCloseButton();
			}
		});

		cancelButton.setText(LangModule.i18n.getString("ButtonLabelCancel"));
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				pressedCancelButton();
			}
		});

		jScrollPane1.setAutoscrolls(true);

		titledBorder1.setTitle(LangModule.i18n.getString("SoundPageBorder"));
		titledBorder1.setTitleJustification(2);

		jScrollPane1.setBorder(titledBorder1);

		this.add(jScrollPane1, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(
						5, 5, 5, 5), 0, 0));

		jScrollPane1.getViewport().add(jTable1);

		this.add(jProgressPanel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0));

		jProgressPanel.add(jLabelProgress);
		jProgressPanel.add(jProgressBar);

		this.add(jButtonPanel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0));

		jButtonPanel.add(playButton);
		jButtonPanel.add(stopButton);
		jButtonPanel.add(cancelButton);
		jButtonPanel.add(closeButton);

		// Disable the stop button
		stopButton.setEnabled(false);
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
		playButton.setEnabled(ConfigModule.can(CONF_SND_PLAY));
		closeButton.setEnabled(true);
		stopButton.setEnabled(false);
		cancelButton.setEnabled(false);

		jLabelProgress.setText(LangModule.i18n.getString("SoundPageGuiLabel3"));
		jProgressBar.setValue(0);
	}

	// Method:
	// Play sound message
	public void playAudio(String audioName) {

		// Setting button state
		stopButton.setEnabled(ConfigModule.can(CONF_SND_STOP));
		playButton.setEnabled(false);
		cancelButton.setEnabled(false);
		closeButton.setEnabled(false);

		audioPlayer_ = new AudioPlayer(FileModule.getTempDataDirName() + FS
				+ audioName, playButton, recordButton, stopButton,
				cancelButton, closeButton);

		Thread audioPlayerThread = new Thread(audioPlayer_, "AudioPlayer");

		audioPlayerThread.start();
	}

	// Callbacks:
	// ----------

	// Callback:
	public void pressedPlayButton() {

		// Setting status
		buttonStatus_ = SOUND_STATUS_PLAY;

		// Make sure the user selected a sound message
		if (-1 == jTable1.getSelectedRow()) {

			// No course content is selected. Let the user know
			JOptionPane.showMessageDialog(this, LangModule.i18n
					.getString("SoundPageDialog1"), LangModule.i18n
					.getString("SoundPage"), JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Get selcted row
		int selectedRow = jTable1.getSelectedRow();

		// Get sound message information
		String soundMsgId = (String) dataModel_
				.getValueAt(selectedRow, FILE_ID);
		String soundAlias = (String) dataModel_.getValueAt(selectedRow,
				FILE_ALIAS);
		int fileSize = Integer.parseInt((String) dataModel_.getValueAt(
				selectedRow, FILE_LENGTH));

		// Setting the sound id
		soundId_ = soundMsgId;

		String fileName = MODULE_SOUND + "_" + soundMsgId;

		File file = new File(FileModule.getTempDataDirName(), fileName);

		// Get the sound message
		if (SoundModule.fetch(soundMsgId, file, className_, teamName_,
				SOUND_ARCHIVE_PLAY)) {

			playAudio(fileName);
		} else {

			// Set button state
			playButton.setEnabled(false);
			stopButton.setEnabled(false);
			closeButton.setEnabled(false);
			cancelButton.setEnabled(true);

			// Init JProgressBar
			jProgressBar.setStringPainted(true);
			jProgressBar.setMinimum(0);
			jProgressBar.setMaximum(fileSize);
			jProgressBar.setValue(0);

			jLabelProgress.setText(LangModule.i18n
					.getString("SoundPageGuiLabel2")
					+ " " + soundAlias);

			SoundModule.download(soundMsgId, file, className_, teamName_,
					SOUND_ARCHIVE_PLAY);
		}
	}

	// Callback:
	public void pressedStopButton() {

		// Setting status
		buttonStatus_ = SOUND_STATUS_STOP;

		Thread stopThread = new Thread(new Runnable() {

			public void run() {

				// Setting button state
				stopButton.setEnabled(false);
				cancelButton.setEnabled(false);
				playButton.setEnabled(ConfigModule.can(CONF_SND_PLAY));
				closeButton.setEnabled(true);

				audioPlayer_.stopAudioPlay();
			}
		});

		// Start thread
		stopThread.start();
	}

	// Callback
	public void pressedCloseButton() {

		PageModule.removeArchivePage(className_, teamName_);
		archivePage_.closePage();
	}

	// Callback:
	public void pressedCancelButton() {

		// Here we only disable the cancel button.
		// The other buttons are set on callback message
		cancelButton.setEnabled(false);

		switch (buttonStatus_) {

		case SOUND_STATUS_PLAY:

			try {

				FileModule.cancelDownload(soundId_, UserModule.getId(),
						className_, teamName_);
			} catch (Exception e) {
			}

			break;

		default:

		//Nothing to do here
		}

		// Setting status
		buttonStatus_ = SOUND_STATUS_CANCEL;
	}

}

