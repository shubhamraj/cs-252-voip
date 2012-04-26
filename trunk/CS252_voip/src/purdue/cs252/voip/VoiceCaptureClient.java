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
	Recorder r;
	
	public VoiceCaptureClient(final String SERVERIP, final int SERVERPORT){
		
		this.SERVERIP = SERVERIP;
		this.SERVERPORT = SERVERPORT;
		running = true;
		r = new Recorder();
		rec = new Thread(r);
		rec.start();
	}
	public void setStop(){
		running = false;
		try {
			rec.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void run() {
		android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
		try {
			// Retrieve the ServerName
			InetAddress serverAddr = InetAddress.getByName(SERVERIP);

			//Log.d("UDP", "C: Connecting...");
			/* Create new UDP-Socket */
			DatagramSocket socket = new DatagramSocket();
			int i = 0;
			while (running) {
				/* Create UDP-packet with
				 * data & destination(url+port) */
				i = i%10;
				DatagramPacket packet = new DatagramPacket(Recorder.buffer[i], Recorder.buffer[i].length, serverAddr, SERVERPORT);
				i++;
				/* Send out the packet */
				socket.send(packet);
			}
			socket.close();
			
		} catch (Exception e) {
			Log.e("UDP", "VoiceCaptureClient: Error sending UDP packets", e);
		}
	}
}
