// $Id: UserViewTreeGui.java,v 1.4 2003/05/08 20:09:03 thomas Exp $

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
import org.openrct.client.module.ClassModule;
import org.openrct.client.module.TeamModule;
import org.openrct.client.module.GroupModule;
import javax.swing.*;
import javax.swing.tree.*;

public class UserViewTreeGui extends JTree implements Const {

	// Creating the root node
	private DefaultMutableTreeNode rootNode_ = new DefaultMutableTreeNode();

	// Constructor
	public UserViewTreeGui() {

		// Init user, class, team, and group sequences
		RCT.UserSeqHolder userSeq = new RCT.UserSeqHolder();
		RCT.ClassSeqHolder classSeq = new RCT.ClassSeqHolder();
		RCT.TeamSeqHolder teamSeq = new RCT.TeamSeqHolder();
		RCT.GroupSeqHolder groupSeq = new RCT.GroupSeqHolder();

		// Getting the class relevant users
		UserModule.getUsersRelatedByClasses(UserModule.getId(), userSeq);

		// Populate the user tree view
		// Creating and adding the user nodes
		for (int i = 0; i < userSeq.value.length; i++) {

			DefaultMutableTreeNode userNode = new DefaultMutableTreeNode(
					new UserNode(userSeq.value[i].alias,
							conv_status(userSeq.value[i].online_status),
							userSeq.value[i].first_name,
							userSeq.value[i].last_name,
							userSeq.value[i].permission));
			rootNode_.add(userNode);

			// Getting all the user relevant classes
			ClassModule.getClassesFromUserIds(UserModule.getId(),
					userSeq.value[i].user_id, classSeq);

			// Creating and adding classe nodes
			for (int j = 0; j < classSeq.value.length; j++) {

				DefaultMutableTreeNode classNode = new DefaultMutableTreeNode(
						new ClassNode(classSeq.value[j].name,
								conv_status(classSeq.value[j].active_status)));
				userNode.add(classNode);

				// Getting all the class relevant teams and groups
				TeamModule.getTeamsFromUserAndClass(UserModule.getId(),
						userSeq.value[i].user_id, classSeq.value[j].class_id,
						teamSeq);
				GroupModule.getGroupsFromUserAndClass(userSeq.value[i].user_id,
						classSeq.value[j].class_id, groupSeq);

				// Creating and adding Team nodes
				for (int t = 0; t < teamSeq.value.length; t++) {

					classNode.add(new DefaultMutableTreeNode(new TeamNode(
							teamSeq.value[t].name,
							conv_status(teamSeq.value[t].active_status))));
				}

				// Creating and adding group nodes
				for (int g = 0; g < groupSeq.value.length; g++) {

					classNode.add(new DefaultMutableTreeNode(new GroupNode(
							groupSeq.value[g].name)));
				}

			} // End class seq. for loop

		} // End user seq. for loop

		// Set the user view modle
		this.setModel(new DefaultTreeModel(rootNode_));

		// Set the tree node style
		this.putClientProperty(LINE_STYLE, ANGLED);

		// Hide the root node
		this.setRootVisible(false);
	}

	// Helper Methods:
	// ---------------

	// Helper Method:
	// This method convers the string status to a boolean
	private boolean conv_status(String status) {

		if (status.equals(TRUE_STR2) || status.equals(TRUE_STR1)) {

			return true;
		} else {

			return false;
		}
	}

	// Methods:
	// --------

	// Methdo:
	public void refresh() {

		rootNode_.removeAllChildren();

		// Init user, class, team, and group sequences
		RCT.UserSeqHolder userSeq = new RCT.UserSeqHolder();
		RCT.ClassSeqHolder classSeq = new RCT.ClassSeqHolder();
		RCT.TeamSeqHolder teamSeq = new RCT.TeamSeqHolder();
		RCT.GroupSeqHolder groupSeq = new RCT.GroupSeqHolder();

		// Getting the class relevant users
		UserModule.getUsersRelatedByClasses(UserModule.getId(), userSeq);

		// Populate the user tree view
		// Creating and adding the user nodes
		for (int i = 0; i < userSeq.value.length; i++) {

			DefaultMutableTreeNode userNode = new DefaultMutableTreeNode(
					new UserNode(userSeq.value[i].alias,
							conv_status(userSeq.value[i].online_status),
							userSeq.value[i].first_name,
							userSeq.value[i].last_name,
							userSeq.value[i].permission));
			rootNode_.add(userNode);

			// Getting all the user relevant classes
			ClassModule.getClassesFromUserIds(UserModule.getId(),
					userSeq.value[i].user_id, classSeq);

			// Creating and adding classe nodes
			for (int j = 0; j < classSeq.value.length; j++) {

				DefaultMutableTreeNode classNode = new DefaultMutableTreeNode(
						new ClassNode(classSeq.value[j].name,
								conv_status(classSeq.value[j].active_status)));
				userNode.add(classNode);

				// Getting all the class relevant teams and groups
				TeamModule.getTeamsFromUserAndClass(UserModule.getId(),
						userSeq.value[i].user_id, classSeq.value[j].class_id,
						teamSeq);
				GroupModule.getGroupsFromUserAndClass(userSeq.value[i].user_id,
						classSeq.value[j].class_id, groupSeq);

				// Creating and adding Team nodes
				for (int t = 0; t < teamSeq.value.length; t++) {

					classNode.add(new DefaultMutableTreeNode(new TeamNode(
							teamSeq.value[t].name,
							conv_status(teamSeq.value[t].active_status))));
				}

				// Creating and adding group nodes
				for (int g = 0; g < groupSeq.value.length; g++) {

					classNode.add(new DefaultMutableTreeNode(new GroupNode(
							groupSeq.value[g].name)));
				}

			} // End class seq. for loop
		} // End user seq. for loop
	}
}

