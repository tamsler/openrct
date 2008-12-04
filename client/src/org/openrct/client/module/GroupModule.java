// $Id: GroupModule.java,v 1.5 2003/05/08 19:37:23 thomas Exp $

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

// Group Module
package org.openrct.client.module;

import org.openrct.client.Const;
import org.openrct.client.gui.GroupGui;
import org.openrct.client.util.ActionTimerRingBell;

public class GroupModule implements Const {

	// Access to the group server
	private static RCT.GroupServer groupServer_ = null;

	// The Group Gui
	private static GroupGui groupGui_ = new GroupGui();

	// Notification: ring bell
	private static ActionTimerRingBell actionTimerRingBell_;

	// Constructor
	private GroupModule() {

		// Nothing to do here
	}

	// Init: Getting Group Server Ref.
	public static void init(org.omg.CosNaming.NamingContext nc) {

		try {
			org.omg.CORBA.Object obj;

			org.omg.CosNaming.NameComponent[] objectName = new org.omg.CosNaming.NameComponent[2];
			objectName[0] = new org.omg.CosNaming.NameComponent(RCT_ID,
					RCT_KIND);
			objectName[1] = new org.omg.CosNaming.NameComponent(GROUP_ID,
					GROUP_KIND);

			obj = nc.resolve(objectName);

			groupServer_ = RCT.GroupServerHelper.narrow(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Inint the ActionTimeRingBell
		actionTimerRingBell_ = new ActionTimerRingBell(ConfigModule
				.getNumber(CONF_ACTION_TIMER_SLEEP));
		actionTimerRingBell_.startWorking();
	}

	// Method:
	// For team events, we can ring the bell
	public static void ringBell(int mode) {

		// If the client configuration for the
		// group bell ring event is set to true
		// we ring the system bell
		if (ConfigModule.can(CONF_GROUP_RING_BELL)) {

			switch (mode) {

			case RING_BELL_NOW:

				java.awt.Toolkit.getDefaultToolkit().beep();
				break;

			case RING_BELL_TIMED:

				actionTimerRingBell_.performAction();
				break;

			default:
			// Don't do anything
			}
		}
	}

	// Gui Wrapper Methods:
	// --------------------

	// Gui Wrapper Method:
	public static void displayJoinRequest(String className, String groupName,
			String userAlias) {

		// Ring the bell for notification
		ringBell(RING_BELL_NOW);

		groupGui_.displayJoinRequestDialog(className, groupName, userAlias);
	}

	// Gui Wrapper Method:
	public static void chooseNewManager(String className, String groupName) {

		groupGui_.displayChooseNewManager(className, groupName);
	}

	// IDL Wrapper Methods:
	// --------------------

	// IDL Wrapper Method:
	public static void getGroupsFromUserAndClass(String userId, String classId,
			RCT.GroupSeqHolder groupSeq) {

		groupServer_.get_groups_from_user_and_class(userId, classId, groupSeq);
	}

	// IDL Wrapper Method:
	public static void getGroupsFromClass(String classId,
			RCT.GroupSeqHolder groupSeq) {

		groupServer_.get_groups_from_class(classId, groupSeq);
	}

	// IDL Wrapper Method:
	public static void getGroupsFromClassName(String className,
			RCT.GroupSeqHolder groupSeq) {

		groupServer_.get_groups_from_class_name(className, groupSeq);
	}

	// IDL Wrapper Method:
	public static void getAllGroups(RCT.GroupSeqHolder groupSeq) {

		groupServer_.get_all_groups(groupSeq);
	}

	// IDL Wrapper Method:
	public static boolean createGroup(String className, String groupName,
			String userId, String userAlias) {

		return groupServer_.create_group(className, groupName, userId,
				userAlias);
	}

	// IDL Wrapper Method:
	public static void joinGroupRequest(String className, String groupName,
			String userId, String userAlias) {

		groupServer_
				.join_group_request(className, groupName, userId, userAlias);
	}

	// IDL Wrapper Method:
	public static void joinGroupRequestReply(String className,
			String groupName, String userId, String userAlias,
			String requestorAlias, boolean granted) {

		groupServer_.join_group_request_reply(className, groupName, userId,
				userAlias, requestorAlias, granted);
	}

	// IDL Wrapper Method:
	public static void exitGroup(String className, String groupName,
			String userId, String userAlias) {

		groupServer_
				.exit_group(className, groupName, userId, userAlias, NORMAL);
	}

	// IDL Wrapper Method:
	public static void setNewManager(String className, String groupName,
			String userId, String userAlias, String newManagerAlias) {

		groupServer_.set_new_manager(className, groupName, userId, userAlias,
				newManagerAlias);
	}

	// IDL Wrapper Method:
	public static boolean isUserInGroup(String className, String groupName,
			String userAlias) {

		return groupServer_.is_user_in_group(className, groupName, userAlias);
	}

	// IDL Wrapper Method:
	public static String getManagerId(String className, String groupName) {

		return groupServer_.get_manager_id(className, groupName);
	}

	// IDL Wrapper Method:
	public static void getChatArchiveLastN(String className, String groupName,
			int nMsg, RCT.ChatMsgHistSeqHolder chatMsgHistSeq)
			throws RCT.GroupServerPackage.DataSelectionExceedsLimit {

		groupServer_.get_chat_archive_last_n(className, groupName, nMsg,
				chatMsgHistSeq);
	}

	// IDL Wrapper Method:
	public static void getSoundArchiveLastN(String className, String groupName,
			int nMsg, RCT.SoundMsgHistSeqHolder soundMsgHistSeq)
			throws RCT.GroupServerPackage.DataSelectionExceedsLimit {

		groupServer_.get_sound_archive_last_n(className, groupName, nMsg,
				soundMsgHistSeq);
	}

	// IDL Wrapper Method:
	public static void getFtpArchiveLastN(String className, String groupName,
			int nMsg, RCT.FtpMsgHistSeqHolder ftpMsgHistSeq)
			throws RCT.GroupServerPackage.DataSelectionExceedsLimit {

		groupServer_.get_ftp_archive_last_n(className, groupName, nMsg,
				ftpMsgHistSeq);
	}

	// IDL Wrapper Method:
	public static String getStatus() {

		return groupServer_.get_status();
	}
}

