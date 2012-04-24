package purdue.cs252.voip;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import android.util.Log;


public class DirectoryClient {
	static String name = "default_user";
	static String serverIp = "127.0.0.1";
	static int portNum = 4567;
	public static boolean joinedServer = false;
	public static void joinServer(String address, int port, String myName){
		//connect to a server and set values
		serverIp = address;
		portNum = port;
		name = myName;
		Log.d("DEBUG", "ip = " + serverIp + "port = " + portNum + "name = " + name);
		joinedServer = true;
		try{
			//connect to the server and send an "adduser" command
			InetAddress ip = InetAddress.getByName(serverIp);
			Socket sock = new Socket(ip, portNum);
			PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sock.getOutputStream())), true);
			out.println("adduser");
			out.println(name);
			sock.close();
		}
		catch(UnknownHostException e){
			Log.d("Unknown Host", "Unknown Host");
		}
		catch(IOException e){
			Log.d("IO EXCEPTION", "IO EXCEPTION");
		}
	}
	
	public static void leaveServer(){
		try{
			//connect to a server and send 
			joinedServer = false;
			InetAddress ip = InetAddress.getByName(serverIp);
			Socket sock = new Socket(ip, portNum);
			PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sock.getOutputStream())), true);
			out.println("removeuser");
			out.println(name);
			sock.close();
		}
		catch(UnknownHostException e){
			System.err.println("TCP EXCEPTION");
		}
		catch(IOException e){
			System.err.println("IO EXCEPTION");
		}
	}
	
	public static String lookupIp(String hostName)
	{
		System.out.println("looking up " + hostName);
		try{
			InetAddress ip = InetAddress.getByName(serverIp);
			Socket sock = new Socket(ip, portNum);
			PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sock.getOutputStream())), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			out.println("lookup");
			out.println(hostName);
			
			String theirIp = in.readLine();
			System.out.println("got " + theirIp);
			sock.close();
			return theirIp;
		}
		catch(UnknownHostException e){
			System.err.println("TCP EXCEPTION");
			return null;
		}
		catch(IOException e){
			System.err.println("IO EXCEPTION");
			return null;
		}
	}
	
	public static String[] getUserList(){
		try{
			InetAddress ip = InetAddress.getByName(serverIp);
			Socket sock = new Socket(ip, portNum);
			PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sock.getOutputStream())), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			out.println("listusers");
			int numUsers = Integer.parseInt(in.readLine());
			String[] list = new String[numUsers];
			for(int x = 0; x < numUsers; x++)
				list[x] = in.readLine();
			
			sock.close();
			
			return list;
		}
		catch(UnknownHostException e){
			System.err.println("TCP EXCEPTION");
			return null;
		}
		catch(IOException e){
			System.err.println("IO EXCEPTION");
			return null;
		}
	}
	

}

