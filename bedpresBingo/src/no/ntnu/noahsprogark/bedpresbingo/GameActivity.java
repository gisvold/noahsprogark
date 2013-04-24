package no.ntnu.noahsprogark.bedpresbingo;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;

public class GameActivity extends Activity implements
		BingoView.OnCellTouchListener {
	private BingoView view = null;
	public static String pName = "";

	private BingoType lastBingo = BingoType.NONE;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_screen);
		SharedPreferences settings = getSharedPreferences("settings",
				MODE_PRIVATE);
		String host = settings.getString("hostName", "127.0.0.1:8000");
		pName = settings.getString("playerName", "");
		ServerCommunication s = new ServerCommunication(pName, host);
		s.getBoardFromServer(this);
		String[] words = s.getWords();
		String goldenWord = s.getGoldenWord();
		if (words != null) {
			double rawDim = Math.sqrt(words.length);

			if ((int) rawDim != rawDim) {
				Toast t = Toast.makeText(getApplicationContext(),
						"En feil oppsto ved henting av spillebrett",
						Toast.LENGTH_SHORT);
				t.setGravity(Gravity.CENTER, 0, 0);
				t.show();
			} else {
				int dim = (int) rawDim;

				view = BingoView.INSTANCE;
				view.setOnCellTouchListener(this);
				view.setWords(words);
				view.setGoldenWord(goldenWord);
				view.setDim(dim);
				view.buildBoard(this);
			}
		}
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
		view.invalidate();
	}
}
