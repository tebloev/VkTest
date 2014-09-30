package com.example.vktest;

import java.io.InputStream;
import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.util.LruCache;

public class ImagesCache { 
	
	 private LruCache<String, Bitmap> mMemoryCache;
	 static ArrayList<Item> data;
	 private static ImagesCache instance; 
	 
	 private ImagesCache (){ 
		 final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
		    final int cacheSize = maxMemory / 8;
		    mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
		        @Override
		        protected int sizeOf(String key, Bitmap bitmap) {
		            return bitmap.getByteCount() / 1024;
		        }
		    };
		    for (int i = 0; i<data.size(); i++)	    	
		    	new DownloadImageTask().execute(data.get(i).getUrl());
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
	
	public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
	    if (getBitmapFromMemCache(key) == null) {
	        mMemoryCache.put(key, bitmap);
	    }
	}

	public Bitmap getBitmapFromMemCache(String key) {
	    return mMemoryCache.get(key);
	}
	 
	 public static ImagesCache getInstance(ArrayList<Item> arr){ 
	  if (null == instance){ 
		  data = arr;		
		  instance = new ImagesCache(); 
	  } 
	  return instance; 
	 }

	public LruCache<String, Bitmap> getmMemoryCache() {
		return mMemoryCache;
	}

	public void setmMemoryCache(LruCache<String, Bitmap> mMemoryCache) {
		this.mMemoryCache = mMemoryCache;
	}
}
