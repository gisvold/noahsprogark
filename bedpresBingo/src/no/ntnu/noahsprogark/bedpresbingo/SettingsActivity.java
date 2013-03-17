package no.ntnu.noahsprogark.bedpresbingo;


import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class SettingsActivity extends Activity{
	
	private AppPreferences _appPrefs;
	
	EditText playerNameField;
	Button OKButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		_appPrefs = new AppPreferences(getApplicationContext());
		setContentView(R.layout.activity_settings);
		initiateLayout();
		playerNameField.setText(_appPrefs.getPlayerName());
		findViewById(R.id.OKButton).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				_appPrefs.savePlayerName(playerNameField.getText().toString());
			}
		});
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_settings_, menu);
		return true;
	}


	private void initiateLayout() {
		OKButton = (Button)findViewById(R.id.OKButton);
		playerNameField = (EditText)findViewById(R.id.PlayerNameField);
	}
	
}
