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

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Classe représentant la création de compte.
 */
public class CreateAccountActivity extends AppCompatActivity {
    /**
     * TextView pour afficher les erreurs lors de la création de compte.
     */
    private TextView errorCreateAccountTextView;

    /**
     * Variable pour stocker le nom de l'utilisateur.
     */
    private String nom;

    /**
     * Variable pour stocker le prénom de l'utilisateur.
     */
    private String prenom;

    /**
     * Variable pour stocker l'adresse e-mail de l'utilisateur.
     */
    private String mail;

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
        EditText createPasswordEditText;
        EditText createUsernameEditText;
        EditText mailEditText;
        EditText prenomEditText;
        EditText nomEditText;
        Button createAccountBtn;
        TextView alreadyHasAccountBtn;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        // Initialisation des vues et des boutons
        alreadyHasAccountBtn = findViewById(R.id.alreadyHasAccountBtn);
        createAccountBtn = findViewById(R.id.createAccountBtn);
        nomEditText = findViewById(R.id.nomEditText);
        prenomEditText = findViewById(R.id.prenomEditText);
        mailEditText = findViewById(R.id.mailEditText);
        createUsernameEditText = findViewById(R.id.createUsernameEditText);
        createPasswordEditText = findViewById(R.id.createPasswordEditText);
        errorCreateAccountTextView = findViewById(R.id.errorCreateAccountTextView);

        // Gestion du clic sur le bouton de création de compte
        createAccountBtn.setOnClickListener(v -> {
            nom = nomEditText.getText().toString();
            prenom = prenomEditText.getText().toString();
            mail = mailEditText.getText().toString();
            username = createUsernameEditText.getText().toString();
            password = createPasswordEditText.getText().toString();
            createAccount();
        });

        // Gestion du clic sur le bouton "Déjà un compte ?"
        alreadyHasAccountBtn.setOnClickListener(v -> {
            Intent createToAccountActivity = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(createToAccountActivity);
        });
    }

    /**
     * Méthode pour créer un nouveau compte en envoyant une requête à l'API.
     */
    private void createAccount() {
        String url = "https://deching.alwaysdata.net/connxionUser/createAccount.php";
        JSONObject postData = new JSONObject();
        try {
            postData.put(getString(R.string.nom), nom);
            postData.put(getString(R.string.prenom), prenom);
            postData.put(getString(R.string.mail), mail);
            postData.put(getString(R.string.username), username);
            postData.put(getString(R.string.userpassword), password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData,
                response -> {
                    try {
                        boolean success = response.getBoolean("success");

                        if (success) {
                            // Compte créé avec succès
                            Intent intent = new Intent(CreateAccountActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // Création de compte échouée, afficher un message d'erreur
                            errorCreateAccountTextView.setVisibility(View.VISIBLE);
                            errorCreateAccountTextView.setText(R.string.erreurCreationCompte);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(CreateAccountActivity.this, R.string.erreurTraitementDonnees, Toast.LENGTH_SHORT).show();
                    }
                }, error -> Log.e("API Connection", "Erreur lors de la connexion à l'API", error));

        // Ajout de la requête à la file d'attente
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }
}
