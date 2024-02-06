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

public class CreateAccountActivity extends AppCompatActivity {
    private TextView errorCreateAccountTextView;
    private String username;
    private String password;
    private DatabaseManager databaseManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EditText passwordEditText;
        EditText usernameEditText;
        Button createAccountBtn;
        TextView alreadyHasAccountBtn;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        alreadyHasAccountBtn = findViewById(R.id.alreadyHasAccountBtn);
        createAccountBtn = findViewById(R.id.createAccountBtn);
        usernameEditText = findViewById(R.id.createUsernameEditText);
        passwordEditText = findViewById(R.id.createPasswordEditText);
        errorCreateAccountTextView = findViewById(R.id.errorCreateAccountTextView);

        databaseManager = new DatabaseManager(getApplicationContext());

        createAccountBtn.setOnClickListener(v -> {
            username = usernameEditText.getText().toString();
            password = passwordEditText.getText().toString();

            createAccount();
        });

        alreadyHasAccountBtn.setOnClickListener(v -> {
            Intent createToAccountActivity = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(createToAccountActivity);
        });
    }

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
                errorCreateAccountTextView.setVisibility(View.VISIBLE);
                errorCreateAccountTextView.setText(error);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void createAccount() {
        String url = "http://10.0.2.2/test/actions/createAccount.php";

        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, this::onApiResponse, error -> Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show());

        databaseManager.queue.add(jsonObjectRequest);
    }
}