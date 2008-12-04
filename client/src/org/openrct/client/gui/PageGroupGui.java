// $Id: PageGroupGui.java,v 1.4 2003/05/08 20:09:03 thomas Exp $

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
import org.openrct.client.module.UserModule;
import org.openrct.client.module.GroupModule;
import org.openrct.client.module.LangModule;
import org.openrct.client.util.RctStyle;
import javax.swing.*;

public class PageGroupGui extends PageGui implements Const {

	private String className_;

	private String groupName_;

	private int numChatMsg_ = MAX_N_MSG;

	private int numSoundMsg_ = MAX_N_MSG;

	private int numUrlMsg_ = MAX_N_MSG;

	private int numFtpMsg_ = MAX_N_MSG;

	// Constructor
	public PageGroupGui(String className, String groupName) throws Exception {

		super(className, groupName, PAGE_GROUP);

		className_ = className;
		groupName_ = groupName;

		// Building the userListModel
		DefaultListModel userListModel = new DefaultListModel();
		RCT.UserSeqHolder userSeq = new RCT.UserSeqHolder();

		UserModule.getUsersFromGroupName(className_, groupName_, userSeq);

		// Find out who the manager is in this group
		String userId = GroupModule.getManagerId(className_, groupName_);

		for (int i = 0; i < userSeq.value.length; i++) {

			// If we have the group manager, add him/her at
			// the top of the user list
			if (userSeq.value[i].user_id.equals(userId)) {

				userListModel.insertElementAt(userSeq.value[i].alias, 0);
			} else {

				userListModel.insertElementAt(userSeq.value[i].alias,
						userListModel.getSize());
			}
		}

		userList.setModel(userListModel);

		// Start retrieving existing module context
		// ----------------------------------------

		// Set this falg if an exception occurs
		// and then check flag before we access data
		boolean hasException = false;

		// CHAT Start
		// Getting the existing chat messages
		// ChatMsgHistSeq
		RCT.ChatMsgHistSeqHolder chatMsgHistSeq = new RCT.ChatMsgHistSeqHolder();

		try {

			GroupModule.getChatArchiveLastN(className_, groupName_,
					numChatMsg_, chatMsgHistSeq);
		} catch (RCT.GroupServerPackage.DataSelectionExceedsLimit dsel) {

			JOptionPane.showMessageDialog(this, LangModule.i18n
					.getString("ExceptionDialog4"), LangModule.i18n
					.getString("ExceptionDialog2"),
					JOptionPane.INFORMATION_MESSAGE);

			hasException = true;
		}

		if (!hasException) {

			// Test if there are any chat messages in this group
			if (0 < chatMsgHistSeq.value.length) {

				String style = RctStyle.REGULAR;

				for (int i = 0; i < chatMsgHistSeq.value.length; i++) {

					if (chatMsgHistSeq.value[i].user_alias.equals(UserModule
							.getAlias())) {

						style = RctStyle.BOLD;
					} else {

						style = RctStyle.REGULAR;
					}

					chatModulePage.displayChatMsg("[ "
							+ chatMsgHistSeq.value[i].user_alias + " ] > "
							+ chatMsgHistSeq.value[i].chat_msg, style);
				}
			}
		}

		// Reset exception flag
		hasException = false;
		// CHAT Stop

		// SOUND Start
		RCT.SoundMsgHistSeqHolder soundMsgHistSeq = new RCT.SoundMsgHistSeqHolder();

		try {

			GroupModule.getSoundArchiveLastN(className_, groupName_,
					numSoundMsg_, soundMsgHistSeq);
		} catch (RCT.GroupServerPackage.DataSelectionExceedsLimit dsel) {

			JOptionPane.showMessageDialog(this, LangModule.i18n
					.getString("ExceptionDialog4"), LangModule.i18n
					.getString("ExceptionDialog2"),
					JOptionPane.INFORMATION_MESSAGE);

			hasException = true;
		}

		if (!hasException) {
			// Test if there are any sound messages in this group
			if (0 < soundMsgHistSeq.value.length) {

				soundModulePage.displaySoundMsg(soundMsgHistSeq);

			}
		}

		// Reset exception flag
		hasException = false;
		// SOUND Stop

		// FTP Start
		RCT.FtpMsgHistSeqHolder ftpMsgHistSeq = new RCT.FtpMsgHistSeqHolder();

		try {

			GroupModule.getFtpArchiveLastN(className_, groupName_, numFtpMsg_,
					ftpMsgHistSeq);
		} catch (RCT.GroupServerPackage.DataSelectionExceedsLimit dsel) {

			JOptionPane.showMessageDialog(this, LangModule.i18n
					.getString("ExceptionDialog4"), LangModule.i18n
					.getString("ExceptionDialog2"),
					JOptionPane.INFORMATION_MESSAGE);

			hasException = true;
		}

		if (!hasException) {
			// Test if there are any ftp messages in this group
			if (0 < ftpMsgHistSeq.value.length) {

				ftpModulePage.displayFtpUploadMsg(ftpMsgHistSeq);
			}
		}
		// FTP Stop
	}

	// Abstract Methods:
	// -----------------

	// Abstract Method: Get the class name
	public String getClassName() {

		return className_;
	}

	// Abstract Method: Get assebmly name
	// Assembly name is either the team or group name
	public String getAssemblyName() {

		return groupName_;
	}

	// Abstract Method: Exit
	public void exitPage() {

		GroupModule.exitGroup(getClassName(), getAssemblyName(), UserModule
				.getId(), UserModule.getAlias());

	}

	// Abstract Method: Logout
	public void logout() {

		GroupModule.exitGroup(getClassName(), getAssemblyName(), UserModule
				.getId(), UserModule.getAlias());
	}

	// Abstract Method: canLogout
	public boolean canLogout() {

		DefaultListModel userListModel = (DefaultListModel) userList.getModel();
		int nUsers = userListModel.size();

		if (isManaged_ && (1 < nUsers)) {

			return false;
		} else {

			return true;
		}
	}

	// Methods:
	// --------

	// Method:
	public void setIsManaged(boolean status) {

		isManaged_ = status;
	}

	// Callbacks:
	// ----------
}

