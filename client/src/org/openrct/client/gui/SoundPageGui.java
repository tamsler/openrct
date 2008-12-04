// $Id: SoundPageGui.java,v 1.16 2003/05/14 21:21:37 thomas Exp $

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
import org.openrct.client.module.SoundModule;
import org.openrct.client.module.FileModule;
import org.openrct.client.module.SessionModule;
import org.openrct.client.module.UserModule;
import org.openrct.client.module.CacheModule;
import org.openrct.client.util.SoundRecordingTimerThread;
import org.openrct.client.util.AudioRecorder;
import org.openrct.client.util.AudioPlayer;
import org.openrct.client.util.ActionTimerRingBell;
import org.openrct.client.util.Utility;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;

public class SoundPageGui extends UncontrolledModulePageGui {

	private JPanel jButtonPanel = new JPanel();

	private JPanel jProgressPanel = new JPanel();

	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private JButton playButton = new JButton();

	private JButton stopButton = new JButton();

	private JButton recordButton = new JButton();

	private JButton cancelButton = new JButton();

	private JProgressBar jProgressBar = new JProgressBar();

	private JLabel jLabelProgress = new JLabel();

	private JScrollPane jScrollPane1 = new JScrollPane();

	private TitledBorder titledBorder1;

	private JTable jTable1;

	private DefaultTableColumnModel columnModel;

	private TableModel dataModel;

	// Flag used for determining when to stop recording
	// in case user forgets to click the stop button
	private SoundRecordingTimerThread soundRecordingTimer_;

	private AudioRecorder audioRecorder_;

	private AudioPlayer audioPlayer_;

	private int audioActionType_;

	private int buttonStatus_ = SOUND_STATUS_UNDEF;

	private String soundId_;

	private ActionTimerRingBell actionTimerRingBell_;

	// The counter is used for the temp sound file name creation
	private int counter_;

