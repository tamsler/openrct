// $Id: TextpadListData.java,v 1.4 2003/05/08 20:09:03 thomas Exp $

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
import org.openrct.client.module.TextpadModule;
import java.util.*;
import javax.swing.table.*;

public class TextpadListData extends AbstractTableModel implements Const {

	private Object[][] data_ = null;

	// Constructor
	public TextpadListData() {

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

		return TEXTPAD_LIST_COLUMN_NAMES.length;
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

		return TEXTPAD_LIST_COLUMN_NAMES[col];
	}

	// Abstract Method:
	public Class getColumnClass(int col) {

		return data_[0][col].getClass();
	}

	// Methods:
	// --------

	// Method:
	private void populateData() {

		int numTextpads = TextpadModule.editList.size();

		if (0 < numTextpads) {

			data_ = new Object[numTextpads][TEXTPAD_LIST_N_FIELDS];

		} else {

			data_ = new Object[0][0];
		}

		Enumeration enum = TextpadModule.editList.keys();

		for (int i = 0; enum.hasMoreElements(); i++) {

			String tpid = (String) enum.nextElement();

			TextpadInfo tpi = TextpadModule.editList.get(tpid);

			data_[i] = tpi.getAllData();
		}

	}
}

