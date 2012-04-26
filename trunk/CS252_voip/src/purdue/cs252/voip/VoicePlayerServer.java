package purdue.cs252.voip;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

public class VoicePlayerServer{
	private AudioTrack player;
	private int sampleRate = 8000;
	private int channelConfig = AudioFormat.CHANNEL_CONFIGURATION_MONO;
	private int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
	private int bufferSize;
	private byte[][] buffer;
	private String ipAddress;
	private int portNumber;
	private InetAddress serverAddr;
	private DatagramSocket socket;
	private boolean running = false;
	//comment
	public VoicePlayerServer(String ip, int port){
		bufferSize = AudioTrack.getMinBufferSize(sampleRate, channelConfig, audioFormat);
		buffer = new byte[10][bufferSize];
		ipAddress = ip;
		portNumber = port;
		
		startRunning();
	}
	
	public void stopRunning(){
		running = false;
	}

	public void startRunning(){
		running = true;
		// Start the server
		new Thread(new VoiceServer()).start();
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) { }
		
		new Thread(new VoicePlayer()).start();
	}
	
	private class VoicePlayer implements Runnable{
		public VoicePlayer(){
			player = new AudioTrack(AudioManager.STREAM_VOICE_CALL, sampleRate, 
					channelConfig, audioFormat, bufferSize, AudioTrack.MODE_STREAM);
		}
		
		public void run() {
			android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
			player.play();
			int i = 0;
			// Loop forever playing the audio
			while (running) {
				// Play the sound
				i = i%10;
				player.write(buffer[i], 0, bufferSize);
				i++;
			}
			
		}
	}
	
	private class VoiceServer implements Runnable{
		public VoiceServer(){
			try{
				serverAddr = InetAddress.getByName(ipAddress);
				socket = new DatagramSocket(portNumber, serverAddr);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		
		@Override
		public void run() {
			android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
			try {
				int i = 0;
				while(running){
					i = i%10;
					DatagramPacket packet = new DatagramPacket(buffer[i], buffer[i].length);
					socket.receive(packet);
					buffer[i]=packet.getData();
					i++;
				}
				socket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
}