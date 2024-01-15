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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ImageButton boutonLogo, boutonHome, boutonMap, boutonAddPost, boutonEvent, boutonProfile;

    //ajouter des variables pour recuperer les coordonnees d'un marqueur quand on clic sur la carte
    private double lastClickedLatitude;
    private double lastClickedLongitude;
    // Ajoutez une variable pour suivre l'état actuel quand on clic sur le button "Pointer" du popup
    private boolean modePointer = false;
    //creation d'un dechet instantane pour la suppression d'un dechet reference
    private Button boutonSignaler;

    private List<Dechet> listeDechets = new ArrayList<>();
    // creation d'un evenement instantane
    public static List<Evenement> listeEvenements = new ArrayList<>();
    //pour recuperer les imformation du derniere point creer afin de creer un evenement
    private Dechet dernierDechetClique=null;
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
        boutonEvent = (ImageButton) findViewById(R.id.imageButtonEvent);
        boutonEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ajouter le code pour naviguer vers l'activité Evenement
                Intent intentEvent = new Intent(MapActivity.this, EvenementActivity.class);
                startActivity(intentEvent);
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

                if (modePointer) {
                    // Ajoutez un marqueur à l'emplacement cliqué
                    googleMap.addMarker(new MarkerOptions().position(latLng));
                    // Mettre à jour les variables globales avec les dernières coordonnées
                    lastClickedLatitude = latLng.latitude;
                    lastClickedLongitude = latLng.longitude;
                    // Ajoutez les informations que vous souhaitez récupérer en tant que tag du marqueur
                    newDechet.setTag(new Dechet("ID", latLng.latitude, latLng.longitude, "Description"));
                    afficherToast("Marqueur ajouté avec succès", R.color.green);

                    // Désactivez le mode "Pointer" et réaffichez le popup
                    modePointer = false;
                    afficherPopup();
                }


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
                // Récupérez les informations du marqueur à partir de la balise
                Dechet markerInfo = (Dechet) marker.getTag();

                // Vérifiez si la balise contient des informations
                if (markerInfo != null) {
                    // Utilisez les informations du marqueur comme nécessaire
                    Log.d("Marker Clicked", "ID: " + markerInfo.id + ", Latitude: " + markerInfo.latitude +
                            ", Longitude: " + markerInfo.longitude + ", Description: " + markerInfo.description);
                }

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
                .setPositiveButton("Partager", (dialog, which) -> {
                    // Appeler la méthode pour partager le déchet
                    partagerDechet(dechet);
                })

                .setNegativeButton("Annuler", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void partagerDechet(Dechet dechet) {

    }

    private List<Button> boutonsTaille = new ArrayList<>();
    private List<Button> boutonsDetails = new ArrayList<>();
    private List<Button> boutonsDetails2 = new ArrayList<>();
    private AlertDialog alertDialog;
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
        // Ajouter un espace pour un adresse
        TextView positionLabel = new TextView(this);
        positionLabel.setText("Position");
        layout.addView(positionLabel);
        // Ajouter des TextViews pour la latitude et la longitude
        TextView latitudeLabel = new TextView(this);
        latitudeLabel.setText("Latitude: " + lastClickedLatitude);
        layout.addView(latitudeLabel);

        TextView longitudeLabel = new TextView(this);
        longitudeLabel.setText("Longitude: " + lastClickedLongitude);
        layout.addView(longitudeLabel);
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
        // Récupérer le bouton "Valider" pour pouvoir ajouter un clic
        Button boutonValider = (Button) layoutBoutonsValider.getChildAt(1);
        // Ajouter un clic pour le bouton "Valider"
        boutonValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onValiderClick(alertDialog);
            }
        });
        // Récupérer le bouton "Pointer" pour pouvoir ajouter un clic
        Button boutonPointer = (Button) layoutBoutonsValider.getChildAt(0);
        // Ajouter un clic pour le bouton "Pointer"
        boutonPointer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Activez le mode "Pointer" et masquez le popup
                modePointer = true;
                alertDialog.dismiss();
            }
        });

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
    // Ajouter la méthode pour gérer le clic sur le bouton "Valider"
    private void onValiderClick(AlertDialog alert) {
        String tailleSelectionnee = getBoutonSelectionne(boutonsTaille);
        String detailsSelectionnes = getBoutonSelectionne(boutonsDetails) + ", " + getBoutonSelectionne(boutonsDetails2);
        String commentaire = getCommentaire();
        String position = getPosition();

        // Faire quelque chose avec les informations récupérées
        Log.d("Valider", "Taille: " + tailleSelectionnee + ", Détails: " + detailsSelectionnes + ", Position: " + position + ", Commentaire: " + commentaire);

        // Ajouter le nouveau déchet à la liste
        String nouvelId = ""; // ID temporaire
        Dechet nouveauDechet = new Dechet(nouvelId, lastClickedLatitude, lastClickedLongitude, detailsSelectionnes);
        listeDechets.add(nouveauDechet);
        // Stocker le dernier déchet cliqué
        dernierDechetClique = nouveauDechet;
        // Afficher un Toast avec les informations du déchet ajouté
        afficherToast("Déchet ajouté avec succès\nLatitude: " + lastClickedLatitude + "\nLongitude: " + lastClickedLongitude, R.color.green);
        // Vérifier les détails sélectionnés et afficher une boîte de dialogue d'alerte appropriée
        afficherAlerte("Attention: (message a rediger) "+ nouveauDechet.description);
        // Fermer le popup
        fermerPopup(alert);
    }
    // Méthode pour afficher une boîte de dialogue d'alerte standard
    private void afficherAlerte(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                })
                .show();
    }
    // Ajouter la methode pour fermer le popup
    private void fermerPopup(AlertDialog alertDialog) {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }
    private String getPosition() {
        return "Latitude: " + lastClickedLatitude + ", Longitude: " + lastClickedLongitude;
    }


    // Méthode utilitaire pour récupérer le texte du champ de commentaire
    private String getCommentaire() {
    // TextInputLayout commentaireInputLayout = findViewById(R.id.commentaireInputLayout);
    //  EditText commentaireEditText = commentaireInputLayout.findViewById(R.id.commentaireEditText);
       return ""; //commentaireEditText.getText().toString();
  }

    // Méthode pour récupérer le texte du bouton sélectionné
    private String getBoutonSelectionne(List<Button> boutons) {
        for (Button bouton : boutons) {
            if (bouton.getBackground() != null && bouton.getBackground().getConstantState().equals(getResources().getDrawable(R.color.highlightColor).getConstantState())) {
                return bouton.getText().toString();
            }
        }
        return "";
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