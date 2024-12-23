package com.example.gestiondeclasse;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class CompetenceActivity extends AppCompatActivity {

    private String[] skillNames = {"Java", "Kotlin", "XML", "Android", "SQLite", "UI Design"};
    private int[] skillProgress = {80, 70, 50, 90, 60, 75};

    private ArrayList<Competence> competenceList = new ArrayList<>();
    private CompetenceAdapter adapter;

    private static final int REQUEST_CODE_PERMISSION = 1;
    private static final int PICK_IMAGE_REQUEST = 2;

    private ImageView dialogImagePreview;
    private int userId;
    private String userName, userEmail, userSurname;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competence);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_PERMISSION);
        }

        dbHelper = new DatabaseHelper(this);

        GridView gridView = findViewById(R.id.gridCompetences);
        adapter = new CompetenceAdapter(this, competenceList);
        gridView.setAdapter(adapter);

        // Charger les compétences par défaut
        loadDefaultCompetences();

        ImageView addCompetenceButton = findViewById(R.id.ajouter_competence);
        addCompetenceButton.setOnClickListener(v -> showAddCompetenceDialog());

        ImageView iconBack = findViewById(R.id.icon_back);
        iconBack.setOnClickListener(v -> {
            Intent intent = new Intent(CompetenceActivity.this, DashboardActivity.class);
            startActivity(intent);
        });

        Intent intent = getIntent();
        userId = intent.getIntExtra("userId", -1);
        userName = intent.getStringExtra("userName");
        userEmail = intent.getStringExtra("userEmail");
        userSurname = intent.getStringExtra("userSurName");
    }

    // Charger les compétences par défaut pour les utilisateurs
    private void loadDefaultCompetences() {
        for (int i = 0; i < skillNames.length; i++) {
            Competence competence = new Competence(i + 1, skillNames[i], skillProgress[i]);
            competenceList.add(competence);
        }
        adapter.notifyDataSetChanged();
    }

    // Afficher la boîte de dialogue pour ajouter une nouvelle compétence
    private void showAddCompetenceDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_ajouter_competence);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.background_dialog);

        EditText inputName = dialog.findViewById(R.id.input_name);
        Spinner careerSpinner = dialog.findViewById(R.id.career_spinner);
        Button btnSave = dialog.findViewById(R.id.btn_save);
        Button btnCancel = dialog.findViewById(R.id.btn_cancel);
        dialogImagePreview = dialog.findViewById(R.id.competenceLogo);

        // Initialisation du spinner
        SpinnerUtils.setupSpinner(this, careerSpinner, R.array.career_spinner, R.layout.spinners_item, null);

        btnSave.setOnClickListener(v -> {
            String name = inputName.getText().toString().trim();
            if (name.isEmpty()) {
                Toast.makeText(this, "Le nom de la compétence est requis", Toast.LENGTH_SHORT).show();
                return;
            }

            // Ajout de la compétence et enregistrement dans la base de données
            addNewCompetence(name, 0);
            dialog.dismiss();
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    // Ajouter une compétence à la base de données et à la liste
    private void addNewCompetence(String name, int progress) {
        for (Competence competence : competenceList) {
            if (competence.getName().equalsIgnoreCase(name)) {
                Toast.makeText(this, "Cette compétence existe déjà.", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        int newId = generateNewCompetenceId();
        Competence newCompetence = new Competence(newId, name, progress);

        competenceList.add(newCompetence);
        adapter.notifyDataSetChanged();
        Toast.makeText(this, "Compétence ajoutée avec succès : " + name, Toast.LENGTH_SHORT).show();
    }

    // Méthode pour générer un ID unique
    private int generateNewCompetenceId() {
        return competenceList.size() + 1;
    }
}
