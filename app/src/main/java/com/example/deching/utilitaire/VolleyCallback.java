package com.example.deching.utilitaire;

import com.example.deching.Modele.Modele.Dechet;

import java.util.ArrayList;

/**
 * Interface d'appel à Volley
 */
public interface VolleyCallback {

    /**
     * Methode qui permet de tester la réponse
     * @param reponse Liste des déchets
     */
    void onSuccess(ArrayList<Dechet> reponse);

}
