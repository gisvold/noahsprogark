package no.ntnu.noahsprogark.bedpresbingo;

import static no.ntnu.noahsprogark.bedpresbingo.ServerCommunication.JSON_TAIL;
import static no.ntnu.noahsprogark.bedpresbingo.ServerCommunication.PROTOCOL;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;

public class GameStatusPoller implements Runnable {
	private String gURI;
	private String host;
	private boolean run;
	private int sleepTime;

	public GameStatusPoller(String gameURI, int sleepTime, String host) {
		this.gURI = gameURI;
		this.run = true;
		this.sleepTime = sleepTime;
		this.host = host;
	}

	@Override
	public void run() {
		while (run) {
			Scanner s = null;
			try {
				s = new Scanner(new URL(PROTOCOL + host + gURI + "?"
						+ JSON_TAIL).openConnection().getInputStream());
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (s == null)
				continue;

			StringBuilder sb = new StringBuilder();
			while (s.hasNext()) {
				sb.append(s.nextLine());
			}

			s.close();
			String res = sb.toString();

			try {
				JSONObject obj = new JSONObject(res);
				JSONObject leader = obj.optJSONObject("bingo_leader");
				if (leader == null) {
					BingoView.updateLeader("", 0);
				} else {
					BingoView.updateLeader(leader.getString("name"),
							obj.getInt("bingo_value"));
				}
				Thread.sleep(sleepTime);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
