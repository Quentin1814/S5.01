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

/**
 * Classe représentant la page de la carte
 */
public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    /**
     * Client pour accéder aux services de localisation fusionnés fournis par Google Play services.
     * Utilisé pour obtenir des informations sur la position géographique de l'appareil.
     */
    private FusedLocationProviderClient fusedLocationProviderClient;

    /**
     * Latitude du marqueur quand on clique sur la carte
     */
    private double lastClickedLatitude;

    /**
     * Longitude du marqueur quand on clique sur la carte
     */
    private double lastClickedLongitude;

    /**
     * Suivre l'état actuel quand on clic sur le button "Pointer" du popup
     */
    private boolean modePointer = false;

    /**
     * Stocker les paramètres du popup
     */
    private Map<String, String> popupParameters = new HashMap<>();

    /**
     * Création d'une liste de déchet instantané pour la suppression d'un déchet référence
     */
    private final List<Dechet> listeDechets = new ArrayList<>();

    /**
     * Création d'un déchet instantané pour la suppression d'un déchet référence
     */
    private final ArrayList<Dechet> listeDechetsInit = new ArrayList<>();

    /**
     * Récuperer les imformations du dernier point créé afin de créer un événement
     */
    protected static final List<Evenement> listeEvenements = new ArrayList<>();

    /**
     * Déplacez la déclaration ici pour qu'elle soit accessible à toutes les méthodes
     */
    private GoogleMap googleMap;

    /**
     * Code de requête pour démarrer l'activité de la caméra.
     * Cette constante est utilisée pour identifier la demande de résultat lorsque l'activité de la caméra est terminée.
     */
    private static final int CAMERA_ACTIVITY_REQUEST_CODE = 100;

    /**
     * Gestionnaire pour les préférences partagées de l'application.
     * Permet de stocker et récupérer des données de manière persistante à travers les sessions de l'application.
     */
    private SharedPreferences sharedPreferences;

    /**
     * Bouton permettant d'accéder à la position actuelle de l'appareil.
     * En cliquant sur ce bouton, l'utilisateur peut obtenir des informations sur sa position géographique.
     */
    ImageButton boutonPosition;

    /**
     * Méthode appelée lors de la création de l'activité.
     *
     * @param savedInstanceState données permettant de reconstruire l'activité lorsqu'elle est recréée, si null alors aucune donnée n'est disponible.
     */
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

    /**
     * Demande l'autorisation d'accéder à la localisation de l'appareil.
     * Vérifie si les autorisations nécessaires pour accéder à la localisation fine (précise) et à la localisation grossière (approximative)
     * ont été accordées. Si les autorisations ne sont pas accordées, la méthode demande à l'utilisateur de les accorder.
     * Une fois les autorisations demandées, la méthode appelle la méthode {@link #onRequestPermissionsResult(int, String[], int[])} pour gérer le résultat.
     */
    private void askAuthorisation() {
        // Vérifie si l'autorisation d'accéder à la localisation fine n'est pas accordée
        if (ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Demande la permission d'accéder à la localisation fine
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        // Vérifie si l'autorisation d'accéder à la localisation grossière n'est pas accordée
        else if (ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Demande la permission d'accéder à la localisation grossière
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
        // Appelle onRequestPermissionsResult pour gérer le résultat de la demande d'autorisation
        onRequestPermissionsResult(1, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, new int[]{PackageManager.PERMISSION_GRANTED});
    }


    /**
     * Méthode appelée lorsque l'utilisateur répond à une demande d'autorisation.
     * Cette méthode est invoquée automatiquement en réponse à une demande d'autorisation précédente.
     * Elle vérifie si la demande correspond à celle effectuée par {@link #askAuthorisation()}.
     * Si la permission est accordée, elle modifie l'image du bouton de position pour indiquer l'accès à la localisation,
     * puis lance un processus asynchrone pour obtenir la position actuelle de l'appareil.
     *
     * @param requestCode  Le code de demande de permission précédemment passé à {@link #askAuthorisation()}.
     * @param permissions  Les permissions demandées.
     * @param grantResults Les résultats des demandes de permission, indiquant si chaque permission a été accordée ou non.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Vérifie si la demande de permission correspond à celle effectuée par askAuthorisation() et si la permission est accordée
        if (requestCode == 1 && (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            // Change l'image du bouton de position pour indiquer l'accès à la localisation
            boutonPosition = findViewById(R.id.imageButtonPosition);
            boutonPosition.setImageResource(R.drawable.position_clicked);
            // La permission a été accordée, lance le processus asynchrone pour obtenir la position actuelle de l'appareil
            handler.post(runnableCode);
            handlerMap.post(runnableMap);
            handlerMap.post(runnableDechet);
        }
    }

    private final Handler handlerMap = new Handler();

    private final Runnable runnableMap = new Runnable() {
        @Override
        public void run() {
            // Mets à jour la map pour les déchets
            updateMap();
            handler.postDelayed(this, 10000);
        }
    };

    private void updateMap() {
        if (!listeDechets.isEmpty()) {
            googleMap.clear();
            // Ajouter des marqueurs pour chaque déchet dans la liste
            for (Dechet dechet : listeDechets) {
                LatLng position = new LatLng(dechet.getLatitude(), dechet.getLongitude());
                googleMap.addMarker(new MarkerOptions().position(position).title(dechet.toString()));
            }
        }
    }

    private final Runnable runnableDechet = new Runnable() {
        @Override
        public void run() {
            reinitialiseListeDechets();
            handler.postDelayed(this, 4000);
        }
    };
    private void reinitialiseListeDechets(){
        listeDechets.clear();
        getAllZoneDechet(listeDechets::addAll);
    }

    /**
     * Gestionnaire utilisé pour programmer et exécuter des actions différées dans le thread de l'interface utilisateur.
     * Utilisé dans ce contexte pour exécuter périodiquement une tâche pour mettre à jour la carte avec la position de l'utilisateur.
     */
    private final Handler handler = new Handler();

    /**
     * Tâche exécutable périodiquement par le gestionnaire d'actions différées.
     * Cette tâche est responsable de centrer la carte sur la position actuelle de l'utilisateur et de planifier son exécution future.
     * Elle est exécutée à intervalles réguliers pour maintenir la mise à jour de la position de l'utilisateur sur la carte.
     */
    private final Runnable runnableCode = new Runnable() {
        @Override
        public void run() {
            // Centre la carte sur la position de l'utilisateur
            centerMapOnMyLocation();
            // Planifie l'exécution de cette tâche après un délai de 4000 millisecondes (4 secondes)
            handler.postDelayed(this, 4000);
        }
    };

    /**
     * Méthode appelée lorsque l'activité entre dans l'état "reprise".
     * Cela se produit après que l'activité a été arrêtée ou masquée et revient à l'avant-plan.
     * Dans cette méthode, le gestionnaire d'actions différées est utilisé pour planifier l'exécution périodique de la tâche {@link #runnableCode},
     * qui met à jour la carte avec la position de l'utilisateur.
     */
    @Override
    protected void onResume() {
        super.onResume();
        // Planifie l'exécution périodique de la tâche pour mettre à jour la carte avec la position de l'utilisateur
        handler.post(runnableCode);
    }

    /**
     * Méthode appelée lorsque l'activité entre dans l'état "pause".
     * Cela se produit lorsque l'activité n'est plus en premier plan, par exemple lorsqu'elle est masquée par une autre activité.
     * Dans cette méthode, l'exécution de la tâche périodique {@link #runnableCode} est arrêtée,
     * et l'image du bouton de position est réinitialisée pour indiquer que la localisation n'est pas activée.
     */
    @Override
    protected void onPause() {
        super.onPause();
        // Arrête l'exécution périodique de la tâche pour mettre à jour la carte avec la position de l'utilisateur
        handler.removeCallbacks(runnableCode);
        // Change l'image du bouton de position pour indiquer que la localisation n'est pas activée
        boutonPosition = findViewById(R.id.imageButtonPosition);
        boutonPosition.setImageResource(R.drawable.position_not_clicked);
    }

    /**
     * Centre la carte sur la dernière position connue de l'utilisateur en récupérant sa position actuelle.
     * Utilise le fournisseur de localisation fusionnée (FusedLocationProviderClient) pour obtenir la dernière position.
     * Une fois la position récupérée, la caméra est animée pour centrer la carte sur cette position.
     */
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

    /**
     * Anime la caméra vers une nouvelle position sur la carte avec une transition fluide.
     *
     * @param map         La carte Google Maps sur laquelle l'animation doit être effectuée.
     * @param newPosition La nouvelle position à laquelle la caméra doit être centrée.
     */
    private void animateCameraToPosition(GoogleMap map, LatLng newPosition) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(newPosition)   // Sets the new camera position
                .zoom(15f)             // Sets the zoom
                // .bearing(90)        // Optional
                // .tilt(40)           // Optional
                .build();              // Creates a CameraPosition from the builder

        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 2000, null);
    }

    /**
     * Méthode appelée lorsque la carte Google est prête à être utilisée.
     * Initialise la carte avec des marqueurs et des écouteurs appropriés.
     *
     * @param googleMap L'objet GoogleMap qui représente la carte Google.
     */
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


    /**
     * Méthode pour supprimer un déchet de la liste (simulée)
     * @param dechet
     */
    private void supprimerDechet(Dechet dechet) {
        listeDechets.remove(dechet);
        deleteZoneDechet(dechet);
        // Mettez à jour l'affichage sur la carte
        afficherMarqueursSurCarte();
    }


    /**
     * Méthode pour trouver un déchet par sa description (simulée)
     * @param toStringDechet
     * @return
     */
    private Dechet trouverDechetParToString(String toStringDechet) {
        for (Dechet dechet : listeDechets) {
            if (dechet.toString().equals(toStringDechet)) {
                return dechet;
            }
        }
        return null;
    }

    /**
     * Méthode pour afficher des marqueurs sur la carte
     */
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

    /**
     * Méthode qui permet l'affichage des details du dechet selectionne
     * @param dechet
     */
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

    /**
     * Méthode appelée lorsque l'activité reçoit le résultat d'une activité de caméra lancée pour prendre une photo.
     * Affiche la photo capturée dans une image view et effectue des opérations de traitement sur celle-ci.
     *
     * @param requestCode Le code de demande d'activité utilisé pour identifier la requête de caméra.
     * @param resultCode  Le code de résultat indiquant le succès ou l'échec de l'activité de caméra.
     * @param data        Les données associées au résultat de l'activité de caméra, telles que l'image capturée.
     */
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

    /**
     * Liste des différentes tailles des déchets
     */
    private final List<Button> boutonsTaille = new ArrayList<>();

    /**
     * Liste des différents détails des déchets
     */
    private final List<Button> boutonsDetails = new ArrayList<>();

    /**
     * Liste des différents détails des déchets
     */
    private final List<Button> boutonsDetails2 = new ArrayList<>();

    /**
     * Méthode qui permet d'afficher le popup sur la carte
     */
    private void afficherPopup() {
        afficherPopup(0.0, 0.0);
    }

    /**
     * Méthode qui permet d'afficher la popup sur la carte à un endroit précis
     * @param lat
     * @param Lng
     */
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

    /**
     * Méthode générique pour configurer le clic des boutons du popup.
     *
     * @param bouton        Le bouton sur lequel configurer le clic.
     * @param boutons       La liste de tous les boutons du popup.
     * @param messageResId  L'ID de ressource du message à afficher lors du clic sur le bouton.
     * @param couleurResId  L'ID de ressource de la couleur de fond du Toast affiché lors du clic sur le bouton.
     */
    private void configurerBouton(Button bouton, List<Button> boutons, int messageResId, int couleurResId) {
        bouton.setOnClickListener(v -> {
            mettreEnSurbrillance(boutons, bouton);
            afficherToast(getString(messageResId), couleurResId);
        });
    }

    /**
     * Met en surbrillance un bouton spécifique dans une liste de boutons en fonction de la valeur d'un paramètre donné.
     *
     * @param parameter   Le nom du paramètre pour lequel la valeur est vérifiée.
     * @param parameters  La carte de paramètres contenant les valeurs associées à chaque paramètre.
     * @param buttons     La liste de boutons parmi lesquels rechercher et mettre en surbrillance le bouton.
     */
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

    /**
     * Affiche une photo à partir des préférences partagées dans un ImageView spécifié.
     *
     * @param displayPhoto L'ImageView dans lequel afficher la photo.
     */
    private void displayPhotoFromSharedPreferences(ImageView displayPhoto) {
        sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String imagePath = sharedPreferences.getString(getString(R.string.image_path), "");
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        displayPhoto.setImageBitmap(bitmap);
    }

    /**
     * Définit le texte d'un EditText avec le texte spécifié.
     *
     * @param commentaire     L'EditText dans lequel définir le texte.
     * @param commentaireText Le texte à définir dans l'EditText.
     */
    private void setCommentaireText(EditText commentaire, String commentaireText) {
        commentaire.setText(commentaireText);
    }

    /**
     * Méthode pour gérer le clic sur le bouton "Valider"
     */
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

    /**
     * Met en surbrillance un bouton spécifié dans une liste de boutons en ajustant ses couleurs d'arrière-plan.
     *
     * @param boutons       La liste de boutons dans laquelle rechercher le bouton à mettre en surbrillance.
     * @param boutonClique  Le bouton à mettre en surbrillance.
     */
    private void mettreEnSurbrillance(List<Button> boutons, Button boutonClique) {
        // Parcourir la liste de boutons et retirer la surbrillance mais pas le gris
        reinitialiserBoutons(boutons);

        // Mettre en surbrillance le bouton cliqué
        boutonClique.setBackgroundColor(getResources().getColor(R.color.green));
        // Ajouter un état pour savoir rapidement si le bouton est sélectionné
        boutonClique.setSelected(true);
    }

    /**
     * Réinitialise les couleurs d'arrière-plan de tous les boutons spécifiés dans une liste de boutons.
     *
     * @param boutons   La liste de boutons à réinitialiser.
     */
    private void reinitialiserBoutons(List<Button> boutons) {
        for (Button bouton : boutons) {
            bouton.setBackgroundColor(getResources().getColor(R.color.light_gray));
            bouton.setSelected(false);
        }
    }

    /**
     * Affiche une boîte de dialogue d'alerte standard avec un message spécifié et un bouton "OK".
     *
     * @param message Le message à afficher dans la boîte de dialogue.
     */
    private void afficherAlerte(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    // Code à exécuter lorsque l'utilisateur clique sur OK
                /*
                    // Exemple de code à exécuter lors du clic sur OK :
                    Evenement nouvelEvenement = new Evenement(
                            0,
                            "Recolting",
                            "Événement associé au déchet : " + dernierDechetClique.description,
                             1,
                            lastClickedLatitude + " " + lastClickedLongitude,
                            "00/00/0000"
                    );

                    // Ajouter le nouvel événement à la liste
                    listeEvenements.add(nouvelEvenement);

                    // Afficher un Toast indiquant que Recolting a été ajouté avec succès
                    afficherToast("Recolting ajouté avec succès", R.color.green);
                 */
                })
                .show();
    }

    /**
     * Retourne une chaîne de caractères représentant la position actuelle, avec les coordonnées de latitude et de longitude.
     *
     * @return Une chaîne de caractères représentant la position actuelle sous la forme "Latitude: <latitude>, Longitude: <longitude>".
     */
    private String getPosition() {
        return getString(R.string.dechetLatitude) + getString(R.string.deuxPoints) + lastClickedLatitude + getString(R.string.coma) + getString(R.string.dechetLongitude) + getString(R.string.deuxPoints) + lastClickedLongitude;
    }

    /**
     * Retourne le texte du bouton sélectionné dans une liste de boutons.
     *
     * @param boutons La liste des boutons parmi lesquels rechercher le bouton sélectionné.
     * @return Le texte du bouton sélectionné, ou une chaîne vide si aucun bouton n'est sélectionné.
     */
    private String getBoutonSelectionne(List<Button> boutons) {
        for (Button bouton : boutons) {
            // Vérifier si le bouton est sélectionné
            if (bouton.isSelected()) {
                return bouton.getText().toString();
            }
        }
        return "";
    }


    /**
     * Affiche un message Toast personnalisé avec le texte spécifié et le fond de couleur spécifié.
     *
     * @param texteNotification Le texte à afficher dans le Toast.
     * @param couleurBackground La couleur de fond du Toast.
     */
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

    /**
     * Ajoute un déchet à la base de données à partir des informations spécifiées.
     *
     * @param unDechet Le déchet à ajouter à la base de données.
     */
    protected void addZoneDechet(Dechet unDechet){

        try {
            // Création d'une file d'attente de requêtes Volley
            RequestQueue queue;
            queue = Volley.newRequestQueue(this);
            queue.start();

            // Création d'un objet JSON contenant les informations du déchet
            JSONObject objetJSON = new JSONObject();
            objetJSON.put("id",unDechet.getId());
            objetJSON.put("latitude",unDechet.getLatitude());
            objetJSON.put("longitude",unDechet.getLongitude());
            objetJSON.put("taille",unDechet.getTaille());
            objetJSON.put("description",unDechet.getDescription());
            // ID de l'événement associé au déchet
            objetJSON.put("idEvenement","3");
            // ID de la collecte associée au déchet
            objetJSON.put("idCollecte","2");
            // ID de l'utilisateur associé au déchet
            objetJSON.put("idUtilisateur","1");

            // URL de l'API pour ajouter un déchet à la base de données
            String url = "https://deching.alwaysdata.net/actions/Dechet.php";

            // Création de la requête JSON avec la méthode POST
            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST,url, objetJSON, reponse -> {
                // Code à exécuter en cas de succès de la requête
                // (peut être laissé vide, ou peut inclure des actions supplémentaires si nécessaire)
            },
                    error -> {
                        // Code à exécuter en cas d'erreur lors de la requête
                        // (peut être laissé vide, ou peut inclure des actions supplémentaires si nécessaire)
                    }) {
            };

            // Ajout de la requête à la file d'attente
            queue.add(jsonRequest);
        } catch (Exception exception){
            // Gestion des exceptions lors de la création de la requête
            Log.d("erreurHttp", Objects.requireNonNull(exception.getMessage()));
        }

    }

    /**
     * Récupère tous les déchets enregistrés en base de données sous forme de collection.
     *
     * @param callback Callback à exécuter une fois les déchets récupérés.
     */
    protected void getAllZoneDechet(final VolleyCallback callback){
        // Création d'une file d'attente de requêtes Volley
        RequestQueue queue;
        queue = Volley.newRequestQueue(this);
        queue.start();

        // Initialisation d'une liste pour stocker les déchets récupérés
        ArrayList<Dechet> dechetsRecuperes = new ArrayList<>();

        // URL de l'API pour récupérer tous les déchets
        String url = "https://deching.alwaysdata.net/actions/Dechet.php";

        // Création de la requête JSON avec la méthode GET
        JsonArrayRequest jsonRequest = new JsonArrayRequest( url, reponse -> {
            // Code à exécuter une fois les données chargées
            for(int i=0;i < reponse.length();i++){
                try {
                    // Récupération des informations de chaque déchet dans la réponse JSON
                    JSONObject unDechetJSON = reponse.getJSONObject(i);
                    int id = unDechetJSON.getInt("id");
                    double latitude = unDechetJSON.getDouble("latitude");
                    double longitude = unDechetJSON.getDouble("longitude");
                    String taille = unDechetJSON.getString("taille");
                    String description = unDechetJSON.getString("description");

                    // Création d'un objet Dechet avec les informations récupérées
                    Dechet unDechet = new Dechet(id,latitude,longitude,taille,description);

                    // Ajout du déchet à la liste
                    dechetsRecuperes.add(unDechet);

                } catch (JSONException e) {
                    // Gestion des exceptions lors de l'analyse de la réponse JSON
                    throw new RuntimeException(e);
                }
            }
            // Appel du callback avec la liste de déchets récupérés en paramètre
            callback.onSuccess(dechetsRecuperes);
        },
                error -> {
                    // Code à exécuter lorsqu'une erreur de communication avec le serveur est détectée
                    Log.e("erreur récupération des déchets", Objects.requireNonNull(error.getMessage()));
                }) {
        };
        // Ajout de la requête à la file d'attente
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

