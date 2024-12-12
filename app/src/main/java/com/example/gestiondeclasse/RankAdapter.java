package com.example.gestiondeclasse;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.StudentViewHolder> {

    private final List<Student> students;
    private final boolean isCompactView; // Détermine si l'affichage est compact

    public RankAdapter(List<Student> students, boolean isCompactView) {
        this.students = students;
        this.isCompactView = isCompactView;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rank_student_item, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Student student = students.get(position);
        // Affichage du rang, nom et score
        holder.rankTextView.setText(String.valueOf(position + 1));
        holder.nameTextView.setText(student.getName());
        holder.scoreTextView.setText(String.valueOf(student.getScore())+"%");

        // Affichage compact : cacher les détails si nécessaire
        holder.scoreTextView.setVisibility(isCompactView ? View.GONE : View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    static class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView rankTextView, nameTextView, scoreTextView;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            rankTextView = itemView.findViewById(R.id.student_rank);
            nameTextView = itemView.findViewById(R.id.student_name);
            scoreTextView = itemView.findViewById(R.id.student_score);
        }
    }
}
