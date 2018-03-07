package com.movieapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Upcoming_Adapter extends BaseAdapter {

    Context context;
    ArrayList<HashMap<String, String>> ary;
    ImageLoaderConfiguration config;
    ImageLoader imageLoader;
    /* ArrayList<Bitmap> bitmaps; */
    Boolean btn_bool;
    String url1 = "http://api.rottentomatoes.com/api/public/v1.0/lists/movies/upcoming.json?apikey=r3peqch73zncwneb58ufqm8c&page=";
    //String url2 = "http://api.themoviedb.org/3/movie/upcoming?api_key=58ac63f63559b2036c4936054f9573e3&page=";
    String url2 = "https://api.themoviedb.org/3/movie/now_playing?api_key=dd958aab0d5a41dd11a54e9791f5c00a&language=en-US&page=";

    int page = 2;
    LayoutInflater inflater;
    HashMap<String, String> temp_ary, temp_hsh;
    HashMap<String, String> hsh;

    public Upcoming_Adapter(Context context,
                            ArrayList<HashMap<String, String>> ary, Boolean btn_bool/*
                                                                     * ,ArrayList
																	 * <Bitmap>
																	 * bitmaps
																	 */) {

        this.context = context;
        this.ary = ary;
        this.btn_bool = btn_bool;
		/* this.bitmaps=bitmaps; */
        inflater = LayoutInflater.from(context);

        config = new ImageLoaderConfiguration.Builder(context)

                .build();
        ImageLoader.getInstance().init(config);

        imageLoader = ImageLoader.getInstance();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return ary.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    // ArrayList<HashMap<String, String>> list_ary;

    @Override
    public View getView(final int arg0, View arg1, ViewGroup arg2) {

        viewholder holder;

        if (arg1 == null) {
            arg1 = inflater.inflate(R.layout.row_list, null);
            holder = new viewholder();
            holder.txttitle = (TextView) arg1.findViewById(R.id.txttitle);
            holder.txttime = (TextView) arg1.findViewById(R.id.txttime);
            holder.txtmsg = (TextView) arg1.findViewById(R.id.txtmsg);

            holder.imgposter = (ImageView) arg1.findViewById(R.id.imgposter);
            holder.ratingBar = (RatingBar) arg1.findViewById(R.id.ratingBar);
            holder.txtsource = (TextView) arg1.findViewById(R.id.txtsource);
            holder.btn_more = (Button) arg1.findViewById(R.id.btn_more);
            holder.ll = (LinearLayout) arg1.findViewById(R.id.ll);

            arg1.setTag(holder);

        } else {
            holder = (viewholder) arg1.getTag();
        }

        if (arg0 == ary.size() - 1) {
            if (btn_bool) {
                holder.btn_more.setVisibility(View.VISIBLE);
                holder.imgposter.setVisibility(View.GONE);
                holder.txttitle.setVisibility(View.GONE);
                holder.txtsource.setVisibility(View.GONE);
                holder.txtmsg.setVisibility(View.GONE);
                holder.ratingBar.setVisibility(View.GONE);
                holder.txttime.setVisibility(View.GONE);

				/*
				 * if(p==121) { holder.btn_more.setVisibility(View.GONE); }
				 */

                holder.btn_more.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {

                        new downloadtask().execute();
                    }
                });
            }
        } else {

            holder.btn_more.setVisibility(View.GONE);
            holder.ratingBar.setVisibility(View.GONE);

            holder.txttitle.setText(ary.get(arg0).get("title"));
            holder.txttime.setText(ary.get(arg0).get("theater"));
            holder.txtmsg.setText(ary.get(arg0).get("synopsis"));

            // holder.imgposter.setImageBitmap(bitmaps.get(arg0));
            imageLoader.displayImage(ary.get(arg0).get("img_url"),
                    holder.imgposter);
            holder.txtsource.setText(ary.get(arg0).get("source"));

            holder.ratingBar.setFocusable(false);

            int r = Integer.parseInt(ary.get(arg0).get("audience_score"));
            Log.e("rating", r + "");

            float d = (float) ((r * 5) / 100);

            if (r < 20) {
                holder.ratingBar.setRating(d);
            }
            if (r < 40) {
                holder.ratingBar.setRating(d);
            }
            if (r < 60) {
                holder.ratingBar.setRating(d);
            }
            if (r < 80) {
                holder.ratingBar.setRating(d);
            }
            if (r < 100) {
                holder.ratingBar.setRating(d);
            }
            if (r == 100) {
                holder.ratingBar.setRating(d);
            }

        }


        holder.ll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("position", String.valueOf(arg0));
                intent.putExtra("id",ary.get(arg0).get("id"));
                context.startActivity(intent);
            }
        });

        return arg1;
    }

    class viewholder {
        TextView txttitle, txttime, txtmsg, txtsource;
        ImageView imgposter;
        RatingBar ratingBar;
        Button btn_more;
        LinearLayout ll;
    }

    class downloadtask extends AsyncTask<Void, Void, Void> {

        ProgressDialog dialog;

        String json_string, json_string2;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(context);
            dialog.setCancelable(false);
            dialog.setMessage("Loading. . . ");
            dialog.show();

            ary.remove(ary.size() - 1);
            // list_ary = new ArrayList<HashMap<String, String>>();
			/* bitmaps = new ArrayList<Bitmap>(); */
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                json_string = Network_Class.executeHttpGet(url1 + page);

                json_string2 = Network_Class.executeHttpGet(url2 + page);

                parse_json(json_string);
                Log.e("size", ary.size() + "");
                parse_json2(json_string2);
                Log.e("size", ary.size() + "");

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            page = page + 1;

            for (int j = 0; j < ary.size(); j++)
                for (int i = 0; i < ary.size() - 1; i++) {

                    temp_ary = new HashMap<String, String>();
                    temp_hsh = new HashMap<String, String>();

                    int i1 = Integer.parseInt(ary.get(i).get("audience_score"));
                    int i2 = Integer.parseInt(ary.get(i + 1).get(
                            "audience_score"));
                    // Log.e("" + i1, i2 + "");
                    if (!(i1 > i2)) {
                        temp_ary = ary.get(i);
                        temp_hsh = ary.get(i + 1);

                        ary.remove(i);
                        ary.add(i, temp_hsh);

                        ary.remove(i + 1);
                        ary.add(i + 1, temp_ary);

                        // Log.e("" + i1, i2 + "");
                    }

                }

            hsh = new HashMap<String, String>();
            ary.add(hsh);

            dialog.dismiss();
            ary = ary;
            notifyDataSetChanged();
        }

        public void parse_json2(String j_string) throws Exception {
            JSONObject obj = new JSONObject(j_string);

            JSONArray ary1 = obj.getJSONArray("results");
            for (int i = 0; i < ary1.length(); i++) {
                hsh = new HashMap<String, String>();
                JSONObject ob = ary1.getJSONObject(i);

                hsh.put("title", ob.getString("title"));

                hsh.put("theater", ob.getString("release_date"));

                hsh.put("synopsis", ob.getString("title"));

                hsh.put("img_url",
                        "http://image.tmdb.org/t/p/w300"
                                + ob.getString("poster_path"));

                hsh.put("source", "http://www.themoviedb.org/");

                int y = (int) (Double.parseDouble(ob.getString("vote_average")) * 10);
				/*
				 * int y1; String s=y+""; String g; if(s.contains(".")) {
				 * y1=(int) (y*10); g=y1+""; } else { g=y+""; }
				 */

                Log.e("rating", y + "");
                hsh.put("audience_score", (y) + "");

                ary.add(hsh);
            }

        }

        public void parse_json(String j_string) throws Exception {
            JSONObject obj = new JSONObject(j_string);

            JSONArray ary1 = obj.getJSONArray("movies");
            Log.e(ary1.length() + "", json_string);
            for (int i = 0; i < ary1.length(); i++) {
                hsh = new HashMap<String, String>();
                JSONObject ob = ary1.getJSONObject(i);

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

                ary.add(hsh);

            }

        }

    }

}
