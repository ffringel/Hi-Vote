package com.iceteck.hivote;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.iceteck.hivote.data.Nominees;
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
    private static String CATEGORY_NOMINEE_ID ;
    private static Context nContext;
    private static String path;
    private SwipeRefreshLayout mRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nominee);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nomineeRecycler = (RecyclerView) findViewById(R.id.nomineeRecyclerview);
        emptyTextView = (TextView) findViewById(R.id.nomineeTextViewEmpty);
        nomineeRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNominees(getIntent().getStringExtra("categoryid"));
            }
        });

        nomineesList = new ArrayList<>();
        nContext = NomineeActivity.this;
        CATEGORY_NOMINEE_ID = getIntent().getStringExtra("categoryid");

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
            CATEGORY_NOMINEE_ID = getIntent().getStringExtra("categoryid");
            getNominees(getIntent().getStringExtra("categoryid"));
            System.out.println(getIntent().getStringExtra("categoryid"));
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
                                            "",
                                            cobject.get("nominee_bitmap").getAsString(),
                                            cobject.get("nominee_votes").getAsLong(),
                                            cobject.get("cat__nom_id").getAsString()));
                                }
                                if(nomineesList.isEmpty()){
                                    setEmptyView(true, getResources().getString(R.string.emptyNomineeList));
                                }

                                nomineeAdapter = new NomineeAdapter(NomineeActivity.this, nomineesList);
                                nomineeRecycler.setAdapter(nomineeAdapter);

                                nomineeRecycler.swapAdapter(nomineeAdapter, true);

                                mRefreshLayout.setRefreshing(false);
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
    private static void addNominee(String name, String desc, File profileImage) throws Exception{
        Ion.with(nContext)
                .load(BASEURL + "addnominee/" + CATEGORY_NOMINEE_ID)
                .uploadProgressBar(new ProgressBar(nContext))
                .setMultipartParameter("name", name)
                .setMultipartParameter("bitmap", profileImage.getName())
                .setMultipartParameter("portfolio", desc)
                .setMultipartParameter("url", " ")
                .setMultipartFile("profile", "application/*", profileImage)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        // do stuff with the result or error
                        //System.out.println(e.getMessage());
                        if(e == null)
                        try {
                            //String resultStatus = result.get("status").getAsString();
                            //System.out.println("Status: "+resultStatus);
                            System.out.println(result);
                        } catch (Exception e1) {
                            e1.printStackTrace();
//                            setEmptyView(true, "");
                        }
                        else{
                            System.out.println(e);
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

        private static int RESULT_LOAD_IMAGE = 1;

        EditText nomineeName;
        EditText nomineeDesc;
        ImageView nomProfileImage;
        Button addImage;
        View nomineeView;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            LayoutInflater inflater = getActivity().getLayoutInflater();
            nomineeView = inflater.inflate(R.layout.add_nominee_dialog, null);

            nomineeName = (EditText) nomineeView.findViewById(R.id.nominee_name);
            nomineeDesc = (EditText) nomineeView.findViewById(R.id.description);
            nomProfileImage = (ImageView) nomineeView.findViewById(R.id.nom_image);
            addImage = (Button) nomineeView.findViewById(R.id.addImage);
            addImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, RESULT_LOAD_IMAGE);
                }
            });

            builder.setView(nomineeView)
                    .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            try {
                                addNominee(nomineeName.getText().toString(), nomineeDesc.getText().toString(), new File(path));
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(getActivity(),"Failed to add nominee. Please try gain.", Toast.LENGTH_LONG).show();
                                AddNominee.this.getDialog().cancel();
                            }
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

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
                Uri selectedImage = data.getData();

                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

                path = picturePath;
                nomProfileImage = (ImageView) nomineeView.findViewById(R.id.nom_image);
                nomProfileImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }
    }
}
}
