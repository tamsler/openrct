// $Id: SessionModule.java,v 1.4 2003/05/08 19:37:23 thomas Exp $

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

// Session Module
package org.openrct.client.module;

import org.openrct.client.Const;
import org.openrct.client.gui.SessionGui;
import org.openrct.client.corba.BcAnyPushConsumer;
import org.openrct.client.corba.BcAnyPullConsumer;
import org.openrct.client.corba.MsgAnyPushConsumer;
import org.openrct.client.corba.MsgAnyPullConsumer;
import org.openrct.client.gui.PageGui;
import java.io.*;
import org.omg.CosNotifyChannelAdmin.*;
import java.awt.*;
import javax.swing.*;

public class SessionModule implements Const {

	// Access to broadcast channel
	private static EventChannel bcEventChannel_ = null;

	// Access to user event channel
	private static EventChannel userEventChannel_ = null;

	// Access to the session server
	private static RCT.SessionServer sessionServer_ = null;

	// The Session GUI
	private static SessionGui sessionGui_ = null;

	private static JFrame sessionFrame_ = null;

	// BC Any Push Consumer
	private static BcAnyPushConsumer bcAnyPushConsumer_ = null;

	// BC Any Pull Consumer
	private static BcAnyPullConsumer bcAnyPullConsumer_ = null;

	// MSG Any Push Consumer
	private static MsgAnyPushConsumer msgAnyPushConsumer_ = null;

	// MSG Any Pull Consuer
	private static MsgAnyPullConsumer msgAnyPullConsumer_ = null;

	// Constructor
	private SessionModule() {

		// Nothing to do here
	}

