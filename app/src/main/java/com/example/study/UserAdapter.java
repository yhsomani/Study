/*
 * UserAdapter.java
 * Adapter class for managing the RecyclerView in the ChatFragment, displaying user information.
 */

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
    private Context context;

    // ArrayList to hold user data
    private ArrayList<Users> usersArrayList;

    // Constructor to initialize the adapter with context and user data
    public UserAdapter(Context context, ArrayList<Users> usersArrayList) {
        this.context = context;
        this.usersArrayList = usersArrayList;
    }

    // Inflates the user item layout and creates ViewHolder instances
    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.uesr_item, parent, false);
        return new UserAdapter.ViewHolder(view);
    }

    // Binds user data to the ViewHolder
    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {
        Users users = usersArrayList.get(position);
        holder.userName.setText(users.getUserName());
        holder.userStatus.setText(users.getStatus());

        // Load user profile picture using Picasso library
        Picasso.get().load(users.getProfilepic()).into(holder.userProfile);
    }

    // Returns the total number of items in the dataset
    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }

    // ViewHolder class to hold and manage the user item views
    public class ViewHolder extends RecyclerView.ViewHolder {
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
