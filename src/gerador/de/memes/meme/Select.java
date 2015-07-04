package gerador.de.memes.meme;

import gerador.de.memes.meme.adapter.GridAdapter;
import gerador.de.memes.meme.util.Parser;
import gerador.de.memes.meme.util.Util;
import java.util.Locale;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockActivity;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class Select extends SherlockActivity implements OnItemClickListener {

	private Parser parser;
	
	private GridView gView;
	
	private ProgressBar pBar;
	
	private boolean custom;
	
	private Util u;
	
	private boolean doubleBackToExitPressedOnce = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select);
		
		startAd();
		
		u = new Util(this);
		
		parser = new Parser();
		
		gView = (GridView) findViewById(R.id.gViewMemes);
		
		pBar = (ProgressBar) findViewById(R.id.pBarSelect);
		
		parser.addMeme("MVD4zRj", getString(R.string.gallery), "http://i.imgur.com/MVD4zRj.png");
										
		parser.addMeme("nJjlN8s", "Google Play", "http://i.imgur.com/nJjlN8s.png");
		
		addBrasileiros();

		if(u.isNetworkAvailable())
			new JSONTask().execute();
		else {
			pBar.setVisibility(View.GONE);
			gView.setVisibility(View.VISIBLE);
			//if(u.getLastSavedMemesList() == null)
			Toast.makeText(Select.this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
			gView.setAdapter(new GridAdapter(Select.this, 
					parser.getMemes(u.getLastSavedMemesList())));
		}
		
		gView.setOnItemClickListener(this);
	}
		
	private void startAd() {
		AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
	}
	
	public void addBrasileiros() {
		parser.addMeme("6d6Wceo", "Brasileiro", "http://i.imgur.com/6d6Wceo.jpg");
		parser.addMeme("bCQkaQl", "Cão Dorgado", "http://i.imgur.com/bCQkaQl.jpg");
		parser.addMeme("Ixi3Twa", "Chapolim Decepcionado", "http://i.imgur.com/Ixi3Twa.jpg");
		parser.addMeme("37pbvDE", "Chapolim Explica", "http://i.imgur.com/37pbvDE.jpg");
		parser.addMeme("RjYiAdk", "Cantadas de Pedreiro", "http://i.imgur.com/RjYiAdk.jpg");		
	}

	@Override
	protected void onStart() {
		super.onStart();
		EasyTracker.getInstance().activityStart(this);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		EasyTracker.getInstance().activityStop(this);
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if(arg2==0)
			custom = true;
		
		if(Locale.getDefault().getDisplayLanguage().equals("português")) {
			if (arg2==1) {
				u.launchMarket("market://details?id=peel.melhores.imagens.piadas.videos.engracadas.humor");
			} else if (arg2==2) {
				Toast.makeText(Select.this, R.string.rate_us, Toast.LENGTH_LONG).show();
				u.launchMarket(null);
			} else
				startActivity(new Intent(this, Editor.class)
					.putExtra("title", parser.getMeme(arg2).getTitle())
					.putExtra("link", parser.getMeme(arg2).getLink())
					.putExtra("custom", custom));
		} else {
			if (arg2==1) {
				Toast.makeText(Select.this, R.string.rate_us, Toast.LENGTH_LONG).show();
				u.launchMarket(null);
			} else
				startActivity(new Intent(this, Editor.class)
					.putExtra("title", parser.getMeme(arg2).getTitle())
					.putExtra("link", parser.getMeme(arg2).getLink())
					.putExtra("custom", custom));
		}			
	}
	
	private class JSONTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... arg0) {
			return parser.getJSON(parser.getMEMEGEN());
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			pBar.setVisibility(View.GONE);
			gView.setVisibility(View.VISIBLE);
			if(result == null)
				Toast.makeText(Select.this, R.string.error, Toast.LENGTH_SHORT).show();
			else
				u.setMemesList(result);
			gView.setAdapter(new GridAdapter(Select.this, parser.getMemes(result)));
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		
	}

	@Override
	public void onBackPressed() {
		if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, R.string.press_again_to_exit, Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
             doubleBackToExitPressedOnce=false;   

            }
        }, 2000);
	}
	
	private boolean verifyGooglePlayServices() {
		int isAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(Select.this);
		
		if (isAvailable != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(isAvailable)) {
				Dialog dialog = GooglePlayServicesUtil.getErrorDialog(isAvailable, 
					Select.this, 10);
				dialog.show();
			} else {
				Log.i("Menu", "This device is not supported.");
	            finish();
			}
			return false;
		}
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		verifyGooglePlayServices();
	}
}
