package com.system2override.hobbes.UsageHistory;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.system2override.hobbes.R;
import com.system2override.hobbes.Utilities.RandomUtilities;

import java.util.List;
import java.util.Map;

// each bar will represent 6 hours of time. if the amount of time on an app was longer than 6 hours,
// no overflow or anything. it'll look the same as if the user had spent exactly 6 hours.
// each bar's width is 170dp
public class AppsUsageAdapter extends RecyclerView.Adapter<AppsUsageViewHolder> {
    private static final String TAG = "AppsUsageAdapter";
    private Map<String, ApplicationInfo> applications;
    private Context context;
    private PackageManager pm;
    private  List<Map.Entry<Long, String>> appsTimeList;
    private int barWidth;
    private long barTime = 1000 * 60 * 60 * 6;
    private final float scale;

    public AppsUsageAdapter(Context context, List<Map.Entry<Long, String>> appsTimeList, Map<String, ApplicationInfo> applications) {
        this.applications = applications;
        this.context = context;
        this.appsTimeList = appsTimeList;

        this.pm = this.context.getPackageManager();
        // this doesn't work. i get the result '1'
        // this.barWidth = R.dimen.usage_history_bar_width - R.dimen.usage_history_bar_time_width;
        this.barWidth = 170;
        this.scale = this.context.getResources().getDisplayMetrics().density;

        Log.d(TAG, "AppsUsageAdapter: barwidth " + Integer.toString(barWidth));
    }


    @Override
    public void onBindViewHolder(AppsUsageViewHolder holder, int position) {
        Map.Entry<Long, String> appTimeEntry = this.appsTimeList.get(position);
        String packageName = (String) appTimeEntry.getValue();
        // see comment in UsageStatsHelper for why this is -1. it's because the default behavior of
        // SortedTreeMap is to go from smallest to biggest, so without this, the first app would have
        // had 0 ms of use
        long time = ((Long) appTimeEntry.getKey()).longValue() * -1;
        ApplicationInfo appInfo = this.applications.get(packageName);

        if (appInfo == null) {
            setImageToBlank(holder.appIcon);
            holder.appHistoryBarLabel.setText(packageName);
        } else {

            try {
                holder.appIcon.setImageDrawable(pm.getApplicationIcon(appInfo.packageName));
            } catch (PackageManager.NameNotFoundException e) {
                setImageToBlank(holder.appIcon);
            }
            holder.appHistoryBarLabel.setText(this.pm.getApplicationLabel(appInfo));
        }

        holder.appTimeTextView.setText(RandomUtilities.formatMillisecondsToHHMM(time));
        int appBarLength = (int) (this.barWidth * ((double)time / (double) this.barTime));
        Log.d(TAG, "onBindViewHolder: appbarlength of app " + packageName + " is " + Integer.toString(appBarLength));
        if (appBarLength > this.barWidth) {
            appBarLength = this.barWidth;
        }

 //       /*
        ViewGroup.LayoutParams params = holder.historyBar.getLayoutParams();
        params.width = (int) (appBarLength * scale + 0.5f);
        holder.historyBar.setLayoutParams(params);
//        */

    }

    @Override
    public AppsUsageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.usage_history_bar, parent, false);
        return new AppsUsageViewHolder(v, this.context);
    }

    @Override
    public int getItemCount() {
        return this.appsTimeList.size();
    }

    private void setImageToBlank(ImageView view) {
        view
                .setImageDrawable(ResourcesCompat.getDrawable(this.context.getResources(), R.drawable.empty_app_icon, null));
    }
}
