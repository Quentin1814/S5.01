package com.example.deching;
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

public class MainActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button connectBtn;
    private TextView createAccountBtn;
    private TextView errorConnectAccountTextView;

    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        connectBtn = findViewById(R.id.connectBtn);
        createAccountBtn = findViewById(R.id.createAccountBtn);
        errorConnectAccountTextView = findViewById(R.id.errorConnectAccountTextView);

        connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = usernameEditText.getText().toString();
                password = passwordEditText.getText().toString();
                connectUser();
            }
        });

        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Rediriger vers l'activité de création de compte si on n'a pas de compte
                Intent intent = new Intent(MainActivity.this, CreateAccountActivity.class);
                startActivity(intent);
            }
        });
    }

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
                            Log.d("success","Connexion reussie");
                            Intent intent = new Intent(MainActivity.this, MapActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // Connexion échouée, afficher un message d'erreur
                            errorConnectAccountTextView.setVisibility(View.VISIBLE);
                            errorConnectAccountTextView.setText("Connexion échouée. Veuillez vérifier vos identifiants.");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("API Connection",response.toString());
                        Toast.makeText(MainActivity.this, "Erreur de traitement des données", Toast.LENGTH_SHORT).show();
                    }
                }, error -> Log.e("API Connection", "Erreur lors de la connexion à l'API", error));


        // Ajout de la requête à la file d'attente
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }
}
