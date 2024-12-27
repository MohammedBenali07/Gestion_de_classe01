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
    private ArrayList<CourseAcadymic> courses, allCourses;
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
        iconBack.setOnClickListener(v -> {
            // Rediriger vers DashboardActivity
            Intent intent = new Intent(CoursesActivity.this, DashboardActivity.class);
            startActivity(intent);
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
     * Retourne une liste fictive de cours de type CourseAcadymic.
     */
    private ArrayList<CourseAcadymic> getCourses() {
        ArrayList<CourseAcadymic> courses = new ArrayList<>();

        // Liste des compétences associées à chaque cours
        ArrayList<String> J22Skills = new ArrayList<>();
        J22Skills.add("Java");
        J22Skills.add("Oracle");
        J22Skills.add("DevWeb");
        J22Skills.add("Html");
        J22Skills.add("Css");
        J22Skills.add("figma");

        ArrayList<String> AndroidSkills = new ArrayList<>();
        AndroidSkills.add("Java");
        AndroidSkills.add("Firebase");
        AndroidSkills.add("Sqlite");
        AndroidSkills.add("Xml");
        AndroidSkills.add("Figma");
        AndroidSkills.add("Kotlin");
        AndroidSkills.add("Android");

        ArrayList<String> PythonSkills = new ArrayList<>();
        PythonSkills.add("Python");
        PythonSkills.add("Django");
        PythonSkills.add("Flask");
        PythonSkills.add("Data Science");
        PythonSkills.add("Numpy");
        PythonSkills.add("Pandas");
        ArrayList<String> DataScienceSkills = new ArrayList<>();
        DataScienceSkills.add("Python");
        DataScienceSkills.add("Machine Learning");
        DataScienceSkills.add("Deep Learning");
        DataScienceSkills.add("Data Analysis");
        DataScienceSkills.add("TensorFlow");
        DataScienceSkills.add("Pandas");
        DataScienceSkills.add("Scikit-learn");

        // Ajouter chaque cours avec ses informations
        courses.add(new CourseAcadymic("JEE", "Berrich Mohammed", "https://drive.google.com/drive/folders/1BvBRmfdaDS0NLVYC6o29-jm8T0wX4Mzm", J22Skills));
        courses.add(new CourseAcadymic("Dev Mobile", "Benouda Hanane", "https://drive.google.com/drive/folders/1qvYz4diMrWEwu2bYUNSVRrX22e_y2zUm", AndroidSkills));
        courses.add(new CourseAcadymic("Introduction to Python", "Haja Zakaria", "https://drive.google.com/python", PythonSkills));
        courses.add(new CourseAcadymic("Data Science", "Toumi Bouchentouf", "https://drive.google.com/datascience", DataScienceSkills));

        return courses;
    }
}
