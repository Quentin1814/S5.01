package com.example.deching;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.deching.R;

public class HomePageActivity extends AppCompatActivity {
    private ImageButton boutonLogo;
    private ImageButton boutonHome;
    private ImageButton boutonMap;
    private ImageButton boutonAddPost;
    private ImageButton boutonEvent;
    private ImageButton boutonProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        boutonMap=(ImageButton)findViewById(R.id.imageButtonMap);
        boutonMap.setOnClickListener(v -> {
            Intent intentMap=new Intent(HomePageActivity.this, MapActivity.class);
            startActivity(intentMap);
        });
        boutonEvent = (ImageButton) findViewById(R.id.imageButtonEvent);
        boutonEvent.setOnClickListener(v -> {
            // Ajouter le code pour naviguer vers l'activité Evenement
            Intent intentEvent = new Intent(HomePageActivity.this, EvenementActivity.class);
            startActivity(intentEvent);
        });
    }
}