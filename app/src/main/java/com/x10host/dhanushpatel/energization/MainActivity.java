package com.x10host.dhanushpatel.energization;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements MediaPlayer.OnErrorListener {

    ImageButton playButton;
    ImageButton previousButton;
    ImageButton nextButton;
    MediaPlayer mPlayer;
    TextView stepShow;
    TextView stepNumName;
    ImageView iv;
    SeekBar seekBarStep;
    int stepSelected = 0;
    String audioLengthGot;
    int bufferGot;
    String audioLength;
    int bufferS;
    int length;
    int lastStep = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        iv = (ImageView) findViewById(R.id.imageView);
        playButton = (ImageButton) findViewById(R.id.playButton);
        previousButton = (ImageButton) findViewById(R.id. previousButton);
        nextButton = (ImageButton) findViewById(R.id.nextButton);
        seekBarStep = (SeekBar) findViewById(R.id.seekBarStep);
        stepShow = (TextView) findViewById(R.id.stepID);
        stepNumName = (TextView) findViewById(R.id.step);
        // iv.setImageResource(R.drawable.splashscreen);

        seekBarStep.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);
        seekBarStep.getThumb().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);

        addListeners();

        if(stepSelected==0) {
            stepSelected+=2;
            seekBarStep.setProgress(stepSelected);
            stepShow.setText("Selected: " + stepSelected + " of " + seekBarStep.getMax());
            startMusic(stepSelected);
        }
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
        Log.i("audiolength use in Main", audioLength);
    }

    public void getAndSetBuffer() {
        final SharedPreferences mSharedPreference = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        bufferGot = (mSharedPreference.getInt("buffer", 0));
        bufferS = bufferGot * 1000;
        if(bufferGot!=0) {
            new CountDownTimer(bufferS, 1000) {

                public void onTick(long millisUntilFinished) {
                    //mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
                }

                public void onFinish() {
                    Log.i(bufferGot+"s buffering","done");
                    mPlayer.start();
                    mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @SuppressLint("LongLogTag")
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            Log.i("came to music complete - top1", "yes");
                            playButton.setImageResource(R.drawable.play24);

                        }
                    });
                    //mTextField.setText("done!");
                }
            }.start();
        }
        else{
            mPlayer.start();
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @SuppressLint("LongLogTag")
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Log.i("came to music complete - top2", "yes");
                    playButton.setImageResource(R.drawable.play24);

                }
            });
        }
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
            i.putExtra("lastPosition",length);
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

    public void startMusic(int step){
        setCompletedSets();
        /**
         if(step < 0){
         Toast.makeText(getApplicationContext(), "Can't seek back more.", Toast.LENGTH_SHORT).show();
         }
         else if(step > 43){
         Toast.makeText(getApplicationContext(), "Can't seek forward more.", Toast.LENGTH_SHORT).show();
         }
         **/
        getAudioLength();
        if(mPlayer==null) {
            Log.i("is mPlayer null?", (mPlayer == null) + "");
            if(audioLength.equals("none")){
                setPic(step);
                playButton.setImageResource(R.drawable.play24);
                lastStep = step;
                Log.i("Button press", "only pic display");
            }
            else{
                int id = setStepMusic(step, audioLength);
                if(id!=-100000) {
                    mPlayer = MediaPlayer.create(getApplicationContext(), id);

                    mPlayer.start();
                    //getAndSetBuffer();

                    mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            Log.i("came to music complete", " - bottom yes");
                            playButton.setImageResource(R.drawable.play24);

                        }
                    });
                    playButton.setImageResource(R.drawable.pause24);
                    lastStep = step;
                    Log.i("Button press", "music play first time");
                }
                else{
                    setPic(step);
                    lastStep = step;
                    Log.i("Button press", "only pic display");
                }
            }
        }
        else if(mPlayer!=null){
            Log.i("is mPlayer null?", (mPlayer == null) + "");
            if(audioLength.equals("none")){
                playButton.setImageResource(R.drawable.play24);
                setPic(step);
                lastStep = step;
                Log.i("Button press", "only pic display");
            }
            else {
                int id = setStepMusic(step, audioLength);
                //b = true;
                if(id!=-100000) {
                    if (lastStep!=step) {
                        mPlayer.stop();
                        mPlayer = null;
                        mPlayer = MediaPlayer.create(getApplicationContext(), id);
                        Log.i("does lastStep!=step ?", (lastStep!=step)+"");
                        playButton.setImageResource(R.drawable.pause24);
                        lastStep = step;
                        getAndSetBuffer();
                    }
                    else {
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
                                lastStep = step;
                                getAndSetBuffer();
                            }
                        } else if (!mPlayer.isPlaying()) {
                            mPlayer.seekTo(length);
                            Log.i("seek to music at", length + "");
                            playButton.setImageResource(R.drawable.pause24);
                            getAndSetBuffer();
                        }
                    }

                }
                else {
                    setPic(step);
                    mPlayer.stop();
                    mPlayer = null;
                    playButton.setImageResource(R.drawable.play24);
                }


            }
        }
    }

    public void addListeners() {

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seekBarStep.setProgress(stepSelected);
                stepShow.setText("Selected: " + stepSelected + " of " + seekBarStep.getMax());
                startMusic(stepSelected);
                Log.i("Play step index", "" + stepSelected);

            }
        });
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int stepP = stepSelected;
                if(--stepP < 1){
                    Toast.makeText(getApplicationContext(), "Can't seek back more.", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(stepP==1){
                        startActivity(new Intent(getApplicationContext(),InstructionsActivity.class));
                    }
                    else {
                        stepSelected--;
                        seekBarStep.setProgress(stepSelected);
                        stepShow.setText("Selected: " + stepSelected + " of " + seekBarStep.getMax());
                        startMusic(stepSelected);
                        Log.i("Play music step", "" + stepSelected);
                    }
                }
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int stepN = stepSelected;
                if (++stepN > 43) {
                    Toast.makeText(getApplicationContext(), "Can't seek forward more.", Toast.LENGTH_SHORT).show();
                } else {
                    stepSelected++;
                    seekBarStep.setProgress(stepSelected);
                    stepShow.setText("Selected: " + stepSelected + " of " + seekBarStep.getMax());
                    startMusic(stepSelected);
                    Log.i("Play music step", "" + stepSelected);
                }
            }
        });

        seekBarStep.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBarStep, int progressValue, boolean fromUser) {
                stepSelected = progressValue;
                seekBarStep.setProgress(stepSelected);
                if (stepSelected < 2) {
                    if (stepSelected == 0) {
                        stepSelected = ++progressValue;
                    }
                    previousButton.setVisibility(View.GONE);
                    startActivity(new Intent(getApplicationContext(), InstructionsActivity.class));
                } else {
                    previousButton.setVisibility(View.VISIBLE);
                }


                if (stepSelected == 43) {
                    nextButton.setVisibility(View.GONE);
                } else {
                    nextButton.setVisibility(View.VISIBLE);
                }

                if (stepSelected == 0 || stepSelected == 1 || stepSelected == 43) {
                    playButton.setVisibility(View.GONE);
                } else {
                    playButton.setVisibility(View.VISIBLE);
                }
                startMusic(stepSelected);
                // Toast.makeText(getApplicationContext(), "Changing seekbar's progress", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBarStep) {
                // Toast.makeText(getApplicationContext(), "Started tracking seekbar", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBarStep) {
                if (stepSelected > 43) {
                    stepSelected = 43;
                }
                stepShow.setText("Selected: " + stepSelected + " of " + seekBarStep.getMax());
                // textView.setText("Covered: " + progress + "/" + seekBarStep.getMax());
                // Toast.makeText(getApplicationContext(), "Stopped tracking seekbar", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void setPic(int stepChosen){
        //boolean showedIntro = getIntent().getBooleanExtra("showedIntro",true);
        if (stepChosen == 2) {
            //prayer
            stepNumName.setText("Begin with this prayer");
            iv.setImageResource(R.drawable.prayer);
        } else if (stepChosen == 3) {
            //first actual exercise
            iv.setImageResource(R.drawable.body1);
            stepNumName.setText("1: Double Breathing (palms touching)");
        } else if (stepChosen == 4) {
            iv.setImageResource(R.drawable.body2);
            stepNumName.setText("2: Calf Recharging");
        } else if (stepChosen == 5) {
            iv.setImageResource(R.drawable.body3);
            stepNumName.setText("3: Ankle Rotation");
        } else if (stepChosen == 6) {
            iv.setImageResource(R.drawable.body4);
            stepNumName.setText("4: Calf/Forearm, Thigh/Upper Arm");
        } else if (stepChosen == 7) {
            iv.setImageResource(R.drawable.body5);
            stepNumName.setText("5: Chest & Buttock Recharging");
        } else if (stepChosen == 8) {
            iv.setImageResource(R.drawable.body6);
            stepNumName.setText("6: Back Recharging");
        } else if (stepChosen == 9) {
            iv.setImageResource(R.drawable.body7);
            stepNumName.setText("7: Shoulder Rotation");
        } else if (stepChosen == 10) {
            iv.setImageResource(R.drawable.body8);
            stepNumName.setText("8: Throat Recharging");
        } else if (stepChosen == 11) {
            iv.setImageResource(R.drawable.body9);
            stepNumName.setText("9: Neck Recharging");
        } else if (stepChosen == 12) {
            iv.setImageResource(R.drawable.body10);
            stepNumName.setText("10: Neck Rotation");
        } else if (stepChosen == 13) {
            iv.setImageResource(R.drawable.body11);
            stepNumName.setText("11: Spinal Recharging");
        } else if (stepChosen == 14) {
            iv.setImageResource(R.drawable.body12);
            stepNumName.setText("12: Spinal Rotation");
        } else if (stepChosen == 15) {
            iv.setImageResource(R.drawable.body13);
            stepNumName.setText("13: Spinal Stretching");
        } else if (stepChosen == 16) {
            iv.setImageResource(R.drawable.body14);
            stepNumName.setText("14: Spinal Adjustment");
        } else if (stepChosen == 17) {
            iv.setImageResource(R.drawable.body15);
            stepNumName.setText("15: Upper Spinal Twisting");
        } else if (stepChosen == 18) {
            iv.setImageResource(R.drawable.body16);
            stepNumName.setText("16: Skull Tapping");
        } else if (stepChosen == 19) {
            iv.setImageResource(R.drawable.body17);
            stepNumName.setText("17: Scalp Massage");
        } else if (stepChosen == 20) {
            iv.setImageResource(R.drawable.body18);
            stepNumName.setText("18: Medulla Massage");
        } else if (stepChosen == 21) {
            iv.setImageResource(R.drawable.body19);
            stepNumName.setText("19: Biceps Recharging");
        } else if (stepChosen == 22) {
            iv.setImageResource(R.drawable.body20);
            stepNumName.setText("20: 20-Part Body Recharging");
        } else if (stepChosen == 23) {
            iv.setImageResource(R.drawable.body21);
            stepNumName.setText("21: Lifting Weights in Front");
        } else if (stepChosen == 24) {
            iv.setImageResource(R.drawable.body22);
            stepNumName.setText("22: Double Breathing (elbows touching)");
        } else if (stepChosen == 25) {
            iv.setImageResource(R.drawable.body23);
            stepNumName.setText("23: Weight Pulling (from the side)");
        } else if (stepChosen == 26) {
            iv.setImageResource(R.drawable.body24);
            stepNumName.setText("24: Arm Rotation (in small circles)");
        } else if (stepChosen == 27) {
            iv.setImageResource(R.drawable.body25);
            stepNumName.setText("25: Weight Pulling (from the front)");
        } else if (stepChosen == 28) {
            iv.setImageResource(R.drawable.body26);
            stepNumName.setText("26: Finger Recharging");
        } else if (stepChosen == 29) {
            iv.setImageResource(R.drawable.body27);
            stepNumName.setText("27: 4-Part Arm Recharging (with double breathing)");
        } else if (stepChosen == 30) {
            iv.setImageResource(R.drawable.body28);
            stepNumName.setText("28: Single Arm Raising");
        } else if (stepChosen == 31) {
            iv.setImageResource(R.drawable.body29);
            stepNumName.setText("29: Stretching side to side");
        } else if (stepChosen == 32) {
            iv.setImageResource(R.drawable.body30);
            stepNumName.setText("30: Walking in Place");
        } else if (stepChosen == 33) {
            iv.setImageResource(R.drawable.body31);
            stepNumName.setText("31: Running in Place");
        } else if (stepChosen == 34) {
            iv.setImageResource(R.drawable.body32);
            stepNumName.setText("32: Fencing");
        } else if (stepChosen == 35) {
            iv.setImageResource(R.drawable.body33);
            stepNumName.setText("33: Arm Rotation (in large circles)");
        } else if (stepChosen == 36) {
            iv.setImageResource(R.drawable.body34);
            stepNumName.setText("34: Stomach Exercise");
        } else if (stepChosen == 37) {
            iv.setImageResource(R.drawable.body35);
            stepNumName.setText("35: Double Breathing (palms touching)");
        } else if (stepChosen == 38) {
            iv.setImageResource(R.drawable.body36);
            stepNumName.setText("36: Calf Recharging");
        } else if (stepChosen == 39) {
            iv.setImageResource(R.drawable.body37);
            stepNumName.setText("37: Ankle Rotation");
        } else if (stepChosen == 40) {
            iv.setImageResource(R.drawable.body38);
            stepNumName.setText("38: Hip Recharging");
        } else if (stepChosen == 41) {
            iv.setImageResource(R.drawable.body39);
            stepNumName.setText("39: Double Breathing (without tension)");
        }
        //--- new
        else if (stepChosen == 42) {
            //aum
            iv.setImageResource(R.drawable.aum);
            stepNumName.setText("Namaste"+"\nNumber of sets complete: "+getCompletedSets());
        }
        else if (stepChosen == 43) {
            //founder & quote
            stepNumName.setText("");
            iv.setImageResource(R.drawable.founder);
        }
        //--- new ended
    }

    public int setStepMusic(int stepChosen,String length){
        int id = -100000;
        if(length=="short") {

            if (stepChosen == 2) {
                id = getApplicationContext().getResources().getIdentifier("prayer", "raw", getApplicationContext().getPackageName());
                setPic(stepChosen); //prayer
            }
            //--- new ended
            else if(stepChosen>2 && stepChosen < 42){
                String sound = "brief"+(stepChosen-2);
                Log.i("Simplified code works",sound);
                id = getApplicationContext().getResources().getIdentifier(sound, "raw", getApplicationContext().getPackageName());
                setPic(stepChosen);
            }
            //--- new
            else if (stepChosen == 42) {
                id = getApplicationContext().getResources().getIdentifier("aumamen", "raw", getApplicationContext().getPackageName());
                setPic(42);
            }
            else{
                setPic(43);//no sound
            }
            //--- new ended
        }
        if(length=="long") {
            if (stepChosen == 2) {
                id = getApplicationContext().getResources().getIdentifier("prayer", "raw", getApplicationContext().getPackageName());
                setPic(stepChosen); //prayer
            }
            //--- new ended
            else if(stepChosen>2 && stepChosen < 42){
                String sound = "detail"+(stepChosen-2);
                id = getApplicationContext().getResources().getIdentifier(sound, "raw", getApplicationContext().getPackageName());
                setPic(stepChosen);
            }
            //--- new
            else if (stepChosen == 42) {
                id = getApplicationContext().getResources().getIdentifier("aumamen", "raw", getApplicationContext().getPackageName());
                setPic(42);
            }
            else{
                setPic(43);//no sound, founder
            }
            //--- new ended
        }

        //error checking
        if (id==-100000 && stepChosen!=0 && stepChosen!=1 && stepChosen!=43){
            Toast.makeText(getApplicationContext(),"ERROR: Can't play sound file",
                    Toast.LENGTH_LONG).show();
            Log.e("Music play error", "couldn't play sound file");
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
        editor.putInt("step", stepSelected);
        editor.commit();
        //will be executed onPause
        Log.i("went to onPause","");
        super.onPause();
    }

    public void setCompletedSets(){
        int currentSteps = getCompletedSteps();
        if(currentSteps<40){
            SharedPreferences sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            currentSteps++;
            editor.putInt("currentSteps", currentSteps);
            editor.commit();
        }
        else if(currentSteps==40){
            SharedPreferences sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            int currentSets = getCompletedSets();
            currentSets++;
            editor.putInt("currentSets", currentSets);
            editor.putInt("currentSteps",0);
            editor.commit();
        }
    }

    public int getCompletedSets(){
        SharedPreferences sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
        return sp.getInt("currentSets",0);
    }
    public int getCompletedSteps(){
        SharedPreferences sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
        return sp.getInt("currentSteps",0);
    }

    @Override
    public void onResume(){
        SharedPreferences sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
        stepSelected = sp.getInt("step",0);
        seekBarStep.setProgress(stepSelected);
        stepShow.setText("Selected: " + stepSelected + " / " + seekBarStep.getMax());
        startMusic(stepSelected);
        Log.i("Resumed play step index", "" + stepSelected);
        //will be executed onResume
        super.onResume();
    }

}