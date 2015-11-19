package com.iceteck.hivote.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.iceteck.hivote.R;
import com.iceteck.hivote.data.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CatViewHolder>{

    private Context context;
    private List<Category> cateogryList;

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

    public CategoryAdapter(Context ctx, List<Category> clist){
        context = ctx;
        cateogryList = clist;
    }
    @Override
    public CatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_recycler, parent, false);
        CatViewHolder viewHolder = new CatViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CatViewHolder holder, int position) {
        //TODO
        Category sCateory = cateogryList.get(position);
        String title = sCateory.getTitle().substring(0,1);
        holder.category.setText(sCateory.getTitle());
        holder.description.setText(sCateory.getDescription());
        holder.date.setText(sCateory.getDate());
        TextDrawable drawable1 = TextDrawable.builder()
                .buildRoundRect(title, Color.RED, 150); // radius in px
        holder.categoryImage.setImageDrawable(drawable1);
    }

    @Override
    public int getItemCount() {
        return cateogryList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
