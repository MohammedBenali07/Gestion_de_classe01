package com.example.gestiondeclasse;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ImageView;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    private TextView fullName;
    private RecyclerView top3RecyclerView;
    private TextView overallRanking;
    private TextView career;
    private ProgressBar progressBar;
    private List<Student> students;
    private BottomNavigationView bottomNavigationView;
    private TextView pourcentageProgress;
    private ImageView imageProfile;

    // Variables pour stocker les données utilisateur
    private int userId;
    private String userName, userEmail, userSurname, userBirtday;
    private String userImageProfile;  // Chemin du fichier d'image

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Initialisation des vues
        top3RecyclerView = findViewById(R.id.ranking_recycler_view);
        overallRanking = findViewById(R.id.overall_tanking_text);
        progressBar = findViewById(R.id.progressCircular);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        pourcentageProgress = findViewById(R.id.ProgressPercentage);
        fullName = findViewById(R.id.fullName);
        imageProfile = findViewById(R.id.imgae_profile);
        career = findViewById(R.id.id_career);

        // Récupérer les données utilisateur depuis SharedPreferences
        SharedPreferences sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE);

        userId = sharedPref.getInt("userId", -1);
        userName = sharedPref.getString("userName", "");
        userSurname = sharedPref.getString("userSurName", "");
        userEmail = sharedPref.getString("userEmail", "");
        userBirtday = sharedPref.getString("userBirthday", "");
        userImageProfile = sharedPref.getString("userProfileImagePath", null);  // Chemin du fichier image
        // Vérifier que les données sont valides et les afficher
        if (userName != null && userSurname != null) {
            fullName.setText(userName + " " + userSurname);
        }
        career.setText(sharedPref.getString("userCareer", ""));

        // Afficher l'image de profil si disponible
        if (userImageProfile != null && !userImageProfile.isEmpty()) {
            try {
                // Charger l'image depuis le fichier local
                File imgFile = new  File(userImageProfile);
                if(imgFile.exists()){
                    Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    imageProfile.setImageBitmap(bitmap);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Erreur de chargement de l'image", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Afficher une image par défaut si l'image de profil n'est pas disponible
            imageProfile.setImageResource(R.drawable.mon_image);
            Toast.makeText(getApplicationContext(), "Aucune image de profil disponible", Toast.LENGTH_SHORT).show();
        }

        // Simuler une liste d'étudiants
        students = new ArrayList<>();
        students.add(new Student("Mohamed", 95));
        students.add(new Student("Said", 88));
        students.add(new Student("Imane", 92));
        students.add(new Student("Salma", 85));
        students.add(new Student("Mouad", 78));
        students.add(new Student("Khalid", 73));
        students.add(new Student("Samira", 68));
        students.add(new Student("Hiba", 60));
        students.add(new Student("Ridwane", 40));

        // Trier la liste par score décroissant
        Collections.sort(students, (s1, s2) -> s2.getScore() - s1.getScore());

        // Configurer le RecyclerView pour afficher les trois meilleurs
        List<Student> top3 = students.subList(0, Math.min(3, students.size()));
        top3RecyclerView.setLayoutManager(new LinearLayoutManager(this));
        top3RecyclerView.setAdapter(new RankAdapter(top3, false));

        // Ajouter un listener pour afficher le popup des classements
        overallRanking.setOnClickListener(v -> showOverallRankingPopup());

        // Mise à jour du ProgressBar en fonction du score
        updateProgressBar();

        // Configurer le BottomNavigationView pour changer d'activité
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                redirectToActivity(DashboardActivity.class);
                return true;
            } else if (item.getItemId() == R.id.nav_competence) {
                redirectToActivity(CompetenceActivity.class);
                return true;
            } else if (item.getItemId() == R.id.nav_contact) {
                redirectToActivity(ContactActivity.class);
                return true;
            } else if (item.getItemId() == R.id.nav_absence) {
                redirectToActivity(CalenderActivity.class);
                return true;
            } else if (item.getItemId() == R.id.nav_profile) {
                redirectToActivity(ProfileActivity.class);
                return true;
            }
            return false;
        });

        TextView seeCourses = findViewById(R.id.see_courses);
        Button discoverTutorialsButton = findViewById(R.id.discover_tutorials);

        // Rediriger vers CoursesActivity lorsque l'utilisateur clique sur "see_courses"
        seeCourses.setOnClickListener(v -> redirectToActivity(CoursesActivity.class));
        discoverTutorialsButton.setOnClickListener(v -> redirectToActivity(FormationActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Récupérer à nouveau les données utilisateur dans le cas où l'activité a été redémarrée
        SharedPreferences sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE);
        userName = sharedPref.getString("userName", "");
        userSurname = sharedPref.getString("userSurName", "");

        // Vérifiez que les données sont récupérées correctement
        if (userName != null && userSurname != null) {
            fullName.setText(userName + " " + userSurname);
        }

        // Vous pouvez également récupérer l'image de profil si elle est mise à jour
        userImageProfile = sharedPref.getString("userProfileImagePath", null);
        if (userImageProfile != null && !userImageProfile.isEmpty()) {
            try {
                // Charger l'image depuis le fichier local
                File imgFile = new  File(userImageProfile);
                if(imgFile.exists()){
                    Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    imageProfile.setImageBitmap(bitmap);
                    imageProfile.invalidate();  // Force le redessin de l'ImageView
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Erreur de chargement de l'image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateProgressBar() {
        try {
            String progressText = pourcentageProgress.getText().toString().trim();
            if (progressText.endsWith("%")) {
                progressText = progressText.substring(0, progressText.length() - 1);
            }
            int progressPercentage = Integer.parseInt(progressText);
            progressBar.setProgress(progressPercentage);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void showOverallRankingPopup() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_overall_ranking, null);
        RecyclerView dialogRecyclerView = dialogView.findViewById(R.id.popup_recycler_view);
        Button cancelButton = dialogView.findViewById(R.id.popup_button_cancel);

        dialogRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        dialogRecyclerView.setAdapter(new RankAdapter(students, false));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView).setCancelable(false);

        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.background_dialog);

        cancelButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    // Méthode pour inclure les données utilisateur dans l'Intent
    private void redirectToActivity(Class<?> targetActivity) {
        Intent intent = new Intent(DashboardActivity.this, targetActivity);
        // Récupérer les données directement depuis SharedPreferences pour éviter de les passer via Intent
        SharedPreferences sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE);
        intent.putExtra("userId", sharedPref.getInt("userId", -1));
        intent.putExtra("userName", sharedPref.getString("userName", ""));
        intent.putExtra("userSurName", sharedPref.getString("userSurName", ""));
        intent.putExtra("userEmail", sharedPref.getString("userEmail", ""));
        intent.putExtra("userBirthday", sharedPref.getString("userBirthday", ""));
        intent.putExtra("userProfileImagePath", sharedPref.getString("userProfileImagePath", null)); // Chemin de l'image si nécessaire
        startActivity(intent);
    }
}
