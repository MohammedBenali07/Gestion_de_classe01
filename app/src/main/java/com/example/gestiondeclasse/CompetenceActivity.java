package com.example.gestiondeclasse;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class CompetenceActivity extends AppCompatActivity {

    // Données des compétences (ajusté pour utiliser progress au lieu des images)
    private String[] skillNames = {"Java", "Kotlin", "XML", "Android", "SQLite", "UI Design"};
    private int[] skillProgress = {80, 70, 50, 90, 60, 75}; // Progrès des compétences (0 à 100)

    private ArrayList<Competence> competenceList = new ArrayList<>();
    private CompetenceAdapter adapter;

    private static final int REQUEST_CODE_PERMISSION = 1;
    private static final int PICK_IMAGE_REQUEST = 2;

    private ArrayList<Uri> selectedImageUris = new ArrayList<>(); // Liste des URI sélectionnés
    private ImageView dialogImagePreview; // Aperçu de l'image dans la boîte de dialogue

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competence);

        // Vérifier les permissions pour accéder aux fichiers
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_PERMISSION);
        }

        // Initialiser l'adaptateur et configurer le GridView
        GridView gridView = findViewById(R.id.gridCompetences);
        adapter = new CompetenceAdapter(this, competenceList);
        gridView.setAdapter(adapter);

        // Charger les compétences par défaut
        loadDefaultCompetences();

        // Ajouter une compétence
        ImageView addCompetenceButton = findViewById(R.id.ajouter_competence);
        addCompetenceButton.setOnClickListener(v -> showAddCompetenceDialog());

        // Bouton de retour
        ImageView iconBack = findViewById(R.id.icon_back);
        iconBack.setOnClickListener(v -> {
            Intent intent = new Intent(CompetenceActivity.this, DashboardActivity.class);
            startActivity(intent);
        });
    }

    // Charger les compétences par défaut
    private void loadDefaultCompetences() {
        for (int i = 0; i < skillNames.length; i++) {
            Competence competence = new Competence(skillNames[i], skillProgress[i]);
            competenceList.add(competence);
        }
        adapter.notifyDataSetChanged();
    }

    private void showAddCompetenceDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_ajouter_competence);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.background_dialog);

        EditText inputName = dialog.findViewById(R.id.input_name);
        Spinner careerSpinner = dialog.findViewById(R.id.career_spinner);
        Button btnSave = dialog.findViewById(R.id.btn_save);
        Button btnCancel = dialog.findViewById(R.id.btn_cancel);
        dialogImagePreview = dialog.findViewById(R.id.competenceLogo);

        // Configurez le bouton pour choisir l'image (ici, cette fonctionnalité n'est plus nécessaire pour cette version)
        dialog.findViewById(R.id.btn_select_image).setOnClickListener(v -> pickImage());

        // Configurer le spinner de carrière
        SpinnerUtils.setupSpinner(this, careerSpinner, R.array.career_spinner, R.layout.spinners_item, null);

        // Sauvegarder la compétence
        btnSave.setOnClickListener(v -> {
            String name = inputName.getText().toString().trim();
            if (name.isEmpty()) {
                Toast.makeText(this, "Le nom de la compétence est requis", Toast.LENGTH_SHORT).show();
                return;
            }

            // Nous avons remplacé la logique d'image par un attribut de progrès
            int defaultProgress = 0;  // On peut laisser la compétence avec une progression à 0 par défaut
            addNewCompetence(name, defaultProgress); // Ajouter la compétence
            dialog.dismiss();
        });

        // Annuler l'ajout
        btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void addNewCompetence(String name, int progress) {
        // Vérifier si la compétence existe déjà
        for (Competence competence : competenceList) {
            if (competence.getName().equalsIgnoreCase(name)) {
                Toast.makeText(this, "Cette compétence existe déjà.", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Ajouter une compétence
        Competence newCompetence = new Competence(name, progress);
        competenceList.add(newCompetence);
        adapter.notifyDataSetChanged();
        Toast.makeText(this, "Compétence ajoutée avec succès : " + name, Toast.LENGTH_SHORT).show();
    }

    private void pickImage() {
        // Intent pour sélectionner une ou plusieurs images depuis la galerie (cela peut être omis, car nous n'utilisons plus d'images dans le modèle)
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); // Permet la sélection multiple d'images
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUris.clear(); // Vide les précédents URI pour éviter la duplication

            if (data.getClipData() != null) {
                // Sélection multiple d'images
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    selectedImageUris.add(imageUri);
                }
                Toast.makeText(this, selectedImageUris.size() + " images sélectionnées", Toast.LENGTH_SHORT).show();
            } else if (data.getData() != null) {
                // Sélection d'une seule image
                Uri imageUri = data.getData();
                selectedImageUris.add(imageUri);
                Toast.makeText(this, "1 image sélectionnée", Toast.LENGTH_SHORT).show();
            }

            // Mettre à jour l'aperçu de l'image dans le dialogue (non utilisé pour l'instant)
            if (!selectedImageUris.isEmpty()) {
                Uri selectedImageUri = selectedImageUris.get(0);
                dialogImagePreview.setImageURI(selectedImageUri);
            }
        }
    }
}
