package com.example.study;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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

    public static String receiverImage;
    public static String senderImg;
    String receiverImg, senderRoom, receiverRoom, receiverUid, receiverName, receiverStatus, senderUid;
    private CircleImageView profileImage;
    private TextView profileName;
    private TextView statusTextView;
    private ImageButton sendButton;
    private EditText textMessage;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    ArrayList<MessageModel> messagesArrayList;
    private RecyclerView messageRecyclerView;
    private MessagesAdapter messagesAdapter;
    CardView cardViewButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        getSupportActionBar().hide();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        profileImage = findViewById(R.id.userProfile);
        profileName = findViewById(R.id.receiverName);
        statusTextView = findViewById(R.id.receiverStatus);
        sendButton = findViewById(R.id.sendButton);
        textMessage = findViewById(R.id.TextMessage);
        messageRecyclerView = findViewById(R.id.messageRecyclerView);
        cardViewButton = findViewById(R.id.sendCardViewButton);

        // Initialize messagesArrayList
        messagesArrayList = new ArrayList<>();

        if (getIntent() != null) {
            receiverImg = getIntent().getStringExtra("receiverImage");
            receiverName = getIntent().getStringExtra("name");
            receiverStatus = getIntent().getStringExtra("status");
            receiverUid = getIntent().getStringExtra("receiverUid");
        }

        if (receiverImage != null) {
            Picasso.get().load(receiverImg).into(profileImage);
        }

        if (receiverName != null) {
            profileName.setText(receiverName);
        }

        if (receiverStatus != null) {
            statusTextView.setText(receiverStatus);
        }

        DatabaseReference reference = firebaseDatabase.getReference().child("user").child(firebaseAuth.getUid());
        DatabaseReference chatReference;

        if (senderRoom != null && receiverRoom != null) {
            chatReference = firebaseDatabase.getReference().child("chats").child(senderRoom).child("messages");
            chatReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    messagesArrayList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        MessageModel messages = dataSnapshot.getValue(MessageModel.class);
                        messagesArrayList.add(messages);
                    }
                    messagesAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle database error if needed
                }
            });
        } else {
            // Handle the case where senderRoom or receiverRoom is null
            // You might want to show an error message or take appropriate action
        }

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                senderImg = snapshot.child("profilepic").getValue().toString();
                receiverImage = receiverImg;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error if needed
            }
        });

        senderUid = firebaseAuth.getUid();
        senderRoom = senderUid + receiverUid;
        receiverRoom = receiverUid + senderUid;

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        messageRecyclerView.setLayoutManager(layoutManager);

        // Initialize messagesAdapter
        messagesAdapter = new MessagesAdapter(ChatWindowActivity.this, messagesArrayList);
        messageRecyclerView.setAdapter(messagesAdapter);


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSendButtonClick();
            }
        });
    }

    private void handleSendButtonClick() {
        String message = textMessage.getText().toString();
        if (message.isEmpty()) {
            Toast.makeText(ChatWindowActivity.this, "Enter The Message First", Toast.LENGTH_SHORT).show();
            return;
        }
        textMessage.setText("");
        Date date = new Date();
        MessageModel messages = new MessageModel(message, senderUid, date.getTime());

        if (senderRoom != null && receiverRoom != null) {
            firebaseDatabase.getReference().child("chats")
                    .child(senderRoom)
                    .child("messages")
                    .push().setValue(messages)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            firebaseDatabase.getReference().child("chats")
                                    .child(receiverRoom)
                                    .child("messages")
                                    .push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            // Handle completion if needed
                                        }
                                    });
                        }
                    });
        } else {
            // Handle the case where senderRoom or receiverRoom is null
            // You might want to show an error message or take appropriate action
        }
    }
}
