package com.example.study;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountFragment extends Fragment {

    // UI components
    private Button updateProfileBtn;
    private TextView textViewName, textViewStudentId, textViewEmail;
    private CircleImageView profileImg;

    // Firebase
    private FirebaseAuth auth;
    private DatabaseReference userReference;
    private FirebaseUser currentUser;

    // Progress dialog to show loading or updating status
    private ProgressDialog progressDialog;

    // Default constructor
    public AccountFragment() {
        // Required empty public constructor
    }

    // onCreate method...

    // onCreateView method...

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views and Firebase components
        initializeViews(view);

        // Set click listener for the update profile button
        updateProfileBtn.setOnClickListener(v -> updateProfile());

        // Load user data
        loadUserData();
    }

    // Initialize views and Firebase components
    private void initializeViews(View view) {
        updateProfileBtn = view.findViewById(R.id.updateProfileBtn);
        textViewName = view.findViewById(R.id.textView11);
        textViewStudentId = view.findViewById(R.id.textView13);
        textViewEmail = view.findViewById(R.id.textView15);
        profileImg = view.findViewById(R.id.profileImg);

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        if (currentUser != null) {
            userReference = database.getReference("user").child(currentUser.getUid());
        }
    }

    // Load user data from Firebase
    private void loadUserData() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading User Data...");
        progressDialog.show();

        if (currentUser != null) {
            userReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Users user = snapshot.getValue(Users.class);
                        if (user != null) {
                            // Load data into views
                            textViewName.setText("Name: " + user.getUserName());
                            textViewStudentId.setText("Student ID: " + user.getUserId());
                            textViewEmail.setText("Email: " + user.getMail());

                            // Load profile image using Glide
                            loadProfileImage(user.getProfilepic());

                            progressDialog.dismiss(); // Dismiss the progress dialog
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    progressDialog.dismiss(); // Dismiss the progress dialog
                    Toast.makeText(getContext(), "Error loading user data", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // Load profile image using Glide
    private void loadProfileImage(String profileImageUrl) {
        if (getContext() != null) {
            if (profileImg != null) {
                if (profileImageUrl == null || profileImageUrl.isEmpty()) {
                    // Use default profile image if user's profile image is not available
                    profileImg.setImageResource(R.drawable.user_profile);
                } else {
                    // Load non-default image using Glide
                    Glide.with(getContext())
                            .load(profileImageUrl)
                            .into(profileImg);
                }
            }
        }
    }

    // Update profile logic (not fully implemented in this example)
    private void updateProfile() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Updating Profile...");
        progressDialog.show();

        // Get values from EditText fields
        // String newName = editName.getText().toString().trim();
        // String newStudentId = editStudentId.getText().toString().trim();

        // Your existing updateProfile() logic here...

        // Dismiss the progress dialog after the update is complete
        progressDialog.dismiss();
        Toast.makeText(getContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
    }
}
