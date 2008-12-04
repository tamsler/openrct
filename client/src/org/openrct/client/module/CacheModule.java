// $Id: CacheModule.java,v 1.8 2003/05/20 17:25:23 thomas Exp $

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

// Cache Module
// The following are the specific key values per module:
// -----------------------------------------------------
// User Module Key: userAlias
// CourseContent Module Key: courseContentId (file id)
// Sound Module Key: SoundId (file id)
// Textpad Module Key: TextpadId
package org.openrct.client.module;

import org.openrct.client.Const;
import java.io.*;
import java.util.*;

// Cache Module
public class CacheModule implements Const {

	// Hash Table to access cached data
	private static Hashtable cache_ = new Hashtable();

	// Constructor
	private CacheModule() {

		// Nothing to do here
	}

	// Methods:
	// --------

	// Method:
	// Check if it is a cache hit
	public static boolean isHit(String key, int module) {

		String cacheKey = genCacheKey(key, module);

		return cache_.containsKey(cacheKey);
	}

	// Method:
	// Get data from cache
	public static boolean get(String key, int module,
			RCT.BinaryFileHolder fileData) {

		String cacheKey = genCacheKey(key, module);

		byte[] data = null;

		try {

			File file = (File) cache_.get(cacheKey);

			int fileSize = (int) file.length();

			data = new byte[fileSize];

			DataInputStream in = new DataInputStream(new FileInputStream(file));

			in.readFully(data);
			in.close();
		} catch (Exception e) {

			System.err.println("ERROR: CacheModule.get()");
			return false;
		}

		fileData.value = data;
		return true;
	}

	// Method:
	// Put data into cache
	// This put method takes a RCT.BinaryFile Holder
	// The file name should not include user generated input
	// If possible, use module name and unique id
	public static boolean put(String key, int module,
			RCT.BinaryFileHolder fileData, String fileName) {

		String cacheKey = genCacheKey(key, module);

		File file = new File(FileModule.getTempDataDirName(), fileName);

		try {

			DataOutputStream out = new DataOutputStream(new FileOutputStream(
					file));

			out.write(fileData.value);
			out.close();

		} catch (Exception e) {

			System.err.println("ERROR: CacheModule.put()");
			return false;
		}

		cache_.put(cacheKey, file);

		return true;
	}

	// Method:
	// Put data into cache
	// This put method takes a FILE object
	// Overloads previous put method
	public static boolean put(String key, int module, File file, String fileName) {

		String cacheKey = genCacheKey(key, module);

		File cacheFile = new File(FileModule.getTempDataDirName(), fileName);

		if (!cacheFile.exists()) {

			// Copy the file to the user's cache dir
			if (!FileModule.copy(file, cacheFile)) {

				System.err.println("ERROR: CacheModule.put method!");
				return false;
			}
		}

		if (!isHit(key, module)) {

			// Make cache entry
			cache_.put(cacheKey, file);
		}

		return true;
	}

	// Method:
	// Remove an entry from the cache. Also delete the associated file.
	public static boolean remove(String key, String fileName, int module) {

		// Check if we have a valid key
		if (isHit(key, module)) {

			// Remove the key from the cache
			cache_.remove(genCacheKey(key, module));

			// We also delet the associated file
			// Get file
			File file = new File(FileModule.getTempDataDirName(), fileName);

			// Does File exist?
			if (file.exists()) {

				return file.delete();
			} else {

				return false;
			}
		} else {

			return false;
		}
	}

	// Method:
	// Generate cache key
	private static String genCacheKey(String key, int module) {

		switch (module) {

		case MOD_USER:
			return MODULE_USER + key;

		case MOD_CLASS:
			return MODULE_CLASS + key;

		case MOD_TEAM:
			return MODULE_TEAM + key;

		case MOD_GROUP:
			return MODULE_GROUP + key;

		case MOD_SOUND:
			return MODULE_SOUND + key;

		case MOD_FTP:
			return MODULE_FTP + key;

		case MOD_MAIL:
			return MODULE_MAIL + key;

		case MOD_TEXTPAD:
			return MODULE_TEXTPAD + key;

		case MOD_TD:
			return MODULE_TD + key;

		default:
			return "";
		}
	}
}

