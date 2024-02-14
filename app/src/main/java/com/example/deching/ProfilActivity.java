package com.example.deching;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.deching.utilitaire.SingletonUtilisateur;

public class ProfilActivity extends AppCompatActivity {
    private ImageButton boutonHome;
    private TextView nomUtilisateur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        boutonHome = (ImageButton) findViewById(R.id.imageButtonHome);

        SingletonUtilisateur utilisateur= SingletonUtilisateur.getInstance("");
        boutonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentHome = new Intent(ProfilActivity.this, HomePageActivity.class);
                startActivity(intentHome);

            }


        });
        nomUtilisateur = (TextView) findViewById(R.id.usernameTextView);
        nomUtilisateur.setText(utilisateur.getNomUtilisateur());
    }
}