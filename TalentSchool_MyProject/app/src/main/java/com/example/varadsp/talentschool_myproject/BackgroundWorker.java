package com.example.varadsp.talentschool_myproject;

/**
 * Created by VARAD on 20-04-2018.
 */


import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

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

public class BackgroundWorker extends AsyncTask<String,Void,String> {

    Context context;

    AlertDialog alertDialog ;


    BackgroundWorker(Context ctx){
        context = ctx;

    }

    @Override
    protected String doInBackground(String... voids) {



        int iLanguage = 0;
        TextView lbl;
        Typeface arabicFont = null;
        int TIMEOUT_MILLISEC = 10000; // = 10 seconds



        String type  = voids[0];

        String login_url = voids[1];



        if(type.equalsIgnoreCase("login")){

            try {
                URL url = new URL(login_url);

                String user_name = voids[1];

                String password = voids[2];



                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();

                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                String post_data = URLEncoder.encode("send_notification","UTF-8")+"&"+URLEncoder.encode("token","UTF-8")+"="+URLEncoder.encode(FirebaseInstanceId.getInstance().getToken().toString(),"UTF-8");


                Log.e("Final Url",""+post_data);


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


                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return null;
    }

    @Override
    protected void onPreExecute(){






    }

    @Override
    protected void onPostExecute(String result){




    }

    protected void onProgressUpdate(Void... values ){

        super.onProgressUpdate(values);
    }

}
