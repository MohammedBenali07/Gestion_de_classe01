package com.example.gestiondeclasse;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class FormationAdapter extends RecyclerView.Adapter<FormationAdapter.FormationViewHolder> {

    private ArrayList<Course> courseList;

    // Constructor for the adapter
    public FormationAdapter(ArrayList<Course> courseList) {
        this.courseList = courseList;
    }

    @Override
    public FormationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the layout for each item in the list
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course, parent, false);
        return new FormationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FormationViewHolder holder, int position) {
        // Retrieve a course from the list
        Course course = courseList.get(position);

        // Fill data into the views
        holder.titleTextView.setText(course.getTitle());

        // Load image from URL (if present) using Glide
        if (!course.getImageUrl().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(course.getImageUrl())
                    .into(holder.courseImageView);
        }

        // Handle clicks to redirect to the course link
        holder.itemView.setOnClickListener(v -> {
            String courseLink = course.getLink();

            // Log the URL to ensure it's correct
            Log.d("FormationAdapter", "Redirecting to: " + courseLink);

            // Ensure the URL starts with "http" or "https"
            if (courseLink != null && !courseLink.startsWith("http")) {
                courseLink = "https://" + courseLink;  // Add "https://" if missing
            }

            // Verify the URL format
            if (courseLink != null && !courseLink.isEmpty()) {
                // Create an Intent to open the link in a browser
                Uri uri = Uri.parse(courseLink);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);

                // Check if there is a browser to handle the intent
                if (intent.resolveActivity(holder.itemView.getContext().getPackageManager()) != null) {
                    holder.itemView.getContext().startActivity(intent);  // Start activity to open the link
                } else {
                    Toast.makeText(holder.itemView.getContext(), "No browser available to open the link", Toast.LENGTH_SHORT).show();  // Show a message if no browser is available
                }
            } else {
                Toast.makeText(holder.itemView.getContext(), "Invalid course link", Toast.LENGTH_SHORT).show();  // Show a message for invalid link
            }
        });
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    // ViewHolder class to optimize view recycling
    public static class FormationViewHolder extends RecyclerView.ViewHolder {

        private TextView titleTextView; // For the course title
        private ImageView courseImageView; // For the course image

        public FormationViewHolder(View itemView) {
            super(itemView);

            // Initialize views from the item_course layout
            titleTextView = itemView.findViewById(R.id.titleTextView);
            courseImageView = itemView.findViewById(R.id.courseImageView);
        }
    }
}
