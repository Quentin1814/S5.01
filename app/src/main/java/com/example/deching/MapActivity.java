package com.example.deching;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.deching.Modele.Modele.Dechet;
import com.example.deching.utilitaire.VolleyCallback;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    // ajouter des variables pour recuperer les coordonnees d'un marqueur quand on clic sur la carte
    private double lastClickedLatitude;
    private double lastClickedLongitude;
    // Ajoutez une variable pour suivre l'état actuel quand on clic sur le button "Pointer" du popup
    private boolean modePointer = false;
    // Ajoutez une variable pour stocker les paramètres du popup
    private Map<String, String> popupParameters = new HashMap<>();
    //creation d'un dechet instantane pour la suppression d'un dechet reference


    private final List<Dechet> listeDechets = new ArrayList<Dechet>();

    private ArrayList<Dechet> listeDechetsInit = new ArrayList<Dechet>();
    // creation d'un evenement instantane
    public static List<Evenement> listeEvenements = new ArrayList<>();
    //pour recuperer les imformation du derniere point creer afin de creer un evenement

    private GoogleMap googleMap;  // Déplacez la déclaration ici pour qu'elle soit accessible à toutes les méthodes

    private final int CAMERA_ACTIVITY_REQUEST_CODE = 100;
    // Créer un LinearLayout pour centrer l'ImageView


    private SharedPreferences sharedPreferences;
    ImageButton boutonPosition;
    private LinearLayout layoutImageView;
    private ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Button boutonSignaler;
        ImageButton boutonEvent;
        ImageButton boutonMap;
        ImageButton boutonHome;
        ImageButton boutonLogo;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Récupérer tous les frame layouts
        FrameLayout layoutMap = findViewById(R.id.map);
        ScrollView layoutPopup = findViewById(R.id.popup);
        layoutMap.setVisibility(View.VISIBLE);
        layoutPopup.setVisibility(View.GONE);

        // Chargement des données de déchets récupérées dasn le base de données
        getAllZoneDechet(new VolleyCallback() {
            @Override
            public void onSuccess(ArrayList<Dechet> reponse) {
                listeDechetsInit.addAll(reponse);
                Log.d("initialisation de la liste de déchet",listeDechetsInit.toString());
            }
        });

        // Initialisation de la liste de déchets (simulée)
        listeDechets.add(new Dechet(1, 48.858844, 2.294350, "moyen","Déchet 1"));
        listeDechets.add(new Dechet(2, 48.860000, 2.297000,"grand" ,"Déchet 2"));
        boutonHome = (ImageButton) findViewById(R.id.imageButtonHome);
        boutonHome.setOnClickListener(v -> {
            Intent intentHome = new Intent(MapActivity.this, HomePageActivity.class);
            startActivity(intentHome);

        });
        boutonEvent = findViewById(R.id.imageButtonEvent);
        boutonEvent.setOnClickListener(v -> {
            // Ajouter le code pour naviguer vers l'activité Evenement
            Intent intentEvent = new Intent(MapActivity.this, EvenementActivity.class);
            startActivity(intentEvent);
        });
        boutonMap = findViewById(R.id.imageButtonMap);

        boutonLogo = findViewById(R.id.imageButtonLogo);
        boutonLogo.setOnClickListener(v -> {
            Intent intent = new Intent(MapActivity.this, HomePageActivity.class);
            startActivity(intent);
        });

        // Chargement de la carte dans le fragment prévu à cet effet
        SupportMapFragment fragmentMap = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.layoutMap);
        try {
            assert fragmentMap != null;
            fragmentMap.getMapAsync(this);
        } catch (Exception exception) {
            Log.d("exception", exception.getMessage() + " ");
        }

        // Bouton pour ajouter un déchet
        boutonSignaler = findViewById(R.id.buttonSignaler);
        boutonSignaler.setOnClickListener(v -> {
            popupParameters = new HashMap<>();
            afficherPopup();
        });

        askAuthorisation();
    }

    private void askAuthorisation() {
        if (ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Demandez la permission si elle n'est pas déjà accordée
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else if (ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Demandez la permission si elle n'est pas déjà accordée
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
        onRequestPermissionsResult(1, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, new int[]{PackageManager.PERMISSION_GRANTED});
    }

    private void animateCameraToPosition(GoogleMap map, LatLng newPosition) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(newPosition)   // Sets the new camera position
                .zoom(15f)             // Sets the zoom
                // .bearing(90)        // Optional
                // .tilt(40)           // Optional
                .build();              // Creates a CameraPosition from the builder

        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 2000, null);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap=googleMap;

        googleMap.setMapStyle(new MapStyleOptions(getResources().getString(R.string.style_business)));
        googleMap.setOnMapClickListener(latLng -> {
            if (modePointer) {
                // Mettre à jour les variables globales avec les dernières coordonnées
                lastClickedLatitude = latLng.latitude;
                lastClickedLongitude = latLng.longitude;

                // Désactivez le mode "Pointer" et réaffichez le popup
                modePointer = false;
                afficherPopup(lastClickedLatitude, lastClickedLongitude);
            } else {
                Marker newDechet = googleMap.addMarker(new MarkerOptions().position(latLng));
                Dechet unDechet = new Dechet(  latLng.latitude, latLng.longitude,"petit" ,"Description");
                // Ajoutez un marqueur à l'emplacement cliqué
                googleMap.addMarker(new MarkerOptions().position(latLng));
                // Mettre à jour les variables globales avec les dernières coordonnées
                lastClickedLatitude = latLng.latitude;
                lastClickedLongitude = latLng.longitude;
                // Ajoutez les informations que vous souhaitez récupérer en tant que tag du marqueur
                assert newDechet != null;
                newDechet.setTag(unDechet);
                addZoneDechet(unDechet);
                afficherToast(getString(R.string.dechetAdd), R.color.green);
            }
        });

        listeDechets.addAll(listeDechetsInit);

        // Ajouter des marqueurs pour chaque déchet dans la liste
        for (Dechet dechet : listeDechets) {
            LatLng position = new LatLng(dechet.getLatitude(), dechet.getLongitude());
            googleMap.addMarker(new MarkerOptions().position(position).title(dechet.getDescription()));
        }
        // Ajoute un marqueur à des coordonnées fixes
        LatLng positionFixe = new LatLng(48.858844, 2.294350);
        googleMap.addMarker(new MarkerOptions().position(positionFixe).title("Marqueur Fixe"));

        // Centrer la caméra sur le marqueur ajouté
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(positionFixe, 10));

        // Centrer la caméra sur la première position (si elle existe)
        if (!listeDechets.isEmpty()) {
            LatLng premierDechet = new LatLng(listeDechets.get(0).getLatitude(), listeDechets.get(0).getLongitude());
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(premierDechet, 10));
        }

        // Ajouter un listener de clic sur les marqueurs
        googleMap.setOnMarkerClickListener(marker -> {
            // Récupérer le déchet associé au marqueur
            Dechet dechetSelectionne = trouverDechetParDescription(marker.getTitle());
            // Récupérez les informations du marqueur à partir de la balise
            Dechet markerInfo = (Dechet) marker.getTag();

            // Vérifiez si la balise contient des informations
            if (markerInfo != null) {
                // Utilisez les informations du marqueur comme nécessaire
                Log.d("Marker Clicked", "ID: " + markerInfo.getId() + ", Latitude: " + markerInfo.getLatitude() +
                        ", Longitude: " + markerInfo.getLongitude() + ", Description: " + markerInfo.getDescription());
            }

            // Afficher une boîte de dialogue ou exécuter l'action appropriée
            if (dechetSelectionne != null) {
               // Afficher les détails du déchet
                afficherDetailsDechet(dechetSelectionne);
            }

            return true;
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
            if (dechet.getDescription().equals(description)) {
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
                LatLng position = new LatLng(dechet.getLatitude(), dechet.getLongitude());
                googleMap.addMarker(new MarkerOptions().position(position).title(dechet.getDescription()));
            }
        }
    }
    // affichage des details du dechet selectionne
    private void afficherDetailsDechet(Dechet dechet) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Détails du Déchet")
                .setMessage(getString(R.string.dechetID) + getString(R.string.deuxPoints) + dechet.getId() + "\n"
                        + getString(R.string.dechetLatitude) + getString(R.string.deuxPoints) + dechet.getLatitude() + "\n"
                        + getString(R.string.dechetLongitude) + getString(R.string.deuxPoints) + dechet.getLongitude() + "\n"
                        + getString(R.string.dechetDescription) + getString(R.string.deuxPoints) + dechet.getDescription())
                .setPositiveButton(getString(R.string.delete), (dialog, which) -> {
                    // Appeler la méthode pour supprimer le déchet après confirmation
                    supprimerDechet(dechet);
                    afficherToast(getString(R.string.dechetDelete), R.color.red);
                })
                .setNegativeButton(getString(R.string.cancel), (dialog, which) -> dialog.dismiss())
                .show();
    }

    private final List<Button> boutonsTaille = new ArrayList<>();
    private final List<Button> boutonsDetails = new ArrayList<>();
    private final List<Button> boutonsDetails2 = new ArrayList<>();

    private void afficherPopup() {
        afficherPopup(0.0, 0.0);
    }
    private void afficherPopup(Double lat, Double Lng) {
        // Créer le constructeur de l'AlertDialog avec le contexte actuel
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Créer le layout principal pour le contenu de l'AlertDialog
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        // Ajouter le texte "Taille des déchets" au layout principal
        TextView tailleDesDechetsText = new TextView(this);
        tailleDesDechetsText.setText(getString(R.string.dechetSize));
        layout.addView(tailleDesDechetsText);

        // Créer un layout pour les boutons de taille
        LinearLayout layoutBoutons = createButtonLayout(boutonsTaille, getString(R.string.dechetSizeSmall), getString(R.string.dechetSizeMedium), getString(R.string.dechetSizeLarge));
        layout.addView(layoutBoutons);

        // Ajouter le texte "Détails" au layout principal
        TextView details = new TextView(this);
        details.setText(getString(R.string.dechetDetails));
        layout.addView(details);

        // Créer un layout pour les boutons de détails
        LinearLayout layoutBoutonsDetails = createButtonLayout(boutonsDetails, getString(R.string.dechetStings), getString(R.string.dechetBites), getString(R.string.dechetWet));
        layout.addView(layoutBoutonsDetails);

        // Créer un autre layout pour les boutons de détails
        LinearLayout layoutBoutonsDetails2 = createButtonLayout(boutonsDetails2, getString(R.string.dechetToxic), getString(R.string.dechetOrganic), getString(R.string.dechetElectronic));
        layout.addView(layoutBoutonsDetails2);

        // Ajouter un espace pour un adresse
        TextView positionLabel = new TextView(this);
        positionLabel.setText(getString(R.string.dechetPosition));
        layout.addView(positionLabel);

        // Ajouter des TextViews pour la latitude et la longitude
        TextView latitudeLabel = new TextView(this);
        latitudeLabel.setText(String.format("%s%s%s", getString(R.string.dechetLatitude), getString(R.string.deuxPoints), lat));
        layout.addView(latitudeLabel);

        TextView longitudeLabel = new TextView(this);
        longitudeLabel.setText(String.format("%s%s%s", getString(R.string.dechetLongitude), getString(R.string.deuxPoints), Lng));
        layout.addView(longitudeLabel);

        layoutImageView = new LinearLayout(this);
        layoutImageView.setVisibility(View.GONE);

        // Ajouter un bouton pour une prise de photo
        Button photoButton = new Button(this);
        photoButton.setText(getString(R.string.dechetPhoto));
        photoButton.setOnClickListener(v -> {
            // Appeler l'activité de la caméra
            Intent intent = new Intent(MapActivity.this, CameraActivity.class);
            startActivityForResult(intent, CAMERA_ACTIVITY_REQUEST_CODE);
        });
        layout.addView(photoButton);

        imageView = new ImageView(this);

        layoutImageView.setGravity(Gravity.CENTER); // Centrer l'ImageView

        // Définir la largeur et la hauteur du LinearLayout
        int layoutWidth = (int) (getResources().getDisplayMetrics().widthPixels * 0.4);
        int layoutHeight = (int) (getResources().getDisplayMetrics().heightPixels * 0.2);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(layoutWidth, layoutHeight);
        layoutImageView.setLayoutParams(layoutParams);

        layoutImageView.addView(imageView);
        layout.addView(layoutImageView);


        // Ajouter un espace pour un commentaire
        TextView commentaire = new TextView(this);
        commentaire.setText(getString(R.string.dechetComment));
        layout.addView(commentaire);

        // Ajouter un champ de saisie pour le commentaire
        TextInputLayout commentaireInputLayout = createCommentInputLayout();
        layout.addView(commentaireInputLayout);

        if (popupParameters != null) {
            // Parcourir la liste de paramètres et mettre en surbrillance les boutons appropriés
            for (String parametre : popupParameters.keySet()) {
                switch (parametre) {
                    case "Taille":
                        for (Button bouton : boutonsTaille) {
                            if (bouton.getText().toString().equals(popupParameters.get(parametre))) {
                                mettreEnSurbrillance(boutonsTaille, bouton);
                            }
                        }
                        break;
                    case "Détails":
                        for (Button bouton : boutonsDetails) {
                            if (bouton.getText().toString().equals(popupParameters.get(parametre))) {
                                mettreEnSurbrillance(boutonsDetails, bouton);
                            }
                        }
                        break;
                    case "Détails2":
                        for (Button bouton : boutonsDetails2) {
                            if (bouton.getText().toString().equals(popupParameters.get(parametre))) {
                                mettreEnSurbrillance(boutonsDetails2, bouton);
                            }
                        }
                        break;
                    case "Commentaire":
                        commentaireInputLayout.getEditText().setText(popupParameters.get(parametre));
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + parametre);
                }
            }
        }

        // Créer un layout pour les boutons "Pointer" et "Valider"
        LinearLayout layoutBoutonsValider = createButtonLayout(null, getString(R.string.dechetPoint), getString(R.string.dechetValidate));
        layout.addView(layoutBoutonsValider);

        // Définir la largeur et la hauteur de l'AlertDialog
        int dialogWidth = (int) (getResources().getDisplayMetrics().widthPixels * 0.8);
        int dialogHeight = (int) (getResources().getDisplayMetrics().heightPixels * 0.8);

        ScrollView scrollView = new ScrollView(this);
        scrollView.addView(layout);

        // Configurer le layout personnalisé pour l'AlertDialog
        builder.setView(scrollView);
        AlertDialog alertDialog = builder.create();

        // Afficher l'AlertDialog
        alertDialog.show();

        // Appliquer la largeur et la hauteur à l'AlertDialog
        alertDialog.getWindow().setLayout(dialogWidth, dialogHeight);
        // Récupérer le bouton "Valider" pour pouvoir ajouter un clic
        Button boutonValider = (Button) layoutBoutonsValider.getChildAt(1);
        // Ajouter un clic pour le bouton "Valider"
        boutonValider.setOnClickListener(v -> {
            onValiderClick(alertDialog);
            popupParameters.put("Taille", getBoutonSelectionne(boutonsTaille));
            popupParameters.put("Détails", getBoutonSelectionne(boutonsDetails));
            popupParameters.put("Détails2", getBoutonSelectionne(boutonsDetails2));
            popupParameters.put("Commentaire", commentaireInputLayout.getEditText().getText().toString());
            if (sharedPreferences != null) {
                sharedPreferences.edit().putString("image_path", "").apply();
            }
        });
        // Récupérer le bouton "Pointer" pour pouvoir ajouter un clic
        Button boutonPointer = (Button) layoutBoutonsValider.getChildAt(0);
        // Ajouter un clic pour le bouton "Pointer"
        boutonPointer.setOnClickListener(v -> {
            // Activez le mode "Pointer" et masquez le popup
            modePointer = true;
            popupParameters.put("Taille", getBoutonSelectionne(boutonsTaille));
            popupParameters.put("Détails", getBoutonSelectionne(boutonsDetails));
            popupParameters.put("Détails2", getBoutonSelectionne(boutonsDetails2));
            popupParameters.put("Commentaire", commentaireInputLayout.getEditText().getText().toString());
            alertDialog.dismiss();
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
        // Parcourir la liste de boutons et retirer la surbrillance mais pas le gris
        for (Button bouton : boutons) {
            bouton.setBackground(null);
        }

        // Mettre en surbrillance le bouton cliqué
        boutonClique.setBackgroundColor(getResources().getColor(R.color.highlightColor));
    }

    // Ajouter la méthode pour gérer le clic sur le bouton "Valider"
    private void onValiderClick(AlertDialog alert) {
        Dechet dernierDechetClique=null;
        String tailleSelectionnee = getBoutonSelectionne(boutonsTaille);
        String detailsSelectionnes = getBoutonSelectionne(boutonsDetails) + ", " + getBoutonSelectionne(boutonsDetails2);
        String commentaire = getCommentaire();
        String position = getPosition();

        // Faire quelque chose avec les informations récupérées
        Log.d("Valider", "Taille: " + tailleSelectionnee + ", Détails: " + detailsSelectionnes + ", Position: " + position + ", Commentaire: " + commentaire);

        // Ajouter le nouveau déchet à la liste
        int nouvelId = 0; // ID temporaire
        Dechet nouveauDechet = new Dechet(nouvelId, lastClickedLatitude, lastClickedLongitude,tailleSelectionnee ,detailsSelectionnes);
        listeDechets.add(nouveauDechet);
        // Stocker le dernier déchet cliqué
        dernierDechetClique = nouveauDechet;
        // Afficher un Toast avec les informations du déchet ajouté
        afficherToast(getString(R.string.dechetAdd) + getString(R.string.returnLine) + getString(R.string.dechetLatitude) + getString(R.string.deuxPoints) + lastClickedLatitude + getString(R.string.returnLine) + getString(R.string.dechetLongitude) + getString(R.string.deuxPoints) + lastClickedLongitude, R.color.green);
        // Vérifier les détails sélectionnés et afficher une boîte de dialogue d'alerte appropriée
        afficherAlerte(getString(R.string.dechetWarning));
        // Fermer le popup
        fermerPopup(alert);
    }
    // Méthode pour afficher une boîte de dialogue d'alerte standard
    private void afficherAlerte(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    // Code à exécuter lorsque l'utilisateur clique sur OK
                    /*
                        Evenement nouvelEvenement = new Evenement(
                                0,
                                " Recolting",
                                "Événement associé au déchet : " + dernierDechetClique.description,
                                 1,
                                lastClickedLatitude+" "+lastClickedLongitude,
                                "00/00/0000"
                        );

                        // Ajouter le nouvel événement à la liste
                        listeEvenements.add(nouvelEvenement);

                        afficherToast("Recolting ajouté avec succès", R.color.green);
                     */
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
        return getString(R.string.dechetLatitude) + getString(R.string.deuxPoints) + lastClickedLatitude + getString(R.string.coma) + getString(R.string.dechetLongitude) + getString(R.string.deuxPoints) + lastClickedLongitude;
    }


    // Méthode utilitaire pour récupérer le texte du champ de commentaire
    // je ne sers a rien pour toujours et a jamais
    private String getCommentaire() {
        // TextInputLayout commentaireInputLayout = findViewById(R.id.commentaireInputLayout);
        //  EditText commentaireEditText = commentaireInputLayout.findViewById(R.id.commentaireEditText);
        return ""; //commentaireEditText.getText().toString();
    }

    // Méthode pour récupérer le texte du bouton sélectionné
    private String getBoutonSelectionne(List<Button> boutons) {
        for (Button bouton : boutons) {
            ColorDrawable background = (ColorDrawable) bouton.getBackground();
            int colorId = 0;
            if (background != null) {
                colorId = background.getColor();
            }
            int colorCible = ContextCompat.getColor(this, R.color.highlightColor);
            if (colorId == colorCible) {
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

    protected void addZoneDechet(Dechet unDechet){

        try {
            RequestQueue queue;
            queue = Volley.newRequestQueue(this);
            queue.start();

            JSONObject objetJSON = new JSONObject();
            objetJSON.put("id",unDechet.getId());
            objetJSON.put("latitude",unDechet.getLatitude());
            objetJSON.put("longitude",unDechet.getLongitude());
            objetJSON.put("taille",unDechet.getTaille());
            objetJSON.put("description",unDechet.getDescription());
            objetJSON.put("idEvenement","3");
            objetJSON.put("idCollecte","2");
            objetJSON.put("idUtilisateur","1");

            Log.d("JSON",objetJSON.toString());

            String url = "https://deching.alwaysdata.net/actions/Dechet.php";
            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST,url, objetJSON, reponse -> {// code à exécuter une fois les données chargées
                Log.d("retourHTTPAddDechet",reponse.toString());
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {// code à exécuter lorsqu'une erreur de communication // avec le serveur est détectée
                        }
                    }) {
            };
            queue.add(jsonRequest);
            Log.d("JsonRequest",jsonRequest.toString());
        }catch (Exception exception){
            Log.d("erreurHttp",exception.getMessage());
        }

    }
    //Méthode qui permet de récupérer tous les déchets enregistrés en base de données sous forme de collection
    protected void getAllZoneDechet(final VolleyCallback callback){

        RequestQueue queue;
        queue = Volley.newRequestQueue(this);
        queue.start();

        ArrayList<Dechet> dechetsRecuperes = new ArrayList<Dechet>();

        String url = "https://deching.alwaysdata.net/actions/Dechet.php";
        JsonArrayRequest jsonRequest = new JsonArrayRequest( url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray reponse) {// code à exécuter une fois les données chargées
                Log.d("retourHTTPGetALLDechet",reponse.toString());
                for(int i=0;i < reponse.length();i++){
                    try {
                        JSONObject unDechetJSON = reponse.getJSONObject(i);
                        int id = unDechetJSON.getInt("id");
                        double latitude = unDechetJSON.getDouble("latitude");
                        double longitude = unDechetJSON.getDouble("longitude");
                        String taille = unDechetJSON.getString("taille");
                        String description = unDechetJSON.getString("description");

                        Dechet unDechet = new Dechet(id,latitude,longitude,taille,description);
                        Log.d("dechetRecup"+i,unDechet.toString());

                        dechetsRecuperes.add(unDechet);
                        Log.d("dechetsRecup",dechetsRecuperes.toString());

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
                callback.onSuccess(dechetsRecuperes);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {// code à exécuter lorsqu'une erreur de communication // avec le serveur est détectée
                    }
                }) {
        };
        queue.add(jsonRequest);
    }
}

