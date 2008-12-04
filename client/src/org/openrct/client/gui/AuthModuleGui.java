// $Id: AuthModuleGui.java,v 1.5 2003/07/01 16:07:18 thomas Exp $

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
import org.openrct.client.Client;
import org.openrct.client.Admin;
import org.openrct.client.module.LangModule;
import org.openrct.client.module.ConfigModule;
import org.openrct.client.module.ErrorModule;
import org.openrct.client.module.PlatformModule;
import org.openrct.client.module.AuthModule;
import org.openrct.client.module.UserModule;
import org.openrct.client.module.PingModule;
import org.openrct.client.util.Utility;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class AuthModuleGui extends JFrame implements Const {

	private JPanel contentPane;

	private JPanel buttonPanel = new JPanel();

	private JMenuBar jMenuBar1 = new JMenuBar();

	private JMenu jMenuFile = new JMenu();

	private JMenu jMenuHelp = new JMenu();

	private JMenuItem jMenuFileExit = new JMenuItem();

	private JMenuItem jMenuHelpAbout = new JMenuItem();

	private JMenuItem jMenuChangeServer = new JMenuItem();

	private JMenuItem jMenuChangePort = new JMenuItem();

	private JMenuItem jMenuServerList = new JMenuItem();

	private JLabel statusBar = new JLabel();

	private JLabel imageLabel = new JLabel(new ImageIcon(ClassLoader.getSystemResource(SPLASH_IMG)));;
	
	private JLabel loginLabel = new JLabel();
	
	private JLabel passwordLabel = new JLabel();

	private JTextField loginField = new JTextField();

	private JPasswordField passwordField = new JPasswordField();

	private JButton loginButton;

	private JButton clearButton;

	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	// Button states
	private boolean loginState = false;

	private boolean clearState = false;

	private JFrame authFrame;

	// Level indicates if we authenticate a user or an administrator
	private int authLevel_;

	// Construct the frame
	public AuthModuleGui(int authLevel) {
		
		authFrame = this;

		enableEvents(AWTEvent.WINDOW_EVENT_MASK);

		authLevel_ = authLevel;

		try {

			jbInit();
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	//Component initialization
	private void jbInit() throws Exception {

		contentPane = (JPanel) this.getContentPane();
		contentPane.setLayout(gridBagLayout1);

		this.setSize(new Dimension(AUTH_GUI_WIDTH, AUTH_GUI_HEIGHT));

		this.setTitle(OPEN_RCT_NAME + " "
				+ LangModule.i18n.getString("AuthFrameTitle") + " v"
				+ RCT_VERSION);

		buttonPanel.add(loginButton = new JButton(LangModule.i18n
				.getString("AuthButtonLogin")));
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				pressedLoginButton();
			}
		});

		buttonPanel.add(clearButton = new JButton(LangModule.i18n
				.getString("AuthButtonClear")));
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				loginField.setText("");
				passwordField.setText("");
				loginField.requestFocus();
			}
		});

		loginField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {

				passwordField.requestFocus();
			}
		});

		passwordField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (loginField.getText().equals("")) {
					loginField.requestFocus();
				} else if (passwordField.getPassword().length != 0) {
					pressedLoginButton();
				}
			}
		});

		DocumentListener d = new DocumentListener() {
			public void changedUpdate(DocumentEvent evt) {
				// Nothing to do
			}

			public void insertUpdate(DocumentEvent evt) {

				checkButtons();
			}

			public void removeUpdate(DocumentEvent evt) {

				checkButtons();
			}
		};

		loginField.getDocument().addDocumentListener(d);
		passwordField.getDocument().addDocumentListener(d);

		statusBar.setBorder(BorderFactory.createLoweredBevelBorder());
		statusBar.setText(composeStatusText(LangModule.i18n
				.getString("AuthStatus1")));

		jMenuFile.setText(LangModule.i18n.getString("AuthMenuFile"));
		jMenuChangeServer.setText(LangModule.i18n
				.getString("AuthMenuChangeServer"));
		jMenuChangePort
				.setText(LangModule.i18n.getString("AuthMenuChangePort"));
		jMenuServerList
				.setText(LangModule.i18n.getString("AuthMenuServerList"));
		jMenuFileExit.setText(LangModule.i18n.getString("AuthMenuExit"));
		jMenuHelp.setText(LangModule.i18n.getString("AuthMenuHelp"));
		jMenuHelpAbout.setText(LangModule.i18n.getString("AuthMenuAbout"));

		jMenuChangeServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuChangeServer_actionPerformed(e);
			}
		});

		jMenuChangePort.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuChangePort_actionPerformed(e);
			}
		});

		jMenuServerList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuServerList_actionPerformed(e);
			}
		});

		jMenuFileExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuFileExit_actionPerformed(e);
			}
		});

		jMenuHelpAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jMenuHelpAbout_actionPerformed(e);
			}
		});

		loginLabel.setText(LangModule.i18n.getString("AuthLabelLogin"));

		passwordLabel.setText(LangModule.i18n.getString("AuthLabelPassword"));

		jMenuFile.add(jMenuChangeServer);
		jMenuFile.add(jMenuChangePort);
		jMenuFile.addSeparator();
		jMenuFile.add(jMenuServerList);
		jMenuFile.addSeparator();
		jMenuFile.add(jMenuFileExit);

		jMenuHelp.add(jMenuHelpAbout);

		jMenuBar1.add(jMenuFile);
		jMenuBar1.add(Box.createHorizontalGlue());
		jMenuBar1.add(jMenuHelp);

		this.setJMenuBar(jMenuBar1);

		passwordField.setEchoChar('*');

		contentPane.add(imageLabel, new GridBagConstraints(0, 0, 2, 1, 1.0,
				1.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));

		contentPane.add(loginLabel, new GridBagConstraints(0, 1, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));

		contentPane.add(loginField, new GridBagConstraints(1, 1, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 15), 0, 0));

		contentPane.add(passwordLabel, new GridBagConstraints(0, 2, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 0, 0));

		contentPane.add(passwordField, new GridBagConstraints(1, 2, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 15), 0, 0));

		contentPane.add(buttonPanel, new GridBagConstraints(0, 3, 2, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0));

		contentPane.add(statusBar, new GridBagConstraints(0, 4, 2, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(15, 5, 5, 5), 0, 0));

		// Center the Frame in the middle of the screen
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = this.getSize();

		if (frameSize.height > screenSize.height) {

			frameSize.height = screenSize.height;
		}

		if (frameSize.width > screenSize.width) {

			frameSize.width = screenSize.width;
		}

		this.setLocation((screenSize.width - frameSize.width) / 2,
				(screenSize.height - frameSize.height) / 2);

		this.setVisible(true);

		// Both buttons are disabled at first
		loginButton.setEnabled(false);
		clearButton.setEnabled(false);
	}

	// File | Change Server action performed
	public void jMenuChangeServer_actionPerformed(ActionEvent e) {

		String serverName = JOptionPane.showInputDialog(this, LangModule.i18n
				.getString("ConfigMsg1"));

		if (null == serverName) {

			// Nothing to do. User clicked Cancel button
		} else if (0 == serverName.length()) {

			// User entered a zero length string
			JOptionPane.showMessageDialog(this, LangModule.i18n
					.getString("ConfigMsg2"), LangModule.i18n
					.getString("ConfigMsg3"), JOptionPane.ERROR_MESSAGE);
		} else {

			if (ConfigModule.setServerName(serverName)) {

				statusBar.setText(composeStatusText(LangModule.i18n
						.getString("AuthStatus1")));
			} else {

				ErrorModule.display(this, "Server Setup Error",
						"ERROR: Could not set server name!");
			}
		}
	}

	// File | Change Port action performed
	public void jMenuChangePort_actionPerformed(ActionEvent e) {

		String port = JOptionPane.showInputDialog(this, LangModule.i18n
				.getString("ConfigMsg4"));

		if (null == port) {

			// Nothing to do. User clicked Cancel button
		} else if (0 == port.length()) {

			// User entered a zero length string
			JOptionPane.showMessageDialog(this, LangModule.i18n
					.getString("ConfigMsg5"), LangModule.i18n
					.getString("ConfigMsg6"), JOptionPane.ERROR_MESSAGE);
		} else {

			if (ConfigModule.setServerPort(port)) {

				statusBar.setText(composeStatusText(LangModule.i18n
						.getString("AuthStatus1")));
			} else {

				ErrorModule.display(this, LangModule.i18n
						.getString("AuthModuleGuiDialog2"), LangModule.i18n
						.getString("AuthModuleGuiDialog3"));
			}
		}
	}

	// File | Server List action performed
	public void jMenuServerList_actionPerformed(ActionEvent e) {

		Thread serverListThread = new Thread(new Runnable() {

			public void run() {

				ServerListDialog serverListDialog = new ServerListDialog(
						AuthModuleGui.this, LangModule.i18n
								.getString("AuthModuleGuiDialog1"));

				// Check which button was clicked on
				switch (serverListDialog.getValue().getButtonValue()) {

				case JOptionPane.OK_OPTION:

					if (ConfigModule.setServerName(serverListDialog.getValue()
							.getServerName())) {

						statusBar.setText(composeStatusText(LangModule.i18n
								.getString("AuthStatus1")));
					} else {

						ErrorModule.display(AuthModuleGui.this, LangModule.i18n
								.getString("AuthModuleGuiDialog4"),
								LangModule.i18n
										.getString("AuthModuleGuiDialog5"));
					}

					if (ConfigModule.setServerPort(serverListDialog.getValue()
							.getPortNumber())) {

						statusBar.setText(composeStatusText(LangModule.i18n
								.getString("AuthStatus1")));
					} else {

						ErrorModule.display(AuthModuleGui.this, LangModule.i18n
								.getString("AuthModuleGuiDialog6"),
								LangModule.i18n
										.getString("AuthModuleGuiDialog7"));
					}

					break;

				default:

				// Nothing to do
				}
			}
		});

		serverListThread.start();
	}

	// File | Exit action performed
	public void jMenuFileExit_actionPerformed(ActionEvent e) {

		System.exit(0);
	}

	// Help | About action performed
	public void jMenuHelpAbout_actionPerformed(ActionEvent e) {

		SessionAboutGui dlg = new SessionAboutGui(this);
		Dimension dlgSize = dlg.getPreferredSize();
		Dimension frmSize = getSize();
		Point loc = getLocation();

		dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x,
				(frmSize.height - dlgSize.height) / 2 + loc.y);

		dlg.setModal(true);

		dlg.show();

	}

	// Overridden so we can exit when window is closed
	protected void processWindowEvent(WindowEvent e) {

		super.processWindowEvent(e);

		if (e.getID() == WindowEvent.WINDOW_CLOSING) {

			jMenuFileExit_actionPerformed(null);
		}
	}

	public void pressedLoginButton() {

		// Disable buttons so that user cannot click them again
		loginButton.setEnabled(false);
		clearButton.setEnabled(false);

		statusBar.setText(composeStatusText(LangModule.i18n
				.getString("AuthStatus2")));
		statusBar.setIcon(new ImageIcon(ClassLoader.getSystemResource(AUTH_STATUS_IMG)));

		Thread authThread = new Thread(new Runnable() {

			public void run() {

				int connectionType = Utility.canClientConnectToServer(
						PlatformModule.getIp(), ConfigModule.getServerIp());

				switch (connectionType) {

				case GLOB_TO_GLOB:
					// Don't do anything
					break;

				case GLOB_TO_PRIV:
					ConfigModule.set(CONF_FIREWALL_NAT, true);
					break;

				case PRIV_TO_PRIV:
					// Don't do anything
					break;

				case PRIV_TO_GLOB:

					// Show error message
					JOptionPane.showMessageDialog(authFrame, LangModule.i18n
							.getString("AuthStatus3"), LangModule.i18n
							.getString("AuthDialogLogin"),
							JOptionPane.ERROR_MESSAGE);

					// Enable the buttons so that the user can try to login
					// again
					loginButton.setEnabled(true);
					clearButton.setEnabled(true);

					// Resetting the status bar info
					statusBar.setText(composeStatusText(LangModule.i18n
							.getString("AuthStatus1")));
					statusBar.setIcon(null);

					return;

				default:

					System.err.println(LangModule.i18n
							.getString("AuthModuleGuiDialog8"));
					return;
				}

				if (AUTH_LEVEL_USER == authLevel_) {

					Client.initCorba();

					if (AuthModule
							.verifyUserAuth(loginField.getText(), new String(
									passwordField.getPassword()), authLevel_)) {

						// Close the AuthModuleGui
						authFrame.setVisible(false);
						authFrame.dispose();

						// Starting the session gui, and starting the client
						// orb,
						// and sending login notification to server
						AuthModule.login(BC_MSG_USER_ONLINE,
								UserModule.getId(), UserModule.getAlias(),
								UserModule.getFirstName(), UserModule
										.getLastName(), LangModule.i18n
										.getString("SessionMethodLogin"),
								PlatformModule.getIp(), PlatformModule.getOs(),
								RCT_VERSION);

						// Automatic Bandwidth test
						PingModule.testBandwidth();
					} else {

						// Show error message in dialog frame
						JOptionPane.showMessageDialog(authFrame, AuthModule
								.getAuthRespReason(), LangModule.i18n
								.getString("AuthDialogLogin"),
								JOptionPane.ERROR_MESSAGE);

						// Enable the buttons so that the user can try to login
						// again
						loginButton.setEnabled(true);
						clearButton.setEnabled(true);

						// Resetting the status bar info
						statusBar.setText(composeStatusText(LangModule.i18n
								.getString("AuthStatus1")));
						statusBar.setIcon(null);
					}
				} else if (AUTH_LEVEL_ADMIN == authLevel_) {

					Admin.initCorba();

					if (AuthModule
							.verifyAdminAuth(loginField.getText(), new String(
									passwordField.getPassword()), authLevel_)) {

						// Automatic Bandwidth test
						PingModule.testBandwidth();
					} else {

						// Show error message in dialog frame
						JOptionPane.showMessageDialog(authFrame, AuthModule
								.getAuthRespReason(), LangModule.i18n
								.getString("AuthDialogLogin"),
								JOptionPane.ERROR_MESSAGE);

						// Enable the buttons so that the user can try to login
						// again
						loginButton.setEnabled(true);
						clearButton.setEnabled(true);

						// Resetting the status bar info
						statusBar.setText(composeStatusText(LangModule.i18n
								.getString("AuthStatus1")));
						statusBar.setIcon(null);
					}
				} else {

					// Show error dialog
					// Not implemented yet
				}
			}
		});

		authThread.start();
	}

	public void checkButtons() {
		// Check whether the buttons should be enabled or not
		boolean newLoginState;
		boolean newClearState;
		boolean nameClear = loginField.getText().equals("");
		boolean passwordClear = (passwordField.getPassword().length == 0);

		newLoginState = nameClear == false && passwordClear == false;
		newClearState = nameClear == false || passwordClear == false;

		if (newLoginState != loginState) {
			loginButton.setEnabled(newLoginState);
			loginState = newLoginState;
		}

		if (newClearState != clearState) {
			clearButton.setEnabled(newClearState);
			clearState = newClearState;
		}
	}

	private String composeStatusText(String status) {

		String text = status + " " + ConfigModule.getServerName() + " "
				+ LangModule.i18n.getString("AuthStatusAt") + " "
				+ ConfigModule.getServerPort();

		return text;
	}
}

