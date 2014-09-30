package com.example.vktest;

import java.io.InputStream;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;



public class FriendsAdapter extends BaseAdapter {
	private LruCache<String, Bitmap> mMemoryCache;

	ArrayList<Item> data = new ArrayList<Item>();
	Context context;
	
	public FriendsAdapter(Context context, ArrayList<Item> arr) {
		if (arr != null) {
			data = arr;
		}
		ImagesCache ms = ImagesCache.getInstance(data);
		mMemoryCache = ms.getmMemoryCache();
//	    final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
//	    final int cacheSize = maxMemory / 8;
//	    mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
//	        @Override
//	        protected int sizeOf(String key, Bitmap bitmap) {
//	            return bitmap.getByteCount() / 1024;
//	        }
//	    };
//	    for (int i = 0; i<data.size(); i++)	    	
//	    	new DownloadImageTask().execute(data.get(i).getUrl());
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int num) {
		// TODO Auto-generated method stub
		return data.get(num);
	}

	public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
	    if (getBitmapFromMemCache(key) == null) {
	        mMemoryCache.put(key, bitmap);
	    }
	}

	public Bitmap getBitmapFromMemCache(String key) {
	    return mMemoryCache.get(key);
	}
	
	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

	    protected Bitmap doInBackground(String... urls) {
	        String url = urls[0];
	        Bitmap mBitmap = null;
	        try {
	            InputStream in = new java.net.URL(url).openStream();
	            mBitmap = BitmapFactory.decodeStream(in);
	        } catch (Exception e) {
	            Log.e("Error", e.getMessage());
	            e.printStackTrace();
	        }
	        addBitmapToMemoryCache(urls[0], mBitmap);
	        return mBitmap;
	    }

	    protected void onPostExecute(Bitmap result) {

	    }
	}
	
	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int i, View someView, ViewGroup arg2) {
		LayoutInflater inflater = LayoutInflater.from(context);
		if (someView == null) {
			someView = inflater.inflate(R.layout.listview_item, arg2, false);
		}
		TextView name = (TextView) someView.findViewById(R.id.textView);
		ImageView image = (ImageView) someView.findViewById(R.id.imageView);
		name.setText(data.get(i).getName());
		image.setImageBitmap(getBitmapFromMemCache(data.get(i).getUrl()));
		return someView;
	}
}
