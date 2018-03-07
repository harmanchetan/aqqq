package com.movieapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Database extends SQLiteOpenHelper {

	static String dbname = "database_movie";
	static String tbname = "table_movie";

	SQLiteDatabase db;

	public Database(Context context) {
		super(context, dbname, null, 1);

		db = context.openOrCreateDatabase(dbname,
				SQLiteDatabase.CREATE_IF_NECESSARY, null);
		String qry = " create table if not exists "
				+ tbname
				+ " ( name varchar(200) , username varchar(200) ,password varchar(200),fav varchar(200)  )  ";
		db.execSQL(qry);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public Boolean insert(String name, String username, String password,String fav) {

		// using query
		db = this.getWritableDatabase();

		ContentValues cv = new ContentValues();
		cv.put("name", name);
		cv.put("username", username);
		cv.put("password", password);
		cv.put("fav", fav);

		db.insert(tbname, null, cv);

		Log.e("data inserted", "data inserted");
		return true;

	}

	/*
	 * public void update(String name, String unique_id, String msg, String
	 * time) {
	 * 
	 * // update query db = this.getWritableDatabase(); String update_qry =
	 * "  UPDATE '" + tbname + "' SET name= '" + name + "' , msg= '" + msg +
	 * "',time='" + time + "' WHERE unique_id='" + unique_id + "'   ";
	 * db.execSQL(update_qry);
	 * 
	 * Log.e("data updated", "data updated");
	 * 
	 * }
	 */

/*	ArrayList<HashMap<String, String>> ar;
	HashMap<String, String> hsh;

	public ArrayList<HashMap<String, String>> getrecords(String unique_id) {
		ar = new ArrayList<HashMap<String, String>>();
		// fetch records
		db = this.getWritableDatabase();
		String select_qry = " select * from " + tbname + " where unique_id="
				+ unique_id + " ";

		Cursor cr = db.rawQuery(select_qry, null);
		while (cr.moveToNext()) {
			hsh = new HashMap<String, String>();

			hsh.put("phn_no", cr.getString(0));
			hsh.put("unique_id", cr.getString(1));

			hsh.put("flag_server", cr.getString(2));
			hsh.put("flag_device", cr.getString(3));

			hsh.put("msg", cr.getString(4));
			hsh.put("msg_tag", cr.getString(5));

			hsh.put("msg_id", cr.getString(6));

			ar.add(hsh);
		}

		return ar;

	}*/

	public Boolean select_name_password(String username,String password) {

		db = this.getWritableDatabase();
		String select_uniqueid_qry = " select * from '" + tbname
				+ "' where username= '" + username + "' AND password= '" + password + "' ";

		Cursor cr = db.rawQuery(select_uniqueid_qry, null);

		if (cr.moveToNext()) {
			return true;
		} else {
			return false;
		}

	}
	
	public Boolean select_name(String username) {

		db = this.getWritableDatabase();
		String select_uniqueid_qry = " select * from '" + tbname
				+ "' where username= '" + username + "'  ";

		Cursor cr = db.rawQuery(select_uniqueid_qry, null);

		if (cr.moveToNext()) {
			return true;
		} else {
			return false;
		}

	}

	public void delete() {
		db = this.getWritableDatabase();
		String del_qry = " delete  from " + tbname + " ";

		db.execSQL(del_qry);
	}

	public void close_db() {
		if (db.isOpen())
			db.close();
	}
}
