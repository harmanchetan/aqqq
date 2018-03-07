package com.movieapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	EditText username, password;
	Button btnlogin;

	TextView txtregister;
	
	SharedPreferences sp;
	Editor ed;

	Database obj;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		username = (EditText) findViewById(R.id.edtusername);
		password = (EditText) findViewById(R.id.edtpassword);

		btnlogin = (Button) findViewById(R.id.btnlogin);
		
		sp=getSharedPreferences("movie",Context.MODE_PRIVATE);
		ed=sp.edit();
		
		obj=new Database(getApplicationContext());
		
		btnlogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (username.getText().toString().length() > 0) {
					if (password.getText().toString().length() > 0) {

						if(obj.select_name_password(username.getText().toString(), password.getText().toString()))
						{
							ed.putBoolean("bool", true);
							ed.commit();
							Intent intent = new Intent(MainActivity.this,Menu.class);
							finish();
							startActivity(intent);
						}
						else {
							Toast.makeText(getApplicationContext(),
									"invalid username or password", Toast.LENGTH_SHORT).show();
						}
						
						
					} else {
						Toast.makeText(getApplicationContext(),
								"Fill password", Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(getApplicationContext(), "Fill Username",
							Toast.LENGTH_SHORT).show();
				}

			}
		});

		txtregister = (TextView) findViewById(R.id.txtregister);
		txtregister.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent intent = new Intent(MainActivity.this,SignUp.class);
				finish();
				startActivity(intent);

			}
		});

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		if(sp.getBoolean("bool", false))
		{
			Intent intent = new Intent(MainActivity.this,Menu.class);
			finish();
			startActivity(intent);
		}
		
	}
	
}
