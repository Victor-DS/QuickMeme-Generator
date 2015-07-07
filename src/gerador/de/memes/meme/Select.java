package gerador.de.memes.meme;

import gerador.de.memes.meme.adapter.GridAdapter;
import gerador.de.memes.meme.stuff.Meme;
import gerador.de.memes.meme.util.InternetUtil;
import gerador.de.memes.meme.util.Parser;
import gerador.de.memes.meme.util.Util;

import java.util.ArrayList;
import java.util.List;

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

/**
 * @author victor
 * Selection screen. Shows the user a GridView with icons of all available memes.
 */
public class Select extends SherlockActivity implements OnItemClickListener {

	List<Meme> memes;
	private GridView gView;
	private ProgressBar pBar;
	private boolean custom, doubleBackToExitPressedOnce = false;
	private Util u;
	
	private final int SD_CARD = 0;
	private final int GOOGLE_PLAY = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select);
		
		
		init();
		
		if(!u.isNetworkAvailable()) {
			pBar.setVisibility(View.GONE);
			gView.setVisibility(View.VISIBLE);
			Toast.makeText(Select.this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
			gView.setAdapter(new GridAdapter(Select.this, 
					Parser.getMemes(u.getLastSavedMemesList())));			
		} else new JSONTask().execute();
		
		gView.setOnItemClickListener(this);
	}
	
	private void init() {
		startAd();
		
		u = new Util(this);
		gView = (GridView) findViewById(R.id.gViewMemes);		
		pBar = (ProgressBar) findViewById(R.id.pBarSelect);
		
		memes = new ArrayList<Meme>();		
		memes.add(new Meme("MVD4zRj", getString(R.string.gallery), "http://i.imgur.com/MVD4zRj.png"));		
		memes.add(new Meme("nJjlN8s", "Google Play", "http://i.imgur.com/nJjlN8s.png"));
		
		addBrasileiros();
	}
		
	private void startAd() {
		AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
	}
	
	//Add Brazilian Memes
	public void addBrasileiros() {
		memes.add(new Meme("6d6Wceo", "Brasileiro", "http://i.imgur.com/6d6Wceo.jpg"));
		memes.add(new Meme("bCQkaQl", "Cão Dorgado", "http://i.imgur.com/bCQkaQl.jpg"));
		memes.add(new Meme("Ixi3Twa", "Chapolim Decepcionado", "http://i.imgur.com/Ixi3Twa.jpg"));
		memes.add(new Meme("37pbvDE", "Chapolim Explica", "http://i.imgur.com/37pbvDE.jpg"));
		memes.add(new Meme("RjYiAdk", "Cantadas de Pedreiro", "http://i.imgur.com/RjYiAdk.jpg"));		
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if(arg2 == SD_CARD)
			custom = true;
		else custom = false;
		
		if (arg2 == GOOGLE_PLAY) { //Clicked on Google Play
				Toast.makeText(Select.this, R.string.rate_us, Toast.LENGTH_LONG).show();
				u.launchMarket(null);
		} else
			startActivity(new Intent(this, Editor.class)
				.putExtra("title", memes.get(arg2).getTitle())
				.putExtra("link", memes.get(arg2).getLink())
				.putExtra("custom", custom));
	}
	
	/**
	 * @author victor
	 * Connects to the IMGUR API on the background to get a String
	 *  with the JSON response and then parses it and puts 
	 *  it on the GridView.
	 */
	private class JSONTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... arg0) {
			return InternetUtil.getResponse(InternetUtil.MEMEGEN_ENDPOINT);
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
			memes.addAll(Parser.getMemes(result));
			gView.setAdapter(new GridAdapter(Select.this, memes));
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

}
