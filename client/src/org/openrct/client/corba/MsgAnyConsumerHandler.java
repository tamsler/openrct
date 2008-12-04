// $Id: MsgAnyConsumerHandler.java,v 1.14 2003/05/20 17:25:23 thomas Exp $

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

package org.openrct.client.corba;

import org.openrct.client.Const;
import org.openrct.client.module.TextpadModule;
import org.openrct.client.module.ControlModule;
import org.openrct.client.module.ChatModule;
import org.openrct.client.module.SoundModule;
import org.openrct.client.module.UrlModule;
import org.openrct.client.module.FtpModule;
import org.openrct.client.module.SessionModule;
import org.openrct.client.module.GroupModule;
import org.openrct.client.module.PageModule;
import org.openrct.client.module.UserModule;
import org.openrct.client.module.TeamModule;
import org.openrct.client.module.TDModule;
import org.openrct.client.module.CourseContentModule;
import org.omg.CORBA.*;

class MsgAnyConsumerHandler implements Const {

	// Constructor
	private MsgAnyConsumerHandler() {

		// Nothing to do here
	}

	public static void dispatchEvent(Any eventMsg) {

		// Dealing with Textpad Messages
		if ((eventMsg.type()).equivalent(RCT.TextpadMessageHelper.type())) {

			// Extract any eventMsg
			RCT.TextpadMessage textpadMsg;
			textpadMsg = RCT.TextpadMessageHelper.extract(eventMsg);

			switch (textpadMsg.base_msg.type) {

			case TEXTPAD_TEAM_CREATE:

				TextpadModule.displayCreate(textpadMsg.base_msg.class_name,
						textpadMsg.base_msg.team_name,
						textpadMsg.base_msg.user_alias, textpadMsg.name,
						textpadMsg.ref, textpadMsg.id);
				break;

			case TEXTPAD_GROUP_CREATE:

				TextpadModule.displayCreate(textpadMsg.base_msg.class_name,
						textpadMsg.base_msg.group_name,
						textpadMsg.base_msg.user_alias, textpadMsg.name,
						textpadMsg.ref, textpadMsg.id);
				break;

			case TEXTPAD_TEAM_EDIT:

				TextpadModule.displayEdit(textpadMsg.base_msg.class_name,
						textpadMsg.base_msg.team_name,
						textpadMsg.base_msg.user_alias, textpadMsg.name,
						textpadMsg.ref, textpadMsg.id, textpadMsg.text);
				break;
			case TEXTPAD_GROUP_EDIT:

				TextpadModule.displayEdit(textpadMsg.base_msg.class_name,
						textpadMsg.base_msg.group_name,
						textpadMsg.base_msg.user_alias, textpadMsg.name,
						textpadMsg.ref, textpadMsg.id, textpadMsg.text);
				break;

			case TEXTPAD_TEAM_CLOSE:

				TextpadModule.displayClose(textpadMsg.base_msg.class_name,
						textpadMsg.base_msg.team_name);
				break;

			case TEXTPAD_GROUP_CLOSE:

				TextpadModule.displayClose(textpadMsg.base_msg.class_name,
						textpadMsg.base_msg.group_name);
				break;

			case TEXTPAD_TEAM_REM_INS:

				TextpadModule.displayRemIns(textpadMsg.base_msg.class_name,
						textpadMsg.base_msg.team_name,
						textpadMsg.base_msg.user_alias, textpadMsg.offset,
						textpadMsg.text);
				break;

			case TEXTPAD_GROUP_REM_INS:

				TextpadModule.displayRemIns(textpadMsg.base_msg.class_name,
						textpadMsg.base_msg.group_name,
						textpadMsg.base_msg.user_alias, textpadMsg.offset,
						textpadMsg.text);
				break;

			case TEXTPAD_TEAM_INS:

				TextpadModule.displayIns(textpadMsg.base_msg.class_name,
						textpadMsg.base_msg.team_name,
						textpadMsg.base_msg.user_alias, textpadMsg.offset,
						textpadMsg.text);
				break;

			case TEXTPAD_GROUP_INS:

				TextpadModule.displayIns(textpadMsg.base_msg.class_name,
						textpadMsg.base_msg.group_name,
						textpadMsg.base_msg.user_alias, textpadMsg.offset,
						textpadMsg.text);
				break;

			case TEXTPAD_TEAM_REM:

				TextpadModule.displayRem(textpadMsg.base_msg.class_name,
						textpadMsg.base_msg.team_name,
						textpadMsg.base_msg.user_alias, textpadMsg.offset,
						textpadMsg.text);
				break;

			case TEXTPAD_GROUP_REM:

				TextpadModule.displayRem(textpadMsg.base_msg.class_name,
						textpadMsg.base_msg.group_name,
						textpadMsg.base_msg.user_alias, textpadMsg.offset,
						textpadMsg.text);
				break;

			default:

				System.err.println("ERROR: Wrong Textpad Message Type!");
			}
		}
		// Dealing with Control Messages
		else if ((eventMsg.type()).equivalent(RCT.ControlMessageHelper.type())) {

			// Extract any eventMsg
			RCT.ControlMessage controlMsg;
			controlMsg = RCT.ControlMessageHelper.extract(eventMsg);

			switch (controlMsg.base_msg.type) {

			case CONTROL_REQ_TEAM_NOTIF:

				ControlModule.displayRequestControl(
						controlMsg.base_msg.class_name,
						controlMsg.base_msg.team_name,
						controlMsg.base_msg.user_alias, controlMsg.module_name);
				break;

			case CONTROL_REQ_GROUP_NOTIF:

				ControlModule.displayRequestControl(
						controlMsg.base_msg.class_name,
						controlMsg.base_msg.group_name,
						controlMsg.base_msg.user_alias, controlMsg.module_name);
				break;

			case CONTROL_CAN_REQ_TEAM_NOTIF:

				ControlModule.displayCancelRequest(
						controlMsg.base_msg.class_name,
						controlMsg.base_msg.team_name,
						controlMsg.base_msg.user_alias, controlMsg.module_name);
				break;

			case CONTROL_CAN_REQ_GROUP_NOTIF:

				ControlModule.displayCancelRequest(
						controlMsg.base_msg.class_name,
						controlMsg.base_msg.group_name,
						controlMsg.base_msg.user_alias, controlMsg.module_name);
				break;

			case CONTROL_REL_TEAM_NOTIF:

				ControlModule.displayReleaseControl(
						controlMsg.base_msg.class_name,
						controlMsg.base_msg.team_name,
						controlMsg.base_msg.user_alias, controlMsg.module_name);
				break;

			case CONTROL_REL_GROUP_NOTIF:

				ControlModule.displayReleaseControl(
						controlMsg.base_msg.class_name,
						controlMsg.base_msg.group_name,
						controlMsg.base_msg.user_alias, controlMsg.module_name);
				break;

			case CONTROL_TAKE_TEAM_NOTIF:

				ControlModule.displayTakeControl(
						controlMsg.base_msg.class_name,
						controlMsg.base_msg.team_name,
						controlMsg.base_msg.user_alias, controlMsg.module_name);
				break;

			case CONTROL_TAKE_GROUP_NOTIF:

				ControlModule.displayTakeControl(
						controlMsg.base_msg.class_name,
						controlMsg.base_msg.group_name,
						controlMsg.base_msg.user_alias, controlMsg.module_name);
				break;

			case CONTROL_ACTIVE_TEAM_NOTIF:

				ControlModule.displayActiveControl(
						controlMsg.base_msg.class_name,
						controlMsg.base_msg.team_name,
						controlMsg.base_msg.user_alias, controlMsg.module_name);
				break;

			case CONTROL_ACTIVE_GROUP_NOTIF:

				ControlModule.displayActiveControl(
						controlMsg.base_msg.class_name,
						controlMsg.base_msg.group_name,
						controlMsg.base_msg.user_alias, controlMsg.module_name);
				break;

			case CONTROL_EXIT_TEAM_NOTIF:

				ControlModule.displayExitControl(
						controlMsg.base_msg.class_name,
						controlMsg.base_msg.team_name,
						controlMsg.base_msg.user_alias, controlMsg.module_name);
				break;

			case CONTROL_EXIT_GROUP_NOTIF:

				ControlModule.displayExitControl(
						controlMsg.base_msg.class_name,
						controlMsg.base_msg.group_name,
						controlMsg.base_msg.user_alias, controlMsg.module_name);
				break;

			default:
				System.err.println("ERROR: Wrong Control Message Type!");
			}
		}
		// Dealing with Chat Messages
		else if ((eventMsg.type()).equivalent(RCT.ChatMessageHelper.type())) {

			// Extract any eventMsg
			RCT.ChatMessage chatMsg;
			chatMsg = RCT.ChatMessageHelper.extract(eventMsg);

			switch (chatMsg.base_msg.type) {

			case CHAT_GROUP_MSG:

				ChatModule.displayChatMsg(chatMsg.base_msg.class_name,
						chatMsg.base_msg.group_name,
						chatMsg.base_msg.user_alias, chatMsg.data);
				break;

			case CHAT_TEAM_MSG:

				ChatModule.displayChatMsg(chatMsg.base_msg.class_name,
						chatMsg.base_msg.team_name,
						chatMsg.base_msg.user_alias, chatMsg.data);
				break;

			case CHAT_ARCHIVE_TEAM_MSG:

				break;

			default:

				System.err.println("ERROR: Wrong Chat Mesage Type!");
			}
		}
		// Dealing with Sound Messages
		else if ((eventMsg.type()).equivalent(RCT.SoundMessageHelper.type())) {

			// Extract any eventMsg
			RCT.SoundMessage soundMsg;
			soundMsg = RCT.SoundMessageHelper.extract(eventMsg);

			switch (soundMsg.base_msg.type) {

			case SOUND_TEAM:

				SoundModule.displaySoundMsg(soundMsg.base_msg.user_alias,
						soundMsg.base_msg.class_name,
						soundMsg.base_msg.team_name, soundMsg.info);
				break;

			case SOUND_GROUP:

				SoundModule.displaySoundMsg(soundMsg.base_msg.user_alias,
						soundMsg.base_msg.class_name,
						soundMsg.base_msg.group_name, soundMsg.info);
				break;

			default:

				System.err.println("ERROR: Wrong Sound Mesage Type!");
			}

		}
		// Dealing with Url Messages
		else if ((eventMsg.type()).equivalent(RCT.UrlMessageHelper.type())) {

			// Extract any eventMsg
			RCT.UrlMessage urlMsg;
			urlMsg = RCT.UrlMessageHelper.extract(eventMsg);

			switch (urlMsg.base_msg.type) {

			case URL_TEAM_SEND:

				UrlModule.displayUrlSendMsg(urlMsg.base_msg.user_alias,
						urlMsg.base_msg.class_name, urlMsg.base_msg.team_name,
						urlMsg.url);
				break;

			case URL_GROUP_SEND:

				UrlModule.displayUrlSendMsg(urlMsg.base_msg.user_alias,
						urlMsg.base_msg.class_name, urlMsg.base_msg.group_name,
						urlMsg.url);
				break;

			default:

				System.err.println("ERROR: Wrong Url Mesage Type!");
			}
		}
		// Dealing with Ftp Messages
		else if ((eventMsg.type()).equivalent(RCT.FtpMessageHelper.type())) {

			// Extract any eventMsg
			RCT.FtpMessage ftpMsg;
			ftpMsg = RCT.FtpMessageHelper.extract(eventMsg);

			switch (ftpMsg.base_msg.type) {

			case FTP_TEAM_UPLOAD:

				FtpModule.displayFtpUploadMsg(ftpMsg.base_msg.user_alias,
						ftpMsg.base_msg.class_name, ftpMsg.base_msg.team_name,
						ftpMsg.info);
				break;

			case FTP_GROUP_UPLOAD:

				FtpModule.displayFtpUploadMsg(ftpMsg.base_msg.user_alias,
						ftpMsg.base_msg.class_name, ftpMsg.base_msg.group_name,
						ftpMsg.info);
				break;

			default:

				System.err.println("ERROR: Wrong Ftp Mesage Type!");
			}
		}
		// Dealing with File Packet Messages
		else if ((eventMsg.type()).equivalent(RCT.FilePacketMessageHelper
				.type())) {

			RCT.FilePacketMessage filePacketMsg;
			filePacketMsg = RCT.FilePacketMessageHelper.extract(eventMsg);

			switch (filePacketMsg.base_msg.type) {

			case FTP_TEAM_DOWNLOAD:

				FtpModule.packetHandler(filePacketMsg.base_msg.user_alias,
						filePacketMsg.base_msg.class_name,
						filePacketMsg.base_msg.team_name, filePacketMsg.id,
						filePacketMsg.packet_type, filePacketMsg.packet);
				break;

			case FTP_GROUP_DOWNLOAD:

				FtpModule.packetHandler(filePacketMsg.base_msg.user_alias,
						filePacketMsg.base_msg.class_name,
						filePacketMsg.base_msg.group_name, filePacketMsg.id,
						filePacketMsg.packet_type, filePacketMsg.packet);
				break;

			case FTP_ARCHIVE_DOWNLOAD:

				FtpModule.archivePacketHandler(
						filePacketMsg.base_msg.user_alias,
						filePacketMsg.base_msg.class_name,
						filePacketMsg.base_msg.team_name, filePacketMsg.id,
						filePacketMsg.packet_type, filePacketMsg.packet);
				break;

			case SOUND_TEAM_PLAY:

				SoundModule.packetHandler(filePacketMsg.base_msg.user_alias,
						filePacketMsg.base_msg.class_name,
						filePacketMsg.base_msg.team_name, filePacketMsg.id,
						filePacketMsg.packet_type, filePacketMsg.packet);
				break;

			case SOUND_GROUP_PLAY:

				SoundModule.packetHandler(filePacketMsg.base_msg.user_alias,
						filePacketMsg.base_msg.class_name,
						filePacketMsg.base_msg.group_name, filePacketMsg.id,
						filePacketMsg.packet_type, filePacketMsg.packet);
				break;

			case SOUND_ARCHIVE_PLAY:

				SoundModule.archivePacketHandler(
						filePacketMsg.base_msg.user_alias,
						filePacketMsg.base_msg.class_name,
						filePacketMsg.base_msg.team_name, filePacketMsg.id,
						filePacketMsg.packet_type, filePacketMsg.packet);
				break;

			case CC_DOWNLOAD:

				CourseContentModule.packetHandler(
						filePacketMsg.base_msg.user_alias,
						filePacketMsg.base_msg.class_name, filePacketMsg.id,
						filePacketMsg.packet_type, filePacketMsg.packet);
				break;

			default:

				System.err.println("ERROR: Wrong File Packet Message Type!");
			}
		}
		// Dealing with Regular Messages
		else if ((eventMsg.type()).equivalent(RCT.MessageHelper.type())) {

			// Extract any eventMsg
			RCT.Message msg;
			msg = RCT.MessageHelper.extract(eventMsg);

			switch (msg.type) {

			case GROUP_CREATED_MSG:

				SessionModule.displayCreateGroupEvent(msg.class_name,
						msg.group_name, msg.user_alias);
				break;

			case GROUP_JOIN_REQ_MSG:

				GroupModule.displayJoinRequest(msg.class_name, msg.group_name,
						msg.user_alias);
				break;

			case GROUP_JOIN_REQ_MSG_GRANTED:

				SessionModule.displayJoinGroupRequestGranted(msg.class_name,
						msg.group_name);
				PageModule.displayJoinPage(msg.class_name, msg.group_name,
						CLASS_GROUP_TYPE);
				break;

			case GROUP_JOIN_REQ_MSG_DENIED:

				SessionModule.displayJoinGroupRequestDenied(msg.class_name,
						msg.group_name);

				break;

			case GROUP_JOIN_MEMBER_MSG:

				// Display the user in the PageTeam UserActive list
				PageModule.displayAssemblyMember(msg.class_name,
						msg.group_name, msg.user_alias);

				// Change add user to class view w.r.t. class and group
				SessionModule.displayJoinGroupEvent(msg.class_name,
						msg.group_name, msg.user_alias);
				break;

			case GROUP_CHOOSE_NEW_MGR:

				GroupModule.chooseNewManager(msg.class_name, msg.group_name);
				break;

			case GROUP_MEMBER_EXITS:

				// Don't send message to user who exited
				if (!msg.user_alias.equals(UserModule.getAlias())) {

					// Remove the user in the Page Userlist
					PageModule.removeAssemblyMember(msg.class_name,
							msg.group_name, msg.user_alias);
				}

				SessionModule.displayMemberExitsGroupEvent(msg.class_name,
						msg.group_name, msg.user_alias);
				break;

			case GROUP_REMOVED:

				SessionModule.displayRemoveGroupEvent(msg.class_name,
						msg.group_name);
				break;

			case GROUP_NEW_MANAGER:

				SessionModule.displayNotifyNewManager(msg.class_name,
						msg.group_name, msg.user_alias);

				PageModule.displayAssemblyMgrAtTop(msg.class_name,
						msg.group_name, msg.user_alias);
				break;

			case TEAM_JOIN_MSG:

				// Ring the bell for notification
				TeamModule.ringBell(RING_BELL_NOW);

				// Display the user in the ChatPageTeam UserActive list
				PageModule.displayAssemblyMember(msg.class_name, msg.team_name,
						msg.user_alias);

				// Change status in the ClassView
				SessionModule.displayJoinTeamEvent(msg.class_name,
						msg.team_name, msg.user_alias);
				break;

			case TEAM_EXIT_MSG:

				// Don't send message to user who exited
				if (!msg.user_alias.equals(UserModule.getAlias())) {

					// Remove the user in the ChatPage Userlist
					PageModule.removeAssemblyMember(msg.class_name,
							msg.team_name, msg.user_alias);
				}

				// Change status in the ClassView
				SessionModule.displayExitTeamEvent(msg.class_name,
						msg.team_name, msg.user_alias);
				break;

			default:
				System.err
						.println("ERROR: (1) Message Type Not Implemented Yet!");
				System.err.println("ERROR: (2) " + msg.type);
			}
		}
		// Dealing with Threaded Discussion Messages
		else if ((eventMsg.type()).equivalent(RCT.TDMessageHelper.type())) {

			// Extract any eventMsg
			RCT.TDMessage tdMsg;
			tdMsg = RCT.TDMessageHelper.extract(eventMsg);

			TDModule.displayPostMsg(tdMsg.base_msg.user_alias,
					tdMsg.base_msg.class_name, tdMsg.base_msg.team_name,
					tdMsg.post_id, tdMsg.parent_id, tdMsg.subject, tdMsg.type,
					tdMsg.date);
		} else {

			System.err
					.println("ERROR: Did not recognize the message type in User event channel!");
		}
	}
}

