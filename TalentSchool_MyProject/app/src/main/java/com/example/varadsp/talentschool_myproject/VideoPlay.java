package com.example.varadsp.talentschool_myproject;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

public class VideoPlay extends AppCompatActivity {

    private Button btnonce, btncontinuously, btnstop, btnplay,btnfullscreen;
    private VideoView vv;
    private MediaController mediacontroller;
    private Uri uri;
    private boolean isContinuously = false;
    private ProgressBar progressBar;

    VideoView videoView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);



        Intent i = getIntent();

        String VideoUrl = i.getStringExtra("VideoUrl");

        progressBar = (ProgressBar) findViewById(R.id.progrss);
        btnonce = (Button) findViewById(R.id.btnonce);
        btncontinuously = (Button) findViewById(R.id.btnconti);
        btnstop = (Button) findViewById(R.id.btnstop);
        btnplay = (Button) findViewById(R.id.btnplay);
        btnfullscreen = (Button) findViewById(R.id.btnfullscreen);
        vv = (VideoView) findViewById(R.id.vv);

        mediacontroller = new MediaController(this);
        mediacontroller.setAnchorView(vv);
        //String uriPath = "https://www.demonuts.com/Demonuts/smallvideo.mp4"; //update package name
        final String uriPath = VideoUrl;



        uri = Uri.parse(uriPath);

        vv.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(isContinuously){
                    vv.start();
                }
            }
        });

        btnstop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vv.pause();
            }
        });

        btnplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vv.start();
            }
        });

        btnonce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isContinuously = false;
                progressBar.setVisibility(View.VISIBLE);
                vv.setMediaController(mediacontroller);
                vv.setVideoURI(uri);
                vv.requestFocus();
                vv.start();
            }
        });

        btncontinuously.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isContinuously = true;
                progressBar.setVisibility(View.VISIBLE);
                vv.setMediaController(mediacontroller);
                vv.setVideoURI(uri);
                vv.requestFocus();
                vv.start();
            }
        });

        btnfullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                isContinuously = true;
//                progressBar.setVisibility(View.VISIBLE);
//                vv.setMediaController(mediacontroller);
//                vv.setVideoURI(uri);
//                vv.requestFocus();
//                vv.start();

                Intent intent = new Intent(Intent.ACTION_VIEW );
                intent.setDataAndType(Uri.parse(uriPath), "video/*");
                startActivity(intent);
            }
        });



        vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            // Close the progress bar and play the video
            public void onPrepared(MediaPlayer mp) {
                progressBar.setVisibility(View.GONE);
            }
        });

    }
}
