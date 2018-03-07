package com.movieapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Home extends Activity {

	ListView listView_theaters;

	/* Button btnlogout; */
	SharedPreferences sp;
	Editor ed;
	ImageButton imagesearch, imagefilter;
	EditText edtsearch;
	Globals globals;

	String url1 = "http://api.rottentomatoes.com/api/public/v1.0/lists/movies/in_theaters.json?apikey=r3peqch73zncwneb58ufqm8c&page=";
	//String url2 = "http://api.themoviedb.org/3/movie/now-playing?api_key=58ac63f63559b2036c4936054f9573e3&page=";
	String url2="https://api.themoviedb.org/3/movie/now_playing?api_key=dd958aab0d5a41dd11a54e9791f5c00a&language=en-US&page=";

	int page = 1;

	Data_Most_Searched obj_db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);

		globals = (Globals) getApplicationContext();

		listView_theaters = (ListView) findViewById(R.id.listView_theaters);

		new getjson().execute();

		sp = getSharedPreferences("movie", Context.MODE_PRIVATE);
		ed = sp.edit();

		obj_db = new Data_Most_Searched(getApplicationContext());

		/*
		 * btnlogout = (Button) findViewById(R.id.btnlogout);
		 * btnlogout.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View arg0) {
		 * 
		 * ed.clear(); ed.commit(); Intent intent = new Intent(Home.this,
		 * MainActivity.class); finish(); startActivity(intent);
		 * 
		 * } });
		 */

		imagefilter = (ImageButton) findViewById(R.id.imagefilter);
		imagefilter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				String ar[] = { "Ratings", "Most Searched"/*, "Geners"*/ };

				AlertDialog.Builder obj = new AlertDialog.Builder(Home.this);
				obj.setTitle("Sort By");
				obj.setCancelable(true);
				obj.setItems(ar, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						if (which == 1) {
							String searched = obj_db.get_data();
							list_ary_1 = new ArrayList<HashMap<String, String>>();
							// bitmaps_1=new ArrayList<Bitmap>();

							for (int i = 0; i < list_ary.size() - 1; i++) {

								if (list_ary.get(i).get("title").toLowerCase()
										.contains(searched.toLowerCase())) {
									list_ary_1.add(list_ary.get(i));

								}

							}

							if (list_ary_1.size() > 0) {
								Adapter adapter = new Adapter(Home.this,
										list_ary_1,false);
								listView_theaters.setAdapter(adapter);
							} else {
								Toast.makeText(getApplicationContext(),
										"No match Found", Toast.LENGTH_SHORT)
										.show();
							}
							edtsearch.setText(searched);
							Log.e("searched", searched + "");

						}
						
						if(which == 0)
						{
								for (int j = 0; j < list_ary.size()-1; j++)
							for (int i = 0; i < list_ary.size() - 2; i++) {

								temp_ary = new HashMap<String, String>();
								temp_hsh = new HashMap<String, String>();

								int i1 = Integer.parseInt(list_ary.get(i).get(
										"audience_score"));
								int i2 = Integer.parseInt(list_ary.get(i + 1).get(
										"audience_score"));
								// Log.e("" + i1, i2 + "");
								if (!(i1 > i2)) {
									temp_ary = list_ary.get(i);
									temp_hsh = list_ary.get(i + 1);

									list_ary.remove(i);
									list_ary.add(i, temp_hsh);

									list_ary.remove(i + 1);
									list_ary.add(i + 1, temp_ary);

									// Log.e("" + i1, i2 + "");
								}

							}

						/*hsh = new HashMap<String, String>();
						list_ary.add(hsh);*/

						Adapter adapter = new Adapter(Home.this, list_ary,false);
						listView_theaters.setAdapter(adapter);
							
						}

					}

				});

				AlertDialog ob = obj.create();
				ob.show();

			}
		});

		edtsearch = (EditText) findViewById(R.id.edtsearch);
		imagesearch = (ImageButton) findViewById(R.id.imagesearch);
		imagesearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				String s = edtsearch.getText().toString();

				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(edtsearch.getWindowToken(), 0);
				if (s.length() > 0) {
					obj_db.insert_upgrade(s);
					list_ary_1 = new ArrayList<HashMap<String, String>>();
					// bitmaps_1=new ArrayList<Bitmap>();

					for (int i = 0; i < list_ary.size() - 1; i++) {

						if (list_ary.get(i).get("title").toLowerCase()
								.contains(s.toLowerCase())) {
							list_ary_1.add(list_ary.get(i));
							/* bitmaps_1.add(bitmaps.get(i)); */
						}

					}

					if (list_ary_1.size() > 0) {
						Adapter adapter = new Adapter(Home.this, list_ary_1,false);
						listView_theaters.setAdapter(adapter);
					} else {
						Toast.makeText(getApplicationContext(),
								"No match Found", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});

	}

	ArrayList<HashMap<String, String>> list_ary_1;
	// ArrayList<Bitmap> bitmaps_1;

	ProgressDialog dialog;

	String json_string, json_string2;

	class getjson extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(Home.this);
			dialog.setCancelable(false);
			dialog.setMessage("Loading. . . ");
			dialog.show();
			list_ary = new ArrayList<HashMap<String, String>>();
			/* bitmaps = new ArrayList<Bitmap>(); */
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

		/*	for (int j = 0; j < list_ary.size(); j++)
				for (int i = 0; i < list_ary.size() - 1; i++) {

					temp_ary = new HashMap<String, String>();
					temp_hsh = new HashMap<String, String>();

					int i1 = Integer.parseInt(list_ary.get(i).get(
							"audience_score"));
					int i2 = Integer.parseInt(list_ary.get(i + 1).get(
							"audience_score"));
					// Log.e("" + i1, i2 + "");
					if (!(i1 > i2)) {
						temp_ary = list_ary.get(i);
						temp_hsh = list_ary.get(i + 1);

						list_ary.remove(i);
						list_ary.add(i, temp_hsh);

						list_ary.remove(i + 1);
						list_ary.add(i + 1, temp_ary);

						// Log.e("" + i1, i2 + "");
					}

				}*/

			hsh = new HashMap<String, String>();
			list_ary.add(hsh);

			dialog.dismiss();
			Adapter adapter = new Adapter(Home.this, list_ary,true);
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

	/*
	 * public Bitmap get_bitmap(String urll) { Bitmap bitmap = null; try { URL
	 * url = new URL(urll); bitmap =
	 * BitmapFactory.decodeStream(url.openConnection() .getInputStream()); }
	 * catch (Exception g) { g.printStackTrace(); } return bitmap; }
	 */

}
