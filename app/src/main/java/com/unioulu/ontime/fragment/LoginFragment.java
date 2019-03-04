package com.unioulu.ontime.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.unioulu.ontime.R;

public class LoginFragment extends Fragment {

    private TextView tvRegister;
    private TextView tvForgetPassword;

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

        tvRegister.setOnClickListener(registerClickListener);
        tvForgetPassword.setOnClickListener(forgetPasswordClickListener);

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

    private View.OnClickListener registerClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mListener.registerAccountClicked();
        }
    };

    private View.OnClickListener forgetPasswordClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
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
    }
}