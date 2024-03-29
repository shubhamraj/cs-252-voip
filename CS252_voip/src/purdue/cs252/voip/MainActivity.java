package purdue.cs252.voip;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.LinkedList;
import purdue.cs252.voip.R;
import purdue.cs252.voip.RingerClient;
import purdue.cs252.voip.RingerServer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity {

	public static VoicePlayerServer voicePlayer;
	public static VoiceCaptureClient voiceCapture;
	final static int UDP_PORT = 5000;
	Button recordButton, settingsButton, refreshButton, btn_quit;
	
	//following button is for testing only
	Button testButton; Intent testPage;
	
	boolean clicked;
	DirectoryClient client;
	ListView listView;
	RingerClient ringerClient;
	public static String callerName;
	public static Socket callerSocket;
	public static String callee;
	public static String tempName;
	public static boolean initiatingCall = false;
	public static boolean chatting = false;
	
	String[] values = new String[]{"No Users Present"};
	public static String ipAddress;
	//private LinkedList<Contact> contactList = new LinkedList<Contact>();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		new Thread(new RingerServer(this)).start();
		
		ArrayAdapter<String> adapter;
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, values);
		ListView listView = (ListView)findViewById(R.id.contactList);
		listView.setAdapter(adapter);
		
		//voiceCapture = new VoiceCaptureClient();
		//voicePlayer = new VoicePlayerServer();
		
		listView.setOnItemClickListener(new OnItemClickListener(){
			
			public void onItemClick(AdapterView<?> arg0, View view, int pos,
					long id) {
				//Toast.makeText(getApplicationContext(), "Click ListItem Number " + pos, Toast.LENGTH_SHORT).show();4
				startDialog(arg0,pos);
				
				//MainActivity.startCall(ipAddress);
			}
			
		});
		//following 2 lines are for testing only
		testButton = (Button)findViewById(R.id.callTest);
		btn_quit = (Button)findViewById(R.id.btn_quit);
		testPage = new Intent(getApplicationContext(),DirectLinkTest.class);

		
		setOnClickListeners();
		
		
			
	}
	
	 public void startDialog(AdapterView<?> arg0, final int pos){
		 
		 ipAddress = DirectoryClient.lookupIp(((AdapterView<ListAdapter>) arg0).getItemAtPosition(pos).toString());
		 callee = ((AdapterView<ListAdapter>) arg0).getItemAtPosition(pos).toString();
		 Log.d("ipAddress", ipAddress);
		 Log.d("callee", callee);
		 Log.d("SERVERIP", ipAddress);
		 tempName = UserOptions.settings.getString("userIdText", null);
	     
		 AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
	     myAlertDialog.setTitle("Make a Selection");
	     myAlertDialog.setPositiveButton("Call", new DialogInterface.OnClickListener() {
	        
	            // do something when the button is clicked
	            public void onClick(DialogInterface arg0, int arg1) {
	            	MainActivity.startCall(ipAddress);
					//displayFrom(tempName);
					//ringerClient.start(ipAddress, tempName);
	            }
	        });
	     myAlertDialog.setNeutralButton("Chat", new DialogInterface.OnClickListener() {
	       
	            // do something when the button is clicked
	            public void onClick(DialogInterface arg0, int arg1) {
	            	chatting = true;
	            	RingerClient ringerClient = new RingerClient();
	            	ringerClient.start(ipAddress, tempName);
	            }
	        });
	     myAlertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	       
	            // do something when the button is clicked
	            public void onClick(DialogInterface arg0, int arg1) {
	                Toast.makeText(getApplicationContext(), "'No' button clicked", Toast.LENGTH_LONG).show();
	            }
	        });
	     myAlertDialog.show();
	    }
	
	public void onResume(){
		super.onResume();
		refreshUsers();
	}
	
	public void onDestroy(){
		super.onDestroy();
		DirectoryClient.leaveServer();	
	}
	
	public void onPause(){
		super.onPause();
		//DirectoryClient.leaveServer();
	}
	
	public String getIpAddress(){
		return ipAddress;
	}
	
	public void displayFrom(String name){
		callerName = name;
		initiatingCall = true;
		Intent launchCallScreen = new Intent(getApplicationContext(),CallScreen.class);
		startActivity(launchCallScreen);
	}
	
	public void display(String name, Socket socket){
		callerSocket = socket;
		callerName = name;
		initiatingCall = false;
		Intent launchCallScreen = new Intent(getApplicationContext(),CallScreen.class);
		startActivity(launchCallScreen);
	}
	
	public void refreshUsers(){
		if(DirectoryClient.joinedServer == true){
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, DirectoryClient.getUserList());
		ListView listView = (ListView)findViewById(R.id.contactList);
		listView.setAdapter(adapter);
		}
	}


	public void setOnClickListeners(){
		settingsButton = (Button)findViewById(R.id.settings);
		refreshButton = (Button)findViewById(R.id.refreshButton);
		
		refreshButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				refreshUsers();
				
			}
		});
		btn_quit.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
				DirectoryClient.leaveServer();	
				finish();
				System.exit(0);
			}
		});
		settingsButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				//go to setting menu
				openOptions();	
				
			}
		});
		
		testButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
				startActivity(testPage);
			}
		});
	}
	
	private void openOptions(){
		Intent launchOptions = new Intent(getApplicationContext(),UserOptions.class);
		startActivity(launchOptions);
	}
	static String getLocalIpAddress() {
	    try {
	        for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
	            NetworkInterface intf = en.nextElement();
	            for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
	                InetAddress inetAddress = enumIpAddr.nextElement();
	                if (!inetAddress.isLoopbackAddress()) {
	                    return inetAddress.getHostAddress().toString();
	                }
	            }
	        }
	    } catch (SocketException ex) {
	        //Log.e(LOG_TAG, ex.toString());
	    }
	    return null;
	}
	static Thread vc;
	public static void startCall(String toIP){
		voicePlayer = new VoicePlayerServer(UDP_PORT);
		voiceCapture = new VoiceCaptureClient(toIP, UDP_PORT);
		vc = new Thread(voiceCapture);
		vc.start();
	}
	public static void endCall(){
		
		voiceCapture.setStop();
		voicePlayer.stopRunning();
		try {
			vc.join();
		} catch (Exception e){
			
		}
	}
	
	

}
		
	

