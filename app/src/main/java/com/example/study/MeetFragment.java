// MeetFragment.java
package com.example.study;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;

public class MeetFragment extends Fragment {

    EditText secretCodeBox;
    Button joinBtn, shareBtn, createBtn;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public MeetFragment() {
        // Required empty public constructor
    }

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
            getArguments().getString(ARG_PARAM1);
            getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meet, container, false);
        secretCodeBox = view.findViewById(R.id.secretCodeBox);
        createBtn = view.findViewById(R.id.createBtn);
        joinBtn = view.findViewById(R.id.joinBtn);
        shareBtn = view.findViewById(R.id.shareBtn);

        // Initialize JitsiMeet SDK and set default conference options
        initializeJitsiMeet();

        joinBtn.setOnClickListener(v -> joinMeeting());
        createBtn.setOnClickListener(v -> createMeeting());
        shareBtn.setOnClickListener(v -> shareMeetingCode());

        return view;
    }

    // Initialize JitsiMeet SDK with default conference options
    private void initializeJitsiMeet() {
        try {
            URL serverURL = new URL("https://meet.jit.si");
            JitsiMeetConferenceOptions defaultOptions = new JitsiMeetConferenceOptions.Builder()
                    .setServerURL(serverURL)
                    .setFeatureFlag("welcomepage.enabled", false)
                    .setFeatureFlag("prejoinpage.enabled", false)
                    .setFeatureFlag("lobby-mode.enabled", false)
                    .setFeatureFlag("ask-to-join.enabled", false)
                    .build();
            JitsiMeet.setDefaultConferenceOptions(defaultOptions);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    // Join a Jitsi Meet meeting using the provided room name
    private void joinMeeting() {
        JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                .setRoom(secretCodeBox.getText().toString())
                .setConfigOverride("requireDisplayName", false)
                .setFeatureFlag("welcomepage.enabled", false)
                .setFeatureFlag("prejoinpage.enabled", false)
                .setFeatureFlag("lobby-mode.enabled", false)
                .setFeatureFlag("ask-to-join.enabled", false)
                .build();

        JitsiMeetActivity.launch(requireContext(), options);
    }

    // Create a new Jitsi Meet meeting using the provided room name
    private void createMeeting() {
        JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                .setRoom(secretCodeBox.getText().toString())
                .build();

        JitsiMeetActivity.launch(requireContext(), options);
    }

    // Share the meeting code through other apps
    private void shareMeetingCode() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        String shareBody = "Required Meeting Code: " + secretCodeBox.getText().toString();
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(intent);
    }
}
