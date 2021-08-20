package com.bpal.androidlauncher.Constant;

import android.content.Context;
import android.widget.Toast;

import com.bpal.androidlauncher.Model.AppInfo;

import java.util.ArrayList;

public class Common {
    public static AppInfo current_app;

    // The EditText String will be
    // stored in this variable
    // when MINIMIZE or MAXIMIZE
    // button is pressed
    public static String currentDesc = "";
    
    public static ArrayList<AppInfo> tb_list;

    public static void showToast(Context context, String s) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }
}
