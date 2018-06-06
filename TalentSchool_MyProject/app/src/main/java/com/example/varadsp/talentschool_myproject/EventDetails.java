package com.example.varadsp.talentschool_myproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.PicassoProvider;
import com.squareup.picasso.Target;

import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicBoolean;

public class EventDetails extends AppCompatActivity {


    ImageView imageView = null;

    private ProgressBar progressBar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        progressBar.setVisibility(progressBar.VISIBLE);



        TextView eventName = (TextView)findViewById(R.id.eventName);
        TextView eventDescr = (TextView)findViewById(R.id.eventDescr);
        TextView eventDate = (TextView)findViewById(R.id.eventDate);








        Intent i = getIntent();


        String EventName = i.getStringExtra("EventName");
        String EventDescr = i.getStringExtra("EventDescr");
        String EventDate = i.getStringExtra("EventDate");





//        URL url = null;
//        try {
//            url = new URL(ImageUrl);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//        Bitmap bmp = null;
//        try {
//            bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        imageView.setImageBitmap(bmp);



        eventName.setText(EventName);
        eventDescr.setText(EventDescr);
        eventDate.setText(EventDate);







    }

    //save image
    public static void imageDownload(Context ctx, String url){
        Picasso.get()
                .load("http://blog.concretesolutions.com.br/wp-content/uploads/2015/04/Android1.png")
                .into(getTarget(url));
    }

    //target to save
    private static Target getTarget(final String url) {
        Target target = new Target() {

            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {

                    @Override
                    public void run() {

                        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/" + url);
                      Log.e("SavedLocation",""+Environment.getExternalStorageDirectory().getPath() + "/" + url);

                        try {
                            file.createNewFile();
                            FileOutputStream ostream = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, ostream);
                            ostream.flush();
                            ostream.close();
                        } catch (IOException e) {
                            Log.e("IOException", e.getLocalizedMessage());
                        }
                    }
                }).start();

            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }

        };
        return target;
    }




            @Override
    protected void onStart() {
        super.onStart();

        Intent i = getIntent();

        final String ImageUrl = i.getStringExtra("ImageUrl");

         imageView= (ImageView)findViewById(R.id.eventImage);


//        Picasso.get().load(ImageUrl).into(imageView);


        Picasso.get().load(ImageUrl).into(imageView, new Callback(){

            @Override
            public void onSuccess() {
                progressBar.setVisibility(progressBar.INVISIBLE);

//                typedView.viewIndicator.setVisibility(View.GONE);
//                Picasso.with(context).cancelRequest(typedView.viewImage);
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(EventDetails.this, "Error Downloading Image!", Toast.LENGTH_SHORT).show();


            }
        });




        final boolean[] isImageFitToScreen = new boolean[1];

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DownloadImage().execute(ImageUrl);


                if(isImageFitToScreen[0]) {
                    isImageFitToScreen[0] =false;
                    imageView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    imageView.setAdjustViewBounds(true);
                    imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                }else{
                    isImageFitToScreen[0] =true;
                    imageView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                }
            }
        });
        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                imageDownload(getApplicationContext(),ImageUrl);
                Toast.makeText(EventDetails.this, "Downloading Image...", Toast.LENGTH_SHORT).show();
//
//                Intent i = getIntent();
//
//                String ImageUrl = i.getStringExtra("ImageUrl");

                new DownloadImage().execute(ImageUrl);
                return false;
            }
        });
//
//        final AtomicBoolean loaded = new AtomicBoolean();
//        Picasso.with(getApplicationContext()).load(ImageUrl).into(imageView, new Callback.EmptyCallback() {
//            @Override public void onSuccess() {
//                loaded.set(true);
//            }
//        });
//        if (loaded.get()) {
//            // The image was immediately available.
//        }




    }

}




// DownloadImage AsyncTask
class DownloadImage extends AsyncTask<String, Void, Bitmap> {


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // Create a progressdialog

    }

    @Override
    protected Bitmap doInBackground(String... URL) {

        String imageURL = URL[0];

        Bitmap bitmap = null;
        try {
            // Download Image from URL
            InputStream input = new java.net.URL(imageURL).openStream();
            // Decode Bitmap
            bitmap = BitmapFactory.decodeStream(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        // Set the bitmap into ImageView

    }
}
