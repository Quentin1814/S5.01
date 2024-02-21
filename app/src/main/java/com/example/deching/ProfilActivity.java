package com.example.deching;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import com.example.deching.utilitaire.SingletonUtilisateur;

public class ProfilActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ImageButton boutonEvent;
        ImageButton boutonMap;
        ImageButton boutonHome;
        ImageButton boutonMenu;
        ImageButton boutonCloseMenu;
        ImageButton boutonAddPost;
        ImageButton boutonProfile;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        boutonHome = (ImageButton) findViewById(R.id.imageButtonHome);
        boutonMap = (ImageButton) findViewById(R.id.imageButtonMap);
        boutonEvent = (ImageButton) findViewById(R.id.imageButtonEvent);
        boutonMenu = (ImageButton) findViewById(R.id.imageButtonMenu);
        boutonCloseMenu = (ImageButton) findViewById(R.id.menuClose);
        boutonAddPost = (ImageButton) findViewById(R.id.imageButtonAddPost);
        boutonProfile = (ImageButton) findViewById(R.id.imageButtonProfile);

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



        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES || AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM) {
            Log.d("HomePageActivity", "Mode nuit");
            boutonMenu.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_IN);
            boutonHome.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_IN);
            boutonMap.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_IN);
            boutonAddPost.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_IN);
            boutonEvent.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_IN);
            boutonProfile.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_IN);
            boutonCloseMenu.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_IN);
        } else {
            Log.d("HomePageActivity", "Mode jour");
            boutonMenu.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
            boutonHome.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
            boutonMap.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
            boutonAddPost.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
            boutonEvent.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
            boutonProfile.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
            boutonCloseMenu.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
        }
    }
}