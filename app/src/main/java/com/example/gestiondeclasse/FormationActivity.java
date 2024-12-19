package com.example.gestiondeclasse;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FormationActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FormationAdapter formationAdapter;
    private ArrayList<Course> courseList = new ArrayList<>();
    private ArrayList<Course> filteredCourseList = new ArrayList<>();
    private ProgressBar progressBar; // Déclaration du ProgressBar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formation);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        formationAdapter = new FormationAdapter(filteredCourseList);
        recyclerView.setAdapter(formationAdapter);

        // Initialize ProgressBar
        progressBar = findViewById(R.id.progressBar);  // Trouver la référence au ProgressBar

        // Fetch data from multiple Coursera API URLs
        fetchCourseData();

        // Configure back button
        ImageView iconBack = findViewById(R.id.icon_back);
        iconBack.setOnClickListener(v -> {
            Intent intent = new Intent(FormationActivity.this, DashboardActivity.class);
            startActivity(intent);
        });

        // Set up search functionality
        EditText searchInput = findViewById(R.id.search_formation);
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                filterCourses(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    private void fetchCourseData() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        // Afficher le ProgressBar avant de commencer la récupération des données
        progressBar.setVisibility(View.VISIBLE);

        executor.execute(() -> {
            try {
                String[] urls = {
                        "https://api.coursera.org/api/courses.v1?q=search&query=computer%20science&fields=name,slug,photoUrl,partnerLogo",
                        "https://api.coursera.org/api/courses.v1?q=search&query=data%20science&fields=name,slug,photoUrl,partnerLogo",
                        "https://api.coursera.org/api/courses.v1?q=search&query=programming&fields=name,slug,photoUrl"
                };

                // Pour chaque URL, on effectue la requête
                for (String urlString : urls) {
                    URL url = new URL(urlString);

                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Accept", "application/json");

                    int responseCode = connection.getResponseCode();

                    if (responseCode == 200) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        String line;

                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        reader.close();

                        // Parse JSON
                        JSONObject jsonResponse = new JSONObject(response.toString());
                        JSONArray courses = jsonResponse.getJSONArray("elements");

                        // Pour chaque cours dans la réponse
                        for (int i = 0; i < courses.length(); i++) {
                            JSONObject course = courses.getJSONObject(i);
                            String title = course.getString("name");
                            String link = "https://www.coursera.org/learn/" + course.getString("slug");
                            String imageUrl = course.optString("photoUrl", "");

                            Course newCourse = new Course(title, link, imageUrl);
                            courseList.add(newCourse);
                        }
                    }
                }

                // Mise à jour du RecyclerView après avoir récupéré toutes les données
                handler.post(() -> {
                    filteredCourseList.addAll(courseList);  // Afficher tous les cours au départ
                    formationAdapter.notifyDataSetChanged();
                    Toast.makeText(FormationActivity.this, "Cours récupérés avec succès!", Toast.LENGTH_SHORT).show();

                    // Cacher le ProgressBar après la récupération des données
                    progressBar.setVisibility(View.GONE);
                });

            } catch (Exception e) {
                handler.post(() -> {
                    Toast.makeText(FormationActivity.this, "Erreur lors de la récupération des données.", Toast.LENGTH_SHORT).show();

                    // Cacher le ProgressBar en cas d'erreur
                    progressBar.setVisibility(View.GONE);
                });
            }
        });
    }

    private void filterCourses(String query) {
        filteredCourseList.clear();
        if (query.isEmpty()) {
            filteredCourseList.addAll(courseList); // Si la recherche est vide, afficher tous les cours
        } else {
            for (Course course : courseList) {
                if (course.getTitle().toLowerCase().contains(query.toLowerCase())) {
                    filteredCourseList.add(course);
                }
            }
        }
        formationAdapter.notifyDataSetChanged();
    }
}