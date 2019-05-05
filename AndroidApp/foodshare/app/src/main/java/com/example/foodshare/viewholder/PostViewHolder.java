package com.example.foodshare.viewholder;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.foodshare.ImageUtils;
import com.example.foodshare.R;
import com.example.foodshare.models.Post;
import com.example.foodshare.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.content.Context;
public class PostViewHolder extends RecyclerView.ViewHolder {
    public ImageView postAuthorPhoto;
    public TextView titleView;
    public TextView authorView;
    public ImageView starView;
    public TextView numStarsView;
    public TextView bodyView;
    public TextView addressView;
    public TextView starTextView;
    public ImageView pictureView;
    private DatabaseReference mDatabase;
    public Context context;

    public TextView textView_estimatedPrice;

    public PostViewHolder(View itemView, Context context) {
        super(itemView);

        titleView = itemView.findViewById(R.id.postTitle);
        authorView = itemView.findViewById(R.id.postAuthor);
        postAuthorPhoto = itemView.findViewById(R.id.postAuthorPhoto);
        starView = itemView.findViewById(R.id.star);
        numStarsView = itemView.findViewById(R.id.postNumStars);
        bodyView = itemView.findViewById(R.id.postBody);
        addressView = itemView.findViewById(R.id.postAddress);
        pictureView = itemView.findViewById(R.id.pictureImageView);
        starTextView = itemView.findViewById(R.id.starTextView);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        this.context = context;
    }

    public void bindToPost(Post post, View.OnClickListener starClickListener) {
        titleView.setText(post.title);
        authorView.setText(post.author);
        numStarsView.setText(String.valueOf(post.starCount));
        bodyView.setText(post.description);
        addressView.setText(post.address);
        if (post.picture!=null && !post.picture.equals("")){
            pictureView.setImageBitmap(ImageUtils.stringToBitmap(post.picture));
            pictureView.setVisibility(View.VISIBLE);
        }
        else{
            pictureView.setVisibility(View.GONE);
        }
        starView.setOnClickListener(starClickListener);
        if (post.uid!=null) {
            DatabaseReference ref = mDatabase.child("users").child(post.uid);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                    //Serialize retrieved data to a User object
                    User user = dataSnapshot.getValue(User.class);
                    //Now you have an object of the User class and can use its getters like this
                    if (user != null) {
                        // Set the user profile picture
                        if (user.avatar != null) {
                            postAuthorPhoto.setImageBitmap(user.getAvatar());
                        }
                        starTextView.setText( String.format("%.1f", user.rate)+"/5.0");
                    }

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w("TAG", "loadPost:onCancelled", databaseError.toException());
                }
            });
        }
        else{
            postAuthorPhoto.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_person_24px));
        }
    }
}