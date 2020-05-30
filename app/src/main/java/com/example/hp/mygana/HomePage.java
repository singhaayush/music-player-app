package com.example.hp.mygana;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class HomePage extends AppCompatActivity {
    static public ArrayList songAlbums = new ArrayList();
    static public ArrayList songArtists = new ArrayList();
    static public ArrayList songPath = new ArrayList();
    static public ArrayList title = new ArrayList();
    static public SQLiteDatabase mdb;
    static public ArrayList listtoShow=new ArrayList();
    static public ArrayList pathtoPlay=new ArrayList();
    static public int []positions;
       static String notificationPlayPause;
       private DrawerLayout d1;
       private ActionBarDrawerToggle abdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home_page);
        d1=findViewById(R.id.mynavslide);
        abdt=new ActionBarDrawerToggle(this,d1,R.string.open,R.string.close);
        abdt.setDrawerIndicatorEnabled(true);
        d1.addDrawerListener(abdt);
        abdt.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView=findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return true;
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return abdt.onOptionsItemSelected(item)||super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        listtoShow.clear();

    }
    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        AlertDialog.Builder adb;
        adb=new AlertDialog.Builder(this);
        adb.setTitle("exit");
        adb.setIcon(R.drawable.icon);
        adb.setCancelable(false);
        adb.setMessage("Are you sure you want to exit ?");
        adb.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                doubleBackToExitPressedOnce=true;
                Intent i1=new Intent(HomePage.this,MyService.class);
                i1.putExtra("option","STOP");
                startService(i1);

                stopService(i1);
                finish();
            }
        });
        adb.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(HomePage.this, "NO", Toast.LENGTH_SHORT).show();
            }
        });
        adb.show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    protected void onResume() {
        super.onResume();

        int i=0;

        mdb=openOrCreateDatabase("MainData",MODE_PRIVATE,null);
        try {mdb.execSQL("delete from ParentTable");
            mdb.execSQL("create table ParentTable (position INT(4)primary key,title varchar(50),artist varchar(50),album varchar(50),path varchar(50))");
        }
        catch (Exception e)
        {

        }


        ContentResolver contentResolver = getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(songUri, null, null, null, null);

        if (songCursor != null && songCursor.moveToFirst()) {
            int songId = songCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int path = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            int album = songCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            int artist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);


            do {


                long currentId = songCursor.getLong(songId);
                String currentTitle = songCursor.getString(songTitle);
                String currentArtist = songCursor.getString(artist);
                String filePath = songCursor.getString(path);
                String currentAlbum = songCursor.getString(album);
                try
                { mdb.execSQL("insert into ParentTable values('"+i+"','"+currentTitle+"','"+currentArtist+"','"+currentAlbum+"','"+filePath+"')");}
                catch (Exception e)
                {

                }
                i++;
                songArtists.add(currentArtist);
                songAlbums.add(currentAlbum);
                title.add(currentTitle);
                songPath.add(filePath);
            } while (songCursor.moveToNext());

            positions=new int[i];












            CardView cd1 = findViewById(R.id.card1);
            CardView cd2 = findViewById(R.id.card2);
            CardView cd3 = findViewById(R.id.card3);
            CardView cd4 = findViewById(R.id.card4);
            cd1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Cursor c=mdb.rawQuery("select distinct artist from ParentTable order by artist asc ",null);
                    while (c.moveToNext())
                    {
                        listtoShow.add(c.getString(0));
                    }
                    Intent i=new Intent(HomePage.this,Filter.class);
                    i.putExtra("source","artist");
                    startActivity(i);
                }
            });
            cd2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Cursor c=mdb.rawQuery("select distinct album from ParentTable order by album asc ",null);
                    while (c.moveToNext())
                    {
                        listtoShow.add(c.getString(0));
                    }
                    Intent i=new Intent(HomePage.this,Filter.class);
                    i.putExtra("source","album");
                    startActivity(i);

                }
            });
            cd3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int flag=0;
                    Cursor c=mdb.rawQuery("select title,position,path from ParentTable order by title asc ",null);
                    while (c.moveToNext())
                    {
                        listtoShow.add(c.getString(0));
                        positions[flag]=c.getInt(1);
                        pathtoPlay.add(c.getString(2));
                        flag++;

                    }
                    Intent i=new Intent(HomePage.this,MainList.class);
                    startActivity(i);


                }
            });

        }


    }
}