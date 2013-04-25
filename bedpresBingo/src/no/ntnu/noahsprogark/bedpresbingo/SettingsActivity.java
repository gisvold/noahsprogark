package no.ntnu.noahsprogark.bedpresbingo;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Simple settings screen that stores the name and host to the
 * {@link SharedPreferences}
 */
public class SettingsActivity extends Activity {

	TextView playerNameField;
	TextView hostNameField;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		SharedPreferences settings = getSharedPreferences("settings",
				MODE_PRIVATE);
		String playerName = settings.getString("playerName", "");
		String hostName = settings.getString("hostName", "127.0.0.1:8000");

		setContentView(R.layout.activity_settings);

		View okNameButton = findViewById(R.id.OKNameButton);
		playerNameField = (TextView) findViewById(R.id.PlayerNameField);
		playerNameField.setText(playerName);

		View okHostButton = findViewById(R.id.OKHostButton);
		hostNameField = (TextView) findViewById(R.id.EnterHostNameField);
		hostNameField.setText(hostName);

		okNameButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String playerName = playerNameField.getText().toString();
				SharedPreferences.Editor editor = getSharedPreferences(
						"settings", MODE_PRIVATE).edit();
				editor.putString("playerName", playerName);
				editor.commit();
				Toast t = Toast.makeText(getApplicationContext(),
						"Navn endret til " + playerName, Toast.LENGTH_SHORT);
				t.setGravity(Gravity.CENTER, 0, 0);
				t.show();
			}
		});

		okHostButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String hostName = hostNameField.getText().toString();
				SharedPreferences.Editor editor = getSharedPreferences(
						"settings", MODE_PRIVATE).edit();
				editor.putString("hostName", hostName);
				editor.commit();
				Toast t = Toast.makeText(getApplicationContext(),
						"Vertsnavn endret til " + hostName, Toast.LENGTH_SHORT);
				t.setGravity(Gravity.CENTER, 0, 0);
				t.show();
			}
		});
	}
}