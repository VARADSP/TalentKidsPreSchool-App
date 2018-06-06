package com.example.varadsp.talentschool_myproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;

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
import java.util.Random;
import java.util.UUID;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class welcome_activity extends AppCompatActivity {
    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private Button btnSkip, btnNext;
    private PrefManager prefManager;

    Random rand = new Random();
    Integer id = rand.nextInt(9000000) + 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_activity);
        // Checking for first time launch - before calling setContentView()
        prefManager = new PrefManager(this);








        if (!prefManager.isFirstTimeLaunch()) {
            launchHomeScreen();
            finish();
        }
        else{

            Toast.makeText(this, "Registering With Firebase", Toast.LENGTH_SHORT).show();
            HttpTask asnyc = new HttpTask();
            asnyc.execute();




        }

        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        setContentView(R.layout.activity_welcome_activity);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        btnSkip = (Button) findViewById(R.id.btn_skip);
        btnNext = (Button) findViewById(R.id.btn_next);


        // layouts of all welcome sliders
        // add few more layouts if you want
        layouts = new int[]{
                R.layout.welcome1,
                R.layout.welcome2,
                R.layout.welcome3};

        // adding bottom dots
        addBottomDots(0);

        // making notification bar transparent
        changeStatusBarColor();

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);



        ConnectivityManager conMgr = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        if ( conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED ) {

            // user is online
            //getSupportActionBar().setTitle("Sign In");



        }
        else if ( conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED) {

            // notify user you are not online

            AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
            builder.setMessage("Please Connect to Internet")
                    .setCancelable(false)
                    .setPositiveButton("Connect to Internet", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                        }
                    });

            AlertDialog alert = builder.create();
            alert.show();



            //getSupportActionBar().setTitle("Sign In");
//            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Please Turn On Internet");


        }


        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchHomeScreen();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking for last page
                // if last page home screen will be launched
                int current = getItem(+1);
                if (current < layouts.length) {
                    // move to next screen
                    viewPager.setCurrentItem(current);
                } else {
                    launchHomeScreen();

                    //Add Token inserting COde Here
//                    String HttpUrl = "https://showmetheshows.000webhostapp.com/inserttoken.php";
//                    // Creating Volley RequestQueue.
//                    RequestQueue requestQueue;
//                    // Creating Volley newRequestQueue .
//                    requestQueue = Volley.newRequestQueue(welcome_activity.this);
//
//
//
//                    // Creating string request with post method.
//                    StringRequest stringRequest = new StringRequest(Request.Method.POST, HttpUrl,
//                            new Response.Listener<String>() {
//                                @Override
//                                public void onResponse(String ServerResponse) {
//
//
//
//                                    // Showing response message coming from server.
//                                    Toast.makeText(welcome_activity.this, ServerResponse, Toast.LENGTH_LONG).show();
//                                }
//                            },
//                            new Response.ErrorListener() {
//                                @Override
//                                public void onErrorResponse(VolleyError volleyError) {
//
//
//                                    // Showing error message if something goes wrong.
//                                    Toast.makeText(welcome_activity.this, volleyError.toString(), Toast.LENGTH_LONG).show();
//                                }
//                            }) {
//                        @Override
//                        protected Map<String, String> getParams() {
//
//                            // Creating Map String Params.
//                            Map<String, String> params = new HashMap<String, String>();
//
//                            // Adding All values to Params.
//                            params.put("username", FirebaseAuth.getInstance().getCurrentUser().toString());
//                            params.put("token", FirebaseInstanceId.getInstance().getToken().toString());
//
//
//
//                            return params;
//                        }
//
//                    };
//
//
//
//                    // Creating RequestQueue.
//                    RequestQueue requestQueue1 = Volley.newRequestQueue(welcome_activity.this);
//
//                    // Adding the StringRequest object into requestQueue.
//                    requestQueue1.add(stringRequest);

                    handleSSLHandshake();

                    final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(welcome_activity.this, "Signed In", Toast.LENGTH_SHORT).show();
                    }
                }
            });


                    try {
                        FirebaseInstanceId.getInstance().deleteInstanceId();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://showmetheshows.000webhostapp.com/inserttoken.php?id="+id+"&"+"username=user"+"&"+"token="+FirebaseInstanceId.getInstance().getToken(),
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Toast.makeText(welcome_activity.this,"Getting Ready ",Toast.LENGTH_LONG).show();
                                    Log.e("Response",""+response);

                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(welcome_activity.this,"Error Please Try Restarting App "+error.toString(),Toast.LENGTH_LONG).show();
                                    Log.e("Error ",""+error);
                                }
                            }){
                        @Override
                        protected Map<String,String> getParams(){
                            Map<String,String> params = new HashMap<String, String>();
                            params.put("username","user");




                            params.put("token",FirebaseInstanceId.getInstance().getToken());
                            params.put("id", String.valueOf(id));

                            Log.e("ID",""+id);










                            return params;
                        }

                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(welcome_activity.this);
                    requestQueue.add(stringRequest);

                    //creating a new user object

                    User user = new User(id,"User","parlikarvarad@gmail.com","Unknown","NormalUser","8830476866"
                    );


                    //storing the user in shared preferences
                    SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);








            }


            }
        });

    }


