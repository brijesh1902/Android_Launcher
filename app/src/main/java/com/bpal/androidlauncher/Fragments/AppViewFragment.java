package com.bpal.androidlauncher.Fragments;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.bpal.androidlauncher.Constant.Common;
import com.bpal.androidlauncher.Model.AppInfo;
import com.bpal.androidlauncher.R;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class AppViewFragment extends Fragment {

    AppInfo appInfo = Common.current_app;
    TextView app_name, test;
    private AlertDialog dialog;

    public AppViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = null;
        try {
            view = inflater.inflate(R.layout.fragment_app_view, container, false);

            PackageManager packageManager =  container.getContext().getPackageManager();
            Intent mapIntent = packageManager.getLaunchIntentForPackage(appInfo.getPackageName().toString());
            mapIntent.addCategory("android.intent.category.LAUNCHER");
            startActivity(mapIntent);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

}