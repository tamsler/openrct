// $Id: ArchiveLastNGui.java,v 1.4 2003/05/08 20:09:03 thomas Exp $

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
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class ArchiveLastNGui extends JPanel implements Const {

	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private GridLayout gridLayout1 = new GridLayout();

	private JLabel jLabel1 = new JLabel();

	private JSlider jSlider1 = new JSlider(JSlider.HORIZONTAL, MIN_N_MSG,
			MAX_N_MSG, TICK_N_MSG);

	private JPanel jPanel2 = new JPanel();

	private JPanel jPanel3 = new JPanel();

	private JButton jButton2 = new JButton();

	private JButton jButton3 = new JButton();

	private ChangeListener listener = new SliderListener();

	private JTextField nMsgSelectedTextField = new JTextField();

	private int nTicks_ = TICK_N_MSG;

	private String className_;

	private String teamName_;

	private ArchivePageGui archivePage_;

	private int module_;

	// Constructor
	public ArchiveLastNGui(String className, String teamName,
			ArchivePageGui archivePage, int module) {

		className_ = className;
		teamName_ = teamName;
		archivePage_ = archivePage;
		module_ = module;

		nMsgSelectedTextField.setText(LangModule.i18n
				.getString("ArchiveLastNTF1")
				+ " "
				+ TICK_N_MSG
				+ " "
				+ LangModule.i18n.getString("ArchiveLastNTF2"));
		nMsgSelectedTextField.setEditable(false);

		jSlider1.addChangeListener(listener);
		jSlider1.setPaintTicks(true);
		jSlider1.setMajorTickSpacing(5);
		jSlider1.setMinorTickSpacing(1);
		jSlider1.putClientProperty("JSlider.isFilled", Boolean.TRUE);
		jSlider1.setPaintLabels(true);

		this.setLayout(gridBagLayout1);

		jLabel1.setText(LangModule.i18n.getString("ArchiveLastNLabel1"));

		jPanel2.setLayout(gridLayout1);

		gridLayout1.setColumns(1);
		gridLayout1.setHgap(5);
		gridLayout1.setRows(2);
		gridLayout1.setVgap(5);

		jButton2.setText(LangModule.i18n.getString("ButtonLabelCancel"));
		jButton2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				pressedCancelButton();
			}
		});

		jButton3.setText(LangModule.i18n.getString("ButtonLabelOk"));
		jButton3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				pressedOkButton();

			}
		});

		this.add(jLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 5, 5, 5), 0, 40));

		this.add(jPanel2, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0));

		jPanel2.add(nMsgSelectedTextField);
		jPanel2.add(jSlider1);

		this.add(jPanel3, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0));
		jPanel3.add(jButton3);
		jPanel3.add(jButton2);

	}

	// Callbacks:
	// ----------

	// Callback:
	public void pressedCancelButton() {

		archivePage_.closePage();
	}

	// Callback:
	public void pressedOkButton() {

		switch (module_) {

		case ARCH_MOD_CHAT:

			archivePage_
					.displaySelectionOrDataPage(new ArchiveChatLastNPageGui(
							className_, teamName_, archivePage_, nTicks_));
			break;

		case ARCH_MOD_SOUND:

			archivePage_
					.displaySelectionOrDataPage(new ArchiveSoundLastNPageGui(
							className_, teamName_, archivePage_, nTicks_));
			break;

		case ARCH_MOD_TEXTPAD:

			archivePage_
					.displaySelectionOrDataPage(new ArchiveTextpadLastNPageGui(
							className_, teamName_, archivePage_, nTicks_));
			break;

		case ARCH_MOD_FTP:

			archivePage_.displaySelectionOrDataPage(new ArchiveFtpLastNPageGui(
					className_, teamName_, archivePage_, nTicks_));
			break;

		default:

			System.err.println("ERROR: Did not recognize module!");
		}
	}

	// Classes:
	// --------

	// Class:
	class SliderListener implements ChangeListener {

		public SliderListener() {
			// Nothing to do
		}

		public void stateChanged(ChangeEvent e) {

			JSlider source = (JSlider) e.getSource();

			nMsgSelectedTextField.setText(LangModule.i18n
					.getString("ArchiveLastNTF1")
					+ " "
					+ source.getValue()
					+ " "
					+ LangModule.i18n.getString("ArchiveLastNTF2"));

			if (source.getValueIsAdjusting() == false) {

				nTicks_ = source.getValue();

				nMsgSelectedTextField.setText(LangModule.i18n
						.getString("ArchiveLastNTF1")
						+ " "
						+ nTicks_
						+ " "
						+ LangModule.i18n.getString("ArchiveLastNTF2"));
			}
		}
	}
}

