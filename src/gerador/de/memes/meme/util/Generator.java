package gerador.de.memes.meme.util;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Environment;
import android.widget.RelativeLayout;

public class Generator {
	
	private Context mContext;
	
	private final String FOLDER = "Memes";
	
	private String LAST_IMAGE_SAVED_NAME;
			
	public Generator(Context c) {
		mContext = c;
	}
	
	public String getLastImage() {
		return Environment.getExternalStorageDirectory().getPath()+"/"+FOLDER+"/"+LAST_IMAGE_SAVED_NAME;
	}
	
	public String generateFileName() {
		Random generator = new Random();
		SimpleDateFormat sdf = new SimpleDateFormat("MM_dd_yyyy_HH_mm_ss");
		String currentDateandTime = sdf.format(new Date());
		return "Meme_"+ currentDateandTime + "_" + generator.nextInt(1000000000) +".jpg";
	}
	
	public boolean generateImage(RelativeLayout layout, final String name) {
		File arquivo = new File(Environment.getExternalStorageDirectory().getPath()+"/"+FOLDER+"/");
		
		if(!arquivo.exists())
			arquivo.mkdirs();
		
		try {
			File file = new File (arquivo, name);
			
			layout.setDrawingCacheEnabled(true);
			Bitmap screenshot;
			screenshot = Bitmap.createBitmap(layout.getDrawingCache());
			Canvas canvas = new Canvas(screenshot);
			file.createNewFile();
	        FileOutputStream out = new FileOutputStream(file);
			canvas.drawBitmap(screenshot, 0, 0, null);
			screenshot.compress(CompressFormat.JPEG, 100, out);
			out.flush();
	        out.close();
	        layout.setDrawingCacheEnabled(false);
	        LAST_IMAGE_SAVED_NAME = name;
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, 
					Uri.parse("file://"+Environment.getExternalStorageDirectory().getPath()+"/"+FOLDER+"/"+name)));
		}
	}

}
