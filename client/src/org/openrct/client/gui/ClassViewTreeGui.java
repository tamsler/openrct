// $Id: ClassViewTreeGui.java,v 1.4 2003/05/08 20:09:03 thomas Exp $

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

public class ClassViewTreeGui extends JTree implements Const {

	// Create the root node
	private DefaultMutableTreeNode rootNode_ = new DefaultMutableTreeNode();

	// Constructor
	public ClassViewTreeGui() {

		// Init user, class, team, and group sequences
		RCT.UserSeqHolder userSeqTeam = new RCT.UserSeqHolder();
		RCT.UserSeqHolder userSeqGroup = new RCT.UserSeqHolder();
		RCT.ClassSeqHolder classSeq = new RCT.ClassSeqHolder();
		RCT.TeamSeqHolder teamSeq = new RCT.TeamSeqHolder();
		RCT.GroupSeqHolder groupSeq = new RCT.GroupSeqHolder();

		// Keep track of which users are online in which team
		boolean activeStatus = false;

		// Getting the user relevant classes
		ClassModule.getClassesFromUserId(UserModule.getId(), classSeq);

		// Populate the ClassViewTree
		// Creating and adding the class nodes
		for (int i = 0; i < classSeq.value.length; i++) {

			DefaultMutableTreeNode classNode = new DefaultMutableTreeNode(
					new ClassNode(classSeq.value[i].name,
							convertStatus(classSeq.value[i].active_status)));
			rootNode_.add(classNode);

			// Getting all the class relevant teams
			TeamModule.getTeamsFromUserAndClass(UserModule.getId(), UserModule
					.getId(), classSeq.value[i].class_id, teamSeq);

			// Creating and adding the team nodes
			for (int j = 0; j < teamSeq.value.length; j++) {

				DefaultMutableTreeNode teamNode = new DefaultMutableTreeNode(
						new TeamNode(teamSeq.value[j].name,
								convertStatus(teamSeq.value[j].active_status)));
				classNode.add(teamNode);

				// Getting all the team relevant users
				UserModule.getUsersFromTeamName(classSeq.value[i].name,
						teamSeq.value[j].name, userSeqTeam);

				// Creating and adding the user nodes
				for (int k = 0; k < userSeqTeam.value.length; k++) {

					// Check if the user is active in the team
					activeStatus = TeamModule.isUserActiveInTeam(
							classSeq.value[i].name, teamSeq.value[j].name,
							userSeqTeam.value[k].user_id,
							userSeqTeam.value[k].alias);

					teamNode.add(new DefaultMutableTreeNode(new UserNode(
							userSeqTeam.value[k].alias, activeStatus)));
				}

			} // End team seq. for loop

			// Getting all the class relevant groups
			GroupModule
					.getGroupsFromClass(classSeq.value[i].class_id, groupSeq);

			// Creating and adding the group nodes
			for (int m = 0; m < groupSeq.value.length; m++) {

				DefaultMutableTreeNode groupNode = new DefaultMutableTreeNode(
						new GroupNode(groupSeq.value[m].name));
				classNode.add(groupNode);

				// Getting all the group relevant users
				UserModule.getUsersFromGroupName(classSeq.value[i].name,
						groupSeq.value[m].name, userSeqGroup);

				// Creating and adding the user nodes
				for (int n = 0; n < userSeqGroup.value.length; n++) {

					groupNode.add(new DefaultMutableTreeNode(new UserNode(
							userSeqGroup.value[n].alias, true)));
				}

			} // End group seq. for loop

		} // End class seq. for loop

		// Set the user view modle
		this.setModel(new DefaultTreeModel(rootNode_));

		// Set the tree node style
		this.putClientProperty("JTree.lineStyle", "Angled");

		// Hide the root node
		this.setRootVisible(false);
	}

	// Helper Methods:
	// ---------------

	// Helper Method that convers the string status to a boolean
	private boolean convertStatus(String status) {

		if (status.equals("t") || status.equals("true")) {
			return true;
		} else {
			return false;
		}
	}

	// Methods:
	// --------

	// Method:
	public void refresh() {

		rootNode_.removeAllChildren();

		// Init user, class, team, and group sequences
		RCT.UserSeqHolder userSeqTeam = new RCT.UserSeqHolder();
		RCT.UserSeqHolder userSeqGroup = new RCT.UserSeqHolder();
		RCT.ClassSeqHolder classSeq = new RCT.ClassSeqHolder();
		RCT.TeamSeqHolder teamSeq = new RCT.TeamSeqHolder();
		RCT.GroupSeqHolder groupSeq = new RCT.GroupSeqHolder();

		// Keep track of which users are online in which team
		boolean activeStatus = false;

		// Getting the user relevant classes
		ClassModule.getClassesFromUserId(UserModule.getId(), classSeq);

		// Populate the ClassViewTree
		// Creating and adding the class nodes
		for (int i = 0; i < classSeq.value.length; i++) {

			DefaultMutableTreeNode classNode = new DefaultMutableTreeNode(
					new ClassNode(classSeq.value[i].name,
							convertStatus(classSeq.value[i].active_status)));
			rootNode_.add(classNode);

			// Getting all the class relevant teams
			TeamModule.getTeamsFromUserAndClass(UserModule.getId(), UserModule
					.getId(), classSeq.value[i].class_id, teamSeq);

			// Creating and adding the team nodes
			for (int j = 0; j < teamSeq.value.length; j++) {

				DefaultMutableTreeNode teamNode = new DefaultMutableTreeNode(
						new TeamNode(teamSeq.value[j].name,
								convertStatus(teamSeq.value[j].active_status)));
				classNode.add(teamNode);

				// Getting all the team relevant users
				UserModule.getUsersFromTeamName(classSeq.value[i].name,
						teamSeq.value[j].name, userSeqTeam);

				// Creating and adding the user nodes
				for (int k = 0; k < userSeqTeam.value.length; k++) {

					// Check if the user is active in the team
					activeStatus = TeamModule.isUserActiveInTeam(
							classSeq.value[i].name, teamSeq.value[j].name,
							userSeqTeam.value[k].user_id,
							userSeqTeam.value[k].alias);

					teamNode.add(new DefaultMutableTreeNode(new UserNode(
							userSeqTeam.value[k].alias, activeStatus)));
				}

			} // End team seq. for loop

			// Getting all the class relevant groups
			GroupModule
					.getGroupsFromClass(classSeq.value[i].class_id, groupSeq);

			// Creating and adding the group nodes
			for (int m = 0; m < groupSeq.value.length; m++) {

				DefaultMutableTreeNode groupNode = new DefaultMutableTreeNode(
						new GroupNode(groupSeq.value[m].name));
				classNode.add(groupNode);

				// Getting all the group relevant users
				UserModule.getUsersFromGroupName(classSeq.value[i].name,
						groupSeq.value[m].name, userSeqGroup);

				// Creating and adding the user nodes
				for (int n = 0; n < userSeqGroup.value.length; n++) {

					groupNode.add(new DefaultMutableTreeNode(new UserNode(
							userSeqGroup.value[n].alias, true)));
				}

			} // End group seq. for loop

		} // End class seq. for loop
	}
}

