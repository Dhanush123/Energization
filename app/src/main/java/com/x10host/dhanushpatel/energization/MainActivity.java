package com.x10host.dhanushpatel.energization;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ImageButton playButton;
    ImageButton previousButton;
    ImageButton nextButton;
    MediaPlayer mPlayer;
    TextView stepShow;
    ImageView iv;
    SeekBar seekBar;
    int stepSelected;
    String audioLengthGot;
    String audioLength;
    static final String RES_PREFIX = "android.resource://com.x10host.dhanushpatel.energization/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        iv = (ImageView) findViewById(R.id.imageView);
        playButton = (ImageButton) findViewById(R.id.playButton);
        previousButton = (ImageButton) findViewById(R.id. previousButton);
        nextButton = (ImageButton) findViewById(R.id.nextButton);
        seekBar = (SeekBar) findViewById(R.id.seekBar);;
        stepShow = (TextView) findViewById(R.id.stepID);
        iv.setImageResource(R.drawable.splashscreen);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.steps_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        addButtonListener();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
           // int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                stepSelected = progressValue;
               // Toast.makeText(getApplicationContext(), "Changing seekbar's progress", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
               // Toast.makeText(getApplicationContext(), "Started tracking seekbar", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                stepShow.setText("Selected: " + stepSelected + "/" + seekBar.getMax());
               // textView.setText("Covered: " + progress + "/" + seekBar.getMax());
               // Toast.makeText(getApplicationContext(), "Stopped tracking seekbar", Toast.LENGTH_SHORT).show();
            }
        });
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
        Log.i("audiolength use in Main", audioLength);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(this,SettingsActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    public void startMusic(int step){
        if(step < 1){
            Toast.makeText(getApplicationContext(), "Can't seek back more.", Toast.LENGTH_SHORT).show();
        }
        else if(step > 39){
            Toast.makeText(getApplicationContext(), "Can't seek forward more.", Toast.LENGTH_SHORT).show();
        }
        else {
            getAudioLength();
            if(mPlayer==null) {
                if(audioLength.equals("none")){
                    Log.i("Button press","only pic display");
                    setPicOnly(step);
                }
                else{
                    int id = setStepMusic(step, audioLength);
                    mPlayer = MediaPlayer.create(getApplicationContext(), id);
                    Log.i("Button press","music play");
                    mPlayer.start();
                }
            }
            if(mPlayer!=null){
                if(audioLength.equals("none")){
                    Log.i("Button press","only pic display");
                    setPicOnly(step);
                }
                else {
                    mPlayer.stop();
                    int id = setStepMusic(step, audioLength);
                    mPlayer = MediaPlayer.create(getApplicationContext(), id);
                    Log.i("Button press","music play");
                    mPlayer.start();
                }
            }
        }
    }

    public void addButtonListener() {

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startMusic(stepSelected);
                Log.i("Play music", "button pressed");
            }
        });
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startMusic(stepSelected-1);
                Log.i("Previous music", "button pressed");
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startMusic(stepSelected+1);
                Log.i("Next music","button pressed");
            }
        });
    }

    public void setPicOnly(int stepChosen){
        if (stepChosen == 1) {
            iv.setImageResource(R.drawable.body1);
        } else if (stepChosen == 2) {
            iv.setImageResource(R.drawable.body2);
        } else if (stepChosen == 3) {
            iv.setImageResource(R.drawable.body3);
        } else if (stepChosen == 4) {
            iv.setImageResource(R.drawable.body4);
        } else if (stepChosen == 5) {
            iv.setImageResource(R.drawable.body5);
        } else if (stepChosen == 6) {
            iv.setImageResource(R.drawable.body6);
        } else if (stepChosen == 7) {
            iv.setImageResource(R.drawable.body7);
        } else if (stepChosen == 8) {
            iv.setImageResource(R.drawable.body8);
        } else if (stepChosen == 9) {
            iv.setImageResource(R.drawable.body8);
        } else if (stepChosen == 10) {
            iv.setImageResource(R.drawable.body10);
        } else if (stepChosen == 11) {
            iv.setImageResource(R.drawable.body11);
        } else if (stepChosen == 12) {
            iv.setImageResource(R.drawable.body12);
        } else if (stepChosen == 13) {
            iv.setImageResource(R.drawable.body13);
        } else if (stepChosen == 14) {
            iv.setImageResource(R.drawable.body14);
        } else if (stepChosen == 15) {
            iv.setImageResource(R.drawable.body15);
        } else if (stepChosen == 16) {
            iv.setImageResource(R.drawable.body16);
        } else if (stepChosen == 17) {
            iv.setImageResource(R.drawable.body17);
        } else if (stepChosen == 18) {
            iv.setImageResource(R.drawable.body18);
        } else if (stepChosen == 19) {
            iv.setImageResource(R.drawable.body19);
        } else if (stepChosen == 20) {
            iv.setImageResource(R.drawable.body20);
        } else if (stepChosen == 21) {
            iv.setImageResource(R.drawable.body21);
        } else if (stepChosen == 22) {
            iv.setImageResource(R.drawable.body22);
        } else if (stepChosen == 23) {
            iv.setImageResource(R.drawable.body23);
        } else if (stepChosen == 24) {
            iv.setImageResource(R.drawable.body24);
        } else if (stepChosen == 25) {
            iv.setImageResource(R.drawable.body25);
        } else if (stepChosen == 26) {
            iv.setImageResource(R.drawable.body26);
        } else if (stepChosen == 27) {
            iv.setImageResource(R.drawable.body27);
        } else if (stepChosen == 28) {
            iv.setImageResource(R.drawable.body28);
        } else if (stepChosen == 29) {
            iv.setImageResource(R.drawable.body29);
        } else if (stepChosen == 30) {
            iv.setImageResource(R.drawable.body30);
        } else if (stepChosen == 31) {
            iv.setImageResource(R.drawable.body31);
        } else if (stepChosen == 32) {
            iv.setImageResource(R.drawable.body32);
        } else if (stepChosen == 33) {
            iv.setImageResource(R.drawable.body33);
        } else if (stepChosen == 34) {
            iv.setImageResource(R.drawable.body34);
        } else if (stepChosen == 35) {
            iv.setImageResource(R.drawable.body35);
        } else if (stepChosen == 36) {
            iv.setImageResource(R.drawable.body36);
        } else if (stepChosen == 37) {
            iv.setImageResource(R.drawable.body37);
        } else if (stepChosen == 38) {
            iv.setImageResource(R.drawable.body38);
        } else if (stepChosen == 39) {
            iv.setImageResource(R.drawable.body39);
        }
    }

    public int setStepMusic(int stepChosen,String length){
        int id = -100000;
        if(length=="short") {
            if (stepChosen == 1) {
                id = getApplicationContext().getResources().getIdentifier("brief1", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body1);
            } else if (stepChosen == 2) {
                id = getApplicationContext().getResources().getIdentifier("brief2", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body2);
            } else if (stepChosen == 3) {
                id = getApplicationContext().getResources().getIdentifier("brief3", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body3);
            } else if (stepChosen == 4) {
                id = getApplicationContext().getResources().getIdentifier("brief4", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body4);
            } else if (stepChosen == 5) {
                id = getApplicationContext().getResources().getIdentifier("brief5", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body5);
            } else if (stepChosen == 6) {
                id = getApplicationContext().getResources().getIdentifier("brief6", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body6);
            } else if (stepChosen == 7) {
                id = getApplicationContext().getResources().getIdentifier("brief7", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body7);
            } else if (stepChosen == 8) {
                id = getApplicationContext().getResources().getIdentifier("brief8", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body8);
            } else if (stepChosen == 9) {
                id = getApplicationContext().getResources().getIdentifier("brief9", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body8);
            } else if (stepChosen == 10) {
                id = getApplicationContext().getResources().getIdentifier("brief10", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body10);
            } else if (stepChosen == 11) {
                id = getApplicationContext().getResources().getIdentifier("brief11", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body11);
            } else if (stepChosen == 12) {
                id = getApplicationContext().getResources().getIdentifier("brief12", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body12);
            } else if (stepChosen == 13) {
                id = getApplicationContext().getResources().getIdentifier("brief13", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body13);
            } else if (stepChosen == 14) {
                id = getApplicationContext().getResources().getIdentifier("brief14", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body14);
            } else if (stepChosen == 15) {
                id = getApplicationContext().getResources().getIdentifier("brief15", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body15);
            } else if (stepChosen == 16) {
                id = getApplicationContext().getResources().getIdentifier("brief16", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body16);
            } else if (stepChosen == 17) {
                id = getApplicationContext().getResources().getIdentifier("brief17", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body17);
            } else if (stepChosen == 18) {
                id = getApplicationContext().getResources().getIdentifier("brief18", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body18);
            } else if (stepChosen == 19) {
                id = getApplicationContext().getResources().getIdentifier("brief19", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body19);
            } else if (stepChosen == 20) {
                id = getApplicationContext().getResources().getIdentifier("brief20", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body20);
            } else if (stepChosen == 21) {
                id = getApplicationContext().getResources().getIdentifier("brief21", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body21);
            } else if (stepChosen == 22) {
                id = getApplicationContext().getResources().getIdentifier("brief22", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body22);
            } else if (stepChosen == 23) {
                id = getApplicationContext().getResources().getIdentifier("brief23", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body23);
            } else if (stepChosen == 24) {
                id = getApplicationContext().getResources().getIdentifier("brief24", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body24);
            } else if (stepChosen == 25) {
                id = getApplicationContext().getResources().getIdentifier("brief25", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body25);
            } else if (stepChosen == 26) {
                id = getApplicationContext().getResources().getIdentifier("brief26", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body26);
            } else if (stepChosen == 27) {
                id = getApplicationContext().getResources().getIdentifier("brief27", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body27);
            } else if (stepChosen == 28) {
                id = getApplicationContext().getResources().getIdentifier("brief28", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body28);
            } else if (stepChosen == 29) {
                id = getApplicationContext().getResources().getIdentifier("brief29", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body29);
            } else if (stepChosen == 30) {
                id = getApplicationContext().getResources().getIdentifier("brief31", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body30);
            } else if (stepChosen == 31) {
                id = getApplicationContext().getResources().getIdentifier("brief31", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body31);
            } else if (stepChosen == 32) {
                id = getApplicationContext().getResources().getIdentifier("brief32", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body32);
            } else if (stepChosen == 33) {
                id = getApplicationContext().getResources().getIdentifier("brief33", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body33);
            } else if (stepChosen == 34) {
                id = getApplicationContext().getResources().getIdentifier("brief34", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body34);
            } else if (stepChosen == 35) {
                id = getApplicationContext().getResources().getIdentifier("brief35", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body35);
            } else if (stepChosen == 36) {
                id = getApplicationContext().getResources().getIdentifier("brief36", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body36);
            } else if (stepChosen == 37) {
                id = getApplicationContext().getResources().getIdentifier("brief37", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body37);
            } else if (stepChosen == 38) {
                id = getApplicationContext().getResources().getIdentifier("brief38", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body38);
            } else if (stepChosen == 39) {
                id = getApplicationContext().getResources().getIdentifier("brief39", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body39);
            }
        }
        if(length=="long") {
            if (stepChosen == 1) {
                id = getApplicationContext().getResources().getIdentifier("detail1", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body1);
            } else if (stepChosen == 2) {
                id = getApplicationContext().getResources().getIdentifier("detail2", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body2);
            } else if (stepChosen == 3) {
                id = getApplicationContext().getResources().getIdentifier("detail3", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body3);
            } else if (stepChosen == 4) {
                id = getApplicationContext().getResources().getIdentifier("detail4", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body4);
            } else if (stepChosen == 5) {
                id = getApplicationContext().getResources().getIdentifier("detail5", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body5);
            } else if (stepChosen == 6) {
                id = getApplicationContext().getResources().getIdentifier("detail6", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body6);
            } else if (stepChosen == 7) {
                id = getApplicationContext().getResources().getIdentifier("detail7", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body7);
            } else if (stepChosen == 8) {
                id = getApplicationContext().getResources().getIdentifier("detail8", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body8);
            } else if (stepChosen == 9) {
                id = getApplicationContext().getResources().getIdentifier("detail9", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body8);
            } else if (stepChosen == 10) {
                id = getApplicationContext().getResources().getIdentifier("detail10", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body10);
            } else if (stepChosen == 11) {
                id = getApplicationContext().getResources().getIdentifier("detail11", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body11);
            } else if (stepChosen == 12) {
                id = getApplicationContext().getResources().getIdentifier("detail12", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body12);
            } else if (stepChosen == 13) {
                id = getApplicationContext().getResources().getIdentifier("detail13", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body13);
            } else if (stepChosen == 14) {
                id = getApplicationContext().getResources().getIdentifier("detail14", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body14);
            } else if (stepChosen == 15) {
                id = getApplicationContext().getResources().getIdentifier("detail15", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body15);
            } else if (stepChosen == 16) {
                id = getApplicationContext().getResources().getIdentifier("detail16", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body16);
            } else if (stepChosen == 17) {
                id = getApplicationContext().getResources().getIdentifier("detail17", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body17);
            } else if (stepChosen == 18) {
                id = getApplicationContext().getResources().getIdentifier("detail18", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body18);
            } else if (stepChosen == 19) {
                id = getApplicationContext().getResources().getIdentifier("detail19", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body19);
            } else if (stepChosen == 20) {
                id = getApplicationContext().getResources().getIdentifier("detail20", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body20);
            } else if (stepChosen == 21) {
                id = getApplicationContext().getResources().getIdentifier("detail21", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body21);
            } else if (stepChosen == 22) {
                id = getApplicationContext().getResources().getIdentifier("detail22", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body22);
            } else if (stepChosen == 23) {
                id = getApplicationContext().getResources().getIdentifier("detail23", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body23);
            } else if (stepChosen == 24) {
                id = getApplicationContext().getResources().getIdentifier("detail24", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body24);
            } else if (stepChosen == 25) {
                id = getApplicationContext().getResources().getIdentifier("detail25", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body25);
            } else if (stepChosen == 26) {
                id = getApplicationContext().getResources().getIdentifier("detail26", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body26);
            } else if (stepChosen == 27) {
                id = getApplicationContext().getResources().getIdentifier("detail27", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body27);
            } else if (stepChosen == 28) {
                id = getApplicationContext().getResources().getIdentifier("detail28", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body28);
            } else if (stepChosen == 29) {
                id = getApplicationContext().getResources().getIdentifier("detail29", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body29);
            } else if (stepChosen == 30) {
                id = getApplicationContext().getResources().getIdentifier("detail31", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body30);
            } else if (stepChosen == 31) {
                id = getApplicationContext().getResources().getIdentifier("detail31", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body31);
            } else if (stepChosen == 32) {
                id = getApplicationContext().getResources().getIdentifier("detail32", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body32);
            } else if (stepChosen == 33) {
                id = getApplicationContext().getResources().getIdentifier("detail33", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body33);
            } else if (stepChosen == 34) {
                id = getApplicationContext().getResources().getIdentifier("detail34", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body34);
            } else if (stepChosen == 35) {
                id = getApplicationContext().getResources().getIdentifier("detail35", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body35);
            } else if (stepChosen == 36) {
                id = getApplicationContext().getResources().getIdentifier("detail36", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body36);
            } else if (stepChosen == 37) {
                id = getApplicationContext().getResources().getIdentifier("detail37", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body37);
            } else if (stepChosen == 38) {
                id = getApplicationContext().getResources().getIdentifier("detail38", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body38);
            } else if (stepChosen == 39) {
                id = getApplicationContext().getResources().getIdentifier("detail39", "raw", getApplicationContext().getPackageName());
                iv.setImageResource(R.drawable.body39);
            }
        }
        
        //error checking
        if (id==-100000){
            Toast.makeText(getApplicationContext(),"ERROR: Can't play sound file",
                    Toast.LENGTH_LONG).show();
            Log.e("Music play error","couldn't play sound file");
        }
        return id;
    }

    public void onDestroy() {
        if(mPlayer!=null) {
            mPlayer.stop();
        }
        super.onDestroy();
    }
}
