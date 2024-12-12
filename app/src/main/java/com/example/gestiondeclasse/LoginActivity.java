package com.example.gestiondeclasse;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginButton = findViewById(R.id.login_button);
        TextView registerText = findViewById(R.id.id_register);

        // Utilisation de lambdas pour les OnClickListeners
        loginButton.setOnClickListener(v -> redirectToActivity(DashboardActivity.class));
        registerText.setOnClickListener(v -> redirectToActivity(SignUpActivity.class));
    }

    // Méthode générique pour la redirection vers une activité
    private void redirectToActivity(Class<?> targetActivity) {
        Intent intent = new Intent(LoginActivity.this, targetActivity);
        startActivity(intent);
    }
}
