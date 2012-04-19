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

public class VoiceCaptureClient implements Runnable {
	
	String SERVERIP;
	int SERVERPORT;

	Thread rec;
	
	public VoiceCaptureClient(final String SERVERIP, final int SERVERPORT){
		
		this.SERVERIP = SERVERIP;
		this.SERVERPORT = SERVERPORT;
		
		rec = new Thread(new Recorder());
		rec.start();
	}
	
	@Override
	public void run() {
		try {
			// Retrieve the ServerName
			InetAddress serverAddr = InetAddress.getByName(SERVERIP);

			//Log.d("UDP", "C: Connecting...");
			/* Create new UDP-Socket */
			DatagramSocket socket = new DatagramSocket();
			
			while (true) {
				/* Create UDP-packet with
				 * data & destination(url+port) */
				DatagramPacket packet = new DatagramPacket(Recorder.buffer, Recorder.buffer.length, serverAddr, SERVERPORT);
				/* Send out the packet */
				socket.send(packet);
			}
			
		} catch (Exception e) {
			Log.e("UDP", "VoiceCaptureClient: Error sending UDP packets", e);
		}
	}
}
