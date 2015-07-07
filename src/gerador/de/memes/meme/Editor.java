package gerador.de.memes.meme;

import java.io.FileNotFoundException;
import yuku.ambilwarna.AmbilWarnaDialog;
import yuku.ambilwarna.AmbilWarnaDialog.OnAmbilWarnaListener;
import gerador.de.memes.meme.async.Salvar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.google.analytics.tracking.android.EasyTracker;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

/**
 * @author victor
 * 
 * Meme Editor
 */
public class Editor extends SherlockActivity implements OnMenuItemClickListener, OnClickListener {
		
	private TextView superior, inferior, aPlus, aMinus, color;
	private ImageView imagem;
	private RelativeLayout MemeLayout;
    private DisplayImageOptions options;
    private ImageLoader iLoader;
    private ProgressBar progressBar;
	private int LAST_COLOR = Color.WHITE;
	
	private final int SAVE = 0;
	private final int SHARE = 1;
    	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editor);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(getIntent().getExtras().getString("title"));

		init();
		
		if(getIntent().getExtras().getBoolean("custom")) {
			startActivityForResult(new Intent().setAction(Intent.ACTION_GET_CONTENT).setType("image/*"), 0);
		} else {
			iLoader.displayImage(getIntent().getExtras().getString("link"), imagem, options, 
					loadImage);
		}
	}

	private void init() {
		superior = (TextView) findViewById(R.id.tvSuperior);
		superior.setOnClickListener(this);
		inferior = (TextView) findViewById(R.id.tvInferior);
		inferior.setOnClickListener(this);
		imagem = (ImageView) findViewById(R.id.ivMeme);
		MemeLayout = (RelativeLayout) findViewById(R.id.rlMeme);
		progressBar = (ProgressBar) findViewById(R.id.pBarEditor);
		aPlus = (TextView) findViewById(R.id.tvAPlus);
		aPlus.setOnClickListener(this);
		aMinus = (TextView) findViewById(R.id.tvAMinus);
		aMinus.setOnClickListener(this);
		color = (TextView) findViewById(R.id.tvColor);
		color.setOnClickListener(this);
		
		options = new DisplayImageOptions.Builder()
			.cacheInMemory(true)
			.cacheOnDisc(true)
			.showImageOnFail(R.drawable.warning)
			.build();
	
		iLoader = ImageLoader.getInstance();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(Editor.this)
			.build();
		iLoader.init(config);
	}
	
	SimpleImageLoadingListener loadImage = new SimpleImageLoadingListener() {

		@Override
		public void onLoadingCancelled(String imageUri, View view) {
			super.onLoadingCancelled(imageUri, view);
			MemeLayout.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.GONE);
			inferior.setVisibility(View.GONE);
			superior.setVisibility(View.GONE);
		}

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			super.onLoadingComplete(imageUri, view, loadedImage);
			MemeLayout.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.GONE);
		}

		@Override
		public void onLoadingFailed(String imageUri, View view,
				FailReason failReason) {
			super.onLoadingFailed(imageUri, view, failReason);
			MemeLayout.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.GONE);
			inferior.setVisibility(View.GONE);
			superior.setVisibility(View.GONE);
		}

		@Override
		public void onLoadingStarted(String imageUri, View view) {
			super.onLoadingStarted(imageUri, view);
			MemeLayout.setVisibility(View.GONE);
			progressBar.setVisibility(View.VISIBLE);
		}
		
	};
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode != RESULT_OK) {
			Toast.makeText(getApplicationContext(), R.string.no_image_selected, Toast.LENGTH_SHORT).show();
			MemeLayout.setVisibility(View.GONE);
		}
		
		if (resultCode == RESULT_OK) {
			if (data == null) {
				Toast.makeText(getApplicationContext(), "Oops...", Toast.LENGTH_SHORT).show();
				MemeLayout.setVisibility(View.GONE);
			}
			
			if (data.getData() == null) {
				Toast.makeText(getApplicationContext(), "Oops...", Toast.LENGTH_SHORT).show();
				MemeLayout.setVisibility(View.GONE);
			}
			try {
				Bitmap bmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(data.getData()));
				imagem.setImageBitmap(bmap);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		menu.add(0,SAVE,0,"Save")
			.setTitle(R.string.save)
			.setIcon(android.R.drawable.ic_menu_save)
			.setOnMenuItemClickListener(this)
			.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		
		menu.add(0,SHARE,0,"Share")
			.setIcon(android.R.drawable.ic_menu_share)
			.setOnMenuItemClickListener(this)
			.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
				
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;			
		}
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		switch(item.getItemId()) {
		case SAVE:
			new Salvar(Editor.this, MemeLayout, false).execute();
			break;
			
		case SHARE:
			new Salvar(Editor.this, MemeLayout, true).execute();
			break;
		}
		return true;
	}

	@Override
	public void onClick(View arg0) {
		switch(arg0.getId()) {
		case R.id.tvAPlus: //Increase text size
			superior.setTextSize(TypedValue.COMPLEX_UNIT_PX, superior.getTextSize()+1);
			inferior.setTextSize(TypedValue.COMPLEX_UNIT_PX, inferior.getTextSize()+1);
			break;
			
		case R.id.tvAMinus: //Decrease text size
			superior.setTextSize(TypedValue.COMPLEX_UNIT_PX, superior.getTextSize()-1);
			inferior.setTextSize(TypedValue.COMPLEX_UNIT_PX, inferior.getTextSize()-1);			
			break;
			
		case R.id.tvColor: //Change color
			AmbilWarnaDialog pop = new AmbilWarnaDialog(Editor.this, LAST_COLOR, 
					new OnAmbilWarnaListener() {

						@Override
						public void onCancel(AmbilWarnaDialog dialog) {
							//Do nothing
						}

						@Override
						public void onOk(AmbilWarnaDialog dialog, int cor) {
							// TODO Auto-generated method stub
							color.setTextColor(cor);
							superior.setTextColor(cor);
							inferior.setTextColor(cor);
							LAST_COLOR = cor;
						}				
			});
			pop.show();
			break;
			
		case R.id.tvSuperior: //Change top text
			popDialog(true);
			break;
			
		case R.id.tvInferior: //Change bottom text
			popDialog(false);
			break;
			
		}
	}
	
	public void popDialog(final boolean cima) {
		LayoutInflater lInflater = Editor.this.getLayoutInflater();
		View layout = lInflater.inflate(R.layout.dialog_layout, null);
		AlertDialog.Builder adBuilder = new AlertDialog.Builder(Editor.this);
		adBuilder.setView(layout);
		final EditText eText = (EditText) layout.findViewById(R.id.eText);
		if(cima)
			eText.setText(superior.getText().toString());
		else
			eText.setText(inferior.getText().toString());
		adBuilder.setTitle(gerador.de.memes.meme.R.string.set_text_pop_up_title);
		adBuilder.setCancelable(true);
		adBuilder.setPositiveButton(gerador.de.memes.meme.R.string.ok, new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				if(cima)
					superior.setText(eText.getText().toString());
				else
					inferior.setText(eText.getText().toString());
			}
		});
		adBuilder.setNegativeButton(gerador.de.memes.meme.R.string.cancel, new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		AlertDialog adFinal = adBuilder.create();
		adFinal.show();
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
