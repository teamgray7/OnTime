package com.unioulu.ontime.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.unioulu.ontime.R;

import java.util.ArrayList;

public class StatisticsScreenFragment extends Fragment {

    public StatisticsScreenFragment() {
        // Empty constructor
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static StatisticsScreenFragment newInstance() {
        return new StatisticsScreenFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_statistics, container, false);
        setPieChart(rootView);
        setDate(rootView);

        return rootView;
    }

    private void setPieChart(View rootView) {
        ArrayList<PieEntry> pieEntries = new ArrayList<PieEntry>();
        pieEntries.add(new PieEntry(32f, "Success"));
        pieEntries.add(new PieEntry(13f, "Delayed"));
        pieEntries.add(new PieEntry(8f, "Not taken"));

        PieDataSet dataSet = new PieDataSet(pieEntries, "");
        PieData data = new PieData(dataSet);

        PieChart pieChart = (PieChart) rootView.findViewById(R.id.pie_chart);
        pieChart.setData(data);
        pieChart.animateXY(1300,1300);

        Description description = new Description();
        description.setText("Statistics");
        pieChart.setDescription(description);

        dataSet.setColors(getResources().getColor(R.color.colorGreen),
                getResources().getColor(R.color.colorOrange),
                getResources().getColor(R.color.colorRed));
        dataSet.setValueTextSize(18f);
    }

    private void setDate(View rootView) {
        String date = "May, 2019";

        TextView tvDate = (TextView) rootView.findViewById(R.id.pie_tvDate);
        tvDate.setText(date);
    }
}