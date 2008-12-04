// $Id: CustomDefaultRenderer.java,v 1.4 2003/05/08 20:09:03 thomas Exp $

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
import org.openrct.client.util.RctColor;
import java.awt.*;
import javax.swing.*;
import javax.swing.tree.*;

// This class is a custom renderer based on DefaultTreeCellRenderer
public class CustomDefaultRenderer extends DefaultTreeCellRenderer implements
		Const {

	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {

		// Allow the original renderer to set up the label
		Component c = super.getTreeCellRendererComponent(tree, value, selected,
				expanded, leaf, row, hasFocus);

		this.setBackgroundSelectionColor(RctColor.lavendar);

		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;

		Object nodeObject = node.getUserObject();

		if (nodeObject instanceof UserNode) {

			setIcon(new ImageIcon(ClassLoader.getSystemResource(USER_IMG)));
		} else if (nodeObject instanceof ClassNode) {

			setIcon(new ImageIcon(ClassLoader.getSystemResource(CLASS_IMG)));
		} else if (nodeObject instanceof TeamNode) {

			setIcon(new ImageIcon(ClassLoader.getSystemResource(TEAM_IMG)));
		} else if (nodeObject instanceof GroupNode) {

			setIcon(new ImageIcon(ClassLoader.getSystemResource(GROUP_IMG)));
		} else if (node.isRoot()) {

			// We don't do anything for the root node
		} else {

			System.err.println("ERROR: nodeObject not recognized!");
		}

		return c;
	}
}