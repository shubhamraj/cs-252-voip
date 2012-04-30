package purdue.cs252.voip;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class Chat extends Activity {
	Button btnSend;
	public static SharedPreferences settings;
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chatscreen);
		setOnClickListener();
	}
	
	public void setOnClickListener(){
		btnSend = (Button)findViewById(R.id.btnSend);
		
		btnSend.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try{
					String msg = getMessage();
					PrintWriter out = new PrintWriter(new BufferedWriter(
							new OutputStreamWriter(MainActivity.callerSocket.getOutputStream())), true);
					out.println(msg);
					TextView textOutput = (TextView)findViewById(R.id.textOutput);
					textOutput.append(settings.getString("userIdText", null) + ": " + msg);
					}catch(Exception e){}
				
			}
		});
	}
	
	public String getMessage(){
		EditText textInput = (EditText)findViewById(R.id.textInput);
		return textInput.getText().toString();
	}
	
	public void setText(String user, String msg){
		TextView textOutput = (TextView)findViewById(R.id.textOutput);
		textOutput.append(user + ": " + msg);
	}
}
