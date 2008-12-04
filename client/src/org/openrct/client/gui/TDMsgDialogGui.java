// $Id: TDMsgDialogGui.java,v 1.5 2003/06/11 17:16:59 thomas Exp $

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
import org.openrct.client.module.ErrorModule;
import org.openrct.client.module.LangModule;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TDMsgDialogGui extends JDialog implements Const {

	private JPanel panel = new JPanel();

	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private JPanel buttonPanel = new JPanel();

	private JTextArea textArea = new JTextArea();

	private JScrollPane textScrollPane = new JScrollPane();

	private JButton sendButton;

	private JButton cancelButton;

	private JLabel subjectLabel = new JLabel("Subject:");

	protected JTextField subjectField = new JTextField();

	// Result values
	private int buttonValue_ = JOptionPane.CLOSED_OPTION;

	private String subject_;

	private String text_;

	private boolean isNew_;

	private Frame frame_;

	// Constructor
	// New Msg
	public TDMsgDialogGui(Frame frame, String title) {

		super(frame, title, true);

		frame_ = frame;

		subject_ = "";

		isNew_ = true;

		init();
	}

	// Construcor
	// Reply Msg
	public TDMsgDialogGui(Frame frame, String title, String subject) {

		super(frame, title, true);

		frame_ = frame;

		subject_ = subject;

		isNew_ = false;

		init();
	}

	// Constructor
	public void init() {

		subjectField.setText(subject_);
		subjectField.setEditable(isNew_ ? true : false);
		subjectField.setFont(UNICODE_FONT_12);

		//setResizable(false);

		this.getContentPane().setLayout(gridBagLayout1);

		// JScrollPane
		textScrollPane.setAutoscrolls(true);

		// JTextArea
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setAutoscrolls(true);
		textArea.setFont(UNICODE_FONT_12);

		sendButton = new JButton("Send");
		sendButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent evt) {

				buttonValue_ = JOptionPane.OK_OPTION;
				subject_ = subjectField.getText();
				text_ = textArea.getText();

				if ((null == subject_) || (subject_.equals(""))) {

					ErrorModule.display(frame_, LangModule.i18n
							.getString("TDErrorMsg1"), LangModule.i18n
							.getString("TDErrorMsg2"));
				} else if ((null == text_) || (text_.equals(""))) {

					ErrorModule.display(frame_, LangModule.i18n
							.getString("TDErrorMsg1"), LangModule.i18n
							.getString("TDErrorMsg3"));
				} else {

					TDMsgDialogGui.this.dispose();
				}
			}
		});

		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent evt) {

				buttonValue_ = JOptionPane.CANCEL_OPTION;

				TDMsgDialogGui.this.dispose();
			}
		});

		buttonPanel.add(sendButton);
		buttonPanel.add(cancelButton);

		this.getContentPane().add(
				subjectLabel,
				new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.NONE,
						new Insets(10, 10, 10, 10), 0, 0));

		this.getContentPane().add(
				subjectField,
				new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10,
								10), 0, 0));

		this.getContentPane().add(
				textScrollPane,
				new GridBagConstraints(0, 1, 2, 1, 1.0, 1.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(5, 5, 5, 5), 0, 0));

		textScrollPane.getViewport().add(textArea);

		this.getContentPane().add(
				buttonPanel,
				new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5),
						0, 0));

		// Arrange to catch window closing events
		// and also set initial focus
		this.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent evt) {

				buttonValue_ = JOptionPane.CLOSED_OPTION;

				TDMsgDialogGui.this.dispose();
			}

			public void windowOpened(WindowEvent evt) {

				sendButton.requestFocus();
			}
		});

		// Now, pack the widgets so that we get valid bounds
		this.pack();
		this.setSize(500, 600);

		// Center Dialog over frame
		Rectangle rect1 = frame_.getBounds();
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

	public TDMsgDialogResult getValue() {

		TDMsgDialogResult result = new TDMsgDialogResult(buttonValue_,
				subject_, text_);

		return result;
	}

	// Classes:
	// --------

	// Class
	public class TDMsgDialogResult {

		private int buttonValue_;

		private String subject_;

		private String text_;

		public TDMsgDialogResult(int buttonValue, String subject, String text) {

			buttonValue_ = buttonValue;
			subject_ = subject;
			text_ = text;
		}

		public int getButtonValue() {

			return buttonValue_;
		}

		public String getSubject() {

			return subject_;
		}

		public String getText() {

			return text_;
		}
	}
}