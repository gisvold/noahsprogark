package no.ntnu.noahsprogark.bedpresbingo;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class Admin_Activity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_admin_, menu);
		return true;
	}

}
