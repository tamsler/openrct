// $Id: ArchiveSoundDataTimeRange.java,v 1.4 2003/05/08 20:09:03 thomas Exp $

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
import java.util.*;

public class ArchiveSoundDataTimeRange extends AbstractTableModel implements
		Const {

	private String className_;

	private String teamName_;

	private String fromDate_;

	private String toDate_;

	private int selectionType_;

	private RCT.SoundMsgHistSeqHolder soundSeq_ = null;

	private Object[][] data_ = null;

	// Constructor
	public ArchiveSoundDataTimeRange(String className, String teamName,
			String fromDate, String toDate, int selectionType) {

		className_ = className;
		teamName_ = teamName;
		fromDate_ = fromDate;
		toDate_ = toDate;
		selectionType_ = selectionType;

		populateData();
	}

	// Abstract Methods:
	// -----------------

	// Abstract Method
	public int getRowCount() {

		return data_.length;
	}

	// Abstract Method:
	public int getColumnCount() {

		return FILE_COLUMN_NAMES.length;
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

		return FILE_COLUMN_NAMES[col];
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

		soundSeq_ = new RCT.SoundMsgHistSeqHolder();
		data_ = new Object[0][0];

		if (ARCH_SEL_TODAY == selectionType_) {

			// Get today's date
			Date now = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(now);
			int day = cal.get(Calendar.DAY_OF_MONTH);
			int month = cal.get(Calendar.MONTH) + 1;
			int year = cal.get(Calendar.YEAR);

			String fromDate = year + "-" + month + "-" + day + " 00:00:00";

			String toDate = year + "-" + month + "-" + day + " 23:59:59";

			try {

				TeamModule.getSoundArchiveTimeRange(className_, teamName_,
						fromDate, toDate, soundSeq_);
			} catch (RCT.TeamServerPackage.DataSelectionExceedsLimit dsel) {

				JOptionPane.showMessageDialog(SessionModule.getFrame(),
						LangModule.i18n.getString("ExceptionDialog3"),
						LangModule.i18n.getString("ExceptionDialog1"),
						JOptionPane.WARNING_MESSAGE);
				return;
			}
		} else if (ARCH_SEL_TIMERANGE == selectionType_) {

			try {

				TeamModule.getSoundArchiveTimeRange(className_, teamName_,
						fromDate_, toDate_, soundSeq_);
			} catch (RCT.TeamServerPackage.DataSelectionExceedsLimit dsel) {

				JOptionPane.showMessageDialog(SessionModule.getFrame(),
						LangModule.i18n.getString("ExceptionDialog3"),
						LangModule.i18n.getString("ExceptionDialog1"),
						JOptionPane.WARNING_MESSAGE);
				return;
			}
		}

		// Check if we received any sounds
		if (0 < soundSeq_.value.length) {

			data_ = new Object[soundSeq_.value.length][FILE_N_FIELDS];

			for (int i = 0; i < soundSeq_.value.length; i++) {

				data_[i][FILE_ID] = soundSeq_.value[i].id;
				data_[i][FILE_ALIAS] = soundSeq_.value[i].alias;
				data_[i][FILE_NAME] = soundSeq_.value[i].name;
				data_[i][FILE_MIME_TYPE] = soundSeq_.value[i].mime_type;
				data_[i][FILE_USER_ALIAS] = soundSeq_.value[i].user_alias;
				data_[i][FILE_DATE] = Utility
						.getDateAndTime(soundSeq_.value[i].date);
				data_[i][FILE_LENGTH] = soundSeq_.value[i].length;
			}
		}
	}
}

