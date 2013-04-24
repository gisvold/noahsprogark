package no.ntnu.noahsprogark.bedpresbingo;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.StrictMode;
import android.view.Gravity;
import android.widget.Toast;

public class ServerCommunication {
	private final String PROTOCOL = "http://";
	private final String API_PATH = "/api/v1/";
	private final String BOARD = "board/";
	private final String PLAYER = "player/";
	private final String GAME = "game/";
	private final String PLAYER_NAME_QUERY = "?player__name=";
	private final String ACTIVE = "&active=true";
	private final String NEWEST = "?order_by=-id&limit=1";
	private final String JSON_TAIL = "&format=json";
	private String host;
	private String[] words;
	private String goldenWord;
	private String playerName;

	ServerCommunication(String playerName, String host) {
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectNetwork().permitNetwork().build());
		this.host = host;
		this.playerName = playerName;
		words = null;
		goldenWord = null;
	}

	public void getBoardFromServer(GameActivity a) {
		if (playerName.equals("")) {
			Toast t = Toast.makeText(a.getApplicationContext(),
					"Feil: Du må skrive inn et navn i innstillingene!",
					Toast.LENGTH_LONG);
			t.setGravity(Gravity.CENTER, 0, 0);
			t.show();
			return;
		}

		String response = getNewBoardFromServer(a);

		if (response == null)
			return;

		String terms = null;
		goldenWord = null;
		JSONObject jsonResponse = null;
		try {
			jsonResponse = new JSONObject(response);
			if (jsonResponse.has("objects")) {
				jsonResponse = jsonResponse.getJSONArray("objects")
						.getJSONObject(0);
			}
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

	private String getNewBoardFromServer(GameActivity a) {
		Scanner scanner = null;
		try {
			scanner = new Scanner(new URL(PROTOCOL + host + API_PATH + BOARD
					+ PLAYER_NAME_QUERY + playerName + ACTIVE + JSON_TAIL)
					.openConnection().getInputStream());
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
			return null;

		StringBuilder sb = new StringBuilder();
		while (scanner.hasNext()) {
			sb.append(scanner.nextLine());
		}

		scanner.close();
		String res = sb.toString();

		int retSize = 0;
		try {
			retSize = new JSONObject(res).getJSONArray("objects").length();
		} catch (JSONException e) {
			Toast t = Toast.makeText(a.getApplicationContext(),
					"Det oppsto en feil ved tolkning av data fra serveren. Feilmelding: "
							+ e.getMessage(), Toast.LENGTH_LONG);
			t.setGravity(Gravity.CENTER, 0, 0);
			t.show();
			return null;
		}

		if (retSize > 0)
			return res;
		else
			return createBoardOnServer(a);
	}

	private String createBoardOnServer(GameActivity a) {
		String playerURI = getPlayerURIFromServer(a);
		String gameURI = getGameURIFromServer(a);

		if (playerURI == null || gameURI == null)
			return null;

		JSONObject newBoard = new JSONObject();
		Scanner scanner = null;

		try {
			newBoard.put("active", true);
			newBoard.put("game", gameURI);
			newBoard.put("player", playerURI);

			HttpURLConnection huc = (HttpURLConnection) new URL(PROTOCOL + host
					+ API_PATH + BOARD).openConnection();

			huc.setRequestMethod("POST");
			huc.setRequestProperty("Content-Type", "application/json");
			huc.setRequestProperty("Content-Length",
					Integer.toString(newBoard.toString().getBytes().length));

			huc.setUseCaches(false);
			huc.setDoInput(true);
			huc.setDoOutput(true);

			DataOutputStream dos = new DataOutputStream(huc.getOutputStream());
			dos.writeBytes(newBoard.toString());
			dos.flush();
			dos.close();

			scanner = new Scanner(huc.getInputStream());
		} catch (JSONException e) {
			// Ignore, should never fail
		} catch (MalformedURLException e) {
			// Ignore, caught earlier
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}

		if (scanner == null)
			return null;

		StringBuilder sb = new StringBuilder();
		while (scanner.hasNext()) {
			sb.append(scanner.nextLine());
		}

		return sb.toString();
	}

	private String getGameURIFromServer(GameActivity a) {
		String req = PROTOCOL + host + API_PATH + GAME + NEWEST + JSON_TAIL;
		Scanner scanner = null;
		try {
			scanner = new Scanner(new URL(req).openConnection()
					.getInputStream());
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
			return null;

		StringBuilder sb = new StringBuilder();
		while (scanner.hasNext()) {
			sb.append(scanner.nextLine());
		}

		scanner.close();
		String res = sb.toString();

		String retURI = null;

		try {
			retURI = new JSONObject(res).getJSONArray("objects")
					.getJSONObject(0).getString("resource_uri");
		} catch (JSONException e) {
			Toast t = Toast.makeText(a.getApplicationContext(),
					"Det oppsto en feil ved tolkning av data fra serveren. Feilmelding: "
							+ e.getMessage(), Toast.LENGTH_LONG);
			t.setGravity(Gravity.CENTER, 0, 0);
			t.show();
			return null;
		}

		return retURI;
	}

	private String getPlayerURIFromServer(GameActivity a) {
		String req = PROTOCOL + host + API_PATH + PLAYER + "?name="
				+ playerName + JSON_TAIL;
		Scanner scanner = null;
		try {
			scanner = new Scanner(new URL(req).openConnection()
					.getInputStream());
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
			return null;

		StringBuilder sb = new StringBuilder();
		while (scanner.hasNext()) {
			sb.append(scanner.nextLine());
		}

		scanner.close();
		String res = sb.toString();

		String retURI = null;
		JSONArray objs = null;
		try {
			objs = new JSONObject(res).getJSONArray("objects");
			if (objs.length() > 0) {
				retURI = objs.getJSONObject(0).getString("resource_uri");
			}
		} catch (JSONException e) {
			Toast t = Toast.makeText(a.getApplicationContext(),
					"Det oppsto en feil ved tolkning av data fra serveren. Feilmelding: "
							+ e.getMessage(), Toast.LENGTH_LONG);
			t.setGravity(Gravity.CENTER, 0, 0);
			t.show();
			return null;
		}

		if (retURI != null)
			return retURI;

		JSONObject newPlayer = new JSONObject();
		scanner = null;

		try {
			newPlayer.put("name", playerName);
			HttpURLConnection huc = (HttpURLConnection) new URL(PROTOCOL + host
					+ API_PATH + PLAYER).openConnection();

			huc.setRequestMethod("POST");
			huc.setRequestProperty("Content-Type", "application/json");
			huc.setRequestProperty("Content-Length",
					Integer.toString(newPlayer.toString().getBytes().length));

			huc.setUseCaches(false);
			huc.setDoInput(true);
			huc.setDoOutput(true);

			DataOutputStream dos = new DataOutputStream(huc.getOutputStream());
			dos.writeBytes(newPlayer.toString());
			dos.flush();
			dos.close();

			scanner = new Scanner(huc.getInputStream());
		} catch (JSONException e) {
			// Ignore, should never fail
		} catch (MalformedURLException e) {
			// Ignore, caught earlier
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}

		if (scanner == null)
			return null;

		sb = new StringBuilder();
		while (scanner.hasNext()) {
			sb.append(scanner.nextLine());
		}

		try {
			JSONObject jso = new JSONObject(sb.toString());
			retURI = jso.getString("resource_uri");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return retURI;
	}

	public String[] getWords() {
		return words;
	}

	public String getGoldenWord() {
		return goldenWord;
	}
}
