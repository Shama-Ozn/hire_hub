package com.example.hirehub;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    private Context context;
    private List<Post> posts;
    private PostClickListener listener;

    // Constructor with PostClickListener
    public PostsAdapter(Context context, List<Post> posts, PostClickListener listener) {
        this.context = context;
        this.posts = posts;
        this.listener = listener;
    }

    // Interface definition for handling clicks
    public interface PostClickListener {
        void onPostClick(Post post);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView userNameTextView, postTitleTextView, postDescriptionTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            userNameTextView = itemView.findViewById(R.id.userNameTextView);
            postTitleTextView = itemView.findViewById(R.id.postTitleTextView);
            postDescriptionTextView = itemView.findViewById(R.id.postDescriptionTextView);
            itemView.setOnClickListener(this); // Set the click listener for the entire item view
        }

        public void bind(Post post) {
            userNameTextView.setText(post.getUsername()); // Should display "Default User"
            postTitleTextView.setText(post.getTitle());
            postDescriptionTextView.setText(post.getDescription());
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION && listener != null) {
                listener.onPostClick(posts.get(position));
            }
        }
    }
}
