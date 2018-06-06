package com.example.varadsp.talentschool_myproject;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

/**
 * Created by VARAD on 22-03-2018.
 */

public class VideoList extends ArrayAdapter<EventsClass> {
    private Activity context;

    private List<EventsClass> videoList;



    public VideoList(Activity context, List<EventsClass> videoList){

        super(context,R.layout.list_layout,videoList);
        this.context= context;
        this.videoList = videoList;



    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        Button btn_remove_event = null;
        final EventsClass videos = videoList.get(position);


        View listViewItem = null;
        if (SharedPrefManager.getInstance(context).isLoggedIn()) {
            Toast.makeText(context, SharedPrefManager.getInstance(context).getUser().getCategory(), Toast.LENGTH_SHORT).show();

            if (SharedPrefManager.getInstance(context).getUser().getCategory().equals("Admin")) {

                listViewItem = inflater.inflate(R.layout.list_layout_admin,null,true);

                 btn_remove_event = (Button)listViewItem.findViewById(R.id.deleteEvent);


                btn_remove_event.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("AppEvents");

                        StorageReference videoRef = FirebaseStorage.getInstance().getReferenceFromUrl(videos.getEventImageUrl());

                        videoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                databaseReference.child(videos.getKey()).removeValue();
                                Toast.makeText(context, "Event Deleted Successfully!", Toast.LENGTH_SHORT).show();
                            }

                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "Failure in deleting event,Please Try Again!", Toast.LENGTH_LONG).show();
                            }
                        });



                    }
                });


            }
            else{
                listViewItem = inflater.inflate(R.layout.list_layout,null,true);
            }
        }
        else{
            listViewItem = inflater.inflate(R.layout.list_layout,null,true);
        }


        TextView textViewName = (TextView)listViewItem.findViewById(R.id.textViewVideoName);
        TextView textViewUrl = (TextView)listViewItem.findViewById(R.id.textViewVideoUrl);
//
//
        //        YoYo.with(Techniques.Pulse).duration(700).repeat(10).playOn(findViewById(R.id.textViewVideoName));
//        YoYo.with(Techniques.Shake).duration(700).repeat(10).playOn(findViewById(R.id.textViewVideoName));







        //Event Description is Date
        //Event Name is Event Name

        String vidNo = "Event Description :" + videos.getEventDescription();

        String vidName = "Event Name: "+videos.getEventName();


        textViewName.setText(vidName);
        textViewUrl.setText(vidNo);


        return listViewItem;


    }
}
