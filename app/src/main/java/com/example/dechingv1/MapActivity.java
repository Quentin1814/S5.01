package com.example.dechingv1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.compose.material.icons.Icons;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ImageButton boutonLogo,boutonHome,boutonMap,boutonAddPost,boutonEvent,boutonProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        boutonHome=(ImageButton)findViewById(R.id.imageButtonHome);
        boutonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentHome=new Intent(MapActivity.this, HomePageActivity.class);
                startActivity(intentHome);
            }
        });

        boutonMap=findViewById(R.id.imageButtonMap);

        boutonLogo=findViewById(R.id.imageButtonLogo);
        boutonLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MapActivity.this, HomePageActivity.class);
                startActivity(intent);
            }
        });

        SupportMapFragment fragmentMap=(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.layoutMap);
        try {
            fragmentMap.getMapAsync(this);
        }catch(Exception exception){
            Log.d("exception",exception.getMessage()+" ");
        }

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        googleMap.setMapStyle(new MapStyleOptions(getResources().getString(R.string.style_business)));
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                Marker newDechet=googleMap.addMarker(new MarkerOptions().position(latLng));
            }
        });
        LatLng bayonne = new LatLng(43.4833,-1.4833);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(bayonne));
        Log.d("Activity","c'est l'activité qui affiche la map");
    }



}