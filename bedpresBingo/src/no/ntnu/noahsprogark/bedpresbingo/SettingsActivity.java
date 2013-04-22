package no.ntnu.noahsprogark.bedpresbingo;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends Activity {

	TextView playerNameField;
	Button okButton;
	private static String playerName = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		SharedPreferences settings = getPreferences(MODE_PRIVATE);
		playerName = settings.getString("playerName", "");

		setContentView(R.layout.activity_settings);
		okButton = (Button) findViewById(R.id.OKButton);
		playerNameField = (TextView) findViewById(R.id.PlayerNameField);
		playerNameField.setText(playerName);
		okButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				playerName = playerNameField.getText().toString();
				SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE)
						.edit();
				editor.putString("playerName", playerName);
				editor.commit();
				Toast t = Toast.makeText(getApplicationContext(), "Navn endret til " + playerName, Toast.LENGTH_SHORT);
				t.show();
				Log.d("DERP", playerName);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_settings_, menu);
		return true;
	}
	
	public static String getName() {
		return playerName.intern();
	}
}