package com.bpal.androidlauncher;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.icu.text.Collator;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bpal.androidlauncher.Adapters.AppsDrawerAdapter;
import com.bpal.androidlauncher.Adapters.TaskbarAdapter;
import com.bpal.androidlauncher.Constant.Common;
import com.bpal.androidlauncher.Database.DBTask;
import com.bpal.androidlauncher.Model.AppInfo;
import com.bpal.androidlauncher.SubClass.AppsDrawer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ImageView appDrawer;
    private CardView cardView, cardView1;
    RecyclerView getRecyclerView;
    ArrayList<AppInfo> list = new ArrayList<>();
    TaskbarAdapter taskbarAdapter;
    DBTask data;
    public final static int REQUEST_CODE = 101;
    RecyclerView recyclerView;
    AppsDrawerAdapter adapter;
    List<AppInfo> appsList;
    RecyclerView.LayoutManager layoutManager;
    EditText searchbar;
    byte[] icon;
    ConstraintLayout menu_layout, parent;

    @Override
    protected void onRestart() {
        super.onRestart();
        cardView.setVisibility(View.VISIBLE);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
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
        if (!Settings.canDrawOverlays(getBaseContext())) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
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
        getRecyclerView = findViewById(R.id.rv_taskbar);
        menu_layout = findViewById(R.id.menu_layout);
        parent = findViewById(R.id.parent);

        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menu_layout.setVisibility(View.GONE);
            }
        });
        getRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menu_layout.setVisibility(View.GONE);
            }
        });
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menu_layout.setVisibility(View.GONE);
            }
        });
        appDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menu_layout.setVisibility(View.GONE);
            }
        });

        cardView1.setCardBackgroundColor(Color.TRANSPARENT);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);
        getRecyclerView.setLayoutManager(layoutManager1);

        cardView.setVisibility(View.VISIBLE);
        menu_layout.setVisibility(View.GONE);

        appDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu_layout.setVisibility(View.VISIBLE);
            }
        });

      // try {
           list = data.get_tbApps();
           Common.tb_list = list;
        taskbarAdapter = new TaskbarAdapter(getApplicationContext(), list);
        taskbarAdapter.notifyDataSetChanged();
        taskbarAdapter.notifyItemInserted(taskbarAdapter.getItemCount());
        taskbarAdapter.notifyItemRemoved(taskbarAdapter.getItemCount());
        taskbarAdapter.notifyItemRangeChanged(taskbarAdapter.getItemCount(), list.size());
        getRecyclerView.setAdapter(taskbarAdapter);
      // } catch (Exception e){}

        recyclerView = findViewById(R.id.appDrawer_recylerView);
        searchbar = findViewById(R.id.search);

        searchbar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchValue(s.toString());
            }
        });

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        setUpApps();

        adapter = new AppsDrawerAdapter(getApplicationContext(), appsList);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        Collections.sort(appsList, ALPHA_COMPARATOR);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        menu_layout.setVisibility(View.GONE);
    }

    private void searchValue(String newText) {
        ArrayList<AppInfo> mylist = new ArrayList<>();
        if (!appsList.isEmpty()) {
            for (AppInfo info : appsList) {
                if (info.getLabel().toString().toLowerCase().contains(newText.toLowerCase())) {
                    mylist.add(info);
                }
            }
            adapter = new AppsDrawerAdapter(getApplicationContext(), mylist);
            recyclerView.setAdapter(adapter);
        } else {
            Toast.makeText(getApplicationContext(), "App not found!!", Toast.LENGTH_LONG).show();
        }
    }

    private static final Comparator<AppInfo> ALPHA_COMPARATOR = new Comparator<AppInfo>() {
        @SuppressLint("NewApi")
        private final Collator sCollator = Collator.getInstance();
        @Override
        public int compare(AppInfo object1, AppInfo object2) {
            return sCollator.compare(object1.getLabel(), object2.getLabel());
        }
    };

    private void setUpApps(){
        PackageManager packageManager =  getApplicationContext().getPackageManager();
        appsList = new ArrayList<>();

        Intent i = new Intent(Intent.ACTION_MAIN, null);
        i.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> allApps = packageManager.queryIntentActivities(i, 0);
        for (ResolveInfo ri : allApps) {
            AppInfo app = new AppInfo();
            app.setLabel(ri.loadLabel(packageManager));
            app.setPackageName(ri.activityInfo.packageName);
            app.setIcon(ri.activityInfo.loadIcon(packageManager).toString());
            System.out.println(ri.activityInfo.loadIcon(packageManager));
            appsList.add(app);
        }
    }
}