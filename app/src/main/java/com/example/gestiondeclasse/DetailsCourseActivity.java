package com.example.gestiondeclasse;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class DetailsCourseActivity extends AppCompatActivity {

    private GridView gridCompetencesCourse;
    private GridView gridInfosCompetencesCourse;
    private AppCompatButton course_ressourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_course);

        // Initialisation des composants de l'interface
        gridCompetencesCourse = findViewById(R.id.gridCompetencesCourse);
        gridInfosCompetencesCourse = findViewById(R.id.gridInfosCompetencesCourse);
        course_ressourse = findViewById(R.id.course_ressourse);

        // Création et attribution des adaptateurs au GridView
        CompetancesCourseAdapter adapter1 = new CompetancesCourseAdapter(this, getCompetences());
        gridCompetencesCourse.setAdapter(adapter1);

        InfosCompetencesCourseAdapter adapter2 = new InfosCompetencesCourseAdapter(this, getCompetencesTitles(), getCompetencesValues());
        gridInfosCompetencesCourse.setAdapter(adapter2);

        // Gestion du clic sur le bouton "course_ressourse"
        course_ressourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lien par défaut vers Google Drive
                String driveLink = "https://drive.google.com/drive/folders/1QR3PjR8Zws84z81nvgDqM3SnEzQJNOgF";  // Remplace ce lien par ton lien réel
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(driveLink));
                startActivity(intent);
            }
        });

        // Configuration du bouton retour
        ImageView iconBack = findViewById(R.id.icon_back); // Déplacement ici dans onCreate
        iconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Rediriger vers CoursesActivity
                Intent intent = new Intent(DetailsCourseActivity.this, CoursesActivity.class);
                startActivity(intent);
            }
        });
    }

    // Méthode pour obtenir une liste de compétences
    private String[] getCompetences() {
        return new String[]{
                "Html", "Css", "Js", "Figma",
                "React", "PHP", "SQL"
        };
    }

    // Méthode pour obtenir les titres des compétences
    private String[] getCompetencesTitles() {
        return new String[]{
                "DS1", "DS2", "Sessions", "Absences"
        };
    }

    // Méthode pour obtenir les valeurs des compétences
    private String[] getCompetencesValues() {
        return new String[]{
                "18", "17", "07", "02"
        };
    }
}
