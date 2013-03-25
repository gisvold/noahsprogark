package no.ntnu.noahsprogark.bedpresbingo;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AppPreferences {
	
	public static final String playerName = "";
	private SharedPreferences _sharedPrefs;
	private static final String APP_SHARED_PREFS = AppPreferences.class.getSimpleName();
	private Editor _prefsEditor;
	
	
	public AppPreferences(Context context){
		this._sharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS, Activity.MODE_PRIVATE);
		this._prefsEditor = _sharedPrefs.edit();
	}
	
	public String getPlayerName(){
		return _sharedPrefs.getString(APP_SHARED_PREFS, "");
	}
	
	public void savePlayerName(String text){
		_prefsEditor.putString(playerName, text);
		_prefsEditor.commit();
	}
	
}
