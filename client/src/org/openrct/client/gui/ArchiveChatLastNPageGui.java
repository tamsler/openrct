// $Id: ArchiveChatLastNPageGui.java,v 1.4 2003/05/08 20:09:03 thomas Exp $

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
import org.openrct.client.module.ConfigModule;
import org.openrct.client.module.TeamModule;
import org.openrct.client.module.UserModule;
import org.openrct.client.util.RctStyle;
import org.openrct.client.util.Utility;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ArchiveChatLastNPageGui extends JPanel implements Const {

	private RctJTextPane textArea = new RctJTextPane();

	private JScrollPane jScrollPane = new JScrollPane();

	private GridBagLayout gridBagLayout = new GridBagLayout();

	private JButton closeButton = new JButton();

	private String className_;

	private String teamName_;

	private ArchivePageGui archivePage_;

	private int nTicks_;

	// Constructor
	public ArchiveChatLastNPageGui(String className, String teamName,
			ArchivePageGui archivePage, int nTicks) {

		className_ = className;
		teamName_ = teamName;
		archivePage_ = archivePage;
		nTicks_ = nTicks;

		this.setLayout(gridBagLayout);

		jScrollPane.setAutoscrolls(true);

		closeButton.setText(LangModule.i18n.getString("ButtonLabelClose"));
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				pressedCloseButton();
			}
		});

		textArea.setEditable(false);
		textArea.setAutoscrolls(true);
		textArea.setFont(new Font(ConfigModule.getString(CONF_UNICODE_FONT),
				Font.PLAIN, ConfigModule.getNumber(CONF_UNICODE_FONT_SIZE)));

		jScrollPane.getViewport().add(textArea);

		this.add(jScrollPane, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(
						5, 5, 5, 5), 0, 260));

		this.add(closeButton, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0));

		// Test if we selected any messages
		if (0 == nTicks_) {

			textArea.setText(LangModule.i18n.getString("ChatPageTeamInfo2"));
		} else {

			// ChatMsgHistSeq
			RCT.ChatMsgHistSeqHolder chatMsgHistSeq = new RCT.ChatMsgHistSeqHolder();

			try {

				TeamModule.getChatArchiveLastN(className_, teamName_, nTicks_,
						chatMsgHistSeq);
			} catch (RCT.TeamServerPackage.DataSelectionExceedsLimit dsel) {

				JOptionPane.showMessageDialog(this, LangModule.i18n
						.getString("ExceptionDialog3"), LangModule.i18n
						.getString("ExceptionDialog1"),
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			// Test if there are any chat messages in this team
			if (0 == chatMsgHistSeq.value.length) {

				textArea
						.setText(LangModule.i18n.getString("ChatPageTeamInfo3"));
			} else {

				String style = RctStyle.REGULAR;

				for (int i = 0; i < chatMsgHistSeq.value.length; i++) {

					if (chatMsgHistSeq.value[i].user_alias.equals(UserModule
							.getAlias())) {

						style = RctStyle.BOLD;
					} else {

						style = RctStyle.REGULAR;
					}

					textArea
							.append(
									"< "
											+ chatMsgHistSeq.value[i].user_alias
											+ " : "
											+ Utility
													.getDateAndTime(chatMsgHistSeq.value[i].rct_date)
											+ " >" + NL
											+ chatMsgHistSeq.value[i].chat_msg
											+ NL + NL, style);
				}
			}
		}
	}

	// Methods:
	// --------

	// Method:
	public void dispatchFontChange() {

		textArea.setFont(new Font(ConfigModule.getString(CONF_UNICODE_FONT),
				Font.PLAIN, ConfigModule.getNumber(CONF_UNICODE_FONT_SIZE)));
	}

	// Callbacks:
	// ----------

	// Callback
	public void pressedCloseButton() {

		archivePage_.closePage();
	}
}

