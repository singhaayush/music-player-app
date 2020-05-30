package com.example.hp.mygana;

import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class MyGana extends AppCompatActivity {
    Intent i1;
    static TextView tvi;
    String load="Loading...",load2;
    static int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_my_gana);
        tvi=findViewById(R.id.textView5);
        i1 =new Intent(this,HomePage.class);
        th.start();
        while(i!=load.length())
        {   i++;


            tvi.setText(load2);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    load2=load.substring(0,i);
                    //doubleBackToExitPressedOnce = false;
                }
            }, 200);

        }

    }
    Thread th=new Thread(){
        @Override
        public void run() {
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            startActivity(i1);
            finish();
        }
    };

}
