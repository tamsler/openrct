// $Id: ArchiveTextpadDataLastN.java,v 1.4 2003/05/08 20:09:03 thomas Exp $

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
import org.openrct.client.module.TeamModule;
import org.openrct.client.module.SessionModule;
import org.openrct.client.module.LangModule;
import org.openrct.client.util.Utility;
import javax.swing.*;
import javax.swing.table.*;

public class ArchiveTextpadDataLastN extends AbstractTableModel implements
		Const {

	private String className_;

	private String teamName_;

	private int nTicks_;

	private RCT.TextpadMsgHistSeqHolder textpadSeq_ = null;

	private Object[][] data_ = null;

	// Constructor
	public ArchiveTextpadDataLastN(String className, String teamName, int nTicks) {

		className_ = className;
		teamName_ = teamName;
		nTicks_ = nTicks;

		populateData();
	}

	// Abstract Methods:
	// -----------------

	// Abstract Method:
	public int getRowCount() {

		return data_.length;
	}

	// Abstract Method:
	public int getColumnCount() {

		return TEXTPAD_COLUMN_NAMES.length;
	}

	// Abstract Method:
	public Object getValueAt(int row, int col) {

		return data_[row][col];
	}

	// Abstract Method:
	public void setValueAt(Object value, int row, int col) {

		// Nothing to do
	}

	// Abstract Method:
	public boolean isCellEditable(int row, int col) {

		return false;
	}

	// Abstract Method:
	public String getColumnName(int col) {

		return TEXTPAD_COLUMN_NAMES[col];
	}

	// Abstract Method:
	public Class getColumnClass(int col) {

		return data_[0][col].getClass();
	}

	// Methods:
	// --------

	// Method:
	public void refresh() {

		populateData();

		// Signal the table that the module has changed
		this.fireTableStructureChanged();
	}

	// Method:
	private void populateData() {

		textpadSeq_ = new RCT.TextpadMsgHistSeqHolder();
		data_ = new Object[0][0];

		try {

			TeamModule.getTextpadArchiveLastN(className_, teamName_, nTicks_,
					textpadSeq_);
		} catch (RCT.TeamServerPackage.DataSelectionExceedsLimit dsel) {

			JOptionPane.showMessageDialog(SessionModule.getFrame(),
					LangModule.i18n.getString("ExceptionDialog3"),
					LangModule.i18n.getString("ExceptionDialog1"),
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		// Check if we received any textpads
		if (0 < textpadSeq_.value.length) {

			data_ = new Object[textpadSeq_.value.length][TEXTPAD_N_FIELDS];

			for (int i = 0; i < textpadSeq_.value.length; i++) {

				data_[i][TEXTPAD_ID] = textpadSeq_.value[i].id;
				data_[i][TEXTPAD_NAME] = textpadSeq_.value[i].name;
				data_[i][TEXTPAD_DATE] = Utility
						.getDateAndTime(textpadSeq_.value[i].date);
			}
		}
	}
}

