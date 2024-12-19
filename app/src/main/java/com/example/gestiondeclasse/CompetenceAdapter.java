package com.example.gestiondeclasse;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

public class CompetenceAdapter extends BaseAdapter {

    private Context context;
    private List<Competence> competenceList;
    private LayoutInflater inflater;

    public CompetenceAdapter(Context context, List<Competence> competenceList) {
        this.context = context;
        this.competenceList = competenceList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return competenceList.size();
    }

    @Override
    public Object getItem(int position) {
        return competenceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Si convertView est null, il faut le remplir avec le layout correct
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.competence_view, parent, false);
        }

        // Récupérez les éléments du layout
        TextView nomCompetanceTextView = convertView.findViewById(R.id.nom_competance);
        ProgressBar progressCircular = convertView.findViewById(R.id.progressCircular);
        TextView progressPercentageTextView = convertView.findViewById(R.id.ProgressPercentage);

        // Récupérez la compétence en question de la liste
        Competence competence = competenceList.get(position);

        // Assurez-vous que les vues sont correctement initialisées
        if (nomCompetanceTextView != null) {
            nomCompetanceTextView.setText(competence.getName());  // Afficher le nom de la compétence
        }

        // Si la progression est présente dans la compétence, appliquez-la
        if (progressCircular != null && progressPercentageTextView != null) {
            int progress = competence.getProgress(); // Suppose que votre modèle `Competence` a cette méthode
            progressCircular.setProgress(progress);
            progressPercentageTextView.setText(progress + "%");  // Afficher le pourcentage de la progression
        }

        return convertView;
    }
}
