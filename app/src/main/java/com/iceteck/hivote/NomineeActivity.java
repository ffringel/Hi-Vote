package com.iceteck.hivote;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.iceteck.hivote.data.Category;
import com.iceteck.hivote.data.Nominees;
import com.iceteck.hivote.utils.CategoryAdapter;
import com.iceteck.hivote.utils.NomineeAdapter;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class NomineeActivity extends AppCompatActivity {

    private TextView emptyTextView;
    private RecyclerView nomineeRecycler;
    private List<Nominees> nomineesList;
    public static final String BASEURL = "http://hivote.iceteck.com/index.php/home/";
    private NomineeAdapter nomineeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nominee);
        nomineeRecycler = (RecyclerView) findViewById(R.id.nomineeRecyclerview);
        emptyTextView = (TextView) findViewById(R.id.nomineeTextViewEmpty);
        nomineeRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        nomineesList = new ArrayList<Nominees>();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNominee addNominee = new AddNominee();
                addNominee.show(getSupportFragmentManager(), "Add Nominee");
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra("categorytitle"));

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isNetworkconnected()){
            getNominees(getIntent().getStringExtra("categoryid"));
        }else{
            setEmptyView(true, getResources().getString(R.string.connection_error));
        }
    }
    //retrieve nominee list
    private void getNominees(String nomineeCategoryID){
        try {
            Ion.with(this)
                    .load(BASEURL + "nominees/"+nomineeCategoryID)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            // do stuff with the result or error
                            //System.out.println(e.getMessage());
                            try {
                                JsonArray nominees = result.getAsJsonArray("nominees");
                                nomineesList.clear();
                                for (int i = 0; i < nominees.size(); i++) {
                                    JsonObject cobject = (JsonObject) nominees.get(i);
                                    nomineesList.add(new Nominees(cobject.get("nominee_id").getAsLong(),
                                            cobject.get("nominee_name").getAsString(),
                                            cobject.get("nominee_portfolio").getAsString(),
                                            cobject.get("nominee_url").getAsString(),
                                            cobject.get("nominee_bitmap").getAsString(),
                                            cobject.get("nominee_votes").getAsLong()));
                                }
                                if(nomineesList.isEmpty()){
                                    setEmptyView(true, getResources().getString(R.string.emptyNomineeList));
                                }

                                nomineeAdapter = new NomineeAdapter(NomineeActivity.this, nomineesList);
                                nomineeRecycler.setAdapter(nomineeAdapter);

                                nomineeRecycler.swapAdapter(nomineeAdapter, true);

                            } catch (Exception e1) {
                                e1.printStackTrace();
                                setEmptyView(true, "");
                            }
                        }
                    });
        } catch (NullPointerException e) {
            e.printStackTrace();
            setEmptyView(true, "");
        }
    }
    //check network connection
    private boolean isNetworkconnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return connectivityManager.getActiveNetworkInfo().isAvailable() &&
                connectivityManager.getActiveNetworkInfo().isConnected();
    }

    //set the empty view with an appropriate message
    private void setEmptyView(boolean show, String message){
        if(show){
            emptyTextView.setVisibility(View.VISIBLE);
            emptyTextView.setText(message);
        }else{
            emptyTextView.setVisibility(View.GONE);
        }

    }

    //add a new nominee to an existing category
    private void addNominee(String nomineeCategoryID, String name, String desc, File profileImage) throws Exception{
        Ion.with(NomineeActivity.this)
                .load(BASEURL + "addnominee+/" + nomineeCategoryID)
                .uploadProgressBar(new ProgressBar(NomineeActivity.this))
                .setMultipartParameter("nominee_name", name)
                .setMultipartParameter("nominee_bitmap", profileImage.getName())
                .setMultipartParameter("nominee_portfolio", desc)
                .setMultipartParameter("nominee_url", " ")
                .setMultipartFile("profile", "application/*", profileImage)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        //System.out.println(e.getMessage());
                        try {
                            String resultStatus = result.get("status").getAsString();

                        } catch (Exception e1) {
                            e1.printStackTrace();
//                            setEmptyView(true, "");
                        }
                    }
                });

/*        Ion.with(NomineeActivity.this)
                .load(BASEURL + "addnominee+/"+nomineeCategoryID)
                .setBodyParameter("nominee_name", name)
                .setBodyParameter("nominee_bitmap", profileImage.getName())
                .setBodyParameter("nominee_portfolio", desc)
                .setBodyParameter("nominee_url", "")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        //System.out.println(e.getMessage());
                        try {
                            JsonArray categories = result.getAsJsonArray("categories");

                            setEmptyView(false, "");
                        } catch (Exception e1) {
                            e1.printStackTrace();
                            setEmptyView(true, "");
                        }
                    }
                });*/
    }

    public static class AddNominee extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            LayoutInflater inflater = getActivity().getLayoutInflater();

            builder.setView(inflater.inflate(R.layout.add_category_dialog, null))
                    .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            AddNominee.this.getDialog().cancel();
                        }
                    });
            return builder.create();
        }
    }
}
