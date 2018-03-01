package com.thao.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import model.SongItem;

public class PlaylistActivity extends AppCompatActivity implements SongAdapter.OnItemClickListener {

    public static final String IMAGE_URL = "imageUrl";
    public static final String SONG_URL = "songUrl";
    public static final String TITLE = "title";
    public static final String SINGER = "singer";
    public static final String ARR_SONG = "songList";
    public static final String POSITION = "position";
    RelativeLayout mRlt_parent_layout;
    AnimationDrawable mAnimationDrawable;
    ArrayList<SongItem> arrSongList;
    SongAdapter songAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        initView();
        new ReadJSONObject().execute("https://api.myjson.com/bins/vlfwh");

        initRecyclerView();


    }

    private void initView() {
        arrSongList = new ArrayList<>();
        mRlt_parent_layout = (RelativeLayout) findViewById(R.id.rlt_parent_layout);
        mAnimationDrawable = (AnimationDrawable) mRlt_parent_layout.getBackground();
        mAnimationDrawable.setEnterFadeDuration(3500);
        mAnimationDrawable.setExitFadeDuration(3500);
        mAnimationDrawable.start();
    }

    private void initRecyclerView(){
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rclv);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        songAdapter = new SongAdapter(arrSongList, getApplicationContext());
        recyclerView.setAdapter(songAdapter);
        songAdapter.setOnItemClickListener(PlaylistActivity.this);
    }

    @Override
    public void onItemClick(int position) {

        if(MainActivity.mMediaPlayer != null){
            MainActivity.mMediaPlayer.stop();
            MainActivity.mMediaPlayer.reset();
        }
        Intent playIntent = new Intent(PlaylistActivity.this, MainActivity.class);
        SongItem clickedSongItem = arrSongList.get(position);

        playIntent.putExtra(SONG_URL, clickedSongItem.getSongUrl());
        playIntent.putExtra(SINGER, clickedSongItem.getSinger());
        playIntent.putExtra(TITLE, clickedSongItem.getTitleSong());
        playIntent.putExtra(IMAGE_URL, clickedSongItem.getImgRes());

        Bundle bd = new Bundle();
        bd.putSerializable(ARR_SONG, (Serializable)arrSongList);
        playIntent.putExtra("BUNDLE", bd);
        playIntent.putExtra(ARR_SONG, arrSongList);
        playIntent.putExtra(POSITION, position);

        startActivity(playIntent);
    }

    private class ReadJSONObject extends AsyncTask<String, Void, String>{


        @Override
        protected String doInBackground(String... params) {

            StringBuilder content = new StringBuilder();

            try {
                URL url = new URL(params[0]);

                InputStreamReader inputStreamReader = new InputStreamReader(url.openConnection().getInputStream());

                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String line = "";

                while ((line = bufferedReader.readLine()) != null){
                     content.append(line);
                }

                bufferedReader.close();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return content.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject object = new JSONObject(s);

                JSONArray array = object.getJSONArray("song");

                for(int i = 0; i < array.length(); i++){
                    JSONObject objectElement = array.getJSONObject(i);

                    SongItem songs = new SongItem(objectElement.getString("image"), objectElement.getString("name"), objectElement.getString("singer"), objectElement.getString("url"));
                    arrSongList.add(songs);

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            songAdapter.notifyDataSetChanged();

        }


    }
    /*public ArrayList<SongItem> getArrSongList() {
        if(arrSongList != null) {
            return arrSongList;
        }
        return null;
    }*/



}
