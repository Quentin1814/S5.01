package com.example.deching;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.imagecapture.JpegBytes2Image;
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
import java.util.List;

public class EvenementActivity extends AppCompatActivity {
    private ImageButton boutonHome;
    private ImageButton boutonMap;

    private List<Evenement> evenementsList;
    private ImageView imageView;
    private TextView nomTextView;
    private TextView lieuTextView;
    private TextView descriptionTextView;
    private RecyclerView eventRecyclerView;
    private EventAdapter eventAdapter;
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
        boutonHome=findViewById(R.id.imageButtonHome);
        boutonHome.setOnClickListener(v -> {
            Intent intentHome=new Intent(EvenementActivity.this, HomePageActivity.class);
            startActivity(intentHome);
        });
        imageView = findViewById(R.id.imageView);
        nomTextView = findViewById(R.id.nomTextView);
        lieuTextView = findViewById(R.id.lieuTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);

        evenementsList = new ArrayList<>();
        eventRecyclerView = findViewById(R.id.eventRecyclerView);
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        evenementsList = new ArrayList<>();
        eventAdapter = new EventAdapter(evenementsList);
        eventRecyclerView.setAdapter(eventAdapter);
        // Récupérer les événements depuis l'API
        getEvenementsFromAPI();
    }

    private void getEvenementsFromAPI() {
        String url = "https://deching.alwaysdata.net/actions/Evenement.php";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            try {
                for (int i = 0; i < response.length(); i++) {
                    JSONObject jsonObject = response.getJSONObject(i);
                    String nom = jsonObject.getString("nom");
                    String photoBase64 = String.valueOf(R.drawable.empty);
                    String lieu = jsonObject.getString("lieu");
                    String description = jsonObject.getString("description");

                    Evenement evenement = new Evenement(nom, photoBase64, lieu, description);
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
                evenementsStringBuilder.append("Nom").append(nom).append("\n");
                evenementsStringBuilder.append("").append(photoBase64).append("\n");
                evenementsStringBuilder.append("Lieu").append(lieu).append("\n");
                evenementsStringBuilder.append("Description").append(description).append("\n\n");

            }
            // Afficher toutes les informations des événements dans les TextView
            String  evenementsText = evenementsStringBuilder.toString();
            nomTextView.setText(evenementsText);

        }

}
