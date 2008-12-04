// $Id: TDModel.java,v 1.8 2003/06/13 17:21:17 thomas Exp $

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

import org.openrct.client.module.LangModule;

public class TDModel extends AbstractTreeTableModel {

	// Names of the columns.
	static protected String[] cNames = {
			LangModule.i18n.getString("TDModelNew"),
			LangModule.i18n.getString("TDModelSubject"),
			LangModule.i18n.getString("TDModelSender"),
			LangModule.i18n.getString("TDModelType"),
			LangModule.i18n.getString("TDModelReplies"),
			LangModule.i18n.getString("TDModelDate") };

	// Types of the columns.
	// String.class for first node
	static protected Class[] cTypes = { Boolean.class, TreeTableModel.class,
			String.class, String.class, Integer.class, String.class };

	// Constructor
	public TDModel() {

		super(null);

		// Just create the root node
		root = new TDNode(true, null, 0, 0, "", "", "", "");
	}

	// Methods:
	// --------

	// Method:
	//
	public int getChildCount(Object node) {

		Object[] children = getChildren(node);

		return (children == null) ? 0 : children.length;
	}

	// Method:
	//
	public Object getChild(Object node, int i) {

		return getChildren(node)[i];
	}

	// Method:
	//
	public boolean isLeaf(Object node) {

		return ((TDNode) node).isLeaf();
	}

	// Method:
	//
	public int getColumnCount() {

		return cNames.length;
	}

	// Method:
	//
	public String getColumnName(int column) {

		return cNames[column];
	}

	// Method:
	//
	public Class getColumnClass(int column) {
		return cTypes[column];
	}

	// Method:
	//
	public Object getValueAt(Object node, int column) {

		TDNode tdNode = (TDNode) node;

		return tdNode.getValueAt(column);
	}

	// Method:
	//
	private Object[] getChildren(Object node) {

		TDNode tdNode = ((TDNode) node);

		return (Object[]) tdNode.getChildren();
	}
}