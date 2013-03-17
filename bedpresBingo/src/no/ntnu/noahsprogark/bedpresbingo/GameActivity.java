package no.ntnu.noahsprogark.bedpresbingo;

import android.app.Activity;
import android.os.Bundle;

public class GameActivity extends Activity {
	private String[] words;
	private BingoView view = null;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_screen);
		IServerCommunication s = new TestWords();
		this.words = s.getWordsFromServer();
		double rawDim = Math.sqrt(words.length);
		if ((int) rawDim != rawDim) {
			throw new IllegalArgumentException(
					"The array returned from the server was not square.");
		}
		int dim = (int) rawDim;

		view = (BingoView) findViewById(R.id.bingoview);
		System.out.println(R.id.bingoview);
		System.out.println(view);
//		view.buildBoard(words, dim);

	}
}
