package com.example.study;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // UI components
    private Button updateProfileBtn;
    private TextView textViewName;
    private TextView textViewStudentId;
    private TextView textViewEmail;
    private CircleImageView profileImg;
    private ProgressDialog progressDialog;
    private DatabaseReference userReference;
    private FirebaseUser currentUser;
    private Button logoutBtn;

    // Default constructor
    public AccountFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance(String param1, String param2) {
        Fragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        // Initialize views and Firebase components
        initializeViews(view);

        // Set click listener for the update profile button
        updateProfileBtn.setOnClickListener(v -> updateProfile());

        // Load user data
        loadUserData();

        return view;  // Make sure to return the inflated view
    }

    // Initialize views and Firebase components
    private void initializeViews(View view) {
        updateProfileBtn = view.findViewById(R.id.updateProfileBtn);
        textViewName = view.findViewById(R.id.textView11);
        textViewStudentId = view.findViewById(R.id.textView13);
        textViewEmail = view.findViewById(R.id.textView15);
        profileImg = view.findViewById(R.id.profileImg);
        logoutBtn = view.findViewById(R.id.logoutbtn);

        logoutBtn.setOnClickListener(v -> showLogoutDialog());

        // Firebase
        FirebaseAuth auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        if (currentUser != null) {
            userReference = database.getReference("user").child(currentUser.getUid());
        }
    }

    // Load user data from Firebase
    private void loadUserData() {
        if (currentUser != null) {
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Loading User Data...");
            progressDialog.show();

            userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    progressDialog.dismiss(); // Dismiss the progress dialog once data is fetched

                    if (snapshot.exists()) {
                        Users user = snapshot.getValue(Users.class);
                        if (user != null) {
                            // Load data into views
                            textViewName.setText("Name: " + user.getUserName());
                            textViewStudentId.setText("Student ID: " + user.getUserId());
                            textViewEmail.setText("Email: " + user.getMail());

                            // Load profile image using Glide
                            loadProfileImage(user.getProfilepic());
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    progressDialog.dismiss(); // Dismiss the progress dialog in case of error
                    Toast.makeText(getContext(), "Error loading user data", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // Load profile image using Glide
    private void loadProfileImage(String profileImageUrl) {
        if (getContext() != null && profileImg != null) {
            if (profileImageUrl == null || profileImageUrl.isEmpty()) {
                // Use default profile image if the user's profile image is not available
                profileImg.setImageResource(R.drawable.user_profile);
            } else {
                // Load non-default image using Glide
                Glide.with(getContext())
                        .load(profileImageUrl)
                        .into(profileImg);
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

    // Show logout confirmation dialog
    private void showLogoutDialog() {
        Dialog dialog = new Dialog(requireActivity(), R.style.dialoge);
        dialog.setContentView(R.layout.dialog_layout);
        Button no = dialog.findViewById(R.id.nobtn);
        Button yes = dialog.findViewById(R.id.yesbtn);

        yes.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            dialog.dismiss();
        });

        no.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
}
