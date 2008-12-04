// $Id: TextpadPageGui.java,v 1.4 2003/05/08 20:09:03 thomas Exp $

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

import org.openrct.client.module.ConfigModule;
import org.openrct.client.module.LangModule;
import org.openrct.client.module.TextpadModule;
import org.openrct.client.module.UserModule;
import org.openrct.client.module.CacheModule;
import org.openrct.client.module.SessionModule;
import org.openrct.client.module.FileModule;
import org.openrct.client.util.RctColor;
import org.openrct.client.util.Utility;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.*;
import org.omg.CORBA.*;

public class TextpadPageGui extends ControlledModulePageGui {

	private JButton newButton = new JButton();

	private JButton closeButton = new JButton();

	private JButton saveButton = new JButton();

	private JButton copyButton = new JButton();

	private JButton pasteButton = new JButton();

	private JButton editButton = new JButton();

	private JPanel buttonPanel = new JPanel();

	private JTextArea textArea = new JTextArea();

	private JScrollPane textScrollPane = new JScrollPane();

	private TitledBorder textBorder;

	private Document doc_;

	private boolean hasTextArea_ = false;

	private boolean canEdit_ = false;

	private String oldStr_ = "";

	private String newStr_ = "";

	private int offset_ = 0;

	private int ref_ = 0;

	private String id_ = "";

	private String name_ = "";

	private TextpadWorkerThread textpadWorker_ = null;

