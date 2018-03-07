package com.movieapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class UpComing extends Activity {

	ListView listView_theaters;

	/* Button btnlogout; */
	SharedPreferences sp;
	Editor ed;
	ImageButton imagesearch, imagefilter;
	EditText edtsearch;
	String url1 = "http://api.rottentomatoes.com/api/public/v1.0/lists/movies/upcoming.json?apikey=r3peqch73zncwneb58ufqm8c&page=";
	//String url2 = "http://api.themoviedb.org/3/movie/upcoming?api_key=58ac63f63559b2036c4936054f9573e3&page=";
	String url2="https://api.themoviedb.org/3/movie/now_playing?api_key=dd958aab0d5a41dd11a54e9791f5c00a&language=en-US&page=";
	int page = 1;
	Data_Most_Searched obj_db;
	Globals globals;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.upcoming);

		globals = (Globals) getApplicationContext();

		listView_theaters = (ListView) findViewById(R.id.listView_theaters);

		new getjson().execute();

		sp = getSharedPreferences("movie", Context.MODE_PRIVATE);
		ed = sp.edit();

		obj_db = new Data_Most_Searched(getApplicationContext());

		imagefilter = (ImageButton) findViewById(R.id.imagefilter);

	}

	ArrayList<HashMap<String, String>> list_ary_1;
	// ArrayList<Bitmap> bitmaps_1;

	ProgressDialog dialog;

	String json_string, json_string2;

	class getjson extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(UpComing.this);
			dialog.setCancelable(false);
			dialog.setMessage("Loading. . . ");
			dialog.show();
			list_ary = new ArrayList<HashMap<String, String>>();
		}

		@Override
		protected Void doInBackground(Void... arg0) {

			try {
				json_string = Network_Class.executeHttpGet(url1 + page);

				json_string2 = Network_Class.executeHttpGet(url2 + page);
				if(!"<h1>Developer Inactive</h1>\n".equals(json_string))
				parse_json(json_string);
				Log.e("size", list_ary.size() + "");
				parse_json2(json_string2);
				Log.e("size", list_ary.size() + "");

			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			

			hsh = new HashMap<String, String>();
			list_ary.add(hsh);

			dialog.dismiss();
			Upcoming_Adapter adapter = new Upcoming_Adapter(UpComing.this, list_ary, true);
			listView_theaters.setAdapter(adapter);
		}

	}

	ArrayList<HashMap<String, String>> list_ary;

	HashMap<String, String> temp_ary, temp_hsh;

	HashMap<String, String> hsh;

	/* ArrayList<Bitmap> bitmaps; */

	public void parse_json2(String j_string) throws Exception {
		JSONObject obj = new JSONObject(j_string);

		JSONArray ary = obj.getJSONArray("results");
		for (int i = 0; i < ary.length(); i++) {
			hsh = new HashMap<String, String>();
			JSONObject ob = ary.getJSONObject(i);

			hsh.put("id", ob.getString("id"));

			hsh.put("title", ob.getString("title"));

			hsh.put("theater", ob.getString("release_date"));

			hsh.put("synopsis", ob.getString("title"));

			hsh.put("img_url",
					"http://image.tmdb.org/t/p/w300"
							+ ob.getString("poster_path"));

			hsh.put("source", "http://www.themoviedb.org/");

			hsh.put("overview", ob.getString("overview"));

			hsh.put("vote_average", ob.getString("vote_average"));

			hsh.put("vote_count", ob.getString("vote_count"));

			int y = (int) (Double.parseDouble(ob.getString("vote_average")) * 10);
			/*
			 * int y1; String s=y+""; String g; if(s.contains(".")) { y1=(int)
			 * (y*10); g=y1+""; } else { g=y+""; }
			 */

			Log.e("rating", y + "");
			hsh.put("audience_score", (y) + "");

			list_ary.add(hsh);
		}

		globals.setMovieList(list_ary);



	}

	public void parse_json(String j_string) throws Exception {
		JSONObject obj = new JSONObject(j_string);

		JSONArray ary = obj.getJSONArray("movies");
		Log.e(ary.length() + "", json_string);
		for (int i = 0; i < ary.length(); i++) {
			hsh = new HashMap<String, String>();
			JSONObject ob = ary.getJSONObject(i);

			hsh.put("title", ob.getString("title"));

			JSONObject ob1 = ob.getJSONObject("release_dates");
			hsh.put("theater", ob1.getString("theater"));

			hsh.put("synopsis", ob.getString("synopsis"));

			JSONObject ob2 = ob.getJSONObject("posters");

			hsh.put("img_url", ob2.getString("thumbnail"));
			// bitmaps.add(get_bitmap(ob2.getString("thumbnail")));

			hsh.put("source", "http://www.rottentomatoes.com/");

			JSONObject obr = ob.getJSONObject("ratings");
			hsh.put("audience_score", obr.getString("audience_score"));

			list_ary.add(hsh);

		}

	}

	

}
