package com.example.gestiondeclasse;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.util.Calendar;

public class Fragment1 extends Fragment {

    private TextView tvEmail, tvBirthday, tvAddress;
    private EditText editEmail, editBirthday, editAddress;
    private Button btnSaveChanges, btnCancelChanges;

    // Variables pour l'utilisateur
    private String email;
    private String birthday;
    private String address;
    private int userId; // Identifiant de l'utilisateur pour les mises à jour

    private DatabaseHelper databaseHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Charger la vue de fragment
        View view = inflater.inflate(R.layout.fragment_1, container, false);

        // Initialiser la base de données
        databaseHelper = DatabaseHelper.getInstance(getContext());

        // Récupérer les données passées dans les arguments
        if (getArguments() != null) {
            userId = getArguments().getInt("userId", -1); // Assurez-vous que userId est passé dans les arguments
            email = getArguments().getString("email");
            birthday = getArguments().getString("birthday");
            address = getArguments().getString("Address", "Oujda");
        }

        // Lier les TextViews pour afficher les informations
        tvEmail = view.findViewById(R.id.id_email_fragment1);
        tvBirthday = view.findViewById(R.id.id_birthday_fragment1);
        tvAddress = view.findViewById(R.id.id_address_fragment1);

        // Pré-remplir les TextViews
        tvEmail.setText(email);
        tvBirthday.setText(birthday);
        tvAddress.setText(address);

        // Icône pour lancer l'édition
        View editPenIcon = view.findViewById(R.id.edit_pen);

        // Ajouter un événement pour l'icône d'édition
        editPenIcon.setOnClickListener(v -> showEditDialog());

        return view;
    }

    // Méthode pour afficher le dialogue d'édition
    private void showEditDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_fragment1, null);

        editEmail = dialogView.findViewById(R.id.edit_email);
        editBirthday = dialogView.findViewById(R.id.edit_birthday);
        editAddress = dialogView.findViewById(R.id.edit_address);
        btnSaveChanges = dialogView.findViewById(R.id.btn_save);
        btnCancelChanges = dialogView.findViewById(R.id.btn_cancel);

        // Pré-remplir les champs d'édition
        editEmail.setText(email);
        editBirthday.setText(birthday);
        editAddress.setText(address);

        // Configurer le sélecteur de date
        editBirthday.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    getContext(),
                    R.style.CustomDatePicker,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        String formattedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        editBirthday.setText(formattedDate);
                    },
                    year, month, day
            );
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePickerDialog.show();
        });

        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        // Action pour sauvegarder les modifications
        btnSaveChanges.setOnClickListener(v -> {
            String newEmail = editEmail.getText().toString();
            String newBirthday = editBirthday.getText().toString();
            String newAddress = editAddress.getText().toString();

            // Mise à jour dans la base de données
            boolean isUpdated = databaseHelper.updateUserDetails(userId, newEmail, newBirthday, newAddress);
            if (isUpdated) {
                // Mettre à jour les TextViews
                tvEmail.setText(newEmail);
                tvBirthday.setText(newBirthday);
                tvAddress.setText(newAddress);

                // Mettre à jour les variables
                email = newEmail;
                birthday = newBirthday;
                address = newAddress;
            }

            // Fermer le dialogue
            dialog.dismiss();
        });

        // Action pour annuler
        btnCancelChanges.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }
}
