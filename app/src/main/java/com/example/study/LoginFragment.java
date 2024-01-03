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

    Button login_btn;
    ImageButton visibility;
    Boolean icon = true;
    EditText et_email, et_password;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    FirebaseAuth auth;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);

        login_btn = view.findViewById(R.id.login_btn);
        et_email = view.findViewById(R.id.login_email);
        visibility = view.findViewById(R.id.imageButton);
        et_password = view.findViewById(R.id.login_password);

        auth = FirebaseAuth.getInstance();

        visibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePasswordVisibility();
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

        return view;
    }

    private void togglePasswordVisibility() {
        if (icon) {
            et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            visibility.setImageResource(R.drawable.ic_invisibility);
            et_password.setHint("Password");
            icon = false;
        } else {
            et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            visibility.setImageResource(R.drawable.ic_visibility);
            et_password.setHint("********");
            icon = true;
        }
    }

    private void loginUser() {
        String email = et_email.getText().toString().trim();
        String password = et_password.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            showError("Email is required");
            return;
        } else if (!email.matches(emailPattern)) {
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

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                hideProgressDialog();

                if (task.isSuccessful()) {
                    try {
                        startActivity(new Intent(getActivity(), MainActivity.class));
                        // Finish the current activity to prevent the user from coming back to the login screen
                        getActivity().finish();
                    } catch (Exception e) {
                        showError(e.getMessage());
                    }
                } else {
                    showError(task.getException().getMessage());
                }
            }
        });
    }

    private void showProgressDialog(String message) {
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    private void hideProgressDialog() {
        progressDialog.dismiss();
    }

    private void showError(String message) {
        hideProgressDialog();
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
