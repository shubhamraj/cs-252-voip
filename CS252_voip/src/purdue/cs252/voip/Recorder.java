/**
 * Recorder.java
 * 
 * Continuously records sound to buffer.
 * buffer is shared with VoiceCaptureClient class.
 * 
 * @author Farrukh Yakubov
 * @date 4/19/2012
 * 
 */

package purdue.cs252.voip;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

public class Recorder implements Runnable {

	int sampleRate;
	int channelConfig;
	int audioFormat;
	int bufferSize;
	static byte buffer[];
	
	public Recorder(){
		
		sampleRate = 11025;
		channelConfig = AudioFormat.CHANNEL_CONFIGURATION_MONO;
		audioFormat = AudioFormat.ENCODING_PCM_16BIT;
		bufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat);
		buffer = new byte[bufferSize];
	}
	
	private AudioRecord recorder;

	@Override
	public void run() {
		// Create a new recorder
		recorder = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate,
				channelConfig, audioFormat, bufferSize);

		// Start the recording
		recorder.startRecording();

		// Loop forever recording input
		while (true) {
			// Read from the microphone
			recorder.read(buffer, 0, bufferSize);
		}

	}
}
