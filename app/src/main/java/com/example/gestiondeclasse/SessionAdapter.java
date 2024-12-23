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
        holder.sessionNumberTextView.setText("Session " + (position + 1));
        String session = sessions.get(position);
        holder.sessionTextView.setText(session);
        String pres = presence.get(position);
        holder.presenceTextView.setText(pres);

        boolean isPresent = random.nextBoolean();
        holder.sessionTextView.setBackgroundColor(isPresent ? android.graphics.Color.GREEN : android.graphics.Color.RED);
        holder.presenceTextView.setText(isPresent ? "Present" : "Absent");

        holder.itemView.setOnClickListener(v -> showPopup(session, holder.presenceTextView.getText().toString()));
    }

    private void showPopup(String session, String presence) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.popup_layout, null);
        dialogView.setBackgroundResource(R.drawable.background_card_white);
        TextView cancel = dialogView.findViewById(R.id.btn_cancel);

        AlertDialog dialog = builder.create();
        dialog.setView(dialogView);
        dialog.show();

        cancel.setOnClickListener(v -> dialog.dismiss());
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

