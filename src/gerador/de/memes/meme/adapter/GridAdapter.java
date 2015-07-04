package gerador.de.memes.meme.adapter;

import gerador.de.memes.meme.R;
import gerador.de.memes.meme.stuff.Meme;
import java.util.List;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class GridAdapter extends BaseAdapter {

	private Context mContext;
	
	private List<Meme> memes;

    private DisplayImageOptions options;

    private ImageLoader iLoader;
    
    public GridAdapter(Context mContext, List<Meme> memes) {
		this.mContext = mContext;
		this.memes = memes;
		
		options = new DisplayImageOptions.Builder()
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.showImageOnFail(R.drawable.warning)
		.build();
	
		iLoader = ImageLoader.getInstance();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mContext)
			.build();
		iLoader.init(config);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return memes.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return memes.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		final ImageView imageView;
        if (arg1 == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            imageView.setPadding(5, 5, 5, 5);
            imageView.setVisibility(View.GONE);
            imageView.setBackgroundColor(mContext.getResources().getColor(android.R.color.transparent));
        } else {
            imageView = (ImageView) arg1;
        }

        iLoader.displayImage("http://i.imgur.com/"+memes.get(arg0).getId()+"b.jpg", imageView, options, 
        		new SimpleImageLoadingListener() {

					@Override
					public void onLoadingCancelled(String imageUri, View view) {
						super.onLoadingCancelled(imageUri, view);
			            imageView.setVisibility(View.VISIBLE);
			            imageView.setImageResource(R.drawable.warning);
					}

					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						super.onLoadingComplete(imageUri, view, loadedImage);
			            imageView.setVisibility(View.VISIBLE);
					}

					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
						super.onLoadingFailed(imageUri, view, failReason);
			            imageView.setVisibility(View.VISIBLE);
			            imageView.setImageResource(R.drawable.warning);
					}

					@Override
					public void onLoadingStarted(String imageUri, View view) {
						super.onLoadingStarted(imageUri, view);
			            imageView.setVisibility(View.GONE);
					}
        	
        });
        
        return imageView;
	}

}
