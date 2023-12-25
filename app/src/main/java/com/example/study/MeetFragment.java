package com.example.study;

import static androidx.core.content.ContextCompat.getSystemService;

import static org.webrtc.ContextUtils.getApplicationContext;

import android.content.Context;
import android.content.Intent;
import android.content.RestrictionEntry;
import android.content.RestrictionsManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import org.jitsi.meet.sdk.BroadcastEvent;
import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;


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


//    @SuppressLint("LongLogTag")
//    private void onBroadcastReceived(Intent intent) {
//        if (intent != null) {
//            BroadcastEvent event = new BroadcastEvent(intent);
//
//            switch (event.getType()) {
//                case CONFERENCE_JOINED:
//                    Log.d("Conference Joined with url%s", (String) event.getData().get("url"));
//
//                    break;
//                case PARTICIPANT_JOINED:
//                    Log.d("Participant joined%s", (String) event.getData().get("name"));
//                    break;
//            }
//        }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meet, container, false);
        secretCodeBox = view.findViewById(R.id.secretCodeBox);
        createBtn = view.findViewById(R.id.createBtn);
        joinBtn = view.findViewById(R.id.joinBtn);
        shareBtn = view.findViewById(R.id.shareBtn);

        URL serverURL;
        try {
            serverURL = new URL("https://meet.jit.si");
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
        joinBtn.setOnClickListener(v -> {
            JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                    .setRoom(secretCodeBox.getText().toString())
                    .setConfigOverride("requireDisplayName", false)
                    .setFeatureFlag("welcomepage.enabled", false)
                    .setFeatureFlag("prejoinpage.enabled", false)
                    .setFeatureFlag("lobby-mode.enabled", false)
                    .setFeatureFlag("ask-to-join.enabled", false)
                    .build();

            JitsiMeetActivity.launch(requireContext(), options);
            System.out.println("#################Feature flags" + options.getFeatureFlags());
        });

        createBtn.setOnClickListener(v -> {
            JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                    .setRoom(secretCodeBox.getText().toString())
                    .build();

            JitsiMeetActivity.launch(requireContext(), options);

        });

        shareBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            String shareBody = "Required Meeting Code: " + secretCodeBox.getText().toString();
            intent.setType("text/plain");
//                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Study");
            intent.putExtra(Intent.EXTRA_TEXT, shareBody);
            startActivity(intent);
        });

        // Inflate the layout for this fragment
        return view;
    }

}


//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_meet, container, false);
//        secretCodeBox = view.findViewById(R.id.secretCodeBox);
//        createBtn = view.findViewById(R.id.createBtn);
//        joinBtn = view.findViewById(R.id.joinBtn);
//        shareBtn = view.findViewById(R.id.shareBtn);
//
//        initializeJitsiMeet();
//
//        joinBtn.setOnClickListener(v -> joinMeeting(secretCodeBox.getText().toString()));
//        createBtn.setOnClickListener(v -> createMeeting(secretCodeBox.getText().toString()));
//        shareBtn.setOnClickListener(v -> shareMeetingCode());
//
//        return view;
//    }
//
//    private void initializeJitsiMeet() {
//        try {
//            URL serverURL = new URL("https://meet.jit.si");
//            JitsiMeetConferenceOptions defaultOptions = new JitsiMeetConferenceOptions.Builder()
//                    .setServerURL(serverURL)
//                    .build();
//            JitsiMeet.setDefaultConferenceOptions(defaultOptions);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void joinMeeting(String roomName) {
//        JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
//                .setRoom(roomName)
//                .setConfigOverride("requireDisplayName", false)
//                .build();
//
//        JitsiMeetActivity.launch(requireContext(), options);
//    }
//
//    private void createMeeting(String roomName) {
//        JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
//                .setRoom(roomName)
//                .build();
//
//        JitsiMeetActivity.launch(requireContext(), options);
//    }
//
//    private void shareMeetingCode() {
//        Intent intent = new Intent(Intent.ACTION_SEND);
//        String shareBody = "Required Meeting Code: " + secretCodeBox.getText().toString();
//        intent.setType("text/plain");
//        intent.putExtra(Intent.EXTRA_TEXT, shareBody);
//        startActivity(intent);
//    }
//}


