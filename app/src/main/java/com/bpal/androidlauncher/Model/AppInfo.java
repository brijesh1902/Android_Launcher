package com.bpal.androidlauncher.Model;

import android.graphics.drawable.Drawable;

public class AppInfo {

    CharSequence label;
    CharSequence packageName;
    String icon;

    public AppInfo(){}

    public AppInfo(CharSequence label, String icon, CharSequence packageName) {
        this.label = label;
        this.icon =  icon;
        this.packageName = packageName;
    }

    public CharSequence getLabel() {
        return label;
    }

    public void setLabel(CharSequence label) {
        this.label = label;
    }

    public CharSequence getPackageName() {
        return packageName;
    }

    public void setPackageName(CharSequence packageName) {
        this.packageName = packageName;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }
}
