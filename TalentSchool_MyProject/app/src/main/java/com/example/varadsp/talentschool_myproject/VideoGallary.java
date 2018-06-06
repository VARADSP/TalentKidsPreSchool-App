package com.example.varadsp.talentschool_myproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link VideoGallary.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link VideoGallary#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VideoGallary extends Fragment {


    private RecyclerView recyclerView;
    private AlbumsAdapter adapter;
    private List<Album> albumList;

    Toolbar toolbar;
     CollapsingToolbarLayout collapsingToolbar=null;

    private ArrayList<Integer> covers = new ArrayList<>();


    List<Album> videosList;
    private Firebase mRef;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public VideoGallary() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VideoGallary.
     */
    // TODO: Rename and change types and number of parameters
    public static VideoGallary newInstance(String param1, String param2) {
        VideoGallary fragment = new VideoGallary();
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
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("School Video Gallary");
    }

    @Override
    public void onStart() {
        super.onStart();






        ConnectivityManager conMgr = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        if ( conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED ) {

            collapsingToolbar.setTitle("Getting Videos");
        }
        else if ( conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED) {

            // notify user you are not online
            collapsingToolbar.setTitle("No Internet");
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Please Turn On Internet");


        }





        mRef.addValueEventListener(new ValueEventListener() {
            Integer i2=0;
            @SuppressLint("LongLogTag")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                videosList.clear();

                Log.e("Videos Received",""+dataSnapshot);

                for(DataSnapshot videoSnapshot : dataSnapshot.getChildren()){

                    Log.e("Single Video Object",""+videoSnapshot);

                    Album videoClass = new Album();
                    String videoname = null;
                    String videoUrl=null;
                    String videoDescr = null;
                    String key = null;
                    Integer i = 0;
                    for(DataSnapshot videoParams : videoSnapshot.getChildren()){
                        Log.e("Video Params" , " "+videoParams.getValue(String.class));
                        Log.e("Video Key" , " "+videoSnapshot.getKey());

                        if(i==0){
                            videoname = videoParams.getValue(String.class);
                            key = videoSnapshot.getKey();


                            i=1;
                        }
                        if(i==1){
                            videoUrl = videoParams.getValue(String.class);
                            i=2;
                        }
                        if(i==2){
                            videoDescr = videoParams.getValue(String.class);
                        }

                    }


                    if(i2 == 0){
                        videoClass = new Album(videoname,videoUrl,R.drawable.welcome1,videoDescr,key);
                        i2=1;
                    }
                    else if(i2==1){
                        videoClass = new Album(videoname,videoUrl,R.drawable.welcome2,videoDescr,key);
                         i2=2;
                    }
                    else if(i2==2){
                        videoClass = new Album(videoname,videoUrl,R.drawable.welcome3,videoDescr,key);
                        i2=0;
                    }

//                    a = new Album("Sugar Ray", 8, covers[1]);
//                    albumList.add(a);

//                    Videos videos = videoSnapshot.getValue(Videos.class);
                    // Log.e("video Object transformed",""+videoSnapshot.getValue(VideoClass.class));
//
                    videosList.add(videoClass);

                }
//
//                VideoList adapter = new VideoList(VideoPlayer.this,videosList);
//
//
//
//                listViewVideos.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                collapsingToolbar.setTitle("VideoGallary");


            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });





    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_video_gallary, container, false);

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
//
//            //getSupportActionBar().setTitle("Sign In");
//            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Please Turn On Internet");


        }




//        YoYo.with(Techniques.Tada).duration(7000).repeat(20).playOn(findViewById(R.id.uploader));


        videosList = new ArrayList<Album>();
        mRef = new Firebase("https://talentschoolproject.firebaseio.com/AppVideos");





        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("School Video Gallary");


//        getActivity().requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        setSupportActionBar(toolbar);



//        initCollapsingToolbar();


         collapsingToolbar =
                (CollapsingToolbarLayout) v.findViewById(R.id.collapsing_toolbar);

        AppBarLayout appBarLayout = (AppBarLayout) v.findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle("Video Gallary");
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle("Video Gallary");
                    isShow = false;
                }
            }
        });




        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);

        albumList = new ArrayList<>();
        adapter = new AlbumsAdapter(getActivity(), videosList);




        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getActivity(), "Clicking On Cards"+view, Toast.LENGTH_SHORT).show();
            }
        });



        try {
            Glide.with(this).load(R.drawable.coverforvideogallary).into((ImageView) v.findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }




        return v;
    }





    /**
     * Adding few albums for testing
     */
//    private void prepareAlbums() {
//        int[] covers = new int[]{
//                R.drawable.videothumbnail,
//                R.drawable.videothumbnail,
//                R.drawable.videothumbnail,
//                R.drawable.videothumbnail,
//                R.drawable.videothumbnail,
//                R.drawable.videothumbnail,
//                R.drawable.videothumbnail,
//                R.drawable.videothumbnail,
//                R.drawable.videothumbnail,
//                R.drawable.videothumbnail,
//                R.drawable.videothumbnail};
//
//        Album a = new Album("Maroon5", 13, covers[0]);
////        albumList.add(a);
//
//        a = new Album("Sugar Ray", 8, covers[1]);
//        albumList.add(a);
//
//        a = new Album("Bon Jovi", 11, covers[2]);
//        albumList.add(a);
//
//        a = new Album("The Corrs", 12, covers[3]);
//        albumList.add(a);
//
//        a = new Album("The Cranberries", 14, covers[4]);
//        albumList.add(a);
//
//        a = new Album("Westlife", 1, covers[5]);
//        albumList.add(a);
//
//        a = new Album("Black Eyed Peas", 11, covers[6]);
//        albumList.add(a);
//
//        a = new Album("VivaLaVida", 14, covers[7]);
//        albumList.add(a);
//
//        a = new Album("The Cardigans", 11, covers[8]);
//        albumList.add(a);
//
//        a = new Album("Pussycat Dolls", 17, covers[9]);
//        albumList.add(a);
//
//        adapter.notifyDataSetChanged();
//    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getActivity(), "Clicking On The Whole Card", Toast.LENGTH_SHORT).show();
                }
            });

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
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
