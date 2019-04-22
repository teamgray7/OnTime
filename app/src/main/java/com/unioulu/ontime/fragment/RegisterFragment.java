package com.unioulu.ontime.fragment;

import android.content.Context;
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
import com.unioulu.ontime.helper.StringFormatChecker;

public class RegisterFragment extends Fragment {

    private EditText inputUsername;
    private EditText inputPassword;
    private EditText inputPassword2;
    private EditText inputEmail;
    private Button signupBtn;

    // The interaction listener is defined.
    private OnFragmentInteractionListener mListener;

    public RegisterFragment() {
        // Empty constructor
    }

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_create_account, container, false);

        inputUsername = rootView.findViewById(R.id.inputUsername);
        inputPassword = rootView.findViewById(R.id.inputPassword);
        inputPassword2 = rootView.findViewById(R.id.inputRetypePassword);
        inputEmail = rootView.findViewById(R.id.inputEmail);

        signupBtn = rootView.findViewById(R.id.signUpButton);
        signupBtn.setOnClickListener(signUpClicked);

        return rootView;
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

    private View.OnClickListener signUpClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final String username = inputUsername.getText().toString();
            final String password = inputPassword.getText().toString();
            final String password2 = inputPassword2.getText().toString();
            final String email = inputEmail.getText().toString();

            if(username.equals("") || password.equals("") || password2.equals("") || email.equals("")) {
                String message = "Please fill all the fields..";
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            } else if(!password.equals(password2)) {
                String message = "Passwords do not match.";
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            } else if(password.length() < 7) {
                String message = "Password should contain at least 7 characters.";
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            } else if(!StringFormatChecker.isEmailValid(email)) {
                String message = "Email address does not appear to be valid.";
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            } else {
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
                            DataHolder.getInstance().getAppDatabase().usersTableInterface().createUser(user);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    String message = "User " + username + " is created!" ;
                                    Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                                    mListener.accountCreated();
                                }
                            });
                        } catch (Exception e){
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    String message = "Username or email already exists!";
                                    Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).start();
            }
        }
    };

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
        void accountCreated();
    }
}