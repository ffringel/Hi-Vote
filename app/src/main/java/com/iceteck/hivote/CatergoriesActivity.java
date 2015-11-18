package com.iceteck.hivote;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.iceteck.hivote.data.Category;
import com.iceteck.hivote.utils.CategoryAdapter;
import com.iceteck.hivote.utils.SessionManager;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CatergoriesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private SessionManager session;
    private CategoryAdapter mCategoryAdapter;
    private List<Category> mCategoryList;
    public static final String BASEURL = "http://hivote.iceteck.com/index.php/home/";
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catergories);

        session = new SessionManager(getApplicationContext());
        mCategoryList = new ArrayList<Category>();
        initRecyclerView();
        initToolbar();
        initFab();
        setupDrawerLayout();

        session.checkLogin();

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void initRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initFab() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void setupDrawerLayout() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_catergories);

        //View header = navigationView.getHeaderView(0);
        HashMap<String, String> user = session.getUserDetails();

        String name = user.get(SessionManager.KEY_NAME);
        String email = user.get(SessionManager.KEY_EMAIL);
        String profilePicture = user.get(SessionManager.KEY_PICTURE);

        TextView userName = (TextView) headerLayout.findViewById(R.id.nameTextView);
        TextView userEmail = (TextView) headerLayout.findViewById(R.id.emailTextView);
        CircleImageView imageView = (CircleImageView) headerLayout.findViewById(R.id.profileImageView);

        Toast.makeText(getApplicationContext(), name, Toast.LENGTH_LONG).show();
        Picasso.with(this)
                .load(profilePicture)
                .resize(150, 150)
                .into(imageView);

        userName.setText(name);
        userEmail.setText(email);

    }

    private void setupAdapter(){
        Ion.with(this)
                .load(BASEURL+"fetchcategories")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        JsonArray categories = result.getAsJsonArray();
                        for(int i=0; i<categories.size(); i++){
                            JsonObject cobject = (JsonObject) categories.get(i);
                            mCategoryList.add(new Category(cobject.get("category_id").getAsString(),
                                    cobject.get("category_title").getAsString(),
                                    cobject.get("category_status").getAsString().equals("1")?true:false,
                                    cobject.get("category_url").getAsString(),
                                    cobject.get("category_description").getAsString(),
                                    cobject.get("category_date").getAsString()));
                        }
                        mCategoryAdapter = new CategoryAdapter(CatergoriesActivity.this, mCategoryList);
                        mRecyclerView.setAdapter(mCategoryAdapter);
                    }
                });

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_manage) {
            // Handle the camera action
        }  else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_logout) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
