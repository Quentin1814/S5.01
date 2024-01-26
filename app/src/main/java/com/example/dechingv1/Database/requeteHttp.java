package com.example.dechingv1.Database;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
public class requeteHttp {
    public static String sendPostRequest(String urlString, String data) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            // Configuration de la requête
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            // Envoi des données
            OutputStream outputStream = urlConnection.getOutputStream();
            outputStream.write(data.getBytes());
            outputStream.flush();
            // Logs de débogage
            Log.d("requeteHttp", "Données envoyées : " + data);
            // Lecture de la réponse
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            // Logs de débogage
            Log.d("requeteHttp", "Réponse du serveur : " + stringBuilder.toString());
            return stringBuilder.toString();
        } finally {
            urlConnection.disconnect();
        }
    }
}
