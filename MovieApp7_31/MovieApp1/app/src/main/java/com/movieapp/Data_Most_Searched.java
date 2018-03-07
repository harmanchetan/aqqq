package com.movieapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Data_Most_Searched extends SQLiteOpenHelper {

	static String dbname = "database_movie_search";
	static String tbname = "table_movie_search";

	SQLiteDatabase db;

	@SuppressLint("WrongConstant")
	public Data_Most_Searched(Context context) {
		super(context, dbname, null, 1);

		db = context.openOrCreateDatabase(dbname,
				SQLiteDatabase.CREATE_IF_NECESSARY, null);
		String qry = " create table if not exists " + tbname
				+ " ( text varchar(200) ,varrible int  )  ";
		db.execSQL(qry);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public void insert_upgrade(String text) {
		db = this.getWritableDatabase();
		String select_uniqueid_qry = " select * from '" + tbname
				+ "' where text= '" + text + "'  ";

		Cursor cr = db.rawQuery(select_uniqueid_qry, null);

		if (cr.moveToNext()) {
			// upgrade

			int temp_var = 0;
			temp_var= cr.getInt(1);
			temp_var=temp_var+1;
			String update_qry = "  UPDATE '" + tbname + "' SET varrible= '"
					+ temp_var + "'  WHERE text='" + text + "'   ";
			db.execSQL(update_qry);
			Log.e("data updated", temp_var+"");

		} else {
			// insert
			ContentValues cv = new ContentValues();
			cv.put("text", text);
			cv.put("varrible", 1);
			db.insert(tbname, null, cv);
			Log.e("data inserted", "data inserted");

		}

	}

	public String get_data()
	{
		String most_searched = null;
		
		int temp=0;
		
		db = this.getWritableDatabase();
		
		String select_uniqueid_qry = " select * from '" + tbname+"' ";

		Cursor cr = db.rawQuery(select_uniqueid_qry, null);
		
		while(cr.moveToNext())
		{
			if(cr.getInt(1)>temp)
			{
			temp=cr.getInt(1);
			most_searched=cr.getString(0);
			}
			
		}
		
		return most_searched;
	}
	
	
	
	public void close_db() {
		if (db.isOpen())
			db.close();
	}
}
