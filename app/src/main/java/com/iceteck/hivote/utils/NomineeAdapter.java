package com.iceteck.hivote.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iceteck.hivote.R;
import com.iceteck.hivote.data.Nominees;
import com.koushikdutta.ion.Ion;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NomineeAdapter extends RecyclerView.Adapter<NomineeAdapter.NomineeViewHolder>{

    private Context context;
    private List<Nominees> nomineeList;

    public static class NomineeViewHolder extends RecyclerView.ViewHolder{
        final ImageView voteImage;
        final TextView nomineeNameTextView;
        final TextView votesTextView;
        final CircleImageView nomineeImage;

        public NomineeViewHolder(View itemView) {
            super(itemView);
            voteImage = (ImageView) itemView.findViewById(R.id.vote_image);
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
        Nominees lnominee = nomineeList.get(position);
        //holder.nomineeImage
        holder.nomineeNameTextView.setText(lnominee.getName());
        holder.votesTextView.setText(""+lnominee.getVotes());
        Ion.with(context)
                .load(lnominee.getBitmap())
                .withBitmap()
                .placeholder(R.drawable.com_facebook_profile_picture_blank_portrait)
                .error(R.mipmap.ic_launcher)
                .animateLoad(android.R.anim.fade_in)
                .animateIn(android.R.anim.fade_out)
                .intoImageView(holder.nomineeImage);

        holder.voteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
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
