package com.example.gestiondeclasse;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class ProfileActivity extends AppCompatActivity {

    private ImageView icon1, icon2, icon3, logoutIcon, imageProfile;
    private TextView fullName;
    private  TextView emailText,birthdayText;

    // SharedPreferences variables
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "UserSession";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize views
        icon1 = findViewById(R.id.icon1);
        icon2 = findViewById(R.id.icon2);
        icon3 = findViewById(R.id.icon3);
        fullName = findViewById(R.id.fullname);
        imageProfile = findViewById(R.id.imgae_profile);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // Get data from SharedPreferences
        String userName = sharedPreferences.getString("userName", "");
        String userSurname = sharedPreferences.getString("userSurName", "");
        String email = sharedPreferences.getString("userEmail", "");
        String birthday = sharedPreferences.getString("userBirthday", "");
        String userImageProfile = sharedPreferences.getString("userProfileImagePath", null);
        String address=sharedPreferences.getString("userAddress", "");

        // Display full name
        fullName.setText(userName + " " + userSurname);


        // Afficher l'image de profil si disponible
        if (userImageProfile != null && !userImageProfile.isEmpty()) {
            try {
                // Charger l'image depuis le fichier local
                File imgFile = new  File(userImageProfile);
                if(imgFile.exists()){
                    Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    imageProfile.setImageBitmap(bitmap);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Erreur de chargement de l'image", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Afficher une image par défaut si l'image de profil n'est pas disponible
            imageProfile.setImageResource(R.drawable.mon_image);
            Toast.makeText(getApplicationContext(), "Aucune image de profil disponible", Toast.LENGTH_SHORT).show();
        }


        // Initialize logout icon
        logoutIcon = findViewById(R.id.ic_logout);

        // Ensure icons are initialized correctly
        if (icon1 == null || icon2 == null || icon3 == null) {
            throw new IllegalStateException("One or more ImageViews are not properly initialized. Check the IDs in activity_profile.xml.");
        }

        // Load the first fragment by default and transmit data
        Fragment1 fragment1 = new Fragment1();
        Bundle bundle = new Bundle();
        bundle.putString("email", email);
        bundle.putString("birthday", birthday);
        bundle.putString("Address",address);
        fragment1.setArguments(bundle);
        loadFragment(fragment1);
        activateIcon(R.id.icon1);

        // Handle clicks on the icons
        icon1.setOnClickListener(v -> {
            Fragment1 fragment = new Fragment1();
            fragment.setArguments(bundle); // Transmit the same data
            loadFragment(fragment);
            activateIcon(R.id.icon1);
        });

        icon2.setOnClickListener(v -> {
            Fragment2 fragment = new Fragment2();
            loadFragment(fragment);
            activateIcon(R.id.icon2);
        });

        icon3.setOnClickListener(v -> {
            Fragment3 fragment = new Fragment3();
            loadFragment(fragment);
            activateIcon(R.id.icon3);
        });

        // Handle logout icon click
        logoutIcon.setOnClickListener(v -> showCustomLogoutDialog());

        // Configure back button
        ImageView iconBack = findViewById(R.id.icon_back);
        iconBack.setOnClickListener(v -> {
            // Redirect to DashboardActivity
            Intent dashboardIntent = new Intent(ProfileActivity.this, DashboardActivity.class);
            startActivity(dashboardIntent);
        });
    }

    private void showCustomLogoutDialog() {
        // Create a Dialog object
        Dialog dialog = new Dialog(this);

        // Apply the dialog layout that uses CardView with rounded corners
        dialog.setContentView(R.layout.dialog_logout);

        // Apply custom background for the dialog
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.background_dialog);

        // Retrieve buttons from the dialog layout
        Button btnLogOut = dialog.findViewById(R.id.btn_logOut);
        Button btnCancel = dialog.findViewById(R.id.btn_cancel);

        // Action for the "Log Out" button
        btnLogOut.setOnClickListener(v -> {
            // Perform log out and clear shared preferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

            // Redirect to LoginActivity
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        // Action for the "Cancel" button
        btnCancel.setOnClickListener(v -> dialog.dismiss());

        // Show the dialog
        dialog.show();
    }

    // Method to load a fragment
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    // Method to activate the selected icon
    private void activateIcon(int activeIconId) {
        // Reset all icons to their normal state
        resetIcons();

        // Activate the selected icon
        if (activeIconId == R.id.icon1) {
            icon1.setImageResource(R.drawable.ic_member_card_active);
        } else if (activeIconId == R.id.icon2) {
            icon2.setImageResource(R.drawable.ic_folder_active);
        } else if (activeIconId == R.id.icon3) {
            icon3.setImageResource(R.drawable.ic_security_active);
        }
    }

    // Method to reset all icons to their default state
    private void resetIcons() {
        icon1.setImageResource(R.drawable.ic_member_card);
        icon2.setImageResource(R.drawable.ic_folder);
        icon3.setImageResource(R.drawable.ic_security);
    }

    // Helper method to encode image to String
    public static String encodeImage(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    // Helper method to decode image from String
    public static Bitmap decodeImage(String encodedImage) {
        byte[] decodedByte = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}
