// UserAdapter.java
package com.example.study;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    // Context to be used in the adapter
    private final Context context;

    // ArrayList to hold user data
    private final ArrayList<Users> usersArrayList;

    // Default profile image URL
    private static final String DEFAULT_PROFILE_IMAGE_URL = "https://firebasestorage.googleapis.com/v0/b/study-acbc9.appspot.com/o/man-user-color-icon.png?alt=media&token=4604a5d3-0554-4b11-bcc3-c28499e298f8";

    // Constructor to initialize the adapter with context and user data
    public UserAdapter(Context context, ArrayList<Users> usersArrayList) {
        this.context = context;
        this.usersArrayList = usersArrayList;
    }

    // Inflates the user item layout and creates ViewHolder instances
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the user item layout
        View view = LayoutInflater.from(context).inflate(R.layout.uesr_item, parent, false);
        return new ViewHolder(view);
    }

    // Binds user data to the ViewHolder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the user at the current position
        Users user = usersArrayList.get(position);

        // Check if the user has a profile image URL
        if (user.getProfilepic() != null && !user.getProfilepic().isEmpty()) {
            // If yes, load the profile image using Picasso
            Picasso.get().load(user.getProfilepic()).into(holder.userProfile);
        } else {
            // If no, load the default image
            Picasso.get().load(DEFAULT_PROFILE_IMAGE_URL).into(holder.userProfile);
        }

        // Set user name and status in the ViewHolder
        holder.userName.setText(user.getUserName());
        holder.userStatus.setText(user.getStatus());
    }

    // Returns the total number of items in the dataset
    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }

    // ViewHolder class to hold and manage the user item views
    public static class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView userProfile;
        TextView userName;
        TextView userStatus;

        // Constructor to initialize the views in the ViewHolder
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userProfile = itemView.findViewById(R.id.userProfile);
            userName = itemView.findViewById(R.id.userName);
            userStatus = itemView.findViewById(R.id.userStatus);
        }
    }
}
