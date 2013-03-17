package no.ntnu.noahsprogark.bedpresbingo;

import android.webkit.WebResourceResponse;
import org.apache.http.HttpRequest;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class ServerCommunication implements IServerCommunication {

   /* private WebResourceResponse webResource;
    private ClientResponse clientResponse; */
    private final String SERVER_ROOT_URI = "http://78.91.83.248:8000/api/game/";
    private final String JSON_FORMAT_URI = "?format=json";
    private URL requestURL;
    URLConnection connection;
    Scanner scanner;
    String response;
    JSONObject jsonResponse;
    private String[] games;
    private String[] words;

    ServerCommunication(){

        games = new String[parseJSONResponse(httpRequest.toString()).length];
        words = new String[16];

        try{
            requestURL = new URL(SERVER_ROOT_URI);
            connection = requestURL.openConnection();
            scanner = new Scanner(requestURL.openStream());

        }
        catch(MalformedURLException e){
            System.err.println(e.getMessage());
        }
        catch (IOException e){
            System.err.println(e.getMessage());
        }
        connection.setDoOutput(true);

        response = scanner.useDelimiter("\\Z").next();
        jsonResponse = parseJSONResponse(response);
        scanner.close();

    }


    public JSONObject parseJSONResponse(String jsonResponse) {

        JSONObject json;
        try {
            json = new JSONObject(jsonResponse);
            JSONObject result = json.getJSONObject("objects");
            games = result.getString("Timestamp");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
    }

    @Override
    public String[] getWordsFromServer() {
        for (int i = 0; i < words.length; i++) {
            words[i] = "";
        }
        return words;
    }
}


