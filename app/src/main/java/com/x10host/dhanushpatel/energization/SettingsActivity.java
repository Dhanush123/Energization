package com.x10host.dhanushpatel.energization;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class SettingsActivity extends AppCompatActivity {
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        final SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String audioLengthGot = (mSharedPreference.getString("audiolength","Brief"));
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
    }
    public void onStop(){
        int ID = radioGroup.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(ID);
        String audioLength = (String) radioButton.getText();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("audiolength",audioLength);
        editor.commit();
        final SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String debugging = (mSharedPreference.getString("audiolength","ERROR"));
        Log.i("Current/new audiolength", debugging);
        super.onStop();
    }

}
