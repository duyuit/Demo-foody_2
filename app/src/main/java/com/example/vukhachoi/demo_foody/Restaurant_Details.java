package com.example.vukhachoi.demo_foody;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import Adapter.MyDatabaseAdapter;
import Model.Restaurant;

public class Restaurant_Details extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener  {
    public static GoogleMap map;
    Toolbar toolbar1,toolbar2;

    LatLng latLng;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant__details);
        createMap();
        AddStatus();
        AddData();
        // getSupportActionBar().setDisplayShowCustomEnabled(true);
        //getSupportActionBar().setCustomView(R.layout.toolbar_details);

        AddControl();

        ActivityCompat.requestPermissions(Restaurant_Details.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
    }

    String name,address;
    void AddData()
    {
        Intent intent=getIntent();

        MyDatabaseAdapter myDatabase;
        SQLiteDatabase database;
        myDatabase= new MyDatabaseAdapter(this);
        myDatabase.Khoitai();
        database=myDatabase.getMyDatabase();

        int id=intent.getIntExtra("id",0)+1;
        String query="select * from Restaurant where id='"+id+"'";

        Cursor cursor = database.rawQuery(query,null);
        cursor.moveToFirst();
         name=cursor.getString(1);
        String restype=cursor.getString(7);
         address=cursor.getString(4);
        String description=cursor.getString(10);
        latLng=new LatLng(cursor.getDouble(11),cursor.getDouble(12));
        float posi=cursor.getFloat(9);
        byte[] hinhanh = cursor.getBlob(3);
        //new Restaurant(name,address,restype,hinhanh);
        ImageView imageView=findViewById(R.id.image_res);
        Bitmap bitmap1 = BitmapFactory.decodeByteArray(hinhanh, 0, hinhanh.length);
        imageView.setImageBitmap(bitmap1);

        TextView txtname=findViewById(R.id.txtTitle_detail);
        txtname.setText(name.toString());

        TextView txtrestype=findViewById(R.id.txtrestype);
        txtrestype.setText(restype.toString());

        TextView txtAddress=findViewById(R.id.txtAddress);
        txtAddress.setText(address.toString());

        RatingBar ratingBar=findViewById(R.id.ratingBar);
        ratingBar.setRating(posi);

        TextView descrip=findViewById(R.id.textView2);
        descrip.setText(description);


    }
    private void AddControl() {
        toolbar1=findViewById(R.id.tool_back);
        toolbar1.inflateMenu(R.menu.back_detail);

        toolbar1.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                finish();
                return true;
            }
        });


        toolbar2=findViewById(R.id.tool_fav);
        toolbar2.inflateMenu(R.menu.favourites_details);

        toolbar2.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(Restaurant_Details.this,"fav",Toast.LENGTH_LONG).show();
                return true;
            }
        });
        Button button=findViewById(R.id.btnRoute);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Restaurant_Details.this,Show_Routes.class);
                intent.putExtra("title",name);
                intent.putExtra("address",address);
                intent.putExtra("lati",latLng.latitude);
                intent.putExtra("longti",latLng.longitude);
                startActivity(intent);
            }
        });

    }

    private void createMap() {
        SupportMapFragment smf = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        smf.getMapAsync(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        createMap();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map=googleMap;
        map.addMarker(new MarkerOptions()
                .title(name)
                .snippet(address)
                .position(latLng));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private GoogleApiClient client;

    private LocationRequest locationRequest;

    private android.location.Location lastlocation;

    private Marker currentLocationmMarker;

    protected synchronized void bulidGoogleApiClient() {

        client = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        client.connect();


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void AddStatus()
    { Window window = this.getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(getResources().getColor(R.color.my_color));
//       getSupportActionBar().hide();
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
