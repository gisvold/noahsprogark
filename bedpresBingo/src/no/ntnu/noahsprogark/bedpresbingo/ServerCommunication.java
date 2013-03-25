package no.ntnu.noahsprogark.bedpresbingo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Scanner;

public class ServerCommunication implements IServerCommunication {

    private final String SERVER_ROOT_URI = "http://78.91.83.248:8000/api/game/";
    private final String JSON_FORMAT_URI = "?format=json";
    private URL requestURL;
    private URLConnection connection;
    private Scanner scanner;
    private String response;
    private JSONObject jsonResponse;
    private ArrayList<JSONObject> terms;
    private ArrayList<String> words;

    ServerCommunication(int gameID) {

        terms = new ArrayList<JSONObject>();
        words = new ArrayList<String>();

        try {
            requestURL = new URL(SERVER_ROOT_URI + gameID + JSON_FORMAT_URI);
            connection = requestURL.openConnection();
            scanner = new Scanner(requestURL.openStream());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        connection.setDoOutput(true);

        response = scanner.useDelimiter("\\Z").next();
        //words.add(getWordsFromServer());
        scanner.close();

    }

    @Override
    public String[] getWordsFromServer() {

        try {
            jsonResponse = new JSONObject(response);
            terms.add(jsonResponse.getJSONObject("terms"));

            for (JSONObject term : terms) {
                words.add((terms.getClass().getField("term").toString()));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        return words.toArray(new String[words.size()]);
    }

    @Override
    public void joinGame() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getSessionID() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String[] getWordsForPlayer() {
        return new String[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Boolean registerWord() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void getStatus() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}


