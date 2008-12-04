// $Id: SessionGui.java,v 1.4 2003/05/08 20:09:03 thomas Exp $

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
import org.openrct.client.module.UserModule;
import org.openrct.client.module.ConfigModule;
import org.openrct.client.module.ChatModule;
import org.openrct.client.module.TextpadModule;
import org.openrct.client.module.TeamModule;
import org.openrct.client.module.SessionModule;
import org.openrct.client.util.ActionTimerRingBell;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class SessionGui extends JFrame implements Const {

	private static JPanel contentPane;

	private JMenuBar jMenuBar1 = new JMenuBar();

	private JMenu jMenuFile = new JMenu();

	private JMenuItem jMenuFileExit = new JMenuItem();

	private JMenu jMenuHelp = new JMenu();

	private JMenuItem jMenuHelpAbout = new JMenuItem();

	private JMenu jMenuFont = new JMenu();

	private JMenuItem jMenuFont12 = new JMenuItem();

	private JMenuItem jMenuFont14 = new JMenuItem();

	private JMenuItem jMenuFont16 = new JMenuItem();

	private JMenuItem jMenuFont18 = new JMenuItem();

	private JMenuItem jMenuFont20 = new JMenuItem();

	private JMenu jMenuView = new JMenu();

	private JMenuItem jMenuViewRefreshClass = new JMenuItem();

	private JMenuItem jMenuViewRefreshUser = new JMenuItem();

	private ButtonGroup jConButtonGroup = new ButtonGroup();

	private JMenu jMenuConnection = new JMenu();

	private JRadioButtonMenuItem jRBMLan = new JRadioButtonMenuItem();

	private JRadioButtonMenuItem jRBMDsl = new JRadioButtonMenuItem();

	private JRadioButtonMenuItem jRBMMod = new JRadioButtonMenuItem();

	private JButton logoutButton = new JButton();

	private GridBagLayout gridBagLayout = new GridBagLayout();

	private static JTabbedPane sessionTabbedPane = new JTabbedPane();

	private static JTabbedPane assemblyTabbedPane = new JTabbedPane();

	private static ClassViewPageGui classViewPage = new ClassViewPageGui();

	private static UserViewPageGui userViewPage = new UserViewPageGui();

	private JScrollPane jScrollPane = new JScrollPane();

	private TitledBorder titledBorder;

	private static JTextArea bcTextArea = new JTextArea();

	private static ActionTimerRingBell actionTimerRingBell_;

	// Constructor
	public SessionGui() {

		enableEvents(AWTEvent.WINDOW_EVENT_MASK);

		try {
			initSessionGui();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Init SessionGui
	private void initSessionGui() throws Exception {

		contentPane = (JPanel) this.getContentPane();

		titledBorder = new TitledBorder("");

		contentPane.setLayout(gridBagLayout);

		this.setSize(new Dimension(SESSION_FRAME_WIDTH, SESSION_FRAME_HEIGHT));

		this.setTitle(OPEN_RCT_NAME + " "
				+ LangModule.i18n.getString("SessionFrameTitle") + " v"
				+ RCT_VERSION + SP
				+ LangModule.i18n.getString("SessionFrameTitleUser") + " [ "
				+ UserModule.getAlias() + " ]");

		jMenuFile.setText(LangModule.i18n.getString("SessionMenuFile"));

		jMenuFileExit.setText(LangModule.i18n.getString("SessionMenuExit"));

		jMenuFileExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				actionMenuFileExit(e);
			}
		});

		jMenuHelp.setText(LangModule.i18n.getString("SessionMenuHelp"));

		jMenuHelpAbout.setText(LangModule.i18n.getString("SessionMenuAbout"));

		jMenuHelpAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				actionMenuHelpAbout(e);
			}
		});

		jMenuFont.setText(LangModule.i18n.getString("SessionMenuFont"));

		jMenuFont12.setText(LangModule.i18n.getString("SessionMenuFont12"));
		jMenuFont12.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				actionMenuFont(12);
			}
		});

		jMenuFont14.setText(LangModule.i18n.getString("SessionMenuFont14"));
		jMenuFont14.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				actionMenuFont(14);
			}
		});

		jMenuFont16.setText(LangModule.i18n.getString("SessionMenuFont16"));
		jMenuFont16.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				actionMenuFont(16);
			}
		});

		jMenuFont18.setText(LangModule.i18n.getString("SessionMenuFont18"));
		jMenuFont18.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				actionMenuFont(18);
			}
		});

		jMenuFont20.setText(LangModule.i18n.getString("SessionMenuFont20"));
		jMenuFont20.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				actionMenuFont(20);
			}
		});

		jMenuView.setText(LangModule.i18n.getString("SessionMenuView"));

		jMenuViewRefreshClass.setText(LangModule.i18n
				.getString("SessionMenuViewRC"));
		jMenuViewRefreshClass.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				actionMenuViewRefreshClass();
			}
		});

		jMenuViewRefreshUser.setText(LangModule.i18n
				.getString("SessionMenuViewRU"));
		jMenuViewRefreshUser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				actionMenuViewRefreshUser();
			}
		});

		jMenuConnection.setText(LangModule.i18n
				.getString("SessionMenuConnection"));

		jRBMLan.setText(LangModule.i18n.getString("SessionMenuConLan"));
		jRBMLan.setSelected(true);
		jRBMLan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				actionMenuConnectionLan();
			}
		});

		jRBMDsl.setText(LangModule.i18n.getString("SessionMenuConDsl"));
		jRBMDsl.setSelected(false);
		jRBMDsl.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				actionMenuConnectionDsl();
			}
		});

		jRBMMod.setText(LangModule.i18n.getString("SessionMenuConMod"));
		jRBMMod.setSelected(false);
		jRBMMod.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				actionMenuConnectionMod();
			}
		});

		contentPane.setPreferredSize(new Dimension(SESSION_FRAME_WIDTH,
				SESSION_FRAME_HEIGHT));

		logoutButton.setText(LangModule.i18n.getString("SessionButtonLogout"));
		logoutButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				pressedLogoutButton();
			}
		});

		assemblyTabbedPane.setTabPlacement(JTabbedPane.TOP);
		titledBorder.setTitle(LangModule.i18n.getString("SessionBorderBC"));

		titledBorder.setBorder(BorderFactory.createEtchedBorder());

		titledBorder.setTitleJustification(2);

		jScrollPane.setAutoscrolls(true);

		jScrollPane.setBorder(titledBorder);

		bcTextArea.setBorder(BorderFactory.createLineBorder(Color.black));
		bcTextArea.setFont(new Font(ConfigModule.getString(CONF_UNICODE_FONT),
				Font.PLAIN, ConfigModule.getNumber(CONF_UNICODE_FONT_SIZE)));
		bcTextArea.setAutoscrolls(true);
		bcTextArea.setLineWrap(true);
		bcTextArea.setWrapStyleWord(true);
		bcTextArea.setEditable(false);

		jMenuFile.add(jMenuFileExit);

		jMenuHelp.add(jMenuHelpAbout);

		jMenuFont.add(jMenuFont12);
		jMenuFont.add(jMenuFont14);
		jMenuFont.add(jMenuFont16);
		jMenuFont.add(jMenuFont18);
		jMenuFont.add(jMenuFont20);

		jMenuView.add(jMenuViewRefreshClass);
		jMenuView.add(jMenuViewRefreshUser);

		jMenuConnection.add(jRBMLan);
		jMenuConnection.add(jRBMDsl);
		jMenuConnection.add(jRBMMod);
		jConButtonGroup.add(jRBMLan);
		jConButtonGroup.add(jRBMDsl);
		jConButtonGroup.add(jRBMMod);

		jMenuBar1.add(jMenuFile);
		jMenuBar1.add(jMenuFont);
		jMenuBar1.add(jMenuView);
		jMenuBar1.add(jMenuConnection);
		jMenuBar1.add(Box.createHorizontalGlue());
		jMenuBar1.add(jMenuHelp);

		contentPane.add(sessionTabbedPane, new GridBagConstraints(0, 1, 2, 1,
				1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 5, 5, 5), 0, 0));

		sessionTabbedPane.add(userViewPage, LangModule.i18n
				.getString("SessionUserView"));

		sessionTabbedPane.add(classViewPage, LangModule.i18n
				.getString("SessionClassView"));

		sessionTabbedPane.add(assemblyTabbedPane, LangModule.i18n
				.getString("SessionActiveSessions"));

		contentPane.add(logoutButton, new GridBagConstraints(0, 3, 2, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(5, 0, 5, 0), 0, 0));

		contentPane.add(jScrollPane, new GridBagConstraints(0, 2, 2, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 60));

		jScrollPane.getViewport().add(bcTextArea);

		this.setJMenuBar(jMenuBar1);

		// Disable the Page Tabbed Pane at startup
		sessionTabbedPane.setEnabledAt(MODULE_PAGE, false);

		// Create the ring bell action timer
		actionTimerRingBell_ = new ActionTimerRingBell(ConfigModule
				.getNumber(CONF_ACTION_TIMER_SLEEP));
		actionTimerRingBell_.startWorking();
	}

	// Methods:
	// --------

	// Method
	public JFrame getFrame() {

		return this;
	}

	// Gui Dispatcher Methods:
	// -----------------------

	// Gui Dispatcher Method:
	public static void refreshUserViewPage() {

		userViewPage.refresh();
	}

	// Gui Dispatcher Method:
	public static void refreshClassViewPage() {

		classViewPage.refresh();
	}

	// Gui Dispatcher Method:
	// This method changes the user's active status in the classView
	public static void displayJoinOrExitTeamEvent(String className,
			String teamName, String userAlias, int msgType) {

		classViewPage.displayJoinOrExitTeamEvent(className, teamName,
				userAlias, msgType);
	}

	// Gui Dispatcher Method:
	// Method add a user to the specifice group/class in the
	// classView and userView
	public static void displayJoinGroupEvent(String className,
			String groupName, String userAlias) {

		userViewPage.displayJoinGroupEvent(className, groupName, userAlias);

		classViewPage.displayJoinGroupEvent(className, groupName, userAlias);
	}

	// Gui Dispatcher Method:
	// This method removes the group node in the userView
	// This method removes the user from the group node in the classView
	public static void displayMemberExitsGroupEvent(String className,
			String groupName, String userAlias) {

		classViewPage.displayMemberExitsGroupEvent(className, groupName,
				userAlias);

		userViewPage.displayMemberExitsGroupEvent(className, groupName,
				userAlias);
	}

	// Gui Dispatcher Method:
	// This method removes the group node from the classView
	public static void displayRemoveGroupEvent(String className,
			String groupName) {

		classViewPage.displayRemoveGroupEvent(className, groupName);
	}

	// Gui Dispatcher Method:
	// This method adds the newly created group to both the userView
	// and classView
	public static void displayCreateGroupEvent(String className,
			String groupName, String userAlias) {

		classViewPage.displayCreateGroupEvent(className, groupName, userAlias);

		userViewPage.displayCreateGroupEvent(className, groupName, userAlias);
	}

	// Gui Dispatcher Method:
	// This method changes the user's online status in the userTree
	public static void updateUserOnlineStatus(String userAlias, int msgType) {

		userViewPage.updateUserOnlineStatus(userAlias, msgType);
	}

	// Gui Dispatcher Method:
	// This method enables/disables the Course Content button
	public static void setCCBEnabled(boolean state) {

		classViewPage.setCCBEnabled(state);
	}

	// Gui Dispatcher Method:
	// This method enables/disables the View User Info button
	public static void setUIBEnabled(boolean state) {

		userViewPage.setUIBEnabled(state);
	}

	// Gui Methods:
	// ------------

	// Gui Method:
	// Change selection for connection speed
	public void changeConnectionSpeed(int speed) {

		switch (speed) {

		case PACKET_SIZE_MOD:
			jRBMMod.setSelected(true);
			break;

		case PACKET_SIZE_DSL:
			jRBMDsl.setSelected(true);
			break;

		case PACKET_SIZE_LAN:
			jRBMLan.setSelected(true);
			break;

		default:
			System.err.println("ERROR: unknown connection type!");
			jRBMLan.setSelected(true);
		}
	}

	// Gui Method:
	// Display message from broadcast channel
	public synchronized static void displayMsg(String msg) {

		// Append message
		bcTextArea.append(msg + NL);

		// Make sure that appended message is visible
		bcTextArea.setCaretPosition(bcTextArea.getDocument().getLength());

		// If the client configuration for the
		// Session bell ring event is set to true
		// we ring the system bell per session message
		if (ConfigModule.can(CONF_SESSION_RING_BELL)) {

			actionTimerRingBell_.performAction();
		}
	}

	// Gui Method:
	// This method adds the join group request denied
	// message to the bc window
	public void displayJoinGroupRequestDenied(String className, String groupName) {

		// Display the message
		SessionGui.displayMsg(LangModule.i18n.getString("SessionGuiStr2_1")
				+ SP + groupName + SP
				+ LangModule.i18n.getString("SessionGuiStr2_2") + SP
				+ className);
	}

	// Gui Method:
	// This method adds the join group request granted
	// message to the bc window
	public void displayJoinGroupRequestGranted(String className,
			String groupName) {

		// Display the message
		SessionGui.displayMsg(LangModule.i18n.getString("SessionGuiStr3_1")
				+ SP + groupName + SP
				+ LangModule.i18n.getString("SessionGuiStr3_2") + SP
				+ className);
	}

	// Gui Method:
	// This methods indicates in the bc window if
	// there was a change in the group leader
	public void displayNotifyNewManager(String className, String groupName,
			String userAlias) {

		// If userAlisa/Creator is the current user, he/she
		// is the leader
		if (userAlias.equals(UserModule.getAlias())) {

			// Display the message
			SessionGui.displayMsg(LangModule.i18n.getString("SessionGuiStr4_1")
					+ " \"" + groupName + "\" "
					+ LangModule.i18n.getString("SessionGuiStr4_2") + SP
					+ className);
		} else {

			// Display the message
			SessionGui.displayMsg(LangModule.i18n.getString("SessionGuiStr5_1")
					+ SP + userAlias + SP
					+ LangModule.i18n.getString("SessionGuiStr5_2") + " \""
					+ groupName + "\" "
					+ LangModule.i18n.getString("SessionGuiStr5_3") + SP
					+ className);
		}
	}

	// Gui Method:
	// Method used to add pages
	public synchronized void displayJoinPage(PageGui page, String className,
			String assemblyName) {

		// Adding the page to the assemblyTabbedPane
		SessionGui.addAssemblyPage(page, className + " : " + assemblyName);

		// Switch to the Page after we added the page
		// Also select the newly created page
		SessionGui.viewSessionPage(MODULE_PAGE);
		int index = SessionGui.getAssemblyPageNumber(className + " : "
				+ assemblyName);
		SessionGui.viewAssemblyPage(index);

		// If the sessionTabbedPane has the ModuleTabbedPane disabled,
		// we need to enable it.
		if (false == SessionGui.isSessionPageEnabled(MODULE_PAGE)) {
			SessionGui.setEnabledSessionPage(MODULE_PAGE, true);
		}
	}

	// Gui Method:
	// Method disables module page. This method is called
	// when we are about to close the last module page
	public synchronized void displayDisablePages() {

		// Check how many pages are in the tabbed pane. If we exit from the last
		// page, we need to disable the module page in the sessionTabbedPane.
		// In addition we need to switch focus to the ClassView page.
		if (1 == SessionGui.getNumOfAssemblyPages()) {

			SessionGui.setEnabledSessionPage(MODULE_PAGE, false);
			SessionGui.viewSessionPage(CLASS_VIEW_PAGE);
		}
	}

	// Gui Wrapper Method:
	public void displayServerCannotBeReached() {

		JOptionPane.showMessageDialog(this,
				"Lost connection to server, or server has crashed!",
				"OpenRCT Client", JOptionPane.ERROR_MESSAGE);

		System.exit(0);
	}

	// Gui Method:
	// Method removes selected page
	public synchronized void displayRemoveSelectedPage() {

		// Remove the selected page from the TabbedPane
		int page = SessionGui.getSelectedAssemblyPageIndex();

		SessionGui.removeAssemblyPage(page);
	}

	// Gui Method:
	// Method removes page with given tab label
	public synchronized void displayRemovePageAtTab(String tabName) {

		// Remove the page, mathing tab name from the TabbedPane
		int page = SessionGui.getAssemblyPageNumber(tabName);
		SessionGui.removeAssemblyPage(page);
	}

	// Gui Wrapper Methods:
	// --------------------

	// Gui Wrapper Method:
	// Getting the selected Session page
	public static int getSelectedSessionPageIndex() {

		return sessionTabbedPane.getSelectedIndex();
	}

	// Gui Wrapper Method:
	// Getting the selected Session page
	public static int getSelectedAssemblyPageIndex() {

		return assemblyTabbedPane.getSelectedIndex();
	}

	// Gui Wrapper Method:
	// Remove an assembly page
	public static void removeAssemblyPage(int page) {

		try {

			assemblyTabbedPane.setEnabledAt(page, false);

			assemblyTabbedPane.removeTabAt(page);
		} catch (Exception e) {

			System.err.println("EXCEPTION: SessionGui.removeAssemblyPage()");
		}
	}

	// Gui Wrapper Method:
	// Add an assembly page
	public static void addAssemblyPage(PageGui page, String tag) {

		assemblyTabbedPane.add(page, tag);
	}

	// Gui Wrapper Method:
	// This method selects a page in the session
	// tabbed pane
	public static void viewSessionPage(int page) {

		sessionTabbedPane.setSelectedIndex(page);
	}

	// Gui Wrapper Method:
	// This method selects a page in the assembly
	// tabbed pane
	public static void viewAssemblyPage(int page) {

		assemblyTabbedPane.setSelectedIndex(page);
	}

	// Gui Wrapper Method:
	// Getting a page number from the assembly
	// tabbed pane
	public static int getAssemblyPageNumber(String tag) {

		return assemblyTabbedPane.indexOfTab(tag);
	}

	// Gui Wrapper Method:
	// Check if a certain page is enabled in the session
	// tabbed pane
	public static boolean isSessionPageEnabled(int page) {

		return sessionTabbedPane.isEnabledAt(page);
	}

	// Gui Wrapper Method:
	// Enable a page in the session tabbed pane
	public static void setEnabledSessionPage(int page, boolean status) {

		sessionTabbedPane.setEnabledAt(page, status);
	}

	// Gui Wrapper Method:
	// Gett the number of assembly pages
	public static int getNumOfAssemblyPages() {

		return assemblyTabbedPane.getTabCount();
	}

	// Callbacks:
	// ----------

	// Callback:
	// Font | Size [12, 14, 16, 18, 20]
	private void actionMenuFont(int fontSize) {

		ConfigModule.set(CONF_UNICODE_FONT_SIZE, fontSize);
		ChatModule.dispatchFontChange();
		TextpadModule.dispatchFontChange();
		TeamModule.dispatchFontChange();
	}

	// Callback:
	// View | Refresh Class View
	private void actionMenuViewRefreshClass() {

		refreshClassViewPage();
	}

	// Callback:
	// View | Refresh User View
	private void actionMenuViewRefreshUser() {

		refreshUserViewPage();
	}

	// Callback:
	// File | Exit action performed
	private void actionMenuFileExit(ActionEvent e) {

		// Call the SessionModule's logout method.
		// It will initiage the logout procedure
		SessionModule.logout();
	}

	// Callback:
	// Connection | LAN
	private void actionMenuConnectionLan() {

		ConfigModule.set(CONF_CONNECTION_SPEED, PACKET_SIZE_LAN);
	}

	// Callback:
	// Connection | DSL and Cable Modem
	private void actionMenuConnectionDsl() {

		ConfigModule.set(CONF_CONNECTION_SPEED, PACKET_SIZE_DSL);
	}

	// Callback:
	// Connection | Dialup Modem
	private void actionMenuConnectionMod() {

		ConfigModule.set(CONF_CONNECTION_SPEED, PACKET_SIZE_MOD);
	}

	// Callback:
	// Help | About action performed
	private void actionMenuHelpAbout(ActionEvent e) {

		SessionAboutGui dlg = new SessionAboutGui(this);
		Dimension dlgSize = dlg.getPreferredSize();
		Dimension frmSize = getSize();
		Point loc = getLocation();

		dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x,
				(frmSize.height - dlgSize.height) / 2 + loc.y);

		dlg.setModal(true);

		dlg.show();
	}

	// Callback:
	// Overridden so we can exit when window is closed
	protected void processWindowEvent(WindowEvent e) {

		// Capture the event if user closes the window
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {

			SessionModule.logout();
		}
	}

	// Callback
	private void pressedLogoutButton() {

		// Stop the action worker
		actionTimerRingBell_.stopWorking();

		// Call the SessionModule's logout method.
		// It will initiage the logout procedure
		SessionModule.logout();
	}
}

