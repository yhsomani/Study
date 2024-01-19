package com.example.study;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterFragment extends Fragment {

    // UI components
    private ImageButton visibilityButton;
    private EditText nameEditText, emailEditText, passwordEditText;
    private CircleImageView profileImageView;
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    ProgressDialog progressDialog;

    // Email pattern for validation
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    // User selected image URI
    private Uri imageURI;

    // Firebase components
    FirebaseDatabase database;
    FirebaseStorage storage;

    // URL for the uploaded image
    String imageUri;

    // Flag for password visibility
    private boolean isPasswordVisible = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        // Initialize progress dialog
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);

        // Initialize Firebase components
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();

        // Initialize UI components
        Button registerButton = view.findViewById(R.id.register_btn);
        visibilityButton = view.findViewById(R.id.visible_btn);
        nameEditText = view.findViewById(R.id.register_name);
        emailEditText = view.findViewById(R.id.register_email);
        passwordEditText = view.findViewById(R.id.register_password);
        profileImageView = view.findViewById(R.id.userProfile);
        ImageView selectProfileImageView = view.findViewById(R.id.imageView6);

        // Set click listeners
        visibilityButton.setOnClickListener(v -> togglePasswordVisibility());
        profileImageView.setOnClickListener(v -> openImagePicker());
        selectProfileImageView.setOnClickListener(v -> openImagePicker());
        registerButton.setOnClickListener(v -> registerUser());

        return view;
    }

    // Task 1: Toggle password visibility
    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            visibilityButton.setImageResource(R.drawable.ic_invisibility);
            passwordEditText.setHint("Password");
        } else {
            passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            visibilityButton.setImageResource(R.drawable.ic_visibility);
            passwordEditText.setHint("********");
        }
        isPasswordVisible = !isPasswordVisible;
    }

    // Task 2: Open image picker
    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 10);
    }

    // Task 3: Register user with or without an image
    private void registerUser() {
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String status = "Hey, I'm using this application";

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || !email.matches(emailPattern) || TextUtils.isEmpty(password) || password.length() < 6) {
            Toast.makeText(getActivity(), "Invalid input. Please check your entries.", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Registering User...");
        progressDialog.show();

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            progressDialog.dismiss();
            if (task.isSuccessful()) {
                String id = Objects.requireNonNull(task.getResult().getUser()).getUid();
                DatabaseReference reference = database.getReference().child("user").child(id);

                if (imageURI != null) {
                    // Task 4: Upload selected image and register user
                    uploadImageAndRegisterUser(id, reference, status);
                } else {
                    // Task 5: Upload default image and register user
                    uploadDefaultImageAndRegisterUser(id, reference, status);
                }
            } else {
                Toast.makeText(getActivity(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Task 6: Upload default image and register user
    private void uploadDefaultImageAndRegisterUser(String id, DatabaseReference reference, String status) {
        int defaultImageResourceId = R.drawable.user_profile; // Replace with your drawable resource ID
        Picasso.get().load(defaultImageResourceId).into(profileImageView);
        Users users = new Users(id, nameEditText.getText().toString().trim(), emailEditText.getText().toString().trim(), passwordEditText.getText().toString().trim(), null, status);
        reference.setValue(users).addOnCompleteListener(task -> handleRegistrationResult(task));
    }

    // Task 7: Upload selected image and register user
    private void uploadImageAndRegisterUser(String id, DatabaseReference reference, String status) {
        StorageReference storageReference = storage.getReference().child("upload").child(id);
        progressDialog.setMessage("Uploading Image...");
        progressDialog.show();

        storageReference.putFile(imageURI).addOnCompleteListener(task1 -> {
            if (task1.isSuccessful()) {
                progressDialog.setMessage("Getting Download URL...");
                progressDialog.show();

                storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    imageUri = uri.toString();
                    Users users = new Users(id, nameEditText.getText().toString().trim(), emailEditText.getText().toString().trim(), passwordEditText.getText().toString().trim(), imageUri, status);
                    reference.setValue(users).addOnCompleteListener(task2 -> handleRegistrationResult(task2));
                });
            }
        });
    }

    // Task 8: Handle registration result
    private void handleRegistrationResult(@Nullable @org.jetbrains.annotations.Nullable Task<Void> task) {
        progressDialog.dismiss();

        if (task != null && task.isSuccessful()) {
            Toast.makeText(getContext(), "Registration Successful", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getActivity(), MainActivity.class));
            requireActivity().finish();
        } else {
            Toast.makeText(getContext(), "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 10 && data != null) {
            // Task 9: Load selected image into CircleImageView
            imageURI = data.getData();
            Picasso.get().load(imageURI).into(profileImageView);
        }
    }
}
