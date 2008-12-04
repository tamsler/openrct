// $Id: SoundData.java,v 1.4 2003/05/08 20:09:03 thomas Exp $

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
import org.openrct.client.module.SoundModule;
import org.openrct.client.util.Utility;
import javax.swing.table.*;

public class SoundData extends AbstractTableModel implements Const {

	private String className_ = "";

	private String assemblyName_ = "";

	private int pageType_;

	private RCT.SoundMsgHistSeqHolder soundSeq_ = null;

	private Object[][] data_ = null;

	// Constructor
	public SoundData(String className, String assemblyName, int pageType) {

		className_ = className;
		assemblyName_ = assemblyName;
		pageType_ = pageType;

		data_ = new Object[0][0];
	}

	// Abstract Methods:
	// -----------------

	// Abstract Method:
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
	public void append(String userAlias, RCT.FileInfo sndInfo) {

		int numRows = getRowCount();
		int numCols = getColumnCount();

		Object[][] newData = new Object[numRows + 1][numCols];

		// Copy old data
		for (int i = 0; i < numRows; i++) {

			newData[i][FILE_ID] = data_[i][FILE_ID];
			newData[i][FILE_ALIAS] = data_[i][FILE_ALIAS];
			newData[i][FILE_NAME] = data_[i][FILE_NAME];
			newData[i][FILE_MIME_TYPE] = data_[i][FILE_MIME_TYPE];
			newData[i][FILE_USER_ALIAS] = data_[i][FILE_USER_ALIAS];
			newData[i][FILE_DATE] = data_[i][FILE_DATE];
			newData[i][FILE_LENGTH] = data_[i][FILE_LENGTH];
		}

		// Append new sound data
		newData[numRows][FILE_ID] = sndInfo.id;
		newData[numRows][FILE_ALIAS] = sndInfo.alias;
		newData[numRows][FILE_NAME] = sndInfo.name;
		newData[numRows][FILE_MIME_TYPE] = sndInfo.mime_type;
		newData[numRows][FILE_USER_ALIAS] = userAlias;
		newData[numRows][FILE_DATE] = Utility.getDateAndTime(sndInfo.date);
		newData[numRows][FILE_LENGTH] = sndInfo.length;

		data_ = newData;

		// Signal the table that the module has changed
		this.fireTableStructureChanged();
	}

	// Method:
	public void append(RCT.SoundMsgHistSeqHolder soundMsgHistSeq) {

		int numRows = getRowCount();
		int numCols = getColumnCount();

		Object[][] newData = new Object[numRows + soundMsgHistSeq.value.length][numCols];

		// Copy old data
		for (int i = 0; i < numRows; i++) {

			newData[i][FILE_ID] = data_[i][FILE_ID];
			newData[i][FILE_ALIAS] = data_[i][FILE_ALIAS];
			newData[i][FILE_NAME] = data_[i][FILE_NAME];
			newData[i][FILE_MIME_TYPE] = data_[i][FILE_MIME_TYPE];
			newData[i][FILE_USER_ALIAS] = data_[i][FILE_USER_ALIAS];
			newData[i][FILE_DATE] = data_[i][FILE_DATE];
			newData[i][FILE_LENGTH] = data_[i][FILE_LENGTH];
		}

		for (int j = 0; j < soundMsgHistSeq.value.length; j++) {

			// Append new sound data
			newData[numRows + j][FILE_ID] = soundMsgHistSeq.value[j].id;
			newData[numRows + j][FILE_ALIAS] = soundMsgHistSeq.value[j].alias;
			newData[numRows + j][FILE_NAME] = soundMsgHistSeq.value[j].name;
			newData[numRows + j][FILE_MIME_TYPE] = soundMsgHistSeq.value[j].mime_type;
			newData[numRows + j][FILE_USER_ALIAS] = soundMsgHistSeq.value[j].user_alias;
			newData[numRows + j][FILE_DATE] = Utility
					.getDateAndTime(soundMsgHistSeq.value[j].date);
			newData[numRows + j][FILE_LENGTH] = soundMsgHistSeq.value[j].length;
		}

		data_ = newData;

		// Signal the table that the module has changed
		this.fireTableStructureChanged();
	}

	// Method:
	private void populateData() {

		soundSeq_ = new RCT.SoundMsgHistSeqHolder();

		SoundModule.getSoundInfoFromClassAndAssemblyName(className_,
				assemblyName_, pageType_, soundSeq_);

		// Check if we received any sounds
		if (0 == soundSeq_.value.length) {

			data_ = new Object[0][0];
		} else {

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

