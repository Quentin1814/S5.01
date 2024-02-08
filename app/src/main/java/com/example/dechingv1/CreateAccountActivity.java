package com.example.dechingv1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CreateAccountActivity extends AppCompatActivity {

    private TextView alreadyHasAccountBtn;
    private TextView errorCreateAccountTextView;
    private Button createAccountBtn;

    private EditText nomEditText;
    private EditText prenomEditText;
    private EditText mailEditText;
    private EditText createUsernameEditText;
    private EditText createPasswordEditText;

    private String nom;
    private String prenom;
    private String mail;
    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        alreadyHasAccountBtn = findViewById(R.id.alreadyHasAccountBtn);
        createAccountBtn = findViewById(R.id.createAccountBtn);
        nomEditText = findViewById(R.id.nomEditText);
        prenomEditText = findViewById(R.id.prenomEditText);
        mailEditText = findViewById(R.id.mailEditText);
        createUsernameEditText = findViewById(R.id.createUsernameEditText);
        createPasswordEditText = findViewById(R.id.createPasswordEditText);
        errorCreateAccountTextView = findViewById(R.id.errorCreateAccountTextView);

        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nom = nomEditText.getText().toString();
                prenom = prenomEditText.getText().toString();
                mail = mailEditText.getText().toString();
                username = createUsernameEditText.getText().toString();
                password = createPasswordEditText.getText().toString();
                createAccount();
            }
        });

        alreadyHasAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createToAccountActivity = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(createToAccountActivity);
            }
        });
    }

    private void createAccount() {
        String url = "https://deching.alwaysdata.net/connxionUser/createAccount.php";

        JSONObject postData = new JSONObject();
        try {
            postData.put("nom", nom);
            postData.put("prenom", prenom);
            postData.put("mail", mail);
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
                            // Compte créé avec succès
                            Log.d("success","Compte créé avec succès");
                            Intent intent = new Intent(CreateAccountActivity.this, MainActivity.class);
//                                intent.putExtra("username", username);
                            startActivity(intent);
                            finish();
                        } else {
                            // Création de compte échouée, afficher un message d'erreur
                            errorCreateAccountTextView.setVisibility(View.VISIBLE);
                            errorCreateAccountTextView.setText("Erreur lors de la création du compte. Veuillez réessayer.");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(CreateAccountActivity.this, "Erreur de traitement des données", Toast.LENGTH_SHORT).show();
                    }
                }, error -> Log.e("API Connection", "Erreur lors de la connexion à l'API", error));

        // Ajout de la requête à la file d'attente
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }
}
