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
	int sampleRate = 8000;
	int channelConfig = AudioFormat.CHANNEL_CONFIGURATION_MONO;
	int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
	int bufferSize;
	static byte buffer[][];
	
	public Recorder(){
		bufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat);
		buffer = new byte[10][bufferSize];
	}
	
	
//comment
	@Override
	public void run() {
		int i = 0;
		android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
		// Create a new recorder
		recorder = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate,
				channelConfig, audioFormat, bufferSize);

		// Start the recording
		recorder.startRecording();
		// Loop forever recording input
		while (VoiceCaptureClient.running) {
			// Read from the microphone
			i = i%10;
			
			recorder.read(buffer[i++], 0, bufferSize);
		}

	}
}
