// $Id: GroupGui.java,v 1.4 2003/05/08 20:09:03 thomas Exp $

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
import org.openrct.client.module.UserModule;
import org.openrct.client.module.GroupModule;
import javax.swing.*;

public class GroupGui implements Const {

	public GroupGui() {

		// Nothing to do here
	}

	public synchronized void displayJoinRequestDialog(String className,
			String groupName, String userAlias) {

		int response = JOptionPane.showConfirmDialog(null, LangModule.i18n
				.getString("GroupGuiStr1_1")
				+ SP
				+ userAlias
				+ SP
				+ LangModule.i18n.getString("GroupGuiStr1_2")
				+ SP
				+ groupName
				+ SP
				+ LangModule.i18n.getString("GroupGuiStr1_3")
				+ SP
				+ className + QM);

		switch (response) {

		case JOptionPane.YES_OPTION:
			GroupModule.joinGroupRequestReply(className, groupName, UserModule
					.getId(), UserModule.getAlias(), userAlias, GRANTED);
			break;

		case JOptionPane.NO_OPTION:
		case JOptionPane.CANCEL_OPTION:
		case JOptionPane.CLOSED_OPTION:
			GroupModule.joinGroupRequestReply(className, groupName, UserModule
					.getId(), UserModule.getAlias(), userAlias, DENIED);
			break;

		default:
		// Don't do anything
		}
	}

	public synchronized void displayChooseNewManager(String className,
			String groupName) {

		// Get all the users that are in the group
		RCT.UserSeqHolder userSeq = new RCT.UserSeqHolder();
		UserModule.getUsersFromGroupName(className, groupName, userSeq);

		// After getting the group users we have to check if ther
		// is the situation where no more users are in the group
		// besides the manager
		if (1 == userSeq.value.length) {

			GroupModule.exitGroup(className, groupName, UserModule.getId(),
					UserModule.getAlias());
			return;
		}

		// Subtract one because we will not include the current manager name
		// in the list
		String[] groupMembers = new String[userSeq.value.length - 1];

		for (int i = 0, j = 0; i < userSeq.value.length; i++) {

			// Do not add existing manager
			if (!userSeq.value[i].alias.equals(UserModule.getAlias())) {

				groupMembers[j] = userSeq.value[i].alias;
				j++;
			}
		}

		String newManager = (String) JOptionPane.showInputDialog(null,
				LangModule.i18n.getString("GroupGuiStr2_1"), LangModule.i18n
						.getString("GroupGuiStr2_2"),
				JOptionPane.QUESTION_MESSAGE, null, groupMembers,
				groupMembers[0]);

		// Testing what button was pressed
		// Cancel
		if (null == newManager) {

			JOptionPane.showMessageDialog(null, LangModule.i18n
					.getString("GroupGuiStr2_3"), LangModule.i18n
					.getString("GroupGuiStr2_2"),
					JOptionPane.INFORMATION_MESSAGE);

			displayChooseNewManager(className, groupName);
		}
		// Nothing selected
		else if (0 == newManager.length()) {

			JOptionPane.showMessageDialog(null, LangModule.i18n
					.getString("GroupGuiStr2_3"), LangModule.i18n
					.getString("GroupGuiStr2_2"),
					JOptionPane.INFORMATION_MESSAGE);

			displayChooseNewManager(className, groupName);
		}
		// Item was selected
		else {

			// First we have to check if the newly selected
			// manager is still in the group
			if (GroupModule.isUserInGroup(className, groupName, newManager)) {

				GroupModule.setNewManager(className, groupName, UserModule
						.getId(), UserModule.getAlias(), newManager);
			} else {

				JOptionPane.showMessageDialog(null, LangModule.i18n
						.getString("GroupGuiStr2_4"), LangModule.i18n
						.getString("GroupGuiStr2_2"),
						JOptionPane.INFORMATION_MESSAGE);

				displayChooseNewManager(className, groupName);
			}
		}
	}
}

