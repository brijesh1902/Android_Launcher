package com.bpal.androidlauncher.SubClass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.icu.text.Collator;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.bpal.androidlauncher.Adapters.AppsDrawerAdapter;
import com.bpal.androidlauncher.MainActivity;
import com.bpal.androidlauncher.Model.AppInfo;
import com.bpal.androidlauncher.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AppsDrawer extends AppCompatActivity {

    RecyclerView recyclerView;
    AppsDrawerAdapter adapter;
    List<AppInfo> appsList;
    RecyclerView.LayoutManager layoutManager;
    EditText searchbar;
    byte[] icon;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apps_drawer);

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