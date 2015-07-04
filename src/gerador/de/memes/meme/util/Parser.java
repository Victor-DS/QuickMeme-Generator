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

public class Parser extends Activity {
	
	private final String MEMEGEN = "https://api.imgur.com/3/memegen/defaults.json";

	private final String EXTRAS = "https://api.imgur.com/3/memegen/defaults.json";

	private List<Meme> memes = new ArrayList<Meme>();

	public List<Meme> getMemes(String json) {
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
	
	public void addMeme(String id, String title, String link) {
		memes.add(new Meme(id, title, link));
	}
	
	public String getJSON(String link) {
		try {
			final HttpParams httpParams = new BasicHttpParams();
		    HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
			HttpClient httpclient = new DefaultHttpClient(httpParams);
			HttpGet httpGet = new HttpGet(link);
			httpGet.addHeader("Authorization", "Client-ID c18ea87d0bddb02");
			HttpResponse response = httpclient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			InputStream is = entity.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is,"utf-8"),8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			is.close();
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
			Log.e("Parser", "Connection Error");
			return null;
		}
	}
	
	public Meme getMeme(int i) {
		return memes.get(i);
	}

	public String getMEMEGEN() {
		return MEMEGEN;
	}

	public String getEXTRAS() {
		return EXTRAS;
	}
}
