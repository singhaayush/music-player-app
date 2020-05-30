package com.example.hp.mygana;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.ButtonBarLayout;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;

import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import static com.example.hp.mygana.HomePage.*;
import java.util.ArrayList;



public class GanaPlayer extends AppCompatActivity implements SensorEventListener {
    static MediaPlayer mp;
    static ArrayList titleList = new ArrayList();
    static ArrayList songpath = new ArrayList();
    static ArrayList listtoShowcopy=new ArrayList();
    static ArrayList pathtoPlaycopy=new ArrayList();
    static TextView tv1;
    static TextView tv2;
    static ImageView iv;
    static SeekBar sb;
    static boolean play_pause_status;
    static PendingIntent p1;
    static PendingIntent p2;
    static  ToggleButton tb;
    static int fix = 0;
    static NotificationManager nm;
    static Notification.Builder nb;
    static Notification n;
    static PendingIntent pi;
    Switch sw;
    SensorManager sm;
    Sensor s;
    static int flag=0;
    RelativeLayout layout;
   static Button b1,b2;
   static int i=0;

    @Override
    protected void onStart() {
        tb=findViewById(R.id.playpause);
        tb.setText(" ");
        super.onStart();
        Intent i1 = new Intent(this, MyService.class);
        i1.putExtra("option", "PLAY");
       i1.putExtra("mode",getIntent().getStringExtra("mode"));
        startService(i1);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        listtoShowcopy= (ArrayList) listtoShow.clone();
        pathtoPlaycopy=(ArrayList) pathtoPlay.clone();


        try
        {
            sm= (SensorManager) getSystemService(SENSOR_SERVICE);
            s=sm.getDefaultSensor(Sensor.TYPE_PROXIMITY);


        }
        catch (Exception e)
        {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
        setContentView(R.layout.activity_gana_player);
        tv1 = findViewById(R.id.textView);
        tv2 = findViewById(R.id.textView2);
        sb = findViewById(R.id.seekBar);
        if(mp!=null)
        {   tv2.setText(convertor((mp.getDuration())));
            sb.setMax(mp.getDuration());
        }
        iv = findViewById(R.id.img);
        th.start();

        final Intent i1=new Intent(this,MyService.class);


        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b) {
                    if(mp!=null)
                        mp.seekTo(i);
                }
                if (mp != null) {
                    int x = mp.getCurrentPosition();
                    int full = mp.getDuration();
                    tv1.setText(convertor(x));
                    tv2.setText(convertor(full));


                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    String convertor(int y) {
        int seconds = y / 1000;
        int sec = seconds % 60;
        int minutes = seconds / 60;
        String send;
        if (sec < 10) {
            send = "0" + minutes + ":" + "0" + sec;
        } else {
            send = "0" + minutes + ":" + sec;
        }
        return send;
    }


    Thread th = new Thread() {

        @Override
        public void run() {
            while (true) {

                if (mp != null) {
                    sb.setProgress(mp.getCurrentPosition());


                } else {
                    if (sb != null)
                        sb.setProgress(0);
                }
            }
        }
    };
      void play_pause(View v)
      {     tb.setText("");


          tb = findViewById(R.id.playpause);
          if(i%2==0)
          {
              tb.setBackgroundResource(R.drawable.pause);

              Intent i1 = new Intent(this, MyService.class);
              i1.putExtra("option", "PLAY");
              startService(i1);

          }
          else
          {
              tb.setBackgroundResource(R.drawable.play);
                            Intent i1 = new Intent(this, MyService.class);
              i1.putExtra("option", "PAUSE");
              startService(i1);
          }
            i++;
      }




    public void myStop(View v) {

        Intent i1 = new Intent(this, MyService.class);
        i1.putExtra("option", "STOP");
        startService(i1);
    }


    public void myNext(View v) {

        Intent i1 = new Intent(this, MyService.class);
        i1.putExtra("option", "NEXT");
        startService(i1);
    }


    public void myPrev(View v) {

        Intent i1 = new Intent(this, MyService.class);
        i1.putExtra("option", "PREV");
        startService(i1);
    }




    @Override
    public void onSensorChanged(SensorEvent event) {


        float x[]= event.values;
        float f=x[0];
        Sensor s1= event.sensor;
        if(f==s1.getMaximumRange()){
           if(flag%2==0)
           {


               Intent i1 = new Intent(this, MyService.class);
               i1.putExtra("option", "PAUSE");
               startService(i1);
               flag++;
           }
           else{


               Intent i1 = new Intent(this, MyService.class);
               i1.putExtra("option", "PLAY");
               startService(i1);
               flag++;
           }
        }
        }



    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {


    }

    @Override
    protected void onResume() {
        super.onResume();

        sw=findViewById(R.id.switch1);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {


            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked)
                {
                    registry();
                }
                else
                {
                   deRegister();

                }


            }});


    }

    @Override
    protected void onPause() {
        super.onPause();

        sm.unregisterListener(this);

    }
     void registry()
     {
         try {
             sm.registerListener(this, s, SensorManager.SENSOR_DELAY_FASTEST);
             Toast.makeText(this, "Sensor Enabled", Toast.LENGTH_SHORT).show();
         }
         catch (Exception e)
         {
             Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
         }
     }
     void deRegister()
     {
         sm.unregisterListener(this);
         Toast.makeText(this, "Sensor Disabled", Toast.LENGTH_SHORT).show();
     }


}
