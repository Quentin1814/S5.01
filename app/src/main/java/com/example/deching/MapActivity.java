package com.example.deching;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.deching.Modele.Modele.Dechet;
import com.example.deching.Modele.Modele.Evenement;
import com.example.deching.utilitaire.VolleyCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private FusedLocationProviderClient fusedLocationProviderClient;
    // ajouter des variables pour recuperer les coordonnees d'un marqueur quand on clic sur la carte
    private double lastClickedLatitude;
    private double lastClickedLongitude;
    // Ajoutez une variable pour suivre l'état actuel quand on clic sur le button "Pointer" du popup
    private boolean modePointer = false;
    // Ajoutez une variable pour stocker les paramètres du popup
    private Map<String, String> popupParameters = new HashMap<>();
    //creation d'un dechet instantane pour la suppression d'un dechet reference


    private final List<Dechet> listeDechets = new ArrayList<>();

    private final ArrayList<Dechet> listeDechetsInit = new ArrayList<>();
    // creation d'un evenement instantane
    protected static final List<Evenement> listeEvenements = new ArrayList<>();
    //pour recuperer les imformation du derniere point creer afin de creer un evenement

    private GoogleMap googleMap;  // Déplacez la déclaration ici pour qu'elle soit accessible à toutes les méthodes

    private static final int CAMERA_ACTIVITY_REQUEST_CODE = 100;
    // Créer un LinearLayout pour centrer l'ImageView


    private SharedPreferences sharedPreferences;
    ImageButton boutonPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Button boutonSignaler;
        ImageButton boutonEvent;
        ImageButton boutonHome;
        ImageButton boutonLogo;

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Récupérer tous les frame layouts
        FrameLayout layoutMap = findViewById(R.id.map);
        ScrollView layoutPopup = findViewById(R.id.popup);
        layoutMap.setVisibility(View.VISIBLE);
        layoutPopup.setVisibility(View.GONE);

        // Chargement des données de déchets récupérées dasn le base de données
        getAllZoneDechet(listeDechetsInit::addAll);

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            // change le src de l'imageButtonPosition
            boutonPosition = findViewById(R.id.imageButtonPosition);
            boutonPosition.setImageResource(R.drawable.position_clicked);
            // Permission was granted, launch the runnable
            handler.post(runnableCode);
        }
    }

    private final Handler handler = new Handler();
    private final Runnable runnableCode = new Runnable() {
        @Override
        public void run() {
            centerMapOnMyLocation();
            handler.postDelayed(this, 4000);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        handler.post(runnableCode);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnableCode);
        // change le src de l'imageButtonPosition
        boutonPosition = findViewById(R.id.imageButtonPosition);
        boutonPosition.setImageResource(R.drawable.position_not_clicked);
    }

    @SuppressLint("MissingPermission")
    private void centerMapOnMyLocation() {
        // Récupérer la dernière position connue
        fusedLocationProviderClient.getCurrentLocation(100, null).addOnSuccessListener(this, location -> {
            if (location != null) {
                // Créer un objet LatLng à partir de la dernière position connue
                LatLng lastKnownLocation = new LatLng(location.getLatitude(), location.getLongitude());
                // Centrer la caméra sur la dernière position connue
                animateCameraToPosition(googleMap, lastKnownLocation);
            }
        });
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
            googleMap.addMarker(new MarkerOptions().position(position).title(dechet.toString()));
        }

        // Centrer la caméra sur la première position (si elle existe)
        if (!listeDechets.isEmpty()) {
            LatLng premierDechet = new LatLng(listeDechets.get(0).getLatitude(), listeDechets.get(0).getLongitude());
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(premierDechet, 10));
        }

        boutonPosition.setOnClickListener(v -> onRequestPermissionsResult(1, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, new int[]{PackageManager.PERMISSION_GRANTED}));

        // Add a listener for when the user starts to move the camera
        googleMap.setOnCameraMoveStartedListener(reason -> {
            if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
                // The user manually moves the camera, stop the Runnable
                onPause();
            }
        });

        // Ajouter un listener de clic sur les marqueurs
        googleMap.setOnMarkerClickListener(marker -> {
            // Récupérer le déchet associé au marqueur
            Dechet dechetSelectionne = trouverDechetParToString(marker.getTitle());
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
    private Dechet trouverDechetParToString(String toStringDechet) {
        for (Dechet dechet : listeDechets) {
            if (dechet.toString().equals(toStringDechet)) {
                return dechet;
            }
        }
        return null;
    }

    // Méthode pour afficher des marqueurs sur la carte
    private void afficherMarqueursSurCarte() {
        if (googleMap != null) {
            googleMap.clear(); // Efface les marqueurs existants

            // Ajouter des marqueurs pour chaque déchet dans la liste mise à jour
            for (Dechet dechet : listeDechets) {
                LatLng position = new LatLng(dechet.getLatitude(), dechet.getLongitude());
                googleMap.addMarker(new MarkerOptions().position(position).title(dechet.toString()));
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
                //methode pour partager un dechet
                .setNeutralButton("Partager", (dialog, which) -> {
                    String uri = "http://deching/dechet?id=" + dechet.getId();
                    Log.d("lien",uri);
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Détails du Déchet");
                    intent.putExtra(Intent.EXTRA_TEXT, uri);
                    startActivity(Intent.createChooser(intent, "Partager via"));
                })
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            ImageView displayPhoto = findViewById(R.id.dechetDisplayPhoto);
            afficherToast(getString(R.string.dechetPhoto), R.color.green);

            // Récupérer le chemin de l'image de SharedPreferences
            sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
            String imagePath = sharedPreferences.getString(getString(R.string.image_path), "");

            // Utiliser le chemin de l'image pour afficher l'image
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);

            // Tourner l'image de 90 degrés
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

            // Redimensionner l'image pour s'ajuster à la taille de displayPhoto
            int maxWidth = displayPhoto.getWidth();
            int maxHeight = displayPhoto.getHeight();

            if (maxWidth > 0 && maxHeight > 0) {
                float scale = Math.min(((float) maxWidth) / rotatedBitmap.getWidth(), ((float) maxHeight) / rotatedBitmap.getHeight());

                Matrix resizeMatrix = new Matrix();
                resizeMatrix.postScale(scale, scale);

                Bitmap resizedBitmap = Bitmap.createBitmap(rotatedBitmap, 0, 0, rotatedBitmap.getWidth(), rotatedBitmap.getHeight(), resizeMatrix, true);

                // Afficher l'image redimensionnée
                displayPhoto.setImageBitmap(resizedBitmap);
            } else {
                // Si les dimensions de displayPhoto ne sont pas encore connues, utilisez simplement la bitmap telle quelle
                displayPhoto.setImageBitmap(rotatedBitmap);
            }
        }
    }

    private final List<Button> boutonsTaille = new ArrayList<>();
    private final List<Button> boutonsDetails = new ArrayList<>();
    private final List<Button> boutonsDetails2 = new ArrayList<>();

    private void afficherPopup() {
        afficherPopup(0.0, 0.0);
    }
    private void afficherPopup(Double lat, Double Lng) {
        // Récupérer tous les frame layouts
        FrameLayout layoutMap = findViewById(R.id.map);
        ScrollView layoutPopup = findViewById(R.id.popup);
        // Rendre le layout de la carte invisible
        layoutMap.setVisibility(View.GONE);

        // Rendre le layout du popup visible
        layoutPopup.setVisibility(View.VISIBLE);

        // Récupérer tous les boutons du popup
        ImageButton close = findViewById(R.id.dechetClose);
        Button petit = findViewById(R.id.dechetSizeSmall);
        Button moyen = findViewById(R.id.dechetSizeMedium);
        Button grand = findViewById(R.id.dechetSizeLarge);
        Button piquant = findViewById(R.id.dechetStings);
        Button mordant = findViewById(R.id.dechetBites);
        Button mouille = findViewById(R.id.dechetWet);
        Button toxique = findViewById(R.id.dechetToxic);
        Button organique = findViewById(R.id.dechetOrganic);
        Button electronique = findViewById(R.id.dechetElectronic);
        TextView latitude = findViewById(R.id.latitude);
        TextView longitude = findViewById(R.id.longitude);
        Button takePhoto = findViewById(R.id.dechetPhoto);
        ImageView displayPhoto = findViewById(R.id.dechetDisplayPhoto);
        EditText commentaire = findViewById(R.id.dechetComment2);
        Button pointer = findViewById(R.id.dechetPoint);
        Button valider = findViewById(R.id.dechetValidate);

        close.setOnClickListener(v -> {
            // Rendre le layout de la carte visible
            layoutMap.setVisibility(View.VISIBLE);
            // Rendre le layout du popup invisible
            layoutPopup.setVisibility(View.GONE);
            popupParameters = null;
        });

        latitude.setText(lat.toString());
        longitude.setText(Lng.toString());

        // Ajouter tous les boutons à la liste de boutons
        boutonsTaille.add(petit);
        boutonsTaille.add(moyen);
        boutonsTaille.add(grand);

        boutonsDetails.add(piquant);
        boutonsDetails.add(mordant);
        boutonsDetails.add(mouille);

        boutonsDetails2.add(toxique);
        boutonsDetails2.add(organique);
        boutonsDetails2.add(electronique);

        // Ajout des clics pour chaque bouton de taille
        configurerBouton(petit, boutonsTaille, R.string.dechetSizeSmall, R.color.green);
        configurerBouton(moyen, boutonsTaille, R.string.dechetSizeMedium, R.color.green);
        configurerBouton(grand, boutonsTaille, R.string.dechetSizeLarge, R.color.green);

        // Ajout des clics pour chaque bouton de détails
        configurerBouton(piquant, boutonsDetails, R.string.dechetStings, R.color.green);
        configurerBouton(mordant, boutonsDetails, R.string.dechetBites, R.color.green);
        configurerBouton(mouille, boutonsDetails, R.string.dechetWet, R.color.green);

        // Ajout des clics pour chaque bouton de détails 2
        configurerBouton(toxique, boutonsDetails2, R.string.dechetToxic, R.color.green);
        configurerBouton(organique, boutonsDetails2, R.string.dechetOrganic, R.color.green);
        configurerBouton(electronique, boutonsDetails2, R.string.dechetElectronic, R.color.green);

        takePhoto.setOnClickListener(v -> {
            // Appeler l'activité de la caméra
            Intent intent = new Intent(MapActivity.this, CameraActivity.class);
            startActivityForResult(intent, CAMERA_ACTIVITY_REQUEST_CODE);
        });

        if (popupParameters != null) {
            highlightButtons("Taille", popupParameters, boutonsTaille);
            highlightButtons("Détails", popupParameters, boutonsDetails);
            highlightButtons("Détails2", popupParameters, boutonsDetails2);

            if (popupParameters.containsKey("Photo")) {
                displayPhotoFromSharedPreferences(displayPhoto);
            }

            if (popupParameters.containsKey("Commentaire")) {
                setCommentaireText(commentaire, popupParameters.get("Commentaire"));
            }
        }

        // Ajouter un clic pour le bouton "Valider"
        valider.setOnClickListener(v -> {
            popupParameters.put("Taille", getBoutonSelectionne(boutonsTaille));
            popupParameters.put("Détails", getBoutonSelectionne(boutonsDetails));
            popupParameters.put("Détails2", getBoutonSelectionne(boutonsDetails2));
            popupParameters.put("Commentaire", commentaire.getText().toString());
            if (sharedPreferences != null) {
                sharedPreferences.edit().putString(getString(R.string.image_path), "").apply();
            }
            onValiderClick();
            reinitialiserBoutons(boutonsTaille);
            reinitialiserBoutons(boutonsDetails);
            reinitialiserBoutons(boutonsDetails2);
            displayPhoto.setImageResource(0);
            commentaire.setText("");
            // Rendre le layout de la carte visible
            layoutMap.setVisibility(View.VISIBLE);
            // Rendre le layout du popup invisible
            layoutPopup.setVisibility(View.GONE);
        });

        // Ajouter un clic pour le bouton "Pointer"
        pointer.setOnClickListener(v -> {
            // Activez le mode "Pointer" et masquez le popup
            modePointer = true;
            popupParameters.put("Taille", getBoutonSelectionne(boutonsTaille));
            popupParameters.put("Détails", getBoutonSelectionne(boutonsDetails));
            popupParameters.put("Détails2", getBoutonSelectionne(boutonsDetails2));
            popupParameters.put("Commentaire", commentaire.getText().toString());
            // Rendre le layout de la carte visible
            layoutMap.setVisibility(View.VISIBLE);
            // Rendre le layout du popup invisible
            layoutPopup.setVisibility(View.GONE);
        });

    }

    // Méthode générique pour gérer le clic des boutons du popup
    private void configurerBouton(Button bouton, List<Button> boutons, int messageResId, int couleurResId) {
        bouton.setOnClickListener(v -> {
            mettreEnSurbrillance(boutons, bouton);
            afficherToast(getString(messageResId), couleurResId);
        });
    }

    private void highlightButtons(String parameter, Map<String, String> parameters, List<Button> buttons) {
        if (parameters.containsKey(parameter)) {
            String paramValue = parameters.get(parameter);
            for (Button button : buttons) {
                if (button.getText().toString().equals(paramValue)) {
                    mettreEnSurbrillance(buttons, button);
                    break;
                }
            }
        }
    }

    private void displayPhotoFromSharedPreferences(ImageView displayPhoto) {
        sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String imagePath = sharedPreferences.getString(getString(R.string.image_path), "");
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        displayPhoto.setImageBitmap(bitmap);
    }

    private void setCommentaireText(EditText commentaire, String commentaireText) {
        commentaire.setText(commentaireText);
    }

    // Ajouter la méthode pour gérer le clic sur le bouton "Valider"
    private void onValiderClick() {
        String tailleSelectionnee = popupParameters.get("Taille");
        String detailsSelectionnes = popupParameters.get("Details") + ", " + popupParameters.get("Details2");
        String commentaire = popupParameters.get("Commentaire");
        String position = getPosition();

        // Faire quelque chose avec les informations récupérées
        Log.d("Valider", "Taille: " + tailleSelectionnee + ", Détails: " + detailsSelectionnes + ", Position: " + position + ", Commentaire: " + commentaire);

        // Ajouter le nouveau déchet à la liste
        Dechet nouveauDechet = new Dechet( lastClickedLatitude, lastClickedLongitude, tailleSelectionnee, commentaire);
        listeDechets.add(nouveauDechet);
        addZoneDechet(nouveauDechet);
        afficherMarqueursSurCarte();
        // Afficher un Toast avec les informations du déchet ajouté
        afficherToast(getString(R.string.dechetAdd) + getString(R.string.returnLine) + getString(R.string.dechetLatitude) + getString(R.string.deuxPoints) + lastClickedLatitude + getString(R.string.returnLine) + getString(R.string.dechetLongitude) + getString(R.string.deuxPoints) + lastClickedLongitude, R.color.green);
        // Vérifier les détails sélectionnés et afficher une boîte de dialogue d'alerte appropriée
        afficherAlerte(getString(R.string.dechetWarning));
        popupParameters = null;
    }

    private void mettreEnSurbrillance(List<Button> boutons, Button boutonClique) {
        // Parcourir la liste de boutons et retirer la surbrillance mais pas le gris
        reinitialiserBoutons(boutons);

        // Mettre en surbrillance le bouton cliqué
        boutonClique.setBackgroundColor(getResources().getColor(R.color.green));
        // Ajoute un etat pour savoir rapidement si le bouton est selectionne
        boutonClique.setSelected(true);
    }

    private void reinitialiserBoutons(List<Button> boutons) {
        for (Button bouton : boutons) {
            bouton.setBackgroundColor(getResources().getColor(R.color.light_gray));
            bouton.setSelected(false);
        }
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

    private String getPosition() {
        return getString(R.string.dechetLatitude) + getString(R.string.deuxPoints) + lastClickedLatitude + getString(R.string.coma) + getString(R.string.dechetLongitude) + getString(R.string.deuxPoints) + lastClickedLongitude;
    }

    // Méthode pour récupérer le texte du bouton sélectionné
    private String getBoutonSelectionne(List<Button> boutons) {
        for (Button bouton : boutons) {
            // Vérifier si le bouton est sélectionné
            if (bouton.isSelected()) {
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

    //Méthode qui permet d'ajouter en base de données un déchet créé sur la map
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

            String url = "https://deching.alwaysdata.net/actions/Dechet.php";
            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST,url, objetJSON, reponse -> {
            },
                error -> {
                }) {
            };
            queue.add(jsonRequest);
        } catch (Exception exception){
            Log.d("erreurHttp", Objects.requireNonNull(exception.getMessage()));
        }

    }
    //Méthode qui permet de récupérer tous les déchets enregistrés en base de données sous forme de collection
    protected void getAllZoneDechet(final VolleyCallback callback){
        RequestQueue queue;
        queue = Volley.newRequestQueue(this);
        queue.start();

        ArrayList<Dechet> dechetsRecuperes = new ArrayList<>();

        String url = "https://deching.alwaysdata.net/actions/Dechet.php";
        JsonArrayRequest jsonRequest = new JsonArrayRequest( url, reponse -> {
            // code à exécuter une fois les données chargées
            for(int i=0;i < reponse.length();i++){
                try {
                    JSONObject unDechetJSON = reponse.getJSONObject(i);
                    int id = unDechetJSON.getInt("id");
                    double latitude = unDechetJSON.getDouble("latitude");
                    double longitude = unDechetJSON.getDouble("longitude");
                    String taille = unDechetJSON.getString("taille");
                    String description = unDechetJSON.getString("description");

                    Dechet unDechet = new Dechet(id,latitude,longitude,taille,description);

                    dechetsRecuperes.add(unDechet);

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
            callback.onSuccess(dechetsRecuperes);
        },
                error -> {
                    // code à exécuter lorsqu'une erreur de communication avec le serveur est détectée
                    Log.e("erreur récupération des déchets", Objects.requireNonNull(error.getMessage()));
                }) {
        };
        queue.add(jsonRequest);
    }
    //Méthode qui permet de supprimer un déchet enregistré en base de données via son identifiant
    protected void deleteZoneDechet(Dechet unDechet){

        try {
            RequestQueue queue;
            queue = Volley.newRequestQueue(this);
            queue.start();

            String url = "https://deching.alwaysdata.net/actions/Dechet.php?id="+unDechet.getId();
            StringRequest stringRequest = new StringRequest(Request.Method.DELETE,url, reponse -> {
            },
                    error -> {
                    }) {
            };
            queue.add(stringRequest);
        } catch (Exception exception){
            Log.d("erreurHttp", Objects.requireNonNull(exception.getMessage()));
        }

    }
}

