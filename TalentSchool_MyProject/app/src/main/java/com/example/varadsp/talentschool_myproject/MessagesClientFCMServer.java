package com.example.varadsp.talentschool_myproject;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

/**
 * Created by VARAD on 20-04-2018.
 */

public class MessagesClientFCMServer {
    private static Context mContext;

    public MessagesClientFCMServer(Context mContext) {
        this.mContext = mContext;
    }

    private static final Logger log = Logger.getLogger(MessagesClientFCMServer.class
            .getName());

    private static String SCOPE = "https://www.googleapis.com/auth/firebase.messaging";
    private static String FCM_DEALS_ENDPOINT
            = "https://fcm.googleapis.com/v1/projects/talentschoolproject/messages:send";

    public static void main(String args[]) {
        MessagesClientFCMServer fcmClient = new MessagesClientFCMServer(mContext);
        fcmClient.sendNotification();
        fcmClient.sendData();
    }

    private void sendNotification(){
        String notificationTitle = "Latest Deals";
        String notificationBody = "View latest deals from top brands.";

        sendMessageToFcm(getFcmMessageJSONNotification(notificationTitle, notificationBody));
    }
    private void sendData(){
        sendMessageToFcm("HI");
    }
    public void sendDataNotification(){
        String notificationTitle = "Latest Deals";
        String notificationBody = "View latest deals from top brands.";
        sendMessageToFcm("HI");
    }

    //Using HttpURLConnection it send http post request containing data to FCM server
    private void sendMessageToFcm(String postData) {
        try {

            HttpURLConnection httpConn = getConnection();
            httpConn.setDoOutput(true);
            httpConn.setUseCaches(false);
            httpConn.setRequestMethod("POST");

            DataOutputStream wr = new DataOutputStream(httpConn.getOutputStream());
            wr.writeBytes(postData);
            wr.flush();
            wr.close();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(httpConn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            log.info(response.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getAccessToken() throws IOException {

        AssetManager assetManager = mContext.getAssets();

        AssetFileDescriptor assetFileDescriptor = assetManager.openFd("adminsdk.json");
        FileDescriptor fileDescriptor = assetFileDescriptor.getFileDescriptor();
        FileInputStream stream = new FileInputStream(fileDescriptor);


        GoogleCredential googleCredential = GoogleCredential
                .fromStream(stream)
                .createScoped(Arrays.asList(SCOPE));
        googleCredential.refreshToken();
        String token = googleCredential.getAccessToken();
        return token;
    }

    //create HttpURLConnection setting Authorization token
    //and Content-Type header
    private HttpURLConnection getConnection() throws Exception {
        URL url = new URL(FCM_DEALS_ENDPOINT);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestProperty("Authorization", "Bearer " + getAccessToken());
        httpURLConnection.setRequestProperty("Content-Type", "application/json; UTF-8");
        return httpURLConnection;
    }





    private String getFcmMessageJSONNotification(String title, String msg) {
        JsonObject notifiDetails = new JsonObject();
        notifiDetails.addProperty("body", msg);
        notifiDetails.addProperty("title", title);

        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("topic", "deals");
        jsonObj.add("notification", notifiDetails);

        JsonObject msgObj = new JsonObject();
        msgObj.add("message", jsonObj);

        log.info("json  message "+msgObj.toString());

        return msgObj.toString();
    }


}