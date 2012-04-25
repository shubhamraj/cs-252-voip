package purdue.cs252.voip;

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
		
		//endButton = (Button)findViewById(R.id.)
		
		//setOnClickListeners();
	}
	

}
