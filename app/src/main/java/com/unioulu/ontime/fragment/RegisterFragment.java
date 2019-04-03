package com.unioulu.ontime.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.unioulu.ontime.R;
import com.unioulu.ontime.database_classes.DataHolder;
import com.unioulu.ontime.database_classes.UsersTable;

public class RegisterFragment extends Fragment {



    private EditText inputUsername;
    private EditText inputPassword;
    private EditText inputEmail;
    private Button signupBtn;

    public RegisterFragment() {
        // Empty constructor
    }

    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_create_account, container, false);



        inputUsername = rootView.findViewById(R.id.inputUsername);
        inputPassword = rootView.findViewById(R.id.inputPassword);
        inputEmail = rootView.findViewById(R.id.inputEmail);
        signupBtn = rootView.findViewById(R.id.signUpButton);

        signupBtn.setOnClickListener(signUpClicked);


        return rootView;
    }


    private View.OnClickListener signUpClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // final String username = etUsername.getText().toString();
            // final String password = etPassword.getText().toString();
            final  String username = inputUsername.getText().toString();
            final  String password = inputPassword.getText().toString();
            final  String email = inputEmail.getText().toString();

            // TODO: implement a mechanist to save user to database
            new Thread(new Runnable() {
                @Override
                public void run() {

                    UsersTable user = new UsersTable(
                            username,
                            email,
                            password,
                            true
                    );
                    try {
                        DataHolder.getInstance().getAppDatabase().usersTableInterface().createUser(user); Log.d("Logging fragment", "User added !");
                    }catch (Exception e){

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity().getApplicationContext(), "Email already exists !", Toast.LENGTH_SHORT).show();
                            }
                        });
                        Log.d("Logging fragment", "Email already exists !");
                    }

                }
            }).start();
        }
    };
}