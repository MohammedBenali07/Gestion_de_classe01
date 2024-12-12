package com.example.gestiondeclasse;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    private List<String> courses;  // Liste des cours filtrés
    private List<String> allCourses;  // Liste des cours non filtrés (originale)
    private Context context;

    // Constructeur
    public CourseAdapter(Context context, List<String> courses, List<String> allCourses) {
        this.context = context;
        this.courses = courses;
        this.allCourses = allCourses;  // Sauvegarder la liste originale des cours
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_view, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        String courseName = courses.get(position);
        holder.courseText.setText(courseName);

        // Ajouter un OnClickListener pour chaque élément
        holder.itemView.setOnClickListener(v -> {
            // Créer une intention pour rediriger vers DetailsActivity
            Intent intent = new Intent(context, DetailsCourseActivity.class);
            // Passer les informations du cours sélectionné à l'activité
            intent.putExtra("course_name", courseName);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    // Méthode pour mettre à jour la liste des cours filtrés
    public void updateCourses(List<String> filteredCourses) {
        this.courses = filteredCourses;
        notifyDataSetChanged();  // Met à jour le RecyclerView avec la nouvelle liste filtrée
    }

    // Méthode pour filtrer la liste des cours en fonction de la recherche
    public void filter(String query) {
        List<String> filteredList = new ArrayList<>();

        if (query.isEmpty()) {
            filteredList.addAll(allCourses);  // Si la recherche est vide, afficher tous les cours
        } else {
            for (String course : allCourses) {
                if (course.toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(course);
                }
            }
        }

        updateCourses(filteredList);  // Met à jour la liste filtrée
    }

    public static class CourseViewHolder extends RecyclerView.ViewHolder {

        TextView courseText;

        public CourseViewHolder(View itemView) {
            super(itemView);
            courseText = itemView.findViewById(R.id.nom_course);
        }
    }
}
