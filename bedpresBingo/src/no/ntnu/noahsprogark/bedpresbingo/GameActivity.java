package no.ntnu.noahsprogark.bedpresbingo;

import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONException;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

public class GameActivity extends Activity implements
		BingoView.OnCellTouchListener {
	private BingoView view = null;
	public static String pName = "";

	private BingoType lastBingo = BingoType.NONE;
	private ServerCommunication s;

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
			Toast t = Toast.makeText(getApplicationContext(),
					"Du må skrive inn et navn i innstillingsskjermen!",
					Toast.LENGTH_LONG);
			t.setGravity(Gravity.CENTER, 0, 0);
			t.show();
		} catch (MalformedURLException e) {
			Toast t = Toast.makeText(
					getApplicationContext(),
					"Vertsnavnet i innstillingsskjermen er ugyldig: "
							+ e.getMessage(), Toast.LENGTH_LONG);
			t.setGravity(Gravity.CENTER, 0, 0);
			t.show();
			Log.d("DERP", "getBoardErr: " + e.getMessage());
		} catch (IOException e) {
			Toast t = Toast.makeText(
					getApplicationContext(),
					"Det oppsto en feil ved kommunisering med tjener: "
							+ e.getMessage(), Toast.LENGTH_LONG);
			t.setGravity(Gravity.CENTER, 0, 0);
			t.show();
		} catch (JSONException e) {
			Toast t = Toast.makeText(getApplicationContext(),
					"Det oppsto en feil ved tolkning av data fra tjeneren: "
							+ e.getMessage(), Toast.LENGTH_LONG);
			t.setGravity(Gravity.CENTER, 0, 0);
			t.show();
		}
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
				view.setParentActivity(this);
			}
		}
	}

	public void updateBingo(BingoType bt) {
		try {
			s.updateBingo(bt);
		} catch (MalformedURLException e) {
			Toast t = Toast.makeText(
					getApplicationContext(),
					"Vertsnavnet i innstillingsskjermen er ugyldig: "
							+ e.getMessage(), Toast.LENGTH_LONG);
			t.setGravity(Gravity.CENTER, 0, 0);
			t.show();
			Log.d("DERP", Log.getStackTraceString(e));
		} catch (IOException e) {
			Toast t = Toast.makeText(
					getApplicationContext(),
					"Det oppsto en feil ved kommunisering med tjener: "
							+ e.getMessage(), Toast.LENGTH_LONG);
			t.setGravity(Gravity.CENTER, 0, 0);
			t.show();
		} catch (JSONException e) {
			Toast t = Toast.makeText(getApplicationContext(),
					"Det oppsto en feil ved tolkning av data fra tjeneren: "
							+ e.getMessage(), Toast.LENGTH_LONG);
			t.setGravity(Gravity.CENTER, 0, 0);
			t.show();
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

	public void displayToast(String msg) {
		Toast t = Toast.makeText(getApplicationContext(), msg,
				Toast.LENGTH_LONG);
		t.setGravity(Gravity.CENTER, 0, 0);
		t.show();

	}
}
