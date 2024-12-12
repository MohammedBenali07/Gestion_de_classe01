package com.example.gestiondeclasse;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner spinner_status, spinner_career;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialiser les Spinners
        spinner_status = findViewById(R.id.status_spinner);
        spinner_career = findViewById(R.id.career_spinner);

        // Configurer les adapters pour chaque Spinner
        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.status_spinner, // Array pour status_spinner
                R.layout.spinners_item  // Assurez-vous que ce layout existe
        );
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_status.setAdapter(statusAdapter);

        ArrayAdapter<CharSequence> careerAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.career_spinner, // Array pour career_spinner
                R.layout.spinners_item
        );
        careerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_career.setAdapter(careerAdapter);

        // Définir les listeners pour les deux Spinners
        spinner_status.setOnItemSelectedListener(this);
        spinner_career.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // Identifier quel Spinner a déclenché l'événement
        if (parent.getId() == R.id.status_spinner) {
            String selectedStatus = parent.getItemAtPosition(position).toString();
            Toast.makeText(this, "Statut sélectionné : " + selectedStatus, Toast.LENGTH_SHORT).show();
        } else if (parent.getId() == R.id.career_spinner) {
            String selectedCareer = parent.getItemAtPosition(position).toString();
            Toast.makeText(this, "Carrière sélectionnée : " + selectedCareer, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Action si rien n'est sélectionné
    }
}
