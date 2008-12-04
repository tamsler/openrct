// $Id: AudioPlayer.java,v 1.8 2003/05/08 19:31:42 thomas Exp $

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
import org.openrct.client.module.ConfigModule;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class AudioPlayer implements Runnable, Const {

	private String audioFileName_;

	private SourceDataLine dataLine_;

	private JButton playButton_;

	private JButton recordButton_;

	private JButton stopButton_;

	private JButton cancelButton_;

	private JButton closeButton_;

	private boolean hasError_;

	private boolean isStopped_;

	// Need access to the play, record and stop buttons
	// to enable/disable them after sound message was played
	public AudioPlayer(String audioFileName, JButton playButton,
			JButton recordButton, JButton stopButton, JButton cancelButton,
			JButton closeButton) {

		// Init the error flag
		hasError_ = false;

		// Init the stop flag
		isStopped_ = false;

		audioFileName_ = audioFileName;
		playButton_ = playButton;
		recordButton_ = recordButton;
		stopButton_ = stopButton;
		cancelButton_ = cancelButton;
		closeButton_ = closeButton;
	}

	// Thread's run method
	public void run() {

		play();
	}

	// Init the audio
	public void play() {

		File inputFile = new File(audioFileName_);

		try {

			AudioInputStream audioInputStream = AudioSystem
					.getAudioInputStream(inputFile);

			playAudioStream(audioInputStream);

			// If the play method finishes and was not
			// stopped, we need to set the button state

			if (!isStopped_) {

				playButton_.setEnabled(ConfigModule.can(CONF_SND_PLAY));
				recordButton_.setEnabled(ConfigModule.can(CONF_SND_RECORD));
				closeButton_.setEnabled(true);
				stopButton_.setEnabled(false);
				cancelButton_.setEnabled(false);
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	// Stop playing audio
	public void stopAudioPlay() {

		if (!hasError_) {

			dataLine_.stop();
			dataLine_.close();
		}

		// Setting the stop flag
		isStopped_ = true;
	}

	// Playing the audio data
	public void playAudioStream(AudioInputStream audioInputStream) {

		AudioFormat sourceFormat = audioInputStream.getFormat();

		AudioFormat.Encoding targetEncoding = AudioFormat.Encoding.PCM_SIGNED;

		AudioInputStream newStream;
		newStream = AudioSystem.getAudioInputStream(targetEncoding,
				audioInputStream);

		AudioFormat audioFormat = newStream.getFormat();

		DataLine.Info info = new DataLine.Info(SourceDataLine.class,
				audioFormat);

		if (!AudioSystem.isLineSupported(info)) {

			hasError_ = true;
			System.err.println("ERROR: AudioPlayer data line not supported!");
			return;

		} else {

			try {

				dataLine_ = (SourceDataLine) AudioSystem.getLine(info);

				dataLine_.open(audioFormat);

				dataLine_.start();

			} catch (LineUnavailableException e) {

				hasError_ = true;
				System.err
						.println("EXCEPTION: AudioPlayer LineUnavailableException");
				return;
			} catch (Exception e) {

				e.printStackTrace();
				hasError_ = true;
				System.err.println("EXCEPTION: AudioPlayer other");
				return;
			}

			if (!hasError_) {

				int bufferSize = (int) audioFormat.getSampleRate()
						* audioFormat.getFrameSize();
				byte[] buffer = new byte[bufferSize];

				try {

					int bytesRead = 0;

					while (bytesRead != -1) {

						bytesRead = audioInputStream.read(buffer, 0,
								buffer.length);

						if (bytesRead >= 0) {

							dataLine_.write(buffer, 0, bytesRead);
						}
					}
				} catch (IOException e) {

					System.err.println("EXCEPTION: IOException");
				} catch (Exception e) {

					e.printStackTrace();
				}

				dataLine_.drain();
				dataLine_.close();
			}
		}
	}
}

