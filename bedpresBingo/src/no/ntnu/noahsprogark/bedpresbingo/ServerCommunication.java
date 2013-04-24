package no.ntnu.noahsprogark.bedpresbingo;

import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.StrictMode;
import android.view.Gravity;
import android.widget.Toast;

public class ServerCommunication {
	private final String PROTOCOL = "http://";
	private final String API_PATH = "/api/v1/board/";
	private final String JSON_FORMAT_URI = "?format=json";
	private String host;
	private int gameID;
	private String[] words;
	private String goldenWord;

	ServerCommunication(int gameID, String host) {
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectNetwork().permitNetwork().build());
		this.host = host;
		this.gameID = gameID;
		words = null;
		goldenWord = null;
	}

	public void getBoardFromServer(GameActivity a) {
		Scanner scanner = null;
		try {
			scanner = new Scanner(new URL(PROTOCOL + host + API_PATH + gameID
					+ "/" + JSON_FORMAT_URI).openConnection().getInputStream());
		} catch (UnknownHostException e) {
			Toast t = Toast.makeText(a.getApplicationContext(),
					"Adressen du har lagret til serveren, " + host
							+ ", er ikke et gyldig vertsnavn. Feilmelding: "
							+ e.getMessage(), Toast.LENGTH_LONG);
			t.setGravity(Gravity.CENTER, 0, 0);
			t.show();
		} catch (IOException e) {
			Toast t = Toast.makeText(
					a.getApplicationContext(),
					"Kunne ikke hente data fra serveren. Feilmelding: "
							+ e.getMessage(), Toast.LENGTH_LONG);
			t.setGravity(Gravity.CENTER, 0, 0);
			t.show();
		}
		if (scanner == null)
			return;

		StringBuilder sb = new StringBuilder();
		while (scanner.hasNext()) {
			sb.append(scanner.nextLine());
		}

		scanner.close();
		String response = sb.toString();

		String terms = null;
		goldenWord = null;
		JSONObject jsonResponse = null;
		try {
			jsonResponse = new JSONObject(response);
			terms = jsonResponse.getString("terms");
			goldenWord = jsonResponse.getJSONObject("game")
					.getJSONObject("golden_word").getString("term");

		} catch (JSONException e) {
			Toast t = Toast.makeText(a.getApplicationContext(),
					"Det oppsto en feil ved tolkning av data fra serveren. Feilmelding: "
							+ e.getMessage(), Toast.LENGTH_LONG);
			t.setGravity(Gravity.CENTER, 0, 0);
			t.show();
		}

		if (terms != null)
			this.words = terms.split(",");

	}

	public String[] getWords() {
		return words;
	}

	public String getGoldenWord() {
		return goldenWord;
	}
}
