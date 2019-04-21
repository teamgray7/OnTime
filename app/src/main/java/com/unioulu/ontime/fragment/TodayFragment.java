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

import com.unioulu.ontime.R;
import com.unioulu.ontime.RecyclerViewAdapter;
import com.unioulu.ontime.database_classes.AppDatabase;
import com.unioulu.ontime.database_classes.DataHolder;
import com.unioulu.ontime.database_classes.Medicines;

import java.util.ArrayList;
import java.util.List;

public class TodayFragment extends Fragment implements RecyclerViewAdapter.OnItemClickListener {

    private ArrayList<Medicines> pillsMorning = new ArrayList<>();
    private ArrayList<Medicines> pillsAfternoon = new ArrayList<>();
    private ArrayList<Medicines> pillsEvening = new ArrayList<>();

    RecyclerView nextPillsRV;
    RecyclerViewAdapter nextPillsAdapter;

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

        // Clearing so that there won't be duplicate items.
        pillsMorning.clear();
        pillsAfternoon.clear();
        pillsEvening.clear();

        // TODO: on swipe update?, Also when added a pill, not shown...
        dbThread.start();

        nextPillsRV = rootView.findViewById(R.id.rv_pillList);
        nextPillsAdapter = new RecyclerViewAdapter(getContext(), pillsMorning, pillsAfternoon, pillsEvening);

        nextPillsRV.setAdapter(nextPillsAdapter);
        nextPillsRV.setLayoutManager(new LinearLayoutManager(getContext()));
        nextPillsAdapter.setItemClickListener(this);

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

    private Thread dbThread = new Thread(new Runnable() {
        @Override
        public void run() {
            // Creation of appDatabase instance
            final AppDatabase appDatabase = DataHolder.getInstance().getAppDatabase();

            List<String> active_user = appDatabase.usersTableInterface().getActiveUsers(true);
            final int active_user_id = appDatabase.usersTableInterface().getUserIdByName(active_user.get(active_user.size() - 1)); // ID of last active user

            List<String> medsMorning = appDatabase.medicineDBInterface().fetchMorningPills(active_user_id);
            List<String> medsAfternoon = appDatabase.medicineDBInterface().fetchAfternoonPills(active_user_id);
            List<String> medsEvening = appDatabase.medicineDBInterface().fetchEveningPills(active_user_id);

            for(String pillName: medsMorning) {
                Medicines med = appDatabase.medicineDBInterface().fetchOneMedicineByName(pillName, active_user_id);
                pillsMorning.add(med);
            }

            for(String pillName: medsAfternoon) {
                Medicines med = appDatabase.medicineDBInterface().fetchOneMedicineByName(pillName, active_user_id);
                pillsAfternoon.add(med);
            }

            for(String pillName: medsEvening) {
                Medicines med = appDatabase.medicineDBInterface().fetchOneMedicineByName(pillName, active_user_id);
                pillsEvening.add(med);
            }

            nextPillsAdapter.setDataSet(pillsMorning, pillsAfternoon, pillsEvening);
        }
    });

    @Override
    public void onItemClick(String pillName, String pillImage, String pillAmount, int morning, int afternoon, int evening) {
        mListener.viewEditPill(pillName, pillImage, pillAmount, morning, afternoon, evening);
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
        void viewEditPill(String pillName, String pillImage, String pillAmount, int morning, int afternoon, int evening);
    }
}
