package com.movieapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class DetailsActivity extends Activity {

    TextView txttitle, txttime, txtmsg, txtsource, overview;
    RatingBar ratingBar;
    ImageView imgposter;
    ImageLoaderConfiguration config;
    ImageLoader imageLoader;
    String direc;

    // ArrayList<Bitmap> bitmaps_1;
    ListView listView_theaters;
    /* Button btnlogout; */
    SharedPreferences sp;
    SharedPreferences.Editor ed;
    ImageButton imagesearch, imagefilter;
    EditText edtsearch;
    Globals globals;
    String url1,movieId,videourl;
    TextView director, writer;
    Button trailer;
    //String url2 = "http://api.themoviedb.org/3/movie/now-playing?api_key=58ac63f63559b2036c4936054f9573e3&page=";
  //  String url2 = "https://api.themoviedb.org/3/movie/now_playing?api_key=dd958aab0d5a41dd11a54e9791f5c00a&language=en-US&page=";
    int page = 1;
    Data_Most_Searched obj_db;
    ArrayList<HashMap<String, String>> list_ary_1;
    // ArrayList<Bitmap> bitmaps_1;
    ProgressDialog dialog;
    String json_string, json_string2;
    HashMap<String, String> temp_ary, temp_hsh;
    HashMap<String, String> hsh;
    ArrayList<HashMap<String, String>> list_ary;
    private String directorId;
    private String writerId;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        inItViews();
    }

    private void inItViews() {
        globals = (Globals) getApplicationContext();
        String pos = getIntent().getStringExtra("position");
         movieId = getIntent().getStringExtra("id");
        int postion = Integer.parseInt(pos);
        txttitle =(TextView) findViewById(R.id.txttitle);
        txttime = (TextView)findViewById(R.id.txttime);
        txtmsg =(TextView) findViewById(R.id.txtmsg);
        imgposter =(ImageView) findViewById(R.id.imgposter);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        txtsource = (TextView)findViewById(R.id.txtsource);
        overview = (TextView) findViewById(R.id.overview);
        director = (TextView) findViewById(R.id.director);
        writer = (TextView) findViewById(R.id.writer);
        trailer =(Button) findViewById(R.id.trailer);


        List<HashMap<String, String>> list = globals.getMovieList();
        if (list != null) {
            String title = list.get(postion).get("title");
            String time = list.get(postion).get("theater");
            String message = list.get(postion).get("synopsis");
            String source = list.get(postion).get("source");
            String overvi = list.get(postion).get("overview");


            txttitle.setText(title);
            txttime.setText("Release Date:"+time);
            txtmsg.setText(message);
            //txtsource.setText(source);
            overview.setText(overvi);
        }


        config = new ImageLoaderConfiguration.Builder(DetailsActivity.this).build();
        ImageLoader.getInstance().init(config);
        imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(list.get(postion).get("img_url"), imgposter);


        int r = Integer.parseInt(list.get(postion).get("audience_score"));
        Log.e("rating", r + "");

        float d = (float) ((r * 5) / 100);

        if (r < 20) {
            ratingBar.setRating(d);
        }
        if (r < 40) {
            ratingBar.setRating(d);
        }
        if (r < 60) {
            ratingBar.setRating(d);
        }
        if (r < 80) {
            ratingBar.setRating(d);
        }
        if (r < 100) {
            ratingBar.setRating(d);
        }
        if (r == 100) {
            ratingBar.setRating(d);
        }

        url1 = "https://api.themoviedb.org/3/movie/"+movieId+"/credits?api_key=dd958aab0d5a41dd11a54e9791f5c00a";
        videourl = "https://api.themoviedb.org/3/movie/" + movieId + "/videos?api_key=dd958aab0d5a41dd11a54e9791f5c00a";

        new getjson().execute();


        director.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DetailsActivity.this, PeopleDetailActivity.class);
                intent.putExtra("id",directorId);
                intent.putExtra("actorname",director.getText().toString());
                intent.putExtra("movieId",movieId);
                startActivity(intent);

            }
        });

        writer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DetailsActivity.this, PeopleDetailActivity.class);
                intent.putExtra("id",writerId);
                intent.putExtra("actorname",writer.getText().toString());
                startActivity(intent);
            }
        });

        trailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new videosprocess().execute();
            }
        });
    }


    class getjson extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(DetailsActivity.this);
            dialog.setCancelable(false);
            dialog.setMessage("Loading. . . ");
            dialog.show();
            list_ary = new ArrayList<HashMap<String, String>>();
            /* bitmaps = new ArrayList<Bitmap>(); */
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try {
                json_string = Network_Class.executeHttpGet(url1);

                //json_string2 = Network_Class.executeHttpGet(url2 + page);

             /*   if (!"<h1>Developer Inactive</h1>\n".equals(json_string))
                    parse_json(json_string);
                Log.e("size", list_ary.size() + "");*/
                //parse_json2(json_string);


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

           /* hsh = new HashMap<String, String>();
            list_ary.add(hsh);
*/
            dialog.dismiss();

            JSONObject obj = null;
            try {
                obj = new JSONObject(json_string);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONArray ary = null;
            try {
                ary = obj.getJSONArray("crew");

                for (int i = 0; i < ary.length(); i++) {

                    JSONObject ob = ary.getJSONObject(i);

                    if(i==0){
                         direc = ob.getString("name");
                        director.setText("Director: "+direc);
                         directorId = ob.getString("id");
                    }
                    if(i==1){
                         direc = ob.getString("name");
                        writer.setText("Writer: " +direc);
                         writerId = ob.getString("id");
                    }
            }

                // Adapter adapter = new Adapter(DetailsActivity.this, list_ary, true);
           // listView_theaters.setAdapter(adapter);
        } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    public void parse_json2(String j_string) throws Exception {
        /*JSONObject obj = new JSONObject(j_string);

        JSONArray ary = obj.getJSONArray("crew");
        for (int i = 0; i < ary.length(); i++) {

            JSONObject ob = ary.getJSONObject(i);

            if(i==0){
                String direc = ob.getString("name");
                director.setText("Director: "+direc);
            }
            if(i==1){
                String direc = ob.getString("name");
                director.setText("Writer: " +direc);
            }
*/
            /*hsh = new HashMap<String, String>();
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
			*//*
			 * int y1; String s=y+""; String g; if(s.contains(".")) { y1=(int)
			 * (y*10); g=y1+""; } else { g=y+""; }
			 *//*

            Log.e("rating", y + "");
            hsh.put("audience_score", (y) + "");

            list_ary.add(hsh);*/

        }

       // globals.setMovieList(list_ary);


   }

    class videosprocess extends AsyncTask<Void, Void, Void> {
        String incomingresult;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(DetailsActivity.this);
            pd.setMessage("plz wait---");
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            incomingresult = getJson();


            return null;
        }

        public String getJson() {
            StringBuilder sb = new StringBuilder();
            try {
                URL url = new URL(videourl);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.connect();
                int code = con.getResponseCode();
                if (code == 200) {

                    InputStream in = con.getInputStream();
                    Scanner obj = new Scanner(in);
                    while (obj.hasNext()) {
                        sb.append(obj.nextLine());
                    }

                } else {
                    // Toast.makeText(getContext(), "Response Code:" + code, Toast.LENGTH_SHORT).show();

                }

            } catch (Exception ex) {
            }
            return (sb.toString());
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pd.dismiss();
            try {

                JSONObject mainobj = new JSONObject(incomingresult);
                JSONArray resultsarray = mainobj.getJSONArray("results");

                if (resultsarray.length() == 0) {
                    Toast.makeText(DetailsActivity.this, "Video is not available", Toast.LENGTH_LONG).show();
                } else {

                    JSONObject videoobject = resultsarray.getJSONObject(0);
                    String videokey = videoobject.getString("key");
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + videokey)));
                    Log.i("Video", "Video Playing....");
                }

            } catch (Exception ex) {
                String err = ex.toString();
                Log.e("eee", err);

            }


        }
    }

}
