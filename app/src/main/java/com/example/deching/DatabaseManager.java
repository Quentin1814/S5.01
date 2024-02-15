package com.example.deching;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Classe responsable de la gestion de la base de données.
 * Utilise Volley pour envoyer des requêtes réseau.
 */
public class DatabaseManager {
    /**
     * File d'attente pour les requêtes réseau
     */
    public RequestQueue queue;

    /**
     * Constructeur de la classe DatabaseManager.
     *
     * @param context Le contexte de l'application.
     */
    public DatabaseManager(Context context) {
        // Initialisation de la file d'attente avec Volley
        this.queue = Volley.newRequestQueue(context);
    }
}
