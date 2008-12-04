// $Id: ControlledModulePageGui.java,v 1.4 2003/05/08 20:09:03 thomas Exp $

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
import org.openrct.client.module.UserModule;
import org.openrct.client.module.LangModule;
import org.openrct.client.module.ControlModule;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public abstract class ControlledModulePageGui extends ModulePageGui implements
		Const {

	private JPanel jPanel1 = new JPanel();

	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private GridBagLayout gridBagLayout2 = new GridBagLayout();

	private JButton requestControlButton = new JButton();

	private JButton releaseControlButton = new JButton();

	private JButton cancelRequestButton = new JButton();

	private JButton takeControlButton = new JButton();

	private JScrollPane jScrollPane1 = new JScrollPane();

	private JList userQueueList = new JList();

	private DefaultListModel userQueueListModel = new DefaultListModel();

	private TitledBorder titledBorder1;

	private boolean isManaged_;

	// All the module specific code will be added
	// to the modulePanel
	protected JPanel modulePanel = new JPanel();

	public ControlledModulePageGui(String className, String assemblyName,
			String moduleName, int pageType, boolean isManaged) {
		super(className, assemblyName, moduleName, pageType);

		isManaged_ = isManaged;

		// Assign a default list model to the list
		userQueueList.setVisibleRowCount(1);

		// Getting the user module control queue data
		// START
		RCT.UserAliasSeqHolder userAliasSeq = new RCT.UserAliasSeqHolder();
		UserModule.getModuleControlQueueUsers(className_, assemblyName_,
				moduleName_, userAliasSeq, pageType_);

		for (int i = 0; i < userAliasSeq.value.length; i++) {

			userQueueListModel.insertElementAt(userAliasSeq.value[i],
					userQueueListModel.getSize());
		}
		userQueueList.setModel(userQueueListModel);
		// END

		titledBorder1 = new TitledBorder("");
		this.setLayout(gridBagLayout1);

		jPanel1.setLayout(gridBagLayout2);

		requestControlButton.setText(LangModule.i18n
				.getString("ControlPageButtonReqCont"));
		requestControlButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				pressedRequestControlButton();
			}
		});

		releaseControlButton.setText(LangModule.i18n
				.getString("ControlPageButtonRelCont"));
		releaseControlButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				pressedReleaseControlButton();
			}
		});

		cancelRequestButton.setText(LangModule.i18n
				.getString("ControlPageButtonCanReq"));
		cancelRequestButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				pressedCancelRequestButton();
			}
		});

		takeControlButton.setText(LangModule.i18n
				.getString("ControlPageButtonTakeCont"));
		takeControlButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				pressedTakeControlButton();
			}
		});

		jPanel1.setBorder(titledBorder1);

		titledBorder1.setTitle(LangModule.i18n.getString("ControlPageBorder"));
		titledBorder1.setTitleJustification(2);

		jScrollPane1.setAutoscrolls(true);

		this.add(jPanel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 15));

		jPanel1.add(requestControlButton, new GridBagConstraints(0, 0, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));

		jPanel1.add(releaseControlButton, new GridBagConstraints(1, 0, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));

		jPanel1.add(cancelRequestButton, new GridBagConstraints(2, 0, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));
		jPanel1.add(takeControlButton, new GridBagConstraints(3, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));

		jPanel1.add(jScrollPane1, new GridBagConstraints(4, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						5, 5, 5, 5), 0, 0));

		jScrollPane1.getViewport().add(userQueueList, null);

		this.add(modulePanel, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						5, 5, 5, 5), 0, 0));

		// Setting the initial button states
		setButtonEnableStateOnInit();
	}

	// Methods:
	// --------

	// Method:
	private synchronized boolean isQueueEmpty() {

		return userQueueListModel.isEmpty();
	}

	private synchronized boolean hasQueueNMembers(int numMembers) {

		DefaultListModel userListModel = (DefaultListModel) userQueueList
				.getModel();

		if (userListModel.getSize() == numMembers) {

			return true;
		} else {

			return false;
		}
	}

	// Method:
	private synchronized boolean isQueueMember(String userAlias) {

		DefaultListModel userListModel = (DefaultListModel) userQueueList
				.getModel();

		if (userListModel.contains(userAlias)) {

			return true;
		} else {

			return false;
		}
	}

	// Method:
	private synchronized boolean isUserOnlyQueueMember() {

		if (isQueueMember(UserModule.getAlias()) && hasQueueNMembers(1)) {

			return true;
		} else {

			return false;
		}
	}

	// Method:
	// This method is called if a group manager change
	// occured so we need to set the isManaged_ flag
	public synchronized void setVisibleTakeButton(boolean state) {

		isManaged_ = state;

		// Only a manger has the take button visible
		if (isManaged_) {

			takeControlButton.setVisible(state);
		} else {

			takeControlButton.setVisible(false);
		}
	}

	// Method:
	private synchronized void setEnabledRequestControlButton(boolean state) {

		// This button is only active if:
		// The user is not in the queue
		if (!isQueueMember(UserModule.getAlias())) {

			requestControlButton.setEnabled(state);
		} else {

			requestControlButton.setEnabled(false);
		}
	}

	// Method:
	private synchronized void setEnabledReleaseControlButton(boolean state) {

		// This button is only active if:
		// The user is in the queue
		if (isQueueMember(UserModule.getAlias())) {

			releaseControlButton.setEnabled(state);
		} else {

			releaseControlButton.setEnabled(false);
		}
	}

	// Method:
	private synchronized void setEnabledCancelRequestButton(boolean state) {

		// This button is only active if:
		// The user is in the queue
		if (isQueueMember(UserModule.getAlias())) {

			cancelRequestButton.setEnabled(state);
		} else {

			cancelRequestButton.setEnabled(false);
		}
	}

	// Method:
	private synchronized void setEnabledTakeControlButton(boolean state) {

		// This button is only active if:
		// The queue is not empty
		// The user is not in the queue
		if (state && !isQueueEmpty() && !isQueueMember(UserModule.getAlias())) {

			takeControlButton.setEnabled(state);
		} else {

			takeControlButton.setEnabled(false);
		}
	}

	// Method:
	public synchronized void displayRequestControl(String userAlias) {

		// Add user to the queue
		userQueueListModel.insertElementAt(userAlias, userQueueListModel
				.getSize());

		// Setting button states
		if (userAlias.equals(UserModule.getAlias())) {

			setEnabledRequestControlButton(false);
			setEnabledReleaseControlButton(false);
			setEnabledCancelRequestButton(true);
		}

		setEnabledTakeControlButton(true);
	}

	// Method:
	public synchronized void displayReleaseControl(String userAlias) {

		int index = userQueueListModel.indexOf(userAlias);

		if (Const.ERROR != index) {

			userQueueListModel.remove(index);
		}

		// Setting button state
		if (userAlias.equals(UserModule.getAlias())) {

			setEnabledRequestControlButton(true);
			setEnabledReleaseControlButton(false);
			setEnabledCancelRequestButton(false);

			actionDeactivate();
		}

		setEnabledTakeControlButton(true);
	}

	// Method:
	public synchronized void displayCancelRequest(String userAlias) {

		int index = userQueueListModel.indexOf(userAlias);

		if (Const.ERROR != index) {

			userQueueListModel.remove(index);
		}

		// Setting button state
		if (userAlias.equals(UserModule.getAlias())) {

			setEnabledRequestControlButton(true);
			setEnabledReleaseControlButton(false);
			setEnabledCancelRequestButton(false);

			actionDeactivate();
		}

		setEnabledTakeControlButton(true);
	}

	// Method:
	public synchronized void displayTakeControl(String userAlias) {

		// Update the front of queue
		if (!isQueueEmpty()) {

			String activeUser = "";

			// Getting current front of queue
			try {

				activeUser = (String) userQueueListModel.get(0);
				userQueueListModel.set(0, userAlias);
			} catch (ArrayIndexOutOfBoundsException e) {

				return;
			}

			// Setting button state for previous active user
			if (activeUser.equals(UserModule.getAlias())) {

				setEnabledRequestControlButton(true);
				setEnabledReleaseControlButton(false);
				setEnabledCancelRequestButton(false);
				setEnabledTakeControlButton(false);

				actionDeactivate();
			}
			// Setting button state for new active user,
			// which is the manager since he/she took control
			else if (userAlias.equals(UserModule.getAlias())) {

				setEnabledRequestControlButton(false);
				setEnabledReleaseControlButton(true);
				setEnabledCancelRequestButton(false);
				setEnabledTakeControlButton(false);

				actionActivate();
			}
		}
	}

	// Method:
	public synchronized void displayActiveControl(String userAlias) {

		setEnabledRequestControlButton(false);
		setEnabledReleaseControlButton(true);
		setEnabledCancelRequestButton(false);
		setEnabledTakeControlButton(false);

		// Dispatch call to module
		actionActivate();
	}

	// Method:
	public synchronized void displayExitControl(String userAlias) {

		int index = userQueueListModel.indexOf(userAlias);

		if (Const.ERROR != index) {

			userQueueListModel.remove(index);
		}

		// Set take button state
		setEnabledTakeControlButton(true);
	}

	// Method:
	private synchronized void setButtonEnableStateOnInit() {

		if (!isManaged_) {

			setVisibleTakeButton(false);
		}

		setEnabledRequestControlButton(true);
		setEnabledReleaseControlButton(false);
		setEnabledCancelRequestButton(false);
		setEnabledTakeControlButton(true);
	}

	// Callbacks:
	// ----------

	// Callback:
	public synchronized void pressedRequestControlButton() {

		// Disable the button we just pressed
		setEnabledRequestControlButton(false);

		if (PAGE_TEAM == pageType_) {

			ControlModule.request(className_, assemblyName_, UserModule
					.getAlias(), UserModule.getId(), moduleName_,
					CONTROL_REQ_TEAM);
		} else if (PAGE_GROUP == pageType_) {

			ControlModule.request(className_, assemblyName_, UserModule
					.getAlias(), UserModule.getId(), moduleName_,
					CONTROL_REQ_GROUP);
		} else {

			System.err.println("ERROR: Wrong page type!");
		}
	}

	// Callback:
	public synchronized void pressedReleaseControlButton() {

		// Disable the button we just pressed
		setEnabledReleaseControlButton(false);

		// Call the modules release control method
		actionReleaseControl(isUserOnlyQueueMember());

		if (PAGE_TEAM == pageType_) {

			ControlModule.release(className_, assemblyName_, UserModule
					.getAlias(), UserModule.getId(), moduleName_,
					CONTROL_REL_TEAM);
		} else if (PAGE_GROUP == pageType_) {

			ControlModule.release(className_, assemblyName_, UserModule
					.getAlias(), UserModule.getId(), moduleName_,
					CONTROL_REL_GROUP);
		} else {

			System.err.println("ERROR: Wrong page type!");
		}
	}

	// Callback:
	public synchronized void pressedCancelRequestButton() {

		// Disable the button we just pressed
		setEnabledCancelRequestButton(false);

		if (PAGE_TEAM == pageType_) {

			ControlModule.cancelRequest(className_, assemblyName_, UserModule
					.getAlias(), UserModule.getId(), moduleName_,
					CONTROL_CAN_REQ_TEAM);
		} else if (PAGE_GROUP == pageType_) {

			ControlModule.cancelRequest(className_, assemblyName_, UserModule
					.getAlias(), UserModule.getId(), moduleName_,
					CONTROL_CAN_REQ_GROUP);
		} else {

			System.err.println("ERROR: Wrong page type!");
		}
	}

	// Callback:
	public synchronized void pressedTakeControlButton() {

		setEnabledRequestControlButton(false);
		setEnabledReleaseControlButton(true);
		setEnabledCancelRequestButton(false);
		setEnabledTakeControlButton(false);

		try {
			if (PAGE_TEAM == pageType_) {

				ControlModule.take(className_, assemblyName_, UserModule
						.getAlias(), UserModule.getId(), moduleName_,
						CONTROL_TAKE_TEAM);
			} else if (PAGE_GROUP == pageType_) {

				ControlModule.take(className_, assemblyName_, UserModule
						.getAlias(), UserModule.getId(), moduleName_,
						CONTROL_TAKE_GROUP);
			} else {

				System.err.println("ERROR: Wrong page type!");
			}
		} catch (RCT.ControlServerPackage.QueueIsEmpty qie) {

			setEnabledRequestControlButton(true);
			setEnabledReleaseControlButton(false);
			setEnabledCancelRequestButton(false);
			setEnabledTakeControlButton(false);
		}
	}

	// Abstract Methods:
	// -----------------

	// Abstract Method:
	public abstract void actionActivate();

	// Abstract Method:
	public abstract void actionDeactivate();

	// Abstrct Method:
	// This method is called if we need to signal
	// an action to the module on a release control
	// event
	public abstract void actionReleaseControl(boolean userIsLastInQueue);

	// Abstract Method:
	public abstract void dispatchExitRequest();

	// Abstract Methods Implementation:
	// --------------------------------

	// Abstract Method:
	public void exitRequest() {

		// Check if the user is in the queue
		if (userQueueListModel.contains(UserModule.getAlias())) {

			if (PAGE_TEAM == pageType_) {

				ControlModule.exitRequest(className_, assemblyName_, UserModule
						.getAlias(), UserModule.getId(), moduleName_,
						CONTROL_EXIT_TEAM);
			} else if (PAGE_GROUP == pageType_) {

				ControlModule.exitRequest(className_, assemblyName_, UserModule
						.getAlias(), UserModule.getId(), moduleName_,
						CONTROL_EXIT_GROUP);
			} else {

				System.err.println("ERROR: Wrong page type!");
			}
		}

		// Dispatching the call
		dispatchExitRequest();
	}
}

