package com.example.gestiondeclasse;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CompetenceAdapter extends BaseAdapter {

    private Context context;
    private String[] skillNames;
    private int[] skillLogos;
    private int[] skillProgress;

    public CompetenceAdapter(Context context, String[] skillNames, int[] skillLogos, int[] skillProgress) {
        this.context = context;
        this.skillNames = skillNames;
        this.skillLogos = skillLogos;
        this.skillProgress = skillProgress;
    }

    @Override
    public int getCount() {
        return skillNames.length;
    }

    @Override
    public Object getItem(int position) {
        return skillNames[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.competence_view, parent, false);
        }

        TextView tvSkillName = convertView.findViewById(R.id.nom_competance);
        ProgressBar progressCircular = convertView.findViewById(R.id.progressCircular);
        TextView tvProgressPercentage = convertView.findViewById(R.id.ProgressPercentage);
        ImageView ivSkillLogo = convertView.findViewById(R.id.competenceLogo);

        // Définir les données pour chaque compétence
        tvSkillName.setText(skillNames[position]);
        progressCircular.setProgress(skillProgress[position]);
        tvProgressPercentage.setText(skillProgress[position] + "%");
        ivSkillLogo.setImageResource(skillLogos[position]);

        return convertView;
    }
}
