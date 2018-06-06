package com.example.varadsp.talentschool_myproject;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.firebase.iid.FirebaseInstanceId;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by VARAD on 20-04-2018.
 */

public class TheService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {





        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {

                try {
                    String login_url = "http://192.168.152.2/firebase/api.php";
                    URL url = new URL(login_url);


                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);

                    OutputStream outputStream = httpURLConnection.getOutputStream();

                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                    String post_data = URLEncoder.encode("send_notification","UTF-8")+"&"+URLEncoder.encode("token","UTF-8")+"="+URLEncoder.encode(FirebaseInstanceId.getInstance().getToken().toString(),"UTF-8");

                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();

                    outputStream.close();

                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));

                    String result = "" ;

                    String line ="";


                    while((line = bufferedReader.readLine())!= null){

                        result += line;

                    }

                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                }
        }, 10000);


        return START_STICKY;


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
