package no.ntnu.noahsprogark.bedpresbingo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

public class GameActivity extends Activity implements
		BingoView.OnCellTouchListener {
	private BingoView view = null;

	private BingoType lastBingo = BingoType.NONE;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_screen);
		IServerCommunication s = new ServerCommunication(1);
		String[] words = s.getWordsFromServer();
		double rawDim = Math.sqrt(words.length);
		if ((int) rawDim != rawDim) {
			throw new IllegalArgumentException(
					"The array returned from the server was not square.");
		}
		int dim = (int) rawDim;

		view = BingoView.INSTANCE;
		view.setOnCellTouchListener(this);
		view.setWords(words);
		view.setDim(dim);
		view.buildBoard(this);

	}

	@Override
	public void onTouch(BingoCell c) {
		c.toggleSelected();
		BingoType currentBingo = view.hasBingo();
		if (currentBingo.getValue() > lastBingo.getValue()) {
			Toast t = Toast.makeText(getApplicationContext(),
					"Congratulations you got a " + currentBingo.name()
							+ " bingo!", Toast.LENGTH_SHORT);
			t.setGravity(Gravity.CENTER, 0, 0);
			t.show();
		}
		lastBingo = currentBingo;
		Log.d("DERP", view.hasBingo().name());
		view.invalidate();
	}
}
