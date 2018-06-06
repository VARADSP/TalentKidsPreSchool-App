package com.example.varadsp.talentschool_myproject;

import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;


import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;


public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {


    String imgUrl="";
    String appImgUrl="";
    String notifTitle="";
    String notifText="";
    RemoteViews contentViewBig,contentViewSmall;
    private static final String TAG = "FCM Service";
    Bitmap bitmap;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        super.onMessageReceived(remoteMessage);

        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        // TODO: Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated.
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());



        try {
            if (remoteMessage.getData().size() > 0) {
                Bundle extras = new Bundle();
                for (Map.Entry<String, String> entry : remoteMessage.getData().entrySet()) {
                    extras.putString(entry.getKey(), entry.getValue());

                    //Check for your custom Key Value Pairs
                    if (entry.getKey().equals("image")) {
                        imgUrl = entry.getValue();
                    }
                    if (entry.getKey().equals("image")) {
                        appImgUrl = entry.getValue();
                    }
                    if (entry.getKey().equals("title")) {
                        notifTitle = entry.getValue();
                    }
                    if (entry.getKey().equals("text")) {
                        notifText = entry.getValue();
                    }
                }
            }
            long when = System.currentTimeMillis();
            NotificationManager mNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            contentViewBig = new RemoteViews(getPackageName(), R.layout.custom_notification);
            contentViewSmall = new RemoteViews(getPackageName(),R.layout.custom_notification_small);
            //Null and Empty checks for your Key Value Pairs
            if(imgUrl!=null && !imgUrl.isEmpty()) {
                URL imgUrlLink = new URL(imgUrl);
                contentViewBig.setImageViewBitmap(R.id.image_pic, BitmapFactory.decodeStream(imgUrlLink.openConnection().getInputStream()));
            }
            if(appImgUrl!=null && !appImgUrl.isEmpty()) {
                URL appImgUrlLink = new URL(appImgUrl);
                contentViewBig.setImageViewBitmap(R.id.image_app, BitmapFactory.decodeStream(appImgUrlLink.openConnection().getInputStream()));
                contentViewSmall.setImageViewBitmap(R.id.image_app, BitmapFactory.decodeStream(appImgUrlLink.openConnection().getInputStream()));
            }
            if(notifTitle!=null && !notifTitle.isEmpty()) {
                contentViewBig.setTextViewText(R.id.title, notifTitle);
                contentViewSmall.setTextViewText(R.id.title, notifTitle);
            }

            if(notifText!=null && !notifText.isEmpty()) {
                contentViewBig.setTextViewText(R.id.text, notifText);
                contentViewSmall.setTextViewText(R.id.text, notifText);
            }

            Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, 0);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext())
                    .setSmallIcon(R.drawable.welcome3)
                    .setCustomContentView(contentViewSmall)
                    .setCustomBigContentView(contentViewBig)
                    .setContentTitle("Talent School")
                    .setContentIntent(contentIntent)
                    .setAutoCancel(true)
                    .setWhen(when);

            mNotificationManager.notify(1, notificationBuilder.build());

        } catch (Throwable t) {
            Log.d("MYFCMLIST", "Error parsing FCM message", t);
        }

            Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        //The message which i send will have keys named [message, image, AnotherActivity] and corresponding values.
        //You can change as per the requirement.

        //message will contain the Push Message
        String message = remoteMessage.getData().get("body");
        //imageUri will contain URL of the image to be displayed with Notification
        String imageUri = remoteMessage.getData().get("image");
        //If the key AnotherActivity has  value as True then when the user taps on notification, in the app AnotherActivity will be opened.
        //If the key AnotherActivity has  value as False then when the user taps on notification, in the app MainActivity will be opened.
        String TrueOrFlase = remoteMessage.getData().get("AnotherActivity");

        //To get a Bitmap image from the URL received
        bitmap = getBitmapfromUrl(imageUri);





    }






    /**
     * Create and show a simple notification containing the received FCM message.
     */

    private void sendNotification(String messageBody, Bitmap image, String TrueOrFalse) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("AnotherActivity", TrueOrFalse);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setLargeIcon(image)/*Notification icon image*/
                .setSmallIcon(R.drawable.welcome3)
                .setContentTitle(messageBody)
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(image))/*Notification with Image*/
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    /*
    *To get a Bitmap image from the URL received
    * */
    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }

}
