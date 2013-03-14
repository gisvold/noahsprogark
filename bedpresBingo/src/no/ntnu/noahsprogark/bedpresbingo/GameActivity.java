package no.ntnu.noahsprogark.bedpresbingo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class GameActivity extends Activity {
	private String[] words;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		IServerCommunication s = new TestWords();
		this.words = s.getWordsFromServer();
		double rawDim = Math.sqrt(words.length);
		if ((int) rawDim != rawDim) {
			throw new IllegalArgumentException(
					"The array returned from the server was not square.");
		}
		int dim = (int) rawDim;
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				String word = words[i + j];
			}
		}
		switch (dim) {
		case 3:
			setContentView(R.layout.game_layout_3x3);
			break;
		case 4:
			setContentView(R.layout.game_layout_4x4);
			break;
		case 5:
			setContentView(R.layout.game_layout_5x5);
			break;
		default:
			setContentView(R.layout.game_layout_err);
			break;
		}
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
