// $Id: PageGui.java,v 1.6 2003/05/16 19:19:00 thomas Exp $

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
import org.openrct.client.module.LangModule;
import org.openrct.client.module.SessionModule;
import org.openrct.client.module.PageModule;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

// Each chat page is a tabbed pane
public abstract class PageGui extends JPanel implements Const {

	private String className_;

	private String assemblyName_;

	private int pageType_;

	protected boolean isManaged_;

	protected JTabbedPane moduleTabbedPane = new JTabbedPane();

	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private GridBagLayout gridBagLayout2 = new GridBagLayout();

	private JPanel userListPanel = new JPanel();

	// Modules:
	// For adding additional modules, make the appropriate
	// entries below:
	protected ChatPageGui chatModulePage = null;

	protected SoundPageGui soundModulePage = null;

	protected TextpadPageGui textpadModulePage = null;

	protected UrlPageGui urlModulePage = null;

	protected FtpPageGui ftpModulePage = null;

	protected TDPageGui tdModulePage = null;

	private JButton exitButton = new JButton();

	private TitledBorder titledBorder5;

	private JScrollPane userListScrollPane = new JScrollPane();

	protected JList userList = new JList();

	// Construct the frame
	public PageGui(String className, String assemblyName, int pageType)
			throws Exception {

		// Asseign private memebers
		className_ = className;
		assemblyName_ = assemblyName;
		pageType_ = pageType;
		isManaged_ = UserModule.isUserManagerOfAssembly(className_,
				assemblyName_, UserModule.getId(), pageType_);
		// Module Pages
		// For adding additional modules, make the appropriate
		// entries below:
		chatModulePage = new ChatPageGui(className_, assemblyName_, pageType_);
		soundModulePage = new SoundPageGui(className_, assemblyName_, pageType_);
		textpadModulePage = new TextpadPageGui(className_, assemblyName,
				pageType_, isManaged_);
		urlModulePage = new UrlPageGui(className_, assemblyName_, pageType_);
		ftpModulePage = new FtpPageGui(className_, assemblyName_, pageType_);

		this.setLayout(gridBagLayout1);

		// Modules:
		// For adding additional modules, make the appropriate
		// entries below:
		moduleTabbedPane.setTabPlacement(JTabbedPane.TOP);
		moduleTabbedPane.add(chatModulePage, LangModule.i18n
				.getString("PageGuiTPLabelChat"));
		moduleTabbedPane.add(soundModulePage, LangModule.i18n
				.getString("PageGuiTPLabelSound"));
		moduleTabbedPane.add(textpadModulePage, LangModule.i18n
				.getString("PageGuiTPLabelTextpad"));
		moduleTabbedPane.add(urlModulePage, LangModule.i18n
				.getString("PageGuiTPLabelUrl"));
		moduleTabbedPane.add(ftpModulePage, LangModule.i18n
				.getString("PageGuiTPLabelFtp"));

		titledBorder5 = new TitledBorder("");

		if (this instanceof PageTeamGui) {

			exitButton.setText(LangModule.i18n.getString("PageGuiButtonExitT"));
		} else if (this instanceof PageGroupGui) {

			exitButton.setText(LangModule.i18n.getString("PageGuiButtonExitG"));
		} else {

			System.err.println("ERROR: did not recognize the object type!");
		}

		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				pressedExitButton();
			}
		});

		userListPanel.setLayout(gridBagLayout2);

		titledBorder5.setTitle(LangModule.i18n.getString("PageGuiBorderUsers"));
		titledBorder5.setTitleJustification(2);

		userListScrollPane.setAutoscrolls(true);
		userListScrollPane.setBorder(titledBorder5);

		this.add(moduleTabbedPane, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						5, 5, 5, 5), 0, 0));

		this.add(userListPanel, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
				new Insets(0, 0, 0, 0), 0, 0));

		userListPanel.add(userListScrollPane, new GridBagConstraints(0, 0, 1,
				1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(5, 0, 5, 5), 30, 0));

		userListPanel.add(exitButton, new GridBagConstraints(0, 1, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 5, 5), 0, 0));

		userListScrollPane.getViewport().add(userList);
	}

	// Method: Module Page accessor
	// For adding additional modules, make the appropriate
	// entries below:
	public JPanel getModulePage(int pageType) {

		switch (pageType) {

		case MOD_CHAT:
			return chatModulePage;

		case MOD_SOUND:
			return soundModulePage;

		case MOD_TEXTPAD:
			return textpadModulePage;

		case MOD_URL:
			return urlModulePage;

		case MOD_FTP:
			return ftpModulePage;

		case MOD_TD:
			return tdModulePage;

		default:
			System.err.println("ERROR: PageGui.getModulePage()!");
			return null;
		}
	}

	// Mehtod: Call the exit method for all
	// modules
	// For adding additional modules, make the appropriate
	// entries below:
	private void exitAllModules() {

		chatModulePage.exitRequest();
		soundModulePage.exitRequest();
		textpadModulePage.exitRequest();
		urlModulePage.exitRequest();
		ftpModulePage.exitRequest();

		if (null != tdModulePage) {
			tdModulePage.exitRequest();
		}
	}

	// Method: isManaged accessor
	public boolean isManaged() {

		return isManaged_;
	}

	// Method: Close page
	// Called during the logout process
	public void closePage() {

		SessionModule.displayDisablePages();

		this.exitAllModules();

		this.logout();

		SessionModule.displayRemovePageAtTab(getClassName() + " : "
				+ getAssemblyName());

		// Let the page module know about the exit page call
		PageModule.exitPage(getClassName(), getAssemblyName());
	}

	// Method: Add a user to the active user list
	public synchronized void displayAssemblyMember(String userAlias) {

		// We have to make sure that we do not add the current user twice
		if (!userAlias.equals(UserModule.getAlias())) {

			DefaultListModel userListModel = (DefaultListModel) userList
					.getModel();

			// Test if the user is already present. There are
			// cases when users join a team at "the same time"
			// and the userListModel has the user entry already
			// and the join user notification arrives. In this case
			// we don't add the user twice.
			if (!userListModel.contains(userAlias)) {

				// The group mgr is at the top of the list
				// so if we add new users, we add them
				userListModel.insertElementAt(userAlias, userListModel
						.getSize());
			}
		}
	}

	// Method:
	// This method moves the assembly manager to the top of the module
	// page user list.
	public void displayAssemblyMgrAtTop(String userAlias) {

		// First we remove the user
		this.removeAssemblyMember(userAlias);

		synchronized (this) {

			// Then we add it at the top
			DefaultListModel userListModel = (DefaultListModel) userList
					.getModel();
			userListModel.insertElementAt(userAlias, 0);

			// Also check this user is now the manager
			if (UserModule.getAlias().equals(userAlias)) {

				isManaged_ = true;

				// List controlled modules to activate the
				// take button
				textpadModulePage.setVisibleTakeButton(true);

			}
		}
	}

	// Method: Remove a user from the active user list
	public synchronized void removeAssemblyMember(String userAlias) {

		DefaultListModel userListModel = (DefaultListModel) userList.getModel();
		userListModel.remove(userListModel.indexOf(userAlias));
	}

	// Callbacks:
	// ----------

	// Callback:
	private void pressedExitButton() {

		SessionModule.displayDisablePages();

		exitAllModules();

		exitPage();

		SessionModule.displayRemoveSelectedPage();

		// Let the page module know about the exit page call
		PageModule.exitPage(getClassName(), getAssemblyName());
	}

	// Abstract Methods:
	// -----------------

	// Abstract Method: Get the class name
	public abstract String getClassName();

	// Abstract Method: Get assebmly name
	// Assembly name is either the team or group name
	public abstract String getAssemblyName();

	// Abstract Method: Exit
	// This method is called if a user clicks on the Exit button
	// in a Module Page
	public abstract void exitPage();

	// Abstract Method: Logout
	// This method id called if a user clicks on the RCT logout button
	// and there are still open pages
	public abstract void logout();

	// Abstract Method: canLogout()
	// This method checks if a page is managed by this user
	public abstract boolean canLogout();

}

