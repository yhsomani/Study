package com.example.study;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MeetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MeetFragment extends Fragment {
    EditText secretCodeBox;
    Button joinBtn, shareBtn, createBtn;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MeetFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MeetFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MeetFragment newInstance(String param1, String param2) {
        MeetFragment fragment = new MeetFragment();
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
        View view = inflater.inflate(R.layout.fragment_meet, container, false);
        secretCodeBox =view.findViewById(R.id.secretCodeBox);
        createBtn = view.findViewById(R.id.createBtn);
        joinBtn = view.findViewById(R.id.joinBtn);
        shareBtn = view.findViewById(R.id.shareBtn);

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                String shareBody = "Required Meeting Code: "+ secretCodeBox.getText().toString();
                intent.setType("text/plain");
//                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Study");
                intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(intent);
            }
        });
        // Inflate the layout for this fragment
        return view;
    }
}