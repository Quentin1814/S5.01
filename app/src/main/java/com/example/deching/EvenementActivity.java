package com.example.deching;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import com.example.deching.Modele.Modele.Evenement;

import java.util.List;

/**
 * Classe représentant la page des événements
 */

public class EvenementActivity extends AppCompatActivity {

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

        boutonAddPost = (ImageButton) findViewById(R.id.imageButtonAddPost);
        boutonEvent = (ImageButton) findViewById(R.id.imageButtonEvent);
        boutonProfile = (ImageButton) findViewById(R.id.imageButtonProfile);
        boutonProfile.setOnClickListener(v -> {
            Intent intentProfile = new Intent(EvenementActivity.this, ProfilActivity.class);
            startActivity(intentProfile);
        });

        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES || AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM) {
            boutonHome.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_IN);
            boutonMap.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_IN);
            boutonAddPost.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_IN);
            boutonEvent.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_IN);
            boutonProfile.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_IN);
        } else {
            boutonHome.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
            boutonMap.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
            boutonAddPost.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
            boutonEvent.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
            boutonProfile.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
        }
    }
}