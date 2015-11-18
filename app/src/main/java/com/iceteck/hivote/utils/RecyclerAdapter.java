package com.iceteck.hivote.utils;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iceteck.hivote.R;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.CatViewHolder>{

    public static class CatViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView category;
        TextView description;
        TextView date;
        ImageView categoryImage;

        public CatViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.categoryview);
            category = (TextView) itemView.findViewById(R.id.category_name);
            description = (TextView) itemView.findViewById(R.id.description);
            date = (TextView) itemView.findViewById(R.id.date);
            categoryImage = (ImageView) itemView.findViewById(R.id.category_image);
        }
    }

    @Override
    public CatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_catergories, parent, false);
        CatViewHolder viewHolder = new CatViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CatViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
