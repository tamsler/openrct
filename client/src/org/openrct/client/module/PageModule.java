// $Id: PageModule.java,v 1.6 2003/05/08 19:37:23 thomas Exp $

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

// Page Module
package org.openrct.client.module;

import org.openrct.client.Const;
import org.openrct.client.gui.PageGui;
import org.openrct.client.gui.PageTeamGui;
import org.openrct.client.gui.PageGroupGui;
import java.util.*;

public class PageModule implements Const {

	// Hash Table that keeps track of all the pages
	private static Hashtable pageTable_ = new Hashtable();

	// Hash Table that keeps track of archived pages
	private static Hashtable archivePageTable_ = new Hashtable();

	// Constructor
	private PageModule() {

		// Nothing to do here
	}

	// Method: Logout
	// This method will deal with the logout process
	// related to the Pages.
	// Return true if there are NO open group pages,
	// or group pages are not owned by user.
	// Return false if there are open group pages and
	// user is the owner
	public static boolean logout() {

		// Call the exitPage method for each Page in the
		// in the PageMap

		// This flag is used to determine if there
		// are managed group pages.
		boolean hasNoManagedPages = true;

		// Check if the map is empty
		if (pageTable_.isEmpty()) {
			// Nothing to do in this case
			// since there are no Pages
		} else {

			// Get all the pages' tab names
			Enumeration enum = pageTable_.keys();

			// Iterate through the pages
			while (enum.hasMoreElements()) {

				String key = (String) enum.nextElement();

				// Get a page
				PageGui page = (PageGui) pageTable_.get(key);

				// Check if we can logout of the page.
				// We cannot logout if it's a group page
				// and this user manages the group page
				// and there are more than one user active in the group
				if (page.canLogout()) {

					page.closePage();
				} else {

					// In this case, we have to set the flag
					hasNoManagedPages = false;
					// At this point we don't return since we want
					// to continue to logout of pages so that
					// we are left with managed pages
				}
			}
		}
		return hasNoManagedPages;
	}

	// Method: This method is called if we want to
	// join a team or group page.
	public static void displayJoinPage(String className, String assemblyName,
			int typePage) {

		try {

			if (CLASS_TEAM_TYPE == typePage) {

				// First notify the team module
				if (TeamModule.joinTeam(className, assemblyName, UserModule
						.getId(), UserModule.getAlias())) {

					PageTeamGui pageTeam = new PageTeamGui(className,
							assemblyName);

					pageTable_.put(className + assemblyName, pageTeam);

					SessionModule.displayJoinPage(pageTeam, className,
							assemblyName);
				} else {

					System.out
							.println("ERROR: TeamModule did not allow joinTeam!");
				}
			} else if (CLASS_GROUP_TYPE == typePage) {

				PageGroupGui pageGroup = new PageGroupGui(className,
						assemblyName);
				pageTable_.put(className + assemblyName, pageGroup);

				SessionModule.displayJoinPage(pageGroup, className,
						assemblyName);
			} else if (CLASS_GROUP_MGT_TYPE == typePage) {

				PageGroupGui pageGroup = new PageGroupGui(className,
						assemblyName);
				pageGroup.setIsManaged(true);

				pageTable_.put(className + assemblyName, pageGroup);

				SessionModule.displayJoinPage(pageGroup, className,
						assemblyName);
			} else {

				System.out.println("ERROR: Did not recognize the page type!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Method: This method is called if we want to
	// exit a team or group page
	public synchronized static void exitPage(String className,
			String assemblyName) {

		String key = className + assemblyName;

		if (pageTable_.containsKey(key)) {

			pageTable_.remove(key);
		}
	}

	// Method:
	// Remove a page from the page table
	public synchronized static void removeArchivePage(String className,
			String assemblyName) {

		String key = className + assemblyName;

		if (archivePageTable_.containsKey(key)) {

			archivePageTable_.remove(key);
		}
	}

	// Method:
	// Adding a page to the page table
	public synchronized static void putArchivePage(String className,
			String assemblyName, Object page) {

		String key = className + assemblyName;

		// If a page with identical key exists, remove existing page first
		if (archivePageTable_.containsKey(key)) {

			removeArchivePage(className, assemblyName);
			System.err.println("ERROR: PageModule.putPage");
		}

		archivePageTable_.put(key, page);
	}

	// Method:
	// Get a page
	public synchronized static Object getArchivePage(String className,
			String assemblyName) {

		Object page = null;

		String key = className + assemblyName;

		if (archivePageTable_.containsKey(key)) {

			page = archivePageTable_.get(key);
		}

		return page;
	}

	// Method: Get access to a certain page
	public static PageGui getPage(String className, String assemblyName) {

		PageGui page = null;

		String key = className + assemblyName;

		if (pageTable_.containsKey(key)) {

			page = (PageGui) pageTable_.get(key);
		}

		return page;
	}

	// Method: Get access to all pages
	public static PageGui[] getAllPages() {

		PageGui pages[] = new PageGui[pageTable_.size()];
		Enumeration enum = pageTable_.keys();

		int i = 0;

		while (enum.hasMoreElements()) {

			String key = (String) enum.nextElement();

			// Get a page
			pages[i] = (PageGui) pageTable_.get(key);

			i++;
		}

		return pages;
	}

	// Method: Get access to all team pages
	public static PageTeamGui[] getAllTeamPages() {

		PageTeamGui pages[] = new PageTeamGui[pageTable_.size()];
		Enumeration enum = pageTable_.keys();

		int i = 0;

		while (enum.hasMoreElements()) {

			String key = (String) enum.nextElement();

			if (pageTable_.get(key) instanceof PageTeamGui) {

				pages[i] = (PageTeamGui) pageTable_.get(key);
				i++;
			}
		}

		return pages;
	}

	// Method: Check if the user is the manager of a page
	public static boolean isManagerOfPage(String className, String assemblyName) {

		PageGui page = getPage(className, assemblyName);

		if (null != page) {

			return page.isManaged();
		} else {

			return false;
		}
	}

	// Gui Wrapper Method:
	// This method will add a user to a page who just joined
	// a team or a group.
	public synchronized static void displayAssemblyMember(String className,
			String assemblyName, String userAlias) {

		// Make sure we only send events to actual existing pages
		String key = className + assemblyName;

		if (pageTable_.containsKey(key)) {

			((PageGui) pageTable_.get(key)).displayAssemblyMember(userAlias);
		}
	}

	// Gui Wrapper Method:
	// This method moves the assembly manager to the top of the chat
	// page user list.
	public synchronized static void displayAssemblyMgrAtTop(String className,
			String assemblyName, String userAlias) {

		// Make sure we only send events to actual existing pages
		String key = className + assemblyName;

		if (pageTable_.containsKey(key)) {

			((PageGui) pageTable_.get(key)).displayAssemblyMgrAtTop(userAlias);
		}
	}

	// Method: Remove a user from the active user list
	public synchronized static void removeAssemblyMember(String className,
			String assemblyName, String userAlias) {

		// Make sure we only send events to actual existing pages
		String key = className + assemblyName;

		if (pageTable_.containsKey(key)) {

			PageGui page = (PageGui) pageTable_.get(key);

			if (null != page) {

				page.removeAssemblyMember(userAlias);
			}
		}
	}

	// Method: Test if a given Page is active
	// Method needs className and assembly name
	// to generate the map key
	public static boolean isPageActive(String className, String assemblyName) {

		String key = className + assemblyName;

		if (pageTable_.containsKey(key)) {

			return true;
		} else {

			return false;
		}
	}
}