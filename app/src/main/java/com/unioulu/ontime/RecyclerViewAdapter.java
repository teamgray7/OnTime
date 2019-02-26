package com.unioulu.ontime;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends  RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<String> mImageNames = new ArrayList<>();
    private ArrayList<String> mImage= new ArrayList<>();
    private Context mContext;

    // Constructor
    public RecyclerViewAdapter(Context mContext, ArrayList<String> mImageNames, ArrayList<String> mImage) {
        this.mImageNames = mImageNames;
        this.mImage = mImage;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_list_item, viewGroup, false);
        // Class is defined at the bottom of  this class
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        Log.d(TAG, "onBindViewHolder called");

        // Getting the images
        Glide.with(mContext)
                .asBitmap()
                .load(mImage.get(i))
                .into(viewHolder.recyclerImage);

        viewHolder.recyclerImageName.setText(mImageNames.get(i));

        viewHolder.recyclerLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d(TAG, "On click: clocked on: " + mImageNames.get(i));

                Toast.makeText(mContext, mImageNames.get(i), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mImageNames.size(); // How many items are in the list !
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        // Recycler view items
        CircleImageView recyclerImage;
        TextView recyclerImageName;
        RelativeLayout recyclerLayout;


        public ViewHolder(View itemView){
            super(itemView);

            recyclerImage = (CircleImageView) itemView.findViewById(R.id.recyclerImage);
            recyclerImageName = (TextView) itemView.findViewById(R.id.recyclerText);
            recyclerLayout = (RelativeLayout) itemView.findViewById(R.id.recyclerLayout);



        }
    }
}
