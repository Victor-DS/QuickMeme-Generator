package gerador.de.memes.meme.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.util.Log;

/**
 * @author victor
 *
 * Collection of methods to help dealing with the 
 * connection between phone and internet.
 */
public class InternetUtil {
	
	public static final String MEMEGEN_ENDPOINT = "https://api.imgur.com/3/memegen/defaults.json";

	public static String getResponse(String link) {
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
}
