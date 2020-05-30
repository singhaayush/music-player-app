package com.example.hp.mygana;
import  static com.example.hp.mygana.MainList.*;
import  static com.example.hp.mygana.GanaPlayer.*;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import static com.example.hp.mygana.HomePage.*;

public class RecentSongs extends AppCompatActivity {
    public static int originalpositionr;
    static String[] rec;
    ArrayAdapter recent;
    static int mark = 0;
    static int[] positions;
    static ArrayAdapter eg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String []example={"delete","example","thanks"};
         eg=new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,example);
        super.onCreate(savedInstanceState);
        Cursor c = db.rawQuery("select *from RECENT4", null);

        positions = new int[c.getCount()];
        setContentView(R.layout.activity_recent_songs);
        db = openOrCreateDatabase("MyData", MODE_PRIVATE, null);

        int fl = 0;

        rec = new String[c.getCount()];

        while (c.moveToNext()) {
            rec[fl] = c.getString(1);
            positions[fl] = Integer.parseInt(c.getString(0));
            fl++;
        }

        recent = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, rec);
        ListView rList = findViewById(R.id.recentlist);
        CustomAdapter cusAda=new CustomAdapter();
        rList.setAdapter(cusAda);
        if (rList.getCount() > 5) {
            db.execSQL("delete  from RECENT4");
        }

        rList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i3;
                i3 = new Intent(RecentSongs.this, MyService.class);
                originalpositionr = position;
                if (mp != null) {
                    if (mp.isPlaying()) {


                        i3.putExtra("option", "STOP");
                        startService(i3);
                    }
                }
                fix = positions[position];
                mark = position;
                Intent j = new Intent(RecentSongs.this, GanaPlayer.class);
                j.putExtra("mode", "recent");
                startActivity(j);


            }
        });
    }
 public    class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {

            return recent.getCount();
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
           // img.setImageResource(R.drawable.ic_launcher_background);



            texc.setText(rec[position]);
            int place=positions[position];
            try {

                android.media.MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                mmr.setDataSource(pathtoPlay.get(place).toString());
                byte[] data = mmr.getEmbeddedPicture();
                //coverart is an Imageview object

                // convert the byte array to a bitmap


                if (data != null) {

                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
              //      img.setImageBitmap(bitmap);

                } else {

                 //   img.setImageResource(R.drawable.musico);

                }
            }catch (Exception e) {
               // img.setImageResource(R.drawable.musico);

            }return convertView;
        }
    }

}
