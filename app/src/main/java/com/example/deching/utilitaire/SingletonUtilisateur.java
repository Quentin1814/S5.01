package com.example.deching.utilitaire;

import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.deching.MainActivity;
import com.example.deching.Modele.Modele.Utilisateur;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.Objects;

public class SingletonUtilisateur{

    private static volatile SingletonUtilisateur instance= null;
    private String nomUtilisateur;

    private SingletonUtilisateur(String NomUtilisateur) {
        // The following code emulates slow initialization.
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        nomUtilisateur = NomUtilisateur;
    }


    public String getNomUtilisateur() {
        return nomUtilisateur;
    }

    public void setNomUtilisateur(String nomUtilisateur) {
        this.nomUtilisateur = nomUtilisateur;
    }

    public static SingletonUtilisateur getInstance(String NomUtilisateur) {
        if (instance == null) {
            instance = new SingletonUtilisateur(NomUtilisateur);
        }
        return instance;
    }

}

