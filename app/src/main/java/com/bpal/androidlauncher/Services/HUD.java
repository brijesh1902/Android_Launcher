package com.bpal.androidlauncher.Services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bpal.androidlauncher.Constant.Common;
import com.bpal.androidlauncher.MainActivity;
import com.bpal.androidlauncher.Model.AppInfo;
import com.bpal.androidlauncher.R;
import com.bpal.androidlauncher.SubClass.WindowsAppsActivity;

public class HUD extends Service {

    int LAYOUT_FLAG;
    AppInfo appInfo = Common.current_app;
    View view;
    ImageView max, min, close;
    Button mButton;

    @Override
    public IBinder
    onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        PackageManager packageManager =  getApplicationContext().getPackageManager();
        Intent mapIntent = packageManager.getLaunchIntentForPackage(appInfo.getPackageName().toString());
        mapIntent.addCategory("android.intent.category.LAUNCHER");
        startActivity(mapIntent);
        Log.d("======HUD=====", "WORKING");

        LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = mInflater.inflate(R.layout.view_window, null);

        max = view.findViewById(R.id.app_max);
        min = view.findViewById(R.id.app_min);
        close = view.findViewById(R.id.app_close);

        max.setImageResource(R.drawable.ic_maximize_24);
        min.setImageResource(R.drawable.ic_minimize_24);
        close.setImageResource(R.drawable.ic_close_24);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
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
                Log.d("======MIN=====", "WORKING");
            }
        });

        max.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("======MAX=====", "WORKING");
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP | Gravity.RIGHT;
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        wm.addView(view, params);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mButton != null)
        {
            ((WindowManager) this.getSystemService(WINDOW_SERVICE)).removeView(mButton);
            mButton = null;
        }
    }

}
