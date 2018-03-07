package com.imageloader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

public class ImageLoad {

	Context context;
	ImageView img;
	String path;

	public ImageLoad(Context context) {
		this.context = context;
	}

	public void show(ImageView img, String path) {

		this.img = img;
		this.path = path;

		if (has_file(path)) {
			// from sd card
			File mediaStorageDir = new File(
					Environment.getExternalStorageDirectory() + "/Movieapp/");
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPreferredConfig = Bitmap.Config.ARGB_8888;
			Bitmap bitmap = BitmapFactory.decodeFile(mediaStorageDir+"/"+path, options);
			img.setImageBitmap(bitmap);
		} else {
			// from server
			new background_task().execute();
		}

	}

	public Bitmap download_bitmap(String path) {

		Bitmap bitmap = null;
		try {
			URL url = new URL(path);
			bitmap = BitmapFactory.decodeStream(url.openConnection()
					.getInputStream());
		} catch (Exception g) {
			g.printStackTrace();
		}

		return bitmap;
	}

	public void save_bitmap(Bitmap bitmap, String path) {
		File mediaStorageDir = new File(
				Environment.getExternalStorageDirectory() + "/Movieapp/");

		if (!mediaStorageDir.exists()) {
			mediaStorageDir.mkdirs();
		}
		File outputFile = new File(mediaStorageDir, path);
		Log.e("path" , outputFile.getAbsolutePath()+"");
		try {
			FileOutputStream fos = new FileOutputStream(outputFile);
			bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
			fos.close();
		} catch (FileNotFoundException e) {
			Log.e("exception", "File not found: " + e.getMessage());
		} catch (IOException e) {
			Log.e("exception", "Error accessing file: " + e.getMessage());
		}

	}

	public Boolean has_file(String path) {
		File mediaStorageDir = new File(
				Environment.getExternalStorageDirectory() + "/Movieapp/");

		if (!mediaStorageDir.exists()) {
			mediaStorageDir.mkdirs();
		}

		File[] f = mediaStorageDir.listFiles();

		for (int i = 0; i < f.length; i++) {
			File fg = f[i];

			if (fg.getAbsolutePath().equals(path)) {
				return true;
			}

		}

		return false;
	}

	class background_task extends AsyncTask<Void, Void, Void> {
		Bitmap bitmap;

		@Override
		protected Void doInBackground(Void... params) {

			download_bitmap(path);

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			save_bitmap(bitmap, path);

			img.setImageBitmap(bitmap);

		}

	}

}
