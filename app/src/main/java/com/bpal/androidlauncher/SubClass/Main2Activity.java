package com.bpal.androidlauncher.SubClass;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.display.DisplayManager;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bpal.androidlauncher.R;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {

    private static final String SDC = "SDC_SampleApplication";
    private Context mContext;

    private String name;
    private EditText editText;

    private TextView text1;
    private TextView text2;
    private TextView text3;
    private TextView text4;
    private Button enterBtn;


    private boolean ctrlKeyPressed;
    private Button startEmail;
    private Button startSubActivity;
    private Switch sw;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

        Toast.makeText(mContext, "Created", Toast.LENGTH_SHORT).show();

        updateWindowSize();

        if(isDeXEnabled()){
            if(isAppCurrentlyRunningInDeX()){
                text3.setText("Samsung DeX Enabled \nNow, Your app is running in Samsung DeX");

            } else {
                text3.setText("Samsung DeX Enabled \nNow, Your app is running in Phone");
            }
        } else {
            text3.setText("Samsung DeX NOT Enabled");
        }

        text4.setOnGenericMotionListener(new View.OnGenericMotionListener() {
            @Override
            public boolean onGenericMotion(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                if((action == MotionEvent.ACTION_SCROLL) && ctrlKeyPressed) {
                    float vScroll = motionEvent.getAxisValue(MotionEvent.AXIS_VSCROLL);
                    if(vScroll < 0){
                        //wheel down
                        text4.setTextSize(TypedValue.COMPLEX_UNIT_PX, text4.getTextSize()-1);
                    } else{
                        //wheel up
                        text4.setTextSize(TypedValue.COMPLEX_UNIT_PX, text4.getTextSize()+1);
                    }

                }
                return false;
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putString("name", name);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        Display targetDisplay;
        DisplayManager dm = (DisplayManager)getSystemService(Context.DISPLAY_SERVICE);

        if(sw.isChecked())
        {
            Display[] displays = dm.getDisplays();
            targetDisplay = displays[0];
        }else {
            targetDisplay = dm.getDisplay(Display.DEFAULT_DISPLAY);
        }

        Intent intent = getPackageManager().getLaunchIntentForPackage("com.google.android.gm");

        if(isDeXEnabled()){
            ActivityOptions options = ActivityOptions.makeBasic();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                options.setLaunchDisplayId(targetDisplay.getDisplayId());
                startActivity(intent, options.toBundle());
            }

        } else {
            startActivity(intent);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_CTRL_LEFT:
            case KeyEvent.KEYCODE_CTRL_RIGHT:
                ctrlKeyPressed = true;
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_CTRL_LEFT:
            case KeyEvent.KEYCODE_CTRL_RIGHT:
                ctrlKeyPressed = false;
                break;
        }
        return super.onKeyUp(keyCode, event);
    }

    private boolean isAppCurrentlyRunningInDeX() {
        Configuration config = mContext.getResources().getConfiguration();
        try {
            Class configClass = config.getClass();
            if (configClass.getField("SEM_DESKTOP_MODE_ENABLED").getInt(configClass)
                    == configClass.getField("semDesktopModeEnabled").getInt(config)) {
                Toast.makeText(mContext, "isAppCurrentlyRunningInDeX: true", Toast.LENGTH_LONG).show();
                return true;
            }
        } catch (Exception ignored) {
        }
        return false;
    }

    private boolean isDeXEnabled() {
         Object desktopModeManager = mContext.getApplicationContext().getSystemService(WINDOW_SERVICE);
        if (desktopModeManager != null) {
            try {
                Method getDesktopModeStateMethod = desktopModeManager.getClass().getDeclaredMethod("getDesktopModeState");
                Object desktopModeState = getDesktopModeStateMethod.invoke(desktopModeManager);
                Class desktopModeStateClass = desktopModeState.getClass();
                Method getEnabledMethod = desktopModeStateClass.getDeclaredMethod("getEnabled");
                int enabled = (int) getEnabledMethod.invoke(desktopModeState);
                boolean isEnabled = enabled == desktopModeStateClass.getDeclaredField("ENABLED").getInt(desktopModeStateClass);

                Method getDisplayTypeMethod = desktopModeStateClass.getDeclaredMethod("getDisplayType");
                int displayType = (int) getDisplayTypeMethod.invoke(desktopModeState);
                return isEnabled && displayType == desktopModeStateClass.getDeclaredField("DISPLAY_TYPE_DUAL").getInt(desktopModeStateClass);

            } catch (NoSuchFieldException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                // Device does not support DeX 3.0
                return false;
            }
        }
        return false;
    }

    private void updateWindowSize() {
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);

        Log.d("Window size", "(" + metrics.widthPixels + ", " + metrics.heightPixels + ")");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d(SDC,"call onConfigurationChanged");
        updateWindowSize();
    }
}
