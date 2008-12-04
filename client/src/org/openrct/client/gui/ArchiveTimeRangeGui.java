// $Id: ArchiveTimeRangeGui.java,v 1.5 2003/05/08 20:09:03 thomas Exp $

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
import org.openrct.client.util.Utility;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class ArchiveTimeRangeGui extends JPanel implements Const {

	private JLabel jLabel1 = new JLabel();

	private JPanel jPanel2 = new JPanel();

	private JPanel jPanel3 = new JPanel();

	private JPanel jPanel4 = new JPanel();

	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private GridLayout gridLayout1 = new GridLayout();

	private GridLayout gridLayout2 = new GridLayout();

	private TitledBorder titledBorder2;

	private JLabel jLabel2 = new JLabel();

	private JLabel jLabel3 = new JLabel();

	private JLabel jLabel4 = new JLabel();

	private JLabel jLabel5 = new JLabel();

	private JLabel jLabel6 = new JLabel();

	private JLabel jLabel7 = new JLabel();

	private JButton jButton1 = new JButton();

	private JButton jButton2 = new JButton();

	private JComboBox fromDayCB = new JComboBox(DAYS_OF_MONTH);

	private JComboBox fromMonthCB = new JComboBox(MONTHS_OF_YEAR_STR);

	private JComboBox fromYearCB = new JComboBox(YEARS);

	private JComboBox toDayCB = new JComboBox(DAYS_OF_MONTH);

	private JComboBox toMonthCB = new JComboBox(MONTHS_OF_YEAR_STR);

	private JComboBox toYearCB = new JComboBox(YEARS);

	private TitledBorder titledBorder3;

	private TitledBorder titledBorder4;

	private String className_;

	private String teamName_;

	private ArchivePageGui archivePage_;

	private int module_;

	// Constructor
	public ArchiveTimeRangeGui(String className, String teamName,
			ArchivePageGui archivePage, int module) {

		className_ = className;
		teamName_ = teamName;
		archivePage_ = archivePage;
		module_ = module;

		titledBorder2 = new TitledBorder("");
		titledBorder3 = new TitledBorder("");
		titledBorder4 = new TitledBorder("");

		this.setLayout(gridBagLayout1);

		jLabel1.setAlignmentX((float) 0.5);
		jLabel1.setText(LangModule.i18n.getString("ArchiveTimeRangeLabel1"));

		toDayCB.setBorder(BorderFactory.createLoweredBevelBorder());
		toMonthCB.setBorder(BorderFactory.createLoweredBevelBorder());
		toYearCB.setBorder(BorderFactory.createLoweredBevelBorder());

		jPanel2.setLayout(gridLayout1);

		gridLayout1.setColumns(3);
		gridLayout1.setHgap(5);
		gridLayout1.setRows(2);
		gridLayout1.setVgap(5);

		jLabel2.setText(LangModule.i18n.getString("ArchiveTimeRangeLabel2"));
		jLabel3.setText(LangModule.i18n.getString("ArchiveTimeRangeLabel3"));
		jLabel4.setText(LangModule.i18n.getString("ArchiveTimeRangeLabel4"));

		fromDayCB.setBorder(BorderFactory.createLoweredBevelBorder());
		fromMonthCB.setBorder(BorderFactory.createLoweredBevelBorder());
		fromYearCB.setBorder(BorderFactory.createLoweredBevelBorder());

		jPanel3.setLayout(gridLayout2);

		gridLayout2.setColumns(3);
		gridLayout2.setHgap(5);
		gridLayout2.setRows(2);
		gridLayout2.setVgap(5);

		jLabel5.setText(LangModule.i18n.getString("ArchiveTimeRangeLabel2"));
		jLabel6.setText(LangModule.i18n.getString("ArchiveTimeRangeLabel3"));
		jLabel7.setText(LangModule.i18n.getString("ArchiveTimeRangeLabel4"));

		jButton1.setText(LangModule.i18n.getString("ButtonLabelCancel"));
		jButton1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				pressedCancelButton();
			}
		});

		jButton2.setText(LangModule.i18n.getString("ButtonLabelOk"));
		jButton2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				pressedOkButton();
			}
		});

		jPanel2.setBorder(titledBorder3);

		titledBorder3.setTitle(LangModule.i18n
				.getString("ArchiveTimeRangeBorder1"));
		titledBorder3.setTitleJustification(2);

		jPanel3.setBorder(titledBorder4);

		titledBorder4.setTitle(LangModule.i18n
				.getString("ArchiveTimeRangeBorder2"));
		titledBorder4.setTitleJustification(2);

		this.add(jLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 40));

		this.add(jPanel2, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						5, 5, 5, 5), 0, 0));

		jPanel2.add(jLabel4);
		jPanel2.add(jLabel3);
		jPanel2.add(jLabel2);
		jPanel2.add(fromDayCB);
		jPanel2.add(fromMonthCB);
		jPanel2.add(fromYearCB);

		this.add(jPanel3, new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						5, 5, 5, 5), 0, 0));

		jPanel3.add(jLabel7);
		jPanel3.add(jLabel6);
		jPanel3.add(jLabel5);
		jPanel3.add(toDayCB);
		jPanel3.add(toMonthCB);
		jPanel3.add(toYearCB);

		this.add(jPanel4, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(15, 5, 5, 5), 0, 0));
		jPanel4.add(jButton2);
		jPanel4.add(jButton1);
	}

	// Callbacks:
	// ----------

	// Callback:
	public void pressedCancelButton() {

		archivePage_.closePage();
	}

	// Callback:
	public void pressedOkButton() {

		// Error checking if a valid date was selected
		// for either the from or to date

		// Check From Date
		if (!Utility.isDateValid(MONTHS_OF_YEAR_NUM[fromMonthCB
				.getSelectedIndex()], (String) fromDayCB.getSelectedItem(),
				(String) fromYearCB.getSelectedItem())) {

			JOptionPane.showMessageDialog(this, LangModule.i18n
					.getString("ArchiveTimeRangeError2"), LangModule.i18n
					.getString("ArchiveTimeRangeDialog1"),
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Check To Date
		if (!Utility.isDateValid(MONTHS_OF_YEAR_NUM[toMonthCB
				.getSelectedIndex()], (String) toDayCB.getSelectedItem(),
				(String) toYearCB.getSelectedItem())) {

			JOptionPane.showMessageDialog(this, LangModule.i18n
					.getString("ArchiveTimeRangeError3"), LangModule.i18n
					.getString("ArchiveTimeRangeDialog1"),
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Error checking for selected from and to date
		if (fromYearCB.getSelectedIndex() > toYearCB.getSelectedIndex()) {
			JOptionPane.showMessageDialog(this, LangModule.i18n
					.getString("ArchiveTimeRangeError1"), LangModule.i18n
					.getString("ArchiveTimeRangeDialog1"),
					JOptionPane.ERROR_MESSAGE);

			return;
		} else if ((fromYearCB.getSelectedIndex() == toYearCB
				.getSelectedIndex())
				&& (fromMonthCB.getSelectedIndex() > toMonthCB
						.getSelectedIndex())) {
			JOptionPane.showMessageDialog(this, LangModule.i18n
					.getString("ArchiveTimeRangeError1"), LangModule.i18n
					.getString("ArchiveTimeRangeDialog1"),
					JOptionPane.ERROR_MESSAGE);

			return;
		} else if ((fromYearCB.getSelectedIndex() == toYearCB
				.getSelectedIndex())
				&& (fromMonthCB.getSelectedIndex() == toMonthCB
						.getSelectedIndex())
				&& (fromDayCB.getSelectedIndex() > toDayCB.getSelectedIndex())) {
			JOptionPane.showMessageDialog(this, LangModule.i18n
					.getString("ArchiveTimeRangeError1"), LangModule.i18n
					.getString("ArchiveTimeRangeDialog1"),
					JOptionPane.ERROR_MESSAGE);

			return;
		}

		//Compose the from and to date
		String fromDate = fromYearCB.getSelectedItem() + "-"
				+ MONTHS_OF_YEAR_NUM[fromMonthCB.getSelectedIndex()] + "-"
				+ fromDayCB.getSelectedItem() + " 00:00:00";

		String toDate = toYearCB.getSelectedItem() + "-"
				+ MONTHS_OF_YEAR_NUM[toMonthCB.getSelectedIndex()] + "-"
				+ toDayCB.getSelectedItem() + " 23:59:59";

		switch (module_) {

		case ARCH_MOD_CHAT:

			archivePage_
					.displaySelectionOrDataPage(new ArchiveChatTimeRangePageGui(
							className_, teamName_, archivePage_, fromDate,
							toDate));

			break;

		case ARCH_MOD_SOUND:

			archivePage_
					.displaySelectionOrDataPage(new ArchiveSoundTimeRangePageGui(
							className_, teamName_, archivePage_, fromDate,
							toDate));

			break;

		case ARCH_MOD_TEXTPAD:

			archivePage_
					.displaySelectionOrDataPage(new ArchiveTextpadTimeRangePageGui(
							className_, teamName_, archivePage_, fromDate,
							toDate));
			break;

		case ARCH_MOD_FTP:

			archivePage_
					.displaySelectionOrDataPage(new ArchiveFtpTimeRangePageGui(
							className_, teamName_, archivePage_, fromDate,
							toDate));
			break;
		default:

			System.err.println("ERROR: Did not recognize module!");
		}
	}
}

