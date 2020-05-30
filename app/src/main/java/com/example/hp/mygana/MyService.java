package com.example.hp.mygana;

import static com.example.hp.mygana.GanaPlayer.*;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import androidx.annotation.Nullable;

import android.widget.Toast;

import static com.example.hp.mygana.MainList.*;
import static com.example.hp.mygana.HomePage.*;

public class MyService extends Service implements MediaPlayer.OnCompletionListener {
    PendingIntent pi2;

    Intent inn2;

    public MyService() {
    }

    @Override
    public void onCompletion(MediaPlayer mp) {


        playNextMusic();


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        intentfix = intent;

        String o = null;
        db = openOrCreateDatabase("MyData", MODE_PRIVATE, null);
        try {


            if (intent != null) {
                o = intent.getStringExtra("option");
            }


            switch (o) {
                case "PLAY":
                    playMusic(fix);
                    break;
                case "PAUSE":
                    pauseMusic();
                    break;
                case "STOP":
                    stopMusic();
                    break;
                case "PREV":
                    playPrevMusic();
                    break;
                case "NEXT":
                    playNextMusic();
                    break;

            }
        } catch (Exception e) {

        }


        return super.onStartCommand(intent, flags, startId);


    }

    public void playMusic(int song) {
        getSongImage();
        notificationPlayPause = "PAUSE";
        Intent send = new Intent(this, MyService.class);
        send.putExtra("option", "PAUSE");
        PendingIntent psend = PendingIntent.getService(this, 2, send, 0);


        if (db != null) {
            try {
                db.execSQL("insert into RECENT4 Values('" + Integer.toString(fix) + "','" + listtoShow.get(song).toString() + "')");
                Cursor c1 = db.rawQuery("select *from RECENT4", null);
            } catch (Exception c) {

            }

        }

        if (mp == null) {
            mp = MediaPlayer.create(this, Uri.parse(pathtoPlaycopy.get(song).toString()));

            mp.setOnCompletionListener(this);
            mp.start();
            sb.setMax(mp.getDuration());
        } else if (!mp.isPlaying()) {
            mp.start();
        }
        myNotify(notificationPlayPause, psend);
    }

    public void pauseMusic() {
        if (mp != null && mp.isPlaying()) {
            mp.pause();

        }

        Intent send = new Intent(this, MyService.class);
        send.putExtra("option", "PLAY");
        PendingIntent psend = PendingIntent.getService(this, 5, send, 0);
        notificationPlayPause = "PLAY";
        myNotify(notificationPlayPause, psend);


    }


    public void stopMusic() {

        if (mp != null) {
            mp.stop();
            mp = null;
        }
    }

    public void playNextMusic() {

        if (mp != null) {
            mp.stop();
            mp = null;
        }
        fix++;
        if (ad1.getCount() == fix) {
            fix = 0;
        }
        ad1.getCount();
        try {
            playMusic(fix);
        } catch (ArrayIndexOutOfBoundsException e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            fix = 0;
            playNextMusic();
        }

    }


    public void playPrevMusic() {
        getSongImage();
        if (mp != null) {
            mp.stop();
            mp = null;
            if (fix == 0) {
                fix = songPath.size() - 1;
            } else {
                fix--;
            }

            try {
                playMusic(fix);
            } catch (ArrayIndexOutOfBoundsException e) {
                fix = 0;
                playPrevMusic();
            }

        }


    }

    void getSongImage() {
        android.media.MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(pathtoPlay.get(fix).toString());
        byte[] data = mmr.getEmbeddedPicture();
        //coverart is an Imageview object

        // convert the byte array to a bitmap


        if (data != null) {

            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            iv.setImageBitmap(bitmap);

        } else {

            iv.setImageResource(R.drawable.musico);

        }

    }

    void myNotify(String pp, PendingIntent pendingIntent) {
        int mid = 0;
        if (pp.equals("PAUSE")) {
            mid = android.R.drawable.ic_media_pause;
        } else if (pp.equals("PLAY")) {
            mid = android.R.drawable.ic_media_play;
        }


        PendingIntent notipi;


        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nb = new Notification.Builder(this);
        nb.setSmallIcon(R.drawable.logo);

        android.media.MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(pathtoPlaycopy.get(fix).toString());
        byte[] data = mmr.getEmbeddedPicture();
        //coverart is an Imageview object

        // convert the byte array to a bitmap


        if (data != null) {

            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            nb.setStyle(new Notification.BigPictureStyle().bigPicture(bitmap));

        } else {


        }

        nb.setContentTitle("MyGana");
        nb.setContentText(listtoShowcopy.get(fix).toString());
        Intent inn = new Intent(this, MyGana.class);
        PendingIntent p = PendingIntent.getActivity(this, 1, inn, 0);


        nb.setAutoCancel(true);
        nb.setContentIntent(p);
        nb.setOngoing(true);
        // nb.setDefaults(Notification.DEFAULT_ALL);

        Intent inn1 = new Intent(this, MyService.class);
        inn1.putExtra("option", "PREV");
        PendingIntent pi1 = PendingIntent.getService(this, 1, inn1, 0);


        Intent inn3 = new Intent(this, MyService.class);
        inn3.putExtra("option", "NEXT");
        PendingIntent pi3 = PendingIntent.getService(this, 3, inn3, 0);

        nb.addAction(android.R.drawable.ic_media_previous, "prev", pi1);
        nb.addAction(mid, pp, pendingIntent);
        nb.addAction(android.R.drawable.ic_media_next, "next", pi3);


        n = nb.build();
        nm.notify(1, n);


    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
