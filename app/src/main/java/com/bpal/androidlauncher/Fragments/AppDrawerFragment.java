package com.bpal.androidlauncher.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bpal.androidlauncher.R;

public class AppDrawerFragment extends Fragment {


    public AppDrawerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = null;
        try {
            view =  inflater.inflate(R.layout.fragment_app_drawer, container, false);


        } catch (Exception e) {}

        return view;
    }
}