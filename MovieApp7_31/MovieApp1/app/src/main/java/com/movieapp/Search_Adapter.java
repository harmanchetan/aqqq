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

public class Search_Adapter extends BaseAdapter {

	Context context;
	ArrayList<HashMap<String, String>> ary;

	ImageLoaderConfiguration config;
	ImageLoader imageLoader;
	
	String query;
	
	int page = 2;
	int p=0;

	String tamo_url = "http://api.rottentomatoes.com/api/public/v1.0/movies.json?apikey=r3peqch73zncwneb58ufqm8c";
	// &q=THE%20HUNGER%20GAMES:%20MOCKINGJAY&page=1"

	//String movdb_url = "https://api.themoviedb.org/3/search/movie?api_key=58ac63f63559b2036c4936054f9573e3";
	String movdb_url ="https://api.themoviedb.org/3/movie/now_playing?api_key=dd958aab0d5a41dd11a54e9791f5c00a&language=en-US";

	// &query=happy+ending&page=2"

	Boolean ary1 = true, ary2 = true;

	public Search_Adapter(Context context,
			ArrayList<HashMap<String, String>> ary, int page_index, String query) {

		this.context = context;
		this.ary = ary;
		this.query=query;

		inflater = LayoutInflater.from(context);

		config = new ImageLoaderConfiguration.Builder(context)

		.build();
		ImageLoader.getInstance().init(config);
		
		imageLoader=ImageLoader.getInstance();
		
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

	LayoutInflater inflater;

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

		
		

		if(arg0==ary.size()-1)
		{
			holder.btn_more.setVisibility(View.VISIBLE);
			holder.imgposter.setVisibility(View.GONE);
			holder.txttitle.setVisibility(View.GONE);
			holder.txtsource.setVisibility(View.GONE);
			holder.txtmsg.setVisibility(View.GONE);
			holder.ratingBar.setVisibility(View.GONE);
			holder.txttime.setVisibility(View.GONE);
			
		/*	if(p==121)
			{
				holder.btn_more.setVisibility(View.GONE);	
			}*/
			
			holder.btn_more.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					
					new downloadtask().execute();
				}
			});
			
		}
		else
		{
		
			holder.btn_more.setVisibility(View.GONE);
			holder.ratingBar.setVisibility(View.GONE);
			holder.txttime.setVisibility(View.GONE);
		//ImageLoader.d
		
		imageLoader.displayImage(ary.get(arg0).get("img_url"), holder.imgposter);

		// imageLoad.show(holder.imgposter, ary.get(arg0).get("img_url"));

		holder.txttitle.setText(ary.get(arg0).get("title"));
		//holder.txtsource.setText(ary.get(arg0).get("source"));
		holder.txtmsg.setText(ary.get(arg0).get("date"));

		}

		holder.ll.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(context, DetailsActivity.class);
				intent.putExtra("position",String.valueOf(arg0));
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

		ArrayList<HashMap<String, String>> ary_1;
		HashMap<String, String> hsh;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			dialog = new ProgressDialog(context);
			dialog.setCancelable(false);
			dialog.setMessage("Loading. . . ");
			dialog.show();

			ary_1 = new ArrayList<HashMap<String, String>>();
			
			ary.remove((ary.size()-1));
		}

		@Override
		protected Void doInBackground(Void... params) {

			try {
				String json_1 = Network_Class.executeHttpGet(tamo_url + "&q="
						+ query.replace(" ", "%20")
						+ "&page=" + page);

				String json_2 = Network_Class.executeHttpGet(movdb_url
						+ "&query="
						+ query.replace(" ", "%20")
						+ "&page=" + page);

				Log.e("path_1", tamo_url + "&q="
						+ query.replace(" ", "%20")
						+ "&page=" + page);

				Log.e("path_2", movdb_url + "&query="
						+ query.replace(" ", "%20")
						+ "&page=" + page);

				Log.e("json_1", json_1);

				Log.e("json_2", json_2);

				// for first url
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
				}

				// for second url
				JSONObject jsonObject_2 = new JSONObject(json_2);

				JSONArray jsonArray_2 = jsonObject_2.getJSONArray("results");

				if (jsonArray_2.length() > 0) {

					for (int j = 0; j < jsonArray_2.length(); j++) {
						hsh = new HashMap<String, String>();

						JSONObject obj = jsonArray_2.getJSONObject(j);

						String title = obj.getString("title");
						hsh.put("title", title);

						String date = obj.getString("release_date");
						hsh.put("date", date);

						String img_url = obj.getString("poster_path");
						hsh.put("img_url", "http://image.tmdb.org/t/p/w200"
								+ img_url);

						hsh.put("source", "https://www.themoviedb.org/");

						ary.add(hsh);
						
					}
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
			
			page=page+1;
			
			hsh=new HashMap<String, String>();

			if (ary1 || ary2) {

				
				ary.add(hsh);
				
				// set adapter
				ary=ary;
				notifyDataSetChanged();

			} else {
				// show text view
				p=121;
			}

		}

	}

}
