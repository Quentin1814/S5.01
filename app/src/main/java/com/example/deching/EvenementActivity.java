package com.example.deching;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class EvenementActivity extends AppCompatActivity {
    private ImageButton boutonLogo;
    private ImageButton boutonHome;
    private ImageButton boutonMap;
    private ImageButton boutonAddPost;
    private ImageButton boutonEvent;
    private ImageButton boutonProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evenement);

        // Récupérer la liste des événements depuis MapActivity
        List<Evenement> listeEvenements = MapActivity.listeEvenements;
        boutonMap=(ImageButton)findViewById(R.id.imageButtonMap);
        boutonMap.setOnClickListener(v -> {
            Intent intentMap=new Intent(EvenementActivity.this, MapActivity.class);
            startActivity(intentMap);
        });
        boutonHome = (ImageButton) findViewById(R.id.imageButtonHome);
        boutonHome.setOnClickListener(v -> {
            Intent intentHome = new Intent(EvenementActivity.this, HomePageActivity.class);
            startActivity(intentHome);

        });
    }
}