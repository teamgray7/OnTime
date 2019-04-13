package com.unioulu.ontime.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unioulu.ontime.R;
import com.unioulu.ontime.RecyclerViewAdapter;

import java.util.ArrayList;

public class TodayFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private ArrayList<String> mNamesNext = new ArrayList<>();
    private ArrayList<String> mImageUrlsNext= new ArrayList<>();

    public TodayFragment() {
        // Empty constructor
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static TodayFragment newInstance(int sectionNumber) {
        TodayFragment fragment = new TodayFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_today, container, false);

        initImageBitmaps();

        RecyclerView nextPillsRV = rootView.findViewById(R.id.rv_pillList);
        RecyclerViewAdapter nextPillsAdapter = new RecyclerViewAdapter(getContext(), mNamesNext, mImageUrlsNext);
        nextPillsRV.setAdapter(nextPillsAdapter);
        nextPillsRV.setLayoutManager(new LinearLayoutManager(getContext()));

        return rootView;
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
}
