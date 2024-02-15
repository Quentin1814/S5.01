package com.example.deching;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;

import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

/**
 * Classe représentant la caméra
 */
public class CameraActivity extends AppCompatActivity {

    /**
     * Bouton de capture pour les photos
     */
    ImageButton capture;

    /**
     * Bouton pour le flash
     */
    ImageButton toogleFlash;

    /**
     * Bouton pour retourner la caméra du téléphone
     */
    ImageButton flipCamera;

    /**
     * Vue prévisionnelle pour la caméra
     */
    private PreviewView previewView;

    /**
     * Orientation de la caméra
     */
    int cameraFacing = CameraSelector.LENS_FACING_BACK;

    /**
     * Résultat du lancement de la plateforme de lancement
     */
    private final ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
        if (Boolean.TRUE.equals(result)) {
            startCamera(cameraFacing);
        }
    });

    /**
     * Méthode appelée lors de la création de l'activité.
     *
     * @param savedInstanceState données permettant de reconstruire l'activité lorsqu'elle est recréée, si null alors aucune donnée n'est disponible.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        previewView = findViewById(R.id.previewView);
        capture = findViewById(R.id.capture);
        toogleFlash = findViewById(R.id.toogleFlash);
        flipCamera = findViewById(R.id.flipCamera);

        // Vérification des autorisations de la caméra
        if (ContextCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            activityResultLauncher.launch(Manifest.permission.CAMERA);
        } else {
            startCamera(cameraFacing);
        }

        // Gestion du bouton pour changer de caméra (avant/arrière)
        flipCamera.setOnClickListener(v -> {
            if (cameraFacing == CameraSelector.LENS_FACING_BACK) {
                cameraFacing = CameraSelector.LENS_FACING_FRONT;
            } else {
                cameraFacing = CameraSelector.LENS_FACING_BACK;
            }
            startCamera(cameraFacing);
        });
    }

    /**
     * Retourne un booléen true si supérieur false sinon
     * @return Booléen true si supérieur false sinon
     */
    private boolean isSdkVersionTiramisuOrHigher() {
        return android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU;
    }

    /**
     * Méthode pour demander une autorisation si elle n'est pas accordée
     * @param permission Autorisation à demander
     */
    private void requestPermissionIfNotGranted(String permission) {
        if (ContextCompat.checkSelfPermission(CameraActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
            activityResultLauncher.launch(permission);
        }
    }

    /**
     * Méthode pour démarrer la caméra
     * @param cameraFacing Orientation de la caméra
     */
    private void startCamera(int cameraFacing) {
        // Demande de permission pour accéder aux images en fonction de la version de l'API
        if (isSdkVersionTiramisuOrHigher()) {
            requestPermissionIfNotGranted(Manifest.permission.READ_MEDIA_IMAGES);
        } else {
            requestPermissionIfNotGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        int aspectRatio = AspectRatio(previewView.getWidth(), previewView.getHeight());
        ListenableFuture<ProcessCameraProvider> listenableFuture = ProcessCameraProvider.getInstance(this);

        listenableFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = listenableFuture.get();

                Preview preview = new Preview.Builder().setTargetAspectRatio(aspectRatio).build();

                ImageCapture imageCapture = new ImageCapture.Builder().setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                        .setTargetRotation(getWindowManager().getDefaultDisplay().getRotation()).build();

                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(cameraFacing).build();

                cameraProvider.unbindAll();

                Camera camera = cameraProvider.bindToLifecycle(CameraActivity.this, cameraSelector, preview, imageCapture);

                capture.setOnClickListener(v -> takePicture(imageCapture));

                toogleFlash.setOnClickListener(v -> setFlashIcon(camera));

                preview.setSurfaceProvider(previewView.getSurfaceProvider());
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    /**
     * Méthode qui permet de prendre une photo
     * @param imageCapture Image capturé par la caméra
     */
    public void takePicture(ImageCapture imageCapture) {
        final File file = new File(getExternalFilesDir(null), System.currentTimeMillis() + ".jpg");
        ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(file).build();
        imageCapture.takePicture(outputFileOptions, Executors.newCachedThreadPool(), new ImageCapture.OnImageSavedCallback() {

            /**
             * Méthode qui permet de de sauvegarder l'image
             * @param outputFileResults Résultat du fichier de l'image obtenue
             */
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                runOnUiThread(() -> {});
                startCamera(cameraFacing);

                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                myEdit.putString("image_path", file.getPath());
                myEdit.apply();
                Intent resultIntent = new Intent();
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }

            /**
             * Méthode qui permet de renvoyer un message d'erreur
             * @param exception Exception renvoyé à l'écran
             */
            @Override
            public void onError(@NonNull ImageCaptureException exception) {
                runOnUiThread(() -> Toast.makeText(CameraActivity.this, getString(R.string.imageNotSavedError) + exception.getMessage(), Toast.LENGTH_SHORT).show());
                startCamera(cameraFacing);
            }
        });
    }

    /**
     * Méthode qui permet de modifier l'icon du flash
     * @param camera
     */
    private void setFlashIcon(Camera camera) {
        if (camera.getCameraInfo().hasFlashUnit()) {
            if (camera.getCameraInfo().getTorchState().getValue() == 0) {
                camera.getCameraControl().enableTorch(true);
                toogleFlash.setImageResource(R.drawable.baseline_flash_off_24);
            } else {
                camera.getCameraControl().enableTorch(false);
                toogleFlash.setImageResource(R.drawable.baseline_flash_on_24);
            }
        } else {
            runOnUiThread(() -> Toast.makeText(CameraActivity.this, getString(R.string.flashNotAvailable), Toast.LENGTH_SHORT).show());
        }
    }

    /**
     * Fonction qui permet de calculer le ratio à partir du width et du height
     * @param width
     * @param height
     * @return
     */
    private int AspectRatio(int width, int height) {
        double previewRatio = (double) Math.max(width, height) / Math.min(width, height);
        if (Math.abs(previewRatio - 4.0 / 3.0) <= Math.abs(previewRatio - 16.0 / 9.0)) {
            return AspectRatio.RATIO_4_3;
        }
        return AspectRatio.RATIO_16_9;
    }
}