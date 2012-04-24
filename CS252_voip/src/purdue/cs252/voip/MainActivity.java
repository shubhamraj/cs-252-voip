package purdue.cs252.voip;

import java.util.LinkedList;
import purdue.cs252.voip.R;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity {

	Button recordButton;
	Button settingsButton;
	boolean clicked;
	DirectoryClient client;
	UserOptions settings;
	ListView listView;
	
	String[] values = new String[]{"No Users Present"};
	//private LinkedList<Contact> contactList = new LinkedList<Contact>();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		ArrayAdapter<String> adapter;
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, values);
		ListView listView = (ListView)findViewById(R.id.contactList);
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(new OnItemClickListener(){

			public void onItemClick(AdapterView<?> arg0, View view, int pos,
					long id) {
				Toast.makeText(getApplicationContext(), "Click ListItem Number " + pos, Toast.LENGTH_SHORT).show();
				
			}
			
		});
		setOnClickListeners();
		
			
	}
	
	public void onResume(){
		super.onResume();
		refreshUsers();
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
	}
	
	private void openOptions(){
		Intent launchOptions = new Intent(getApplicationContext(),UserOptions.class);
		startActivity(launchOptions);
	}
	

}
		
	

