package com.unioulu.ontime.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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
        return new ForgetPasswordFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_forget_password, container, false);

        inputUsername = rootView.findViewById(R.id.inputUsername);
        inputEmail = rootView.findViewById(R.id.inputEmail);
        saveMeButton = rootView.findViewById(R.id.saveMeButton);

        saveMeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = inputUsername.getText().toString();
                final String email = inputEmail.getText().toString();

                if(!username.equals("")) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            final UsersTable user = DataHolder.getInstance().getAppDatabase().usersTableInterface().fetchUserByUsername(username);

                            if(user != null) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        String message = "Username: " + user.getUsername() + "\nPassword: " + user.getPassword();
                                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        String message = "There is no user with username: " + username;
                                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }).start();
                } else if(!email.equals("")) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            final UsersTable user = DataHolder.getInstance().getAppDatabase().usersTableInterface().fetchUserByEmail(email);

                            if(user != null) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        String message = "Username: " + user.getUsername() + "\nPassword: " + user.getPassword();
                                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        String message = "There is no user with email: " + email;
                                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }).start();
                } else {
                    Toast.makeText(getContext(), "You didn't specify any username/email..",Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }
}