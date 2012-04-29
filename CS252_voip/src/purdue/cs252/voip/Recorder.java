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
import android.util.Log;

public class Recorder implements Runnable {

	private AudioRecord recorder;
	
	int sampleRate = 8000;
	int channelConfig = AudioFormat.CHANNEL_CONFIGURATION_MONO;
	int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
	static int bufferSize;
	int minSize;
	static byte buffer[];
	
	public Recorder(){
		bufferSize = 3072;
		minSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat);
		//bufferSize = minSize-128;
		Log.d("REC", Integer.toString(minSize));
		buffer = new byte[bufferSize];
	}
	
	
//comment
	@Override
	public void run() {
		//android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
		// Create a new recorder
		recorder = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate,
				channelConfig, audioFormat, minSize*10);

		// Start the recording
		recorder.startRecording();
		// Loop forever recording input
		while (VoiceCaptureClient.running) {
			// Read from the microphone
			recorder.read(buffer, 0, buffer.length);
		}

	}
}
