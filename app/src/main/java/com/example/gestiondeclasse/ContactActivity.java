package com.example.gestiondeclasse;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ContactActivity extends AppCompatActivity {

    private Spinner contactSpinner;
    private EditText subjectEditText, messageEditText;
    private Button addFilesButton, sendButton;

    private Uri selectedFileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        // Initialiser les vues
        contactSpinner = findViewById(R.id.contact_spinner);
        subjectEditText = findViewById(R.id.edit_subject);
        messageEditText = findViewById(R.id.edit_message);
        addFilesButton = findViewById(R.id.add_files_button);
        sendButton = findViewById(R.id.send_button);
        // Configurer le Spinner
        SpinnerUtils.setupSpinner(
                this,
                contactSpinner,
                R.array.Spinner_professeurs,
                android.R.layout.simple_spinner_item,
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String selectedTeacher = parent.getItemAtPosition(position).toString();
                        Toast.makeText(ContactActivity.this, "Selected: " + selectedTeacher, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );

        addFilesButton.setOnClickListener(v -> openFileChooser());

        sendButton.setOnClickListener(v -> {
            String subject = subjectEditText.getText().toString().trim();
            String message = messageEditText.getText().toString().trim();

            if (subject.isEmpty() || message.isEmpty()) {
                Toast.makeText(ContactActivity.this, "Please fill in all fields!", Toast.LENGTH_SHORT).show();
            } else {
                // Adresse email et mot de passe Gmail
                final String senderEmail = "";
                final String senderPassword = "";

                final String recipientEmail = contactSpinner.getSelectedItem().toString();;

                new Thread(() -> {
                    try {
                        String filePath = null;
                        if (selectedFileUri != null) {
                            filePath = FileUtil.getPath(ContactActivity.this, selectedFileUri);
                        }

                        EmailSender.sendEmail(senderEmail, senderPassword, recipientEmail, subject, message, filePath);
                        runOnUiThread(() ->
                                Toast.makeText(ContactActivity.this, "Email sent successfully!", Toast.LENGTH_SHORT).show()
                        );
                    } catch (Exception e) {
                        e.printStackTrace();
                        runOnUiThread(() ->
                                Toast.makeText(ContactActivity.this, "Failed to send email.", Toast.LENGTH_SHORT).show()
                        );
                    }
                }).start();
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*"); // Permet de choisir n'importe quel type de fichier
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedFileUri = data.getData();

            // Afficher le nom du fichier sélectionné dans un Toast
            String fileName = getFileName(selectedFileUri);
            Toast.makeText(this, "File selected: " + fileName, Toast.LENGTH_SHORT).show();
        }
    }

    // Récupère le nom du fichier sélectionné
    private String getFileName(Uri uri) {
        String fileName = null;

        // Vérifier si l'URI utilise le schéma "content"
        if ("content".equals(uri.getScheme())) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (displayNameIndex != -1) {
                        fileName = cursor.getString(displayNameIndex); // Récupérer le nom du fichier
                    } else {
                        Log.e("getFileName", "Column 'DISPLAY_NAME' not found in cursor");
                    }
                }
            } catch (Exception e) {
                Log.e("getFileName", "Error retrieving file name from content URI", e);
            }
        }

        // Si aucune information via le curseur, récupérer le chemin brut
        if (fileName == null) {
            String path = uri.getPath();
            if (path != null) {
                int lastSeparator = path.lastIndexOf('/');
                fileName = lastSeparator != -1 ? path.substring(lastSeparator + 1) : path;
            } else {
                Log.e("getFileName", "Failed to get file path from URI");
                fileName = "unknown_file"; // Fallback
            }
        }

        return fileName;
    }

}
