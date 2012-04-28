package purdue.cs252.voip;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

public class VoicePlayerServer{
	private AudioTrack player;
	private int sampleRate = 8000;
	private int channelConfig = AudioFormat.CHANNEL_CONFIGURATION_MONO;
	private int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
	private int bufferSize;
	private byte[] buffer;
	private String ipAddress;
	private int portNumber;
	private InetAddress serverAddr;
	private DatagramSocket socket;
	private boolean running = false;
	
	//comment
	public VoicePlayerServer(String ip, int port){
		bufferSize = AudioTrack.getMinBufferSize(sampleRate, channelConfig, audioFormat);
		buffer = new byte[bufferSize];
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
			//android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
			player.play();

			// Loop forever playing the audio
			while (running) {
				// Play the sound
				player.write(buffer, 0, bufferSize);
			}
			player.stop();
			
		}
	}
	
	private class VoiceServer implements Runnable{
		public VoiceServer(){
			try{
				//serverAddr = InetAddress.getByName(ipAddress);
				socket = new DatagramSocket(null);
				socket.setReuseAddress(true);
				socket.bind(new InetSocketAddress(portNumber));
				
				Log.d("SERVER", "Listening at: " + socket.getLocalAddress());
				//socket.connect(InetAddress.getByName(ipAddress), portNumber);
				//Log.d("SERVER", "Listening at: " + socket.getInetAddress().getHostAddress());
			}
			catch(SocketException e){
				e.printStackTrace();
				Log.d("VoiceServer", "Exception has occured trying open a socket.");
				Log.d("VoiceServer", "Details:" + e.getMessage());
			}
		}
		
		@Override
		public void run() {
			//android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
			try {
				int i =0;
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
				while(running){
					Log.d("SERVER", "Waiting for packet...");
					socket.receive(packet);
					buffer=packet.getData();
					StringBuilder s = new StringBuilder();
					for (int j = 0; j < buffer.length; j++){
						s.append((char)buffer[j]);
					}
					Log.d("SERVER", "PacketReceived "+i + " Data: " + s.toString());
					i++;
				}
			} catch (IOException e) {
				e.printStackTrace();
				Log.d("VoiceServer", "IOException has occured trying to receive UDP packets.");
			}
			
		}
		
	}
	
}