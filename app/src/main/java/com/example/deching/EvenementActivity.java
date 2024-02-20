package com.example.deching;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.deching.Modele.Modele.Evenement;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EvenementActivity extends AppCompatActivity {
    private ImageButton boutonHome;
    private ImageButton boutonMap;
    private ImageButton boutonAddPost;
    private ImageButton boutonProfile;

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