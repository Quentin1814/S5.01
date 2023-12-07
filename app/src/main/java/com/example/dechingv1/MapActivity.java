package com.example.dechingv1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.compose.material.icons.Icons;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;


public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ImageButton boutonLogo, boutonHome, boutonMap, boutonAddPost, boutonEvent, boutonProfile;
    //creation d'un dechet instantane pour la suppression d'un dechet reference

    private Button boutonSignaler;

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

        // Bouton pour ajouter un déchet
        boutonSignaler = findViewById(R.id.buttonSignaler);
        boutonSignaler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                afficherPopup();
                afficherToast("Déchet signalé avec succès", R.color.green);
            }
        });

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap=googleMap;

        googleMap.setMapStyle(new MapStyleOptions(getResources().getString(R.string.style_business)));
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                Marker newDechet = googleMap.addMarker(new MarkerOptions().position(latLng));
                afficherToast("Déchet ajouté avec succès", R.color.green);
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
    // affichage des details du dechet selectionne
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
                    afficherToast("Déchet supprimé avec succès", R.color.red);
                })
                .setNegativeButton("Annuler", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private List<Button> boutonsTaille = new ArrayList<>();
    private List<Button> boutonsDetails = new ArrayList<>();
    private List<Button> boutonsDetails2 = new ArrayList<>();

    private void afficherPopup() {
        // Créer le constructeur de l'AlertDialog avec le contexte actuel
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Créer le layout principal pour le contenu de l'AlertDialog
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        // Ajouter le texte "Taille des déchets" au layout principal
        TextView tailleDesDechetsText = new TextView(this);
        tailleDesDechetsText.setText("Taille des déchets");
        layout.addView(tailleDesDechetsText);

        // Créer un layout pour les boutons de taille
        LinearLayout layoutBoutons = createButtonLayout(boutonsTaille, "Petit", "Moyen", "Grand");
        layout.addView(layoutBoutons);

        // Ajouter le texte "Détails" au layout principal
        TextView details = new TextView(this);
        details.setText("Détails");
        layout.addView(details);

        // Créer un layout pour les boutons de détails
        LinearLayout layoutBoutonsDetails = createButtonLayout(boutonsDetails, "ça pique", "ça mord", "ça mouille");
        layout.addView(layoutBoutonsDetails);

        // Créer un autre layout pour les boutons de détails
        LinearLayout layoutBoutonsDetails2 = createButtonLayout(boutonsDetails2, "toxique", "organique", "electronique");
        layout.addView(layoutBoutonsDetails2);

        // Ajouter un espace pour un commentaire
        TextView commentaire = new TextView(this);
        commentaire.setText("Commentaire");
        layout.addView(commentaire);

        // Ajouter un champ de saisie pour le commentaire
        TextInputLayout commentaireInputLayout = createCommentInputLayout();
        layout.addView(commentaireInputLayout);

        // Créer un layout pour les boutons "Pointer" et "Valider"
        LinearLayout layoutBoutonsValider = createButtonLayout(null, "Pointer", "Valider");
        layout.addView(layoutBoutonsValider);

        // Définir la largeur et la hauteur de l'AlertDialog
        int dialogWidth = (int) (getResources().getDisplayMetrics().widthPixels * 0.8);
        int dialogHeight = (int) (getResources().getDisplayMetrics().heightPixels * 0.8);

        // Configurer le layout personnalisé pour l'AlertDialog
        builder.setView(layout);
        AlertDialog alertDialog = builder.create();

        // Afficher l'AlertDialog
        alertDialog.show();

        // Appliquer la largeur et la hauteur à l'AlertDialog
        alertDialog.getWindow().setLayout(dialogWidth, dialogHeight);
    }

    // Méthode utilitaire pour créer un layout de boutons
    private LinearLayout createButtonLayout(List<Button> buttonsList, String... buttonLabels) {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);

        for (String label : buttonLabels) {
            Button button = new Button(this);
            button.setText(label);
            button.setOnClickListener(v -> {
                // Appeler la méthode pour gérer le bouton
                mettreEnSurbrillance(buttonsList, button);
                afficherToast(label, R.color.green);
            });

            layout.addView(button);

            if (buttonsList != null) {
                buttonsList.add(button);
            }
        }

        return layout;
    }

    // Méthode utilitaire pour créer un TextInputLayout avec un champ de saisie pour le commentaire
    private TextInputLayout createCommentInputLayout() {
        TextInputLayout commentaireInputLayout = new TextInputLayout(this);
        EditText commentaireEditText = new EditText(this);
        commentaireEditText.setHint("Commentaire");
        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter.LengthFilter(500);
        commentaireEditText.setFilters(filters);
        commentaireInputLayout.addView(commentaireEditText);
        return commentaireInputLayout;
    }

    private void mettreEnSurbrillance(List<Button> boutons, Button boutonClique) {
        // Parcourir la liste de boutons et retirer la surbrillance
        for (Button bouton : boutons) {
            bouton.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        }

        // Mettre en surbrillance le bouton cliqué
        boutonClique.setBackgroundColor(getResources().getColor(R.color.highlightColor));
    }

    private void afficherToast(String texteNotification, int couleurBackground) {
        // Créer un layout personnalisé pour le Toast
        LinearLayout toastLayout = new LinearLayout(this);
        toastLayout.setBackgroundResource(couleurBackground);
        toastLayout.setOrientation(LinearLayout.HORIZONTAL);
        toastLayout.setGravity(Gravity.CENTER);

        // Créer un TextView pour le texte du Toast
        TextView textView = new TextView(this);
        textView.setText(texteNotification);
        textView.setTextColor(getResources().getColor(android.R.color.white)); // Choisir une couleur de texte
        textView.setPadding(16, 16, 16, 16);

        // Ajouter le TextView au layout
        toastLayout.addView(textView);

        // Créer le Toast
        Toast toast = new Toast(this);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(toastLayout);
        toast.show();
    }

}