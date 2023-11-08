package com.example.dechingv1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;


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

        boutonLogo=findViewById(R.id.imageButtonLogo);
        boutonLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MapActivity.this, HomePageActivity.class);
                startActivity(intent);
            }
        });

        SupportMapFragment fragmentMap=(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.layoutMap);
        fragmentMap.getMapAsync(this);

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        LatLng bayonne = new LatLng(43.4833,-1.4833);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(bayonne));
    }
}