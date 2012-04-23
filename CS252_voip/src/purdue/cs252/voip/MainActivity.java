package purdue.cs252.voip;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import android.app.Activity;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

	Button recordButton;
	Button settingsButton;
	boolean clicked;
	VoiceCaptureClient captureClient;
	VoicePlayerServer playerServer;
	
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
				captureClient = new VoiceCaptureClient("10.184.104.69", 20000);
				new Thread(captureClient).start();
				playerServer = new VoicePlayerServer("128.210.246.105", 20000);
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
	
	public String getLocalIpAddress() {
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
	

}
		
	

