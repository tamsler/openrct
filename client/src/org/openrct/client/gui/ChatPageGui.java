// $Id: ChatPageGui.java,v 1.4 2003/05/08 20:09:03 thomas Exp $

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

import org.openrct.client.util.ActionTimerRingBell;
import org.openrct.client.module.LangModule;
import org.openrct.client.module.ConfigModule;
import org.openrct.client.module.UserModule;
import org.openrct.client.module.ChatModule;
import org.openrct.client.util.RctStyle;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class ChatPageGui extends UncontrolledModulePageGui {

	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private TitledBorder inputTitledBorder;

	private TitledBorder outputTitledBorder;

	private JScrollPane inputScrollPane = new JScrollPane();

	private JScrollPane outputScrollPane = new JScrollPane();

	private JTextArea inputTextArea = new JTextArea();

	protected RctJTextPane outputTextArea = new RctJTextPane();

	private JLabel infoLabel = new JLabel();

	private ActionTimerRingBell actionTimerRingBell_;

	// Construct the frame
	public ChatPageGui(String className, String assemblyName, int pageType) {

		super(className, assemblyName, MODULE_CHAT, pageType);

		inputTitledBorder = new TitledBorder("");
		outputTitledBorder = new TitledBorder("");

		this.setLayout(gridBagLayout1);

		inputScrollPane.setAutoscrolls(true);
		inputScrollPane.setBorder(inputTitledBorder);

		inputTitledBorder.setTitle(LangModule.i18n
				.getString("ChatPageGuiBorderInput"));
		inputTitledBorder.setTitleJustification(2);

		outputTitledBorder.setTitle(LangModule.i18n
				.getString("ChatPageGuiBorderOutput"));
		outputTitledBorder.setTitleJustification(2);

		infoLabel.setBorder(BorderFactory.createLoweredBevelBorder());
		infoLabel.setText(LangModule.i18n.getString("ChatPageGuiLabel2"));

		inputTextArea.setWrapStyleWord(true);
		inputTextArea.setLineWrap(true);
		inputTextArea.setAutoscrolls(true);
		inputTextArea.setFont(new Font(ConfigModule
				.getString(CONF_UNICODE_FONT), Font.PLAIN, ConfigModule
				.getNumber(CONF_UNICODE_FONT_SIZE)));

		// Consume the return key for all three event types
		inputTextArea.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent evt) {

				// If the ENTER key was pressed, we get
				// the JTextArea context and send it. Then
				// we clear the JTextArea
				if (evt.getKeyCode() == 10) {

					evt.consume();

					sendChatMessage();
				}
			}

			public void keyTyped(KeyEvent evt) {
			}

			public void keyPressed(KeyEvent evt) {

				if (evt.getKeyCode() == 10) {

					evt.consume();
				}
			}
		});

		outputTextArea.setEditable(false);
		outputTextArea.setAutoscrolls(true);
		outputTextArea.setFont(new Font(ConfigModule
				.getString(CONF_UNICODE_FONT), Font.PLAIN, ConfigModule
				.getNumber(CONF_UNICODE_FONT_SIZE)));

		outputScrollPane.setAutoscrolls(true);
		outputScrollPane.setBorder(outputTitledBorder);

		this.add(inputScrollPane, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 40));

		this.add(outputScrollPane, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 5, 5, 5), 0, 60));
		this.add(infoLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						5, 5, 5, 5), 0, 0));

		outputScrollPane.getViewport().add(outputTextArea);
		inputScrollPane.getViewport().add(inputTextArea);

		// Create the ring bell action timer
		actionTimerRingBell_ = new ActionTimerRingBell(ConfigModule
				.getNumber(CONF_ACTION_TIMER_SLEEP));
		actionTimerRingBell_.startWorking();
	}

	// Method: Display chat input
	public synchronized void displayChatMsg(String msg, String style) {

		// Append message
		outputTextArea.append(msg + NL, style);

		// Make sure that appended message is visible
		outputTextArea.setCaretPosition(outputTextArea.getDocument()
				.getLength());

		// Setting the chat message info
		changeChatMsgInfo();

		// If the client configuration for the
		// chat bell ring event is set to true
		// we ring the system bell per chat message
		if (ConfigModule.can(CONF_CHAT_RING_BELL)) {

			actionTimerRingBell_.performAction();
		}
	}

	// Method: Set the cursor focus in the input text area
	public void focus() {

		inputTextArea.requestFocus();
	}

	// Method: Change Font for input/output test area
	public void dispatchFontChange() {

		inputTextArea.setFont(new Font(ConfigModule
				.getString(CONF_UNICODE_FONT), Font.PLAIN, ConfigModule
				.getNumber(CONF_UNICODE_FONT_SIZE)));

		outputTextArea.setFont(new Font(ConfigModule
				.getString(CONF_UNICODE_FONT), Font.PLAIN, ConfigModule
				.getNumber(CONF_UNICODE_FONT_SIZE)));
	}

	// Method: Changes the last chat message arrived time info
	private void changeChatMsgInfo() {

		Date now = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int month = cal.get(Calendar.MONTH) + 1;
		int year = cal.get(Calendar.YEAR);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		int second = cal.get(Calendar.SECOND);

		String date = year + "-" + month + "-" + day + " " + hour + ":"
				+ minute + ":" + second;

		infoLabel.setText(LangModule.i18n.getString("ChatPageGuiLabel1") + " "
				+ date);
	}

	// Callbacks:
	// ----------

	// Callback: Sending the chat message
	private void sendChatMessage() {

		// Get the chat text
		String chatText = inputTextArea.getText();

		// Check if the user didn't type anything
		if (chatText.equals("")) {

			return;
		}

		// Clear the input text area
		inputTextArea.setText(null);

		// Since we don't get our own chat message from the
		// server we show it here.
		displayChatMsg("[" + UserModule.getAlias() + "]> " + chatText,
				RctStyle.BOLD);

		// Setting the chat message info
		changeChatMsgInfo();

		// Sending chat text to the server
		if (PAGE_TEAM == pageType_) {

			ChatModule.send(chatText, RCT_VERSION, UserModule.getId(),
					UserModule.getAlias(), RCT.ObjPermission.UNCLASSIFIED,
					className_, assemblyName_, CHAT_TEAM_MSG);
		} else if (PAGE_GROUP == pageType_) {

			ChatModule.send(chatText, RCT_VERSION, UserModule.getId(),
					UserModule.getAlias(), RCT.ObjPermission.UNCLASSIFIED,
					className_, assemblyName_, CHAT_GROUP_MSG);
		} else {

			System.err.println("ERROR: Did not recognize the object type!");
		}
	}

	// Abstract Methods Implementation:
	// --------------------------------

	// Abstract Method:
	public void dispatchExitRequest() {

		actionTimerRingBell_.stopWorking();
	}
}

