// $Id: ClassViewPageGui.java,v 1.5 2003/05/08 20:09:03 thomas Exp $

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
import org.openrct.client.module.PageModule;
import org.openrct.client.module.UserModule;
import org.openrct.client.module.GroupModule;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

public class ClassViewPageGui extends JPanel implements Const {

	private GridBagLayout gridBagLayout2 = new GridBagLayout();

	private JPanel classViewButtonPanel = new JPanel();

	private JScrollPane jScrollPane2 = new JScrollPane();

	private JButton joinTeamButton = new JButton();

	private JButton joinGroupButton = new JButton();

	private JButton createGroupButton = new JButton();

	private JButton viewCCButton = new JButton();

	private ClassViewTreeGui classTree = null;

	private CourseContentGui courseContent = new CourseContentGui();

	// Constructor
	public ClassViewPageGui() {

		classTree = new ClassViewTreeGui();

		this.setLayout(gridBagLayout2);

		// Setup the tree we automatic hight adjustment
		classTree.setRowHeight(-1);

		// Make sure that we can only select one node at the time
		classTree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);

		// Mouse listener code for class tree
		addTreeSelectionListenerClassTree();
		addMouseListenerClassTree();

		classTree.setCellRenderer(new CustomDefaultRenderer());

		jScrollPane2.setAutoscrolls(true);

