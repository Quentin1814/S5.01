package com.example.deching;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.widget.ImageButton;

import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.deching.Modele.Modele.Evenement;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;

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
    private List<Evenement> evenementsList;
    private TextView nomTextView;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evenement);
        boutonMap=findViewById(R.id.imageButtonMap);
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
        boutonAddPost.setOnClickListener(v -> {
            Intent intentPost = new Intent(EvenementActivity.this, AddPostActivity.class);
            startActivity(intentPost);
        });
        boutonProfile = (ImageButton) findViewById(R.id.imageButtonProfile);
        boutonProfile.setOnClickListener(v -> {
            Intent intentProfil = new Intent(EvenementActivity.this, ProfilActivity.class);
            startActivity(intentProfil);
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
        // Récupérer les événements depuis l'API
        getEvenementsFromAPI();
    }

    private void getEvenementsFromAPI() {
        String url = "https://deching.alwaysdata.net/actions/Evenement.php";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            try {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH)+1;
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                String date= year+"-"+day+"-"+month;
                for (int i = 0; i < response.length(); i++) {
                    JSONObject jsonObject = response.getJSONObject(i);
                    String nom = jsonObject.getString("nom");
                    String photoBase64 = String.valueOf(R.drawable.empty);
                    String lieu = jsonObject.getString("lieu");
                    String description = jsonObject.getString("description");

                    Evenement evenement = new Evenement(nom,description,0, lieu,date,120,photoBase64);
                    evenementsList.add(evenement);
                }

                // Après avoir récupéré tous les événements, affichez-les
                afficherEvenements();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EvenementActivity.this, "Erreur de connexion", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    private void afficherEvenements() {
            // Créer un StringBuilder pour stocker les informations de tous les événements
            StringBuilder evenementsStringBuilder = new StringBuilder();

            for (Evenement evenement : evenementsList) {
                String nom = evenement.getNom();
                String photoBase64 = evenement.getPhotoBase64();
                String lieu = evenement.getLieu();
                String description = evenement.getDescription();

                // Ajouter les informations de l'événement au StringBuilder
                evenementsStringBuilder.append("Nom :\t").append(nom).append("\n");
                evenementsStringBuilder.append("Lieu : \t").append(lieu).append("\n");
                evenementsStringBuilder.append("Description :\t").append(description).append("\n\n");

            }
            // Afficher toutes les informations des événements dans les TextView
            String  evenementsText = evenementsStringBuilder.toString();
            nomTextView.setText(evenementsText);

        }

}