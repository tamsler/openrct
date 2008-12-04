// $Id: ArchiveSelectionGui.java,v 1.4 2003/05/08 20:09:03 thomas Exp $

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
import org.openrct.client.module.LangModule;
import org.openrct.client.module.ConfigModule;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class ArchiveSelectionGui extends JPanel implements Const {

	private JPanel selectionPanel = new JPanel();

	private JButton nextButton = new JButton();

	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private GridBagLayout gridBagLayout2 = new GridBagLayout();

	private JComboBox modulesComboBox = new JComboBox(ARCHIVE_MODULES);

	private JComboBox selectionComboBox = new JComboBox(ARCHIVE_DATA_SELECTION);

	private JLabel jLabel1 = new JLabel();

	private JLabel jLabel2 = new JLabel();

	private JTextArea textArea = new JTextArea();

	private TitledBorder titledBorder;

	private String className_;

	private String teamName_;

	private ArchivePageGui archivePage_;

	// Constructor
	public ArchiveSelectionGui(String className, String teamName,
			ArchivePageGui archivePage) {

		className_ = className;
		teamName_ = teamName;
		archivePage_ = archivePage;

		this.setLayout(gridBagLayout1);

		titledBorder = new TitledBorder("");
		titledBorder.setTitle(LangModule.i18n
				.getString("ArchiveSelectionBorder1"));
		titledBorder.setTitleJustification(2);

		nextButton.setText(LangModule.i18n
				.getString("ArchiveSelectionButtonNext"));
		nextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				pressedNextButton();
			}
		});
		selectionPanel.setLayout(gridBagLayout2);

		jLabel1.setText(LangModule.i18n.getString("ArchiveSelectionLabel1"));
		jLabel2.setText(LangModule.i18n.getString("ArchiveSelectionLabel2"));

		textArea.setBorder(titledBorder);
		textArea.setText(LangModule.i18n.getString("ArchiveSelectionText1"));
		textArea.append(LangModule.i18n.getString("ArchiveSelectionText2"));
		textArea.append(LangModule.i18n.getString("ArchiveSelectionText3"));

		textArea.setFont(new Font(ConfigModule.getString(CONF_UNICODE_FONT),
				Font.PLAIN, ConfigModule.getNumber(CONF_UNICODE_FONT_SIZE)));
		textArea.setEditable(false);

		this.add(textArea, new GridBagConstraints(0, 0, 1, 1, 0.5, 0.5,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						10, 10, 10, 10), 0, 0));

		this.add(selectionPanel, new GridBagConstraints(1, 0, 1, 1, 0.5, 0.5,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						5, 5, 5, 5), 0, 0));

		selectionPanel.add(jLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(60, 5, 5, 5), 0, 0));

		selectionPanel.add(modulesComboBox, new GridBagConstraints(0, 1, 1, 1,
				0.5, 0.5, GridBagConstraints.NORTH, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));

		selectionPanel.add(jLabel2, new GridBagConstraints(0, 2, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));

		selectionPanel.add(selectionComboBox, new GridBagConstraints(0, 3, 1,
				1, 0.5, 0.5, GridBagConstraints.NORTH, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));

		selectionPanel.add(nextButton, new GridBagConstraints(0, 4, 1, 1, 0.0,
				0.0, GridBagConstraints.NORTH, GridBagConstraints.NONE,
				new Insets(5, 0, 40, 0), 0, 0));
	}

	// Callbacks:
	// ----------

	// Callback:
	public void pressedNextButton() {

		int mod_index = modulesComboBox.getSelectedIndex();
		int sel_index = selectionComboBox.getSelectedIndex();

		switch (sel_index) {

		case ARCH_SEL_TODAY:

			archivePage_.displaySelectionOrDataPage(ArchiveSelectionTodayGui
					.getPage(className_, teamName_, archivePage_, mod_index));
			break;

		case ARCH_SEL_LASTN:

			archivePage_.displaySelectionOrDataPage(ArchiveSelectionLastNGui
					.getPage(className_, teamName_, archivePage_, mod_index));
			break;

		case ARCH_SEL_TIMERANGE:

			archivePage_
					.displaySelectionOrDataPage(ArchiveSelectionTimeRangeGui
							.getPage(className_, teamName_, archivePage_,
									mod_index));
			break;

		default:

			System.err.println("ERROR: Did not recognize selection!");
		}
	}
}

