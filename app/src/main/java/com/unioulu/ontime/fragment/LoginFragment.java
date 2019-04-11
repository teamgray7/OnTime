package com.unioulu.ontime.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.unioulu.ontime.R;
import com.unioulu.ontime.database_classes.DataHolder;
import com.unioulu.ontime.database_classes.UsersTable;

public class LoginFragment extends Fragment {

    private TextView tvRegister;
    private TextView tvForgetPassword;
    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;

    // The interaction listener is defined.
    private OnFragmentInteractionListener mListener;

    public LoginFragment() {
        // Empty constructor
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        tvRegister = (TextView) rootView.findViewById(R.id.tv_registerAccount);
        tvForgetPassword = (TextView) rootView.findViewById(R.id.tv_forgetPassword);
        etUsername = (EditText) rootView.findViewById(R.id.etUsername);
        etPassword = (EditText) rootView.findViewById(R.id.etPassword);
        btnLogin = (Button) rootView.findViewById(R.id.btnLogin);

        tvRegister.setOnClickListener(registerClickListener);
        tvForgetPassword.setOnClickListener(forgetPasswordClickListener);
        btnLogin.setOnClickListener(btnLoginClickListener);
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

    private View.OnClickListener btnLoginClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final String username = etUsername.getText().toString();
            final String password = etPassword.getText().toString();

            // user : DataHolder.getInstance().getAppDatabase().usersTableInterface()...

            new Thread(new Runnable() {
                @Override
                public void run() {


                    final UsersTable user = DataHolder.getInstance().getAppDatabase().usersTableInterface().fetchUserByUsernameAndPassword(username,password);

                    if(user!=null){
                        DataHolder.getInstance().setUsername(username);
                        DataHolder.getInstance().setUser_id(DataHolder.getInstance().getAppDatabase().usersTableInterface().getUserIdByName(username));

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {mListener.proceedLogin(username, password);

                            }
                        });
                    } else {

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity().getApplicationContext(), "User doesn't exist !", Toast.LENGTH_SHORT).show();
                            }
                        });
                        Log.d("Logging fragment", "User doesn't exist !");
                    }

                }
            }).start();
        }
    };

    private View.OnClickListener registerClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // final String username = etUsername.getText().toString();
            // final String password = etPassword.getText().toString();
            mListener.registerAccountClicked();
        }
    };

    private View.OnClickListener forgetPasswordClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO: implement a forget password mechanism
            mListener.forgetPasswordClicked();
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
        void forgetPasswordClicked();
        void registerAccountClicked();
        void proceedLogin(String username, String password);
    }
}