// $Id: FileModule.java,v 1.8 2003/05/08 19:37:23 thomas Exp $

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

// File Module
package org.openrct.client.module;

import org.openrct.client.Const;
import org.openrct.client.util.FileUploadDownload;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class FileModule implements Const {

	// Access to the file server
	private static RCT.FileServer fileServer_ = null;

	// Hash table to access file objects
	// The keys are ids
	public static Hashtable files_ = new Hashtable();

	// Constructor
	private FileModule() {

		// Nothing to do here
	}

	// Init: Getting File Server Ref.
	public static void init(org.omg.CosNaming.NamingContext nc) {

		try {
			org.omg.CORBA.Object obj;

			org.omg.CosNaming.NameComponent[] objectName = new org.omg.CosNaming.NameComponent[2];
			objectName[0] = new org.omg.CosNaming.NameComponent(RCT_ID,
					RCT_KIND);
			objectName[1] = new org.omg.CosNaming.NameComponent(F_ID, F_KIND);

			obj = nc.resolve(objectName);

			fileServer_ = RCT.FileServerHelper.narrow(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Method:
	// Writing a UNICODE string to a file in UTF-8 encoding
	public static void saveUnicodeUTF8(String defaultName, String text) {

		String defaultFileName = "";

		if (null == defaultName || defaultName.equals("")) {

			defaultFileName = FILE_DEF_SAVE_NAME;
		} else {

			defaultFileName = defaultName;
		}

		JFileChooser fc = new JFileChooser();

		fc.setDialogTitle(LangModule.i18n.getString("FileSaveDialog1"));
		fc.setSelectedFile(new File(defaultFileName));

		// Capture the return value after clicking on
		// a button in the FileChooser
		int returnVal = fc.showSaveDialog(new JFrame());

		if (JFileChooser.APPROVE_OPTION == returnVal) {

			// Getting the file name from the save file chooser
			File newFile = fc.getSelectedFile();

			File file = new File(fc.getCurrentDirectory().getAbsolutePath(),
					newFile.getName());

			try {

				FileOutputStream fos = new FileOutputStream(file);

				fos.write(text.getBytes(FILE_UTF_8_ENCODING));

				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// Method:
	// Delete all the files and sub directories
	// in provided directory
	public static void deleteDirectory(File dir) {

		if (dir.isDirectory()) {

			String[] children = dir.list();

			for (int i = 0; i < children.length; i++) {

				deleteDirectory(new File(dir, children[i]));
			}
		}

		dir.delete();
	}

	// Method:
	// Create a directory
	public static void createDirectory(File dir) {

		// First we check if the directory exists
		// If it does, we delete it
		if (dir.exists()) {

			deleteDirectory(dir);
		}

		// Now we can create the directory
		dir.mkdir();
	}

	// Method:
	// Generate the name for the temp data directory
	public static String getTempDataDirName() {

		String name = RCT_DATA + ConfigModule.getServerNameWithoutDomain()
				+ "_" + UserModule.getAlias();

		return name;
	}

	// Method:
	// Remove server side file prefix information
	public static String removePrefix(String name) {

		// the "name" argument is of the following form:
		// cc_F4_users.txt

		// Getting index of first '_'
		int index = name.indexOf('_');

		// Getting substring eg. F4_users.txt
		String tmp = name.substring(index + 1);

		// Getting index of next '_'
		index = tmp.indexOf('_');

		// Return substring eg. users.txt
		return tmp.substring(index + 1);
	}

	// Method:
	// Copy a file
	public static boolean copy(File from, File to) {

		FileInputStream fis_from = null;
		FileOutputStream fos_to = null;
		byte[] buffer = new byte[4096];
		int bytesRead = 0;

		try {

			fis_from = new FileInputStream(from);
			fos_to = new FileOutputStream(to);

			while ((bytesRead = fis_from.read(buffer)) != -1) {

				fos_to.write(buffer, 0, bytesRead);
			}

			fis_from.close();
			fos_to.close();
		} catch (Exception e) {

			return false;
		}

		return true;
	}

	// Method:
	// Check if the file has the line defined by the argument string
	public static boolean hasMatchingLine(File file, String property) {

		BufferedReader in = null;
		String line;

		try {

			in = new BufferedReader(new FileReader(file));

			// Go through the file and check each line against the property
			while ((line = in.readLine()) != null) {

				if (line.equals(property)) {

					// If we have a match we return
					in.close();
					return true;
				}
			}

			// Close the buffered reader
			in.close();
		} catch (Exception e) {
		}

		// We reached the end of the file without a match
		return false;
	}

	// Method:
	// Find a line in a file and return the matching line
	public static String getLine(File file, String prefix) {

		BufferedReader in = null;
		String line = "";

		try {

			in = new BufferedReader(new FileReader(file));

			// Go through the file and check each line if it starts with prefix
			while ((line = in.readLine()) != null) {

				if (line.startsWith(prefix)) {

					// If we have a match we return
					in.close();
					return line;
				}
			}

			// Close the buffered reader
			in.close();
		} catch (Exception e) {
		}

		// We reached the end of the file without a match
		return line;
	}

	// Methdo:
	public static void packageAndSendData(String name, byte[] data,
			JProgressBar jProgressBar, FileUploadDownload fud) {

		int dataSize = 0;
		int iterator = 0;
		int conSpeed = ConfigModule.getNumber(CONF_CONNECTION_SPEED);
		int remainingData = 0;
		byte[] temp = null;
		RCT.FilePacket fp = null;

		// Init JProgressBar
		jProgressBar.setStringPainted(true);
		jProgressBar.setMinimum(0);
		jProgressBar.setMaximum(data.length);
		jProgressBar.setValue(0);

		// Start creating packets and sending them
		while ((data.length > iterator) && (!fud.isStopped())) {

			remainingData = data.length - iterator;

			// Determine packet data size
			if (remainingData > conSpeed) {

				dataSize = conSpeed;
			} else {

				dataSize = remainingData;
			}

			temp = new byte[dataSize];

			for (int i = 0; i < dataSize; i++, iterator++) {

				temp[i] = data[iterator];
			}

			fp = new RCT.FilePacket();

			fp.seq_num = 0;
			fp.packet = temp;

			try {

				FileModule.sendPacket(name, fp);
			} catch (Exception e) {
				System.err.println("EXCEPTION: FileModule.sendPacket!");
			}

			// Update progress bar but check if user cancel
			if (!fud.isStopped()) {

				jProgressBar.setValue(iterator);
			}
		}

		if (fud.isStopped()) {

			// Tell the server to cleanup started transfer
			try {

				FileModule.cancelTransfer(name);
			} catch (Exception e) {
				System.err.println("EXCEPTION: FileModule.cancelTransfer!");
			}
		}
	}

	// IDL Methods:
	// ------------

	// IDL Method:
	public static String getStatus() {

		return fileServer_.get_status();
	}

	// IDL Method:
	public static String getIndex() {

		return fileServer_.get_index();
	}

	// IDL Method:
	public static void startTransfer(String name)
			throws RCT.FileServerPackage.FileIOException {

		fileServer_.start_transfer(name);
	}

	// IDL Method:
	public static void endTransfer(String id, String alias, String name,
			String mimeType, String className, String assemblyName,
			String userId, String userAlias, String version, String rctModule,
			int type, RCT.ObjPermission permission, String length) {

		fileServer_.end_transfer(id, alias, name, mimeType, className,
				assemblyName, userId, userAlias, version, rctModule, type,
				permission, length);
	}

	// IDL Method:
	public static void cancelTransfer(String name)
			throws RCT.FileServerPackage.FileIOException {

		fileServer_.cancel_transfer(name);
	}

	// IDL Method:
	public static void sendPacket(String name, RCT.FilePacket packet)
			throws RCT.FileServerPackage.FileIOException {

		fileServer_.send_packet(name, packet);
	}

	// IDL Method:
	public static void startDownload(String id, String userId,
			String userAlias, String className, String assemblyName,
			String version, int type)
			throws RCT.FileServerPackage.FileIOException {

		fileServer_.start_download(id, userId, userAlias, className,
				assemblyName, version, type, ConfigModule
						.getNumber(CONF_CONNECTION_SPEED));
	}

	// IDL Method:
	public static void cancelDownload(String id, String userId,
			String className, String assemblyName)
			throws RCT.FileServerPackage.FileIOException {

		fileServer_.cancel_download(id, userId, className, assemblyName);
	}
}

