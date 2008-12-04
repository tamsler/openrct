// $Id: FileUploadDownload.java,v 1.3 2003/05/08 19:31:42 thomas Exp $

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

package org.openrct.client.util;

public class FileUploadDownload {

	private boolean stop_;

	// Constructor
	public FileUploadDownload() {

		// Init vars
		stop_ = false;
	}

	// Methods:
	// --------

	// Method:
	public synchronized void stop() {

		stop_ = true;
	}

	// Method:
	public synchronized void reset() {

		stop_ = false;
	}

	// Method:
	public synchronized boolean isStopped() {

		return stop_;
	}
}