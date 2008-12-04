// $Id: TDNode.java,v 1.9 2003/07/01 17:48:13 thomas Exp $

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

class TDNode implements Const {

	// TDNode vars
	private int postId_;

	private int parentId_;

	private boolean isRead_;

	private String subject_;

	private String sender_;

	private String type_;

	private int replies_;

	private String date_;

	// Parent TDNode
	private TDNode parent_;

	// Children
	private Vector children_;

	// Constructor:
	private TDNode() {
	};

	// Constructor:
	//
	public TDNode(boolean isRead, TDNode parent, int parentId, int postId,
			String subject, String sender, String type, String date) {

		parent_ = parent;
		parentId_ = parentId;
		postId_ = postId;

		children_ = new Vector();

		isRead_ = isRead;
		subject_ = subject;
		sender_ = sender;
		type_ = type;
		replies_ = 0;
		date_ = date;
	}

	// Methods:
	// --------

	// Method:
	//
	public int getPostId() {

		return postId_;
	}

	// Method:
	//
	public int getParentId() {

		return parentId_;
	}

	// Method:
	//
	public void setParent(TDNode parent) {

		parent_ = parent;
	}

	// Method:
	public void incrementReplyCount() {

		replies_++;

		// Also increment all parents and test
		// for when to stop at root node whose parent is null
		if (null != parent_) {

			parent_.incrementReplyCount();
		}
	}

	// Method:
	// Get the IDs of direct upwards related nodes
	public String getThreadIDs(String relation) {

		// Test if we reach ROOT parent
		if (null == parent_) {

			return relation;
		} else {

			return parent_.getThreadIDs(postId_ + "#" + sender_ + "#"
					+ subject_ + ":" + relation);
		}
	}

	// Method:
	public void read() {

		isRead_ = true;
	}

	// Method:
	public boolean isRead() {

		return isRead_;
	}

	// Method:
	//
	public String toString() {

		return subject_;
	}

	// Method:
	//
	public boolean isLeaf() {

		return (0 == numChildren()) ? true : false;
	}

	// Method:
	//
	// Return the parent
	//
	public TDNode getParent() {

		return parent_;
	}

	// Method:
	//
	// Return the children
	//
	public TDNode[] getChildren() {

		int numChildren = numChildren();

		TDNode[] tmpArr = new TDNode[numChildren];

		return (TDNode[]) children_.toArray((Object[]) tmpArr);
	}

	// Method:
	//
	public int numChildren() {

		return (null == children_) ? 0 : children_.size();
	}

	// Method:
	//
	public void appendChild(TDNode tdNode) {

		children_.add(tdNode);

		// Increment the parent reply count
		if ((null != parent_) && (this != parent_)) {

			this.incrementReplyCount();
		}
	}

	// Method:
	//
	public void appendChildren(TDNode[] tdNodes) {

		for (int i = 0; i < tdNodes.length; i++) {

			this.appendChild(tdNodes[i]);

			// Increment the parent reply count
			if ((null != parent_) && (this != parent_)) {

				this.incrementReplyCount();
			}
		}
	}

	// Method:
	//
	public Object getValueAt(int field) {

		switch (field) {

		case STATUS:
			return new Boolean(!isRead_);

		case SUBJECT:
			return subject_;

		case SENDER:
			return sender_;

		case TYPE:
			return type_;

		case REPLIES:
			return new Integer(replies_);

		case DATE:
			return date_;

		default:

			return null;
		}
	}
}