	public SoundPageGui(String className, String assemblyName, int pageType) {

		super(className, assemblyName, MODULE_SOUND, pageType);

		// Init counter to zero
		counter_ = 0;

		jLabelProgress.setText(LangModule.i18n.getString("SoundPageGuiLabel3"));

		dataModel = new SoundData(className, assemblyName, pageType);

		jTable1 = new JTable();
		jTable1.setFont(UNICODE_FONT_12);
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
				pressedStopButton(USER_PRESSED_STOP_BUTTON);
			}
		});

		recordButton
				.setText(LangModule.i18n.getString("SoundPageButtonRecord"));
		recordButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				pressedRecordButton();
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
		jButtonPanel.add(recordButton);
		jButtonPanel.add(cancelButton);

		// Disable the stop and cancel button
		stopButton.setEnabled(false);
		cancelButton.setEnabled(false);
		recordButton.setEnabled(ConfigModule.can(CONF_SND_RECORD));

		// Create the ring bell action timer
		actionTimerRingBell_ = new ActionTimerRingBell(ConfigModule
				.getNumber(CONF_ACTION_TIMER_SLEEP));
		actionTimerRingBell_.startWorking();
	}

	// Methods:
	// --------

	// Method:
	private void incrCounter() {

		counter_++;
	}

	// Method:
	public synchronized void displaySoundMsg(String userAlias,
			RCT.FileInfo sndInfo) {

		((SoundData) dataModel).append(userAlias, sndInfo);

		// If the client configuration for the
		// sound bell ring event is set to true
		// we ring the system bell per sound message
		if (ConfigModule.can(CONF_SND_RING_BELL)) {

			actionTimerRingBell_.performAction();
		}
	}

	// Method:
	public synchronized void displaySoundMsg(
			RCT.SoundMsgHistSeqHolder soundMsgHistSeq) {

		((SoundData) dataModel).append(soundMsgHistSeq);
	}

	// Method:
	public void incrementProgressBar(int incrementValue) {

		int currentValue = jProgressBar.getValue();
		jProgressBar.setValue(currentValue + incrementValue);
	}

	// Method:
	public void endDownload() {

		// Enable/Disable buttons
		playButton.setEnabled(ConfigModule.can(CONF_SND_PLAY));
		recordButton.setEnabled(ConfigModule.can(CONF_SND_RECORD));
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
		recordButton.setEnabled(false);
		cancelButton.setEnabled(false);

		audioPlayer_ = new AudioPlayer(FileModule.getTempDataDirName() + FS
				+ audioName, playButton, recordButton, stopButton,
				cancelButton, new JButton());

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

			// No sound is selected. Let the user know
			JOptionPane.showMessageDialog(this, LangModule.i18n
					.getString("SoundPageDialog1"), LangModule.i18n
					.getString("SoundPage"), JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Setting the audio action type
		audioActionType_ = TYPE_PLAY;

		// Get selcted row
		int selectedRow = jTable1.getSelectedRow();

		// Get sound message information
		String soundMsgId = (String) dataModel.getValueAt(selectedRow, FILE_ID);
		String soundAlias = (String) dataModel.getValueAt(selectedRow,
				FILE_ALIAS);
		int fileSize = Integer.parseInt((String) dataModel.getValueAt(
				selectedRow, FILE_LENGTH));

		// Setting the sound id
		soundId_ = soundMsgId;

		String fileName = MODULE_SOUND + "_" + soundMsgId;

		// Init with default value
		int msgType = SOUND_TEAM_PLAY;

		// team or group type message
		if (PAGE_TEAM == pageType_) {

			msgType = SOUND_TEAM_PLAY;
		} else if (PAGE_GROUP == pageType_) {

			msgType = SOUND_GROUP_PLAY;
		} else {

			System.err.println("ERROR: Wrong page type!");
		}

		File file = new File(FileModule.getTempDataDirName(), fileName);

		// Get the sound message
		if (SoundModule.fetch(soundMsgId, file, className_, assemblyName_,
				msgType)) {

			playAudio(fileName);
		} else {

			// Set button state
			playButton.setEnabled(false);
			recordButton.setEnabled(false);
			stopButton.setEnabled(false);
			cancelButton.setEnabled(true);

			// Init JProgressBar
			jProgressBar.setStringPainted(true);
			jProgressBar.setMinimum(0);
			jProgressBar.setMaximum(fileSize);
			jProgressBar.setValue(0);

			jLabelProgress.setText(LangModule.i18n
					.getString("SoundPageGuiLabel2")
					+ " " + soundAlias);

			SoundModule.download(soundMsgId, file, className_, assemblyName_,
					msgType);
		}
	}

	// Callback:
	public void pressedStopButton(final int type) {

		// Setting status
		buttonStatus_ = SOUND_STATUS_STOP;

		Thread stopThread = new Thread(new Runnable() {

			public void run() {

				// Setting button state
				stopButton.setEnabled(false);
				cancelButton.setEnabled(false);
				playButton.setEnabled(ConfigModule.can(CONF_SND_PLAY));
				recordButton.setEnabled(ConfigModule.can(CONF_SND_RECORD));

				if (TYPE_RECORD == audioActionType_) {

					// Signal the Sound Module that we are done
					// with the current recording
					SoundModule.stopRecording();

					// Setting the stop recording flag
					// Since the user click on the stop button
					// we can reset the flag
					soundRecordingTimer_.halt();

					// Stop the recording
					audioRecorder_.stopAudioRecord();

					// Prompt the user for a sound name/alias
					String audioName = null;
					InputDialogUnicode dialog = null;

					if (USER_PRESSED_STOP_BUTTON == type) {
						dialog = new InputDialogUnicode(SessionModule
								.getFrame(), LangModule.i18n
								.getString("CommonLabelInput"), LangModule.i18n
								.getString("SoundPageDialog2"));
					} else if (AUTO_PRESSED_STOP_BUTTON == type) {

						JOptionPane.showMessageDialog(null, LangModule.i18n
								.getString("SoundPageDialog7-1"),
								LangModule.i18n.getString("SoundPageDialog4"),
								JOptionPane.INFORMATION_MESSAGE);

						dialog = new InputDialogUnicode(SessionModule
								.getFrame(), LangModule.i18n
								.getString("CommonLabelInput"), LangModule.i18n
								.getString("SoundPageDialog7-2"));
					}

					// Check which button was clicked on
					switch (dialog.getValue().getButtonValue()) {

					case JOptionPane.OK_OPTION:

						audioName = dialog.getValue().getInputFieldValue();

						break;

					default:

					// For all other options we don't do anything
					}

					// User clicked on cancel button
					if (null == audioName) {

						// nothing to do
					}
					// User entered a zero length string
					else if (0 == audioName.length()) {

						JOptionPane.showMessageDialog(SessionModule.getFrame(),
								LangModule.i18n.getString("SoundPageDialog3"),
								LangModule.i18n.getString("SoundPageDialog4"),
								JOptionPane.ERROR_MESSAGE);
					}
					// User enterd a quoted string
					else if (Utility.hasBadChars(audioName)) {

						JOptionPane.showMessageDialog(SessionModule.getFrame(),
								LangModule.i18n.getString("SoundPageDialog5"),
								LangModule.i18n.getString("SoundPageDialog4"),
								JOptionPane.ERROR_MESSAGE);
					}
					// No errors
					else {

						// Getting the sound file id
						String soundId = FileModule.getIndex();

						// Create path and name for temp file
						String auName = SoundModule.createTempName(counter_);

						// Create the sound name
						String soundName = SOUND_PREFIX + soundId
								+ SOUND_EXT_AU;

						// Create files
						File auFile = new File(auName);

						// Data will hold the sound file data
						byte[] data = null;

						// Reading sound data into data array
						try {

							int fileSize = (int) auFile.length();
							data = new byte[fileSize];

							DataInputStream in = new DataInputStream(
									new FileInputStream(auFile));

							in.readFully(data);
							in.close();
						} catch (Exception e) {

							System.err.println(LangModule.i18n
									.getString("SoundPageError2"));
						}

						// After reading the temp sound file data, we can delete
						// it
						auFile.delete();

						// Init iwth default value
						int msgType = SOUND_TEAM;

						// Now we send the sound file to the server
						if (PAGE_TEAM == pageType_) {

							msgType = SOUND_TEAM;
						} else if (PAGE_GROUP == pageType_) {

							msgType = SOUND_GROUP;
						} else {

							System.err.println(LangModule.i18n
									.getString("SoundPageError3"));
						}

						// Enable the cancel button
						cancelButton.setEnabled(true);
						playButton.setEnabled(false);
						recordButton.setEnabled(false);
						stopButton.setEnabled(false);

						jLabelProgress.setText(LangModule.i18n
								.getString("SoundPageGuiLabel1")
								+ " " + audioName);

						SoundModule.send(soundId, audioName, soundName,
								SOUND_MIME_TYPE, className_, assemblyName_,
								UserModule.getId(), UserModule.getAlias(),
								RCT_VERSION, msgType,
								RCT.ObjPermission.UNCLASSIFIED, data,
								jProgressBar);

						cancelButton.setEnabled(false);
						playButton.setEnabled(ConfigModule.can(CONF_SND_PLAY));
						recordButton.setEnabled(ConfigModule
								.can(CONF_SND_RECORD));
						jLabelProgress.setText(LangModule.i18n
								.getString("SoundPageGuiLabel3"));
						jProgressBar.setValue(0);

						// Putting the created sound message in cache
						RCT.BinaryFileHolder fileData = new RCT.BinaryFileHolder();
						fileData.value = data;
						CacheModule.put(soundId, MOD_SOUND, fileData,
								MODULE_SOUND + "_" + soundId);
					}
				} else if (TYPE_PLAY == audioActionType_) {

					audioPlayer_.stopAudioPlay();
				} else {

					System.err.println(LangModule.i18n
							.getString("SoundPageError4"));
				}
			}
		});

		// Start thread
		stopThread.start();
	}

	// Callback:
	public void pressedRecordButton() {

		// Setting status
		buttonStatus_ = SOUND_STATUS_RECORD;

		// Check if a recording is in process
		if (!SoundModule.canStartRecording()) {

			JOptionPane.showMessageDialog(null, LangModule.i18n
					.getString("SoundPageDialog8"), LangModule.i18n
					.getString("SoundPageDialog4"),
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		// We have to make sure that after the user
		// pressed the recording button, that we limit
		// the max recording time. This also takes care
		// for cases when the user forgets to click on
		// the stop button after starting the recording.
		soundRecordingTimer_ = new SoundRecordingTimerThread(this);
		soundRecordingTimer_.start();

		// Setting the audio action type
		audioActionType_ = TYPE_RECORD;

		// Setting button state
		stopButton.setEnabled(ConfigModule.can(CONF_SND_STOP));
		playButton.setEnabled(false);
		recordButton.setEnabled(false);
		cancelButton.setEnabled(false);

		// Increment the counter
		incrCounter();

		audioRecorder_ = new AudioRecorder(counter_, playButton, recordButton,
				stopButton);
		audioRecorder_.start();
	}

	// Callback:
	public void pressedCancelButton() {

		// Here we only disable the cancel button.
		// The other buttons are set on the callback message
		cancelButton.setEnabled(false);

		switch (buttonStatus_) {

		case SOUND_STATUS_PLAY:

			try {

				FileModule.cancelDownload(soundId_, UserModule.getId(),
						className_, assemblyName_);
			} catch (Exception e) {
			}

			break;

		case SOUND_STATUS_RECORD:

			SoundModule.fileUploadDownload.stop();

			jLabelProgress.setText(LangModule.i18n
					.getString("SoundPageGuiLabel3"));
			jProgressBar.setValue(0);

			break;

		default:

		//Nothing to do here
		}

		// Setting status
		buttonStatus_ = SOUND_STATUS_CANCEL;
	}

	// Abstract Methods Implementation:
	// --------------------------------

	// Abstarct Method:
	public void dispatchExitRequest() {

		actionTimerRingBell_.stopWorking();
	}
}

