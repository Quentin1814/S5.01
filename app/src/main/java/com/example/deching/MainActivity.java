package com.example.deching;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.deching.utilitaire.SingletonUtilisateur;

import org.json.JSONException;
import org.json.JSONObject;

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
     * Méthode appelée lors de la création de l'activité.
     *
     * @param savedInstanceState données permettant de reconstruire l'activité lorsqu'elle est recréée, si null alors aucune donnée n'est disponible.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EditText usernameEditText;
        EditText passwordEditText;
        Button connectBtn;
        TextView createAccountBtn;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        connectBtn = findViewById(R.id.connectBtn);
        createAccountBtn = findViewById(R.id.createAccountBtn);
        errorConnectAccountTextView = findViewById(R.id.errorConnectAccountTextView);

        connectBtn.setOnClickListener(v -> {
            username = usernameEditText.getText().toString();
            password = passwordEditText.getText().toString();
            connectUser();
        });

        createAccountBtn.setOnClickListener(v -> {
            // Rediriger vers l'activité de création de compte si on n'a pas de compte
            Intent intent = new Intent(MainActivity.this, CreateAccountActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Méthode qui permet de connecter un utilisateur
     */
    private void connectUser() {
        String url = "https://deching.alwaysdata.net/connxionUser/connectUser.php";

        JSONObject postData = new JSONObject();
        try {
            postData.put("username", username);
            postData.put("userPassword", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData,
                response -> {
                    Log.d("api",response.toString());
                    try {
                        boolean success = response.getBoolean("success");

                        if (success) {
                            // Connexion réussie, rediriger vers MapActivity
                            SingletonUtilisateur utilisateur= SingletonUtilisateur.getInstance(username);
                            Intent intent = new Intent(MainActivity.this, MapActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // Connexion échouée, afficher un message d'erreur
                            errorConnectAccountTextView.setVisibility(View.VISIBLE);
                            errorConnectAccountTextView.setText(R.string.connexion_echoue);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("API Connection",response.toString());
                        Toast.makeText(MainActivity.this, R.string.erreurTraitementDonnees, Toast.LENGTH_SHORT).show();
                    }
                }, error -> Log.e("API Connection", "Erreur lors de la connexion à l'API", error));

        // Ajout de la requête à la file d'attente
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }
}