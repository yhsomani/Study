package com.example.study;


import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_SEND = 1;
    private static final int ITEM_RECEIVE = 2;

    private final Context context;
    private final ArrayList<MessageModel> messagesList;

    // Constructor to initialize the adapter
    public MessagesAdapter(Context context, ArrayList<MessageModel> messagesList) {
        this.context = context;
        this.messagesList = messagesList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the appropriate layout based on the message sender or receiver
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);

        if (viewType == ITEM_SEND) {
            // Inflate sender layout
            view = inflater.inflate(R.layout.sender_layout, parent, false);
            return new SenderViewHolder(view);
        } else {
            // Inflate receiver layout
            view = inflater.inflate(R.layout.receiver_layout, parent, false);
            return new ReceiverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // Get the message at the current position
        MessageModel message = messagesList.get(position);

        // Handle long click for message deletion
        holder.itemView.setOnLongClickListener(v -> {
            showDeleteDialog(position);
            return false;
        });

        // Bind message data to the appropriate ViewHolder
        if (holder.getItemViewType() == ITEM_SEND) {
            // If the message is sent, bind it to the SenderViewHolder
            bindMessage(holder, message);
        } else {
            // If the message is received, bind it to the ReceiverViewHolder
            bindMessage(holder, message);
        }
    }

    @Override
    public int getItemCount() {
        // Return the total number of messages in the list
        return messagesList.size();
    }

    @Override
    public int getItemViewType(int position) {
        // Determine the type of message (sender or receiver)
        MessageModel message = messagesList.get(position);
        return Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid().equals(message.getSenderId()) ? ITEM_SEND : ITEM_RECEIVE;
    }

    // Bind message data to the SenderViewHolder or ReceiverViewHolder
    private void bindMessage(RecyclerView.ViewHolder holder, MessageModel message) {
        CircleImageView circleImageView = holder.itemView.findViewById(holder instanceof SenderViewHolder ? R.id.profilerggg : R.id.pro);
        TextView messageTextView = holder.itemView.findViewById(holder instanceof SenderViewHolder ? R.id.msgsendertyp : R.id.recivertextset);

        // Set message text and sender/receiver image using Picasso
        messageTextView.setText(message.getMessage());

        // Replace with your actual image URLs
        String imageUrl = (holder instanceof SenderViewHolder) ? ChatWindowActivity.getSenderImg() : ChatWindowActivity.getReceiverImgStatic();
        Picasso.get().load(imageUrl).into(circleImageView);
    }

    // Show a confirmation dialog for message deletion
    private void showDeleteDialog(int position) {
        new AlertDialog.Builder(context)
                .setTitle("Delete")
                .setMessage("Are you sure you want to delete this message?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Implement message deletion logic
                    deleteMessage(position);
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }

    // Delete the message from the list and database
    private void deleteMessage(int position) {
        // Retrieve the message model to obtain messageId
        MessageModel deletedMessage = messagesList.get(position);
        String messageId = deletedMessage.getMessageId(); // Assuming there's a messageId in your MessageModel

        // Remove the message from the list
        messagesList.remove(position);

        // Notify the adapter that an item has been removed
        notifyItemRemoved(position);

        // Implement additional logic if needed, such as deleting the message from the database
        deleteMessageFromFirebase(messageId);
    }

    // Example method to delete the message from Firebase
    private void deleteMessageFromFirebase(String messageId) {
        // Implement the logic to delete the message from your Firebase database
        // This might involve accessing the reference to the specific message node and removing it
        // Replace this example with your actual Firebase database logic

        // Assuming "messages" is the node containing your messages in Firebase
        DatabaseReference messagesRef = FirebaseDatabase.getInstance().getReference("messages");

        // Remove the message node with the given messageId
        messagesRef.child(messageId).removeValue()
                .addOnSuccessListener(aVoid -> {
                    // Deletion successful
                    // Log.d("DeleteMessage", "Message deleted from Firebase: " + messageId);
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                    // Log.e("DeleteMessage", "Error deleting message from Firebase: " + e.getMessage());
                });
    }

    // ViewHolder for sender messages
    static class SenderViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView messageTextView;

        // Constructor to initialize SenderViewHolder
        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.profilerggg);
            messageTextView = itemView.findViewById(R.id.msgsendertyp);
        }
    }

    // ViewHolder for receiver messages
    static class ReceiverViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView messageTextView;

        // Constructor to initialize ReceiverViewHolder
        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.pro);
            messageTextView = itemView.findViewById(R.id.recivertextset);
        }
    }
}
