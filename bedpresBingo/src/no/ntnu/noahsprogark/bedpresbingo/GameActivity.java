package no.ntnu.noahsprogark.bedpresbingo;

import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONException;

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
	private ServerCommunication s;

	/**
	 * Sets up the BingoBoard by getting words from the server based on the
	 * hostName and playerName stored in the settings.
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_screen);
		SharedPreferences settings = getSharedPreferences("settings",
				MODE_PRIVATE);
		String host = settings.getString("hostName", "127.0.0.1:8000");
		pName = settings.getString("playerName", "");
		s = new ServerCommunication(pName, host);
		try {
			s.getBoardFromServer();
		} catch (IllegalArgumentException e) {
			displayToast("Du må skrive inn et navn i innstillingsskjermen!");
		} catch (MalformedURLException e) {
			displayToast("Vertsnavnet i innstillingsskjermen er ugyldig: "
					+ e.getMessage());
		} catch (IOException e) {
			displayToast("Det oppsto en feil ved kommunisering med tjener: "
					+ e.getMessage());
		} catch (JSONException e) {
			displayToast("Det oppsto en feil ved tolkning av data fra tjeneren: "
					+ e.getMessage());
		}
		String[] words = s.getWords();
		String goldenWord = s.getGoldenWord();
		if (words != null) {
			double rawDim = Math.sqrt(words.length);

			if ((int) rawDim != rawDim) {
				displayToast("En feil oppsto ved henting av spillebrett");
			} else {
				int dim = (int) rawDim;

				view = BingoView.INSTANCE;
				view.setOnCellTouchListener(this);
				view.setWords(words);
				view.setGoldenWord(goldenWord);
				view.setDim(dim);
				view.setupMargins(getWindowManager().getDefaultDisplay());
				view.setParentActivity(this);
			}
		}
	}

	/**
	 * Calls the BingoView.resetBingoLeader() method whenever the application is
	 * brought back from the background.
	 */
	public void onResume() {
		super.onResume();
		view.resetBingoLeader();
	}

	/**
	 * Tries to call the updateBingo method in {@link ServerCommunication} when
	 * the local bingo is higher than the one on the server.
	 *
	 * @param bt
	 *            The BingoType to update to.
	 */
	public void updateBingo(BingoType bt) {
		try {
			s.updateBingo(bt);
		} catch (MalformedURLException e) {
			displayToast("Vertsnavnet i innstillingsskjermen er ugyldig: "
					+ e.getMessage());
		} catch (IOException e) {
			displayToast("Det oppsto en feil ved kommunisering med tjener: "
					+ e.getMessage());
		} catch (JSONException e) {
			displayToast("Det oppsto en feil ved tolkning av data fra tjeneren: "
					+ e.getMessage());
		}
	}

	/**
	 * Displays a toast message if the user's bingo "level" has increased since
	 * the last time. Also toggles the touched {@link BingoCell} and redraws the
	 * view.
	 */
	public void onTouch(BingoCell c) {
		c.toggleSelected();
		BingoType currentBingo = view.hasBingo();
		if (currentBingo.getValue() > lastBingo.getValue()) {
			displayToast("Gratulerer, du fikk en " + currentBingo.name()
					+ " bingo!");
		}
		lastBingo = currentBingo;
		view.invalidate();
	}

	/**
	 * Utility method to display a toast message on the game screen.
	 *
	 * @param msg
	 *            The message to display
	 */
	public void displayToast(final String msg) {
		runOnUiThread(new Runnable() {
			public void run() {
				Toast t = Toast.makeText(GameActivity.this, msg,
						Toast.LENGTH_LONG);
				t.setGravity(Gravity.CENTER, 0, 0);
				t.show();
			}
		});
	}
}
