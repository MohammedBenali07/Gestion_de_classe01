package com.example.gestiondeclasse;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class FormationAdapter extends RecyclerView.Adapter<FormationAdapter.FormationViewHolder> {

    private ArrayList<Course> courseList;

    // Constructeur de l'adaptateur
    public FormationAdapter(ArrayList<Course> courseList) {
        this.courseList = courseList;
    }

    @Override
    public FormationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate le layout pour chaque élément de la liste
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course, parent, false);
        return new FormationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FormationViewHolder holder, int position) {
        // Récupère un cours dans la liste
        Course course = courseList.get(position);

        // Remplir les données dans la vue
        holder.titleTextView.setText(course.getTitle());
        holder.linkTextView.setText(course.getLink());

        // Charger l'image à partir de l'URL (si présente) avec Glide
        if (!course.getImageUrl().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(course.getImageUrl())
                    .into(holder.courseImageView);
        }

        // Gérer les clics pour rediriger vers le lien de la formation
        holder.itemView.setOnClickListener(v -> {
            // Créer une Intent pour ouvrir le lien dans le navigateur
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(course.getLink()));
            // Vérifier si l'intent peut être résolu (c'est-à-dire si un navigateur est disponible)
            if (intent.resolveActivity(holder.itemView.getContext().getPackageManager()) != null) {
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    // Classe ViewHolder pour le recyclage des vues
    public static class FormationViewHolder extends RecyclerView.ViewHolder {

        private TextView titleTextView;
        private TextView linkTextView;
        private ImageView courseImageView;

        public FormationViewHolder(View itemView) {
            super(itemView);

            // Initialiser les vues du layout de l'élément
            titleTextView = itemView.findViewById(R.id.titleTextView);
            linkTextView = itemView.findViewById(R.id.linkTextView);
            courseImageView = itemView.findViewById(R.id.courseImageView);
        }
    }
}
