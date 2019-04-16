package com.unioulu.ontime.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.unioulu.ontime.R;
import com.unioulu.ontime.RecyclerViewAdapter;

import java.util.ArrayList;

public class TodayFragment extends Fragment implements RecyclerViewAdapter.OnItemClickListener {

    private ArrayList<String> mNamesNext = new ArrayList<>();
    private ArrayList<String> mImageUrlsNext= new ArrayList<>();

    // The interaction listener is defined.
    private OnFragmentInteractionListener mListener;

    public TodayFragment() {
        // Empty constructor
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static TodayFragment newInstance() {
        return new TodayFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_today, container, false);

        initImageBitmaps();

        RecyclerView nextPillsRV = rootView.findViewById(R.id.rv_pillList);
        RecyclerViewAdapter nextPillsAdapter = new RecyclerViewAdapter(getContext(), mNamesNext, mImageUrlsNext);
        nextPillsRV.setAdapter(nextPillsAdapter);
        nextPillsRV.setLayoutManager(new LinearLayoutManager(getContext()));
        nextPillsAdapter.setItemClickListener(this);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof AddPillScreenFragment.OnFragmentInteractionListener) {
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

    private void initImageBitmaps(){
        // Clearing so that there won't be duplicate items.
        mImageUrlsNext.clear();
        mNamesNext.clear();

        mImageUrlsNext.add("https://i.ibb.co/f00N4B3/20190303-224419.jpg");
        mNamesNext.add("Sinecod 50mg - 1 pill");

        mImageUrlsNext.add("https://i.ibb.co/sbWmw5Y/20190303-224425.jpg");
        mNamesNext.add("Minoset plus - 1 pill");

        mImageUrlsNext.add("https://i.ibb.co/LCMXjwk/20190303-224431.jpg");
        mNamesNext.add("Katarin - 0.5 pill");
    }

    @Override
    public void onItemClick(String pillName) {
        Toast.makeText(getContext(), "hello", Toast.LENGTH_LONG).show();
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
        void viewEditPill();
    }
}