		joinTeamButton.setText(LangModule.i18n
				.getString("SessionButtonJoinTeam"));
		joinTeamButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				pressedJoinTeamButton();
			}
		});

		joinGroupButton.setText(LangModule.i18n
				.getString("SessionButtonJoinGroup"));
		joinGroupButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				pressedJoinGroupButton();
			}
		});

		createGroupButton.setText(LangModule.i18n
				.getString("SessionButtonCreateGroup"));
		createGroupButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				pressedCreateGroupButton();
			}
		});

		viewCCButton.setText(LangModule.i18n.getString("SessionButtonViewCC"));
		viewCCButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				pressedViewCcButton();
			}
		});

		this.add(jScrollPane2, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(
						2, 0, 2, 0), 0, 0));

		this.add(classViewButtonPanel, new GridBagConstraints(0, 1, 2, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 0, 2, 0), 0, 0));

		classViewButtonPanel.add(joinTeamButton);
		classViewButtonPanel.add(joinGroupButton);
		classViewButtonPanel.add(createGroupButton);
		classViewButtonPanel.add(viewCCButton);

		jScrollPane2.getViewport().add(classTree);

		// Buttons are disabled by default
		joinTeamButton.setEnabled(false);
		joinGroupButton.setEnabled(false);
		createGroupButton.setEnabled(false);
		viewCCButton.setEnabled(false);

		// Do not show the Course Content
		courseContent.setVisible(false);
	}

	// Methods:
	// --------

	// Method:
	private void changeCourseContent(String className) {

		this.remove(courseContent);

		courseContent = new CourseContentGui(className);

		this.add(courseContent, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 100, 0));

		this.invalidate();
		this.validate();
		this.repaint();
	}

	// Gui Methods:
	// ------------

	// Gui Method:
	// Adding mouse listener to the class tree
	private void addTreeSelectionListenerClassTree() {

		// Adding TreeSelectionListener to the class tree.
		// This will detect selection changes for tree nodes and will
		// determine which buttons to activate and deactivate
		// depending on the selected node type.
		classTree.addTreeSelectionListener(new TreeSelectionListener() {

			public void valueChanged(TreeSelectionEvent evt) {

				TreePath path = evt.getPath();

				if (null != path) {

					DefaultMutableTreeNode node = (DefaultMutableTreeNode) path
							.getLastPathComponent();

					CustomNode customNode = (CustomNode) node.getUserObject();

					if (customNode instanceof ClassNode) {

						if (courseContent.isSet()) {

							viewCCButton.setEnabled(false);
						} else {

							viewCCButton.setEnabled(true);
						}

						joinTeamButton.setEnabled(false);
						joinGroupButton.setEnabled(false);
						createGroupButton.setEnabled(true);

						// We do not change the course content gui
						// if we click on current class. Also, we make
						// sure that the course content was requested
						// by clicking on View Course Content
						if (!courseContent
								.isSelected(customNode.getNodeValue())
								&& courseContent.isSet()
								&& courseContent.canClose()) {

							changeCourseContent(customNode.getNodeValue());
						}
					} else if (customNode instanceof GroupNode) {

						DefaultMutableTreeNode classNode = (DefaultMutableTreeNode) node
								.getParent();
						CustomNode customClass = (CustomNode) classNode
								.getUserObject();

						// Prevent a user to join a group twice.
						// In the pressedJoinGroupButton callback, we
						// also disable the Join Group Button.
						if (PageModule.isPageActive(customClass.getNodeValue(),
								customNode.getNodeValue())) {

							joinGroupButton.setEnabled(false);
						} else {

							joinGroupButton.setEnabled(true);
						}

						joinTeamButton.setEnabled(false);
						createGroupButton.setEnabled(false);
						viewCCButton.setEnabled(false);

						// Hide the CourseContent
						if (courseContent.canClose()) {

							courseContent.setVisible(false);
							courseContent.deactivate();
						}
					} else if (customNode instanceof TeamNode) {

						DefaultMutableTreeNode classNode = (DefaultMutableTreeNode) node
								.getParent();
						CustomNode customClass = (CustomNode) classNode
								.getUserObject();

						// Prevent a user to join a team twice.
						// In the pressedJoinTeamButton callback, we
						// also disable the Join Team Button.
						if (PageModule.isPageActive(customClass.getNodeValue(),
								customNode.getNodeValue())) {
							joinTeamButton.setEnabled(false);

						} else {

							joinTeamButton.setEnabled(true);
						}

						joinGroupButton.setEnabled(false);
						createGroupButton.setEnabled(false);
						viewCCButton.setEnabled(false);

						// Hide the CourseContent
						if (courseContent.canClose()) {

							courseContent.setVisible(false);
							courseContent.deactivate();
						}
					} else if (customNode instanceof UserNode) {

						joinTeamButton.setEnabled(false);
						joinGroupButton.setEnabled(false);
						createGroupButton.setEnabled(false);
						viewCCButton.setEnabled(false);

						// Hide the CourseContent
						if (courseContent.canClose()) {

							courseContent.setVisible(false);
							courseContent.deactivate();
						}
					} else {

						System.err.println("ERROR: customNode not recognized!");
					}
				}
			}
		});
	}

	// Gui Method:
	// Adding mouse listener to the class tree
	private void addMouseListenerClassTree() {

		// Adding MouseListener to the class tree.
		// This will detect clicks on tree nodes and will
		// determine which buttons to activate and deactivate
		// depending on the selected node type.
		classTree.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent evt) {

				if (evt.getClickCount() == 1) {

					TreePath path = classTree.getPathForLocation(evt.getX(),
							evt.getY());

					if (null != path) {

						DefaultMutableTreeNode node = (DefaultMutableTreeNode) path
								.getLastPathComponent();

						CustomNode customNode = (CustomNode) node
								.getUserObject();

						if (customNode instanceof GroupNode) {

							DefaultMutableTreeNode classNode = (DefaultMutableTreeNode) node
									.getParent();
							CustomNode customClass = (CustomNode) classNode
									.getUserObject();

							// Prevent a user to join a group twice.
							// In the pressedJoinGroupButton callback, we
							// also disable the Join Group Button.
							if (PageModule.isPageActive(customClass
									.getNodeValue(), customNode.getNodeValue())) {

								joinGroupButton.setEnabled(false);
							} else {

								joinGroupButton.setEnabled(true);
							}
						} else if (customNode instanceof TeamNode) {

							DefaultMutableTreeNode classNode = (DefaultMutableTreeNode) node
									.getParent();
							CustomNode customClass = (CustomNode) classNode
									.getUserObject();

							// Prevent a user to join a team twice.
							// In the pressedJoinTeamButton callback, we
							// also disable the Join Team Button.
							if (PageModule.isPageActive(customClass
									.getNodeValue(), customNode.getNodeValue())) {
								joinTeamButton.setEnabled(false);

							} else {

								joinTeamButton.setEnabled(true);
							}
						}
					}
				}
			}
		});
	}

	// Gui Method:
	public void refresh() {

		classTree.refresh();
		DefaultTreeModel classModel = (DefaultTreeModel) classTree.getModel();
		classModel.nodeStructureChanged((DefaultMutableTreeNode) classModel
				.getRoot());
	}

	// Gui Method:
	// This method enables/disables the Course Content button
	public void setCCBEnabled(boolean state) {

		viewCCButton.setEnabled(state);
	}

	// Gui Method:
	// This method adds the newly created group to both the user and class view
	public synchronized void displayCreateGroupEvent(String className,
			String groupName, String userAlias) {

		// Getting the models
		DefaultTreeModel classModel = (DefaultTreeModel) classTree.getModel();

		if (null != classModel) {

			Object classRootNode = classModel.getRoot();
			addGroupToClassView(classModel, classRootNode, className,
					groupName, userAlias);
		} else {

			System.err.println("ERROR: Did not get the class model!");
			return;
		}

		// Display the new group creation message
		SessionGui.displayMsg(LangModule.i18n.getString("SessionGuiStr1_1")
				+ SP + userAlias + SP
				+ LangModule.i18n.getString("SessionGuiStr1_2") + " \""
				+ groupName + "\" "
				+ LangModule.i18n.getString("SessionGuiStr1_3") + SP
				+ className);
	}

	// Gui Method:
	// This method removes the user from the group node in the classView
	public synchronized void displayMemberExitsGroupEvent(String className,
			String groupName, String userAlias) {

		// Getting the models
		DefaultTreeModel classModel = (DefaultTreeModel) classTree.getModel();

		if (null != classModel) {

			Object classRootNode = classModel.getRoot();
			exitGroupMemberClassView(classModel, classRootNode, className,
					groupName, userAlias);
		} else {

			System.err.println("ERROR: Did not get the class model!");
			return;
		}
	}

	// Gui Method:
	// This method removes the group node from the class view
	public synchronized void displayRemoveGroupEvent(String className,
			String groupName) {

		// Getting the model
		DefaultTreeModel classModel = (DefaultTreeModel) classTree.getModel();

		if (null != classModel) {

			Object classRootNode = classModel.getRoot();
			removeGroupClassView(classModel, classRootNode, className,
					groupName);
		} else {

			System.err.println("ERROR: Did not get the class model!");
		}
	}

	// Gui Method:
	// This method changes the user's active status in the classTree
	public synchronized void displayJoinOrExitTeamEvent(String className,
			String teamName, String userAlias, int msgType) {

		// Getting the model
		DefaultTreeModel classModel = (DefaultTreeModel) classTree.getModel();
		boolean status = false;

		if (TEAM_JOIN_MSG == msgType) {

			status = true;
		} else if (TEAM_EXIT_MSG == msgType) {

			status = false;
		} else {

			System.err.println("ERROR: Did not recognize message type!");
			return;
		}

		// Updating the class tree
		if (null != classModel) {

			Object classRootNode = classModel.getRoot();
			updateActiveStatus(classModel, classRootNode, className, teamName,
					userAlias, status);
		} else {
			System.err.println("ERROR: Did not get the class model!");
		}
	}

	// Gui Method:
	// Method add a user to the specifice group/class in class view
	public synchronized void displayJoinGroupEvent(String className,
			String groupName, String userAlias) {

		// Getting the model
		DefaultTreeModel classModel = (DefaultTreeModel) classTree.getModel();

		if (null != classModel) {

			Object classRootNode = classModel.getRoot();
			addUserToGroupInClassView(classModel, classRootNode, className,
					groupName, userAlias);
		} else {

			System.err.println("ERROR: Did not get the class model!");
			return;
		}
	}

	// Helper Methods:
	// ---------------

	// Helper Method:
	// This method adds a group to the classView.
	private void addGroupToClassView(DefaultTreeModel model, Object obj,
			String className, String groupName, String userAlias) {

		int numClasses = 0;

		numClasses = model.getChildCount(obj);

		for (int i = 0; i < numClasses; i++) {

			DefaultMutableTreeNode defaultClass = (DefaultMutableTreeNode) model
					.getChild(obj, i);
			CustomNode customClass = (CustomNode) defaultClass.getUserObject();

			if (customClass.getNodeValue().equals(className)) {

				DefaultMutableTreeNode groupNode = new DefaultMutableTreeNode(
						new GroupNode(groupName));
				defaultClass.add(groupNode);
				groupNode.add(new DefaultMutableTreeNode(new UserNode(
						userAlias, true)));
				model.nodeStructureChanged(defaultClass);
				return;
			}
		}

		// We only execute this part of the code if we did not find
		// the user in the classView. This happens if there is a
		// new class on the server since we last logged in.
		SessionGui.refreshUserViewPage();
		SessionGui.refreshClassViewPage();
	}

	// Helper Method:
	// This method adds a user to a group/class in the classView
	private void addUserToGroupInClassView(DefaultTreeModel model, Object obj,
			String className, String groupName, String userAlias) {

		int numClasses = 0;
		int numAssemblies = 0;

		numClasses = model.getChildCount(obj);

		for (int i = 0; i < numClasses; i++) {

			DefaultMutableTreeNode defaultClass = (DefaultMutableTreeNode) model
					.getChild(obj, i);
			CustomNode customClass = (CustomNode) defaultClass.getUserObject();

			if (customClass.getNodeValue().equals(className)) {

				numAssemblies = model.getChildCount(defaultClass);

				for (int j = 0; j < numAssemblies; j++) {

					DefaultMutableTreeNode defaultAssembly = (DefaultMutableTreeNode) model
							.getChild(defaultClass, j);
					CustomNode customAssembly = (CustomNode) defaultAssembly
							.getUserObject();

					if (customAssembly.getNodeValue().equals(groupName)) {

						defaultAssembly.add(new DefaultMutableTreeNode(
								new UserNode(userAlias, true)));
						model.nodeStructureChanged(defaultAssembly);
						return;
					}
				}
			}
		}
	}

	// Helper Method:
	// Methods deals with group memeber exit events updating class view
	private void exitGroupMemberClassView(DefaultTreeModel model, Object obj,
			String className, String groupName, String userAlias) {

		int numClasses = 0;
		int numAssemblies = 0;
		int numUsers = 0;

		numClasses = model.getChildCount(obj);

		// This is a non recursive approach. Before, I always got an
		// "ArrayIndexOutOfBoundsException" exception while using the
		// recursive approach.
		for (int i = 0; i < numClasses; i++) {

			DefaultMutableTreeNode defaultClass = (DefaultMutableTreeNode) model
					.getChild(obj, i);
			CustomNode customClass = (CustomNode) defaultClass.getUserObject();

			if (customClass.getNodeValue().equals(className)) {

				numAssemblies = model.getChildCount(defaultClass);

				for (int j = 0; j < numAssemblies; j++) {

					DefaultMutableTreeNode defaultAssembly = (DefaultMutableTreeNode) model
							.getChild(defaultClass, j);
					CustomNode customAssembly = (CustomNode) defaultAssembly
							.getUserObject();

					if (customAssembly.getNodeValue().equals(groupName)) {

						numUsers = model.getChildCount(defaultAssembly);

						for (int k = 0; k < numUsers; k++) {

							DefaultMutableTreeNode defaultUser = (DefaultMutableTreeNode) model
									.getChild(defaultAssembly, k);
							CustomNode customUser = (CustomNode) defaultUser
									.getUserObject();

							if (customUser.getNodeValue().equals(userAlias)) {

								// Catch any exception
								try {
									defaultAssembly.remove(defaultUser);
									model.nodeStructureChanged(defaultAssembly);
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
	// This method removes the specified group from the class view
	private void removeGroupClassView(DefaultTreeModel model, Object obj,
			String className, String groupName) {

		int numClasses = 0;
		int numAssemblies = 0;

		numClasses = model.getChildCount(obj);

		for (int i = 0; i < numClasses; i++) {

			DefaultMutableTreeNode defaultClass = (DefaultMutableTreeNode) model
					.getChild(obj, i);
			CustomNode customClass = (CustomNode) defaultClass.getUserObject();

			if (customClass.getNodeValue().equals(className)) {

				numAssemblies = model.getChildCount(defaultClass);

				for (int j = 0; j < numAssemblies; j++) {

					DefaultMutableTreeNode defaultAssembly = (DefaultMutableTreeNode) model
							.getChild(defaultClass, j);
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

	// Helper Method:
	// This method traverses the tree model and changes
	// the user's active status. It is used for ClassView operation.
	private void updateActiveStatus(DefaultTreeModel model, Object obj,
			String className, String teamName, String userAlias, boolean status) {

		int numClasses = 0;
		int numAssemblies = 0;
		int numUsers = 0;

		numClasses = model.getChildCount(obj);

		for (int i = 0; i < numClasses; i++) {

			DefaultMutableTreeNode defaultClass = (DefaultMutableTreeNode) model
					.getChild(obj, i);
			CustomNode customClass = (CustomNode) defaultClass.getUserObject();

			if (customClass.getNodeValue().equals(className)) {

				numAssemblies = model.getChildCount(defaultClass);

				for (int j = 0; j < numAssemblies; j++) {

					DefaultMutableTreeNode defaultAssembly = (DefaultMutableTreeNode) model
							.getChild(defaultClass, j);
					CustomNode customAssembly = (CustomNode) defaultAssembly
							.getUserObject();

					if (customAssembly.getNodeValue().equals(teamName)) {

						numUsers = model.getChildCount(defaultAssembly);

						for (int k = 0; k < numUsers; k++) {

							DefaultMutableTreeNode defaultUser = (DefaultMutableTreeNode) model
									.getChild(defaultAssembly, k);
							CustomNode customUser = (CustomNode) defaultUser
									.getUserObject();

							if (customUser.getNodeValue().equals(userAlias)) {

								customUser.setStatus(status);
								model.nodeStructureChanged(defaultUser);
								return;
							}
						}
					}
				}
			}
		}

		// We only execute this part of the code if we did not find
		// the user in the classView. This happens if there is a
		// new class or team on the server since we last logged in.
		SessionGui.refreshClassViewPage();
		SessionGui.refreshUserViewPage();
	}

	// Callbacks:
	// ----------

	// Callback
	private void pressedJoinTeamButton() {

		// Get the selected node
		DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) classTree
				.getSelectionPath().getLastPathComponent();

		// Get the user object from the selected node,
		// which is a teamNode
		CustomNode teamNode = (CustomNode) selectedNode.getUserObject();

		// Get the parent node of the selected node
		DefaultMutableTreeNode parent = (DefaultMutableTreeNode) selectedNode
				.getParent();

		// Get the user object from the parent node,
		// which is a class node
		CustomNode classNode = (CustomNode) parent.getUserObject();

		String className = classNode.getNodeValue();
		String teamName = teamNode.getNodeValue();

		PageModule.displayJoinPage(className, teamName, CLASS_TEAM_TYPE);

		// Right after we clicked on the JoinTeam button, we
		// need to disable it. This is part of the action to
		// prevent a user to join twice in the same team
		joinTeamButton.setEnabled(false);
	}

	// Callback
	private void pressedJoinGroupButton() {

		// Get the selected node
		DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) classTree
				.getSelectionPath().getLastPathComponent();

		// Get the user object from the selected node,
		// which is a groupNode
		CustomNode groupNode = (CustomNode) selectedNode.getUserObject();

		// Get the parent node of the selected node
		DefaultMutableTreeNode parent = (DefaultMutableTreeNode) selectedNode
				.getParent();

		// Get the user object from the parent node,
		// which is a class node
		CustomNode classNode = (CustomNode) parent.getUserObject();

		String className = classNode.getNodeValue();
		String groupName = groupNode.getNodeValue();

		GroupModule.joinGroupRequest(className, groupName, UserModule.getId(),
				UserModule.getAlias());

		// Right after we clicked on the JoinGroup button, we
		// need to disable it. This is part of the action to
		// prevent a user to join twice in the same group
		joinGroupButton.setEnabled(false);
	}

	// Callback
	private void pressedCreateGroupButton() {

		String groupName = JOptionPane.showInputDialog(this, LangModule.i18n
				.getString("SessionGuiDialog1"));

		if (null == groupName) {

			// Nothing to do. User clicked Cancel button
		} else if (0 == groupName.length()) {

			// User entered a zero length string
			JOptionPane.showMessageDialog(this, LangModule.i18n
					.getString("SessionGuiDialog2"), LangModule.i18n
					.getString("SessionGuiDialog3"), JOptionPane.ERROR_MESSAGE);
		} else {

			// Get the selected class name
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) classTree
					.getSelectionPath().getLastPathComponent();

			// Get the user object from the selected node,
			CustomNode customNode = (CustomNode) selectedNode.getUserObject();

			String className = customNode.getNodeValue();

			boolean canCreateGroup = GroupModule.createGroup(className,
					groupName, UserModule.getId(), UserModule.getAlias());

			if (canCreateGroup) {

				// Everything is ok so we can create the page group
				PageModule.displayJoinPage(className, groupName,
						CLASS_GROUP_MGT_TYPE);

				// Switch to the Module Page after we added the page
				// Also select the newly created page
				SessionGui.viewSessionPage(MODULE_PAGE);
				int page = SessionGui.getAssemblyPageNumber(className + " : "
						+ groupName);
				SessionGui.viewAssemblyPage(page);

				// If the sessionTabbedPane has the TabbedPane disabled,
				// we need to enable it.
				if (false == SessionGui.isSessionPageEnabled(MODULE_PAGE)) {
					SessionGui.setEnabledSessionPage(MODULE_PAGE, true);
				}
			} else {

				// The group name is taken
				JOptionPane.showMessageDialog(this, LangModule.i18n
						.getString("SessionGuiDialog4"), LangModule.i18n
						.getString("SessionGuiDialog3"),
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	// Callback
	private void pressedViewCcButton() {

		DefaultMutableTreeNode node = (DefaultMutableTreeNode) classTree
				.getSelectionPath().getLastPathComponent();

		CustomNode customNode = (CustomNode) node.getUserObject();

		if (customNode instanceof ClassNode) {

			changeCourseContent(customNode.getNodeValue());
		} else {

			System.err.println("ERROR: Expected ClassNode");
		}

		viewCCButton.setEnabled(false);
	}
}

