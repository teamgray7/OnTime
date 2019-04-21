package com.unioulu.ontime;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.unioulu.ontime.database_classes.Medicines;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<Medicines> mPillsMorning;
    private ArrayList<Medicines> mPillsAfternoon;
    private ArrayList<Medicines> mPillsEvening;
    private Context mContext;

    private int sizeOfMorning;
    private int sizeOfAfternoon;
    private int sizeOfEvening;

    private OnItemClickListener mListener;

    public RecyclerViewAdapter(Context mContext, ArrayList<Medicines> mPillsMorning, ArrayList<Medicines> mPillsAfternoon, ArrayList<Medicines> mPillsEvening) {
        this.mContext = mContext;
        this.mPillsMorning = mPillsMorning;
        this.mPillsAfternoon = mPillsAfternoon;
        this.mPillsEvening = mPillsEvening;
        this.sizeOfMorning = mPillsMorning.size();
        this.sizeOfAfternoon = mPillsAfternoon.size();
        this.sizeOfEvening = mPillsEvening.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int index) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int index) {
        Medicines med;
        String text;

        if(index + 1 <= sizeOfMorning) {
            med = mPillsMorning.get(index);
            text = med.getMedicine_name() + " - " + med.getMedicine_amount() + " pills" + " (Morning)";
        } else if(index + 1 > sizeOfMorning && index + 1 <= sizeOfMorning + sizeOfAfternoon) {
            med = mPillsAfternoon.get(index - sizeOfMorning);
            text = med.getMedicine_name() + " - " + med.getMedicine_amount() + " pills" + " (Afternoon)";
        } else {
            med = mPillsEvening.get(index - sizeOfMorning - sizeOfAfternoon);
            text = med.getMedicine_name() + " - " + med.getMedicine_amount() + " pills" + " (Evening)";
        }

        viewHolder.recyclerImageName.setText(text);
        viewHolder.recyclerLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(index + 1 <= sizeOfMorning) {
                    mListener.onItemClick(mPillsMorning.get(index).getMedicine_name(),
                            mPillsMorning.get(index).getPicture_path(),
                            mPillsMorning.get(index).getMedicine_amount(),
                            mPillsMorning.get(index).getMorningAt(),
                            mPillsMorning.get(index).getAfternoonAt(),
                            mPillsMorning.get(index).getEveringAt());
                } else if(index + 1 > sizeOfMorning && index + 1 <= sizeOfMorning + sizeOfAfternoon) {
                    mListener.onItemClick(mPillsAfternoon.get(index - sizeOfMorning).getMedicine_name(),
                            mPillsAfternoon.get(index - sizeOfMorning).getPicture_path(),
                            mPillsAfternoon.get(index - sizeOfMorning).getMedicine_amount(),
                            mPillsAfternoon.get(index - sizeOfMorning).getMorningAt(),
                            mPillsAfternoon.get(index - sizeOfMorning).getAfternoonAt(),
                            mPillsAfternoon.get(index - sizeOfMorning).getEveringAt());
                } else {
                    mListener.onItemClick(mPillsEvening.get(index - sizeOfMorning - sizeOfAfternoon).getMedicine_name(),
                            mPillsEvening.get(index - sizeOfMorning - sizeOfAfternoon).getPicture_path(),
                            mPillsEvening.get(index - sizeOfMorning - sizeOfAfternoon).getMedicine_amount(),
                            mPillsEvening.get(index - sizeOfMorning - sizeOfAfternoon).getMorningAt(),
                            mPillsEvening.get(index - sizeOfMorning - sizeOfAfternoon).getAfternoonAt(),
                            mPillsEvening.get(index - sizeOfMorning - sizeOfAfternoon).getEveringAt());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.sizeOfMorning + this.sizeOfAfternoon + this.sizeOfEvening; // How many items are in the list !
    }

    public void setItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public void setDataSet(ArrayList<Medicines> newMorning, ArrayList<Medicines> newAfternoon, ArrayList<Medicines> newEvening) {
        this.mPillsMorning = newMorning;
        this.mPillsAfternoon = newAfternoon;
        this.mPillsEvening = newEvening;
        this.sizeOfMorning = newMorning.size();
        this.sizeOfAfternoon = newAfternoon.size();
        this.sizeOfEvening = newEvening.size();

        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView recyclerImage;
        TextView recyclerImageName;
        RelativeLayout recyclerLayout;

        ViewHolder(View itemView){
            super(itemView);

            recyclerImage = (CircleImageView) itemView.findViewById(R.id.recyclerImage);
            recyclerImageName = (TextView) itemView.findViewById(R.id.recyclerText);
            recyclerLayout = (RelativeLayout) itemView.findViewById(R.id.recyclerLayout);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String pillName, String pillImage, String pillAmount, int morning, int afternoon, int evening);
    }
}
