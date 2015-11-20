package com.iceteck.hivote.utils;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iceteck.hivote.R;
import com.iceteck.hivote.data.Category;
import com.iceteck.hivote.data.Nominees;

import java.util.List;

public class NomineeAdapter extends RecyclerView.Adapter<NomineeAdapter.CatViewHolder>{

    private Context context;
    private List<Nominees> nomineeList;

    public static class CatViewHolder extends RecyclerView.ViewHolder{
        final CardView cardView;
        final TextView category;
        final TextView description;
        final TextView date;
        final ImageView categoryImage;

        public CatViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.categoryview);
            category = (TextView) itemView.findViewById(R.id.category_name);
            description = (TextView) itemView.findViewById(R.id.description);
            date = (TextView) itemView.findViewById(R.id.date);
            categoryImage = (ImageView) itemView.findViewById(R.id.category_image);
        }
    }

    public NomineeAdapter(Context ctx, List<Nominees> clist){
        context = ctx;
        nomineeList = clist;
    }
    @Override
    public CatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_catergories, parent, false);
        CatViewHolder viewHolder = new CatViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CatViewHolder holder, int position) {
        //TODO
    }

    @Override
    public int getItemCount() {
        return nomineeList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
