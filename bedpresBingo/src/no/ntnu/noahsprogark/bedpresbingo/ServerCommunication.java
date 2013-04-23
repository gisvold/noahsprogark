package no.ntnu.noahsprogark.bedpresbingo;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.StrictMode;
import android.util.Log;

public class ServerCommunication implements IServerCommunication {

	private final String PROTOCOL = "http://";
	private final String API_PATH = "/api/v1/board/";
	private final String JSON_FORMAT_URI = "?format=json";
	private URL requestURL;
	private URLConnection connection;
	private Scanner scanner;
	private String response;
	private JSONObject jsonResponse;
	private String[] words;

	ServerCommunication(int gameID, String host) {
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectNetwork().permitNetwork().build());

		try {
			requestURL = new URL(PROTOCOL + host + API_PATH + gameID + "/"
					+ JSON_FORMAT_URI);
			connection = requestURL.openConnection();
			InputStream is = connection.getInputStream();
			scanner = new Scanner(is);

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		StringBuilder sb = new StringBuilder();
		while (scanner.hasNext()) {
			sb.append(scanner.nextLine());
		}

		scanner.close();
		response = sb.toString();
	}

	@Override
	public String[] getWordsFromServer() {
		String terms = null;
		try {
			jsonResponse = new JSONObject(response);
			terms = jsonResponse.getString("terms");

		} catch (JSONException e) {
			e.printStackTrace();
		}

		if (terms == null)
			this.words = null;
		else
			this.words = terms.split(",");

		return this.words;
	}

	@Override
	public void joinGame() {
		// To change body of implemented methods use File | Settings | File
		// Templates.
	}

	@Override
	public String getSessionID() {
		return null; // To change body of implemented methods use File |
						// Settings | File Templates.
	}

	@Override
	public String[] getWordsForPlayer() {
		return new String[0]; // To change body of implemented methods use File
								// | Settings | File Templates.
	}

	@Override
	public Boolean registerWord() {
		return null; // To change body of implemented methods use File |
						// Settings | File Templates.
	}

	@Override
	public void getStatus() {
		// To change body of implemented methods use File | Settings | File
		// Templates.
	}
}
