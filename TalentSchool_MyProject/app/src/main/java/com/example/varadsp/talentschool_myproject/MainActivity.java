package com.example.varadsp.talentschool_myproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatDelegate;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,EventsFragment.OnFragmentInteractionListener
        ,Home_Fragment.OnFragmentInteractionListener
        ,AdminLogin.OnFragmentInteractionListener
        ,VideoGallary.OnFragmentInteractionListener
        {





    FragmentTransaction ft;

    public TextView coachNameInDrawer;

    int homeFragmentIdentifier;

    TextView eye;

    private final static String BACK_FRAGMENT="BACK_FRAGMENT";


    @Override
    protected void onCreate(Bundle savedInstanceState) {




//        Typeface font = Typeface.createFromAsset( getAssets(), "fontawesome-webfont.ttf" );
//
//        eye = (TextView)findViewById(R.id.eye);
//        eye.setTypeface(font);



//        getSupportActionBar().setTitle("Talent School");

        super.onCreate(savedInstanceState);






       setContentView(R.layout.activity_main);


        Fragment fragment=new Home_Fragment();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment).addToBackStack("MainActivity");
        homeFragmentIdentifier= ft.commit();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            Toast.makeText(this, SharedPrefManager.getInstance(this).getUser().getCategory() , Toast.LENGTH_SHORT).show();

            if(SharedPrefManager.getInstance(this).getUser().getCategory().equals("Admin"))
            {


                handleSSLHandshake();

                final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(MainActivity.this, "Signed In", Toast.LENGTH_SHORT).show();
                        }
                    }
                });




                FirebaseIDService firebaseIDService = new FirebaseIDService();
                firebaseIDService.onTokenRefresh();


                try {
                    FirebaseInstanceId.getInstance().deleteInstanceId();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://showmetheshows.000webhostapp.com/refreshtoken.php?id=1&token="+FirebaseInstanceId.getInstance().getToken(),
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(MainActivity.this,"Refreshing Token",Toast.LENGTH_LONG).show();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MainActivity.this,"Error Please Try Restarting App "+error.toString(),Toast.LENGTH_LONG).show();
                            }
                        }){
                    @Override
                    protected Map<String,String> getParams(){
                        Map<String,String> params = new HashMap<String, String>();




                        params.put("token",FirebaseInstanceId.getInstance().getToken());
                        params.put("id", String.valueOf(1));




                        return params;
                    }

                };

                RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                requestQueue.add(stringRequest);







                finish();
                startActivity(new Intent(this, AdminActivityDrawer.class));
            }

        }




        //On Refresh Token Updating Database





        handleSSLHandshake();

        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Signed In", Toast.LENGTH_SHORT).show();
                }
            }
        });




        FirebaseIDService firebaseIDService = new FirebaseIDService();
        firebaseIDService.onTokenRefresh();

        try {
            FirebaseInstanceId.getInstance().deleteInstanceId();
        } catch (IOException e) {
            e.printStackTrace();
        }

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://showmetheshows.000webhostapp.com/refreshtoken.php?id="+SharedPrefManager.getInstance(getApplicationContext()).getUser().getId()+"&token="+FirebaseInstanceId.getInstance().getToken(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(MainActivity.this,"Assigining Token",Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,"Error Please Try Restarting App "+error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();





                params.put("token",FirebaseInstanceId.getInstance().getToken());
                params.put("id", String.valueOf(SharedPrefManager.getInstance(getApplicationContext()).getUser().getId()));




                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);












        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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


            public void startService(View v){

        Intent intent = new Intent(this,TheService.class);
        startService(intent);


    }

    public void stopService(View v){
        Intent intent = new Intent(this,TheService.class);
        stopService(intent);


    }







    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            getFragmentManager().popBackStack(homeFragmentIdentifier, 0);
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            finish();
            System.exit(0);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {


        Fragment fragment=null;
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_events) {
            fragment=new EventsFragment();
            // Handle the camera action
        } else if (id == R.id.nav_home) {

            fragment=new Home_Fragment();

        }
        else if (id == R.id.nav_admin) {

            fragment=new AdminLogin();
        }
        else if (id == R.id.nav_videos) {

            fragment=new VideoGallary();
        }
        else if (id == R.id.nav_location) {

            fragment=new MapFragment();
        }



        if (fragment != null)
        {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment).addToBackStack("MainActivity");
            homeFragmentIdentifier= ft.commit();
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }





    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
