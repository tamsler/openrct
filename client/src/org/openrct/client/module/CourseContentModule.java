// $Id: CourseContentModule.java,v 1.7 2003/05/08 19:37:23 thomas Exp $

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

// Course Content Module
package org.openrct.client.module;

import org.openrct.client.Const;
import org.openrct.client.gui.CourseContentGui;
import java.io.*;

public class CourseContentModule implements Const {

	// Access to the course content server
	private static RCT.CourseContentServer ccServer_ = null;

	// Constructor
	private CourseContentModule() {

		// Nothing to do here
	}

	// Init: Getting Course Content Server Ref.
	public static void init(org.omg.CosNaming.NamingContext nc) {

		try {
			org.omg.CORBA.Object obj;

			org.omg.CosNaming.NameComponent[] objectName = new org.omg.CosNaming.NameComponent[2];
			objectName[0] = new org.omg.CosNaming.NameComponent(RCT_ID,
					RCT_KIND);
			objectName[1] = new org.omg.CosNaming.NameComponent(COURSEC_ID,
					COURSEC_KIND);

			obj = nc.resolve(objectName);

			ccServer_ = RCT.CourseContentServerHelper.narrow(obj);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	// Methods:
	// ========

	// Method:
	public static void download(String ccId, File file, String className,
			int msgType) {

		// Store the file object
		FileModule.files_.put(ccId, file);

		// First we check if the file is in cache
		if (CacheModule.isHit(ccId, MOD_CC)) {

			RCT.BinaryFileHolder fileData = new RCT.BinaryFileHolder();

			// Cache hit don't get file from server
			CacheModule.get(ccId, MOD_CC, fileData);

			try {

				FileOutputStream fos = new FileOutputStream(file);
				fos.write(fileData.value);
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			ClassModule.getCourseContentRef().endDownload();
		} else {

			// File was not in cache, so we need to download and cache it

			// Initiate file download
			try {

				FileModule.startDownload(ccId, UserModule.getId(), UserModule
						.getAlias(), className, "", RCT_VERSION, msgType);
			} catch (Exception e) {
			}
		}
	}

	// Method:
	public static void packetHandler(String userAlias, String className,
			String id, int packetType, byte[] packet) {

		// Get the storred file object
		File file = (File) FileModule.files_.get(id);

		// Setup the CourseContentGui dispatch object
		CourseContentGui page = ClassModule.getCourseContentRef();

		if (null == page) {

			// Only send the cancel download if we still recieve packets
			if (FILE_DOWNLOAD_PACKET == packetType) {

				try {

					FileModule.cancelDownload(id, UserModule.getId(),
							className, "");
				} catch (Exception e) {
				}
			}

			return;
		}

		// Determine the package type operation
		switch (packetType) {

		case FILE_DOWNLOAD_PACKET:

			try {

				// Open file for append
				FileOutputStream fos = new FileOutputStream(file, true);
				fos.write(packet);
				fos.close();
			} catch (Exception e) {
			}

			// Now we dispatch the progress increment
			if (null != page) {

				page.incrementProgressBar(packet.length);
			}

			break;

		case FILE_DOWNLOAD_END:

			// Got the last file packet, adding file to cache
			CacheModule.put(id, MOD_CC, file, MODULE_CC_ + id);

			// Now we dispatch the end of download
			if (null != page) {

				page.endDownload();
			}

			break;

		case FILE_DOWNLOAD_STOPPED:

			// Clean up partial file download
			if (file.exists()) {

				file.delete();
			}

			// Now we dispatch the end/stop/cancel of download
			if (null != page) {

				page.endDownload();
			}

			break;

		default:
			System.err
					.println("ERROR: CourseContentModule.packetHandler did not recognize type!");
		}
	}

	// IDL Wrapper Methods:
	// ====================

	// IDL Wrapper Method:
	public static boolean fetch(String courseContentId,
			RCT.BinaryFileHolder fileData) {

		if (CacheModule.isHit(courseContentId, MOD_CC)) {

			// Cache hit don't get file from server
			return CacheModule.get(courseContentId, MOD_CC, fileData);
		} else {

			if (ccServer_.fetch(courseContentId, fileData)) {

				if (CacheModule.put(courseContentId, MOD_CC, fileData,
						MODULE_CC + "_" + courseContentId)) {
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
	public static void getCourseContentFromClassName(String className,
			RCT.CourseContentSeqHolder ccSeq) {

		ccServer_.get_course_content_from_class_name(className, ccSeq);
	}

	// IDL Wrapper Method:
	public static String getStatus() {

		return ccServer_.get_status();
	}
}

