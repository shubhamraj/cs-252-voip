package purdue.cs252.voip;

import purdue.cs252.voip.R;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.*;

public class UserOptions extends Activity{
	EditText userId, ip, port;
	Button saveButton, clearButton, backButton;
	String userIdText, ipText;
	int portText;
	SharedPreferences settings;
	SharedPreferences.Editor editor;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);	
		settings = PreferenceManager.getDefaultSharedPreferences(this);
		editor = settings.edit();
		userId = (EditText)findViewById(R.id.userId);
		ip = (EditText)findViewById(R.id.ip);
		port = (EditText)findViewById(R.id.port);
		if(getPort() == 0){
			port.setText("");
		}
		else{
			port.setText(Integer.toString(getPort()));
		}
		ip.setText(getIp());
		userId.setText(getUserId());
		saveButton = (Button)findViewById(R.id.saveButton);
		clearButton = (Button)findViewById(R.id.clearButton);
		backButton = (Button)findViewById(R.id.backButton);
		
		setOnClickListeners();
	}
	
	
	
	public void onResume(){
		super.onResume();
		userId.setText(getUserId());
		ip.setText(getIp());
		if(getPort() == 0){
			port.setText("");
		}
		else{
			port.setText(Integer.toString(getPort()));
		}
	}
	
	public String getUserId(){
		return settings.getString("userIdText", userIdText);
	}
	
	public String getIp(){
		return settings.getString("ipText", ipText);
	}
	
	public int getPort(){
		return settings.getInt("portText", portText);
	}
	
	public void setOnClickListeners(){
		saveButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				userIdText = userId.getText().toString();
				ipText = ip.getText().toString();
				if(getPort() == 0){
				}
				else{
				portText = Integer.parseInt(port.getText().toString());
				editor.putInt("portText", portText);
				}
				editor.putString("userIdText", userIdText);
				editor.putString("ipText", ipText);
				editor.commit();
				
			}
		});
		
		backButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		
		clearButton.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				userId.setText("");
				ip.setText("");
				port.setText("");
				editor.putString("userIdText", userIdText);
				editor.putString("ipText", ipText);
				editor.putInt("portText", 0);
				editor.commit();
				
			}
			
		});
	}
}
