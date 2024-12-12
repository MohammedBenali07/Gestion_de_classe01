package com.example.gestiondeclasse;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {
    private RecyclerView top3RecyclerView;
    private TextView overallRanking;
    private ProgressBar progressBar;
    private List<Student> students;
    private BottomNavigationView bottomNavigationView;
    private TextView pourcentageProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Initialisation des vues
        top3RecyclerView = findViewById(R.id.ranking_recycler_view);
        overallRanking = findViewById(R.id.overall_tanking_text);
        progressBar = findViewById(R.id.progressCircular); // Récupérer la ProgressBar
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        pourcentageProgress = findViewById(R.id.ProgressPercentage);

        // Simuler une liste d'étudiants
        students = new ArrayList<>();
        students.add(new Student("Alice", 95));
        students.add(new Student("Bob", 88));
        students.add(new Student("Charlie", 92));
        students.add(new Student("Diana", 85));
        students.add(new Student("Eve", 78));

        // Trier la liste par score décroissant
        Collections.sort(students, (s1, s2) -> s2.getScore() - s1.getScore());

        // Configurer le RecyclerView pour afficher les trois meilleurs
        List<Student> top3 = students.subList(0, Math.min(3, students.size()));
        top3RecyclerView.setLayoutManager(new LinearLayoutManager(this));
        top3RecyclerView.setAdapter(new RankAdapter(top3, false)); // Mode compact pour le top 3

        // Ajouter un listener pour afficher le popup des classements
        overallRanking.setOnClickListener(v -> showOverallRankingPopup());

        // Mise à jour du ProgressBar en fonction du score
        updateProgressBar();

        // Configurer le BottomNavigationView pour changer d'activité
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                startActivity(new Intent(DashboardActivity.this, DashboardActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_competence) {
                startActivity(new Intent(DashboardActivity.this, CompetenceActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_contact) {
                startActivity(new Intent(DashboardActivity.this, ContactActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_absence) {
                startActivity(new Intent(DashboardActivity.this, CalenderActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_profile) {
                startActivity(new Intent(DashboardActivity.this, ProfileActivity.class));
                return true;
            }
            return false;
        });

        TextView seeCourses = findViewById(R.id.see_courses);
        Button DiscoverTutorialsButton = findViewById(R.id.discover_tutorials);
        // Rediriger vers CoursesActivity lorsque l'utilisateur clique sur "see_courses"
        seeCourses.setOnClickListener(v -> redirectToActivity(CoursesActivity.class));
        DiscoverTutorialsButton.setOnClickListener(v -> redirectToActivity(FormationActivity.class));
    }

    private void updateProgressBar() {
        // Vérifiez si le texte contient un pourcentage valide
        try {
            String progressText = pourcentageProgress.getText().toString().trim(); // Récupérer le texte brut
            // Supprimer le symbole % si présent
            if (progressText.endsWith("%")) {
                progressText = progressText.substring(0, progressText.length() - 1);
            }
            int progressPercentage = Integer.parseInt(progressText); // Convertir en entier
            progressBar.setProgress(progressPercentage); // Mettre à jour la ProgressBar
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }


    private void showOverallRankingPopup() {
        // Inflater la vue du dialog
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_overall_ranking, null);
        RecyclerView dialogRecyclerView = dialogView.findViewById(R.id.popup_recycler_view);
        Button cancelButton = dialogView.findViewById(R.id.popup_button_cancel); // Bouton de fermeture

        // Configurer le RecyclerView du dialog
        dialogRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        dialogRecyclerView.setAdapter(new RankAdapter(students, false)); // Adapter pour afficher la liste complète des étudiants

        // Créer le Dialog avec une vue personnalisée
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView)
                .setCancelable(false);  // Empêcher le dialog de se fermer en cliquant à l'extérieur

        // Créer l'AlertDialog
        final AlertDialog dialog = builder.create();

        // Ajouter un fond personnalisé au dialog
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.background_dialog); // Utilisez un drawable comme fond

        // Configurer le bouton de fermeture
        cancelButton.setOnClickListener(v -> dialog.dismiss());

        // Afficher le dialog
        dialog.show();
    }

    // Méthode générique pour la redirection vers une activité
    private void redirectToActivity(Class<?> targetActivity) {
        Intent intent = new Intent(DashboardActivity.this, targetActivity);
        startActivity(intent);
    }
}
