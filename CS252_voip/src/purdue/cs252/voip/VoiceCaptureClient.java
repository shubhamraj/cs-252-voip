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
import android.util.Log;

//comment
public class VoiceCaptureClient implements Runnable {
	
	static boolean running;
	String SERVERIP;
	int SERVERPORT;

	Thread rec;
	
	public VoiceCaptureClient(final String SERVERIP, final int SERVERPORT){
		
		this.SERVERIP = SERVERIP;
		this.SERVERPORT = SERVERPORT;
		running = true;
		rec = new Thread(new Recorder());
		rec.start();
	}
	public void setStop(){
		running = false;
	}
	@Override
	public void run() {
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
			//int i =0;
			while (running) {
				
				/* Create UDP-packet with
				 * data & destination(url+port) */
				DatagramPacket packet = new DatagramPacket(Recorder.buffer, Recorder.buffer.length, serverAddr, SERVERPORT);
				/* Send out the packet */
				
				socket.send(packet);
				//Log.d("REC", "Sending a packet "+ i);
				//i++;
			}
			Log.d("REC", "Done Sending all.");
			
		} catch (Exception e) {
			Log.e("UDP", "VoiceCaptureClient: Error sending UDP packets", e);
		}
	}
}
