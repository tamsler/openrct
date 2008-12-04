// $Id: FtpModule.java,v 1.14 2003/05/08 19:37:23 thomas Exp $

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

// FTP Module
package org.openrct.client.module;

import org.openrct.client.Const;
import org.openrct.client.gui.PageGui;
import org.openrct.client.gui.FtpPageGui;
import org.openrct.client.gui.ArchiveFtpPageGui;
import org.openrct.client.util.FileUploadDownload;
import javax.swing.*;
import java.io.*;

public class FtpModule implements Const {

	// Access to the ftp server
	private static RCT.FtpServer ftpServer_ = null;

	// FileUploadDownload object
	public static FileUploadDownload fileUploadDownload = new FileUploadDownload();

	// Constructor
	private FtpModule() {
		// Nothing to do here
	}

	// Method:
	// Init: Getting Ftp Server Ref.
	public static void init(org.omg.CosNaming.NamingContext nc) {

		try {
			org.omg.CORBA.Object obj;

			org.omg.CosNaming.NameComponent[] objectName = new org.omg.CosNaming.NameComponent[2];
			objectName[0] = new org.omg.CosNaming.NameComponent(RCT_ID,
					RCT_KIND);
			objectName[1] = new org.omg.CosNaming.NameComponent(FTP_ID,
					FTP_KIND);

			obj = nc.resolve(objectName);

			ftpServer_ = RCT.FtpServerHelper.narrow(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Method:
	public static void upload(String id, String alias, String name,
			String mimeType, String className, String assemblyName,
			String userId, String userAlias, String version, int type,
			RCT.ObjPermission permission, byte[] data, JProgressBar jProgressBar) {

		try {

			FileModule.startTransfer(name);

			FileModule.packageAndSendData(name, data, jProgressBar,
					fileUploadDownload);

			// Test if the file transfer was stopped
			if (fileUploadDownload.isStopped()) {

				fileUploadDownload.reset();
			} else {

				FileModule.endTransfer(id, alias, name, mimeType, className,
						assemblyName, userId, userAlias, version, MODULE_FTP,
						type, permission, String.valueOf(data.length));
			}
		} catch (Exception e) {
		}
	}

	// Method:
	public static void download(String ftpId, File file, String className,
			String assemblyName, int msgType) {

		// Store the file object
		FileModule.files_.put(ftpId, file);

		// First we check if the file is in cache
		if (CacheModule.isHit(ftpId, MOD_FTP)) {

			RCT.BinaryFileHolder fileData = new RCT.BinaryFileHolder();

			// Cache hit don't get file from server
			CacheModule.get(ftpId, MOD_FTP, fileData);

			try {

				FileOutputStream fos = new FileOutputStream(file);
				fos.write(fileData.value);
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			// Setup the FtpPageGui dispatch object
			PageGui page = PageModule.getPage(className, assemblyName);

			FtpPageGui ftpPage = null;

			if (null != page) {

				ftpPage = (FtpPageGui) page.getModulePage(MOD_FTP);
			} else {

				return;
			}

			// Now we dispatch the end of download
			if (null != ftpPage) {

				ftpPage.endDownload();
			}
		} else {

			// File was not in cache, so we need to download and cache it

			// Initiate file download
			try {

				FileModule.startDownload(ftpId, UserModule.getId(), UserModule
						.getAlias(), className, assemblyName, RCT_VERSION,
						msgType);
			} catch (Exception e) {
			}
		}
	}

	// Method:
	public static void packetHandler(String userAlias, String className,
			String assemblyName, String id, int packetType, byte[] packet) {

		// Get the storred file object
		File file = (File) FileModule.files_.get(id);

		// Setup the FtpPageGui dispatch object
		PageGui page = PageModule.getPage(className, assemblyName);

		FtpPageGui ftpPage = null;

		if (null != page) {

			ftpPage = (FtpPageGui) page.getModulePage(MOD_FTP);
		} else {

			// Only send the cancel download if we still recieve packets
			if (FILE_DOWNLOAD_PACKET == packetType) {

				try {

					FileModule.cancelDownload(id, UserModule.getId(),
							className, assemblyName);
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
			if (null != ftpPage) {

				ftpPage.incrementProgressBar(packet.length);
			}

			break;

		case FILE_DOWNLOAD_END:

			// Got the last file packet, adding file to cache
			CacheModule.put(id, MOD_FTP, file, MODULE_FTP_ + id);

			// Now we dispatch the end of download
			if (null != ftpPage) {

				ftpPage.endDownload();
			}

			break;

		case FILE_DOWNLOAD_STOPPED:

			// Clean up partial file download
			if (file.exists()) {

				file.delete();
			}

			// Now we dispatch the end/stop/cancel of download
			if (null != ftpPage) {

				ftpPage.endDownload();
			}

			break;

		default:
			System.err
					.println("ERROR: FileModule.packetHandler did not recognize type!");
		}
	}

	// Method:
	public static void archivePacketHandler(String userAlias, String className,
			String assemblyName, String id, int packetType, byte[] packet) {

		// Get the storred file object
		File file = (File) FileModule.files_.get(id);

		// Test if we still can access the session page

		PageGui page = PageModule.getPage(className, assemblyName);
		// Setup the ArchiveFtpPageGui dispatch object
		ArchiveFtpPageGui archivePage = (ArchiveFtpPageGui) PageModule
				.getArchivePage(className, assemblyName);

		if ((null == page) || (null == archivePage)) {

			// Only send the cancel download if we still recieve packets
			if (FILE_DOWNLOAD_PACKET == packetType) {

				try {

					FileModule.cancelDownload(id, UserModule.getId(),
							className, assemblyName);
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
			if (null != archivePage) {

				archivePage.incrementProgressBar(packet.length);
			}

			break;

		case FILE_DOWNLOAD_END:

			// Got the last file packet, adding file to cache
			CacheModule.put(id, MOD_FTP, file, MODULE_FTP_ + id);

			// Now we dispatch the end of download
			if (null != archivePage) {

				archivePage.endDownload();
			}

			break;

		case FILE_DOWNLOAD_STOPPED:

			// Clean up partial file download
			if (file.exists()) {

				file.delete();
			}

			// Now we dispatch the end/stop/cancel of download
			if (null != archivePage) {

				archivePage.endDownload();
			}

			break;

		default:
			System.err
					.println("ERROR: FileModule.packetHandler did not recognize type!");
		}
	}

	// IDL Wrapper Method:
	public static void getFtpInfoFromClassAndAssemblyName(String className,
			String assemblyName, int pageType, RCT.FtpMsgHistSeqHolder ftpSeq) {

		ftpServer_.get_ftp_info_from_class_and_assembly_name(className,
				assemblyName, pageType, ftpSeq);
	}

	// IDL Wrapper Method:
	public static String getStatus() {

		return ftpServer_.get_status();
	}

	// GUI Wrapper Method:
	public synchronized static void displayFtpUploadMsg(String userAlias,
			String className, String assemblyName, RCT.FileInfo ftpInfo) {

		PageGui page = PageModule.getPage(className, assemblyName);

		FtpPageGui ftpPage = null;

		if (null != page) {

			ftpPage = (FtpPageGui) page.getModulePage(MOD_FTP);
		} else {

			return;
		}

		if (null != ftpPage) {

			ftpPage.displayFtpUploadMsg(userAlias, ftpInfo);
		}
	}
}

