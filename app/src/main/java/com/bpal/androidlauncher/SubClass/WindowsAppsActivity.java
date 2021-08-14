package com.bpal.androidlauncher.SubClass;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bpal.androidlauncher.Constant.Common;
import com.bpal.androidlauncher.Model.AppInfo;
import com.bpal.androidlauncher.R;
import com.bpal.androidlauncher.Services.HUD;

public class WindowsAppsActivity extends AppCompatActivity {

    TextView app_name;
    ImageView max, min, close;
    FrameLayout frameLayout;
    AppInfo appInfo = Common.current_app;
    public final static int REQUEST_CODE = -1010101;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1234) {
            startService(new Intent(this, HUD.class));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_windows_apps);

            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 1234);
                Log.d("======ACT=====", "WORKING");

            } else {
            startService(new Intent(this, HUD.class));
            Log.d("======ACT1=====", "WORKING");
        }




/*        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                appInfo = null;
            }
        });

        min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        max.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/

    }

}