package com.x10host.dhanushpatel.energization;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;


public class BeginPrayerActivity extends AppCompatActivity implements MediaPlayer.OnErrorListener {

    ImageButton playButton;
    MediaPlayer mPlayer;
    private String audioLengthGot;
    private String audioLength;
    private int length;
    private int lastStep, step;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_begin_prayer);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        lastStep = 2;
        step = 2;

        playButton = (ImageButton) findViewById(R.id.playButton1);
        addListeners();
        startMusic();
    }

    private void addListeners() {

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startMusic();
                Log.i("Play step index", "" + 2);

            }
        });

        FloatingActionButton myFab = (FloatingActionButton) findViewById(R.id.fab);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i("Going to","main activity now");
                Intent i = new Intent(BeginPrayerActivity.this,MainActivity.class);
                startActivity(i);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent i = new Intent(this,SettingsActivity.class);
            startActivity(i);
            return true;
        }
        else if(id==R.id.action_info) {
            Intent i = new Intent(this,InfoActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onError(final MediaPlayer arg0, final int arg1, final int arg2) {
        // Error handling logic here
        return true;
    }

    public void getAudioLength() {
        final SharedPreferences mSharedPreference = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        audioLengthGot = (mSharedPreference.getString("audiolength", "Brief"));
        if (audioLengthGot.equals("Brief")) {
            audioLength = "short";
        } else if (audioLengthGot.equals("Detailed")) {
            audioLength = "long";
        } else {
            audioLength = "none";
        }
        Log.i("audiolength use"," in BeginPrayer "+ audioLength);
    }

    public void startMusic(){
        getAudioLength();

        if(mPlayer==null) {
            Log.i("is mPlayer null?", (mPlayer == null) + "");
            if(audioLength.equals("none")){
                playButton.setImageResource(R.drawable.play24);
                Log.i("Button press", "only pic display");
            }
            else{
                int id = setStepMusic(audioLength);
                if(id!=-100000) {
                    mPlayer = MediaPlayer.create(getApplicationContext(), id);

                    playerStart();

                    playButton.setImageResource(R.drawable.pause24);
                    Log.i("Button press", "music play first time");
                }
            }
        }
        else if(mPlayer!=null){
            Log.i("is mPlayer null?", (mPlayer == null) + "");
            if(audioLength.equals("none")){
                playButton.setImageResource(R.drawable.play24);
                Log.i("Button press", "only pic display");
            }
            else {
                Log.i("audiolength is","not none");
                int id = setStepMusic(audioLength);
                if(id!=-100000) {
                        if (mPlayer.isPlaying()) {
                            length = mPlayer.getCurrentPosition();
                            if(length!=0) {
                                mPlayer.pause();
                                playButton.setImageResource(R.drawable.play24);
                                Log.i("paused music at", length + "");
                            }
                            else{
                                mPlayer.stop();
                                mPlayer = null;
                                mPlayer = MediaPlayer.create(getApplicationContext(), id);
                                Log.i("does lastStep!=step ?", (lastStep!=step)+"");
                                playButton.setImageResource(R.drawable.pause24);
                                playerStart();
                            }
                        } else if (!mPlayer.isPlaying()) {
                            mPlayer.seekTo(length);
                            Log.i("seek to music at", length + "");
                            playButton.setImageResource(R.drawable.pause24);

                            playerStart();
                        }
                }
                else {
                    mPlayer.stop();
                    mPlayer = null;
                    playButton.setImageResource(R.drawable.play24);
                }
            }
        }
    }

    private void playerStart(){
        mPlayer.start();
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.i("actually playing music", "yes");
                playButton.setImageResource(R.drawable.play24);

            }
        });
    }

    private int setStepMusic(String audioLength) {
        int id = -100000;
        if(audioLength!="none") {
            id = getApplicationContext().getResources().getIdentifier("prayer", "raw", getApplicationContext().getPackageName());
        }
        return id;
    }

    @Override
    public void onDestroy() {
        if(mPlayer!=null) {
            mPlayer.stop();
        }
        super.onDestroy();
    }
    @Override
    public void onStop(){
        if(mPlayer!=null) {
            mPlayer.release();
            mPlayer = null;
        }
        super.onStop();
    }
    public void onPause(){
        SharedPreferences sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("step",3);
        editor.commit();
        //will be executed onPause
        Log.i("went to onPause","");
        super.onPause();
    }
}