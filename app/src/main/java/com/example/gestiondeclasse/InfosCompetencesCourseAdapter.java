package com.example.gestiondeclasse;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class InfosCompetencesCourseAdapter extends BaseAdapter {

    private Context context;
    private String[] titles; // Utilisation de tableaux String[] au lieu de List<String>
    private String[] values;

    // Constructeur adapté aux données de type String[]
    public InfosCompetencesCourseAdapter(Context context, String[] titles, String[] values) {
        this.context = context;
        this.titles = titles;
        this.values = values;
    }

    @Override
    public int getCount() {
        return titles.length; // Assurez-vous que les deux tableaux ont la même taille.
    }

    @Override
    public Object getItem(int position) {
        return titles[position] + ": " + values[position]; // Optionnel.
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.infos_competences_course, parent, false);
            holder = new ViewHolder();
            holder.titleTextView = convertView.findViewById(R.id.info_title);
            holder.valueTextView = convertView.findViewById(R.id.info_value);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Remplir les données dans les TextViews
        holder.titleTextView.setText(titles[position]);
        holder.valueTextView.setText(values[position]);

        return convertView;
    }

    // ViewHolder pour optimiser les performances
    static class ViewHolder {
        TextView titleTextView;
        TextView valueTextView;
    }
}
