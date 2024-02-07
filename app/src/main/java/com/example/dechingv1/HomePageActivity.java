package com.example.dechingv1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class HomePageActivity extends AppCompatActivity {
    private ImageButton boutonLogo,boutonHome,boutonMap,boutonAddPost,boutonEvent,boutonProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        boutonMap=(ImageButton)findViewById(R.id.imageButtonMap);
        boutonMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMap=new Intent(HomePageActivity.this, MapActivity.class);
                startActivity(intentMap);
            }
        });
        boutonEvent = (ImageButton) findViewById(R.id.imageButtonEvent);
        boutonEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ajouter le code pour naviguer vers l'activité Evenement
                Intent intentEvent = new Intent(HomePageActivity.this, EvenementActivity.class);
                startActivity(intentEvent);
            }
        });


        boutonProfile=(ImageButton)findViewById(R.id.imageButtonProfile);
        boutonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentEvent = new Intent(HomePageActivity.this, profilUtilisateur.class);
                startActivity(intentEvent);
            }
        });

    }
    }
