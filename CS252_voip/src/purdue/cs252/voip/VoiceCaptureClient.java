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

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

//comment
public class VoiceCaptureClient implements Runnable {
	
	static boolean running;
	String SERVERIP;
	int SERVERPORT;

	Thread rec;
	private WifiManager mWifi;
	
	private AudioRecord recorder;
	
	//int sampleRate = 8000;
	//int channelConfig = AudioFormat.CHANNEL_CONFIGURATION_MONO;
	//int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
	//int bufferSize;
	//byte buffer[];
	
	
	public VoiceCaptureClient(final String SERVERIP, final int SERVERPORT){
		
		//bufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat);
		//Log.d("REC", Integer.toString(bufferSize));
		//buffer = new byte[bufferSize];
		
		
		this.SERVERIP = SERVERIP;
		this.SERVERPORT = SERVERPORT;
		
		running = true;
		//mWifi = (WifiManager) MainActivity.context.getSystemService(Context.WIFI_SERVICE);
		rec = new Thread(new Recorder());
		rec.start();
	}
	public void setStop(){
		running = false;
	}
	@Override
	public void run() {
		//android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
				// Create a new recorder
				//recorder = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate,
					//	channelConfig, audioFormat, bufferSize);

				// Start the recording
				//recorder.startRecording();
				
		//android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
		try {
			// Retrieve the ServerName
			InetAddress serverAddr = InetAddress.getByName(SERVERIP);

			//Log.d("UDP", "C: Connecting...");
			/* Create new UDP-Socket */
			DatagramSocket socket = new DatagramSocket();
			//socket.connect(serverAddr, SERVERPORT);
			Log.d("REC", "Starting to send packets to "+serverAddr);
			//Log.d("REC", "Socket at "+socket.getRemoteSocketAddress());
			//long i = 0;
			DatagramPacket packet = null;
			while (running) {
				
				/* Create UDP-packet with
				 * data & destination(url+port) */
				//recorder.read(buffer, 0, buffer.length);
				packet = new DatagramPacket(Recorder.buffer, Recorder.buffer.length, serverAddr, SERVERPORT);
				/* Send out the packet */
				socket.send(packet);
				
				try{
					Thread.sleep(70);
				} catch(Exception e){
					
				}
				//Log.d("REC", "Sending a packet "+ i);
				DirectLinkTest.sent++;
			}
			//recorder.stop();
			Log.d("REC", "Done Sending all.");
			
		} catch (Exception e) {
			Log.e("UDP", "VoiceCaptureClient: Error sending UDP packets", e);
		}
	}
	private InetAddress getBroadcastAddress() throws IOException {
	    DhcpInfo dhcp = mWifi.getDhcpInfo();
	    if (dhcp == null) {
	      Log.d("CAPTURE", "Could not get dhcp info");
	      return null;
	    }

	    int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
	    byte[] quads = new byte[4];
	    for (int k = 0; k < 4; k++)
	      quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
	    return InetAddress.getByAddress(quads);
	  }
}
