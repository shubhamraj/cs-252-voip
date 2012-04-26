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
	private byte[] buffer;
	private String ipAddress;
	private int portNumber;
	private InetAddress serverAddr;
	private DatagramSocket socket;
	private boolean running = false;
	//comment
	
	VoicePlayer voicePlayer;
	VoiceServer receiver;
	Thread playerThread, receiverThread;
	public VoicePlayerServer(){
		bufferSize = AudioTrack.getMinBufferSize(sampleRate, channelConfig, audioFormat);
		buffer = new byte[bufferSize];
		voicePlayer = new VoicePlayer();
		receiver = new VoiceServer();;
	}
	
	public void stopRunning(){
		running = false;
		
		try {
			receiverThread.join();
			playerThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void startRunning(){
		running = true;
		
		// Start the server
		receiverThread = new Thread(receiver);
		playerThread = new Thread(voicePlayer);
		
		receiverThread.start();
		playerThread.start();
	}
	
	private class VoicePlayer implements Runnable{
		public VoicePlayer(){
			player = new AudioTrack(AudioManager.STREAM_VOICE_CALL, sampleRate, 
					channelConfig, audioFormat, bufferSize, AudioTrack.MODE_STREAM);
		}
		
		public void run() {
			android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
			player.play();
			// Loop forever playing the audio
			while (running) {
				// Play the sound
				player.write(buffer, 0, bufferSize);
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
				while(running){
					
					DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
					socket.receive(packet);
					buffer=packet.getData();
					
				}
				socket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
	}

	public void setIPandPort(String ip, int port) {
		// TODO Auto-generated method stub
		this.ipAddress = ip;
		this.portNumber = port;
	}
	
}