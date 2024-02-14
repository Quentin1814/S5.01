package com.example.deching.Modele;
import android.os.AsyncTask;
import android.util.Log;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Blob;
import java.sql.Date;
import java.util.ArrayList;

public class Dechet {
    private int id;//Id du déchet dans la base de données
    private double latitude;//Latitude de la position du déchet
    private double longitude;//Longitude de la position du déchet
    private int taille;//Taille du déchet :
    private String description;//Description optionnelle ajoutée lors de la description d'un déchet


    public Dechet( double latitude, double longitude, int taille, String description) {
        this.id=0;
        this.latitude = latitude;
        this.longitude = longitude;
        this.taille=taille;
        this.description = description;
    }

    public Dechet(int id, double latitude, double longitude, int taille, String description) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.taille=taille;
        this.description = description;
    }

    public int getId(){
        return this.id;
    }

    public void setId(int id){
        this.id = id;
    }

    public double getLongitude(){
        return this.longitude;
    }

    public void setLongitude(double longitude){
        this.longitude = longitude;
    }

    public double getLatitude(){
        return this.latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public int getTaille(){return this.taille;}

    public void setTaille(int taille) { this.taille=taille;}

    public String getDescription(){
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Méthode pour ajouter un déchet via l'API PHP
    public void ajouterDechet(String apiUrl) {
        new AjouterDechetTask().execute(apiUrl);
    }

    private class AjouterDechetTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String apiUrl = "http://deching.alwaysdata.net/home/deching/www/Deching/actions/Dechet.php";
            String response = "";

            try {
                URL url = new URL(apiUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                // Paramètres de la requête
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("latitude", latitude);
                jsonParam.put("longitude", longitude);
                jsonParam.put("taille", taille);
                jsonParam.put("description", description);

                // Configurer la connexion
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestMethod("POST");

                // Écrire les données dans le flux de sortie
                OutputStream os = urlConnection.getOutputStream();
                os.write(jsonParam.toString().getBytes("UTF-8"));
                os.close();

                // Lire la réponse
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String line;
                while ((line = br.readLine()) != null) {
                    response += line;
                }
                br.close();

                // Fermer la connexion
                urlConnection.disconnect();

            } catch (Exception e) {
                Log.e("API Connection", "Erreur lors de la connexion à l'API", e);
            }

            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            // Traitement de la réponse JSON
            try {
                JSONObject jsonResult = new JSONObject(result);
                boolean success = jsonResult.getBoolean("success");
                String message = jsonResult.getString("error");

                if (success) {
                    // Opération réussie
                    Log.d("Ajout Dechet", "Déchet ajouté avec succès");
                } else {
                    // Afficher un message d'erreur
                    Log.e("Ajout Dechet", "Erreur lors de l'ajout du déchet : " + message);
                }

            } catch (JSONException e) {
                Log.e("JSON Error", "Erreur lors de l'analyse de la réponse JSON", e);
            }
        }
    }
}
