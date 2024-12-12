package com.example.gestiondeclasse;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import java.util.ArrayList;
import java.util.List;

public class CalenderActivity extends AppCompatActivity {

    private static final String TAG = "CalenderActivity";
    private TextView tvResult;
    private List<String> sessions;
    private List<String> presence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendrer);

        TextView btnScanQr = findViewById(R.id.btn_scan_qr);
        CalendarView calendarView = findViewById(R.id.calendarView);
        RecyclerView recyclerView = findViewById(R.id.recycler_view_sessions);

        // Initialize session data
        sessions = new ArrayList<>();
        presence = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            sessions.add("S" + i);
            presence.add("P");
        }

        // Set up RecyclerView
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        SessionAdapter adapter = new SessionAdapter(sessions, presence ,this);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new TopDividerItemDecoration(this));

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            // Display sessions for the selected day
            // Here sessions are randomized as per your requirement
            adapter.notifyDataSetChanged();
        });

        btnScanQr.setOnClickListener(v -> {
            IntentIntegrator integrator = new IntentIntegrator(this);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
            integrator.setPrompt("Scan a QR Code");
            integrator.setCameraId(0);  // Use a specific camera of the device
            integrator.setBeepEnabled(true);
            integrator.setBarcodeImageEnabled(true);
            integrator.initiateScan();
        });
        // Configuration du bouton retour
        ImageView iconBack = findViewById(R.id.icon_back); // Déplacement ici dans onCreate
        iconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Rediriger vers DashboardActivity
                Intent intent = new Intent(CalenderActivity.this, DashboardActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Log.d(TAG, "Cancelled scan");
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Log.d(TAG, "Scanned");
                tvResult.setText(result.getContents());
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


}
