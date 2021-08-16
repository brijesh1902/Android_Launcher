package com.bpal.androidlauncher.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bpal.androidlauncher.Constant.Common;
import com.bpal.androidlauncher.Model.AppInfo;
import com.bpal.androidlauncher.Constant.ItemClickListener;
import com.bpal.androidlauncher.R;
import com.bpal.androidlauncher.SubClass.WindowsAppsActivity;

import java.util.List;

public class AppsDrawerAdapter extends RecyclerView.Adapter<AppsDrawerAdapter.ViewHolder> {

    private Context context;
    private List<AppInfo> appsList;

    public AppsDrawerAdapter(Context c, List<AppInfo> list) {
        context = c;
        appsList = list;
    }


    @Override
    public AppsDrawerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AppsDrawerAdapter.ViewHolder holder, int position) {

        AppInfo data = appsList.get(position);

        String appLabel = data.getLabel().toString();
        String appPackage = data.getPackageName().toString();
        Drawable appIcon = data.getIcon();

        holder.textView.setText(appLabel);
        holder.img.setImageDrawable(appIcon);

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                Common.current_app = data;
                Intent intent = new Intent(context, WindowsAppsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT|
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK|
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return appsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textView;
        ImageView img;
        ItemClickListener itemClickListener;

        public ViewHolder(View itemView) {
            super(itemView);

            textView =  itemView.findViewById(R.id.app_name);
            img = itemView.findViewById(R.id.app_icon);
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
