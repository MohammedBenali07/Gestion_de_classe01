package com.example.gestiondeclasse;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CompetancesCourseAdapter extends BaseAdapter {

    private Context context;
    private String[] competences;

    // Constructeur
    public CompetancesCourseAdapter(Context context, String[] competences) {
        this.context = context;
        this.competences = competences;
    }

    @Override
    public int getCount() {
        return competences.length;
    }

    @Override
    public Object getItem(int position) {
        return competences[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Récupérer ou créer la vue pour chaque élément
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.competences_course_view, parent, false);
        }

        // Lier les vues avec les données
        TextView competenceName = convertView.findViewById(R.id.competenceName);

        competenceName.setText(competences[position]);

        return convertView;
    }
}
