package com.unioulu.ontime.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.unioulu.ontime.R;

public class PillTakenFragment extends Fragment {

    // The interaction listener is defined.
    private OnFragmentInteractionListener mListener;
    private Button btnTaken;

    public PillTakenFragment() {
        // Empty constructor
    }

    public static PillTakenFragment newInstance() {
        PillTakenFragment fragment = new PillTakenFragment();
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pills_taken, container, false);

        btnTaken = rootView.findViewById(R.id.okButton);
        btnTaken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.pillTaken();
            }
        });
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
        // Define functions to make snooze and everything...
        void pillTaken();
    }
}