/**
 * VoiceCaptureClient.java
 * 
 * Continuously sends sound data packets to VoicePlayerServer on connected device.
 * 
 * buffer is shared with Recorder class, which writes sound data to the buffer.
 * 
 * @author Farrukh Yakubov
 * @date 4/19/2012
 * 
 */

package purdue.cs252.voip;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

//comment
public class VoiceCaptureClient{
	
	boolean running;
	String SERVERIP;
	int SERVERPORT;

	Thread recorder;
	Thread sender;
	Recorder r;
	Sender s;
	byte buffer[];
	
	public VoiceCaptureClient(){
		
		running = true;
		r = new Recorder();
		s = new Sender();
	}
	
	public void stopRunning(){
		running = false;
		try {
			recorder.join();
			sender.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void startRunning(){
		recorder = new Thread(r);
		sender = new Thread(s);
		recorder.start();
		sender.start();
	}
	

	public void setIPandPort(String ipAddress, int port) {
		this.SERVERIP = ipAddress;
		this.SERVERPORT = port;
	}
	
	private class Sender implements Runnable {
		Sender(){
			
		}
		@Override
		public void run() {
			android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
			try {
				// Retrieve the ServerName
				InetAddress serverAddr = InetAddress.getByName(SERVERIP);

				Log.d("UDP", "C: Sending Voice packets to " + SERVERIP + "at port" + SERVERPORT);
				/* Create new UDP-Socket */
				DatagramSocket socket = new DatagramSocket();
				
				while (running) {
					/* Create UDP-packet with
					 * data & destination(url+port) */
					DatagramPacket packet = new DatagramPacket(buffer, buffer.length, serverAddr, SERVERPORT);
					
					/* Send out the packet */
					socket.send(packet);
				}
				socket.close();
				
			} catch (Exception e) {
				Log.e("UDP", "VoiceCaptureClient: Error sending UDP packets", e);
			}
		}
	}
	
	private class Recorder implements Runnable {

		private AudioRecord recorder;
		int sampleRate = 8000;
		int channelConfig = AudioFormat.CHANNEL_CONFIGURATION_MONO;
		int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
		int bufferSize;
		
		
		public Recorder(){
			bufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat);
			buffer = new byte[bufferSize];
		}
		
		
	//comment
		@Override
		public void run() {
			android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
			// Create a new recorder
			recorder = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate,
					channelConfig, audioFormat, bufferSize);

			// Start the recording
			recorder.startRecording();
			Log.d("REC", "C:Recording voice to buffer...");
			// Loop forever recording input
			while (running) {
				// Read from the microphone
				
				
				recorder.read(buffer, 0, bufferSize);
			}

		}
	}
}
