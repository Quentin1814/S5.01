package com.example.deching;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

/**
 * Classe représentant l'interface
 */

public class InterfaceActivity extends AppCompatActivity {

    /**
     * Méthode appelée lors de la création de l'activité.
     *
     * @param savedInstanceState données permettant de reconstruire l'activité lorsqu'elle est recréée, si null alors aucune donnée n'est disponible.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interface);
    }
}