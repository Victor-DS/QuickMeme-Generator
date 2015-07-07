package gerador.de.memes.meme.util;

import gerador.de.memes.meme.stuff.Meme;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;
import android.app.Activity;
import android.net.ParseException;
import android.util.Log;

/**
 * @author victor
 * 
 * Class that holds all the methods to handle the parsing.
 * It also holds a list of Memes, so you can add a meme calling
 * this class.
 */
public class Parser extends Activity {
	
	private static List<Meme> memes = new ArrayList<Meme>();

	public static List<Meme> getMemes(String json) {
		try {
			final JSONObject jObject = new JSONObject(json);
			
			final JSONArray jArray = jObject.getJSONArray("data");
			
			for (int i = 0; i < jArray.length(); i++) {
				memes.add(new Meme(jArray.getJSONObject(i).getString("id"), 
					jArray.getJSONObject(i).getString("title"), 
					jArray.getJSONObject(i).getString("link")));
			}
		} catch(ParseException e) {
			e.printStackTrace();
			Log.e("Parser", "Error parsing data");
		} catch(Exception e) {
			e.printStackTrace();
			Log.e("Parser", "General error");
		}
		return memes;
	}	
}
