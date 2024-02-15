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
 * Classe représentant la page de connexion à un compte
 */

public class MainActivity extends AppCompatActivity {

    /**
     * Error de création de compte
     */
    private TextView errorConnectAccountTextView;

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
        TextView createAccountBtn;
        Button connectBtn;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        errorConnectAccountTextView = findViewById(R.id.errorConnectAccountTextView);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        connectBtn = findViewById(R.id.connectBtn);
        createAccountBtn = findViewById(R.id.createAccountBtn);

        databaseManager = new DatabaseManager(getApplicationContext());

        connectBtn.setOnClickListener(v -> {
            username = usernameEditText.getText().toString();
            password = passwordEditText.getText().toString();

            connectUser();
        });

        createAccountBtn.setOnClickListener(v -> {
            Intent createAccountActivity = new Intent(getApplicationContext(), CreateAccountActivity.class);
            startActivity(createAccountActivity);
            finish();
        });
    }

    /**
     * Méthode qui permet de récupérer la réponse l'api
     * @param response Réponse de l'API
     */
    public void onApiResponse(JSONObject response) {
        boolean success;
        String error;

        try {
            success = response.getBoolean("success");

            if (success) {
                Intent interfaceActivity = new Intent(getApplicationContext(), InterfaceActivity.class);
                interfaceActivity.putExtra("username", username);
                startActivity(interfaceActivity);
                finish();
            } else {
                error = response.getString("error");
                errorConnectAccountTextView.setVisibility(View.VISIBLE);
                errorConnectAccountTextView.setText(error);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Méthode qui permet de connecter un utilisateur
     */
    public void connectUser() {
        // 10.0.2.2 correspond à localhost dans google chrome
        String url = "http://10.0.2.2/Test/actions/connectUser.php";

        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, response -> {
            onApiResponse(response);
            Toast.makeText(getApplicationContext(), "OPERATION SUCCESSFUL", Toast.LENGTH_LONG).show();
        }, error -> Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show());

        databaseManager.queue.add(jsonObjectRequest);
    }
}