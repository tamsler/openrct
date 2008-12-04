// $Id: JTreeTable.java,v 1.11 2003/06/13 17:21:17 thomas Exp $
//
// JTreeTable Class

/*
 * Copyright 1997, 1998 by Sun Microsystems, Inc., 901 San Antonio Road, Palo
 * Alto, California, 94303, U.S.A. All rights reserved.
 * 
 * This software is the confidential and proprietary information of Sun
 * Microsystems, Inc. ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the terms
 * of the license agreement you entered into with Sun.
 */

package org.openrct.client.gui;

import org.openrct.client.Const;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import javax.swing.table.*;

import java.awt.Dimension;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import java.util.EventObject;

// JTreeTable Class
public class JTreeTable extends JTable implements Const {

	// A subclass of JTree
	protected TreeTableCellRenderer tree;

	// Constructor
	public JTreeTable(TreeTableModel treeTableModel) {

		super();

		// Set properties for the table
		this.setFont(UNICODE_FONT_12);

		// Create the tree. It will be used as a renderer and editor.
		tree = new TreeTableCellRenderer(treeTableModel);

		// Install a tableModel representing the visible rows in the tree.
		super.setModel(new TreeTableModelAdapter(treeTableModel, tree));

		// Force the JTable and JTree to share their row selection models.
		ListToTreeSelectionModelWrapper selectionWrapper = new ListToTreeSelectionModelWrapper();

		tree.setSelectionModel(selectionWrapper);
		setSelectionModel(selectionWrapper.getListSelectionModel());

		// Install the tree editor renderer and editor.
		setDefaultRenderer(TreeTableModel.class, tree);
		setDefaultEditor(TreeTableModel.class, new TreeTableCellEditor());

		// No grid.
		setShowGrid(false);

		// No intercell spacing
		setIntercellSpacing(new Dimension(0, 0));

		// And update the height of the trees row to match that of
		// the table.
		if (tree.getRowHeight() < 1) {
			// Metal looks better like this.
			setRowHeight(TD_ROW_HEIGHT);
		}

		// Set the first visible column to 100 pixels wide
		this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		TableColumn col;

		DefaultTableCellRenderer myColumnRenderer = new DefaultTableCellRenderer();
		myColumnRenderer.setHorizontalAlignment(SwingConstants.CENTER);

		// Status
		col = this.getColumnModel().getColumn(STATUS);
		col.setPreferredWidth(40);

		// Subject
		col = this.getColumnModel().getColumn(SUBJECT);
		col.setPreferredWidth(350);

		// Sender
		col = this.getColumnModel().getColumn(SENDER);
		col.setPreferredWidth(150);
		col.setCellRenderer(myColumnRenderer);

		// Type
		col = this.getColumnModel().getColumn(TYPE);
		col.setPreferredWidth(80);
		col.setCellRenderer(myColumnRenderer);

		// Replies
		col = this.getColumnModel().getColumn(REPLIES);
		col.setPreferredWidth(80);
		col.setCellRenderer(myColumnRenderer);

		// Date
		col = this.getColumnModel().getColumn(DATE);
		col.setPreferredWidth(200);
		col.setCellRenderer(myColumnRenderer);
		this.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
	}

	// Methods:
	// --------

	// Method:
	//
	// Expand all nodes
	public void expandAllRows() {

		int row = 0;

		while (row < tree.getRowCount()) {
			tree.expandRow(row++);
		}
	}

	// Method:
	//
	public void collapsAllRows() {

		int row = 0;

		while (row < tree.getRowCount()) {
			tree.collapseRow(row++);
		}
	}

	// Method:
	//
	// Extract data from selected row
	// Caller should test for null return value
	public Object getSelectedObject() {

		TreePath treePath = tree.getSelectionPath();

		if (null != treePath) {

			return (TDNode) treePath.getLastPathComponent();
		} else {

			return null;
		}
	}

	// Method:
	// The boolean argument indicates if this is a new or reply msg insert
	// isTopLevel == true -> New Message
	// isTopLevel == false -> Reply Message
	public void insert(TDNode tdNode, boolean isTopLevel) {

		// Test if node needs to be inserted at top-level / root

		// Case New Msg
		if (isTopLevel) {

			TDNode root = (TDNode) tree.getModel().getRoot();
			tdNode.setParent(root);
			root.appendChild(tdNode);
		} else {
			// Case Reply Msg
			// Insert node
			walk(tree.getModel(), tree.getModel().getRoot(), tdNode);
		}

		// Update GUI
		updateUI();
	}

