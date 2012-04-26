package purdue.cs252.voip;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import purdue.cs252.voip.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class DirectLinkTest extends Activity{
	EditText ip, port, localIP;
	
	ImageButton saveButton, callButton, exitButton;
	String ipString = "";
	int portNum = 5000;
	VoiceCaptureClient capt;
	VoicePlayerServer player;
	Thread cpTHR;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.audiotest);	
		
		ip = (EditText)findViewById(R.id.ipAddress);
		ip.setText(ipString);
		port = (EditText)findViewById(R.id.port);
		port.setText(Integer.toString(portNum));
		localIP = (EditText)findViewById(R.id.localIP);
		localIP.setText(getLocalIpAddress());
		localIP.setEnabled(false);
		saveButton = (ImageButton)findViewById(R.id.saveInfo);
		callButton = (ImageButton)findViewById(R.id.startCall);
		exitButton = (ImageButton)findViewById(R.id.exitButton);
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
				ipString = ip.getText().toString();
				portNum = Integer.parseInt(port.getText().toString());
			}
		});
		callButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				ipString = ip.getText().toString();
				portNum = Integer.parseInt(port.getText().toString());
				capt = new VoiceCaptureClient(ipString, portNum);
				cpTHR = new Thread(capt);
				cpTHR.start();
				player = new VoicePlayerServer(ipString, portNum);
			}
		});
		exitButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				capt.setStop();
				player.stopRunning();
				try {
					cpTHR.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
}
