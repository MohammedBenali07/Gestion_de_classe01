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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the fragment layout
        View view = inflater.inflate(R.layout.fragment_1, container, false);

        // Initialize the TextViews for displaying information
        tvEmail = view.findViewById(R.id.id_email_fragment1);
        tvBirthday = view.findViewById(R.id.id_birthday_fragment1);
        tvAddress = view.findViewById(R.id.id_address_fragment1);

        // Initialize the button for showing the popup
        View editPenIcon = view.findViewById(R.id.edit_pen);

        // Set the OnClickListener for the edit icon
        editPenIcon.setOnClickListener(v -> showEditDialog(view));

        return view;
    }

    // Method to show the edit popup
    private void showEditDialog(View view) {
        // Create an AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        // Inflate the dialog layout
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_fragment1, null);

        // Initialize the form elements in the dialog
        editEmail = dialogView.findViewById(R.id.edit_email);
        editBirthday = dialogView.findViewById(R.id.edit_birthday);
        editAddress = dialogView.findViewById(R.id.edit_address);

        // Initialize the buttons for save and cancel actions
        btnSaveChanges = dialogView.findViewById(R.id.btn_save);
        btnCancelChanges = dialogView.findViewById(R.id.btn_cancel);

        // Pre-fill the fields with current data
        editEmail.setText(tvEmail.getText().toString());
        editBirthday.setText(tvBirthday.getText().toString());
        editAddress.setText(tvAddress.getText().toString());

        // Set up a DatePickerDialog for the birthday field
        editBirthday.setOnClickListener(v -> {
            // Get the current date to initialize the DatePicker
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            // Create the DatePickerDialog with the custom theme
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    getContext(),
                    R.style.CustomDatePicker,  // Apply custom style
                    (view1, selectedYear, selectedMonth, selectedDay) -> {
                        // Format the selected date and display it in the EditText
                        String formattedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        editBirthday.setText(formattedDate);
                    },
                    year, month, day
            );

            // Optional: Restrict to past dates (e.g., for a birthday field)
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

            // Show the DatePickerDialog
            datePickerDialog.show();
        });

        builder.setView(dialogView);

        // Create and show the dialog
        AlertDialog dialog = builder.create();

        // Action to save the changes
        btnSaveChanges.setOnClickListener(v -> {
            // Get the new values from the fields
            String email = editEmail.getText().toString();
            String birthday = editBirthday.getText().toString();
            String address = editAddress.getText().toString();

            // Update the TextViews with the new data
            tvEmail.setText(email);
            tvBirthday.setText(birthday);
            tvAddress.setText(address);

            // Close the dialog
            dialog.dismiss();
        });

        // Action to cancel the changes
        btnCancelChanges.setOnClickListener(v -> dialog.dismiss());

        // Show the dialog
        dialog.show();
    }
}
