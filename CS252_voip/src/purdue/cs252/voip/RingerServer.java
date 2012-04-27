package purdue.cs252.voip;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import android.content.Intent;
import android.util.Log;

public class RingerServer implements Runnable { 
		public static String callerIP;
		public String SERVERIP; 
		public static int SERVERPORT = 20001;
		MainActivity temp;
		
		public RingerServer(MainActivity activity){
			temp = activity;
		}

		//@Override
		public void run() {
			try {
				//Log.d("Entered", SERVERIP);
				// Create a socket for handling incoming requests
				ServerSocket server = new ServerSocket(SERVERPORT);

				do {
					// Wait for an incoming connection
					Log.d("TCP", "S: Waiting for new connection...");
					
					Socket clientSocket = server.accept();
					InetAddress iAddress = clientSocket.getInetAddress();
					callerIP = iAddress.getHostAddress();
					Log.d("TCP", "S: New connection received from IP: " + callerIP);
					// Read data from the client
					InputStream stream = clientSocket.getInputStream();
					// InputStream is an abstract class. We needed to use a subclass
					BufferedReader data = new BufferedReader(new InputStreamReader(stream));

					// Read a line at a time
					String line;
					line = data.readLine();
					temp.display(line, clientSocket);
					Log.d("TCP", "S: Done.");

				} while (true);

			} catch (IOException e) {
				Log.e("TCP", "S: Error", e);
			}
		}
	
}
