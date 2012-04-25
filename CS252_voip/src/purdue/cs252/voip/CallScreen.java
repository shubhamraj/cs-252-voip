package purdue.cs252.voip;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import purdue.cs252.voip.R;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.*;

public class CallScreen extends Activity{
	TextView contactName;
	Button answerButton;
	Button endButton;
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.phonecall);	
		contactName = (TextView)findViewById(R.id.contactName);
		contactName.setText(MainActivity.callerName);
		
		/*PrintWriter out = new PrintWriter(new BufferedWriter(
				new OutputStreamWriter(clientSocket.getOutputStream())), true);
		out.println(username);
		*/
		
		endButton = (Button)findViewById(R.id.endButton);
		answerButton = (Button)findViewById(R.id.answerButton);
		
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
			}
		});
		
		endButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				try{
				PrintWriter out = new PrintWriter(new BufferedWriter(
						new OutputStreamWriter(MainActivity.callerSocket.getOutputStream())), true);
				out.println("EndCall");
				}catch(Exception e){}
			}
		});
	}
	
	

}
