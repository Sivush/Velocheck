package com.example.lasyd.velocheck;

import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Date;

import static com.example.lasyd.velocheck.BaseRides.DLATITUDE_COLUMN;
import static com.example.lasyd.velocheck.BaseRides.DLONGITUDE_COLUMN;
import static com.example.lasyd.velocheck.BaseRides.PCTIME_COLUMN2;
import static com.example.lasyd.velocheck.BaseRides.RIDE_START;
import static com.example.lasyd.velocheck.BaseRides.TABLE_LOC;
import static com.example.lasyd.velocheck.BaseRides.TABLE_RIDE;
import static java.lang.Math.sqrt;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback{

    Start start;
    Finish finish;

    private BaseRides baseRides;
    private boolean DatabaseClean=false;

    private  DataBaseUtil DatabaseUtil;
    private LocationManager locationManager;

    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;

    GoogleMap m_map;
    boolean MapReady=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start=new Start();
        finish=new Finish();

        getSupportFragmentManager().beginTransaction().add(R.id.mainCont,start).commit();

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        baseRides=new BaseRides(this);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


    }

    @Override
    public void onMapReady(GoogleMap map) {
        MapReady=true;
        m_map=map;
        //m_map.setMyLocationEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                1000 * 10, 10, locationListener);
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 1000 * 10, 10,
                locationListener);
        checkEnabled();

    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(locationListener);
    }

    private LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            showLocation(location);
        }

        @Override
        public void onProviderDisabled(String provider) {
            checkEnabled();
        }

        @Override
        public void onProviderEnabled(String provider) {
            checkEnabled();
            showLocation(locationManager.getLastKnownLocation(provider));
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
    };


    private void showLocation(Location location) {
        SQLiteDatabase db = baseRides.getWritableDatabase();
        ContentValues values = new ContentValues();
        if (MapReady) {
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            LatLng latLng = new LatLng(lat, lng);
            //m_map.addMarker(new MarkerOptions().position(latLng));
            m_map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            m_map.animateCamera(CameraUpdateFactory.zoomTo(15));
            m_map.setMyLocationEnabled(true);
        }


        if (location == null)
            return;
        else {
            values.put(BaseRides.LONGITUDE_COLUMN, location.getLongitude());
            values.put(BaseRides.LATITUDE_COLUMN, location.getLatitude());
            values.put(BaseRides.ALTITUDE_COLUMN, location.getAltitude());
            values.put(BaseRides.SYSVELOCITY_COLUMN, location.getSpeed());
            values.put(BaseRides.PCTIME_COLUMN, location.getTime());
        }


        db.insert(TABLE_LOC,null, values);

        Cursor cursor=db.query(TABLE_LOC, new String[] {DLONGITUDE_COLUMN }, null, null, null,
                null, null);

        cursor.moveToLast();
        cursor.close();

    }

    private void checkEnabled() {
        //если не нажат один из датчиков то текст
        Toast toastGPS = Toast.makeText(getApplicationContext(),
                "GPS is off", Toast.LENGTH_LONG);
        Toast toastNET = Toast.makeText(getApplicationContext(),
                "Network is off", Toast.LENGTH_LONG);


        //GPS status
        isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        // network status
        isNetworkEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnabled){toastGPS.show();}
        if(!isNetworkEnabled){toastNET.show();}

    }

    public void onClickLocationSettings(View view) {
        startActivity(new Intent(
                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }




    //    DatabaseUtil.copyDatabaseToExtStg(MainActivity.this);


    public void onClickStart(View v) {
        getSupportFragmentManager().beginTransaction().replace(R.id.mainCont,finish).commit();
        SQLiteDatabase db = baseRides.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(RIDE_START,1);
        double time= System.currentTimeMillis();
        values.put(PCTIME_COLUMN2,time);
        db.insert(TABLE_RIDE,null, values);
        Toast.makeText(this,"Нажатие",Toast.LENGTH_LONG);
    }

    public void onClickFinish (View v) {
        FragmentManager fm = getFragmentManager();
        DialogFinish dialogFinish=new DialogFinish();
        dialogFinish.show(fm,"TT");
    }

}

