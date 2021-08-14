package com.bpal.androidlauncher.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bpal.androidlauncher.R;
import com.bpal.androidlauncher.SubClass.AppsDrawer;


public class HomeFragment extends Fragment {

    ImageView appDrawer;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = null;
        try {
            view = inflater.inflate(R.layout.fragment_home, container, false);

            appDrawer = view.findViewById(R.id.icon_drawer);

            appDrawer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), AppsDrawer.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });

        } catch (Exception e) {}

        return view;
    }
}