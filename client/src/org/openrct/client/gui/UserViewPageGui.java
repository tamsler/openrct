// $Id: UserViewPageGui.java,v 1.4 2003/05/08 20:09:03 thomas Exp $

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
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

public class UserViewPageGui extends JPanel implements Const {

	private GridBagLayout gridBagLayout3 = new GridBagLayout();

	private JPanel userViewButtonPanel = new JPanel();

	private JScrollPane jScrollPane1 = new JScrollPane();

	private JButton viewUIButton = new JButton();

	private UserViewTreeGui userTree = null;

	private UserInfoGui userInfo = new UserInfoGui();

	// Constructor
	public UserViewPageGui() {

		userTree = new UserViewTreeGui();

		this.setLayout(gridBagLayout3);

		// Setup the tree we automatic hight adjustment
		userTree.setRowHeight(-1);

		// Make sure that we can only select one node at the time
		userTree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);

		// Mouse listener code for user tree
		addTreeSelectionListenerUserTree();

		userTree.setCellRenderer(new CustomDefaultRenderer());

		jScrollPane1.setAutoscrolls(true);

		viewUIButton.setText(LangModule.i18n.getString("SessionButtonVUI"));
		viewUIButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				pressedViewUiButton();
			}
		});

		this.add(jScrollPane1, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(
						0, 0, 5, 0), 0, 0));

		jScrollPane1.getViewport().add(userTree);

		this.add(userViewButtonPanel, new GridBagConstraints(0, 1, 2, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));

		userViewButtonPanel.add(viewUIButton);

		// This is where we add/show a UserInfo object
		this.add(userInfo, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 50, 0));

		// Buttons are disabled by default
		viewUIButton.setEnabled(false);

		// Do not show the UserInfo
		userInfo.setVisible(false);
	}

	// Methods:
	// --------

	// Method:
	private void changeUserInfo(String userAlias, String firstName,
			String lastName, String permission) {

		this.remove(userInfo);

		userInfo = new UserInfoGui(userAlias, firstName, lastName, permission);

		this.add(userInfo, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 50, 0));

		this.invalidate();
		this.validate();
		this.repaint();
	}

	// Gui Methods:
	// ------------

	// Gui Method:
	// Adding mouse listener to the user tree
	private void addTreeSelectionListenerUserTree() {

		// Adding TreeSelectionListener to the user tree.
		// This will detect selection changes for tree nodes and will
		// determine which buttons to activate and deactivate
		// depending on the selected node type.
		userTree.addTreeSelectionListener(new TreeSelectionListener() {

			public void valueChanged(TreeSelectionEvent evt) {

				TreePath path = evt.getPath();

				if (null != path) {

					DefaultMutableTreeNode node = (DefaultMutableTreeNode) path
							.getLastPathComponent();

					CustomNode customNode = (CustomNode) node.getUserObject();

					if (customNode instanceof UserNode) {

						if (userInfo.isSet()) {

							viewUIButton.setEnabled(false);
						} else {

							viewUIButton.setEnabled(true);
						}

						// We do not change the user info gui if we
						// click on current user. Also, make we make
						// sure that the user info was requested by
						// by clicking on View User Info
						if (!userInfo.isSelected(customNode.getNodeValue())
								&& userInfo.isSet()) {

							changeUserInfo(customNode.getNodeValue(),
									((UserNode) customNode).getFirstName(),
									((UserNode) customNode).getLastName(),
									((UserNode) customNode).getPermission());
						}
					} else if (customNode instanceof ClassNode) {

						viewUIButton.setEnabled(false);

						// Hide the UserInfo
						userInfo.setVisible(false);
						userInfo.deactivate();
					} else if (customNode instanceof GroupNode) {

						viewUIButton.setEnabled(false);

						// Hide the UserInfo
						userInfo.setVisible(false);
						userInfo.deactivate();
					} else if (customNode instanceof TeamNode) {

						viewUIButton.setEnabled(false);

						// Hide the UserInfo
						userInfo.setVisible(false);
						userInfo.deactivate();
					} else {

						System.err.println("ERROR: customNode not recognized!");
					}
				}
			}
		});
	}

	// Gui Method:
	public void refresh() {

		userTree.refresh();
		DefaultTreeModel userModel = (DefaultTreeModel) userTree.getModel();
		userModel.nodeStructureChanged((DefaultMutableTreeNode) userModel
				.getRoot());
	}

	// Gui Method:
	// This method enables/disables the View User Info button
	public void setUIBEnabled(boolean state) {

		viewUIButton.setEnabled(state);
	}

	// Gui Method:
	// This method changes the user's online status in the userView
	public synchronized void updateUserOnlineStatus(String userAlias,
			int msgType) {

		// Getting the models
		DefaultTreeModel userModel = (DefaultTreeModel) userTree.getModel();
		boolean status = false;

		if (BC_MSG_USER_ONLINE == msgType) {

			status = true;
		} else if (BC_MSG_USER_OFFLINE == msgType) {

			status = false;
		} else {

			System.err.println("ERROR: Did not recognize message type!");
			return;
		}

		// Updating the user tree
		if (null != userModel) {

			Object userRootNode = userModel.getRoot();
			updateOnlineStatus(userModel, userRootNode, userAlias, status);
		} else {

			System.err.println("INFO: UserTree is empty.");
		}
	}

	// Gui Method:
	// This method adds the newly created group to the userView
	public synchronized void displayCreateGroupEvent(String className,
			String groupName, String userAlias) {

		// Getting the models
		DefaultTreeModel userModel = (DefaultTreeModel) userTree.getModel();

		if (null != userModel) {

			Object userRootNode = userModel.getRoot();
			addGroupToUserView(userModel, userRootNode, className, groupName,
					userAlias);
		} else {

			System.err.println("ERROR: Did not get the user model!");
		}
	}

	// Gui Method:
	// This method removes the group node from the userView
	public synchronized void displayMemberExitsGroupEvent(String className,
			String groupName, String userAlias) {

		// Getting the models
		DefaultTreeModel userModel = (DefaultTreeModel) userTree.getModel();

		if (null != userModel) {

			Object userRootNode = userModel.getRoot();
			exitGroupMemberUserView(userModel, userRootNode, className,
					groupName, userAlias);
		} else {

			System.err.println("ERROR: Did not get the user model!");
		}
	}

	// Gui Method:
	// Method add a user to the specifice group/class in userView
	public synchronized void displayJoinGroupEvent(String className,
			String groupName, String userAlias) {

		// Getting the model
		DefaultTreeModel userModel = (DefaultTreeModel) userTree.getModel();

		if (null != userModel) {

			Object userRootNode = userModel.getRoot();
			addGroupToUserView(userModel, userRootNode, className, groupName,
					userAlias);
		} else {

			System.err.println("ERROR: Did not get the user model!");
		}
	}

	// Helper Methods:
	// ---------------

	// Helper Method:
	// This method adds a group to the userView. It adds the group
	// only to users who joined the group
	private void addGroupToUserView(DefaultTreeModel model, Object obj,
			String className, String groupName, String userAlias) {

		boolean hasGroupName = false;
		int numUsers = 0;
		int numClasses = 0;
		int numAssemblies = 0;

		numUsers = model.getChildCount(obj);

		for (int i = 0; i < numUsers; i++) {

			DefaultMutableTreeNode defaultUser = (DefaultMutableTreeNode) model
					.getChild(obj, i);
			CustomNode customUser = (CustomNode) defaultUser.getUserObject();

			if (customUser.getNodeValue().equals(userAlias)) {

				numClasses = model.getChildCount(defaultUser);

				for (int j = 0; j < numClasses; j++) {

					DefaultMutableTreeNode defaultClass = (DefaultMutableTreeNode) model
							.getChild(defaultUser, j);
					CustomNode customClass = (CustomNode) defaultClass
							.getUserObject();

					if (customClass.getNodeValue().equals(className)) {

						numAssemblies = model.getChildCount(defaultClass);

						for (int k = 0; k < numAssemblies; k++) {

							DefaultMutableTreeNode defaultAssembly = (DefaultMutableTreeNode) model
									.getChild(defaultClass, k);
							CustomNode customAssembly = (CustomNode) defaultAssembly
									.getUserObject();

							if (customAssembly.getNodeValue().equals(groupName)) {

								hasGroupName = true;
							}
						}

						if (!hasGroupName) {

							defaultClass.add(new DefaultMutableTreeNode(
									new GroupNode(groupName)));
							model.nodeStructureChanged(defaultClass);
							return;
						}
					}
				}
			}
		}
	}

	// Helper Method:
	// Methods deals with group memeber exit events updating userView
	private void exitGroupMemberUserView(DefaultTreeModel model, Object obj,
			String className, String groupName, String userAlias) {
		int numClasses = 0;
		int numAssemblies = 0;
		int numUsers = 0;

		numUsers = model.getChildCount(obj);

		for (int i = 0; i < numUsers; i++) {

			DefaultMutableTreeNode defaultUser = (DefaultMutableTreeNode) model
					.getChild(obj, i);
			CustomNode customUser = (CustomNode) defaultUser.getUserObject();

			if (customUser.getNodeValue().equals(userAlias)) {

				numClasses = model.getChildCount(defaultUser);

				for (int j = 0; j < numClasses; j++) {

					DefaultMutableTreeNode defaultClass = (DefaultMutableTreeNode) model
							.getChild(defaultUser, j);
					CustomNode customClass = (CustomNode) defaultClass
							.getUserObject();

					if (customClass.getNodeValue().equals(className)) {

						numAssemblies = model.getChildCount(defaultClass);

						for (int k = 0; k < numAssemblies; k++) {

							DefaultMutableTreeNode defaultAssembly = (DefaultMutableTreeNode) model
									.getChild(defaultClass, k);
							CustomNode customAssembly = (CustomNode) defaultAssembly
									.getUserObject();

							if (customAssembly.getNodeValue().equals(groupName)
									&& (customAssembly instanceof GroupNode)) {

								// Catch any exception
								try {
									defaultClass.remove(defaultAssembly);
									model.nodeStructureChanged(defaultClass);
								} catch (Exception e) {
									// Don't do anything
								}

								return;
							}
						}
					}
				}
			}
		}
	}

	// Helper Method:
	// This method traverses the tree model and changes
	// the user's online status. It is used for UserView operation.
	private void updateOnlineStatus(DefaultTreeModel model, Object obj,
			String userAlias, boolean status) {

		int numUsers = 0;

		numUsers = model.getChildCount(obj);

		for (int i = 0; i < numUsers; i++) {

			DefaultMutableTreeNode defaultUser = (DefaultMutableTreeNode) model
					.getChild(obj, i);
			CustomNode customUser = (CustomNode) defaultUser.getUserObject();

			if (customUser.getNodeValue().equals(userAlias)) {

				customUser.setStatus(status);
				model.nodeStructureChanged(defaultUser);
				// We can return since we found the specified user
				return;
			}
		}

		// We only execute this part of the code
		// if we did not find the user in the UserView.
		// This means that a new user was added on the server since
		// we last logged in.
		SessionGui.refreshUserViewPage();
		SessionGui.refreshClassViewPage();
	}

	// Callbacks:
	// ----------

	// Callback
	private void pressedViewUiButton() {

		DefaultMutableTreeNode node = (DefaultMutableTreeNode) userTree
				.getSelectionPath().getLastPathComponent();

		CustomNode customNode = (CustomNode) node.getUserObject();

		if (customNode instanceof UserNode) {

			changeUserInfo(customNode.getNodeValue(), ((UserNode) customNode)
					.getFirstName(), ((UserNode) customNode).getLastName(),
					((UserNode) customNode).getPermission());
		} else {

			System.err.println("ERROR: Expected UserNode!");
		}

		viewUIButton.setEnabled(false);
	}
}

