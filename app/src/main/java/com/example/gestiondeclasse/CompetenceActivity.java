package com.example.gestiondeclasse;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class CompetenceActivity extends AppCompatActivity {

    private String[] skillNames = {"Java", "Kotlin", "XML", "Android", "SQLite", "UI Design"};
    private int[] skillProgress = {80, 70, 85, 90, 60, 75};

    private ArrayList<Competence> competenceList = new ArrayList<>();
    private CompetenceAdapter adapter;

    private ImageView dialogImagePreview;
    private int userId;
    private String userName, userEmail, userSurname;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competence);

        dbHelper = DatabaseHelper.getInstance(this);

        // Initialiser les vues
        GridView gridView = findViewById(R.id.gridCompetences);
        ImageView addCompetenceButton = findViewById(R.id.ajouter_competence);
        ImageView iconBack = findViewById(R.id.icon_back);
        // Récupérer les données utilisateur depuis SharedPreferences
        SharedPreferences sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE);

        userId = sharedPref.getInt("userId", -1);
        userName = sharedPref.getString("userName", "");
        userSurname = sharedPref.getString("userSurName", "");
        userEmail = sharedPref.getString("userEmail", "");
        // Initialiser l'adaptateur
        adapter = new CompetenceAdapter(this, competenceList);
        gridView.setAdapter(adapter);

        // Charger les compétences par défaut et depuis la base de données
        loadDefaultAndDatabaseCompetences(userId);

        addCompetenceButton.setOnClickListener(v -> showAddCompetenceDialog());

        iconBack.setOnClickListener(v -> {
            Intent intent = new Intent(CompetenceActivity.this, DashboardActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Charge les compétences par défaut et celles de la base de données
     */
    private void loadDefaultAndDatabaseCompetences(int user) {
        competenceList.clear();

        // Charger les compétences depuis la base de données
        Cursor cursor = dbHelper.getSkillsByUser(user);
        boolean hasSkillsFromDatabase = false; // Pour vérifier si des compétences sont récupérées
        if (cursor != null && cursor.getCount() > 0) {
            // Récupérer les indices des colonnes
            int nameColumnIndex = cursor.getColumnIndex(dbHelper.getSkillNameColumn());
            int progressColumnIndex = cursor.getColumnIndex(dbHelper.getProgressColumn());

            // Vérifier si les colonnes existent
            if (nameColumnIndex != -1 && progressColumnIndex != -1) {
                // Parcourir les résultats du curseur
                while (cursor.moveToNext()) {
                    String skillName = cursor.getString(nameColumnIndex);
                    int progress = cursor.getInt(progressColumnIndex);
                    // Ajouter la compétence de la base de données au début de la liste
                    competenceList.add(0, new Competence(generateNewCompetenceId(), skillName, progress));
                    hasSkillsFromDatabase = true;
                }
            } else {
                // Log d'erreur si les colonnes ne sont pas trouvées
                Log.e("CompetenceActivity", "Colonnes introuvables dans le curseur.");
            }
            cursor.close();
        } else {
            // Log si aucune compétence n'a été trouvée dans la base de données
            Log.d("CompetenceActivity", "Aucune compétence trouvée pour userId: " + user);
        }

        // Charger les compétences par défaut, après celles de la base de données
        for (int i = 0; i < skillNames.length; i++) {
            Competence competence = new Competence(i + 1, skillNames[i], skillProgress[i]);
            competenceList.add(0, competence); // Ajouter les compétences par défaut au début
        }

        // Notifier l'adaptateur qu'il y a des nouvelles données
        adapter.notifyDataSetChanged();


    }

    /**
     * Affiche une boîte de dialogue pour ajouter une nouvelle compétence
     */
    private void showAddCompetenceDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_ajouter_competence);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.background_dialog);

        EditText inputName = dialog.findViewById(R.id.input_name);
        Spinner careerSpinner = dialog.findViewById(R.id.career_spinner);
        Button btnSave = dialog.findViewById(R.id.btn_save);
        Button btnCancel = dialog.findViewById(R.id.btn_cancel);
        dialogImagePreview = dialog.findViewById(R.id.competenceLogo);

        // Configurer le spinner
        SpinnerUtils.setupSpinner(this, careerSpinner, R.array.career_spinner, R.layout.spinners_item, null);

        btnSave.setOnClickListener(v -> {
            String name = inputName.getText().toString().trim();
            if (name.isEmpty()) {
                Toast.makeText(this, "Le nom de la compétence est requis", Toast.LENGTH_SHORT).show();
                return;
            }

            // Ajouter une compétence et l'enregistrer dans la base de données
            addNewCompetence(name, 0);
            dialog.dismiss();
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    /**
     * Ajoute une nouvelle compétence à la liste et à la base de données
     */
    private void addNewCompetence(String name, int progress) {
        // Vérifier si la compétence existe déjà
        for (Competence competence : competenceList) {
            if (competence.getName().equalsIgnoreCase(name)) {
                Toast.makeText(this, "Cette compétence existe déjà.", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Ajouter dans la base de données
        long result = dbHelper.addSkill(userId, name, 0); // Progress par défaut est déjà 0
        if (result != -1) {
            Competence newCompetence = new Competence(generateNewCompetenceId(), name, progress);
            competenceList.add(newCompetence);
            adapter.notifyDataSetChanged();
            Toast.makeText(this, "Compétence ajoutée avec succès : " + name, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Erreur lors de l'ajout de la compétence.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Génère un ID unique pour une nouvelle compétence
     */
    private int generateNewCompetenceId() {
        return competenceList.size() + 1;
    }
}
