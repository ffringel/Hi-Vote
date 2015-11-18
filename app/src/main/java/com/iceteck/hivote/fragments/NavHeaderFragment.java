package com.iceteck.hivote.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iceteck.hivote.R;
import com.iceteck.hivote.utils.SessionManager;

import java.util.HashMap;

public class NavHeaderFragment extends Fragment {


    public NavHeaderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.nav_header_catergories, container, false);

        SessionManager session = new SessionManager(getActivity().getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();

        String name = user.get(SessionManager.KEY_NAME);
        String email = user.get(SessionManager.KEY_EMAIL);
        String profilePicture = user.get(SessionManager.KEY_PICTURE);

        TextView userName = (TextView) rootView.findViewById(R.id.nameTextView);
        TextView userEmail = (TextView) rootView.findViewById(R.id.emailTextView);

        try {
            userName.setText(name);
            userEmail.setText(email);

        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }

        session.checkLogin();

        return rootView;
    }


}
