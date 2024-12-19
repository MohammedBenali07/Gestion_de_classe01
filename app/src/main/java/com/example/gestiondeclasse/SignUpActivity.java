package com.example.gestiondeclasse;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spinner_status, spinner_career;
    private EditText editTextName, editTextSurname, editTextEmail;
    private Button buttonSignUp;

    private String selectedStatus, selectedCareer;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialiser les vues
        editTextName = findViewById(R.id.nameInput);
        editTextSurname = findViewById(R.id.surnameInput);
        editTextEmail = findViewById(R.id.emailInput);
        buttonSignUp = findViewById(R.id.signUp_button);
        spinner_status = findViewById(R.id.status_spinner);
        spinner_career = findViewById(R.id.career_spinner);

        // Initialiser la base de données
        databaseHelper = new DatabaseHelper(this);

        // Configurer les adapters pour chaque Spinner
        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.status_spinner,
                android.R.layout.simple_spinner_item
        );
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_status.setAdapter(statusAdapter);

        ArrayAdapter<CharSequence> careerAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.career_spinner,
                android.R.layout.simple_spinner_item
        );
        careerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_career.setAdapter(careerAdapter);

        // Définir les listeners pour les deux Spinners
        spinner_status.setOnItemSelectedListener(this);
        spinner_career.setOnItemSelectedListener(this);

        // Action lorsque l'utilisateur clique sur le bouton d'inscription
        buttonSignUp.setOnClickListener(v -> signUpStudent());
    }

    private void signUpStudent() {
        // Récupérer les données du formulaire
        String name = editTextName.getText().toString().trim();
        String surname = editTextSurname.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();

        // Vérification des champs obligatoires
        if (name.isEmpty() || surname.isEmpty() || email.isEmpty() || selectedStatus == null || selectedCareer == null) {
            Toast.makeText(this, "Tous les champs sont requis", Toast.LENGTH_SHORT).show();
            return;
        }

        // Enregistrer l'étudiant dans la base de données
        long studentId = databaseHelper.addStudent(name, surname, email, selectedStatus, selectedCareer);

        // Vérifier si l'enregistrement a réussi
        if (studentId != -1) {
            Toast.makeText(this, "Inscription réussie !", Toast.LENGTH_SHORT).show();
            finish();  // Optionnel : Fermer l'écran d'inscription après l'enregistrement
        } else {
            Toast.makeText(this, "Erreur lors de l'inscription", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // Identifier quel Spinner a déclenché l'événement
        if (parent.getId() == R.id.status_spinner) {
            selectedStatus = parent.getItemAtPosition(position).toString();
        } else if (parent.getId() == R.id.career_spinner) {
            selectedCareer = parent.getItemAtPosition(position).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Action si rien n'est sélectionné
    }
}
