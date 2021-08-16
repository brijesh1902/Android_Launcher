package com.bpal.androidlauncher.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Insets;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.WindowMetrics;
import android.widget.TextView;

import com.bpal.androidlauncher.Constant.Common;
import com.bpal.androidlauncher.Model.AppInfo;
import com.bpal.androidlauncher.R;

import java.lang.reflect.Method;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = null;
        try {
            view = inflater.inflate(R.layout.fragment_app_view, container, false);

            WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics metrics = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(metrics);

            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x - 20;  // Set your heights
            int height = size.y - 80;

            Log.d("Window size", "(" + metrics.widthPixels + ", " + metrics.heightPixels + ")");

            PackageManager packageManager =  getContext().getPackageManager();
            Intent i = packageManager.getLaunchIntentForPackage(appInfo.getPackageName().toString());
            i.addFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT |
                    Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

            /*DisplayMetrics displayMetrics = new DisplayMetrics();
            Rect mBounds = new Rect(300, 0, getScreenWidth(getActivity()), getScreenHeight(getActivity()));
            mOptions = getActivityOptions(MainActivity.this);
            mOptions = mOptions.setLaunchBounds(mBounds);

            startActivity(i, mOptions.toBundle());

            i = new Intent(this, SplitMainActivity.class);
            mBounds = new Rect(0, 0, 300, getScreenHeight(getActivity()));
            mOptions = getActivityOptions(MainActivity.this);
            mOptions = mOptions.setLaunchBounds(mBounds);*/

            startActivity(i);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public static ActivityOptions getActivityOptions(Context context) {
        ActivityOptions options = ActivityOptions.makeBasic();
        int freeform_stackId = 5;
        try {
            Method method = ActivityOptions.class.getMethod("setLaunchWindowingMode", int.class);
            method.invoke(options, freeform_stackId);
        } catch (Exception e) { /* Gracefully fail */
        }

        return options;
    }

    public static int getScreenWidth(@NonNull Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowMetrics windowMetrics = activity.getWindowManager().getCurrentWindowMetrics();
            Insets insets = windowMetrics.getWindowInsets()
                    .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars());
            return windowMetrics.getBounds().width() - insets.left - insets.right;
        } else {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            return displayMetrics.widthPixels;
        }
    }

    public static int getScreenHeight(@NonNull Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowMetrics windowMetrics = activity.getWindowManager().getCurrentWindowMetrics();
            Insets insets = windowMetrics.getWindowInsets()
                    .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars());
            return windowMetrics.getBounds().height() - insets.top - insets.bottom;
        } else {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            return displayMetrics.heightPixels;
        }
    }

}