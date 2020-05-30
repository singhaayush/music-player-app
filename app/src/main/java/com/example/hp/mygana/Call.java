package com.example.hp.mygana;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class Call extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        final Intent i1;
        Bundle bundle=intent.getExtras();
        if(bundle!=null)
        {
            String state=bundle.getString(TelephonyManager.EXTRA_STATE);
            if(state.equals(TelephonyManager.EXTRA_STATE_RINGING))
            {
              //  String mn;
               // mn=bundle.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
              //  Toast.makeText(context,mn, Toast.LENGTH_SHORT).show();
                 Intent i=new Intent(context.getApplicationContext(),MyService.class);
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
