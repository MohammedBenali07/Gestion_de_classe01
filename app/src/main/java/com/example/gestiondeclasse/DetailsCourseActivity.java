package com.example.gestiondeclasse;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import java.util.ArrayList;

public class DetailsCourseActivity extends AppCompatActivity {

    private GridView gridCompetencesCourse;
    private GridView gridInfosCompetencesCourse;
    private AppCompatButton course_ressourse;
    private TextView courseNameTextView;
    private TextView courseProfessorTextView;

    // Variables pour les données envoyées via l'Intent
    private String courseName;
    private String courseProfessor;
    private String[] competences;
    private ArrayList<String> CourseSkills;
    private String[] competencesTitles;
    private String driveLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_course);

        // Initialisation des composants de l'interface
        gridCompetencesCourse = findViewById(R.id.gridCompetencesCourse);
        gridInfosCompetencesCourse = findViewById(R.id.gridInfosCompetencesCourse);
        course_ressourse = findViewById(R.id.course_ressourse);
        courseNameTextView = findViewById(R.id.nom_course); // Correction
        courseProfessorTextView = findViewById(R.id.professor_name); // Correction

        // Récupération des données envoyées par l'Intent
        Intent intent = getIntent();
        courseName = intent.getStringExtra("course_name");
        courseProfessor = intent.getStringExtra("course_professor");
        competences = intent.getStringArrayExtra("competences");
        competencesTitles = intent.getStringArrayExtra("competences_titles");
        driveLink = intent.getStringExtra("drive_link");
        CourseSkills = intent.getStringArrayListExtra("course_skills");  // Correction

        // Affectation des données reçues aux TextView
        if (courseName != null) {
            courseNameTextView.setText(courseName);
        } else {
            courseNameTextView.setText("Nom du cours indisponible");
        }

        if (courseProfessor != null) {
            courseProfessorTextView.setText(courseProfessor);
        } else {
            courseProfessorTextView.setText("Professeur inconnu");
        }

        // Création et attribution des adaptateurs au GridView pour les compétences
        CompetancesCourseAdapter adapter1 = new CompetancesCourseAdapter(this, competences != null ? competences : getCompetences());
        gridCompetencesCourse.setAdapter(adapter1);

        // Création de l’adaptateur pour les informations liées aux compétences
        InfosCompetencesCourseAdapter adapter2 = new InfosCompetencesCourseAdapter(this,
                competencesTitles != null ? competencesTitles : getCompetencesTitles(),
                getCompetencesValues());
        gridInfosCompetencesCourse.setAdapter(adapter2);

        // Gestion du clic sur le bouton "course_ressourse"
        course_ressourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Utilisation du lien reçu par Intent ou d’un lien par défaut
                String resourceLink = driveLink != null ? driveLink : "https://drive.google.com/drive/folders/1QR3PjR8Zws84z81nvgDqM3SnEzQJNOgF";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(resourceLink));
                startActivity(intent);
            }
        });

        // Configuration du bouton retour
        ImageView iconBack = findViewById(R.id.icon_back);
        iconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Rediriger vers CoursesActivity
                Intent intent = new Intent(DetailsCourseActivity.this, CoursesActivity.class);
                startActivity(intent);
            }
        });
    }

    // Méthode pour obtenir une liste de compétences par défaut
    private String[] getCompetences() {
        return CourseSkills != null ? CourseSkills.toArray(new String[0]) : new String[] {};
    }

    // Méthode pour obtenir les titres des compétences par défaut
    private String[] getCompetencesTitles() {
        return new String[] {
                "DS1", "DS2", "Sessions", "Absences"
        };
    }

    // Méthode pour obtenir les valeurs des compétences par défaut
    private String[] getCompetencesValues() {
        return new String[] {
                "18", "17", "07", "02"
        };
    }
}
