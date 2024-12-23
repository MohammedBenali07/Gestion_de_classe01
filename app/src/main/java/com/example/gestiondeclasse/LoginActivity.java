package com.example.gestiondeclasse;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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
            textError.setText("Please enter a valid email address");
            textError.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            return;
        }

        // Vérifier si les champs sont remplis
        if (email.isEmpty() || password.isEmpty()) {
            textError.setText("Please enter your email and password");
            textError.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            return;
        }

        // Vérification des informations dans la base de données
        boolean isValid = databaseHelper.isUserValid(email, password);
        if (isValid) {
            textError.setText("");
            // Récupérer l'utilisateur avec son email
            User user = databaseHelper.getUserByEmail(email);
            if (user != null) {
                // Transmettre tous les détails de l'utilisateur à l'activité Dashboard
                Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                intent.putExtra("userId", user.getId());
                intent.putExtra("userName", user.getName());
                intent.putExtra("userEmail", user.getEmail());
                intent.putExtra("userSurName", user.getSurname());
                intent.putExtra("userRole", user.getStatus());
                intent.putExtra("userImageProfile", user.getProfileImage());
                intent.putExtra("userBirthday", user.getBirthday());
                startActivity(intent);
                finish();
            } else {
                textError.setText("An unexpected error occurred");
                textError.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            }
        } else {
            textError.setText("Incorrect email or password");
            textError.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        }
    }

    // Méthode pour valider l'email avec regex
    private boolean isValidEmail(String email) {
        return Pattern.matches(EMAIL_REGEX, email);
    }

    // Méthode générique pour rediriger vers une autre activité
    private void redirectToActivity(Class<?> targetActivity) {
        Intent intent = new Intent(LoginActivity.this, targetActivity);
        startActivity(intent);
    }
}
