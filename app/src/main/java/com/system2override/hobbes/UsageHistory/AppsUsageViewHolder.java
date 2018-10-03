package com.system2override.hobbes.UsageHistory;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.system2override.hobbes.R;

public class AppsUsageViewHolder extends RecyclerView.ViewHolder {
    private Context context;
    public View wholeView;
    public ImageView appIcon;
    public View historyBar;
    public TextView appTimeTextView;
    public TextView appHistoryBarLabel;


    public AppsUsageViewHolder(View view, Context context) {
        super(view);
        this.context = context;
        this.wholeView = view;
        this.appIcon = (ImageView) this.wholeView.findViewById(R.id.usageHistoryAppIcon);
        this.historyBar = this.wholeView.findViewById(R.id.singleAppUsageHistoryBar);
        this.appTimeTextView = (TextView) this.wholeView.findViewById(R.id.appUsageHistoryBarTime);
        this.appHistoryBarLabel = (TextView) this.wholeView.findViewById(R.id.appUsageHistoryBarLabel);
    }
}
