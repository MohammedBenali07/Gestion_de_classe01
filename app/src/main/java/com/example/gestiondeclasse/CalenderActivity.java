package com.example.gestiondeclasse;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import java.util.ArrayList;
import java.util.List;

public class CalenderActivity extends AppCompatActivity {
    private static final String TAG = "CalenderActivity";
    private static final int REQUEST_CODE_SCAN = 1;
    private List<String> sessions;
    private List<String> presence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendrer);

        // Trouver les éléments de l'interface
        TextView btnScanQr = findViewById(R.id.btn_scan_qr);
        CalendarView calendarView = findViewById(R.id.calendarView);
        RecyclerView recyclerView = findViewById(R.id.recycler_view_sessions);

        sessions = new ArrayList<>();
        presence = new ArrayList<>();

        // Remplir les données de session et de présence
        for (int i = 1; i <= 4; i++) {
            sessions.add("S" + i);
            presence.add("P");
        }

        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        SessionAdapter adapter = new SessionAdapter(sessions, presence, this);
        recyclerView.setAdapter(adapter);

        // Écouteur pour les changements sur le calendrier
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            adapter.notifyDataSetChanged();
        });

        // Scanner le code QR lorsque l'utilisateur clique sur le bouton
        btnScanQr.setOnClickListener(v -> {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            IntentIntegrator integrator = new IntentIntegrator(this);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
            integrator.setPrompt("Scan a QR Code");
            integrator.setCameraId(0); // Utiliser l'appareil photo arrière
            integrator.setBeepEnabled(true); // Son lors du scan
            integrator.setBarcodeImageEnabled(true); // Enregistrer une image du code scanné
            integrator.initiateScan();
        });

        // Bouton de retour à l'écran Dashboard
        ImageView iconBack = findViewById(R.id.icon_back);
        iconBack.setOnClickListener(v -> {
            Intent intent = new Intent(CalenderActivity.this, DashboardActivity.class);
            startActivity(intent);
        });
    }

    // Gérer le résultat du scan dans onActivityResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Vérifier que c'est le bon code de demande (le scan QR)
        if (requestCode == IntentIntegrator.REQUEST_CODE) {
            // Analyser les résultats du scan
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

            if (result != null && result.getContents() != null) {
                // Si le scan est réussi, afficher un AlertDialog avec le contenu scanné
                String scannedContent = result.getContents();
                Log.d(TAG, "QR Code scanné: " + scannedContent);

                // Créer et afficher l'AlertDialog
                runOnUiThread(() -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CalenderActivity.this);
                    View dialogView = getLayoutInflater().inflate(R.layout.dialo_scan_success, null);


                    AppCompatButton closeButton = dialogView.findViewById(R.id.dialog_close_button);
                    builder.setView(dialogView);

                    AlertDialog dialog = builder.create();
                    dialog.show();

                    // Fermer le dialog lorsque l'utilisateur clique sur le bouton
                    closeButton.setOnClickListener(v -> dialog.dismiss());
                });
            } else {
                Log.d(TAG, "Scan annulé ou pas de contenu");
                Toast.makeText(this, "Scan annulé ou pas de contenu", Toast.LENGTH_LONG).show();
            }
        }
    }
}
