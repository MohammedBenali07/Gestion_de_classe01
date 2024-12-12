package com.example.gestiondeclasse;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class DetailsCourseActivity extends AppCompatActivity {

    private GridView gridCompetencesCourse;
    private GridView gridInfosCompetencesCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_course);

        // Initialisation des composants de l'interface
        gridCompetencesCourse = findViewById(R.id.gridCompetencesCourse);
        gridInfosCompetencesCourse = findViewById(R.id.gridInfosCompetencesCourse);

        // Création et attribution des adaptateurs au GridView
        CompetancesCourseAdapter adapter1 = new CompetancesCourseAdapter(this, getCompetences());
        gridCompetencesCourse.setAdapter(adapter1);

        InfosCompetencesCourseAdapter adapter2 = new InfosCompetencesCourseAdapter(this, getCompetencesTitles(), getCompetencesValues());
        gridInfosCompetencesCourse.setAdapter(adapter2);
        // Configuration du bouton retour
        ImageView iconBack = findViewById(R.id.icon_back); // Déplacement ici dans onCreate
        iconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Rediriger vers DashboardActivity
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
