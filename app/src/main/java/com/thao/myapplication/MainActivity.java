package com.thao.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;




import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import model.SongItem;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;
import static com.thao.myapplication.PlaylistActivity.ARR_SONG;
import static com.thao.myapplication.PlaylistActivity.IMAGE_URL;
import static com.thao.myapplication.PlaylistActivity.POSITION;
import static com.thao.myapplication.PlaylistActivity.SINGER;
import static com.thao.myapplication.PlaylistActivity.SONG_URL;
import static com.thao.myapplication.PlaylistActivity.TITLE;

public class MainActivity extends AppCompatActivity {

    LinearLayout mRlt_parent_layout;
    AnimationDrawable mAnimationDrawable;
    ImageButton mImg_btn_play, btnPrev, btnNext;
    static MediaPlayer mMediaPlayer;
    TextView txtSongTitle, txtSinger, txtStart, txtEnd;
    ImageView imgSong;
    SeekBar sbSong;
    String song;
    PlaylistActivity pla;
    ArrayList<SongItem> dsbh;
    RelativeLayout rlt;
    int pos;
    static boolean isAlreadySetDataSource = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //  pla = new PlaylistActivity();
        //  dsbh = pla.getArrSongList();


        Intent intent = getIntent();
        String imageURL = intent.getStringExtra(IMAGE_URL);
        String songURL = intent.getStringExtra(SONG_URL);
        Bundle bd = intent.getBundleExtra("BUNDLE");
        dsbh = (ArrayList<SongItem>) bd.getSerializable(ARR_SONG);
        pos = intent.getIntExtra(POSITION, 0);
        song = songURL;


        String singer = intent.getStringExtra(SINGER);
        final String title = intent.getStringExtra(TITLE);

        addControl();
        setAnim();

        sbSong.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mMediaPlayer.seekTo(sbSong.getProgress());
            }
        });


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos++;
                if (pos > dsbh.size() - 1) {
                    pos = 0;
                }


                txtSongTitle.setText(dsbh.get(pos).getTitleSong());
                txtSinger.setText(dsbh.get(pos).getSinger());
                Glide.with(MainActivity.this).load(dsbh.get(pos).getImgRes()).into(imgSong);

                mMediaPlayer.stop();
                mMediaPlayer.reset();

                try {
                    mMediaPlayer.setDataSource(dsbh.get(pos).getSongUrl());
                    mMediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mMediaPlayer.start();
                sbSong.setMax(mMediaPlayer.getDuration());
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sbSong.setProgress(mMediaPlayer.getCurrentPosition());
                        handler.postDelayed(this, 500);
                    }
                }, 100);

            }
        });


        try {
            mMediaPlayer = new MediaPlayer();
            mAnimationDrawable.start();
            mImg_btn_play.setImageResource(R.drawable.ic_pause_song);
            setDataSourceMedia(song);
            mMediaPlayer.start();
            sbSong.setMax(mMediaPlayer.getDuration());
            updateTimeSong();
        } catch (IOException e) {
            e.printStackTrace();
        }


        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pos--;
                if (pos == -1) {
                    pos = dsbh.size() - 1;
                }

                txtSongTitle.setText(dsbh.get(pos).getTitleSong());
                txtSinger.setText(dsbh.get(pos).getSinger());
                Glide.with(MainActivity.this).load(dsbh.get(pos).getImgRes()).into(imgSong);
                mMediaPlayer.stop();
                mMediaPlayer.reset();

                try {
                    setDataSourceMedia(dsbh.get(pos).getSongUrl());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                sbSong.setMax(mMediaPlayer.getDuration());
                mMediaPlayer.start();
                updateTimeSong();

            }
        });
        addEvent();

        Glide.with(this).load(imageURL).into(imgSong);
        txtSongTitle.setText(title);
        txtSinger.setText(singer);
    }

    private void addControl() {
        txtSongTitle = (TextView) findViewById(R.id.txtSongTitle);
        txtSongTitle.setSelected(true);
        txtSongTitle.requestFocus();
        txtSinger = (TextView) findViewById(R.id.txtSinger);
        rlt = (RelativeLayout) findViewById(R.id.rlt_img_bg);
        //   txtStart = (TextView) findViewById(R.id.txtStart);
        //   txtEnd = (TextView) findViewById(R.id.txtEnd);
        btnNext = (ImageButton) findViewById(R.id.btnNext);
        btnPrev = (ImageButton) findViewById(R.id.btnPrev);
        imgSong = (ImageView) findViewById(R.id.imgSong);
        sbSong = (SeekBar) findViewById(R.id.sbSong);
        mRlt_parent_layout = (LinearLayout) findViewById(R.id.rlt_parent_layout);
        mAnimationDrawable = (AnimationDrawable) mRlt_parent_layout.getBackground();
        mImg_btn_play = (ImageButton) findViewById(R.id.btnPlay);
        // mMediaPlayer = new MediaPlayer();
        //rippleBackground=(RippleBackground)findViewById(R.id.content);
    }

    private void addEvent() {
        mImg_btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAnimationDrawable.isRunning()) {
                    mAnimationDrawable.stop();
                    // rippleBackground.stopRippleAnimation();
                    mImg_btn_play.setImageResource(R.drawable.ic_play_song);
                    mMediaPlayer.pause();
                } else {
                    try {
                        if (isAlreadySetDataSource) {
                            //  rippleBackground.startRippleAnimation();
                            mMediaPlayer.start();
                            //Toast.makeText(MainActivity.this, String.valueOf(mMediaPlayer.getDuration()), Toast.LENGTH_SHORT).show();
                        } else {
                            setDataSourceMedia(song);
                            mMediaPlayer.start();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mAnimationDrawable.start();
                    mImg_btn_play.setImageResource(R.drawable.ic_pause_song);
                }
            }
        });
    }

    private void setAnim() {
        mAnimationDrawable.setEnterFadeDuration(3500);
        mAnimationDrawable.setExitFadeDuration(3500);
    }

    private void setDataSourceMedia(String s) throws IOException {

        mMediaPlayer.setDataSource(s);
        mMediaPlayer.prepare();
        Toast.makeText(this, song, Toast.LENGTH_SHORT).show();
        isAlreadySetDataSource = true;
    }

    private void updateTimeSong() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sbSong.setProgress(mMediaPlayer.getCurrentPosition());
                handler.postDelayed(this, 500);
            }
        }, 100);
    }
}