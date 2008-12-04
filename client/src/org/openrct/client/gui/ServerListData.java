// $Id: ServerListData.java,v 1.3 2003/05/08 20:09:03 thomas Exp $

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
import java.util.*;
import java.net.*;
import java.io.*;
import javax.swing.table.*;

public class ServerListData extends AbstractTableModel implements Const {

	private Object[][] data_ = null;

	// Constructor
	public ServerListData() {

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

		return SERVER_LIST_COLUMN_NAMES.length;
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

		return SERVER_LIST_COLUMN_NAMES[col];
	}

	// Abstract Method:
	public Class getColumnClass(int col) {

		return data_[0][col].getClass();
	}

	// Methods:
	// --------

	// Method:
	private void populateData() {

		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;

		try {

			URL url = new URL(SERVER_LIST_URL);
			inputStream = url.openStream();

			inputStreamReader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(inputStreamReader);

			// Setting a mark at the beginning of the buffer
			bufferedReader.mark(SERVER_LIST_MARK);

			// Determine the number of lines/server-entries there are
			String line;
			int numberOfLines = 0;
			int numberOfVersionMatch = 0;

			while (((line = bufferedReader.readLine()) != null)
					&& (line.length() > 0)) {

				// Counting all the server lines
				numberOfLines++;

				// Count only the liens with matching client version
				if (line.startsWith(RCT_VERSION)) {
					numberOfVersionMatch++;
				}
			}

			// Allocate memory for the data
			if (numberOfVersionMatch > 0) {

				data_ = new Object[numberOfVersionMatch][SERVER_LIST_N_FIELDS];
			} else {

				data_ = new Object[0][0];
			}

			// Resetting to the start of the buffer
			bufferedReader.reset();

			int index = 0;
			// Now we read each line/server-entry and add it to the data array
			for (int i = 0; i < numberOfLines; i++) {

				// Get the line
				line = bufferedReader.readLine();

				// Parse the line and fill into data array
				String[] serverData = getServerData(line);

				// Make sure that we only add servers that match the client's
				// version
				if (RCT_VERSION.equals(serverData[SERVER_LIST_VERSION])) {
					data_[index] = serverData;
					index++;
				}
			}

			// Close InputStream
			inputStream.close();
		} catch (Exception e) {

			// In case the ServerList server is down,
			// we just return an empty data array.
			if (null == data_) {

				data_ = new Object[0][0];
			}
		}
	}

	// Method
	// Extract fields from ServerList strings
	private String[] getServerData(String line) {

		StringTokenizer st = new StringTokenizer(line, SERVER_LIST_DELIM);

		int numberOfFields = st.countTokens();

		String[] serverDataFields = new String[numberOfFields];

		for (int i = 0; st.hasMoreTokens(); i++) {

			serverDataFields[i] = st.nextToken();
		}

		return serverDataFields;
	}
}

