package com.bpal.androidlauncher.SubClass;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ActivityOptions;
import android.app.AppOpsManager;
import android.app.PictureInPictureParams;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.display.DisplayManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.util.Log;
import android.util.Rational;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bpal.androidlauncher.Constant.Common;
import com.bpal.androidlauncher.Database.DBTask;
import com.bpal.androidlauncher.MainActivity;
import com.bpal.androidlauncher.Model.AppInfo;
import com.bpal.androidlauncher.R;
import com.bpal.androidlauncher.Services.WindowView;

import java.util.ArrayList;

public class WindowsAppsActivity extends AppCompatActivity {

    TextView app_name;
    private CardView max, min, close, cardView;
    FrameLayout frameLayout;
    AppInfo appInfo = Common.current_app;
    public final static int REQUEST_CODE = 101;
    ActivityOptions mOptions;
    Display targetDisplay;
    WindowManager windowManager;
    View view;
    PackageManager packageManager;
    ArrayList<AppInfo> list = new ArrayList<>();
    DBTask dbTask;
    boolean exist;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_windows_apps);

        dbTask = new DBTask(getBaseContext());

        DisplayManager dm = (DisplayManager) getSystemService(Context.DISPLAY_SERVICE);
        targetDisplay = dm.getDisplay(Display.DEFAULT_DISPLAY);

        Context displayContext = getApplicationContext().createDisplayContext(targetDisplay);
        windowManager = (WindowManager) displayContext.getSystemService(Context.WINDOW_SERVICE);

        //Get the WindowManager of display and LayoutInflater through displayContext
        LayoutInflater li = (LayoutInflater) displayContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //Set up Layout and WindowParams for Window buttons
        view = li.inflate(R.layout.view_window, null);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR;
        params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        params.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        params.gravity = Gravity.TOP;

        //Add view in WindowManager
        windowManager.addView(view, params);

        // Adding display size for minimised window of app
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x - 20;  // Set screen width
        int height = size.y - 80; // Set screen height

        max = view.findViewById(R.id.app_max);
        min = view.findViewById(R.id.app_min);
        close = view.findViewById(R.id.app_close);
        app_name = view.findViewById(R.id.app_name);
        cardView = view.findViewById(R.id.cardView);

        app_name.setText(appInfo.getLabel());

        packageManager = getApplicationContext().getPackageManager();

        // Launching app package using intent with window button to top
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Intent i = packageManager.getLaunchIntentForPackage(appInfo.getPackageName().toString());
            i.addFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT | Intent.FLAG_ACTIVITY_MULTIPLE_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            //Rect rect = new Rect(100, 800, 900, 700);
            //ActivityOptions options = ActivityOptions.makeBasic();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                //ActivityOptions bounds = options.setLaunchDisplayId(targetDisplay.getDisplayId());
                //options = options.setLaunchBounds(rect);
                startActivity(i);
                //startService(new Intent(getApplicationContext(), WindowView.class));
            }

        }

        // Activity set to be resumed to enter picture-in-picture
        AppOpsManager manager = (AppOpsManager) getApplicationContext().getSystemService(Context.APP_OPS_SERVICE);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                cardView.setVisibility(View.GONE);
                dbTask.deletetask(appInfo.getLabel().toString());
                //((WindowManager) getApplicationContext().getSystemService(Service.WINDOW_SERVICE)).removeView(view);
                Common.showToast(getApplicationContext(), "App Closed.");
                Log.d("======CLOSE=====", "WORKING");
                appInfo = null;
            }
        });

        min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // checks if minimised app package exists in DB
                exist = dbTask.findTask(appInfo.getLabel().toString());
                if (exist == false) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    cardView.setVisibility(View.GONE);
                    dbTask.addtotask(appInfo.getLabel().toString(), appInfo.getIcon(), appInfo.getPackageName().toString());
                } else {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    cardView.setVisibility(View.GONE);
                }
                Common.showToast(getApplicationContext(), "App Minimised.");
                Log.d("======MIN=====", appInfo.getLabel().toString() + "==" + appInfo.getIcon() + "==" + appInfo.getPackageName().toString());
            }
        });

        // to minimise an app to a custom size window
        // working in this part
        max.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View view) {
                if (manager != null) {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        int mode = manager.unsafeCheckOpNoThrow(AppOpsManager.OPSTR_PICTURE_IN_PICTURE,
                                Process.myUid(),
                                getApplicationContext().getPackageName());
                        if (mode == AppOpsManager.MODE_ALLOWED) {
                            Rational rational = new Rational(width, height);
                            PictureInPictureParams.Builder pip = new PictureInPictureParams.Builder();
                            pip.setAspectRatio(rational).build();
                            enterPictureInPictureMode(pip.build());
                        }
                    }
                }
                Common.showToast(getApplicationContext(), "App Maximised.");
                Log.d("======MAX=====", "WORKING");
            }
        });


    }
}