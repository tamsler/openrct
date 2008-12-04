// $Id: TreeTableModel.java,v 1.1 2003/05/14 21:05:49 thomas Exp $
//
// TreeTableModel Class

/*
 * Copyright (c) 1998 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * This software is the confidential and proprietary information of Sun
 * Microsystems, Inc. ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the terms
 * of the license agreement you entered into with Sun.
 * 
 * SUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, OR
 * NON-INFRINGEMENT. SUN SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY
 * LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES.
 */

package org.openrct.client.gui;

import javax.swing.tree.TreeModel;

public interface TreeTableModel extends TreeModel {

	/**
	 * Returns the number ofs availible column.
	 */
	public int getColumnCount();

	/**
	 * Returns the name for column number <code>column</code>.
	 */
	public String getColumnName(int column);

	/**
	 * Returns the type for column number <code>column</code>.
	 */
	public Class getColumnClass(int column);

	/**
	 * Returns the value to be displayed for node <code>node</code>, at
	 * column number <code>column</code>.
	 */
	public Object getValueAt(Object node, int column);

	/**
	 * Indicates whether the the value for node <code>node</code>, at column
	 * number <code>column</code> is editable.
	 */
	public boolean isCellEditable(Object node, int column);

	/**
	 * Sets the value for node <code>node</code>, at column number
	 * <code>column</code>.
	 */
	public void setValueAt(Object aValue, Object node, int column);
}