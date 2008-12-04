// $Id: BcAnyConsumerHandler.java,v 1.4 2003/05/08 19:31:42 thomas Exp $

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
import org.openrct.client.module.SessionModule;
import org.omg.CORBA.*;

class BcAnyConsumerHandler implements Const {

	// Constructor
	private BcAnyConsumerHandler() {

		// Nothing to do here
	}

	public static void dispatchEvent(Any eventMsg) {

		if ((eventMsg.type()).equivalent(RCT.BCMessageHelper.type())) {

			// Extract any eventMsg
			RCT.BCMessage bcMsg;
			bcMsg = RCT.BCMessageHelper.extract(eventMsg);

			// Display message in bc text area gui
			SessionModule.displayMsg(bcMsg.base_msg.user_alias, bcMsg.data);

			// Check message type and activate action
			switch (bcMsg.base_msg.type) {

			case BC_MSG_USER_ONLINE:
				try {

					SessionModule.updateUserOnlineStatus(
							bcMsg.base_msg.user_alias, BC_MSG_USER_ONLINE);
				} catch (Exception e) {

					System.out.println("EXCEPTION: BC_MSG_USER_ONLINE "
							+ e.getMessage());
				}

				break;

			case BC_MSG_USER_OFFLINE:
				try {

					SessionModule.updateUserOnlineStatus(
							bcMsg.base_msg.user_alias, BC_MSG_USER_OFFLINE);
				} catch (Exception e) {

					System.out.println("EXCEPTION: BC_MSG_USER_OFFLINE "
							+ e.getMessage());
				}

				break;

			case BC_MSG:
				// Nothing to do here
				break;

			default:

				System.out.println("ERROR: Wrong BC Mesage Type!");
			}
		} else {

			System.out
					.println("ERROR: Did not recognize the message type in BC event channel!");
		}
	}
}

