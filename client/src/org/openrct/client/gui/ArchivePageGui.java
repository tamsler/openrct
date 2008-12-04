// $Id: ArchivePageGui.java,v 1.3 2003/05/08 20:09:03 thomas Exp $

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

package org.openrct.client.gui;

import org.openrct.client.Const;
import java.awt.*;
import javax.swing.*;

public class ArchivePageGui extends JPanel implements Const {

	private GridBagLayout gridBagLayout = new GridBagLayout();

	private ArchiveSelectionGui archiveSelectionGui = null;

	private JPanel selectionOrDataPage = null;

	private String className_;

	private String teamName_;

	// Constructor
	public ArchivePageGui(String className, String teamName) {

		className_ = className;
		teamName_ = teamName;

		archiveSelectionGui = new ArchiveSelectionGui(className_, teamName_,
				this);

		this.setLayout(gridBagLayout);

		this.add(archiveSelectionGui, new GridBagConstraints(0, 0, 1, 1, 1.0,
				1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(5, 5, 5, 5), 0, 0));
	}

	// Methods:
	// --------

	// Method:
	// Change Font for text area
	public void dispatchFontChange() {

		if (null != selectionOrDataPage) {

			// We only dispatch the font change to objects that have text areas
			if (selectionOrDataPage instanceof ArchiveChatTodayPageGui) {

				((ArchiveChatTodayPageGui) selectionOrDataPage)
						.dispatchFontChange();
			} else if (selectionOrDataPage instanceof ArchiveChatLastNPageGui) {

				((ArchiveChatLastNPageGui) selectionOrDataPage)
						.dispatchFontChange();
			} else if (selectionOrDataPage instanceof ArchiveChatTimeRangePageGui) {

				((ArchiveChatTimeRangePageGui) selectionOrDataPage)
						.dispatchFontChange();
			} else if (selectionOrDataPage instanceof ArchiveTextpadTodayPageGui) {

				((ArchiveTextpadTodayPageGui) selectionOrDataPage)
						.dispatchFontChange();
			} else if (selectionOrDataPage instanceof ArchiveTextpadLastNPageGui) {

				((ArchiveTextpadLastNPageGui) selectionOrDataPage)
						.dispatchFontChange();
			} else if (selectionOrDataPage instanceof ArchiveTextpadTimeRangePageGui) {

				((ArchiveTextpadTimeRangePageGui) selectionOrDataPage)
						.dispatchFontChange();
			}
		}
	}

	// Method:
	// Display archive selection or data pages
	public void displaySelectionOrDataPage(JPanel page) {

		if (null != selectionOrDataPage) {

			selectionOrDataPage.setVisible(false);
		}

		selectionOrDataPage = page;

		selectionOrDataPage.setVisible(true);
		archiveSelectionGui.setVisible(false);

		this.add(selectionOrDataPage, new GridBagConstraints(0, 0, 1, 1, 1.0,
				1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(5, 5, 5, 5), 0, 0));
	}

	// Method:
	// Hide current page and enabel selection page
	public void closePage() {

		archiveSelectionGui.setVisible(true);
		selectionOrDataPage.setVisible(false);
	}
}

