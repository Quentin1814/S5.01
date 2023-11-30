package com.example.dechingv1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.compose.material.icons.Icons;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;


public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ImageButton boutonLogo, boutonHome, boutonMap, boutonAddPost, boutonEvent, boutonProfile;
    //creation d'un dechet instantane pour la suppression d'un dechet reference
    private List<Dechet> listeDechets = new ArrayList<>();
    private GoogleMap googleMap;  // Déplacez la déclaration ici pour qu'elle soit accessible à toutes les méthodes
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Initialisation de la liste de déchets (simulée)
        listeDechets.add(new Dechet("1", 48.858844, 2.294350, "Déchet 1"));
        listeDechets.add(new Dechet("2", 48.860000, 2.297000, "Déchet 2"));
        boutonHome = (ImageButton) findViewById(R.id.imageButtonHome);
        boutonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentHome = new Intent(MapActivity.this, HomePageActivity.class);
                startActivity(intentHome);
            }
        });

        boutonMap = findViewById(R.id.imageButtonMap);

        boutonLogo = findViewById(R.id.imageButtonLogo);
        boutonLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapActivity.this, HomePageActivity.class);
                startActivity(intent);
            }
        });

        SupportMapFragment fragmentMap = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.layoutMap);
        try {
            fragmentMap.getMapAsync(this);
        } catch (Exception exception) {
            Log.d("exception", exception.getMessage() + " ");
        }

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap=googleMap;

        googleMap.setMapStyle(new MapStyleOptions(getResources().getString(R.string.style_business)));
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                Marker newDechet = googleMap.addMarker(new MarkerOptions().position(latLng));
            }
        });
        LatLng bayonne = new LatLng(43.4833, -1.4833);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(bayonne));
        Log.d("Activity", "c'est l'activité qui affiche la map");


        // Ajouter des marqueurs pour chaque déchet dans la liste
        for (Dechet dechet : listeDechets) {
            LatLng position = new LatLng(dechet.latitude, dechet.longitude);
            googleMap.addMarker(new MarkerOptions().position(position).title(dechet.description));
        }
        // Ajoute un marqueur à des coordonnées fixes
        LatLng positionFixe = new LatLng(48.858844, 2.294350);
        googleMap.addMarker(new MarkerOptions().position(positionFixe).title("Marqueur Fixe"));

        // Centrer la caméra sur le marqueur ajouté
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(positionFixe, 10));

        // Centrer la caméra sur la première position (si elle existe)
        if (!listeDechets.isEmpty()) {
            LatLng premierDechet = new LatLng(listeDechets.get(0).latitude, listeDechets.get(0).longitude);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(premierDechet, 10));
        }

        // Ajouter un listener de clic sur les marqueurs
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                // Récupérer le déchet associé au marqueur
                Dechet dechetSelectionne = trouverDechetParDescription(marker.getTitle());

                // Afficher une boîte de dialogue ou exécuter l'action appropriée
                if (dechetSelectionne != null) {
                   // Afficher les détails du déchet
                    afficherDetailsDechet(dechetSelectionne);
                    // Supprimer le déchet sélectionné
                    supprimerDechet(dechetSelectionne);
                    Toast.makeText(MapActivity.this, "Déchet supprimé", Toast.LENGTH_SHORT).show();
                }

                return true;
            }
        });
    }

    // Méthode pour supprimer un déchet de la liste (simulée)
    private void supprimerDechet(Dechet dechet) {
        listeDechets.remove(dechet);
        // Mettez à jour l'affichage sur la carte
        afficherMarqueursSurCarte();
    }

    // Méthode pour trouver un déchet par sa description (simulée)
    private Dechet trouverDechetParDescription(String description) {
        for (Dechet dechet : listeDechets) {
            if (dechet.description.equals(description)) {
                return dechet;
            }
        }
        return null;
    }

    // Méthode pour afficher des marqueurs sur la carte
    private void afficherMarqueursSurCarte() {
        Log.d("MapActivity", "afficherMarqueursSurCarte appelée");
        if (googleMap != null) {
            Log.d("MapActivity", "googleMap non nul, effacement des marqueurs");
        googleMap.clear(); // Efface les marqueurs existants

        // Ajouter des marqueurs pour chaque déchet dans la liste mise à jour
        for (Dechet dechet : listeDechets) {
            LatLng position = new LatLng(dechet.latitude, dechet.longitude);
            googleMap.addMarker(new MarkerOptions().position(position).title(dechet.description));
        }
        }
    }
    // affichage des detaille du dechet selectionne
    private void afficherDetailsDechet(Dechet dechet) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Détails du Déchet")
                .setMessage("ID: " + dechet.id + "\n"
                        + "Latitude: " + dechet.latitude + "\n"
                        + "Longitude: " + dechet.longitude + "\n"
                        + "Description: " + dechet.description)
                .setPositiveButton("Supprimer", (dialog, which) -> {
                    // Appeler la méthode pour supprimer le déchet après confirmation
                    supprimerDechet(dechet);
                    Toast.makeText(MapActivity.this, "Déchet supprimé", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Annuler", (dialog, which) -> dialog.dismiss())
                .show();
    }




}