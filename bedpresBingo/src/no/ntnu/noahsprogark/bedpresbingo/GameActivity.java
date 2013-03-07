package no.ntnu.noahsprogark.bedpresbingo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class GameActivity extends Activity {
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_layout);
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
