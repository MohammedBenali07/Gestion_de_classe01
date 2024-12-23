package com.example.gestiondeclasse;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class ProfileActivity extends AppCompatActivity {

    private ImageView icon1, icon2, icon3, logoutIcon;
    private TextView fullName;
    private ImageView imageProfile;  // Corrected to imageProfile

    // Variables for storing user data passed via Intent
    private int userId;
    private String userName, userEmail, userSurname, userBirthday;
    private byte[] userImageProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize views
        icon1 = findViewById(R.id.icon1);
        icon2 = findViewById(R.id.icon2);
        icon3 = findViewById(R.id.icon3);
        fullName = findViewById(R.id.fullname);
        imageProfile = findViewById(R.id.imgae_profile); // Corrected ID to match XML

        // Retrieve user data passed via Intent
        Intent intent = getIntent();
        userId = intent.getIntExtra("userId", -1);
        userName = intent.getStringExtra("userName");
        userSurname = intent.getStringExtra("userSurName");
        userEmail = intent.getStringExtra("userEmail");
        userBirthday = intent.getStringExtra("userBirthday");
        userImageProfile = intent.getByteArrayExtra("userImageProfile");

        // Display full name in the header
        fullName.setText(userName + " " + userSurname);

        // Display profile image if available
        if (userImageProfile != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(userImageProfile, 0, userImageProfile.length);
            imageProfile.setImageBitmap(bitmap);
        }

        // Initialize logout icon
        logoutIcon = findViewById(R.id.ic_logout);

        // Ensure icons are initialized correctly
        if (icon1 == null || icon2 == null || icon3 == null) {
            throw new IllegalStateException("One or more ImageViews are not properly initialized. Check the IDs in activity_profile.xml.");
        }

        // Load the first fragment by default and activate its icon
        loadFragment(new Fragment1());
        activateIcon(R.id.icon1);

        // Handle clicks on the icons
        icon1.setOnClickListener(v -> {
            loadFragment(new Fragment1());
            activateIcon(R.id.icon1);
        });

        icon2.setOnClickListener(v -> {
            loadFragment(new Fragment2());
            activateIcon(R.id.icon2);
        });

        icon3.setOnClickListener(v -> {
            loadFragment(new Fragment3());
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
        Button btnSave = dialog.findViewById(R.id.btn_logOut);
        Button btnCancel = dialog.findViewById(R.id.btn_cancel);

        // Action for the "Log Out" button
        btnSave.setOnClickListener(v -> {
            // Perform log out and redirect to LoginActivity
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Optionally, finish the current activity to prevent returning to this one using the back button
        });

        // Action for the "Cancel" button
        btnCancel.setOnClickListener(v -> dialog.dismiss());

        // Show the dialog
        dialog.show();
    }

    // Method to load a fragment and pass data through the fragment arguments
    private void loadFragment(Fragment fragment) {
        // Create a bundle to send data to the fragment
        Bundle bundle = new Bundle();
        bundle.putString("userEmail", userEmail);
        bundle.putString("userBirthday", userBirthday);

        // Set arguments to the fragment
        fragment.setArguments(bundle);

        // Replace the fragment in the container
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
}
