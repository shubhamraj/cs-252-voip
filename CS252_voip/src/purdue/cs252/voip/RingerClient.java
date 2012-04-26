package purdue.cs252.voip;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

import purdue.cs252.voip.RingerServer;
import android.util.Log;

public class RingerClient{
	//@Override

	
	public void start(String ipAddress, String username) {
		try {
			InetAddress serverAddr = InetAddress.getByName(ipAddress);

			// Connect to the server
			Log.d("TCP", "C: Connecting to " + ipAddress);
			Socket clientSocket = new Socket(serverAddr, RingerServer.SERVERPORT);

			// Sending a message to the server
			Log.d("TCP", "C: Sending a packet.");
			PrintWriter out = new PrintWriter(new BufferedWriter(
					new OutputStreamWriter(clientSocket.getOutputStream())), true);
			out.println(username);
			
			InputStream stream = clientSocket.getInputStream();

			BufferedReader data = new BufferedReader(new InputStreamReader(stream));
			
			String callAction = data.readLine();
			
			Log.d("CallAction", callAction);
			
			Log.d("TCP", "C: Sent.");
			Log.d("TCP", "C: Done.");

			// Close the connection
			clientSocket.close();
		} catch (UnknownHostException e) {
			Log.e("TCP", "C: Error", e);
		} catch (IOException e) {
			Log.e("TCP", "C: Error", e);
		}
	}
}