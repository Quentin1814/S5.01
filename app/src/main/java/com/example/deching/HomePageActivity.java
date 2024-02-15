package com.example.deching;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Classe représentant la page d'accueil
 */

public class HomePageActivity extends AppCompatActivity {
    /**
     * Logo de l'application
     */
    private ImageButton boutonLogo;

    /**
     * Bouton de la page d'accueil
     */
    private ImageButton boutonHome;

    /**
     * Bouton de la carte
     */
    private ImageButton boutonMap;

    /**
     * Bouton d'ajout de post
     */
    private ImageButton boutonAddPost;

    /**
     * Bouton des événements
     */
    private ImageButton boutonEvent;

    /**
     * Bouton du profil
     */
    private ImageButton boutonProfile;

    /**
     * Méthode appelée lors de la création de l'activité.
     *
     * @param savedInstanceState données permettant de reconstruire l'activité lorsqu'elle est recréée, si null alors aucune donnée n'est disponible.
     */
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
        boutonProfile=(ImageButton)findViewById(R.id.imageButtonProfile);
        boutonProfile.setOnClickListener(v -> {
            Intent intentProfile = new Intent(HomePageActivity.this, ProfilActivity.class);
            startActivity(intentProfile);
        });
    }
}