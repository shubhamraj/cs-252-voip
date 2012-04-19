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


public class DirectoryClient {
	String name = "default_user";
	String serverIp = "127.0.0.1";
	int portNum = 4567;
	
	void joinServer(String address, int port, String myName){
		//connect to a server and set values
		serverIp = address;
		portNum = port;
		name = myName;
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
			System.err.println("TCP EXCEPTION");
		}
		catch(IOException e){
			System.err.println("IO EXCEPTION");
		}
	}
	
	void leaveServer(){
		try{
			//connect to a server and send 
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
	
	String lookupIp(String hostName)
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
	
	String[] getUserList(){
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
	
	
	
	public static void main(String[] args){
		DirectoryClient client = new DirectoryClient();
		client.joinServer("lore.cs.purdue.edu", 4567, args[0]);
		
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		int exit = 0;
		while(exit ==0){
			try{
				String line = in.readLine();
				if(line.equals("exit"))
					exit = 1;
				else if(line.contains("lookup")){
					String[] s = line.split(" ");
					String ip = client.lookupIp(s[1]);
					System.out.println("Address of " + s[1] + " : " + ip);
				}
				else if(line.contains("list")){
					String[] list = client.getUserList();
					System.out.println("\nUser List:");
					for(String s : list)
						System.out.println(s);
					System.out.println();
				}
			}
			catch(IOException e){}
			
		}
		
		client.leaveServer();
		
			
		
		
		
	}

}

