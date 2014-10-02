package com.example.vktest;

import java.io.InputStream;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;



public class FriendsAdapter extends BaseAdapter {
	ImagesCache mCache;
	ArrayList<Item> data = new ArrayList<Item>();
	Context context;
	
	public FriendsAdapter(Context context, ArrayList<Item> arr) {
		if (arr != null) {
			data = arr;
		}		
		mCache = ImagesCache.getInstance(data);
	    notifyDataSetChanged();
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

	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

		ImageView img;
		public DownloadImageTask(ImageView img) {
			this.img = img;
		}
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
	        mCache.addBitmapToMemoryCache(urls[0], mBitmap);
	        return mBitmap;
	    }

	    protected void onPostExecute(Bitmap result) {
	    	img.setImageBitmap(result);
	    	notifyDataSetChanged();
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
		ImageView mImage = (ImageView) someView.findViewById(R.id.imageView);
		name.setText(data.get(i).getName());
		if (mCache.getBitmapFromMemCache(data.get(i).getUrl()) != null)
		{
			mImage.setImageBitmap(mCache.getBitmapFromMemCache(data.get(i).getUrl()));
		}
		else
		{
			mImage.setImageBitmap(null);
			OnlineCheck mCheck = OnlineCheck.getInstance();
			if(mCheck.isOnline(context))
			{
				new DownloadImageTask(mImage).execute(data.get(i).getUrl());
			}
		}
		return someView;
	}
}
