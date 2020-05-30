package com.example.hp.mygana;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class Flight extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_AIRPLANE_MODE_CHANGED))
        {
          boolean isAirplaneModeON=intent.getBooleanExtra("state",false);
          if(isAirplaneModeON)
          {
              Intent i=new Intent(context,MyService.class);
              i.putExtra("option","PAUSE");
              context.startService(i);
          }
          else
          {
              Intent i=new Intent(context,MyService.class);
              i.putExtra("option","PLAY");
              context.startService(i);
          }




        }
    }
}
