package com.bpal.androidlauncher.Adapters;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bpal.androidlauncher.Constant.Common;
import com.bpal.androidlauncher.Constant.ItemClickListener;
import com.bpal.androidlauncher.Model.AppInfo;
import com.bpal.androidlauncher.R;
import com.bpal.androidlauncher.Services.WindowView;
import com.bpal.androidlauncher.SubClass.WindowsAppsActivity;
import com.bumptech.glide.Glide;

import java.util.List;

public class TaskbarAdapter extends RecyclerView.Adapter<TaskbarAdapter.ViewHolder> {

    private Context context;
    private List<AppInfo> appsList;
    PackageManager packageManager;

    public TaskbarAdapter(Context c, List<AppInfo> list) {
        context = c;
        appsList = list;
    }


    @Override
    public TaskbarAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.taskbar_apps, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TaskbarAdapter.ViewHolder holder, int position) {

        packageManager = context.getPackageManager();
        AppInfo data = appsList.get(position);

        if (data != null) {
            String appLabel = data.getLabel().toString();
            String appPackage = data.getPackageName().toString();
            Drawable appIcon = Drawable.createFromPath(data.getIcon());

            holder.name.setText(appLabel);
            //Glide.with(context).load(appIcon).into(holder.img);
            try {
                holder.img.setImageDrawable(packageManager.getApplicationIcon(appPackage));
            } catch (PackageManager.NameNotFoundException e) {
                holder.img.setImageDrawable(packageManager.getDefaultActivityIcon());
            }

            Log.d("======MIN=====", data.getIcon());

            holder.setItemClickListener(new ItemClickListener() {
                @Override
                public void onItemClickListener(View v, int position) {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        Intent i = packageManager.getLaunchIntentForPackage(appPackage);
                        i.addFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT | Intent.FLAG_ACTIVITY_MULTIPLE_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        Rect rect = new Rect(100, 800, 900, 700);
                        ActivityOptions options = ActivityOptions.makeBasic();
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            options = options.setLaunchBounds(rect);
                            context.startActivity(i, options.toBundle());
                            Common.current_app = data;
                            Intent intent1 = new Intent(context, WindowView.class);
                            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startService(intent1);
                        }
                    }
                   /* Common.current_app = data;
                    Intent intent = new Intent(context, WindowsAppsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT|
                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK|
                            Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    Common.showToast(context, "App Maximised.");*/
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return appsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name;
        ImageView img;
        ItemClickListener itemClickListener;

        public ViewHolder(View itemView) {
            super(itemView);

            name =  itemView.findViewById(R.id.tb_name);
            img = itemView.findViewById(R.id.tb_icon);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            this.itemClickListener.onItemClickListener(v, getLayoutPosition());
        }

        public void setItemClickListener(ItemClickListener ic) {
            this.itemClickListener = ic;
        }

    }
}

