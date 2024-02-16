package com.example.deching;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.deching.utilitaire.SingletonUtilisateur;

public class ProfilActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ImageButton boutonEvent;
        ImageButton boutonMap;
        ImageButton boutonHome;
        ImageButton boutonMenu;
        ImageButton boutonCloseMenu;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        boutonHome = (ImageButton) findViewById(R.id.imageButtonHome);
        boutonMap = (ImageButton) findViewById(R.id.imageButtonMap);
        boutonEvent = (ImageButton) findViewById(R.id.imageButtonEvent);
        boutonMenu = (ImageButton) findViewById(R.id.imageButtonMenu);
        boutonCloseMenu = (ImageButton) findViewById(R.id.menuClose);

        SingletonUtilisateur utilisateur = SingletonUtilisateur.getInstance("");
        boutonHome.setOnClickListener(v -> {
            Intent intentHome = new Intent(ProfilActivity.this, HomePageActivity.class);
            startActivity(intentHome);
        });
        boutonMap.setOnClickListener(v -> {
            Intent intentMap = new Intent(ProfilActivity.this, MapActivity.class);
            startActivity(intentMap);
        });
        boutonEvent.setOnClickListener(v -> {
            Intent intentEvent = new Intent(ProfilActivity.this, EvenementActivity.class);
            startActivity(intentEvent);
        });
        TextView nomUtilisateur = (TextView) findViewById(R.id.usernameTextView);
        nomUtilisateur.setText(utilisateur.getNomUtilisateur());

        FrameLayout mainLayout = (FrameLayout) findViewById(R.id.layoutProfil);
        FrameLayout menuLayout = (FrameLayout) findViewById(R.id.layoutMenu);
        TextView appVersion = (TextView) findViewById(R.id.versionAppValue);

        try {
            appVersion.setText(this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }

        mainLayout.setVisibility(FrameLayout.VISIBLE);
        menuLayout.setVisibility(FrameLayout.GONE);

        boutonMenu.setOnClickListener(v -> {
            mainLayout.setVisibility(mainLayout.getVisibility() == FrameLayout.VISIBLE ? FrameLayout.GONE : FrameLayout.VISIBLE);
            menuLayout.setVisibility(menuLayout.getVisibility() == FrameLayout.VISIBLE ? FrameLayout.GONE : FrameLayout.VISIBLE);
        });

        boutonCloseMenu.setOnClickListener(v -> {
            mainLayout.setVisibility(FrameLayout.VISIBLE);
            menuLayout.setVisibility(FrameLayout.GONE);
        });
    }
}