	// Method:
	//
	public void walk(TreeModel model, Object rootNode, Object node) {

		int cc = model.getChildCount(rootNode);

		for (int i = 0; i < cc; i++) {

			Object child = model.getChild(rootNode, i);

			if (model.isLeaf(child)) {

				if (((TDNode) node).getParentId() == ((TDNode) child)
						.getPostId()) {

					((TDNode) node).setParent((TDNode) child);
					((TDNode) child).appendChild((TDNode) node);
				}
			} else {

				if (((TDNode) node).getParentId() == ((TDNode) child)
						.getPostId()) {

					((TDNode) node).setParent((TDNode) child);
					((TDNode) child).appendChild((TDNode) node);
				}

				walk(model, child, node);
			}
		}
	}

	// Method:
	// 
	// Overridden to message super and forward the method to the tree.
	// Since the tree is not actually in the component hieachy it will
	// never receive this unless we forward it in this manner.
	//
	public void updateUI() {

		super.updateUI();

		if (tree != null) {

			tree.updateUI();
		}

		// Use the tree's default foreground and background colors in the
		// table.
		LookAndFeel.installColorsAndFont(this, "Tree.background",
				"Tree.foreground", "Tree.font");
	}

	// Method:
	//
	// Workaround for BasicTableUI anomaly. Make sure the UI never tries to
	// paint the editor. The UI currently uses different techniques to
	// paint the renderers and editors and overriding setBounds() below
	// is not the right thing to do for an editor. Returning -1 for the
	// editing row in this case, ensures the editor is never painted.
	//
	public int getEditingRow() {

		return (getColumnClass(editingColumn) == TreeTableModel.class) ? -1
				: editingRow;
	}

	// Method:
	//
	// Overridden to pass the new rowHeight to the tree.
	//
	public void setRowHeight(int rowHeight) {

		super.setRowHeight(rowHeight);

		if (tree != null && tree.getRowHeight() != rowHeight) {

			tree.setRowHeight(getRowHeight());
		}
	}

	// Method:
	//
	// Returns the tree that is being shared between the model.
	//
	public JTree getTree() {

		return tree;
	}

	/**
	 * A TreeCellRenderer that displays a JTree.
	 */
	public class TreeTableCellRenderer extends JTree implements
			TableCellRenderer {

		/** Last table/tree row asked to renderer. */
		protected int visibleRow;

		public TreeTableCellRenderer(TreeModel model) {

			super(model);

			// Set properties for the Tree
			this.setRowHeight(TD_ROW_HEIGHT);

			// Set the tree node style
			this.putClientProperty("JTree.lineStyle", "Angled");

			// Set font properties
			this.setFont(UNICODE_FONT_12);

			// Show or hide the root node
			this.setRootVisible(false);
		}

		/**
		 * updateUI is overridden to set the colors of the Tree's renderer to
		 * match that of the table.
		 */
		public void updateUI() {

			try {

				super.updateUI();
				// Make the tree's cell renderer use the table's cell selection
				// colors.

				TreeCellRenderer tcr = getCellRenderer();

				if (tcr instanceof DefaultTreeCellRenderer) {

					DefaultTreeCellRenderer dtcr = ((DefaultTreeCellRenderer) tcr);

					dtcr.setTextSelectionColor(UIManager
							.getColor("Table.selectionForeground"));

					dtcr.setBackgroundSelectionColor(UIManager
							.getColor("Table.selectionBackground"));
				}
			} catch (Exception e) {

				e.printStackTrace();
			}
		}

		/**
		 * Sets the row height of the tree, and forwards the row height to the
		 * table.
		 */
		public void setRowHeight(int rowHeight) {

			if (rowHeight > 0) {

				super.setRowHeight(rowHeight);

				if (JTreeTable.this != null
						&& JTreeTable.this.getRowHeight() != rowHeight) {

					JTreeTable.this.setRowHeight(getRowHeight());
				}
			}
		}

		/**
		 * This is overridden to set the height to match that of the JTable.
		 */
		public void setBounds(int x, int y, int w, int h) {

			super.setBounds(x, 0, w, JTreeTable.this.getHeight());
		}

		/**
		 * Sublcassed to translate the graphics such that the last visible row
		 * will be drawn at 0,0.
		 */
		public void paint(Graphics g) {

			try {
				g.translate(0, -visibleRow * getRowHeight());
				super.paint(g);
			} catch (Exception e) {

				e.printStackTrace();
			}
		}

		/**
		 * TreeCellRenderer method. Overridden to update the visible row.
		 */
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {

			if (isSelected) {

				setBackground(table.getSelectionBackground());
			} else {

				setBackground(table.getBackground());
			}

			visibleRow = row;
			return this;
		}
	}

