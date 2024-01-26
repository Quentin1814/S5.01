package com.example.dechingv1.Database;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

public class DatabaseTask extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {
        String urlString = "https://deching.alwaysdata.net/home/deching/www/Deching/actions/Database.php";  // URL de l'API PHP
        String postData = "username=deching&password=8nj3FzYx*yRV7J.";  // données de connexion

        try {
            Log.d("DatabaseTask", "Sending POST request to: " + urlString);
            Log.d("DatabaseTask", "POST data: " + postData);

            String response = requeteHttp.sendPostRequest(urlString, postData);

            Log.d("DatabaseTask", "Response: " + response);

            return response;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("DatabaseTask", "Error during API connection", e);
            return "Erreur lors de la connexion à l'API.";
        }
    }

    @Override
    protected void onPostExecute(String result) {
        // Traitez le résultat ici
    }
}
