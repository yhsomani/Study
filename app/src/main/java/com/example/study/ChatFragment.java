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

    private RecyclerView mainUserRecycler;
    private FirebaseAuth auth;
    private UserAdapter userAdapter;
    private FirebaseDatabase database;
    private ArrayList<Users> usersArrayList;

    private String mParam1;
    private String mParam2;

    public ChatFragment() {
        // Required empty public constructor
    }

    public static ChatFragment newInstance(String param1, String param2) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        // Initialize Firebase database and get reference to "user" node
        database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("user"); // Use direct reference to "user" node

        // Initialize the user list
        usersArrayList = new ArrayList<>();

        // Initialize the UserAdapter
        userAdapter = new UserAdapter(getContext(), usersArrayList);

        // Initialize RecyclerView
        mainUserRecycler = view.findViewById(R.id.mainUserRecycler);
        mainUserRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mainUserRecycler.setAdapter(userAdapter);

        // Add ValueEventListener to fetch user data from the database
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Clear the userArrayList before adding new data
                usersArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    // Convert dataSnapshot to Users object and add to the userArrayList
                    Users users = dataSnapshot.getValue(Users.class);
                    usersArrayList.add(users);
                    // Notify the adapter about the data change
                    int position = usersArrayList.indexOf(users);
                    userAdapter.notifyItemInserted(position);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error if needed
            }
        });

        return view;
    }
}
