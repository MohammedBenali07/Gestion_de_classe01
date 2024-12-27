package com.example.gestiondeclasse;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword;
    private Button loginButton;
    private TextView registerText, textError;
    private DatabaseHelper databaseHelper;

    // Regex pour valider l'email
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialiser les vues
        editTextEmail = findViewById(R.id.input_email);
        editTextPassword = findViewById(R.id.input_password);
        loginButton = findViewById(R.id.login_button);
        registerText = findViewById(R.id.id_register);
        textError = findViewById(R.id.text_error);

        // Initialisation de la base de données
        databaseHelper = DatabaseHelper.getInstance(this);

        // Action lorsque l'utilisateur clique sur le bouton de connexion
        loginButton.setOnClickListener(v -> authenticateUser());

        // Action pour aller à l'écran d'inscription
        registerText.setOnClickListener(v -> redirectToActivity(SignUpActivity.class));
    }

    private void authenticateUser() {
        // Récupérer l'email et le mot de passe saisis
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // Réinitialiser le message d'erreur à chaque tentative de connexion
        textError.setText("");

        // Vérifier si l'email est valide avec regex
        if (!isValidEmail(email)) {
            textError.setText("**Please enter a valid email address**");
            textError.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark));
            return;
        }

        // Vérifier si les champs sont remplis
        if (email.isEmpty() || password.isEmpty()) {
            textError.setText("**Please enter your email and password**");
            textError.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark));
            return;
        }

        // Vérification des informations dans la base de données
        boolean isValid = databaseHelper.isUserValid(email, password);
        if (isValid) {
            textError.setText("");
            // Récupérer l'utilisateur avec son email
            User user = databaseHelper.getUserByEmail(email);
            if (user != null) {
                // Convertir l'image de profil en Base64 et la sauvegarder dans le stockage local
                byte[] profileImageData = user.getProfileImage();
                if (profileImageData != null) {
                    saveProfileImage(user.getId(), profileImageData);  // Sauvegarder l'image dans le stockage local
                }

                // Stocker les informations utilisateur (hors image) dans SharedPreferences
                SharedPreferences sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("userId", user.getId());
                editor.putString("userName", user.getName());
                editor.putString("userSurName", user.getSurname());
                editor.putString("userEmail", user.getEmail());
                editor.putString("userRole", user.getStatus());
                editor.putString("userCareer", user.getCareer());
                editor.putString("userProfileImagePath", getProfileImagePath(user.getId()));  // Stocker le chemin de l'image
                editor.putString("userBirthday", user.getBirthday());
                editor.putString("userAddress", user.getAddress());
                editor.apply();

                // Transmettre tous les détails de l'utilisateur à l'activité Dashboard
                Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                intent.putExtra("userId", user.getId());
                intent.putExtra("userName", user.getName());
                intent.putExtra("userEmail", user.getEmail());
                intent.putExtra("userSurName", user.getSurname());
                intent.putExtra("userRole", user.getStatus());
                intent.putExtra("userBirthday", user.getBirthday());
                startActivity(intent);
                finish();
            } else {
                textError.setText("An unexpected error occurred");
                textError.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark));
            }
        } else {
            textError.setText("Incorrect email or password");
            textError.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark));
        }
    }

    // Méthode pour valider l'email avec regex
    private boolean isValidEmail(String email) {
        return Pattern.matches(EMAIL_REGEX, email);
    }

    // Méthode pour sauvegarder l'image de profil dans le stockage local avec un nom unique basé sur l'ID utilisateur
    private void saveProfileImage(int userId, byte[] imageData) {
        try {
            // Créer un nom de fichier unique pour chaque utilisateur basé sur son ID
            String fileName = "profile_image_" + userId + ".png";
            // Créer un fichier dans le stockage interne de l'appareil avec un nom unique
            FileOutputStream fos = openFileOutput(fileName, MODE_PRIVATE);
            fos.write(imageData);
            fos.close();
        } catch (IOException e) {
            Log.e("LoginActivity", "Error saving profile image", e);
        }
    }

    // Méthode pour récupérer le chemin de l'image dans le stockage local en fonction de l'ID utilisateur
    private String getProfileImagePath(int userId) {
        String fileName = "profile_image_" + userId + ".png";
        File file = new File(getFilesDir(), fileName);
        return file.getAbsolutePath();
    }

    // Méthode générique pour rediriger vers une autre activité
    private void redirectToActivity(Class<?> targetActivity) {
        Intent intent = new Intent(LoginActivity.this, targetActivity);
        startActivity(intent);
    }
}
