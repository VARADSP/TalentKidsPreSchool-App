package com.example.varadsp.talentschool_myproject;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.MyViewHolder> {

    private Context mContext;
    private List<Album> albumList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count;
        public ImageView thumbnail, overflow;


        public MyViewHolder(View view) {
            super(view);


            title = (TextView) view.findViewById(R.id.title);
            count = (TextView) view.findViewById(R.id.count);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "Item Clicked"+getAdapterPosition(), Toast.LENGTH_SHORT).show();
                }
            });

            thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("ClickingOn Card","Clicked");

                }
            });

            overflow = (ImageView) view.findViewById(R.id.overflow);
        }




    }


    public AlbumsAdapter(Context mContext, List<Album> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_card, parent, false);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Clicking On The Whole Card!", Toast.LENGTH_SHORT).show();
            }
        });

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Album album = albumList.get(position);
        holder.title.setText(album.getNumOfSongs());
        holder.count.setText(album.getName());

        //getNumOfSongs has Video Name
        //getName contains Video Description
        //VideoDescription contains the url


        //On Click For Thumbnail
        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Position" + position+" Url" + album.getNumOfSongs(), Toast.LENGTH_SHORT).show();

                Intent i = new Intent(mContext,VideoPlay.class);
                i.putExtra("VideoUrl",album.getVideoDescription());
                mContext.startActivity(i);

            }
        });

        // loading album cover using Glide library
        Glide.with(mContext).load(album.getThumbnail()).into(holder.thumbnail);

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow,position);
            }
        });
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view,int position) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_album, popup.getMenu());



        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(position));
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
            Toast.makeText(mContext, "Clicking On The Cards Yo!", Toast.LENGTH_SHORT).show();
        }

        private int position;
        public MyMenuItemClickListener(int positon) {
            this.position=positon;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            final Album album = albumList.get(position);
            switch (menuItem.getItemId()) {
                case R.id.action_add_favourite:
                    if (SharedPrefManager.getInstance(mContext).isLoggedIn()) {
                        Toast.makeText(mContext, SharedPrefManager.getInstance(mContext).getUser().getCategory(), Toast.LENGTH_SHORT).show();

                        if (SharedPrefManager.getInstance(mContext).getUser().getCategory().equals("Admin")) {

                            Toast.makeText(mContext, "Admin Remove", Toast.LENGTH_SHORT).show();

                            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("AppVideos");

                            StorageReference videoRef = FirebaseStorage.getInstance().getReferenceFromUrl(album.getVideoDescription());

                            videoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    databaseReference.child(album.getKey()).removeValue();
                                    Toast.makeText(mContext, "Video Deleted Successfully!", Toast.LENGTH_SHORT).show();
                                }

                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(mContext, "Failure in deleting video,Please Try Again!", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                        else{
                            Toast.makeText(mContext, "Admin Access Required!", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(mContext, "Admin Access Required!", Toast.LENGTH_SHORT).show();
                    }

                    return true;
                case R.id.action_play_next:
                    Toast.makeText(mContext, "Play next", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(mContext,VideoPlay.class);

                    i.putExtra("VideoUrl",album.getVideoDescription());
                    mContext.startActivity(i);
                    return true;
                case R.id.thumbnail:
                    Toast.makeText(mContext, "Clicking On The Thumbnail", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }
}
