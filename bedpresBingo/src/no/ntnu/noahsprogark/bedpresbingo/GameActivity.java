package no.ntnu.noahsprogark.bedpresbingo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class GameActivity extends Activity implements
		BingoView.OnCellTouchListener {
	private BingoView view = null;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_screen);
		IServerCommunication s = new TestWords();
		String[] words = s.getWordsFromServer();
		double rawDim = Math.sqrt(words.length);
		if ((int) rawDim != rawDim) {
			throw new IllegalArgumentException(
					"The array returned from the server was not square.");
		}
		int dim = (int) rawDim;

		view = BingoView.INSTANCE;
		Log.d("Derp", "View: " + view);
		Log.d("Derp", "ID: " + R.id.bingoview);
		Log.d("Derp", "Dim: " + dim);
		view.setOnCellTouchListener(this);
		view.setWords(words);
		view.setDim(dim);
		view.buildBoard();

	}

	@Override
	public void onTouch(BingoCell c) {
		Log.d("CellTouch", c.word);
	}
}
