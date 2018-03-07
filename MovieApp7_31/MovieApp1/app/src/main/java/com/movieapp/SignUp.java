package com.movieapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class SignUp extends Activity {

	Spinner spnfav;

	String ar[] = { "select favorite movie", "Romantic", "Comedy", "Emotional" };

	EditText edtname, edtusername, edtpassword;

	String s = "";

	Button btnreg;

	Database obj;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup);

		spnfav = (Spinner) findViewById(R.id.spnfav);
		spnfav.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, ar));

		obj = new Database(getApplicationContext());

		spnfav.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				if (arg2 != 0)
					s = ar[arg2];
				else
					s = "";
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
	

		edtname = (EditText) findViewById(R.id.edtname);
		edtusername = (EditText) findViewById(R.id.edtusername);
		edtpassword = (EditText) findViewById(R.id.edtpassword);

		btnreg = (Button) findViewById(R.id.btnreg);
		btnreg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (edtname.getText().toString().length() > 0
						&& edtusername.getText().toString().length() > 0
						&& edtpassword.getText().toString().length() > 0
						&& s.length() > 0) {

					if(!obj.select_name(edtusername.getText().toString()))
					{
				Boolean b=	obj.insert(edtname.getText().toString(), edtusername
							.getText().toString(), edtpassword.getText()
							.toString(), s);
				
				if(b)
				{
					Toast.makeText(getApplicationContext(), "Register sucessfully login once to confirm",
							Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(SignUp.this,MainActivity.class);
					finish();
					startActivity(intent);
				}
				else
				{
					Toast.makeText(getApplicationContext(), "Error creating account try again",
							Toast.LENGTH_SHORT).show();
				}

					}
					else
					{
						Toast.makeText(getApplicationContext(), "User already exist",
								Toast.LENGTH_SHORT).show();
					}
				
				}
				else
				{
					Toast.makeText(getApplicationContext(), "Fill all details",
							Toast.LENGTH_SHORT).show();
				}
				
				obj.close_db();

			}
		});

	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(SignUp.this,MainActivity.class);
		finish();
		startActivity(intent);
	}
}
