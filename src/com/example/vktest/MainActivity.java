package com.example.vktest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {
	final String REDIRECT = "https://oauth.vk.com/blank.html";
	String url = "https://oauth.vk.com/authorize?client_id=4569803&redirect_uri=" +
				  REDIRECT + "&scope=12&display=mobile&response_type=token";
	WebView mWebView;
	String token, user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWebView = (WebView)findViewById(R.id.webView);
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.loadUrl(url);
    }
    
	private class MyWebViewClient extends WebViewClient 
	{
	    @Override
	    public boolean shouldOverrideUrlLoading(WebView view, String url) 
	    {
	    	view.loadUrl(url);
	    	parseUrl(url);	        
	        return true;
	    }
	}
    
	private void tokenParser(String s)
	{
		String exp = "&";
		String acc = "access_token=";
		int first = s.indexOf(acc) + acc.length();
		int last = s.indexOf(exp);
		token = s.substring(first, last);
		first = s.indexOf("user_id");
		user_id = s.substring(first);
		
	}
	
	class GetFriendsTask extends AsyncTask<String, Void, String>
	{
		private ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
		
		@Override
		protected String doInBackground(String... urls) {
			String result = null;	
			try {		
					URL url = new URL(urls[0]);
					HttpGet httpGet = new HttpGet(url.toString());
					DefaultHttpClient client = new DefaultHttpClient();					
					HttpResponse response = client.execute(httpGet);
					BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(),"UTF-8"));
					StringBuilder sb = new StringBuilder();
					String line = null;
					  while ((line = reader.readLine()) != null) {			   
						   sb.append(line + System.getProperty("line.separator"));
					   }  
					result = sb.toString();	
				} catch (ClientProtocolException e) {

					e.printStackTrace();
				} catch (IOException e) {

					e.printStackTrace();
				}        
	        			
			return result;
		}
		
		@Override 
		protected void onPreExecute()
		{
			progressDialog.setMessage("Список загружается...");
		    progressDialog.show();
		    progressDialog.setCanceledOnTouchOutside(true);	
		}
		
		@Override
		protected void onPostExecute(String result) {
			progressDialog.dismiss();
			parseJSON(result);
			super.onPostExecute(result);
		}
	}
	
	private void parseJSON(String s)
	{
		ArrayList<Item> data = new ArrayList<Item>();
		try {
			JSONObject mJson = new JSONObject(s);
			JSONObject mResponse = mJson.getJSONObject("response");
			JSONArray mItems = mResponse.getJSONArray("items");
			for (int i = 0; i<mItems.length(); i++)
			{
				JSONObject mCurItem = mItems.getJSONObject(i);
				String name = mCurItem.getString("first_name") + " " + mCurItem.getString("last_name");
				String url = mCurItem.getString("photo_100");	
				data.add(new Item(name, url));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ImagesCache ms = ImagesCache.getInstance(data);
		ListView mListView = (ListView)findViewById(R.id.listView);
		mListView.setAdapter(new FriendsAdapter(MainActivity.this, data));	
	}
	
	
	
	private void parseUrl(String mUrl) {
	    try {
	        if(mUrl==null)
	            return;
	        if (mUrl.toString().contains(REDIRECT))
	        {
	        	tokenParser(mUrl);
	        	mWebView.loadUrl(mUrl);
	        	mWebView.setVisibility(WebView.GONE);
	        	Toast.makeText(this, "Вы успешно авторизовались!", Toast.LENGTH_LONG).show();     
	        	new GetFriendsTask().execute("https://api.vk.com/method/friends.get?" + user_id +
	        			"&client_id=4569803&v=5.25&order=random&fields=photo_100&name_case=nom&access_token=" + token);
	        	
	        }
	    } catch (Exception e) {
	        e.printStackTrace();	    
	        Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();        
	        }
	}
    
}
