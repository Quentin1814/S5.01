package com.example.deching;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

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
     * Bouton de message
     */
    private ImageButton boutonMessage;

    /**
     * Méthode appelée lors de la création de l'activité.
     *
     * @param savedInstanceState données permettant de reconstruire l'activité lorsqu'elle est recréée, si null alors aucune donnée n'est disponible.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        boutonHome = (ImageButton) findViewById(R.id.imageButtonHome);
        boutonAddPost = (ImageButton) findViewById(R.id.imageButtonAddPost);

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

        boutonMessage=(ImageButton)findViewById(R.id.imageButtonChat);
        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES || AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM) {
            Log.d("HomePageActivity", "Mode nuit");
            boutonMessage.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_IN);
            boutonHome.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_IN);
            boutonMap.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_IN);
            boutonAddPost.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_IN);
            boutonEvent.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_IN);
            boutonProfile.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_IN);
        } else {
            Log.d("HomePageActivity", "Mode jour");
            boutonMessage.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
            boutonHome.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
            boutonMap.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
            boutonAddPost.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
            boutonEvent.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
            boutonProfile.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
        }
    }
}