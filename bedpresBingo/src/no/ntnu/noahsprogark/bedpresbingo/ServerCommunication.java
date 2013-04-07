package no.ntnu.noahsprogark.bedpresbingo;

import android.os.StrictMode;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Scanner;

public class ServerCommunication implements IServerCommunication {

    private final String SERVER_ROOT_URI = "http://78.91.82.168:8000/api/game/";
    private final String JSON_FORMAT_URI = "?format=json";
    private URL requestURL;
    private URLConnection connection;
    private Scanner scanner;
    private String response;
    private JSONObject jsonResponse;
    private JSONArray terms;
    private ArrayList<String> words;

    ServerCommunication(int gameID) {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectNetwork().permitNetwork().build());

        words = new ArrayList<String>();

        try {
            requestURL = new URL(SERVER_ROOT_URI + gameID + "/"
                    + JSON_FORMAT_URI);
            connection = requestURL.openConnection();
            InputStream is = connection.getInputStream();
            scanner = new Scanner(is);

        } catch (MalformedURLException e) {
            Log.d("DERP", "MALFORMED" + e.toString());
            e.printStackTrace();
        } catch (IOException e) {
            Log.d("DERP", "IOEx" + e.toString());
            e.printStackTrace();
        } catch (Exception e) {
            Log.d("DERP", "Ex" + e.toString());
        }

        response = scanner.useDelimiter("\\Z").next();
        scanner.close();
    }

    @Override
    public String[] getWordsFromServer() {

        try {
            jsonResponse = new JSONObject(response);
            terms = jsonResponse.getJSONArray("terms");

            for (int i = 0; i < terms.length(); i++) {
                words.add(terms.getJSONObject(i).getJSONObject("term")
                        .getString("term"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return words.toArray(new String[words.size()]);
    }

    @Override
    public void joinGame() {

    }

    @Override
    public String getSessionID() {
        return null;
    }

    @Override
    public String[] getWordsForPlayer() {
        return new String[0];
    }

    @Override
    public Boolean registerWord() {
        return null;
    }

    @Override
    public void getStatus() {

    }
}
