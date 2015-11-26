package com.iceteck.hivote.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.iceteck.hivote.NomineeActivity;
import com.iceteck.hivote.R;
import com.iceteck.hivote.data.Nominees;
import com.koushikdutta.async.future.FutureCallback;
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
    public void onBindViewHolder(final NomineeViewHolder holder, int position) {
        //TODO
        final Nominees lnominee = nomineeList.get(position);
        //holder.nomineeImage
        holder.nomineeNameTextView.setText(lnominee.getName());
        holder.votesTextView.setText(""+lnominee.getVotes());
        System.out.println(lnominee.getBitmap().replaceAll("\\\\",""));
        Ion.with(context)
                .load(lnominee.getBitmap().replaceAll("\\\\",""))
                .withBitmap()
                .placeholder(R.drawable.com_facebook_profile_picture_blank_portrait)
                .error(R.drawable.com_facebook_profile_picture_blank_portrait)
                .animateLoad(android.R.anim.fade_in)
                .animateIn(android.R.anim.fade_out)
                .intoImageView(holder.nomineeImage);

        holder.voteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    SessionManager msession = new SessionManager(context);
                    Ion.with(context)
                            .load(NomineeActivity.BASEURL + "votenominee/" + lnominee.getId())
                            .setBodyParameter("email", msession.getUserDetails().get(SessionManager.KEY_EMAIL))
                            .setBodyParameter("number", " ")
                            .setBodyParameter("name", msession.getUserDetails().get(SessionManager.KEY_NAME))
                            .setBodyParameter("category",lnominee.getCategoryId())
                            .setBodyParameter("voter_id",msession.getUserDetails().get(SessionManager.KEY_EMAIL))
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {
                                    if (e == null) {
                                        try {
                                            if (result.get("status").getAsString().equals("200")) {
                                                Toast.makeText(context, "Voted successfully", Toast.LENGTH_SHORT).show();
                                                holder.votesTextView.setText(String.format("%d", lnominee.getVotes() + 1));
                                            } else
                                                Toast.makeText(context, "Voting error occurred", Toast.LENGTH_SHORT).show();
                                        } catch (Exception e1) {
                                            e1.printStackTrace();
                                        }
                                    } else {
                                        System.out.println(e.getMessage());
                                        Toast.makeText(context, "Error voting. Please try again", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    Toast.makeText(context, context.getResources().getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                }
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
