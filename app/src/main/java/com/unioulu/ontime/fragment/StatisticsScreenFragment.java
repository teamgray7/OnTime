package com.unioulu.ontime.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.unioulu.ontime.R;

import java.util.ArrayList;

public class StatisticsScreenFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private PieChart pieChart;

    public StatisticsScreenFragment() {
        // Empty constructor
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static StatisticsScreenFragment newInstance(int sectionNumber) {
        StatisticsScreenFragment fragment = new StatisticsScreenFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_statistics, container, false);
        setPieChart(rootView);

        return rootView;
    }

    /*
        This functions set statistical data and assign them into a pie chart.
        TODO : Retrieve data from database and place them into pieEntries arrayList.
     */
    private void setPieChart(View rootView) {
        ArrayList<PieEntry> pieEntries = new ArrayList<PieEntry>();
        pieEntries.add(new PieEntry(81f, "Success"));
        pieEntries.add(new PieEntry(18f, "Danger"));
        pieEntries.add(new PieEntry(1f, "Fail"));

        PieDataSet dataSet = new PieDataSet(pieEntries, "Statistics of medicine");
        PieData data = new PieData(dataSet);

        pieChart = (PieChart) rootView.findViewById(R.id.pie_chart);
        pieChart.setData(data);

        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieChart.animateXY(2000,2000);
    }
}