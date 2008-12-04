// $Id: TeamModule.java,v 1.4 2003/05/08 19:37:23 thomas Exp $

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

// Team Module
package org.openrct.client.module;

import org.openrct.client.Const;
import org.openrct.client.gui.PageTeamGui;
import org.openrct.client.util.ActionTimerRingBell;

public class TeamModule implements Const {

	// Access to the team server
	private static RCT.TeamServer teamServer_ = null;

	// Notification: ring bell
	private static ActionTimerRingBell actionTimerRingBell_;

	// Constructor
	private TeamModule() {

		// Nothing to do here
	}

	// Init: Getting Team Server Ref.
	public static void init(org.omg.CosNaming.NamingContext nc) {

		try {

			org.omg.CORBA.Object obj;

			org.omg.CosNaming.NameComponent[] objectName = new org.omg.CosNaming.NameComponent[2];
			objectName[0] = new org.omg.CosNaming.NameComponent(RCT_ID,
					RCT_KIND);
			objectName[1] = new org.omg.CosNaming.NameComponent(TEAM_ID,
					TEAM_KIND);

			obj = nc.resolve(objectName);

			teamServer_ = RCT.TeamServerHelper.narrow(obj);
		} catch (Exception e) {

			e.printStackTrace();
		}

		// Inint the ActionTimeRingBell
		actionTimerRingBell_ = new ActionTimerRingBell(ConfigModule
				.getNumber(CONF_ACTION_TIMER_SLEEP));
		actionTimerRingBell_.startWorking();
	}

	// Dispatch Font Settings Change
	public static void dispatchFontChange() {

		PageTeamGui pages[] = PageModule.getAllTeamPages();

		for (int i = 0; (i < pages.length) && (pages[i] != null); i++) {

			pages[i].dispatchFontChange();
		}
	}

	// Method:
	// For team events, we can ring the bell
	public static void ringBell(int mode) {

		// If the client configuration for the
		// team bell ring event is set to true
		// we ring the system bell
		if (ConfigModule.can(CONF_TEAM_RING_BELL)) {

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

	// IDL Wrapper Method:
	public static void getTeamsFromUserAndClass(String userId1, String userId2,
			String classId, RCT.TeamSeqHolder teamSeq) {

		teamServer_.get_teams_from_user_and_class(userId1, userId2, classId,
				teamSeq);
	}

	// IDL Wrapper Method:
	public static void getTeamsFromClassName(String className,
			RCT.TeamSeqHolder teamSeq) {

		teamServer_.get_teams_from_class_name(className, teamSeq);
	}

	// IDL Wrapper Method:
	public static void getTeamsUserIsActiveIn(String userId,
			RCT.TeamSeqHolder teamSeq) {

		teamServer_.get_teams_user_is_active_in(userId, teamSeq);
	}

	// IDL Wrapper Method:
	public static void getAllteams(RCT.TeamSeqHolder teamSeq) {

		teamServer_.get_all_teams(teamSeq);
	}

	// IDL Wrapper Method:
	public static boolean joinTeam(String className, String teamName,
			String userId, String userAlias) {

		return teamServer_.join_team(className, teamName, userId, userAlias);
	}

	// IDL Wrapper Method:
	public static void exitTeam(String className, String teamName,
			String userId, String userAlias) {

		teamServer_.exit_team(className, teamName, userId, userAlias);
	}

	// IDL Wrapper Method:
	public static boolean isUserActiveInTeam(String className, String teamName,
			String userId, String userAlias) {

		return teamServer_.is_user_active_in_team(className, teamName, userId,
				userAlias);
	}

	// IDL Wrapper Method:
	public static void getChatArchiveTimeRange(String className,
			String teamName, String fromDate, String toDate,
			RCT.ChatMsgHistSeqHolder chatMsgHistSeq)
			throws RCT.TeamServerPackage.DataSelectionExceedsLimit {

		teamServer_.get_chat_archive_time_range(className, teamName, fromDate,
				toDate, chatMsgHistSeq);
	}

	// IDL Wrapper Method:
	public static void getSoundArchiveTimeRange(String className,
			String teamName, String fromDate, String toDate,
			RCT.SoundMsgHistSeqHolder sndMsgHistSeq)
			throws RCT.TeamServerPackage.DataSelectionExceedsLimit {

		teamServer_.get_sound_archive_time_range(className, teamName, fromDate,
				toDate, sndMsgHistSeq);
	}

	// IDL Wrapper Method:
	public static void getTextpadArchiveTimeRange(String className,
			String teamName, String fromDate, String toDate,
			RCT.TextpadMsgHistSeqHolder textpadMsgHistSeq)
			throws RCT.TeamServerPackage.DataSelectionExceedsLimit {

		teamServer_.get_textpad_archive_time_range(className, teamName,
				fromDate, toDate, textpadMsgHistSeq);
	}

	// IDL Wrapper Method:
	public static void getFtpArchiveTimeRange(String className,
			String teamName, String fromDate, String toDate,
			RCT.FtpMsgHistSeqHolder ftpMsgHistSeq)
			throws RCT.TeamServerPackage.DataSelectionExceedsLimit {

		teamServer_.get_ftp_archive_time_range(className, teamName, fromDate,
				toDate, ftpMsgHistSeq);
	}

	// IDL Wrapper Method:
	public static void getChatArchiveLastN(String className, String teamName,
			int nMsg, RCT.ChatMsgHistSeqHolder chatMsgHistSeq)
			throws RCT.TeamServerPackage.DataSelectionExceedsLimit {

		teamServer_.get_chat_archive_last_n(className, teamName, nMsg,
				chatMsgHistSeq);
	}

	// IDL Wrapper Method:
	public static void getSoundArchiveLastN(String className, String teamName,
			int nSnd, RCT.SoundMsgHistSeqHolder sndMsgHistSeq)
			throws RCT.TeamServerPackage.DataSelectionExceedsLimit {

		teamServer_.get_sound_archive_last_n(className, teamName, nSnd,
				sndMsgHistSeq);
	}

	// IDL Wrapper Method:
	public static void getTextpadArchiveLastN(String className,
			String teamName, int nTextpad,
			RCT.TextpadMsgHistSeqHolder textpadMsgHistSeq)
			throws RCT.TeamServerPackage.DataSelectionExceedsLimit {

		teamServer_.get_textpad_archive_last_n(className, teamName, nTextpad,
				textpadMsgHistSeq);
	}

	// IDL Wrapper Method:
	public static void getFtpArchiveLastN(String className, String teamName,
			int nFtpInfo, RCT.FtpMsgHistSeqHolder ftpMsgHistSeq)
			throws RCT.TeamServerPackage.DataSelectionExceedsLimit {

		teamServer_.get_ftp_archive_last_n(className, teamName, nFtpInfo,
				ftpMsgHistSeq);
	}

	// IDL Wrapper Method:
	public static String getStatus() {

		return teamServer_.get_status();
	}
}

