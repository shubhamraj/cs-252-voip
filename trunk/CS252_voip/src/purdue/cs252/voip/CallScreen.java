package purdue.cs252.voip;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import purdue.cs252.voip.R;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.*;

public class CallScreen extends Activity{
	TextView contactName, banner;
	Button answerButton;
	Button endButton;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.phonecall);	
		if(MainActivity.initiatingCall == true){
			RingerClient ringerClient = new RingerClient();
			ringerClient.start(MainActivity.ipAddress, MainActivity.tempName);
		}
		contactName = (TextView)findViewById(R.id.contactName);
		endButton = (Button)findViewById(R.id.endButton);
		answerButton = (Button)findViewById(R.id.answerButton);
		banner = (TextView)findViewById(R.id.incomingCallBanner);
		if(MainActivity.initiatingCall == false){
		contactName.setText(MainActivity.callerName);
		//answerButton.setVisibility(View.VISIBLE);
		//endButton.setVisibility(View.VISIBLE);
		}
		else{
			contactName.setText(MainActivity.callee);
			banner.setText("Calling..");
			
			//answerButton.setVisibility(View.INVISIBLE);
			//endButton.setVisibility(View.INVISIBLE);
		}
		
		/*PrintWriter out = new PrintWriter(new BufferedWriter(
				new OutputStreamWriter(clientSocket.getOutputStream())), true);
		out.println(username);
		*/
	
		
		setOnClickListeners();
	}
	
	public void setOnClickListeners(){
		answerButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				try{
				PrintWriter out = new PrintWriter(new BufferedWriter(
						new OutputStreamWriter(MainActivity.callerSocket.getOutputStream())), true);
				out.println("Answer");
				}catch(Exception e){}
			//	MainActivity.startCall(RingerServer.callerIP);
			}
		});
		
		endButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				try{
				PrintWriter out = new PrintWriter(new BufferedWriter(
						new OutputStreamWriter(MainActivity.callerSocket.getOutputStream())), true);
				out.println("EndCall");
				}catch(Exception e){}
			//	MainActivity.endCall();
				
				finish();
			}
		});
	}
	
	

}
