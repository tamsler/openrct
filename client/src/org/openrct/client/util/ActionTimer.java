// $Id: ActionTimer.java,v 1.3 2003/05/08 19:31:42 thomas Exp $

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

import org.openrct.client.Const;

public abstract class ActionTimer implements Const {

	private Thread timer_ = null;

	private int sleepTime_;

	private boolean performAction_;

	private boolean stopPerformAction_;

	// Constructor
	public ActionTimer(int sleepTime) {

		// Init vars
		stopPerformAction_ = false;
		performAction_ = true;
		sleepTime_ = sleepTime;

		// Create thread
		timer_ = new Thread(new Runnable() {

			public void run() {

				try {

					while (!stopPerformAction_) {

						// Sleep for preset time
						Thread.sleep(sleepTime_);

						doPerformAction();
					}
				} catch (Exception e) {
				}
			}
		});
	}

	// Methods:
	// --------

	// Method:
	// Tell the action timer to perform action
	public void doPerformAction() {

		performAction_ = true;
	}

	// Method:
	// Tell the action timer to NOT to perform action
	public void doNotPerformAction() {

		performAction_ = false;
	}

	// Method:
	// Tell the action timer to stop
	public void stopWorking() {

		stopPerformAction_ = true;
	}

	// Method:
	// Tell the action timer to start
	public void startWorking() {

		timer_.start();
	}

	// Method:
	// Check if we can perform action
	public boolean canPerformAction() {

		return performAction_;
	}

	// Abstract Methods:
	// -----------------

	// Abstarct Method:
	public abstract void performAction();
}

