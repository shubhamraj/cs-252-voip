package purdue.cs252.voip;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import android.content.Intent;
import android.util.Log;

public class RingerServer implements Runnable { 

		public String SERVERIP; 
		public static int SERVERPORT = 20000;
		MainActivity temp = new MainActivity();

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
					temp.display();
					Log.d("TCP", "S: New connection received.");
					// Read data from the client
					InputStream stream = clientSocket.getInputStream();
					// InputStream is an abstract class. We needed to use a subclass
					BufferedReader data = new BufferedReader(new InputStreamReader(stream));

					// Read a line at a time
					String line;
					while ((line = data.readLine()) != null) {
						Log.d("TCP", "S: Received: '" + line + "'");
					}
					Log.d("TCP", "S: Done.");

				} while (true);

			} catch (IOException e) {
				Log.e("TCP", "S: Error", e);
			}
		}
	
}
