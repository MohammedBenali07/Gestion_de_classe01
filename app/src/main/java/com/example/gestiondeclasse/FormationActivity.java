package com.example.gestiondeclasse;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FormationActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FormationAdapter formationAdapter;
    private ArrayList<Course> courseList = new ArrayList<>();
    private ArrayList<Course> filteredCourseList = new ArrayList<>();
    private ProgressBar progressBar;
    private int userId;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formation);

        // Initializing views
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);

        // Initializing DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        formationAdapter = new FormationAdapter(filteredCourseList);
        recyclerView.setAdapter(formationAdapter);

        // Setup Back button functionality
        ImageView iconBack = findViewById(R.id.icon_back);
        iconBack.setOnClickListener(v -> {
            Intent intent = new Intent(FormationActivity.this, DashboardActivity.class);
            startActivity(intent);
        });

        // Retrieve userId from shared preferences
        SharedPreferences sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE);
        userId = sharedPref.getInt("userId", -1);

// Fetch courses based on user skills
        // Fetch courses based on user skills
        Cursor cursor = dbHelper.getSkillsByUser(userId);
        if (cursor != null) {
            // Get the index of the column where skills are stored
            int skillColumnIndex = cursor.getColumnIndex(dbHelper.getSkillNameColumn());

            // Check if the column exists (the index should be >= 0)
            if (skillColumnIndex >= 0) {
                // Iterate through the skills found in the cursor
                while (cursor.moveToNext()) {
                    String skill = cursor.getString(skillColumnIndex);  // Fetch the skill based on valid column index
                    fetchCoursesFromWeb(skill);  // Fetch courses based on skill
                }
            } else {
                // Handle the case where the column index is invalid (column not found)
                Log.e("FormationActivity", "Skill column not found in the cursor.");
            }
            cursor.close();
        }

        // Modify if needed based on skills from cursor
        // Fetch courses via API
        fetchCourseData();
        // Implement search functionality
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

        // Display ProgressBar while loading data
        progressBar.setVisibility(View.VISIBLE);

        executor.execute(() -> {
            try {
                String[] urls = {
                        "https://api.coursera.org/api/courses.v1?q=search&query=computer%20science&fields=name,slug,photoUrl,partnerLogo",
                        "https://api.coursera.org/api/courses.v1?q=search&query=data%20science&fields=name,slug,photoUrl,partnerLogo",
                        "https://api.coursera.org/api/courses.v1?q=search&query=programming&fields=name,slug,photoUrl"
                };

                // Fetch data from multiple URLs
                for (String urlString : urls) {
                    fetchCourseFromApi(urlString);
                }

                // Once data is retrieved, update UI
                handler.post(() -> {
                    filteredCourseList.addAll(courseList);  // Display all courses initially
                    formationAdapter.notifyDataSetChanged();
                    Toast.makeText(FormationActivity.this, "Courses retrieved successfully", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE); // Hide ProgressBar after data is fetched
                });

            } catch (Exception e) {
                // Handle errors
                handler.post(() -> {
                    Toast.makeText(FormationActivity.this, "Error retrieving data", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);  // Hide ProgressBar if error occurs
                });
            }
        });
    }

    private void fetchCourseFromApi(String urlString) throws Exception {
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

            // Parse the JSON response
            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray courses = jsonResponse.getJSONArray("elements");

            // Loop through each course and add it to the list
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

    private void scrapeCoursera(String url) throws IOException {
        // Afficher dans le log que le scraping commence
        Log.d("Scraping", "Scraping started for URL: " + url);

        Document document = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                .get();

        // Vérifier si le document est bien récupéré
        if (document != null) {
            Log.d("Scraping", "Document fetched successfully");

            // Récupération des éléments contenant les informations sur les cours
            Elements courses = document.select(".css-2fumar");

            Log.d("Scraping", "Courses found: " + courses.size());
            if (courses.isEmpty()) {
                Log.d("Scraping", "No courses found with the selector '.ais-InfiniteHits-item'. Trying another selector.");
                courses = document.select("li"); // Essayons un autre sélecteur pour trouver les cours
                Log.d("Scraping", "Courses found with 'li' selector: " + courses.size());
            }

            for (Element courseElement : courses) {
                String title = courseElement.select(".cds-CommonCard-title.css-6ecy9b").text();
                String link = "https://www.coursera.org" + courseElement.select("a").attr("href");
                String imageUrl = courseElement.select("img").attr("src");

                // Ajouter des logs pour chaque élément
                Log.d("CourseScraping", "Title: " + title);
                Log.d("CourseScraping", "Link: " + link);
                Log.d("CourseScraping", "Image URL: " + imageUrl);

                // Vérifier si les données récupérées ne sont pas vides
                if (!title.isEmpty() && !link.isEmpty() && !imageUrl.isEmpty()) {
                    // Créer un objet Course
                    Course newCourse = new Course(title, link, imageUrl);
                    courseList.add(newCourse);
                } else {
                    Log.d("Scraping", "Incomplete course data found. Skipping this course.");
                }
            }
        } else {
            Log.d("Scraping", "Document is null or error fetching.");
        }
    }

    private void fetchCoursesFromWeb(final String query) {
        progressBar.setVisibility(View.VISIBLE);

        // Démarrer une tâche dans un autre thread pour effectuer le scraping
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String courseraUrl = "https://www.coursera.org/courses?query=" + query;
                    scrapeCoursera(courseraUrl);

                    // Mettre à jour l'UI après avoir récupéré les données
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                            formationAdapter.notifyDataSetChanged();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(FormationActivity.this, "Error scraping data", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }
            }
        }).start();
    }

    private void filterCourses(String query) {
        filteredCourseList.clear();
        if (query.isEmpty()) {
            filteredCourseList.addAll(courseList);
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
