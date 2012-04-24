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

	private AudioRecord recorder;
	int sampleRate = 11025;
	int channelConfig = AudioFormat.CHANNEL_CONFIGURATION_MONO;
	int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
	int bufferSize;
	static byte buffer[];
	
	public Recorder(){
		bufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat);
		buffer = new byte[bufferSize];
	}
	
	

	@Override
	public void run() {
		// Create a new recorder
		recorder = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate,
				channelConfig, audioFormat, bufferSize);

		// Start the recording
		recorder.startRecording();
		// Loop forever recording input
		while (VoiceCaptureClient.running) {
			// Read from the microphone
			recorder.read(buffer, 0, bufferSize);
		}

	}
}
