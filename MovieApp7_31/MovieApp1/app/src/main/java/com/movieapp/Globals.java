package com.movieapp;

import android.app.Application;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by touchstone on 28-02-2018.
 */

public class Globals extends Application {


    private ArrayList<HashMap<String, String>> movieList = new ArrayList<HashMap<String, String>>();
    private ArrayList<HashMap<String, String>> upComingList = new ArrayList<HashMap<String, String>>();

    @Override
    public void onCreate() {
        super.onCreate();
    }


    public void setMovieList(ArrayList<HashMap<String, String>> movieList) {
        this.movieList = movieList;
    }

    public ArrayList<HashMap<String, String>> getMovieList() {
        return movieList;
    }

    public void setUpComingList(ArrayList<HashMap<String, String>> upComingList) {
        this.upComingList = upComingList;
    }

    public ArrayList<HashMap<String, String>> getUpComingList() {
        return upComingList;
    }
}
