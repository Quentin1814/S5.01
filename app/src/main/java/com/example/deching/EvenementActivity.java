package com.example.deching;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

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
    }
}