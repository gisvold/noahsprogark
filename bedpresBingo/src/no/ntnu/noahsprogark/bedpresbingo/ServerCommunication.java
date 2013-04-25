package no.ntnu.noahsprogark.bedpresbingo;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.StrictMode;

public class ServerCommunication {
	public static final String PROTOCOL = "http://";
	public static final String API_PATH = "/api/v1/";
	public static final String BOARD = "board/";
	public static final String PLAYER = "player/";
	public static final String GAME = "game/";
	public static final String PLAYER_NAME_QUERY = "player__name=";
	public static final String NAME_QUERY = "name=";
	public static final String ACTIVE = "active=true";
	public static final String NEWEST = "order_by=-id&limit=1";
	public static final String JSON_TAIL = "format=json";
	private String host;
	private String[] words;
	private String goldenWord;
	private String playerName;
	private String playerURI;
	private String gameURI;
	private Thread pollerThread;

	/**
	 * Creates the {@link ServerCommunication} object.
	 *
	 * @param playerName
	 *            The name of the current player
	 * @param host
	 *            The host name of the server
	 */
	public ServerCommunication(String playerName, String host) {
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectNetwork().permitNetwork().build());
		this.host = host;
		this.playerName = playerName;
		words = null;
		goldenWord = null;
	}

	/**
	 * Gets a playing board, containing the assigned words and golden word, from
	 * the server. Also initializes the polling thread that runs the
	 * {@link GameStatusPoller}. Running this method sets all the relevant
	 * fields of the class.
	 *
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws JSONException
	 */
	public void getBoardFromServer() throws MalformedURLException, IOException,
			JSONException {
		if (playerName.equals("")) {
			throw new IllegalArgumentException(
					"Du må skrive inn et navn i innstillingene!");
		}

		String response = getNewBoardFromServer();

		if (response == null)
			return;

		String terms = null;
		goldenWord = null;
		JSONObject jsonResponse = new JSONObject(response);
		if (jsonResponse.has("objects")) {
			jsonResponse = jsonResponse.getJSONArray("objects")
					.getJSONObject(0);
		}
		terms = jsonResponse.getString("terms");
		playerURI = jsonResponse.getString("player");
		JSONObject game = jsonResponse.getJSONObject("game");
		goldenWord = game.getJSONObject("golden_word").getString("term");
		gameURI = game.getString("resource_uri");

		if (terms != null)
			this.words = terms.split(",");

		pollerThread = new Thread(new GameStatusPoller(gameURI, 3000, host));
		pollerThread.start();

	}

	/**
	 * @return The player's current board if it exists; calls a method that
	 *         creates one if it doesn't.
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws JSONException
	 */
	private String getNewBoardFromServer() throws MalformedURLException,
			IOException, JSONException {
		Scanner scanner = null;
		scanner = new Scanner(new URL(PROTOCOL + host + API_PATH + BOARD + "?"
				+ PLAYER_NAME_QUERY + playerName + "&" + ACTIVE + "&"
				+ JSON_TAIL).openConnection().getInputStream());

		StringBuilder sb = new StringBuilder();
		while (scanner.hasNext()) {
			sb.append(scanner.nextLine());
		}

		scanner.close();
		String res = sb.toString();

		int retSize = 0;
		retSize = new JSONObject(res).getJSONArray("objects").length();

		if (retSize > 0)
			return res;
		else
			return createBoardOnServer();
	}

	/**
	 * @return a new game board based on the player name and the newest game on
	 *         the server.
	 * @throws JSONException
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	private String createBoardOnServer() throws JSONException,
			MalformedURLException, IOException {
		String playerURI = getPlayerURIFromServer();
		String gameURI = getGameURIFromServer();

		if (playerURI == null || gameURI == null)
			return null;

		JSONObject newBoard = new JSONObject();
		Scanner scanner = null;

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

		StringBuilder sb = new StringBuilder();
		while (scanner.hasNext()) {
			sb.append(scanner.nextLine());
		}

		return sb.toString();
	}

	/**
	 * @return the URI for the newest game on the server.
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws JSONException
	 */
	private String getGameURIFromServer() throws MalformedURLException,
			IOException, JSONException {
		String req = PROTOCOL + host + API_PATH + GAME + "?" + NEWEST + "&"
				+ JSON_TAIL;
		Scanner scanner = new Scanner(new URL(req).openConnection()
				.getInputStream());
		StringBuilder sb = new StringBuilder();
		while (scanner.hasNext()) {
			sb.append(scanner.nextLine());
		}

		scanner.close();
		String res = sb.toString();

		String retURI = new JSONObject(res).getJSONArray("objects")
				.getJSONObject(0).getString("resource_uri");

		return retURI;
	}

	/**
	 * @return the URI for the player name provided when the object was
	 *         constructed. If this player doesn't exist on the server, it is
	 *         created first.
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws JSONException
	 */
	private String getPlayerURIFromServer() throws MalformedURLException,
			IOException, JSONException {
		String req = PROTOCOL + host + API_PATH + PLAYER + "?" + NAME_QUERY
				+ playerName + "&" + JSON_TAIL;

		Scanner scanner = new Scanner(new URL(req).openConnection()
				.getInputStream());

		StringBuilder sb = new StringBuilder();
		while (scanner.hasNext()) {
			sb.append(scanner.nextLine());
		}

		scanner.close();
		String res = sb.toString();

		String retURI = null;
		JSONArray objs = new JSONObject(res).getJSONArray("objects");
		if (objs.length() > 0) {
			retURI = objs.getJSONObject(0).getString("resource_uri");
		}

		if (retURI != null)
			return retURI;

		JSONObject newPlayer = new JSONObject();
		scanner = null;

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

		sb = new StringBuilder();
		while (scanner.hasNext()) {
			sb.append(scanner.nextLine());
		}

		JSONObject jso = new JSONObject(sb.toString());
		retURI = jso.getString("resource_uri");

		return retURI;
	}

	/**
	 * @return The words for the bingo board.
	 */
	public String[] getWords() {
		return words;
	}

	/**
	 * @return The golden word for the bingo game.
	 */
	public String getGoldenWord() {
		return goldenWord;
	}

	/**
	 * Updates the server with the player as the new bingo leader.
	 * @param bt The new max bingo score.
	 * @throws JSONException
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public void updateBingo(BingoType bt) throws JSONException,
			MalformedURLException, IOException {
		String req = PROTOCOL + host + gameURI;

		JSONObject updateScore = new JSONObject();
		updateScore.put("bingo_leader", playerURI);
		updateScore.put("bingo_value", bt.getValue());
		HttpURLConnection huc = (HttpURLConnection) new URL(req)
				.openConnection();

		huc.setRequestMethod("PUT");
		huc.setRequestProperty("Content-Type", "application/json");
		huc.setRequestProperty("Content-Length",
				Integer.toString(updateScore.toString().getBytes().length));

		huc.setUseCaches(false);
		huc.setDoInput(false);
		huc.setDoOutput(true);

		DataOutputStream dos = new DataOutputStream(huc.getOutputStream());
		dos.writeBytes(updateScore.toString());
		dos.flush();
		dos.close();

		huc.getResponseCode();
	}
}