//    /**
//     * Gnereate unique ID from UUID in positive space
//     * @return long value representing UUID
//     */
//    private Integer generateUniqueId()
//    {
//        Integer val = -1;
//        do
//        {
//            final UUID uid = UUID.randomUUID();
//            final ByteBuffer buffer = ByteBuffer.wrap(new byte[16]);
//            buffer.putInt((int) uid.getLeastSignificantBits());
//            buffer.putInt((int) uid.getMostSignificantBits());
//            final BigInteger bi = new BigInteger(buffer.array());
//            val = bi.intValue();
//        } while (val < 0);
//        return val;
//    }

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


    class HttpTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }



        @Override
        protected String doInBackground(String... params) {


//                      handleSSLHandshake();
//
//            final String[] username = new String[1];
//            final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//            firebaseAuth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                @Override
//                public void onComplete(@NonNull Task<AuthResult> task) {
//                    if(task.isSuccessful()){
//                        username[0] = firebaseAuth.getCurrentUser().getDisplayName().toString();
//                    }
//                }
//            });
//
//
//
//            try {
//                RequestQueue requestQueue = Volley.newRequestQueue(welcome_activity.this);
//                String URL = "https://showmetheshows.000webhostapp.com/inserttoken.php";
//                JSONObject jsonBody = new JSONObject();
//                jsonBody.put("username","User"+username[0]);
//                jsonBody.put("token", "fdikbFKNpwM:APA91bE1Mbbgz7E40-llaVCigjgNwF1YHwITpddnDxlNZuZ9kKamZJsM8VxHZtSWGdT6n8Ly1TMsSX-NKs0FNCeWjQ6d1fiGIUOJ7d-cLta98sY1H0qj5mQcnInBdYXDEP277uXaEjiN");
//                final String requestBody = jsonBody.toString();
//
//                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
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
//



//            try {
//                String login_url = "https://showmetheshows.000webhostapp.com/inserttoken.php";
//                URL url = new URL(login_url);
//
//
//                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//
//                httpURLConnection.setRequestMethod("POST");
//                httpURLConnection.setDoOutput(true);
//                httpURLConnection.setDoInput(true);
//
//                OutputStream outputStream = httpURLConnection.getOutputStream();
//
//                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
//
//                String post_data = URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(FirebaseAuth.getInstance().getCurrentUser().toString(),"UTF-8")+"&"+URLEncoder.encode("token","UTF-8")+"="+URLEncoder.encode(FirebaseInstanceId.getInstance().getToken().toString(),"UTF-8");
//
//
//                Log.e("Post_data in welcome",""+post_data);
//
//                bufferedWriter.write(post_data);
//
//                Log.e("URL",""+bufferedWriter.toString());
//
//                bufferedWriter.flush();
//                bufferedWriter.close();
//
//                outputStream.close();
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
//
//
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



    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];


        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    private void launchHomeScreen() {
        prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(welcome_activity.this, SplashScreen.class));
        finish();
    }

    //	viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.length - 1) {
                // last page. make button text to GOT IT

                btnNext.setText(getString(R.string.start));
                btnSkip.setVisibility(View.GONE);
            } else {
                // still pages are left
                btnNext.setText(getString(R.string.next));
                btnSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    /**
     * Making notification bar transparent
     */
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }


    /**
     * View pager adapter
     */
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}
