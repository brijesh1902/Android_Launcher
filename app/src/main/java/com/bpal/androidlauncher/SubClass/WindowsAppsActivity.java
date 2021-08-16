package com.bpal.androidlauncher.SubClass;

import static com.bpal.androidlauncher.Fragments.AppViewFragment.getScreenHeight;
import static com.bpal.androidlauncher.Fragments.AppViewFragment.getScreenWidth;
import static com.bpal.androidlauncher.R.layout.view_window;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.display.DisplayManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bpal.androidlauncher.Constant.Common;
import com.bpal.androidlauncher.Fragments.AppViewFragment;
import com.bpal.androidlauncher.MainActivity;
import com.bpal.androidlauncher.Model.AppInfo;
import com.bpal.androidlauncher.R;
import com.bpal.androidlauncher.Services.HUD;

public class WindowsAppsActivity extends AppCompatActivity {

    TextView app_name;
    CardView max, min, close;
    FrameLayout frameLayout;
    AppInfo appInfo = Common.current_app;
    public final static int REQUEST_CODE = -1010101;
    ActivityOptions mOptions;
    Display targetDisplay;
    WindowManager windowManager;
    View view;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1234) {
            startService(new Intent(this, HUD.class));
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //updateWindowSize();
    }

    @Override
    public void onMultiWindowModeChanged(boolean isInMultiWindowMode) {
        super.onMultiWindowModeChanged(isInMultiWindowMode);

        if (isInMultiWindowMode) {
            // Activity has entered multi-window mode
        } else {
            // Activity has exited multi-window mode
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_windows_apps);

        DisplayManager dm = (DisplayManager) getSystemService(Context.DISPLAY_SERVICE);
        targetDisplay = dm.getDisplay(Display.DEFAULT_DISPLAY);

        Context displayContext = getApplicationContext().createDisplayContext(targetDisplay);
        windowManager = (WindowManager) displayContext.getSystemService(Context.WINDOW_SERVICE);

//Get the WindowManager of display and LayoutInflater through displayContext
        LayoutInflater li = (LayoutInflater) displayContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

//Set up Layout and WindowParams
        view = li.inflate(view_window, null, false);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR;
        params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        params.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        params.gravity = Gravity.TOP;
//Add view in WindowManager
        windowManager.addView(view, params);

        /*DisplayMetrics displaymetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displaymetrics);
        int width = (int) ((int)displaymetrics.widthPixels * 0.9);
        int height = (int) ((int)displaymetrics.heightPixels * 0.9);*/

        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x - 20;  // Set your heights
        int height = size.y - 80;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            PackageManager packageManager = getApplicationContext().getPackageManager();
            Intent i = packageManager.getLaunchIntentForPackage(appInfo.getPackageName().toString());
            Rect rect = new Rect(100, 800, 900, 700);
            ActivityOptions options = ActivityOptions.makeBasic();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                ActivityOptions bounds = options.setLaunchDisplayId(targetDisplay.getDisplayId());
                options = options.setLaunchBounds(rect);

                //startActivity(i, bounds.toBundle());
                startActivity(i, options.toBundle());
            }
            //getSupportFragmentManager().beginTransaction().add(R.id.container, new AppViewFragment()).commit();
        }


        max = view.findViewById(R.id.app_max);
        min = view.findViewById(R.id.app_min);
        close = view.findViewById(R.id.app_close);
        app_name = view.findViewById(R.id.app_name);

        app_name.setText(appInfo.getLabel());

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                windowManager.removeView(view);
                Common.showToast(getApplicationContext(), "App Closed.");
                Log.d("======CLOSE=====", "WORKING");
                appInfo = null;
            }
        });

        min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                Common.showToast(getApplicationContext(), "App Minimised.");
                Log.d("======MIN=====", "WORKING");
            }
        });

        max.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    PackageManager packageManager = getApplicationContext().getPackageManager();
                    Intent i = packageManager.getLaunchIntentForPackage(Common.current_app.getPackageName().toString());
                    Rect rect = new Rect(100, 800, 900, 700);
                    ActivityOptions options = ActivityOptions.makeBasic();
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        ActivityOptions bounds = options.setLaunchDisplayId(targetDisplay.getDisplayId());
                        startActivity(i, bounds.toBundle());
                    }
                    Common.showToast(getApplicationContext(), "App Maximised.");
                    //startActivity(i);
                    //getSupportFragmentManager().beginTransaction().add(R.id.container, new AppViewFragment()).commit();
                }
                Log.d("======MAX=====", "WORKING");
            }
        });

    }
}