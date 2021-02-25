package com.example.deedo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

public class Create_Lotate extends FragmentActivity implements OnMapReadyCallback {
    String userId;
    Context context;
    DBHelper db;
    private GoogleMap mMap;
    Double set_latitude;
    Double set_longitude;
    Button Create_btn;
    MarkerOptions set_Marker;
    EditText create_EditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_lotate);

        db = new DBHelper(Create_Lotate.this);


        Create_btn = findViewById(R.id.create_Area_button);

        Create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userId = getIntent().getStringExtra("id");
                Toast.makeText(getApplicationContext(), db.insert_create_lotate(userId, create_EditText.getText().toString(),set_latitude.toString(), set_longitude.toString() ), Toast.LENGTH_SHORT).show();


            }
        });



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                context = Create_Lotate.this;
                BitmapDrawable bd = (BitmapDrawable) context.getResources().getDrawable(R.drawable.marker);
                Bitmap b = bd.getBitmap();
                Bitmap bitMapImage = Bitmap.createScaledBitmap(b,80,100,false);
                mMap.clear();
                MarkerOptions mOptions = new MarkerOptions();

                mOptions.title("마커 좌표");

                set_latitude = point.latitude;
                set_longitude = point.longitude;

                mOptions.snippet(set_latitude.toString() + ", " + set_longitude.toString());

                mOptions.position(new LatLng(set_latitude, set_longitude));

                googleMap.addMarker(mOptions.icon(BitmapDescriptorFactory.fromBitmap(bitMapImage)).position(new LatLng(set_latitude, set_longitude)));

            }
        });


        // Add a marker in Sydney and move the camera
        context = Create_Lotate.this;
        LatLng sydney = new LatLng(35.2379, 128.6342);
        BitmapDrawable bd = (BitmapDrawable) context.getResources().getDrawable(R.drawable.marker);
        Bitmap b = bd.getBitmap();
        Bitmap bitMapImage = Bitmap.createScaledBitmap(b,80,100,false);

        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(bitMapImage)).position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
    }




}