package purdue.cs252.voip;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import purdue.cs252.voip.R;
import android.app.Activity;
import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

public class DirectLinkTest extends Activity{
	EditText ip, port, localIP;
	TextView sent_l, recieved_l;
	ImageButton saveButton, callButton, exitButton;
	String ipString = "";
	int portNum = 5000;

	VoicePlayerServer vs;
	VoiceCaptureClient vc;
	static Context context;
	static long sent = 0, recieved = 0;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.audiotest);	
		context = this.getApplicationContext();
		ip = (EditText)findViewById(R.id.ipAddress);
		ipString = "192.168.1.3";
		ip.setText(ipString);
		port = (EditText)findViewById(R.id.port);
		port.setText(Integer.toString(portNum));
		localIP = (EditText)findViewById(R.id.localIP);
		try {
			InetAddress ia = this.getBroadcastAddress();
			localIP.setText(getLocalIpAddress());
			Log.d("LINKTEST","Host Address " +ia.getHostAddress());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		localIP.setEnabled(false);
		saveButton = (ImageButton)findViewById(R.id.saveInfo);
		callButton = (ImageButton)findViewById(R.id.startCall);
		exitButton = (ImageButton)findViewById(R.id.exitButton);
		sent_l = (TextView)findViewById(R.id.lbl_sent);
		recieved_l = (TextView)findViewById(R.id.lbl_recieved);
		setOnClickListeners();
	}
	
	
	
	public void onResume(){
		super.onResume();
		ip.setText(ipString);
		port.setText(Integer.toString(portNum));
		
	}
	
	
	public void setOnClickListeners(){
		saveButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//ipString = ip.getText().toString();
				//portNum = Integer.parseInt(port.getText().toString());
				//Log.d("CALL", "Saved IP & Port: " +ipString + " " + portNum);
				//new Thread(new Recorder());
				//new VoicePlayerServer(5000);
				Log.d("CALL", "Saved IP & Port: " +ipString + " " + portNum);
			}
		});
		callButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				ipString = ip.getText().toString();
				portNum = Integer.parseInt(port.getText().toString());
				Log.d("CALL", "Saved IP & Port: " +ipString + " " + portNum);
				
				
				vs = new VoicePlayerServer(portNum);
				vc = new VoiceCaptureClient(ipString, portNum);
				new Thread(vc).start();
				
			}
		});
		exitButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				vs.stopRunning();
				vc.setStop();
				sent_l.setText("Sent: "+ sent);
				recieved_l.setText("Recieved: "+ recieved);
				
				Toast t = Toast.makeText(context, "Sent: " + sent+" Received: " + recieved, 1000*Toast.LENGTH_LONG);
				t.show();
				vs = null;
				vc = null;
				finish();

			}
		});
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
	private InetAddress getBroadcastAddress() throws IOException {
		WifiManager mWifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
	    DhcpInfo dhcp = mWifi.getDhcpInfo();
	    if (dhcp == null) {
	      Log.d("CAPTURE", "Could not get dhcp info");
	      return null;
	    }

	    int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
	    byte[] quads = new byte[4];
	    for (int k = 0; k < 4; k++)
	      quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
	    return InetAddress.getByAddress(quads);
	  }
}
