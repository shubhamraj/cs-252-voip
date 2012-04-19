package CS252.lab6.VOIP;

import CS252.lab6.VOIP.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class UserOptions extends Activity{
	EditText userId;
	EditText ip;
	Button saveButton;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		
		userId = (EditText)findViewById(R.id.userId);
		ip = (EditText)findViewById(R.id.ip);
		saveButton = (Button)findViewById(R.id.saveButton);
		
		saveButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String userIdText = userId.getText().toString();
				String ipInput = ip.getText().toString();
			}
		});
		}
	
}
