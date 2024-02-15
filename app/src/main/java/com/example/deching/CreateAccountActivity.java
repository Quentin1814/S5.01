package com.example.deching;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Classe représentant la création de compte.
 */
public class CreateAccountActivity extends AppCompatActivity {

    /**
     * Error de création de compte
     */
    private TextView errorCreateAccountTextView;

    /**
     * Nom d'utilisateur
     */
    private String username;

    /**
     * Mot de passe de l'utilisateur
     */
    private String password;

    /**
     * Gestionnaire de la base de données pour la communication avec le serveur.
     * Utilisé pour envoyer des requêtes et recevoir des réponses.
     */
    private DatabaseManager databaseManager;

    /**
     * Méthode appelée lors de la création de l'activité.
     *
     * @param savedInstanceState données permettant de reconstruire l'activité lorsqu'elle est recréée, si null alors aucune donnée n'est disponible.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EditText passwordEditText;
        EditText usernameEditText;
        Button createAccountBtn;
        TextView alreadyHasAccountBtn;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        // Initialisation des vues et des boutons
        alreadyHasAccountBtn = findViewById(R.id.alreadyHasAccountBtn);
        createAccountBtn = findViewById(R.id.createAccountBtn);
        usernameEditText = findViewById(R.id.createUsernameEditText);
        passwordEditText = findViewById(R.id.createPasswordEditText);
        errorCreateAccountTextView = findViewById(R.id.errorCreateAccountTextView);

        // Initialisation du gestionnaire de base de données
        databaseManager = new DatabaseManager(getApplicationContext());

        // Gestion du clic sur le bouton de création de compte
        createAccountBtn.setOnClickListener(v -> {
            username = usernameEditText.getText().toString();
            password = passwordEditText.getText().toString();

            createAccount();
        });

        // Gestion du clic sur le bouton "Déjà un compte ?"
        alreadyHasAccountBtn.setOnClickListener(v -> {
            Intent createToAccountActivity = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(createToAccountActivity);
        });
    }

    /**
     * Méthode appelée lors de la réception d'une réponse de l'API.
     *
     * @param response Objet JSON représentant la réponse de l'API.
     */
    public void onApiResponse(JSONObject response) {
        boolean success;
        String error;

        try {
            success = response.getBoolean("success");

            if (success) {
                // Si la création de compte est réussie, démarrer l'activité principale
                Intent interfaceActivity = new Intent(getApplicationContext(), InterfaceActivity.class);
                interfaceActivity.putExtra("username", username);
                startActivity(interfaceActivity);
                finish();
            } else {
                // Si la création de compte a échoué, afficher le message d'erreur
                error = response.getString("error");
                errorCreateAccountTextView.setVisibility(View.VISIBLE);
                errorCreateAccountTextView.setText(error);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Méthode pour créer un nouveau compte en envoyant une requête à l'API.
     */
    public void createAccount() {
        String url = "http://10.0.2.2/test/actions/createAccount.php";

        // Paramètres de la requête
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        JSONObject parameters = new JSONObject(params);

        // Création de la requête JSON
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, this::onApiResponse, error -> Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show());

        // Ajout de la requête à la file d'attente du gestionnaire de base de données
        databaseManager.queue.add(jsonObjectRequest);
    }
}
