package com.movieapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;

public class Menu extends Activity {

	//String url = "http://api.themoviedb.org/3/movie/now-playing?api_key=58ac63f63559b2036c4936054f9573e3";
	String url="https://api.themoviedb.org/3/movie/now_playing?api_key=dd958aab0d5a41dd11a54e9791f5c00a&language=en-US";
	LinearLayout ll_search, ll_playingnow, ll_upcoming;
	FrameLayout ll_image;
	TextView moviename;
	ImageView imageView1;
	SharedPreferences sp;
	Editor ed;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_xml);

		moviename = (TextView) findViewById(R.id.moviename);
		ll_image = (FrameLayout) findViewById(R.id.ll_image);
		sp = getSharedPreferences("movie", Context.MODE_PRIVATE);
		ed = sp.edit();
		// logout
		imageView1 = (ImageView) findViewById(R.id.imageView1);
		imageView1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				ed.clear();
				ed.commit();
				Intent intent = new Intent(Menu.this, MainActivity.class);
				finish();
				startActivity(intent);

			}
		});

		ll_search = (LinearLayout) findViewById(R.id.ll_search);
		ll_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent intent = new Intent(Menu.this, Search_Movie.class);
				startActivity(intent);

			}
		});

		ll_playingnow = (LinearLayout) findViewById(R.id.ll_playingnow);
		ll_playingnow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent intent = new Intent(Menu.this, Home.class);
				startActivity(intent);
			}
		});

		ll_upcoming = (LinearLayout) findViewById(R.id.ll_upcoming);
		ll_upcoming.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent intent = new Intent(Menu.this, UpComing.class);
				startActivity(intent);
			}
		});

		new download_task().execute();
	}

	class download_task extends AsyncTask<Void, Void, Void> {

		ProgressDialog dialog;
		Bitmap bitmap;
		String text;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			dialog = new ProgressDialog(Menu.this);
			dialog.setCancelable(false);
			dialog.setMessage("Loading. . . ");
			dialog.show();
		}

		@Override
		protected Void doInBackground(Void... arg0) {

			try {
				String incoming_json = Network_Class.executeHttpGet(url);

				JSONObject jsonObject = new JSONObject(incoming_json);

				JSONArray array = jsonObject.getJSONArray("results");

				JSONObject object = array.getJSONObject(0);

				text = object.getString("title");

				String path = "http://image.tmdb.org/t/p/w500"
						+ object.getString("backdrop_path");

				bitmap = get_bitmap(path);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			dialog.dismiss();

			BitmapDrawable background = new BitmapDrawable(bitmap);
			ll_image.setBackgroundDrawable(background);

			moviename.setText(text);

		}

	}

	public Bitmap get_bitmap(String urll) {
		Bitmap bitmap = null;
		try {
			URL url = new URL(urll);
			bitmap = BitmapFactory.decodeStream(url.openConnection()
					.getInputStream());
		} catch (Exception g) {
			g.printStackTrace();
		}
		return bitmap;
	}

}
