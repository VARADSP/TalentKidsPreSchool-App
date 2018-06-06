package com.example.varadsp.talentschool_myproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AdminLogin.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AdminLogin#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminLogin extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FirebaseAuth firebaseAuth;

    private ProgressDialog progressDialog;


    EditText editTextUsername, editTextPassword;
    ProgressBar progressBar;
    ArrayAdapter<String> arrayAdapter;
    Spinner cat_spinner;
    String text;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AdminLogin() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminLogin.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminLogin newInstance(String param1, String param2) {
        AdminLogin fragment = new AdminLogin();
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
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_admin_login, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(getActivity());


        YoYo.with(Techniques.Flash).duration(7000).repeat(20).playOn(v.findViewById(R.id.buttonLogin));


        //getSupportActionBar().setTitle("Sign In");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Sign In");


//        if (SharedPrefManager.getInstance(getContext()).isLoggedIn()) {
//            Toast.makeText(getActivity(), SharedPrefManager.getInstance(getContext()).getUser().getCategory() , Toast.LENGTH_SHORT).show();
//
//
//                getActivity().finish();
//                startActivity(new Intent(getActivity(), MainActivity.class));
//
//
//        }

        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        editTextUsername = (EditText) v.findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) v.findViewById(R.id.editTextPassword);


        v.findViewById(R.id.buttonLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });



        return v;
    }


    private void userLogin() {
        Toast.makeText(getActivity(), "Clicked User Login", Toast.LENGTH_SHORT).show();
        //first getting the values
        final String username = editTextUsername.getText().toString();
        final String password = editTextPassword.getText().toString();

        //validating inputs
        if (TextUtils.isEmpty(username)) {
            editTextUsername.setError("Please enter your username");
            editTextUsername.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Please enter your password");
            editTextPassword.requestFocus();
            return;
        }
        //storing the user in shared preferences
//        SharedPrefManager.getInstance(getActivity().getApplicationContext()).userLogin(user);

//        if(username.equals("vsp") && password.equals("vsp")){
//
//            User user = new User( 1,"VSP","parlikarvarad@gmail.com","Male","Admin","8830476866"
//            );
//
//
//            //storing the user in shared preferences
//            SharedPrefManager.getInstance(getActivity()).userLogin(user);
//
//
//
//            getActivity().finish();
//            startActivity(new Intent(getActivity(), AdminActivityDrawer.class));
//
//        }


        progressDialog.setMessage("Loggin In ..");
        progressDialog.show();



        firebaseAuth.signInWithEmailAndPassword(username,password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()){
                            //start the profile activity

                            User user = new User( 1,"VSP","parlikarvarad@gmail.com","Male","Admin","8830476866"
            );


            //storing the user in shared preferences
            SharedPrefManager.getInstance(getActivity()).userLogin(user);
                            firebaseAuth.signInWithCustomToken("1b7ef0d8-3614-43be-b1eb-7da63d4deae6");


                            getActivity().finish();
                            startActivity(new Intent(getActivity(),AdminActivityDrawer.class));
                        }
                    }
                });

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
