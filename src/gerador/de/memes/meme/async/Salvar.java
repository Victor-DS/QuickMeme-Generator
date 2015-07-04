package gerador.de.memes.meme.async;

import gerador.de.memes.meme.R;
import gerador.de.memes.meme.util.Generator;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class Salvar extends AsyncTask<Void,Void,Boolean> {
		
	private Context c;
	
	private Generator g;
	
	private boolean share;
	
	private RelativeLayout rLayout;
	
	public Salvar(Context c, RelativeLayout layout, boolean share) {
		this.c = c;
		g = new Generator(c);
		this.share = share;
		rLayout = layout;
	}
			
	@Override
	protected Boolean doInBackground(Void... arg0) {
		return g.generateImage(rLayout, g.generateFileName());
	}
	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		if(result)
			Toast.makeText(c, R.string.save_success, Toast.LENGTH_LONG).show();
		else
			Toast.makeText(c, R.string.error, Toast.LENGTH_LONG).show();
		if(share)
			c.startActivity(share());
		share = false;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		//TODO mudar para R.String...
		Toast.makeText(c, R.string.saving, Toast.LENGTH_SHORT).show();
	}
	
	private Intent share() {
		ContentValues values = new ContentValues(2);
	    values.put(MediaStore.Images.Media.MIME_TYPE, "image/*");
	    values.put(MediaStore.Images.Media.DATA, g.getLastImage());
	    Uri uri = c.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
	    Intent nick = new Intent();
        nick.setAction(Intent.ACTION_SEND);
        nick.setType("image/*");
        nick.putExtra(Intent.EXTRA_STREAM, uri);
        return nick;
	}

}
