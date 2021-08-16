package com.bpal.androidlauncher.Constant;

import android.content.Context;
import android.widget.Toast;

import com.bpal.androidlauncher.Model.AppInfo;

public class Common {
    public static AppInfo current_app;

    public static String currentDesc = "";
    public static String savedDesc = "";

    public static void showToast(Context context, String s) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }
}
