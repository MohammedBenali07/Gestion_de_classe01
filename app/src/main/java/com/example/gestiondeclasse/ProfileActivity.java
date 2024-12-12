package com.example.gestiondeclasse;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class ProfileActivity extends AppCompatActivity {

    private ImageView icon1, icon2, icon3, logoutIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialiser les icônes
        icon1 = findViewById(R.id.icon1);
        icon2 = findViewById(R.id.icon2);
        icon3 = findViewById(R.id.icon3);

        // Initialiser l'icône de logOut
        logoutIcon = findViewById(R.id.ic_logout);

        // Vérifier que les icônes sont correctement initialisées
        if (icon1 == null || icon2 == null || icon3 == null) {
            throw new IllegalStateException("One or more ImageViews are not properly initialized. Check the IDs in activity_profile.xml.");
        }

        // Charger le premier fragment et activer l'icône par défaut
        loadFragment(new Fragment1());
        activateIcon(R.id.icon1);

        // Gérer les clics sur les icônes
        icon1.setOnClickListener(v -> {
            loadFragment(new Fragment1());
            activateIcon(R.id.icon1);
        });

        icon2.setOnClickListener(v -> {
            loadFragment(new Fragment2());
            activateIcon(R.id.icon2);
        });

        icon3.setOnClickListener(v -> {
            loadFragment(new Fragment3());
            activateIcon(R.id.icon3);
        });

        // Gérer le clic sur l'icône de déconnexion
        logoutIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomLogoutDialog();  // Afficher le pop-up personnalisé
            }
        });
        // Configuration du bouton retour
        ImageView iconBack = findViewById(R.id.icon_back); // Déplacement ici dans onCreate
        iconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Rediriger vers DashboardActivity
                Intent intent = new Intent(ProfileActivity.this, DashboardActivity.class);
                startActivity(intent);
            }
        });
    }

    private void showCustomLogoutDialog() {
        // Créer un objet Dialog
        Dialog dialog = new Dialog(this);

        // Appliquer le layout dialog_logout qui utilise CardView et les coins arrondis
        dialog.setContentView(R.layout.dialog_logout);

        // Appliquer le fond personnalisé
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.background_dialog);

        // Récupérer les boutons du dialogue
        Button btnSave = dialog.findViewById(R.id.btn_logOut);
        Button btnCancel = dialog.findViewById(R.id.btn_cancel);

        // Action pour le bouton "Log Out"
        btnSave.setOnClickListener(v -> {
            // Action de déconnexion (ajoutez ici la logique de déconnexion si nécessaire)
            dialog.dismiss();
        });

        // Action pour le bouton "Cancel"
        btnCancel.setOnClickListener(v -> dialog.dismiss());

        // Afficher le dialog
        dialog.show();
    }




    // Méthode pour charger un fragment
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    // Méthode pour activer une icône
    private void activateIcon(int activeIconId) {
        // Réinitialiser toutes les icônes à leur état normal
        resetIcons();

        // Activer l'icône sélectionnée
        if (activeIconId == R.id.icon1) {
            icon1.setImageResource(R.drawable.ic_member_card_active);
        } else if (activeIconId == R.id.icon2) {
            icon2.setImageResource(R.drawable.ic_folder_active);
        } else if (activeIconId == R.id.icon3) {
            icon3.setImageResource(R.drawable.ic_security_active);
        }
    }

    // Méthode pour réinitialiser toutes les icônes
    private void resetIcons() {
        icon1.setImageResource(R.drawable.ic_member_card);
        icon2.setImageResource(R.drawable.ic_folder);
        icon3.setImageResource(R.drawable.ic_security);
    }
}
