// $Id: CourseContentData.java,v 1.5 2003/05/08 20:09:03 thomas Exp $

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
import org.openrct.client.module.CourseContentModule;
import org.openrct.client.util.Utility;
import javax.swing.table.*;

public class CourseContentData extends AbstractTableModel implements Const {

	private String className_ = "";

	private RCT.CourseContentSeqHolder courseContentSeq_ = null;

	private Object[][] data_ = null;

	// Constructor
	public CourseContentData(String className) {

		className_ = className;

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

		return CC_COLUMN_NAMES.length;
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

		return CC_COLUMN_NAMES[col];
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

		courseContentSeq_ = new RCT.CourseContentSeqHolder();

		CourseContentModule.getCourseContentFromClassName(className_,
				courseContentSeq_);

		// Check if we received any course content
		if (0 == courseContentSeq_.value.length) {

			data_ = new Object[0][0];
		} else {

			data_ = new Object[courseContentSeq_.value.length][CC_N_FIELDS];

			for (int i = 0; i < courseContentSeq_.value.length; i++) {
				data_[i][CC_ID] = courseContentSeq_.value[i].id;
				data_[i][CC_ALIAS] = courseContentSeq_.value[i].alias;
				data_[i][CC_LOCATION] = courseContentSeq_.value[i].location;
				data_[i][CC_NAME] = courseContentSeq_.value[i].name;
				data_[i][CC_SOURCE] = courseContentSeq_.value[i].source;
				data_[i][CC_MIME_TYPE] = courseContentSeq_.value[i].mime_type;
				data_[i][CC_CLASS_ID] = courseContentSeq_.value[i].class_id;
				data_[i][CC_VISIBLE] = courseContentSeq_.value[i].visible;
				data_[i][CC_PER] = courseContentSeq_.value[i].permission;
				data_[i][CC_LENGTH] = courseContentSeq_.value[i].length;
				data_[i][CC_DATE] = Utility
						.getDateAndTime(courseContentSeq_.value[i].date);
				data_[i][CC_VER] = courseContentSeq_.value[i].version;
			}
		}
	}
}

