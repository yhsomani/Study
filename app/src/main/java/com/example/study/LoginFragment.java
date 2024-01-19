// LoginFragment.java
package com.example.study;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFragment extends Fragment {

    // UI components
    Button loginBtn;
    ImageButton visibilityBtn;
    Boolean isPasswordVisible = true;
    EditText emailEditText, passwordEditText;

    // Firebase Authentication
    FirebaseAuth auth;

    // Progress dialog for login process
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        // Initialize UI components and Firebase Authentication
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        loginBtn = view.findViewById(R.id.login_btn);
        emailEditText = view.findViewById(R.id.login_email);
        visibilityBtn = view.findViewById(R.id.imageButton);
        passwordEditText = view.findViewById(R.id.login_password);
        auth = FirebaseAuth.getInstance();

        // Set click listeners
        visibilityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePasswordVisibility();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

        return view;
    }

    // Toggle password visibility
    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            visibilityBtn.setImageResource(R.drawable.ic_invisibility);
            passwordEditText.setHint("Password");
        } else {
            passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            visibilityBtn.setImageResource(R.drawable.ic_visibility);
            passwordEditText.setHint("********");
        }
        isPasswordVisible = !isPasswordVisible;
    }

    // Attempt to log in the user
    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Validate email and password
        if (TextUtils.isEmpty(email)) {
            showError("Email is required");
            return;
        } else if (!email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
            showError("Invalid Email");
            return;
        } else if (TextUtils.isEmpty(password)) {
            showError("Password is required");
            return;
        } else if (password.length() <= 6) {
            showError("Weak Password");
            return;
        }

        showProgressDialog("Logging in...");

        // Sign in with Firebase Authentication
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                hideProgressDialog();

                if (task.isSuccessful()) {
                    try {
                        // If login is successful, start the main activity
                        startActivity(new Intent(getActivity(), MainActivity.class));
                        // Finish the current activity to prevent the user from coming back to the login screen
                        getActivity().finish();
                    } catch (Exception e) {
                        showError(e.getMessage());
                    }
                } else {
                    // If login fails, show error message
                    showError(task.getException().getMessage());
                }
            }
        });
    }

    // Show progress dialog with a given message
    private void showProgressDialog(String message) {
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    // Hide the progress dialog
    private void hideProgressDialog() {
        progressDialog.dismiss();
    }

    // Show an error message using Toast
    private void showError(String message) {
        hideProgressDialog();
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
