package com.example.hp.mygana;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import static com.example.hp.mygana.GanaPlayer.mp;
import static com.example.hp.mygana.HomePage.*;
public class MyPlaylistSongs extends AppCompatActivity {
    static public int originalpositionp;
static ArrayList<String> playlistTitles=new ArrayList<String>();
    static ArrayList<Integer> positions=new ArrayList();
   static ArrayAdapter ada;
    static ListView pl;
    static   SQLiteDatabase pdb;
    static public int sizeOfData;
    static Cursor cp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_playlist_songs);

}

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.remove_songs,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info= (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        SQLiteDatabase pdb;
        switch (item.getItemId())
        {
            case R.id.removesongs:



                    pdb = openOrCreateDatabase("Playlist_songs",MODE_PRIVATE,null);

                 String s=playlistTitles.get(info.position);

                pdb.execSQL("delete from my_playlist1 where title='"+s+"'");
                playlistTitles.remove(info.position);
               ada.notifyDataSetChanged();



        }

                return super.onContextItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        playlistTitles.clear();
        positions.clear();
    }

    @Override
    protected void onResume() {
        super.onResume();
        pdb= openOrCreateDatabase("Playlist_songs",MODE_PRIVATE,null);
        cp=pdb.rawQuery("select * from my_playlist1" ,null);

        while (cp.moveToNext())
        {
            playlistTitles.add(cp.getString(1));
            positions.add(Integer.valueOf(cp.getInt(0)));
            listtoShow.add(cp.getString(1));

        }

        pl = findViewById(R.id.pList);
        registerForContextMenu(pl);

        ada = new ArrayAdapter(this,android.R.layout.simple_expandable_list_item_1,playlistTitles);
        pl.setAdapter(ada);


        pl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i3;
                i3 = new Intent(MyPlaylistSongs.this, MyService.class);


                if (mp != null) {
                    if (mp.isPlaying()) {


                        i3.putExtra("option", "STOP");
                        startService(i3);
                    }
                }
                cp=pdb.rawQuery("select * from my_playlist1" ,null);
                listtoShow.clear();
                pathtoPlay.clear();
                while (cp.moveToNext())
                {
                   listtoShow.add(cp.getString(1));
                   pathtoPlay.add(cp.getString(2));
                }


                Intent j = new Intent(MyPlaylistSongs.this, MainList.class);
                j.putExtra("mode", "playlist");
                startActivity(j);
                finish();

            }

        });


    }



    @Override
    protected void onPause() {
        super.onPause();
     finish();

        }

    @Override
    protected void onRestart() {
        super.onRestart();

        playlistTitles.clear();
        pl.setAdapter(null);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
