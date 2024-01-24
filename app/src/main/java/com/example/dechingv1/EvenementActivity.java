package com.example.dechingv1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.dechingv1.Modele.Evenement;

import java.util.List;

public class EvenementActivity extends AppCompatActivity {
    private ImageButton boutonLogo,boutonHome,boutonMap,boutonAddPost,boutonEvent,boutonProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evenement);

        // Récupérer la liste des événements depuis MapActivity
        List<Evenement> listeEvenements =MapActivity.listeEvenements;
        boutonMap=(ImageButton)findViewById(R.id.imageButtonMap);
        boutonMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMap=new Intent(EvenementActivity.this, MapActivity.class);
                startActivity(intentMap);
            }
        });
        boutonHome = (ImageButton) findViewById(R.id.imageButtonHome);
        boutonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentHome = new Intent(EvenementActivity.this, HomePageActivity.class);
                startActivity(intentHome);

            }
        });
    }
}