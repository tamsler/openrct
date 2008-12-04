// $Id: ArchiveChatTimeRangePageGui.java,v 1.3 2003/03/18 23:08:57 thomas Exp $

package org.openrct.client.gui;

import org.openrct.client.Const;
import org.openrct.client.module.LangModule;
import org.openrct.client.module.ConfigModule;
import org.openrct.client.module.TeamModule;
import org.openrct.client.module.UserModule;
import org.openrct.client.util.Utility;
import org.openrct.client.util.RctStyle;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ArchiveChatTimeRangePageGui extends JPanel implements Const {

	private RctJTextPane textArea = new RctJTextPane();

	private JScrollPane jScrollPane = new JScrollPane();

	private GridBagLayout gridBagLayout = new GridBagLayout();

	private JButton closeButton = new JButton();

	private String className_;

	private String teamName_;

	private ArchivePageGui archivePage_;

	private String fromDate_;

	private String toDate_;

	// Constructor
	public ArchiveChatTimeRangePageGui(String className, String teamName,
			ArchivePageGui archivePage, String fromDate, String toDate) {

		className_ = className;
		teamName_ = teamName;
		archivePage_ = archivePage;
		fromDate_ = fromDate;
		toDate_ = toDate;

		this.setLayout(gridBagLayout);

		jScrollPane.setAutoscrolls(true);

		closeButton.setText(LangModule.i18n.getString("ButtonLabelClose"));
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				pressedCloseButton();
			}
		});

		textArea.setEditable(false);
		textArea.setAutoscrolls(true);
		textArea.setFont(new Font(ConfigModule.getString(CONF_UNICODE_FONT),
				Font.PLAIN, ConfigModule.getNumber(CONF_UNICODE_FONT_SIZE)));

		jScrollPane.getViewport().add(textArea);

		this.add(jScrollPane, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(
						5, 5, 5, 5), 0, 260));

		this.add(closeButton, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0));

		// ChatMsgHistSeq
		RCT.ChatMsgHistSeqHolder chatMsgHistSeq = new RCT.ChatMsgHistSeqHolder();

		try {

			TeamModule.getChatArchiveTimeRange(className_, teamName_,
					fromDate_, toDate_, chatMsgHistSeq);
		} catch (RCT.TeamServerPackage.DataSelectionExceedsLimit dsel) {

			JOptionPane
					.showMessageDialog(this, LangModule.i18n
							.getString("ExceptionDialog3"), LangModule.i18n
							.getString("ExceptionDialog1"),
							JOptionPane.WARNING_MESSAGE);
			return;
		}

		// Test if there are any chat messages in this team
		if (0 == chatMsgHistSeq.value.length) {

			textArea.setText(LangModule.i18n.getString("ChatPageTeamInfo1"));
		} else {

			String style = RctStyle.REGULAR;

			for (int i = 0; i < chatMsgHistSeq.value.length; i++) {

				if (chatMsgHistSeq.value[i].user_alias.equals(UserModule
						.getAlias())) {

					style = RctStyle.BOLD;
				} else {

					style = RctStyle.REGULAR;
				}

				textArea
						.append(
								"< "
										+ chatMsgHistSeq.value[i].user_alias
										+ " : "
										+ Utility
												.getDateAndTime(chatMsgHistSeq.value[i].rct_date)
										+ " >" + NL
										+ chatMsgHistSeq.value[i].chat_msg + NL
										+ NL, style);
			}
		}
	}

	// Methods:
	// --------

	// Method:
	public void dispatchFontChange() {

		textArea.setFont(new Font(ConfigModule.getString(CONF_UNICODE_FONT),
				Font.PLAIN, ConfigModule.getNumber(CONF_UNICODE_FONT_SIZE)));
	}

	// Callbacks:
	// ----------

	// Callback
	public void pressedCloseButton() {

		archivePage_.closePage();
	}
}