package com.movieapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Search_Movie extends Activity {

    ListView listsearch;
    EditText edtsearch;
    ImageView imgbtnsearch;
    int page = 1;
    String tamo_url = "http://api.rottentomatoes.com/api/public/v1.0/movies.json?apikey=r3peqch73zncwneb58ufqm8c";
    // &q=THE%20HUNGER%20GAMES:%20MOCKINGJAY&page=1"

    //String movdb_url = "https://api.themoviedb.org/3/search/movie?api_key=58ac63f63559b2036c4936054f9573e3";
    String movdb_url = "https://api.themoviedb.org/3/search/movie/?api_key=dd958aab0d5a41dd11a54e9791f5c00a&language=en-US";

    // &query=happy+ending&page=2"

    Boolean ary1 = true, ary2 = true;

    TextView txtnoresult;
    Globals globals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_movie);

        globals = (Globals) getApplicationContext();

        listsearch = (ListView) findViewById(R.id.listsearch);

        txtnoresult = (TextView) findViewById(R.id.txtnoresult);
        txtnoresult.setVisibility(View.GONE);

        edtsearch = (EditText) findViewById(R.id.edtsearch);

        imgbtnsearch = (ImageView) findViewById(R.id.imgbtnsearch);
        imgbtnsearch.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (edtsearch.getText().toString().length() > 0) {

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edtsearch.getWindowToken(), 0);

                    txtnoresult.setVisibility(View.GONE);
                    new downloadtask().execute();
                }

            }
        });

    }

    class downloadtask extends AsyncTask<Void, Void, Void> {

        ProgressDialog dialog;

        ArrayList<HashMap<String, String>> ary;
        HashMap<String, String> hsh;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            dialog = new ProgressDialog(Search_Movie.this);
            dialog.setCancelable(false);
            dialog.setMessage("Loading. . . ");
            dialog.show();

            ary = new ArrayList<HashMap<String, String>>();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                /*String json_1 = Network_Class.executeHttpGet(tamo_url + "&q="
						+ edtsearch.getText().toString().replace(" ", "%20")
						+ "&page=" + page);*/

                String json_2 = Network_Class.executeHttpGet(movdb_url
                        + "&query="
                        + edtsearch.getText().toString().replace(" ", "%20")
                        + "&page=" + page);

                Log.e("path_1", tamo_url + "&q="
                        + edtsearch.getText().toString().replace(" ", "%20")
                        + "&page=" + page);

                Log.e("path_2", movdb_url + "&query="
                        + edtsearch.getText().toString().replace(" ", "%20")
                        + "&page=" + page);

                //Log.e("json_1", json_1);

                Log.e("json_2", json_2);

				/*// for first url
				JSONObject jsonObject_1 = new JSONObject(json_1);

				JSONArray jsonArray_1 = jsonObject_1.getJSONArray("movies");

				if (jsonArray_1.length() > 0) {

					for (int i = 0; i < jsonArray_1.length(); i++) {
						hsh = new HashMap<String, String>();

						JSONObject obj = jsonArray_1.getJSONObject(i);

						String title = obj.getString("title");
						hsh.put("title", title);

						try{
						JSONObject ob_date = obj.getJSONObject("release_dates");
						String date = ob_date.getString("theater");
						hsh.put("date", date);
						}catch(Exception e)
						{
							hsh.put("date", "");	
						}

						JSONObject ob_img = obj.getJSONObject("posters");
						String img_url = ob_img.getString("thumbnail");
						hsh.put("img_url", img_url);

						hsh.put("source", "http://www.rottentomatoes.com/");

						ary.add(hsh);

					}
					ary1 = true;

				} else {
					ary1 = false;
				}*/

                // for second url
                JSONObject jsonObject_2 = new JSONObject(json_2);

                JSONArray jsonArray_2 = jsonObject_2.getJSONArray("results");

                if (jsonArray_2.length() > 0) {

                    for (int j = 0; j < jsonArray_2.length(); j++) {
                        hsh = new HashMap<String, String>();
                        JSONObject obj = jsonArray_2.getJSONObject(j);
                        hsh.put("id", obj.getString("id"));
                        String title = obj.getString("title");
                        hsh.put("title", title);
                        String date = obj.getString("release_date");
                        hsh.put("date", date);
                        String img_url = obj.getString("poster_path");
                        hsh.put("img_url", "http://image.tmdb.org/t/p/w300"
                                + img_url);
                        hsh.put("source", "https://www.themoviedb.org/");
                        hsh.put("overview", obj.getString("overview"));

                        hsh.put("vote_average", obj.getString("vote_average"));

                        hsh.put("vote_count", obj.getString("vote_count"));

                        int y = (int) (Double.parseDouble(obj.getString("vote_average")) * 10);
			/*
			 * int y1; String s=y+""; String g; if(s.contains(".")) { y1=(int)
			 * (y*10); g=y1+""; } else { g=y+""; }
			 */

                        Log.e("rating", y + "");
                        hsh.put("audience_score", (y) + "");

                        ary.add(hsh);
                    }

                    globals.setMovieList(ary);
                    ary2 = true;

                } else {
                    ary2 = false;
                }


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


            hsh = new HashMap<String, String>();

            if (ary1 || ary2) {

                listsearch.setVisibility(View.VISIBLE);
                txtnoresult.setVisibility(View.GONE);

                ary.add(hsh);

                // set adapter
                Search_Adapter adapter = new Search_Adapter(Search_Movie.this,
                        ary, page, edtsearch.getText().toString()
                        .replace(" ", "%20"));
                listsearch.setAdapter(adapter);
                edtsearch.setText("");

            } else {
                // show text view
                listsearch.setVisibility(View.GONE);
                txtnoresult.setVisibility(View.VISIBLE);
            }

        }

    }

}
