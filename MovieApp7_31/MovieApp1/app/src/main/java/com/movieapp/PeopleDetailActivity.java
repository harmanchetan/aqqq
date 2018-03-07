package com.movieapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class PeopleDetailActivity extends Activity {

    TextView detailsTV ,actorTv;
    String url ;
    ProgressDialog dialog;
    String json_string ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_people_detail);

        initViews();
    }

    private void initViews() {

        detailsTV =(TextView) findViewById(R.id.details);
        actorTv =(TextView) findViewById(R.id.txtactorname);

        String id = getIntent().getStringExtra("id");
        String actorname = getIntent().getStringExtra("actorname");
        actorTv.setText("About "+actorname);


        url = "https://api.themoviedb.org/3/person/"+ id+"?api_key=dd958aab0d5a41dd11a54e9791f5c00a&language=en-US";

        new getjson().execute();
    }

    class getjson extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(PeopleDetailActivity.this);
            dialog.setCancelable(false);
            dialog.setMessage("Loading. . . ");
            dialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try {
                json_string = Network_Class.executeHttpGet(url);

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



            dialog.dismiss();
            JSONObject obj = null;
            try {
                obj = new JSONObject(json_string);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                String biography= obj.getString("biography");
                if(!"".equals(biography)) {
                    detailsTV.setText(biography);
                }else{
                    detailsTV.setText("Biography is not available");
                }
                // Adapter adapter = new Adapter(DetailsActivity.this, list_ary, true);
                // listView_theaters.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }
}
