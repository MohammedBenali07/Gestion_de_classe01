package com.example.gestiondeclasse;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class ContactActivity extends AppCompatActivity {

    private Spinner contactSpinner;
    private EditText subjectEditText, messageEditText;
    private AppCompatButton addFilesButton, sendButton;
    private Uri fileUri;  // URI du fichier sélectionné

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
                        // Aucune action nécessaire si rien n'est sélectionné
                    }
                }
        );

        // Pré-sélectionner une personne par défaut dans le Spinner
        contactSpinner.setSelection(0); // Change l'index si nécessaire, ici c'est l'index 0 (premier élément)

        // Ajouter un écouteur pour le bouton "Add files"
        addFilesButton.setOnClickListener(v -> {
            // Ouvrir le sélecteur de fichiers
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");  // Permet de choisir tous types de fichiers
            startActivityForResult(intent, 1);  // 1 est un code de requête arbitraire
        });

        // Ajouter un écouteur pour le bouton "Send"
        sendButton.setOnClickListener(v -> {
            String subject = subjectEditText.getText().toString().trim();
            String message = messageEditText.getText().toString().trim();
            String selectedTeacher = contactSpinner.getSelectedItem().toString();

            if (subject.isEmpty() || message.isEmpty()) {
                Toast.makeText(ContactActivity.this, "Please fill in all fields!", Toast.LENGTH_SHORT).show();
            } else {
                // Créer un Intent pour envoyer un e-mail sans choisir de client email
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("message/rfc822"); // Type pour les emails

                // Remplir les champs avec le sujet, message et email de destination
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"mohammed07.benali00@gmail.com"});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                emailIntent.putExtra(Intent.EXTRA_TEXT, message);

                // Joindre un fichier si sélectionné
                if (fileUri != null) {
                    emailIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
                }

                // Vérifier si il existe des applications pouvant gérer l'email
                try {
                    startActivity(Intent.createChooser(emailIntent, "Send email via"));
                } catch (android.content.ActivityNotFoundException e) {
                    Toast.makeText(ContactActivity.this, "No email clients found.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Configuration du bouton retour
        ImageView iconBack = findViewById(R.id.icon_back); // Déplacement ici dans onCreate
        iconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Rediriger vers DashboardActivity
                Intent intent = new Intent(ContactActivity.this, DashboardActivity.class);
                startActivity(intent);
            }
        });
    }

    // Méthode qui capture le résultat du sélecteur de fichiers
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data != null) {
                fileUri = data.getData();  // Récupérer l'URI du fichier sélectionné
                Toast.makeText(ContactActivity.this, "File selected: " + fileUri, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
