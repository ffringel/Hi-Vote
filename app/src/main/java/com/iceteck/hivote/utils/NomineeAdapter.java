package com.iceteck.hivote.utils;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.iceteck.hivote.R;
import com.iceteck.hivote.data.Category;
import com.iceteck.hivote.data.Nominees;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NomineeAdapter extends RecyclerView.Adapter<NomineeAdapter.NomineeViewHolder>{

    private Context context;
    private List<Nominees> nomineeList;

    public static class NomineeViewHolder extends RecyclerView.ViewHolder{
        final ImageButton voteButton;
        final TextView nomineeNameTextView;
        final TextView votesTextView;
        final CircleImageView nomineeImage;

        public NomineeViewHolder(View itemView) {
            super(itemView);
            voteButton = (ImageButton) itemView.findViewById(R.id.vote_image);
            nomineeNameTextView = (TextView) itemView.findViewById(R.id.nominee_name);
            votesTextView = (TextView) itemView.findViewById(R.id.votes_num);
            nomineeImage = (CircleImageView) itemView.findViewById(R.id.nom_image);
        }
    }

    public NomineeAdapter(Context ctx, List<Nominees> clist){
        context = ctx;
        nomineeList = clist;
    }

    @Override
    public NomineeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nominee_feed_recycler, parent, false);
        NomineeViewHolder viewHolder = new NomineeViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NomineeViewHolder holder, int position) {
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
