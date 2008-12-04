// $Id: ClassModule.java,v 1.5 2003/05/08 19:37:23 thomas Exp $

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

// Class Module
package org.openrct.client.module;

import org.openrct.client.Const;
import org.openrct.client.gui.CourseContentGui;

public class ClassModule implements Const {

	// Access to the class server
	private static RCT.ClassServer classServer_ = null;

	// Access reference to the Course Content Gui Page
	private static CourseContentGui courseContentGui_ = null;

	// Constructor
	private ClassModule() {

		// Nothing to do here
	}

	// Init: Getting Class Server Ref.
	public static void init(org.omg.CosNaming.NamingContext nc) {

		try {
			org.omg.CORBA.Object obj;

			org.omg.CosNaming.NameComponent[] objectName = new org.omg.CosNaming.NameComponent[2];
			objectName[0] = new org.omg.CosNaming.NameComponent(RCT_ID,
					RCT_KIND);
			objectName[1] = new org.omg.CosNaming.NameComponent(CLASS_ID,
					CLASS_KIND);

			obj = nc.resolve(objectName);

			classServer_ = RCT.ClassServerHelper.narrow(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Methods:
	// ========

	// Method:
	// Setting the course content gui
	public static void setCourseContentRef(CourseContentGui ccg) {

		courseContentGui_ = ccg;
	}

	// Method:
	// Access the course content gui
	public static CourseContentGui getCourseContentRef() {

		return courseContentGui_;
	}

	// IDL Wrapper Method:
	public static void getClassesFromUserId(String userId,
			RCT.ClassSeqHolder classSeq) {

		classServer_.get_classes_from_user_id(userId, classSeq);
	}

	// IDL Wrapper Method:
	public static void getClassesFromUserIds(String userId1, String userId2,
			RCT.ClassSeqHolder classSeq) {

		classServer_.get_classes_from_user_ids(userId1, userId2, classSeq);
	}

	// IDL Wrapper Method:
	public static void getAllClasses(RCT.ClassSeqHolder classSeq) {

		classServer_.get_all_classes(classSeq);
	}

	// IDL Wrapper Method:
	public static String getStatus() {

		return classServer_.get_status();
	}
}

