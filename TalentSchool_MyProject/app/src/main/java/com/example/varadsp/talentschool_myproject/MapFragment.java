package com.example.varadsp.talentschool_myproject;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Location;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.LocationListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {

    GoogleMap map;


    SupportMapFragment supportMapFragment;


    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;

    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Talent School Location");

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_map, container, false);








        ConnectivityManager conMgr = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        if ( conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED ) {

            // user is online
            //getSupportActionBar().setTitle("Sign In");
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("School Location");


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







        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        if (supportMapFragment == null) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            supportMapFragment = SupportMapFragment.newInstance();
            ft.replace(R.id.map, supportMapFragment).commit();
        }

        supportMapFragment.getMapAsync(this);

        return v;
    }

    @Override
    public void onPause() {
        super.onPause();

        //stop location updates when Activity is no longer active
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, (com.google.android.gms.location.LocationListener) this);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }




    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getActivity())
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        map.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getActivity(), "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Talent School Location");

        map = googleMap;


        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
                map.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        }
        else {
            buildGoogleApiClient();
            map.setMyLocationEnabled(true);
        }




        //19.870252,75.313285

        LatLng pp = new LatLng(19.870252, 75.313285);
        MarkerOptions option = new MarkerOptions();
        option.position(pp).title("Talent School");




        map.addMarker(option);
        map.moveCamera(CameraUpdateFactory.newLatLng(pp));
//        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(19.870252,75.313285), 15.5f), 400, null);


        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker marker) {
                final LatLng position = marker.getPosition();
                marker.setTitle("PlotNo. 5/13/94,Padampura,Aurangabad.431 005");

                marker.showInfoWindow();

                map.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 14.0f), 1000, new GoogleMap.CancelableCallback() {

                    @Override
                    public void onFinish() {
                        Projection projection = map.getProjection();
                        Point point = projection.toScreenLocation(position);
                        point.x -= 100;
                        point.y -= 100;
                        LatLng offsetPosition = projection.fromScreenLocation(point);
                        map.animateCamera(CameraUpdateFactory.newLatLng(offsetPosition), 300, null);
                    }

                    @Override
                    public void onCancel() {
                    }
                });
                return true;
            }
        });



        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng position) {
                List<CameraUpdate> updates = new ArrayList<CameraUpdate>();
                CameraPosition.Builder builder = CameraPosition.builder();
                builder.target(position);
                updates.add(CameraUpdateFactory.newCameraPosition(builder.build()));
                builder.target(new LatLng(position.latitude + 20, position.longitude));
                updates.add(CameraUpdateFactory.newCameraPosition(builder.build()));
                builder.bearing(90);
                updates.add(CameraUpdateFactory.newCameraPosition(builder.build()));
                builder.target(new LatLng(position.latitude + 20, position.longitude + 40));
                updates.add(CameraUpdateFactory.newCameraPosition(builder.build()));
                builder.bearing(180);
                updates.add(CameraUpdateFactory.newCameraPosition(builder.build()));
                builder.target(new LatLng(position.latitude, position.longitude + 40));
                updates.add(CameraUpdateFactory.newCameraPosition(builder.build()));
                builder.bearing(270);
                updates.add(CameraUpdateFactory.newCameraPosition(builder.build()));
                builder.target(position);
                updates.add(CameraUpdateFactory.newCameraPosition(builder.build()));

                loopAnimateCamera(updates);
            }
        });

        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

            @Override
            public void onMapLongClick(LatLng position) {
                map.stopAnimation();
            }
        });



//        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//
//        LatLng coordinate = new LatLng(19.870252, 75.313285); //Store these lat lng values somewhere. These should be constant.
//        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
//                coordinate, 15);
//        map.animateCamera(location);


//
//        map.setMyLocationEnabled(true);
//        CameraPosition cameraPosition = new CameraPosition.Builder()
//                .target(new LatLng(19.870252, 75.313285)).zoom(15).build();
//        map.animateCamera(CameraUpdateFactory
//                .newCameraPosition(cameraPosition));


    }

    private void loopAnimateCamera(final List<CameraUpdate> updates) {
        CameraUpdate update = updates.remove(0);
        updates.add(update);
        map.animateCamera(update, 1000, new GoogleMap.CancelableCallback() {

            @Override
            public void onFinish() {
                loopAnimateCamera(updates);
            }

            @Override
            public void onCancel() {
                Log.i("MAPTAG", "camera animation cancelled");
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Your Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = map.addMarker(markerOptions);

        mCurrLocationMarker.setTitle("Your Location");

        mCurrLocationMarker.showInfoWindow();



        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                final LatLng position = mCurrLocationMarker.getPosition();
                mCurrLocationMarker.setTitle("Your Location");

                mCurrLocationMarker.showInfoWindow();

                map.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 14.0f), 1000, new GoogleMap.CancelableCallback() {

                    @Override
                    public void onFinish() {
                        Projection projection = map.getProjection();
                        Point point = projection.toScreenLocation(position);
                        point.x -= 100;
                        point.y -= 100;
                        LatLng offsetPosition = projection.fromScreenLocation(point);
                        map.animateCamera(CameraUpdateFactory.newLatLng(offsetPosition), 300, null);
                    }

                    @Override
                    public void onCancel() {
                    }
                });
                return true;
            }
        });

        //move map camera
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,11));

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
//            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
