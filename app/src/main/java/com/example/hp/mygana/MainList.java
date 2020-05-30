package com.example.hp.mygana;
import static com.example.hp.mygana.GanaPlayer.*;

import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import static com.example.hp.mygana.RecentSongs.*;

import java.util.ArrayList;
import static com.example.hp.mygana.HomePage.*;

public class MainList extends AppCompatActivity {

    public static int originalPosition;
    static Intent intentfix;
    static ListView mainList;
    static ArrayList title = new ArrayList();
    static ArrayList title1 = new ArrayList();
    static ArrayList albums = new ArrayList();
    static ArrayAdapter ad1;
    static ArrayList Path = new ArrayList();
    Intent i3;
    String[] dummy= new String[]{"ayush"};
   static SQLiteDatabase db;
    Intent jump;
    static int place;
    PendingIntent pi2 = null;

     static String[] arrange;


    @Override
    protected void onStart() {
        super.onStart();
        ListView list_view=findViewById(R.id.li);
        registerForContextMenu(list_view);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        jump=new Intent(this,RecentSongs.class);
        db=openOrCreateDatabase("MyData",MODE_PRIVATE,null);
        try
        {
     db.execSQL("create table RECENT4(position varchar(4)unique ,title varchar(30))");}
     catch (Exception e)
     {

     }
        i3 = new Intent(this, MyService.class);
        setContentView(R.layout.activity_main_list);

        AutoCompleteTextView ac=findViewById(R.id.automatic);
           ad1 = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1,listtoShow);
         ac.setAdapter(ad1);

        mainList = findViewById(R.id.li);
        CustomAdapter customAdapter=new CustomAdapter();
        mainList.setAdapter(customAdapter);
        final Intent i1 = new Intent(this, GanaPlayer.class);



        mainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {




                if (mp != null) {
                    mark=position;
                    if (mp.isPlaying()) {


                        i3.putExtra("option", "STOP");
                        startService(i3);
                    }}
               String s=ad1.getItem(position).toString();

               Cursor c= mdb.rawQuery("select position from ParentTable where title='"+s+"'",null);
               while(c.moveToNext())
               {

               }
                   fix=position;
                    i3.putExtra("option","PLAY");
                    startService(i3);



                i1.putExtra("mode","main");

                startActivity(i1);




            }
        });

        ac.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id)
            {



                if (mp != null) {
                    mark=position;
                    if (mp.isPlaying()) {


                        i3.putExtra("option", "STOP");
                        startService(i3);
                    }}
                String s=parent.getItemAtPosition(position).toString();
                Toast.makeText(MainList.this,s , Toast.LENGTH_SHORT).show();
                Cursor c= mdb.rawQuery("select position from ParentTable where title='"+s+"'",null);
                int i=0;
                while(!s.equals(listtoShow.get(i)))
                {
                  i++;
                }
                fix=i;
                Toast.makeText(MainList.this, Integer.toString(fix), Toast.LENGTH_SHORT).show();
                i3.putExtra("option","PLAY");
                startService(i3);



                i1.putExtra("mode","main");

                startActivity(i1);



            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
      /*  mp.stop();
        Toast.makeText(this, "restart", Toast.LENGTH_SHORT).show();
        Intent i1 = new Intent(this, MyService.class);
        i1.putExtra("option", "STOP");
        startService(i1);
        mp=null;
*/
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gana_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        if(item.getItemId()==R.id.rp)
        startActivity(jump);
        else if(item.getItemId()==R.id.mp)
        {
            Intent i1=new Intent(this,MyPlaylistSongs.class);
            startActivity(i1);
        }
        return super.onOptionsItemSelected(item);


    }

    @Override
    protected void onPause() {
        super.onPause();
        AutoCompleteTextView ac1=findViewById(R.id.automatic);
        ac1.setText("");
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.context,menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info= (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int posforplaylist=0;
        SQLiteDatabase pdb = null;
        Cursor playing = null;
         switch (item.getItemId())
         {

             case R.id.playList:

               try{

                  playing= mdb.rawQuery("select path,position from ParentTable where title='"+listtoShow.get(info.position)+"'",null);
                   pdb = openOrCreateDatabase("Playlist_songs",MODE_PRIVATE,null);
                  // pdb.execSQL("delete from my_playlist1");

                   pdb.execSQL("create table my_playlist1 (position int(4)unique ,title varchar(30),path varchar(150))");
               }
               catch(Exception e)
               {
                   Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();

               }
                 try {
                   String p;
                   while (playing.moveToNext())
                   {

                       pdb.execSQL("insert into my_playlist1 Values('" + playing.getInt(1) + "','" + listtoShow.get(info.position).toString() + "','"+playing.getString(0)+"')");
                       Toast.makeText(this, "inserted", Toast.LENGTH_SHORT).show();
                   }



                 }
                 catch (SQLiteConstraintException s)
                 {
                     Toast.makeText(this, "already exists", Toast.LENGTH_SHORT).show();
                 }

         }
        return super.onContextItemSelected(item);
    }

    public    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {

            return ad1.getCount();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView=getLayoutInflater().inflate(R.layout.customlayout,null);
          //  ImageView img=convertView.findViewById(R.id.customimage);
            TextView texc=convertView.findViewById(R.id.customtext);
            TextView texca=convertView.findViewById(R.id.customtextartist);
         //   img.setImageResource(R.drawable.ic_launcher_background);


           Cursor c= mdb.rawQuery("select artist from ParentTable where title='"+listtoShow.get(position).toString()+"'",null);
            while(c.moveToNext())
            {
             texca.setText("Artist-> "+c.getString(0));
            }
            texc.setText(listtoShow.get(position).toString());



            return convertView;
        }
    }
}
