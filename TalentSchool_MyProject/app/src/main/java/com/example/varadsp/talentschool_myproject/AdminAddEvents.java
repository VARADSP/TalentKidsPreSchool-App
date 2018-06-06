package com.example.varadsp.talentschool_myproject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.felipecsl.gifimageview.library.GifImageView;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import static android.app.Activity.RESULT_OK;
import static android.widget.Toast.makeText;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AdminAddEvents.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AdminAddEvents#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminAddEvents extends Fragment implements View.OnClickListener {
    String msg="New Event is here! Click To See";


    private GifImageView gifImageView;


    private static final int PICK_IMAGE_REQUEST = 101;
    private StorageReference mStorageRef;
    static int i = 0;

    static Boolean isNotRepeated;
    static Boolean isFileChosen = false;



    private static String vidName = null;
    private static String videoNameFinal = null;

    private static String finalPath = null;

    private Firebase mRootRef;

    private FirebaseAuth firebaseAuth;


    private static String gameNameFinal = null;
    private Uri filePath;

    private EditText editTextEventName;
    private EditText editTextEventDescription;

    private DatabaseReference databaseReference;


    private Button upload;
    private Button selection;
    String date;






    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AdminAddEvents() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminAddEvents.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminAddEvents newInstance(String param1, String param2) {
        AdminAddEvents fragment = new AdminAddEvents();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onResume() {
        super.onResume();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // To Avoid Soft Keyboard Problem
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_admin_add_events, container, false);

        firebaseAuth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("AppEvents");



        firebaseAuth.signInWithCustomToken("1b7ef0d8-3614-43be-b1eb-7da63d4deae6");




        ConnectivityManager conMgr = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        if ( conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED ) {

            // user is online
            //getSupportActionBar().setTitle("Sign In");
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Upload New Events");


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







        selection = (Button)v.findViewById(R.id.select_video_for_session);

        upload = (Button)v.findViewById(R.id.upload_imageButton);



        gifImageView = (GifImageView)v.findViewById(R.id.gifImageView);


        editTextEventName = (EditText)v.findViewById(R.id.editTextEventName);
        editTextEventDescription = (EditText)v.findViewById(R.id.editTextEventDescription);


         date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());


        selection.setOnClickListener(this);
        upload.setOnClickListener(this);



        YoYo.with(Techniques.RubberBand).duration(7000).repeat(20).playOn(v.findViewById(R.id.upload_imageButton));

        YoYo.with(Techniques.Bounce).duration(7000).repeat(20).playOn(v.findViewById(R.id.select_video_for_session));



        try{
            InputStream inputStream = getContext().getAssets().open("upcoming_events.gif");
            byte[] bytes = IOUtils.toByteArray(inputStream);
            gifImageView.setBytes(bytes);
            gifImageView.startAnimation();

        }
        catch (IOException ex){
            Toast.makeText(getActivity(), "Error"+ex, Toast.LENGTH_SHORT).show();

        }
        //wait for 3 seconds and start Activity Main
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


            }
        },100000); // 3000 means 3 secs




        return v;
    }



    /**
     * Enables https connections
     */
    @SuppressLint("TrulyRandom")
    public void handleSSLHandshake() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });
        } catch (Exception ignored) {
        }
    }


    //method to show file chooser
    private void showFileChooser() {
        isFileChosen = true;
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
    }







    //method to upload the videos in firebase
    private void uploadFile() {
        //if there is a file to upload
        if (filePath != null) {
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Uploading");
            progressDialog.show();
            mStorageRef = FirebaseStorage.getInstance().getReference();







            final String videoname = vidName;

            final String gameName = "AppEvents";




            Log.e("gameName",""+gameName);
            Toast.makeText(getActivity(), ""+gameName, Toast.LENGTH_SHORT).show();

            StorageReference riversRef = mStorageRef.child(gameName + "/"+videoname);

            finalPath = gameName + "/" + videoname;


            videoNameFinal = videoname;




            gameNameFinal = gameName;

            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                            //if the upload is successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();


                            mStorageRef.child(finalPath).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // Got the download URL for 'users/me/profile.png'
                                    Uri downloadUri = taskSnapshot.getMetadata().getDownloadUrl();
                                    final String generatedFilePath = String.valueOf(downloadUri); /// The string(file link) that you need



                                    //putting to firebase database


                                    mRootRef = new Firebase("https://talentschoolproject.firebaseio.com/"+gameNameFinal);




                                    Toast.makeText(getActivity(), "Success Listener", Toast.LENGTH_SHORT).show();






                                    mRootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {



                                            Log.w("VIDEOS",""+dataSnapshot);



                                            isNotRepeated = false;








//                                            Iterable<DataSnapshot> contactChildren = dataSnapshot.getChildren();


//
//                                            Log.e("Snaps "," " + contactChildren);
//
//                                            for (DataSnapshot contact : contactChildren) {
//
//                                                Log.e("Snaps With Now",""+contact);
//
//
////                                                if(generatedFilePath.equalsIgnoreCase(contact.getValue(String.class))){
////                                                    Toast.makeText(Uploader.this, "File is already uploaded", Toast.LENGTH_SHORT).show();
////                                                    finish();
////                                                }
//
//                                            }



//                                                if(generatedFilePath.equals(snap.getValue())){
//
//                                                    Toast.makeText(Uploader.this, "File is already uploaded", Toast.LENGTH_SHORT).show();
//                                                    finish();
//
//
//                                            }


//                                            Log.e(dataSnapshot.getKey(),dataSnapshot.getChildrenCount() + "");

//                                            i = (int) dataSnapshot.getChildrenCount();



//                                            Log.e("Count is " , " =" +i);



                                            final Firebase databaseRef = new Firebase("https://talentschoolproject.firebaseio.com/"+gameNameFinal);

                                            databaseRef.addValueEventListener(new ValueEventListener() {
                                                @SuppressLint("LongLogTag")
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {




                                                    Log.e("Videos Received", "" + dataSnapshot);

                                                    long count = dataSnapshot.getChildrenCount();
                                                    Log.e("Intial Count " , " Count = " + count);




                                                }

                                                @Override
                                                public void onCancelled(FirebaseError firebaseError) {

                                                }
                                            });





                                                DatabaseReference childRef = databaseReference.push();//Need count here!
                                                childRef.child("EventName").setValue(editTextEventName.getText().toString());
                                                childRef.child("EventDate").setValue(date);
                                                childRef.child("EventDescription").setValue(editTextEventDescription.getText().toString());
                                                childRef.child("EventImageUrl").setValue(generatedFilePath);










                                        }

                                        @Override
                                        public void onCancelled(FirebaseError firebaseError) {

                                        }
                                    });





                                    Toast.makeText(getActivity(), generatedFilePath, Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle any errors
                                }
                            });

                            //and displaying a success toast
                            Toast.makeText(getActivity(), "File Uploaded ", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //if the upload is not successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying error message
                            Toast.makeText(getActivity(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            //displaying percentage in progress dialog
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        }
        //if there is not any file
        else {
            //you can display an error toast
            Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                File file = new File(filePath.getPath());
                vidName = file.getName();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }









    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    @Override
    public void onClick(View v) {
        if(v == selection){
            //selection code


            showFileChooser();
            makeText(getContext(), "In File Chooser", Toast.LENGTH_SHORT).show();



        }


        if(v == upload){
            //uploading to firebase code

            Toast.makeText(getActivity(), "You Clicked Upload", Toast.LENGTH_SHORT).show();
            if(!isFileChosen){
                Toast.makeText(getActivity(), "Please Choose A File", Toast.LENGTH_SHORT).show();
            }
            else if(editTextEventDescription.getText().toString().trim().length() == 0 || editTextEventName.getText().toString().trim().length() == 0 ){
                Toast.makeText(getActivity(), "Please Enter Fill All Event Details", Toast.LENGTH_SHORT).show();
            }

            else{
                Toast.makeText(getActivity(), "In Uploading", Toast.LENGTH_SHORT).show();
                uploadFile();


                String temp = "https://showmetheshows.000webhostapp.com/sendnotifications.php?tokenMessage="+msg;

                temp = temp.replaceAll(" ", "%20");


                StringRequest stringRequest = new StringRequest(Request.Method.GET, temp,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(getActivity(),"Messages Are Away!",Toast.LENGTH_LONG).show();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getActivity(),"Error! Please Try Again :"+error,Toast.LENGTH_LONG).show();
                            }
                        }){
                    @Override
                    protected Map<String,String> getParams(){
                        //doesn't work in GET Request
                        Map<String,String> params = new HashMap<String, String>();
                        params.put("tokenMessage",msg);

                        try {
                            FirebaseInstanceId.getInstance().deleteInstanceId();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        params.put("token",FirebaseInstanceId.getInstance().getToken());

                        return params;
                    }

                };

                RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                requestQueue.add(stringRequest);



            }




        }

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
