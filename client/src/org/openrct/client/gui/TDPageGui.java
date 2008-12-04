// $Id: TDPageGui.java,v 1.16 2003/07/01 17:48:13 thomas Exp $

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

import org.openrct.client.module.SessionModule;
import org.openrct.client.module.LangModule;
import org.openrct.client.module.TDModule;
import org.openrct.client.module.UserModule;
import org.openrct.client.module.CacheModule;
import org.openrct.client.module.ErrorModule;
import org.openrct.client.util.Utility;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import org.omg.CORBA.*;

public class TDPageGui extends UncontrolledModulePageGui {

	private JFrame frame_ = null;

	private GridBagLayout mainGridBagLayout = new GridBagLayout();

	private GridBagLayout tdGridBagLayout = new GridBagLayout();

	private GridBagLayout msgGridBagLayout = new GridBagLayout();

	private GridBagLayout readGridBagLayout = new GridBagLayout();

	private TDModel model;

	private JTreeTable treeTable;

	private JPanel tdPanel = new JPanel();

	private JPanel msgPanel = new JPanel();

	private JPanel readPanel = new JPanel();

	private JPanel buttonPanel = new JPanel();

	private JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
			tdPanel, msgPanel);

	private JLabel senderLabel = new JLabel();

	private JLabel subjectLabel = new JLabel();

	private JTextArea textArea = new JTextArea();

	private JScrollPane textScrollPane = new JScrollPane();

	private JButton readButton = new JButton();

	private JButton expandButton = new JButton();

	private JButton collapsButton = new JButton();

	private JButton replyButton = new JButton();

	private JButton newButton = new JButton();

	private JButton loadButton = new JButton();

	// This flag is used to only process new TD messages
	// once the load method has finished.
	private boolean canReceiveMsg_ = false;

	// Constructor
	//
	public TDPageGui(String className, String assemblyName, int pageType) {

		super(className, assemblyName, MODULE_TD, pageType);

		frame_ = SessionModule.getFrame();

		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(120);

		this.setLayout(mainGridBagLayout);
		tdPanel.setLayout(tdGridBagLayout);
		msgPanel.setLayout(msgGridBagLayout);
		readPanel.setLayout(readGridBagLayout);

		// Create model
		model = new TDModel();

		// Create JTreeTable
		treeTable = new JTreeTable(model);

		// Config read objects
		senderLabel.setFont(UNICODE_FONT_12);
		senderLabel.setText(LangModule.i18n.getString("TDSampleFrom"));

		subjectLabel.setFont(UNICODE_FONT_12);
		subjectLabel.setText(LangModule.i18n.getString("TDSampleSubject"));

		textScrollPane.setAutoscrolls(true);

		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setAutoscrolls(true);
		textArea.setFont(UNICODE_FONT_12);
		textArea.setText(LangModule.i18n.getString("TDSampleText"));
		textArea.setEditable(false);

		// Config buttons
		readButton.setText(LangModule.i18n.getString("ButtonLabelRead"));
		readButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				pressedReadButton();
			}
		});

		expandButton.setText(LangModule.i18n.getString("ButtonLabelExpandAll"));
		expandButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				pressedExpandButton();
			}
		});

		collapsButton.setText(LangModule.i18n
				.getString("ButtonLabelCollapsAll"));
		collapsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				pressedCollapsButton();
			}
		});

		replyButton.setText(LangModule.i18n.getString("ButtonLabelReply"));
		replyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				pressedReplyButton();
			}
		});

		newButton.setText(LangModule.i18n.getString("ButtonLabelNew"));
		newButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				pressedNewButton();
			}
		});

		loadButton.setText(LangModule.i18n.getString("ButtonLabelLoad"));
		loadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				pressedLoadButton();
			}
		});

		// Adding buttons
		buttonPanel.add(loadButton);
		buttonPanel.add(newButton);
		buttonPanel.add(replyButton);
		buttonPanel.add(readButton);
		buttonPanel.add(expandButton);
		buttonPanel.add(collapsButton);

		// Add TD Components
		tdPanel.add(new JScrollPane(treeTable), new GridBagConstraints(0, 0, 1,
				1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));

		// Add Read Components
		readPanel.add(senderLabel, new GridBagConstraints(0, 0, 1, 1, 0.5, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0));

		readPanel.add(subjectLabel, new GridBagConstraints(1, 0, 1, 1, 0.5,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0));

		readPanel.add(textScrollPane, new GridBagConstraints(0, 1, 2, 1, 1.0,
				1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(5, 5, 5, 5), 0, 0));

		textScrollPane.getViewport().add(textArea);

		// Adding MSG components
		msgPanel.add(readPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						5, 5, 5, 5), 0, 0));

		msgPanel.add(buttonPanel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						5, 5, 5, 5), 0, 0));

		// Adding the Split Pane
		this.add(splitPane, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						5, 5, 5, 5), 0, 0));

		// Only show the load button
		readButton.setEnabled(false);
		expandButton.setEnabled(false);
		collapsButton.setEnabled(false);
		replyButton.setEnabled(false);
		newButton.setEnabled(false);

	}

	// Methods:
	// --------

	// Method:
	//
	public boolean canReceiveMsg() {

		return canReceiveMsg_;
	}

	// Method:
	//
	public synchronized void displayPostMsg(String userAlias, int postId,
			int parentId, String subject, String type, String date) {

		if (0 == parentId) {

			// Insert new message thus setting isNew = true
			insertMessage(parentId, postId, subject, userAlias, type, date,
					true);
		} else {

			// Insert reply message thus setting isNew = false
			insertMessage(parentId, postId, subject, userAlias, type, date,
					false);
		}
	}

	// Method:
	//
	public void displayMessage(String sender, String subject, String text) {

		// Set sender
		senderLabel.setText(LangModule.i18n.getString("TDFrom") + " " + sender);

		// Set subject
		subjectLabel.setText(LangModule.i18n.getString("TDSubject") + " "
				+ subject);

		// Set text
		textArea.append(text);
	}

	public void displayParentMessage(String sender, String subject, String text) {
		// Set sender
		textArea
				.append(LangModule.i18n.getString("TDFrom") + " " + sender + NL);

		// Set subject
		textArea.append(LangModule.i18n.getString("TDSubject") + " " + subject
				+ NL);

		// Set text
		textArea.append(text);
		textArea.append(NL + NL + "====================================" + NL
				+ NL);
	}

	// Method:
	//
	public void clearReadArea() {

		textArea.setText("");
	}

	// Method:
	//
	public void insertMessage(final int parentId, final int postId,
			final String subject, final String sender, final String type,
			final String date, final boolean isNew) {

		try {

			// Use invokeLater to prevent JTreeTable swing thread problems
			SwingUtilities.invokeLater(new Runnable() {

				public void run() {

					TDNode tdNode = new TDNode(false, null, parentId, postId,
							subject, sender, type, date);

					treeTable.insert(tdNode, (isNew ? true : false));
				}
			});
		} catch (Exception e) {

			System.out.println("EXCEPTION: TDPageGui.insertMessage()");
		}
	}

	// Method:
	//
	public void readParentMsg(String parentInfo[]) {

		// Getting the text from cache or server
		RCT.BinaryFileHolder textData = new RCT.BinaryFileHolder();

		// Check cache for text
		if (CacheModule.isHit(parentInfo[0], MOD_TD)) {

			// HIT
			CacheModule.get(parentInfo[0], MOD_TD, textData);
		} else {

			// MISS
			StringHolder text = new StringHolder();
			TDModule.getText(UserModule.getId(), parentInfo[0], false, text);

			try {

				textData.value = text.value.getBytes(FILE_UTF_8_ENCODING);
			} catch (Exception e) {
			}

			CacheModule.put(parentInfo[0], MOD_TD, textData, MODULE_TD_
					+ parentInfo[0]);
		}

		try {

			// NIY
			// Display the text
			displayParentMessage(parentInfo[1], parentInfo[2], new String(
					textData.value, FILE_UTF_8_ENCODING));
		} catch (Exception e) {
		}
	}

	// Callbacks:
	// ----------

	// Callback
	//
	public void pressedLoadButton() {

		RCT.TDSeqHolder tdSeq = new RCT.TDSeqHolder();

		TDModule.load(UserModule.getId(), className_, assemblyName_, tdSeq);

		int tdSeqLength = tdSeq.value.length;
		TDNode tdNode = null;

		for (int i = 0; i < tdSeqLength; i++) {

			tdNode = new TDNode(tdSeq.value[i].is_read, null,
					tdSeq.value[i].parent_id, tdSeq.value[i].post_id,
					tdSeq.value[i].subject, tdSeq.value[i].sender,
					tdSeq.value[i].type, Utility
							.getDateAndTime(tdSeq.value[i].date));

			if (0 == tdNode.getParentId()) {

				treeTable.insert(tdNode, true);
			} else {

				treeTable.insert(tdNode, false);
			}
		}

		// Now that we loaded all the messages, we
		// enable all the other buttons and disable
		// the load button
		readButton.setEnabled(true);
		expandButton.setEnabled(true);
		collapsButton.setEnabled(true);
		replyButton.setEnabled(true);
		newButton.setEnabled(true);
		loadButton.setEnabled(false);

		// Now we set the receive message flag
		canReceiveMsg_ = true;
	}

	// Callback
	//
	public void pressedExpandButton() {

		treeTable.expandAllRows();
	}

	// Callback
	//
	public void pressedCollapsButton() {

		treeTable.collapsAllRows();
	}

	// Callback
	//
	public void pressedReadButton() {

		// Get the node
		TDNode tdNode = (TDNode) treeTable.getSelectedObject();

		if (null == tdNode) {

			ErrorModule.display(frame_, LangModule.i18n
					.getString("TDErrorMsg1"), LangModule.i18n
					.getString("TDErrorMsg4"));
			return;
		}

		// Reset reading area
		clearReadArea();

		// Getting the text from cache or server
		RCT.BinaryFileHolder textData = new RCT.BinaryFileHolder();

		String postID = String.valueOf(tdNode.getPostId());

		// First getting text of all related messages in thread
		String threadIDs[] = tdNode.getThreadIDs("").split(":");

		for (int i = 0; i < threadIDs.length; i++) {

			String nodeInfo[] = threadIDs[i].split("#");

			if (!nodeInfo[0].equals(postID)) {

				readParentMsg(nodeInfo);
			}
		}

		// Check cache for text
		if (CacheModule.isHit(postID, MOD_TD)) {

			// HIT
			CacheModule.get(postID, MOD_TD, textData);
		} else {

			// MISS
			StringHolder text = new StringHolder();
			TDModule.getText(UserModule.getId(), postID, tdNode.isRead(), text);

			try {

				textData.value = text.value.getBytes(FILE_UTF_8_ENCODING);
			} catch (Exception e) {
			}

			CacheModule.put(postID, MOD_TD, textData, MODULE_TD_ + postID);
		}

		try {

			// Display the text
			displayMessage((String) tdNode.getValueAt(SENDER), (String) tdNode
					.getValueAt(SUBJECT), new String(textData.value,
					FILE_UTF_8_ENCODING));
		} catch (Exception e) {
		}

		// Mark the node as read
		tdNode.read();
		treeTable.updateUI();
	}

	// Callback
	//
	public void pressedReplyButton() {

		// Get selected message we want to reply to
		TDNode tdNode = (TDNode) treeTable.getSelectedObject();

		// Test if user selected a message
		if (null == tdNode) {

			ErrorModule.display(frame_, LangModule.i18n
					.getString("TDErrorMsg1"), LangModule.i18n
					.getString("TDErrorMsg4"));
			return;
		}

		TDMsgDialogGui tdMsgDialog = new TDMsgDialogGui(frame_, LangModule.i18n
				.getString("TDReply"), (String) tdNode.getValueAt(SUBJECT));

		// Check which button was clicked on
		switch (tdMsgDialog.getValue().getButtonValue()) {

		case JOptionPane.OK_OPTION:

			TDModule.postReplyMsg(UserModule.getId(), UserModule.getAlias(),
					className_, assemblyName_, "NORMAL", RCT_VERSION, tdNode
							.getPostId(), tdMsgDialog.getValue().getSubject(),
					tdMsgDialog.getValue().getText(),
					RCT.ObjPermission.UNCLASSIFIED);

			break;

		default:

		// Nothing to do
		}
	}

	// Callback
	//
	public void pressedNewButton() {

		TDMsgDialogGui tdMsgDialog = new TDMsgDialogGui(frame_, LangModule.i18n
				.getString("TDNew"));

		// Check which button was clicked on
		switch (tdMsgDialog.getValue().getButtonValue()) {

		case JOptionPane.OK_OPTION:

			// Put the text of the new message in local cache

			// Notify server
			TDModule.postNewMsg(UserModule.getId(), UserModule.getAlias(),
					className_, assemblyName_, "NORMAL", RCT_VERSION,
					tdMsgDialog.getValue().getSubject(), tdMsgDialog.getValue()
							.getText(), RCT.ObjPermission.UNCLASSIFIED);
			break;

		default:

		// Nothing to do
		}
	}

	// Abstract Methods Implementation:
	// --------------------------------

	// Abstract Method:
	public void dispatchExitRequest() {

		// Nothing to do
	}
}