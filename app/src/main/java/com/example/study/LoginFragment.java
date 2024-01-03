package com.example.study;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    Button login_btn;
    ImageButton visibility;
    Boolean icon = true;
    EditText et_email, et_password;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    FirebaseAuth auth;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);


        login_btn =view.findViewById(R.id.login_btn);
        et_email = view.findViewById(R.id.login_email);
        visibility = view.findViewById(R.id.imageButton);
        et_password = view.findViewById(R.id.login_password);


        auth = FirebaseAuth.getInstance();

        visibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = et_email.getText().toString().trim();
                String password = et_password.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    et_email.setError("Email is required");
                    return;
                } else if (!email.matches(emailPattern)) {
                    et_email.setError("Invalid Email");
                    return;
                } else if(TextUtils.isEmpty(password)) {
                    et_password.setError("Password is required");
                    return;
                } else if (password.length()<=6) {
                    et_password.setError("Weak Password");
                } else {
                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                try {

                                    startActivity(new Intent(getActivity(), MainActivity.class));
                                    // Finish the current activity to prevent the user from coming back to the login screen
                                    getActivity().finish();
                                } catch (Exception e) {
                                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        return view;
    }
}