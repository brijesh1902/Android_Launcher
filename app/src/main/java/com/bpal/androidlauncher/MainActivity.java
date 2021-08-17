package com.bpal.androidlauncher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bpal.androidlauncher.SubClass.AppsDrawer;

public class MainActivity extends AppCompatActivity {

    private ImageView appDrawer;
    private CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appDrawer = findViewById(R.id.icon_drawer);
        cardView = findViewById(R.id.dcard);

        cardView.setVisibility(View.VISIBLE);

        appDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AppsDrawer.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                cardView.setVisibility(View.INVISIBLE);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}


/*package com.bpal.androidlauncher.Services;

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
        import android.view.ViewGroup;
        import android.view.WindowManager;
        import android.view.View.OnTouchListener;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.bpal.androidlauncher.Constant.Common;
        import com.bpal.androidlauncher.Model.AppInfo;
        import com.bpal.androidlauncher.R;

public class HUD extends Service implements View.OnTouchListener {

    int LAYOUT_FLAG;
    AppInfo appInfo = Common.current_app;
    View view;
    TextView app_name;
    ImageView max, min, close;

    @Override
    public IBinder
    onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //mView = new HUDView(this);

        Log.d("======HUD=====", "WORKING");

        /*LayoutInflater mInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //view = mInflater.inflate(R.layout.view_window, null, false);

        app_name = view.findViewById(R.id.aname);
        max = view.findViewById(R.id.app_max);
        min = view.findViewById(R.id.app_min);
        close = view.findViewById(R.id.app_close);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }

        app_name.setText(appInfo.getLabel().toString());

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                0,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.RIGHT | Gravity.TOP;
        params.setTitle(appInfo.getLabel());
        WindowManager wm = (WindowManager) this.getSystemService(WINDOW_SERVICE);
        wm.addView(view, params);*/
   /*     WindowManager windowManager  = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        layoutParams.gravity = Gravity.TOP | Gravity.LEFT;
        layoutParams.flags = 0;//WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        View window = LayoutInflater.from(this).inflate(R.layout.view_window, null, false);
        windowManager.addView(window, layoutParams);

        PackageManager packageManager =  getApplicationContext().getPackageManager();
        Intent mapIntent = packageManager.getLaunchIntentForPackage(appInfo.getPackageName().toString());
        mapIntent.addCategory("android.intent.category.LAUNCHER");
        startActivity(mapIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(close != null)
        {
            ((WindowManager) this.getSystemService(WINDOW_SERVICE)).removeView(close);
            close = null;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Toast.makeText(this,"Overlay button event", Toast.LENGTH_SHORT).show();
        return false;
    }


}
*/