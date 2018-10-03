package com.system2override.hobbes.BannedAppManagement;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.system2override.hobbes.MyApplication;
import com.system2override.hobbes.OttoMessages.BannedAppAdded;
import com.system2override.hobbes.OttoMessages.BannedAppRemoved;
import com.system2override.hobbes.R;

import java.util.List;

public class SelectedAppIconAdapter extends RecyclerView.Adapter<SelectedAppIconViewHolder> {
    private static final String TAG = "SelectedAppIconAdapter";
    private List<ApplicationInfo> applications;
    private Context context;
    private PackageManager pm;

    private Bus bus;

    public SelectedAppIconAdapter(Context context, List<ApplicationInfo> list, Bus bus) {
        this.applications = list;
        this.context = context;
        this.pm = this.context.getPackageManager();
        this.bus = bus;

        bus.register(this);
    }

    @Override
    public SelectedAppIconViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.selected_app, parent, false);
        return new SelectedAppIconViewHolder(v, this.context);
    }

    private void setImageToBlank(ImageView view) {
            view
            .setImageDrawable(ResourcesCompat.getDrawable(this.context.getResources(), R.drawable.empty_app_icon, null));
    }

    @Override
    public void onBindViewHolder(SelectedAppIconViewHolder holder, int position) {
        ApplicationInfo appInfo = this.applications.get(position);

        if (appInfo == null) {
            setImageToBlank(holder.iconView);
        } else {

            try {
                holder.iconView.setImageDrawable(pm.getApplicationIcon(appInfo.packageName));
            } catch (PackageManager.NameNotFoundException e) {
                setImageToBlank(holder.iconView);
            }
        }
    }

    @Override
    public int getItemCount() {
        return this.applications.size();
    }

    @Subscribe
    public void onBannedAppUnchecked(BannedAppRemoved event) {
        Log.d(TAG, "onBannedAppUnchecked: event " + event.appInfo.packageName);
        int appIndex = -1;
        for (int i = 0; i < this.applications.size(); i++) {
            ApplicationInfo curAppInfo = this.applications.get(i);
            if (curAppInfo == null) {
                continue;
            }
            if (event.appInfo.packageName.equals(curAppInfo.packageName)){
                appIndex = i;

                this.applications.set(i, null);
                notifyDataSetChanged();
            }
        }
        if (appIndex == -1) {
            Log.d(TAG, "onBannedAppUnchecked: something terrible happened");
        }
        Log.d(TAG, "onBannedAppUnchecked: appIndex " + Integer.toString(appIndex));
    }

    @Subscribe
    public void onBannedAppChecked(BannedAppAdded event) {
        Log.d(TAG, "onBannedAppChecked: event " + event.appInfo.packageName);
        int firstNull = this.applications.indexOf(null);
        this.applications.set(firstNull, event.appInfo);
        notifyDataSetChanged();
    }
}
