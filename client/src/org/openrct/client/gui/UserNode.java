// $Id: UserNode.java,v 1.3 2003/05/08 20:09:03 thomas Exp $

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

// User node definition
public class UserNode extends CustomNode implements Const {

	private String firstName_;

	private String lastName_;

	private String permission_;

	// Constructor
	public UserNode(String userAlias, boolean onlineStatus) {

		super(userAlias, onlineStatus);
		firstName_ = TYPE_UNDEF;
		lastName_ = TYPE_UNDEF;
		permission_ = TYPE_UNDEF;
	}

	// Constructor
	public UserNode(String userAlias, boolean onlineStatus, String firstName,
			String lastName, String permission) {

		super(userAlias, onlineStatus);
		firstName_ = firstName;
		lastName_ = lastName;

		// Setting the permission to
		// User, Manager, or Admin
		if (permission.equals(USER)) {

			permission_ = TYPE_USER;
		} else if (permission.equals(MANAGER)) {

			permission_ = TYPE_MANAGER;
		} else if (permission.equals(ADMIN)) {

			permission_ = TYPE_ADMIN;
		} else {

			permission_ = TYPE_UNDEF;
			System.err.println("ERROR: Wrong user permission!");
		}
	}

	public String toString() {

		if (nodeObjectStatus) {

			return STATUS_ON_HEAD_USER + nodeObjectValue + STATUS_ON_TAIL;
		} else {

			return STATUS_OFF_HEAD_USER + nodeObjectValue + STATUS_OFF_TAIL;
		}
	}

	public String getFirstName() {

		return firstName_;
	}

	public String getLastName() {

		return lastName_;
	}

	public String getPermission() {

		return permission_;
	}
}

