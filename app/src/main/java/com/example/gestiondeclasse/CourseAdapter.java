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

    private List<CourseAcadymic> courses;  // Liste des cours filtrés
    private final List<CourseAcadymic> allCourses;  // Liste des cours non filtrés (originale)
    private final Context context;

    // Constructeur
    public CourseAdapter(Context context, List<CourseAcadymic> courses, List<CourseAcadymic> allCourses) {
        this.context = context;
        this.courses = courses;
        this.allCourses = allCourses;  // Sauvegarder la liste originale des cours
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Charger le layout pour chaque élément de la liste
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_view, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        CourseAcadymic course = courses.get(position);

        // Validation pour éviter les NullPointerExceptions
        if (course != null) {
            holder.courseText.setText(course.getCourseName() != null ? course.getCourseName() : "Nom du cours non disponible");
        }

        // Ajouter un OnClickListener pour rediriger vers DetailsCourseActivity
        holder.itemView.setOnClickListener(v -> {
            if (context != null) {
                Intent intent = new Intent(context, DetailsCourseActivity.class);
                // Passer les informations du cours à l'activité de détails
                intent.putExtra("course_name", course.getCourseName());
                intent.putExtra("course_professor", course.getProfessorName());
                intent.putExtra("course_drive_link", course.getDriveLink());
                intent.putStringArrayListExtra("course_skills", new ArrayList<>(course.getSkills()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return courses != null ? courses.size() : 0;
    }

    // Méthode pour mettre à jour la liste des cours filtrés
    public void updateCourses(List<CourseAcadymic> filteredCourses) {
        this.courses = filteredCourses;
        notifyDataSetChanged();  // Met à jour le RecyclerView avec la nouvelle liste filtrée
    }

    // Méthode pour filtrer la liste des cours
    public void filter(String query) {
        List<CourseAcadymic> filteredList = new ArrayList<>();

        if (query.isEmpty()) {
            filteredList.addAll(allCourses);  // Si la recherche est vide, afficher tous les cours
        } else {
            for (CourseAcadymic course : allCourses) {
                if (course.getCourseName() != null && course.getCourseName().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(course);
                }
            }
        }

        updateCourses(filteredList);  // Met à jour la liste filtrée
    }

    // ViewHolder pour les éléments de la liste
    public static class CourseViewHolder extends RecyclerView.ViewHolder {

        TextView courseText; // Texte du nom du cours

        public CourseViewHolder(View itemView) {
            super(itemView);
            courseText = itemView.findViewById(R.id.nom_course); // Assurez-vous que ce TextView est défini dans course_view.xml
        }
    }
}
