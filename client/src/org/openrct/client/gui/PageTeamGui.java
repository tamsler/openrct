// $Id: PageTeamGui.java,v 1.8 2003/06/13 17:21:17 thomas Exp $

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
import org.openrct.client.module.UserModule;
import org.openrct.client.module.TeamModule;
import org.openrct.client.module.PageModule;
import javax.swing.*;

public class PageTeamGui extends PageGui implements Const {

	// Class Vars
	// ----------
	private String className_;

	private String teamName_;

	//    private TDPageGui tdModulePage;
	private ArchivePageGui archiveModulePage;

	// Constructor
	public PageTeamGui(String className, String teamName) throws Exception {

		super(className, teamName, PAGE_TEAM);

		className_ = className;
		teamName_ = teamName;

		tdModulePage = new TDPageGui(className_, teamName_, PAGE_TEAM);
		archiveModulePage = new ArchivePageGui(className_, teamName_);

		moduleTabbedPane.add(tdModulePage, "Threaded Discussion");
		moduleTabbedPane.add(archiveModulePage, LangModule.i18n
				.getString("ChatPageTeamTPLabel"));

		// Building the userListModel
		DefaultListModel userListModel = new DefaultListModel();
		RCT.UserSeqHolder userSeq = new RCT.UserSeqHolder();

		UserModule.getOnlineUsersFromTeamName(className_, teamName_, userSeq);

		for (int i = 0; i < userSeq.value.length; i++) {

			userListModel.addElement(userSeq.value[i].alias);
		}

		userList.setModel(userListModel);
	}

	// Abstract Methods:
	// -----------------

	// Abstract Method: Get the class name
	public String getClassName() {

		return className_;
	}

	// Abstract Method: Get assebmly name
	// Assembly name is either the team or group name
	public String getAssemblyName() {

		return teamName_;
	}

	// Abstract Method: Exit
	public void exitPage() {

		TeamModule.exitTeam(getClassName(), getAssemblyName(), UserModule
				.getId(), UserModule.getAlias());

		// In case an archive page is open, we want
		// to make sure that we remove the page
		PageModule.removeArchivePage(className_, teamName_);
	}

	// Abstract Method: Logout
	public void logout() {

		TeamModule.exitTeam(getClassName(), getAssemblyName(), UserModule
				.getId(), UserModule.getAlias());
	}

	// Abstarct Method: canLogout
	public boolean canLogout() {

		// There no restrictions to a team logout,
		// thus we just return true.
		return true;
	}

	// Methods:
	// --------

	// Method:
	// Change Font for text area
	public void dispatchFontChange() {

		archiveModulePage.dispatchFontChange();
	}
}

