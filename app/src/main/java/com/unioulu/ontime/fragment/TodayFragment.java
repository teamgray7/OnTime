package com.unioulu.ontime.fragment;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.unioulu.ontime.AlarmActivity;
import com.unioulu.ontime.R;
import com.unioulu.ontime.RecyclerViewAdapter;

import java.util.ArrayList;

public class TodayFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private ArrayList<String> mNamesNext = new ArrayList<>();
    private ArrayList<String> mImageUrlsNext= new ArrayList<>();
    private ArrayList<String> mNamesPrevious = new ArrayList<>();
    private ArrayList<String> mImageUrlsPrevious= new ArrayList<>();

    private TextView nextPills;
    private TextView previousPills;

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

        RecyclerView nextPillsRV = rootView.findViewById(R.id.NextPillRecyclerView);
        RecyclerView alreadyTakenPillsRV = rootView.findViewById(R.id.AlreadyTakenRecyclerView);

        initImageBitmaps();

        RecyclerViewAdapter nextPillsAdapter = new RecyclerViewAdapter(getContext(), mNamesNext, mImageUrlsNext);
        RecyclerViewAdapter alreadyTakenPillsAdapter = new RecyclerViewAdapter(getContext(), mNamesPrevious, mImageUrlsPrevious);

        nextPillsRV.setAdapter(nextPillsAdapter);
        alreadyTakenPillsRV.setAdapter(alreadyTakenPillsAdapter);

        nextPillsRV.setLayoutManager(new LinearLayoutManager(getContext()));
        alreadyTakenPillsRV.setLayoutManager(new LinearLayoutManager(getContext()));

        nextPills = (TextView) rootView.findViewById(R.id.nextPillsTextView);
        previousPills = (TextView) rootView.findViewById(R.id.alreadyTakenPillsTextView);
        nextPills.setPaintFlags(nextPills.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        previousPills.setPaintFlags(previousPills.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        // Should be removed ! Only implemented to make alarm activity accessible !
        nextPills.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(v.getContext(), AlarmActivity.class);
                startActivity(intent);
                return true;
            }
        });

        return rootView;
    }

    private void initImageBitmaps(){
        // Clearing so that there won't be duplicate items.
        mImageUrlsNext.clear();
        mNamesNext.clear();
        mImageUrlsPrevious.clear();
        mNamesPrevious.clear();

        mImageUrlsNext.add("https://i.ibb.co/f00N4B3/20190303-224419.jpg");
        mNamesNext.add("Sinecod 50mg - 1 pill");

        mImageUrlsNext.add("https://i.ibb.co/sbWmw5Y/20190303-224425.jpg");
        mNamesNext.add("Minoset plus - 1 pill");

        mImageUrlsNext.add("https://i.ibb.co/LCMXjwk/20190303-224431.jpg");
        mNamesNext.add("Katarin - 0.5 pill");

        mImageUrlsPrevious.add("https://i.ibb.co/LCMXjwk/20190303-224431.jpg");
        mNamesPrevious.add("Katarin - 0.5 pill - 08:00");

        mImageUrlsPrevious.add("https://i.ibb.co/6ZZj4cJ/20190303-224445.jpg");
        mNamesPrevious.add("Croxilex - 1 pill - 08:00");
    }
}
