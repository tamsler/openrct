// $Id: WhiteboardPageGui.java,v 1.3 2003/05/08 20:09:03 thomas Exp $

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

public class WhiteboardPageGui extends ControlledModulePageGui {

	public WhiteboardPageGui(String className, String assemblyName,
			int pageType, boolean isManaged) {

		super(className, assemblyName, MODULE_WHITEBOARD, pageType, isManaged);
	}

	// Abstract Methods Implementation:
	// --------------------------------

	// Abstract Method:
	public void actionActivate() {

	}

	// Abstract Method:
	public void actionDeactivate() {

	}

	// Abstract Method:
	public void actionReleaseControl(boolean userIsLastInQueue) {

	}

	// Abstract Method:
	public void dispatchExitRequest() {

		// Nothing to do yet
	}
}

