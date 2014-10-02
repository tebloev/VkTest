package com.example.vktest;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.util.LruCache;

public class ImagesCache { 
	
	 public static LruCache<String, Bitmap> mMemoryCache;
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

}
