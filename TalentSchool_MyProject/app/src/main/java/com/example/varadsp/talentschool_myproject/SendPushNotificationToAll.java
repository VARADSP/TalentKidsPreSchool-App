package com.example.varadsp.talentschool_myproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SendPushNotificationToAll.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SendPushNotificationToAll#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SendPushNotificationToAll extends Fragment {
    String msg="Hello From TalentSchool";


    EditText tokenmsg;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SendPushNotificationToAll() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SendPushNotificationToAll.
     */
    // TODO: Rename and change types and number of parameters
    public static SendPushNotificationToAll newInstance(String param1, String param2) {

        SendPushNotificationToAll fragment = new SendPushNotificationToAll();
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

        tokenmsg=(EditText)getActivity().findViewById(R.id.tokenMessage);

    }

    public void startService(View v){

        Intent intent = new Intent(getContext(),TheService.class);
        getActivity().startService(intent);


    }

    public void stopService(View v){
        Intent intent = new Intent(getContext(),TheService.class);
        getActivity().stopService(intent);


    }

    class HttpTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();



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

        @Override
        protected String doInBackground(String... params) {
            handleSSLHandshake();

            final String[] username = new String[1];
            final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        username[0] = firebaseAuth.getCurrentUser().getDisplayName();
                    }
                }
            });


//            try {
//                RequestQueue requestQueue = Volley.newRequestQueue(getContext());
//                String URL = "https://showmetheshows.000webhostapp.com/sendnotifications.php";
//                JSONObject jsonBody = new JSONObject();
//                jsonBody.put("username",username[0]);
//                jsonBody.put("tokenMessage",msg);
//                Log.e("TokenMessage","TokenMsg = "+msg);
//                jsonBody.put("token", FirebaseInstanceId.getInstance().getToken().toString());
//                final String requestBody = jsonBody.toString();
//
//                StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.i("VOLLEY", response);
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.e("VOLLEY", error.toString());
//                    }
//                }) {
//                    @Override
//                    public String getBodyContentType() {
//                        return "application/json; charset=utf-8";
//                    }
//
//                    @Override
//                    public byte[] getBody() throws AuthFailureError {
//                        try {
//                            return requestBody == null ? null : requestBody.getBytes("utf-8");
//                        } catch (UnsupportedEncodingException uee) {
//                            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
//                            return null;
//                        }
//                    }
//
//                    @Override
//                    protected Response<String> parseNetworkResponse(NetworkResponse response) {
//                        String responseString = "";
//                        Log.e("Response",""+response.data.toString());
//                        if (response != null) {
//                            responseString = String.valueOf(response.statusCode);
//                            // can get more details such as response.headers
//                        }
//                        return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
//                    }
//                };
//
//                requestQueue.add(stringRequest);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
            FirebaseMessagingService firebaseMessagingService = new FirebaseMessagingService();
            firebaseMessagingService.onCreate();

            FirebaseMessaging.getInstance().subscribeToTopic("news");



            String temp = "https://showmetheshows.000webhostapp.com/sendnotifications.php?tokenMessage="+msg;

            temp = temp.replaceAll(" ", "%20");


            StringRequest stringRequest = new StringRequest(Request.Method.GET, temp,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(getActivity(),"Messages Are Away!",Toast.LENGTH_LONG).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getActivity(),"Please Try Again :"+error,Toast.LENGTH_LONG).show();
                        }
                    }){
                @Override
                protected Map<String,String> getParams(){
                    //doesn't work in GET Request
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("tokenMessage",msg);

                    try {
                        FirebaseInstanceId.getInstance().deleteInstanceId();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    params.put("token",FirebaseInstanceId.getInstance().getToken());

                    return params;
                }

            };

            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(stringRequest);



            Log.e("In DoInBack","I am in");
//            try {
//                Log.e("Trying ","I am in try block");
//
//                String login_url = "https://showmetheshows.000webhostapp.com/sendnotifications.php";
//                URL url = new URL(login_url);
//
//                Log.e("Url",""+url);
//
//
//                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//
//                Log.e("After Conn","Connection Opened"+httpURLConnection);
//                httpURLConnection.setRequestMethod("GET");
////                httpURLConnection.getRequestMethod();
//
//                httpURLConnection.setInstanceFollowRedirects(false);
//
//                httpURLConnection.setRequestProperty("Host", "https://showmetheshows.000webhostapp.com");
//
//                httpURLConnection.setRequestProperty("Accept-Charset", "UTF-8");
//
//
//                httpURLConnection.setConnectTimeout (5000) ;
//                httpURLConnection.setDoOutput(true);
//                httpURLConnection.setDoInput(true);
//                Log.e("RequestMethod",""+httpURLConnection.getRequestMethod());
//
//
//
//                Log.e("Connect1","Connected");
//
//
//                Log.e("Error1",""+httpURLConnection.getOutputStream());
//
//
//                OutputStream outputStream = httpURLConnection.getOutputStream();
//
//                Log.e("Connect2","Connected");
//
//                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
//
//                String post_data = URLEncoder.encode("send_notification","UTF-8")+"&"+URLEncoder.encode("token","UTF-8")+"="+URLEncoder.encode(FirebaseInstanceId.getInstance().getToken().toString(),"UTF-8");
//
//
//
//                Log.e("Post_data",""+post_data);
//
//                bufferedWriter.write(post_data);
//
//                Log.e("Connect3","Connected");
//                Log.e("URL",""+bufferedWriter.toString());
//
//                bufferedWriter.flush();
//                bufferedWriter.close();
//
//                outputStream.close();
//                httpURLConnection.connect();
//
//
//                Log.e("Connect","Connected");
//
//                InputStream inputStream = httpURLConnection.getInputStream();
//                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
//
//                String result = "" ;
//
//                String line ="";
//
//
//                while((line = bufferedReader.readLine())!= null){
//
//                    Log.e("response",""+line);
//                    result += line;
//
//                }
//                Log.e("Result",""+result);
//
//
//                bufferedReader.close();
//                inputStream.close();
//                httpURLConnection.disconnect();
//
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }









            return null;
        }
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_send_push_notification_to_all, container, false);

        tokenmsg = (EditText)v.findViewById(R.id.tokenMessage);

        Button notify = (Button)v.findViewById(R.id.sendNotification);

        notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//
//                EditText tokenId = (EditText)v.findViewById(R.id.tokenid);
//
//
//
//               tokenId.setText(FirebaseInstanceId.getInstance().getToken().toString());
                try {
                    Log.e("Deleting Id","Instance Id Is Deleted");
                    FirebaseInstanceId.getInstance().deleteInstanceId();
                } catch (IOException e) {
                    e.printStackTrace();
                }




//                Log.e("Token","Token="+FirebaseInstanceId.getInstance().getToken().toString());





                if(tokenmsg.getText() != null){

                    msg = tokenmsg.getText().toString().trim();
                    Log.e("MSG",""+msg);


                }
                        //performe the deskred task
                        HttpTask asnyc = new HttpTask();
                        asnyc.execute();

//                Log.e("My Unique Id",""+generateUniqueId());
//                Toast.makeText(getActivity(), ""+generateUniqueId(), Toast.LENGTH_SHORT).show();


                String type = "login";

//                startService(v);

//                BackgroundWorker backgroundWorker = new BackgroundWorker(getContext());
//
//
//                backgroundWorker.execute(type,"VSP","VSP");



//                MessagesClientFCMServer message = new MessagesClientFCMServer(getContext());
//                message.sendDataNotification();
                Toast.makeText(getActivity(), "Sending ...", Toast.LENGTH_SHORT).show();

            }
        });

        return v;

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
