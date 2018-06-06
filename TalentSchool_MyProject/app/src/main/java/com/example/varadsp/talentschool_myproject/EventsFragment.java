package com.example.varadsp.talentschool_myproject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EventsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EventsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class EventsFragment extends Fragment {


    ListView listViewVideos;

    Activity activity;

    List<EventsClass> videosList;

    private Firebase mRef;

    private FirebaseAuth firebaseAuth;





    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public EventsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EventsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EventsFragment newInstance(String param1, String param2) {
        EventsFragment fragment = new EventsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        activity = getActivity();



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_events, container, false);


        ConnectivityManager conMgr = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        if ( conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED ) {

            // user is online
            //getSupportActionBar().setTitle("Sign In");



        }
        else if ( conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED) {

            // notify user you are not online

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Please Connect to Internet")
                    .setCancelable(false)
                    .setPositiveButton("Connect to Internet", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                        }
                    })
                    .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();



            //getSupportActionBar().setTitle("Sign In");
//            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Please Turn On Internet");


        }







        activity=getActivity();

        listViewVideos = (ListView)v.findViewById(R.id.listViewVideo);






        listViewVideos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(getActivity(),EventDetails.class);
                i.putExtra("ImageUrl",videosList.get(position).getEventImageUrl());
                i.putExtra("EventName",videosList.get(position).getEventName());
                i.putExtra("EventDescr",videosList.get(position).getEventDescription());
                i.putExtra("EventDate",videosList.get(position).getEventDate());


                startActivity(i);

            }
        });

        mRef = new Firebase("https://talentschoolproject.firebaseio.com/"+"AppEvents");


        videosList = new ArrayList<EventsClass>();



        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onStart() {
        super.onStart();

        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Getting Latest Events");
        pd.show();



        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Getting Events");



        ConnectivityManager conMgr = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        if ( conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED ) {

            // user is online
            //getSupportActionBar().setTitle("Sign In");


        }
        else if ( conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED) {

            // notify user you are not online

            //getSupportActionBar().setTitle("Sign In");
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Please Turn On Internet");


        }










        mRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                videosList.clear();

                Log.e("Videos Received",""+dataSnapshot);

                for(DataSnapshot videoSnapshot : dataSnapshot.getChildren()){

                    Log.e("Single Video Object",""+videoSnapshot);

                    EventsClass eventClass = new EventsClass();

                    String eventDate = null;
                    String eventDescr = null;
                    String imageUrl = null;
                    String eventName = null;
                    String key=null;




                    Integer i = 0;
                    for(DataSnapshot videoParams : videoSnapshot.getChildren()){
                        Log.e("Video Params" , " "+videoParams.getValue(String.class));
                        if(i==0){
//                            videoname = videoParams.getValue(String.class);
                            eventDate = videoParams.getValue(String.class);
                            key = videoSnapshot.getKey();
                            i=1;
                        }
                        else if(i==1){
                            eventDescr = videoParams.getValue(String.class);
                            i=2;
                        }
                        else if(i==2){
                            imageUrl = videoParams.getValue(String.class);
                            i=3;
                        }
                        else{
                            eventName = videoParams.getValue(String.class);
                        }

                    }
                    Log.e("Event Object transformed Name",""+eventName);
                    Log.e("Event Object transformed Name",""+eventDescr);
                    Log.e("Event Object transformed Name",""+imageUrl);
                    Log.e("Event Object transformed Name",""+eventDate);

                    eventClass = new EventsClass(eventName,eventDescr,imageUrl,eventDate,key);

//                    Videos videos = videoSnapshot.getValue(Videos.class);

//
                    videosList.add(eventClass);

                }


//
                //Crashes app sometimes ! Error Occurs Which I can't figure out :(
                VideoList adapter = new VideoList(activity,videosList);



                listViewVideos.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                pd.dismiss();

                //getSupportActionBar().setTitle("Sign In");
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Events From School");

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Talent School");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
