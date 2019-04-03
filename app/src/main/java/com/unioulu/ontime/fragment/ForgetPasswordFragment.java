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

public class ForgetPasswordFragment extends Fragment {

    private EditText inputUsername;
    private EditText inputEmail;
    private Button saveMeButton;

    public ForgetPasswordFragment() {
        // Empty constructor
    }

    public static ForgetPasswordFragment newInstance() {
        ForgetPasswordFragment fragment = new ForgetPasswordFragment();
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_forget_password, container, false);

        inputUsername=rootView.findViewById(R.id.inputUsername);
        inputEmail=rootView.findViewById(R.id.inputEmail);
        saveMeButton=rootView.findViewById(R.id.saveMeButton);

        saveMeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = inputEmail.getText().toString();

                new Thread(new Runnable() {
                    @Override
                    public void run() {


                            final UsersTable user = DataHolder.getInstance().getAppDatabase().usersTableInterface().fetchUserByEmail(email);

                            if(user!=null){
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity().getApplicationContext(), "Your username " + user.getUsername() + " and password" + user.getPassword(), Toast.LENGTH_SHORT).show();
                                }
                            });
                            } else {

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity().getApplicationContext(), "Email doesn't exist !", Toast.LENGTH_SHORT).show();
                                }
                            });
                            Log.d("Logging fragment", "Email already exists !");
                        }

                    }
                }).start();
            }
        });

        return rootView;
    }
}