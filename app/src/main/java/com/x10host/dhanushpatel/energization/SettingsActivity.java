package com.x10host.dhanushpatel.energization;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private Button feedbackButton;
    private SeekBar seekBarBuffer;
    int buffer;
    TextView bufferAmount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        feedbackButton = (Button) findViewById(R.id.feedbackButton);
        seekBarBuffer = (SeekBar) findViewById(R.id.seekBarBuffer);
        bufferAmount = (TextView) findViewById(R.id.bufferAmount);
        addListeners();

        seekBarBuffer.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);
        seekBarBuffer.getThumb().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);
        final SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String audioLengthGot = (mSharedPreference.getString("audiolength","Brief"));
        buffer = (mSharedPreference.getInt("buffer",0));
        seekBarBuffer.setProgress(buffer);
        bufferAmount.setText("Selected: " + buffer + "/" + seekBarBuffer.getMax());
        if(audioLengthGot.equals("None")){
            radioGroup.check(R.id.noneAudio);
        }
        else if(audioLengthGot.equals("Detailed")){
            radioGroup.check(R.id.detailedAudio);
        }
        else{
            radioGroup.check(R.id.briefAudio);
        }
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        // my_child_toolbar is defined in the layout file
        Toolbar myChildToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myChildToolbar);
        myChildToolbar.setTitleTextColor(Color.WHITE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onStop(){
        super.onStop();
        savePrefs();
    }
    @Override
    public void onPause() {
        super.onPause();
        savePrefs();
    }

    public void savePrefs(){
        int ID = radioGroup.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(ID);
        String audioLength = (String) radioButton.getText();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("audiolength", audioLength);
        editor.putInt("buffer", buffer);
        editor.commit();
        final SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String debugging = (mSharedPreference.getString("audiolength","ERROR"));
        Log.i("Current/new audiolength", debugging);
    }
    public void addListeners() {
        feedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"ee.android.app@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback on Energization app");
                //intent.putExtra(Intent.EXTRA_TEXT, "mail body");
                startActivity(Intent.createChooser(intent, ""));
                Log.i("Feedback button", "pressed");
            }
        });
        seekBarBuffer.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBarBuffer, int progressValue, boolean fromUser) {
                buffer = progressValue;
                seekBarBuffer.setProgress(buffer);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBarBuffer) {
                // Toast.makeText(getApplicationContext(), "Started tracking seekbar", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBarBuffer) {
                bufferAmount.setText("Selected: " + buffer + "/" + seekBarBuffer.getMax());

            }
        });


    }


}
