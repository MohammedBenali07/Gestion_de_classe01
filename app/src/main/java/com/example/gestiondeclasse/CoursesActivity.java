package com.example.gestiondeclasse;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CoursesActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCourses;
    private Spinner levelSpinner, semestreSpinner;
    private CourseAdapter adapter;
    private ArrayList<String> courses, allCourses;
    private EditText inputSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);

        // Initialisation des composants
        recyclerViewCourses = findViewById(R.id.recyclerViewCourses);
        levelSpinner = findViewById(R.id.level_spinner);
        semestreSpinner = findViewById(R.id.semestre_spinner);
        inputSearch = findViewById(R.id.input_search);

        // Configuration des spinners
        SpinnerUtils.setupSpinner(
                this,
                levelSpinner,
                R.array.level_spinner,
                R.layout.spinners_item,
                null
        );

        SpinnerUtils.setupSpinner(
                this,
                semestreSpinner,
                R.array.semestre_spinner,
                R.layout.spinners_item,
                null
        );

        // Configuration des données pour le RecyclerView
        allCourses = getCourses();  // Liste des cours originale
        courses = new ArrayList<>(allCourses);  // Liste filtrée initialement identique à la liste originale

        // Configuration du RecyclerView
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);  // 2 colonnes
        recyclerViewCourses.setLayoutManager(layoutManager);

        // Initialisation de l'adaptateur avec les deux listes (filtrée et originale)
        adapter = new CourseAdapter(this, courses, allCourses);
        recyclerViewCourses.setAdapter(adapter);

        // Configuration du bouton retour
        ImageView iconBack = findViewById(R.id.icon_back);
        iconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Rediriger vers DashboardActivity
                Intent intent = new Intent(CoursesActivity.this, DashboardActivity.class);
                startActivity(intent);
            }
        });

        // Ajouter un TextWatcher pour filtrer les cours lors de la saisie dans le champ de recherche
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Appel à la méthode filter() de l'adaptateur pour filtrer les cours en fonction du texte de recherche
                adapter.filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    /**
     * Retourne une liste fictive de cours.
     */
    private ArrayList<String> getCourses() {
        ArrayList<String> courses = new ArrayList<>();
        courses.add("Mathématiques");
        courses.add("Physique");
        courses.add("Programmation");
        courses.add("Base de données");
        courses.add("Réseaux");
        courses.add("Design UI/UX");
        courses.add("Développement Mobile");
        courses.add("Intelligence Artificielle");
        return courses;
    }
}
