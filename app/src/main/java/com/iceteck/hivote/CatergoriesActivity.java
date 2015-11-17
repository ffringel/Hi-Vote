package com.iceteck.hivote;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iceteck.hivote.utils.AccountSessionManager;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class CatergoriesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SharedPreferences sessionSp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catergories);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        sessionSp = PreferenceManager.getDefaultSharedPreferences(this);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //configure views for the navigation Drawer
        TextView nameTextview = (TextView) findViewById(R.id.nameTextView);
        TextView emailtextView = (TextView)findViewById(R.id.emailTextView);
        nameTextview.setText(sessionSp.getString(AccountSessionManager.USERNAME,"google"));
        emailtextView.setText(sessionSp.getString(AccountSessionManager.USEREMAIL, ""));
        CircleImageView profileImage = (CircleImageView) findViewById(R.id.profileImageView);
        LinearLayout drawerLinearLayout  = (LinearLayout) findViewById(R.id.linearLayout);
        drawerLinearLayout.getBackground();

        Picasso.with(this)
                .load(getIntent().getStringExtra("image"))
                .resize(150, 150)
                .placeholder(R.drawable.ic_action_user)
                .error(R.mipmap.ic_launcher)
                .noFade()
                .into(profileImage);
        /*Picasso.with(this)
                .load(getIntent().getStringExtra("cover"))
                .resize(100, 100)
                .placeholder(R.drawable.ic_action_user)
                .error(R.mipmap.ic_launcher)
                .into(profileImage);*/
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
