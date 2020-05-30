package com.example.hp.mygana;
import static com.example.hp.mygana.HomePage.*;

import android.content.Intent;
import android.database.Cursor;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

public class Filter extends AppCompatActivity {
    static  boolean filterCheck;
    ListView filter;
    @Override
    protected void onPause() {
        super.onPause();
     //  filter.setAdapter(null);

    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Switch f=findViewById(R.id.switch2);
        AutoCompleteTextView afilter=findViewById(R.id.autoilter);

        filter = findViewById(R.id.filterlist);
        ArrayAdapter show=new ArrayAdapter(this,android.R.layout.simple_expandable_list_item_1,listtoShow);
        filter.setAdapter(show);
        afilter.setAdapter(show);
        afilter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {




                if(filterCheck) {
                    String source = getIntent().getStringExtra("source");
                    String broader;
                    Toast.makeText(Filter.this, source, Toast.LENGTH_LONG).show();
                    Cursor broad;
                    if (source.equals("album")) {
                        broad = mdb.rawQuery("select title,path,album from ParentTable", null);
                    } else {

                        broad = mdb.rawQuery("select title,path,artist from ParentTable", null);
                    }


                    broader = parent.getItemAtPosition(position).toString();
                    String comp = broader.split(" ")[0];
                    if (!listtoShow.isEmpty()) {
                        listtoShow.clear();
                        pathtoPlay.clear();
                    }
                    while (broad.moveToNext()) {
                        String x = broad.getString(2);
                        if (x.contains(comp)) {
                            listtoShow.add(broad.getString(0));
                            pathtoPlay.add(broad.getString(1));
                        }
                    }
                }
                else
                {
                    String source=getIntent().getStringExtra("source");
                    Toast.makeText(Filter.this, source, Toast.LENGTH_LONG).show();
                    Cursor local;
                    if(source.equals("album"))
                    { local= mdb.rawQuery("select title,path from ParentTable where album='"+parent.getItemAtPosition(position).toString()+"'",null);}
                    else {
                        local= mdb.rawQuery("select title,path from ParentTable where artist='"+parent.getItemAtPosition(position).toString()+"'",null);}

                    if(local==null)
                    {
                        Toast.makeText(Filter.this, "it is null", Toast.LENGTH_SHORT).show();
                    }
                    if(!listtoShow.isEmpty())
                    { listtoShow.clear();
                        pathtoPlay.clear();}
                    while (local.moveToNext())
                    {

                        listtoShow.add(local.getString(0));
                        pathtoPlay.add(local.getString(1));
                    }


                }



                Intent intent=new Intent(Filter.this,MainList.class);
                startActivity(intent);
                finish();
            }


        });
        f.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                filterCheck=isChecked;
            }
        });
        filter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(filterCheck) {
                    String source = getIntent().getStringExtra("source");
                    String broader;
                    Toast.makeText(Filter.this, source, Toast.LENGTH_LONG).show();
                    Cursor broad;
                    if (source.equals("album")) {
                        broad = mdb.rawQuery("select title,path,album from ParentTable", null);
                    } else {

                        broad = mdb.rawQuery("select title,path,artist from ParentTable", null);
                    }


                    broader = parent.getItemAtPosition(position).toString();
                    String comp = broader.split(" ")[0];
                    if (!listtoShow.isEmpty()) {
                        listtoShow.clear();
                        pathtoPlay.clear();
                    }
                    while (broad.moveToNext()) {
                        String x = broad.getString(2);
                        if (x.contains(comp)) {
                            listtoShow.add(broad.getString(0));
                            pathtoPlay.add(broad.getString(1));
                        }
                    }
                }
                else
                {
                    String source=getIntent().getStringExtra("source");
                    Toast.makeText(Filter.this, source, Toast.LENGTH_LONG).show();
                    Cursor local;
                    if(source.equals("album"))
                    { local= mdb.rawQuery("select title,path from ParentTable where album='"+parent.getItemAtPosition(position).toString()+"'",null);}
                    else {
                        local= mdb.rawQuery("select title,path from ParentTable where artist='"+parent.getItemAtPosition(position).toString()+"'",null);}

                    if(local==null)
                    {
                        Toast.makeText(Filter.this, "it is null", Toast.LENGTH_SHORT).show();
                    }
                    if(!listtoShow.isEmpty())
                    { listtoShow.clear();
                        pathtoPlay.clear();}
                    while (local.moveToNext())
                    {

                        listtoShow.add(local.getString(0));
                        pathtoPlay.add(local.getString(1));
                    }


                }



                Intent intent=new Intent(Filter.this,MainList.class);
                startActivity(intent);
                finish();
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       // filter.setAdapter(null);
    }
}
