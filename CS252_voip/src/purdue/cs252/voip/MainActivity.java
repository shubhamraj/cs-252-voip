package purdue.cs252.voip;

import java.net.Socket;
import java.util.LinkedList;
import purdue.cs252.voip.R;
import purdue.cs252.voip.RingerClient;
import purdue.cs252.voip.RingerServer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
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

	Button recordButton;
	Button settingsButton;
	
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
		
		listView.setOnItemClickListener(new OnItemClickListener(){

			public void onItemClick(AdapterView<?> arg0, View view, int pos,
					long id) {
				//Toast.makeText(getApplicationContext(), "Click ListItem Number " + pos, Toast.LENGTH_SHORT).show();4
				ipAddress = DirectoryClient.lookupIp(arg0.getItemAtPosition(pos).toString());
				callee = arg0.getItemAtPosition(pos).toString();
				
				//rserv.SERVERPORT = settings.getPort();
				Log.d("SERVERIP", ipAddress);
				RingerClient ringerClient = new RingerClient();
				tempName = UserOptions.settings.getString("userIdText", null);
				//displayFrom(tempName);
				ringerClient.start(ipAddress, tempName);
				
				
			}
			
		});
		//following 2 lines are for testing only
		testButton = (Button)findViewById(R.id.callTest);
		testPage = new Intent(getApplicationContext(),DirectLinkTest.class);

		
		setOnClickListeners();
		
		
			
	}
	
	public void onResume(){
		super.onResume();
		refreshUsers();
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
	

}
		
	