	public TextpadPageGui(String className, String assemblyName, int pageType,
			boolean isManaged) {

		super(className, assemblyName, MODULE_TEXTPAD, pageType, isManaged);

		textBorder = new TitledBorder("");
		textBorder.setTitleFont(new Font(ConfigModule
				.getString(CONF_UNICODE_FONT), Font.PLAIN, ConfigModule
				.getNumber(CONF_UNICODE_FONT_SIZE)));

		modulePanel.setLayout(new GridBagLayout());

		textScrollPane.setAutoscrolls(true);
		textScrollPane.setBorder(textBorder);

		displayTextName(LangModule.i18n.getString("TextpadPageDefaultName"));
		textBorder.setTitleJustification(2);

		// Setting up text area and make it not editable by default
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setAutoscrolls(true);
		textArea.setFont(new Font(ConfigModule.getString(CONF_UNICODE_FONT),
				Font.PLAIN, ConfigModule.getNumber(CONF_UNICODE_FONT_SIZE)));

		// Getting the textArea's document
		doc_ = textArea.getDocument();

		// Label all the buttons and attach callback methods
		newButton.setText(LangModule.i18n.getString("ButtonLabelNew"));
		newButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				pressedNewButton();
			}
		});

		closeButton.setText(LangModule.i18n.getString("ButtonLabelClose"));
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				pressedCloseButton();
			}
		});

		saveButton.setText(LangModule.i18n.getString("ButtonLabelSave"));
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				pressedSaveButton();
			}
		});

		copyButton.setText(LangModule.i18n.getString("ButtonLabelCopy"));
		copyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				pressedCopyButton();
			}
		});

		pasteButton.setText(LangModule.i18n.getString("ButtonLabelPaste"));
		pasteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				pressedPasteButton();
			}
		});

		editButton.setText(LangModule.i18n.getString("ButtonLabelEdit1"));
		editButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				pressedEditButton();
			}
		});

		// Adding button panel to panel
		modulePanel.add(buttonPanel, new GridBagConstraints(0, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(5, 5, 5, 5), 0, 0));

		// Adding text area to panel
		modulePanel.add(textScrollPane, new GridBagConstraints(0, 1, 1, 1, 1.0,
				1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(5, 5, 5, 5), 0, 0));

		textScrollPane.getViewport().add(textArea);

		buttonPanel.add(newButton);
		buttonPanel.add(editButton);
		buttonPanel.add(saveButton);
		buttonPanel.add(copyButton);
		buttonPanel.add(pasteButton);
		buttonPanel.add(closeButton);

		// By default, textarea and buttons are not enabled
		textArea.setEditable(false);
		newButton.setEnabled(false);
		editButton.setEnabled(false);
		saveButton.setEnabled(false);
		closeButton.setEnabled(false);
		copyButton.setEnabled(false);
		pasteButton.setEnabled(false);

		// If this is in a group, we don't show the edit button
		if (PAGE_GROUP == pageType) {

			editButton.setVisible(false);
		}

		StringHolder name = new StringHolder();
		StringHolder id = new StringHolder();
		IntHolder ref = new IntHolder();
		StringHolder text = new StringHolder();

		if (TextpadModule.isActive(className_, assemblyName, pageType_, name,
				id, ref, text)) {

			try {

				doc_.insertString(0, text.value, null);
			} catch (BadLocationException e) {

				System.err.println("EXCEPTION: BadLocationException");
			}

			// Init all the textpad data
			ref_ = ref.value;
			name_ = name.value;
			id_ = id.value;

			displayTextName(name_);

			// Setting the textarea to true
			hasTextArea_ = true;

			// Start textpad worker thread after we
			// got the server textpad ref number
			textpadWorker_ = new TextpadWorkerThread();
			textpadWorker_.start();

			// Show start of text area
			textArea.setCaretPosition(0);
		}
	}

	// Methods:
	// --------

	// Method:
	// This method is called after clicking on the close button
	private void helperClose() {

		// Stop the worker thread
		if (null != textpadWorker_) {

			textpadWorker_.stopWorking();
			textpadWorker_ = null;
		}

		// After closing the textpad, we need to set
		// all the button and text area states to inactive.
		// Only the new button is active to create a new textpad.

		// Setting flag that text area is not ready
		hasTextArea_ = false;

		textArea.setEditable(false);

		newButton.setEnabled(true);
		editButton.setEnabled(true);
		saveButton.setEnabled(false);
		closeButton.setEnabled(false);
		copyButton.setEnabled(false);
		pasteButton.setEnabled(false);

		// Send message to the server
		TextpadModule.close(id_, ref_, RCT_VERSION, UserModule.getId(),
				UserModule.getAlias(), RCT.ObjPermission.UNCLASSIFIED,
				className_, assemblyName_,
				(PAGE_TEAM == pageType_) ? TEXTPAD_TEAM_CLOSE
						: TEXTPAD_GROUP_CLOSE);
	}

	// Method: Change Font for text area
	public void dispatchFontChange() {

		textArea.setFont(new Font(ConfigModule.getString(CONF_UNICODE_FONT),
				Font.PLAIN, ConfigModule.getNumber(CONF_UNICODE_FONT_SIZE)));
	}

	// Method: set/display the textarea name
	private void displayTextName(String name) {

		textBorder.setTitle(LangModule.i18n.getString("TextpadPageBorder")
				+ ": " + name);
		this.validate();
		this.repaint();
	}

	// Method: Display create textpad
	public synchronized void displayCreate(String userAlias, String name,
			int ref, String id) {

		// Setting Textarea flag
		hasTextArea_ = true;

		// Setting textpad ref, name, and id
		ref_ = ref;
		name_ = name;
		id_ = id;

		displayTextName(name_);

		// Init strings
		oldStr_ = "";
		newStr_ = "";

		// Start textpad worker thread after we
		// got the server textpad ref number
		textpadWorker_ = new TextpadWorkerThread();
		textpadWorker_.start();
	}

	// Method: Display create textpad
	public synchronized void displayEdit(String userAlias, String name,
			int ref, String id, String text) {

		// Setting textpad ref, name, and id
		ref_ = ref;
		name_ = name;
		id_ = id;

		// Display the name
		displayTextName(name_);

		// Setting Textarea flag
		hasTextArea_ = true;

		try {

			doc_.insertString(0, text, null);
		} catch (BadLocationException e) {

			System.err.println("EXCEPTION: BadLocationException");
		}

		// Init strings
		oldStr_ = text;
		newStr_ = text;

		// Start textpad worker thread after we
		// got the server textpad ref number
		textpadWorker_ = new TextpadWorkerThread();
		textpadWorker_.start();

		// Show start of text area
		textArea.setCaretPosition(0);
	}

	// Method: Display close textpad
	public synchronized void displayClose() {

		// Remove the associated cache entries
		// This is for the case when we edited a textpad
		if (!CacheModule.remove(id_, MODULE_TEXTPAD_ + id_, MOD_TEXTPAD)) {

			// Nothing to do here
		}

		// Stopping the text area worker thread
		if (null != textpadWorker_) {

			textpadWorker_.stopWorking();
			textpadWorker_ = null;
		}

		// Setting Textarea flag
		hasTextArea_ = false;

		displayTextName(LangModule.i18n.getString("TextpadPageDefaultName"));

		// When we close the textpad, we need to blank
		// the text area.
		try {

			doc_.remove(0, doc_.getLength());
		} catch (Exception e) {
		}
	}

	// Method: Display Update Rem Ins
	public synchronized void displayRemIns(String userAlias, int offset,
			String text) {

		if (userAlias.equals(UserModule.getAlias())) {

			return;
		}

		try {

			textArea.requestFocus();
			textArea.setCaretPosition(offset);

			textArea.setSelectedTextColor(Color.red);
			textArea.setSelectionColor(RctColor.lavendar);

			int range1 = doc_.getLength() - offset;

			doc_.remove(offset, range1);
			doc_.insertString(offset, text, null);

			if (range1 > text.length()) {

				int line = textArea.getLineOfOffset(offset);
				int start = textArea.getLineStartOffset(line);

				textArea.setCaretPosition(start);
				textArea.setSelectionStart(start);
				textArea.moveCaretPosition(offset - (range1 - text.length()));
				textArea.setSelectionEnd(offset - (range1 - text.length()));
			} else {

				textArea.setCaretPosition(offset);
				textArea.setSelectionStart(offset);
				textArea.moveCaretPosition(offset + text.length() - range1);
				textArea.setSelectionEnd(offset + text.length() - range1);
			}
		} catch (Exception e) {
		}
	}

	// Method: Display Update Rem Ins
	public synchronized void displayRem(String userAlias, int offset,
			String text) {

		if (userAlias.equals(UserModule.getAlias())) {

			return;
		}

		try {

			textArea.requestFocus();
			textArea.setCaretPosition(offset);

			textArea.setSelectedTextColor(Color.red);
			textArea.setSelectionColor(RctColor.lavendar);

			doc_.remove(offset, doc_.getLength() - offset);

			int line = textArea.getLineOfOffset(offset);
			int start = textArea.getLineStartOffset(line);

			textArea.setCaretPosition(start);
			textArea.setSelectionStart(start);
			textArea.moveCaretPosition(offset);
			textArea.setSelectionEnd(offset);
		} catch (Exception e) {
		}
	}

	// Method: Display Update Rem Ins
	public synchronized void displayIns(String userAlias, int offset,
			String text) {

		if (userAlias.equals(UserModule.getAlias())) {

			return;
		}

		try {

			textArea.requestFocus();
			textArea.setSelectedTextColor(Color.red);
			textArea.setSelectionColor(RctColor.lavendar);

			int line = textArea.getLineOfOffset(offset);
			int start = textArea.getLineStartOffset(line);

			textArea.setCaretPosition(offset);
			textArea.setSelectionStart(offset);

			doc_.insertString(offset, text, null);

			int end = textArea.getLineEndOffset(line);

			textArea.moveCaretPosition(end);
			textArea.setSelectionEnd(end);

		} catch (Exception e) {
		}
	}

	// Callbacks:
	// ----------

	// Callback:
	public void pressedNewButton() {

		// Prompt the user to enter a textpad name, subject
		String name = null;

		InputDialogUnicode dialog = new InputDialogUnicode(SessionModule
				.getFrame(), LangModule.i18n.getString("CommonLabelInput"),
				LangModule.i18n.getString("TextpadPageDialog1"));

		// Check which button was clicked on
		switch (dialog.getValue().getButtonValue()) {

		case JOptionPane.OK_OPTION:

			name = dialog.getValue().getInputFieldValue();
			break;

		default:

			// For all other options we just return
			return;
		}

		// Checking for valid Name
		if (null == name) {

			// nothing to do
		}
		// User entered a zero length string
		else if (0 == name.length()) {

			JOptionPane
					.showMessageDialog(this, LangModule.i18n
							.getString("TextpadPageDialog2"), LangModule.i18n
							.getString("TextpadPageDialog3"),
							JOptionPane.ERROR_MESSAGE);
		}
		// User entered string with bad chars
		else if (Utility.hasBadChars(name)) {

			JOptionPane
					.showMessageDialog(this, LangModule.i18n
							.getString("TextpadPageDialog4"), LangModule.i18n
							.getString("TextpadPageDialog3"),
							JOptionPane.ERROR_MESSAGE);
		} else {

			// Setting flag that text area is ready
			hasTextArea_ = true;

			// Send message to the server
			boolean wasCreated = TextpadModule.create(name, RCT_VERSION,
					UserModule.getId(), UserModule.getAlias(),
					RCT.ObjPermission.UNCLASSIFIED, className_, assemblyName_,
					(PAGE_TEAM == pageType_) ? TEXTPAD_TEAM_CREATE
							: TEXTPAD_GROUP_CREATE);

			// Test if the server allowd us to create a textpad
			if (wasCreated) {

				// Setting the textarea and button states
				textArea.setEditable(true);
				textArea.requestFocus();

				newButton.setEnabled(false);
				editButton.setEnabled(false);
				saveButton.setEnabled(true);
				closeButton.setEnabled(true);
				copyButton.setEnabled(true);
				pasteButton.setEnabled(true);
			} else {

				// Inform the user that the server
				// did not allow us to creat a new textpad
				JOptionPane.showMessageDialog(this, LangModule.i18n
						.getString("TextpadPageDialog5"), LangModule.i18n
						.getString("TextpadPageDialog3"),
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	// Callback:
	public void pressedCloseButton() {

		// First, get confirmation from user to close the
		// textpad
		int response = JOptionPane.showConfirmDialog(this, LangModule.i18n
				.getString("TextpadPageDialog7"));

		// Check which button was clicked on
		switch (response) {

		case JOptionPane.YES_OPTION:
			// We don't do anything and just proceed
			break;

		default:
			// For the other options we just return
			return;
		}

		// Call helper method for further processing
		helperClose();
	}

	// Callback:
	public void pressedSaveButton() {

		// Save textpad to local file
		try {

			FileModule.saveUnicodeUTF8(TEXTPAD_DEF_SAVE_NAME, doc_.getText(0,
					doc_.getLength()));
		} catch (Exception e) {
		}
	}

	// Callback:
	public void pressedCopyButton() {

		// Copy text area to copy buffer
		textArea.copy();
	}

	// Callback:
	public void pressedPasteButton() {

		// Paste copy buffer
		textArea.paste();
	}

	// Callback:
	public void pressedEditButton() {

		// Check first the textpad edit list has any entries
		int numTextpads = TextpadModule.editList.size();

		if (0 >= numTextpads) {

			JOptionPane
					.showMessageDialog(this, LangModule.i18n
							.getString("TextpadPageDialog8"), LangModule.i18n
							.getString("TextpadPageDialog3"),
							JOptionPane.ERROR_MESSAGE);
		} else {

			TextpadListDialog textpadListDialog = new TextpadListDialog(
					SessionModule.getFrame(), LangModule.i18n
							.getString("TextpadPageDialog9"));

			// Check which button was clicked on
			switch (textpadListDialog.getValue().getButtonValue()) {

			case JOptionPane.OK_OPTION:

				boolean canEdit_ = TextpadModule.edit(textpadListDialog
						.getValue().getId(), RCT_VERSION, UserModule.getId(),
						UserModule.getAlias(), RCT.ObjPermission.UNCLASSIFIED,
						className_, assemblyName_,
						(PAGE_TEAM == pageType_) ? TEXTPAD_TEAM_EDIT
								: TEXTPAD_GROUP_EDIT);

				if (canEdit_) {

					// Setting the textarea and button states
					textArea.setEditable(true);
					textArea.requestFocus();

					newButton.setEnabled(false);
					editButton.setEnabled(false);
					saveButton.setEnabled(true);
					closeButton.setEnabled(true);
					copyButton.setEnabled(true);
					pasteButton.setEnabled(true);
				} else {

					// Inform the user that the server
					// did not allow us to edit the textpad
					JOptionPane.showMessageDialog(this, LangModule.i18n
							.getString("TextpadPageDialog5"), LangModule.i18n
							.getString("TextpadPageDialog3"),
							JOptionPane.ERROR_MESSAGE);
				}

				break;

			default:

			// Nothing to do
			}
		}
	}

	// Abstract Methods Implementation:
	// --------------------------------

	// Abstarct Method:
	public void dispatchExitRequest() {

		// Nothing to do yet
	}

	// Abstract Method:
	public void actionActivate() {

		// Ring the system bell on active notification
		if (ConfigModule.can(CONF_TEXTPAD_RING_BELL)) {

			java.awt.Toolkit.getDefaultToolkit().beep();
		}

		// Setting the canEdit_ flag to true
		canEdit_ = true;

		if (!hasTextArea_) {

			newButton.setEnabled(true);
			editButton.setEnabled(true);
		} else {

			// Focus the text area
			textArea.requestFocus();
			textArea.setCaretColor(Color.black);
			textArea.setSelectedTextColor(Color.black);

			saveButton.setEnabled(true);
			closeButton.setEnabled(true);
			copyButton.setEnabled(true);
			pasteButton.setEnabled(true);

			textArea.setEditable(true);
		}
	}

	// Abstract Method:
	public void actionDeactivate() {

		// Setting the canEdit_ flag to false
		canEdit_ = false;

		textArea.setEditable(false);

		newButton.setEnabled(false);
		editButton.setEnabled(false);
		saveButton.setEnabled(false);
		closeButton.setEnabled(false);
		copyButton.setEnabled(false);
		pasteButton.setEnabled(false);
	}

	// Abstract Method:
	public void actionReleaseControl(boolean userIsLastInQueue) {

		// Check if user is last in queue
		// and if text area is active
		if (!userIsLastInQueue || !hasTextArea_) {

			return;
		}

		// Ask the user if he/she want's to close the
		// textpad in case the current user is the only
		// one left in the queue
		int response = JOptionPane.showConfirmDialog(this, LangModule.i18n
				.getString("TextpadPageDialog7"));

		// Check which button was clicked on
		switch (response) {

		case JOptionPane.YES_OPTION:
			// We don't do anything and just proceed
			break;

		default:
			// For the other options we just return
			return;
		}

		helperClose();
	}

	// Classes:
	// --------

	// TextpadWorkerThread class
	// This thread detects text area updates and sends those
	// updates to the server
	public class TextpadWorkerThread extends Thread {

		boolean stopThread = false;

		public void stopWorking() {

			stopThread = true;
		}

		public void run() {

			String str = "";
			boolean flag = true;

			while (!stopThread) {

				try {

					// Sleep for some time
					Thread.sleep(WORKER_THREAD_SLEEP);

					if (!canEdit_) {

						// Since someone else is updating the text area
						// we just take these changes and put it into
						// the newStr var. Then when it is our turn,
						// we continue below where we copy the newStr to oldStr.
						newStr_ = doc_.getText(0, doc_.getLength());

						continue;
					}

					// Copy new string to old string
					oldStr_ = newStr_;

					// Get text from document
					newStr_ = doc_.getText(0, doc_.getLength());

					// Reset offset_
					offset_ = 0;

					// Reset flag
					flag = true;

					// Case 1:
					// Old string is smaller then new string
					if (oldStr_.length() < newStr_.length()) {

						for (int i = 0; i < oldStr_.length(); i++) {

							if (newStr_.charAt(i) != oldStr_.charAt(i)) {

								offset_ = i;
								str = newStr_.substring(offset_);

								if (!stopThread) {

									TextpadModule
											.update(
													str,
													offset_,
													ref_,
													RCT_VERSION,
													UserModule.getId(),
													UserModule.getAlias(),
													RCT.ObjPermission.UNCLASSIFIED,
													className_,
													assemblyName_,
													(PAGE_TEAM == pageType_) ? TEXTPAD_TEAM_REM_INS
															: TEXTPAD_GROUP_REM_INS);
								}

								flag = false;
								break;
							}
						}

						// If there was now change within the old string length,
						// then we just add the remaining new substring
						if (flag) {

							offset_ = oldStr_.length();
							str = newStr_.substring(offset_);

							if (!stopThread) {

								TextpadModule
										.update(
												str,
												offset_,
												ref_,
												RCT_VERSION,
												UserModule.getId(),
												UserModule.getAlias(),
												RCT.ObjPermission.UNCLASSIFIED,
												className_,
												assemblyName_,
												(PAGE_TEAM == pageType_) ? TEXTPAD_TEAM_INS
														: TEXTPAD_GROUP_INS);
							}
						}
					}
					// Case 2:
					// New string is smaller then old string
					else if (oldStr_.length() > newStr_.length()) {

						for (int i = 0; i < newStr_.length(); i++) {

							if (newStr_.charAt(i) != oldStr_.charAt(i)) {

								offset_ = i;
								str = newStr_.substring(offset_);

								if (!stopThread) {

									TextpadModule
											.update(
													str,
													offset_,
													ref_,
													RCT_VERSION,
													UserModule.getId(),
													UserModule.getAlias(),
													RCT.ObjPermission.UNCLASSIFIED,
													className_,
													assemblyName_,
													(PAGE_TEAM == pageType_) ? TEXTPAD_TEAM_REM_INS
															: TEXTPAD_GROUP_REM_INS);
								}

								flag = false;
								break;
							}
						}

						// If there was no change withing the new string length,
						// then we just remove the remaining old substring
						if (flag) {

							offset_ = newStr_.length();

							if (!stopThread) {

								TextpadModule
										.update(
												str,
												offset_,
												ref_,
												RCT_VERSION,
												UserModule.getId(),
												UserModule.getAlias(),
												RCT.ObjPermission.UNCLASSIFIED,
												className_,
												assemblyName_,
												(PAGE_TEAM == pageType_) ? TEXTPAD_TEAM_REM
														: TEXTPAD_GROUP_REM);
							}
						}
					}
					// Case 3:
					// New string and old string have the same length
					else {

						for (int i = 0; i < newStr_.length(); i++) {

							if (newStr_.charAt(i) != oldStr_.charAt(i)) {

								offset_ = i;
								str = newStr_.substring(offset_);

								if (!stopThread) {

									TextpadModule
											.update(
													str,
													offset_,
													ref_,
													RCT_VERSION,
													UserModule.getId(),
													UserModule.getAlias(),
													RCT.ObjPermission.UNCLASSIFIED,
													className_,
													assemblyName_,
													(PAGE_TEAM == pageType_) ? TEXTPAD_TEAM_REM_INS
															: TEXTPAD_GROUP_REM_INS);
								}

								break;
							}
						}
					}
				} catch (Exception e) {
				}
			}
		}
	}
}

