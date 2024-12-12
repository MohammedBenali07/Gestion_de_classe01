package com.example.gestiondeclasse;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.Random;

public class SessionAdapter extends RecyclerView.Adapter<SessionAdapter.ViewHolder> {

    private final List<String> sessions;
    private final List<String> presence;
    private final Context context;
    private final Random random = new Random();

    public SessionAdapter(List<String> sessions, List<String> presence, Context context) {
        this.sessions = sessions;
        this.presence = presence;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.session_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Définir le texte de session_numer
        holder.sessionNumberTextView.setText("Session " + (position + 1));

        // Définir uniquement le texte de la session à partir de la liste
        String session = sessions.get(position);
        holder.sessionTextView.setText(session); // Affiche uniquement la donnée de sessions

        // Définir le texte de présence
        String pres = presence.get(position);
        holder.presenceTextView.setText(pres);

        // Couleur aléatoire pour indiquer la présence ou l'absence
        if (random.nextBoolean()) {
            holder.sessionTextView.setBackgroundColor(android.graphics.Color.RED);
            holder.presenceTextView.setText("Absent");
        } else {
            holder.sessionTextView.setBackgroundColor(android.graphics.Color.GREEN);
            holder.presenceTextView.setText("Present");
        }

        // Gestion du clic sur un élément
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(session, pres); // Passez les données au dialogue
            }
        });
    }

    private void showPopup(String session, String presence) {
        // Créer un AlertDialog.Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Charger le layout personnalisé
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.popup_layout, null);

        // Ajouter l'arrière-plan personnalisé au conteneur racine
        dialogView.setBackgroundResource(R.drawable.background_card_white); // Background personnalisé

        // Ajouter les données dynamiquement dans le layout
        TextView cancel = dialogView.findViewById(R.id.btn_cancel);

        // Créer et afficher le dialogue
        AlertDialog dialog = builder.create();
        dialog.setView(dialogView);
        dialog.show();

        // Gestion du bouton "Fermer"
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss(); // Fermer le dialogue
            }
        });
    }


    @Override
    public int getItemCount() {
        return sessions.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView sessionTextView;
        TextView presenceTextView;
        TextView sessionNumberTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sessionTextView = itemView.findViewById(R.id.session_text);
            presenceTextView = itemView.findViewById(R.id.item);
            sessionNumberTextView = itemView.findViewById(R.id.session_numer);
        }
    }
}
