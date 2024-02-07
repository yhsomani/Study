package com.example.study;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatWindowActivity extends AppCompatActivity {

    private String receiverImg, receiverUid, receiverName, senderUid, receiverStatus;
    private CircleImageView profile;
    private TextView receiverNameTextView, receiverStatusTextView;
    private FirebaseDatabase database;
    private FirebaseAuth firebaseAuth;
    private ImageButton sendButton;
    private EditText textMessage;
    private RecyclerView messageRecyclerView;
    private ArrayList<MessageModel> messagesArrayList;
    private MessagesAdapter messagesAdapter;

    private String senderRoom, receiverRoom;

    private static String senderImg;
    private static String receiverImgStatic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        getSupportActionBar().hide();

        initializeViews();
        setupRecyclerView();

        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        retrieveIntentData();

        loadChatMessages();

        configureSendButton();
    }

    private void initializeViews() {
        receiverStatusTextView = findViewById(R.id.receiverStatus);
        sendButton = findViewById(R.id.sendButton);
        textMessage = findViewById(R.id.textMessage);
        receiverNameTextView = findViewById(R.id.receiverName);
        profile = findViewById(R.id.userImage);
        messageRecyclerView = findViewById(R.id.messageRecyclerView);
    }

    private void setupRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        messageRecyclerView.setLayoutManager(linearLayoutManager);
        messagesArrayList = new ArrayList<>();
        messagesAdapter = new MessagesAdapter(ChatWindowActivity.this, messagesArrayList);
        messageRecyclerView.setAdapter(messagesAdapter);
    }

    private void retrieveIntentData() {
        receiverName = getIntent().getStringExtra("recipientName");
        receiverImg = getIntent().getStringExtra("recipientImage");
        receiverUid = getIntent().getStringExtra("recipientId");
        receiverStatus = getIntent().getStringExtra("recipientStatus");

        Picasso.get().load(receiverImg).into(profile);
        receiverNameTextView.setText(receiverName);
        receiverStatusTextView.setText(receiverStatus);

        senderUid = firebaseAuth.getUid();
        senderRoom = senderUid + receiverUid;
        receiverRoom = receiverUid + senderUid;

        // Set values for static variables
        senderImg = "URL_TO_YOUR_SENDER_IMAGE"; // Replace with the actual URL
        receiverImgStatic = "URL_TO_YOUR_RECEIVER_IMAGE"; // Replace with the actual URL
    }

    public static String getSenderImg() {
        return senderImg;
    }

    public static String getReceiverImgStatic() {
        return receiverImgStatic;
    }

    private void loadChatMessages() {
        DatabaseReference chatReference = database.getReference().child("chats").child(senderRoom).child("messages");
        chatReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    MessageModel message = dataSnapshot.getValue(MessageModel.class);
                    messagesArrayList.add(message);
                }
                messagesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ChatWindowActivity.this, "Database Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void configureSendButton() {
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
    }

    private void sendMessage() {
        String messageText = textMessage.getText().toString().trim();
        if (!messageText.isEmpty()) {
            Date date = new Date();
            MessageModel newMessage = new MessageModel(messageText, senderUid, date.getTime());

            saveMessage(senderRoom, newMessage);
            saveMessage(receiverRoom, newMessage);

            // Clear the input field after sending the message
            textMessage.setText("");
        } else {
            Toast.makeText(ChatWindowActivity.this, "Enter The Message First", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveMessage(String room, MessageModel message) {
        database.getReference().child("chats").child(room).child("messages").push().setValue(message)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(ChatWindowActivity.this, "Failed to send message", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
