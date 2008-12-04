// $Id: AudioRecorder.java,v 1.5 2003/05/08 19:31:42 thomas Exp $

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

package org.openrct.client.util;

import org.openrct.client.Const;
import org.openrct.client.module.SoundModule;
import org.openrct.client.module.PlatformModule;
import org.openrct.client.module.ConfigModule;
import org.openrct.client.module.SessionModule;
import org.openrct.client.module.LangModule;
import java.io.*;
import javax.swing.*;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.AudioFileFormat;

public class AudioRecorder extends Thread implements Const {

	private TargetDataLine line_;

	private AudioFileFormat.Type targetType_;

	private AudioInputStream audioInputStream_;

	private File outputFile_;

	private boolean hasError_;

	public AudioRecorder(int counter, JButton playButton, JButton recordButton,
			JButton stopButton) {

		// Init the error flag
		hasError_ = false;

		// Setting the output file name
		String fileName = SoundModule.createTempName(counter);

		outputFile_ = new File(fileName);

		// If the file exists, we delete it
		if (outputFile_.exists()) {

			outputFile_.delete();
		}

		// Audio Format Mono
		AudioFormat audioFormat;

		if (PlatformModule.isMacOsX()) {

			audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
					44100.0F, 16, 1, 2, 44100.0F, false);
		} else {

			audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
					8000.0F, 16, 1, 2, 8000.0F, false);
		}

		// Get Data Line info
		DataLine.Info info;
		info = new DataLine.Info(TargetDataLine.class, audioFormat);

		// Testing Data Line support
		if (!AudioSystem.isLineSupported(info)) {

			hasError_ = true;

			stopButton.setEnabled(false);
			playButton.setEnabled(ConfigModule.can(CONF_SND_PLAY));
			recordButton.setEnabled(ConfigModule.can(CONF_SND_RECORD));

			JOptionPane.showMessageDialog(SessionModule.getFrame(),
					LangModule.i18n.getString("SoundPageDialog6"),
					LangModule.i18n.getString("SoundPageDialog4"),
					JOptionPane.ERROR_MESSAGE);
		} else {

			try {

				line_ = (TargetDataLine) AudioSystem.getLine(info);
				line_.open(audioFormat);
			} catch (LineUnavailableException e) {

				System.err.println("EXCEPTION: LineUnavailableException");
				System.err.println("ERROR: Unable to get a recording line");
			}

			targetType_ = AudioFileFormat.Type.AU;

			audioInputStream_ = new AudioInputStream(line_);
		}
	}

	// Threads start method
	// Starts the recording.
	// The line and thread are started
	public void start() {

		if (!hasError_) {

			line_.start();
			super.start();
		}
	}

	// Stop the audio recording
	public void stopAudioRecord() {

		if (!hasError_) {

			line_.drain();
			line_.stop();
			line_.close();
			line_ = null;
		}
	}

	// Threads run method
	public void run() {

		try {

			AudioSystem.write(audioInputStream_, targetType_, outputFile_);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

