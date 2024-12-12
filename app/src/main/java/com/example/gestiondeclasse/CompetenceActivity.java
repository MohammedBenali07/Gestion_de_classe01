package com.example.gestiondeclasse;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CompetenceActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private String[] skillNames = {"Java", "Kotlin", "XML", "Android", "SQLite", "UI Design"};
    private int[] skillLogos = {
            R.drawable.java,
            R.drawable.java,
            R.drawable.java,
            R.drawable.java,
            R.drawable.java,
            R.drawable.java
    };
    private int[] skillProgress = {90, 80, 70, 85, 75, 95}; // Progression des compétences

    private CompetenceAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competence);

        // Configurer l'adaptateur pour le GridView
        GridView gridView = findViewById(R.id.gridCompetences);
        adapter = new CompetenceAdapter(this, skillNames, skillLogos, skillProgress);
        gridView.setAdapter(adapter);

        // Bouton pour ajouter une compétence
        ImageView addCompetenceButton = findViewById(R.id.ajouter_competence);
        addCompetenceButton.setOnClickListener(v -> showAddCompetenceDialog());

        // Configuration du bouton retour
        ImageView iconBack = findViewById(R.id.icon_back); // Déplacement ici dans onCreate
        iconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Rediriger vers DashboardActivity
                Intent intent = new Intent(CompetenceActivity.this, DashboardActivity.class);
                startActivity(intent);
            }
        });
    }

    // Méthode pour afficher le dialog
    private void showAddCompetenceDialog() {
        Dialog dialog = new Dialog(this); // Optionnel : Appliquer un style
        dialog.setContentView(R.layout.dialog_ajouter_competence);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.background_dialog);

        // Récupérer les vues du dialog
        EditText inputName = dialog.findViewById(R.id.input_name);
        Spinner careerSpinner = dialog.findViewById(R.id.career_spinner);
        Spinner coursesSpinner = dialog.findViewById(R.id.courses_spinner);
        Button btnSave = dialog.findViewById(R.id.btn_save);
        Button btnCancel = dialog.findViewById(R.id.btn_cancel);

        // Vérifiez que les Spinners ne sont pas null avant de les utiliser
        if (careerSpinner != null && coursesSpinner != null) {
            // Configurer les Spinners dans le dialog
            SpinnerUtils.setupSpinner(
                    this,
                    careerSpinner,
                    R.array.career_spinner,
                    R.layout.spinners_item,
                    null
            );

            SpinnerUtils.setupSpinner(
                    this,
                    coursesSpinner,
                    R.array.courses_spinner,
                    R.layout.spinners_item,
                    null
            );
        } else {
            Toast.makeText(this, "Erreur d'initialisation des spinners", Toast.LENGTH_SHORT).show();
            return; // Ne pas poursuivre si un spinner est null
        }

        // Action pour le bouton "Enregistrer"
        btnSave.setOnClickListener(v -> {
            String name = inputName.getText().toString().trim();
            String career = careerSpinner.getSelectedItem().toString();
            String course = coursesSpinner.getSelectedItem().toString();

            if (name.isEmpty()) {
                Toast.makeText(this, "Le nom de la compétence est requis", Toast.LENGTH_SHORT).show();
                return;
            }

            // Ajouter la nouvelle compétence
            addNewCompetence(name, career, course);

            // Fermer le dialog
            dialog.dismiss();
        });

        // Action pour le bouton "Annuler"
        btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    // Ajouter une compétence
    private void addNewCompetence(String name, String career, String course) {
        // Exemple pour ajouter une compétence au GridView (vous pouvez adapter selon votre logique)
        Toast.makeText(this, "Compétence ajoutée : " + name + " - Carrière : " + career + " - Cours : " + course, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // Ceci gère la sélection dans le Spinner du dialog
        if (parent.getId() == R.id.career_spinner) {
            String selectedCareer = parent.getItemAtPosition(position).toString();
            Toast.makeText(this, "Carrière sélectionnée : " + selectedCareer, Toast.LENGTH_SHORT).show();
        } else if (parent.getId() == R.id.courses_spinner) {
            String selectedCourse = parent.getItemAtPosition(position).toString();
            Toast.makeText(this, "Cours sélectionné : " + selectedCourse, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Aucune action
    }
}
