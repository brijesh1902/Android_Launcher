package com.bpal.androidlauncher.Services;

import android.app.ActivityOptions;
import android.app.PictureInPictureParams;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.display.DisplayManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.util.Rational;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;

import com.bpal.androidlauncher.Constant.Common;
import com.bpal.androidlauncher.Database.DBTask;
import com.bpal.androidlauncher.MainActivity;
import com.bpal.androidlauncher.Model.AppInfo;
import com.bpal.androidlauncher.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;

public class WindowView extends Service {

    TextView app_name;
    private CardView max, min, close, cardView;
    AppInfo appInfo = Common.current_app;
    Display targetDisplay;
    WindowManager windowManager;
    View view;
    PackageManager packageManager;
    DBTask dbTask ;
    int LAYOUT_FLAG;
    boolean exist;
    ArrayList<AppInfo> list = Common.tb_list;
    private WindowView mApplication;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        dbTask = new DBTask(getBaseContext());

        DisplayManager dm = (DisplayManager) getSystemService(Context.DISPLAY_SERVICE);
        targetDisplay = dm.getDisplay(Display.DEFAULT_DISPLAY);

        Context displayContext = getApplicationContext().createDisplayContext(targetDisplay);
        windowManager = (WindowManager) displayContext.getSystemService(Context.WINDOW_SERVICE);

        //Get the WindowManager of display and LayoutInflater through displayContext
        LayoutInflater li = (LayoutInflater) displayContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //Set up Layout and WindowParams
        view = li.inflate(R.layout.view_window, null);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }

        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR;
        params.type = LAYOUT_FLAG;
        params.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        params.gravity = Gravity.TOP;

        //Add view in WindowManager
        windowManager.addView(view, params);

        max = view.findViewById(R.id.app_max);
        min = view.findViewById(R.id.app_min);
        close = view.findViewById(R.id.app_close);
        app_name = view.findViewById(R.id.app_name);
        cardView = view.findViewById(R.id.cardView);

        app_name.setText(appInfo.getLabel());

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                cardView.setVisibility(View.GONE);
                dbTask.deletetask(appInfo.getLabel().toString());
                stopSelf();
                //((WindowManager) getApplicationContext().getSystemService(Service.WINDOW_SERVICE)).removeView(view);
                Common.showToast(getApplicationContext(), "App Closed.");
                Log.d("======CLOSE=====", "WORKING");
                appInfo = null;
            }
        });

        min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exist = dbTask.findTask(appInfo.getLabel().toString());
                if (exist == false) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    stopSelf();
                    cardView.setVisibility(View.GONE);
                    dbTask.addtotask(appInfo.getLabel().toString(), appInfo.getIcon(), appInfo.getPackageName().toString());
                } else {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    stopSelf();
                    cardView.setVisibility(View.GONE);
                }
                Common.showToast(getApplicationContext(), "App Minimised.");
                Log.d("======MIN=====", appInfo.getLabel().toString() + "==" + appInfo.getIcon() + "==" + appInfo.getPackageName().toString());
            }
        });

        max.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Display d = windowManager.getDefaultDisplay();
                Point point = new Point();
                d.getSize(point);
                int width = point.x;
                int height = point.y;

                /*if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    Rational rational = new Rational(width, height);
                    PictureInPictureParams.Builder pip = new PictureInPictureParams.Builder();
                    pip.setAspectRatio(rational).build();
                    this.enterPictureInPictureMode(pip.build());
                    /*PictureInPictureParams params = new PictureInPictureParams.Builder()
                            .setAspectRatio(rational)
                            .build();
                    getApplicationContext().setPictureInPictureParams(params);*/
                //}
                Common.showToast(getApplicationContext(), "App Maximised.");
                Log.d("======MAX=====", "WORKING");
            }
        });

    }

}
