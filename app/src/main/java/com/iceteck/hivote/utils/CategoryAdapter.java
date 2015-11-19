package com.iceteck.hivote.utils;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.iceteck.hivote.R;
import com.iceteck.hivote.data.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CatViewHolder>{

    private Context context;
    private List<Category> cateogryList;

    public static class CatViewHolder extends RecyclerView.ViewHolder {
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

        return new CatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CatViewHolder holder, int position) {
        Category sCategory = cateogryList.get(position);
        String title = sCategory.getTitle().substring(0,1);
        holder.category.setText(sCategory.getTitle());
        holder.description.setText(sCategory.getDescription());
        holder.date.setText(sCategory.getDate());

        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color = generator.getRandomColor();

        TextDrawable drawable = TextDrawable.builder()
                .buildRound(title, color); // radius in px
        holder.categoryImage.setImageDrawable(drawable);
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
