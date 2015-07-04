package gerador.de.memes.meme.util;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.widget.Toast;

public class Util {
	
	private Context mContext;
	
	private SharedPreferences s;
	
	private int count;
	
	public Util(Context c) {
		mContext = c;
		s = mContext.getSharedPreferences("MemesG", mContext.MODE_PRIVATE);
		count = s.getInt("Contagem", 0);
	}
	
	public boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) mContext.getSystemService(mContext.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	
	public void rate(String title, String message) {
		AlertDialog.Builder adBuilder = new AlertDialog.Builder(mContext);
		adBuilder.setTitle(title);
		adBuilder.setCancelable(true);
		adBuilder.setMessage(message);
		adBuilder.setPositiveButton("Avaliar", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				launchMarket(null);
			}
		});
		adBuilder.setNegativeButton("Depois", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.cancel();
			}
		});
		AlertDialog adFinal = adBuilder.create();
		adFinal.show();
	}
	
	public void launchMarket(String link) {
		Uri uri;
		if (link == null)
			uri = Uri.parse("market://details?id=" + mContext.getPackageName());
		else
			uri = Uri.parse(link);
	    Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
	    try {
	        mContext.startActivity(myAppLinkToMarket);
	    } catch (ActivityNotFoundException e) {
	        Toast.makeText(mContext, "Ops", Toast.LENGTH_LONG).show();
	    }
	}
	
	public void addCount() {
		count++;
		s.edit().putInt("Contagem", count).commit();
	}
	
	public int getCount() {
		return count;
	}
	
	public String getLastSavedMemesList() {
		return s.getString("MemesList", null);
	}
	
	public void setMemesList(String list) {
		s.edit().putString("MemesList", list).commit();
	}
	
	public Intent getOpenFacebookIntent() {
		try {
		   mContext.getPackageManager().getPackageInfo("com.facebook.katana", 0);
		   return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/1432467627026940"));
	   } catch (Exception e) {
		   return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/1432467627026940"));
	   }
	}

}
