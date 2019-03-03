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
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<String> mImageNames;
    private ArrayList<String> mImage;
    private Context mContext;

    public RecyclerViewAdapter(Context mContext, ArrayList<String> mImageNames, ArrayList<String> mImage) {
        this.mImageNames = mImageNames;
        this.mImage = mImage;
        this.mContext = mContext;
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
        viewHolder.recyclerImageName.setText(mImageNames.get(index));
        viewHolder.recyclerLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, mImageNames.get(index), Toast.LENGTH_SHORT).show();
            }
        });

        // Getting the images
        Glide.with(mContext)
                .asBitmap()
                .load(mImage.get(index))
                .into(viewHolder.recyclerImage);
    }

    @Override
    public int getItemCount() {
        return mImageNames.size(); // How many items are in the list !
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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
