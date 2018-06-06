package com.example.varadsp.talentschool_myproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class AdminActivityDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,EventsFragment.OnFragmentInteractionListener
        ,Home_Fragment.OnFragmentInteractionListener,AdminAddVideos.OnFragmentInteractionListener,VideoGallary.OnFragmentInteractionListener,
         AdminAddEvents.OnFragmentInteractionListener,
            SendPushNotificationToAll.OnFragmentInteractionListener{

    private FirebaseAuth firebaseAuth;

    int homeFragmentIdentifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Fragment fragment=new Home_Fragment();

        firebaseAuth = FirebaseAuth.getInstance();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_admin_frame, fragment).addToBackStack("AdminActivityDrawer");
        homeFragmentIdentifier= ft.commit();



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.admin_activity_drawer, menu);
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
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;

        if (id == R.id.nav_admin_home) {
            fragment=new Home_Fragment();


            // Handle the camera action
        } else if (id == R.id.nav_addevemt) {
            fragment = new AdminAddEvents();



        } else if (id == R.id.nav_addvideo) {
            fragment = new AdminAddVideos();




        }  else if (id == R.id.nav_admin_events) {

            fragment = new EventsFragment();

        }
        else if (id == R.id.nav_location) {

            fragment = new MapFragment();

        }
        else if (id == R.id.nav_notification) {

            fragment = new SendPushNotificationToAll();

        }
        else if (id == R.id.nav_admin_videos) {

            fragment=new VideoGallary();

        } else if (id == R.id.nav_logout) {

            firebaseAuth.signOut();

            Toast.makeText(getApplicationContext(),"You are successfully logout",Toast.LENGTH_SHORT).show();
            finish();
            SharedPrefManager.getInstance(getApplicationContext()).logout();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));

        }

        if (fragment != null)
        {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_admin_frame, fragment).addToBackStack("AdminActivityDrawer");
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
