// $Id: SoundModule.java,v 1.12 2003/05/08 19:37:23 thomas Exp $

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

// Sound Module
package org.openrct.client.module;

import org.openrct.client.Const;
import org.openrct.client.gui.PageGui;
import org.openrct.client.gui.SoundPageGui;
import org.openrct.client.gui.ArchiveSoundPageGui;
import org.openrct.client.util.FileUploadDownload;
import java.io.*;
import javax.swing.*;

public class SoundModule implements Const {

	// Access the sound server
	private static RCT.SoundServer soundServer_ = null;

	// FileUploadDownload object
	public static FileUploadDownload fileUploadDownload = new FileUploadDownload();

	// Sound recording flag. This is used to make sure
	// that only one recording at a time is made
	private static boolean recording_ = false;

	// Constructor
	private SoundModule() {
		// Nothing to do here
	}

	// Init: Getting Sound Server Ref.
	public static void init(org.omg.CosNaming.NamingContext nc) {

		try {
			org.omg.CORBA.Object obj;

			org.omg.CosNaming.NameComponent[] objectName = new org.omg.CosNaming.NameComponent[2];
			objectName[0] = new org.omg.CosNaming.NameComponent(RCT_ID,
					RCT_KIND);
			objectName[1] = new org.omg.CosNaming.NameComponent(SOUND_ID,
					SOUND_KIND);

			obj = nc.resolve(objectName);

			soundServer_ = RCT.SoundServerHelper.narrow(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Method:
	public static void send(String id, String alias, String name,
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
						assemblyName, userId, userAlias, version, MODULE_SOUND,
						type, permission, String.valueOf(data.length));
			}
		} catch (Exception e) {
		}
	}

	// Method:
	public static void packetHandler(String userAlias, String className,
			String assemblyName, String id, int packetType, byte[] packet) {

		// Get the storred file object
		File file = (File) FileModule.files_.get(id);

		// Setup the SoundPageGui dispatch object
		PageGui page = PageModule.getPage(className, assemblyName);

		SoundPageGui soundPage = null;

		if (null != page) {

			soundPage = (SoundPageGui) page.getModulePage(MOD_SOUND);
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
			if (null != soundPage) {

				soundPage.incrementProgressBar(packet.length);
			}

			break;

		case FILE_DOWNLOAD_END:

			String audioFile = MODULE_SOUND_ + id;

			// Got the last file packet, adding file to cache
			CacheModule.put(id, MOD_SOUND, file, audioFile);

			// Now we dispatch the end of download
			if (null != soundPage) {

				soundPage.endDownload();

				soundPage.playAudio(audioFile);
			}

			break;

		case FILE_DOWNLOAD_STOPPED:

			// Clean up partial file download
			if (file.exists()) {

				file.delete();
			}

			// Now we dispatch the end/stop/cancel of download
			if (null != soundPage) {

				soundPage.endDownload();
			}

			break;

		default:
			System.err
					.println("ERROR: SoundModule.packetHandler did not recognize type!");
		}
	}

	// Method:
	public static void archivePacketHandler(String userAlias, String className,
			String assemblyName, String id, int packetType, byte[] packet) {

		// Get the storred file object
		File file = (File) FileModule.files_.get(id);

		// Test if we still can access the session page
		PageGui page = PageModule.getPage(className, assemblyName);

		// Setup the ArchiveSoundPageGui dispatch object
		ArchiveSoundPageGui archivePage = (ArchiveSoundPageGui) PageModule
				.getArchivePage(className, assemblyName);

		// Check if page has been close
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

			String audioFile = MODULE_SOUND_ + id;

			// Got the last file packet, adding file to cache
			CacheModule.put(id, MOD_SOUND, file, audioFile);

			// Now we dispatch the end of download
			if (null != archivePage) {

				archivePage.endDownload();

				archivePage.playAudio(audioFile);
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
					.println("ERROR: SoundModule.packetHandler did not recognize type!");
		}
	}

	// Method:
	public static void download(String soundMsgId, File file, String className,
			String assemblyName, int msgType) {

		try {

			// Get sound message from server
			FileModule.startDownload(soundMsgId, UserModule.getId(), UserModule
					.getAlias(), className, assemblyName, RCT_VERSION, msgType);
		} catch (Exception e) {
		}
	}

	// IDL Wrapper Method:
	public static boolean fetch(String soundMsgId, File file, String className,
			String assemblyName, int msgType) {

		// Store the file object
		FileModule.files_.put(soundMsgId, file);

		if (CacheModule.isHit(soundMsgId, MOD_SOUND)) {

			return true;
		} else {

			return false;
		}
	}

	// IDL Wrapper Method:
	public static void getSoundInfoFromClassAndAssemblyName(String className,
			String assemblyName, int pageType, RCT.SoundMsgHistSeqHolder sndSeq) {

		soundServer_.get_sound_info_from_class_and_assembly_name(className,
				assemblyName, pageType, sndSeq);
	}

	// IDL Wrapper Method:
	public static String getStatus() {

		return soundServer_.get_status();
	}

	// GUI Wrapper Method:
	public synchronized static void displaySoundMsg(String userAlias,
			String className, String assemblyName, RCT.FileInfo sndInfo) {

		PageGui page = PageModule.getPage(className, assemblyName);

		SoundPageGui soundPage = null;

		if (null != page) {

			soundPage = (SoundPageGui) page.getModulePage(MOD_SOUND);
		} else {

			return;
		}

		if (null != soundPage) {

			soundPage.displaySoundMsg(userAlias, sndInfo);
		}
	}

	// Methods:
	// --------

	// Method:
	// Create an absolute temp file name
	public static String createTempName(int num) {

		String name = FileModule.getTempDataDirName() + FS + SOUND_PREFIX + "_"
				+ num + SOUND_EXT_AU;

		return name;
	}

	// Method:
	// This method needs e called before we record
	// sound
	public synchronized static boolean canStartRecording() {

		// If the recording flag is set, one recording is
		// already running so we return false
		if (recording_) {

			return false;
		} else {

			recording_ = true;

			return true;
		}
	}

	// Method:
	// This method needs to be called before after we
	// stopped recording
	public synchronized static void stopRecording() {

		recording_ = false;
	}
}

