// ChatFragment.java
package com.example.study;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    RecyclerView mainUserRecycler;
    FirebaseAuth auth;
    UserAdapter userAdapter;
    FirebaseDatabase database;
    ArrayList<Users> usersArrayList;

    private String mParam1;
    private String mParam2;

    public ChatFragment() {
        // Required empty public constructor
    }

    public static ChatFragment newInstance(String param1, String param2) {
        // Task 1: Create a new instance of the ChatFragment with parameters
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Task 2: Check if there are any arguments passed to the fragment
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Task 3: Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        // Task 4: Initialize Firebase database and get reference to "user" node
        database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference().child("user");
        usersArrayList = new ArrayList<>();

        // Task 5: Add ValueEventListener to fetch user data from the database
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Task 6: Clear the userArrayList before adding new data
                usersArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    // Task 7: Convert dataSnapshot to Users object and add to the userArrayList
                    Users users = dataSnapshot.getValue(Users.class);
                    usersArrayList.add(users);
                }
                // Task 8: Notify the adapter about the data change
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Task 9: Handle database error if needed
            }
        });

        // Task 10: Initialize FirebaseAuth and RecyclerView
        auth = FirebaseAuth.getInstance();
        mainUserRecycler = view.findViewById(R.id.mainUserRecycler);
        mainUserRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        // Task 11: Create UserAdapter and set it to the RecyclerView
        userAdapter = new UserAdapter(getContext(), usersArrayList);
        mainUserRecycler.setAdapter(userAdapter);

        return view;
    }
}
