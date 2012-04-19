//package com.tcp.example.rudy;
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

import java.util.Enumeration;
import java.util.Hashtable;


public class DirectoryServer {
	static Hashtable<String, String> h;
	
	//add a user to the table
	static void addUser(String name, String ip){
		h.put(name, ip);
	}
	
	static void removeUser(String name){
		h.remove(name);
	}
	//return a user's ip address
	static String lookupIp(String name)
	{
		return h.get(name);
	}
	
	//return a list of online users
	static String[] getUserList()
	{
		Enumeration<String> e = h.keys();
		String[] names = new String[h.size()];
		for(int x = 0; x < names.length; x++)
			names[x] = e.nextElement();
		return names;
	}
	
	public static void printServerStatus(){
		System.out.println("\nConnected Clients:");
		String[] list = getUserList();
		for(String s : list){
			String ip = h.get(s);
			System.out.println(s + " : " + ip);
		}
	}
	
	public static void main(String[] args)
	{
		h = new Hashtable<String,String>();
		
		int portNum = Integer.parseInt(args[0]);
		try{
			//Create a server socket to listen for connections
			ServerSocket server = new ServerSocket(portNum);
			while(true){
				//repeatedly try to accept an incoming client
				Socket slave = server.accept();
				
				//set up i/o to socket
				InputStream is = slave.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(is));
				PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(slave.getOutputStream())), true);
				//First line that client sends indicates action
				String action = reader.readLine();
				System.out.println(action);
				if(action.equals("adduser")){
					//If we are adding a user, read the name, and get the ip address from the socket
					//and add them to the table
					String user = reader.readLine();
					String ip = slave.getInetAddress().getHostAddress();
					addUser(user, ip);
					printServerStatus();
				}
				if(action.equals("removeuser")){
					//If we are removing a user, read the name and delete them
					String user = reader.readLine();
					removeUser(user);
					printServerStatus();
				}
				if(action.equals("lookup")){
					//IF we are looking up an ip, get the user name from the client
					//and send back the ip
					String name = reader.readLine();
					String ip = lookupIp(name);
					System.out.println(name + " ==> " + ip);
					pw.println(ip);
				}
				if(action.equals("listusers")){
					//If we are listing users, write the list to the client
					String[] userList = getUserList();
					//write size and then list
					pw.println(userList.length);
					for(String s : userList)
						pw.println(s);
				}
				
				slave.close();
				
			}
		}
		catch(UnknownHostException e){
			System.err.println("TCP EXCEPTION");
		}
		catch(IOException e){
			System.err.println("IO EXCEPTION");
		}
		
	}
	
	

}