	// Init: Getting Session Server Ref.
	public static void init(org.omg.CosNaming.NamingContext nc) {

		try {
			org.omg.CORBA.Object obj;

			org.omg.CosNaming.NameComponent[] objectName = new org.omg.CosNaming.NameComponent[2];
			objectName[0] = new org.omg.CosNaming.NameComponent(RCT_ID,
					RCT_KIND);
			objectName[1] = new org.omg.CosNaming.NameComponent(SESSION_ID,
					SESSION_KIND);

			obj = nc.resolve(objectName);

			sessionServer_ = RCT.SessionServerHelper.narrow(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Init the broadcast event channel
	public static void initBcEventChannel(org.omg.CosNaming.NamingContext nc) {

		try {
			org.omg.CORBA.Object obj;

			org.omg.CosNaming.NameComponent[] objectName = new org.omg.CosNaming.NameComponent[1];
			objectName[0] = new org.omg.CosNaming.NameComponent(BC_CHANNEL_ID,
					BC_CHANNEL_KIND);

			obj = nc.resolve(objectName);

			SessionModule.bcEventChannel_ = EventChannelHelper.narrow(obj);

			if (null == bcEventChannel_) {

				System.err
						.println("\nERROR: Was not able to retrieve BC Event Channel!\n");
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	// Init the user event channel
	public static void initUserEventChannel(org.omg.CosNaming.NamingContext nc) {

		try {
			org.omg.CORBA.Object obj;

			org.omg.CosNaming.NameComponent[] objectName = new org.omg.CosNaming.NameComponent[2];
			objectName[0] = new org.omg.CosNaming.NameComponent(RCT_ID,
					RCT_KIND);
			// The user id is used for both the context ID and KIND field
			objectName[1] = new org.omg.CosNaming.NameComponent(UserModule
					.getId(), UserModule.getId());

			obj = nc.resolve(objectName);

			SessionModule.userEventChannel_ = EventChannelHelper.narrow(obj);

			if (null == userEventChannel_) {

				System.err
						.println("\nERROR: Was not able to retrieve User Event Channel!\n");
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	// Init BC Any Push Consumer
	public static void initBcAnyPushConsumer() {

		bcAnyPushConsumer_ = new BcAnyPushConsumer(bcEventChannel_);
	}

	// Init BC Any Pull Consumer
	public static void initBcAnyPullConsumer() {

		bcAnyPullConsumer_ = new BcAnyPullConsumer(bcEventChannel_);
	}

	// Init MSG Any Push Consumer
	public static void initMsgAnyPushConsumer() {

		msgAnyPushConsumer_ = new MsgAnyPushConsumer(userEventChannel_);
	}

	// Init MSG Any Pull Consuer
	public static void initMsgAnyPullConsumer() {

		msgAnyPullConsumer_ = new MsgAnyPullConsumer(userEventChannel_);
	}

	// Start the session gui
	public static void startSessionGui() {

		sessionGui_ = new SessionGui();
		sessionFrame_ = sessionGui_.getFrame();

		// Center the window
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = sessionGui_.getSize();

		if (frameSize.height > screenSize.height) {

			frameSize.height = screenSize.height;
		}

		if (frameSize.width > screenSize.width) {

			frameSize.width = screenSize.width;
		}

		sessionGui_.setLocation((screenSize.width - frameSize.width) / 2,
				(screenSize.height - frameSize.height) / 2);

		sessionGui_.pack();
		sessionGui_.setVisible(true);
	}

	// Method:
	// Access the SessionGui's frame
	public static JFrame getFrame() {

		return sessionFrame_;
	}

	// Method:
	// Change connection type/speed
	public static void changeConnectionSpeed(int speed) {

		sessionGui_.changeConnectionSpeed(speed);
	}

	// This method is called when a user clicks on the Logout button
	// or attempts to close the application window, or selects exit
	// from the file menu
	public static void logout() {

		// Let the Page Module know about the Logout
		// The Page Module will call the exit method for each page
		// We also check the return value in case there are open group
		// pages that are owned by this user
		if (PageModule.logout()) {

			// Let the server know about the logout
			AuthModule.logout(BC_MSG_USER_OFFLINE, UserModule.getAlias(),
					UserModule.getPassword(), LangModule.i18n
							.getString("SessionEventLogout"), RCT_VERSION);

			// Test Config if we need to delete the data dir
			if (!ConfigModule.can(CONF_SYS_KEEP_DATA_DIR)) {

				FileModule.deleteDirectory(new File(FileModule
						.getTempDataDirName()));
			}

			// Closing application
			System.exit(0);
		} else {

			// In this case, there are open group pages
			JOptionPane.showMessageDialog(null, LangModule.i18n
					.getString("SessionDialog1_1")
					+ "\n" + LangModule.i18n.getString("SessionDialog1_2"),
					LangModule.i18n.getString("SessionDialog2"),
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	// Gui Wrapper Method:
	public static void displayMsg(String userAlias, String bcMsg) {

		SessionGui.displayMsg("[ " + userAlias + " ] " + bcMsg);
	}

	// Gui Wrapper Method:
	// This method displays messages in the Broadcast test area
	public static void displayModuleMsg(String msg) {

		SessionGui.displayMsg(msg);
	}

	// Gui Wrapper Method:
	public static synchronized void updateUserOnlineStatus(String userAlias,
			int msgType) {

		SessionGui.updateUserOnlineStatus(userAlias, msgType);
	}

	// Gui Wrapper Method:
	public static void displayJoinPage(PageGui page, String className,
			String assemblyName) {

		sessionGui_.displayJoinPage(page, className, assemblyName);
	}

	// Gui Wrapper Method:
	public static void displayJoinTeamEvent(String className, String teamName,
			String userAlias) {

		SessionGui.displayJoinOrExitTeamEvent(className, teamName, userAlias,
				TEAM_JOIN_MSG);
	}

	// Gui Wrapper Method:
	public static void displayJoinGroupEvent(String className,
			String groupName, String userAlias) {

		SessionGui.displayJoinGroupEvent(className, groupName, userAlias);
	}

	// Gui Wrapper Method:
	public static void displayMemberExitsGroupEvent(String className,
			String groupName, String userAlias) {

		SessionGui
				.displayMemberExitsGroupEvent(className, groupName, userAlias);
	}

	// Gui Wrapper Method:
	public static void displayRemoveGroupEvent(String className,
			String groupName) {

		SessionGui.displayRemoveGroupEvent(className, groupName);
	}

	// Gui Wrapper Method:
	public static void displayExitTeamEvent(String className, String teamName,
			String userAlias) {

		SessionGui.displayJoinOrExitTeamEvent(className, teamName, userAlias,
				TEAM_EXIT_MSG);
	}

	// Gui Wrapper Method:
	public static void displayCreateGroupEvent(String className,
			String groupName, String userAlias) {

		SessionGui.displayCreateGroupEvent(className, groupName, userAlias);
	}

	// Gui Wrapper Method:
	public static void displayJoinGroupRequestDenied(String className,
			String groupName) {

		sessionGui_.displayJoinGroupRequestDenied(className, groupName);
	}

	// Gui Wrapper Method:
	public static void displayJoinGroupRequestGranted(String className,
			String groupName) {

		sessionGui_.displayJoinGroupRequestGranted(className, groupName);
	}

	// Gui Wrapper Method:
	public static void displayNotifyNewManager(String className,
			String groupName, String userAlias) {

		sessionGui_.displayNotifyNewManager(className, groupName, userAlias);
	}

	// Gui Wrapper Method:
	public static void displayRemoveSelectedPage() {
		sessionGui_.displayRemoveSelectedPage();
	}

	// Gui Wrapper Methdo:
	public static void displayRemovePageAtTab(String tabName) {

		sessionGui_.displayRemovePageAtTab(tabName);
	}

	// Gui Wrapper Method:
	public static void displayDisablePages() {

		sessionGui_.displayDisablePages();
	}

	// Gui Wrapper Method:
	public static void displayServerCannotBeReached() {

		sessionGui_.displayServerCannotBeReached();
	}

	// IDL Wrapper Method:
	public static String getStatus() {

		return sessionServer_.get_status();
	}
}

