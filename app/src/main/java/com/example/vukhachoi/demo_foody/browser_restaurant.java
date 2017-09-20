package com.example.vukhachoi.demo_foody;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import Adapter.Adapter_Res;
import Adapter.MyDatabaseAdapter;
import Model.Restaurant;

public class browser_restaurant extends AppCompatActivity {
    Toolbar toolbar1,toolbar2;
    ListView listView;
    Adapter_Res adapter_res;
    ArrayList<Restaurant>  arrayList;
    private  int REQUESTCODE=123;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser_restaurant);
        AddControl();

    }
    void AddData()
    {

        MyDatabaseAdapter myDatabase;
        SQLiteDatabase database;
        myDatabase= new MyDatabaseAdapter(this);
        myDatabase.Khoitai();
        database=myDatabase.getMyDatabase();

        Cursor cursor = database.rawQuery("select * from Restaurant", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            String name=cursor.getString(1);
            String restype=cursor.getString(7);
            String address=cursor.getString(4);
            byte[] hinhanh = cursor.getBlob(3);
            arrayList.add(new Restaurant(name,address,restype,hinhanh));
            cursor.moveToNext();
        }
        cursor.close();


    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void AddControl() {

        { Window window = this.getWindow();
            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
            window.setStatusBarColor(getResources().getColor(R.color.my_color));
//       getSupportActionBar().hide();
        }
        toolbar1 = findViewById(R.id.tool_back1);
        toolbar1.setNavigationIcon(R.drawable.navi);
        toolbar1.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                finish();
                return true;
            }
        });
        toolbar2=findViewById(R.id.tool_fav1);
        toolbar2.inflateMenu(R.menu.search);

        toolbar2.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
//                Toast.makeText(Restaurant_Details.this,"fav",Toast.LENGTH_LONG).show();
                Intent intent=new Intent(browser_restaurant.this,location_chooser.class);
                startActivityForResult(intent,REQUESTCODE);
                return true;
            }
        });

        listView=findViewById(R.id.listview);
        arrayList=new ArrayList<>();

AddData();
        adapter_res=new Adapter_Res(this,R.layout.item_restaurant,arrayList);
        listView.setAdapter(adapter_res);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(browser_restaurant.this,Restaurant_Details.class);
                intent.putExtra("id",i);

                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode==REQUESTCODE&&resultCode==RESULT_OK)
        {
            try {
                arrayList.clear();
                    ArrayList<Restaurant> arrayListResult = (ArrayList<Restaurant>) data.getSerializableExtra("arraylist");
                    for (Restaurant i : arrayListResult) {
                        arrayList.add(i);
                    adapter_res.notifyDataSetChanged();
                }
            }catch (Exception e){};
        }else {
            arrayList.clear();
            AddData();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
