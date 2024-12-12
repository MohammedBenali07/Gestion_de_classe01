package com.example.gestiondeclasse;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SpinnerUtils {

    /**
     * Configure un Spinner avec un ArrayAdapter et un listener.
     *
     * @param context         Le contexte de l'activité ou du fragment.
     * @param spinner         Le Spinner à configurer.
     * @param arrayResourceId L'ID du tableau de chaînes (R.array).
     * @param itemLayout      Le layout pour les éléments du Spinner (par exemple, R.layout.spinners_item).
     * @param listener        Le listener pour les événements de sélection.
     */
    public static void setupSpinner(Context context, Spinner spinner, int arrayResourceId, int itemLayout, AdapterView.OnItemSelectedListener listener) {
        // Créer l'adaptateur
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                context,
                arrayResourceId,
                itemLayout
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Associer l'adaptateur au Spinner
        spinner.setAdapter(adapter);

        // Définir le listener si fourni
        if (listener != null) {
            spinner.setOnItemSelectedListener(listener);
        }
    }
}