package CS252.lab6.VOIP;

import java.util.LinkedList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity {

	Button recordButton;
	Button settingsButton;
	boolean clicked;
	
	String[] values = new String[]{"Jaya", "Brian", "Cole", "Nick", "Farrukh"};
	//private LinkedList<Contact> contactList = new LinkedList<Contact>();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, values);
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
	
		
	/*	@Override
		public boolean onCreateOptionsMenu(Menu menu) {
		    MenuInflater inflater = getMenuInflater();
		    inflater.inflate(R.menu.settings, menu);
		    return true;
		}*/


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
		
	