	/**
	 * TreeTableCellEditor implementation. Component returned is the JTree.
	 */
	public class TreeTableCellEditor extends AbstractCellEditor implements
			TableCellEditor {

		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int r, int c) {
			return tree;
		}

		/**
		 * Overridden to return false, and if the event is a mouse event it is
		 * forwarded to the tree.
		 * <p>
		 * The behavior for this is debatable, and should really be offered as a
		 * property. By returning false, all keyboard actions are implemented in
		 * terms of the table. By returning true, the tree would get a chance to
		 * do something with the keyboard events. For the most part this is ok.
		 * But for certain keys, such as left/right, the tree will
		 * expand/collapse where as the table focus should really move to a
		 * different column. Page up/down should also be implemented in terms of
		 * the table. By returning false this also has the added benefit that
		 * clicking outside of the bounds of the tree node, but still in the
		 * tree column will select the row, whereas if this returned true that
		 * wouldn't be the case.
		 * <p>
		 * By returning false we are also enforcing the policy that the tree
		 * will never be editable (at least by a key sequence).
		 */
		public boolean isCellEditable(EventObject e) {

			if (e instanceof MouseEvent) {

				for (int counter = getColumnCount() - 1; counter >= 0; counter--) {

					if (getColumnClass(counter) == TreeTableModel.class) {

						MouseEvent me = (MouseEvent) e;

						MouseEvent newME = new MouseEvent(tree, me.getID(), me
								.getWhen(), me.getModifiers(), me.getX()
								- getCellRect(0, counter, true).x, me.getY(),
								me.getClickCount(), me.isPopupTrigger());

						tree.dispatchEvent(newME);

						break;
					}
				}
			}
			return false;
		}
	}

	/**
	 * ListToTreeSelectionModelWrapper extends DefaultTreeSelectionModel to
	 * listen for changes in the ListSelectionModel it maintains. Once a change
	 * in the ListSelectionModel happens, the paths are updated in the
	 * DefaultTreeSelectionModel.
	 */
	class ListToTreeSelectionModelWrapper extends DefaultTreeSelectionModel {

		/** Set to true when we are updating the ListSelectionModel. */
		protected boolean updatingListSelectionModel;

		public ListToTreeSelectionModelWrapper() {

			super();

			getListSelectionModel().addListSelectionListener(
					createListSelectionListener());

			// Set selection mode to SINGLE
			setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		}

		/**
		 * Returns the list selection model. ListToTreeSelectionModelWrapper
		 * listens for changes to this model and updates the selected paths
		 * accordingly.
		 */
		ListSelectionModel getListSelectionModel() {

			return listSelectionModel;
		}

		/**
		 * This is overridden to set <code>updatingListSelectionModel</code>
		 * and message super. This is the only place DefaultTreeSelectionModel
		 * alters the ListSelectionModel.
		 */
		public void resetRowSelection() {

			if (!updatingListSelectionModel) {

				updatingListSelectionModel = true;

				try {
					super.resetRowSelection();
				} finally {
					updatingListSelectionModel = false;
				}
			}
			// Notice how we don't message super if
			// updatingListSelectionModel is true. If
			// updatingListSelectionModel is true, it implies the
			// ListSelectionModel has already been updated and the
			// paths are the only thing that needs to be updated.
		}

		/**
		 * Creates and returns an instance of ListSelectionHandler.
		 */
		protected ListSelectionListener createListSelectionListener() {

			return new ListSelectionHandler();
		}

		/**
		 * If <code>updatingListSelectionModel</code> is false, this will
		 * reset the selected paths from the selected rows in the list selection
		 * model.
		 */
		protected void updateSelectedPathsFromSelectedRows() {

			if (!updatingListSelectionModel) {

				updatingListSelectionModel = true;

				try {

					// This is way expensive, ListSelectionModel needs an
					// enumerator for iterating.
					int min = listSelectionModel.getMinSelectionIndex();
					int max = listSelectionModel.getMaxSelectionIndex();

					clearSelection();

					if (min != -1 && max != -1) {

						for (int counter = min; counter <= max; counter++) {

							if (listSelectionModel.isSelectedIndex(counter)) {

								TreePath selPath = tree.getPathForRow(counter);

								if (selPath != null) {

									addSelectionPath(selPath);
								}
							}
						}
					}
				} finally {

					updatingListSelectionModel = false;
				}
			}
		}

		/**
		 * Class responsible for calling updateSelectedPathsFromSelectedRows
		 * when the selection of the list changse.
		 */
		class ListSelectionHandler implements ListSelectionListener {

			public void valueChanged(ListSelectionEvent e) {
				updateSelectedPathsFromSelectedRows();
			}
		}
	}
}