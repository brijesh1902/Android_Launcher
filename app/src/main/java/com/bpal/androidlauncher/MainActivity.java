package com.bpal.androidlauncher;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;

import com.bpal.androidlauncher.Adapters.AppsDrawerAdapter;
import com.bpal.androidlauncher.Adapters.TaskbarAdapter;
import com.bpal.androidlauncher.Constant.Common;
import com.bpal.androidlauncher.Database.DBTask;
import com.bpal.androidlauncher.Model.AppInfo;
import com.bpal.androidlauncher.SubClass.AppsDrawer;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ImageView appDrawer;
    private CardView cardView, cardView1;
    RecyclerView recyclerView;
    ArrayList<AppInfo> list = new ArrayList<>();
    TaskbarAdapter adapter;
    DBTask data;
    public final static int REQUEST_CODE = 101;

    @Override
    protected void onRestart() {
        super.onRestart();
        cardView.setVisibility(View.VISIBLE);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        /** check if received result code
         is equal our requested code for draw permission  */
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
                startActivity(intent);
                finish();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void checkDrawOverlayPermission() {
        /** check if we already  have permission to draw over other apps */
        if (!Settings.canDrawOverlays(getBaseContext())) {
            /** if not construct intent to request permission */
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            /** request permission via start activity for result */
            startActivityForResult(intent, REQUEST_CODE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkDrawOverlayPermission();

        data = new DBTask(getApplicationContext());

        appDrawer = findViewById(R.id.icon_drawer);
        cardView = findViewById(R.id.dcard);
        cardView1 = findViewById(R.id.tbcard);
        recyclerView = findViewById(R.id.rv_taskbar);

        cardView1.setCardBackgroundColor(Color.TRANSPARENT);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        cardView.setVisibility(View.VISIBLE);

        appDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AppsDrawer.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                cardView.setVisibility(View.INVISIBLE);
                cardView1.setVisibility(View.INVISIBLE);
            }
        });

      // try {
           list = data.get_tbApps();
           Common.tb_list = list;
           adapter = new TaskbarAdapter(getApplicationContext(), list);
           adapter.notifyDataSetChanged();
           adapter.notifyItemInserted(adapter.getItemCount());
           adapter.notifyItemRemoved(adapter.getItemCount());
           adapter.notifyItemRangeChanged(adapter.getItemCount(), list.size());
           recyclerView.setAdapter(adapter);
      // } catch (Exception e){}

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}