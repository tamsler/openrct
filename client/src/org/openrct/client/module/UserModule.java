// $Id: UserModule.java,v 1.4 2003/05/08 19:37:23 thomas Exp $

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

// User Module
package org.openrct.client.module;

import org.openrct.client.Const;
import org.omg.CORBA.*;

public class UserModule implements Const {

	// PRIVATE:

	// Access to the user server
	private static RCT.UserServer userServer_ = null;

	// User fields
	private static String id_ = "";

	private static String alias_ = "";

	private static String firstName_ = "";

	private static String lastName_ = "";

	private static String password_ = "";

	private static String permission_ = "";

	// Constructor
	private UserModule() {

		// Nothing to do here
	}

	// Init: Getting User Server Ref.
	public static void init(org.omg.CosNaming.NamingContext nc) {

		try {
			org.omg.CORBA.Object obj;

			org.omg.CosNaming.NameComponent[] objectName = new org.omg.CosNaming.NameComponent[2];
			objectName[0] = new org.omg.CosNaming.NameComponent(RCT_ID,
					RCT_KIND);
			objectName[1] = new org.omg.CosNaming.NameComponent(USER_ID,
					USER_KIND);

			obj = nc.resolve(objectName);

			userServer_ = RCT.UserServerHelper.narrow(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Init the user variables
	public static void initUser(String id, String alias, String firstName,
			String lastName, String permission, String password) {

		id_ = id;
		alias_ = alias;
		firstName_ = firstName;
		lastName_ = lastName;
		permission_ = permission;
		password_ = password;
	}

	// Init the user variables
	public static void initUser(RCT.User user) {

		id_ = user.user_id;
		alias_ = user.alias;
		firstName_ = user.first_name;
		lastName_ = user.last_name;
		permission_ = user.permission;
		password_ = user.password;
	}

	// Get the user's id
	public static String getId() {

		return id_;
	}

	// Get the user's alias
	public static String getAlias() {

		return alias_;
	}

	// Get the user's first name
	public static String getFirstName() {

		return firstName_;
	}

	// Get the user's last name
	public static String getLastName() {

		return lastName_;
	}

	// Get the user's password
	public static String getPassword() {

		return password_;
	}

	// Get the user's permission
	public static String getPermission() {

		return permission_;
	}

	// IDL Wrapper Method:
	public static RCT.User getUserFromUserId(String userId) {

		return userServer_.get_user_from_user_id(userId);
	}

	// IDL Wrapper Method:
	public static RCT.User getUserFromUserAlias(String userAlias) {

		return userServer_.get_user_from_user_alias(userAlias);
	}

	// IDL Wrapper Method:
	public static void getUsersFromClassName(String className,
			RCT.UserSeqHolder userSeq) {

		userServer_.get_users_from_class_name(className, userSeq);
	}

	// IDL Wrapper Method:
	public static void getUsersFromTeamName(String className, String teamName,
			RCT.UserSeqHolder userSeq) {

		userServer_.get_users_from_team_name(className, teamName, userSeq);
	}

	// IDL Wrapper Method:
	public static void getUserIdsFromTeamName(String className,
			String teamName, RCT.UserIdSeqHolder userIdSeq) {

		userServer_.get_user_ids_from_team_name(className, teamName, userIdSeq);
	}

	// IDL Wrapper Method:
	public static void getUsersFromGroupName(String className,
			String groupName, RCT.UserSeqHolder userSeq) {

		userServer_.get_users_from_group_name(className, groupName, userSeq);
	}

	// IDL Wrapper Method:
	public static void getUserIdsFromGroupName(String className,
			String groupName, RCT.UserIdSeqHolder userIdSeq) {

		userServer_.get_user_ids_from_group_name(className, groupName,
				userIdSeq);
	}

	// IDL Wrapper Method:
	public static void getUsersRelatedByClasses(String userId,
			RCT.UserSeqHolder userSeq) {

		userServer_.get_users_related_by_classes(userId, userSeq);
	}

	// IDL Wrapper Method:
	public static void getAllUsers(RCT.UserSeqHolder userSeq) {

		userServer_.get_all_users(userSeq);
	}

	// IDL Wrapper Method:
	public static void getOnlineUsersFromTeamName(String className,
			String teamName, RCT.UserSeqHolder userSeq) {

		userServer_.get_online_users_from_team_name(className, teamName,
				userSeq);
	}

	// IDL Wrapper Method:
	public static void getOnlineUserIdsFromTeamName(String className,
			String teamName, RCT.UserIdSeqHolder userIdSeq) {

		userServer_.get_online_user_ids_from_team_name(className, teamName,
				userIdSeq);
	}

	// IDL Wrapper Method:
	public static boolean getUserImage(String userAlias,
			RCT.BinaryFileHolder fileData) {

		RCT.FileInfoHolder fileInfo = new RCT.FileInfoHolder();

		if (CacheModule.isHit(userAlias, MOD_USER)) {

			// Cache hit don't get file from server
			return CacheModule.get(userAlias, MOD_USER, fileData);
		} else {

			StringHolder user_id = new StringHolder();

			if (userServer_.get_user_image(userAlias, user_id, fileInfo,
					fileData)) {

				if (CacheModule.put(userAlias, MOD_USER, fileData, MODULE_USER
						+ "_" + user_id.value)) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
	}

	// IDL Wrapper Method:
	public static void getModuleControlQueueUsers(String className,
			String assemblyName, String moduleName,
			RCT.UserAliasSeqHolder userAliasSeq, int type) {

		userServer_.get_module_control_queue_users(className, assemblyName,
				moduleName, userAliasSeq, type);
	}

	// IDL Wrapper Method:
	public static boolean isUserManagerOfAssembly(String className,
			String assemblyName, String userId, int type) {

		return userServer_.is_user_manager_of_assembly(className, assemblyName,
				userId, type);
	}

	// IDL Wrapper Method:
	public static String getStatus() {

		return userServer_.get_status();
	}
}

