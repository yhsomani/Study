package com.example.study;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatWindowActivity extends AppCompatActivity {

    private String receiveImg;
    private String receiveName;
    private String receiverStatus;
    private CircleImageView profileImage;
    private TextView profileName;
    private TextView textMessage;

    private TextView statusTextView;
    private ImageButton sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        getSupportActionBar().hide();

        // Initialize views
        profileImage = findViewById(R.id.userProfile);
        profileName = findViewById(R.id.receiverName);
        statusTextView = findViewById(R.id.receiverStatus);
        sendButton = findViewById(R.id.sendButton);
        textMessage = findViewById(R.id.TextMessage);

        // Get data from Intent
        if (getIntent() != null) {
            receiveImg = getIntent().getStringExtra("receiverImage");
            receiveName = getIntent().getStringExtra("name");
            receiverStatus = getIntent().getStringExtra("status");
        }

        // Set received values to views
        if (receiveImg != null) {
            Picasso.get().load(receiveImg).into(profileImage);
        }

        if (receiveName != null) {
            profileName.setText(receiveName);
        }

        if (receiverStatus != null) {
            statusTextView.setText(receiverStatus);
        }

        // Set click listener for the send button
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSendButtonClick();
            }
        });
    }

    private void handleSendButtonClick() {
        String message = textMessage.getText().toString(); // Corrected to get text from TextView

        if (!message.isEmpty()) {
            // Message is not empty, handle it
            Toast.makeText(ChatWindowActivity.this, "Sending: " + message, Toast.LENGTH_SHORT).show();
            textMessage.setText("");
            Date date = new Date();
            MessageModel messageModel = new MessageModel();
        } else {
            // Message is empty, show a toast or handle accordingly
            Toast.makeText(ChatWindowActivity.this, "Please enter a message", Toast.LENGTH_SHORT).show();
        }
    }
}
